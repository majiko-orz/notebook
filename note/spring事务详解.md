# spring事务详解

## 事务的概念

 我们知道，在JavaEE的开发过程中，service方法用于处理主要的业务逻辑，而业务逻辑的处理往往伴随着对数据库的多个操作。以我们生活中常见的转账为例，service方法要实现将A账户转账到B账户的功能，则该方法内必定要有两个操作：先将A账户的金额减去要转账的数目，然后将B账户加上相应的金额数目。这两个操作必定要全部成功，方才表示本次转账成功；若有任何一方失败，则另一方必须回滚（即全部失败）。事务指的就是这样一组操作：这组操作是不可分割的，要么全部成功，要么全部失败 

## 事务的特性

事物具有**ACID四个特性**：

**原子性（Atomicity）**:事务是一个不可分割的工作单位，事务中的操作要么都发生，要么都不发生

**一致性（Consistency）:**事务在完成后的数据的完整性必须保持一致

**隔离性（Isolation）：**多个用户并发访问数据库时，一个用户的事务不能被其他事务干扰，多个并发事务之间的数据要相互隔离

**持久性（Durability）:**一个事务一旦提交，他对数据库中数据的改变应该是永久性的，即使数据库发生故障也不应该对其有任何影响

## spring事务管理接口

 Spring 事务管理为我们提供了三个高层抽象的接口，分别是PlatformTransactionManager，TransactionDefinition，TransactionStatus 

### 1. PlatformTransactionManager事务管理器

 PlatformTransactionManager 用于执行具体的事务操作。接口定义 如下：

```java
Public interface PlatformTransactionManager{
  TransactionStatus getTransaction(TransactionDefinition definition)
   throws TransactionException;
   void commit(TransactionStatus status)throws TransactionException;
   void rollback(TransactionStatus status)throws TransactionException;
}
```

 Spring事务管理器的接口是org.springframework.transaction.PlatformTransactionManager，Spring框架并不直接管理事务，而是通过这个接口为不同的持久层框架提供了不同的PlatformTransactionManager接口实现类，也就是将事务管理的职责委托给Hibernate或者iBatis等持久化框架的事务来实现 

```
org.springframework.jdbc.datasource.DataSourceTransactionManager：使用JDBC或者iBatis进行持久化数据时使用
org.springframework.orm.hibernate5.HibernateTransactionManager：使用hibernate5版本进行持久化数据时使用
org.springframework.orm.jpa.JpaTransactionManager：使用JPA进行持久化数据时使用
org.springframework.jdo.JdoTransactionManager：当持久化机制是jdo时使用
org.springframework.transaction.jta.JtaTransactionManager：使用一个JTA实现来管理事务，在一个事务跨越多个资源时必须使用
```

### 2. TransactionDefinition定义事务基本属性

 org.springframework.transaction.TransactionDefinition接口用于定义一个事务，它定义了Spring事务管理的五大属性：**隔离级别**、**传播行为**、**是否只读**、**事务超时**、**回滚规则** 

 Spring 为我们提供了一个默认的实现类：DefaultTransactionDefinition，该类适用于大多数情况。如果该类不能满足需求，可以通过实现 TransactionDefinition 接口来实现自己的事务定义。 

#### 2.1 隔离级别

 隔离级别就是用来描述并发事务之间隔离程度的大小，在并发事务之间如果不考虑隔离性，会引发如下安全性问题： 

**脏读：**一个事务读到了另一个事务未提交的数据

**不可重复读：**一个事务读到了另一个事务已经提交的update的数据导致多次查询结果不一致，侧重于数据修改

**幻读：**一个事务读到了另一个事务已经提交的insert的数据导致多次查询结果不一致，侧重于记录数的改变

在spring事务管理中，为我们定义了如下的**隔离级别**：

**ISOLATION_DEFAULT:**使用数据库默认的隔离级别

**ISOLATION_READ_UNCOMMITTED:**最低的隔离级别，允许读取已改变而没有提交的数据，可能会导致脏读、幻读、不可重复读

**ISOLATION_COMMITTED:**允许读取事务已经提交的数据，可以组织脏读，但是幻读、不可重复读仍有可能发生

**ISOLATION_REPEATABLE_READ:**对同一字段的多次读取的结果都是一致的，除非数据事务本身改变，可以阻止脏读、不可重复读，但幻读仍有可能发生

**ISOLATION_SERIALIZABLE:**最高的隔离级别，完全服从ACID的隔离级别，确保不发生脏读、不可重复读、幻读，也是最慢的事务隔离级别，因为它通常是通过完全锁定事务相关数据库表来实现的

#### 2.2 传播行为

 Spring事务传播机制规定了事务方法和事务方法发生嵌套调用时事务如何进行传播，即协调已经有事务标识的方法之间的发生调用时的事务上下文的规则
