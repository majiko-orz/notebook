##  servlet

### servlet的实现

+ 实现servlet规范，即继承HttpServlet类（或继承GenericServlet，或实现servlet接口）
+ 重写service方法（或doGet、doPost方法）
+ 设置注解，使用@WebService将一个继承于javax.servlet.http.HttpServlet的类定义为Servlet组件。

```java
@WebServlet("/ser01")
public class Servlet01 extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpservletResponse resp) throws ServletException, IOException {
        
    }
}
```

**servlet生命周期**

+ 实例和初始化时机

  当请求到达容器时，容器查找该servlet对象是否存在，如果不存在，则会创建实例并进行初始化

+ 就绪/调用/服务阶段

  有请求到达容器，容器调用servlet对象的service()方法，处理请求的方法在整个生命周期中可以被多次调用。HttpServlet的service()方法，会依据请求方式来调用doGet()或者doPost()方法。但是，这两个do方法默认情况下会抛出异常，需要子类去重写

+ 销毁时机

  当容器关闭时（应用程序停止时），会将程序中的servlet实例进行销毁

上述的生命周期可以通过servlet中的生命周期方法来观察。在servlet中有三个生命周期方法，不由用户手动调用，而是在特定的时机有容器自动调用。

+ init方法，在servlet实例创建之后执行（证明该servlet有实例创建了）

  ```java
  public void init(ServletConfig config) throws ServletException {
      
  }
  ```

+ service方法，每次请求到达某个servlet方法时执行，用来处理请求（证明该servlet进行服务了）

  ```java
  protected void service(HttpServletRequest req, HttpservletResponse resp) throws ServletException, IOException {
          
      }
  ```

+ destroy方法，servlet实例销毁时执行（证明该servlet的实例被销毁了）

  ```java
  public void destroy() {
      
  }
  ```

servlet生命周期：servlet类加载 --> 实例化 --> 服务 --> 销毁

![](/img/servlet_1.png)

1. Web Client向servlet容器（tomcat）发出http请求
2. servlet容器接收Web Client的请求
3. servlet容器创建一个HttpServletRequest对象，将Web Client请求的信息封装到这个对象中
4. servlet容器创建一个HttpServletResponse对象
5. servlet容器调用HttpServlet对象service方法，把request与response作为参数，传给HttpServlet
6. HttpServlet调用HttpServletRequest对象的有关方法，获取http请求信息
7. HttpServlet调用HttpServletResponse对象的有关方法，生成响应数据
8. servlet容器把HttpServlet的响应结果传给Web Client

### HttpServletRequest对象

主要作用是用来接收客户端发送过来的请求信息，例如：请求的参数，发送的头信息等，service()方法中的形参接收的是HttpServletRequest接口的实例化对象，表示该对象主要应用在http协议上，该对象由tomcat封装好传递过来

**常用方法**

| 方法                     | 描述                                       |
| ------------------------ | ------------------------------------------ |
| getRequestURL()          | 获取客户端发出请求时的完整URL              |
| getRequestURI()          | 获取请求行中的资源名称部分（项目名称开始） |
| getQueryString()         | 获取请求行中的参数部分                     |
| getMethod()              | 获取客户端请求方式                         |
| getProtocol()            | 获取HTTP版本号                             |
| getContextPath()         | 获取webapp名称                             |
| getParameter(name)       | 获取指定名称的参数                         |
| getParameterValues(name) | 获取指定名称参数的所有值                   |

**请求乱码问题**

解析过程中默认的编码方式是ISO-8859-1，所以解析时会乱码

+ request.setCharacterEncoding("UTF-8");

  这种方式只针对post有效，无论使用什么版本，post请求都会乱码，tomcat8以后的GET方式请求不会出现乱码

+ new String(request.getParameter(name).getBytes("ISO-8859-1"),"UTF-8");

**请求转发**

请求转发是一种服务器的行为，当客户端请求到达后，服务器进行转发，此时会将请求对象进行保存，地址栏中的URL地址不会改变，得到响应后，服务器端再将响应发送给客户端，从始至终只有一个请求发出

request.getRequestDispatcher(url).forward(request,response);

请求数据可以共享

**request作用域**

通过该对象可以在一个请求中传递数据，作用范围：在一次请求中有效，即服务器跳转有效

```java
//设置域对象内容
request.setAttribute(name,value);
//获取域对象内容
request.getAttribute(name);
//删除域对象内容
request.removeAttribute(name);
```

### HttpServletResponse对象

**响应数据**

两种形式：

+ getWriter()：获取字符流（只能响应回字符）
+ getOutputStream()：获取字节流（能响应一切数据）

**响应乱码问题**

```java
//设置服务端的编码
response.setCharacterEncoding("UTF-8");
//设置客户端的响应类型及编码
response.setHeader("content-type","text/html;charset=UTF-8");

//以上两端编码的指定也可以使用一句代替
response.setContentType("text/html;charset=UTF8");
```

**重定向**

重定向是一种服务器指导，客户端的行为。客户端发出第一个请求，被服务器处理后，服务器会进行响应，在响应的同时，服务器会给客户端一个新的地址，当客户端接收到响应后，会立刻根据服务器给的新地址发起第二个请求，服务器接收请求并作出相应，重定向完成。

请求地址会发生变化，两次请求，request对象不共享

```java
//重定向跳转到index.jsp
response.sendRedirect("index.jsp");
```

**请求转发与重定向区别**

| 请求转发                        | 重定向                          |
| ------------------------------- | ------------------------------- |
| 一次请求，数据在request域中共享 | 两次请求，request域中数据不共享 |
| 服务器端行为                    | 客户端行为                      |
| 地址栏不发生变化                | 地址栏发生变化                  |
| 绝对地址定位到站点后            | 绝对地址可写到http://           |

### Cookie对象

Cookie是浏览器提供的一种技术，通过服务器的程序能将一些只须保存在客户端，或者在客户端进行处理的数据，放在本地的计算机上，不需要通过网络传输，因而提高网页处理的效率，并且能够减少服务器的负载，但是由于Cookie是服务器端保存在客户端的信息，所以其安全性也是很差的

有一个专门操作Cookie的类，javax.servlet.http.Cookie

```java
//Cookie的创建
Cookie cookie = new Cookie("uname","zhangsan");
response.addCookie(cookie);

//Cookie的获取
Cookie[] cookies = request.getCookies();
```

**Cookie设置到期时间**

默认当前浏览器关闭即失效。可以手动设置cookies的有效时间，通过setMaxAge(int time);方法设定cookie的最大有效时间，以秒为单位

到期事件取值：

+ 负整数

  若为负数，表示不存储该cookie，cookie的maxAge属性的默认值就是-1，表示只在浏览器内存中存活，一旦关闭浏览器窗口，cookie就会消失

+ 正整数

  若大于0的整数，表示存储的秒数。表示cookie对象可存活指定的秒数。当生命大于0时，浏览器会把Cookie保存到硬盘上，就算关闭浏览器，就算重启客户端电脑，cookie也会存活相应的时间

+ 零

  若为0，表示删除该cookie。cookie生命等于0是一个特殊的值，他表示cookie被作废。也就是说，如果原来浏览器已经保存了这个Cookie，那么可以通过Cookie的setMaxAge(0)来删除这个Cookie。无论是在浏览器内存中，还是在客户端的硬盘上都会删除这个Cookie

**Cookie注意点**

1. Cookie保存在当前浏览器中
2. Cookie不能存中文
3. 如果服务器端发送重复的Cookie会覆盖原有的cookie
4. 不同的浏览器中，Cookie的数量是有限的

**Cookie的路径**

Cookie的setPath设置cookie的路径，这个路径直接决定服务器的请求是否会从浏览器中加载某些cookie

```java
//设置路径为/，表示当前服务器下任何项目的任意资源都可获取cookie对象，默认为当前项目下
Cookie cookie = new Cookie("xxx","xxx");
cookie.setPath("/");
response.addCookie(cookie);
```

### HttpSession对象

HttpSession对象是javax.servlet.http.HttpSession的实例

对于服务器而言，每一个连接到他的客户端都是一个session，servlet容器使用此接口创建HTTP客户端和HTTP服务器之间的会话。会话将保留指定的时间段，跨多个连接或来自用户的页面请求。

```java
//获取session对象
HttpSession session = request.getSession();
```

**标识符JSESSIONID**

Session为了标识一次会话，那么这次会话就应该有一个唯一的标志，这个标志就是JSESSIONID

每当一次请求到达服务器，如果开启了会话（访问了session），服务器第一步会查看是否从客户端回传一个名为JSESSIONID的cookie，如果没有则认为这是一次新的会话，会创建一个新的session对象，并用唯一的sessionid为此次会话做一个标志。

session的底层依赖cookie实现

**session域对象**

```java
//设置域对象内容
session.setAttribute(name,value);
//获取域对象内容
session.getAttribute(name);
//删除域对象内容
session.removeAttribute(name);
```

**session对象的销毁**

```xml
<!-- tomcat中conf目录下web.xml文件，默认30分钟 -->
<session-config>
	<session-timeout>30</session-timeout>
</session-config>
```

```java
//设置session到期时间，单位秒
session.setMaxInactiveInterval(15);

//销毁session对象
session.invalidate();
```

关闭浏览器，session失效，关闭服务器，session销毁。

### ServletContext对象

每一个web应用有且仅有一个ServletContext对象，又称Application对象。

该对象有两大作用：

+ 作为域对象用来共享数据，此时数据在整个应用程序中共享
+ 该对象中保存了当前应用程序相关信息

```java
//通过request对象获取
ServletContext servletContext = request.getServletContext();

//通过session对象获取
ServletContext servletContext = request.getSession().getServletContext();

//通过servletConfig对象获取
ServletConfig servletConfig = getServletConfig();
ServletContext servletContext = servletConfig.getServletContext();

//直接获取
ServletContext servletContext = getServletContext();

//设置域对象内容
servletContext.setAttribute(name,value);
//获取域对象内容
servletContext.getAttribute(name);
//删除域对象内容
servletContext.removeAttribute(name);
```

## tomcat

### tomcat架构

#### HTTP工作原理

![](/img/tomcat_1.png)

**http服务器请求处理**

![](/img/tomcat_2.png)

**Servlet容器工作流程**

当客户请求某个资源时，HTTP服务器会用一个ServletRequest对象把客户的请求信息封装起来，然后调用Servlet容器的service方法，Servlet容器拿到请求后，根据请求的URL和Servlet的映射关系，找到对应的service，如果servlet还未加载，就用反射机制创建这个Servlet，并调用Servlet的init方法来完成初始化，接着调用servlet的service方法来处理请求，把ServletResponse对象返回给HTTP服务器，HTTP服务器会把响应发送给客户端

![](/img/tomcat_3.png)

#### tomcat整体架构

tomcat要实现两个核心功能：

+ 处理Socket连接，负责网络字节流与Request和Response对象的转化
+ 加载和管理Servlet，以及具体处理Request请求

因此tomcat设计了两个核心组件连接器（Connector）和容器（Container）来分别做这两件事

![](/img/tomcat_4.png)

#### 连接器-Coyote

Coyote是tomcat的连接器框架的名称，是tomcat服务器提供的供客户端访问的外部接口。客户端通过Coyote与服务器建立连接、发送请求并接受响应

Coyote封装了底层的网络通信（Socket请求及响应处理），为Catalina容器提供了统一的接口，使Catalina容器与具体的请求协议及IO操作方式完全解耦。Coyote将Socket输入转换封装为Request对象，交由Catalin容器进行处理，处理请求完成后，Catalina通过Coyote提供的Response对象将结果写入输出流

Coyote作为独立的模块，只负责具体协议和IO的相关操作，与Servlet规范实现没有直接关系，因此即便是Request和Response对象也并未实现Servlet规范对应的接口，而是在Catalina中将他们进一步封装为ServletRequest和ServletResponse

![](/img/tomcat_5.png)

**IO模型与协议**

在Coyote中，tomcat支持的多种IO模型和应用层协议。

tomcat支持的IO模型（自8.5/9.0版本起，tomcat移除了对BIO的支持）

| IO模型 | 描述                                                         |
| ------ | ------------------------------------------------------------ |
| NIO    | 非阻塞IO，采用Java NIO类库实现                               |
| NIO2   | 异步IO，采用JDK7最新的NIO2类库实现                           |
| APR    | 采用Apache可移植运行库实现，是C/C++编写的本地库。如果选择该方案，需要单独安装APR库 |

tomcat支持的应用层协议

| 应用层协议 | 描述                                                         |
| ---------- | ------------------------------------------------------------ |
| HTTP/1.1   | 这是大部分web应用采用的访问协议                              |
| AJP        | 用于和Web服务器集成（如Apache），以实现对静态资源的优化和集群部署，当前支持AJP/1.3 |
| HTTP/2     | HTTP2.0大幅度提升了Web性能，下一代HTTP协议，自8.5/9.0之后支持 |

**连接器组件**

![](/img/tomcat_6.png)

连接器中的各个组件的作用如下：

+ EndPoint：Coyote通信端点，即通信监听的接口，是具体Socket接受和发送处理器，是对传输层的抽象，因此EndPoint用来实现TCP/IP协议的

  tomcat并没有EndPoint接口，而是提供了一个抽象类AbstractEndPoint，里面定义了两个内部类：Acceptor和SocketProcessor。Acceptor用于监听Socket连接请求。SocketProcessor用于处理接收到的socket请求，它实现Runnable接口，在run方法里调用协议处理组件Processor进行处理。为了提高处理能力，SocketProcessor被提交到线程池来执行。而这个线程池叫做执行器（Executor）

+ Processor：Coyote协议处理接口，如果说EndPoint是用来实现TCP/IP协议的，那么Processor用来实现HTTP协议，Processor接收来自EndPoint的Socket，读取字节流解析成Tomcat Request和Response对象，并通过Adapter将其提交到容器处理，Processor是对应用层协议的抽象

+ ProtocolHandler：Coyote协议接口，通过EndPoint和Processor，实现针对具体协议的处理能力。tomcat按照协议和IO提供了6个实现类：AjpNioProtocol，AjpAprProtocol，AjpNio2Protocol，Http11NioProtocol，Http11Nio2Protocol，Http11AprProtocol。我们在配置tomcat/conf/server.xml时，至少要指定具体的ProtocolHandler，当然也可以指定协议名称，如：HTTP/1.1，如果安装了APR，那么将使用Http11AprProtocol协议，否则使用Http11NioProtocol

+ Adapter：CoyoteAdapter使用适配器模式，将request转成ServletRequest，再调用容器的Service方法

#### 容器-Catalina

![](/img/tomcat_7.png)

**Catalina结构**

![](/img/tomcat_8.png)

Catalina负责管理Server，而Server表示着整个服务器。Server下面有多个服务Service，每个服务都包含着多个连接器组件Connector(Coyote实现)和一个容器组件Container。在tomcat启动的时候，会初始化一个Catalina的实例

**Container结构**

tomcat设计了4种容器：Engine、Host、Context和Wrapper，这4种容器不是平行关系，而是父子关系

![](/img/tomcat_9.png)

+ Engine：表示整个Catalina的Servlet引擎，用来管理多个虚拟站点，一个Service最多只能有一个Engine，但是一个引擎包含多个Host
+ Host：代表一个虚拟主机，或者说一个站点，可以给Tomcat配置多个虚拟主机地址，而一个虚拟主机下可包含多个Context
+ Context：表示一个Web应用程序，一个Web应用程序可包含多个wrapper
+ Wrapper：表示一个Servlet，wrapper作为容器中的最底层，不能包含子容器

#### tomcat启动流程

![](/img/tomcat_10.png)

#### tomcat请求处理流程

tomcat是怎么确定每一个请求应该由哪个wrapper容器里的servlet来处理的呢？

tomcat是用mapper组件来完成这个任务的。Mapper组件的功能就是将用户请求的URL定位到一个Servlet，他的工作原理是：Mapper组件里保存了Web应用的配置信息，其实就是容器组件与访问路径的映射关系，比如Host容器里配置的域名、Context容器里的Web应用路径，以及Wrapper容器里servlet映射的路径

当一个请求到来时，Mapper组件通过解析请求URL里的域名和路径，再到自己保存的Map里去找，就能定位到一个servlet。

![](/img/tomcat_11.png)

![](/img/tomcat_12.png)



**请求流程源码解析**

![](/img/tomcat_13.png)

### tomcat服务器配置

#### server.xml

server.xml是tomcat服务器的核心配置文件，包含了tomcat的servlet容器的所有配置

**Server**

Server是server.xml的根元素，用于创建一个Server实例，默认使用的实现类是org.apache.catalina.core.StandardServer

```xml
<server port="8005" shutdown="SHUTDOWN">
</server>
```

port：tomcat监听的关闭服务器的端口

shutdown：关闭服务器的指令字符串

Server内嵌的子元素为Listener、GlobalNamingResources、Service

```xml
<!-- 默认配置的5个Listener的含义 -->

<!-- 用于以日志形式输出服务器、操作系统、JVM的版本信息 -->
<Listener className="org.apache.catalina.startup.VersionLoggerListener"/>

<!-- 用于加载（服务器启动）和销毁（服务器停止）APR，如果找不到APR库，则会输出日志，并不影响tomcat启动 -->
<Listener className="org.apache.catalina.core.AprLifecycleListener" SSLEngine="on"/>

<!-- 用于避免JRE内存泄漏问题 -->
<Listener className="org.apache.catalina.core.JreMemoryLeakPreventionListener"/>

<!-- 用于加载（服务器启动）和销毁（服务器停止）全局命名服务 -->
<Listener className="org.apache.catalina.mbeans.GlobalResourcesLifecycleListener"/>

<!-- 用于在Context停止时重建Executor池中的线程，以避免ThreadLocal相关的内存泄漏 -->
<Listener className="org.apache.catalina.core.ThreadLocalLeakPreventionListener"/>
```

**Service**

该元素用来创建Service实例，默认使用org.apache.catalina.core.StandardService。默认情况下，tomcat仅指定了Service的名称，值为"Catalina"。Service可以内嵌的元素为：Listener、Executor、Connector、Engine，其中Listener用于为Service添加生命周期监听器，Executor用于配置Service共享线程池，Connector用于配置Service包含的连接器，Engine用于配置Service中连接器对应的Servlet容器引擎

**Executor**

默认情况下，Service并未添加共享线程池配置。如果想添加一个线程池，可以添加如下配置

```xml
<Executor name="tomcatThreadPool"
          namePrefix="catalina-exec-"
          maxThreads="1000"
          minSpareThreads="100"
          maxIdleTime="6000"
          maxQueueSize="Interger.MAX_VALUE"
          prestartminSpareThreads="false"
          threadPriority="5"
          className="org.apache.catalina.core.StandardThreadExecutor"/>
```

**Connector**

Connector用于创建连接器实例。默认情况下，server.xml配置了两个连接器，一个支持HTTP协议，一个支持AJP协议

```xml
<Connector port="8080" protocol="HTTP/1.1" connectionTimeout="20000" redirectPort="8443" />

<Connector port="8009" protocol="AJP/1.3" redirectPort="8443" />
```

**Engine**

Engine作为Servlet引擎的顶级元素，内部可以嵌入：Cluster、Listener、Realm、Valve和Host

```xml
<Engine name="Catalina" defaultHost="localhost">
</Engine>
```

**Host**

Host元素用于配置一个虚拟主机，它支持以下嵌入元素：Alias、Cluster、Listener、Valve、Realm、Context。如果在Engine下配置Realm，那么此配置将在当前Engine下的所有Host中共享。同样，如果在Host中配置Realm，则在当前Host下的所有Context中共享。Context中的Realm优先级>Host的Realm优先级>Engine中的Realm优先级

```xml
<Host name="localhost" appBase="webapps" unpackWARS="true" autoDeploy="true"></Host>
```

**Context**

Context用于配置一个Web应用

```xml
<Context doBase="myApp" path="/myApp">
</Context>
```

#### tomcat-users.xml

该配置文件中，主要配置的是tomcat的用户，角色等信息，用来控制tomcat中的manager，host-manager的访问权限

### Web应用配置

web.xml是web应用的描述文件，它支持的元素及属性来自于Servlet规范定义。

```xml
<context-param>
	<param-name>project_param_01</param-name>
    <param-value>itcast</param-value>
</context-param>
```

servlet中获取参数：req.getServletContext().getInitParameter("project_param_01")





## spring5

![](\img\spring_1.png)

### IOC（概念和原理）

#### 什么是IOC

1. 控制反转，把对象创建和对象之间的调用过程交给spring进行管理
2. 使用ioc的目的：降低耦合度

#### IOC底层原理

xml解析、工厂模式、反射

#### IOC过程

1. xml配置文件，配置创建的对象

<bean id="dao" class="com.atguigu.UserDao"></bean>

2. 有service类和dao类，创建工厂类

```java
class UserFctory{
    public static UserDao getDao(){
        String classValue=class属性值;//1.xml解析
        Class clazz=Class.forName(classValue);//2.通过反射创建对象
        return (UserDao)clazz.newInstance();
    }
}
```

#### IOC(接口)

1. IOC思想基于IOC容器完成，IOC容器底层就是对象工厂
2. spring提供IOC容器两种实现方式：（两个接口）

+ **BeanFactory**：IOC容器基本实现，是spring内部的使用接口，不提供开发人员进行使用

  加载配置文件时候不会创建对象，在获取对象（使用）才去创建对象

+ **ApplicationContext**：BeanFactory接口的子接口，提供更多更强大的功能，一般由开发人员进行使用

  加载配置文件时候就会把配置文件对象进行创建

3. ApplicationContext接口有实现类

主要实现类**FileSystemXmlApplicationContext、ClassPathXmlApplicationContext**

### IOC操作 Bean管理

