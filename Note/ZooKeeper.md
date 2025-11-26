## 概述

Zookeeper从设计模式角度来理解：是一个基于观察者模式设计的分布式服务管理框架，它负责存储和管理大家都关心的数据，然后接受观察者的注册，一旦这些数据的状态发生变化，Zookeeper就将负责通知已经在Zookeeper上注册的那些观察者做出相应的反应

![](/img/zk_1.png)

![](/img/zk_2.png)

![](/img/zk_3.png)

**应用场景**

+ 统一命名服务：在分布式环境下，经常需要对应用/服务进行统一命名，便于识别
+ 统一配置管理：将配置信息写入ZooKeeper上的一个Znode，各个客户端服务器监听这个Znode，一旦发生修改，将通知各个客户端服务器
+ 统一集群管理：将节点信息写入ZooKeeper上的一个Znode，监听这个Znode可获取它的实时状态变化
+ 服务器节点动态上下线：客户端能实时洞察到服务器上下线的变化
+ 软负载均衡等：在ZooKeeper中记录每台服务器的访问数，让访问最少的服务器去处理最新的客户请求

**配置参数**

ZooKeeper中的配置文件zoo.conf中参数含义解读如下：

+ ticketTime=2000：通信心跳时间，ZooKeeper服务器与客户端心跳时间，单位毫秒
+ initLimit=10：Leader和Follower初始通信时限，初始连接时能容忍的最多心跳数（ticketTime的数量）
+ syncLimit=5：Leader和Follower同步通信时限，如果超过syncLimit*ticketTime，Leader认为Follower死掉，从服务器列表中删除Follower
+ dataDir：保存ZooKeeper中的数据，注意，默认的tmp目录容易被Linux定期删除，所以一般不用默认的tmp目录
+ clientPort=2181：客户端连接端口，通常不做修改

## 集群操作

### 集群操作

1. 解压安装

   在服务器解压ZooKeeper安装包到opt/module/目录下，修改apache-zookeeper-3.537-bin名称为zookeeper-3.5.7

2. 配置服务器编号

   1. 在opt/module/zookeeper-3.5.7/这个目录下创建zkData
   2. 在opt/module/zookeeper-3.5.7/zkData目录下创建一个myid的文件，在文件中添加与server对应的编号，注意上下左右不要有空格

3. 拷贝配置好的zookeeper到其他机器上，并修改myid中的内容

4. 配置zoo.cfg文件

   1. 重命名/opt/module/zookeeper-3.5.7/conf这个目录下的zoo_sample.cfg为zoo.cfg

   2. 修改数据存储路径dataDir=/opt/module/zookeeper-3.5.7/zkData

   3. 增加如下配置

      ```shell
      ##cluster##
      server.2=hadoop102:2888:3888
      server.3=hadoop103:2888:3888
      server.4=hadoop104:2888:3888
      ```

      server.A=B:C:D

      A是一个数字，表示这个是第几号服务器，myid文件中的数字就是A的值，Zookeeper启动时读取这个文件，拿到里面的数据与zoo.cfg里面的配置信息比较从而判断哪个是server

      B是这个服务器的地址

      C是这个服务器Follower与集群中的Leader服务器交换信息的端口

      D是万一集群中的Leader服务器挂掉了，需要一个端口来重新进行选举，选出一个新的Leader，而这个端口就是用来执行选举时服务器相互通信的端口

### 选举机制

**第一次启动**

![](/img/zk_4.png)

**非第一次启动**

![](/img/zk_5.png)

### 客户端命令行操作

**命令行语法**

| 命令行基本语法 | 功能描述                                                     |
| -------------- | ------------------------------------------------------------ |
| help           | 显示所有命令操作                                             |
| ls path        | 使用 ls 命令来查看当前znode的子节点 [可监听] <br />-w 监听子节点变化<br />-s 附加次级信息 |
| create         | 普通创建<br />-s 含有序列<br />-e 临时（重启或者超时消失）   |
| get path       | 获得节点的值 [可监听]<br />-w 监听节点内容变化<br />-s 附加次级信息 |
| set            | 设置节点的具体值                                             |
| stat           | 查看节点状态                                                 |
| delete         | 删除节点                                                     |
| deleteall      | 递归删除节点                                                 |

启动客户端：zookeeper-3.5.7下执行 bin/zkCli.sh -server hadoop102:2128

**znode节点数据信息**

1. 查看znode中所包含的内容：启动客户端后执行 ls /

