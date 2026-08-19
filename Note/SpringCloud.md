![](/img/SpringCloud_1.png)

![](/img/SpringCloud_3.jpg)

### SpringCloud

#### Eureka服务注册与发现

**什么是服务治理**

在传统的RPC远程调用框架中，管理每个服务与服务之间依赖关系比较复杂，管理比较复杂，所以需要使用服务治理，管理服务与服务之间的依赖关系，可以实现服务调用、负载均衡、容错等，实现服务发现与注册

**什么是服务注册与发现**

Eureka采用了CS的设计架构，Eureka Server作为服务注册功能的服务器，他是服务注册中心。而系统中的其他微服务，使用Eureka的客户端连接到Eureka Server并维持心跳连接。这样系统的维护人员就可以通过Eureka Server来监控系统中各个微服务是否正常运行

在服务注册与发现中，有一个注册中心。当服务器启动的时候，会把当前自己服务器的信息，比如服务地址通讯地址等以别名方式注册到注册中心上。另一方面（消费者|服务提供者），以该别名的方式去注册中心上获取到实际的服务通讯地址，然后再实现本地RPC调用RPC远程调用框架核心设计思想：在于注册中心，因为使用注册中心管理每个服务与服务之间的一个依赖关系（服务治理概念）。在任何RPC远程框架中，都会有一个注册中心（存放服务地址相关信息（接口地址））

![](/img/SpringCloud_2.png)

**Eureka包含两个组件：Eureka Server和Eureka Client**

Eureka Server提供服务注册服务，各个微服务节点通过配置启动后，会在EurekaServer中进行注册，这样EurekaServer中的服务注册表中将会存储所有可用服务节点的信息，服务节点的信息可以在界面中直观看到

EurekaClient通过注册中心进行访问，是一个Java客户端，用于简化EurekaServer的交互，客户端同时也具备一个内置的、使用轮询负载算法的负载均衡器。在应用启动后，将会向EurekaServer发送心跳（默认周期为30秒）。如果EurekaServer在多个心跳周期内没有接收到某个节点的心跳，EurekaServer将会从服务注册表中把这个服务节点移除（默认90秒）

**Eureka自我保护**

某时刻某一个微服务不可用了，Eureka不会立刻清理，依旧会对该微服务的信息进行保存。属于CAP里面的AP分支

#### Ribbon

负载均衡+RestTemplate调用

**核心组件IRule**

IRule接口：根据特定算法中从服务列表中选取一个要访问的服务

+ com.netflix.loadbalancer.RoundRobinRule：轮询
+ com.netflix.loadbalancer.RandomRule：随机
+ com.netflix.loadbalancer.RetryRule：先按照RoundRobinRule的策略获取服务，如果获取服务失败则在指定时间内会进行重试，获取可用的服务
+ WeightedResponseTimeRule：对RoundRobinRule的扩展，响应速度越快的实例选择权重越大，越容易被选择
+ BestAvailableRule：会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，然后选择一个并发量最小的服务
+ AvailabilityFilteringRule：先过滤故障实例，再选择并发较小的实例
+ ZoneAvoidanceRule：默认规则，复合判断server所在区域的性能和server的可用性选择服务器

**替换负载规则**

自定义配置类不能放在@ComponentScan所扫描的当前包以及子包下，负责我们自定义的这个配置类就会被所有的Ribbon客户端所共享，达不到特殊定制化的目的

**负载均衡轮询算法原理**

rest接口第几次请求数 % 服务器集群总数量 = 实际调用服务器位置下标，每次服务重启动后rest接口计数从1开始

#### OpenFeign

feign是一个声明式的web服务客户端，让编写web服务客户端变得非常容易，只需要创建一个接口并在接口上添加注解即可

feign旨在使编写Java Http客户端变得更容易。前面在使用Ribbon+RestTemplate时，利用RestTemplate对http请求的封装处理，形成了一套模板化的调用方法。但是在实际开发中，由于对服务的依赖调用可能不止一处，往往一个接口会被多处调用，所以通常都会针对每个微服务自行封装一些客户端类来包装这些依赖服务的调用。所以Feign在此基础上进一步封装，由他来帮助我们定义和实现依赖服务接口的定义。在feign的实现下，我们只需创建一个接口并使用注解的方式配置它

#### Hystrix断路器

