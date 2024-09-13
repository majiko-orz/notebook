### shiro架构原理

![](../%E5%B7%A5%E5%85%B7/shiro.png)

**subject**

主体。每个用户登录成功后都会对应一个Subject对象，所有用户信息都存放在Subject中。可以理解：Subject就是shiro提供的用户实体类

**Security Manager**

shiro最大的容器，此容器中包含了shiro的绝大多数功能。在非springboot项目中获取Security Manager是编写代码的第一步。而在springboot中已经帮我们自动化配置了。

**Authenticator**

认证器。执行认证过程远程调用的组件。里面包含了认证策略。

**Authorizer**

授权器。执行授权时调用的组件。

**Session Manager**

shiro被web集成之后，HttpSession对象会由shiro的session manager进行管理

**Cache Manager**

缓存管理。shiro执行很多第三方缓存技术。例如:EHCache等。

**Session DAO**

操作session内容的组件。

**Realms**

shiro框架实现权限控制不依赖于数据库，通过内置数据也可以实现权限控制。但是目前绝大多数应用的数据都存储在数据库中，所以shiro提供了realms组件，此组件的作用就是访问数据库。shiro内置的访问数据库的代码，通过简单配置就可以访问数据库，也可以自定义realms实现数据库访问逻辑（绝大多数都这么做）

### ini配置文件

**1 [main] 主体部分**

内置了根对象：securityManager,注意对象名大小写

```bash
[main]
securityManager.属性=值
key=value
securityManager.对象属性=com.pojo.People #后面值是字符串

peo=com.pojo.People
securityManager.对象属性=$peo #出现$时才表示是引用对象
```

**2 [users]**

```bash
[users]
用户名=密码.角色1,角色2 #角色部分可以省略
zhangsan=123
```

**3 [roles]**

```bash
[roles]
角色名=权限名,权限名
role1=user:insert,user:update
```

**4 [urls]**

定义哪个控制器被那个过滤器过滤，shiro内置很多过滤器，此部分主要在web应用中使用

| 配置缩写          | 对应的过滤器                         | 功能                                                         |
| ----------------- | ------------------------------------ | ------------------------------------------------------------ |
| 身份验证相关的    |                                      |                                                              |
| anon              | AnonymousFilter                      | 指定url可以匿名访问                                          |
| authc             | FormAuthenticationFilter             | 基于表单的拦截器；如“/**=authc”，如果没有登录会跳到相应的登录页面登录；主要属性：usernameParam：表单提交的用户名参数名（ username）； passwordParam：表单提交的密码参数名（password）； rememberMeParam：表单提交的密码参数名（rememberMe）； loginUrl：登录页面地址（/login.jsp）；successUrl：登录成功后的默认重定向地址； failureKeyAttribute：登录失败后错误信息存储key（shiroLoginFailure） |
| authcBasic        | BasicHttpAuthenticationFilter        | Basic HTTP身份验证拦截器，主要属性： applicationName：弹出登录框显示的信息（application） |
| logout            | authc.LogoutFilter                   | 退出拦截器，主要属性：redirectUrl：退出成功后重定向的地址（/） |
| user              | UserFilter                           | 用户拦截器，用户已经身份验证/记住我登录的都可                |
| 授权相关的        |                                      |                                                              |
| roles             | RolesAuthorizationFilter             | 角色授权拦截器，验证用户是否拥有所有角色；主要属性： loginUrl：登录页面地址（/login.jsp）；unauthorizedUrl：未授权后重定向的地址；示例“/admin/**=roles[admin]” |
| perms             | PermissionsAuthorizationFilter       | 权限授权拦截器，验证用户是否拥有所有权限；属性和roles一样；示例“/user/**=perms[“user:create”]” |
| port              | PortFilter                           | 端口拦截器，主要属性：port（80）：可以通过的端口；示例“/test= port[80]”，如果用户访问该页面是非80，将自动将请求端口改为80并重定向到该80端口，其他路径/参数等都一样 |
| rest              | HttpMethodPermissionFilter           | rest风格拦截器，自动根据请求方法构建权限字符串（GET=read, POST=create,PUT=update,DELETE=delete,HEAD=read,TRACE=read,OPTIONS=read, MKCOL=create）构建权限字符串；示例“/users=rest[user]”，会自动拼出“user:read,user:create,user:update,user:delete”权限字符串进行权限匹配（所有都得匹配，isPermittedAll） |
| ssl               | SslFilter                            | SSL拦截器，只有请求协议是https才能通过；否则自动跳转会https端口（443）；其他和port拦截器一样 |
| noSessionCreation | NoSessionCreationAuthorizationFilter | 需要指定权限才能访问                                         |

```bash
[urls]
控制器名称=过滤器名称
/login=authc
/**=anon
```

### 认证流程

获取主体，通过主体Subject对象的login方法进行登录

把Subject中内容传递给SecurityManager

SecurityManager 内部组件Authenticator进行认证

认证数据使用InI Realm,调用ini文件中数据

**名词解释**

**Principle**: 身份。用户名、邮箱、手机号等能够唯一确认身份的信息

**Credential**:凭证，代表密码等

**AuthenticationInfo**:认证时存储认证信息



```java
public class ShiroDemo {
    public static void main(String[] args) {
        //1.创建安全管理器对象
        DefaultSecurityManager securityManager=new DefaultSecurityManager();

        //2.给安全管理器设置realm
        securityManager.setRealm(new IniRealm("classpath:shiro.ini"));

        //3.SecurityUtils 给安全工具类设置安全管理器
        SecurityUtils.setSecurityManager(securityManager);

        //4.关键对象 subject主体
        Subject subject=SecurityUtils.getSubject();

        //5.创建令牌
        UsernamePasswordToken token=new UsernamePasswordToken("zhangsan","123456");
        try {
            subject.login(token);
            System.out.println("认证状态："+subject.isAuthenticated());
        }catch (UnknownAccountException e){
            e.printStackTrace();
            System.out.println("认证失败：账号错误");
        }catch (IncorrectCredentialsException e){
            e.printStackTrace();
            System.out.println("认证失败：密码错误");
        }
    }
}
```

#### 自定义realm

```java
/**
 *  自定义realm,将认证/授权数据的来源转为数据库的实现
 */
