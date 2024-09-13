### JDBC

JDBC(Java Database Connectivity)是sun公司提供一套用于数据库操作的接口，Java程序员只需要面向这套接口编程即可。不同的数据库厂商，需要针对这套接口，提供不同的实现。不同的实现的集合，即为不同数据库的驱动。

![](/img/jdbc_1.png)

**JDBC编写步骤**

![](/img/jdbc_2.png)

#### JDBC操作流程

```java
//读取配置文件信息
InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
Properties pros = new Properties();
pros.load(is);
String user = pros.getProperty("user");
String password = pros.getProperty("password");
String url = pros.getProperty("url");
String driverClass = pros.getProperty("driverClass");

//1.加载jdbc驱动
Class.forName("com.mysql.cj.jdbc.Driver");

//2.建立连接
Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/数据库名称","用户名称","密码");

//3.发送sql语句
//例如：插入请求
String sql="select id,username,pwd from t_user where id>?";
PrepareStatement ps=conn.prepareStatement(sql);
ps.setObject(1, 2); //第一个"问号",传入2. -->把id大于2的记录都取出来
rs=ps.executeQuery();

//4.返回查询结果
ResultSet rs=ps.executeQuery();  //执行查询请求，并返回"结果集"

//5.关闭连接
//关闭连接时 顺序为:resultset-->preparestatement-->connection 这样的关闭顺序
if(rs!=null){ //RsultSet rs
   rs.close();
}
if(ps!=null){ //PreparedStatement ps
   ps.close();
}
if(conn!=null){ //connection conn
   conn.close();
}
```

#### 使用PreparedStatement实现CRUD操作

在java.sql中有3个接口分别定义了对数据库的调用的不同方式

+ Statement：用于执行静态SLQ语句并返回它所生成结果的对象
+ PreparedStatement：SQL语句被预编译并存储在此对象中，可以使用此对象多次高效地执行该语句
+ CallableStatement：用于执行SQL存储过程

**使用Statement操作数据表的弊端**

+ 存在拼串操作，繁琐
+ 存在SQL注入问题

**PreparedStatement的使用**

 ```java
//增删改
String sql = "insert into customers(name,email,birth)values(?,?,?)";
PreparedStatement ps = conn.prepareStatement(sql);
ps.setString(1,"哪吒");
ps.setString(2,"nezha@gmail.com");
ps.setDate(3,new Date(32235235325L));

ps.excute();
ps.close();
conn.close();

//查询
String sql = "select id,name,email,birth from customers where id = ?";
PreparedStatement ps = conn.prepareStatement(sql);
ps.setLong(1,1);

ResultSet resultSet = ps.executeQuery();
if(resultSet.next()) {//判断结果集下一条是否有数据，如果有返回true并指针下移
    int id = resultSet.getInt(1);
    String name = resultSet.getString(2);
    String email = resultSet.getString(3);
    Date birth = resultSet.getString(4);
}
 ```

**PreparedStatement好处**

+ 不需要拼串
+ 预编译解决sql注入
+ 可以操作Blob数据
+ 可以实现更高效的批量操作

#### 数据库连接池

数据库连接池基本思想：为数据库连接建立一个缓冲池，预先在缓冲池中放入一定数量的连接，当需要建立数据库连接时，只需要从缓冲池中取出一个，使用完毕以后再放回去

数据库连接池负责分配、管理和释放数据库连接，它允许应用程序反复使用一个现有的数据库连接，而不是重新建立一个

数据库连接池在初始化时将创建一定数量的数据库连接放到连接池中，这些数据库连接的数量是由最小数据库连接数来设定的。无论这些数据库连接是否被使用，连接池都将一直保证至少拥有这么多连接数量。连接池的最大数据库连接数量限定了这个连接池能占有的最大连接数，当应用程序向连接池请求的连接超过最大连接数量时，这些请求将被加入到等待队列中。