Hystrix是一个用于分布式系统的延迟和容错的开源库，在分布式系统里，许多依赖不可避免的会调用失败，比如超时、异常等，Hystrix能够保证在一个依赖出问题的情况下，不会导致整体服务失败，避免级联故障，以提高分布式系统的弹性

断路器是一种开关装置，当某个服务单元发生故障之后，通过断路器的故障监控（类似熔断保险丝），向调用方返回一个符合预期的、可处理的备选响应（fallback），而不是长时间的等待或者抛出调用方无法处理的异常，这样就保证了服务调用方的线程不会被长时间、不必要地占用，从而避免了故障在分布式系统中的蔓延，乃至雪崩。

**服务降级**

服务器忙，请稍后再试，不让客户端等待并立刻返回一个友好提示，fallback

触发降级情况：程序运行异常，超时，服务熔断触发服务降级，线程池/信号量打满也会导致服务降级

配置：@HystrixCommand，主启动类@EnableCircuitBreaker

**服务熔断**

类比保险丝达到最大服务访问后，直接拒绝访问，然后调用服务降级的方法并返回友好提示

熔断机制是应对雪崩效应的一种微服务链路保护机制。当扇出的链路的某个微服务出错不可用或者响应时间太长时，会进行服务的降级，进而熔断该节点微服务的调用，快速返回错误的响应信息。当检测到该节点微服务调用响应正常后，恢复调用链路

**服务限流**

秒杀高并发等操作，严禁一窝蜂的过来拥挤，大家排队，一秒钟N个，有序进行

#### Gateway

三大核心概念

+ Route（路由）：路由是构建网关的基本模块，它由ID，目标URI，一系列的断言和过滤器组成，如果断言为true则匹配该路由
+ Predicate（断言）：开发人员可以匹配HTTP请求中的所有内容（例如请求头或请求参数），如果请求与断言相匹配则进行路由
+ Filter（过滤）：指的是Spring框架中GatewayFilter的实例，使用过滤器，可以在请求被路由前或后对请求进行修改

#### SpringCloud Config

为微服务架构中的微服务提供集中化的外部配置支持，配置服务器为各个不同微服务应用的所有环境提供了一个中心化的外部配置

### SpringCloud Alibaba

### Seata

seata事务管理中有3个重要的角色：

+ TC（Transaction Coordinator）事务协调者：维护全局和分支事务的状态，协调全局事务提交和回滚
+ TM（Transaction Manager）事务管理器：定义全局事务的范围、开始全局事务、提交或回滚全局事务
+ RM（Resource Manager）资源管理器：管理分支事务处理的资源，与TC交谈以注册分支事务和报告分支事务的状态，并驱动分支事务提交或回滚

seata提供了四种不同的分布式事务解决方案：

+ XA模式：强一致性分阶段事务模式，牺牲了一定的可用性，无业务侵入
+ TCC模式：最终一致性的分阶段事务模式，有业务侵入
+ AT模式：最终一致性的分阶段事务模式，无业务侵入，也是seata默认的模式
+ SAGA模式：长事务模式，有业务侵入

![](/img/seata_7.png)

#### XA模式

XA规范是X/Open组织定义的分布式事务处理（DTP，Distributed Transaction Processing）标准，XA规范描述了全局的TM与局部的RM之间的接口，几乎所有主流的数据库都对XA规范提供了支持

![](/img/seata_1.png)

![](/img/seata_2.png)

**seata的XA模式**

seata的XA模式做了一些调整，但大体相似

![](/img/seata_3.png)

RM一阶段的工作：

+ 注册分支事务到TC
+ 执行分支业务sql但不提交
+ 报告执行状态到TC

TC二阶段的工作

+ TC检测各分支事务执行状态
  + 如果都成功，通知所有RM提交事务
  + 如果有失败，通知所有RM回滚事务

RM二阶段的工作

+ 接收TC指令，提交或回滚事务

XA模式优点：

+ 事务强一致性，满足ACID原则
+ 常用的数据库都支持，实现简单，并且没有代码侵入

XA模式缺点：

+ 因为一阶段需要锁定数据库资源，等待二阶段结束才释放，性能较差
+ 依赖关系型数据库实现事务

**实现XA模式**

1. 修改application.yml文件（每个参与事务的微服务），开启XA模式

   ```yaml
   seata:
     data-source-proxy-mode: XA # 开启数据源代理的XA模式
   ```

2. 给发起全局事务的入口方法添加@GlobalTransactional注解，本例中是OrderServiceImpl中的create方法

   ```java
   @GlobalTransactional
   public Long create(Order order) {
       // 创建订单
       orderMapper.insert(order);
       // 扣余额
       // 扣减库存
       return order.getId();
   }
   ```

