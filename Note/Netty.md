Netty是一个异步的、基于事件驱动的网络应用框架，用以快速开发高性能、高可靠性的网络IO程序

**IO模型**

+ BIO：同步阻塞，服务器实现模式为一个连接一个线程，即客户端有连接请求时服务器需要启动一个线程进行处理，如果这个连接不做任何事情会造成不必要的线程开销
+ NIO：同步非阻塞，服务器实现模式为一个线程处理多个请求，即客户端发送的连接请求都会注册到多路复用器上，多路复用器轮询到连接有IO请求就进行处理
+ AIO：异步非阻塞，AIO引入了异步通道的概念，采用了Proactor模式，简化了程序编写，有效的请求才启动线程，他的特点是先由操作系统完成后才通知服务端程序启动线程去处理，一般适用于连接数较多且连接时间较长的应用

**使用场景**

+ BIO：适用于连接数目比较小且固定的架构，这种方式对服务器资源要求比较高，并发局限于应用中，JDK1.4之前的唯一选择，但程序简单易理解
+ NIO：适用于连接数目多且连接比较短的架构，比如聊天服务器，弹幕系统，服务器间通讯等。编程比较复杂，JDK1.4开始支持
+ AIO：适用于连接数目多且连接比较长的架构，比如相册服务器，充分调用OS参与并发操作，编程比较复杂，JDK7开始支持

### NIO基本介绍

NIO三大核心：Channel(通道)、Buffer(缓冲区)、Selector(选择器)

NIO是面向缓冲区，或者面向块编程的。数据读取到一个它稍后处理的缓冲区，需要时可在缓冲区中前后移动，这就增加了处理过程中的灵活性，使用它可以提供非阻塞式的高伸缩性网格

**Selector、Channel、Buffer的关系**

+ 每个channel都对应一个buffer
+ selector对应一个线程，一个线程对应多个channel
+ 程序切换到哪个channel是由事件决定的
+ selector会根据不同的事件，在各个通道上切换
+ buffer就是一个内存块，底层是有一个数组
+ 数据的读写是通过buffer，BIO中要么是输出流要么是输入流，不能双向，但是NIO的buffer可以读也可以写，需要flip方法切换
+ channel是双向的，可以返回底层操作系统的情况，比如Linux，底层的操作系统通道就是双向的

#### 缓冲区Buffer

缓冲区本质是一个可以读写数据的内存块，可以理解成一个容器对象（含数组），该对象提供了一组方法，可以更轻松的使用内存块，缓冲区对象内置了一些机制，能够跟踪和记录缓冲区的状态变化情况。

四个属性

+ Capacity：容量，即可以容纳的最大数据量，在缓冲区创建时被设定并且不能改变
+ Limit：表示缓冲区的当前终点，不能对缓冲区超过极限的位置进行读写操作。且极限是可以修改的
+ Position：位置，下一个要被读或写的元素的索引，每次读写缓冲区数据时都会改变该值，为下次读写作准备
+ Mark：标记

#### 通道Channel

NIO的通道类似于流，但有些区别如下：

+ 通道可以同时进行读写，而流只能读或者只能写
+ 通道可以实现异步读写数据
+ 通道可以从缓冲读数据，也可以写数据到缓冲

Channel在NIO中是一个接口，常用的Channel类有FileChannel、DatagramChannel、ServerSocketChannel和SocketChannel

```java
//实例1
String str = "hello，尚硅谷";
FileOutputStream fileOutputStream = new FileOutputStream("d:\\file01.txt");
FileChannel fileChannel = fileOutputStream.getChannel();
ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
byteBuffer.put(str.getBytes());
byteBuffer.flip();
fileChannel.write(byteBuffer);
fileOutputStream,close();
```

**Buffer和Channel的注意事项**

1. ByteBuffer支持类型化的get和put，放入的是什么数据类型，get就应该使用相应的数据类型来取出，否则可能BufferUnderFlowException
2. 可以将一个普通Buffer转为只读Buffer，buffer.asReadOnlyBuffer()
3. NIO还提供了MappedByteBuffer，可以让文件直接在内存（堆外的内存）中进行修改，而如何同步到文件由NIO来完成
4. NIO还支持多个Buffer完成读写操作，即Scattering和Gatering

#### 选择器Selector

Selector能够检测多个注册的通道上是否有事件发生（多个Channel以事件的方式可以注册到同一个Selector），如果有事件发生，便获取事件然后针对每个事件进行相应的处理。这样就可以只用一个单线程去管理多个通道，也就是管理多个连接和请求

只有在连接/通道真正有读写事件发生时，才会进行读写，就大大减少了系统开销，并且不必为每一个连接都创建一个线程，不用去维护多个线程，避免了多线程之间的上下文切换导致的开销

![](/img/Netty_1.png) 