**多种开源数据库连接池**

+ JDBC的数据库连接池使用javax.sql.DataSource来表示，DataSource只是一个接口，该接口通常由服务器（Weblogic，WebSphere，Tomcat）提供实现，又有一些开源组织提供实现：
  + DBCP是Apache提供的数据库连接池。tomcat服务器自带dbcp数据库连接池。速度相对c3p0较快，但因自身存在BUG，Hibernate3已不再提供支持
  + C3P0是一个开源组织提供的一个数据库连接池，速度相对较慢，稳定性还可以，hibernate官方推荐使用
  + Proxool是sourceforge下的一个开源项目数据库连接池，有监控连接池状态的功能，稳定性较c3p0差一点
  + BoneCP是一个开源组织提供的数据库连接池，速度快
  + Druid是阿里提供的数据库连接池，据说是集DBCP、C3P0、Proxool优点于一身的数据库连接池，但是速度不确定是否有BoneCP快
+ DataSource通常被称为数据源，它包含连接池和连接池管理两个部分，习惯上也经常把DataSource称为连接池
+ DataSource用来取代DriverManager来获取Connection，获取速度快，同时可以大幅度提高数据库访问速度

### mybatis

https://mybatis.org/mybatis-3/index.html

#### 配置

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
	PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
	"http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--设置-->
    <settings>
        <setting name="" value=""/>
    </settings>
    <!--别名-->
    <typeAliases>
    	<typeAlias alias="" type=""/>
        <!--每一个在包 domain.blog 中的 Java Bean，在没有注解的情况下，会使用 Bean 的首字母小写的非限定类名来作为它的别名 -->
        <package name="domain.blog"/>
    </typeAliases>
    <!--类型处理器-->
    <typeHandlers>
 		<typeHandler handler="org.mybatis.example.ExampleTypeHandler"/>
	</typeHandlers>
    <!--对象工厂-->
    <objectFactory type="org.mybatis.example.ExampleObjectFactory">
  		<property name="someProperty" value="100"/>
    </objectFactory>
    <!--插件 -->
    <plugins>
    	<plugin interceptor="org.mybatis.example.ExamplePlugin">
    		<property name="someProperty" value="100"/>
    	</plugin>
	</plugins>
    <!--数据库厂商标志 -->
    <databaseIdProvider type="DB_VENDOR">
    	<property name="SQL Server" value="sqlserver"/>
 		<property name="DB2" value="db2"/>
  		<property name="Oracle" value="oracle" />
	</databaseIdProvider>
    <!-- 环境配置-->
	<environments default="development">
        <environment id="development">
            <transactionManager type="JDBC">
            <property name="..." value="..."/>
   			</transactionManager>
    		<dataSource type="POOLED">
      			<property name="driver" value="com.mysql.cj.jdbc.Driver"/>
      			<property name="url" value="jdbc:mysql://localhost:3306/detector_manager_system?characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false"/>
      			<property name="username" value="root"/>
     			<property name="password" value="root"/>
    		</dataSource>
  		</environment>
	</environments>
    <!--属性配置-->
    <properties resource="org/mybatis/example/config.properties">
        <property name="username" value="dev_user"/>
  		<property name="password" value="F2Fa3!33TYyg"/>
	</properties>
    
    <!--映射器配置-->
    <mappers>
        <!-- 使用相对于类路径的资源引用 -->
        <mapper resource="../mapper/*Mapper.xml"></mapper>
        <!-- 使用完全限定资源定位符（URL） -->
        <mapper url="file:///var/mappers/AuthorMapper.xml"/>
        <!-- 使用映射器接口实现类的完全限定类名 -->
        <mapper class="org.mybatis.builder.AuthorMapper"/>
        <!-- 将包内的映射器接口实现全部注册为映射器 -->
        <package name="org.mybatis.builder"/>
    </mappers>
