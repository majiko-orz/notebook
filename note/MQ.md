## RabbitMQ

### 基本概念

**安装**

https://blog.csdn.net/m0_67392182/article/details/126040124

**什么是MQ**

MQ(Message Queue)即消息队列，本质是个FIFO队列，只不过队列中存放的是message，还是一种跨进程的通信机制，用于上下游传递消息。在互联网架构中，MQ是一种非常常见的上下游逻辑解耦+物理解耦的消息通信服务。使用了MQ之后，消息发送上游只需要依赖MQ，不用依赖其他服务。

**三大功能**

+ 流量消峰
+ 应用解耦
+ 异步处理

**MQ的分类**

+ ActiveMQ
  + 优点：单机吞吐量万级，时效性ms级，可用性高，基于主从架构实现高可用性，消息可靠性较低的概率丢失数据
  + 缺点：官方社区现在对5.x维护越来越少，高吞吐量场景较少使用
+ Kafka
  + 优点：性能卓越，单机写入TPS约在百万条每秒，最大的优点就是吞吐量高。时效性ms级，可用性非常高，kafka是分布式的
  + 缺点：Kafka单机超过64个队列/分区，load会发生明显的CPU飙高现象，队列越多，load越高，发送消息响应时间变长，使用短轮询方式，实时性取决于轮询间隔时间，消费失败不支持重试
+ RocketMQ
  + 优点：单机吞吐量十万级，可用性非常高，分布式架构，消息可以做到0丢失，支持10亿级别的消息堆积，不会因为堆积导致性能下降，源码是Java写的
  + 缺点：支持的客户端语言不多，目前是Java和C++，没有在MQ核心中去实现JMS接口
+ RabbitMQ
  + 优点：由于erlang语言的高并发特性，性能较好，吞吐量到万级，MQ功能比较完备，健壮，稳定，易用，跨平台，支持多种语言
  + 缺点：商业版需要收费

**四大核心概念**

+ 生产者
+ 交换机：一方面接收来自生产者的消息，另一方面将消息推送到队列中
+ 消费者
+ 队列

**核心部分**

![](/img/rabbitMQ_5.png)

**工作原理**

![](/img/rabbitMQ_6.png)

+ Broker：接收和发送消息的应用，RabbitMQ Server就是Message Broker
+ Virtual host：出于多租户和安全因素设计的，把AMQP的基本组件划分到一个虚拟的分组中，类似网络中的namespace概念。当多个不同的用户使用同一个RabbitMQ Server提供的服务时，可以划分出多个vhost，每个用户在自己的vhost创建exchange/queue等
+ Connection：publisher/customer和broker之间的TCP连接
+ Channel：如果每一次访问RabbitMQ都建立一个Connection，在消息量大的时候建立TCP Connection的开销将是巨大的，效率也较低。Channel是在connection内部建立的逻辑连接，如果应用程序支持多线程，通常每个thread创建单独的channel进行通讯，AMQP method包含了channel id帮助客户端和message broker识别channel，所以channel之间是完全隔离的。Channel作为轻量级的Connection极大减少了操作系统建立TCP connection的开销
+ Exchange：message到达broker的第一站，根据分发规则，匹配查询表中的routing key分发消息到queue中去。常用的类型有：direct（point-to-point），topic（publish-subscribe），fanout（multicast）
+ Queue：消息最终被送到这里等待consumer取走
+ Binding：exchange和queue之间的虚拟连接，binding中可以包含routing key，Binding信息被保存到exchange中的查询表中，用于message的分发依据

### Hello World

**生产者**

```java
@Service
public class Producer {
    public static final String QUEUE_NAME = "hello";

    public void producer() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "hello world";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

        System.out.println("消息发送完毕");
    }
}
```

**消费者**

```java
@Service
public class Consumer {
    public static final String QUEUE_NAME = "hello";

    public void consumer() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> System.out.println(new String(message.getBody()));

        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag -> System.out.println("消息消费被中断");

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
```

### Work Queues

工作队列的主要思想是避免立即执行资源密集型任务，而不得不等待它完成。相反我们安排任务在之后执行。我们把任务封装为消息并将其发送到队列。在后台运行的工作进程将弹出任务并最终执行作业。当有多个工作线程时，这些工作线程将一起处理这些任务

#### 轮询分发消息

多个工作线程会采用轮询的方式接收消息

#### 消息应答

为了保证消息在发送的过程中不丢失，rabbitmq引入消息应答机制，消息应答就是：消费者在接收到消息并且处理该消息之后，告诉rabbitmq它已经处理好了，rabbitmq可以把该消息删除了

**自动应答**

消息发送后立即认为已经传送成功，这种模式需要在高吞吐量和数据传输安全性方面做权衡。这种模式消费者那边可以传递过载的消息，没有对传递的消息数量进行限制，当然这样有可能使得消费者这边由于接收太多还来不及处理的消息，导致这些消息的积压，最终使得内存耗尽，最终这些消费者线程被操作系统杀死，所以这种模式仅适用于消费者可以高效并以某种速率能够处理这些消息的情况下使用

**消息应答的方法**

+ Channel.basicAck：用于肯定确认，rabbitmq已经知道该消息并且成功的处理消息，可以将其丢弃了
+ Channel.basicNack：用于否定确认
+ Channel.basicReject：用于否定确认，与basicNack相比少一个参数，不处理该消息了直接拒绝，可以将其丢弃了

**Multiple的解释**

手动应答的好处是可以批量应答并且减少网络拥堵

channel.basicAck(deliveryTag, multiple)第二个参数，true代表批量应答channel上未应答的消息

**消息自动重新入队**

如果消费者由于某种原因失去连接，导致消息未发送ACK确认，rabbitmq将了解到消息未完全处理，并将对其重新排队。如果此时其他消费者可以处理，他将很快将其重新分发给另一个消费者。这样，即使某个消费者偶尔死亡，也可以确保不会丢失任何消息

**消息手动应答**

默认消息采用的是自动应答，所以我们要想实现消息消费过程中不丢失，需要把自动应答改为手动应答

```java
@Service
public class Worker01 {

    public static final String QUEUE_NAME = "hello";

    public void worker01() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //接收消息
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            System.out.println("接收到的消息：" + new String(delivery.getBody()));
            //1.消息标记tag 2.false代表只应答接收到的那个传递的消息，true为应答所有消息包括传递过来的消息
            channel.basicAck(delivery.getEnvelope().getDeliveryTag, false);
        }

        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag -> System.out.println("消费者取消消息接口回调逻辑");
        System.out.println("C1等待接收消息...");
        
        //手动应答
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
```

#### RabbitMQ持久化

**队列持久化**

```java
@Service
public class Producer {
    public static final String QUEUE_NAME = "hello";

    public void producer() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //消息队列持久化
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        
        String message = "hello world";
        //消息持久化
        channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAN, message.getBytes());
        System.out.println("消息发送完毕");
    }
}
```

注意：如果之前声明的队列不是持久化的，需要将原先队列先删除，或者重新创建一个持久化队列，不然会报错

**消息持久化**

channel.basicPublish第三个参数改为 MessageProperties.PERSISTENT_TEXT_PLAN

将消息标记为持久化并不能完全保证不会丢失消息。尽管它告诉rabbitmq将消息保存到磁盘，但是这里依然存在当消息刚准备存储在磁盘的时候，但是还没有存储完，消息还在缓存的一个间隔点。此时并没有真正写入磁盘。持久性保证并不强，但是对于我们的简单队列而言，这已经绰绰有余了

**不公平分发**

channel.basicQos(1)

```java
@Service
public class Worker01 {

    public static final String QUEUE_NAME = "hello";

    public void worker01() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //接收消息
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            System.out.println("接收到的消息：" + new String(delivery.getBody()));
            //1.消息标记tag 2.false代表只应答接收到的那个传递的消息，true为应答所有消息包括传递过来的消息
            channel.basicAck(delivery.getEnvelope().getDeliveryTag, false);
        }

        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag -> System.out.println("消费者取消消息接口回调逻辑");
        System.out.println("C1等待接收消息...");
        
        //设置不公平分发
        channel.basicQos(1);
        //手动应答
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
```

**预取值**

本身消息的发送就是异步发送的，所以在任何时候，channel上肯定不止只有一个消息另外来自消费者的手动确认本质上也是异步的。因此这里就存在一个未确认的消息缓冲区，因此希望开发人员能限制此缓冲区的大小，以避免缓冲区里面无限制的未确认消息问题。这个时候就可以通过使用basicQos分发设置预取计数值来完成，该值定义通道上允许的未确认消息的最大数量

```java
@Service
public class Worker01 {

    public static final String QUEUE_NAME = "hello";

    public void worker01() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //接收消息
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            System.out.println("接收到的消息：" + new String(delivery.getBody()));
            //1.消息标记tag 2.false代表只应答接收到的那个传递的消息，true为应答所有消息包括传递过来的消息
            channel.basicAck(delivery.getEnvelope().getDeliveryTag, false);
        }

        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag -> System.out.println("消费者取消消息接口回调逻辑");
        System.out.println("C1等待接收消息...");
        
        //设置不公平分发
        //channel.basicQos(1);
        //预取值是5
        int prefetchCount = 5;
        channel.basicQos(prefetchCount);
        //手动应答
        boolean autoAck = false;
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
```

### 发布确认

#### 发布确认原理

生产者将信道设置为confirm模式，一旦信道进入confirm模式，所有在该信道上面发布的消息都将会被指派一个唯一的ID（从1开始），一旦消息被投递到所有匹配的队列之后，broker就会发送一个确认给生产者（包含消息的唯一ID），这就使得生产者知道消息已经正确到达目的队列，如果消息和队列是可持久化的，那么确认消息会在将消息写入磁盘后发出，broker回传给生产者的确认消息中delivery-tag域包含了确认消息的序列号，此外broker也可以设置basicAck的multiple域，表示这个序列号之前所有的消息都已经得到了处理

confirm最大的好处在于他是异步的，一旦发布一条消息，生产者应用程序就可以在等信道返回的同时继续发送下一条消息，当消息最终得到确认之后，生产者应用便可以通过回调方法来处理该确认消息，如果rabbitmq因为自身内部错误导致消息丢失，就会发送一条nack消息，生产者应用程序同样可以在回调方法中处理该nack消息

#### 发布确认的策略

**开启发布确认的方法**

发布确认默认是没有开启的，如果要开启需要调用方法confirmSelect，每当你要想使用发布确认，都需要在channel上调用该方法

**单个确认发布**

这是一种简单的确认方式，他是一种同步确认发布的方式，也就是发布一个消息之后只有他被确认发布，后续的消息才能继续发布，waitForConfirmsOrDie(long)这个方法只有在消息被确认的时候才返回，如果在指定时间范围内这个消息没有被确认那么他将抛出异常

这种确认方式有一个最大的缺点就是：发布速度特别慢

```java
public class ConfirmMessage {

    public static final int MESSAGE_COUNT = 1000;

    public void publishMessageIndividually() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启确认发布
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            //单个消息就马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功");
            }
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时" + (end - begin) + "ms");
    }
}
```

**批量发布确认**

上面那种方式非常慢，与单个等待确认消息相比，先发布一批消息然后一起确认可以极大地提高吞吐量，当然这种方式的缺点就是：当发生故障导致发布出现问题时，不知道是哪个消息出现问题了，我们必须将整个批处理保存在内存中，以记录重要的信息后重新发布消息。当然这种方案仍然是同步的，也一样阻塞消息的发布

```java
    public void publishMessageBatch() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启确认发布
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量确认消息大小
        int batchSize = 100;

        //批量发送消息，批量发布确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());

            //判断达到100条消息的时候，批量确认一次
            if (i % batchSize == 0) {
                //发布确认
                channel.waitForConfirms();
            }
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时" + (end - begin) + "ms");
    }
```

**异步发布确认**

异步确认虽然编程逻辑比上两个要复杂，但是性价比最高，无论是可靠性还是效率都没得说，它是利用回调函数来达到消息可靠性传递的，这个中间件也是通过函数回调来保证是否投递成功

```java
    public void publishMessageAsync() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启确认发布
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //消息确认成功 回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            System.out.println("确认的消息：" + deliveryTag);
        };
        //消息确认失败 回调函数
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            System.out.println("未确认的消息：" + deliveryTag);
        };
        //准备消息的监听器，监听哪些消息成功了，哪些消息失败了，异步通知
        channel.addConfirmListener(ackCallback, nackCallback);

        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个异步确认消息，耗时" + (end - begin) + "ms");
    }
```

**如何处理异步未确认消息**

最好的解决方案就是把未确认的消息放到一个基于内存的能被发布线程访问的队列，比如说用ConcurrentLinkedQueue这个队列在confirm callbacks与发布线程之间进行消息传递

```java
    public void publishMessageAsync() throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启确认发布
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //线程安全有序的一个哈希表，适用于高并发的情况下
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();
        //消息确认成功 回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            if (multiple) {
                //2.删除掉已经确认的消息，剩余未确认的消息
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            } else {
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认的消息：" + deliveryTag);
        };
        //消息确认失败 回调函数
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            //打印一下未确认的消息都有哪些
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息是：" + message + "未确认的消息tag：" + deliveryTag);
        };
        //准备消息的监听器，监听哪些消息成功了，哪些消息失败了，异步通知
        channel.addConfirmListener(ackCallback, nackCallback);

        //批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            //1.此处记录下所有要发送的消息 消息的总和
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
        }

        //结束时间
        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个异步确认消息，耗时" + (end - begin) + "ms");
    }
```

### 交换机

生产者生产的消息从不会直接发送到队列，生产者只能将消息发送到交换机，交换机工作的内容非常简单，一方面他接收来自生产者的消息，另一方面将他们推入队列

**exchanges的类型**

+ 直接（direct）
+ 主题（topic）
+ 标题（headers）
+ 扇出（fanout）

**临时队列**

断开消费者连接，队列将自动删除

创建临时队列的方式：channel.queueDeclare().getQueue()

#### Fanout

这种类型非常简单，他是将接收到的所有消息广播到他知道的所有队列中。

```java
public class ReceiveLogs01 {

    public static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //声明一个临时队列
        String queueName = channel.queueDeclare().getQueue();
        //绑定交换机与队列
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("等待接收消息，把接收到的消息打印在屏幕上......");

        //接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> System.out.println(new String(message.getBody()));
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
```

```java
public class ReceiveLogs02 {

    public static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //声明一个临时队列
        String queueName = channel.queueDeclare().getQueue();
        //绑定交换机与队列
        channel.queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("等待接收消息，把接收到的消息打印在屏幕上......");

        //接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> System.out.println(new String(message.getBody()));
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});
    }
}
```

#### Direct exchange

消息只能去到他绑定的routingKey队列中去

```java
public class ReceiveLogsDirect01 {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明一个临时队列
        channel.queueDeclare("console", false, false, false, null);
        //绑定交换机与队列
        channel.queueBind("console", EXCHANGE_NAME, "info");
        channel.queueBind("console", EXCHANGE_NAME, "warning");

        //接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> System.out.println(new String(message.getBody()));
        channel.basicConsume("console", true, deliverCallback, consumerTag -> {});
    }
}
```

```java
public class ReceiveLogsDirect02 {
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        //声明一个临时队列
        channel.queueDeclare("disk", false, false, false, null);
        //绑定交换机与队列
        channel.queueBind("disk", EXCHANGE_NAME, "error");

        //接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> System.out.println(new String(message.getBody()));
        channel.basicConsume("disk", true, deliverCallback, consumerTag -> {});
    }
}
```

```java
public class DirectLogs {
    public static final String EXCHANGE_NAME = "direct_logs";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME, "info", null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息：" + message);
        }
    }
}
```

#### Topics

direct交换机改进了我们的系统，但是他仍然存在局限性，比方说我们想接收的日志类型有info.base和info.advantage，某个队列只想info.base的消息，那这个时候direct就办不到了，这个时候只能使用topic类型

发送到类型是topic交换机的消息的routing_key不能随意写，必须满足一定的要求，他必须是一个单词列表，以点号分隔开，比如stock.usd.nyse，单词列表最多不能超过255个字节

替换符：

+ *：可以代替一个单词
+ #：可以代替零个或多个单词

当一个队列绑定键是#，那么这个队列将接收所有数据，就有点像fanout了

如果队列绑定键中没有#和*出现，那么该队列绑定类型就是direct了

```java
public class ReceiveLogsTopic01 {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //声明一个队列
        channel.queueDeclare("Q1", false, false, false, null);
        //绑定交换机与队列
        channel.queueBind("Q1", EXCHANGE_NAME, "*.orange.*");

        //接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> System.out.println(new String(message.getBody()));
        channel.basicConsume("Q1", true, deliverCallback, consumerTag -> {});
    }
}
```