3. 重启服务并测试

#### AT模式

AT模式同样是分阶段提交的事务模型，不过弥补了XA模型中资源锁定周期过长的缺陷

![](/img/seata_4.png)

阶段一RM的工作：

+ 注册分支事务
+ 记录undo-log（数据快照）
+ 执行业务sql并提交
+ 报告事务状态

阶段二提交时RM的工作：

+ 删除undo-log即可

阶段二回滚时RM的工作

+ 根据undo-log恢复数据到更新前

AT模式和XA模式区别：

+ XA模式一阶段不提交事务，锁定资源；AT模式一阶段直接提交，不锁定资源
+ XA模式依赖数据库机制实现回滚；AT模式利用数据快照实现数据回滚
+ XA模式强一致性；AT模式最终一致性

AT模式的优点：

+ 一阶段完成直接提交事务，释放数据库资源，性能比较好
+ 利用全局锁实现读写隔离
+ 没有代码侵入，框架自动完成回滚和提交

AT模式的缺点：

+ 两阶段之间属于软状态，属于最终一致
+ 框架的快照功能会影响性能，但比XA模型要好很多

**实现AT模式**

AT模式中的快照生成、回滚等动作都是由框架自动完成，没有任何代码侵入，因此实现非常简单

1. 导入seata-at.sql，其中lock_table导入到TC服务关联的数据库，undo_log表导入到微服务相关的数据库

2. 修改application.yml文件，将事务模式修改为AT模式即可

   ```yaml
   seata:
     data-source-proxy-mode: AT # 开启数据源代理的AT模式
   ```

3. 重启服务并测试

#### TCC模式

TCC模式与AT模式非常相似，每阶段都是独立事务，不同的是TCC通过人工编码来实现数据恢复。需要实现三个方法：

+ Try：资源的检测和预留
+ Confirm：完成资源操作业务；要求Try成功Confirm一定要能成功
+ Cancel：预留资源释放，可以理解为try的反向操作

![](/img/seata_5.png)

TCC的优点：

+ 一阶段完成直接提交事务，释放数据库资源，性能好
+ 相比AT模型，无需生成快照，无需使用全局锁，性能最强
+ 不依赖数据库事务，而是依赖补偿操作，可以用于非事务型数据库

TCC的缺点：

+ 有代码侵入，需要人为编写try、Confirm和Cancel接口，太麻烦
+ 软状态，事务是最终一致性
+ 需要考虑Confirm和Cancel的失败情况，做好幂等处理

**TCC的空回滚和业务悬挂**

当某分支事务的try阶段阻塞时，可能导致全局事务超时而触发二阶段的cancel操作。在未执行try操作时先执行了cancel操作，这时cancel不能做回滚，就是空回滚

对于已经空回滚的业务，如果以后继续执行try，就永远不可能confirm或cancel，这就是业务悬挂。应当阻止执行空回滚后的try操作，避免悬挂

![](/img/seata_6.png)。

**实现TCC模式**

TCC的Try、Confirm、Cancel方法都需要在接口中基于注解来声明，语法如下

```java
@LocalTCC
public interface TCCService {
    // Try逻辑，@TwoPhaseBusinessAction中的name属性要与当前方法名一致，用于指定Try逻辑对应的方法
    @TwoPhaseBusinessAction(name = "prepare", commitMethod = "confirm", rollbackMethod = "cancel")
    void prepare(@BusinessActionContextParameter(paramName = "param") String param);
    
    // 二阶段confirm确认方法，可以另命名，但要保证与commitMethod一致
    // context上下文，可以传递try方法的参数
    boolean confirm(BusinessActionContext context);
    
    // 二阶段回滚方法，要保证与rollbackMethod一致
    boolean cancel(BusinessActionContext context);
}
```

#### Saga模式

Saga模式是Seata提供的长事务解决方案。也分为两个阶段：

+ 一阶段：直接提交本地事务
+ 二阶段：成功则什么都不做；失败则通过编写补偿业务来回滚

优点：

+ 事务参与者可以基于事件驱动实现异步调用，吞吐高
+ 一阶段直接提交事务，无锁，性能好
+ 不用编写TCC中的三个阶段，实现简单

缺点：

+ 软状态持续时间不确定，时效性差
+ 没有锁，没有事务隔离，会有脏写