1. 当客户端连接时，会通过ServerSocketChannel得到SocketChannel
2. 将SocketChannel注册到Selector上，register(Selector sel, int ops)，一个selector上可以注册多个SocketChannel
3. 注册后返回一个SelectionKey，会和该Selector关联（集合）
4. Selector进行监听，select方法，返回有事件发生的通道的个数
5. 进一步得到各个SelectionKey（有事件发生）
6. 再通过SelectionKey反向获取SocketChannel，方法channel()
7. 可以通过得到的channel，完成业务处理

```java
public class NIOServer {
    public static void main(String[] args) throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        serverSocketChannel.bind(new InetSocketAddress(6666));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            if (selector.select(1000) == 0) {//没有事件发生
                System.out.println("服务器等待了1s，无连接");
                continue;
            }

            //返回关注事件的集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                //关注事件为ACCEPT
                if (key.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                //关注事件为READ
                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel)key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("from客户端" + new String(buffer.array()));
                }

                //手动从集合中移动当前的selectionKey，防止重复操作
                iterator.remove();
            }
        }
    }
}
```

```java
public class NIOClient {
    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作");
            }
        }
        //如果连接成功，发送数据
        String str = "hello";
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        socketChannel.write(buffer);
        
    }
}
```

**群聊系统**

```java
//服务器端
public class GroupChatServer {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    public GroupChatServer() {
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        try {
            while (true) {
                int count = selector.select(2000);
                if (count > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()) {
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                            System.out.println(sc.getRemoteAddress() + "上线");
                        }
                        if (key.isReadable()) {
                            //处理读
                            readData(key);
                        }

                        //当前的key删除，防止重复处理
                        iterator.remove();
                    }
                } else {
                    System.out.println("等待...");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readData(SelectionKey key) {
        //取到关联的channel
        SocketChannel channel = null;
        try {
            //得到channel
            channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            if (count > 0) {
                String msg = new String(buffer.array());
                System.out.println("from客户端：" + msg);

                //向其他客户端转发消息,去掉自己
                sendInfoToOtherClients(msg, channel);
            }
        } catch (Exception e) {
            try {
                System.out.println(channel.getRemoteAddress() + "离线了");
                //取消注册
                key.channel();
                //关闭通道
                channel.close();;
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    //转发消息给其他客户
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException{
        System.out.println("服务器转发消息中...");
        //遍历所有注册到selector上的SocketChannel，并排除self
        for (SelectionKey key : selector.keys()) {
            Channel targetChannel = key.channel();
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                SocketChannel dest = (SocketChannel) targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChatServer chatServer= new GroupChatServer();
        new Thread(() -> {
            while (true) {
                chatServer.listen();
            }
        }).start();
    }
}
```

```java
//客户端
public class GroupChatClient {

    public static final String HOST = "127.0.0.1";
    public static final int PORT = 6667;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public GroupChatClient() throws Exception{
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username + "is ok...");
    }

    //向服务器发送消息
    public void sendInfo(String info) {
        info = username + "说：" + info;

        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //读取服务器发送的消息
    public void readInfo() {

        try {
            int readChannels = selector.select();
            if (readChannels > 0) {//有可用的通道
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        sc.read(buffer);
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }
                }
                iterator.remove();
            } else {
                System.out.println("没有可用的通道");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception{
        GroupChatClient chatClient = new GroupChatClient();
        //启动一个线程，每隔3秒，读取从服务器发送数据
        new Thread(() -> {
            while (true) {
                chatClient.readInfo();
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }
    }
}
```

#### 零拷贝

零拷贝是从操作系统角度来看，因为内核缓冲区之间，没有数据是重复的（只有kernel buffer有一份数据），零拷贝没有cpu拷贝。零拷贝不仅仅带来更少的数据复制，还能带来其他的性能优势，例如更少的上下文切换，更少的CPU缓存伪共享以及无CPU校验和计算

Java中常用零拷贝方式：mmap（内存映射）、sendFile

mmap通过内存映射，将文件映射到内核缓冲区，同时，用户空间可以共享内核空间的数据。这样，在进行网络传输时，就可以减少内核空间到用户空间的拷贝次数

Linux2.1版本提供了sendFile函数，其基本原理如下：数据根本不经过用户态，直接从内核缓冲区进入到SocketBuffer，同时由于和用户态完全无关，就减少了一次上下文切换

Linux2.4版本中，做了一些修改，避免了从内核缓冲区拷贝到SocketBuffer的操作，直接拷贝到协议栈，从而再一次减少了数据拷贝

![](/img/Netty_2.png)

**mmap和sendFile的区别**

1. mmap时候小数据量读写，sendFile适合大文件传输
2. mmap需要4次上下文切换，3次数据拷贝；sendFile需要3次上下文切换，最少2次数据拷贝
3. sendFile可以利用DMA方式，减少CPU拷贝，mmap不能（必须从内核拷贝到Socket缓冲区）

