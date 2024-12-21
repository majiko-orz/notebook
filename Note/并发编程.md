### **进程**

+ 程序由指令和数据组成，这些指令要运行，数据要读写，就必须将指令加载至CPU，数据加载至内存，在指令运行过程中还需要用到磁盘、网络等设备。进程就是用来加载指令，管理内存，管理IO的

+ 当一个程序被运行，从磁盘加载这个程序的代码至内存，这时就开启了一个进程。
+ 进程就可以视为程序的一个实例。大部分程序可以同时运行多个实例进程，也有的程序只能启动一个实例进程

### **线程**

+ 一个进程之内可以分为一到多个线程
+ 一个线程就是一个指令流，将指令流中的一条条指令以一定的顺序交给CPU执行
+ Java中，线程作为最小的调度单位，进程作为资源分配的最小单位。

### **并行**

多核cpu下，每个核都可以调度运行线程，这时候线程可以是并行的

### **并发**

单核cpu下，线程实际还是串行执行的。操作系统中有一个组件叫做任务调度器，将cpu的时间片分给不同的线程使用，只是由于cpu在线程间的切换非常快，人类感觉是同时运行的，一般会将这种线程轮流使用cpu的做法称为并发：concurrent

### **同步**

需要等待返回结果，才能继续运行

### **异步**

不需要等待返回结果，就能继续运行

### **多线程的创建**

1. 继承Thread类

   1. 创建一个继承于Thread类的子类
   2. 重写Thread类的run()
   3. 创建Thread类的子类的对象
   4. 通过此对象调用start()

   ```java
   //创建线程对象
   Thread t = new Thread() {
       public void run() {
           //要执行的任务
       }
   }
   //启动线程
   t.start();
   ```

2. 实现Runnable接口

   1. 创建一个类实现了Runnable接口的类
   2. 实现类去实现Runnable中的抽象方法：run()
   3. 创建实现类的对象
   4. 将此对象作为参数传递到Thread类的构造器中，创建Thread类的对象
   5. 通过Thread类的对象调用start()

   ```java
   Runnable runnable = new Runnable() {
       public void run(){
           
       }
   };
   Thread t = new Thread(runnable);
   t.start();
   ```

   用Runnable更容易与线程池等高级API配合，用Runnable让任务类脱离了Thread继承体系，更灵活

3. FutureTask配合Thread

   FutureTask能够接收Callable类型的参数，用来处理有返回结果的情况

   ```java
   FutureTask<Integer> task = new FutureTask<>(() -> {
       log.debug("hello");
       return 100;
   })
   //参数1是任务对象，参数2是线程名字
   new Thread(task,"t").start();
   
   //主线程阻塞，同步等待task执行完毕的结果
   Integer result = task.get();
   ```

   实现Callable接口

   与使用Runnable相比，Callable功能更强大

   相比run()方法，call()方法可以有返回值，方法可以抛出异常，支持泛型的返回值，需要借助FutureTask类，比如获取返回结果

   1. 创建一个实现Callable接口的实现类
   2. 重写call方法，将此线程需要执行的操作声明在call方法中
   3. 创建Callable接口实现类的对象
   4. 将此Callable接口实现类的对象作为参数传递到FutureTask构造器中，创建FutureTask的对象
   5. 将FutureTask的对象作为参数传递到Thread类的构造器中，创建Thread对象，并调用start()
   6. 获取Callable中call方法的返回值

4. 使用线程池

   好处：提高响应速度（减少了线程创建时间），降低资源消耗（重复利用线程池中线程，不需要每次都创建），便于线程管理（corePoolSize：核心池大小，maximumPoolSize：最大线程数，keepAliveTime：线程没有任务时最多保持多长时间后会终止）

   **ExcutorService：**真正的线程池接口。常见子类ThreadPoolExecutor

   + void execute(Runnable command)：执行任务/命令，没有返回值，一般用来执行Runnable
   + <T> Future <T> submit (Callable<T> task)：执行任务，有返回值，一般用来执行Callable
   + void shutdown()：关闭连接池

   **Executors：**工具类、线程池的工厂类，用于创建不同类型的线程池

   + Executors.newCachedThreadPool()：创建一个可根据需要创建新线程的线程池

   + Executors.newFixedThreadPool()：创建一个可重用固定线程数的线程池
   + Executors.newSingleThreadPool()：创建一个只有一个线程的线程池

   + Executors.newScheduledThreadPool()：创建一个线程池，它可安排 在给定延迟后运行命令或者定期的执行

   1. 创建指定线程数量的线程池
   2. 执行指定的线程的操作。需要提供实现Runnable接口或Callable接口实现类的对象
   3. 关闭连接池



比较创建线程的两种方式。

优先选择：实现Runnable接口的方式

原因：实现的方式没有单继承的局限性，实现的方式更适合来处理多个线程有共享数据的情况

### **栈与栈帧**

jvm中由堆、栈、方法区所组成，其中栈内存给线程用，每个线程启动后，虚拟机就会为其分配一块栈内存

每个栈由多个栈帧组成，对应着每次方法调用时所占用的内存

每个线程只能有一个活动栈帧，对应着当前正在执行的那个方法

### 线程上下文切换（Thread Context Switch）

因为以下一些原因导致cpu不再执行当前的线程，转而执行另一个线程的代码

+ 线程的cpu时间片用完
+ 垃圾回收
+ 有更高优先级的线程需要运行
+ 线程自己调用了sleep、yield、wait、join、park、synchronized、lock等方法

当Context Switch发生时，需要由操作系统保存当前线程的状态，并恢复另一个线程的状态，Java中对应的概念就是程序计数器，它的作用是记住下一条jvm指令的执行地址，是线程私有的

+ 状态包括程序计数器、虚拟机栈中每个栈帧的信息，如局部变量、操作数栈、返回地址等等
+ Context Switch频繁发生会影响性能

### 常见方法

| 方法名           | 功能说明                                                 | 注意                                                         |
| ---------------- | -------------------------------------------------------- | ------------------------------------------------------------ |
| start()          | 启动一个新线程，在新的线程运行run方法中的代码            | start方法只是让线程进入就绪，里面代码不一定立刻运行（CPU时间片还没分给他）。每个线程对象的start方法只能调用一次，如果调用了多次会出现IllegalThreadStateException |
| run()            | 新线程启动后会调用的方法                                 | 如果在构造Thread对象时传递了Runnable参数，则线程启动后会调用Runnable中的run方法，否则默认不执行任何操作。但可以创建Thread的子类对象，来覆盖默认行为 |
| join()           | 等待线程运行结束                                         |                                                              |
| join(long n)     | 等待线程运行结束，最多等待n毫秒                          |                                                              |
| getId()          | 获取线程长整型的id                                       | id唯一                                                       |
| getName()        | 获取线程名                                               |                                                              |
| setName(String)  | 修改线程名                                               |                                                              |
| getPriority()    | 获取线程优先级                                           |                                                              |
| setPriority(int) | 修改线程优先级                                           | Java中规定线程的优先级是1~10的整数，较大的优先级能提高线程被CPU调度的几率 |
| getState()       | 获取线程状态                                             | Java中线程状态是用6个enum表示，分别为：NEW,RUNNABLE,BLOCKED,WAITING,TIMED_WAITING,TERMINATED |
| isInterrupted()  | 判断是否被打断                                           | 不会清除打断标记                                             |
| isAlive()        | 线程是否存活（还没有运行完毕）                           |                                                              |
| interrupt()      | 打断线程                                                 | 如果被打断线程正在sleep，wait，join会导致被打断的线程抛出InterruptedException，并清除打断标记；如果打断的正在运行的线程，则会设置打断标记；park的线程被打断，也会设置打断标记 |
| interrupted()    | 判断当前线程是否被打断                                   | 会清除打断标记                                               |
| currentThread()  | 获取当前正在执行的线程                                   |                                                              |
| sleep(long n)    | 让当前执行的线程休眠n毫秒，休眠时让出cpu时间片给其他线程 |                                                              |
| yield()          | 提示线程调度器让出当前线程对cpu的使用                    | 主要是为了测试和调试                                         |



**sleep**

1. 调用sleep会让当前线程从Running进入Timed Waiting状态
2. 其他线程可以使用interrupt方法打断正在睡眠的线程，这时sleep方法会抛出InterruptedException
3. 睡眠结束后的线程未必会立刻得到执行
4. 建议用TimeUnit的sleep代替Thread的sleep来获得更好的可读性

**yield**

1. 调用yield会让当前线程从Running进入Runnable状态，然后通过调度执行其他同优先级的线程。如果这时没有同优先级的线程，那么不能保证让当前线程暂停的效果
2. 具体的实现依赖于操作系统的任务调度器

### 两阶段终止模式

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_1.png)

```java
@Slf4j
class TwoPhaseTermination{
    private Thread monitor;
    private volatile boolean stop=false;
    //启动监控线程
    public void start(){
        monitor=new Thread(()->{
           while(true){
               Thread current=Thread.currentThread();
               //if(current.isInterrupted()){
               //    log.debug("料理后事");
               //    break;
               //}
                if(stop){
                   log.debug("料理后事");
                   break;
               }
               try{
                   Thread.sleep(1000);
                   log.debug("执行监控记录");
               }catch(InterruptedException e){
                   e.printStackTrace();
                   //重新设置打断标记
                   //current.interrupt();
               }
           } 
        },"monitor");
        monitor.start();
    }
    //停止监控线程
    public void stop(){
        //monitor.interrupt();
        stop=true;
    }
}
```

 

### 不推荐方法

还有一些不推荐的方法，这些方法已过时，容易破坏同步代码块，造成线程死锁

| 方法名    | 功能说明             |
| --------- | -------------------- |
| stop()    | 停止线程运行         |
| suspend() | 挂起（暂停）线程运行 |
| resume()  | 恢复线程运行         |

### 主线程和守护线程

默认情况下，Java进程需要等待所有的线程都运行结束，才会结束。有一种特殊的线程叫做守护线程，只要其他非守护线程运行结束了，即使守护线程的代码没有执行完，也会强制结束。t1.setDaemon(true);设置为守护线程

+ 垃圾回收器线程就是一种守护线程
+ Tomcat中的Acceptor和Poller线程都是守护线程，所以Tomcat接收到shutdown命令 后，不会等待他们处理完当前请求

### 五种状态

操作系统层面来描述

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_2.png)

+ 初始状态：仅是在语言层面创建了线程对象，还未与操作系统线程关联
+ 可运行状态：（就绪状态）指该线程已经被创建（与操作系统线程关联），可以由CPU调度执行
+ 运行状态：指获取了CPU时间片运行中的状态，当CPU时间片用完，会从运行状态转换至可运行状态，会导致线程的上下文切换
+ 阻塞状态：如果调用了阻塞API，如BIO读写文件，这时线程实际不会用到CPU，会导致线程上下文切换，进入阻塞状态，等BIO操作完毕，会由操作系统唤醒阻塞的线程，转换至可运行状态，与可运行状态的区别是，对阻塞状态的线程来说，只要他们一直不唤醒，调度器就一直不会考虑调度他们
+ 终止状态：表示线程已经执行完毕，生命周期已经结束，不会再转换为其他状态

### 六种状态

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_3.png)

+ NEW：线程刚被创建，但是还没有调用start()方法
+ RUNNABLE：当调用了start()方法后，注意，Java API层面的RUNNABLE状态覆盖了操作系统层面的可运行状态，运行状态和阻塞状态（由于BIO导致的线程阻塞，在Java里无法区分，仍然认为是可运行的）
+ BLOCKED，WAITING，TIMED_WAITING：都是Java API层面对阻塞状态的细分
+ TERMINATED：当线程代码运行结束

**情况1 NEW --> RUNNABLE**

当调用t.start()方法时，由NEW --> RUNNABLE

**情况2 RUNNABLE <--> WAITING**

t线程用synchronized(obj)获取了对象锁后

+ 调用obj.wait()方法时，t线程从RUNNBALE -->WAITING
+ 调用obj.notify()，obj.notifyAll()，t.interrupt()时
  + 竞争锁成功，t线程从WAITING -->RUNNABLE
  + 竞争锁失败，t线程从WAITING -->BLOCKED

**情况3 RUNNABLE <--> WAITING**

+ 当前线程调用t.join()方法时，当前线程从RUNNABLE --> WAITING，注意是当前线程在t线程对象的监视器上等待
+ t线程运行结束，或调用了当前线程的interrupt()时，当前线程从WAITING --> RUNNABLE

**情况4 RUNNABLE <--> WAITING** 

+ 当前线程调用LockSupport.park()方法会让当前线程从RUNNABLE --> WAITING
+ 调用LockSupport.unpark(目标线程)或调用了线程的interrupt()，会让目标线程从WAITING --> RUNNBALE

**情况5 RUNNABLE <--> TIMED_WAITING**

t线程用synchronized(obj)获取了对象锁后

+ 调用obj.wait(long n)方法时，t线程从RUNNABLE -->TIMED_WAITING
+ t线程等待时间超过了n毫秒，或调用obj.notify()，obj.notifyAll()，t.interrupt()时
  + 竞争锁成功，t线程从TIMED_WAITING -->RUNNABLE
  + 竞争锁失败，t线程从TIMED_WAITING -->BLOCKED

**情况6 RUNNABLE <--> TIMED_WAITING**

+ 当前线程调用t.join(long n)方法时，当前线程从RUNNABLE -->TIMED_WAITING，注意是当前线程在t线程对象的监视器上等待
+ 当前线程等待时间超过了n毫秒，或t线程运行结束，或调用了当前线程的interrupt()时，当前线程从TIMED_WATING --> RUNNABLE

**情况7 RUNNABLE <--> TIMED_WAITING**

+ 当前线程调用Thread.sleep(long n)，当前线程从RUNNABLE --> TIMED_WAITING
+ 当前线程等待时间超过了n毫秒，当前线程从TIMED_WAITING --> RUNNABLE

**情况8 RUNNABLE <--> TIMED_WAITING**

+ 当前线程调用LockSupport.parkNanos(long nanos)或LockSupport.parkUntil(long millis)时，当前线程从RUNNABLE --> TIMED_WAITING
+ 调用LockSupport.unpark(目标线程)或调用了线程的interrupt()，或是等待超时，会让目标线程从TIMED_WAITING --> RUNNABLE

**情况9 RUNNABLE <--> BLOCKED**

+ t线程用synchronized(obj)获取了对象锁时如果竞争失败，从RUNNABLE -->BLOCKED
+ 持obj锁线程的同步代码块执行完毕，会唤醒该对象上所有BLOCKED的线程重新竞争，如果其中t线程竞争成功，从BLOCKED --> RUNNABLE，其他失败的线程仍然BLOCKED

**情况10 RUNNABLE <--> TERMINATED**

当前线程所有代码运行完毕，进入TERMINATED