2. 查看当前节点详细数据：ls -s /

   ![](/img/zk_6.png)

   + czxid：创建节点的事务zxid

     每次修改ZooKeeper状态都会产生一个ZooKeeper事务ID。事务ID是ZooKeeper中所有修改总的次序。每次修改都有唯一的zxid，如果zxid1小于zxid2，那么zxid1在zxid2之前发生

   + ctime：znode被创建的毫秒数（从1970年开始）

   + mzxid：znode最后更新的事务zxid

   + mtime：znode最后修改的毫秒数（从1970年开始）

   + pZxid：znode最后更新的子节点zxid

   + cversion：znode子节点变化号，znode子节点修改次数

   + dataversion：znode数据变化号

   + aclVersion：znode访问控制列表的变化号

   + ephemeralOwner：如果是临时节点，这个是znode拥有者的session id。如果不是临时节点则是0

   + dataLength：znode的数据长度

   + numChildren：znode子节点数量

**节点类型**

![](/img/zk_7.png)

1. 创建2个普通节点（永久节点 + 不带序号）

   create /sanguo "diaochan"

   create /sanguo/shuguo "liubei"

2. 获得节点的值

   get -s /sanguo

3. 创建带序号的节点（永久节点 + 带序号）

   create -s /sanguo/weiguo "caocao"

4. 创建短暂节点（短暂节点 + 不带序号 or 带序号）

   短暂不带序号：create -e /sanguo/wuguo "zhouyu"

   短暂带序号：create -e  -s /sanguo/wuguo "zhouyu"

5. 修改节点数值

   set /sanguo/weiguo "simayi"

**监听器原理**

客户端注册监听它关心的目录节点，当目录节点发生变化（数据改变、节点删除、子目录节点增加删除）时，ZooKeeper会通知客户端。监听机制保证ZooKeeper保存的任何的数据的任何改变都能快速的响应到监听了该节点的应用程序

![](/img/zk_8.png)

1. 节点的值变化监听

   + 在hadoop104主机上注册监听/sanguo节点数据变化：get -w /sanguo

   + 在hadoop103主机上修改/sanguo节点的数据：set /sanguo "xisi"

   + 观察hadoop104主机收到的数据变化的监听

     WATCHER::

     WatchedEvent	state:SyncConnected	type:NodeDataChanged	path:/sanguo

   注意：在hadoop103再多次修改/sanguo的值，hadoop104上不会再收到监听。因为注册一次，只能监听一次。想再次监听，需要再次注册。

2. 节点的子节点变化监听（路径变化）

   + 在hadoop104主机上注册监听/sanguo节点的子节点变化：ls -w /sanguo

   + 在hadoop103主机/sanguo节点上创建子节点：create /sanguo/jin "simayi"

   + 观察hadoop104主机收到的数据变化的监听

     WATCHER::

     WatchedEvent	state:SyncConnected	type:NodeChildrenChanged	path:/sanguo

   注意：节点的路径变化，也是注册一次，生效一次。想多次生效，就需要注册多次。

**节点的删除与查看**

1. 删除节点：delete /sanguo/jin
2. 递归删除节点：deleteall /sanguo/shuguo
3. 查看节点状态：state /sanguo

### 客户端API

```java
public class zkClient {
    private String connectString = "hadoop102：2181,hadoop103:2181,hadoop104:2181";
    private int sessionTimeout = 2000;
    private ZooKeeper zkClient;
    
    @Before
    public void init() throws IOException {
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                List<String> children = zkClient.getChildren("/", true);
        		for(String child : children) {
            		System.out.println(child);
        		}
            }
        })
    }
    
    @Test
    public void create() {
        String nodeCreated = zkClient.create("/atguigu", "ss.avi".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }
    
    @Test
    public void getChildren() {
        List<String> children = zkClient.getChildren("/", true);
        for(String child : children) {
            System.out.println(child);
        }
    }
    
    @Test
    public void exist() {
        Stat stat = zkClient.exist("/atguigu", false);
        System.out.println(stat == null);
    }
}
```

### 写数据原理

**写入请求直接发送给Leader节点**

![](/img/zk_9.png)

**写入请求直接发送给Follower节点**

![](/img/zk_10.png)

## 服务器动态上下线监听案例

![](/img/zk_1.png)

1. 先在集群上创建/servers节点：create /servers "servers"