**NIO零拷贝：fileChannel.transferTo()**

```java
public class NewIOServer {
    public static void main(String[] args) throws Exception{

        InetSocketAddress address = new InetSocketAddress(7001);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(address);

        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            int readcount = 0;
            while (-1 != readcount) {
                readcount = socketChannel.read(byteBuffer);
            }
            byteBuffer.remaining();//倒带position=0 mark作废
        }
    }
}
```

```java
public class NewIOClient {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 7001));
        String fileName = "protoc-3.6.1-win32.zip";
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();

        long startTime = System.currentTimeMillis();
        //在Linux下一个transferTo方法就可以完成传输
        //在windows下，一次调用transferTo只能发送8m，就需要分段传输文件
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);

        System.out.println("发送的总的字节数=" + transferCount + "耗时：" + (System.currentTimeMillis() - startTime));

        fileChannel.close();
    }
}
```

#### AIO基本介绍

JDK7引入了Asynchronous I/O，即AIO。在进行IO编程中，常用到两种模式：Reactor和Proactor。Java的NIO是Reactor，当事件触发时，服务器端得到通知，进行相应的处理

AIO即NIO2.0，叫做异步不阻塞的IO。AIO引入异步通道的概念，采用了Proactor模式，简化了程序编写，有效的请求才启动线程，它的特点是先由操作系统完成后才通知服务端程序启动线程去处理，一般适用于连接数较多且连接时间较长的应用

### Netty

**原生NIO存在的问题**

+ NIO的类库和API繁杂，使用麻烦：需要熟练掌握Selector、Channel、Buffer等
+ 需要具备其他额外的技能：要熟悉多线程编程，因为NIO编程涉及到Reactor模式，必须对多线程和网络编程非常熟悉，才能写出高质量的NIO程序
+ 开发工作量和难度都非常大：例如客户端面临断连重连、网络闪断、半包读写、失败缓存、网络拥塞和异常流的处理等
+ JDK NIO的bug：Epoll Bug，它会导致Selector空轮询，最终导致CPU100%。直到1.7版本该问题仍旧存在，没有根本解决

目前推荐使用Netty4.x稳定版本

#### 线程模型

目前存在的线程模型有：

+ 传统阻塞IO服务模型

  采用阻塞IO模式获取输入数据，每个连接都需要独立的线程完成数据的输入、业务处理、数据返回

  当并发数很大，就会创建大量的线程，占用很大系统资源。连接创建后，如果当前线程暂时没有数据可读，该线程会阻塞在read操作，造成线程资源浪费

+ Reactor模型

  针对传统阻塞IO服务模型的缺点，做了改进：

  + 基于IO复用模型：多个连接共用一个阻塞对象，应用程序只需要在一个阻塞对象等待，无需阻塞等待所有连接。当某个连接有新的数据可以处理时，操作系统通知应用程序，线程从阻塞状态返回，开始进行业务处理
  + 基于线程池复用线程资源：不必再为每个连接创建线程，将连接完成后的业务处理任务分配给线程进行处理，一个线程可以处理多个连接的业务

根据Reactor的数量和处理资源池线程的数量不同，有3种典型的实现

+ 单Reactor单线程
+ 单Reactor多线程
+ 主从Reactor多线程

Netty线程模型主要基于主从Reactor多线程模型做了一定的改进，主从Reactor多线程模型有多个Reactor

![](/img/Netty_3.png)

1. Reactor模式，通过一个或多个输入同时传递给服务处理器的模式（基于事件驱动）
2. 服务器端程序处理传入的多个请求，并将它们同步分派到相应的线程处理
3. Reactor模式使用IO复用监听事件，收到事件后分发给某个线程，这点就是网络服务器高并发处理关键

Reactor模式种核心组成：

1. Reactor：Reactor在一个单独的线程中运行，负责监听和分发事件，分发给适当的处理程序来对IO事件作出反应
2. Handlers：处理程序执行IO事件要完成的实际事件，类似于客户想要与之交谈的公司中的实际官员。Reactor通过调度适当的处理程序来响应IO事件，处理程序执行非阻塞操作

**单Reactor单线程**

![](/img/Netty_4.png)

优点：模型简单，没有多线程、进程通信、竞争的问题，全部在一个线程中完成

缺点：性能问题，只有一个线程，无法完全发挥多核CPU的性能。Handler在处理某个连接上的业务时，整个进程无法处理其他来凝结时间，很容易导致性能瓶颈。可靠性问题，线程意外终止或者进入死循环，会导致整个系统通信模块不可用，不能接收和处理外部消息，造成节点故障

使用场景：客户端的数量有限，业务处理非常快速

**单Reactor多线程**

![](/img/Netty_5.png)

