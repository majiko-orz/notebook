![](/img/Dubbo_1.png)

**@EnableDubbo**

用于扫描@DubboService并把对应的对象实例化，发布成RPC服务

扫描路径：应用这个注解的类（启动类）所在的包及其子包

**@DubboService**

暴露服务，SpringBoot会创建这个类型的对象，并发布成Dubbo服务

**@DubboReference**

引用服务，注入远端服务的代理对象