```java
public class ReceiveLogsTopic02 {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //声明一个队列
        channel.queueDeclare("Q2", false, false, false, null);
        //绑定交换机与队列
        channel.queueBind("Q2", EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind("Q2", EXCHANGE_NAME, "lazy.#");

        //接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> System.out.println(new String(message.getBody()));
        channel.basicConsume("Q2", true, deliverCallback, consumerTag -> {});
    }
}
```

```java
public class TopicLogs {
    public static final String EXCHANGE_NAME = "topic_logs";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.basicPublish(EXCHANGE_NAME, "*.*.rabbit", null, message.getBytes("UTF-8"));
    }
}
```

### 死信队列

死信就是无法被消费的消息

死信的来源：

+ 消息TTL过期
+ 队列达到最大长度
+ 消息被拒绝

```java
public class Cousumer01 {

    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "dead_exchange";
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception{

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明死信和普通交换机，类型为direct
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //声明普通队列
        Map<String, Object> arguments = new HashMap<>();
        //设置过期时间，消息TTL过期
        arguments.put("x-message-ttl", 10000);
        //正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //设置死信routingKey
        arguments.put("x-dead-letter-routing-key", "lisi");
        //设置正常队列长度的限制，队列达到最大长度
        arguments.put("x-max-length", 6);
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);

        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        //绑定普通的交换机与普通的队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        //绑定死信的交换机与死信的队列
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody, "UTF-8");
            if(msg.equals("info5")) {
                //消息被拒绝
                channel.basicReject(message.getEnvelop().getDeliveryTag(), false);
            }
            System.out.println(new String(message.getBody()));
            channel.basicAck(message.getEnvelop().getDeliveryTag(), false);
        };
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, consumerTag -> {});
    }
}

```

```java
public class Producer {

    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //死信消息，设置TTL时间
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
        String message = "info";
        channel.basicPublish(NORMAL_EXCHANGE, "zhangsan",properties ,message.getBytes());
    }
}
```

```java
public class Cousumer02 {

    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception{

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        DeliverCallback deliverCallback = (consumerTag, message) -> System.out.println(new String(message.getBody()));
        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, consumerTag -> {});
    }
}

```

### 延迟队列

延迟队列，队列内部是有序的，最重要的特性就体现在它的延时属性上，延迟队列中的元素是希望在指定的时间到了以后或之前取出和处理，简单来说，延时队列就是用来存放需要在指定时间被处理的元素的队列

#### 利用死信队列实现延迟队列

![](/img/rabbitMQ_7.png)

```java
//配置类
@Configuration
public class TtlQueueConfig {

    //普通交换机名称
    public static final String X_EXCHANGE = "X";
    //死信交换机的名称
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    //普通队列的名称
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    //死信队列的名称
    public static final String DEAD_LETTER_QUEUE = "QD";

    //声明xExchange
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    //声明yExchange
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    //声明普通队列，TTL为10s
    @Bean("queueA")
    public Queue queueA() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", "YD");
        arguments.put("x-message-ttl", 10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    //声明普通队列，TTL为40s
    @Bean("queueB")
    public Queue queueB() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", "YD");
        arguments.put("x-message-ttl", 40000);
        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    //死信队列
    @Bean("queueD")
    public Queue queueD() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
    }

    //绑定
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queueA,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueA).to(xExchange).with("XA");
    }

    //绑定
    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,
                                  @Qualifier("xExchange") DirectExchange xExchange) {
        return BindingBuilder.bind(queueB).to(xExchange).with("XB");
    }

    //绑定
    @Bean
    public Binding queueDBindingX(@Qualifier("queueD") Queue queueD,
                                  @Qualifier("yExchange") DirectExchange yExchange) {
        return BindingBuilder.bind(queueD).to(yExchange).with("YD");
    }

}
```

```java
//生产者
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMessageController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {
      log.info("当前时间：{},发送一条消息给两个TTL队列：{}", new Date().toString(), message);

      rabbitTemplate.convertAndSend("X", "XA", "消息来自TTL为10s的队列：" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自TTL为40s的队列：" + message);
    }
}
```

```java
//消费者
@Slf4j
@Component
public class DeadLetterQueueConsumer {

    //接收消息
    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel) throws Exception {
        String msg = new String(message.getBody());
        log.info("当前时间：{}，收到死信队列的消息：{}", new Date().toString(), msg);
    }
}
```

**延迟队列优化**

上方架构缺点：每增加一个新的时间需求，就要增加一个队列

![](/img/rabbitMQ_8.png)

```java
//配置类新增
public static final String QUEUE_C = "QC";

//声明普通队列
@Bean("queueC")
public Queue queueC() {
    Map<String, Object> arguments = new HashMap<>();
    arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
    arguments.put("x-dead-letter-routing-key", "YD");
    return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
}

//绑定
@Bean
public Binding queueCBindingX(@Qualifier("queueC") Queue queueC,
                              @Qualifier("xExchange") DirectExchange xExchange) {
    return BindingBuilder.bind(queueC).to(xExchange).with("XC");
}
```

```java
//生产者
@RequestMapping("/sendMessage/{message}/{ttlTime}")
public void sendMessage(@PathVariable String message, @PathVariable String ttlTime) {
    log.info("当前时间：{},发送一条时长{}毫秒TTL信息给队列QC：{}", new Date().toString(), ttlTime, message);

    rabbitTemplate.convertAndSend("X", "XC", message, msg -> {
        //发送消息的时候，延迟时长
        msg.getMessageProperties().setExpiration(ttlTime);
        return msg;
    });
}
```

如果使用在消息属性上设置TTL的方式，消息可能并不会按时死亡，因为rabbitMQ只会检查第一个消息是否过期，如果过期则丢到死信队列，如果第一个消息的延时时长很长，而第二个消息的延时时长很短，第二个消息并不会优先得到执行

#### RabbitMQ插件实现延迟队列

插件：https://www.rabbitmq.com/community-plugins.html

/usr/lib/rabbitmq/lib/rabbitmq_server-3.8.8/plugins

rabbitmq-plugins enable rabbitmq_delayed_message_exchange

安装完后重启rabbitmq：systemctl restart rabbitmq-server

![](/img/rabbitMQ_9.png)

```java
//配置类
@Configuration
public class DelayQueueConfig {

    //队列
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    //交换机
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    //routingKey
    public static final String DELAYED_ROUTING_KEY = "delayed.routingKey";

    @Bean
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }

    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, arguments);
    }

    public Binding delayedQueueBindingDelayedExchange(@Qualifier("delayedQueue") Queue delayedQueue,
                                                      @Qualifier("delayedExchange") CustomExchange delayedExchange) {
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
```

```java
//生产者
@RequestMapping("/sendDelayedMessage/{message}/{delayedTime}")
public void sendMessage(@PathVariable String message, @PathVariable Integer delayedTime) {
    log.info("当前时间：{},发送一条时长{}毫秒信息给延迟队列队列delayed.queue：{}", new Date().toString(), delayedTime, message);

    rabbitTemplate.convertAndSend(DelayQueueConfig.DELAYED_EXCHANGE_NAME, DelayQueueConfig.DELAYED_ROUTING_KEY, message, msg -> {
        //发送消息的时候，延迟时长 单位ms
        msg.getMessageProperties().setDelay(delayedTime);
        return msg;
    });
}
```

```java
//消费者
@Slf4j
@Component
public class DelayedQueueConsumer {
    //接收消息
    @RabbitListener(queues = DelayQueueConfig.DELAYED_QUEUE_NAME)
    public void receiveDelayQueue(Message message) throws Exception {
        String msg = new String(message.getBody());
        log.info("当前时间：{}，收到延迟队列的消息：{}", new Date().toString(), msg);
    }
}
```

### 发布确认高级

#### 发布确认

配置文件添加spring.rabbitmq.publisher-confirm-type=correlated

+ NONE：禁用发布确认模式，默认值
+ CORRELATED：发布消息成功到交换机后会触发回调方法
+ SIMPLE：有两种效果，第一种和CORRELATED一样，第二种是在发布消息成功后使用rabbitTemplate调用waitForConfirms或waitForConfirmsOrDie方法等待broker节点返回发送结果，根据返回结果来判断下一步的逻辑，要注意的点是waitForConfirmOrDie方法如果返回false则会关闭channel，则接下来无法发送消息到broker。发一条确认一次

```java
//配置类
@Configuration
public class ConfirmConfig {

    public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";

    public static final String CONFIRM_QUEUE_NAME = "confirm_queue";

    public static final String CONFIRM_ROUTING_KEY = "key1";

    @Bean
    public DirectExchange confirmExchange() {
        return new DirectExchange(CONFIRM_EXCHANGE_NAME);
    }

    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    @Bean
    public Binding queueBindingExchange(@Qualifier("confirmQueue") Queue confirmQueue,
                                        @Qualifier("confirmExchange") DirectExchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }
}
```

```java
//生产者
@RequestMapping("/sendMessage/{message}")
public void sendMessage(@PathVariable String message) {
    //设置传送到回调接口里的信息
    CorrelationData correlationData = new CorrelationData("1");
    rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME, ConfirmConfig.CONFIRM_ROUTING_KEY, message,correlationData);
}
```

```java
//消费者
@Slf4j
@Component
public class Consumer {
    //接收消息
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveConfirmMessage(Message message) throws Exception {
        System.out.println(new String(message.getBody()));
    }
}
```

```java
//回调接口
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
    }

    //交换机确认回调方法
    //1.发消息，交换机接收到了，回调。correlationData保存回调消息的id及相关消息，交换机收到消息ack=true,cause为null
    //2.发消息，交换机接收失败了，回调。correlationData保存回调消息的id及相关消息，交换机收到消息ack=false,cause为失败的原因
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经接收到id为{}的消息",id);
        } else {
            log.info("交换机还未收到id为{}的消息，原因是{}", correlationData.getId(), cause);
        }
    }
}
```

#### 回退消息

**Mandatory参数**

在仅开启了生产者确认机制的情况下，交换机接收到消息后，会直接给消息生产者发送确认消息，如果发现该消息不可路由，那么消息会被直接丢弃，此时生产者是不知道消息被丢弃这个事件的。通过设置mandatory参数可以在当消息传递过程中不可达目的地时将消息返回给生产者

配置文件添加spring.rabbitmq.publisher-returns=true

```java
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    //交换机确认回调方法
    //1.发消息，交换机接收到了，回调。correlationData保存回调消息的id及相关消息，交换机收到消息ack=true,cause为null
    //2.发消息，交换机接收失败了，回调。correlationData保存回调消息的id及相关消息，交换机收到消息ack=false,cause为失败的原因
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经接收到id为{}的消息",id);
        } else {
            log.info("交换机还未收到id为{}的消息，原因是{}", correlationData.getId(), cause);
        }
    }

    //当消息传递过程中不可达目的地时将消息返回给生产者
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("消息{}被退回", returnedMessage.getMessage());
    }
}
```

### 备份交换机

当我们为一个交换机声明一个备份交换机时，当交换机接收到一条不可路由消息时，将会把这条消息转发备份到备份交换机中，由备份交换机进行转发和处理，通常备份交换机的类型为Fanout，这样就能把所有消息都投递到与其绑定的队列中

mandatory参数与备份交换机可以一起使用的时候，如果两者同时开启，备份交换机优先级高

![](/img/rabbitMQ_10.png)

```java
//配置类
@Configuration
public class ConfirmConfig {

    public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";

    public static final String CONFIRM_QUEUE_NAME = "confirm_queue";

    public static final String CONFIRM_ROUTING_KEY = "key1";
    //备份交换机
    public static final String BACKUP_EXCHANGE_NAME = "backup_exchange";
    //备份队列
    public static final String BACKUP_QUEUE_NAME = "backup_queue";
    //报警队列
    public static final String WARNING_QUEUE_NAME = "warning_queue";

    @Bean
    public DirectExchange confirmExchange() {
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).durable(true).withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME).build();
    }

    @Bean
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    @Bean
    public Binding queueBindingExchange(@Qualifier("confirmQueue") Queue confirmQueue,
                                        @Qualifier("confirmExchange") DirectExchange confirmExchange) {
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }

    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    @Bean
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    @Bean
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    @Bean
    public Binding backupQueueBindingBackupExchange(@Qualifier("backupQueue") Queue backupQueue,
                                        @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }

    @Bean
    public Binding warningQueueBindingBackupExchange(@Qualifier("warningQueue") Queue warningQueue,
                                                    @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }
}
```

```java
//消费者
@Component
@Slf4j
public class WarningConsumer {

    //接收消息
    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWarningMsg(Message message) {
        String msg = new String(message.getBody());
        log.info("报警发现不可路由消息：{}", msg);
    }
}
```

### RabbitMQ其他知识点

#### 幂等性

**概念**

用户对于同一操作发起的一次请求或者多次请求的结果是一致的，不会因为多次点击而产生了副作用

**消息重复消费**

消费者在消费MQ中的消息时，MQ已把消息发送给消费者，消费者在给MQ返回ack时网络中断，故MQ未收到确认信息，该条消息回重新发送给其他的消费者，或者在网络重连后再次发送给该消费者，但实际上该消费者已成功消费了该条消息，造成消费者消费了重复的消息

**解决思路**

MQ消费者的幂等性的解决一般使用全局ID或者写个唯一标识比如时间戳、UUID或者订单消费者消费MQ中的消息也可利用MQ的该id来判断，或者可按自己的规则生成一个全局唯一id，每次消费消息时用该id先判断该消息是否已消费过

**消费端的幂等性保障**

在海量订单生成的业务高峰期，生产端有可能重复发生了消息，这时候消费端就要实现幂等性，这就意味着我们的消息永远不会消费多次，即使我们收到了一样的消息。业界主流的幂等性有两种操作：

1. 唯一id+指纹码机制，利用数据库主键去重

   指纹码：我们的一些规则或者时间戳加别的服务给到的唯一信息码，他不一定是我们系统生成的，基本都是我们的业务规则拼接而来，但是一定要保证唯一性，然后就利用查询语句进行判断这个id是否存在数据库中，优势是实现简单就一个拼接，然后查询判断是否重复；劣势就是在高并发时，如果是单个数据库会有写入性能瓶颈当然也可以采用分库分表提升性能，但也不是我们最推荐的方式

2. 利用redis的原子性去实现

   redis原子性：利用redis执行setnx命令，天然具有幂等性，从而实现不重复消费

#### 优先级队列

优先级为0-255

**如何添加**

1. 控制台页面添加

2. 队列中代码添加优先级

   ```java
   Map<String, Object> params = new HashMap();
   params.put("x-max-priority", 10);
   channel.queueDeclare("hello", true, false, false, params);
   ```

3. 消息中代码添加优先级

   ```java
   AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
   ```

注意事项：队列需要设置为优先级队列，消息需要设置消息的优先级，消费者需要等待消息已经发送到队列中才去消费，因为这样才有机会对消息进行排序

```java
@Service
public class Producer {
    public static final String QUEUE_NAME = "hello";

    public void producer() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("47.109.47.76");
        factory.setUsername("admin");
        factory.setPassword("admin");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        Map<String, Object> params = new HashMap();
        params.put("x-max-priority", 10);
        channel.queueDeclare(QUEUE_NAME, false, false, false, params);

        for (int i = 1; i < 11; i++) {
            String message = "info" + i;
            if (i == 5) {
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());
            } else {
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            }
        }

        System.out.println("消息发送完毕");
    }
}
```

#### 惰性队列

RabbitMQ3.6.0版本开始引入了惰性队列。惰性队列会尽可能的将消息存入磁盘中，而在消费者消费到相应的消息时才会被加载到内存中，他的一个设计目标是能够支持更长的队列，即支持更多的消息存储。

**两种模式**

+ default：默认
+ lazy：

```java
Map<String, Object> params = new HashMap();
params.put("x-queue-mode", "lazy");
channel.queueDeclare("hello", true, false, false, params);
```

**内存开销对比**

在发送一百万条消息，每条消息大概占1KB的情况下，普通队列占用的内存是1.2G，而惰性队列拒绝占用1.5M

### RabbitMQ集群

#### 搭建集群

**搭建步骤**

1. 修改三台机器的主机名称

   vim /etc/hostname

2. 配置各个节点的hosts文件，让各个节点都能互相识别对方

   vim /etc/hosts

   10.211.55.74 node1

   10.211.55.75 node2

   10.211.55.76 node3

3. 以确保各个节点的cookie文件使用的是同一个值

   在node1上执行远程操作命令

   scp /var/lib/rabbitmq/.erlang.cookie root@node2:/var/lib/rabbitmq/.erlang.cookie

   scp /var/lib/rabbitmq/.erlang.cookie root@node3:/var/lib/rabbitmq/.erlang.cookie