Spring定义了七种传播行为，这里以方法A和方法B发生嵌套调用时如何传播事务为例说明： 

PROPAGATION_REQUIRED:A如果有事务，B将使用该事务；如果A没有事务，B将创建一个新事务

PROPAGATION_SUPPORTS:A如果有事务，B将使用该事务；如果A没有事务，B将以非事务运行

PROPAGATION_MANDATORY:A如果有事务，B将使用该事务，如果A没有事务，B将抛异常

PROPAGATION_REQUIRES_NEW:A如果有事务，将A的事务挂起，B创建一个新的事务；如果A没有事务，B将创建一个新的事务

PROPAGATION_NOT_SUPPORTED:A如果有事务，将A的事务挂起，B将以非事务执行；如果A没有事务，B将以非事务执行

PROPAGATION_NEVER:A如果有事务，B将抛异常；A如果没有事务，B将以非事务执行

PROPAGATION_NESTED:A和B底层采用保存点机制，形成嵌套事务

#### 2.3 是否只读

如果将事务设置为只读，表示这个事务只读取数据但不更新数据，这样可以帮助数据库引擎优化事务

#### 2.4 事务超时

 事务超时就是事务的一个定时器，在特定时间内事务如果没有执行完毕，那么就会自动回滚，而不是一直等待其结束。在 TransactionDefinition 中以 int 的值来表示超时时间，默认值是-1，其单位是秒 

#### 2.5 回滚规则

 回滚规则定义了哪些异常会导致事务回滚而哪些不会。默认情况下，事务只有遇到运行期异常时才会回滚 

### 3. TransactionStatus事务状态

 org.springframework.transaction.TransactionStatus接口用来记录事务的状态，该接口定义了一组方法，用来获取或判断事务的相应状态信息
TransactionStatus接口源码： 

```java
public interface TransactionStatus extends SavepointManager, Flushable {
    boolean isNewTransaction();// 是否是新的事物

    boolean hasSavepoint();// 是否有恢复点

    void setRollbackOnly();// 设置为只回滚

    boolean isRollbackOnly();// 是否为只回滚

    void flush();// 刷新

    boolean isCompleted();// 是否已完成
}
```

## Spring 事务管理实现方式

Spring 事务管理有两种方式：**编程式事务管理**、**声明式事务管理**
编程式事务管理通过TransactionTemplate手动管理事务，在实际应用中很少使用，我们来重点学习声明式事务管理
声明式事务管理有三种实现方式：**基于TransactionProxyFactoryBean的方式**、**基于AspectJ的XML方式**、**基于注解的方式**

**数据库表**

```sql
create table account
(
  id    bigint auto_increment primary key,
  name  varchar(32) not null,
  money bigint      not null,
  constraint account_name_uindex
  unique (name)
);
insert into account (name, money) values('Bill', 2000),('Jack', 2000);
```