1. 什么是bean管理

+ spring创建对象
+ spring注入属性

2. bean管理操作有两种方式

+ 基于xml配置文件方式实现
+ 基于注解方式实现

#### 基于xml方式创建对象

<bean id="dao" class="com.atguigu.UserDao"></bean>

1. 在spring配置文件中，使用bean标签，标签里面添加对应的属性，就可以实现对象创建

2. bean标签常用属性

   id属性：唯一标识

   class属性：类全路径（包类路径）

3. 创建对象时候，默认也是执行无参构造方法完成对象创建

#### 基于xml方式注入属性

1. DI：依赖注入，就是注入属性，是IOC的具体实现

+ 第一种注入方式：使用set方法进行注入
  + 创建类，定义set方法
  + 在spring配置文件配置对象创建，配置属性注入

+ 第二种注入方式：使用有参构造进行注入

2. p名称空间注入（了解）

​    使用p名称空间注入，可以简化基于xml配置方式

​	第一步：添加p名称空间在配置文件中

​	xmlns:p="http://www.springframework.org/schema/p"

​	第二步：在bean标签里边进行操作

3. xml注入其他类型属性

   1. 字面量
      1. 设置null值
      2. 属性值包含特殊符号
   2. 注入属性-外部bean
      1. 创建两个类service类和dao类
      2. 在service调用dao里面的方法

   3. 注入属性-内部bean和级联赋值

4. xml注入集合属性

   1. 注入数组类型属性
   2. 注入List集合类型属性
   3. 注入map集合类型属性
      1. 创建类，定义数组、list、map、set类型属性，生成对应set方法
      2. 在spring的配置文件中进行配置
   4. 在集合里面设置对象类型值
   5. 把集合注入部分提取出来
      1. 在spring配置文件中引入名称空间util
      2. 使用util标签完成list集合注入提取

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    <!--p名称空间注入 -->
	xmlns:p="http://www.springframework.org/schema/p"
	xmlns:util="http://www.springframework.org/schema/util"
	<!--aop名称空间注入-->
	xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:context="http://www.springframework.org/schema/context"
	<!--tx名称空间注入-->
	xmlns:tx="http://www.springframework.org/schema/tx"
    xsi:schemaLocation="
http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
http://www.springframework.org/schema/util
        https://www.springframework.org/schema/util/spring-util.xsd
http://www.springframework.org/schema/aop
        https://www.springframework.org/schema/aop/spring-aop.xsd
http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd
http://www.springframework.org/schema/tx
        https://www.springframework.org/schema/tx/spring-tx.xsd
" >

    <bean id="book" class="com.spring5.book">
        <!--使用property完成属性注入-->
    	<property name="bname" value="张三"></property>
        <!--null值-->
        <property name="address">
        	<null/>
        </property>
        <property name="address1" value=""></property>
        <!--属性值包含特殊符号
				1.把《》进行转义 &lt; &gt;
				2.把带特殊符号内容写到CDATA-->
        <property name="address2">
        	<value><![CDATA[《南京》]]></value>
        </property>
    </bean>
	
	<!--p名称空间注入-->
	<bean id="book1" class="com.spring5.Book1" p:name="bname1" p:author="zhangsan"></bean>

    <bean id="orders" class="com.spring5.Orders">
        <!--有参构造注入属性-->
    	<constructor-arg name="oname" value="abc"></constructor-arg>
        <constructor-arg index="0" value="123"></constructor-arg>
    </bean>
	
	<!--外部bean注入-->
	<bean id="userService" class="com.spring5.UserService">
        <!-- name属性：类里面属性名称。ref属性：创建userDao对象bean标签id -->
		<property name="userDao" ref="userDaoImpl"></property>
	</bean>
	<bean id="userDaoImpl" class="com.spring5.UserDaoImpl"></bean>
		
	<bean id="emp" class="com.spring5.Emp">
		<property name="ename" value="lucy"></property>
        <property name="gender" value="女"></property>
        <!--内部bean-->
        <property name="dept">
        	<bean id="dept" class="com.spring5.Dept">
            	<property name="dname" value="安保部"></property>
            </bean>
        </property>
        
        <!--级联赋值 第一种写法-->
        <property name="dept" ref="dept"></property>
        <!--级联赋值 第二种写法-->
        <property name="dept.dname" value="技术部"></property>
	</bean>
	<bean id="dept" class="com.spring5.Dept">
		<property name="dname" value="财务部"></property>
	</bean>

	<!--集合类型属性的注入-->
	<bean id="stu" class="com.spring5.Stu">
        <!--数组类型属性注入-->
		<property name="courses">
        	<array>
            	<value>java</value>
                <value>数据库</value>
            </array>
        </property>
        
        <!--list类型属性注入-->
		<property name="list">
        	<list>
            	<value>张三</value>
                <value>李四</value>
            </list>
        </property>
        
        <!--map类型属性注入-->
		<property name="maps">
        	<map>
            	<entry key="JAVA" value="java"></entry>
                <entry key="Python" value="python"></entry>
            </map>
        </property>
        
         <!--set类型属性注入-->
		<property name="sets">
        	<set>
            	<value>MySQL</value>
                <value>Redis</value>
            </set>
        </property>
        
        <!--注入list集合类型，值是对象-->
        <property name="courseList">
        	<list>
            	<ref bean="course1"></ref>
                <ref bean="course2"></ref>
            </list>
        </property>
	</bean>

	<bean id="course1" class="com.spring5.Course">
		<property name="cname" value="Spring5"></property>
	</bean>
	<bean id="course2" class="com.spring5.Course">
		<property name="cname" value="MyBatis"></property>
	</bean>

	<!--提取list集合类型属性注入-->
	<util:list id="bookList">
		<value>1</value>
        <value>2</value>
        <value>3</value>
	</util:list>
	<!--提取list集合类型属性注入使用-->
	<bean id="book" class="com.spring5.Book">
		<property name="list" ref="bookList"></property>
	</bean>

	<!--自动装配-->
	<bean id="emp" class="com.spring5.Emp" autowire="byName/byType">
        <property name="dept" ref="dept"></property>
	</bean>
	<bean id="dept" class="com.spring5.Dept"></bean>

	<!--直接配置连接池-->
	<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
        <preperty name="url" value="jdbc:mysql://localhost:3306/userDb"></preperty>
        <property name="username" value="root"></property>
        <property name="password" value="root"></property>
	</bean>

	<!--引入外部属性文件-->
	<context:property-placeholder location="classpath:jdbc.properties"/>
	<!--配置连接池-->
	<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driverClassName" value="${prop.driverClass}"></property>
        <preperty name="url" value="${prop.url}"></preperty>
        <property name="username" value="${prop.username}"></property>
        <property name="password" value="${prop.password}"></property>
	</bean>

	<!--开启组件扫描,多个包之间使用逗号隔开-->
	<context:component-scan base-package="com.spring5.dao,com.spring5.service">			</context:component-scan>

	<!--开启组件扫描细节配置-->
		<!--示例1
			use-default-filters="false" 表示现在不使用默认filter，自己配置filter
			context:include-filter,设置扫描哪些内容-->
	<context:component-scan base-package="com.spring5" use-default-filters="false">
		<context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
	</context:component-scan>
		<!--示例2
			context:exclude-filter,设置不扫描哪些内容-->
	<context:component-scan base-package="com.spring5">
		<context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"
	</context:component-scan>
        
    <!--开启aspectj,生成代理对象-->    
    <aop:aspectj-autoproxy></aop:aspectj-autoproxy>
        
    <!--aop配置-->
    <bean id="book" class="com.spring5.aopxml.Book"></bean>
    <bean id="bookProxy" class="com.spring5.aopxml.BookProxy"></bean>
        
    <aop:config>
        <!--切入点-->
        <aop:pointcut id="p" expression="execution(* com.spring5.Book.buy(..))"></aop:pointcut>
        <!--配置切面-->
        <aop:aspect ref="bookProxy">
            <!--增强作用在具体的方法上-->
            <aop:before method="before" pointcut-ref="p"></aop:before>
        </aop:aspect>
    </aop:config>   
        
    <!--创建事务管理器-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <!--注入数据源-->
        <property name="dataSource" ref="dataSource"></property>
    </bean>
    <!--开启事务注解-->
    <tx:annotation-driven transaction-manager="transactionManager"></tx:annotation-driven>
        
    <!--xml方式配置事务-->
    <!--配置通知-->
    <tx:advice id="txadvice">
    	<!--配置事务参数-->
        <tx:attributes>
            <!--指定哪种规则的方法上面添加事务-->
            <tx:method name="accountMoney" propagation="REQUIRED"/>
            <tx:method name="account*"/>
        </tx:attributes>
    </tx:advice>
    <!--配置切入点和切面-->
    <aop:config>
        <!--配置切入点-->
        <aop:pointcut id="pt" expression="(* com.spring5.service.UserService.add(..))"></aop:pointcut>
        <!--配置切面-->
        <aop:advisor advice-ref="txadvice" pointcut-ref="pt"></aop:advisor>
    </aop:config>
        
    <!--springMVC-->
    <!--Controller的组件扫描-->
    <context:component-scan base-package="com.spring5.dao,com.spring5.controller"></context:component-scan>
    <!--配置内部资源视图解析器-->
    <bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    	<property name="prefix" value="/jsp"></property>
        <property name="suffix" value=".jsp"></property>
    </bean>
    <!--配置处理器映射器-->
    <bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
    	<property name="messageConverters">
        	<list>
            	<bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter"/>
            </list>
        </property>  
    </bean>
    <!--mvc的注解驱动-->
    <mvc:annotation-driven></mvc:annotation-driven>jioayu
    <!--开发资源的访问-->
    <mvc:resources mapping="/js/**" location="/js/"></mvc:resources>
    <!--配置拦截器-->
	<mvc:interceptors>
    	<mvc:interceptor>
            <!--对哪些资源执行拦截操作-->
            <mvc:mapping path="/**/"/>
            <bean class="com.springmvc.MyInterceptor"></bean>  
        </mvc:interceptor>    
    </mvc:interceptors>