public class RealmDemo extends AuthorizingRealm {

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }
    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //在token中获取用户名
        String principal=(String) token.getPrincipal();
        System.out.println(principal);
        //根据身份信息使用jdbc,mybatis查询相关数据库
        if("zhangsan".equals(principal)){
            //参数1：返回数据库中的用户名 参数2:返回数据库中正确的密码 参数3：提供当前realm的名字
            SimpleAuthenticationInfo simpleAuthenticationInfo=new SimpleAuthenticationInfo("zhangsan","123456",this.getName());
            return simpleAuthenticationInfo;
        }
        return null;
    }
}

```

#### 加密

**MD5算法**

作用：一般用来加密或者 签名（校验和）

特点：MD5算法不可逆，如果内容相同，无论执行多少次md5生成的结果始终都是一样的

生成结果：始终是16进制32位字符串

```java
      //使用MD5
        Md5Hash md5Hash=new Md5Hash("123456");
        System.out.println(md5Hash.toHex());

        //使用MD5+salt处理
        Md5Hash md5Hash1=new Md5Hash("123456","xo*7ps");

        //使用MD5+salt+Hash散列
        Md5Hash md5Hash2=new Md5Hash("123456","xo*7ps",1024);
```

```java
public class TestRealmDemo {
    public static void main(String[] args) {
        //创建securityManager
        DefaultSecurityManager defaultSecurityManager=new DefaultSecurityManager();

        //注入realm
        RealmDemo realmDemo=new RealmDemo();
        //设置realm使用hash凭证匹配器
        HashedCredentialsMatcher credentialsMatcher=new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName("md5");
        credentialsMatcher.setHashIterations(1024);
        realmDemo.setCredentialsMatcher(credentialsMatcher);
        //设置自定义realm
        defaultSecurityManager.setRealm(realmDemo);

        //将安全工具类设置安全工具类
        SecurityUtils.setSecurityManager(defaultSecurityManager);

        //通过安全工具类获取subject
        Subject subject=SecurityUtils.getSubject();

        UsernamePasswordToken token=new UsernamePasswordToken("zhangsan","123456");
        try{
            subject.login(token);
        }catch (AuthenticationException e){
            e.printStackTrace();
        }
//-------------------        
         //认证用户进行授权
        if(subject.isAuthenticated()){
            //基于角色权限控制
            System.out.println(subject.hasRole("admin"));
            //基于多角色的权限控制
            System.out.println(subject.hasRoles(Arrays.asList("admin", "user")));
            //是否只有其中一个角色
            boolean[] booleans=subject.hasRoles(Arrays.asList("admin","user","super"));
            for (boolean aBoolean:booleans){
                System.out.println(aBoolean);
            }
            //基于权限字符串的访问控制  资源标识符：操作：资源类型
            System.out.println(subject.isPermitted("user:*:*"));
            //分别具有哪些权限
            boolean[] permitted=subject.isPermitted("user:*:01","order:*:10");
            for (boolean b:permitted) {
                System.out.println(b);
            }
            //同时具有哪些权限
            boolean permittedAll=subject.isPermittedAll("user:*:01","product:*");
            System.out.println(permittedAll);
        }
    }
```

```java
public class RealmDemo extends AuthorizingRealm {

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("------------------");
        String primaryPrincipal=(String)principals.getPrimaryPrincipal();
        System.out.println("身份信息："+primaryPrincipal);
        //根据身份信息 用户名 获取当前用户的角色信息，以及权限信息  zhangsan  admin  user
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRole("admin");
        simpleAuthorizationInfo.addRole("user");
        //将数据库中的查询权限信息赋值给权限对象
        simpleAuthorizationInfo.addStringPermission("user:*:01");
        return simpleAuthorizationInfo;
    }
    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //在token中获取用户名
        String principal=(String) token.getPrincipal();
        System.out.println(principal);
        //根据身份信息使用jdbc,mybatis查询相关数据库
        if("zhangsan".equals(principal)){
            //参数1：返回数据库中的用户名 参数2:返回数据库中正确的密码 参数3：提供当前realm的名字
            //参数1：返回数据库中的用户名 参数2:数据库MD5+salt之后的密码 参数3：注册时的随机盐 参数4：realm的名字
            SimpleAuthenticationInfo simpleAuthenticationInfo=new SimpleAuthenticationInfo("zhangsan","123456", ByteSource.Util.bytes("xo*7ps"),this.getName());
            return simpleAuthenticationInfo;
        }
        return null;
    }
}
```

### 授权

**关键对象**

授权可简单理解为who对what（which）进行how操作

**who、即主体（subject）**,主体需要访问系统中的资源。

**what、即资源（Resource）**,如系统菜单、页面、按钮、系统商品信息等等

**how、权限/许可（Permission）**，规定主体对资源的操作许可，通过权限可知主体对哪些资源有哪些许可操作

**授权方式**

+ 基于角色的访问控制

  + RBAC基于角色的访问控制（Role-Based Access Control）是以角色为中心进行访问控制

  ```java
  if(subject.hasRole("admin")){
      //操作什么资源
  }
  ```

+ 基于资源的访问控制

  + RBAC基于资源的访问控制（Resource-Based Access Control）是以资源为中心进行访问控制

  ```java
  if(subject.isPermission("user:update:01")){
      //对01用户进行修改
  }
  ```

**权限字符串**

权限字符串的规则是：**资源标识符:操作:资源实例标识符**，意思是对哪个资源的哪个实例具有什么操作，**":"**是资源/操作/实例的分隔符，权限字符串也可以使用*****通配符

例子：

+ 用户创建角色权限：user:create、或user:create:*
+ 用户修改实例001的权限：user:update:001
+ 用户实例001的所有权限：user:*:001

**shiro中授权编程实现方式**

+ 编程式

```java
Subject subject=SecurityUtils.getSubject();
if(subject.hasRole("admin")){
    //有权限
}else{
    //无权限
```

+ 注解式

```java
@RequiresRoles("admin")
public void hello(){
    //有权限
}
```

+ 标签式

```java
//JSP/GSP标签：在JSP/GSP页面通过相应的标签完成：
<shiro:hasRole name="admin">
    <!-有权限->
</shiro:hasRole>
//注意：Thymeleaf中使用shiro相应额外集成！
```

### springboot整合shiro

![](../%E5%B7%A5%E5%85%B7/shiro_1.png)

1. 创建entity，controller，service，mapper
2. 创建salt工具类

```java
public class SaltUtils {
    public static String getSalt(int n){
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()".toCharArray();
        StringBuilder  sb=new StringBuilder();
        for (int i = 0; i < n; i++) {
            char aChar = chars[new Random().nextInt(chars.length)];
            sb.append(aChar);
        }
        return sb.toString();
    }
}
```

3. service实现类

```java
@Service
@Transactional
public class IShiroUserServiceImpl implements IShiroUserService {
    @Autowired
    private ShiroUserMapper shiroUserMapper;

    @Override
    public void register(ShiroUser shiroUser) {
        //处理业务调用dao
        //1.生成随机盐
        String salt= SaltUtils.getSalt(8);
        //2.将随机盐保存到数据
        shiroUser.setSalt(salt);
        //3.明文密码进行md5+salt+hash散列
        Md5Hash md5Hash = new Md5Hash(shiroUser.getPassword(),salt,1024);
        shiroUser.setPassword(md5Hash.toHex());
        shiroUserMapper.save(shiroUser);
    }
}
```

4. shiroConfig



5. MyRealm

### cache

#### EhCache

```java
 //3.创建自定义realm
    @Bean
    public Realm getRealm(){
        MyRealm myRealm=new MyRealm();
        //修改凭证校验匹配器
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //设置加密算法为md5
        credentialsMatcher.setHashAlgorithmName("md5");
        //设置散列次数
        credentialsMatcher.setHashIterations(1024);
        myRealm.setCredentialsMatcher(credentialsMatcher);
        //开启缓存管理
        myRealm.setCacheManager(new EhCacheManager());
        myRealm.setCachingEnabled(true); //开启全局缓存
        myRealm.setAuthenticationCachingEnabled(true); //开启认证缓存
        myRealm.setAuthenticationCacheName("authenticationCache");
        myRealm.setAuthorizationCachingEnabled(true); //开启授权缓存
        myRealm.setAuthorizationCacheName("authorizationCache");

        return myRealm;
    }
```

#### Redis

