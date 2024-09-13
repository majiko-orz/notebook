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