### 临界区 Critical Section

一个程序运行多个线程本身是没有问题的，问题出在多个线程访问共享资源，多个线程读共享资源其实也没有问题，在多个线程对共享资源读写操作时发生指令交错，就会出现问题，一段代码块内如果存在对共享资源的多线程读写操作，称这段代码为临界区

```java
static int counter=0;
static void increment()
//临界区
{
    counter++;
}
static void decrement()
//临界区
{
    counter--;
}
```

### 竞态条件 Race Condition

多个线程在临界区执行，由于代码的执行序列不同而导致结果无法预测，称之为发生了竞态条件

为了避免临界区的竞态条件发生，有多种手段可以达到目的。

+ 阻塞式的解决方案：synchronized，Lock
+ 非阻塞式的解决方案：原子变量

### synchronized解决方案

阻塞式解决方案：synchronized，俗称对象锁，它采用互斥的方式让同一时刻至多只有一个线程能够持有对象锁，其他线程再想获取这个对象锁时就会阻塞住。这样就能保证拥有锁的线程可以安全的执行临界区内的代码，不用担心线程上下文切换

注意：

虽然Java中互斥和同步都可以采用synchronized关键字来完成，但他们还是有区别的：

+ 互斥是保证临界区的竞态条件发生，同一时刻只能有一个线程执行临界区代码
+ 同步是由于线程执行的先后、顺序不同，需要一个线程等待其他线程运行到某个点

### synchronized

**语法**

```java
synchronized(对象)
{
    临界区
}
```

```java
static int counter=0;
static Object lock=new Object();

Thread t1=new Thread(()->{
    for(int i=0;i<5000;i++){
        synchronized(lock){
            counter++;
        }
    }
},"t1");

Thread t2=new Thread(()->{
    for(int i=0;i<5000;i++){
        synchronized(lock){
            counter--;
        }
    }
},"t2");

t1.start();
t2.start();
```

**方法上的synchronized**

```java
class Test{
    public synchronized void test(){
        
    }
}
等价于
class Test{
    public void test(){
        synchronized(this){
            
        }
    }
}

class Test{
    public synchronized static void test(){
        
    }
}
等价于
class Test{
    public static void test(){
        synchronized(Test.class){
            
        }
    }
}   
```

### 所谓的"线程八锁"

其实就是考虑synchronized锁住的是哪个对象

情况1：结果：12或21

```java
class Number{
    public synchronized void a(){
        log.debug("1");
    }
    public synchronized void b(){
        log.debug("2");
    }
}
public static void main(String[] args){
    Number n1=new Number();
    new Thread(()->{n1.a();}).start();
    new Thread(()->{n1.b();}).start();
}
```

情况2：结果：一秒后12或2一秒后1

```java
class Number{
    public synchronized void a(){
        sleep(1);
        log.debug("1");
    }
    public synchronized void b(){
        log.debug("2");
    }
}
public static void main(String[] args){
    Number n1=new Number();
    new Thread(()->{n1.a();}).start();
    new Thread(()->{n1.b();}).start();
}
```

情况3：结果：3 1秒后12，23 1秒后1，32 一秒后1

```java
class Number{
    public synchronized void a(){
        sleep(1);
        log.debug("1");
    }
    public synchronized void b(){
        log.debug("2");
    }
    public void c(){
        log.debug("3");
    }
}
public static void main(String[] args){
    Number n1=new Number();
    new Thread(()->{n1.a();}).start();
    new Thread(()->{n1.b();}).start();
    new Thread(()->{n1.c();}).start();
}
```

情况4：结果：2一秒后1

```java
class Number{
    public synchronized void a(){
        sleep(1);
        log.debug("1");
    }
    public synchronized void b(){
        log.debug("2");
    }
}
public static void main(String[] args){
    Number n1=new Number();
    Number n2=new Number();
    new Thread(()->{n1.a();}).start();
    new Thread(()->{n2.b();}).start();
}
```

情况5：结果：2一秒后1

```java
class Number{
    public static synchronized void a(){
        sleep(1);
        log.debug("1");
    }
    public synchronized void b(){
        log.debug("2");
    }
}
public static void main(String[] args){
    Number n1=new Number();
    new Thread(()->{n1.a();}).start();
    new Thread(()->{n1.b();}).start();
}
```

情况6：结果：一秒后12或2一秒后1

```java
class Number{
    public static synchronized void a(){
        sleep(1);
        log.debug("1");
    }
    public static synchronized void b(){
        log.debug("2");
    }
}
public static void main(String[] args){
    Number n1=new Number();
    new Thread(()->{n1.a();}).start();
    new Thread(()->{n1.b();}).start();
}
```

情况7：结果：2一秒后1

```java
class Number{
    public static synchronized void a(){
        sleep(1);
        log.debug("1");
    }
    public synchronized void b(){
        log.debug("2");
    }
}
public static void main(String[] args){
    Number n1=new Number();
    Number n2=new Number();
    new Thread(()->{n1.a();}).start();
    new Thread(()->{n2.b();}).start();
}
```

情况8：结果：一秒后12或2一秒后1

```java
class Number{
    public static synchronized void a(){
        sleep(1);
        log.debug("1");
    }
    public static synchronized void b(){
        log.debug("2");
    }
}
public static void main(String[] args){
    Number n1=new Number();
    Number n2=new Number();
    new Thread(()->{n1.a();}).start();
    new Thread(()->{n2.b();}).start();
}
```

### 变量的线程安全分析

**成员变量和静态变量是否线程安全**

+ 如果他们没有共享，则线程安全
+ 如果他们被共享了，根据他们的状态是否能够改变，又分为两种
  + 如果只有读操作，则线程安全
  + 如果又读写操作，则这段代码是临界区，需要考虑线程安全

**局部变量是否线程安全**

+ 局部变量是线程安全的
+ 但局部变量引用的对象则未必
  + 如果该对象没有逃离方法的作用范围，他是线程安全的
  + 如果该对象逃离方法的作用范围，需要考虑线程安全

### 常见线程安全类

String、Integer、StringBuffer、Random、Vector、Hashtable、java.util.concurrent包下的类

这里说他们是线程安全的是指，多个线程调用他们同一个实例的某个方法时，是线程安全的。也可以理解为，他们的每个方法是原子的，但注意他们多个方法的组合不是原子的。

**不可变类线程安全性**

String、Integer等都是不可变类，因为其内部的状态不可以改变，因此他们的方法都是线程安全的

### Monitor（锁）

#### **java对象头**

普通对象

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_4.png)

数组对象

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_5.png)

32位jvm

其中Mark Word结构为

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_6.png)



![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_31.png)

Monitor被翻译为**监视器**或**管程**，**每个Java对象都可以关联一个Monitor对象**，如果使用synchronized给对象上锁（重量级）之后，该对象头的Mark Word就被设置指向Monitor对象的指针

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_7.png)

+ 刚开始Monitor中Owner为null
+ 当Thread-2执行synchronized(obj)就会将Monitor的所有者Owner置为Thread-2，Monitor中只能有一个Owner
+ 在Thread-2上锁的过程中，如果Thread-3，Thread-4，Thread-5也来执行synchronized(obj)，就会进入EntryList BLOCKED
+ Thread-2执行完同步代码块的内容，然后唤醒EntryList中等待的线程来竞争锁，竞争的是非公平的
+ 图中WaitSet中的Thread-0，Thread-1是之前获得过锁，但条件不满足进入WAITING状态的线程

注意：

+ synchronized必须是进入同一个对象的monitor才有上述的效果
+ 不加synchronized的对象不会关联监视器，不遵从以上规则

#### **synchronized原理**

##### **轻量级锁**

轻量级锁的使用场景：如果一个对象虽然有多线程访问，但多线程访问的时间是错开的（也就是没有竞争），那么可以使用轻量级锁来优化

轻量级锁对使用者是透明的，即语法仍然是synchronized

假设有两个方法同步块，利用同一个对象加锁

```java
static final Object obj=new Object();
public static void method1(){
    synchronized(obj){
		//同步块A
        method2();
    }
}
public static void method2(){
    synchronized(obj){
		//同步块B
    }
}
```

+ 创建锁记录（Lock Record）对象，每个线程的栈帧都会包含一个锁记录的结构，内部可以存储锁定对象的Mark Word

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_8.png)

+ 让锁记录中Object reference指向锁对象，并尝试用cas替换Object的Mark Word，将Mark Word的值存入锁记录

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_9.png)

+ 如果cas替换成功，对象头中存储了**锁记录地址和状态00**，表示由该线程给对象加锁，这时图示如下

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_10.png)

+ 如果cas失败，有两种情况
  + 如果是其他线程已经持有了该Object的轻量级锁，这时表明有竞争，进入锁膨胀过程
  + 如果是自己执行了synchronized锁重入，那么再添加一条Lock Record作为重入的计数

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_11.png)

+ 当退出synchronized代码块（解锁时），如果有取值为null的锁记录，表示有重入，这时重置锁记录，表示重入计数减一

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_12.png)

+ 当退出synchronized代码块（解锁时），锁记录的值不为null，这时使用cas将Mark Word的值恢复给对象头
  + 成功，则解锁成功
  + 失败，说明轻量级锁进行了锁膨胀或已经升级为重量级锁，进入重量级锁解锁流程



##### 锁膨胀

如果再尝试加轻量级锁的过程中，cas操作无法成功，这时一种情况就是有其他线程为此对象加上了轻量级锁（有竞争），这时需要进行膨胀，将轻量级锁变为重量级锁

```java
static Object obj=new Object();
public static void method1(){
    synchronized(obj){
        //同步块
    }
}
```

+ 当Thread-1进行轻量级加锁时，T和read-0已经对该对象加了轻量级锁

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_13.png)

+ 这时Thread-1加轻量级锁失败，进入锁膨胀流程
  + 即为Object对象申请Monitor锁，让Object指向重量级锁地址
  + 然后自己进入Monitor的EntryList BLOCKED

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_14.png)

+ 当Thread-0退出同步代码块解锁时，使用cas将Mark Word的值恢复给对象头，失败。这时会进入重量级锁流程，即按照Monitor地址找到Monitor对象，设置Owner为null，唤醒EntryList中的BLOCKED线程



##### 自旋优化

重量级锁竞争的时候，还可以使用自旋来进行优化，如果当前线程自旋成功（即这时候持锁线程已经退出了同步块，释放了锁），这时当前线程就可以避免阻塞。

自旋重试成功的情况

| 线程1（cpu1上）         | 对象Mark               | 线程2（cpu2上）         |
| ----------------------- | ---------------------- | ----------------------- |
| -                       | 10（重量锁）           | -                       |
| 访问同步块，获取monitor | 10（重量锁）重量锁指针 | -                       |
| 成功（加锁）            | 10（重量锁）重量锁指针 | -                       |
| 执行同步块              | 10（重量锁）重量锁指针 | -                       |
| 执行同步块              | 10（重量锁）重量锁指针 | 访问同步块，获取monitor |
| 执行同步块              | 10（重量锁）重量锁指针 | 自旋重试                |
| 执行完毕                | 10（重量锁）重量锁指针 | 自旋重试                |
| 成功（解锁）            | 01（无锁）             | 自旋重试                |
| -                       | 10（重量锁）重量锁指针 | 成功（加锁）            |
| -                       | 10（重量锁）重量锁指针 | 执行同步块              |
| -                       | ...                    | ...                     |

自旋重试失败的情况

| 线程1（cpu1上）         | 对象Mark               | 线程2（cpu2上）         |
| ----------------------- | ---------------------- | ----------------------- |
| -                       | 10（重量锁）           | -                       |
| 访问同步块，获取monitor | 10（重量锁）重量锁指针 | -                       |
| 成功（加锁）            | 10（重量锁）重量锁指针 | -                       |
| 执行同步块              | 10（重量锁）重量锁指针 | -                       |
| 执行同步块              | 10（重量锁）重量锁指针 | 访问同步块，获取monitor |
| 执行同步块              | 10（重量锁）重量锁指针 | 自旋重试                |
| 执行同步块              | 10（重量锁）重量锁指针 | 自旋重试                |
| 执行同步块              | 10（重量锁）重量锁指针 | 自旋重试                |
| 执行同步块              | 10（重量锁）重量锁指针 | 阻塞                    |
| -                       | ...                    | ...                     |

+ 在Java6之后自旋锁是自适应的，比如对象刚刚的一次自旋操作成功过，那么认为这次自旋成功的可能性会高，就多旋几次，反之，就少自旋甚至不自旋，总之，比较智能。
+ 自旋会占用cpu时间，单核cpu自旋就是浪费，多核cpu自旋才能发挥优势。
+ Java7之后不能控制是否开启自旋功能



##### 偏向锁

轻量级锁在没有竞争时（就自己这个线程），每次重入仍然需要执行cas操作。

Java6中引入了偏向锁来做进一步优化：只有第一次使用cas将线程id设置到对象的Mark Word头，之后发现这个线程id是自己的就表示没有竞争，不用重新cas，以后只要不发生竞争，这个对象就归该线程所有

```java
static Object obj=new Object();
public static void m1(){
    synchronized(obj){
        //同步块A
        m2();
    }
}
public static void m2(){
    synchronized(obj){
        //同步块B
        m3();
    }
}
public static void m3(){
    synchronized(obj){
        //同步块C
    }
}
```

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_15.png)

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_16.png)

**偏向状态**

64位JVM

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_17.png)

一个对象创建时：

+ 如果开启了偏向锁（默认开启），那么对象创建后，Mark Word值为0x05即最后三位为101，这时它的thread、epoch、age都为0，加VM参数 -XX:UseBiasedLocking 禁用偏向锁
+ 偏向锁默认是延迟的，不会在程序启动时立即生效，如果想避免延迟，可以加VM参数-XX:BiasedLockingStartupDelay=0来禁用延迟
+ 如果没有开启偏向锁，那么对象创建后，Mark Word值为0x01即最后三位为001，这时他的hashcode、age都为0，第一次用到hashcode时才会赋值

**撤销偏向状态：调用对象hashcode**

调用了对象的hashcode，但偏向锁的对象MarkWord中存储的是线程id，如果调用hashcode会导致偏向锁被撤销

+ 轻量级锁会在锁记录中记录hashcode
+ 重量级锁会在Monitor中记录hashcode

**撤销偏向状态：其他线程使用对象**

当其他线程使用偏向锁对象时，会将偏向锁升级为轻量级锁

**撤销偏向状态：调用wait-notify**

**批量重偏向**

如果对象虽然被多个线程访问，但没有竞争，这时偏向了线程T1的对象仍有机会重新偏向T2，重偏向会重置对象的ThreadID

当撤销偏向锁阈值超过20次后，jvm会这样觉得，我是不是偏向错了呢，于是会在给这些对象加锁时重新偏向至加锁线程

