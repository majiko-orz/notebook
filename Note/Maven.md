**Maven中的坐标**

+ groupId：公司或组织的id
+ artifactId：一个项目或者是项目中一个模块的id
+ version：版本号



```java
//生成maven工程
mvn archetype:generate
```

```xml
<!-- 打包方式 -->
<!-- jar:生成jar包，Java工程 -->
<!-- war:生成war包，web工程 -->
<!-- pom:关联其他工程的工程 -->
<packaging>jar</packaging>

<properties>
    <!-- 构建过程中读取源码时使用的字符集 -->
	<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>

<dependencies>
	<dependency>
    	<groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
        <scope>test</scope>
        <!-- 使用excludes标签配置依赖的排除 -->
        <exclusions>
        	<exclusion>
                <!-- 指定要排除的依赖的坐标（不需要写version） -->
            	<groupId>commons-logging</groupId>
                <artifactId>commons-logging</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
</dependencies>
```

**执行Maven的构建命令**

运行maven中和构建操作相关的命令时，必须进入到pom.xml所在的目录

```mysql
#清理操作：删除target目录
mvn clean
    
#编译操作
#主程序编译
mvn compile
#测试程序编译
mvn test-compile
#主题程序编译结果存放的目录
target/classes
#测试程序编译结果存放的目录
target/test-class

#测试操作
mvn test
#测试的报告存放的目录
target/surefire-reports

#打包操作：打包结果-jar包，存放目录target
mvn package

#安装操作
#1.将本地构建过程中生成的jar包存入maven本地仓库
#2.将pom.xml文件转换为xxx.pom一起放入本地仓库
mvn install
```

**依赖范围：scope**

标签位置：dependencies/dependency/scope

标签的可选值：compile/test/provided/system/runtime/import

|          | main目录(空间) | test目录(空间) | 开发过程(时间) | 部署到服务器(时间) |
| -------- | -------------- | -------------- | -------------- | ------------------ |
| compile  | 有效           | 有效           | 有效           | 有效               |
| test     | 无效           | 有效           | 有效           | 无效               |
| provided | 有效           | 有效           | 有效           | 无效               |
| system   |                |                |                |                    |
| runtime  |                |                |                |                    |
| import   |                |                |                |                    |

**依赖的传递性**

在A依赖B，B依赖C的前提下，C是否能够传递到A，取决于B依赖C时使用的依赖范围

+ B依赖C时使用compile范围可以传递
+ B依赖C时使用test或provided范围：不能传递

**继承**

1. 创建父工程，打包方式为pom

   ```xml
   <groupId>com.atguigu.maven</groupId>
   <artifactId>pro03-maven-parent</artifactId>
   <version>1.0-SNAPSHOT</version>
   
   <packaging>pom</packaging>
   
   <!-- 聚合的配置 -->
   <modules>
   	<module>pro04-maven-module</module>
       <module>pro05-maven-module</module>
   </modules>
   
   <!-- 在父工程中统一管理依赖信息 -->
   <!-- 注意：即使在父工程配置了对依赖的管理，子工程需要使用具体哪一个依赖还是要明确配置 -->
   <dependencyManagement>
   	<dependencies>
       	<dependency>
           	<groupId>org.springframework</groupId>
   			<artifactId>spring-core</artifactId>
   			<version>4.0.0.RELEASE</version>
           </dependency>
       </dependencies>
   </dependencyManagement>
   ```

2. 子工程

   ```xml
   <parent>
   	<groupId>com.atguigu.maven</groupId>
   	<artifactId>pro03-maven-parent</artifactId>
   	<version>1.0-SNAPSHOT</version>
   </parent>
   
   <!-- 子工程的groupId，version如果和父工程一样，可以省略 -->
   <groupId>com.atguigu.maven</groupId>
   <artifactId>pro04-maven-module</artifactId>
   <version>1.0-SNAPSHOT</version>
   
   <!-- 注意：即使在父工程配置了对依赖的管理，子工程需要使用具体哪一个依赖还是要明确配置 -->
   <dependencies>
       <dependency>
           <groupId>org.springframework</groupId>
           <artifactId>spring-core</artifactId>
           <!-- 对于已经在父工程进行了管理的依赖，子工程中引用时可以不写version,如果和父工程管理的版本不一致，会覆盖父工程的版本 -->
           <version>4.0.0.RELEASE</version>
       </dependency>
   </dependencies>
   ```

   

3. 