</configuration>
```

#### 映射

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
	PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
	"http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="">

    <select id="selectPerson" parameterType="int" resultType="hashmap">
  		SELECT * FROM PERSON WHERE ID = #{id}
	</select>
    
    <insert id="insertAuthor">
  		insert into Author (id,username,password,email,bio)
  		values (#{id},#{username},#{password},#{email},#{bio})
	</insert>

	<update id="updateAuthor">
  		update Author set
    	username = #{username},
    	password = #{password},
    	email = #{email},
    	bio = #{bio}
  		where id = #{id}
	</update>

	<delete id="deleteAuthor">
  		delete from Author where id = #{id}
	</delete>
    
</mapper>
```

#### mybatis获取参数值的两种方式

mybatis获取参数值的两种方式：

+ ${}字符串拼接
+ #{}占位符赋值

mybatis获取参数值的各种情况

1. mapper接口方法的参数为单个的字面量类型

   可以通过${}和#{}以任意的字符串获取参数值，但是需要注意${}的单引号问题

2. mapper接口方法的参数为多个时

   此时mybatis会将这些参数放在一个map集合中，以两种方式进行存储

   + 以arg0、arg1...为键，以参数为值
   + 以param1,param2...为键，以参数为值

   因此只需要通过#{}和${}以键的方式访问值即可

3. 若mapper接口方法的参数有多个时，可以手动的将这些参数放在一个map中存储

   只需要通过#{}和${}以键的方式访问值即可 

4. mapper接口方法的参数是实体类类型的参数

   只需要通过#{}和${}以属性的方式访问属性值即可 

5. 使用@Param命名参数

#### mybatis的各种查询功能

1. 若查询出的数据只有一条
   1. 可以通过实体类对象接收
   2. 可以通过list集合接收
   3. 可以通过map集合接收
2. 若查询出的数据有多条
   1. 可以通过实体类型的list集合接收
   2. 可以通过map类型的list集合接收
   3. 可以在mapper接口的方法上添加@MapKey注解，此时就可以将每条数据转换的map集合作为值，以某个字段的值作为键，放在同一个map集合中

#### 特殊SQL的执行

```mysql
#模糊查询
select * from t_user where username like '%${username}%'
select * from t_user where username like concat('%',#{username},'%')
select * from t_user where username like "%"#{username}"%"

#批量删除
delete from t_user where id in (${ids})

#动态设置表名
select * from ${tableName}

#添加功能获取自增主键
<insert id="insertUser" useGenerateKeys="true" keyProperty="id">
    insert into t_user values(null,#{usernmae},#{password})                     
</insert>                                                
```

#### 自定义映射

**解决字段名和属性名不一致的情况**

+ 为字段起别名，保持和属性名的一致

+ 设置全局配置，将_自动映射为驼峰

  < setting name="mapUnderscoreToCamelCase" value="true"/>

+ 通过resultMap设置自定义的映射关系

**处理多对一映射关系**

+ 级联属性赋值

  ```xml
  <resultMap id="empAndDeptResultMapOne" type="Emp">
  	<id property="eid" column="eid"></id>
      <result property="empName" column="emp_name"></result>
      <result property="dept.id" column="did"></result>
      <result property="dept.deptName" column="dept_name"></result> 
  </resultMap>
  ```

+ association

  ```xml
  <resultMap id="empAndDeptResultMapOne" type="Emp">
  	<id property="eid" column="eid"></id>
      <result property="empName" column="emp_name"></result>
      <association property="dept" javaType="Dept">
          <id property="did" column="did"></id>
          <result property="deptName" column="dept_name"></result> 
      </association>
  </resultMap>
  ```

+ 分步查询

  ```xml
  <resultMap id="empAndDeptResultMapOne" type="Emp">
  	<id property="eid" column="eid"></id>
      <result property="empName" column="emp_name"></result>
      <association property="dept"
                   select="com.atguigu.mybatis.mapper.DeptMapper.getEmpByStep"
                   column="did"
                   fetchType="eager"></association>
  </resultMap>
  ```