**批量撤销**

当撤销偏向锁阈值超过40次后，jvm会这样觉得，自己确实偏向错了，根本就不该偏向，于是整个类的所有对象都会变为不可偏向的，新建的对象也是不可偏向的

##### 锁消除

JIT编译器在编译的时候，进行逃逸分析。分析synchronized锁对象是不是只可能被一个线程加锁，不存在其他线程来竞争加锁的情况。这时就可以消除该锁了，提升执行效率。

禁用锁消除  VM参数 -XX:-EliminateLocks

##### 锁粗化

JIT编译时，发现一段代码中频繁的加锁释放锁，会将前后的锁合并为一个锁，避免频繁加锁释放锁

### wait notify

#### 原理之wait/notify

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_18.png)

+ Owner线程发现条件不满足，调用wait方法，即可进入WaitSet变为WAITING状态
+ BLOCKED和WAITING的线程都处于阻塞状态，不占用cpu时间片
+ BLOCKED线程会在Owner线程释放锁时唤醒
+ WAITING线程会在Owner线程调用notify或notifyALL时唤醒，但唤醒后并不意味着立刻获得锁，仍需进入EntryList重新竞争

#### API介绍

+ obj.wait()让进入object监视器的线程到waitSet等待
+ obj.notify()在object上正在waitSet等待的线程中挑一个唤醒
+ obj.notifyAll()让object上正在waitSet等待的线程全部唤醒

他们都是线程之间进行协作的手段，都属于Object对象的方法，必须获得此对象的锁，才能调用这几个方法

#### sleep(long n)和wait(long n)的区别

+ sleep是Thread方法，而wait是Object的方法
+ sleep不需要强制和synchronized配合使用，但wait需要和synchronized一起用
+ sleep在睡眠的同时，不会释放对象锁，但wait在等待的时候会释放对象锁
+ 他们的状态都是TIMED_WAITING

#### wait notify的正确姿势

```java
synchronized(lock){
    while(条件不成立){
        lock.wait();
    }
    //干活
}
//另一个线程
synchronized(lock){
    lock.notifyAll();
}
```

### 同步模式之保护性暂停

即Guarded Suspension，用在一个线程等待另一个线程的执行结果

+ 有一个结果需要从一个线程传递到另一个线程，让他们关联同一个GuardedObject
+ 如果有结果不断从一个线程到另一个线程那么可以使用消息队列（见生产者/消费者）
+ JDK中，join的实现，Future的实现，采用的就是此模式
+ 因为要等待另一方的结果，因此归类到同步模式

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_19.png)

### 异步模式之生产者/消费者模式

+ 与前面的保护性暂停中的GuardObject不同，不需要产生结果和消费结果的线程一一对应
+ 消息队列可以用来平衡生产和消费的线程资源
+ 生产者仅负责产生结果数据，不关心数据该如何处理，而消费者专心处理结果数据
+ 消息队列是有容量限制的，满时不会再加入数据，空时不会再消耗数据
+ JDK中各种阻塞队列，采用的就是这种模式

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_20.png)

### park&unpark

他们是LockSupport类中的方法

```java
//暂停当前线程
LockSupport.park();
//恢复某个线程的运行
LockSupport.unpark(暂停线程对象);
```

与Object的wait&notify相比

+ wait，notify和notifyAll必须配合Object Monitor一起使用，而park，unpark不必
+ park&unpark是以线程为单位来阻塞和唤醒线程，而notify只能随机唤醒一个等待线程，notifyAll是唤醒所有的线程，就不那么精确
+ park&unpark可以先unpark，而wait&notify不能先notify

#### 原理之park&unpark

每个线程都有自己的一个parker对象，由三部分组成 _counter,  _cond和 _mutex打个比喻

+ 线程就像一个旅人，Parker就像他随身携带的背包，_cond条件变量就好比背包中的帐篷， _counter好比背包中的干粮（0为耗尽，1为充足）
+ 调用park就是要看需不需要停下来休息
  + 如果备用干粮耗尽，那么钻进帐篷休息
  + 如果备用干粮充足，那么不需停留，继续前进
+ 调用unpark，就好比令干粮充足
  + 如果这时线程还在帐篷，就唤醒让他继续前进
  + 如果这时线程还在运行，那么下次他调用park时，仅是消耗掉备用干粮，不需停留继续前进
    + 因为背包空间有限，多次调用unpark仅会补充一份备用干粮

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_21.png)

1. 当前线程调用Unsafe.park()方法
2. 检查_counter, 本情况为0，这时，获得 _mutex互斥锁
3. 线程进入_cond条件变量阻塞
4. 设置_counter=0

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_22.png)

1. 调用Unsafe.unpark(Thread_0)方法，设置_counter为1
2. 唤醒_cond条件变量中的Thread _0
3. Thread_0恢复运行
4. 设置_counter=0

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_23.png)

1. 调用Unsafe.unpark(Thread_0)方法，设置_counter为1
2. 当前线程调用Unsafe.park()方法
3. 检查_counter，本情况为1，这时线程无需阻塞，继续运行
4. 设置_counter为0

### 多把锁

+ 好处，可以增强并发度
+ 坏处，如果一个线程需要同时获得多把锁，就容易发生死锁

```java
private final Object studyRoom=new Object();
private final Object bedRoom=new Object();
public void sleep(){
    synchronized(bedRoom){
        log.debug("sleeping2小时")
            Sleeper.sleep(2);
    }
}
public void study(){
    synchronized(studyRoom){
        log.debug("study1小时")
            Sleeper.sleep(1);
    }
}
```

### 线程的活跃性

#### 死锁

一个线程需要同时获得多把锁，就容易发生死锁

t1线程获得A对象锁，接下来想获取B对象的锁

t2线程获得B对象锁，接下来想获取A对象的锁

```java
Objec A=new Object();
Objec B=new Object();
Thread t1=new Thread(()->{
    synchronized(A){
        log.debug("lock A");
        sleep(1);
        synchronized(B){
            log.debug("lock B");
            log.debug("操作...");
        }
    }
},"t1");
Thread t2=new Thread(()->{
    synchronized(B){
        log.debug("lock B");
        sleep(0.5);
        synchronized(A){
            log.debug("lock A");
            log.debug("操作...");
        }
    }
},"t2");

t1.start();
t2.start();
```

#### 定位死锁

jps查看java进程id，jstack 进程id查看线程信息

jconsole

#### 活锁

活锁出现在两个线程互相改变对方的结束条件，最后谁也无法结束

可以增加随机睡眠时间使两个线程不用集中在一起执行来解决

```java
static volatile int count=10;
static final Object lock=new Object();
Thread t1=new Thread(()->{
    while(count>0){
        sleep(0.2);
        count--;
        log.debug("count:{}",count);
    }
},"t1").start();
Thread t2=new Thread(()->{
    while(count<20){
        sleep(0.2);
        count++;
        log.debug("count:{}",count);
    }
},"t2").start();
```

#### 饥饿

一个线程由于优先级太低，始终得不到CPU调度执行，也不能够结束

reentrantLock解决

### ReentrantLock

相对于synchronized它具备如下特点

+ 可中断
+ 可以设置超时时间，tryLock()
+ 可以设置为公平锁，默认是不公平的
+ 支持多个条件变量

与synchronized一样，都支持可重入

基本语法

```java
//获取锁
reentrantLock.lock();
try{
    //临界区
}finally{
    //释放锁
    reentrantLock.unlock();
}
```

#### 可重入

可重入是指同一个线程如果首次获得了这把锁，那么因为他是这把锁的拥有者，因此有权利再次获取这把锁

如果是不可重入锁，那么第二次获得锁时，自己也会被锁住

#### 可中断

```java
public class Test {
    private static ReentrantLock lock = new ReentrantLock();
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                //如果没有竞争那么此方法就会获取lock对象锁
                //如果有竞争就进入阻塞队列，可以被其他线程用interrupt方法打断
                log.debug("尝试获取锁");
                lock.lockInterruptibly();
            }catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("没有获得锁,返回")：
                return;
            }
            try {
                loeg.debug("获取到锁");
            }finally {
                lock.unlock();
            }
        },"t1");
        
        //lock.lock();
        t1.start();
    }
}
```

#### 锁超时

```java
public class Test {
    private static ReentrantLock lock = new ReentrantLock();
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            log.debug("尝试获得锁");
            try {
                if (!lock.tryLock(1,TimeUnit.SECONDS)) {
                	log.debug("获取不到锁");
                    return;
            	}
            } catch (InterruptedException e) {
                e.printStackTrace();
                log.debug("获取不到锁");
                return;
            } 
            
            try {
                log.debug("获得到锁");
            } finally {
                lock.unLock();
            }
        },"t1");
        
        lock.lock();
        log.debug("获得到锁");
        t1.start();
    }
}
```

#### 公平锁

ReentrantLock默认是不公平的

ReentrantLock lock = new ReentrantLock(false);// 设置为公平锁

公平锁一般没有必要，会降低并发度

#### 条件变量

synchronized中也有条件变量，就是waitSet,当条件不满足时进入waitSet等待

ReentrantLock的条件变量比synchronized强大之处在于，他是支持多个条件变量的，这就好比

+ synchronized是那些不满足条件的线程都在一间休息室等消息
+ 而ReentrantLock支持多间休息室，有专门等烟的休息室、专门等早餐的休息室，唤醒时也是按休息室来唤醒

使用流程

+ await前需要获得锁
+ await执行后，会释放锁，进入conditionObject等待
+ await的线程被唤醒（或打断、或超时）取重新竞争lock锁
+ 竞争lock锁成功后，从await后继续执行

```java
static final Object room=new Object();
static ReentrantLock lock=new ReentrantLock();
static Condition waitCigaretteSet=lock.new Condition();
static Condition waitBreakfastSet=lock.new Condition();
static volatile boolean hasCigarette=false;
static volatile boolean hasBreakfast=false;

new Thread(()->{
    lock.lock();
    try{
        log.debug("有烟没？[{}]",hasCigarette);
        while(!hasCigarette){
            log.debug("没烟，先歇会！")
                try{
                    waitCigaretteSet.await();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
        }
        log.debug("有烟没？[{}]",hasCigarette);
        log.debug("可以开始干活了")
    }finally{
        lock.unlock();
    }
},"小南").start();
new Thread(()->{
    lock.lock();
    try{
        log.debug("外卖到了没？[{}]",hasBreakfast);
        while(!hasBreakfast){
            log.debug("没外卖，先歇会！")
                try{
                    waithasBreakfastSet.await();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
        }
        log.debug("有外卖没？[{}]",hasBreakfast);
        log.debug("可以开始干活了")
    }finally{
        lock.unlock();
    }
},"小女").start();

new Thread(()->{
    lock.lock();
    try{
        hasBreakfast=true;
        waitBreakfastSet.signal();
    }finally{
        lock.unlock();
    }
},"送外卖的").start;
new Thread(()->{
    lock.lock();
    try{
        hasCigarette=true;
        waitCigaretteSet.signal();
    }finally{
        lock.unlock();
    }
},"送烟的").start;
```

### 同步模式之顺序控制

#### 固定运行顺序

**wait notify 版**

```java
public class Test {
    static final Object lock = new Object();
    //表示t2是否运行过
    static boolean t2runned = false;
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized(lock) {
                while (!t2.runned) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("1");
            }
        },"t1");
        
        Thread t2 = new Thread(() -> {
            synchronnized (lock) {
                log.debug("2");
                t2runned = true;
                lock.notify();
            }
        },"t2");
        
        t1.start();
        t2.start();
    }
}
```

**park unpark 版**

```java
public class Test {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
           LockSupport.park();
           log.debug("1");
        },"t1");
        
        Thread t2 = new Thread(() -> { 
            log.debug("2");
            LockSupport.unpark(t1);
        },"t2");
        
        t1.start();
        t2.start();
    }
}
```

#### 交替输出

**wait notify 版**

输出abcabcabcabcabc

```java
public class Test {
    public static void main(String[] args) {
        WaitNotify wn = new WaitNotify(1,5);
        new Thread(() -> {
            wn.print("a",1,2);
        }).start();
        new Thread(() -> {
            wn.print("b",2,3);
        }).start();
        new Thread(() -> {
            wn.print("c",3,1);
        }).start();
    }
    
    class WaitNotify {
        public void print(String str, int waitFlag, int nextFlag) {
        	for (int i = 0; i < loopNumber; i++) {
                synchronized (this) {
                while(flag != waitFlag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(str);
                flag = nextFlag;
                this.notifyAll();
            }
            }
        }
        
        //等待标记
        private int flag;
        //循环次数
        private int loopNumber;
        
        public WaitNotify(int flag, int loopNumber) {
            this.flag = flag;
            this.loopNumber = loopNumber;
        }
    }
}
```

**await signal 版**

```java
public class Test {
    public static void main(String[] args) {
        AwaitSignal awaitSignal = new AwaitSignal(5);
        Condition a = awaitSignal.newCondition();
        Condition b = awaitSignal.newCondition();
        Condition c = awaitSignal.newCondition();
         new Thread(() -> {
            wn.print("a",a,b);
        }).start();
        new Thread(() -> {
            wn.print("b",b,c);
        }).start();
        new Thread(() -> {
            wn.print("c",c,a);
        }).start();
        
        Thread.sleep(1000);
        awaitSignal.lock();
        try {
            System.out.println("开始...");
            a.signal();
        } finally {
            awaitSignal.unlock();
        }
    }
    
    class AwaitSignal extends ReentrantLock {
        private int loopNumber;
        
        public AwaitSignal(int loopNumber) {
            this.loopNumber = loopNumber;
        }
        
        public void print(String str, Condition current, Condition next) {
            for (int i = 0; i < loopNumber; i++) {
                lock();
                try {
                    current.await();
                    System.out.print(str);
                    next.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    unlock();
                }
            }
        }
    }
}
```

**park unpark 版**

```JAVA
public class Test {
    static Thread t1;
    static Thread t2;
    static Thread t3;
    public static void main(String[] args) {
        ParkUnpark pu = new ParkUnpark(5);
        t1 = new Thread(() -> {
            pu.print("a",t2);
        });
        t2 = new Thread(() -> {
            pu.print("b",t3);
        });
        t3 = new Thread(() -> {
            pu.print("c",t1);
        });
        t1.start();
        t2.start();
        t3.start();
        
        LockSupport.unpark(t1);
    }
}

class ParkUnpark {
    public void print(String str, Thread next) {
        for (int i = 0; i < loopNumber; i++) {
            LockSupport.park();
            System.out.println(str);
            LockSupport.unpark(next);
        }
    }
    
    private int loopNumber;
    
    public ParkUnpark(int loopNumber) {
        this.loopNumber = loopNumber;
    }
}
```

### JMM