4. 启动rabbitMQ服务，顺带启动Erlang虚拟机和RabbitMQ应用服务，在三台节点上分别执行以下命令

   rabbitmq-server -detached

5. 在节点2执行

   rabbitmqctl stop_app（rabbitmqctl stop会将Erlang虚拟机关闭，rabbitmqctl stop_app只关闭RabbitMQ服务）

   rabbitmqctl reset

   rabbitmqctl join_cluster rabbit@node1

   rabbitmqctl start_app（只启动应用服务）

6. 在节点3执行

   rabbitmqctl stop_app（rabbitmqctl stop会将Erlang虚拟机关闭，rabbitmqctl stop_app只关闭RabbitMQ服务）

   rabbitmqctl reset

   rabbitmqctl join_cluster rabbit@node1

   rabbitmqctl start_app（只启动应用服务）

7. 集群状态

   rabbitmqctl cluster_status

8. 需要重新设置用户

   创建账号：rabbitmqctl add_user admin 123

   设置用户角色：rabbitmqctl set_user_tags admin adminstrator

   设置用户权限：rabbitmqctl set_permissions -p "/" admin ".*" ". *" ". *"

9. 解除集群节点（node2和node3机器分别执行）

   rabbitmqctl stop_app

   rabbitmqctl reset

   rabbitmqctl start_app

   rabbitmqctl cluster-status

   rabbitmqctl forget_cluster_node rabbit@node2（node1机器上执行）

#### 镜像队列

如果rabbitmq集群中只有一个broker节点，那么该节点的失效将导致整体服务的临时性不可用，并且也可能会导致消息的丢失。可以将所有消息都设置为持久化，并且相应队列的durable属性也设置为true，但是这样仍然无法避免由于缓存导致的问题：因为消息在发送之后和被写入磁盘并执行刷盘动作之间存在一个短暂却会产生问题的时间窗。通过publisherconfirm机制能够确保客户端知道哪些消息已经存入磁盘，尽管如此，一般不希望遇到因单点故障导致的服务不可以

引入镜像队列（Mirror Queue）的机制，可以将队列镜像到集群中的其他broker节点之上，如果集群中的一个节点失效了，队列能自动地切换到镜像中的另一个节点上以保证服务的可用性

rabbitmq控制台配置

![](/img/rabbitMQ_11.png)

#### 负载均衡

haproxy+keepalive

haproxy也可用nginx

#### Federation Exchange

在每台机器上开启federation相关插件

rabbitmq-plugins enable rabbitmq_federation

rabbitmq-plugins enable rabbitmq_federation_management

控制台配置

#### Federation Queue

#### Shovel

## RocketMQ

### 基本概念

**消息**

消息是指消息系统所传输消息的物理载体，生产和消费数据的最小单位，每条消息必须属于一个主题

**主题（Topic）**

Topic表示一类消息的集合，每个主题包含若干条消息，每条消息只能属于一个主题，是RocketMQ进行消息订阅的基本单位

一个生产者可以同时发送多种topic的消息；而一个消费者只对某种特定的topic感兴趣，即只可订阅和消费一种topic的消息

**标签（Tag）**

为消息设置的标签，用于同一主题下区分不同类型的消息。来自同一业务单元的消息，可以根据不同业务目的在同一主题下设置不同的标签。标签能够有效的保持代码的清晰度和连贯性，并优化RocketMQ提供的查询系统。消费者可以根据Tag实现对不同子主题的不同消费逻辑，实现更好的扩展性

**队列（Queue）**

存储消息的物理实体。一个Topic中可以包含多个Queue，每个Queue中存放的就是该topic的消息。一个Topic的Queue也被称为一个Topic中消息的分区（Partition）

分片（Sharding）：不同于分区，在RocketMQ中，分片指的是存放相应Topic的Broker。每个分片中会创建出相应数量的分区，即Queue，每个Queue的大小都是相同的

![](/img/RocketMQ_1.png)

**消息标识（MessageId/Key）**

RocketMQ中的每个消息拥有唯一的MessageId，且可以携带业务标识的Key，以方便对消息的查询。不过需要注意的是，MessageId有两个：在生产者send消息时会自动生成一个MessageId（msgId），当消息到达Broker后，Broker也会自动生成一个MessageId（offsetMsgId）

+ msgId：由producer端生成，其生成规则为producerIp+进程pid+MessageClientIDSetter类的Classloader的hashCode+当前时间+AutomicInteger自增计数器
+ offsetMsgId：由Broker端生成，其生成规则为brokerIp+物理分区的offset
+ key：由用户指定的业务相关的唯一标识

### 系统架构

#### Producer

消息生产者，负责生产消息。Producer通过MQ的负载均衡模块选择相应的Broker集群队列进行消息投递，投递的过程支持快速失败并且低延迟

RocketMQ中的消息生产者都是以生产者组（Producer Group）的形式出现的。生产者组是同一类生产者的集合，这类Producer发送相同Topic类型的消息。一个生产者组可以同时发送多个主题的消息

#### Consumer

消息消费者 ，负责消费消息。一个消息消费者会从Broker服务器中获取到消息，并对消息进行相关业务处理。

RocketMQ中的消息消费者都是以消费者组（Consumer Group）的形式出现的。消费者组是同一类消费者的集合，这类Consumer消费的是同一个Topic类型的消息。消费者组使得在消息消费方面，实现负载均衡（将一个Topic中的不同的Queue平均分配给同一个Consumer Group的不同的Consumer）和容错（一个Consumer挂了，该Consumer Group中的其他Consumer可以接着消费原Consumer消费的Queue）的目标变得非常容易。

#### Name Server

NameServer是一个Broker和Topic路由的注册中心，支持Broker的动态注册和发现

主要包括两个功能：

+ Broker管理：接受Broker集群的注册信息并且保存下来作为路由信息的基本数据；提供心跳检测机制，检查Broker是否存活
+ 路由消息管理：每个NameServer中都保存着Broker集群的整个路由信息和用于客户端查询的队列信息。Producer和Consumer通过NameServer可以获取整个Broker集群的路由信息，从而进行消息的消费和投递

**路由注册**

NameServer通常也是以集群的方式部署，不过NameServer是无状态的，即NameServer集群中的各个节点间是无差异的，各节点间相互不进行信息通讯。那各节点中的数据是如何进行数据同步的呢？在Broker节点启动时，轮询NameServer列表，与每个NameServer节点建立长连接，发起注册请求。在NameServer内部维护一个Broker列表，用来动态存储Broker信息

+ 优点：NameServer集群搭建简单，扩容简单
+ 缺点：对于Broker，必须明确指出所有NameServer地址，否则未指出的将不会去注册。也正因为如此，NameServer不能随便扩容

Broker节点为了证明自己是活着的，为了维护与NameServer间的长连接，会将最新的信息以心跳包的方式上报给NameServer，每30秒发送一次心跳。心跳包中包含BrokerId、Broker地址（IP+端口）、Broker名称、Broker所属集群名称等等。NameServer在接收到心跳包后，会更新心跳时间戳，记录这个Broker的最新存活时间

**路由剔除**

由于Broker关机、宕机或网络抖动等原因，NameServer没有收到Broker的心跳，NameServer可能会将其从Broker列表中删除

NameServer中有一个定时任务，每隔10秒就会扫描一次Broker表，查看每一个Broker的最新心跳时间戳距离当前时间是否超过120秒，如果超过，则会判定Broker失效，然后将其从Broker列表中移除

**路由发现**

RockerMQ的路由发现采用的是Pull模型。当Topic路由信息出现变化的时候，NameServer不会主动推送给客户端，而是客户端定时拉取主题最新的路由。默认客户端每30秒会拉取一次最新的路由

+ Push模型：推送模型，实时性较好，是一个发布订阅模型，需要维护一个长连接，而长连接的维护是需要资源成本的。该模型适合实时性要求较高，Client数量不多，Server数据变化较频繁
+ Pull模型：拉取模型，实时性较差
+ Long Polling模型：长轮询模型，是对Push和Pull模型的整合，充分利用了这两种模型的优势，屏蔽了它们的劣势

**客户端NameServer选择策略**

首先采用的是随机策略进行的选择，失败后采用的是轮询策略

#### Broker

Broker充当着消息中转角色，负责存储消息、转发消息。Broker在RocketMQ系统中负责接收并存储从生产者发送来的消息，同时为消费者的拉取请求做准备。Broker同时也存储着消息相关的元数据，包括消费者组消费进度偏移offset、主题、队列等

+ Remoting Module：整个Broker的实体，负责处理来自clients端的请求。而这个Broker实体则由以下模块构成
+ Client Manager：客户端管理器。负责接收、解析客户端（Producer/Consumer）请求，管理客户端。例如维护Consumer的Topic订阅信息
+ Store Service：存储服务。提供方便简单的API接口，处理消息存储到物理硬盘和消息查询功能
+ HA Service：高可用服务，提供Master Broker和Slave Broker之间的数据同步功能
+ Index Service：索引服务。根据特定的Message Key，对投递到Broker的消息进行索引服务，同时也提供根据Message Key对消息进行快速查询的功能

**集群部署**

为了增强Broker性能与吞吐量，Broker一般都是以集群形式出现的。各集群节点中可能存放着相同Topic的不同Queue。如果Broker节点宕机，如何保证数据不丢失呢？其解决方案是将每个Broker集群节点进行横向扩展，即将Broker节点再建为一个HA集群，解决单点问题。

Broker节点集群是一个主从集群，即集群中有Master和Slave两种角色。Master负责处理读写操作请求，而Slave仅负责对Master中的数据进行备份。当Master挂了，Slave会自动切换为Master去工作。所以这个Broker集群是主备集群。一个master可以包含多个slave，但是一个slave只能隶属于一个master。Master与Slave的对应关系是通过指定相同的BrokerName、不同的BrokerId来确定的。BrokerId为0表示Master，而非0表示Slave。每个Broker与NameServer集群中的所有节点建立长连接，定时注册Topic信息到所有NameServer

![](/img/RocketMQ_2.png)

#### 工作流程

1. 启动NameServer，NameServer启动后开始监听端口，等待Broker、Producer、Consumer连接
2. 启动Broker时，Broker会与所有的NameServer建立并保持长连接，然后每30秒向NameServer定时发送心跳包
3. 发送消息前，可以先创建Topic，创建Topic时需要指定该Topic要存储在哪些Broker上，当然，在创建Topic时也会将Topic与Broker的关系写入到NameServer中。不过，这步是可选的，也可以在发送消息时自动创建Topic
4. Producer发送消息，启动时先跟NameServer集群中的其中一台建立长连接，并从NameServer中获取路由信息，即当前发送的Topic消息的Queue与Broker的地址（IP+port）的映射关系。然后根据算法策略从队选择一个Queue，与队列所在的Broker建立长连接从而向Broker发消息。当然，在获取到路由信息后，Producer会首先将路由信息缓存到本地，再每30秒从NameServer更新一次路由信息
5. Consumer跟Producer类似，跟其中一台NameServer建立长连接，获取其订阅Topic的路由信息，然后根据算法策略从路由信息中获取到其所要消费的Queue，然后直接跟Broker建立长连接，开始消费其中的消息。Consumer再获取到路由信息后，同样会每30秒从NameServer更新一次路由信息。不过不同于Producer的是，Consumer还会向Broker发送心跳，以确保Broker的存活状态

**Topic的创建**

手动创建Topic时，有两种模式：

+ 集群模式：该模式下创建的Topic在该集群中，所有Broker中的Queue数量是相同的
+ Broker模式：该模式下创建的Topic在该集群中，每个Broker中的数量可以不同

自动创建Topic时，默认采用的是Broker模式，会为每个Broker默认创建4个Queue

**读/写队列**

从物理上来讲，读/写队列是同一个队列，所以不存在数据同步问题。读/写队列是逻辑上进行区分的概念。一般情况下，读写队列数量是相同的。

这样设计的目的是为了方便Topic的Queue的缩容

### 集群搭建理论

#### 数据复制与刷盘策略

**复制策略**

复制策略是Broker的Master与Slave间的数据同步方式。分为同步复制与异步复制

+ 同步复制：消息写入master后，master会等待slave同步数据成功后才向producer返回成功ACK

+ 异步复制：消息写入master后，master立即向producer返回成功ACK，无需等待slave同步数据成功

  异步复制会降低系统的写入延迟，RT变小，提高了系统的吞吐量

**刷盘策略**

刷盘策略指的是broker中消息的落盘方式，即消息发送到Broker内存后消息持久化到磁盘的方式。分为同步刷盘和异步刷盘：

+ 同步刷盘：当消息持久化到broker的磁盘后才算是消息写入成功

+ 异步刷盘：当消息写入到broker的内存后即表示消息写入成功，无需等待消息持久化到磁盘

  异步刷盘会降低系统的写入延迟，RT变小，提高了系统的吞吐量。消息写入到Broker内存，一般是写入到了pageCache，对于异步刷盘策略，消息会写入到pageCache后立即返回成功ACK，但并不会立即做落盘操作，而是当pageCache到达一定量时会自动进行落盘

#### Broker集群模式

**单Master**

只有一个broker，生产环境不能使用，存在单点问题

**多master**

broker集群由多个master构成，不存在slave。同一个Topic的各个Queue会分布在各个master节点上

+ 优点：配置简单，单个master宕机或重启维护对应用无影响，在磁盘配置为RAID10时，即使机器宕机不可恢复情况下，由于RAID10磁盘非常可靠，消息也不会丢（异步刷盘丢失少量消息，同步刷盘一条不丢），性能最高
+ 缺点：单台机器宕机期间，这台机器上未被消费的消息在机器恢复之前不可订阅，消息实时性会受到影响

**多Master多Slave模式-异步复制**

broker集群由多个master构成，每个master又配置了多个slave（在配置了RAID磁盘阵列的情况下，一个master一般配置一个slave即可）。master与slave的关系是主备关系，即master负责处理消息的读写情况，而slave仅负责消息的备份与master宕机后的角色切换

由于slave与master的同步具有短暂的延迟，所以当master宕机后，这种异步复制方式可能存在少量消息的丢失

**多Master多Slave模式-同步双写**

该模式是多master多slave模式的同步复制实现。所谓同步双写，指的是消息写入master成功后，master会等待slave同步数据成功后才向producer返回成功ACK，即master与slave都要写入成功后才会返回ACK

该模式与异步复制模式比，优点是消息的安全性更高，不存在消息丢失的情况。单个消息的RT略高，从而导致性能要略低（大约低10%）

该模式存在一个最大的问题：对于目前的版本，master宕机后，slave不能自动切换到master

**最佳实践**

一般会为master配置RAID10磁盘阵列，然后再为其配置一个slave，即利用RAID10磁盘阵列的高效、安全性，又解决了可能会影响订阅的问题

#### 集群搭建实践

双主双从

rocketmqOS1 master1+slave2

rocketmqOS2 master2+slave1

**修改配置文件**

配置文件在rocketMQ解压目录的conf/2m-2s-async目录中

**修改broker-a.properties**

```shell
#指定整个broker集群的名称，或者说是rocketmq集群的名称
brokerClusterName=DefaultCluster
#指定master-slave集群的名称。一个RocketMQ集群可以包含多个master-slave集群
brokerName=broker-a
#master的brokerId为0
brokerId=0
#指定删除消息存储过期文件的时间为凌晨4点
deleteWhen=04
#指定未发生更新的消息存储文件的保留时长为48小时，48小时后会过期，将会被删除
fileReservedTime=48
#指定当前broker为异步复制master
brokerRole=ASYNC_MASTER
#指定刷盘策略为异步刷盘
flushDiskType=ASYNC_FLUSH
#指定nameserver的地址
namesrvAddr=192.168.59.164:9876;192.168.59.165:9876
```

**修改broker-b-s.properties**

```shell
brokerClusterName=DefaultCluster
#指定这是另外一个master-slave集群
brokerName=broker-b
#slave的brokerId为非0
brokerId=1
deleteWhen=04
fileReservedTime=48
#指定当前broker为slave
brokerRole=SLAVE
flushDiskType=ASYNC_FLUSH
namesrvAddr=192.168.59.164:9876;192.168.59.165:9876
#指定Broker对外提供服务的接口，即broker与producer与consumer通信的端口。默认10911。由于当前主机同时充当着master1与slave2，而前面的master1使用的是默认端口，这里需要将这两个端口加以区分，以区分出master1和slave2
listenPort=11911
#指定消息存储相关的路径。默认路径为~/store目录。由于当前主机同时充当着master1和slave2，master1使用的是默认路径，这里就需要再指定一个不同的路径
storePathRootDir=~/store-s
storePathCommitLog=~/store-s/commitlog
storePathConsumeQueue=~/store-s/consumequeue
storePathIndex=~/store-s/index
storeCheckPoint=~/store-s/checkpoint
abortFile=~/store-s/abort
```