2. 服务端向ZooKeeper注册代码

   ```java
   public class DistributeServer {
       
       private String connectString = "hadoop102：2181,hadoop103:2181,hadoop104:2181";
       private int sessionTimeout = 2000;
       private ZooKeeper zk;
       
       public static void main(String[] args) {
           
           DistributeServer server = new DistributeServer();
           
           // 1.获取zk连接
           server.getConnection();
           
           // 2.注册服务器到zk集群
           server.regist(args[0]);
           
           // 3.启动业务逻辑
           server.business();
       }
       
       private void business() {
       	Thread.sleep(Long.MAX_VALUE);    
       }
       
       private void regist(String hostname) {
           String create = zk.create("/servers", hostname, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
       }
       
       private void getConnection() {
           zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
               @Override
               public void process(WatchedEvent watchedEvent) {
                   
               }
           })
       }
   }
   ```

   ```Java
   public class DistributeClient {
       
       private String connectString = "hadoop102：2181,hadoop103:2181,hadoop104:2181";
       private int sessionTimeout = 2000;
       private ZooKeeper zk;
       
       public static void main(String[] args) {
           
           DistributeClient client = new DistributeClient();
           
           // 1.获取zk连接
           server.getConnection();
           
           // 2.监听/servers下面子节点的增加和删除
           server.getServerList();
           
           // 3.业务逻辑
           server.business();
       }
       
       private void business() {
       	Thread.sleep(Long.MAX_VALUE);    
       }
       
       private void getServerList(String hostname) {
           List<String> children = zk.getChildren("/servers", true);
           
           ArrayList<String> servers = new ArrayList<>();
           
           for(String child : children) {
               byte[] data = zk.getData("/servers/" + child, false, null);
               servers.add(new String(data));
           }
           
           System.out.println(servers);
       }
       
       private void getConnection() {
           zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
               @Override
               public void process(WatchedEvent watchedEvent) {
                   getServerList();
               }
           })
       }
   }
   ```

## ZooKeeper分布式锁

### 原生ZooKeeper实现分布式锁

![](/img/zk_11.png)

```java
public class DistributedLock {
    
    private String connectString = "hadoop102：2181,hadoop103:2181,hadoop104:2181";
    private int sessionTimeout = 2000;
    private ZooKeeper zk;
    
    private CountDownLatch connectLatch = new CountDownLatch(1);
    private CountDownLatch waitLatch = new CountDownLatch(1);
    
    private String waitPath;
    private String currentMode;
    
    public DistributedLock() {
        
        // 获取连接
        zk = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                // connectLatch 如果连接上zk，可以释放
                if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    connectLatch.countDown();
                }
                
                // waitLatch 需要释放
                if (watchedEvent.getType() == Event.EventType.NodeDeleted && watchedEvent.getPath().equals(waitPath)) {
                    waitLatch.countDown();
                }
            }
        });
        
        // 等待zk正常连接后，往下走程序
        connectLatch.await();
        
        // 判断根节点/locks是否存在
        Stat stat = zk.exists("/locks", false);
        
        if(stat == null) {
            // 创建一下根节点
            zk.create("/locks", "locks".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }
    
    // 对zk加锁
    public void zkLock() {
        // 创建对应的临时带序号节点
        currentMode = zk.create("/locks/" + "seq_", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        
        // 判断创建的节点是否是最小的序号节点，如果是获取到锁，如果不是，监听它序号前一个节点
        List<String> children = zk.getChildren("/locks", false);
        
        // 如果children只有一个值，那就直接获取锁；如果有多个节点，需要判断，谁最小
        if(children.size() == 1) {
            return;
        } else {
            Collections.sort(children);
            
            // 获取节点名称 seq-00000000
            String thisNode = currentMode.substring("/locks/".length());
            // 通过seq-00000000获取该节点在children集合的位置
            int index = children.indexOf(thisNode);
            
            // 判断
            if(index == -1) {
                System.out.println("数据异常");
            } else if(index == 0) {
                // 就一个节点，可以获取锁了
                return;
            } else {
                // 需要监听它前一个节点变化
                waitPath = "/locks/" + children.get(index - 1);
                zk.getData(waitPath, true, null);
                
                // 等待监听
                waitLatch.await();
                
                return;
            }
        }
    }
    
    // 解锁
    public void zkUnLock() {
        
        // 删除节点
        zk.delete(currentMode, -1);
    }
}
```

### Curator框架实现分布式锁

Curator是一个专门解决分布式锁的框架，解决了原生Java API开发分布式遇到的问题