JMM即Java Memory Model，Java内存模型，它定义了主存，工作内存抽象概念，底层对应着CPU寄存器、缓存、硬件内存、CPU指令优化等。

JMM主要体现在以下几个方面

+ 原子性-保证指令不会受到线程上下文切换的影响
+ 可见性-保证指令不会受cpu缓存的影响
+ 有序性-保证指令不受cpu指令并行优化的影响

#### 可见性

**退不出的循环**

先看一个现象，main线程对run变量的修改对于t线程不可见，导致了t线程无法停止

```java
static boolean run=true;
Thread t=new Thread(()->{
    while(run){
        //...
    }
});
t.start();
sleep(1);
run=false;//线程t不会如预想的停下来
```

为什么呢？分析一下

1. 初始状态，t线程刚开始从主内存读取run的值到工作内存

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_24.png)

2. 因为t线程要频繁从主内存中读取run的值，JIT编译器会将run的值缓存至自己工作内存中的高速缓存中，减少对主存中run的访问，提高效率

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_25.png)

3. 1秒之后，main线程修改了run的值，并同步至主存，而t是从自己工作内存中的高速缓存中读取这个变量的值，结果永远是旧值

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_26.png)

**解决办法**

volatile（易变关键字）

它可以用来修饰成员变量和静态成员变量，它可以避免线程从自己的工作缓存中查找变量的值，必须到主存中获取它的值，线程操作volatile变量都是直接操作主存

synchronized也可解决可见性，但需要创建Monitor属于重量级的操作

#### 可见性vs原子性

前面例子体现的实际就是可见性，他保证的是多个线程之间，一个线程对volatile变量的修改对宁一个线程可见，不能保证原子性，仅用在一个写线程，多个读线程的情况

synchronized语句块既能保证代码块的原子性，也同时保证代码块内变量的可见性(前提是这个变量都交给synchronized管理)。但缺点是synchronized是属于重量级操作，性能相对更低

#### 有序性

JVM会在不影响正确性的前提下，可以调整语句的执行顺序，这种特性称为指令重排，多线程下指令重排会影响正确性

**指令重排序优化**

事实上，现代处理器会设置为一个时钟周期完成一条执行时间最长的CPU指令。为什么这样做呢？可以想到指令还可以再划分成一个个更小的阶段，例如，每条指令都可以分为：取指令-指令译码-执行指令-内存访问-数据写回，五个阶段

现代CPU支持多级指令流水线，例如支持同时执行 取指令-指令译码-执行指令-内存访问-数据写回 的处理器，就可以称之为五级指令流水线，此时CPU可以在一个时钟周期内，同时运行五条指令的不同阶段（相当于一条执行时间最长的复杂指令），IPC-1，本质上，流水线技术并不能缩短单条指令的执行时间，但它变相地提高了指令的吞吐率

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_27.png)

### volatile原理

volatile的底层实现原理是内存屏障，Memory Barrier(Memory Fence)

+ 对volatile变量的写指令后会加入写屏障
+ 对volatile变量的读指令前会加入读屏障

**如何保证可见性**

+ 写屏障（sfence）保证在该屏障之前的，对共享变量的改动，都同步到主存当中

  ```java
  public void actor2(I_Result r) {
      num = 2;
      ready = true; // ready是volatile赋值带写屏障
      //写屏障
  }
  ```

+ 而读屏障（lfence）保证在该屏障之后，对共享变量的读取，加载的是主内存中最新的数据

  ```java
  public void actor2(I_Result r) {
     //读屏障
     //ready是volatile读取值带读屏障
      if (ready) {
          r.r1 = num + num;
      } else {
          r.r1 = 1;
      }
  }
  ```

**如何保证有序性**

+ 写屏障会保证指令重排序时，不会将写屏障之前的代码排在写屏障之后
+ 读屏障会保证指令重排序时，不会将读屏障之后的代码排在读屏障之前

**不能解决指令交错**：

+ 写屏障仅仅是保证之后的读能够读到最新的结果，但不能保证读跑到它前面去
+ 而有序性的保证也只是保证了本线程内相关代码不被重排序

### **happends-before规则**

happens-before规定了对共享变量的写操作对其他线程的读操作可见，他是可见性与有序性的一套规则总结，抛开以下happends-before规则，JMM并不能保证一个线程对共享变量的写，对于其他线程对该共享变量的读可见

+ 线程解锁m之前对变量的写，对于接下来对m加锁的其他线程对该变量的读可见

```java
static int x;
static Object m=new Object();

new Thread(()->{
    synchronized(m){
        x=10
    }
},"t1").start();

new Thread(()->{
    synchronized(m){
        System.out.println(x);
    }
},"t2").start();
```

+ 线程对volatile变量的写，对接下来其他线程对该变量的读可见

```java
volatile static int x;

new Thread(()->{   
     x=10    
},"t1").start();

new Thread(()->{
     System.out.println(x);
},"t2").start();
```

+ 线程start前对变量的写，对该线程开始后对该变量的读可见

```java
static int x;
x=10;

new Thread(()->{
     System.out.println(x);
},"t2").start();
```

+ 线程结束前对变量的写，对其他线程得知它结束后的读可见（比如其他线程调用t1.isAlice()或t1.join()等待它结束）

```java
static int x;

Thread t1=new Thread(()->{
    x=10;
},"t1");
t1.start();

t1.join();
System.out.println(x);
```

+ 线程t1打断t2（interrupt）前对变量的写，对于其他线程得知t2被打断后对变量的读可见（通过t2.interrupted()或t2.isInterrupted()）

```java
static int x;

public static void main(String[] args){
    Thread t2=new Thread(()->{
        while(true){
            if(Thread.currentThread().isInterrupted()){
                System.out.println(x);
                break;
            }
        }
    },"t2");
    t2.start();
    
    new Thread(()->{
        sleep(1);
        x=10;
        t2.interrupt();
    },"t1").start();
    
    while(!t2.isInterrupted()){
        Thread.yield();
    }
    Sytem.out.println(x);
}
```

+ 对变量默认值（0，false，null）的写，对其他线程对该变量的读可见
+ 具有传递性，如果x hb-> y并且y hb-> z那么有x hb-> z，配合volatile的防指令重排，有下面的例子

```java
volatile static int x;
static int y;

new Thread(()->{
    y=10;
    x=20;
},"t1").start();

new Thread(()->{
    //x=20对t2可见，同时y=10也对t2可见
    Sytem.out.println(x);
},"t2").start();
```

### 共享模型之无锁

#### **CAS和volatile**

前面看到的AtomicInteger的解决方法，内部并没有用锁来保护共享变量的线程安全。

```java
AtomicInteger balance;
public void withdraw(Integer amount){
    while(true){
        int prev=balance.get();
        int next=prev-amount;
        if(balance.compareAndSet(prev,next)){
            break;
        }
    }
}
```

其中的关键是compareAndSet，简称CAS（或Compare And Swap），他必须是原子操作

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_28.png)

**注意：**

其实CAS的底层是lock cmpxchg指令(X86架构)，在单核CPU和多核CPU下都能保证 比较-交换 的原子性

+ 在多核状态下，某个执行到带lock的指令时，CPU会让总线锁住，当这个核把此指令执行完毕，再开启总线。这个过程中不会被线程的调度机制锁打断，保证了多个线程对内存操作的准确性，是原子的。

#### **volatile**

获取共享变量时，为了保证该变量的可见性，需要使用volatile修饰

它可以用来修饰成员变量核静态变量，它可以避免线程从自己的工作缓存中查找变量的值，必须到主存中获取它的值，线程操作volatile变量都是直接操作主存。即一个线程对volatile变量的修改，对另一个线程可见，volatile仅仅保证了共享变量的可见性，让其他线程能够看到最新值，但不能解决指令交错问题（不能保证原子性）

CAS必须借助volatile才能读取到共享变量的最新值来实现比较并交换的效果

#### 为什么无锁效率高

+ 无锁情况下，即使重试失败，线程始终都在高速运行，没有停歇，而synchronized会让线程在没有获得锁的时候，发生上下文切换，进入阻塞，打个比喻
+ 线程就好像高速跑道上的赛车，高速运行时，速度极快，一旦发生上下文切换，就好比赛车要减速、熄火，等被唤醒又得重新打火、启动、加速...恢复到高速运行，代价比较大
+ 但无锁情况下，因为线程要保持运行，需要额外CPU支持，CPU在这里就好比高速跑道，没有额外的跑道，线程想高速运行也无从谈起，虽然不会进入阻塞，但由于没有分到时间片，仍然会进入可运行状态，还是会导致上下文切换

#### CAS的特点

结合CAS和volatile可以实现无锁并发，适用于线程数少、多核CPU的场景下

+ CAS是基于乐观锁的思想：最乐观的估计，不怕别的线程来修改共享变量，就算改了也没关系，我吃亏点再重试呗
+ synchronized是基于悲观锁的思想：最悲观的估计，得防着其他线程来修改共享变量，我上了锁你们都别想改，我改完了解开锁，你们才有机会

+ CAS体现的是无锁并发、无阻塞并发
  + 因为没有使用synchronized，所以线程不会陷入阻塞，这是效率提升的因素之一
  + 但如果竞争激烈，可以想到重试必然频繁发生，反而效率会受影响

### 原子整数

JUC并发包提供了AtomicBoolean，AtomicInteger，AtomicLong

### 原子引用

AtomicReference，AtomicMarkableReference，AtomicStampedReference

#### ABA问题

主线程仅能判断出共享变量的值与最初值A是否相同，不能感知到这种从A改为B又改回A的情况，如果主线程希望：

只要有其他线程动过了共享变量，那么自己的cas就算失败，这时，仅比较值是不够的，需要再加一个版本号

AtomicStampedReference可以给原子引用加上版本号，追踪原子引用整个的变化过程，如：A->B->A->C，通过AtomicStampedReference，我们可以知道，引用变量中途被更改了几次。但是有时候，并不关心引用变量更改了几次，只是单纯的关心是否更改过，所以就有了AtomicMarkableReference

### 原子数组

AtomicIntegerArray，AtomicLongArray，AtomicReferenceArray

### 字段更新器

AtomicReferenceFieldUpdater，AtomicIntegerFieldUpdater，AtomicLongFieldUpdater，利用字段更新器，可以针对对象的某个域（Field）进行原子操作，只能配合volatile修饰的字段使用，否则会出现异常

### 原子累加器

LongAdder，LongAccumulator，DoubleAdder，DoubleAccumulator

性能比原子整数高，在有竞争时，设置多个累加单元，Thread-0累加Cell[0]，而Thread-1累加Cell[1]...最后将结果汇总。这样他们在累加时操作的不同的Cell变量，因此减少了CAS重试失败，从而提高了性能。

#### LongAdder源码

LongAdder类有几个关键域

```java
//累加单元数组，懒惰初始化
transient volatile Cell[] cells;

//基础值，如果没有竞争，则用cas累加这个域
transient volatile long base;

//在cells创建或扩展时，置为1，表示加锁
transient volatile int cellsBusy;
```

**cas锁**

```java
//不要用于实践
private class LockCas{
    private AtomicInteger state=new AtomicInteger(0);
    public void lock(){
        while(true){
            if(state.compareAndSet(0,1)){
                break;
            }
        }
    }
}
```

#### 伪共享

一个缓存行加入多个Cell对象

其中Cell即为累加单元

```java
//注解防止缓存行伪共享
@sun.misc.Contended
    static final class Cell{
        volatile long value;
        Cell(long x){value=x;}
        
        //最重要的方法，用来cas方式进行累加，prev表示旧值，next表示新值
        final boolean cas(long prev,long next){
            return UNSAFE.compareAndSwapLong(this,valueOffset,prev,next);
        }
        //省略不重要代码
    }
```



缓存与内存速度比较

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_29.png)

| 从cpu到 | 大约需要的时钟周期            |
| ------- | ----------------------------- |
| 寄存器  | 1cycle（4GHz的CPU约为0.25ns） |
| L1      | 3-4cycle                      |
| L2      | 10-20cycle                    |
| L3      | 40-45cycle                    |
| 内存    | 120-240cycle                  |

因为CPU与内存的速度差异很大，需要靠预读数据至缓存来提升效率

而缓存以缓存行为单位，每个缓存行对应着一块内存，一般是64byte(8个long)

缓存的加入会造成数据副本的产生，即同一份数据会存在不同核心的缓存行中

CPU要保证数据的一致性，如果某个CPU核心更改了数据，其他CPU核心对应的整个缓存行必须失效

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_30.png)

因为Cell是数组形式，在内存中可以连续存储，一个Cell为24字节（16字节的对象头和8字节的value），因此缓存行可以存下2个的Cell对象。这样问题来了：

+ Core-0要修改Cell[0]
+ Core-1要修改Cell[1]

无论谁修改成功，都会导致对方Core的缓存行失效，比如Core-0中的Cell[0]=6000，Cell[1]=8000 要累加Cell[0]=6001，Cell[1]=8000，这时会让Core-1的缓存行失效

@sun.misc.Contended用来解决这个问题，它的原理是在使用此注解的对象或字段的前后各增加128字节大小的padding，从而让CPU将对象预读至缓存时占用不同的缓存行，这样，不会造成对方缓存行的失效

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_32.png)

### Unsafe

Unsafe对象提供了非常底层的，操作内存、线程的方法，Unsafe对象不能直接调用，只能通过反射获得

### 共享模型之不可变

#### 不可变设计

String类是不可变的

**final的使用**

String类、类中所有属性都是final的

+ 属性用final修饰保证了该属性是只读的，不能修改
+ 类用final修饰保证了该类中的方法不能被覆盖，防止子类无意间破坏不可变性

#### **保护性拷贝**

使用字符串时，也有一些跟修改相关的方法，比如substring，那么下面就看一看是如何实现的

```java
public String substring(int beginIndex){
    if(beginIndex<0){
        throw new StringIndexOutOfBoundException(beginIndex);
    }
    int subLen=value.length-beginIndex;
    if(subLen<0){
        throw new StringIndexOutOfBoundException(subLen);
    }
    return (beginIndex==0) ? this : new String(value,beginIndex,subLen);
}
```

发现其内部是调用String的构造方法创建了一个新字符串，再进入这个构造看看，是否对final char[] value做出了修改：

```java
public String(char value[],int offset,int count){
    if(offset<0){
        throw new StringIndexOutOfBoundException(offset);
    }
    if(count<=0){
        if(count<0){
            throw new StringIndexOutOfBoundException(count);
        }
        if(offset<=value.length){
            this.value="".value;
            return
        }
    }
    if(offset>value.length-count){
        throw new StringIndexOutOfBoundException(offset+count);
    }
    this.value=Arrays.copyOfRange(value,offset,offset+count);
}
```

结果发现也没有，构造新字符串对象时，会生成新的char[] value，对内容进行复制。这种通过创建副本对象来避免共享的手段称之为保护性拷贝