1. Reactor对象通过select监控客户端请求事件，收到事件后，通过dispatch进行分发
2. 如果建立连接请求，则由Acceptor通过accept处理连接请求，然后创建一个handler对象处理完成连接后的各种事件
3. 如果不是连接请求，则由reactor分发调用连接对应的handler来处理
4. handler只负责响应事件，不做具体的业务处理，通过read读取数据后，会分发给后面的worker线程池的某个线程处理业务
5. worker线程池会分配一个独立的线程完成真正的业务，并将结果返回给handler
6. handler收到响应后，通过send将结果返回给client

优点：可以充分利用多核cpu的处理能力

缺点：多线程数据共享和访问比较复杂，reactor处理所有的事件的监听和响应，在单线程运行，在高并发应用场景容易出现性能瓶颈

**主从Reactor多线程**

![](/img/Netty_6.png)

1. Reactor主线程MainReactor对象通过select监听连接事件，收到事件后，通过Acceptor处理连接事件
2. 当Acceptor处理连接事件后，MainReactor将连接分配给SubReactor
3. subreactor将连接加入到连接队列进行监听，并创建handler进行各种事件处理
4. 当有新事件发生时，subreactor就会调用对应的handler处理
5. handler通过read读取数据，分发给后面的worker线程处理
6. worker线程池分配独立的worker线程进行业务处理，并返回结果
7. handler收到响应的结果后，在通过send分发将结果返回给client
8. Reactor主线程可以对应多个Reactor子线程，即MainReactor可以关联多个SubReactor

优点：父线程和子线程的数据交互简单职责明确，父线程只需要接收新连接，子线程完成后续业务处理。父线程与子线程的数据交互简单，Reactor主线程只需要把新连接传给子线程，子线程无需返回数据

缺点：编程复杂度较高

**Reactor模式具有如下优点**

1. 响应快，不必为单个同步时间所阻塞，虽然Reactor本身依然是同步的
2. 可以最大程度的避免复杂的多线程及同步问题，并且避免了多线程/进程的切换开销
3. 扩展性好，可以方便的通过增加Reactor实例个数来充分利用CPU资源
4. 复用性好，Reactor模型本身与具体事件处理逻辑无关，具有很高的复用性

#### Netty模型

![](/img/Netty_7.png)

1. Netty抽象出两组线程池BossGroup专门负责接收客户端的连接，WorkGroup专门负责网络的读写
2. BossGroup和WorkGroup类型都是NioEventLoopGroup
3. NioEventLoopGroup相当于一个事件循环组，这个组中有多个事件
4. NioEventLoop表示一个不断循环的执行处理任务的线程，每个NioEventLoop都有一个selector，用于监听绑定在其上的socket网络通讯
5. NioEventLoopGroup可以有多个线程，即可以含有多个NioEventLoop
6. 每个Boss NioEventLoop执行的步骤有3步
   1. 轮询accept事件
   2. 处理accept事件，与client建立连接，生成NioSocketChannel，并将其注册到某个worker NIOEventLoop上的selector
   3. 处理任务队列的任务，即runAllTasks
7. 每个Worker NIOEventLoop循环执行的步骤
   1. 轮询read,write事件
   2. 处理io事件，即read,write事件，在对应NioSocketChannel处理
   3. 处理任务队列的任务，即runAllTasks
8. 每个Worker NIOEventLoop处理业务时，会使用pipeline（管道），pipeline中包含了channel，即通过pipeline可以获取到对应的管道，管道中维护了很多的处理器

```java
public class NettyServer {
    public static void main(String[] args) throws Exception{

        //创建BossGroup和WorkerGroup
        //创建两个线程组bossGroup和workerGroup
        //bossGroup只是处理连接请求，真正的和客户端业务处理，会交给workerGroup完成
        //两个都是无限循环
        //bossGroup和workerGroup含有的子线程（NioEventLoop）的个数默认实际cpu*2
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务器端的启动对象，配置参数
            ServerBootstrap bootStrap = new ServerBootstrap();

            //使用链式编程进行设置
            bootStrap.group(bossGroup, workerGroup) //设置两个线程组
                    .channel(NioServerSocketChannel.class) //使用NioServerSocketChannel作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) //设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { //创建一个通道初始化对象
                        //给pipeline设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    }); //给workerGroup的EventLoop对应的管道设置处理器

            System.out.println("...服务器 is ready...");

            //绑定一个端口并且同步，生成了一个ChannelFuture对象
            ChannelFuture cf = bootStrap.bind(6668).sync();

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
```

```java
/*
自定义一个handler，需要继承netty规定好的某个HandlerAdapter（规范），这时我们自定义一个handler才能称为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    //读取数据实际（这里我们可以读取客户端发送的消息）
    //ChannelHandlerContext ctx:上下文对象，含有管道pipeline，通道channel，地址
    //Object msg：就是客户端发送的数据，默认Object
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx =" + ctx);
        //将msg转成一个ByteBuffer
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送的消息是：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址：" + ctx.channel().remoteAddress());
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓存，并刷新
        //一般讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端", CharsetUtil.UTF_8));
    }

    //处理异常，一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
```