</beans>
```

#### IOC操作bean管理（FactoryBean）

1. spring有两种类型bean,一种普通bean,另外一种是工厂bean(FactoryBean)
2. 普通bean：在配置文件中定义bean类型就是返回类型
3. 工厂bean：在配置文件定义bean类型可以和返回类型不一致
   1. 创建类，让这个类作为工厂bean，实现接口FactoryBean
   2. 实现接口里面的方法，在实现的方法中定义返回的bean类型

#### IOC操作bean管理（bean作用域）

1. 在spring里面，设置创建bean实列是单实例还是多实例
2. 在spring里面，默认情况下，bean是单实例对象

3. 如何设置单实例还是多实例
   1. 在spring配置文件bean标签里面有属性（scope）用于设置单实例还是多实例
   2. scope属性值
      1. singleton，默认值，单实例
      2. prototype，表示多实例对象
      3. 设置scope值是singleton时候，加载spring配置文件时候就会创建单实例，设置scope值是prototype时候，不是在加载spring配置文件时候创建对象，在调用getBean方法时候创建多实例对象
      4. request, Web项目中，给每一个http request新建一个Bean实例 
      5. session, Web项目中，给每一个http session新建一个Bean实例。 
      6.  GlobalSession:这个只在portal应用中有用，给每一个global http session新建一个Bean实例。 

#### IOC操作bean管理（bean生命周期）

1. bean生命周期
   1. 通过构造器创建bean实例（无参数构造）
   2. 为bean的属性设置值和对其他bean引用（调用set方法)
   3. 把bean实例传递给bean后置处理器的方法，执行postProcessBeforeInitialization
   4. 调用bean的初始化的方法（需要进行配置）init-method
   5. 把bean实例传递给bean后置处理器的方法，执行postProcessAfterInitialization
   6. bean可以使用了（对象获取到了）
   7. 当容器关闭时，调用bean的销毁方法（需要进行配置销毁的方法）destroy-method
2. 创建后置处理器
   1. 创建类实现接口BeanPostProcessor,创建后置处理器
   2. 后置处理器会为当前配置文件中的所有bean添加后置处理器

#### IOC操作bean管理（自动装配）

1. 什么是自动装配

   根据指定装配规则（属性名称和属性类型），spring自动将匹配的属性值进行注入

2. 自动装配过程(见spring配置文件)

#### IOC操作bean管理（外部属性文件）

1. 直接配置数据库信息

   1. 配置druid连接池
   2. 引入druid连接池依赖jar包

2. 引入外部属性文件配置数据库连接池

   1. 创建外部配置文件

   2. 把外部配置文件引入到spring配置文件中

      引入context名称空间，在spring配置文件使用标签引入外部属性文件

#### IOC操作bean管理（基于注解方式）

1. spring针对bean管理中创建对象提供注解
   1. @Component：value属性可以省略，默认值是类名称首字母小写
   2. @Service
   3. @Controller
   4. @Repository

上面四个注解功能是一样的，都可以用来创建bean实例

2. 基于注解方式实现对象创建
   1. 引入依赖
   2. 开启组件扫描
   3. 开启组件扫描细节配置
3. 基于注解方式实现属性注入
   1. @Autowired：根据属性类型进行自动装配
      1. 把service和dao对象创建，在service和dao类添加创建对象注解
      2. 在service里面注入dao对象，在service类添加dao类型属性，在属性上面使用注解
   2. @Qualifier：根据属性名称进行注入
      1. 这个@Qualifier注解的使用，和上面@Autowired一起使用
   3. @Resource：可以根据类型注入，可以根据名称注入（填写name参数）
   4. @Value：注入普通类型属性
4. 完全注解开发
   1. 创建配置类，替代xml配置文件
   2. @ComponentScan(basePackages={"com.spring5"})
   
   

SpelExpressionParser:spel表达式解析器

DefaultParameterNameDiscover:参数名发现器

### AOP(概念和原理)

1. 什么是AOP

   1. 面向切面编程，利用AOP可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各部分之间的耦合度降低，提高程序的可重用性，同时提高了开发的效率
   2. 通俗描述：不通过修改源代码的方式，添加新的功能

2. AOP底层原理

   1. AOP底层使用动态代理

      1. 两种情况的动态代理

         第一种，有接口情况，使用JDK动态代理

         创建接口实现类的代理对象，增强类的方法

         第二种，没有接口情况，使用CGLIB动态代理

         创建子类的代理对象，增强类的方法

   2. JDK动态代理

      使用JDK动态代理，使用Proxy类里面的newProxyInstance方法创建代理对象

      ```java
      public static Object newProxyInstance(ClassLoader loader,
                                            类<?>[] interfaces,
                                            InvocationHandler h)
                                     throws IllegalArgumentException
      ```
   
      方法有三个参数：

      第一参数：类加载器

      第二参数：增强方法所在类，这个类实现的接口，支持多个接口

      第三参数：实现这个接口InvocationHandler,创建代理对象，写增强的方法

      ```java
      class UserDaoProxy implements InvocationHandler {
          //1.把创建的是谁的代理对象，把谁传进来，UserDaoImpl
          //有参构造传递
          private Object obj;
          public UserDaoProxy(Object obj) {
              this.obj = obj;
          }
          
          @Override
          public Object invoke(Object proxy, Method method, Object[] args) throw Throwable {
              //方法之前
              System.out.println("方法之前执行");
              
              //被增强的方法
              Object res = method.invoke(obj, args);
              
              //方法之后
              System.out.println("方法之后执行");
              
              return res;
          }
      }
      ```
   
      https://blog.csdn.net/weixin_38899094/article/details/115655832?spm=1001.2101.3001.6650.4&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-4-115655832-blog-124208539.235%5Ev35%5Epc_relevant_anti_vip&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%7ERate-4-115655832-blog-124208539.235%5Ev35%5Epc_relevant_anti_vip&utm_relevant_index=6
   
   3. CGLIB动态代理


### AOP（术语）

- **连接点：**类里面哪些方法可以被增强，这些方法称为连接点
- **切入点：**实际被真正增强的方法
- **通知：**实际增强的逻辑部分
- **Aspect：切面**，把通知应用到切入点过程，由一系列切点、增强和引入组成的模块对象，可定义优先级，从而影响增强和引入的执行顺序。事务管理（Transaction management）在java企业应用中就是一个很好的切面样例。
- **Join point：接入点**，程序执行期的一个点，例如方法执行、类初始化、异常处理。 在Spring AOP中，接入点始终表示方法执行。
- **Advice：增强**，切面在特定接入点的执行动作，包括 “around,” “before” and “after”等多种类型。包含Spring在内的许多AOP框架，通常会使用拦截器来实现增强，围绕着接入点维护着一个拦截器链。
- **Pointcut：切点**，用来匹配特定接入点的谓词（表达式），增强将会与切点表达式产生关联，并运行在任何切点匹配到的接入点上。通过切点表达式匹配接入点是AOP的核心，Spring默认使用AspectJ的切点表达式。
- **Introduction：引入**，为某个type声明额外的方法和字段。Spring AOP允许你引入任何接口以及它的默认实现到被增强对象上。
- **Target object：目标对象**，被一个或多个切面增强的对象。也叫作被增强对象。既然Spring AOP使用运行时代理（runtime proxies），那么目标对象就总是代理对象。
- **AOP proxy：AOP代理**，为了实现切面功能一个对象会被AOP框架创建出来。在Spring框架中AOP代理的默认方式是：有接口，就使用基于接口的JDK动态代理，否则使用基于类的CGLIB动态代理。但是我们可以通过设置`proxy-target-class="true"`，完全使用CGLIB动态代理。
- **Weaving：织入**，将一个或多个切面与类或对象链接在一起创建一个被增强对象。织入能发生在编译时 （compile time ）(使用AspectJ编译器)，加载时（load time），或运行时（runtime） 。Spring AOP默认就是运行时织入，可以通过`枚举AdviceMode`来设置。
- **五种通知：**
  - **前置通知:**
  - **后置通知:**
  - **环绕通知:**
  - **异常通知:**
  - **最终通知:**

![advice执行顺序](img\advice执行顺序.png)

### AOP操作（准备）

1. spring框架一般基于AspectJ实现AOP操作

   AspectJ不是spring组成部分，独立AOP框架，一般把AspectJ和spring框架一起使用，进行AOP操作

2. 基于AspectJ实现AOP操作

   1. 基于xml配置文件实现
   2. 基于注解方式实现（使用）

3. 切入点表达式

   1. 切入点表达式作用：知道对哪个类里面的哪个方法进行增强

   2. 语法结构

      execution([权限修饰符] [返回类型] [类全路径] [方法名称] ([参数列表]))

      例子：execution(* com.dao.BookDao.add(..))

### AOP操作（AspectJ注解）

1. 创建类，在类里面定义方法

2. 创建增强类（编写增强逻辑）

   1. 在增强类里面，创建方法，让不同方法代表不同通知类型

3. 进行通知的配置

   1. 在spring配置文件中，开启注解扫描
   2. 使用注解创建User和UserProxy对象,@Component
   3. 在增强类上面添加注解@Aspect
   4. 在spring配置文件中开启生成代理对象

4. 配置不同类型的通知

   在增强类的里面，在作为通知方法上面添加通知类型注解，使用切入点表达式配置

5. 公共切入点提取

   ```java
   @Pointcut(value="execution()")
   public void pointdemo(){
       
   }
   @Before(value="pointdemo()")
   ```

6. 多个增强类对同一个方法进行增强，设置增强优先级

   在增强类上面添加注解@Order(数字类型值)，数字类型值越小，优先级越高

7. 完全使用注解开发

   创建配置类，不需要创建xml配置文件

   ```java
   @Configuration
   @ComponentScan(basePackages={"com.spring5"})
   @EnableAspectJAutoProxy(proxyTargetClass=true)
   public class ConfigAop(){
   }
   ```

### AOP操作（AspectJ配置文件）

1. 创建两个类，增强类和被增强类，创建方法
2. 在spring配置文件中创建两个类对象
3. 在spring配置文件中配置切入点
4. 配置类上加入@EnableAspectJAutoProxy注解

### 事务操作

1. 什么是事务

事务是数据库操作最基本单元，逻辑上一组操作，要么都成功，如果有一个失败所有操作都失败

典型场景：银行转账

2. 事务四大特性（ACID）

   1. 原子性
   2. 一致性
   3. 隔离性
   4. 持久性

3. 事务操作过程

   1. 开启事务
   2. 进行业务操作
   3. 没有发生异常，提交事务
   4. 出现异常，事务回滚

4. spring事务管理

   1. 事务添加到JavaEE三层结构里面Service层（业务逻辑层）

   2. 在spring进行事务管理操作

      两种方式：编程式事务管理，声明式事务管理（使用）

   3. 声明式事务管理

      1. 基于注解方式（使用）
      2. 基于xml配置文件方式

   4. 在spring进行声明式事务管理，底层使用AOP原理

   5. spring事务管理api

      1. 提供一个接口，代表事务管理器，这个接口针对不同的框架提供不同的实现类，PlatformTransactionManager

### 事务操作（注解声明式事务管理）

1. 在spring中配置事务管理器
2. 在spring配置文件，开启事务注解
   1. 在spring配置文件引入名称空间tx
   2. 开启事务注解
3. 在service类上面（或service类里面方法上面）添加事务注解 @Transactional

### 事务操作（声明式事务管理参数配置）

在service类上面添加@Transactional，在这个注解里面可以配置事务相关参数

1. propagation：事务传播行为

   ```java
   @Transactional
   public void add(){
       //调用update方法
       update();
   }
   public void update(){
       
   }
   ```

   Spring定义了七种传播行为，这里以方法A和方法B发生嵌套调用时如何传播事务为例说明： 

   1. REQUIRED:如果A有事务，B将使用该事务；如果A没有事务，B将创建一个新事务
   2. REQUIRES_NEW:如果A有事务，将A的事务挂起，B创建一个新的事务；如果A没有事务，B将创建一个新的事务
   3. NOT_SUPPORTED:如果A有事务，将A的事务挂起，B将以非事务执行；如果A没有事务，B将以非事务执行
   4. SUPPORTS:如果A有事务，B将使用该事务；如果A没有事务，B将以非事务运行
   5. MANDATORY:如果A有事务，B将使用该事务，如果A没有事务，B将抛异常
   6. NEVER:如果A有事务，B将抛异常；A如果没有事务，B将以非事务执行
   7. NESTED:A和B底层采用保存点机制，形成嵌套事务

2. isolation：事务隔离级别

   隔离级别就是用来描述并发事务之间隔离程度的大小，在并发事务之间如果不考虑隔离性，会引发如下安全性问题： 

   **脏读：**一个事务读到了另一个事务未提交的数据

   **不可重复读：**一个事务读到了另一个事务已经提交的update的数据导致多次查询结果不一致，侧重于数据修改

   **幻读：**一个事务读到了另一个事务已经提交的insert的数据导致多次查询结果不一致，侧重于记录数的改变

   在spring事务管理中，为我们定义了如下的**隔离级别**：

   1. **DEFAULT:**使用数据库默认的隔离级别

   2. **READ_UNCOMMITTED:**最低的隔离级别，允许读取已改变而没有提交的数据，可能会导致脏读、幻读、不可重复读

   3. **COMMITTED:**允许读取事务已经提交的数据，可以组织脏读，但是幻读、不可重复读仍有可能发生

   4. **REPEATABLE_READ:**对同一字段的多次读取的结果都是一致的，除非数据事务本身改变，可以阻止脏读、不可重复读，但幻读仍有可能发生

   5. **SERIALIZABLE:**最高的隔离级别，完全服从ACID的隔离级别，确保不发生脏读、不可重复读、幻读，也是最慢的事务隔离级别，因为它通常是通过完全锁定事务相关数据库表来实现的

3. timeout：超时时间

   1. 事务需要在一定时间内进行提交，如果不提交进行回滚
   2. 默认值是-1，设置时间以秒单位进行计算

4. readOnly：是否只读

   1. 读：查询操作，写：添加删除修改操作
   2. readOnly默认值false，表示可以查询，可以添加修改删除操作
   3. 设置readOnly值是true，设置成true之后，只能查询

5. rollbackFor：回滚

   设置出现哪些异常进行回滚

6. noRollbackFor：不回滚

   设置出现哪些异常不进行回滚

### 事务操作（XML声明式事务管理）

1. 在spring配置文件中进行配置
   1. 配置事务管理器
   2. 配置通知
   3. 配置切入点和切面

### 事务操作（完全注解开发）

1. 创建配置类，替代xml

   ```java
   @Configuration
   @ComponentScan(basePackages="com.atguigu") //组件扫描
   @EnableTransactionManagement //开启事务
   public class TxConfig {
       
       @Bean
       public DruidDataSource getDruidDataSource() {
           DruidDataSource dataSource = new DruidDataSource();
           dataSource.setDriverClassName("com.mysql.jdbc.Driver");
           dataSource.setUrl("jdbc:mysql:///user_db");
           dataSource.setUsername("root");
           dataSource.setPassword("root");
           return dataSource;
       }
       
       @Bean
       public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
           JdbcTemplate jdbcTemplate = new JdbcTemplate();
           jdbcTemplate.setDataSource(dataSource);
           return jdbcTemplate;
       }
       
       @Bean
       public DataSourceTransactionManager getDataSourceTransactionManager() {
           DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
           transactionManager.setDataSource(dataSource);
           return transactionManager;
       }
   }
   ```

### Webflux

spring5添加的新模块，用于web开发，功能和Spring MVC类似，响应式编程，异步非阻塞框架，Servlet3.1以后支持，核心是基于Reactor的相关API实现

**特点**

+ 非阻塞：在有限资源下，提高系统吞吐量和伸缩性，以Reactor为基础实现响应式编程
+ 函数式编程：spring5框架基于Java8，webflux使用Java8函数式编程方式实现请求路由



## springmvc

**配置web.xml**

1. 默认配置方式

   springMVC的配置文件默认位于WEB-INF下。默认名称为< servlet-name>-servlet.xml

```xml
<!-- 配置springMVC的前端控制器，对浏览器发送的请求统一进行处理 -->
<servlet>
	<servlet-name>springMVC</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <servlet-mapping>
    	<servlet-name>springMVC</servlet-name>
        <!-- 配置springMVC能处理的请求路径，不能匹配.jsp请求 -->
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</servlet>
```

2. 扩展配置方式

可通过init-param标签设置springMVC配置文件的位置和名称，通过load-on-startup标签设置springMVC前端控制器DispatcherServlet的初始化时间

```xml
<!-- 配置springMVC的前端控制器，对浏览器发送的请求统一进行处理 -->
<servlet>
	<servlet-name>springMVC</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <!-- 配置springMVC配置文件的位置和名称 -->
    <init-param>
    	<param-name>contextConfigLocation</param-name>
        <param-value>classpath:springMVC.xml</param-value>
    </init-param>
    <!-- 将前端控制器DispatcherServlet的初始化时间提前到服务器启动时 -->
    <load-on-startup>1</load-on-startup>
    
    <servlet-mapping>
    	<servlet-name>springMVC</servlet-name>
        <!-- 配置springMVC能处理的请求路径，不能匹配.jsp请求 -->
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</servlet>
```

**springMVC配置文件springMVC.xml**

```xml
<!-- 扫描组件 -->
<context:component-scan base-package="com.atguigu.mvc.controller"></context:component-scan>
```

**springMVC支持ant风格的路径**

+ ?：表示任意的单个字符
+ *：表示任意的0个或多个字符
+ **：表示任意的一层或多层目录

注意：在使用**时，只能使用 / * */xx的方式

**springMVC支持路径中的占位符**

原始方式：/deleteUser?id=1

rest方式：/deleteUser/1

springMVC路径中的占位符通常用于restful风格中，当请求路径中将某些数据通过路径的方式传输到服务器中，就可以在相应的@RequestMapping注解的value属性中通过占位符{xxx}表示传输的数据，在通过@PathVariable注解，将占位符所表示的数据赋值给控制器方法的形参

```java
@RequestMapping("/testRest/{id}/{username}")
public String testRest(@PathVariable("id") String id,@PathVariable("username") String username){
    return "success";
}
```

### springMVC获取请求参数

1. 通过servletAPI获取

   将HttpServletRequest作为控制器方法的形参，此时HttpServletRequest类型的参数表示封装了当前请求的请求报文的对象

   ```java
   @RequestMapping("/testParam")
   public String testParam(HttpServletRequest request){
       String username = request.getParameter("username");
       String password = request.getParameter("password");
       return "success";
   }
   ```

2. 通过控制器方法的形参获取请求参数

   在控制器方法的形参位置，设置和请求参数同名的形参，当浏览器发送请求，匹配到请求映射时，在DispatcherServlet中就会将请求参数赋值给相应的形参

   若请求参数中有多个同名的请求参数，可以在控制器方法的形参中设置字符串数组或者字符串类型的形参接收此请求参数

   若使用字符串数组类型的形参，此参数的数组中包含了每一个数据

   若使用字符串类型的形参，此参数的值为每个数据中间使用逗号拼接的结果

   ```java
   @RequestMapping("/testParam")
   public String testParam(String username, String password){
       String username = username;
       String password = password;
       return "success";
   }
   ```

3. @RequestParam

   @RequestParam是将请求参数和控制器方法的形参创建映射关系

   @RequestParam注解一共有三个属性：

   + value：指定为形参赋值的请求参数的参数名
   + required：设置是否必须传输此请求参数，默认值为true
   + defaultValue：当value指定的请求参数没有传输时，使用默认值为形参赋值

4. @RequestHeader

   @RequestParam是将请求头信息和控制器方法的形参创建映射关系

   @RequestHeader一共有三个属性：value、required、defaultValue，用法同@RequestParam

5. @CookieValue

   @CookieValue是将cookie数据和控制器方法的形参创建映射关系

   @CookieValue一共有三个属性：value、required、defaultValue，用法同@RequestParam

6. 通过POJO获取请求参数

   可以在控制器方法的形参位置设置一个实体类类型的形参，此时若浏览器传输的请求参数的参数名和实体类中的属性名一致，那么请求参数就会为此属性赋值

7. 解决获取请求参数的乱码问题

   web.xml中配置过滤器

   ```xml
   <filter>
   	<filter-name>CharacterEncodingFilter</filter-name>
       <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
       <init-param>
       	<param-name>encoding</param-name>
           <param-value>UTF-8</param-value>
       </init-param>
       <init-param>
       	<param-name>forceResponseEncoding</param-name>
           <param-value>true</param-value>
       </init-param>
   </filter>
   ```

### 域对象共享数据

1. 使用servletAPI向request域对象共享数据

   ```java
   @RequestMapping("/testServletAPI")
   public String testServletAPI(HttpServletRequest request){
       request.setAttribute("testScope", "hello")
       return "success";
   }
   ```

2. 使用ModelAndView向request域对象共享数据

   ```java
   @RequestMapping("/testModelAndView")
   public ModelAndView testModelAndView(){
       ModelAndView mav = new ModelAndView();
       mav.addObject("testScope", "hello");
       mav.setViewName("success");
       return mav;
   }
   ```

3. 使用Model向request域对象共享数据

   ```java
   @RequestMapping("/testModel")
   public String testModel(Model model){
       model.addAttribute("testScope", "hello");
       return "success";
   }
   ```

4. 使用map向request域对象共享数据

   ```java
   @RequestMapping("/testMap")
   public String testMap(Map<String, Object> map){
       map.put("testScope", "hello");
       return "success";
   }
   ```

5. 使用ModelMap

   ```java
   @RequestMapping("/testModelMap")
   public String testModelMap(ModelMap modelMap){
       modelMap.addAttribute("testScope", "hello");
       return "success";
   }
   ```

6. Model、ModelMap、Map的关系

   本质上都是BindingAwareModelMap类型的

7. 向session域共享数据

   ```java
   @RequestMapping("/testSession")
   public String testSession(HttpSession session){
       session.setAttribute("testScope", "hello");
       return "success";
   }
   ```

8. 向application域共享数据

   ```java
   @RequestMapping("/testApplication")
   public String testApplication(HttpSession session){
       ServletContext application = session.getServletContext();
       application.setAttribute("testScope", "hello");
       return "success";
   }
   ```

### RESTFul

REST：Representational State Transfer，表现层资源状态转移

GET：获取资源

POST：新建资源

PUT：更新资源

DELETE：删除资源

HiddenHttpMethodFilter

### HttpMessageConvert

报文信息转换器，将请求报文转换为Java对象，或将Java对象转换为响应报文

HttpMessageConvert提供了两个注解和两个类型：@RequestBody，@ResponseBody，RequestEntity，ResponseEntiry

1. @RequestBody

   可以获取请求体，需要在控制器方法设置一个形参，使用@RequestBody进行标识，当前请求的请求体就会为当前注解所标识的形参赋值

   ```java
   @RequestMapping("/testRequestBody")
   public String testRequestBody(@RequestBody String requestBody){
       return "success";
   }
   ```

2. RequestEntity

   RequestEntity封装请求报文的一种类型，需要在控制器方法的形参中设置该类型的形参，当前请求的请求报文就会赋给改形参，可以通过getHeaders()获取请求头信息，通过getBody()获取请求体信息

   ```java
   @RequestMapping("/testRequestEntity")
   public String testRequestEntity(RequestEntity<String> requestEntity){
       System.out.println(requestEntity.getHeaders());
       System.out.println(requestEntity.getBody());
       return "success";
   }
   ```

3. @ResponseBody

   ```java
   //原生servletAPI
   @RequestMapping("/testResponse")
   public void testResponse(HttpServletResponse response){
       response.getWriter().print("hello,response");
       return "success";
   }
   
   @RequestMapping("/testResponseBody")
   @ResponseBody
   public String testResponseBody(){
       return "success";
   }
   ```

4. ResponseEntiry

   用于控制器方法的返回值类型，该控制器方法的返回值就是响应到浏览器的响应报文

5. @RestController

   复合注解，标识在控制器的类上，就相当于为类添加了@Controller注解，并且为其中的每个方法添加了@ResponseBody注解

### 拦截器

springMVC中的拦截器用于拦截控制器方法的执行

springMVC中的拦截器需要实现HandlerInterceptor或者继承HandlerInterceptorAdapter类（过时）

springMVC的拦截器必须在springMVC的配置文件中进行配置

```xml
<mvc:interceptors>
    <!-- 拦截所有请求 -->
    <bean class="com.atguigu.interceptor.FirstInterceptor"></bean>
    <ref bean="firstInterceptor"></ref>
    <!-- 拦截配置的请求 -->
    <mvc:interceptor>
        <mvc:mapping path="/**"/>
        <mvc:exclude-mapping path="/testRequestEntity"/>
        <ref:bean="firstInterceptor"/>
    </mvc:interceptor>
</mvc:interceptors>

```

```java
public class FirstInterceptor implements HandlerInterceptor {
    
    //控制器方法之前执行，起boolean类型的返回值表示是否拦截或放行，返回true为放行
    @Overried
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return false;
    }
    
    //控制器方法执行之后执行postHandle
    @Overried
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        
    }
    
    //处理完视图和模型数据，渲染视图完毕后执行
    @Overried
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler) {
        
    }
}
```

**多个拦截器执行顺序**

+ 若每个拦截器的preHandle()都返回true，此时多个拦截器的执行顺序和拦截器在springMVC配置文件的配置顺序有关，preHandle()会按照配置的顺序执行，而postHandle()和afterComplation()会按照配置的反序执行
+ 若某个拦截器的preHandle()返回了false，preHandle()返回false和他之前的拦截器preHandle()都会执行，postHandle()都不执行，返回false的拦截器之前的拦截器的afterComplation()会执行

### 异常处理器

**基于配置的异常处理**

springMVC提供了一个处理控制器方法执行过程中所有出现的异常的接口：HandlerExceptionResolver

HandlerExceptionResolver接口的实现类有DefaultHandlerExceptionResolver和SimpleMappingExceptionResolver

springMVC提供了自定义的异常处理器SimpleMappingExceptionResolver，使用方式

```xml
<bean class="org.springframework.web.servlet.handler.SimpleMappingExceptionResolver">
	<property name="exceptionMappings">
        <props>
        	<prop key="java.lang.ArithmeticException">error</prop>
        </props>
    </property>
    <!-- 设置将异常信息共享在请求域中的键 -->
    <property name="exceptionAttribute" value="ex"></property>
</bean>
```

**基于注解的异常处理**

```java
//@ControllerAdvice将当前类标识为异常处理的组件
@ControllerAdvice
public class ExceptionController {
    
    //@ExceptionHandler用于设置所标识方法处理的异常
    @ExceptionHandler(AirthmeticException.class)
    public String handleAirthmeticException(Exception ex, Model model) {
        model.addAttribute("ex", ex);
        return "error";
    }
}
```

### 注解配置springMVC

在servlet3.0环境中，容器会在类路径中查找实现javax.servlet.ServletContainerInitializer接口的类，如果找到的话就用它来配置Servlet容器

spring提供了整个接口的实现，名为SpringServletContainerInitializer，这个类反过来又会查找实现WebApplicationInitializer的类并将配置的任务交给他们来完成。Spring3.2引入了一个便利的WebApplicationInitializer基础实现，名为AbstractAnnotationConfigDispatcherServletInitializer，当我们的类扩展了AbstractAnnotationConfigDispatcherServletInitializer，并将其部署到Servlet3.0容器的时候，容器会自动发现它，并用它来配置Servlet上下文

```java
//代替web.xml
public class WebInit extends AbstractAnnotationConfigDispatcherServletInitializer {
    //指定spring的配置类
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{SpringConfig.class};
    }
    
    //指定springMVC的配置类
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }
    
    //指定DispatcherServlet的映射规则，即url-pattern
    @Override
    protected String[] getServletMapprings() {
        return new String[]{"/"};
    }
    
    //注册过滤器
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceResponseEncoding(true);
        HiddenHttpMethodFilter hiddenHttpMethodFilter = new HiddenHttpMethodFilter();
        return new Filter[]{characterEncodingFilter, hiddenHttpMethodFilter};
    }
}
```



```java
//代替springMVC的配置文件：1.扫描组件 2.视图解析器 3.view-controller 4.default-servlet-handler 5.mvc注解驱动 6.文件上传解析器 7.异常处理 8.拦截器
@Configuration
//扫描组件
@ComponentScan
//mvc注解驱动
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer{
    
}
```

### springMVC执行流程

**springMVC常用组件**

+ DispatcherServlet：前端控制器，统一处理请求和响应，整个流程控制的中心，由他调用其他组件处理用户的请求
+ HandlerMapping：处理器映射器，根据请求的url、method等信息查找handler，即控制器方法
+ Handler：处理器，在DispatcherServlet的控制下Handler对具体的用户请求进行处理
+ HandlerAdaptor：处理器适配器，通过HandlerAdaptor对处理器（控制器方法）进行执行
+ ViewResolver：视图解析器，进行视图解析，得到相应的视图
+ View：视图，将模型数据通过页面展示给用户

**DispatcherServlet初始化流程**

![springmvc流程](img\springmvc流程.png)



```xml
在springMVC的各个组件中，处理器映射器，处理器适配器，视图解析器称为springMVC三大组件。使用<mvc:annotation-driven/>自动加载RequestMappingHandlerMapping(处理器映射器)和RequestMappingHandlerAdapter(处理器适配器),同时默认底层就会集成Jackson进行对象或集合的Json格式字符串的转换
```

## SpringBoot

### SpringBoot基础

#### SpringBoot特点

+ 依赖管理
  + 父项目做依赖管理
  + 开发导入starter场景启动器
  + 无需关注版本号，自动版本仲裁
  + 可以修改版本号
+ 自动配置
  + 自动配好Tomcat
  + 自动配置好SpringMVC
  + 自动配好web常见功能，如：字符编码问题
  + 默认的包结构
    + 主程序所在包及其下面的所有子包里面的组件都会被默认扫描进来
    + 想要改变扫描路径，@SpringBootApplication(scanBasePackages="com.atguigu")
    + 或者使用@ComponentScan指定扫描路径
  + 各种配置拥有默认值
  + 按需加载所有的配置项
    + SpringBoot所有的自动配置功能都在spring-boot-autoconfigure包里面

#### 容器功能

**@Configuration**

```java
/*告诉SpringBoot这是一个配置类
1.配置类里面使用@Bean标注在方法上给容器注册组件，默认也是单实例的
2.配置类本身也是组件
3.proxyBeanMethods:代理bean的方法
	Full(proxyBeanMethods=true)
	Lite(proxyBeanMethods=false)
	组件依赖：
		配置类组件之间无依赖关系用Lite模式加速容器启动过程，减少判断
		配置类组件之间有依赖关系，方法会被调用得到之前单实例组件，用Full模式
  如果@Configuration(proxyBeanMethods=true)代理对象调用方法。SpringBoot总会检查这个组件是否在容器中有，如果有就不会新建，保证组件单实例
*/
@Configuration(proxyBeanMethods=true) //默认为true
public class MyConfig {
    
    //给容器中添加组件，以方法名作为组件的id，返回类型就是组件类型。返回的值，就是组件在容器中的实例
    @Bean
    public User user01() {
        return new User("zhangsan",18);
    }
    
    //组件id修改为tom
    @Bean("tom")
    public Pet tomcatPet() {
        return new Pet("tomcat");
    }
}
```

**@Bean、@Component、@Controller、@Service、@Repository**

**@ComponentScan、@Import**

@Import({User.class, DBHelper.class})，给容器中自动创建出这两个类型的组件，默认组件的名字就是全类名

**@Conditional**

条件装配：满足Conditional指定的条件，则进行组件注入

**@ImportResource**

@ImportResource("classpath:beans.xml")导入spring的配置文件

**@Component + @ComfigurationProperties**

开启属性配置功能

**@EnableConfigurationProperties**

@EnableConfigurationProperties(Car.class)

写在配置类上，开启属性配置功能，把Car这个组件自动注册到容器中

#### 自动配置原理

@SpringBootApplication相当于@SpringBootConfiguration、@EnableAutoConfiguration、@ComponentScan

+ @SpringBootConfiguration：代表是一个配置类

+ @ComponentScan：指定要扫描哪些

+ @EnableAutoConfiguration：@AutoConfigurationPackage、@Import(AutoConfigurationImportSelector.class)的合成注解

  + @AutoConfigurationPackage

    @Import(AutoConfigurationPackage.Register.class)，给容器中导入组件，利用Register给容器中导入一系列组件，将main程序所在包下的所有组件导入进来

  + @Import(AutoConfigurationImportSelector.class)

    1. 利用getAutoConfigurationEntry(annotationMetadata)方法给容器中批量导入一些组件

    2. 调用List<String> configurations = getCandidateConfigurations(annotationMetadata,attributes);获取所有需要导入到容器中的配置类
    3. 利用工厂加载Map<String, List< String>> loadSpringFactories(@Nullable ClassLoader classLoader);得到所有的组件
    4. 从META-INF/spring.factories位置加载一个文件，默认扫描我们当前系统里面所有META-INF/spring.factories位置的文件。spring-boot-autoconfigure-2.3.4.RELEASE.jar包里面也有META-INF/spring.factories，文件里面写死了spring-boot一启动就要给容器中加载的所有配置类，按照条件装配规则（@Conditional），最终会按需配置

总结：

+ SpringBoot先加载所有的自动配置类：xxxAutoConfiguration
+ 每个自动配置类按照条件进行生效，默认都会绑定配置文件指定的值。xxxProperties里面拿。xxxProperties和配置文件进行了绑定
+ 生效的配置类就会给容器中装配很多组件
+ 只要容器中有这些组件，相当于这些功能就有了
+ 定制化配置
  + 用户直接自己@Bean替换底层的组件
  + 用户去看这个组件是获取的配置文件什么值就去修改

xxxAutoConfiguration --> 组件 --> xxxProperties里面拿值 --> application.properties

**最佳实践**

+ 引入场景依赖

  https://docs.spring.io/spring-boot/docs/2.7.12/reference/html/using.html#using.build-systems.starters

+ 查看自动配置了哪些（选做）

  + 自己分析，引入场景对应的自动配置一般都生效了
  + 配置文件中debug=true开启自动配置报告。Negative（不生效）/Positive（生效）

+ 是否需要修改

  + 参照文档修改配置项
    + https://docs.spring.io/spring-boot/docs/2.7.12/reference/html/application-properties.html#appendix.application-properties
    + 自己分析。xxxProperties绑定了配置文件的哪些
  + 自定义加入或者替换组件
    + @Bean、@Component...
  + 自定义器 xxxCustomizer
  + ...

### SpringBoot核心技术

#### 配置文件

**yaml**

+ key : value，kv之间有空格
+ 大小写敏感
+ 使用缩减表示层级关系
+ 缩进不允许使用tab，只允许空格
+ 缩进的空格数不重要，只要相同层级的元素左对齐即可
+ #表示注释
+ 字符串无需加引号，''与""表示字符串内容会被转义/不转义，'\n'输出\n， "\n"输出换行符

```yaml
#字面量：单个的、不可再分的值。date、boolean、string、number、null
k: v