#### 享元模式

享元模式是通过和其他相同的对象分享尽可能多的数据来最小化内存的使用

**体现**

+ **包装类**

在JDK中Boolean,Byte,Short,Integer,Long,Character等包装类提供了valueOf方法，例如Long的valueOf会缓存-128~127之间的Long对象，在这个范围之间会重用对象，大于这个范围，才会新建对象

Byte,Short,Long缓存的范围是-128~127

Character缓存的范围是0~127

Integer的默认范围是-128~127，最小值不能变，但最大值可以通过调整虚拟机参数-Djava.lang.Integer.IntegerCache.high来改变

Boolean缓存了TRUE和FALSE

+ **String串池**

+ **BigDecimal，BigInteger**

### final原理

#### 设置final变量的原理

理解了volatile原理，再对比final的实现就比较简单了，final变量的赋值也会通过putfield指令来完成，同样在这条指令之后也会加入写屏障，保证在其他线程读到他的值时不会出现为0的情况

### 无状态

在web阶段学习时，设计servlet时为了保证其线程安全，都会有这样的建议，不要为servlet设置成员变量，这种没有任何成员变量的类是线程安全的，因为成员变量保存的数据也可以称为状态信息，因此，没有成员变量就称之为无状态

### 自定义线程池

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_33.png)

```java
//手写线程池
public class ThreadPool{
    //任务队列
    private BlockingQueue<Runnable> taskQueue;
    //线程集合
    private HashSet<Worker> workers=new HashSet<>();
    //核心线程数
    private int coreSize;
    //获取任务的超时时间
    private long timeout;
    
    private TimeUnit timeUnit;
    
    //执行任务
    public void execute(Runnable task){
        //当任务数没有超过coreSize时，直接交给worker对象执行
        //当任务数超过coreSzie时，加入任务队列暂存
        synchronized(workers){
             if(workers.size()<coreSize){
                Worker worker=new Worker(task);
                log.debug("新增worker{},{}",worker,task);
                workers.add(worker);
                worker.start();
            }else{
                log.debug("加入任务队列{}",task);
                taskQueue.put(task);
            }
        }
    }
    
    public ThreadPool(int coreSize,long timeout,TimeUnit timeUnit,int queueCapacity){
        this.coreSize=coreSize;
        this.timeout=timeout;
        this.timeUnit=timeUnit;
        this.taskQueue=new BlockingQueue<>(queueCapacity);
    }
        
    class Worker extends Thread{
        private Runnable task;
        
        public Worker(Runnable task){
            this.task=task;
        }
        
        public void run(){
            //执行任务
            //当task不为空，执行任务
            //当task执行完毕，再接着从任务队列获取任务并执行
            while(task!=null||(task=taskQueue.take())!=null{
                try{
                    log.debug("正在执行...{}",task);
                    task.run();
                }catch(Exception e){
                    e.printStackTrace();
                }finally{
                    task=null;
                }
            }
            synchronized(workers){
                log.debug("worker被移除{}"，this);
                workers.remove(this);
            }      
        }
    }
}

class BlockingQueue<T>{
    //1.任务队列
    private Deque<T> queue=new ArrayDeque();
    //2.锁
    private ReentrantLock lock=new ReentrantLock();
    //3.生产者条件变量
    private Condition fullWaitSet=lock.newCondition();
    //4.消费者条件变量
    private Condition emptyWaitSet=lock.newCondition();
    //5.容量
    private int capacity;
    
    public BlockingQueue(int capacity){
        this.capacity=capacity;
    }
    //带超时的阻塞获取
    public T poll(long timeout,TimeUnit unit){
        lock.lock();
        try{
            //将timeout统一转换为纳秒
            long nanos=unit.toNanos(timeout);
            while(queue.isEmpty()){
               try{
                   //返回的是剩余时间
                   if(nanos<=0){
                       return null; 
                   }
                   emptWaitSet.awaitNanos(nanos);
               }catch(InterruptedException e){
                   e.printStackTrace();
               }                 
            }
            T t=queue.removeFirst();
            fullWaitSet.signal();
            return t;
        }finally{
            lock.unlock();
        }
    }
    //阻塞获取
    public T take(){
        lock.lock();
        try{
            while(queue.isEmpty()){
               try{
                   emptWaitSet.await();
               }catch(InterruptedException e){
                   e.printStackTrace();
               }                 
            }
            T t=queue.removeFirst();
            fullWaitSet.signal();
            return t;
        }finally{
            lock.unlock();
        }
    }
    //阻塞添加
    public void put(T element){
        lock.lock();
        try{
            while(queue.size==capacity){
                try{
                    fullWaitSet.await();
                }catch(InterruptedException e){
                    e.ptintStackTrace();
                }
            queue.addLast(element);
            emptyWaitSet.signal();
            }
        }finally{
            lock.unlock();
        }
    }
    //获取大小
    public int size(){
        lock.lock();
        try{
            return queue.size();
        }finally{
            lock.unlock();
        }
    }
}
```

### ThreadPoolExecutor

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_34.png)

#### 线程池状态

ThreadPoolExecutor使用int的高3位来表示线程池状态，低29位表示线程数量

| 状态名     | 高3位 | 接收新任务 | 处理阻塞队列任务 | 说明                                      |
| ---------- | ----- | ---------- | ---------------- | ----------------------------------------- |
| RUNNING    | 111   | Y          | Y                |                                           |
| SHUTDOWN   | 000   | N          | Y                | 不会接收新任务，但会处理阻塞队列剩余任务  |
| STOP       | 001   | N          | N                | 会中断正在执行的任务，并抛弃阻塞队列任务  |
| TIDYING    | 010   | -          | -                | 任务全部执行完毕，活动线程为0即将进入终结 |
| TERMINATED | 011   | -          | -                | 终结状态                                  |

从数字上比较，TERMINATED>TIDYING>STOP>SHUTDOWN>RUNNING

这些信息存储在一个原子变量ctl中，目的是将线程池状态和线程个数合二为一，这样就可以用一次cas原子操作进行赋值

```java
//c为旧值，ctlOf返回结果为新值
ctl.compareAndSet(c,ctlOf(targetState,workerCountOf(c)));
//rs为高3位代表线程池状态，wc为低29位代表线程个数，ctl是合并他们
private static int ctlOf(int rs,int wc){return rs|wc;}
```

#### 构造方法

```java
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit;
                          BlockingQueue<Runnable> workQueue,
                          ThreadFactory threadFactory,
                          RejectedExecutionHandler handler
                         )
```

+ corePoolSize核心线程数目（最多保留的线程数）
+ maximumPoolSize最大线程数目
+ keepAliveTime生存时间，针对救急线程
+ unit时间单位，针对救急线程
+ workQueue阻塞队列
+ threadFactory线程工厂，可以为线程创建时起个名字
+ handler拒绝策略

工作方式：

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_35.png)

+ 线程池中刚开始并没有线程，当一个任务提交给线程池后，线程池会创建一个新线程来执行任务。
+ 当线程数达到corePoolSize并没有线程空闲，这时再加入任务，新的任务会被加入到workQueue队列排队，直到有空闲的线程。
+ 如果队列选择了有界队列，那么任务超过了队列大小时，会创建maximumPoolSize-corePoolSize数目的线程来救急。
+ 如果线程到达maximumPoolSize仍然有新任务这时会执行拒绝策略。拒绝策略jdk提供了4种实现，其他著名框架也提供了实现
  + AbortPolicy让调用者抛出RejectedExecutionException异常，这是默认策略
  + CallerRunsPolicy让调用者运行任务 
  + DiscardPolicy放弃本次任务
  + DiscardOldestPolicy放弃队列中最早的任务，本任务取而代之
  + Dubbo的实现，在抛出RejectedExecutionException异常之前会记录日志，并dump线程栈信息，方便定位问题
  + Netty的实现，是创建一个新线程来执行任务
  + ActiveMQ的实现，带超时等待（60s）尝试放入队列
  + PinPoint的实现，它使用了一个拒绝策略链，会逐一尝试策略链中每种拒绝策略
+ 当高峰过去后，超过corePoolSize的救急线程如果一段时间没有任务做，需要结束节省资源，这个时间由keepAliveTime和unit控制

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_36.png)

#### newFixedThreadPool

```java
public static ExecutorService newFixedThreadPool(int nThreads){
    return new ThreadPoolExecutor(nThreads,nThreads,0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
}
```

特点

+ 核心线程数==最大线程数（没有救急线程被创建），因此也无需超时时间
+ 阻塞队列是无界的，可以放任意数量的任务
+ 适用于任务量已知，相对耗时的任务

#### newCachedThreadPool

```java
public static ExecutorService newCachedThreadPool(){
    return new ThreadPoolExecutor(0,Integer.MAX_VALUE,60L,TimeUnit.SECONDS,new SynchronousQueue<Runnable>());
}
```

特点

+ 核心线程数是0，最大线程数是Integer.MAX_VALUE，救急线程的空闲生存时间是60s，意味着
  + 全部都是救急线程（60s后可以回收）
  + 救急线程可以无限创建
+ 队列采用了SynchronousQueue实现特点是，他没有容量，没有线程来取是放不进去的（一手交钱、一手交货）

+ 整个线程池表现为线程数会根据任务量不断增长，没有上限，当任务执行完毕，空闲一分钟后释放线程。适合任务数比较密集，但每个任务执行时间较短的情况。

#### newSingleThreadExecutor

```java
public static ExecutorService newSingleThreadExecutor(){
    return new FinalizableDelegatedExecutorService(new ThreadPoolExecutor(1,1,0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>()));
}
```

使用场景：

希望多个任务排队执行，线程数固定为1，任务数多于1时，会放入无界队列排队。任务执行完毕，这唯一的线程也不会被释放。

区别：

+ 自己创建一个单线程串行执行任务，如果任务执行失败而终止那么没有任何补救措施，而线程池还会新建一个线程，保证池的正常工作
+ Executors.newSingleThreadExecutor()线程个数始终为1，不能修改
  + FinalizableDelegatedExecutorService应用的是装饰器模式，只对外暴露了ExecutorService接口，因此不能调用ThreadPoolExecutor中特有的方法
+ Executors.newFixedThreadPool(1)初始时为1，以后还可以修改
  + 对外暴露的是ThreadPoolExecutor对象，可以强转后调用setCorePoolSize等方法进行修改

#### 提交任务

```java
//执行任务
void execute(Runnable command);

//提交任务task,用返回值Future获得任务执行结果
<T> Future<T> submit(Callable<T> task);

//提交tasks中所有的任务
<T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException;

//提交tasks中所有的任务，带超时时间
<T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,long timeout,TimeUnit unit) throws InterruptedException;

//提交tasks中所有的任务，哪个任务先成功执行完毕，返回此任务执行结果，其他任务取消
<T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException,ExecutionException;

//提交tasks中所有的任务，哪个任务先成功执行完毕，返回此任务执行结果，其他任务取消，带超时时间
<T> T invokeAny(Collection<? extends Callable<T>> tasks,long timeout,TimeUnit unit) throws InterruptedException,ExecutionException,TimeoutException;
```

#### 关闭线程池

**shutdown**

```java
//线程池状态变为SHUTDOWN,不会接收新任务，但已提交的任务会执行完，此方法不会阻塞调用线程的执行
void shutdown();

public void shutdown(){
    final ReentrantLock mainLock=this.mainLock;
    mainLock,lock();
    try{
        checkShutdownAccess();
        //修改线程池状态
        advanceRunState(SHUTDOWN);
        //仅会打断空闲线程
        interruptIdleWorkers();
        onShutdown();//扩展点 ScheduledThreadPoolExecutor
    }finally{
        mainLock.unLock();
    }
    //尝试终结（没有运行的线程可以立刻终结，如果还有运行的线程也不会等）
    tryTerminate();
}
```

**shutdownNow**

```java
//线程池状态变为STOP,不会接收新任务，会将队列中的任务返回，并用interrupt的方法中断正在执行的任务
List<Runnable> shutdownNow();

public List<Runnable> shutdownNow(){
    List<Runnable> tasks;
    final ReentrantLock mainLock=this.mainLock;
    mainLock,lock();
    try{
        checkShutdownAccess();
        //修改线程池状态
        advanceRunState(STOP);
        //打断所有线程
        interruptWorkers();
        //获取队列中剩余的任务
        tasks=drainQueue();
    }finally{
        mainLock.unLock();
    }
    //尝试终结（没有运行的线程可以立刻终结，如果还有运行的线程也不会等）
    tryTerminate();
}
```

**其他方法**

```java
//不在RUNNING状态的线程池，此方法就返回true
boolean isShutdown();

//线程池状态是否是TERMINATED
boolean isTerminated();

//调用shutdown后，由于调用线程并不会等待所有任务运行结束，因此如果它想在线程池TERMINATED后做些事情，可以利用此方法等待
boolean awaitTermination(long timeout,TimeUnit unit) throws InterruptedException;
```

#### 异步模式之工作线程

**定义**

让有限的工作线程（Worker Thread）来轮流异步处理无线多的任务。也可以将其归类为分工模式，他的典型实现就是线程池，也体现了经典设计模式中的享元模式。

不同任务类型应该使用不同的线程池，这样能够避免饥饿，并能提升效率

**饥饿**

固定大小线程池会有饥饿现象

+ 两个工人是同一个线程池中的两个线程
+ 他们要做的事情是：为客人点餐和到后厨做菜，这是两个阶段的工作
  + 客人点餐：必须先点完餐，等菜做好，上菜，在此期间处理点餐的工人必须等待
  + 后厨做菜：做菜
+ 比如工人A处理了点餐任务，接下来他要等着工人B把菜做好，然后上菜，他俩也配合的蛮好
+ 但现在同时来了两个客人，这个时候工人A和工人B都去处理点餐了，这时没人做饭了，死锁

**创建多少线程合适**

+ 过小会导致程序不能充分的利用系统资源，容易导致饥饿
+ 过大会导致更多的线程上下文切换，占用更多内存

**CPU密集型运算**

通常采用cpu核数+1能够实现最优的CPU利用率，+1是保证当线程由于页缺失故障（操作系统）或其他原因导致暂停时，额外的这个线程就能顶上去，保证CPU时钟周期不被浪费

**I/O密集型运算**

CPU不总是处于繁忙状态，例如，当你执行业务计算时，这时候会使用CPU资源，但当你执行IO操作时，远程RPC调用时，包括进行数据库操作时，这时候CPU就闲下来了，你可以利用多线程提高它的利用率

线程数 = 核数 * 期望CPU利用率 * 总时间（CPU计算时间+等待时间）/ CPU计算时间

#### 任务调度线程池