```java
public class NettyClient {
    public static void main(String[] args) throws Exception{

        //客户端需要一个事件循环组
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            //创建客户端启动对象
            Bootstrap bootstrap = new Bootstrap();

            //设置相关参数
            bootstrap.group(group) //设置线程组
                    .channel(NioSocketChannel.class) //设置客户端通道的实现类（反射）
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler()); //加入自己的处理器
                        }
                    });

            System.out.println("客户端 is ok...");

            //启动客户端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();

            //给关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }

    }
}
```

```java
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    //当通道就绪就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client" + ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,server", CharsetUtil.UTF_8));
    }

    //当通道有读取事件时，会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息：" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址：" + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

```

**任务队列中的Task有3种典型使用场景**

1. 用户程序自定义的普通任务

2. 用户自定义定时任务

3. 非当前Reactor线程调用Channel的各种方法

   例如在推送系统的业务线程里面，根据用户的标识，找到对应的Channel引用，然后调用write类方法向该用户推送消息，就会加入这种场景。最终write会提交到任务队列中后被异步消费

```java
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //用户程序自定义的普通任务
        //非常耗时的任务->异步执行->提交该channel对应的NIOEventLoop的taskQueue中
        //Thread.sleep(10 * 1000);
        //ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端2", CharsetUtil.UTF_8));
        //解决方案1
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端2", CharsetUtil.UTF_8));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //用户自定义定时任务->该任务是提交到scheduleTaskQueue中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端3", CharsetUtil.UTF_8));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 5, TimeUnit.SECONDS);
        
        System.out.println("go on...");
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓存，并刷新
        //一般讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端1", CharsetUtil.UTF_8));
    }

    //处理异常，一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
```

**异步模型**

+ Netty中的IO操作都是异步的，包括Bind、Write、Connect等操作会简单的返回一个ChannelFuture

+ 调用者并不能立即获得结果，而是通过Future-Listener机制，用户可以方便的主动获取或者通过通知机制获得IO操作结果
+ Netty的异步模型是建立在future和callback的基础之上的。callback就是回调。重点说Future，它的核心思想是：假设一个方法fun，计算过程可能非常耗时，等待fun返回显然不合适。那么可以在调用fun的时候，立马返回一个Future，后续可以通过Future去监控方法fun的处理过程（即future-listener机制）
  + Future表示异步的执行结果，可以通过它提供的方法来检测执行是否完成，比如检索计算等
  + ChannelFuture是一个接口，我们可以添加监听器，当监听的事件发生时，就会通知到监听器

**HTTP服务实例**

```java
public class TestServer {
    public static void main(String[] args) throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new TestServerInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind(6668).sync();

            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
```

```java
//SimpleChannelInboundHandler继承ChannelInboundHandlerAdapter
//HttpObject 客户端和服务器端相互通讯的数据被封装成HttpObject
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    //读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        if (msg instanceof HttpRequest) {

            System.out.println("msg类型=" + msg.getClass());
            System.out.println("客户端地址" + ctx.channel().remoteAddress());

            //获取到
            HttpRequest httpRequest = (HttpRequest) msg;
            //获取uri,过滤指定的资源
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了favicon.ico，不做响应");
                return;
            }
            //回复信息给浏览器[http协议]
            ByteBuf content = Unpooled.copiedBuffer("hello, 我是服务器", CharsetUtil.UTF_8);

            //构造一个http的响应，即httpResponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());

            //将构建好的response返回
            ctx.writeAndFlush(response);

        }
    }
}
```

```java
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //向管道加入处理器

        //得到管道
        ChannelPipeline pipeline = ch.pipeline();

        //加入一个Netty提供的httpServerCodec codec => [coder-decoder]
        //HttpServerCodec是netty提供的处理http的编码解码器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        //增加一个自定义的handler
        pipeline.addLast("MyTestHttpServerHandler", new TestHttpServerHandler());
    }
}
```

#### Netty核心模块组件

**Bootstrap、ServerBootstrap**

+ Bootstrap意思是引导，一个Netty应用通常由一个Bootstrap开始，主要作用是配置整个Netty程序，串联各个组件，Netty中Bootstrap类是客户端程序的启动引导类，ServerBootstrap是服务端启动引导类
+ 常见的方法有
  + public ServerBootstrap group：该方法用于服务端，用来设置两个EventLoop
  + public B group：该方法用于客户端，用来设置一个EventLoop
  + public B channel：该方法用来设置一个服务器端的通道实现
  + public B option：用来个ServerChannel添加配置
  + public ServerBootstrap childOption：用来给接收到的通道添加配置
  + public ServerBootstrap childHandler：该方法用来设置业务处理类
  + public ChannelFuture bind：该方法用于服务端，用来设置占用的端口号
  + public ChannelFuture connect：该方法用于客户端，用来连接服务器