分布查询的优点：可以实现延迟加载，但是必须在核心配置文件中设置全局配置信息

lazyLoadingEnabled：延迟加载的全局配置。当开启时，所有关联对象都会延迟加载，设置为true

aggressiveLazyLoading：当开启时，任何方法的调用都会加载该对象的所有属性。否则，每个属性会按需加载，设置为false

**处理一对多映射关系**

+ collection

  ```xml
  <resultMap id="deptAndEmpResultMap" type="Emp">
  	<id property="did" column="did"></id>
      <result property="deptName" column="dept_name"></result>
      <collection property="emps" ofType="Emp">
          <id property="eid" column="eid"></id>
          <result property="empName" column="emp_name"></result> 
      </collection>
  </resultMap>
  ```

+ 分步查询

  ```xml
  <resultMap id="deptAndEmpResultMap" type="Emp">
  	<id property="did" column="did"></id>
      <result property="deptName" column="dept_name"></result>
      <collection property="emps"
                   select="com.atguigu.mybatis.mapper.DeptMapper.getEmpByStep"
                   column="did"
                   fetchType="lazy"></collection>
  </resultMap>
  ```

#### 动态SQL

#### mybatis的缓存

**一级缓存**

一级缓存是SqlSession级别的，通过同一个SqlSession查询的数据会被缓存，下次查询相同的数据，就会从缓存中直接获取，不会从数据库重新访问。默认开启

一级缓存失效的四种情况：

+ 不同的SqlSession对应不同的一级缓存
+ 同一个SqlSession但是查询条件不同
+ 同一个SqlSession两次查询期间执行了任何一次增删改操作
+ 同一个SqlSession两次查询期间手动清空了缓存

**二级缓存**

二级缓存是SqlSessionFactory级别，通过同一个SqlSessionFactory创建的SqlSession查询的结果会被缓存，此后若再次执行相同的查询语句，结果就会从缓存中获取

二级缓存开启的条件：

+ 在核心配置文件中，设置全局配置属性cacheEnabled="true"，默认为true，不需要设置
+ 在映射文件中设置标签< cache/>
+ 二级缓存必须在SqlSession关闭或提交之后有效
+ 查询的数据所转换的实体类类型必须实现序列化接口

二级缓存失效情况：

两次查询之间执行力任意的增删改，会使一级和二级缓存同时失效

**二级缓存的相关配置**

在mapper配置文件中添加的cache标签可以设置一些属性：

+ eviction：缓存回收策略
  + LRU（Least RecentLy Used）：最近最少使用，移除最长时间不被使用的对象，默认
  + FIFO：先进先出，按对象进入缓存的顺序来移除
  + SOFT：软引用，移除基于垃圾回收器状态和软引用规则的对象
  + WEAK：弱引用，更积极的移除基于垃圾回收器状态和弱引用规则的对象
+ flushInterval：刷新间隔，单位毫秒，默认不设置，也就是没有刷新间隔，缓存仅仅调用语句时刷新
+ size：引用数目，正整数，代表缓存最多可以存储多少个对象，太大容易导致内存溢出
+ readOnly：只读，true/false
  + true：只读缓存，会给所有调用者返回缓存对象的相同实例。因此这些对象不能被修改。这提供了很重要的性能优势
  + false：读写缓存，会返回缓存对象的拷贝（通过序列化），会慢一些，但是安全

**mybatis缓存查询的顺序**

+ 先查询二级缓存，因为二级缓存中可能会有其他程序已经查出来的数据，可以拿来直接使用
+ 如果二级缓存没有命中，再查询一级缓存
+ 如果一级缓存也没有命中，则查询数据库
+ SqlSession关闭之后，一级缓存中的数据会写入二级缓存

#### mybatis的逆向工程

#### mybatis分页插件