在任务调度线程池功能加入之前，可以使用java.util.Timer来实现定时功能，Timer的优点在于简单易用，但由于所有任务都是由同一个线程来调度，因此所有任务都是串行执行的，同一时间只能由一个任务在执行，前一个任务的延迟或异常都会影响到之后的任务

```java
ScheduledExecutorService pool=Executors.newScheduledThreadPool(2);

pool.schedule(()->{
    log.debug("task1");
    sleep(2);
},1,TimeUnit.SECONDS);

pool.schedule(()->{
    log.debug("task2");
    sleep(2);
},1,TimeUnit.SECONDS);

pool.scheduleAtFixedRate(()->{
    log.debug("running")
    sleep(2);
},1, 1,TimeUnit.SECONDS);//定时执行任务

pool.scheduleWithFixedDelay(()->{
    log.debug("running")
    sleep(2);
},1, 1,TimeUnit.SECONDS);
```

#### Tomcat线程池

Tomcat在哪用到了线程池呢

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_37.png)

+ LimitLatch用来限流，可以控制最大连接个数，类似JUC中的Semaphore
+ Acceptor只负责接收新的socket连接
+ Poller只负责监听socket channel是否有可读的I/O事件
+ 一旦可读，封装一个任务对象（socketProcessor），提交给Executor线程池处理
+ Executor线程池中的各种线程最终负责处理请求

tomcat线程池扩展了ThreadPoolExecutor，行为稍有不同

+ 如果总线程数达到maximumPoolSize
  + 这时不会立刻抛RejectedExecutionException异常
  + 而是再次尝试将任务放入队列，如果还失败，才抛出RejectedExecutionException异常

Connector配置

| 配置项              | 默认值 | 说明                                 |
| ------------------- | ------ | ------------------------------------ |
| acceptorThreadCount | 1      | acceptor线程数量                     |
| pollerThreadCount   | 1      | poller线程数量                       |
| minSpareThreads     | 10     | 核心线程数，即corePoolSize           |
| maxThreads          | 200    | 最大线程数，即maximumPoolSize        |
| executor            | -      | Executor名称，用来引用下面的Executor |

Executor线程配置

| 配置项                  | 默认值            | 说明                                    |
| ----------------------- | ----------------- | --------------------------------------- |
| threadPriority          | 5                 | 线程优先级                              |
| daemon                  | true              | 是否守护线程                            |
| minSpareThreads         | 25                | 核心线程数，即corePoolSize              |
| maxThreads              | 200               | 最大线程数，即maximumPoolSize           |
| maxIdleTime             | 60000             | 线程生存时间，单位是毫秒，默认值即1分钟 |
| maxQueueSize            | Integer.MAX_VALUE | 队列长度                                |
| prestartminSpareThreads | false             | 核心线程是否在服务器启动时启动          |

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_38.png)

### Fork/Join

Fork/Join是JDK1.7加入的新的线程池的实现，他体现的是一种分治思想，适用于能够进行任务拆分的cpu密集型运算

所谓的任务拆分，是将一个大任务拆分为算法上相同的小任务，直至不能拆分可以直接求解。跟递归相关的一些计算，如归并排序、斐波那契数列、都可以用分治的思想进行求解

Fork/Join在分治的基础上加入了多线程，可以把每个任务的分解和合并交给不同的线程来完成，进一步提升了运算效率

Fork/Join默认会创建与cpu核心数大小相同的线程池

**使用**

提交给Fork/Join线程池的任务需要继承RecursiveTask(有返回值)或RecursiveAction(没有返回值)，例如下面定义了一个对1~n之间的整数求和的任务

```java
public class TestForkJoin{
    public static void main(String[] args){
    	ForkJoinPool pool=new ForkJoinPool(4);
    	System.out.println(pool.invoke(new Mytask(5)));
        //new MyTask(5) 5+new MyTask(4) 4+new MyTask(3) 3+new MyTask(2) 2+new MyTask(1)
	}
}


class Mytask entends RecursiveTask<Integer>{
    private int n;
    
    public Mytask(int n){
        this.n=n;
    }
    
    @Override
    protected Integer compute(){
        if(n==1){
            return 1;
        }
        
        MyTask t1=new MyTask(n-1);
        t1.fork();//让一个线程去执行此任务
        
        int result=n+t1.join();//获取任务结果
        return result;
    }
}
```

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_39.png)

### AQS原理

全称是AbstractQueuedSynchronizer，是阻塞式锁和相关同步器工具的框架

特点：

+ 用state属性来表示资源的状态（分独占模式和共享模式），子类需要定义如何维护这个状态，控制如何获取锁和释放锁
  + getState-获取state状态
  + setState-设置state状态
  + compareAndSetState-cas乐观锁机制设置状态
  + 独占模式是只有一个线程能够访问资源，而共享模式可以允许多个线程访问资源
+ 提供了基于FIFO的等待队列，类似于Monitor的EntryList
+ 条件变量来实现等待、唤醒机制，支持多个条件变量，类似于Monitor的WaitSet

子类主要实现这样一些方法（默认抛出UnsupportedOperationException）

+ tryAcquire
+ tryRelease
+ tryAcquireShared
+ tryReleaseShared
+ isHeldExclusively

获取锁的姿势

```java
//如果获取锁失败
if(!tryAcquired(arg)){
    //入队，可以选择阻塞当前线程  park unpark
}
```

释放锁的姿势

```java
//如果释放锁成功
if(tryRelease(arg)){
    //让阻塞线程恢复运行
}
```

### 自定义锁

```java
public class TestAqs{
    public static void main(String[] args){
        MyLock lock=new MyLock();
        new Thread(()->{
           lock.lock();
            try{
                log.debug("locking...");
                sleep(1);
            }finally{
                log.debug("unlocking...");
                lock.unlock();
            }
        },"t1").start();
        
        new Thread(()->{
           lock.lock();
            try{
                log.debug("locking...");
            }finally{
                log.debug("unlocking...");
                lock.unlock();
            }
        },"t2").start();
    }
}

class MyLock implements Lock{
    
    //独占锁 同步器类
    class MySync extends AbstractQueuedSynchronizer{
        @Override
        protected boolean tryAcquire(int arg){
            if(compareAndSetStare(0,1)){
                //加上了锁，并设置owner为当前线程
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }
        @Override
        protected boolean tryRelease(int arg){
            setExclusiveOwnerThread(null);
            setState(0);
            return true;
        }
        @Override//是否持有独占锁
        public boolean isHeldExcluively(){
            return getState()==1;
        }
        
        public Condition newCondition(){
            return new ConditionObject();
        }
    }
    
    private MySync sync=new MySync();
    
    @Override//加锁（不成功，进入等待队列等待）
    public void lock(){
        sync.acquire(1);
    }
    @Override//加锁，可打断
    public void lockInterruptibly() throws InterruptedException{
        sync.acquireInterruptibly(1);
    }
    @Override//尝试加锁（一次）
    public boolean tryLock(){
        return sync.tryAcqure(1);
    }
    @Override//尝试加锁，带超时
    public boolean tryLock(long time,TimeUnit unit) throws InterruptedException{
        return sync.tryAcquireNanos(1,unit.toNanos(time));
    }
    @Override//解锁
    public void unlock(){
        sync.relaese(1);
    }
    @Override//创建条件变量
    public Condition newCondition(){
        return sync.newCondition();
    }
}
```

### ReentrantLock原理

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_40.png)

从构造器看，默认为非公平锁

```java
public ReentrantLock(){
    sync=new NonfairSync();
}
```



#### 非公平锁实现原理

没有竞争时

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_41.png)

第一个竞争出现时

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_42.png)

Thread-1执行了

1. CAS尝试将state由0改为1，结果失败
2. 进入tryAcquire逻辑，这时state已经是1，结果仍然失败
3. 接下来进入addWaiter逻辑，构造Node队列

+ 图中黄色三角表示该Node的waitStatus状态，其中0为默认正常状态
+ Node的创建是懒惰的
+ 其中第一个Node称为Dummy(哑元)或哨兵、用来占位，并不关联线程

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_43.png)

当前线程进入acquireQueued逻辑

1. acquiredQueued会在一个死循环中不断尝试获得锁，失败后进入park阻塞
2. 如果自己是紧邻着head(排第二位)，那么再次tryAcquire尝试获取锁，当然这时state仍为1，失败
3. 进入shouldParkAfterFailedAcquire逻辑，将前驱node，即head的waitStatus改为-1，这次返回false

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_44.png)

4. shouldParkAfterFailedAcquire执行完毕回到acquireQueued，再次tryAcquire尝试获取锁，当然这时state仍为1，失败
5. 当再次进入shouldParkAfterFailedAcquire时，这时因为其前驱node的waitStatus已经是-1，返回true
6. 进入parkAndCheckInterrupt，Thread-1park(灰色表示)

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_45.png)

再次有多个线程经历上述过程竞争失败，变成这个样子

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_46.png)

Thread-0释放锁，进入tryRelease流程，如果成功

+ 设置exclusiveOwnerThread为null
+ state=0

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_47.png)

当前队列不为null，并且head的waitStatus=-1进入unparkSuccessor流程

找到队列中离head最近的一个Node(没取消的)，unpark恢复其运行，本例中即为Thread-1

回到Thread-1的acquireQueued流程

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_48.png)

如果加锁成功（没有竞争），会设置

+ exclusiveOwnerThread为Thread-1，state=1
+ head指向刚刚Thread-1所在的Node，该Node清空Thread
+ 原本的head因为从链表断开，而可被垃圾回收

如果这时候有其他线程来竞争（非公平的体现），例如这时有Thread-4来了

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_49.png)

如果不巧又被Thread-4抢了先

+ Thread-4被设置为exclusiveOwnerThread，state=1
+ Thread-1再次进入acquireQueued流程，获取锁失败，重新加入park阻塞

#### 可重入原理

```java
static final class NonfairSync extends Sync{
    //...
    
    //Sync继承过来的方法，方便阅读，放在此处
    final boolean nofairTryAcquire(int acquires){
        final Thread current=Thread.currentThread();
        int c=getState();
        //如果没有获得锁
        if(c==0){
            //尝试用cas获得，这里体现了非公平性：不去检查AQS队列
            if(compareAndSetState(0,acquires)){
                setExclusiveOwnerThread(current);
                return true;
            }
        }
        //如果已经获得了锁，线程还是当前线程，表示发生了锁重入
        else if(current==getExclusiveOwnerThread()){
            //state++
            int nextc=c+acquires;
            if(nextc<0)//overflow
                throws new Error("Maximum lock count exceeded");
            setState(nextc);
            return true;
        }
        return false;
    }
    
    //Sync继承过来的方法，方便阅读，放在此处
    protected final boolean tryRelease(int releases){
        //state--
        int c=getState()-releases;
        if(Thread.currentThread()!=getExclusoveOwnerThread())
            throws new IllegalMonitorStateException();
        boolean free=false;
        //支持锁重入，只要state减为0，才释放成功
        if(c==0){
            free=true;
            setExclusiveOwnerThread(null);
        }
        setState(c);
        return free;
    }
}
```

#### 可打断原理

**不可打断模式**

在此模式下，即使它被打断，仍会驻留在AQS队列中，等获得锁后方能继续运行（是继续运行！只是打断标记设置为true）

```java
//Sync继承自AQS
static final class NonfairSync extends Sync{
    //...
    private final boolean parkAndCheckInterrupt(){
        //如果打断标记已经是true,则park会失效
        LockSupport.park(this);
        //interrupted会消除打断标记
        return Thread.interrupted();
    }
    
    final boolean acquireQueued(final Node,int arg){
        boolean failed=true;
        try{
            boolean interrupted=false;
            for(;;){
                final Node p=node.predecessor();
                if(p==head&&tryAcquire(arg)){
                    setHead(node);
                    p.next=null;
                    failed=false;
                    //还是需要获得锁后，才能返回打断状态
                    return interrupted;
                }
                if(shouldParkAfterFailedAcquire(p,node)&&parkAndCheckInterrupt()){
                    //如果是因为interrupt被唤醒，返回打断状态为true
                    interrupted=true;
                }
            }
        }finally{
            if(failed)
                cancelAcquire(node);
        }
    }
    
    public final void acquire(int arg){
        if(!tryAcquire(arg)&&acquireQueued(addWaiter(Node.EXCLUSIVE),arg)){
            //如果打断状态为true
            selfInterrupt();
        }
    }
    static void selfInterrupt(){
        //重新产生一次中断
        Thread.currentThread().interrupt();
    }
}
```



**可打断模式**

```java
static final class NonfairSync extends Sync{
    public final void acquireInterruptibly(int arg) throws InterruptedException{
        if(Thread.interrupted())
            throws new InterruptedException();
        //如果没有获得锁，进入(一)
        if(!tryAcquire(arg))
            doAcquireInterruptibly(arg);
    }
    
    //(一)可打断的获取锁流程
    private void doAcquireInterruptibly(int arg) throws InterruptedException{
        final Node node=addWaiter(Node.EXCLUSIVE);
        boolean failed=true;
        try{
            for(;;){
                final Node p=node.predecessor();
                if(p==head&&tryAcquire(arg)){
                    setHead(node);
                    p.next=null;
                    failed=false;
                    return;
                }
                if(shouldParkAfterFailedAcquire(p,node)&&parkAndCheckInterrupt()){
                    //在park过程中如果被interrupt会进入此
                    //这时候会抛出异常，而不会再次进入for(;;)
                    throw new InterruptedException();
                }
        }finally{
            if(failed)
                cancelAcquire(node);
        }
    }
}
```

#### 公平锁实现原理

```java
static final class FairSync extends sync{
    //与非公平锁主要区别在于tryAcquire方法的实现
    protected final boolean tryAcquire(int acquires){
        final Thread current=Thread.currentThread();
        int c=getState();
        if(c==0){
            //先检查AQS队列中是否有前缀节点，没有才去竞争
            if(!hasQueuedPredecessors&&compareAndSetState(0,acquires)){
                setExclusiveOwnerThread(current);
                return true;
            }
        }
         //如果已经获得了锁，线程还是当前线程，表示发生了锁重入
        else if(current==getExclusiveOwnerThread()){
            //state++
            int nextc=c+acquires;
            if(nextc<0)//overflow
                throws new Error("Maximum lock count exceeded");
            setState(nextc);
            return true;
        }
        return false;
    }
    
    public final boolean hasQueuedPredecessors(){
    	Node t=tail;
        Node h=head;
        Node s;
        //h!=t时表示队列中有Node
        return h!=t&&(
        	//(s=h.next)==null表示队列中还有没有老二
            (s=h.next)==null||
            //或者队列中老二线程是不是此线程
            s.thread!=Thread.currentThread()
        );
	}
}
```

#### 条件变量实现原理

每个条件变量其实就对应着一个等待队列，其实现类是ConditionObject

**await流程**

开始Thread-0持有锁，调用await，进入ConditionObject的addConditionWaiter流程