原始数据：

 ![数据库.JPG](https://upload-images.jianshu.io/upload_images/13172436-4428b7019f33a636.JPG?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

**DAO实现类**

```java
public class TransferDaoImpl extends JdbcDaoSupport implements TransferDao {

    /**
     * @param name 账户名称
     * @param amount 支出金额
     */
    @Override
    public void payMoney(String name, Long amount) {

        String sql = "update account set money=money-? where name=?";
        this.getJdbcTemplate().update(sql, amount, name);
    }

    /**
     * @param name 账户名称
     * @param amount 收入金额
     */
    @Override
    public void collectMoney(String name, Long amount) {

        String sql = "update account set money=money+? where name=?";
        this.getJdbcTemplate().update(sql, amount, name);
    }
}
```

**Service实现类（事务管理类）**

```java
public class TransferServiceImpl implements TransferService {

    private TransferDao transferDao;

    public void setTransferDao(TransferDao transferDao) {
        this.transferDao = transferDao;
    }
     /**
     * @param source 支出方账户名称
     * @param name 收入方账户名称
     * @param amount 转账金额
     */
    @Override
    public void transferMoney(String source, String destination, Long amount) {
        transferDao.payMoney(source, amount);
        int i = 100/0;//此处用于测试抛异常时是否会回滚
        transferDao.collectMoney(destination, amount);
    }
}
```

**Spring核心配置文件**

```xml
  <!-- 读取db.properties配置信息 -->
    <context:property-placeholder location="db.properties"></context:property-placeholder>
    <!-- 配置c3p0数据源 -->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="${db.driverClass}" />
        <property name="jdbcUrl" value="${db.url}" />
        <property name="user" value="${db.username}" />
        <property name="password" value="${db.password}" />
    </bean>

    <bean id="transferDao" class="com.tx.dao.impl.TransferDaoImpl">
        <property name="dataSource" ref="dataSource" />
    </bean>

    <bean id="transferService" class="com.tx.service.impl.TransferServiceImpl">
        <property name="transferDao" ref="transferDao" />
    </bean>
```



### 1. 基于TransactionProxyFactoryBean的方式

 Spring 早期推荐的声明式事务管理方式，但是在 Spring 2.0 中已经不推荐了。 

在spring核心配置文件中添加事务管理器的配置和TransactionProxyFactoryBean代理对象

```xml
 <!--配置事务管理器-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource" />
    </bean>

    <!--配置业务层的代理-->
    <bean id="transferServiceProxy" class="org.springframework.transaction.interceptor.TransactionProxyFactoryBean">
        <!--配置目标对象-->
        <property name="target" ref="transferService" />
        <!--注入事务管理器-->
        <property name="transactionManager" ref="transactionManager" />
        <!--注入事务属性-->
        <property name="transactionAttributes">
            <props>
                <!--
                    prop的格式：
                        * PROPAGATION :事务的传播行为
                        * ISOLATION :事务的隔离级别
                        * readOnly :是否只读
                        * -Exception :发生哪些异常回滚事务
                        * +Exception :发生哪些异常不回滚事务
                -->
                <prop key="transfer*">PROPAGATION_REQUIRED</prop>
            </props>
        </property>
    </bean>
```

测试代码：

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringTransactionApplicationTests {

    @Autowired
    TransferService transferService;

    @Resource(name="transferServiceProxy")
    TransferService transferServiceProxy;

    @Test
    public void contextLoads() {
        //注意，此处引入的是代理对象transferServiceProxy，而不是transferService
        transferServiceProxy.transferMoney("Bill","Jack", 200L);
    }
}
```

 运行结果： 

```java
java.lang.ArithmeticException: / by zero

	at com.tx.service.impl.TransferServiceImpl.transferMoney(TransferServiceImpl.java:22)
	at com.tx.service.impl.TransferServiceImpl$$FastClassBySpringCGLIB$$5196ddf2.invoke(<generated>)
```

 执行service事务方法时抛出异常，事务回滚，数据库中数据未发生改变 

 ![数据库.JPG](https://upload-images.jianshu.io/upload_images/13172436-1867090c728aaa3e.JPG?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

### 2. 基于AspectJ的xml方式

 在spring核心配置文件中添加事务管理器的配置、事务的增强以及切面 

```xml
   <!--配置事务管理器-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource" />
    </bean>

   <!--配置事务的通知-->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <tx:attributes>
            <tx:method name="transfer*" propagation="REQUIRED" />
        </tx:attributes>
    </tx:advice>

    <!--配置切面-->
    <aop:config>
        <aop:pointcut id="pointcut1" expression="execution(* com.tx.service.impl.*ServiceImpl.*(..))" />
        <aop:advisor advice-ref="txAdvice" pointcut-ref="pointcut1" />
    </aop:config>
```

测试代码：

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringTransactionApplicationTests {

    @Autowired
    TransferService transferService;

    @Test
    public void contextLoads() {
        transferService.transferMoney("Bill","Jack", 200L);
    }
}
```

 运行结果： 

```
java.lang.ArithmeticException: / by zero

	at com.tx.service.impl.TransferServiceImpl.transferMoney(TransferServiceImpl.java:22)
	at com.tx.service.impl.TransferServiceImpl$$FastClassBySpringCGLIB$$5196ddf2.invoke(<generated>)
```

 执行service事务方法时抛出异常，事务回滚，数据库中数据未发生改变 

 ![数据库.JPG](https://upload-images.jianshu.io/upload_images/13172436-1867090c728aaa3e.JPG?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240) 

### 3. 基于@Transactional注解的方式

 除了基于命名空间的事务配置方式，Spring 2.x 还引入了基于 Annotation 的方式，具体主要涉及@Transactional 标注。@Transactional 可以作用于接口、接口方法、类以及类方法上。当作用于类上时，该类的所有 public 方法将都具有该类型的事务属性，同时，我们也可以在方法级别使用该标注来覆盖类级别的定义。 

 在spring核心配置文件中添加事务管理器的配置和开启事务注解 

```xml
<!--配置事务管理器-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource" />
    </bean>

    <!--开启事务注解-->
    <tx:annotation-driven transaction-manager="transactionManager" />
```

 在事务方法中添加@Transaction注解 

```java
 @Transactional
    public void transferMoney(String source, String destination, Long amount) {

        transferDao.payMoney(source, amount);
        int i = 100/0;
        transferDao.collectMoney(destination, amount);
    }
```

##### 总结

在声明式事务管理的三种实现方式中，基于TransactionProxyFactoryBean的方式需要为每个进行事务管理的类配置一个TransactionProxyFactoryBean对象进行增强，所以开发中很少使用；基于AspectJ的XML方式一旦在XML文件中配置好后，不需要修改源代码，所以开发中经常使用；基于注解的方式开发较为简单，配置好后只需要在事务类上或方法上添加@Transaction注解即可，所以开发中也经常使用