#对象：键值对的集合。map、hash、set、object
#行内写法
k: {k1:v1,k2:v2}
#或
k: 
  k1: v1
  k2: v2
  
#数组：一组按次序排列的值。array、list、queue
#行内写法
k: [v1,v2]
#或
k:
  - v1
  - v2

```

```xml
<!-- 自定义类绑定的配置提示 -->
<dependency>
	<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
</dependency>

<!-- 打包时排除 -->
<build>
	<plugins>
    	<plugin>
        	<groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
            	<excludes>
                	<exclude>
                    	<groupId>org.springframework.boot</groupId>
    					<artifactId>spring-boot-configuration-processor</artifactId>
                    </exclude>
                </excludes>
            </configuration>
        </plugin>
    </plugins>
</build>
```

#### Web开发

##### 简单功能分析

**静态资源访问**

只要静态资源放在类路径下：/static、/public、/resources、/META-INF/resources

访问：当前项目根路径/ + 静态资源名

原理：静态映射/**

请求进来，先去找Controller看能不能处理。不能处理的所有请求又都交给静态资源处理器。

```yaml
#静态资源访问前缀，默认无前缀
spring:
  mvc:
    static-path-pattern: /resources/**
#静态资源访问路径
  resource:
    staic-locations: classpath:/haha
    add-mappings: false #禁用所有静态资源

```

**欢迎页支持**

+ 静态资源路径下index.html
  + 可以配置静态资源路径
  + 但是不可以配置静态资源的访问前缀。否则导致index.html不能被默认访问
+ controller能处理/index

**自定义Favicon**

将favicon.ico图片放在静态资源路径下

不可以配置静态资源的访问前缀。否则导致favicon.ico不能被默认访问

**静态资源配置原理**

+ SpringBoot启动默认加载xxxAutoConfiguration类

+ SpringMVC功能的自动配置类WebMvcAutoConfiguration，生效

  ```java
  @AutoConfiguration(
      after = {DispatcherServletAutoConfiguration.class, TaskExecutionAutoConfiguration.class, ValidationAutoConfiguration.class}
  )
  @ConditionalOnWebApplication(
      type = Type.SERVLET
  )
  @ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class})
  @ConditionalOnMissingBean({WebMvcConfigurationSupport.class})
  @AutoConfigureOrder(-2147483638)
  public class WebMvcAutoConfiguration
  ```

+ 给容器中配了什么

  ```java
  @Configuration(
      proxyBeanMethods = false
  )
  @Import({WebMvcAutoConfiguration.EnableWebMvcConfiguration.class})
  @EnableConfigurationProperties({WebMvcProperties.class, WebProperties.class})
  @Order(0)
  public static class WebMvcAutoConfigurationAdapter implements WebMvcConfigurer, ServletContextAware {
      //配置类只有一个有参构造器，有参构造器所有参数的值都会从容器中确定
      //WebProperties webProperties 获取和spring.web绑定的所有的值的对象
      //WebMvcProperties mvcProperties 获取和spring.mvc绑定的所有值的对象
      //ListableBeanFactory beanFactory spring的beanFactory
      //HttpMessageConverters 找到所有的HttpMessageConverters
      //ResourceHandlerRegistrationCustomizer 找到资源处理器的自定义器
      //DispatcherServletPath
      //ServletRegistrationBean 给应用注册Servlet、Filter...
      public WebMvcAutoConfigurationAdapter(WebProperties webProperties, WebMvcProperties mvcProperties, ListableBeanFactory beanFactory, ObjectProvider<HttpMessageConverters> messageConvertersProvider, ObjectProvider<WebMvcAutoConfiguration.ResourceHandlerRegistrationCustomizer> resourceHandlerRegistrationCustomizerProvider, ObjectProvider<DispatcherServletPath> dispatcherServletPath, ObjectProvider<ServletRegistrationBean<?>> servletRegistrations) {
          this.resourceProperties = webProperties.getResources();
          this.mvcProperties = mvcProperties;
          this.beanFactory = beanFactory;
          this.messageConvertersProvider = messageConvertersProvider;
          this.resourceHandlerRegistrationCustomizer = (WebMvcAutoConfiguration.ResourceHandlerRegistrationCustomizer)resourceHandlerRegistrationCustomizerProvider.getIfAvailable();
          this.dispatcherServletPath = dispatcherServletPath;
          this.servletRegistrations = servletRegistrations;
          this.mvcProperties.checkConfiguration();
      }
  }
  
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
      if (!this.resourceProperties.isAddMappings()) {
          logger.debug("Default resource handling disabled");
      } else {
          this.addResourceHandler(registry, "/webjars/**", "classpath:/META-INF/resources/webjars/");
          this.addResourceHandler(registry, this.mvcProperties.getStaticPathPattern(), (registration) -> {
              registration.addResourceLocations(this.resourceProperties.getStaticLocations());
              if (this.servletContext != null) {
                  ServletContextResource resource = new ServletContextResource(this.servletContext, "/");
                  registration.addResourceLocations(new Resource[]{resource});
              }
  
          });
      }
  }
  
  //欢迎页的规则
  //handlerMapping:处理器映射，保存了每一个handler能处理哪些请求
  @Bean
  public WelcomePageHandlerMapping welcomePageHandlerMapping(ApplicationContext applicationContext, FormattingConversionService mvcConversionService, ResourceUrlProvider mvcResourceUrlProvider) {
      return (WelcomePageHandlerMapping)this.createWelcomePageHandlerMapping(applicationContext, mvcConversionService, mvcResourceUrlProvider, WelcomePageHandlerMapping::new);
  }
  
  private <T extends AbstractUrlHandlerMapping> T createWelcomePageHandlerMapping(ApplicationContext applicationContext, FormattingConversionService mvcConversionService, ResourceUrlProvider mvcResourceUrlProvider, WebMvcAutoConfiguration.WelcomePageHandlerMappingFactory<T> factory) {
      TemplateAvailabilityProviders templateAvailabilityProviders = new TemplateAvailabilityProviders(applicationContext);
      String staticPathPattern = this.mvcProperties.getStaticPathPattern();
      T handlerMapping = factory.create(templateAvailabilityProviders, applicationContext, this.getIndexHtmlResource(), staticPathPattern);
      handlerMapping.setInterceptors(this.getInterceptors(mvcConversionService, mvcResourceUrlProvider));
      handlerMapping.setCorsConfigurations(this.getCorsConfigurations());
      return handlerMapping;
  }
  ```

+ 配置文件的相关属性和xxx进行了绑定，WebMvcProperties==spring.mvc，WebProperties==spring.web

  ```java
  //WebProperties
  public static class Resources {
      private static final String[] CLASSPATH_RESOURCE_LOCATIONS = new String[]{"classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/"};
          
  ```

##### 请求参数处理

**Rest原理（表单提交要使用REST的时候）**

+ 表单提交会带上_method=PUT
+ 请求过来被HiddenHttpMethodFilter拦截
  + 请求是否正常，并且是POST
    + 获取到_method的值
    + 兼容以下请求：PUT、DELETE、PATCH
    + 原生request(post)，包装模式requestWrapper重写了getMethod方法，返回的是传入的_method的值
    + 过滤器链放行的时候用wrapper。以后的方法getMethod是调用requestWrapper的

**Rest使用客户端工具**

+ 如postman直接发送PUT、DELETE等方式请求，无需Filter

```yaml
spring:
  mvc:
    hiddenmethod:
      filter:
       enabled: true #开启页面表单的REST功能
```

**请求映射原理**

SpringMVC功能分析都从DispatcherServlet --> doDispatch() -->

```java
protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpServletRequest processedRequest = request;
        HandlerExecutionChain mappedHandler = null;
        boolean multipartRequestParsed = false;
        WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);

        try {
            try {
                ModelAndView mv = null;
                Object dispatchException = null;

                try {
                    processedRequest = this.checkMultipart(request);
                    multipartRequestParsed = processedRequest != request;
                    //找到当前请求使用哪个Handler（Controller的方法）处理
                    mappedHandler = this.getHandler(processedRequest);
                    
    @Nullable
    protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        if (this.handlerMappings != null) {
            Iterator var2 = this.handlerMappings.iterator();

            while(var2.hasNext()) {
                //HandlerMapping，处理器映射
                //RequestHanderMapping:保存了所有@RequestMapping和handler的映射规则
                HandlerMapping mapping = (HandlerMapping)var2.next();
                HandlerExecutionChain handler = mapping.getHandler(request);
                if (handler != null) {
                    return handler;
                }
            }
        }

        return null;
    }
```

所有的请求映射都在HandlerMapping中

+ SpringBoot自动配置欢迎页的HandlerMapping。访问/能访问到index.html
+ SpringBoot自动配置了默认的RequestMappingHandlerMapping
+ 请求进来，挨个尝试所有的HandlerMapping看是否有请求信息
  + 如果有就找到这个请求对应的handler
  + 如果没有就是下一个HandlerMapping
+ 我们需要一些自定义的映射处理，我们也可以自己给容器中放HandlerMapping

**普通参数与基本注解**

+ @PathVariable（路径变量）

+ @RequestHeader（获取请求头）

+ @RequestParam（获取请求参数）

+ @CookieValue（获取cookie值）

+ @RequestBody（获取post请求体）

+ @RequestAttribute（获取request域属性）

+ @MatrixVariable（矩阵变量）：;分隔

  页面开发，cookie禁用，session里面的内容怎么使用？

  url重写：/abc;jsessionid=xxx 把cookie的值使用矩阵变量的方式进行传递

  SpringBoot默认禁用了矩阵变量的功能，对于路径的处理UrlPathHelper进行解析。removeSemicolonContent（移除分号内容）支持矩阵变量的属性。矩阵变量必须有url路径变量才能被解析

Servlet API

WebRequest、ServletRequest、MultipartRequest、HttpSession、javax.servlet.http.PushBuilder、Principal、InputStream、Reader、HttpMethod、Locale、TimeZone、ZoneId

复杂参数

Map、Model（map、model里面的数据会被放在request的请求域 request.setAttribute）、Errors/BindingResult、RedirectAttributes（重定向携带数据）、ServletResponse（response）、SessionStatus、UriComponentsBuilder、ServletUriComponentsBuilder

Map、Model、HttpServletRequest都是可以给request域中放数据

Map、Model类型的参数，会返回mavContainer.getModel() 获取到值的

自定义对象参数

可以自动类型转换与格式化，可以级联封装



**POJO封装过程**



**参数处理原理**

+ HandlerMapping中找到能处理请求的Handler（Controller.method()）
+ 为当前handler找一个适配器HandlerAdapter；RequestMappingHandlerAdapter
+ 

执行目标方法

DispatcherServlet --> doDispatcher --> mv = ha.handle

真正执行目标方法：ServletInvocableHandlerMethod里invokeForRequest()

参数解析器

argumentResolves，确定将要执行的目标方法的每一个参数的值是什么

SpringMVC目标方法能写多少种参数类型取决于参数解析器

HandlerMethodArgumentResolver接口，supportsParameter和resolveArgument方法

+ 当前解析器是否支持这种参数
+ 支持就调用resolveArgument方法

返回值处理器

returnValueHandlers

自定义类型参数封装POJO

ServletModelAttributeMethodProcessor这个参数处理器支持

WebDataBinder binder = binderFactory.createBinder();

WebDataBinder：web数据绑定器，将请求参数的值绑定到指定的JavaBean里面

WebDataBinder利用它里面的Converters将请求数据转成指定的数据类型。再次封装到JavaBean中

目标方法执行完成

将所有的数据都放在ModelAndViewContainer，包含要去的页面地址view，还包含model数据

处理派发结果

processDispatcherResult方法

renderMergedOutputModel

exposeModelAsRequestAttribute：model中所有的数据遍历放在请求域中

##### 数据响应与内容协商

**响应json**

jackson.jar + @ResponseBody

给前端自动返回json数据

返回值处理器returnValueHandlers

1. 返回值处理器判断是否支持这种类型的返回值supportReturnType

2. 返回值处理器调用handleReturnValue进行处理

3. RequestResponseBodyMethodProcessor可以处理返回值标注了@ResponseBody注解的

   1. 利用MessageConverters进行处理，将数据写为json

      内容协商（浏览器默认会以请求头accept的方式告诉服务器他能接收什么样的内容类型）

      服务器最终根据自身的能力，决定服务器能生产出什么样内容类型的数据

      SpringMVC会挨个遍历所有容器底层的HttpMessageConverter，看谁能处理


springMVC支持的返回值：ModelAndView、Model、View、ResponseEntity、ResponseBodyEmitter、StreamingResponseBody、HttpEntity、HttpHeaders、Callable、DeferredResult、ListenableFuture、CompletionStage、WebAsyncTask、@ModelAttribute、@ResponseBody

**HttpMessageConverter原理**

看是否支持将此Class类型的对象转为MediaType类型的数据

最终MappingJackson2HttpMessageConverter把对象转为json（利用底层的jackson的objectMapper）

**开启浏览器参数方式内容协商功能**

为了方便内容协商，开启基于请求参数的内容协商功能

```yaml
spring:
  mvc:
    contentnegotiation:
      favor-parameter: true
```

http://localhost:8080/test/person?format=json

http://localhost:8080/test/person?format=xml

确定客户端接收什么样的内容类型

1. parameter策略优先确定是要返回json数据（获取请求头中的format的值）

**内容协商**

根据客户端接受能力的不同，返回不同媒体类型的数据

内容协商原理

1. 判断当前响应头中是否已经有确定的媒体类型。MediaType
2. 获取客户端支持接收的内容类型（获取Accept请求头字段）
3. 遍历循环所有当前系统的MessageConverter，看谁支持操作这个对象
4. 找到支持操作的converter，把converter支持的媒体类型统计出来
5. 进行内容协商的最佳匹配
6. 用支持将对象转为最佳匹配媒体类型的converter，调用它进行转化

**自定义MessageConverter**

实现HttpMessageConverter接口

自定义springMVC的什么功能，一个入口容器中添加一个WebMvcConfigure

有可能我们添加的自定义的功能会覆盖默认很多功能，导致一些默认的功能失效

##### 视图解析与模板引擎

ThymeleafAutoConfiguration

自动配置好的策略

+ 所有的thymeleaf的配置都在ThymeleafProperties
+ 配置好了SpringTemplateEngine
+ 配好了ThymeleafViewResolver
+ 我们只需要直接开发页面

**视图解析原理流程**

1. 目标方法处理的过程中，所有数据都会被放在ModelAndViewContainer里面，包括数据和视图地址

2. 方法的参数是一个自定义类型对象（从请求参数中确定的），把他重新放在ModelAndViewContainer

3. 任何目标方法执行完成以后都会返回ModelAndView（数据和视图地址）

4. processDispatcherResult处理派发结果（页面如何响应）

   1. render(mv, request, response)

      1. 根据方法的String返回值得到View对象（定义了页面的渲染逻辑）

         1. 所有的视图解析器尝试是否能根据当前返回值得到View对象

         2. 得到了redirect:/main.html --> Thymeleaf new RedirectView()

         3. ContentNegotiationViewResolver里面包含了所有的视图解析器，内部还是利用所有视图解析器得到视图对象

         4. view.render()，视图对象调用自定义的render进行页面渲染

            RedirectView如何渲染：获取目标url地址，response.sendRedirect(encodedURL)

**视图解析**

+ 返回值以forward:开始：new InternalResourceView(forwardURL); --> 转发

  request.getResourceDispatcher(path).forward(request, response)

+ 返回值以redirect:开始：new RedirectView() --> render就是重定向

+ 返回值是普通字符串：new ThymeleafView() --> 

##### 拦截器

1. 编写一个拦截器实现HandlerInterceptor接口
2. 拦截器注册到容器中（实现WebMvcConfigurer的addInterceptors）
3. 指定拦截规则，如果是拦截所有，静态资源也会被拦截

**HandlerInterceptor接口**

```java
public class LoginInterceptor implements HandlerInterceptor {
    
    //控制器方法之前执行，起boolean类型的返回值表示是否拦截或放行，返回true为放行
    @Overried
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HttpSession session = request.getSession();
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser != null) {
            return true;
        }
        request.sendRedirect("/");
        return false;
    }
    
    //控制器方法执行之后执行postHandle
    @Overried
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        
    }
    
    //处理完视图和模型数据，渲染视图完毕后执行
    @Overried
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler) {
        
    }
}
```

**配置拦截器**

```java
@Configuration
public class AdminWebConfig implements WebMvcConfigurer {
    
    @Override
    public void addInterceptors(IntercepterRegistry registry) {
        registry.addInterceptor(new LoginInterceptor)
                .addPathPatterns("/**")
                .excludePathPattern("/","/login");
    }
}
```

**拦截器原理**

1. 根据当前请求，找到HandlerExecutionChain（可以处理的handler以及handler的所有拦截器）
2. 先来顺序执行所有拦截器的preHandler方法
   1. 如果preHandler返回为true，则执行下一个拦截器的preHandler
   2. 如果当前拦截器返回为false，直接倒序执行所有已经执行了的拦截器的afterCompletion
3. 如果任何一个拦截器返回false，直接跳出不执行目标方法
4. 所有拦截器都返回true。执行目标方法
5. 倒序执行所有拦截器的postHandler方法
6. 前面的步骤有任何异常都会直接倒序触发afterCompletion
7. 页面成功渲染完成以后，也会倒序触发afterCompletion

##### 文件上传

```java
@PostMapping("/upload")
public String upload(@RequestPart("headerImg") MultipartFile headerImg
                     @RequestPart("photos") MultipartFile[] photos) {
    if (!headerImg.isEmpty()) {
        String originalFilename = headerImg.getOriginalFilename();
        headerImg.transferTo(new File("H:\\" + originalFilename));
    }
    
    if (photos.length > 0) {
        for (MultipartFile photo : photos) {
            if (!photo.isEmpty()) {
                String originalFilename = photo.getOriginalFilename();
                photo.transferTo(new File("H:\\" + originalFilename))
            }
        }
    }
}
```

```yaml
spring:
  servlet:
    multipart:
      max-file-size: 10MB #单个文件上传大小，默认为1M
      max-request-size: 100MB #上传文件总大小，默认10MB
```

**自动配置原理**

文件上传自动配置类-MultipartAutoConfiguration-MultipartProperties

+ 自动配置好了StandardServletMultipartResolver文件上传解析器

+ 原理步骤

  1. 请求进来使用文件上传解析器判断（isMultipart）并封装（resolverMultipart，返回MultipartHttpServletRequest）文件上传请求
  2. 参数解析器来解析请求中的文件内容，封装成MultipartFile
  3. 将request中文件信息封装成一个Map；MultiValueMap<String, MultipartFile>

  FileCopyUtils。实现文件流的拷贝

##### 异常处理

**默认规则**

+ 默认情况下，springBoot提供/error处理所有错误的映射
+ 对于机器客户端，他将生成JSON响应，其中包含错误，HTTP状态和异常消息的详细信息。对于浏览器客户端，响应一个whitelabel错误视图，以HTML格式呈现相同的数据
+ 要对其进行自定义，添加view解析为error
+ 要完全替换默认行为，可以实现ErrorController并注册该类型的bean定义，或添加ErrorAttributes类型的组件以使用现有机制但替换其内容
+ error/下的4xx，5xx页面会被自动解析

**定制错误处理逻辑**

+ 自定义错误页

  error/404.html、error/5xx.html；有精确的错误状态码页面就匹配精确，没有就找4xx.html；如果都没有就触发空白页

+ @ControllerAdvice + @ExceptionHandler处理异常；底层是ExceptionHandlerExceptionResolver支持的

  ```java
  //处理整个web controller的异常
  @Slf4j
  @ControllerAdvice
  public class GlobalExceptionHandler {
      
      @ExceptionHandler({ArithmeticException.class,NullPointerException.class})
      public String handlerArithException(Exception e) {
          log.error("异常是：{}", e);
          return "login"; //视图地址
      }
  }
  ```

+ @ResponseStatus + 自定义异常；底层是ResponseStatusExceptionResolver，把responsestatus注解信息组装成ModelAndView返回；底层调用response.sendError(statusCode, resolvedReason)；tomcat发送的/error

  ```java
  @ResponseStatus(value=HttpStatus.FORBIDDEN,reason="用户数量太多")
  public class UserTooManyException extends RuntimeException {
      
      public UserTooManyException(String message) {
          super(message);
      }
  }
  ```

+ Spring底层的异常：如参数类型转换异常；DefaultHandlerExceptionResolver处理框架底层的异常

+ 自定义实现HandlerExceptionResolver处理异常；可以作为默认的全局异常处理规则

  ```java
  @Order(value=Ordered.HIGHEST_PRECEDENCE) //优先级，数字越小优先级越高
  @Component
  public class CustomerHandlerExceptionResolver implements HandlerExceptionResolver {
      
      @Override
      public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
          response.sendError("511", "我喜欢的错误");
          return new ModelAndView();
      }
  }
  ```

+ ErrorViewResolver实现自定义处理异常；一般不自定义

  response.sendError。error请求就会转给controller

  你的异常没有任何人处理。tomcat底层response.sendError

  basicErrorController要去的页面地址是ErrorViewResolver

**异常处理自动配置原理**

+ ErrorMvcAutoConfiguration自动配置异常处理规则

  + 容器中的组件：类型DefaultErrorAttributes -> errorAttributes

    public class DefaultErrorAttributes implements ErrorAttributes,HandlerExceptionResolver

    DefaultErrorAttributes：定义错误页面中可以包含哪些数据

  + 容器中的组件：类型BasicErrorController -> basicErrorController

    处理默认/error路径的请求；页面响应new ModelAndView("error", model);

    容器中有组件View -> id是error

    容器中放组件BeanNameViewResolver（视图解析器）；按照返回的视图名作为组件的id去容器中找view对象

  + 容器中的组件：DefaultErrorViewResolver -> id: conventionErrorViewResolver

    如果发生错误，会以HTTP的状态码作为视图页地址（viewname），找到真正的页面

    error/viewname.html，4xx、5xx.html

**异常处理步骤流程**

1. 执行目标方法，目标方法运行期间有任何异常都会被catch，而且标志当前请求结束；并且用dispatchException封装

2. 进入视图解析流程（页面跳转）

   processDispatchResult(processRequest, response, mapperHandler, mv, dispatchException)

3. mv = processHandlerException：处理handler发生的异常，处理完成返回ModelAndView

   遍历所有的handlerExceptionResolvers，看谁能处理当前异常，HandlerExceptionResolver处理器异常解析器

   系统默认的异常解析器：DefaultErrorAttributes、HandlerExceptionResolverComposite、ExceptionHandlerExceptionResolver、ResponseStatusExceptionResolver、DefaultHandlerExceptionResolver

   DefaultErrorAttributes先来处理异常，把异常信息保存到request域，并且返回null

   默认没有任何人能处理异常，所以异常会被抛出

   如果没有任何人能处理最终底层就会发送/error请求，会被底层的BasicErrorController处理

   解析错误视图，遍历所有的ErrorViewResolver看谁能解析

   默认的DefaultErrorViewResolver，作用是把响应状态码作为错误页的地址拼接成，error/500.html

4. 模板引擎最终响应这个页面

##### Web原生组件注入（Servlet、Filter、Listener）

dispatcherServlet如何注册进来

+ 容器中自动配置了DispatcherServlet属性绑定到WebMvcProperties；对应的配置文件是spring.mvc
+ 通过ServletRegistrationBean< DispatcherServlet>把DispatcherServlet配置进来
+ 默认映射的是 / 路径

tomcat-servlet

多个Servlet都能处理到同一层路径，精确优先原则

**使用servlet API**

```java
@WebServlet(urlPatterns="/my")
public class MyServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequeset request, HttpServletResponse response) throw ServletException {
        response.getWritter().write("6666");
    }
}
```

```java
@ServletComponentScan(basePackages="com.atguigu") //指定原生Servlet组件放哪儿
@SpringBootApplication
public class BootApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }
}
```

```java
@WebFilter(urlPatterns={"/css/*","/images/*"})
public class MyFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException {
        
    }
    
    @Override
    public void destroy() {
        
    }
}
```

```java
@WebListener
public class MyServletContextListener implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        
    }
}
```

**使用RegistrationBean**

ServletRegistrationBean、FilterRegistrationBean、ServletListenerRegistrationBean

不用写@WebServlet、@WebFilter、@WebListener注解

```java
//proxyBeanMethods = true：保证依赖的组件始终是单实例的
@Configuration(proxyBeanMethods = true)
public class MyRegistryConfig {
    
    @Bean
    public ServletRegistrationBean myServlet() {
        MyServlet myServlet = new MyServlet();
        return new ServletRegistrationBean(myServlet,"/my","/my02");
    }
    
    @Bean
    public FilterRegistrationBean myFilter() {
        MyFilter myFilter = new MyFilter();
        //return new FilterRegistrationBean(myFilter,myServlet);//使用myServlet路径
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(myFilter);
        filterRegistrationBean.setUrlPatterns(Array.asList("/my","/css/*"))
        return filterRegistrationBean;
    }
    
    @Bean
    public ServletListenerRegistrationBean myListener() {
       MyServletContextListener myServletContextListener = new MyServletContextListener();
        return new ServletListenerRegistrationBean(myServletContextListener);
    }
}
```

##### 嵌入式Servlet容器

**切换嵌入式servlet容器**

+ 默认支持的webServerr

  + Tomcat、Jetty、Undertow
  + ServletWebServerApplicationContext容器启动寻找ServletWebServerFactory并引导创建服务器

+ 切换服务器

  ```xml
  <dependencies>
  	<dependency>
      	<groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-web</artifactId>
          <exclusions>
          	<exclusion>
              	<groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-starter-tomcat</artifactId>
              </exclusion>
          </exclusions>
      </dependency>
      <dependency>
      	<groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-jetty</artifactId>
      </dependency>
  </dependencies>
  ```

  

+ 原理

  + SpringBoot应用启动发现当前是web应用。web场景包-导入tomcat
  + web应用会创建一个web版的ioc容器ServletWebServerApplicationContext
  + ServletWebServerApplicationContext启动寻找ServletWebServerFactory（Servlet的Web服务器工厂 --> Servlet的Web服务器）
  + SpringBoot默认有很多的WebServer工厂：TomcatServletWebServerFactory、JettyServletWebServerFactory、UndertowServletWebServerFactory
  + 底层会有一个自动配置类：ServletWebServerFactoryAutoConfiguration
  + ServletWebServerFactoryAutoConfiguration导入了ServletWebServerFactoryConfiguration配置类
  + ServletWebServerFactoryConfiguration配置类根据动态判断系统中到底导入了哪个web服务器的包（默认是导入web-starter导入tomcat包），容器中就有TomcatServletWebServerFactory
  + TomcatServletWebServerFactory创建tomcat服务器并启动：TomcatWebServer的构造器拥有初始化方法initialize---this.tomcat.start()
  + 内嵌服务器，就是手动把启动服务器的代码调用（tomcat核心jar包存在）

**定制Servlet容器**

+ 实现WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>

  把配置文件的值和ServletWebServerFactory进行绑定

+ 修改配置文件server.xxx

+ 直接自定义ConfigurableServletWebServerFactory

##### 定制化原理

**定制化的常见方式**

+ @Bean替换、增加容器中默认组件；视图解析器
+ 修改配置文件
+ xxxCustomizer
+ 编写自定义配置类xxxConfiguration
+ web应用编写一个配置类实现WebMvcConfigurer即可定制化web功能 + @Bean给容器中再扩展一些组件
+ @EnableWebMvc + WebMvcConfigurer ---  @Bean可以全面接管SpringMVC，所有规则全部自己重新配置，实现定制和扩展功能
  + WebMvcAutoConfiguration默认的SpringMVC的自动配置功能类。静态资源、欢迎页...
  + 一旦使用@EnableWebMvc，会Import(DelegatingWebMvcConfiguration.class)
  + DelegatingWebMvcConfiguration的作用，只保证SpringMVC最基本的使用
    + 把系统中所有的WebMvcConfigurer拿过来。所有功能的定制都是这些WebMvcConfigurer合起来一起生效
    + 自动配置了一些非常底层的组件。RequestMappingHandlerMapping、这些组件依赖的组件都是从容器中获取
    + public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport
  + WebMvcAutoConfiguration里面的配置要能生效必须@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
  + @EnableWebMvc导致了WebMvcAutoConfiguration没有生效



**原理分析套路**

场景starter - xxxAutoConfiguration - 导入xxx组件 - 绑定xxxProperties --- 绑定配置文件项

#### 数据访问

##### SQL

**数据源自动配置**

```xml
<!-- 导入JDBC -->
<dependency>
	<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jdbc</artifactId>
</dependency>

<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connection-java</artifactId>
    <version>5.1.49</version>
</dependency>

想要修改版本
1.直接依赖引入具体版本
2.重新声明版本
<properties>
	<mysql.version>5.1.49</mysql.version>
</properties>
```

**分析自动配置**

自动配置的类

+ DataSourceAutoConfiguration：数据源的自动配置
  + 修改数据源相关的配置：spring.datasource
  + 数据库连接池的配置，是自己容器中没有DataSource才自动配
  + 底层配置好的连接池是：HikariDataSource
+ DataSourceTransactionManagerAutoConfiguration：事务管理器的自动配置
+ JdbcTemplateAutoConfiguration：JdbcTemplate的自动配置，可以来对数据库crud
  + 修改spring.jdbc来修改JdbcTemplate的配置
  + 容器中有这个组件
+ JndiDataSourceAutoConfiguration：jndi的自动配置
+ XADataSourceAutoConfiguration：分布式事务相关

**使用Druid数据源**

```XML
<dependency>
	<groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.1.17</version>
</dependency>
```

```java
@Configuration
public class MyDataSourceConfig {
    
    @ConfigurationProperties("spring.datasource")
    @Bean
    public DataSource dataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        //加入监控功能
        druidDataSource.setFilters("stat");
        return druidDataSource;
    }
    
    //配置druid的监控页功能
    @Bean
    public ServletRegistrationBean statViewServlet() {
        StatViewServlet statViewServlet = new StatViewServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(statViewServlet, "/druid/*");
        return registrationBean;
    }
}
```

```xml
<dependency>
	<groupId>com.alibaba</groupId>
    <artifactId>druid-spring-boot-starter</artifactId>
    <version>1.1.17</version>
</dependency>
```

**分析自动配置**

+ 扩展配置项spring.datasource.druid
+ DruidSpringAopConfiguration.class；监控springBean的；配置项：spring.datasource.druid.aop-patterns
+ DruidStatViewServletConfiguration.class，监控页的配置：spring.datasource.druid.stat-view-servlet，默认开启
+ DruidWebStatFilterConfiguration.class，web监控配置；spring.datasource.druid.web-stat-filter，默认开启
+ DruidFilterConfiguration.class；所有druid自己filter的配置

##### NoSQL

**自动配置**

+ RedisAutoConfiguration自动配置类。RedisProperties属性类 --> spring.redis
+ 连接工厂是准备好的。LettuceConnectionConfiguration、JedisConnectionConfiguration
+ 自动注入了RedisTemplate<Object,Object>、StringRedisTemplate（kv都是String）

#### 单元测试

##### JUnit5的变化

SpringBoot2.2.0开始引入JUnit5作为单元测试默认库

JUnit5 = JUnit Platform + JUnit Jupiter + JUnit Vintage

JUnit Platform：是在JVM上启动测试框架的基础，不仅支持JUnit自制的测试引擎，其他测试引擎也都可以接入

JUnit Jupiter：提供了JUnit5的新的编程模型，是JUnit5新特性的核心。内部包含了一个测试引擎，用于在JUnit Platform上运行

JUnit Vintage：提供了兼容JUnit4.x、JUnit3.x的测试引擎

SpringBoot整合JUnit后

+ 测试类加上@SpringBootTest注解

+ 编写测试方法：@Test标注（注意需要使用JUnit5版本的注解）
+ JUnit的类具有Spring的功能，@Autowired、@Transactional（测试完成后自动回滚）

##### JUnit5常用注解

+ @Test：表示方法是测试方法
+ @ParameterizedTest：表示方法是参数化测试
+ @RepeatedTest：表示方法可重复执行
+ @DisplayName：为测试类或者测试方法设置展示名称
+ @BeforeEach：表示在每个单元测试之前执行
+ @AfterEach：表示在每个单元测试之后执行
+ @BeforeAll：表示在所有单元测试之前执行
+ @AfterAll：表示在所有单元测试之后执行
+ @Tag：表示单元测试类别
+ @Disabled：表示测试类或测试方法不执行
+ @Timeout：表示测试方法如果超过了指定时间将会返回错误
+ @ExtendWith：为测试类或测试方法提供扩展类应用

##### 断言（assertions）

断言是测试方法中的核心部分，用来对测试需要满足的条件进行验证。这些断言方法都是org.junit.jupiter.api.Assertions的静态方法

**简单断言**

用来对单个值进行简单的验证

| 方法            | 说明                                 |
| --------------- | ------------------------------------ |
| assertEquals    | 判断两个对象或两个原始类型是否相等   |
| assertNotEquals | 判断两个对象或两个原始类型是否不相等 |
| assertSame      | 判断两个对象引用是否指向同一个对象   |
| assertNotSame   | 判断两个对象引用是否指向不同的对象   |
| assertTrue      | 判断给定的布尔值是否为true           |
| assertFalse     | 判断给定的布尔值是否为false          |
| assertNull      | 判断给定的对象引用是否为null         |
| assertNotNull   | 判断给定的对象引用是否不为null       |

**数组断言**

assertArrayEquals方法判断两个对象或原始类型的数组是否相等

**组合断言**

assertAll方法接受多个org.junit.jupiter.api.Executable函数式接口的实例作为要验证的断言，可以通过lambda表达式很容易的提供这些断言

**异常断言**

assertThrows

**超时断言**

assertTimeout

**快速失败**

fail

##### 前置条件（assumptions）

类似于断言，不同之处在于不满足的断言会使得测试方法失败，而不满足的前置条件只会使得测试方法的执行终止

##### 嵌套测试

通过Java中的内部类和@Nested注解实现嵌套测试，从而可以更好的把相关的测试方法组织在一起。在内部类中可以使用@BeforeEach和@AfterEach注解，而且嵌套的层次没有限制

##### 参数化测试

参数化测试是JUnit5很重要的一个新特性，它使得用不同的参数多次运行测试成为了可能，也为我们的单元测试带来许多便利

利用@ValueSource等注解，指定入参，我们将可以使用不同的参数进行多次单元测试，而不需要每新增一个参数就新增一个单元测试

+ @ValueSource：为参数化测试指定入参来源，支持八大基础类以及String类型，Class类型
+ @NullSource：表示为参数化测试提供一个null的入参
+ @EnumSource：表示为参数化测试提供一个枚举入参
+ @CsvFileSource：表示读取指定CSV文件内容作为参数化测试入参
+ @MethodSource：表示读取指定方法的返回值作为参数化测试入参（注意方法返回需要是一个流）

#### 指标监控

**SpringBoot Actuator**

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

```yaml
#management是所有actuator的配置
#management.endpoint.端点名.xxx 对某个端点的具体配置
management: 
  endponits: 
    enable-by-default: true #默认开启所有监控端点
    web:
      exposure:
        include: '*' #以web方式暴露所有端点
  endpoint:
    health:
      show-details: always
```

**Actuator Endpoint**

最常用的端点

| ID               | 描述                                                         |
| ---------------- | ------------------------------------------------------------ |
| auditevents      | 暴露当前应用程序的审核事件信息，需要一个AuditEventRepository组件 |
| beans            | 显示应用程序中所有Spring Bean的完整列表                      |
| caches           | 暴露可用的缓存                                               |
| conditions       | 显示自动配置的所有条件信息，包括匹配和不匹配的原因           |
| configprops      | 显示所有@ConfigurationProperties                             |
| env              | 暴露spring的属性ConfigurableEnvironment                      |
| flyway           | 显示已应用的所有flyway数据库迁移，需要一个或多个flyway组件   |
| health           | 显示应用程序运行状况信息                                     |
| httptrace        | 显示http跟踪信息（默认情况下，最近100个http请求-响应）需要一个HttpTraceRepository组件 |
| info             | 显示应用程序信息                                             |
| integrationgraph | 显示spring integrationgraph，需要依赖spring-integration-core |
| loggers          | 显示和修改应用程序中日志的配置                               |
| liquibase        | 显示已应用的所有liquibase数据库迁移。需要一个或多个liquibase组件 |
| metrics          | 显示当前应用程序的指标信息                                   |
| mappings         | 显示所有@RequestMapping路径列表                              |
| scheduledtasks   | 显示应用程序中的计划任务                                     |
| sessions         | 允许从Spring session支持的会话存储中检索和删除用户会话。需要使用Spring session的基于Servlet的web应用程序 |
| shutdown         | 使应用程序正常关闭。默认禁用                                 |
| startup          | 显示由ApplicationStartup收集的启动步骤数据。需要使用SpringApplication进行配置BufferingApplicationStartup |
| threaddump       | 执行线程转储                                                 |

如果你的应用程序是web应用程序（springMVC，Spring WebFlux或Jersey），则可以使用以下附加端点

| ID         | 描述                                                         |
| ---------- | ------------------------------------------------------------ |
| heapdump   | 返回hprof堆转储文件                                          |
| jolokia    | 通过http暴露JMX bean（需要引入jolokia，不适用于WebFlux）需要引入依赖jolokia-core |
| logfile    | 返回日志文件的内容（如果已设置logging.file.name或logging.file.path属性）。支持使用http Range标头来检索部分日志文件的内容 |
| prometheus | 以Prometheus服务器可以抓取到的格式公开指标。需要依赖micrometer-registry-prometheus |

最常用的

+ Health：监控状况
+ Metrics：运行时指标
+ Loggers：日志记录

**Health Endpoint**

健康检查端点，一般用于在云平台，平台会定时的检查应用的情况，我们就需要health endpoint可以作为平台返回当前应用的一系列组件健康状况的集合

重要的几点：

+ health endpoint返回的结果，应该是一系列健康检查后的一个汇总报告
+ 很多的健康检查默认以及自动配置好了，比如数据库、redis等
+ 可以很容易的添加自定义的健康检查机制

**Metrics Endpoint**

http://localhost:8080/actuator/metrics

提供详细的、层级的、空间指标信息，这些信息可以被pull（主动推送）或者push（被动获取）方式得到

+ 通过Metrics对接多种监控系统
+ 简化核心Metrics开发
+ 添加自定义Metrics或者扩展已有的Metrics

**开启与禁用Endpoints**

+ 默认所有的Endpoint除了shutdown都是开启的
+ 需要开启或者禁用某个Endpoint。配置模式为management.endpoint.< endpointName>.enable=true
+ 或者禁用所有的endpoints然后手动开启指定的endpoint

```yaml
management:
  endpoints:
    enabled-by-default: false
  endpoint:
    beans:
      enable: true
    health:
      enable: true
```

**定制Endpoint**

```java
@Component
public class MyComHealthIndicator extends AbstractHealthIndicator {
    
    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        Map<String,Object> map = new HashMap<>();
        if (1 == 1) {
            //builder.up();//健康
            builder.status(Status.UP);
            map.put("count",1);
            map.put("ms",100);
        } else {
            //builder.down();//不健康
            builder.status(Status.OUT_OF_SERVICE);
            map.put("err","连接超时");
            map.put("ms",3000);
        }
        
        builder.withDetail("code",100)
               .withDetails(map);
    }
}
```

**定制info信息**

1. 编写配置文件

```yaml
info:
  appName: boot-admin
  appVersion: 1.0.0
  mavenProjectName: @project.artifactId@ #使用@@可以获取maven的pom文件值
  mavenProjectVersion: @project.version@
```

2. 编写InfoContributor

```java
@Component
public class ExampleInfoContributor implements InfoContributor {
    
    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("msg","你好")
               .withDetails(Collections.singletonMap("world","666"));
    }
}
```

**定制Metrics信息**

```java
class MyService {
    Counter counter;
    public MyService(MeterRegistry meterRegistry) {
        counter = meterRegistry.count("myservice.method.running.counter");
    }
    
    public hello() {
        counter.increment();
    }
}

//也可以用下面的方式
@Bean
MeterBinder queueSize(Queue queue) {
    return (registry) -> Gauge.builder("queueSize", queue::size).register(registry);
}
```

**定制Endpoint**

```java
@Component
@Endpoint(id="myservice")
public class MyServiceEndPoint {
    
    @ReadOperation
    public Map getDockerInfo() {
        return Collections.singletonMap("dockerInfo","docker started....");
    }
    
    @WriteOperation
    public void stopDocker() {
        System.out.println("docker stopped...");
    }
}
```

**可视化界面**

spring-boot-admin

#### 原理解析

##### Profile功能

为了方便多环境适配，springboot简化了profile功能

**application-profile功能**

+ 默认配置文件application.yaml；任何时候都会加载

+ 指定环境配置文件application-{env}.yaml

+ 激活指定环境

  + 配置文件激活：spring.profiles.active=prod（激活application-prod.yaml）

  + 命令行激活：java -jar xxx.jar --spring.profiles.active=prod --person.name=haha

    修改配置文件的默认值，命令行优先

+ 默认配置与环境配置同时生效

+ 同名配置项，profile配置优先

**@Profile条件转配功能**

```java
@Configuration
@Profile("prod")
public class ProductionConfiguration {
    
}
```

**Profile分组**

```yaml
spring:
  profiles:
    group:
      production[0]: proddb
      production[1]: prodmq
      
#使用： --spring.profiles.active=production 激活
```

##### 外部化配置

**外部配置源**

常用：Java属性文件、yaml文件、环境变量、命令行参数

**配置文件查找位置**

+ classpath根路径
+ classpath根路径下config目录
+ jar包当前目录
+ jar包当前目录的config目录
+ /config子目录的直接目录

**配置文件加载顺序**

+ 当前jar包内部的application.properties和application.yml
+ 当前jar包内部的application-{profile}.properties和application-{profile}.yml
+ 引用外部jar包的application.properties和application.yml
+ 引用外部jar包的application-{profile}.properties和application-{profile}.yml

**指定环境优先，外部优先，后面的可以覆盖前面的同名配置项**

##### 自定义starter

**starter启动原理**

+ starter.pom引入autoconfigurer包
+ autoconfiguration包中配置使用META-INFO/spring.factories中EnableAutoConfiguration的值，使得项目启动加载指定的自动配置类
+ 编写自动配置类xxxAutoConfiguration -> xxxProperties

##### springboot原理

**springboot启动过程**

+ 创建SpringApplication
  + 保存了一些信息
  + 判定当前应用的类型，ClassUtils。Servlet
  + bootstrappers：初始启动引导器（List< Bootstrapper>）：去spring.factories文件中找org.springframework.boot.Bootstrapper
  + 找ApplicationContextInitializer；去spring.factories文件中找
  + 找ApplicationListener：应用监听器；去spring.factories文件中找
+ 运行springApplication
  + stopWatch
  + 记录应用的启动时间
  + 创建引导上下文（Context环境）createBootstrapContext
    + 获取到所有之前的bootstrappers挨个执行initializer()来完成对引导启动器上下文环境设置
  + 让当前应用进入headless模式。java.awt.headless
  + 获取所有RunListener（运行监听器）【为了方便所有Listener进行事件感知】
    + getSpringFactoriesInstance去spring.factories找SpringApplicationRunListener
  + 遍历SpringApplicationRunListener调用starting方法
    + 相当于通知所有感兴趣系统正在启动过程的人，项目正在starting
  + 保存命令行参数
  + 准备环境prepareEnvironment()
    + 返回或者创建基础环境信息。StandardServletEnvironment
    + 配置环境信息对象
      + 读取所有的配置源的配置属性值
    + 绑定环境信息
    + 监听器调用environmentPrepared；通知所有的监听器当前环境准备完成
  + 创建IOC容器（createApplicationContext()）
    + 根据项目类型（Servlet）创建容器
    + 当前会创建AnnotationConfigServletWebServerApplicationContext
  + 准备ApplicationContext IOC容器的基本信息，prepareContext()
    + 保存环境信息
    + IOC容器的后置处理流程
    + 应用初始化器：applyInitializers
      + 遍历所有的ApplicationContextInitializer。调用initialize，来对IOC容器进行初始化扩展
      + 遍历所有的listener调用contextPrepared。EventPublishRunListener；通知所有的监听器contextPrepared
    + 所有的监听器调用contextLoaded。通知所有的监听器contextLoaded
  + 刷新IOC容器。refreshContext
    + 创建容器中的所有组件，spring源码
  + 容器刷新完成后工作？afterRefresh
  + 所有监听器调用listeners.started(context)方法；通知所有的监听器started
  + 调用所有的runners；callRunners()
    + 获取容器中的ApplicationRunner
    + 获取容器中的CommandLineRunner
    + 合并所有的runner并且按照order排序
    + 遍历所有的runner。调用run方法
  + 如果以上有异常
    + 调用listener的failed方法
  + 调用所有监听器的running方法。listener.running(context)；通知所有的监听器running
  + running如果有问题。继续通知failed。调用所有Listener的failed，通知所有监听器failed

**Application Events and Listeners**

ApplicationContextInitializer

ApplicationListener

SpringApplicationRunListener

**ApplicationRunner与CommandLineRunner**

## SpringSecurity

### 整体架构

#### 整体架构

+ 认证：Authentication

  + AuthenticationManager

    AuthenticationManager主要实现类为ProviderManager，在ProviderManager中管理了很多AuthenticationProvider实例。在一次完整的认证流程中，SpringSecurity允许存在多个AuthenticationProvider，用来实现多种认证方式

  + Authentication

    认证以及认证成功的信息主要是由Authentication的实现类进行保存的

    + getAuthorities：获取用户权限信息
    + getCredentials：获取用户凭证信息，一般指密码
    + getDetails：获取用户详细信息
    + getPrinciple：获取用户身份信息，用户名、用户对象等等
    + isAuthenticated：用户是否认证成功

  + SecurityContextHolder

    用来获取登录之后的用户信息。SpringSecurity会将登录用户数据保存在session中。但是，为了使用方便，SpringSecurity在此基础上做了一些改进，其中最主要的一个变化就是线程绑定。当用户登录成功后，SpringSecurity会将登录成功的用户信息保存到SecurityContextHolder中。SecurityContextHolder中的数据保存默认是通过ThreadLocal来实现的，使用ThreadLocal创建的变量只能被当前线程访问，不能被其他线程访问和修改，也就是用户数据和请求线程绑定在一起。当登录请求处理完毕后，SpringSecurity会将SecurityContextHolder中的数据拿出来保存到Session中，同时将SecurityContextHodler中的数据清空。以后每当有请求到来时，SpringSecurity就会先从Session中取出用户登录数据，保存到SecurityContextHolder中，方便在该请求的后续处理过程中使用，同时在请求结束时将SecurityContextHolder中的数据拿出来保存到Session中，然后将SecurityContextHolder中的数据清空。这一策略非常方便用户在Controller、Service层以及任何代码中获取当前登录用户数据。

+ 授权：Authorization

  + AccessDecisionManager

    访问决策管理器，用来决定此次访问是否被允许

  + AccessDecisionVoter

    访问决策投票器，投票器会检查用户是否具备应有的角色，进而投出赞成、反对或弃权票

    AccessDecisionManager和AccessDecisionVoter都有众多的实现类，在AccessDecisionManager中会挨个遍历AccessDecisionVoter，进而决定是否允许用户访问，因而AccessDecisionManager和AccessDecisionVoter类似于ProviderManager和AuthenticationProvider的关系

  + ConfigAttribute

    用来保存授权时的角色信息，在SpringSecurity中，用户请求一个资源需要的角色会被封装成一个ConfigAttribute对象，在ConfigAttribute中只有一个getAttribute方法，该方法返回一个String字符串，就是角色的名称。一般来说，角色名称都带有一个ROLE_前缀，投票器AccessDecisionVoter所做的事情，其实就是比较用户所具备的角色和请求某个资源所需的ConfigAttribute之间的关系

#### 实现原理

![](/img/SpringSecurity_1.png)

#### SpringSecurity中提供的过滤器

| 过滤器                                               | 作用                                                    | 默认是否加载 |
| ---------------------------------------------------- | ------------------------------------------------------- | ------------ |
| ChannelProcessingFilter                              | 过滤请求协议HTTP、HTTPS                                 | NO           |
| **WebAsyncManagerIntegrationFilter**                 | 将WebAsyncManager与SpringSecurity上下文进行集成         | YES          |
| **SecurityContextPersistenceFilter**                 | 在处理请求之前，将安全信息加载到SecurityContextHolder中 | YES          |
| **HeaderWriterFiler**                                | 处理头信息加入响应中                                    | YES          |
| CorsFilter                                           | 处理跨域问题                                            | NO           |
| **CsrfFilter**                                       | 处理CSRF攻击                                            | YES          |
| **LogoutFilter**                                     | 处理注销登录                                            | YES          |
| OAuth2AuthorizationRequestRedirectFilter             | 处理OAuth2认证重定向                                    | NO           |
| Saml2WebSsoAuthenticationRequestFilter               | 处理SAML认证                                            | NO           |
| X509AuthenticationFilter                             | 处理X509认证                                            | NO           |
| AbstractPerAuthenticatedProcessingFilter             | 处理预认证问题                                          | NO           |
| CasAuthenticationFilter                              | 处理CAS单点登录                                         | NO           |
| OAuth2LoginAuthenticationFilter                      | 处理OAuth2认证                                          | NO           |
| Saml2WebSsoAuthenticationFilter                      | 处理SAML认证                                            | NO           |
| **UsernamePasswordAuthenticationFilter**             | 处理表单登录                                            | YES          |
| OpenIDAuthenticationFilter                           | 处理OpenID认证                                          | NO           |
| **DefaultLoginPageGeneratingFilter**                 | 配置默认登录页面                                        | YES          |
| **DefaultLogoutPageGeneratingFilter**                | 配置默认注销页面                                        | YES          |
| ConcurrentSessionFilter                              | 处理session有效期                                       | NO           |
| DigestAuthenticationFilter                           | 处理HTTP摘要认证                                        | NO           |
| BearerTokenAuthenticationFilter                      | 处理OAuth2认证的AccessToken                             | NO           |
| **BasicAuthenticationFilter**                        | 处理HttpBasic登录                                       | YES          |
| **RequestCacheAwareFilter**                          | 处理请求缓存                                            | YES          |
| **SecurityContextHolder**<br/>**AwareRequestFilter** | 包装原始请求                                            | YES          |
| JaasApiIntegrationFilter                             | 处理JAAS认证                                            | NO           |
| RememberMeAuthenticaitonFilter                       | 处理RememberMe登录                                      | NO           |
| **AnonymousAuthenticationFilter**                    | 配置匿名请求                                            | YES          |
| OAuth2AuthorizationCodeGrantFilter                   | 处理OAuth2认证中授权码                                  | NO           |
| **SessionManagementFilter**                          | 处理session并发问题                                     | YES          |
| **ExceptionTranslationFilter**                       | 处理认证授权中的异常                                    | YES          |
| **FilterSecurityInterceptor**                        | 处理授权相关                                            | YES          |
| SwitchUserFilter                                     | 处理账户切换                                            | NO           |

默认情况下SpringBoot在对SpringSecurity进入自动化配置时，会创建一个名为SpringSecurityFilterChain的过滤器，并注入到Spring容器中，这个过滤器将负责所有的安全管理，包括用户认证、授权、重定向到登录页面等。具体可以参考WebSecurityConfiguration

SpringBootWebSecurityConfiguration：SpringBoot中SpringSecurity自动配置类

#### 默认登录页面流程

![](/img/SpringSecurity_2.png)

```java
//SpringBootWebSecurityConfiguration
@Bean
@Order(SecurityProperties.BASIC_AUTH_ORDER)
SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeRequests().anyRequest().authenticated();
    http.formLogin();
    http.httpBasic();
    return http.build();
}
```

UserDetailService是顶层父接口，接口中的loaderUserByUserName方法是用来在认证时进行用户名认证方法，默认使用的是内存实现，如果想要修改数据库实现，我们只需要自定义UserDetailService实现，最终返回UserDetails实例即可

UserDetailServiceAutoConfiguration中配置了默认登录时用户名密码

WebSecurityConfigurerAdapter：扩展SpringSecurity所有默认配置

UserDetaiService：用来修改默认认证的数据源信息

### 自定义认证

#### 自定义资源权限规则前后端分离解决方案

```java
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/index").permitAll() //放行资源
                .anyRequest().authenticated()
                .and().formLogin();
    }
}
```

+ permitAll()：代表放行该资源，该资源为公共资源，无需认证和授权可以直接访问
+ anyRequest().authenticated()：代表所有请求，必须认证后才能访问
+ formLogin()：开启表单认证

#### 自定义登录界面

```java
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/index").permitAll() //放行资源
                .anyRequest().authenticated()
                .and().formLogin()
                .loginPage("/login.html") //指定默认登录页面，注意，一旦自定义登陆页面以后必须指定登录url
                .loginProcessingUrl("/doLogin") //指定处理登录请求url
                .userNameParameter("uname")
                .passwordParameter("passwd")
                //.successForwardUrl("/index") //认证成功forward跳转路径,始终在认证成功之后跳转到指定请求
                .defaultSuccessUrl("/index") //认证成功redirect之后跳转，根据上一保存请求进行成功跳转
                .and()
                .crsf().disable(); //禁止crsf跨站请求保护
    }
}
```

#### 认证成功处理（前后端分离）

```java
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/index").permitAll() //放行资源
                .anyRequest().authenticated()
                .and().formLogin()
                .loginPage("/login.html") //指定默认登录页面，注意，一旦自定义登陆页面以后必须指定登录url
                .loginProcessingUrl("/doLogin") //指定处理登录请求url
                .userNameParameter("uname")
                .passwordParameter("passwd")
                //.successForwardUrl("/index") //认证成功forward跳转路径,始终在认证成功之后跳转到指定请求
                //.defaultSuccessUrl("/index") //认证成功redirect之后跳转，根据上一保存请求进行成功跳转
                .successHandler(new MyAuthenticationSuccessHandler()) //认证成功时处理 前后端分离解决方案
                .and()
                .crsf().disable(); //禁止crsf跨站请求保护
    }
}
```

```java
//自定义认证成功之后处理
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Map<String, Obejct> result = new HashMap<>();
        result.put("msg", "登陆成功");
        result.put("status", 200);
        result.put("authentication", authentication);
        response.setContentType("application/json;charset=UTF-8");
        String s = new ObjectMapper().writeValueAsString(result);
        response.getWritter().println(s);
    }
}
```

#### 显示登录失败信息

源码参考SimpleUrlAuthenticationFailureHandler

```java
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/index").permitAll() //放行资源
                .anyRequest().authenticated()
                .and().formLogin()
                .loginPage("/login.html") //指定默认登录页面，注意，一旦自定义登陆页面以后必须指定登录url
                .loginProcessingUrl("/doLogin") //指定处理登录请求url
                .userNameParameter("uname")
                .passwordParameter("passwd")
                //.successForwardUrl("/index") //认证成功forward跳转路径,始终在认证成功之后跳转到指定请求
                //.defaultSuccessUrl("/index") //认证成功redirect之后跳转，根据上一保存请求进行成功跳转
                .successHandler(new MyAuthenticationSuccessHandler()) //认证成功时处理 前后端分离解决方案
                //.failureForwardUrl("/login.html") //认证失败后forward跳转 异常信息从request里SRPING_SECURITY_LAST_EXCEPTION取
                //.failureUrl("/login.html") //默认 认证失败后redirect跳转 异常信息从session里SRPING_SECURITY_LAST_EXCEPTION取
                .failureHandler(new MyAuthenticationFailureHandler()) //用来自定义认证失败之后处理 前后端分离解决方案
                .and()
                .crsf().disable(); //禁止crsf跨站请求保护
    }
}
```

```java
//自定义认证失败之后处理
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        Map<String, Obejct> result = new HashMap<>();
        result.put("msg", "登陆失败"+exception.getMessage());
        result.put("status", 500);
        response.setContentType("application/json;charset=UTF-8");
        String s = new ObjectMapper().writeValueAsString(result);
        response.getWritter().println(s);
    }
}
```

#### 注销登录

**开启注销登录默认开启**

```java
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .and().formLogin()
                .and.logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessUrl("/login.html")
                .and()
                .crsf().disable(); //禁止crsf跨站请求保护
    }
}
```

+ 通过logout()方法开启注销配置
+ logoutUrl指定退出登录请求地址，默认是GET请求，路径为/logout
+ invalidateHttpSession退出时是否是session失效，默认值为true
+ clearAuthentication退出时是否清除认证信息，默认值为true
+ logoutSuccessUrl退出登陆时跳转地址

**配置多个注销登录请求**

如果项目中有需要，开发者还可以配置多个注销登录的请求，同时还可以指定请求的方法

```java
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .and().formLogin()
                .and.logout()
                .logoutRequestMatcher(new OrRequestMatcher(
                		new AntPathRequestMatcher("/logout1","GET"),
                        new AntPathRequestMatcher("/logout","GET")
                ))
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessUrl("/login.html")
                .and()
                .crsf().disable(); //禁止crsf跨站请求保护
    }
}
```

**前后端分离注销登录配置**

如果是前后端分离开发，注销成功之后就不需要页面跳转了，只需要将注销成功的信息返回前端即可，此时我们可以通过自定义LogoutSuccessHandler实现来返回内容注销之后信息

```java
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .and().formLogin()
                .and.logout()
                .logoutRequestMatcher(new OrRequestMatcher(
                		new AntPathRequestMatcher("/logout1","GET"),
                        new AntPathRequestMatcher("/logout","GET")
                ))
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessHandler(new MyLogoutSuccessHandler())
                .and()
                .crsf().disable(); //禁止crsf跨站请求保护
    }
}
```

```java
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    
    @Override
    public void onLogoutSuccess(HttpServletRequeset request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Map<String, Object> result = new HashMap<>();
        result.put("msg", "注销成功");
        result.put("status", 200);
        response.setContentType("application/json;charset=UTF-8");
        String s = new ObjectMapper().writeValueAsString(result);
        response.getWriter().println(s);
    }
}
```

#### 登录用户数据获取

**SecurityContextHolder**

用来获取登录之后的用户信息。SpringSecurity会将登录用户数据保存在session中。但是，为了使用方便，SpringSecurity在此基础上做了一些改进，其中最主要的一个变化就是线程绑定。当用户登录成功后，SpringSecurity会将登录成功的用户信息保存到SecurityContextHolder中。SecurityContextHolder中的数据保存默认是通过ThreadLocal来实现的，使用ThreadLocal创建的变量只能被当前线程访问，不能被其他线程访问和修改，也就是用户数据和请求线程绑定在一起。当登录请求处理完毕后，SpringSecurity会将SecurityContextHolder中的数据拿出来保存到Session中，同时将SecurityContextHodler中的数据清空。以后每当有请求到来时，SpringSecurity就会先从Session中取出用户登录数据，保存到SecurityContextHolder中，方便在该请求的后续处理过程中使用，同时在请求结束时将SecurityContextHolder中的数据拿出来保存到Session中，然后将SecurityContextHolder中的数据清空。

实际上SecurityContextHolder中存储的是SecurityContext，在SecurityContext中存储的是Authentication

```java
public class SecurityContextHolder {
    public static final String MODE_THREADLOCAL = "MODE_THREADLOCAL";
    public static final String MODE_INHERITABLETHREADLOCAL = "MODE_INHERITABLETHREADLOCAL";
    public static final String MODE_GLOBAL = "MODE_GLOBAL";
    private static final String MODE_PRE_INITIALIZED = "MODE_PRE_INITIALIZED";
    public static final String SYSTEM_PROPERTY = "spring.security.strategy";
    private static String strategyName = System.getProperty("spring.security.strategy");
    private static SecurityContextHolderStrategy strategy;
    private static int initializeCount = 0;

    public SecurityContextHolder() {
    }

    private static void initialize() {
        initializeStrategy();
        ++initializeCount;
    }

    private static void initializeStrategy() {
        if ("MODE_PRE_INITIALIZED".equals(strategyName)) {
            Assert.state(strategy != null, "When using MODE_PRE_INITIALIZED, setContextHolderStrategy must be called with the fully constructed strategy");
        } else {
            if (!StringUtils.hasText(strategyName)) {
                strategyName = "MODE_THREADLOCAL";
            }

            if (strategyName.equals("MODE_THREADLOCAL")) {
                strategy = new ThreadLocalSecurityContextHolderStrategy();
            } else if (strategyName.equals("MODE_INHERITABLETHREADLOCAL")) {
                strategy = new InheritableThreadLocalSecurityContextHolderStrategy();
            } else if (strategyName.equals("MODE_GLOBAL")) {
                strategy = new GlobalSecurityContextHolderStrategy();
            } else {
                try {
                    Class<?> clazz = Class.forName(strategyName);
                    Constructor<?> customStrategy = clazz.getConstructor();
                    strategy = (SecurityContextHolderStrategy)customStrategy.newInstance();
                } catch (Exception var2) {
                    ReflectionUtils.handleReflectionException(var2);
                }

            }
        }
    }
}
```

+ MODE_THREADLOCAL：这种策略是将SecurityContext存放在ThreadLocal中，ThreadLocal的特点是在哪个线程中存储就要在哪个线程中读取，这其实非常适合web应用，因为在默认情况下，一个请求无论经过多少Filter到达Servlet，都是由一个线程来处理的。这也是SecurityContextHolder的默认存储策略，这种存储策略意味着如果在具体的业务处理代码中，开启了子线程，在子线程中去获取登录用户数据，就会获取不到
+ MODE_INHERITABLETHREADLOCAL：这种存储模式适用于多线程环境，如果希望在子线程中也能够获取到登录用户数据，那么可以使用这种存储模式，修改VM参数：-Dspring.security.strategy=MODE_INHERITABLETHREADLOCAL
+ MODE_GLOBAL：这种存储模式实际上是将数据保存在一个静态变量中，在Javaweb开发中，这种模式很少使用到

**SecurityContextHolderStrategy**

SecurityContextHolderStrategy接口用来定义存储策略方法，该接口中一共定义了四个方法

+ clearContext：清除存储的SecurityContext对象
+ getContext：获取存储的SecurityContext对象
+ setContext：设置存储的SecurityContext对象
+ createEmptyContext：创建一个空的SecurityContext对象

```java
@RequestMapping("/hello")
public String hello() {
    //1.获取认证信息
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //身份信息
    User user = (User)authentication.getPrincipal();
    user.getUserName();
    //权限信息
    authentication.getAuthorites();
    return "hello spring security";
}
```

#### 自定义认证数据源

**认证流程分析**

![](/img/SpringSecurity_3.png)

+ 发起认证请求，请求中携带用户名、密码，该请求会被UsernamePasswordAuthenticationFilter拦截
+ 在UsernamePasswordAuthenticationFilter的attemptAuthentication方法中将请求用户名和密码封装为Authentication对象，交给AuthenticationManager认证
+ 认证成功，将认证信息存储到SecurityContextHolder以及调用RememberMe等，并回调AuthenticationSuccessHandler处理
+ 认证失败，清除SecurityContextHolder以及RememberMe中的信息，回调AuthenticationFailureHandler进行处理

**三者关系**

AuthenticationManager是认证的核心类，但实际上在底层真正认证时还离不开ProviderManager和AuthenticationProvider

+ AuthenticationManager是一个认证管理器，它定义了SpringSecurity过滤器要执行认证操作
+ ProviderManager是AuthenticationManager接口的实现类。SpringSecurity认证时默认使用就是ProvideManager
+ AuthenticationProvider就是针对不同的身份类型执行的具体的身份认证

在SpringSecurity中，允许系统同时支持多种不同的认证方式，例如同时支持用户名/密码认证、RememberMe认证、手机号码动态认证等，而不同的认证方式对应了不同的AuthenticationProvider，所以一个完整的认证流程可能由多个AuthenticationProvider来提供。多个AuthenticationProvider将组成一个列表，这个列表将由ProviderManager代理。换句话说，在ProviderManager中存在一个AuthenticationProvider列表，在ProviderManager中遍历列表的每一个AuthenticationProvider去执行身份认证，最终得到认证结果。

ProviderManager本身也可以再配置一个AuthenticationManager作为parent，这样当ProviderManager认证失败后，就可以进入到parent中再次进行认证。理论上来说，ProviderManager的parent可以是任意类型的AuthenticationManager，但通常都是由ProviderManager来扮演parent角色，也就是ProviderManager是ProviderManager的parent

ProviderManager本身也可以有多个，多个ProviderManager共用同一个parent。有时，一个应用程序有受保护资源的逻辑组（例如，所有符合路径模式的网络资源，如/api/**），每个组可以有自己的专用AuthenticationManager。通常，每个组都是一个ProviderManager，他们共享一个父级。然后父级是一种全局资源，作为所有提供者的后备资源

![](/img/SpringSecurity_4.png)

默认情况下AuthenticationProvider是由DaoAuthenticationProvider类来实现认证的，在DaoAuthenticationProvider认证时又通过UserDetailsService完成数据源的校验

总结：

AuthenticationManager是认证管理器，在SpringSecurity中有全局AuthenticationManager，也可以有局部AuthenticationManager。无论全局认证管理器还是局部认证管理器都是由ProviderManager进行实现。每一个ProviderManager中都代理一个AuthenticationProvider的列表，列表中每一个实现代表一种身份认证方式。认证时底层数据源需要调用UserDetailService来实现

**配置全局AuthenticationManager**

+ 默认的全局AuthenticationManager

  ```java
  @Configuration
  public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
      
      @Autowired
      public void initialize(AuthenticationManagerBuilder builder) {
          
      }
  }
  ```

  springboot对security进行自动配置时自动在工厂中创建一个全局AuthentiationManager

  总结：

  1. 默认自动配置创建全局AuthenticationManager默认找当前项目中是否存在自定义UserDetailService实例，自动将当前项目UserDetaiService实例设置为数据源
  2. 默认自动配置全局AuthenticationManager在工厂中使用时直接在代码中注入即可

+ 自定义全局AuthenticationManager

  ```java
  @Configuration
  public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
      
      @Bean
      public UserDetailsService userDetailsService() {
      	InMemoryUserDetailsManager userDetailService = new InMemoryUserDetailsManager();
          return userDetailsService;
      }
      
      //没有在工厂中暴露出来
      @Override
      public void configure(AuthenticationManagerBuilder builder) {
          builder.userDetaisService(userDetailService());
      }
      
      //用来将自定义AuthenticationManager在工厂中进行暴露，可以在任何位置注入
      @Override
      @Bean
      public AuthenticationManager authenticationManagerBean() {
          return super.authenticationManagerBean()
      }
  }
  ```

  总结：

  1. 一旦通过configure方法自定义AuthenticationManager实现，就会将工厂中自动配置AuthenticationManager进行覆盖
  2. 一旦通过configure方法自定义AuthenticationManager实现，需要在实现中指定认证数据源对象UserDetailService实例
  3. 一旦通过configure方法自定义AuthenticationManager实现，这种方式创建AuthenticationManager对象工厂内部本地一个AuthenticationManager对象不允许在其他自定义组件中进行注入

**自定义数据库数据源**

```java
@Component
public class MyUserDetailService implements UserDetailsService {
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.查询用户
        User user = userDao.loadUserByUsername(username);
        //2.查询权限信息
        List<Role> roles = userDao.getRolesByUid(user.getId());
        user.setRoles(roles);
        return user;
    }
}
```

```java
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    
    //构造注入
    private final MyUserDetailService myUserDetailService;
    
    @Autowired
    public WebSecurityConfigurer(MyUserDetailService myUserDetailService) {
        this.myUserDetailService = myUserDetailService;
    }
    
    @Override
    public void configure(AuthenticationManagerBuilder builder) {
        builder.userDetaisService(myUserDetailService);
    }
}
```

#### 添加认证验证码

**配置验证码**

```xml
<dependency>
	<groupId>com.github.penggle</groupId>
    <artifactId>kaptcha</artifactId>
    <version>2.3.2</version>
</dependency>
```

```java
@Configuration
public class KaptchaConfig {
    
    @Bean
    public Producer kaptcha() {
        Properties properties = new Properties();
        properties.setProperty("kaptcha.image.width", "150");
        properties.setProperty("kaptcha.image.height", "50");
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789");
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
```

**生成验证码**

```java
@RestController
public class VerifyCodeController {
    
    private final Producer producer;
    
    @Autowired
    public VerifyCodeController(Producer producer) {
        this.producer = producer;
    }
    
    @GetMapping("/vc.jpg")
    public String getVerifyCode(HttpSession session) {
        //1.生成验证码
        String text = producer.createText();
        //2.放入session redis实现
        session.setAttribute("kaptcha", text);
        //3.生成图片
        BufferdImage bi = producer.createImage(text);
        FastByteArrayOutputStream fos = new FastByteArrayOutputStream();
        ImageIO.write(bi, "jpg", fos);
        //4.返回base64
        return Base64.encodeBase64String(fos.toByteArray());
    }
}
```

**SpringSecurity配置类**

```java
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.withUsername("root").password("{noop}123").roles("admin").build());
        return inMemoryUserDetailsManager;
    }
    
    @Override
    protected void config(AuthenticationManagerBuilder auth) {
        auth.userDetailsService(userDetailsService());
    }
    
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() {
        return super.authenticationManagerBean();
    }
    
    @Bean
    public LoginKaptchaFilter loginKaptchaFilter() {
        LoginKaptchaFilter loginKaptchaFilter = new LoginKaptchaFilter();
        //1.认证url
        loginKaptchaFilter.setFilterProcessesUrl("/dologin");
        //2.认证接收参数
        loginKaptchaFilter.setUsernameParameter("uname");
        loginKaptchaFilter.setPasswordParameter("passwd");
        loginKaptchaFilter.sertKaptchaParameter("kaptcha");
        //3.指定认证管理器
        loginKaptchaFilter.setAuthenticationManager(authenticationManager());
        
        return loginKaptchaFilter;
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .mvcMatchers("/vc.jpg").permitAll()
            .anyRequest().authenticated()
            .and().formLogin()
            .and().logout()
            .and().csrf().disable();
        
        http.addFilterAt(loginKaptchaFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
```

**自定义Filter**

```java
public class LoginKaptchaFilter extends UsernamePasswordAuthenticationFilter {
    
    @Override
    public Authentication attempAuthentication(HttpServletRequest request, HttpServletResponse response) {
        //1.获取请求验证码
        Map<String, String> userInfo = new ObjectMapper().readValue(request.getInputStream(), Map.class);
        String kaptcha = userInfo.get("kaptcha");
        String username = userInfo.get("username");
        String password = userInfo.get("password");
        //2.获取session中验证码
        String sessionVerifyCode = (String)request.getSession().getAttribute("kaptcha");
        if (kaptcha.equalsIgnoreCase(sessionVerifyCode)) {
            //3.获取用户名和密码认证
         	UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }
}
```

### 密码加密

#### 常见方案

**Hash算法**

最早我们使用类似SHA-256、SHA-512、MD5等这样的单向Hash算法

**单向自适应函数**

在SpringSecurity中，使用一种自适应单向函数（Adaptive One-way Functions）来处理密码问题，这种自适应单向函数在进行密码匹配时，会有意占用大量系统资源（CPU、内存等），这样可以增加恶意用户攻击系统的难度。在SpringSecurity中，开发者可以通过bcrypt、PBKDF2、sCrypt以及argon2来体验这种自适应单向函数加密。由于自适应单向函数有意占用大量系统资源，因此每个登录认证请求都会大大降低应用程序的性能，但是SpringSecurity不会采取任何措施来提高密码验证速度，因为它正是通过这种方式来增强系统的安全性

+ BCryptPasswordEncoder
+ Argon2PasswordEncoder
+ Pbkdf2PasswordEncoder
+ SCryptPasswordEncoder

#### PasswordEncoder

通过对认证流程源码分析得知，实际密码比较是由PasswordEncoder完成的，因此只需要使用PasswrodEncoder不同实现就可以实现不同方式加密

```java
public interface PasswordEncoder {
    //加密
    String encode(CharSequence rawPassword);
    //比较密码
    boolean matches(CharSequence rawPassword, String encodedPassword);
    //密码进行升级
    default boolean upgradeEncoding(String encodedPassword) {
        return false;
    }
}
```

#### DelegatingPasswordEncoder

Spring Security5.0之后，默认的密码加密方案是DelegatingPasswordEncoder。DelegatingPasswordEncoder是一个代理类，而非一种全新的密码加密方案，DelegatingPasswordEncoder主要用来代理上面介绍的不同的密码加密方案。为什么使用DelegatingPasswordEncoder？

+ 兼容性：使用DelegatingPasswordEncoder可以帮助许多使用旧密码加密方式的系统顺利迁移到SpringSecurity中，它允许在同一个系统中同时存在多种不同的密码加密方案
+ 便捷性：密码存储的最佳方案不可能一直不变，如果使用DelegatingPasswordEncoder作为默认的密码加密方案，当需要修改加密方案时，只需要修改很小一部分代码就可以实现

DelegatingPasswordEncoder继承PasswordEncoder

#### 如何使用PasswordEncoder

如果在工厂中指定了PasswordEncoder，就会使用指定的PasswordEncoder，否则就会使用默认的DelegatingPasswordEncoder

+ 使用固定密码加密方案

```java
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

+ 使用灵活密码加密方案 推荐

```java
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Bean
    public UserDetailsService UserDetailsService() {
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
        inMemoryUserDetailsManager.createUser(User.withUsername("root").password("{bcrypt}123").roles("admin").build());
        return inMemoryUserDetailsManager;
    }
}
```

#### 密码自动升级

```java
@Service
public class MyUserDetailService implements UserDetailsService, UserDetailPasswordService {
    
    private final UserDao userDao;
    
    @Autowired
    public MyUserDetailService(UserDao userDao) {
        this.userDao = userDao;
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userDao.loadUserByUserName(username);
        user.setRoles(userDao.getRolesByUid(user.getId()));
        return user;
    }
    
    //默认使用DelegatingPasswordEncoder 默认使用BCrypt加密
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        Integer result = userDao.updatePassword(user.getUsername(), newPassword);
        if (result = 1) {
            ((User) user).setPassword(newPassword);
        }
        return user;
    }
}
```

### RememberMe

#### 基本使用

```java
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().formLogin()
                .and().rememberMe() //开启记住我功能
                .and().crsf().disable();
    }
}
```

#### 原理分析

**RememberMeAuthenticationFilter**

勾选记住我选项后，登录请求中多了一个remember-me : on的参数，该请求会被RememberMeAuthenticationFilter拦截然后自动登录

```java
private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    //1.请求到达过滤器后，首先判断SecurityContextHolder中是否有值，没值表示用户尚未登录，此时调用autoLogin自动登录
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
        this.logger.debug(LogMessage.of(() -> {
            return "SecurityContextHolder not populated with remember-me token, as it already contained: '" + SecurityContextHolder.getContext().getAuthentication() + "'";
        }));
        chain.doFilter(request, response);
    } else {
        Authentication rememberMeAuth = this.rememberMeServices.autoLogin(request, response);
        if (rememberMeAuth != null) {
            try {
                //2.rememberMeAuth不为null表示自动登录成功，调用authenticate方法对key进行校验，并且将登陆成功的用户信息保存到SecurityContextHolder中
                rememberMeAuth = this.authenticationManager.authenticate(rememberMeAuth);
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(rememberMeAuth);
                SecurityContextHolder.setContext(context);
                this.onSuccessfulAuthentication(request, response, rememberMeAuth);
                this.logger.debug(LogMessage.of(() -> {
                    return "SecurityContextHolder populated with remember-me token: '" + SecurityContextHolder.getContext().getAuthentication() + "'";
                }));
                this.securityContextRepository.saveContext(context, request, response);
                if (this.eventPublisher != null) {
                    this.eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(SecurityContextHolder.getContext().getAuthentication(), this.getClass()));
                }

                if (this.successHandler != null) {
                    //调用登录成功回调，并发布登录成功事件
                    this.successHandler.onAuthenticationSuccess(request, response, rememberMeAuth);
                    return;
                }
            } catch (AuthenticationException var6) {
                this.logger.debug(LogMessage.format("SecurityContextHolder not populated with remember-me token, as AuthenticationManager rejected Authentication returned by RememberMeServices: '%s'; invalidating remember-me token", rememberMeAuth), var6);
                //登录失败，调用loginFail处理登录失败回调
                this.rememberMeServices.loginFail(request, response);
                this.onUnsuccessfulAuthentication(request, response, var6);
            }
        }

        chain.doFilter(request, response);
    }
}
```

**RememberMeServices**

```java
public interface RememberMeServices {
    //从请求中提取出需要的参数，完成自动登录功能
    Authentication autoLogin(HttpServletRequest request, HttpServletResponse response);
    //自动登录失败的回调
    void loginFail(HttpServletRequest request, HttpServletResponse response);
    //自动登录成功的回调
    void loginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication);
}
```

**TokenBasedRememberMeServices**

```java
//验证Cookie中的令牌是否合法
protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) {
    if (cookieTokens.length != 3) {
        throw new InvalidCookieException("Cookie token did not contain 3 tokens, but contained '" + Arrays.asList(cookieTokens) + "'");
    } else {
        long tokenExpiryTime = this.getTokenExpiryTime(cookieTokens);
        if (this.isTokenExpired(tokenExpiryTime)) {
            throw new InvalidCookieException("Cookie token[1] has expired (expired on '" + new Date(tokenExpiryTime) + "'; current time is '" + new Date() + "')");
        } else {
            UserDetails userDetails = this.getUserDetailsService().loadUserByUsername(cookieTokens[0]);
            Assert.notNull(userDetails, () -> {
                return "UserDetailsService " + this.getUserDetailsService() + " returned null for username " + cookieTokens[0] + ". This is an interface contract violation";
            });
            String expectedTokenSignature = this.makeTokenSignature(tokenExpiryTime, userDetails.getUsername(), userDetails.getPassword());
            if (!equals(expectedTokenSignature, cookieTokens[2])) {
                throw new InvalidCookieException("Cookie token[2] contained signature '" + cookieTokens[2] + "' but expected '" + expectedTokenSignature + "'");
            } else {
                return userDetails;
            }
        }
    }
}

//登录成功设置Cookie返回
public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
    String username = this.retrieveUserName(successfulAuthentication);
    String password = this.retrievePassword(successfulAuthentication);
    if (!StringUtils.hasLength(username)) {
        this.logger.debug("Unable to retrieve username");
    } else {
        if (!StringUtils.hasLength(password)) {
            UserDetails user = this.getUserDetailsService().loadUserByUsername(username);
            password = user.getPassword();
            if (!StringUtils.hasLength(password)) {
                this.logger.debug("Unable to obtain password for user: " + username);
                return;
            }
        }

        int tokenLifetime = this.calculateLoginLifetime(request, successfulAuthentication);
        long expiryTime = System.currentTimeMillis();
        expiryTime += 1000L * (long)(tokenLifetime < 0 ? 1209600 : tokenLifetime);
        String signatureValue = this.makeTokenSignature(expiryTime, username, password);
        this.setCookie(new String[]{username, Long.toString(expiryTime), signatureValue}, tokenLifetime, request, response);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Added remember-me cookie for user '" + username + "', expiry: '" + new Date(expiryTime) + "'");
        }

    }
}
```

#### 内存令牌

**PersistentTokenBasedRememberMeServices**

```java
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().formLogin()
                .and().rememberMe() //开启记住我功能
                .rememberMeServices(rememberMeServices()) // 指定rememberMeService实现
                .and().crsf().disable();
    }
    
    @Bean
    public RememberMeServcies rememberMeServices() {
        return new PersistentTokenBasedRememberMeServices(UUID.randomUUID().toString(), userDetailService(), new InMemoryTokenRepositoryImpl());
    }
}
```

### 会话管理

#### 简介

当浏览器调用登录接口登录成功后，服务端会和浏览器之间建立一个会话（Session），浏览器在每次发送请求时都会携带一个SessionId，服务端则根据这个SessionId来判断用户身份。当浏览器关闭后，服务端的session并不会自动销毁，需要开发者手动在服务端调用Session销毁方法，或者等session过期时间到了自动销毁。在SpringSecurity中，与HttpSession相关的功能由SessionManagementFilter和SessionAuthenticationStrategy接口来处理，SessionManagementFilter过滤器将Session相关操作委托给SessionAuthenticationStrategy接口去完成

#### 会话并发管理

会话并发管理就是指在当前系统中，同一个用户可以同时创建多少个会话，如果一台设备对应一个会话，那么也可以理解为一个用户可以同时在多少台设备上进行登录。默认情况下，同一用户在多少台设备上登录并没有限制，不过开发者可以在SpringSecurity中对此进行设置

```java
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().formLogin()
                .and().crsf().disable()
                .sessionManagement() //开启会话管理
                .maximumSessions(1); //允许会话最大并发只能一个客户端
    }
    
}
```

#### 会话失效处理

**传统web开发处理**

```java
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().formLogin()
                .and().crsf().disable()
                .sessionManagement() //开启会话管理
                .maximumSessions(1) //允许会话最大并发只能一个客户端
                .expiredUrl("/login"); //会话过期处理，传统web开发处理 
    }
    
}
```

**前后端分离开发处理**

```java
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().formLogin()
                .and().crsf().disable()
                .sessionManagement() //开启会话管理
                .maximumSessions(1) //允许会话最大并发只能一个客户端
                //.expiredUrl("/login"); //会话过期处理，传统web开发处理
                .expiredSessionStrategy(event -> {
                    HttpServletResponse response = event.getResponse();
                    Map<String, Object> result = new HashMap<>();
                    result.put("status", 500);
                    result.put("msg", "当前会话以及失效，请重新登陆");
                    String s = new ObjectMapper().writeValueAsString(result);
                    response.getWriter().println(s);
                    response.flushBuffer();
                }); //前后端分离开发
    }
    
}
```

#### 禁止再次登录

```java
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().formLogin()
                .and().crsf().disable()
                .sessionManagement() //开启会话管理
                .maximumSessions(1) //允许会话最大并发只能一个客户端
                //.expiredUrl("/login"); //会话过期处理，传统web开发处理
                .expiredSessionStrategy(event -> {
                    HttpServletResponse response = event.getResponse();
                    Map<String, Object> result = new HashMap<>();
                    result.put("status", 500);
                    result.put("msg", "当前会话以及失效，请重新登陆");
                    String s = new ObjectMapper().writeValueAsString(result);
                    response.getWriter().println(s);
                    response.flushBuffer();
                }) //前后端分离开发
                .maxSessionPreventsLogin(true); //登录之后禁止再次登录
    }
    
}
```

#### 会话共享

前面所讲的会话管理都是单机上的会话管理，如果当前是集群环境，前面所讲的会话管理方案就会失效。此时可以利用spring-session结合redis实现session共享

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-data-redis</artifactId>
</dependency>
```

```java
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    
    private final FindByIndexNameSessionRepository sessionRepository;
    
    @Autowired
    public SecurityConfig(FindByIndexNameSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().formLogin()
                .and().crsf().disable()
                .sessionManagement() //开启会话管理
                .maximumSessions(1) //允许会话最大并发只能一个客户端
                //.expiredUrl("/login"); //会话过期处理，传统web开发处理
                .expiredSessionStrategy(event -> {
                    HttpServletResponse response = event.getResponse();
                    Map<String, Object> result = new HashMap<>();
                    result.put("status", 500);
                    result.put("msg", "当前会话以及失效，请重新登陆");
                    String s = new ObjectMapper().writeValueAsString(result);
                    response.getWriter().println(s);
                    response.flushBuffer();
                })
                .sessionRegistry(sessionRegistry()) //将session交给谁管理
                .maxSessionPreventsLogin(true); //登录之后禁止再次登录
    }
    
    //创建session同步到redis中的方案
    @Bean
    public SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry(sessionRepository);
    }
}
```

### CSRF漏洞保护

CSRF（Cross-Site Request Forgery 跨站请求伪造），也可称为一键攻击，通常缩写为CSRF或者XSRF

CSRF攻击是一种挟持用户在当前已登录的浏览器上发送恶意请求的攻击方式。相对于XSS利用用户对指定网站的信任，CSRF则是利用网站对用户网页浏览器的信任。简单来说，CSRF是攻击者通过一些技术手段欺骗用户的浏览器，去访问一个用户曾经认证过的网站并执行恶意请求，例如发送邮件、发消息、甚至财产操作。由于客户已经在该网站上认证过，所以该网站会认为是真正的用户在操作而执行请求

#### CSRF防御

**令牌同步模式**

目前主流的CSRF攻击防御方案。具体的操作方式就是在每一个HTTP请求中，除了默认自动携带的Cookie参数外，再提供一个安全的、随机生成的字符串，我们称之为CSRF令牌。这个CSRF令牌是服务端生成，生成后在HttpSession中保存一份。当前端请求到达后，将请求携带的CSRF令牌信息和服务端中保存的令牌进行比对，如果两者不相等，则拒绝掉该HTTP请求

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .anyRequest().authenticated()
        .and().formLogin()
        .and().crsf() //开启CSRF防御
}
```

**前后端分离使用CSRF**

只需要将生成csrf放入到cookie中，并在请求时获取cookie中令牌信息进行提交即可

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .anyRequest().authenticated()
        .and().formLogin()
        .and().crsf()
        //将令牌保存到cookie中，允许cookie前端获取
        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
}
```

+ 请求参数中携带令牌：key:_csrf、value:"xxx"
+ 请求头中携带令牌：X-XSRF-TOKEN:value

### 跨域

#### 简介

CORS（Cross-Origin Resource Sharing）是由W3C指定的一种跨域资源共享技术标准，其目的就是为了解决前端的跨域请求。

CORS中新增了一组HTTP请求头字段，通过这些字段，服务器告诉浏览器，哪些网站通过浏览器有权限访问哪些资源。同时规定，对那些可能修改服务器数据的HTTP请求方法（如get以外的方法），浏览器必须首先使用OPTIONS方法发起一个预检请求，预检请求的目的是查看服务端是否支持即将发起的跨域请求，如果服务端允许，才发送实际的HTTP请求。在预检请求的返回中，服务器端也可以通知客户端，是否需要携带身份凭证（如Cookie、HTTP认证信息等）

**简单请求**

如果服务端支持跨域请求，那么返回的响应头中将包含如下字段：

Access-Control-Allow-Origin : http://localhost:8081

Access-Control-Allow-Origin字段用来告诉浏览器可以访问该资源的域

**非简单请求**

对于一些非简单请求，会首先发送一个预检请求。

```
OPTIONS /PUT HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Accept: */*
Access-Control-Request-Method: PUT
Origin: http://localhost:8081
Referer: http://localhost:8081/index.html
```

服务端如果允许即将发起的跨域请求，则会给出如下响应：

```
HTTP/1.1 200
Access-Control-Allow-Origin: http://localhost:8081
Access-Control-Request-Method: PUT
Access-Control-Max-Age: 3600
```

Access-Control-Max-Age表示预检请求的有效期，单位为秒，如果在有效期内发起该跨域请求，则不用再次发起预检请求。预检请求结束后，接下来就会发起一个真正的跨域请求，跨域请求和前面的简单请求跨域步骤类似

#### Spring跨域解决方案

**@CrossOrigin**

该注解可以添加在方法上，也可以添加在Controller上。当添加在Controller上时，表示Controller中的所有接口都支持跨域

@CrossOrigin注解各属性含义：

+ allowCredentials：浏览器是否应当发送凭证信息，如Cookie
+ allowedFeaders：请求被允许的请求头字段，*代表所有字段
+ exposedHeaders：哪些响应头可以作为响应的一部分暴露出来
+ maxAge：预检请求有效期，有效期内不必再次发送预检请求，默认是1800秒
+ methods：允许的请求方法，*表示允许所有方法
+ origins：允许的预，*表示所有域

**addCrossMapping**

```java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCrossMappings (CrosRegistry registry) {
        registry.addMapping("/**")
            .allowedMethods("*")
            .allowedorigins("*")
            .allowedHeaders("*")
            .allowCredentials(false)
            .exposedHeaders("")
            .maxAge(3600);
    }
}
```

**CorsFilter**

CorsFilter是Spring Web中提供的一个处理跨域的过滤器，开发者也可以通过该过滤器处理跨域

```java
@Configuration
public class WebMvcConfig {
    @Bean
    FilterRegistrationBean<CrosFilter> corsFilter() {
        FilterRegistrataionBean<CrosFilter> registrationBean = new FilterRegistrataionBean<>();
        CrosConfiguration corsConfiguration = new CrosConfiguration();
        corsConfiguration.setAllowedHeaders(Arrays.asList(""));
        corsConfiguration.setAllowedMethods(Arrays.asList(""));
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        registrationBean.setFilter(new CorsFilter(Source));
        registrationBean.setOrder(-1);
        return registrationBean;
    }
}
```

#### SpringSecurity跨域解决方案

项目添加springSecurity依赖后，发现以上三种跨域方式有的失效了。有没有失效需要看过滤器的优先级

client -> Filer（Web|Security Filter）-> DispatcherServlet -> Interceptor -> Controller

由于非简单请求都要首先发送一个预检请求，而预检请求并不会携带认证信息，，所以预检请求就有被Security拦截的可能。因此通过@CrossOrigin注解或者重写addCorsMappings方法配置跨域就会失效。如果使用CorsFilter配置的跨域，只要过滤器优先级高于SpringSecurity过滤器就不会有问题。

```java
@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().formLogin()
                .and().cors()
                .configurationSource(configurationSource())
                .and().csrf().disable();
    }
    
    public CorsConfigurationResource configurationResource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