创建新的Node状态为-2（Node.CONDITION），关联Thread-0，加入等待队列尾部

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_50.png)

接下来进入AQS的fullyRelease流程，释放同步器上的锁

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_51.png)

unpark AQS队列中的下一个节点，竞争锁，假设没有其他竞争线程，那么Thread-1竞争成功

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_52.png)



**signal流程**

假设Thread-1要来唤醒Thread-0

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_53.png)

进入ConditionObject的doSignal流程，取得等待队列中的第一个Node，即Thread-0所在的Node

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_54.png)

执行transferForSignal流程，将该Node加入AQS队列尾部，将Thread-0的waitStatus改为0，Thread-3的waitStatus改为-1

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_55.png)

Thread-1释放锁，进入unlock流程

### 读写锁

#### ReentrantReadWriteLock

当读操作远远高于写操作时，这时候使用读写锁让读读可以并发，提高性能



提供一个数据容器类内部分别使用读锁保护数据的read()方法，写锁保护数据的write()方法

```java
public class TestReadWriteLock{
    public static void main(String[] args){
        DataContainer dataContainer=new DataContainer();
        new Thread(()->{
            dataContainer.read();
		},"t1").start();
        Thread.sleep(100);
        new Thread(()->{
            dataContainer.write();
		},"t2").start();
    }
}

class DataContainer{
    private Object data;
    private ReentrantReadWriteLock rw=new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock r=rw.readLock();
    private ReentrantReadWriteLock.WriteLock w=rw.writeLock();
    
    public Object read(){
        r.lock();
        try{
        	log.debug("读取");
            sleep(1);
       		return data;
        }finally{
            r.unlock();
        }

    }
    
        public void write(){
        w.lock();
        try{
        	log.debug("写入");
        }finally{
            w.unlock();
        }

    }
}
```

**注意事项**

+ 读锁不支持条件变量
+ 重入时升级不支持：即持有读锁的情况下去获取写锁，会导致获取写锁永久等待

```java
r.lock();
try{
    w.lock();
    try{
        
    }finally{
        w.unlock();
    }
}finally{
    r.unlock();
}
```

+ 重入时降级支持：即持有写锁的情况下获取读锁

```java
class CachedData{
    Object data;
    //是否有效，如果失效，需要重新计算data
    volatile boolean cacheValid;
    final ReentrantReadWriteLock rw1=new ReentrantReadWriteLock();
    void processCachedData(){
        rw1.readLock().lock();
        if(!cacheValid){
            //获取写锁前必须释放读锁
            rw1.readLock().unlock();
            rw1.writeLock().lock();
            try{
                //判断是否有其他线程已经获取了写锁、更新了缓存，避免重复更新
                if(!cacheValid){
                    data=...
                    cacheValid=true;
                }
                //降级为读锁，释放写锁，主要能够让其他线程读取缓存
                rw1.readLock().lock();
            }finally{
                rw1.writeLock().unlock();
            }
        }
        //自己用完数据，释放读锁
        try{
            use(data);
        }finally{
            rw1.readLock(),unlock();
        }
    }
}
```

#### 读写锁原理

读写锁用的是同一个Sync同步器，因此等待队列、state等也都是同一个

**t1 w.lock，t2 r.lock**

1. t1成功上锁，流程与ReentrantLock加锁相比没有特殊之处，不同是写锁状态占了state的低16位，而读锁使用的是state的高16位

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_56.png)

2. t2执行r.lock，这时进入读锁的sync.acquireShared(1)流程，首先会进入 tryAcquireShared流程，如果有写锁占据，那么tryAcauireShared返回-1，表示失败

trAcquireSahred返回值表示

+ -1表示失败
+ 0表示成功，但后继节点不会继续唤醒
+ 正数表示成功，而且数值是还有几个后继节点需要唤醒，读写锁返回1

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_57.png)

3. 这时会进入sync.doAcquireShared(1)流程，首先也是调用addWaiter添加节点，不同之处在于节点被设置为Node.SHARED模式而非Node.EXCLUSIVE模式，注意此时t2仍处于活跃状态

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_58.png)

4. t2会看看自己的节点是不是老二，如果是，还会再次调用tryAcquireShared(1)来尝试获得锁
5. 如果没有成功，在doAcquireShared内for(;;)循环一次，把前驱节点的waitStatus改为-1，再for(;;)循环一次尝试tryAcquireShared(1)如果还不成功，那么在parkAndCheckInterrupt()处park

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_59.png)



**t3 r.lock，t4 w.lock**

这种状态下，假设又有t3加读锁和t4加写锁，这期间t1仍然持有锁，就变成了下面的样子

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_60.png)

**t1 w.unlock**

这时会走到写锁的sync.release(1)流程，调用sync.tryRelease(1)成功，变成下面的样子

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_61.png)

接下来执行唤醒流程sync.unparkSuccessor，即让老二恢复运行，这时t2在doAcquireShared内parkAndCheckInterrupt()处恢复运行

这回再来一次for(;;)执行tryAcquireShared成功则让读锁计数加一

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_62.png)

这时t2已经恢复运行，接下来t2调用setHeadAndPropagate(node,1)，他原本所在节点被置为头节点

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_63.png)

事情还没完，在setHeadAndPropagate方法内还会检查下一个节点是否是shared，如果是则调用doReleaseShared()将head的状态由-1改为0并唤醒老二，这时t3在doAcquireShared内parkAndCheckInterrupt()处恢复运行

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_64.png)

这回再来一次for(;;)执行tryAcquireShared成功则让读锁计数加一

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_65.png)

这时t3已经恢复运行，接下来t3调用setHeadAndPropagate(node,1)，他原本所在节点被置为头节点

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_66.png)

**t2 r.unlock，t3 r.unlock**

t2进入sync.releaseShared(1)中，调用tryReleaseShared(1)让计数减一，但由于计数还不为零

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_67.png)

t3进入sync.releaseShared(1)中，调用tryReleaseShared(1)让计数减一，这回计数为零了，进入doReleaseShared()将头节点从-1改为0并唤醒老二，即

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_68.png)

之后t4在acquireQueued中parkAndCheckInterrupt处恢复运行，再次for(;;)这次自己是老二，并且没有其他竞争，tryAcquire(1)成功，修改头节点，流程结束

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_69.png)

#### StampedLock

该类自JDK8加入，是为了进一步优化读性能，他的特点是在使用读锁、写锁时都必须配合戳使用

加解读锁

```java
long stamp=lock.readLock();
lock.unlockRead(stamp);
```

加解写锁

```java
long stamp=lock.writeLock();
lock.unlockWrite(stamp);
```

乐观读，StampedLock支持tryOptimisticRead()方法（乐观读），读取完毕后需要做一次戳校验，如果校验通过，表示这期间确实没有写操作，数据可以安全使用，如果校验没有通过，需要重新获取读锁，保证数据安全

```java
long stamp=lock.tryOptimisticRead();
//验戳
if(!lock.validate(stamp)){
    //锁升级
}
```



```java
public class TestStampedLock{
    public static void main(String[] args){
        DataContainerStamped dataContainer=new DataContainerStamped(1);
        new Thread(()->{
            dataContainer.read(1);
		},"t1").start();
        Thread.sleep(100);
        new Thread(()->{
            dataContainer.read(0);
		},"t2").start();
    }
}

class DataContainerStamped{
    private Object data;
	private final StampedLock lock=new StampedLock();
    
    public DataContainerStamped(int data){
        this.data=data;
    }
    
    public int read(int readTime){
        long stamp=lock.tryOptimisticRead();
        log.debug("optimistic read locking...{}",stamp);
        sleep(readTime);
        if(lock.validate(stamp)){
            log.debug("read finish...{}",stamp);
            return data;
        }
        log.debug("updating to read lock...{}",stamp);
        try{
            stamp=lock.readLock();
        	log.debug("read lock{}",stamp);
            sleep(readTime);
            log.debug("read finish...{}",stamp);
       		return data;
        }finally{
           	log.debug("read unlock{}",stamp);
            lock.unlockRead(stamp);
        }
    }
    
     public void write(int newData){
        long stamp=lock.writeLock();
         log.debug("write lock{}",stamp);
        try{
            sleep(2);
        	this.data=newData;
        }finally{
            log.debug("write unlock{}",stamp);
            lock.unlockWrite(stamp);
        }
    }
}
```

### Semaphore

信号量，用来限制能同时访问共享资源的线程上限

```java
public class TestSemaphore{
    public static void main(String[] args){
        //1.创建Semaphore对象
        Semaphore semaphore=new Semaphore(3);
        //2.10个线程同时运行
        for(int i=0;i<10;i++){
            new Thread(()->{
                try{
                    semaphore.acquire();
                }catch(InterruptedException e){
                    e.printStackTrack();
                }
                try{
                    log.debug("running...");
                	sleep(1);
                	log.debug("ending")
                }finally{
                    semaphore.release();
                }
            }).start();
        }
    }
}
```

**Semaphore应用**

+ 使用Semaphore限流，在访问高峰期时，让请求线程阻塞，高峰过去了再释放许可，当然它只适合限制单机线程数量，并且仅是限制线程数，而不是限制资源数（例如连接数）

+ 用Semaphore实现简单连接池，对比享元模式下的实现，性能和可读性显然更好

#### Semaphore原理

Semaphore有点像一个停车场，permits就好像停车位数量，当线程获得了permits就像是获得了停车位，然后停车场显示空余车位减一

刚开始，permits(state)为3，这时5个线程来获取资源

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_70.png)

假设其中Thread-1,Thread-2,Thread-4 cas竞争成功，而Thread-0和Thread-3竞争失败，进入AQS队列park阻塞

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_71.png)

这时Thread-4释放了permits，状态如下

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_72.png)

接下来Thread-0竞争成功，permits再次设置为0，设置自己为head节点，断开原来的head节点，unpark接下来的Thread-3节点，但由于permits是0，因此Thread-3在尝试不成功后再次进入park状态

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_73.png)

### CountdownLatch

用来进行线程同步协作，等待所有线程完成倒计时

其中构造参数用来初始化等待计数值，await()用来等待计数归零，countDown()用来让计数减一

```java
public class TestCountdownLatch{
    public static void main(String[] args) throws InterruptedException{
        CountdownLatch latch=new CountdownLatch(3);
        ExecutorService service=Executors.newFixedThreadPool(4);
        service.submit(()->{
            log.debug("begin...");
            sleep(1);
            latch.countDown();
            log.debug("end...{}",latch.getCount());
        });
        service.submit(()->{
            log.debug("begin...");
            sleep(1.5);
            latch.countDown();
            log.debug("end...{}",latch.getCount());
        });
        service.submit(()->{
            log.debug("begin...");
            sleep(2);
            latch.countDown();
            log.debug("end...{}",latch.getCount());
        });
        service.submit(()->{
            log.debug("waiting...");
        	latch.await();
        	log.debug("waiting end...");
        });
     }

	public static void test4() throws InterruptedException{
        CountdownLatch latch=new CountdownLatch(3);
        new Thread(()->{
            log.debug("begin...");
            sleep(1);
            latch.countDown();
            log.debug("end...")
        }).start();
        
        new Thread(()->{
            log.debug("begin...");
            sleep(1.5);
            latch.countDown();
            log.debug("end...")
        }).start();
        
        new Thread(()->{
            log.debug("begin...");
            sleep(2);
            latch.countDown();
            log.debug("end...")
        }).start();
        
        log.debug("waiting...");
        latch.await();
        log.debug("waiting end...");
    }
}
```

### CyclicBarrier

循环栅栏，用来进行线程操作，等待线程满足某个计数。构造时设置计数个数，每个线程执行到某个需要同步的时刻调用await()方法进行等待，当等待的线程数满足计数个数时，继续执行

线程数要和CyclicBarrier参数一致

```java
public class TestCyclicBarrier{
    public static void main(String[] args){
        ExecutorService service=Executors.newFixedThreadPool(2);
        CyclicBarrier barrier=new CyclicBarrier(2,()->{
            log.debug("task1 task2 finish...");
        });
        
        service.submit(()->{
            log.debug("task1 begin...");
            sleep(1);
            barrier.await();//相当于countDown()方法  2-1=1
            log.debug("task1 end...");
        });
        
         service.submit(()->{
            log.debug("task2 begin...");
            sleep(2);
            barrier.await();
            log.debug("task2 end...");
        });
    }
}
```

### 线程安全集合类

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_74.png)

线程安全集合类可以分为三大类：

+ 遗留的线程安全集合如Hashtable，Vector
+ 使用Collections装饰的线程安全集合，如：
  + Collections.synchronizedCollection
  + Collections.synchronizedList
  + Collections.synchronizedMap
  + Collections.synchronizedSet
  + Collections.synchronizedNavigableMap
  + Collections.synchronizedNavigableSet
  + Collections.synchronizedSortedMap
  + Collections.synchronizedSortedSet
+ java.util.concurrent.*

重点介绍java.util.concurrent.*下的线程安全集合类，可以发现他们有规律，里面包含三类关键词：Blocking、CopyOnWrite、Concurrent

+ Blocking大部分实现基于锁，并提供用来阻塞的方法
+ CopyOnWrite之类容器修改开销相对较重
+ Concurrent类型的容器
  + 内部很多操作使用cas优化，一般可以提供较高吞吐量
  + 弱一致性
  + 遍历时弱一致性，例如，当利用迭代器遍历时，如果容器发生修改，迭代器仍然可以继续进行遍历，这时内容是旧的
  + 求大小弱一致性，size操作未必是100%准确
  + 读取弱一致性

遍历时如果发生了修改，对于非安全容器来讲，使用fail-fast机制也就是让遍历立刻失败，抛出ConcurrentModificationException，不再继续遍历

### ConcurrentHashMap

#### **重要属性和内部类**

```java
//默认为0；当初始化时，为-1；当扩容时，为-（1+扩容线程数）；当初始化或扩容完成后，为下一次的扩容的阈值大小
private transient volatile int sizeCtl;

//整个ConcurrentHashMap就是一个Node[]
static class Node<K,V> implements Map.Entry<K,V>{}

//hash表
transient volatile Node<K,V>[] table;

//扩容时的新hash表
private transient volatile Node<K,V>[] nextTable;

//扩容时如果某个bin迁移完毕，用ForwardingNode作为旧table bin的头节点
static final class ForwardingNode<K,V> extends Node<K,V>{}

//用在compute以及computeIfAbsent时，用来占位，计算完成后替换为普通Node
static final class ReservationNode<K,V> extends Node<K,V>{}

//作为treebin的头节点，存储root和first
static final class TreeBin<K,V> extends Node<K,V>{}

//作为treebin的节点，存储parent,left,right
static final class TreeNode<K,V> extends Node<K,V>{}
```

#### **重要方法**