**Future、ChannelFuture**

+ Netty中所有的IO操作都是异步的，不能立刻得知消息是否被正确处理。但是可以过一会儿等它执行完成或者直接注册一个监听，具体的实现就是通过Future和ChannelFutures，他们可以注册一个监听，当操作执行成功或失败时监听会自动触发注册的监听事件
+ 常见的方法有
  + Channel channel：返回当前正在进行IO操作的通道
  + ChannelFuture sync：等待异步操作执行完毕

**Channel**

+ Netty网络通信的组件，能够用于执行网络IO操作
+ 通过Channel可获得当前网络连接的通道的状态
+ 通过Channel可获得网络连接的配置参数
+ Channel提供异步的网络IO操作（如建立连接，读写，绑定端口），异步调用意味着任何IO调用都将立即返回，并且不保证在调用结束时所请求的IO操作已完成
+ 调用立即返回一个ChannelFuture实例，通过注册监听器到ChannelFuture上，可以IO操作成功、失败或取消时回调通知调用方
+ 支持关联IO操作与对应的处理程序
+ 不同协议、不同的阻塞类型的连接都有不同的Channel类型与之对应，常用的Channel类型
  + NioSocketChannel：异步的客户端TCP Socket连接
  + NioServerSocketChannel：异步的服务器端TCP Socket连接
  + NioDatagramChannel：异步的UDP连接
  + NioSctpChannel：异步的客户端Sctp连接
  + NioSctpServerChannel：异步的Sctp服务器端连接

**Selector**

+ Netty基于Selector对象实现IO多路复用，通过Selector一个线程可以监听多个连接的Channel事件
+ 当向一个Selector中注册Channel后，Selector内部的机制就可以自动不断的查询这些注册的Channel是否有已就绪的IO事件（例如可读、可写、网络连接完成等），这样程序就可以很简单地使用一个线程高效地管理多个channel

**ChannelHandler**

+ ChannelHandler是一个接口，处理IO事件或拦截IO操作，并将其转发到其ChannelPipeline（业务处理链）中的下一个处理程序
+ ChannelHandler本身并没有提供很多方法，因为这个接口有许多的方法需要实现，方便使用期间，可以继承它的子类

![](/img/Netty_8.png)

+ ChannelInboundHandler用于处理入站IO事件
+ ChannelOutboundHandler用于处理出站IO操作
+ ChannelInboundHandlerAdapter用于处理入站IO事件
+ ChannelOutboundHandlerAdapter用于处理出站IO操作
+ ChannelDuplexHandler用于处理入站和出战事件

我们经常需要自定义一个Handler类去继承ChannelInboundHandlerAdapter，然后通过重写相应方法实现业务逻辑

+ channelActive方法：通道就绪事件
+ channelRead方法：通道读取数据事件
+ channelReadComplete：数据读取完毕事件
+ exceptionCaught：通道发生异常事件

**Pipeline和ChannelPipeline**

+ ChannelPipeline是一个重点
+ ChannelPipeline是一个Handler的集合，他负责处理和拦截inbound或者outbound的事件和操作，相当于一个贯穿Netty的链。也可以这样理解：ChannelPipeline是保存ChannelHandler的list，用于处理或拦截Channel的入站事件和出站事件
+ ChannelPipeline实现了一种高级形式的拦截过滤器模式，使用户可以完全控制事件的处理方式，以及channel中各个的ChannelHandler如何相互交互
+ 在Netty中每个Channel都有且仅有一个ChannelPipeline与之对应

![](/img/Netty_9.png)

+ 一个Channel包含了一个ChannelPipeline，而ChannelPipeline中又维护了一个由ChannelHandlerContext组成的双向链表，而且每个ChannelHandlerContext中又关联着一个ChannelHandler
+ 入站事件和出站事件在一个双向链表中，入站事件会从链表head往后传递到最后一个入站的handler，出站事件会从链表tail往前传递到最前一个出站的handler，两种类型的handler互不干扰

常用方法

+ ChannelPipeline addFirst：把一个业务处理类（handler）添加到链的第一个位置
+ ChannelPipeline addLast：把一个业务处理类（handler）添加到链的最后一个位置

**ChannelHandlerContext**

+ 保存Channel相关的所有上下文信息，同时关联一个ChannelHandler对象
+ 即ChannelHandlerContext中包含一个具体的事件处理器ChannelHandler，同时ChannelHandlerContext中也绑定了对应的pipeline和Channel信息，方便对ChannelHandler进行调用

常用方法

+ ChannelFuture close：关闭通道
+ ChannelOutboundInvoker flush：刷新
+ ChannelFuture writeAndFlush：将数据写道ChannelPipeline中当前ChannelHandler的下一个ChannelHandler开始处理（出站）