```

### 异常处理

SpringSecurity中异常主要分为两大类：

+ AuthenticationException：认证异常
+ AccessDeniedException：授权异常

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .anyRequest().authenticated()
        //...
        .exceptionHandler()//异常处理
        .authenticationEntryPoint((request, response, e) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWritter().write("尚未认证，请进行认证操作");
        })
        .accessDeniedHandler((request, response, e) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWritter().write("无权访问");
        });
}
```

### 授权

+ 基于角色权限设计：用户 <=> 角色 <=>资源三者关系，返回就是用户的角色
+ 基于资源权限设计：用户 <=> 权限 <=>资源三者关系，返回就是用户的权限
+ 基于角色和资源权限设计：用户 <=> 角色 <=> 权限 <=>资源，返回统称为用户的权限

SpringSecurity中提供的权限管理策略主要有两种类型：

+ 基于过滤器的权限管理（FilterSecurityInterceptor）

  主要是用来拦截HTTP请求，拦截下来后，根据HTTP请求地址进行权限校验

+ 基于AOP的权限管理（MethodSecurityInterceptor）

  主要是用来处理方法级别的权限问题。当需要调用某一个方法的适合，通过AOP将操作拦截下来，然后判断用户是否具备相关的权限

#### 基于URL的权限管理

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
        .mvcMatchers("/admin").hasRole("ADMIN") //具有ADMIN角色
        .mvcMatchers("/user").hasRole("USER") //具有USER角色
        .mvcMatchers("/getInfo").hasAuthority("READ_INFO") //具有READ_INFO权限
        .anyRequest().authenticated()
        .and().formLogin()
        .and().csrf().disable();
        
}
```

**权限表达式**

| 方法                                                         | 说明                                               |
| ------------------------------------------------------------ | -------------------------------------------------- |
| hasAuthority(String authority)                               | 当前用户是否具备指定权限                           |
| hasAnyAuthority(String... authorities)                       | 当前用户是否具备指定权限中任意一个                 |
| hasRole(String role)                                         | 当前用户是否具备指定角色                           |
| hasAnyRoles(String... roles)                                 | 当前用户是否具备指定角色中任意一个                 |
| permitAll()                                                  | 放行所有请求/调用                                  |
| denyAll()                                                    | 拒绝所有请求/调用                                  |
| isAnonymous()                                                | 当前用户是否是一个匿名用户                         |
| isAuthenticated()                                            | 当前用户是否已经认证成功                           |
| isRememberMe()                                               | 当前用户是否通过RememberMe自动登录                 |
| isFullAuthenticated()                                        | 当前用户是否既不是匿名用户又不是通过RememberMe登录 |
| hasPermission(Object target, Object permission)              | 当前用户是否具备指定目标的指定权限信息             |
| hasPermission(Object targetId, String targetType, Object permission) | 当前用户是否具备指定目标的指定权限信息             |

#### 基于方法的权限管理

基于方法的权限管理主要是通过AOP实现的，SpringSecurity中通过MethodSecurityInterceptor来提供相关实现。不同于FilterSecurityInterceptor只是在请求之间进行前置处理，MethodSecurityInterceptor除了前置处理之外还可以进行后置处理。

**@EnableGlobalMethodSecurity**

该注解用来开启权限注解，用法如下：

```java
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true,securedEnabled=true,jsr250Enabled=true)
public class SecurityConfig extends WebsecurityConfigurerAdapter{}
```

+ prePostEnabled：开启SpringSecurity提供的4个权限注解，@PostAuthorize、@PostFilter、@PreAuthorize、@PreFilter
+ securedEnabled：开启SpringSecurity提供的@Secured注解支持，该注解不支持权限表达式
+ jsr250Enabled：开启JSR-250提供的注解，主要是@DenyAll、@PermitAll、@RolesAllowed同样这些注解也不支持权限表达式

| 注解           | 含义                                       |
| -------------- | ------------------------------------------ |
| @PostAuthorize | 在目标方法执行之后进行权限校验             |
| @PostFilter    | 在目标方法执行之后对方法的返回结果进行过滤 |
| @PreAuthorize  | 在目标方法执行之前进行权限校验             |
| @PreFilter     | 在目标方法执行之前对方法参数进行过滤       |
| @Secured       | 访问目标方法必须具有相应的角色             |
| @DenyAll       | 拒绝所有访问                               |
| @PermitAll     | 允许所有访问                               |
| @RolesAllowed  | 访问目标方法必须具备相应的角色             |

### OAuth2

OAuth是一个开放标准，该标准允许用户让第三方应用访问该用户在某一网站上存储的私密资源（如头像、图片、视频等），并且在这个过程中无须将用户名和密码提供给第三方应用。通过令牌token可以实现这一功能，每一个令牌授权一个特定的网站在特定的时段内允许可特定的资源。

#### 四种授权模式

+ 授权码模式：常见的第三方平台登录功能基本都是使用这种模式
+ 简化模式：不需要第三方服务端参与，直接在浏览器中向授权服务器申请令牌，如果网站是纯静态页面，则可以采用这种方式
+ 密码模式：用户把用户名/密码直接告诉客户端，客户端使用这些信息后授权服务器申请令牌，这需要用户对客户端高度信任，例如客户端应用和服务供应商是一家公司
+ 客户端模式：客户端使用自己的名义而不是用户的名义向服务提供者申请授权。严格来说，客户端模式并不能算作OAuth协议解决问题的一种解决方案，但是对于开发者来说，在一些为移动端提供的授权服务器上使用这种模式还是非常方便的

**OAuth2授权整体流程**

![](/img/SpringSecurity_5.png)

**授权码模式**

授权码模式（Authorization Code）是功能最完整、流程最严密、最安全并且使用最广泛的一种OAuth2授权模式。同时也是最复杂的一种授权模式，他的特点就是通过客户端的后台服务器，与服务提供商的认证服务器进行互动

![](/img/SpringSecurity_6.png)

具体流程：

+ A用户访问第三方应用，第三方应用通过浏览器导向认证服务器
+ B用户选择是否给予客户端授权
+ C假设用户给予授权，认证服务器将用户导向客户端事先指定的重定向URI（redirection uri），同时附上一个授权码
+ D客户端收到授权码，附上早先的重定向URI，向认证服务器申请令牌。这一步是在客户端的后台服务器上完成的，对用户不可见
+ E认证服务器核对了授权码和重定向URI，确认无误后，向客户端发送访问令牌（access token）和更新令牌（refresh token）

核心参数：

例如：https://wx.com/oauth/authorize?response_type=code&client_id=CLIENT_ID&redirect_uri=http://www.baidu.com&scope=read

| 字段          | 描述                                           |
| ------------- | ---------------------------------------------- |
| client_id     | 授权服务器注册应用后的唯一标识                 |
| repsonse_type | 必须，固定值，在授权码中必须为code             |
| redirect_uri  | 必须，通过客户端注册的重定向url                |
| scope         | 必须，令牌可以访问资源权限 read只读，all读写   |
| state         | 可选，存在原样返回客户端，用来防止CSRF跨站攻击 |

**简化模式**

简化模式（implicit grant type）不通过第三方应用程序的服务器，直接在浏览器中向认证服务器申请令牌，跳过了授权码这个步骤，因此得名。所有步骤在浏览器中完成，令牌对访问者是可见的，且客户端不需要认证。

![](/img/SpringSecurity_7.png)

具体流程：

+ A第三方应用将用户导向认证服务器
+ B用户决定是否给予客户端权限
+ C假设用户给予授权，认证服务器将用户导向客户端指定的重定向URI，并在URI的hash部分包含了访问令牌
+ D浏览器向资源服务器发出请求，其中不包括上一步收到的hash值
+ E资源服务器返回一个网页，其中包含的代码可以获取Hash值中的令牌
+ F浏览器执行上一步获得的脚本，提取出令牌
+ G浏览器将令牌发给客户端

| 字段          | 描述                                           |
| ------------- | ---------------------------------------------- |
| client_id     | 授权服务器注册应用后的唯一标识                 |
| repsonse_type | 必须，固定值，在授权码中必须为token            |
| redirect_uri  | 必须，通过客户端注册的重定向url                |
| scope         | 必须，令牌可以访问资源权限 read只读，all读写   |
| state         | 可选，存在原样返回客户端，用来防止CSRF跨站攻击 |

**密码模式**

密码模式（Resource Owner Password Credentials Grant），客户向客户端提供自己的用户名和密码。客户端使用这些信息，向服务商提供商索要授权。在这种模式中，用户必须把自己的密码给客户端，但是客户端不得存储密码。这通常在用户对客户端高度信任的情况下，比如客户端是操作系统的一部分，或者由同一个公司出品。而认证服务器只有在其他授权模式无法执行的情况下，才考虑使用这种模式。

![](/img/SpringSecurity_8.png)

具体流程：

+ A用户向客户端提供用户名和密码
+ B客户端将用户名和密码发给认证服务器，向后者请求令牌
+ C认证服务器确认无误后，向客户端提供访问令牌

**客户端模式**

客户端模式（Client Credentials Grant）指客户端以自己的名义，而不是以用户的名义，向服务提供商进行认证。严格来说，客户端模式并不属于OAuth框架所要解决的问题。在这种模式中，用户直接向客户端注册，客户端以自己的名义要求服务提供商提供服务，其实不存在授权问题

![](/img/SpringSecurity_9.png)

具体流程：

+ A客户端向认证服务器进行身份验证，并要求一个访问令牌
+ 认证服务器确认无误后，向客户端提供访问令牌

#### OAuth2标准接口

+ /oauth/authorize：授权端点
+ /oauth/token：获取令牌端点
+ /oauth/confirm_access：用户确认授权提交端点
+ /oauth/error：授权服务器错误信息端点
+ /oauth/check_token：用于资源服务访问的令牌解析端点
+ /oauth/token_key：提供公有密钥的端点，如果使用JWT令牌的话

#### SpringSecurity OAuth2





------





springSecurity本质是一个过滤器链

代码底层流程：重点看三个过滤器

+ FilterSecurityInterceptor：是一个方法级的权限过滤器，基本位于过滤器链的最底层
+ ExceptionTranslationFiler：是一个异常过滤器，用来处理在认证授权过程中抛出的异常
+ UsernamePasswordAuthenticationFilter：对/login的POST请求做拦截，校验表单中的用户名，密码

过滤器如何进行加载的？

1. 使用SpringSecurity配置过滤器

   DelegatingFilterProxy doFilter() --> initDelegate() --> FilterChainProxy --> getFilters

**UserDetailsService接口**

查询数据库用户名和密码的过程

创建类继承UsernamePasswordAuthenticationFilter，重写三个方法

创建类实现UserDetailService，编写查询数据过程，返回User对象，这个User对象是安全框架提供的对象

**PasswordEncoder接口**

数据加密接口，用于返回User对象里面密码加密

**设置登录的用户名和密码**

+ 通过配置文件

  ```yaml
  spring:
    security:
      user:
        name: atguigu
        password: atguigu
  ```

+ 通过配置类

  ```java
  @Configuration
  public class SecurityConfig extends WebSecurityConfigurerAdapter{
      
      @Override
      protected void configure(AuthenticationManagerBuilder auth) throws Exception {
          BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
          String password = passwordEncoder.encode("123");
          auth.inMemoryAuthentication().withUser("lucy").password(password).roles("admin");
      }
      
      @Bean
      PasswordEncoder password() {
          return new BCryptPasswordEncoder();
      }
  }
  ```

+ 自定义编写实现类

  1. 创建配置类，设置使用哪个userDetailsService实现类
  2. 编写实现类，返回User对象，User对象有用户名密码和操作权限

  ```java
  @Configuration
  public class SecurityConfigTest extends WebSecurityConfigurerAdapter{
      
      @Autowired
      private UserDetailsService userDetailsService;
      
      @Override
      protected void configure(AuthenticationManagerBuilder auth) throws Exception {
          auth.userDetailsService(userDetailsService).passwordEncoder(password());
      }
      
      @Bean
      PasswordEncoder password() {
          return new BCryptPasswordEncoder();
      }
      
      @Override
      protected void configure(HttpSecurity http) throws Exception {
          //用户注销，退出
          http.logout().logoutUrl("/logout").logoutSuccessUrl("/index").permitAll();
          //配置没有权限访问跳转自定义页面
          http.exceptionHandling().accessDeniedPage("/unauth.html");
          //自定义自己编写的登录页面
          http.formLogin()
              .loginPage("/login.html")  //登录页面设置
              .loginProcessingUrl("/user/login")  //登录访问路径
              .defaultSuccessUrl("/test/index").permitAll()  //登录成功之后，跳转路径
              .and().authorizeRequests()
              .antMatchers("/", "test/hello", "user/login").permitAll() //设置哪些路径可以直接访问，不需要认证
              //当前登录用户，只有具有admins权限才可以访问这个路径
              //1.hasAuthority
              .antMatchers("/test/index").hasAuthority("admins") 
              //2.hasAnyAuthority
              .antMatchers("/test/index").hasAnyAuthority("admins,manager") 
              //3.hasRole ROLE_sale
              .antMatchers("/test/index").hasRole("sale") 
              //4.hasAnyRole
              .anyRequest().authenticated()
              .and().csrf().disable(); //关闭csrf防护
      }
  }
  ```

  ```java
  @Service("userDetailsService")
  public class MyUserDetailsService implements UserDetailsService {
      
      @Override
      public UserDetails loadUserByUserName(String username) throws UsernameNotFoundException {
          QueryWrapper<User> wrapper == new QueryWrapper();
          wrapper.eq("username",username);
          Users users = userMapper.selectOne(wrapper);
          
          if(users == null) {
              throw new UsernameNotFoundException("用户名不存在");
          }
          
          List<GrantedAutority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("admins,ROLE_sale");
          return new User(users.getUsername(), new BCryptPasswordEncoder().encode(users.getPassword()), auths);
      }
  }
  ```

**基于角色或权限进行访问控制**

+ hasAuthority方法

  如果当前的主体具有指定的权限，则返回true，否则返回false

  1. 在配置类设置当前访问地址有哪些权限

     ```java
     //当前登录用户，只有具有admins权限才可以访问这个路径
     .antMatchers("/test/index").hasAuthority("admins") 
     ```

  2. 在UserDetailsService，把返回User对象设置权限

     List<GrantedAutority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("admins");

+ hasAnyAutority方法

  如果当前的主体有任何提供的角色的话，返回true

+ hasRole方法

  .antMatchers("/test/index").hasRole("sale") 

  List<GrantedAutority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList("admins,ROLE_sale");

+ hasAnyRole方法

**注解使用**

使用注解先要开启注解功能

配置类或启动类添加注解@EnableGlobalMethodSecurity(securedEnabled=true)

+ @Secured

  判断是否具有角色，另外需要注意的是这里匹配的字符串需要添加前缀ROLE_

  ```java
  @GetMapping("update")
  @Secured("ROLE_sale","ROLE_manager")
  public String update() {
      
      return "hello update";
  }
  ```

+ @PreAuthorize

  注解适合进入方法前的权限认证，@PreAuthorize可以将登录用户的roles/permissions参数传到方法中

  ```java
  @RequestMapping("/preAuthorize")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('menu:system')")
  public String preAuthorize() {
      return "preAuthorize";
  }
  ```

+ @PostAuthorize

  注解使用并不多，在方法执行后再进行权限验证，适合验证带有返回值的权限

  ```java
  @RequestMapping("/preAuthorize")
  @ResponseBody
  @PostAuthorize("hasAnyAuthority('menu:system')")
  ```

+ @PostFilter

  @PostFilter("filterObject.username == 'admin1'")

  权限验证之后对数据进行过滤，留下用户名是admin1的数据

  表达式中的filterObject引用的是方法返回值List中的某一个元素

  ```java
  @RequestMapping("getAll")
  @PreAuthorize("hasRole('ROLE_管理员')")
  @PostFilter("filterObject.username == 'admin1'")
  @ResponseBody
  public List<UserInfo> getAllUser() {
      ArrayList<UserInfo> list = new ArrayList<>();
      list.add(new UserInfo(1L, "admin1", "666"));
      list.add(new UserInfo(2L, "admin2", "888"));
      return list;
  }
  ```

+ @PreFilter

  进入控制器之前对数据进行过滤

  ```java
  @RequestMapping("getTestPreFilter")
  @PreAuthorize("hasRole('ROLE_管理员')")
  @PreFilter("filterObject.id%2 == 0")
  @ResponseBody
  ```

**用户注销**

在配置类中添加退出的配置