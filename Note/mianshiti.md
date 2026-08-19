### Java

#### new一个对象的过程

1. 类加载检查
2. 分配内存
3. 初始化零值
4. 设置对象头
5. 执行构造方法

### RocketMQ

#### RocketMQ会不会重复消费？如何避免？如何做到幂等性？

![](/img/m_1.png)

#### RocketMQ如何保证消息的顺序性？

+ 生产者：单一生产者、串行发送
+ 消息队列：单一队列（全局有序），同一生产者组（局部有序）通过MessageQueueSelector保证消息在同一组
+ 消费者：使用MessageListenerOrder监听器

#### RocketMQ如何解决消息丢失的问题？

+ 生产者：同步发送、异步+重试+补偿
+ 消息队列：同步刷盘（broker.conf配置flushDiskType=SYNC_FLUSH），主从采用同步复制（主节点broker.conf配置brokerRole=SYNC_MASTER）
+ 消费者：重试+幂等性+死信补偿、消费者组减轻单节点负载（多个消费者消费一个Topic，每一个消费者对应一个MessageQueue）

#### RocketMQ有哪些使用场景？

+ 异步、削峰、解耦

+ 分布式事务

  ![](/img/m_2.png)

+ 延迟任务

  ![](/img/m_3.png)

#### 如何处理RocketMQ消息积压的问题？

+ 消费者代码性能低的话，优化代码
+ 增加queue，增加消费者

### SpringBoot

#### SpringBoot如何保证接口幂等性

1. **Token机制（一次性令牌）**

   客户端在调用接口前先申请一个token（通常由服务端生成并存储），服务端在第一次收到请求时校验token，校验成功后删除token，执行操作，后续重复请求会因token无效而拒绝

2. **唯一索引**（数据库层面）

   利用数据库的唯一索引防止重复数据，适用于插入操作，比如订单号唯一

3. **乐观锁**

   在数据表中增加版本号字段，更新时带上版本号条件（如`update table set value=new_value, version=version+1 where id=xxx and version=old_version`）

4. **状态机**

   业务状态流转有固定顺序（如订单状态：创建->支付->完成），更新状态时校验当前状态是否允许流转到目标状态

5. **分布式锁**

   在操作前获取分布式锁（如Redis的setnx命令），执行操作后释放锁，保证同一时间只有一个请求能执行操作

#### 如何设计一个高并发系统

+ 系统拆分
+ 缓存加速
+ MQ异步/削峰
+ 数据分离
+ 读写分离
+ 服务监控

#### Spring中事务失效场景

1. 异常被捕获但未抛出
2. 抛出检查异常，spring默认只在遇到runtimeException和error时回滚，可以通过rollbackFor配置
3. 方法非public修饰
4. 方法被final或static修饰
5. 同类方法自调用，通过`AopContext.currentProxy()`获取代理对象调用（需启用`exposeProxy`）解决

#### Spring中bean的生命周期

1. 通过BeanDefinition获取bean的定义信息
2. 调用构造函数实例化bean
3. bean的依赖注入
4. 处理Aware接口（BeanNameAware、BeanFactoryAware、ApplicationcontextAware）
5. bean的后置处理器BeanPostProcessor-前置
6. 初始化方法（initializingBean、init-method）
7. bean的后置处理器BeanPostProcessor-后置

#### Spring事务传播机制

1. REQUIRE：默认，如果当前存在事务就加入事务，如果当前没有事务就新建一个事务
2. REQUIRES_NEW：无论当前是否存在事务都新建一个事务
3. SUPPORTS：如果当前存在事务就加入事务，当前没有事务就以非事务运行
4. NOT_SUPPORTED：以非事务运行
5. MANDATORY：如果当前存在事务就加入，如果当前没有事务抛出异常
6. NEVER：如果当前存在事务就抛出异常，如果当前没有事务就以非事务运行
7. NESTED：如果当前存在事务就创建嵌套事务，如果当前没有事务新建事务