**ChannelOption**

+ Netty在创建Channel实例后一般都需要设置ChannelOption参数
+ ChannelOption参数如下
  + ChannelOption.SO_BACKLOG：对应TCP/IP协议listen函数中的backlog参数，用来初始化服务器可连接队列大小。服务端处理客户端连接请求是顺序处理的，所以同一时间只能处理一个客户端连接。多个客户端来的时候，服务端将不能处理的客户端连接请求放在队列中等待处理，backlog参数指定了队列的大小
  + ChannelOption.SO_KEEPALIVE：一直保持连接活动状态

**EventLoopGroup和其实现类NioEventLoopGroup**

+ EventLoopGroup是一组EventLoop的抽象，Netty为了更好地利用多核CPU资源，一般会有多个EventLoop同时工作，每个EventLoop维护着一个Selector实例

+ EventLoopGroup提供next接口，可以从组里面按照一定规则获取其中一个EventLoop来处理任务。在Netty服务器编程中，我们一般都需要提供两个EventLoopGroup，例如BossEventLoopGroup和WorkerEventLoopGroup

+ 通常一个服务端口即一个ServerSocketChannel对应一个Selector和一个EventLoop线程。BossEventLoop负责接收客户端的连接并将SocketChannel交给WorkerEventLoopGroup来进行IO处理，如下图所示

  ![](/img/Netty_10.png)

+ BossEventLoopGroup通常是一个单线程的EventLoop，EventLoop维护着一个注册了ServerSocketChannel的Selector实例BossEventLoop不断轮询Selector将连接事件分离出来

+ 通常是OP_ACCEPT事件，然后将接收到的SocketChannel交给WorkerEventLoopGroup

+ WorkerEventLoopGroup会由next选择其中一个EventLoopGroup来将这个SocketChannel注册到其维护的Selector并对其后续的IO事件进行处理

**Unpooled类**

+ Netty提供一个专门用来操作缓冲区（即Netty的数据容器）的工具类
+ 常用方法
  + ByteBuf copiedBuffer：通过给定的数据和字符串编码返回一个ByteBuf对象

#### Netty应用实例-群聊系统

```java
public class GroupChatServer {

    private int port;

    public GroupChatServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入解码器
                            pipeline.addLast("decoder", new StringDecoder());
                            //加入编码器
                            pipeline.addLast("encoder", new StringEncoder());
                            //加入自己的业务处理handler
                            pipeline.addLast(new GroupChatServerHandler());
                        }
                    });
            System.out.println("netty 服务器启动");
            ChannelFuture channelFuture = b.bind(port).sync();

            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws Exception {
        new GroupChatServer(7000).run();
    }
}
```

```java
public class GroupChatServerHandler extends SimpleChannelInboundHandler {

    //返回一个channel组，管理所有的channel
    //GlobalEventExecutor是一个全局的事件执行器，是一个单列
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //表示连接建立，一旦连接，第一个被执行
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入聊天的信息推送给其他在线的客户端
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "加入聊天\n");
        channelGroup.add(channel);
    }

    //断开连接，将xx客户离开信息推送给当前在线的用户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "离开了\n");
    }

    //表示channel处于活动状态，提示xx上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了");
    }

    //表示channel处于不活动状态，提示xx离线了
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线了");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush("[客户]" + channel.remoteAddress() + "发送了消息" + o.toString() + "\n");
            } else {
                ch.writeAndFlush("[自己]发送了消息" + o.toString() + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
```

```java
public class GroupChatClient {

    private final String host;
    private final int port;

    public GroupChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("decoder", new StringDecoder());
                            pipeline.addLast("encoder", new StringEncoder());
                            pipeline.addLast(new GroupChatClientHandler());
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Channel channel = channelFuture.channel();
            System.out.println("-------" + channel.remoteAddress() + "-------");

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                channel.writeAndFlush(msg + "\r\n");
            }
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        new GroupChatClient("127.0.0.1", 7000).run();
    }
}
```

```java
public class GroupChatClientHandler extends SimpleChannelInboundHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println(o.toString().trim());
    }
}
```

#### Netty心跳机制

```java
public class MyServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)) //增加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入一个netty提供的IdleStateHandler
                            //IdleStateHandler是netty提供的处理空闲状态的处理器
                            //long readerIdleTime:表示多长时间没有读，就会发送一个心跳检测包检测是否连接
                            //long writerIdleTime:表示多长时间没有写，就会发送一个心跳检测包检测是否连接
                            //long allIdleTime:表示多长时间没有读写，就会发送一个心跳检测包检测是否连接
                            //当IdleStateEvent触发后，就会传递给管道的下一个handler去处理，通过调用下一个handler的userEventTriggered，在该方法中去处理IdleStateEvent
                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                            pipeline.addLast(new MyServerHandler());
                        }
                    });

            ChannelFuture channelFuture = b.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
```