**其他配置**

除了以上配置外，这些配置文件中还可以设置其他属性

```shell

```

**启动NameServer集群**

```shell
nohup sh bin/mqnamesrv &
tail -f ~/logs/rocketmqlogs/namesrv.log
```

**启动master**

```shell
nohup sh bin/mqbroker -c conf/2m-2s-async/broker-a.properties &
tail -f ~/logs/rocketmqlogs/broker.log
```

**启动slave**

```shell
nobup sh bin/mqbroker -c conf/2m-2s-async/broker-b-s.properties &
tail -f ~/logs/rocketmqlogs/broker.log
```

#### mqadmin命令

mq解压目录bin目录下有一个mqadmin命令，该命令是一个运维命令，用于对mq的主题，集群，broker等信息进行管理

在运行mqadmin命令前，需要先修改mq解压目录下bin/tools.sh配置的jdk的ext目录位置。

### RocketMQ工作原理

#### 消息的生产

**消息的生产过程**

producer可以将消息写入到某broker中的某queue中，其经历了如下过程：

+ producer发送消息之前，会先向NameServer发出获取消息Topic的路由信息的请求
+ NameServer返回该Topic的路由表及Broker列表
+ Producer根据代码中指定的Queue选择策略，从Queue列表中选出一个队列，用于后续存储消息
+ Producer对消息做一些特殊处理，例如，消息本身超过4M，则会对其进行压缩
+ Producer向选择出的Queue所在的Broker发出RPC请求，将消息发送到选择出的Queue

路由表：实际是一个Map，key为topic名称，value是一个QueueData实例列表。QueueData并不是一个Queue对应一个QueueData，而是一个Broker中该Topic的所有Queue对应一个QueueData。即只要涉及到该topic的broker，一个broker对应一个QueueData。QueueData中包含brokerName。简单来说，路由表的key为Topic名称，value则为所有涉及该topic的brokerName列表

Broker列表：其实际也是一个Map，key是brokerName，value为BrokerData。一套brokerName名称相同的Master-slave小集群对应一个BrokerData。BrokerData中包含brokerName及一个map。该map的key为brokerId，value为该broker对应的地址。brokerId为0表示该broker为Master，非0表示slave

**Queue选择算法**

对于无序消息，其Queue选择算法，也称为消息投递算法，常见的有两种：

+ 轮询算法

  默认选择算法。该算法保证了每个Queue中可以均匀的获取到消息

  该算法存在一个问题：由于某些原因，在某些Broker上的Queue可能投递延迟较严重。从而导致Producer的缓存队列中出现较大的消息积压，影响消息的投递性能。

+ 最小投递延迟算法

  该算法会统计每次消息投递的时间延迟，然后根据统计出的结果将消息投递到时间延迟最小的Queue。如果延迟相同，则采用轮询算法投递。该算法可以有效提升消息的投递性能。

#### 消息的存储

RocketMQ中的消息存储在本地文件系统中，这些相关文件默许在当前用户主目录下的store目录中

+ abort：该文件在Broker启动后会自动创建，正常关闭Broker，该文件会自动消失。若在没有启动Broker的情况下，发现这个文件是存在的，则说明之前Broker的关闭是非正常关闭。

+ checkpoint：其中存储着commitlog、consumequeue、index文件的最后刷盘时间戳

+ commitlog：其中存放着commitlog文件，而消息是写在commitlog文件中的

+ config：存放着Broker运行期间的一些配置数据

+ consumequeue：其中存放着consumequeue文件，队列就存放在这个目录

+ index：其中存放着消息索引文件indexFile

+ lock：运行期间使用到的全局资源锁

**commitlog文件**

在很多资料中commitlog目录中的文件简单就称为commitlog文件。但在源码中，该文件被命名为mappedFile

commitlog目录中存放着很多的mappedFile文件，当前Broker中的所有消息都是落盘到这些mappedFile文件中的。mappedFile文件大小为1G（小于等于1G），文件名由20位十进制数构成，表示当前文件的第一条消息的起始位移偏移量。

一个Broker中仅包含一个commitlog目录，所有的mappedFile文件都是存放在该目录中的。即无论当前Broker中存放着多少Topic的消息，这些消息都是被顺序写入到mappedFile文件中的。也就是说，这些消息在Broker中存放时并没有按照Topic进行分类存放。

mappedFile文件内容由一个个消息单元构成。每个消息单元中包含消息总长度MsgLen、消息的物理位置physicalOffset、消息内容体Body、消息体长度BodyLength、消息主题Topic、Topic长度TopicLength、消息生产者BornHost、消息发送时间戳BornTimestamp、消息所在的队列QueueId、消息在Queue中存储的偏移量QueueOffset等近20项消息相关属性

**comsumequeue**

为了提高效率，会为每个Topic在~/store/consumequeue中创建一个目录，目录名为Topic名称。在该Topic目录下，会再为每个该Topic的Queue创建一个目录，目录名为queueId。每个目录中存放着若干consumequeue文件，consumequeue文件是commitlog的索引文件，可以根据consumequeue定位到具体的消息

consumequeue文件名也由20位数字构成，表示当前文件的第一个索引条目的起始位移偏移量。与mappedFile文件名不同的是，其后续文件名是固定的。因为consumequeue文件大小是固定不变的

每个consumequeue文件可以包含30w个索引条目，每个索引条目包含了三个消息重要属性：消息在mappedFile文件中的偏移量CommitLog Offset（8字节）、消息长度（4字节）、消息Tag的hashcode值（8字节）。这三个属性占20个字节，所以每个文件的大小是固定的30w*20字节

**消息写入**

一条消息进入到Broker后经历了以下几个过程才最终被持久化

+ Broker根据queueId，获取到该消息对应索引条目要在consumequeue目录中的写入偏移量，即QueueOffset
+ 将queueId、queueOffset等数据，与消息一起封装为消息单元
+ 将消息单元写入到commitlog
+ 形成消息索引条目
+ 将消息索引条目分发到对应的consumequeue

**消息拉取**

当Consumer来拉取消息的时候会经历以下几个步骤

+ Consumer获取到其要消费消息所在Queue的消费偏移量offset，计算出其要消费消息的消息offset
+ Consumer向Broker发送拉取请求，其中会包含其要拉取消息的Queue、消息offset及消息Tag
+ Broker计算在该consumequeue中的queueoffset
+ 从该queueOffset处开始向后查找第一个指定Tag的索引条目
+ 解析该索引条目的前八个字节，即可定位到该消息在commitlog中的commitlog offset
+ 从对应commitlog offset中读取消息单元，并发送给Consumer

**性能提升**

RocketMQ对文件的读写操作是通过mmap零拷贝进行的，将对文件的操作转化为直接内存地址进行操作，从而极大地提高了文件的读写效率

其次，consumequeue中的数据是顺序存放的，还引入了PageCache的预读取机制，使得对consumequeue文件的读取几乎接近于内存读取，即使在消息堆积的情况下也不会影响性能

PageCache机制，页缓存机制，是OS对文件的缓存机制，用于加速对文件的读写操作。一般来说，程序对文件进行顺序读写的速度几乎接近于内存读写速度，主要原因是由于OS使用PageCache机制对读写访问操作进行性能优化，将一部分内存用于PageCache

RocketMQ中可能会影响性能的是对commitlog文件的读取。因为对commitlog文件来说，读取消息时会产生大量的随机访问，而随机访问会严重影响性能。不过，如果选择合适的系统IO调度算法，比如设置调度算法为Deadline（采用SSD固态硬盘的话），随机读的性能也会有所提升

#### indexFile

除了通过通常的指定Topic进行消息消费外，RocketMQ还提供了根据key进行消息查询的功能。该查询是通过store目录中的index子目录中的indexFile进行索引实现的快速查询。当然，这个indexFile中的索引数据是在包含了key的消息被发送到Broker时写入的。如果消息中没有包含key，则不会写入

**索引条目结构**

每个Broker中会包含一组indexFile，每个indexFile都是以一个时间戳命名的。每个indexFile文件由三部分构成：indexHeader、slots槽位、indexes索引数据。每个indexFile文件中包含500w个slot槽。而每个slot槽又可能会挂载很多的index索引单元。

![](/img/RocketMQ_3.png)

indexHeader固定40字节，其中存放如下数据

+ beginTimestamp：该indexFile中第一条消息的存储时间
+ endTimestamp：该indexFile中最后一条消息存储时间
+ beginPhyoffset：该indexFile中第一条消息在commitlog中的偏移量commitlog offset
+ endPhyoffset：该indexFile中最后一条消息在commitlog中的偏移量commitlog offset
+ hashSlotCount：已经填充有index的slot数量
+ indexCount：该indexFile中包含的索引个数

indexFile中最复杂的是Slots与Indexes间的关系。在实际存储时，Indexes是在Slots后面的，但为了便于理解，将他们的关系展示为如下形式

![](/img/RocketMQ_4.png)

key的hash值%500w的结果为slot槽位，然后将该slot值修改为该index索引单元的indexNo，根据这个indexNo可以计算出该index单元在indexFile中的位置。不过，该取模结果的重复率是很高的，为了解决该问题，在每个index索引单元中增加了preIndexNo，用于指定该slot中当前index索引单元的前一个index索引单元。而slot中始终存放的是其下最新的index索引单元的indexNo，这样的话，只要找到了slot就可以找到其最新的index索引单元，而通过这个index索引单元就可以找到其之前的所有index所有单元

indexNo是一个在indexFile中的流水号，从0开始依次递增。即在一个indexFile中所有indexNo是依次递增的。indexNo在index索引单元中是没有体现的，其是通过indexes中依次数出来的

![](/img/RocketMQ_5.png)

indes索引单元默认20个字节，其中存放着以下四个属性

+ keyHash：消息中指定业务key的hash值
+ phyOffset：当前key对应的消息在commitlog中的偏移量commitlog offset
+ timeDiff：当前key对应消息的存储时间与当前indexFile创建时间的时间差
+ preIndexNo：当前slot下当前index索引单元的前一个index索引单元的indexNo

**indexFile的创建**

indexFile的文件名为当前文件被创建时的时间戳

根据业务key进行查询时，查询条件除了key之外，还需要指定一个要查询的时间戳，表示要查询不大于该时间戳的最新的消息，即查询指定时间戳之前存储的最新消息。这个时间戳文件名可以简化查询，提高查询效率

indexFile文件是何时创建的，其创建的条件有两个

+ 当第一条带key的消息发送来后，系统发现没有indexFile，此时会创建第一个indexFile文件
+ 当一个indexFile中挂载的index索引单元数量超过2000w个时，会创建新的indexFile。当带key的消息发送到来后，系统会找到最新的indexFile，并从其indexHeader的最后4字节中读取到indexCount。若indexCount>=2000w时，会创建新的indexFile

由此可以推算出，一个indexFile的最大大小是：（40 + 500w * 4 + 2000w * 20）字节

**查询流程**

计算指定消息key的slot槽位序号：slot槽位序号 = key的hash % 500w

计算槽位序号为n的slot在indexFile中的起始位置：slot(n)位置 = 40 + (n - 1) * 4

计算indexNo为m的index在indexFile中的位置：index(m)位置 = 40 + 500w * 4 + (m - 1) * 20

![](/img/RocketMQ_6.png)

#### 消息的消费

消费者从Broker中获取消息的方式有两种：pull拉取方式和push推动方式。消费者组对于消息消费的模式又分为两种：集群消费Clustering和广播消费Broadcasting

**推拉消费类型**

+ 拉取式消费：Consumer主动从Broker中拉取消息，主动权由Consumer控制。一旦获取了批量消息，就会启动消费过程。不过该方式的实时性较弱，即Broker中有了新消息时消费者并不能及时发现并消费

+ 推送式消费：该模式下Broker收到数据后会主动推送给Consumer。该消费模式一般实时性较高

  该消费类型是典型的发布订阅模式，即Consumer向其关联的Queue注册了监听器，一旦发现有新的消息到来就会触发回调的执行，回调方法是Consumer去Queue中拉取消息。而这些都是基于Consumer与Broker间的长连接的。长连接的维护是需要消耗系统资源的。

对比

+ pull：需要应用去实现对关联Queue的遍历，实时性差；但便于应用控制消息的拉取
+ push：封装了对关联Queue的遍历，实时性强，但会占用较多的系统资源

**消费模式**

+ 广播消费：相同的Consumer Group的每个Consumer实例都接收同一个Topic的全量消息。即每条消息都会被发送到Consumer Group中的每个Consumer
+ 集群消费：相同Consumer Group的每个Consumer实例平均分摊同一个Topic的消息。即每条消息只会被发送到Consumer Group中的某个Consumer

消息进度保存

+ 广播模式：消费进度保存在consumer端。因为广播模式下consumer group中每个consumer都会消费所有消息，但他们的消费进度是不同的。所以consumer各自保存各自的消费进度
+ 集群模式：消费进度保存在broker中。consumer group中的所有consumer共同消费同一个Topic中的消息，同一条消息只会被消费一次。消费进度会参与到了消费的负载均衡中，故消费进度是需要共享的

**Rebalance机制**

Rebalance机制讨论的前提是集群消费

Rebalance即再均衡，指的是将一个Topic下的多个Queue在同一个Consumer Group中的多个Consumer间进行重新分配的过程

![](/img/RocketMQ_7.png)

Rebalance机制的本意是为了提升消息的并行消费能力。

Rebalance限制：由于一个队列最多分配给一个消费者，因此当某个消费者组下的消费者实例数量大于队列的数量时，多余的消费者实例将分配不到任何队列

Rebalance危害：

+ 消费暂停：在只有一个Consumer时，其负责消费所有队列；在新增了一个Consumer后会触发Rebalance的发生。此时原Consumer就需要暂停部分队列的消费，等到这些队列分配给新的consumer后，这些暂停消费的队列才能继续消费
+ 消费重复：Consumer在消费新分配给自己的队列时，必须接着之前Consumer提交的消费进度的offset继续消费。然而默认情况下，offset是异步提交的，这个异步性导致提交到Broker的offset与Consumer实际消费的消息并不一致。这个不一致的差值就是可能会重复消费的消息
+ 消费突刺：由于Rebalance可能导致重复消费，如果需要重复消费的消息过多，或者因为Rebalance暂停时间过长而导致积压了部分消息。那么有可能会导致在Rebalance结束之后瞬间需要消费很多消息

Rebalance产生的原因：消费者所订阅Topic的Queue数量发生变化，或消费者组中消费者的数量发生变化

Rebalance过程：在Broker中维护着多个map集合，这些集合中动态存放着当前Topic中Queue的信息、Consumer Group中Consumer实例的信息。一旦发现消费者所订阅的Queue数量发生变化，或者消费者组中消费者的数量发生变化，立即向Consumer Group中的每个实例发出Rebalance通知。Consumer实例在接收到通知后会采用Queue分配算法自己获取到相应的Queue，即由Consumer实例自主进行Rebalance

TopicConfigManager：key是topic名称，value是topicConfig。TopicConfig中维护着该Topic中所有Queue的数据

ConsumerManager：key是Consumer Group Id，value是ConsumerGroupInfo。ConsumerGroupInfo中维护着该Group中所有Consumer实例数据

ConsumerOffsetManager：key为topic与订阅该topic的Group的组合，value是一个内层map。内层map的key为QueueId，内层Map的value为该Queue的消费进度offset

**Queue分配算法**

一个Topic中的Queue只能由Consumer Group中的一个Consumer进行消费，而一个Consumer可以同时消费多个Queue中的消息。那么Queue与Consumer间的配对关系是如何确定的，即Queue要分配给哪个Consumer进行消费，也是有算法策略的。常见的有四种策略。这些策略是通过在创建Consumer时的构造器传进去的

+ 平均分配策略

  该算法是要根据avg = QueueCount / ConsumerCount的计算结果进行分配的。如果能够整除，则按顺序将avg个Queue逐个分配Consumer；如果不能整除，则将多余出的Queue按照Consumer顺序逐个分配

+ 环形平均策略

  环形平均算法是指根据消费者的顺序，依次在由queue队列形成的环形图中逐个分配

+ 一致性hash策略

  该算法会将consumer的hash值作为Node节点存放到hash环上，然后将queue的hash值也放到hash环上，通过顺时针方向，距离queue最近的那个consumer就是该queue要分配的consumer

  该算法存在的问题：分配不均