#### Springboot缓存预热

1. 使用ApplicationListener 监听 ContextRefreshedEvent 或 ApplicationReadyEvent 等应用上下文初始化完成事件，在这些事件触发后执行数据加载到缓存的操作
2. 使用@PostConstruct注解实现
3. 使用CommandLineRunner或ApplicationRunner接口
4. 实现 InitializingBean 接口并重写 afterPropertiesSet 方法，可以在 Spring Bean 初始化完成后执行缓存预热
5. @Async异步预热

#### 如何保证分布式事务一致性

强一致性：两阶段提交（2PC）

最终一致：Seata AT模式、TCC、SAGA、本地消息表、RocketMQ事务消息

#### 常用负载均衡算法

+ 轮询（默认）：将请求**按顺序**依次分配给后端服务器
+ 加权轮询：权重值越高，被分配到的请求就越多
+ 最少连接：将新请求分配给当前活跃连接数最少的服务器
+ IP哈希：根据客户端IP地址计算哈希值，确保同一客户端的请求始终被定向到同一台后端服务器
+ 通用哈希：根据用户指定的变量（如 `$request_uri`, `$cookie_sessionid`）计算哈希值来分配请求，支持一致性哈希
+ 随机算法：在加权随机选择的两台服务器中，挑出连接数更少的那一台
+ 最少时间：综合考量服务器的平均响应时间和活跃连接数，选择性能最优的节点。可选择基于收到响应首字节（`header`）或完整响应（`last_byte`）的时间来计算

### Redis

#### 缓存穿透

1. 缓存空数据
2. 布隆过滤器

#### 缓存击穿

1. 互斥锁，强一致，性能差
2. 逻辑过期，不设置过期时间，高可用，性能优，不能保证数据绝对一致

#### 缓存雪崩

1. 给不同的key的TTL添加随机值
2. 利用redis集群提高服务可用性
3. 给缓存业务添加降级限流策略
4. 给业务增加多级缓存

#### 缓存与数据库一致性如何解决

以下这三种在实际工作中不建议使用，存在比较大的数据不一致隐患：

- 先写缓存再写数据库
- 先写数据库再写缓存
- 先删除缓存再写数据库

可以根据业务场景选择下述缓存一致性方案：

- 延迟双删：先删除缓存，再更新数据库，等待一段时间再删除缓存
- MQ异步通知：如果公司现有消息队列中间件，可以考虑使用该方案，反之则不需要考虑。
- 先写数据库再删缓存：这种方案从实时性以及技术实现复杂度来说都比较不错，推荐大家使用这种方案。
- Binlog 异步更新缓存：如果希望实现最终一致性以及数据多中心模式，该方案无疑是最合适的。
- 分布式锁

#### 分布式锁实现原理

redis实现分布式锁主要是利用setnx命令

加锁成功后会开启Watch dog看门狗，默认30秒过期，每隔10s（releaseTime / 3）的时间做一次续期

抢不到锁的线程会不断循环尝试获取锁，加锁、设置过期时间等操作都是基于lua脚本完成

redisson实现的分布式锁是可重入的，利用hash结构记录线程id和重入次数

RedLock：不能只在一个redis实例上创建锁，应该在多个redis实例上创建锁（n / 2 + 1），避免在一个redis实例上加锁

### MySQL

#### 索引失效

1. 联合索引不满足最左匹配原则
2. 范围查询右边的列，不能使用索引
3. 运算、函数、隐式类型转换
4. like查询以%开头
5. or连接非索引列
6. 不等于导致索引失效
7. 使用IS NULL 或 IS NOT NULL

#### 深分页

1. 索引覆盖：建立联合索引，不用回表，但业务需要select *就会失效
2. 子查询延迟关联：内层子查询先查id
3. 标签记录法/游标查询：记录上一页最后一条记录的id，然后查询大于他的数据，但不支持跳页

### 场景题