```java
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     *
     * @param ctx 上下文
     * @param evt 事件
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()) {
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空闲";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + "--超时事件--" + eventType);
        }
    }
}
```

#### Netty通过WebSocket实现服务器和客户端长连接

```java
public class MyServer {
    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO)) //增加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            //因为基于http协议，使用http的编码和解码器
                            pipeline.addLast(new HttpServerCodec());
                            //是以块方式写，添加ChunkedWriteHandler
                            pipeline.addLast(new ChunkedWriteHandler());

                            //http数据在传输过程中是分段，HttpObjectAggregator就是可以将多个段聚合
                            //这就是为什么，当浏览器发送大量数据时，就会发出多次http请求
                            pipeline.addLast(new HttpObjectAggregator(8192));

                            //对于websocket，它的数据是以帧的形式传递的
                            //WebSocketFrame下面有6个子类
                            //浏览器请求时 ws://localhost:7000/hello 表示请求的uri
                            //WebSocketServerProtocolHandler核心功能是将http协议升级为ws协议，保持长连接
                            //是通过一个状态码101
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

                            //自定义handler，处理业务逻辑
                            pipeline.addLast(new MyTextWebSocketFrameHandler());
                        }
                    });

            ChannelFuture channelFuture = b.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
```

```java
//TextWebSocketFrame表示一个文本帧
public class MyTextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        System.out.println("服务器收到消息" + msg.text());

        //回复消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间" + LocalDateTime.now() + " " + msg.text()));
    }

    //当客户端连接后，触发方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        //id表示唯一的值，LongText是唯一的，ShortText不是唯一
        System.out.println("handlerAdded 被调用" + ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用" + ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

        System.out.println("handlerAdded 被调用" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生" + cause.getMessage());
        ctx.close();
    }
}
```

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<script>
    var socket;
    //判断当前浏览器是否支持websocket
    if (window.WebSocket) {
        socket = new WebSocket("ws://localhost:7000/hello");
        //相当于channelReado，ev收到服务器端回送的消息
        socket.onmessage = function (ev) {
            var rt = document.getElementById('responseText');
            rt.value = rt.value + "\n" + ev.data;
        }

        //相当于连接开启
        socket.onopen = function (ev) {
            var rt = document.getElementById('responseText');
            rt.value = "连接开启了.."
        }

        //相当于连接关闭
        socket.onclose = function (ev) {
            var rt = document.getElementById('responseText');
            rt.value = rt.value + "\n" + "连接关闭了..";
        }
    } else {
        alert("当前浏览器不支持websocket")
    }

    function send(message) {
        if (!window.socket) {
            return;
        }
        if (socket.readyState == WebSocket.OPEN) {
            socket.send(message)
        } else {
            alert("连接没有开启");
        }
    }
</script>
    <form onsubmit="return false">
        <textarea name="message" style="height: 300px; width: 300px"></textarea>
        <input type="button" value="发送消息" onclick="send(this.form.message.value)">
        <textarea id="responseText" style="height: 300px; width: 300px"></textarea>
        <input type="button" value="清空内容" onclick="document.getElementById('responseText').value = ''">
    </form>
</body>
</html>
```

#### Netty编码解码

编写网络应用程序时，因为数据在网络中传输的都是二进制字节码数据，在发送数据时就需要编码，接收数据时就需要解码

codec（编码解码器）的组成部分有两个：decoder（解码器）和encoder（编码器）。

encoder负责把业务数据转换为字节码数据，decoder负责把字节码数据转换成业务数据

Netty本身提供了一些codec

+ StringEncoder：对字符串数据进行编码
+ ObjectEncoder：对Java对象进行编码
+ StringDecoder：对字符串数据进行解码
+ ObjectDecoder：对Java对象进行解码

Netty自带的ObjectDeconder和ObjectEncoder可以用来实现POJO对象或各种业务对象的解码和编码，底层使用的仍是Java序列化技术，而Java序列化技术本身效率就不高，存在如下问题：

+ 无法跨语言
+ 序列化后的体积太大，是二进制编码的5倍多
+ 序列化性能太低

引入新的解决方案Google的Protobuf

**Protobuf**

+ Protobuf是Google发布的开源项目，全称Google Protocol Buffers，是一种轻便高效的结构化数据存储格式，可以用于结构化数据串行化，或者说序列化。它很适合做数据存储或RPC【远程过程调用remote Produce Call】数据交换格式

+ Protobuf是以message的方式来管理数据的

+ 支持跨平台、跨语言
+ 高性能、高可靠性
+ 使用protobuf编译器能自动生成代码，Protobuf是将类的定义使用.proto文件进行描述。
+ 然后通过protoc.exe编译器根据.proto自动生成.java文件

#### TCP粘包拆包