+ 同机房策略

  该算法会根据queue的部署机房位置和consumer的位置，过滤出当前consumer相同机房的queue。然后按照平均分配策略或环形分配策略对同机房queue进行分配。如果没有同机房queue，则按照平均分配策略或环形平均策略对所有queue进行分配

对比：

两种平均分配策略的分配效率较高，一致性hash策略的较低。因为一致性hash算法较复杂。另外，一致性hash策略分配的结果也很大可能上存在不平均的情况。

一致性hash算法可以有效减少由于消费者扩容或缩容所带来的大量Rebalance

**至少一次原则**

RocketMQ有一个原则：每条消息必须要被成功消费一次

那什么是成功消费呢？Consumer在消费完消息后会向其消费进度记录器提交其消费消息的offset，offset被成功记录到记录器中，那么这条消息就被成功消费了

对于广播消费模式来说，consumer本身就是消费进度记录器

对于集群消费来说，broker就是消费进度记录器

#### 订阅关系的一致性

订阅关系的一致性指的是，同一个消费者组（Group ID相同）下所有Consumer实例所订阅的Topic与Tag及对消息的处理逻辑必须完全一致。否则，消息消费的逻辑就会混乱，甚至导致消息丢失。

**正确订阅关系**

多个消费者组订阅了多个Topic，并且每个消费者组里的多个消费者实例的订阅关系保持了一致

![](/img/RocketMQ_8.png)

**错误订阅关系**

一个消费者组订阅了多个Topic，但是该消费者组里的多个Consumer实例的订阅关系并没有保持一致

![](/img/RocketMQ_9.png)

#### offset管理

这里的offset指的是Consumer的消费进度offset

消费进度offset是用来记录每个Queue的不同消费组的消费进度的。根据消费进度记录器的不同，可以分为两种模式：本地模式和远程模式。

**offset本地管理模式**

当消费模式为广播消费时，offset使用本地模式存储。因为每条消息会被所有的消费者消费，每个消费者管理自己的消费进度，各个消费者之间不存在消费进度的交集

Consumer在广播消费模式下offset相关数据以json的形式持久化到Consumer本地磁盘文件中，默认文件路径为当前用户主目录下的.rocketmq_offsets/${clientId}/${group}/offsets.json。其中${clientId}为当前消费者id，默认为ip@DEFAULT；${group}为消费者组名称

**offset远程管理模式**

当消费模式为集群消费时，offset使用远程模式管理。因为所有Consumer实例对消息采用的是均衡消费，所有Consumer共享Queue的消费进度。

Consumer在集群消费模式下offset相关数据以json的形式持久化到Broker磁盘文件中，文件路径为当前用户主目录下的store/config/consumerOffset.json。

Broker启动时会加载这个文件，并写入到一个双层Map。外层map的key为topic@group，value为内层map。内层map的key为queueId，value为offset。当发生Rebalance时，新的Consumer会从该map中获取到相应的数据来继续消费

集群模式下offset采用远程管理模式，主要是为了保证Rebalance机制

**offset用途**

当消费完一批消息后，Consumer会提交其消费进度offset给Broker，Broker在收到消费进度后会将其更新到那个双层map（ConsumerOffsetManager）及consumerOffset.json文件中，然后向该consumer进行ACK，而ACK内容中包含三项数据：当前消费队列最小offset（minOffset）、最大offset（maxOffset）、及下次消费的起始offset（nextBeginOffset）

**重试队列**

当rocketMQ对消息的消费出现异常时，会将发生异常的消息的offset提交到Broker中的重试队列。系统在发生消息消费异常时会为当前的topic@group创建一个重试队列，该队列以%RETRY%开头，到达重试时间后进行消费重试

**offset的同步提交与异步提交**

集群消费模式下，Consumer消费完消息后会向Broker提交消费进度offset，其提交方式分为两种：

+ 同步提交：消费者在消费完一批消息后会向Broker提交这些消息的offset，然后等待broker的成功响应。若在等待超时之前收到了成功响应，则继续读取下一批消息进行消费。若没有收到响应，则会重新提交，直到获取到响应。而在这个等待过程中，消费者是阻塞的。其严重影响了消费者的吞吐量。
+ 异步提交：消费者在消费完一批消息后向broker提交offset，但无需等待broker的成功响应，可以继续读取并消费下一批消息。这种方式增加了消费者的吞吐量。但需要注意，broker在收到提交后的offset后，还是会向消费者进行响应的

#### 消费幂等

当出现消费者对某条消息重复消费的情况时，重复消费的结果与消费一次的结果是相同的，并且多次消费并未对业务系统产生任何负面影响

**重复消费情况**

+ 发送时消息重复

  当一条消息已被成功发送到broker并完成持久化，此时出现了网络闪断，从而导致broker对producer应答失败。如果此时producer意识到消息发送失败并尝试再次发送消息，此时broker中就可能会出现两条内容相同并且MessageID也相同的消息，那么后续Consumer就一定会消费两次该消息

+ 消费时消息重复

  消息已投递到consumer并完成业务处理，当consumer给broker反馈应答时网络闪断，broker没有接收到消费成功响应。为了保证消息至少被消费一次的原则，broker将在网络恢复后再次尝试投递之前已被处理过的消息。此时消费者就会收到与之前处理过的内容相同、MessageID也相同的消息

+ Rebalance时消息重复

  当consumer Group中的consumer数量发生变化时，或其订阅的topic的queue数量发生变化时，会触发Rebalance，此时consumer可能收到曾经被消费过的消息

**通用解决方案**

幂等解决方案中的设计中涉及到两项要素：幂等令牌，与唯一性处理

+ 幂等令牌：是生产者和消费者两者中的既定协议，通常指具备唯一业务标识的字符串。例如，订单号、流水号。一般由Producer随着消息一同发送来的
+ 唯一性处理：服务端通过采用一定的算法策略，保证同一个业务逻辑不会被重复执行成功多次

对于常见系统，幂等性操作的通用性解决方案是：

1. 首先通过缓存去重。在缓存中如果已经存在了某幂等令牌，则说明本次操作是重复性操作；若缓存没有命中，则进入下一步
2. 在唯一性处理之气，现在数据库中查询幂等令牌作为索引的数据是否存在。若存在，则说明本次操作作为重复性操作；若不存在，则进入下一步
3. 在同一事务中完成三项操作：唯一性处理后，将幂等令牌写入到缓存，并将幂等令牌作为唯一索引的数据写入到DB中

第一步已经判断过是否是重复性操作了，为什么第二步还要再次判断？

一般缓存中的数据是具有有效期的。缓存中数据的有效期一旦过期，就会发生缓存穿透，使请求直接就到达了DBMS

**消费幂等的实现**

解决方案：为消息指定不会重复的唯一性标识。因为MessageID有可能出现重复的情况，所以真正安全的幂等处理，不建议以MessageID作为处理依据。最好的方式是以业务唯一标识作为幂等处理的关键依据，而业务的唯一标识可以通过消息key设置。

#### 消息的堆积与消费延迟

消息处理流程中，如果Consumer的消费速度跟不上Producer的发送速度，MQ中未处理的消息会越来越多，这部分消息就被称为堆积消息。消息出现堆积进而会造成消息的消费延迟。以下场景需要重点关注消息堆积和消费延迟问题：

+ 业务系统上下游能力不匹配造成的持续堆积，且无法自行恢复
+ 业务系统对消息的消费时时性要求较高，即使是短暂的堆积造成的消息延迟也无法接受。

**产生原因分析**

![](/img/RocketMQ_10.png)

Consumer使用长轮询Pull模式消费消息时，分为以下两个阶段：

+ 拉取消息

  Consumer通过长轮询Pull模式批量拉取的方式从服务端获取消息，将拉取到的消息缓存到本地缓冲队列中。对于拉取式消费，在内网环境下会有很高的吞吐量，所以这一阶段一般不会成为消息堆积的瓶颈

+ 消费消息

  Consumer将本地缓存的消息提交到消费线程中，使用业务消费逻辑对消息进行处理，处理完毕后获取到一个结果。这是真正的消息消费过程。此时Consumer的消费能力就完全依赖于消息的消费耗时和消费并发度了。如果由于业务处理逻辑复杂等原因，导致处理单条消息的耗时过长，则整体的消息吞吐量肯定不会高，此时就导致Consumer本地缓冲队列达到上限，停止从服务端拉取消息

**消费耗时**

影响消息处理时长的主要因素是代码逻辑。而代码逻辑中可能会影响处理时长代码主要有两种类型：CPU内部计算型代码和外部I/O操作性代码

通常情况下代码中如果没有复杂的递归和循环的话，内部计算耗时相对外部I/O操作来说几乎可以忽略。所以外部I/O型代码是影响消息处理时长的主要症结所在

**消费并发度**

一般情况下，消费者端的消费并发度由单节点线程数和节点数量共同决定的，其值为单节点线程数*节点数。不过通常要优先调整单节点的线程数，若单机硬件资源达到了上限，则需要通过横向扩展来提高消费并发度。

对于普通消息、延时消息、事务消息，并发度计算都是单节点线程数*节点数

对于顺序消息，并发度等于Topic的Queue分区数量

**单机线程数计算**

理想环境下单节点的最优线程数计算模型为：C * (T1 + T2) / T1

+ C：CPU内核数
+ T1：CPU内部逻辑耗计算耗时
+ T2：外部IO操作耗时

#### 消息的清理

消息被消费过后不会被清理掉

消息是被顺序存储在commitlog文件的，且消息大小不定长，所以消息的清理是不可能以消息为单位进行清理的，而是以commitlog文件为单位进行清理的。否则会急剧下降清理效率，并实现逻辑复杂。

commitlog文件存在一个过期时间，默认为72小时，除了用户手动清理外，在以下情况也会被自动清理，无论文件中的消息是否被消费过：

+ 文件过期，其达到清理时间点（默认为凌晨四点）后，自动清理过期文件
+ 文件过期，且磁盘空间占用率已达过期清理警戒线（默认75%）后，无论是否达到清理时间点，都会自动清理过期文件
+ 磁盘占用率达到清理警戒线（默认85%）后，开始按照设定好的规则清理文件，无论是否过期。默认会从最老的文件开始清理
+ 磁盘占用率达到系统的危险警戒线（默认90%）后，Broker将拒绝消息写入

注意：

+ 对于RocketMQ系统来说，删除一个1G大小的文件，是一个压力巨大的IO操作。在删除过程中，系统性能会骤然下降。所以其默认清理时间点为凌晨4点。也正因为如此，我们要保障磁盘空间的空闲率，不要使系统在其他时间点删除commitlog文件
+ 官方建议RocketMQ服务的Linux文件系统采用ext4。因为对于文件删除操作，ext4要比ext3性能更好

### RocketMQ应用

#### 普通消息

消息发送分类

+ 同步发送消息

  同步发送消息是指，Producer发出一条消息后，会在收到MQ返回的ACK后才发下一条消息。该方式的消息可靠性最高，但消息发送效率太低。

+ 异步发送消息

  异步发送消息是指，Producer发出消息后无需等待MQ返回ACK，直接发送下一条消息。该方式的消息可靠性可以得到保障，消息发送效率也可以。

+ 单向发送消息

  单向发送消息是指，Producer仅负责发送消息，不等待，不处理MQ的ACK。该发送方式时MQ也不返回ACK。该方式的消息发送效率最高，但消息可靠性较差。

#### 顺序消息

顺序消息指的是严格按照消息的发送顺序进行消费的消息

默认情况下生产者会把消息以Round Robin轮询方式发送到不同的Queue分区队列；而消费消息时会从多个Queue上拉取消息，这种情况下发送和消费是不能保证顺序的。如果将消息仅发送到同一个Queue中，消费时也只从这个Queue上拉取消息，就严格保证了消息的顺序性。

**有序性分类**

+ 全局有序

  当发送和消费参与的Queue只有一个时所保证的有序是整个Topic中消息的顺序，称为全局有序

+ 分区有序

  如果有多个Queue参与，其仅可保证在该Queue分区队列上的消息顺序，则称为分区有序

  如何实现Queue的选择？在定义Producer时我们可以指定消息队列选择器，而这个选择器是我们自己实现了MessageQueueSelector接口定义的。

  在定义选择器的选择算法时，一般需要使用选择key。这个选择key可以是消息key，也可以是其他数据。但无论谁做选择key，都不能重复，都是唯一的。

  一般性的选择算法是，让选择key（或hash值）与该Topic包含的Queue的数量取模，其结果即为选择出的Queue的QueueId

  取模算法存在一个问题：不同选择key与Queue数量取模结果可能会是相同的，即不同选择key的消息可能出现在相同的Queue，即同一个Consumer可能会消费到不同选择key的消息。这个问题如何解决？一般性的做法是从消息中获取到选择key，对其进行判断。若是当前Consumer需要消费的消息，则直接消费，否则，什么也不做

#### 延时消息

当消息写入到Broker后，在指定的时长后才可被消费处理的消息，称为延时消息。

采用RocketMQ的延时消息可以实现定时任务的功能，而无需使用定时器。

**延时等级**

延时消息的延迟时长不支持随意时长的延迟，是通过特定的延迟等级来指定的。延迟等级定义在RocketMQ服务端的MessageStoreConfig类中

messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h"

若指定的延时等级为3，则表示延时10s，即延迟等级是从1开始计数的

如果需要自定义延时等级，可以通过在broker加载的配置中新增如下配置（例如增加一天这个等级1d）。配置文件在RocketMQ安装目录下的conf目录中。

messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h 1d"

**延时消息实现原理**

**1.修改消息**

![](/img/RocketMQ_11.png)

Producer将消息发送到Broker后，Broker会首先将消息写入到commitlog文件，然后需要将其分发到相应的consumequeue。不过，在分发之前，系统会先判断消息中是否带有延时等级。若没有，则直接正常分发；若有则需要经历一个复杂的过程：

+ 修改消息的Topic为SCHEDULE_TOPIC_XXXX

+ 根据延时等级，在consumequeue目录中SCHEDULE_TOPIC_XXXX主题下创建出相应的queueId目录与consumequeue文件（如果没有这些文件的话）

  延迟等级delayLevel与queueId的对应关系为queueId = delayLevel - 1

  需要注意，在创建queueId目录时，并不是一次性地将所有延迟等级对应的目录全部创建完成，而是用到哪个延迟等级创建哪个目录

+ 修改消息索引单元内容。索引单元中的Message Tag HashCode部分原本存放的是消息的Tag的hash值。现修改为消息的投递时间。投递时间是指该消息被重新修改为原Topic后再次被写入到commitlog中的时间。投递时间 = 消息存储时间 + 延迟等级时间。消息存储时间指的是消息被发送到Broker时的时间戳。

+ 将消息索引写入到SCHEDULE_TOPIC_XXXX主题下相应的consumequeue中

**2.投递消息**

Broker内部有一个延迟消息服务类ScheduleMessageService，其会消费SCHEDULE_TOPIC_XXXX中的消息，即按照每条消息的投递时间，将延时消息投递到目标Topic中。不过在投递之前会从commitlog中将原来写入的消息再次读出，并将其原来的延时等级设置为0，即原消息变为一条不延迟的普通消息。然后再次将消息投递到目标Topic中

ScheduleMessageService在Broker启动时，会创建并启动一个定时器Timer，用于执行相应的定时任务。系统会根据延时等级的个数，定义相应数量的TimerTask，每个TimerTask负责一个延迟等级消息的消费与投递。每个TimerTask都会检测相应Queue队列的第一条消息是否到期。若第一条消息未到期，则后面的所有消息更不会到期；若第一条消息到期了，则将该消息投递到目标Topic

**3.将消息重新写入commitlog**

延迟消息服务类ScheduleMessageService将延迟消息再次发送给了commitlog，并再次形成新的消息索引条目，分发到相应的queue。

#### 事务消息

**事务消息**

RocketMQ提供了类似X/Open XA的分布式事务功能，通过事务消息能达到分布式事务的最终一致。XA是一种分布式事务解决方案。

**半事务消息**

暂不能投递的消息，发送方已经成功地将消息发送到了Broker，但是Broker未收到最终确认指令，此时该消息被标记成暂不能投递状态，即不能被消费者看到。处于该种状态下的消息即半事务消息。

**本地事务状态**

Producer回调操作执行的结果为本地事务状态，其会发送给TC，而TC会再发送给TM。TM会根据TC发送来的本地事务状态来决定全局事务确认指令。

**消息回查**

消息回查，即重新查询本地事务的执行状态。

**RocketMQ中的消息回查设置**

broker加载的配置文件中设置：

+ transactionTimeout=20，指定TM在20s内应将最终确认状态发送给TC，否则引发消息回查。默认为60s
+ transactionCheckMax=5，指定最多回查5次，超过后将丢弃消息并记录错误日志。默认15次
+ transactionCheckInterval=10，指定设置的多次消息回查的时间间隔为10s。默认为60s