```java
//获取Node[]中第i个Node
static final <K,V> Node<K,V> tabAt(Node<K,V>[] tab,int i)
    
//cas修改Node[]中第i个Node的值，c为旧值，v为新值
static final <K,V> boolean casTabAt(Node<K,V>[] tab,int i,Node<K,V> c,Node<K,V> v)
    
//直接修改Node[]中第i个Node的值，v为新值
static final <K,V> void setTabAt(Node<K,V>[] tab,int i,Node<K,V> v)
```

#### **构造器分析**

可以看到实现了懒惰初始化，在构造方法中仅仅计算了table大小，以后在第一次使用时才会真正创建

```java
public ConcurrentHashMap(int initialCapacity,float loadFactor,int concurrencyLevel){
    if(!(loadFactor>0.0f)||initialCapacity<0||concurrencyLevel<=0)
        throw new IllegalArgumentException();
    if(initialCapacity<concurrencyLevel) //Use at least as many bins
        initialCapacity=concurrencyLevel; //as estimated threads
    long size=(long)(1.0+(long)initialCapacity/loadFactor);
    //tableSizeFor 仍然是保证计算的大小是2^n，即16，32，64 ...
    int cap=(size>(long)MAXIMUM_CAPACITY)?MAXIMUM_CAPACITY:tableSizeFor((int)size);
    this.sizeCtl=cap;
}
```

#### **get流程**

```java
public V get(Object key){
    Node<K,V>[] tab;Node<K,V> e,p;int n,eh;K ek;
    //spread方法能确保返回结果是正数
    int h=spread(key.hashCode());
    if((tab=table)!=null&&(n=table.length)>0&&(e=tabAt(tab,(n-1)&h))!=null){
        //如果头节点已经是要查找的key
        if((eh=e.hash)==h){
            if((ek=e.key)==key||(ek!=null&&key.equals(ek)))
                return e.val;
        }
        //hash为负数表示该bin在扩容中或是treebin,这时调用find方法来查找
        else if(eh<0)
            return (p=e.find(h,key))!=null?p.val:null'
        //正常遍历链表，用equals比较
        while((e=e.next)!=null){
            if(e.hash==h&&((ek=e.key)==key||(ek!=null&&key.equals(ek))))
                return e.val;
        }
	}
    return null;
}
```

#### **put流程**

以下数组简称table，链表简称bin

```java
public V put(K key,V value){
    return putVal(key,value,false);
}
final V putVal(K key,V value,boolean onlyIfAbsent){
    if(key==null||value==null) throw new NullPointerException();
    //其中spread方法会综合高位低位，具有更好的hash性
    int hash=spread(key.hashCode());
    int binCount=0;
    for(Node<K,V>[] tab=table;;){
        //f是链表头节点，fh是链表头节点的hash,i是链表在table中的下标
        Node<K,V> f;int n,i,fh;
        //要创建table
        if(tab==null||(n=tab.length)==0)
            //初始化table使用了cas，无需synchronized，创建成功，进入下一轮循环
            tab=initTable();
        //要创建链表头节点
        else if((f=tabAt(tab,i=(n-1)&hash))==null){
            //添加链表头使用了cas,无需synchronized
            if(casTabAt(tab,i,null,new Node<K,V>(hash,key,value,null)))
                break;
        }
        //帮忙扩容
        else if((fh=f.hash)==MOVED)
            //帮忙之后，进入下一轮循环
            tab=helpTransfer(tab,f);
        else{
            V oldVal=null;
            //锁住链表头节点
            synchronized(f){
                //再次确认链表头节点没有被移动
                if(tabAt(tab,i)==f){
                    //链表
                    if(fh>=0){
                        binCount=1;
                        //遍历链表
                        for(Node<K,V> e=f;;++binCount){
                            K ek;
                            //找到相同的key
                            if(e.hash==hash&&((ek=e.key)==key||(ek!=null&&key.equals(ek)))){
                                oldVal=e.val;
                                //更新
                                if(!onlyIfAbsent)
                                    e.val=value;
                                break;
                            }
                            Node<K,V> pred=e;
                            //已经是最后的节点了，新增Node,追加至链表尾
                            if((e=e.next)==null){
                                pred.next=new Node<K,V>(hash,key,value,null);
                                break;
                            }
                        }
                    }
                    //红黑树
                    else if(f instanceof TreeBin){
                        Node<K,V> p;
                        binCount=2;
                        //putTreeVal会看key是否已经在树中，是，则返回对应的TreeNode
                        if((p=((TreeBin<K,V>)f).putTreeVal(hash,key,value))!=null){
                            oldVal=p.val;
                            if(!onlyIfAbsent)
                                p.val=value;
                        }
                    }
                }
                //释放链表头节点的锁
            }
            if(binCount!=0){
                if(binCount>=TREEIFY_THRESHOLD)
                    //如果链表长度>=树化阈值(8)，进行链表转为红黑树
                    treeifyBin(tab,i);
                if(oldVal!=null)
                    return oldVal;
                break;
            }
        }
    }
    //增加size计数
    addCount(1L,binCount);
    return null;
}

private final Node<K,V>[] initTable(){
    Node<K,V>[] tab;int sc;
    while((tab=table)==null||tab.length==0){
        if((sc=sizeCtl)<0)
            Thread.yield();
        //尝试将sizeCtl设置为-1(表示初始化table)
        else if(U.compareAndSwapInt(this,SIZECTL,sc,-1)){
            //获得锁，创建table,这时其他线程会在while()循环中yield直至table创建
            try{
                if((tab=table)==null||tab.length==0){
                    int n=(sc>0)?sc:DEFAULT_CAPACITY;
                    Node<K,V>[] nt=(Node<K,V>[])new Node<?,?>[n];
                    table=tab=nt;
                    sc=n-(n>>>2);
                }
            }finally{
                sizeCtl=sc;
            }
            break;
        }
    }
    return tab;
}

//check是之前binCount的个数
private final void addAccount(long x,int check){
    CounterCell[] as;long b,s;
    if(
    	//已经有了counterCells,向cell累加
        (as=counterCells)!=null||
        //还没有，向baseCount累加
        !U.compareAndSwapLong(this,BASECOUNT,b=baseCount,s=b+x)
    ){
        CounterCell a;long v;int m;
        boolean uncontended=true;
        if(
        	//还没有counterCells
            as==null||(m=as.length-1)<0||
            //还没有cell
            (a=as[ThreadLocalRandom.getProbe()&m])==null||
            //cell cas增加计数失败
            !(uncontended=U.compareAndSwapLong(a,CELLVALUE,v=a.value,v+x))
        ){
            //创建累加单元数组和cell,累加重试
            fullAddCount(x,uncontended);
            return;
        }
        if(check<=1)
            return;
        //获取元素个数
        s=sumCount();
    }
    if(check>=0){
        Node<K,V>[] tab,nt;int n,sc;
        while(s>=(long)(sc=sizeCtl)&&(tab=table)!=null&&(n=tab.length)<MAXIMUN_CAPACITY){
			int rs=resizeStamp(n);
            if(sc<0){
                if((sc>>>RESIZE_STAMP-SHIFT)!=rs||sc==rs+1||sc==rs+MAX_RESIZES||(nt=nextTable)==null||transferIndex<=0)
                    break;
                //nextTable已经创建了，帮忙扩容
                if(U.compareAndSwapInt(this,SIZECTL,sc,sc+1))
                    transfer(tab,nt);               
            }
            //需要扩容，这时newtable未创建
            else if(U.compareAndSwapInt(this,SIZECTL,sc,(rs<<RESIZE_STAMP_SHIFT)+2))
            	transfer(tab,null);
            s=sumCount();
        }
    }
}
```

#### **size计算流程**

size计算实际发生在put,remove改变集合元素的操作之中

+ 没有竞争发生，向baseCount累加计数
+ 有竞争发生，新建counterCells,向其中的一个cell累加计数
  + counterCells初始有两个cell
  + 如果计数竞争比较激烈，会创建新的cell来累加计数

```java
public int size(){
    long n=sumCount();
    return((n<0L)?0:(n>(long)Integer.MAX_VALUE)?Integer.MAX_VALUE:(int)n);
}

final long sumCount(){
    CounterCell[] as=counterCells;CounterCell a;
    //将baseCount计数与所有cell计数累加
    long sum=baseCount;
    if(as!=null){
        for(int i=0;i<as.length;i++){
            if((a=as[i])!=null)
                sum+=a.value;
        }
    }
    return sum;
}
```

### LinkedBlockingQueue原理

#### 基本的入队出队

```java
public class LinkedBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E>,java.io.Serializable{
    static class Node<E>{
        E item;
        
        Node<E> next;
        Node(E x){item=x};
    }
}
```

初始化链表last=head=new Node<E>(null);Dummy节点用来占位，item为null

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_75.png)

当一个节点入队last=last.next=node;

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_76.png)

再来一个节点入队last=last.next=node;

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_77.png)

出队

```java
Node<E> h=head;
Node<E> first=h.next;
h.next=h;
head=first;
E x=first.item;
first.item=null;
return x;
```

h=head

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_78.png)

first=h.next

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_79.png)

h.next=h

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_80.png)

head=first

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_81.png)

E x=first.item;
first.item=null;
return x;

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_82.png)

#### 加锁分析

高明之处在于用了两把锁和dummy节点

+ 用一把锁，同一时刻，最多只允许有一个线程（生产者或消费者，二选一）执行
+ 用两把锁，同一时刻，可以允许两个线程同时（一个生产者，一个消费者）执行
  + 消费者与消费者线程仍然串行
  + 生产者与生产者线程仍然串行

线程安全分析

+ 当节点总数大于2时（包括dummy节点），putLock保证的是last节点的线程安全，takeLock保证的是head节点的线程安全。两把锁保证了入队和出队没有竞争
+ 当节点总数等于2时（即一个dummy节点，一个正常节点）这时候，仍然是两把锁锁两个对象，不会竞争
+ 当节点总数等于1时（就一个dummy节点）这时take线程会被notEmpty条件阻塞，有竞争，会阻塞

```java
//用于put(阻塞)offer(非阻塞)
private final ReentrantLock putLock=new ReentrantLock();

//用于take(阻塞)poll(非阻塞)
private final ReentrantLock takeLock=new ReentrantLock();
```

#### put操作

```java
public void put(E e) throws InterruptedException{
    if(e==null) throw new NullPointerException();
    int c=-1;
    Node<E> node=new Node<E>(e);
    final ReentrantLock putLock=this.putLock;
    //count用来维护元素计数
    final AtomicInteger count=this.count;
    putLock.lockInterruptibly();
    try{
        //满了等待
        while(count.get()==capacity){
            //等待notFull
            notFull.await();
        }
        //有空位，入队且计数加一
        enqueue(node);
        c=count.getAndIncrement();
        //除了自己put以外，队列还有空位，由自己叫醒其他put线程
        if(c+1<capacity)
            notFull.signal();
    }finally{
        putLock.unlock();
    }
    //如果队列中有一个元素，叫醒take线程
    if(c==0)
        //这里调用的是notEmpty.signal()而不是notEmpty.signalAll()是为了减少竞争
        signalNotEmpty();
}
```

#### take操作

```java
public E take() throws InterruptedException{
    E x;
    int c=-1;
    final AtomicInteger couny=this.count;
    final ReentrantLock takeLock=this.takeLock;
    takeLock,lockInterruptibly();
    try{
        while(count.get()==0){
            notEmpty.await();
        }
        x=dequeue();
        c=count.getAndDecrement();
        if(c>1)
            notEmpty.signal();
    }finally{
        takeLock.unlock();
    }
    //如果队列中只有一个空位时，叫醒put线程
    //如果有多个线程进行出队，第一个线程满足c==capacity，但后续线程c<capacity
    if(c==capacity)
        //这里调用的是notFull.signal()而不是notFull.signalAll()是为了减少竞争
        signalNotFull();
    return x;
}
```

#### LinkedBlockingQueue和ArrayBlockingQueue性能比较

+ Linked支持有界，Array强制有界
+ Linked实现是链表，Array实现是数组
+ Linked是懒惰的，而Array需要提前初始化Node数组
+ Linked每次入队会生成新的Node，而Array的Node是提前创建好的
+ Linked两把锁，Array一把锁

### ConcurrentLinkedQueue

ConcurrentLinkedQueue的设计与LinkedBlockingQueue非常像，也是

+ 两把锁，同一时刻，可以允许两个线程同时（一个生产者，一个消费者）执行
+ dummy节点的引入让两把锁将来锁住的是不同对象，避免竞争
+ 只是这锁使用了cas来实现

事实上，ConcurrentLikedQueue应用还是非常广泛的

例如之前讲的Tomcat的Connector结构时，Acceptor作为生产者向Poller消费者传递事件信息时，正是采用了ConcurrentlinkedQueue将SocketChannel给Poller使用

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_83.png)

### CopyOnWriteArrayList

CopyOnWriteArraySet是它的马甲，调用的都是CopyOnWriteArrayList中的方法

底层实现采用了写入时拷贝的思想，增删改操作会将底层数组拷贝一份，更改操作在数组上执行，这时不会影响其他线程的并发读，读写分离

以新增为例：

```java
public boolean add(E e){
    synchronized(lock){
        //获取旧的数组
        Object[] es=getArray();
        int len=es.length;
        //拷贝新的数组（这里是比较耗时的操作，但不影响其他读线程）
        es=Arrays.copyOf(es,len+1);
        //添加新元素
        es[len]=e;
        //替换旧的数组
        setArray(es);
        return true;
    }
}
```

这里的源码版本是Java11，在Java1.8中使用的是可重入锁而不是synchronized

其他读操作未加锁，例如：

```java
public void forEach(Consumer<? super E> action){
    Object.requiredNonNull(action);
    for(Object x:getArray()){
        @SuppressWarnings("unchecked") E e=(E)x;
    	action.accept(e);
    }      
}
```

适合读多写少的应用场景

#### get弱一致性

![](img/%E5%B9%B6%E5%8F%91%E7%BC%96%E7%A8%8B_84.png)

| 时间点 | 操作                        |
| ------ | --------------------------- |
| 1      | Thread-0 getArray()         |
| 2      | Thread-1 getArray()         |
| 3      | Thread-1setArray(arrayCopy) |
| 4      | Thread-0 array[index]       |

#### 迭代器弱一致性

```java
CopyOnWriteArrayList<Integer> list=new CopyOnWriteArrayList<>();
list.add(1);
list.add(2);
list.add(3);
Iterator<Integer> iter=list.iterator();
new Thread(()->{
    list.remove(0);
    System.out.println(list);
}).start();
sleep(1);
while(iter.hasNext()){
    System.out.println(iter.next());
}
```

不要觉得弱一致性就不好

+ 数据库的MVCC都是弱一致性的表现
+ 并发高和一致性是矛盾的，需要权衡