#### 批量消息

**批量发送消息**

生产者进行消息发送时可以一次发送多条消息，这可以大大提升Producer的发送效率。不过需要注意以下几点：

+ 批量发送的消息必须具有相同的Topic
+ 批量发送的消息必须具有相同的刷盘策略
+ 批量发送的消息不能是延时消息与事务消息

默认情况下，一批发送的消息总大小不能超过4MB字节。如果想超出该值，有两种解决方案：

+ 方案一：将批量消息进行拆分，拆分为若干不大于4M的消息集合分多次批量发送

+ 方案二：在Producer端与Broker端修改属性

  Producer端需要在发送之前设置Producer的maxMessageSize属性

  Broker端需要修改其加载的配置文件中的maxMessageSize属性

生产者通过send()方法发送的Message，并不是直接将Message序列化后发送到网络上的，而是通过这个Message生成了一个字符串发送出去的。这个字符串由四部分组成：Topic、消息body、消息日志（占20字节）及用于描述消息的一堆属性key-value。这些属性中包含例如生产者地址、生产时间、要发送的QueueId等。最终写入到Broker中消息单元中的数据都是来自于这些属性。

**批量消费消息**

Consumer的MessageListenerConcurrently监听接口的consumeMessage()方法的第一个参数为消息列表，但默认情况下每次只能消费一条消息。若要使其一次可以消费多条消息，则可以通过修改Consumer的consumeMessageBatchMaxSize属性来指定。不过该值不能超过32。因为默认情况下消费者可以拉取的消息最多32条。若要修改一次拉取的最大值，则可以通过修改Consumer的pullBatchSize属性来指定

Consumer的pullBatchSize属性和consumeMessageBatchMaxSize属性不是设置的越大越好

+ pullBatchSize值设置的越大，Consumer每拉取一次需要的时间就越长，且在网络传输出现问题的可能性就越高。若在拉取过程中出现了问题，那么本批次所有消息都要全部重新拉取
+ consumeMessageBatchMaxSize值设置的越大，Consumer的消息并发消费能力越低，且这批被消费的消息具有相同的消费结果。因为consumeMessageBatchMaxSize指定的一批消息只会使用一个线程进行处理，且在消费过程中只要有一个消息处理异常，则这批消息需要全部重新再次消费处理

#### 消息过滤

消费者在进行消息订阅时，除了可以指定要订阅消息的Topic外，还可以对指定Topic中的消息根据指定条件进行过滤，及可以订阅比Topic更加细粒度的消息类型

对于指定Topic消息的过滤有两种过滤方式：

+ Tag过滤

  通过consumer的subscribe()方法指定要订阅消息的tag。如果订阅多个tag的消息，tag间使用||运算符连接

+ SQL过滤

  一种通过特定表达式对事先埋入到消息中的用户属性进行筛选过滤的方式。通过SQL过滤，可以实现对消息的复杂过滤。不过只有使用PUSH模式的消费者才能使用SQL过滤

  SQL过滤表达式中支持多种常量类型与运算符

  支持的常量类型：

  + 数值：比如：123，3.1415
  + 字符：必须用单引号包裹起来，比如：'abc'
  + 布尔：TRUE或FALSE
  + NULL：特殊的常量，表示空

  支持的运算符有：

  + 数值比较：>，>=，<，<=，BETWEEN，=
  + 字符比较：=，<>，IN
  + 逻辑运算：AND，OR，NOT
  + NULL判断：IS NULL或者IS NOT NULL

  默认情况下Broker没有开启消息的SQL过滤功能，需要在Broker加载的配置文件中添加如下属性，以开启该功能

  enablePropertyFilter = true

#### 消息发送重试机制

Producer对发送失败的消息进行重新发送的机制，称为消息发送重试机制，也称为消息重投机制

对于消息重投，需要注意以下几点：

+ 生产者在发送消息时，若采用同步或异步发送方式，发送失败会重试，但oneway消息发送方式发送失败没有重试机制
+ 只有普通消息具有发送重试机制，顺序消息是没有的
+ 消息重投机制可以保证消息尽可能发送成功、不丢失，但可能会造成消息重复。消息重复在RocketMQ是无法避免的问题
+ 消息重复在一般情况下不会发生，当出现消息量大、网络抖动，消息重复就会成为大概率事件
+ producer主动重发、consumer负载变化（发生Rebalance，不会导致消息重复，但可能出现重复消费）也会导致重复消息
+ 消息重复无法避免，但要避免消息的重复消费
+ 避免消息重复消费的解决方案是，为消息添加唯一标识，使消费者对消息进行消费判断来避免重复消费
+ 消息发送重试有三种策略可以选择：同步发送失败策略、异步发送失败策略、消息刷盘失败策略

**同步发送失败策略**

对于普通消息，消息发送默认采用round-robin策略来选择所发送到的队列。如果发送失败，默认重试2次。但在重试时是不会选择上次发送失败的Broker，而是选择其他Broker。当然，若只有一个Broker且也只能发送到该Broker，但会尽量发送到该Broker上的其他Queue

同时，Broker还具有失败隔离的功能，使Producer尽量选择未发生过失败的Broker作为目标Broker。

如果超过重试次数，则抛出异常，由Producer去保证消息不丢。当然当生产者出现RemotingException、MQClientException和MQBrokerException时，Producer会自动重投消息

**异步发送失败策略**

异步发送失败重试时，异步重试不会选择其他broker，仅在同一个broker上做重试，所以该策略无法保证消息不丢

**消息刷盘失败策略**

消息刷盘超时（Master或slave）或slave不可用（slave在做数据同步时向master返回状态非SEND_OK）时，，默认是不会将消息尝试发送到其他broker的。不过对于重要的消息可以通过在Broker配置文件设置retryAnotherBrokerWhenNotStoreOK属性为true开启。

#### 消息消费重试机制

**顺序消息的消费重试**

对于顺序消息，当Consumer消费消息失败后，为了保证消息的顺序性，其会自动不断地进行消息重试，直到消费成功。消息重试默认间隔时间为1000毫秒，其取值范围为[10, 30000]毫秒。重试期间应用会出现消息消费被阻塞的情况。

由于对顺序消息的重试是无休止的，不间断的，直至消费成功，所以对于顺序消息的消费，务必要保证应用能够及时监控并处理消费失败的情况，避免消费被永久性阻塞。

**无序消息的消费重试**

对于无序消息（普通消息、延时消息、事务消息），当Consumer消费消息失败时，可以通过设置返回状态达到消息重试的效果。不过需要注意，无序消息的重试只对集群消费方式生效，广播消费方式不提供失败重试特性。即对于广播消费，消费失败后，失败消息不再重试，继续消费后续消息。

**消费重试次数与间隔**

对于无序消息集群消费下的重试消费，每条消息默认最多重试16次，但每次重试的间隔时间是不同的，会逐渐变长。每次重试的间隔时间如下表

| 重试次数 | 与上次重试的间隔时间 | 重试次数 | 与上次重试的间隔时间 |
| -------- | -------------------- | -------- | -------------------- |
| 1        | 10s                  | 9        | 7m                   |
| 2        | 30s                  | 10       | 8m                   |
| 3        | 1m                   | 11       | 9m                   |
| 4        | 2m                   | 12       | 10m                  |
| 5        | 3m                   | 13       | 20m                  |
| 6        | 4m                   | 14       | 30m                  |
| 7        | 5m                   | 15       | 1h                   |
| 8        | 6m                   | 16       | 2h                   |

若16次重试后仍然失败，则将消息投递到死信队列

修改消费重试次数consumer.setMaxReconsumeTimes(20);

对于修改过的重试次数，将按照以下策略执行：

+ 若修改值小于16，则按照指定间隔进行重试
+ 若修改值大于16，则超过16次的重试时间间隔均为2小时

对于Consumer Group，若仅修改了一个Consumer的消费的重试次数，则会应用到该group中所有其他consumer实例

**重试队列**

对于需要重试消费的消息，并不是Consumer在等待了指定时长后再次去拉取原来的消息进行消费，而是将这些需要重试消费的消息放入到了一个特殊Topic的队列中，而后进行再次消费的。这个特殊的队列就是重试队列。

当出现需要进行重试消费的消息时，broker会为每个消费组都设置一个topic名称为%RETRY%consumerGroup@consumerGroup的重试队列。

这个重试队列是针对消费者组的，而不是针对每个topic设置的（一个topic的消息可以让多个消费者组进行消费，所以会为这些消费者组各创建一个重试队列）

只有当出现需要进行重试消费的消息时，才会为该消费者组创建重试队列

Broker对于重试消息的处理是通过延时消息实现的。先将消息保存到SCHEDULE_TOPIC_XXXX延迟队列中，延迟时间到后，会将消息投递到%RETRY%consumerGroup@consumerGroup重试队列中

**消费重试配置方式**

集群消费方式下，消息消费失败后若希望消费重试，则需要在消息监听器接口的实现中明确进行如下三种方式之一的配置：

+ 返回ConsumeConcurrentlyStatus.RECONSUME_LATER（推荐）
+ 返回Null
+ 抛出异常

**消费不重试配置方式**

集群消费方式下，消息消费失败后若不希望消费重试，则在捕获到异常后同样也返回与消费成功后的相同的结果，即ConsumeConcurrentlyStatus.CONSUME_SUCCESS，则不进行消费重试

#### 死信队列

当一条消息初次消费失败，消息队列会自动进行消费重试，达到最大重试次数后，若消费依然失败，则表明消费者在正常情况下无法正确的消费该消息，此时，消息队列不会立刻将消息丢弃，而是将其发送到该消费者对应的特殊队列中。这个队列就是死信队列（Dead-Letter Queue，DLQ），而其中的消息则称为死信消息（Dead-Letter Message，DLM）

死信队列是用于处理无法被正常消费的消息

**死信队列特征**

+ 死信队列中的消息不会再被消费者正常消费，即DLQ对于消费者是不可见的
+ 死信存储有效期与正常消息相同，均为3天（commitlog文件的过期时间），3天后会被自动删除
+ 死信队列就是一个特殊的Topic，名称为%DLQ%consumerGroup@consumerGroup
+ 如果一个消费者组未产生死信消息，则不会为其创建死信队列

**死信消息的处理**

实际上，当一条消息进入死信队列，就意味着系统中某些地方出现了问题，从而导致消费者无法正常消费该消息，不如代码中原本就存在Bug。因此，对于死信消息，通常需要开发人员进行特殊处理。最关键的步骤是要排查可疑因素，解决代码中可能存在的bug，然后再将原来的死信消息再次进行投递消息

## Kafka

### 概述

kafka传统定义：kafka是一个分布式的基于发布订阅模式的消息队列，主要应用于大数据实时处理领域。

Kafka最新定义：Kafka是一个开源的分布式事件流平台，被数千家公司用于高性能数据管道、流分析、数据集成和关键任务应用。

**应用场景**

+ 缓冲/削峰：有助于控制和优化数据流经过系统的速度，解决生产消息和消费消息的处理速度不一致的情况
+ 解耦：允许你独立的扩展或修改两边的处理过程，只要确保它们遵守同样的接口约束
+ 异步通信：允许用户把一个消息放入队列，但并不立即处理它，然后再需要的时候再去处理他们

**消息队列的两种模式**

+ 点对点模式

  消费者主动拉取数据，消息收到后清除消息

+ 发布/订阅模式

  可以有多个topic主题（浏览、点赞、收藏、评论等）。消费者消费数据之后，不删除数据。每个消费者相互独立，都可以消费到数据

**基础架构**

![](/img/Kafka_1.png)

![](/img/Kafka_2.png)

### Kafka生产者

#### 生产者消息发送流程

在消息发送的过程中，涉及到了两个线程：main线程和Sender线程。在main线程中创建了一个双端队列RecordAccumulator。main线程将消息发送给RecordAccumulator，Sender线程不断从RecordAccumulator中拉取消息发送到Kafka Broker

![](/img/Kafka_3.png)

#### 异步发送

**普通异步发送**

```java
//配置
Properties properties = new Properties();

//连接集群
properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092,hadoop103:9092");

//指定对应的key和value的序列化类型
properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

//创建Kafka生产者对象
KafkaProduer<String, String> kafkaProduer = new KafkaProduer<>(properties);

//发送数据
for (int i = 0; i < 5; i++) {
 kafkaProducer.send(new ProducerRecord<>("first", "atguigu" + i));   
}

//关闭资源
kafkaProducer.close();
```

**带回调函数的异步发送**

回调函数会在producer收到ack时调用，为异步调用，该方法有两个参数，分别是元数据信息（RecordMetadata）和异常信息（Exception），如果Exception为null，说明消息发送成功，如果Exception不为null，说明消息发送失败

```java
//配置
Properties properties = new Properties();

//连接集群
properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092,hadoop103:9092");

//指定对应的key和value的序列化类型
properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

//创建Kafka生产者对象
KafkaProduer<String, String> kafkaProduer = new KafkaProduer<>(properties);

//发送数据
for (int i = 0; i < 5; i++) {
 kafkaProducer.send(new ProducerRecord<>("first", "atguigu" + i), new Callback() {
     @Override
     public void onCompletion(RecordMetadata metadata, Exception exception) {
         if (exception == null) {
             System.out.println("主题：" + metadata.topic() + "分区：" + metadata.partition());
         }
     }
 });   
}

//关闭资源
kafkaProducer.close();
```

#### 同步发送

```java
//配置
Properties properties = new Properties();

//连接集群
properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092,hadoop103:9092");

//指定对应的key和value的序列化类型
properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

//创建Kafka生产者对象
KafkaProduer<String, String> kafkaProduer = new KafkaProduer<>(properties);

//发送数据
for (int i = 0; i < 5; i++) {
 kafkaProducer.send(new ProducerRecord<>("first", "atguigu" + i)).get();   
}

//关闭资源
kafkaProducer.close();
```

#### 生产者分区

**分区好处**

+ 便于合理使用存储资源，每个Partition在一个Broker上存储，可以把海量的数据按照分区切割成一块块数据存储在多台Broker上。合理控制分区的任务，可以实现负载均衡的效果
+ 提高并行度，生产者可以以分区为单位发送数据；消费者可以以分区为单位进行消费数据

**分区策略**

默认的分区器DefaultPartitioner

![](/img/Kafka_4.png)

**自定义分区器**

定义类实现Partitioner接口，重写partition()方法

```java
public class MyPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        //获取数据
        String msgValues = value.toString();
        
        int partition;
        
        if (msgValues.contains("atguigu")) {
            partition = 0;
        } else {
            partition = 1;
        }
        
        return partition;
    }
}

//关联自定义分区器
properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, MyPartitioner.class.getName());
```

#### 生产经验

**生产者如何提高吞吐量**

+ batch.size：批次大小，默认16k
+ linger.ms：等待时间，修改为5-100ms
+ compression.type：压缩snappy
+ RecordAccumulator：缓冲区大小，修改为64m

```java
//配置
Properties properties = new Properties();

//连接集群
properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092,hadoop103:9092");

//指定对应的key和value的序列化类型
properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

//缓冲区大小,默认32m
properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);

//批次大小,默认16k
properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);

//linger.ms
properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);

//压缩
properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
    
//创建Kafka生产者对象
KafkaProduer<String, String> kafkaProduer = new KafkaProduer<>(properties);

//发送数据
for (int i = 0; i < 5; i++) {
 kafkaProducer.send(new ProducerRecord<>("first", "atguigu" + i)).get();   
}

//关闭资源
kafkaProducer.close();
```

**数据可靠性**

ack应答级别：

+ 0：生产者发送过来的数据，不需要等数据落盘应答，可靠性差，效率高
+ 1：生产者发送过来的数据，Leader收到数据后应答，可靠性中等，效率中等
+ -1（all）：生产者发送过来的数据，Leader和ISR队列里面所有节点收齐数据后应答，可靠性高，效率低

Leader收到数据，所有Follower都开始同步数据，但有一个Follower，因为某种故障，迟迟不能与Leader进行同步，那这个问题怎么解决？

Leader维护了一个动态的in-sync replica set（ISR）意为和Leader保持同步的Follower+Leader集合（leader:0, isr:0,1,2）。如果Follower长时间未向Leader发送通信请求或同步数据，则该Follower将被踢出ISR。该时间阈值由replica.lag.time.max.ms参数设定，默认30s。例如2超时，（leader:0, isr:0,1）。这样就不用等长期联系不上或者已经故障的节点。

如果分区副本设置为1个，或者ISR里应答的最小副本数量（min.insync.replicas默认为1）设置为1，和ack=1的效果是一样的，仍然有丢数的风险

数据完全可靠条件 = ACK级别设置为-1 + 分区副本大于等于2 + ISR里应答的最小副本数量大于等于2

在生产环境中，acks=0很少使用，acks=1，一般用于传输普通日志，允许丢个别数据；acks=-1，一般用于传输和钱相关的数据，对可靠性要求比较高的场景

```java
//acks,默认all
properties.put(ProducerConfig.ACKS_CONFIG, "1");

//重试次数,默认2147483647
properties.put(ProducerConfig.RETRIES_CONFIG, 3);
```

**数据去重**

数据传递语义

+ 至少一次（At Least Once）= ACK级别设置为-1 + 分区副本大于等于2 + ISR里应答的最小副本数量大于等于2
+ 最多一次（At Most Once）= ACK级别设置为0
+ 精确一次：即不重复也不丢失

至少一次可以保证数据不丢失，但是不能保证数据不重复

最多一次可以保证数据不重复，但是不能保证数据不丢失

**幂等性**

幂等性就是指Producer不论向Broker发送多少次重复数据，Broker端都只会持久化一条，保证了不重复

精确一次 = 幂等性 + 至少一次

重复数据的判断标准：具有<PID, Partition, SeqNumber>相同主键的消息提交时，Broker只会持久化一条。其中PID是Kafka每次重启都会分配一个新的；Partition表示分区号；SeqNumber是单调自增的

所以幂等性只能保证是在单分区单会话内不重复

如何使用幂等性？

开启参数enable.idempotence，默认为true，false关闭

**事务**

开启事务必须开启幂等性

![](/img/Kafka_5.png)

```java
//Kafka事务一共有5个API

//1.初始化事务
void initTransaction();

//2.开启事务
void beginTransaction() throws ProducerFencedException;

//3.在事务内提交已经消费的偏移量（主要用于消费者）
void sendOffsetToTransaction(Map<TopicPartition, OffsetAndMetadata> offsets, String consumerGroupId) throws ProducerFencedException;

//4.提交事务
void commitTransaction() throws ProducerFencedException;

//5.放弃事务（类似于回滚事务的操作）
void abortTransaction() throws ProducerFencedException;
```

**数据有序**

单分区内，有序（有条件的，详见下节）；

多分区，分区与分区间无序

**数据乱序**

kafka在1.x版本之前保证数据单分区有序，条件如下：

max.in.flight.requests.per.connection=1：每个broker最多缓存一个请求，（不需要考虑是否开启幂等性）

kafka在1.x及以后版本保证数据单分区有序，条件如下：

+ 未开启幂等性：max.in.flight.requests.per.connection需要设置为1

+ 开启幂等性：max.in.flight.requests.per.connection需要设置小于等于5

  原因说明：因为在kafka1.x以后，启用幂等性后，kafka服务端会缓存producer发来的最近的5个request的元数据，故无论如何，都可以保证最近5个request的数据都是有序的

### Kafka Broker

#### 工作流程

![](/img/Kafka_6.png)

#### 节点服役和退役

**服役新节点**

1. 新节点准备

   新增kafka节点，修改broker.id，启动kafka：bin/kafka-server-start.sh -daemon ./config/server.properties

2. 执行负载均衡操作

   1. 创建一个要均衡的主题

      ```shell
      vim topics-to-move.json
      
      {
      	"topics": [
      		{"topic" : "first"}
      	],
      	"version": 1
      }
      ```

   2. 生成一个负载均衡的计划

      ```shell
      bin/kafka-reassign-partitions.sh --bootstrap-server hadoop102:9092 --topics-to-move-json-file topic-to-move.json --broker-list "0,1,2,3" --generate
      ```

   3. 创建副本计划（所有副本存储在broker0，broker1，broker2，broker3中）

      ```shell
      vim increase-replication-factor.json
      
      文件内容为上述命令执行后的Proposed partition reassignment configuration的内容	
      ```

   4. 执行副本存储计划

      ```shell
      bin/kafka-reassign-partitions.sh --bootstrap-server hadoop102:9092 --reassignment-json-file increase-replication-factor.json --execute
      ```

   5. 验证副本存储计划

      ```shell
      bin/kafka-reassign-partitions.sh --bootstrap-server hadoop102:9092 --reassignment-json-file increase-replication-factor.json --verify
      ```

**退役旧节点**

1. 执行负载均衡操作

   先按照退役一台节点，生成执行计划，然后按照服役时操作流程执行负载均衡

   1. 创建一个要均衡的主题

      ```shell
      vim topics-to-move.json
      
      {
      	"topics": [
      		{"topic" : "first"}
      	],
      	"version": 1
      }
      ```

   2. 创建执行计划

      ```shell
      bin/kafka-reassign-partitions.sh --bootstrap-server hadoop102:9092 --topics-to-move-json-file topic-to-move.json --broker-list "0,1,2" --generate
      ```

   3. 创建副本计划（所有副本存储在broker0，broker1，broker2中）

      ```shell
      vim increase-replication-factor.json
      
      文件内容为上述命令执行后的Proposed partition reassignment configuration的内容	
      ```

   4. 执行副本存储计划

      ```shell
      bin/kafka-reassign-partitions.sh --bootstrap-server hadoop102:9092 --reassignment-json-file increase-replication-factor.json --execute
      ```

   5. 验证副本存储计划

      ```shell
      bin/kafka-reassign-partitions.sh --bootstrap-server hadoop102:9092 --reassignment-json-file increase-replication-factor.json --verify
      ```

2. 执行停止命令

   ```shell
   bin/kafka-server-stop.sh
   ```

#### Kafka副本

**副本基本信息**

1. Kafka副本作用：提高数据可靠性
2. Kafka默认副本1个，生产环境一般配置为2个，保证数据可靠性；太多副本会增加磁盘存储空间，增加网络上数据传输，降低效率
3. Kafka中副本分为Leader和Follower。Kafka生产者只会把数据发往Leader，然后Follower找Leader进行同步数据
4. Kafka分区中的所有副本统称为AR（Assigned Replicas）

AR = ISR + OSR

ISR表示和Leader保持同步的Follower集合。如果Follower长时间未向Leader发送通信请求或同步数据，则该Follower将被踢出ISR。该时间阈值由replica.lag.time.max.ms参数设定，默认30s。Leader发生故障之后，就会从ISR中选举新的Leader。

OSR表示Follower与Leader副本同步时，延迟过多的副本。

**Leader选举流程**

![](/img/Kafka_6.png)

**Leader和Follower故障处理细节**

LEO（Log End Offset）：每个副本最后一个offset，LEO其实就是最新的offset + 1

HW（High Watermark） ：所有副本中最小的LEO

Follower故障

1. Follower发生故障后会被临时踢出ISR
2. 这个期间Leader和Follower继续接收数据
3. 待该Follower恢复后，Follower会读取本地磁盘记录的上次HW，并将log文件高于HW的部分截取掉，从HW开始向Leader进行同步。
4. 等该Follower的LEO大于等于该Partition的HW，即Follower追上Leader之后，就可以重新加入ISR了

Leader故障

1. Leader发生故障之后，会从ISR中选出一个新的Leader
2. 为保证多个副本之间的数据一致性，其余的Follower会先将各自的log文件高于HW的部分截掉，然后从新的Leader同步数据

注意：这只能保证副本之间数据的一致性，并不能保证数据不丢失或者不重复

**Leader Partition自动平衡**

正常情况下，Kafka本身会自动把Leader Partition均匀分散到各个机器上，来保证每台机器的读写吞吐量都是均匀的。但是如果某些Broker宕机，会导致Leader Partition过于集中在其他少部分几台broker上，这会导致少数几台broker的读写请求压力过高，其他宕机的broker重启之后都是follower partition，读写请求很低，造成集群负载不均衡。

+ auto.leader.rebalance.enable，默认为true，自动Leader Partition平衡
+ leader.imbalance.per.broker.percentage，默认是10%。每个broker允许的不平衡的leader的比率。如果每个broker超过了这个值，控制器会触发leader的平衡
+ leader.imbalance.check.interval.seconds，默认300s。检查leader负载是否平衡的间隔时间

#### 文件存储

**文件存储机制**

Topic是逻辑上的概念，而partition是物理上的概念，每个partition对应于一个log文件，该log文件中存储的就是Producer生产的数据。Producer生产的数据会被不断追加到该log文件末端，为防止log文件过大导致数据定位效率低下，Kafka采取了分片和索引机制，将每个partition分为多个segment。每个segment包括：.index文件、.log文件、.timeindex等文件。这些文件位于一个文件夹下，该文件夹的命名规则为：topic名称+分区序号，例如：first-0

index和log文件以当前segment的第一条消息的offset命名

topic文件存储在kafka/datas下（first-0、first-1）

通过工具查看index和log信息：

kafka-run-class.sh kafka.tools.DumpLogSegments --files ./00000000000000000000.index

kafka-run-class.sh kafka.tools.DumpLogSegments --files ./00000000000000000000.log

![](/img/Kafka_7.png)

**文件清理策略**

Kafka中默认的日志保持时间为7天，可以通过调整如下参数修改保存时间

+ log.retention.hours，最低优先级小时，默认7天
+ log.retention.minutes，分钟
+ log.retention.ms，最高优先级毫秒
+ log.retention.check.interval.ms，负责设置检查周期，默认5分钟

日志一旦超过了设置的时间，怎么处理呢？Kafka中提供的日志清理策略有delete和compact两种

+ delete日志删除：将过期数据删除

  log.cleanup.policy = delete 所有数据启用删除策略

  1. 基于时间：默认打开。以segment中所有记录中的最大时间戳作为该文件的时间戳

  2. 基于大小：默认关闭。超过设置的所有日志总大小，删除最早的segment。

     log.retention.bytes，默认等于-1，表示无穷大

+ compact日志压缩

  对于相同key的不同value值，只保留最后一个版本

  log.cleanup.policy = compact 所有数据启用压缩策略

  ![](/img/Kafka_8.png)

  压缩后的offset可能是不连续的，比如上图中没有6，当从这些offset消费消息时，将会拿到比这个offset大的offset对应的消息，实际上会拿到offset为7的消息，并从这个位置开始消费

  这种策略只适合特殊场景，比如消息的key是用户ID，value是用户的资料，通过这种压缩策略，整个消息集里就保存了所有用户最新的资料。

#### 高效读写数据

1. Kafka本身是一个分布式集群，可以采用分区技术，并行度高

2. 读数据采用稀疏索引，可以快速定位要消费的数据

3. 顺序写磁盘

   kafka的producer生产数据，要写入到log文件中，写的过程是一直追加到文件末端，为顺序写。官网有数据表明，同样的磁盘，顺序写能到600M/s，而随机写只有100K/s。这与磁盘的机械结构有关，顺序写之所以快，是因为其省去了大量磁头寻址的时间

4. 页缓存+零拷贝技术

   零拷贝：kafka的数据加工处理操作交由Kafka生产者和消费者处理。Kafka Broker应用层不关心存储的数据，所以就不用走应用层，传输效率高

   PageCache页缓存：kafka重度依赖底层操作系统提供的PageCache功能。当上层有写操作时，操作系统只是将数据写入PageCache。当读操作发生时，先从PageCache中查找，如果找不到，再去磁盘中读取。实际上PageCache是把尽可能多的空闲内层都当作了磁盘缓存来使用。

   ![](/img/Kafka_9.png)

### Kafka消费者

#### Kafka消费方式

+ pull（拉）模式：consumer采用从broker中主动拉取数据，Kafka采用这种方式。pull模式不足之处是，如果Kafka没有数据，消费者可能会陷入循环中，一直返回空数据
+ push（推）模式：Kafka没有采取这种方式，因为由broker决定消息发送速率，很难适应所有消费者的消费速率

#### Kafka消费者工作流程

![](/img/Kafka_10.png)

**消费者组原理**

Consumer Group（CG）：消费者组，由多个consumer组成。形成一个消费者组的条件是所有消费者的groupid相同。

+ 消费者组内每一个消费者负责消费不同分区的数据，一个分区只能由一个组内消费者消费
+ 消费者组之间互不影响。所有的消费者都属于某个消费者组，即消费者组是逻辑上的一个订阅者。

+ 如果向消费者组中添加更多的消费者，超过主题分区数量，则有一部分消费者就会闲置，不会接收任何消息

**消费者组初始化流程**

coordinator：辅助实现消费者组的初始化和分区的分配

coordinator节点选择 = groupid的hashcode值 % 50 （_consumer_offsets的分区数量）

例如groupid的hashcode值为1，1%50=1，那么_consumer_offsets主题的1号分区，在哪个broker上，就选择这个节点的coordinator作为这个消费者组的老大。消费者组下的所有的消费者提交offset的时候就往这个分区去提交offset

![](/img/Kafka_11.png)

**消费者组详细消费流程**

![](/img/Kafka_12.png)

#### 消费者API

注意：在消费者API代码中必须配置消费者组id。命令行启动消费者不填写消费者组id会被自动填写随机的消费者组id

**独立消费者（订阅主题）**

```java
//配置
Properties properties = new Properties();

//连接
properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092,hadoop103:9092");

//反序列化
properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

//配置消费者组id
properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test");

//创建一个消费者
KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);

//订阅主题
ArrayList<String> topics = new ArrayList<>();
topics.add("first");
kafkaConsumer.subscribe(topics);

//消费数据
while(true) {
    ConsumerRecords<String, String> consumerRecords = kafkaConsumer.pull(Duration.ofSeconds(1));
    for(ConsumerRecords<String, String> consumerRecord : consumerRecords) {
        System.out.println(consumerRecord);
    }
}
```

**独立消费者（订阅分区）**

```java
//配置
Properties properties = new Properties();

//连接
properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "hadoop102:9092,hadoop103:9092");

//反序列化
properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

//配置消费者组id
properties.put(ConsumerConfig.GROUP_ID_CONFIG, "test");

//创建一个消费者
KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);

//订阅主题对应的分区
ArrayList<TopicPartition> topicPartitions = new ArrayList<>();
topicPartitions.add(new TopicPartition("first", 0));
kafkaConsumer.assign(topicPartitions);

//消费数据
while(true) {
    ConsumerRecords<String, String> consumerRecords = kafkaConsumer.pull(Duration.ofSeconds(1));
    for(ConsumerRecords<String, String> consumerRecord : consumerRecords) {
        System.out.println(consumerRecord);
    }
}
```

#### 分区的分配以及再平衡

一个consumer group中有多个consumer组成，一个topic有多个partition组成，到底由哪个consumer来消费哪个partition的数据

Kafka有四种主流的分区分配策略：Range、RoundRobin、Sticky、CooperativeSticky。

可以通过配置参数partition.assignment.strategy，修改分区的分配策略。默认策略是Range + CooperaticeSticky。Kafka可以同时使用多个分区分配策略

+ Range

  Range是对每个topic而言的。首先对同一个topic里面的分区按照序号进行排序，并对消费者按照字母顺序进行排序。

  通过partitions数/consumer数来决定每个消费者应该消费几个分区。如果除不尽，那么前面几个消费者将会多消费一个分区

  如果有N多个topic，前面的消费者将多消费N个分区，容易产生数据倾斜

+ RoundRobin

  RoundRobin针对集群中所有Topic而言。RoundRobin轮询分区策略，是把所有的partition和所有的consumer都列出来，然后按照hashcode进行排序，最后通过轮询算法来分配partition给到各个消费者

+ Sticky

  粘性分区定义：可以理解为分配的结果带有粘性的。即在执行一次新的分配之前，考虑上一次分配的结果，尽量少的调整分配的变动，可以节省大量的开销

  粘性分区是Kafka从0.11.x版本开始引入这种分配策略，首先会尽量均衡的放置分区到消费者上面，在出现同一消费者组内消费者出现问题的时候，会尽量保持原有分配的分区不变化

#### offset位移

![](/img/Kafka_13.png)

_consumer_offsets主题里面采用key和value的方式存储数据。key是group.id + topic + 分区号，value就是当前offset的值。每隔一段时间，Kafka内部就会对这个topic进行compact，也就是每个group.id + topic + 分区号就保留最新数据

在配置文件config/consumer.properties中添加配置exclude.internal.topics=false，默认是true，表示不能消费系统主题。为了查看该系统主题数据，所以该参数修改为false。

查看消费者消费主题_consumer_offsets

`bin/kafka-console-consumer.sh --topic _consumer_offsets --bootstrap-server hadoop102:9092 --consumer.config config/consumer.properties --formatter "kafka.coordinator.group.GroupMetadataManager\$OffsetsMessageFormatter" --from-beginning`

**自动提交offset**

+ enable.auto.commit：是否开启自动提交offset功能，默认是true
+ auto.commit.interval.ms：自动提交offset的时间间隔，默认是5s

![](/img/Kafka_14.png)

**手动提交offset**

手动提交offset的方法有两种：分别是commitSync（同步提交）和commitAsync（异步提交）。两者的相同点是，都会将本次提交的一批数据最高的偏移量提交；不同点是，同步提交阻塞当前线程，一直到提交成功，并且会自动失败重试（由不可控因素导致，也会出现提交失败）；而异步提交则没有失败重试机制，故有可能提交失败。

**指定offset消费**

auto.offset.reset = earliest|latest|none，默认是latest

当Kafka中没有初始偏移量（消费者组第一次消费）或服务器上不再存在当前偏移量时（例如该数据已被删除），该怎么办

+ earliest：自动将偏移量重置为最早的偏移量，--from-beginning
+ latest（默认值）：自动将偏移量重置为最新偏移量
+ none：如果未找到消费者组的先前偏移量，则向消费者抛出异常

指定offset消费：kafkaConsumer.seek(topicPartition, 600);

**指定时间消费**

```java
//指定位置进行消费
Set<TopicPartition> assignment = kafkaConsumer.assignment();

//保证分区分配方案已经指定完毕
while(assignment.size() == 0) {
    kafkaConsumer.poll(Duration.ofSeconds(1));
    assignment = kafkaConsumer.assignment();
}

//希望把时间转换为对应的offset
HashMap<TopicPartition, Long> topicPartitionLongHashMap = new HashMap<>();

for(TopicPartition topicPartition : assignment) {
    topicPartitionLongHashMap.put(topicPartition, System.currentTimeMillis() - 1 * 24 * 3600 * 1000);
}

Map<TopicPartition, OffsetAndTimestamp> topicPartitionOffsetAndTimestampMap = kafkaConsumer.offsetsForTimes(topicPartitionLongHashMap);

//指定消费的offset
for(TopicPartition topicPartition : assignment) {
    OffsetAndTimestamp offsetAndTimestamp = topicPartitionOffsetAndTimestampMap.get(topicPartition);
    kafkaConsumer.seek(topicPartition, offsetAndTimestamp.offset());
}
```

**漏消费和重复消费**

重复消费：已经消费了数据，但是offset没有提交

漏消费：先提交offset后消费，有可能会造成数据的漏消费

如果想完成Consumer端的精确一次性消费，那么需要Kafka消费端将消费过程和提交offset过程做原子绑定。此时我们需要将Kafka的offset保存到支持事务的自定义介质（例如MySQL）

**数据积压**

1. 如果是Kafka消费能力不足，则可以考虑增加topic的分区数，并且同时提升消费者组的消费者数量，消费者数 = 分区数
2. 如果是下游的数据处理不及时：提高每批次拉取的数量。批次拉取数据过少（拉取数据 / 处理时间 < 生产速度），使处理的数据小于生产的数据，也会造成数据积压

### Kafka-Eagle监控

### Kafka-Kraft模式

kafka-Kraft模式不再依赖zookeeper集群，而是用三台controller节点代替zookeeper，元数据保存在controller中，由controller直接进行Kafka集群管理

这样做的好处有以下几个：

+ Kafka不再依赖外部框架，而是能够独立运行
+ controller管理集群时，不再需要从zookeeper中先读取数据，集群性能上升
+ 由于不依赖zookeeper，集群扩展时不再受zookeeper读写能力限制
+ controller不再动态选举，而是由配置文件规定。这样我们可以有针对性的加强controller节点的配置，而不是像以前一样对随机controller节点的高负载束手无策

**集群部署**

1. 解压一份Kafka安装包

   tar -zxvf kafka_2.12-3.0.0.tgz -C /opt/module

2. 重命名为kafk2

   mv  kafka_2.12-3.0.0/ kafka2

3. 修改/opt/module/kafka2/config/kraft/server.properites配置文件

   ```shell
   #kafka的角色(controller相当于主机、broker节点相当于从机，主机类似zk功能)
   process.roles=broker,controller
   #节点id
   node.id=2
   #controller服务协议别名
   controller.listener.names=CONTROLLER
   #全controller列表
   controller.quorum.voters=2@hadoop102:9093,3@hadoop103:9093,4@hadoop104:9093
   #不同服务器绑定的端口
   listeners=PLAINTEXT://:9092,CONTROLLER://:9093
   #broker服务协议别名
   inter.broker.listener.name=PLAINTEXT
   #broker对外暴露的地址
   advertised.Listeners=PLAINTEXT://hadoop102:9092
   #协议别名到安全协议的映射
   listener.security.protocol.map=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT,SSL:SSL,SASL_PLAINTEXT:SASL_PLAINTEXT,SASL_SSL:SASL_SSL
   #kafka数据存储目录
   log.dirs=/opt/module/kafka2/data
   ```

4. 分发kafka2

   xsync kafka2/

   + 在hadoop103和104上需要对node.id相应改变，值需要和controller.quorum.voters对应
   + 在hadoop103和104上需要根据各自的主机名称，修改对应的advertised.Listeners地址

5. 初始化集群数据目录

   1. 首先生成存储目录唯一ID

      bin/kafka-storage.sh random-uuid

      J7s9e8PPTKOO47PxzI39VA

   2. 用该ID格式化kafka存储目录（三台节点）

      ```shell
      #hadoop102
      bin/kafka-storage.sh format -t J7s9e8PPTKOO47PxzI39VA -c /opt/module/kafka2/config/kraft/server.properties
      
      #hadoop103
      bin/kafka-storage.sh format -t J7s9e8PPTKOO47PxzI39VA -c /opt/module/kafka2/config/kraft/server.properties
      
      #hadoop104
      bin/kafka-storage.sh format -t J7s9e8PPTKOO47PxzI39VA -c /opt/module/kafka2/config/kraft/server.properties
      ```

6. 启动Kafka集群

   ```shell
   #hadoop102
   bin/kafka-server-start.sh -daemon config/kraft/server.properties
   
   #hadoop103
   bin/kafka-server-start.sh -daemon config/kraft/server.properties
   
   #hadoop104
   bin/kafka-server-start.sh -daemon config/kraft/server.properties
   ```

7. 停止kafka集群

   ```shell
   #hadoop102
   bin/kafka-server-stop.sh
   
   #hadoop103
   bin/kafka-server-stop.sh
   
   #hadoop104
   bin/kafka-server-stop.sh
   ```

### 集成SpringBoot

**生产者**

1. 修改SpringBoot核心配置文件application.properties，添加生产者相关信息

   ```properties
   #指定Kafka的地址
   spring.kafka.boostrap-servers=hadoop102:9092,hadoop103:9092,hadoop104:9092
   
   #指定key和value的序列化器
   spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
   spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
   ```

2. 创建controller从浏览器接收数据，并写入指定的topic

   ```java
   @RestController
   public class ProducerController {
       @Autowired
       KafkaTemplate<String, String> kafka;
       
       @RequestMappring("/atguigu")
       public String data(String msg) {
           kafka.send("first", msg);
           return "ok";
       }
   }
   ```


**消费者**

1. 修改SpringBoot核心配置文件application.properties

   ```properties
   #指定Kafka的地址
   spring.kafka.boostrap-servers=hadoop102:9092,hadoop103:9092,hadoop104:9092
   
   #指定key和value的反序列化器
   spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
   spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
   
   #指定消费者组的group_id
   spring.kafka.consumer.group-id=atguigu
   ```

2. 创建类消费kafka中指定topic的数据

   ```java
   public class KafkaConsumer {
       
       @KafkaListener(topic="first")
       public void consumerTopic(String msg) {
           System.out.println("收到的消息：" + msg);
       }
   }
   ```

### Kafka调优

#### 硬件选择

1. 100万日活 * 每人每天产生日志100条 = 1亿（中型公司）

   处理日志速度 1亿条 / （24 * 3600s）= 1155条/s

   一条日志（0.5k - 2k 1k）

   1150条 * 1k/s = 1m/s

   高峰值（中午小高峰8 - 12）：1m/s * 20倍 = 20m/s - 40m/s

2. 购买多少台服务器

   服务器台数 = 2 * （生产者峰值生产速率 * 副本数 / 100） + 1

   ​                    = 2 * （20m/s * 2 / 100） + 1

   ​                    = 3台

3. 磁盘选择

   Kafka按照顺序读写，机械硬盘和固态硬盘顺序读写速度差不多

   1亿条 * 1k = 100g

   100g * 2个副本 * 3天 / 0.7 = 1t

   建议三台服务器总的磁盘大小大于1t

4. 内存选择

   kafka 内存 = 堆内存（kafka内部配置）+ 页缓存（服务器内存）

   + kafka堆内存建议每个节点10g-15g，在kafka-server-start.sh中修改KAFKA_HEAP_OPTS="-Xmx10G -Xms10G"

     查看Kafka进程号：jps

     根据Kafka进程号查看Kafka的GC情况：jstat -gc 2312 1s 10

     根据Kafka进程号查看Kafka的堆内存：jmap -heap 2312

   + 页缓存：页缓存是Linux系统服务器的内存。我们只需要保证一个segment（1g）中25%的数据在内存中就好

     每个节点页缓存大小 = （分区数 * 1g * 25%）/ 节点数。

     建议服务器内存大于等于11G

5. CPU选择

   num.io.threads = 8，负责写磁盘的线程数，这个参数值要占总核数的50%

   num.replica.fetchers = 1，副本拉取线程数，这个参数要占总核数的50%的1/3

   num.network.threads = 3，数据传输线程，这个参数占总核数的50%的2/3

   建议32个cpu core

6. 网络选择

   网络带宽 = 峰值吞吐量 ≈ 20MB/s，选择千兆网卡即可

   100Mbps单位是bit；10M/s单位是byte；1byte = 8bit，100Mbps/8 = 12.5M/s

#### Kafka生产者

![](/img/Kafka_3.png)

| 参数名称                              | 描述                                                         |
| ------------------------------------- | ------------------------------------------------------------ |
| bootstrap.servers                     | 生产者连接集群所需的broker地址清单。例如hadoop102:9092,hadoop103:9092，可以设置一个或者多个，中间用逗号隔开。注意这里并非需要所有的broker地址，因为生产者从给定的broker里查找到其他broker信息 |
| key.serializer和value.serializer      | 指定发送消息的key和value的序列化类型。一定要写全类名         |
| buffer.memory                         | RecordAccumulator缓冲区总大小，默认32m                       |
| batch.size                            | 缓冲区一批数据最大值，默认16k。适当增加该值可以提高吞吐量，但是如果该值设置太大，会导致数据传输延迟增加 |
| linger.ms                             | 如果数据迟迟未达到batch.size，sender等待linger.time之后就会发送数据。单位ms，默认值是0ms，表示没有延迟。生产环境建议该值大小为5-100ms之间 |
| acks                                  | 0：生产者发送过来的数据，不需要等数据落盘应答<br>1：生产者发送过来的数据，Leader收到数据后应答<br>-1（all）：生产者发送过来的数据，Leader和isr队列里面的所有节点收齐数据后应答。默认值是-1 |
| max.in.flight.requests.per.connection | 允许最多没有返回ack的次数，默认为5，开启幂等性要保证该值是1-5的数字 |
| retries                               | 当消息发送出现错误的时候，系统会重发消息。retries表示重试次数。默认是int最大值，2147483647。如果设置了重试，还想保证消息的有序性，需要设置max.in.flight.requests.per.connection=1，否则在重试此失败消息的时候，其他的消息可能发送成功了 |
| retry.backoff.ms                      | 两次重试之间的时间间隔，默认是100ms                          |
| enable.idempotence                    | 是否开启幂等性，默认true，开启幂等性                         |
| compression.type                      | 生产者发送的所有数据的压缩方式。默认是none，也就是不压缩。支持压缩类型：none、gzip、snappy、lz4和zstd |

#### Kafka Broker

![](/img/Kafka_6.png)

| 参数名称                                | 描述                                                         |
| --------------------------------------- | ------------------------------------------------------------ |
| replica.lag.time.max.ms                 | ISR中，如果follower长时间未向Leader发送通信请求或同步数据，则该follower将被踢出ISR。该时间阈值默认30s |
| auto.leader.rebalance.enable            | 默认是true。自动Leader Partition平衡。建议关闭               |
| leader.imbalance.per.broker.percentage  | 默认是10%。每个broker允许的不平衡的leader的比率。如果每个broker超过了这个值，控制器会触发leader平衡 |
| leader.imbalance.check.interval.seconds | 默认值300s。检查leader负载是否平衡的间隔时间                 |
| log.segment.bytes                       | Kafka中log日志是分成一块块存储的，此配置是指log日志划分成块的大小，默认值1G |
| log.index.interval.bytes                | 默认4kb，Kafka里面每当写入了4kb大小的日志（.log），然后就往index文件里面记录一个索引 |
| log.retention.hours                     | Kafka中数据保存的时间，默认7天                               |
| log.retention.minutes                   | Kafka中数据保存的时间，分钟级别，默认关闭                    |
| log.retention.ms                        | Kafka中数据保存的时间，毫秒级别，默认关闭                    |
| log.retention.check.interval.ms         | 检查数据是否保存超时的间隔，默认是5分钟                      |
| log.retention.bytes                     | 默认等于-1，表示无穷大。超过设置的所有日志总大小，删除最早的segment |
| log.cleanup.policy                      | 默认是delete，表示所有数据启用删除策略；如果设置值为compact，表示所有数据启用压缩策略 |
| num.io.threads                          | 默认是8。负责写磁盘的线程数。这个参数值要占总核数的50%       |
| num.replica.fetchers                    | 默认是1。副本拉取线程数，这个参数要占总核数50%的1/3          |
| num.network.threads                     | 默认是3。数据传输的线程数，这个参数要占总核数50%的2/3        |
| log.flush.interval.messages             | 强制页缓存刷写到磁盘的条数，默认是long的最大值，9223372036854775807。一般不建议修改，交给系统自己管理 |
| log.flush.interval.ms                   | 每隔多久，刷数据到磁盘，默认是null。一般不建议修改，交给系统自己管理 |
| auto.create.topic.enable                | 默认为true。当生产者向一个未创建的主题发送消息时，会自动创建一个分区数num.partitions（默认为1）、副本因子default.replication.factor（默认值为1）的主题。生产环境建议将该参数设置为false |

#### Kafka 消费者

![](/img/Kafka_11.png)

![](/img/Kafka_12.png)

| 参数名称                             | 描述                                                         |
| ------------------------------------ | ------------------------------------------------------------ |
| bootstrap.servers                    | 向Kafka集群建立初始链接用到的host/port列表                   |
| key.deserializer和value.deserializer | 指定接收消息的key和value的反序列化类型。一定要写全类名       |
| group.id                             | 标记消费者所属的消费者组                                     |
| enable.auto.commit                   | 默认值为true，消费者会自动周期性的向服务器提交偏移量         |
| auto.commit.interval.ms              | 如果设置了enable.auto.commit的值为true，则该值定义了消费者偏移量向Kafka提交的频率，默认为5s |
| auto.offset.reset                    | 当Kafka中没有初始偏移量或当前偏移量在服务器中不存在，该如何处理？<br />earliest：自动重置偏移量到最早的偏移量<br />latest：默认，自动重置偏移量为最新的偏移量<br />none：如果消费者组原来的偏移量不存在，则向消费者抛异常<br />anything：向消费者抛异常 |
| offset.topic.num.partitions          | _consumer_offsets的分区数，默认是50个分区。不建议修改        |
| heartbeat.interval.ms                | Kafka消费者和coordinator之间的心跳时间，默认3s。该条目的值必须小于session.timeout.ms，也不应该高于session.timeout.ms的1/3，不建议修改 |
| session.timeout.ms                   | Kafka消费者和coordinator之间连接超时时间，默认45s。超过该值，该消费者被移除，消费者组执行再平衡 |
| max.poll.interval.ms                 | 消费者处理消息的最大时长，默认5分钟。超过该值，该消费者被移除，消费者组执行再平衡 |
| partition.assignment.strategy        | 消费者分区分配策略，默认策略是Range + CooperativeSticky。Kafka可以同时使用多个分区分配策略。可以选择的策略包括：Range、RoundRobin、Sticky、CooperativeSticky |
| fetch.max.bytes                      | 默认Default：5242880（50m）。消费者获取服务器端一批消息最大的字节数。如果服务器端一批次的数据大于该值仍然可以拉取回来这批数据，因此这不是一个绝对最大值。一批次的大小受message.max.bytes（broker config）or max.message.bytes（topic config）影响 |
| max.poll.records                     | 一次poll拉取数据返回消息的最大条数，默认值是500条            |

