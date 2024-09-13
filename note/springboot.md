## springboot

### maven配置阿里云镜像

```xml
<mirrors>
	<mirror>
    	<id>nexus-aliyun</id>
        <mirrorOf>central</mirrorOf>
        <name>Nexus aliyun</name>
        <url>http://maven.aliyun.com/nexus/content/groups/public</url>
    </mirror>
</mirrors>

<profiles>
	<profile>
    	<id>jdk-1.8</id>
        <activation>
        	<activeByDefault>true</activeByDefault>
            <jdk>1.8</jdk>
        </activation>
        <properties>
        	<maven.compiler.source>1.8</maven.compiler.source>
            <maven.compiler.target>1.8</maven.compiler.target>
            <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
        </properties>
    </profile>
</profiles>
```

### springboot基础

1. 依赖管理

2. 自动配置

   自动配置好tomcat,springMVC,web常见功能，如字符编码问题

   默认包结构：主程序所在包及其下面的所有子包里面的组件都会被默认扫描进来，想要改变扫描路径，@SpringbootApplication(scanBasePackages="包名")或者@ComponentScan 指定扫描路径

   各种配置拥有默认值，配置文件的值最终会绑定到每个类上，这个类会在容器中创建对象

   spring boot所有的自动配置功能都在spring-boot-autoconfigure包里面

   @Import导入组件，@Conditional条件装配，@ImportResource导入spring配置文件，

   配置绑定：

   @Component+@ConfigurationProperties

   启动类@EnableConfigurationProperties(A.class)+@ConfigurationProperties

3. 自动配置原理入门

   1. 引导加载自动配置类

      ```java
      @SpringBootConfiguration
      @EnableAutoConfiguration
      @ComponentScan(
       excludeFilters = {@Filter(type = FilterType.CUSTOM,classes = {TypeExcludeFilter.class}), @Filter(type = FilterType.CUSTOM,classes = {AutoConfigurationExcludeFilter.class})})
      public @interface SpringBootApplication {}
      ```

      + @SpringBootConfiguration

        @Configuration,代表当前是一个配置类

      + @ComponentScan

        指定扫描哪些spring注解

      + @EnableAutoConfiguration

        ```java
        @AutoConfigurationPackage
        @Import({AutoConfigurationImportSelector.class})
        public @interface EnableAutoConfiguration {}
        ```

        + @AutoConfigurationPackage

          ```java
          @Import({Registrar.class}) //给容器中导入一个组件
          public @interface AutoConfigurationPackage {}
          
          //利用register给容器中导入一系列组件
          //将指定包下的所有组件导入进来
          ```

        + @Import({AutoConfigurationImportSelector.class})

          ```java
          利用getAutoConfigurationEntry(autoConfigurationMetadata, annotationMetadata);给容器中批量导入一些组件
          调用List<String> configurations = this.getCandidateConfigurations(annotationMetadata, attributes);获取到所有需要导入到容器中的配置类
          利用工厂加载Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader)得到所有的组件
          classLoader.getResources("META-INF/spring.factories")，从META-INF/spring.factories位置加载一个文件，默认扫描当前系统里面所有META-INF/spring.factories位置的文件
          ```

   2. 按需开启自动配置项

      有相关的包才能加载

   3. 定制化修改自动配置

      spring boot默认会在底层配好所有的组件，但是如果用户自己配置了以用户的优先

      

   总结：

   + SpringBoot先加载所有的自动配置类 xxxAutoConfiguration
   + 每个自动配置类按照条件进行生效，默认都会绑定配置文件指定的值，xxxProperties里面拿，xxxProperties和配置文件进行了绑定
   + 生效的配置类就会给容器中装配很多组件
   + 只要容器中有这些组件，相当于这些功能就有了
   + 只要用户有自己配置的，就以用户的优先
   + 定制化配置
     + 用户直接自己@Bean替换底层的组件
     + 用户去看这个组件时获取的配置文件什么值就去修改

   

   查看自动配置了哪些

   配置文件debug=true开启自动配置报告，Negative(不生效)，Positive(生效)

```java
@SpringBootApplication
public class MainApplication{
    public static void main(String[] args){
    //1.返回IOC容器
    ConfigurableApplicationContext run=SpringApplication.run(MainApplication.class,args);
    //2.查看容器里面的组件
    String[] names=run.getBeanDefinitionNames();
    for(String name:names){
    	System.out.println(name);
   	}
    }
}
```

### springboot核心

1. 配置文件

   1. properties

   2. yaml

      1. 基本语法

         + key: value   kv之间有空格
         + 大小写敏感
         + 使用缩进表示层级关系
         + 缩进不允许使用tab，只允许空格，idea中可以使用tab
         + 缩进的空格数不重要，只要相同层级的元素左对齐即可
         + #表示注释
         + ''与""表示字符串内容会被转义/不转义

      2. 数据类型

         + 字面量：单个的，不可再分的值。date、boolean、string、number、null

           ```yaml
           k: v
           ```

         + 对象：键值对的集合。map、hash、set、object

           ```yaml
           行内写法：k: {k1:v1,k2:v2,k3:v3}
           或
           k: 
           	k1: v1
           	k2: v2
           	k3: v3
           ```

         + 数组：一组按照次序排列的值。array、list、queue

           ```yaml
           行内写法: k: [v1,v2,v3]
           或
           k: 
           	- v1
           	- v2
           	- v3
           ```

   3. 配置文件加载位置

      + -file:./config/
      + -file:./
      + -classpath:/config/
      + -classpath:/

       **即根目录下的config目录下，然后是 根目录下，然后是classpath路径下的config目录下，最后是classpath路径下。 优先级由高到低，高优先级的配置会覆盖低优先级的配置** 

2. web开发

   1. 静态资源访问

      1. 静态资源目录：类路径下：directory called `/static` (or `/public` or `/resources` or `/META-INF/resources`) 

      访问：当前目录根路径/+静态资源名

      原理：静态映射/**

      请求进来，先去找controller看能不能处理，不能处理的所有请求又都交给静态资源处理器，静态资源也找到

      改变默认静态资源路径

      ```yaml
      spring: 
      	web:
      		resources:
      			static-locations: [classpath:/haha,..]
      ```

      2. 静态资源访问前缀

      默认无前缀

      ```yaml
      spring: 
      	mvc:
      		static-path-pattern: /resources/**
      ```

      当前项目+static-path-pattern+静态资源名=静态资源文件夹下找

      3. 欢迎页支持

      + 静态资源路径下 index.html

        可以配置静态资源路径，但是不可以配置静态资源的访问前缀，否则导致index.html不能被默认访问

      + controller能处理/index

   4. 静态资源配置原理

      + spring boot启动默认加载 xxxAutoConfiguration类（自动配置类）

      + springMVC功能的自动配置类 WebMvcAutoConfiguration,生效

        ```java
        @Configuration
        @ConditionalOnWebApplication(type = Type.SERVLET)
        @ConditionalOnClass({Servlet.class, DispatcherServlet.class, WebMvcConfigurer.class})
        @ConditionalOnMissingBean({WebMvcConfigurationSupport.class})
        @AutoConfigureOrder(-2147483638)
        @AutoConfigureAfter({DispatcherServletAutoConfiguration.class,TaskExecutionAutoConfiguration.class, ValidationAutoConfiguration.class})
        public class WebMvcAutoConfiguration {}
        ```

        给容器中配了什么

        ```java
        @Configuration
        @Import({WebMvcAutoConfiguration.EnableWebMvcConfiguration.class})
        @EnableConfigurationProperties({WebMvcProperties.class, ResourceProperties.class})
        @Order(0)
        public static class WebMvcAutoConfigurationAdapter implements WebMvcConfigurer, ResourceLoaderAware {}
        ```

        配置文件的相关属性和xxx进行了绑定。WebMvcProperties==**spring.mvc**,ResourceProperties==**spring.resources**

        

        配置类只有一个有参构造器

        ```java
        //有参构造器所有参数的值都会从容器中确定
        //ResourceProperties resourceProperties,获取和spring.resources绑定的所有值的对象
        //WebMvcProperties mvcProperties, 获取和spring.mvc绑定的所有值的对象
        // ListableBeanFactory beanFactory, spring的beanFactory
        //HttpMessageConverters,找到所有的HttpMessageConverters
        //ResourceHandlerRegistrationCustomizer,找到资源处理器的自定义器
        public WebMvcAutoConfigurationAdapter(ResourceProperties resourceProperties, WebMvcProperties mvcProperties, ListableBeanFactory beanFactory, ObjectProvider<HttpMessageConverters> messageConvertersProvider, ObjectProvider<WebMvcAutoConfiguration.ResourceHandlerRegistrationCustomizer> resourceHandlerRegistrationCustomizerProvider) {
                    this.resourceProperties = resourceProperties;
                    this.mvcProperties = mvcProperties;
                    this.beanFactory = beanFactory;
                    this.messageConvertersProvider = messageConvertersProvider;
                    this.resourceHandlerRegistrationCustomizer = (WebMvcAutoConfiguration.ResourceHandlerRegistrationCustomizer)resourceHandlerRegistrationCustomizerProvider.getIfAvailable();
                }
        ```

        资源处理的默认规则

        ```java
         public void addResourceHandlers(ResourceHandlerRegistry registry) {
                    if (!this.resourceProperties.isAddMappings()) {
                        logger.debug("Default resource handling disabled");
                    } else {
                        Duration cachePeriod = this.resourceProperties.getCache().getPeriod();
                        CacheControl cacheControl = this.resourceProperties.getCache().getCachecontrol().toHttpCacheControl();
                        if (!registry.hasMappingForPattern("/webjars/**")) {
                            this.customizeResourceHandlerRegistration(registry.addResourceHandler(new String[]{"/webjars/**"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/webjars/"}).setCachePeriod(this.getSeconds(cachePeriod)).setCacheControl(cacheControl));
                        }
        
                        String staticPathPattern = this.mvcProperties.getStaticPathPattern();
                        if (!registry.hasMappingForPattern(staticPathPattern)) {
                            this.customizeResourceHandlerRegistration(registry.addResourceHandler(new String[]{staticPathPattern}).addResourceLocations(getResourceLocations(this.resourceProperties.getStaticLocations())).setCachePeriod(this.getSeconds(cachePeriod)).setCacheControl(cacheControl));
                        }
        
                    }
                }
        ```

        ```yaml
        spring:
        	resources:
        		add-mappings: false   #禁用所有静态资源规则
        ```

      

   欢迎页的处理规则

   ```java
       @Bean
      public WelcomePageHandlerMapping welcomePageHandlerMapping(ApplicationContext applicationContext, FormattingConversionService mvcConversionService, ResourceUrlProvider mvcResourceUrlProvider) {
      WelcomePageHandlerMapping welcomePageHandlerMapping = new WelcomePageHandlerMapping(new TemplateAvailabilityProviders(applicationContext), applicationContext, this.getWelcomePage(), this.mvcProperties.getStaticPathPattern());
      welcomePageHandlerMapping.setInterceptors(this.getInterceptors(mvcConversionService, mvcResourceUrlProvider));
      welcomePageHandlerMapping.setCorsConfigurations(this.getCorsConfigurations());
                  return welcomePageHandlerMapping;
              }
   ```

   3. 请求参数处理

      1. 请求映射

      + @xxxMapping,rest风格支持，

      + 核心filter：HiddenHttpMethodFilter

        用法：表单method=post，隐藏域 _method=put

        springboot中需要手动开启

        ```yaml
        spring.mvc.hiddenmethod.filter.enabled: true  #选择性开启
        ```

      + Rest原理（表单提交要使用REST的时候）

        + 表单提交会带上_method=put
        + 请求过来被HiddenHttpMethodFilter拦截，
          + 请求是否正常，并且时POST
          + 获取到_method的值
          + 兼容以下请求：PUT,DELETE,PATCH
          + 原生request（post），包装模式requestWrapper重写了getMethod方法，返回的是传入的值
          + 过滤器链放行的时候用wrapper

      + Rest使用客户端工具

        如postMan直接发送put、delete等方式请求，无需Filter

      2. 请求映射原理

         FrameworkServlet->processRequest->doService(DispatcherServlet)->doDispatch

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
                             //找到当前请求使用哪个Handler(Controller的方法)处理
                             mappedHandler = this.getHandler(processedRequest);
                             
                             //HandlerMapping:处理器映射 /请求->谁来处理
         ```

         RequestMappingHandlerMapping：保存了所有@RequestMapping和handler的映射规则

         所有的请求映射都在handlerMapping中

         spring boot自动配置了欢迎页的WelcomPageHandlerMapping，访问/能访问到index.html;

         springboot自动配置了默认的RequestMappingHandlerMapping

         请求进来，挨个尝试所有的handlerMapping看是否有请求信息，如果有就找到这个请求信息的handler,如果没有就是下一个HandlerMapping

         我们需要一些自定义的映射处理，我们也可以自己给容器中放HandlerMapping，自定义HandlerMapping

      3. 普通参数与基本注解

         + 注解

           @PathVariable（路径变量）、@RequestHeader（获取请求头）、@ModelAttribute、@RequestParam（获取请求参数）、@MatrixVariable（矩阵变量：在请求路径中用分号分隔参数：/car/{path;a=1;b=2;c=3}）、@CookieValue（获取cookie值）、@RequestBody、@RequestAttribute（获取request域属性）、@RequestBody（获取请求体）

           url重写：把cookie的值使用矩阵变量的方式进行传递 /abc;jsession=xxxx

           springboot默认禁用矩阵变量的功能，

           手动开启：原理：对于路径的处理。UrlPathHelper进行解析。removeSemicolonContent(移除分号内容)支持矩阵变量的

           矩阵变量必须有url路径变量才能被解析

           ```java
           @Bean
           public WebMvcConfigurer webMvcConfigurer(){
               return new WebMvcConfigurer(){
                   @Override
                   public void configurePathMatch(PathMatchConfigurer configurer){
                       UrlPathHelper urlPathHelper=new UrlPathHelper();
                       //不移除;后面的内容。矩阵变量可以生效
                       urlPathHelper.setRemoveSemicolonContent(false);
                       configurer.setUrlPathHelper(urlPathHelper);
                   }
               };
           }
           ```

           

         + Servlet API

           WebRequest、ServletReaquest、MultipartRequest、HttpSession、javax.servlet.http.PushBuilder、Principal、InputStream、Reader、HttpMethod、Locale、TimeZone、Zoneld

         + 复杂参数

           Map、Model(map,modle里面的数据会被放在request的请求域 request.setAttribute)、Errors/BindingResult、RedirectAttributes(重定向携带数据)、ServletResponse(response)、SessionStatus、UrlComponentsBuilder、servletUriComponentsBuilder

         + 自定义对象参数

           可以自动类型转换与格式化，可以级联封装,ServletModelAttributeMethodProcessor解析

      4. 参数处理原理

         + HandlerMapping中找到能处理请求的Handler（Cotroller.method()）

         + 为当前Handler找到一个适配器HandlerAdapter;   RequestMappingHandlerAdapter

           + HandlerAdapter：RequestMappingHandlerAdapter(支持方法上标注@RequestMapping)、HandlerFunctionAdapter(支持函数式编程的)、HttpRequestHandlerAdapter、SimpleControllerHandlerAdapter

           + 执行目标方法

             ```java
             //DispatcherServelet -- doDispatcher
             mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
             ```

             ```java
             //RequestMappingHandlerAdapter -- handler
             mav = this.invokeHandlerMethod(request, response, handlerMethod);//执行目标方法
             //ServletInvocableHandlerMethod -- invokeAndHandle
             Object returnValue = this.invokeForRequest(webRequest, mavContainer, providedArgs);//真正执行目标方法
             ```

           + 参数解析器 @PathParam等

             确定将要执行的目标方法的每一个参数的值是多少

             springmvc目标方法能写多少种参数类型，取决于参数解析器 HandlerMethodArgumentResolver

             ```java
             //RequestMappingHandlerAdapter -- invokeHandlerMethod
             argumentResolvers
             ```

             ```java
             public interface HandlerMethodArgumentResolver {
             //当前解析器是否支持解析这种参数
             boolean supportsParameter(MethodParameter var1);
             //支持就调用解析方法
             @Nullable
             Object resolveArgument(MethodParameter var1, @Nullable ModelAndViewContainer var2, NativeWebRequest var3, @Nullable WebDataBinderFactory var4) throws Exception;
             }
             ```

           + 返回值处理器 @ResponseBody等

             ```java
             //RequestMappingHandlerAdapter -- invokeHandlerMethod
             returnValueHandlers
             ```

           + 如何确定目标方法每一个参数的值

             ```java
             //InvocableHandlerMethod -- getMethodArgumentValues
             ```

             + 挨个判断所有参数解析器哪个支持这个参数

               ```java
               //HandlerMethodArgumentResolverComposite -- getArgumentResolver
               ```

             + 解析这个参数的值

               ```java
               //调用各自HandlerMethodArgumentResolver的resolverArgument方法即可
               ```

           + 自定义类型参数 封装POJO

             ServletModelAttributeMethodProcessor这个参数处理器支持

             ```java
             //ModelAttributeMethodProcessor -- resolveArgument
             WebDataBinder binder = binderFactory.createBinder(webRequest, attribute, name);
             //WebDataBinder:web数据绑定器，将请求参数的值绑定到指定的JavaBean里面
             //WebDataBinder 利用它里面的Converters将请求数据转成指定的数据类型，再次封装到JavaBean中
             
             //GenericConversionService：在设置每一个值的时候，找他里面所有converter哪个可以将这个数据类型（request带来的字符串）转换到指定的类型（JavaBean -- Integer）
             ```

           + 自定义Converter

             ```java
             @Bean
             public WebMvcConfigurer webMvcConfigurer(){
                 return new WebMvcConfigurer(){
                     @Override
                     public void addFormatters(FormatterRegistry registry){
                         registry.addConverter(new Converter<String,Pet>(){
                             @Override
                             public Pet converter(String source){
                                 if(!StringUtils.isEmpty(source)){
                                     Pet pet=new Pet();
                                     String[] split=source.split(",");
                                     pet.setName(split[0]);
                                     pet.setAge(Integer.parseInt(split[1]))
                                     return pet;
                                 }
                                 return null;
                             }
                         });
                     }
                 };
             }
             ```

   4. 数据响应

      + jacson.jar+@ResponseBody，spring-boot-starter-web自动引入了json场景

      + 返回值处理

      ```java
      //ServletInvocableHandlerMethod
      this.returnValueHandlers.handleReturnValue(returnValue, this.getReturnValueType(returnValue), mavContainer, webRequest);
      ```

      ```java
      //HandlerMethodReturnValueHandlerComposite
      public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
              HandlerMethodReturnValueHandler handler = this.selectHandler(returnValue, returnType);
              if (handler == null) {
                  throw new IllegalArgumentException("Unknown return value type: " + returnType.getParameterType().getName());
              } else {
                  handler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
              }
          }
      ```

      ```java
      public interface HandlerMethodReturnValueHandler {
          //返回值处理器先判断是否支持这种类型的返回值
          boolean supportsReturnType(MethodParameter var1);
      	//如果支持，返回值处理器调用进行处理
          void handleReturnValue(@Nullable Object var1, MethodParameter var2, ModelAndViewContainer var3, NativeWebRequest var4) throws Exception;
      }
      ```

      ```java
      //RequestResponseBodyMethodProcessor
      public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {
              mavContainer.setRequestHandled(true);
              ServletServerHttpRequest inputMessage = this.createInputMessage(webRequest);
              ServletServerHttpResponse outputMessage = this.createOutputMessage(webRequest);
          //使用消息转换器进行写出操作
              this.writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);
          }
      
      //利用MessageConverters进行处理，将数据写为json
          //1.内容协商：浏览器默认会以请求头的方式告诉服务器他能接受什么样的内容类型
          //2.服务器最终根据自己自身的能力，决定服务器能生产出什么样内容类型的数据
          //springMVC会挨个遍历所有容器底层的HttpMessageConverter,看谁能处理
      ```

      + springMVC支持哪些返回值（HandlerMethodReturnValueHandler接口实现类）

        ```txt
        ModelAndView
        Model
        View
        ResponseEntity
        ResponseBodyEmitter
        StreamingResponseBidy
        HttpEntity
        HttpHeaders
        Callable
        DeferredResult
        ListenableFuture
        CompletionStage
        WebAsyncTask
        有@ModleAttribute
        @ResponseBody注解 --->RequestResponseBodyMethodProcessor处理
        ```

      + HTTPMessageConverter原理

        HttpMessageConverter：看是否支持将此Class类型的对象，转为MediaType类型的数据

        例子：Person对象转为json，或者json转为Person

        默认的MessageConverter：AbstractMessageConverter实现类

        最终MappingJackson2HttpMessageConverter 把对象转为json(利用底层的jackson的objectMapper转换的)

      + 内容协商：根据客户端接受能力的不同，返回不同媒体类型的数据

        内容协商原理（AbstractMessageConverterMethodProcessor）

        + 判断当前响应头中是否已经有确定的媒体类型。MediaType
        + 获取客户端（Postman、浏览器）支持接收的内容类型。（获取客户端Accept请求头）
          + contentNegotiationManager 内容协商管理器 默认使用基于请求头的策略
          + HeaderContentNegotiationStrategy 确定客户端可以接受的内容类型

        + 遍历循环所有当前系统的MessageConverter,看谁支持操作这个对象（Person）
        + 找到支持操作Person的converter，把converter支持的媒体统计出来
        + 客户需要【application/xml】，服务端能力【10种】
        + 进行内容协商最佳匹配
        + 用支持将对象转为最佳匹配媒体类型的converter，调用它进行转化

        开启浏览器参数方式内容协商功能

        ```yaml
        #配置文件中加入以下配置
        spring:
        	mvc:
        		contentnegotiation:
        			favor-parameter: true
        ```

        请求路径中加入format=返回类型  localhost:8080/test/person?format=json

      + 自定义MessageConverter

        实现多协议数据兼容，json、xml、x-guigu(自定义协议)

        1. @ResponseBody 响应数据出去调用RequestResponseBodyMethodProcessor处理

        2. Processor处理方法返回值，通过MessageConverter处理

        3. 所有MessageConveter合起来可以支持各种媒体类型数据的操作（读、写）

        4. 内容协商找到最终的messageConveter

           

        浏览器发送请求返回xml   [application/xml]   jacksonXmlConverter

        如果是ajax请求返回json   [application/json]   jacksonJsonConverter

        如果返回自定义协议数据   [application/x-guigu]   xxxxConverter

        步骤：

        1. 添加自定义的MessageConveter进系统层
        2. 系统底层就会统计出所有MessageConverter能操作哪些类型
        3. 客户端内容协商

        ```java
        //将自定义MessageConverter添加到springboot中
        @Bean
        public WebMvcConfigurer webMvcConfigurer(){
            return new WebMvcConfigurer(){
                //自定义MessageConverter
                @Override
                public void extendMessageConverters(List<HttpMessageConverter<?>> converters){
                    converters.add(new MyConverter());
                }
                //自定义内容协商策略
               @Override
                public void configureContentNegotiation(ContentNegotiationConfigurer configurer){
        			Map<String,MediaType> mediaTypes=new HashMap<>();
                    mediaTypes.put("json",MediaType.APPLICATION_JSON);
                    mediaTypes.put("xml",MediaType.APPLICATION_XML);
                    mediaTypes.put("gg",MediaType.parseMediaType("application/x-guigu"));
             //指定支持解析哪些参数对应的哪些媒体类型，基于请求参数   localhost:8080/test/person?format=gg
                    ParameterContentNegotiationStrategy parameterStrategy=new ParameterContentNegotiationStrategy(mediaTypes);
                    //基于请求头
                    HeaderContentNegotiationStrategy headeStrategy=new HeaderContentNegotiationStrategy();
                    configurer.strategies(Arrays.asList(parameterStrategy,headeStrategy));
                }
            };
        }
        ```

        ```java
        //自定义MessageConverter
        public class MyMessageConverter implements HttpMessageConverter<User> {
            @Override
            public boolean canRead(Class aClass, MediaType mediaType) {
                return false;
            }
        
            @Override
            public boolean canWrite(Class aClass, MediaType mediaType) {
                return aClass.isAssignableFrom(User.class);
            }
        
            /**
             * 服务器要统计所有MessageConverter都能写出哪些内容类型
             * @return
             */
            @Override
            public List<MediaType> getSupportedMediaTypes() {
                return MediaType.parseMediaTypes("application/x-guigu");
            }
        
            @Override
            public User read(Class<? extends User> aClass, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
                return null;
            }
        
            @Override
            public void write(User user, MediaType mediaType, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
                //自定义协议数据的写出
                String data=user.getUsername()+";"+user.getPhone()+";"+user.getPassword();
                //写出去
                OutputStream body=httpOutputMessage.getBody();
                body.write(data.getBytes());
            }
        }
        ```

   5. 视图解析原理流程  前后端分离不再需要视图解析器

      1. 目标方法处理的过程中，所有数据都会被放在ModelAndViewContainer里面，包括数据和视图地址

      2. 方法的参数是一个自定义类型对象（从请求参数中确定的），把他重新放在ModelAndViewContainer

      3. 任何目标方法执行完以后都会返回ModelAndView

      4. processDispatchResult 处理派发结果（页面如何响应）

         1. render(mv,request,response);进行页面渲染逻辑

            根据方法的string返回值得到view对象[定义了页面的渲染逻辑]

            所有的视图解析器尝试是否能根据当前的返回值得到view对象

            得到了 redirect:/main.html -->Thymeleaf new RedirectView

            ContentNegotiationViewResolver里面包含了视图解析器，内容还是利用下面所有视图解析器得到视图对象

            view.render(mv.getModelInternal(),request,response);视图对象调用自定义的render进行页面渲染工作

            RedirectView如何渲染【重定向到一个页面】：1. 获取目标url地址 2. response.sendRedirect(encodedURL)

      5. 视图解析

         返回值以forward：开始 new internalResourceView(forwardUrl); --> 转发

         request.getRequestDispatcher(path).forward(request,response);

         返回值以redirect：开始：new RedirectView() --> render就是重定向

         返回值是普通字符串：new ThymeleafView() -->

      6. 自定义视图解析器+自定义视图

   6. 拦截器

      ```java
      //自定义拦截器
      /**
       * 配置好拦截器要拦截哪些请求
       * 把这些配置放在容器中
       */
      public class MyInteceptor implements HandlerInterceptor {
          //目标方法执行前
          @Override
          public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
              //登录检查逻辑
              HttpSession session=request.getSession();
              Object loginUser=session.getAttribute("loginUser");
              if (loginUser!=null)
                  //放行
                  return true;
              //拦截住。未登录，跳转到登录页
              session.setAttribute("msg","请先登录");
              response.sendRedirect("/");
              return false;
          }
          //目标方法执行完成后
          @Override
          public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
      
          }
          //视图渲染完之后
          @Override
          public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
      
          }
      }
      ```

      ```java
      //配置拦截器
      public class AdminWebMvcConfig implements WebMvcConfigurer {
      
          @Override
          public void addInterceptors(InterceptorRegistry registry) {
              registry.addInterceptor(new MyInteceptor())
                      .addPathPatterns("/**")  //所有请求都被拦截，包括静态资源
                      .excludePathPatterns("/","login","/css/**","/fonts/**","/images/**","/js/**");
          }
      }
      
      ```

      拦截器原理

      1. 根据当前请求，找到HandlerExecutionChain【可以处理请求的handler以及handler所有的拦截器】
      2. 先来顺序执行所有拦截器的preHandler方法
         + 如果当前拦截器prehandler返回为true，则执行下一个拦截器的preHandler
         + 如果当前拦截器prehandler返回为false,直接倒序执行所有已经执行的拦截器的 afterCompletion
      3. 如果任何一个拦截器返回false，直接跳出不执行目标方法
      4. 所有拦截器都返回true，执行目标方法
      5. 倒序执行所有拦截器的postHandle方法
      6. 前面的步骤有任何异常都会直接触发afterCompletion
      7. 页面成功渲染完成后，也会倒序触发afterCompletion

       ![](C:\Users\我的电脑\Desktop\笔记\img\springboot_1.png)

   7. 跨域

   8. 文件上传

      + 文件上传代码

      ```java
          @PostMapping("/upload2")
          @ResponseBody
          public void upload2(@RequestParam(value = "file") MultipartFile file,
                                @RequestParam(value = "files") MultipartFile[] files) throws IOException {
              if (!file.isEmpty()){
                  //保存到文件服务器，oss服务器
                  String originalFilename=file.getOriginalFilename();
                  file.transferTo(new File("D:\\cache\\"+originalFilename));
              }
      
              if (files.length>0){
                  for (MultipartFile file1:files) {
                      if (!file1.isEmpty()){
                          String originalFileName=file1.getOriginalFilename();
                          file1.transferTo(new File("D:\\cache\\"+originalFileName));
                      }
                  }
              }
          }
      ```

      + 自动配置原理

        文件上传自动配置类-MultipartAutoConfiguration-MultipartProperties

        自动配置好了StandardServletMultipartResolver  【文件上传解析器】

        原理步骤

        1. 请求进来使用文件上传解析器判断（isMultipart）并封装（resolveMultipart，返回MultipartHttpServletRequest）文件上传请求
        2. 参数解析器来解析请求中的文件内容封装成MultipartFile
        3. 将request中文件信息封装为一个map；MultiValueMap<String,MultipartFile>

   9. 异常处理

      默认情况下，spring boot提供/error处理所有错误的映射

      要对其进行自定义，添加view解析为error

      要完全替代默认行为，可实现ErrorController并注册该类型的Bean定义，或添加ErrorAttributes类型的组件以使现有机制替代其内容

      + 定制错误处理逻辑

        + 自定义错误页：error/404.html   error/5xx.html，有精确的错误状态码页面就精确匹配，没有就找4xx.html，如果都没有就触发白页
        + @ControllerAdvice+@ExceptionHandler处理异常，底层是ExceptionHandlerExceptionResolver支持的
        + @ResponseStatus+自定义异常；底层是ResponseStatusExceptionResolver,把responsestatus注解的信息组装成ModelAndView返回；底层调用response.sendError(StatusCode,resolvedReason);tomcat发送的/error
        + Spring底层的异常，如，参数类型异常:DefaultHandlerExceptionResolver处理框架底层的异常
        + ErrorViewResolver 实现自定义异常处理：response.sendError,error就会转给controller，你的异常没有任何人能处理，tomcat底层response.sendError，basicErrorController要去的页面地址是ErrorViewResolver;
        + 自定义实现HandlerExceptionResolver 处理异常，可以作为默认的全局异常处理规则

      + 异常处理自动配置原理

        + ErrorMvcAutoConfiguration 自动配置异常处理规则

          + 容器中组件：类型：DefaultErrorAttributes -> id: errorAttributes

            ```java
            // 返回页面包含的基本属性
            public class DefaultErrorAttributes implements ErrorAttributes, HandlerExceptionResolver, Ordered
               errorAttributes.put("status", 999);
               errorAttributes.put("error", "None");
             errorAttributes.put("exception", error.getClass().getName());
               errorAttributes.put("message",)
          
            ```

          + 容器中组件：类型：BasicErrorController -> id: basicErrorController（json+白页 适配响应）

            处理默认/erro路径的请求：页面响应new ModelAndView("error",model);

            容器中有组件View->id: error（响应默认错误页）

            容器中放组件BeanNameViewResolver(视图解析器)：按照返回的视图名作为组件的id去容器中找view对象

            如果想要返回页面；就会找error视图【StaticView】。（默认是一个白页）

          + 容器中组件：类型：DefaultErrorViewResolver -> id: conventionErrorViewResolver
          
            如果发生错误，会以http的状态码作为视图页地址（viewName）,找到真正的页面
          
            error/404.html   error/5xx.html

      + 异常处理流程步骤

        1. 执行目标方法，目标方法运行期间有任何异常都会被catch，而且标志当前请求结束；并且用dispatchException
        
        2. 进入视图解析流程（页面渲染）
        
           processDispatchResult(processedRequest,response,mappedHandler,mv,dispatchException);
        
        3. mv=processHandlerException;处理handler发生的异常，处理完成返回ModelAndView
        
           1. 遍历所有的handlerExceptionResolvers,看谁能处理当前异常【HandlerExceptionResolver处理器异常解析器】
           2. 系统默认的异常解析器：DefaultErrorAttributes,HandlerExceptionResolverComposite,ExceptionHandlerExceptionResolcer,ResponseStatusExceptionResolver,DefaultHandlerExceptionResolver
           3. DefaultErrorAttributes先来处理异常，把异常信息保存到request域，并且返回null;
           4. 默认没有任何人能处理异常，所以异常会被抛出
           5. 如果没有任何人能处理最终底层就会发送/error请求，会被底层的BasicErrorController处理
           6. 解析错误视图：遍历所有的ErrorViewResolver看谁能解析
           7. 默认的DefaultErrorViewResolver,作用是把响应状态码作为错误页的地址拼接成，error/500.html
           8. 模板引擎最终响应这个页面
        
        4. 

   10. 原生组件注入(Servlet、Filter、Listener)

       1. 使用Servlet API  注解

          @ServletComponentScan(basePackages="com.atguigu.admin");指定原生servlet组件都放在哪里

          @WebServlet(urlPatterns="/my");效果：直接响应，没有经过spring的拦截器

          @WebFilter(urlPatterns={"/css","/images/*"})

          @WebListener

          

          DispatchServlet如何注册进来

          + 容器中自动配置了DispatcherServlet属性绑定到WebMvcProperties;对应的配置文件配置项是spring.mvc
          + 通过ServletRegistrationBean<DispatcherServlet>把DispatcherServlet配置进来
          + 默认映射的是/路径

       2. 使用RegistrationBean

          

   11. 嵌入式web容器

       1. 切换嵌入式Servlet容器

          + 默认支持的srpwebserver

            Tomcat,jetty,Undertow. 

             ServletWebServerApplicationContext容器启动寻找ServletWebServerFactory并引导创建服务器

          + 切换服务器

            ```xml
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
                <exclusions>
                	<exclusion>
                    	<groupId>org.springframework.boot</groupId>
                		<artifactId>spring-boot-starter-tomcat</artifactId>
                    </exclusion>
                </exclusions>
            ```

            

          + 原理

            + spring boot应用启动发现当前是web应用，web场景包导入tomcat

            + web应用会创建一个web版的ioc容器ServletWebServerApplicationContext容器启动寻找ServletWebServerFactory（Servlet的web服务器工厂 --->Servlet的web服务器）

            + springboot底层默认有很多的WebServer工厂：TomcatServletWebServerFactory,JettyServletWebServerFactory,UndertowServletWebServerFactory

            + 底层直接会有一个自动配置类，ServletWebServerFactoryAutoConfiguration
            + ServletWebServerFactoryAutoConfiguration导入了ServletWebServerFactoryConfiguration(配置类)
            + ServletWebServerFactoryConfiguration 配置类 根据动态判断系统中到底导入了哪个web服务器的包。（默认是web-starter导入tomcat包），容器中就有了TomcatServletWebServerFactory
            + TomcatServletWebServerFactory 创建Tomcat服务器并启动，TomcatWebServer的构造器拥有初始化方法initialize---this.tomcat.start();
            + 内嵌服务器，就是手动把启动服务器的代码调用（tomcat核心jar包程序）

       2. 定制Servlet容器

          + 实现WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>

            把配置文件的值和ServletWebServerFactory进行绑定

          + 修改配置文件server.xxx

          + 直接自定义ConfigurableServletWebServerFactory

   12. 定制化原理

       1. 定制化的常见方式

          + 编写自定义的配置类xxxConfiguration;+@Bean替换、增加容器中的默认组件；视图解析器

          + 修改配置文件

          + xxxxCustomizer

          + web应用实现WebMvcConfigurer即可定制化功能

          + @EnableWebMvc+WebMvcConfigurer ——@Bean可以全面接管SpringMVC,所有的规则全部自己重新配置；实现定制和扩展功能

            原理：

            1. WebMvcAutoConfiguration 默认的springMVC的自动配置功能类、静态资源、欢迎页...

            2. 一旦使用@EnableWebMvc  会@Import(DelegatingWebMvcConfiguration.class)

            3. DelegatingWebMvcConfiguration的作用，只保证springmvc最基本的使用

               把系统中所有的WebMvcConfigurer拿过来，所有功能的定制都是这些WebMvcConfigurer合起来一起生效，自动配置了一些非常底层的组件、RequestMappringHandlerMapping、这些组件依赖的组件都是从容器中获取。

               ```java
               public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport
               ```

            4. WebMvcAutoConfiguration 里面的配置要都能生效，必须@ConditionalOnMissingBean(WebMvcConfiguraionSupport.class)

            5. @EnableWebMvc导致了WebMvcAutoConfiguration 没有生效

       2. 原理分析套路

          场景starter-xxxAutoConfiguration - 导入xxx组件 - 绑定xxxProperties -- 绑定配置文件项

3. 数据访问

   1. SQL

      + 导入jdbc场景

        ```xml
        <dependency>
        	<groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jdbc</artifactId>
        </dependency>
        ```

        数据库驱动？

        为什么导入jdbc场景，官方不导入驱动？官方不知道我们要操作什么数据库

        数据库版本和驱动版本要对应

        ```xml
        默认版本：<mysql.version>8.0.22</mysql.version>
        
        <dependency>
        	<groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <!--<version>5.1.49</version>-->
        </dependency>
        
        想要修改版本
        1.直接依赖引入具体版本（maven就近依赖原则）
        2.重新声明版本（maven属性的就近优先原则）
        <properties>
        	<mysql.version>5.1.49</mysql.version>
        </properties>
        ```

        

      + 分析自动配置

        1. 自动配置的类

           + DataSourceAutoConfiguration：数据源的自动配置

             修改数据源相关的配置：spring-datasource

             数据库连接池的配置，是自己容器中没有DataSource才自动配置

             底层配置好的连接池是：HiakriDataSource

           + DataSourceTransactionManagerAutoConfiguration：事务管理器的自动配置

           + JdbcTemplateAutoConfiguration：jdbcTemplate的自动配置

             可以修改这个配置项@ConfigurationProperties(prefix="spring.jdbc")来修改jdbcTemplate

             @Bean@Primary jdbcTemplate; 容器中有这个组件

           + JndiDataSourceAutoConfiguration：jndi的自动配置

           + XADataSourceAutoConfiguration：分布式事务相关的自动配置

        2. 

      + 修改配置项

        ```yaml
        spring:
        	datasource:
        		driverClassName: com.mysql.cj.jdbc.Driver
                url: jdbc:mysql://localhost:3306/portals_detector_manager?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false
                username: root
                password: root
        ```

        

   2. 使用druid数据源

      https://github.com/alibaba/druid

      + 引入druid-starter

        ```xml
        <dependency>
        	<groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.17</version>
        </dependency>
        ```

      + 分析自动配置

        + 扩展配置项 spring.datasource.druid

        + @Import({DruidSpringAopConfiguration.class, 监控springBean; 配置项：spring.datasource.druid.aop-patterns

        + DruidStatViewServletConfiguration.class, 监控页的配置：spring.datasource.druid.stat-view-servlet，默认开启

        + DruidWebStatFilterConfiguration.class,web监控配置：spring.datasource.druid.web-stat-filter，默认开启

        + DruidFilterConfiguration.class}) 所有druid自己filter的配置

          ```java
          private static final String FILTER_STAT_PREFIX = "spring.datasource.druid.filter.stat";
          private static final String FILTER_CONFIG_PREFIX = "spring.datasource.druid.filter.config";
          private static final String FILTER_ENCODING_PREFIX = "spring.datasource.druid.filter.encoding";
          private static final String FILTER_SLF4J_PREFIX = "spring.datasource.druid.filter.slf4j";
          private static final String FILTER_LOG4J_PREFIX = "spring.datasource.druid.filter.log4j";
          private static final String FILTER_LOG4J2_PREFIX = "spring.datasource.druid.filter.log4j2";
          private static final String FILTER_COMMONS_LOG_PREFIX = "spring.datasource.druid.filter.commons-log";
          private static final String FILTER_WALL_PREFIX = "spring.datasource.druid.filter.wall";
          private static final String FILTER_WALL_CONFIG_PREFIX = FILTER_WALL_PREFIX + ".config";
          ```

   3. 整合MyBatis操作

   4. 整合MyBatis-Plus完成crud

   5. NoSQL

   6. redis自动配置

      + RedisAutoConfiguration自动配置类。RedisProperties 属性类 --> spring.redis.xxx是对redis的配置
      + 连接工厂是准备好的。LettuceConnectionConfiguration,JedisConnectionConfiguration
      + 自动注入了RedisTemplate<Object,Object>:xxxtemplate; 
      + 自动注入了StringRedisTemplate
      + 底层只要使用StringRedisTemplate、RedisTemplate就可以操作redis

   7. RedisTemplate与lettuce

   8. 切换至jedis

4. 单元测试

   

5. 指标监控

6. 原理解析

7. springboot启动过程

### springboot配置log4j2

#### 避坑指南

```
由于springboot默认是用logback的日志框架的，所以需要排除logback，不然会出现jar依赖冲突的报错；
```

```xml
<!-- 排除 Spring-boot-starter 默认的日志配置 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-logging</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <!-- 引入log4j2依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-log4j2</artifactId>
        </dependency>
```

#### log4j2模板

```xml
<!--
Configuration后面的status，这个用于设置log4j2自身内部的信息输出，可以不设置，
当设置成trace时，可以看到log4j2内部各种详细输出
-->
<!--monitorInterval：Log4j能够自动检测修改配置 文件和重新配置本身，设置间隔秒数-->
<configuration monitorInterval="5">
    <!--日志级别以及优先级排序:
    OFF > FATAL > ERROR > WARN > INFO > DEBUG > TRACE > ALL
    -->

    <!--变量配置-->
    <Properties>
        <!--
            格式化输出：
            %d表示日期，
            %thread表示线程名，
            %-5level：级别从左显示5个字符宽度
            %msg：日志消息，%n是换行符
        -->
        <!--
        %logger{36} 表示 Logger 名字最长36个字符
        -->
        <property name="LOG_PATTERN" value="%d{yyyy-MM-dd HH:mm:ss.SSS} %highlight{%-5level}[%thread] %style{%logger{36}}{cyan} : %msg%n" />

        <!-- 定义日志存储的路径，不要配置相对路径 -->
        <property name="FILE_PATH" value="此处更换为你的日志文件存储路径" />
        <property name="FILE_NAME" value="此处更换为你的项目名称" />
    </Properties>

    <appenders>
        <console name="Console" target="SYSTEM_OUT">
            <!--输出日志的格式-->
            <PatternLayout pattern="${LOG_PATTERN}" disableAnsi="false" noConsoleNoAnsi="false"/>

            <!--控制台只输出level及其以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->
            <ThresholdFilter level="info" onMatch="ACCEPT" onMismatch="DENY"/>

        </console>

        <!--文件会打印出所有信息，这个log每次运行程序会自动清空，由append属性决定，适合临时测试用-->
        <File name="FileLog" fileName="${FILE_PATH}/test.log" append="false">
            <PatternLayout pattern="${LOG_PATTERN}"/>
        </File>

        <!--
        这个会打印出所有的info及以下级别的信息，每次大小超过size，
        则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，
        作为存档
        -->
        <RollingFile name="RollingFileInfo" fileName="${FILE_PATH}/info.log" filePattern="${FILE_PATH}/${FILE_NAME}-INFO-%d{yyyy-MM-dd}_%i.log.gz">
            <!--控制台只输出level及以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->
            <ThresholdFilter level="info" onMatch="ACCEPT" onMismatch="DENY"/>
            <PatternLayout pattern="${LOG_PATTERN}"/>
            <Policies>
                <!--interval属性用来指定多久滚动一次，默认是1 hour-->
                <TimeBasedTriggeringPolicy interval="1"/>
                <SizeBasedTriggeringPolicy size="20MB"/>
            </Policies>
            <!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件开始覆盖-->
            <DefaultRolloverStrategy max="15"/>
        </RollingFile>

        <!-- 这个会打印出所有的warn及以下级别的信息，每次大小超过size，则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档-->
        <RollingFile name="RollingFileWarn" fileName="${FILE_PATH}/warn.log" filePattern="${FILE_PATH}/${FILE_NAME}-WARN-%d{yyyy-MM-dd}_%i.log.gz">
            <!--控制台只输出level及以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->
            <ThresholdFilter level="warn" onMatch="ACCEPT" onMismatch="DENY"/>
            <PatternLayout pattern="${LOG_PATTERN}"/>
            <Policies>
                <!--interval属性用来指定多久滚动一次，默认是1 hour-->
                <TimeBasedTriggeringPolicy interval="1"/>
                <SizeBasedTriggeringPolicy size="20MB"/>
            </Policies>
            <!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件开始覆盖-->
            <DefaultRolloverStrategy max="15"/>
        </RollingFile>

        <!-- 这个会打印出所有的error及以下级别的信息，每次大小超过size，则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档-->
        <RollingFile name="RollingFileError" fileName="${FILE_PATH}/error.log" filePattern="${FILE_PATH}/${FILE_NAME}-ERROR-%d{yyyy-MM-dd}_%i.log.gz">
            <!--控制台只输出level及以上级别的信息（onMatch），其他的直接拒绝（onMismatch）-->
            <ThresholdFilter level="error" onMatch="ACCEPT" onMismatch="DENY"/>
            <PatternLayout pattern="${LOG_PATTERN}"/>
            <Policies>
                <!--interval属性用来指定多久滚动一次，默认是1 hour-->
                <TimeBasedTriggeringPolicy interval="1"/>
                <SizeBasedTriggeringPolicy size="20MB"/>
            </Policies>
            <!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件开始覆盖-->
            <DefaultRolloverStrategy max="15"/>
        </RollingFile>

    </appenders>

    <!--Logger节点用来单独指定日志的形式，比如要为指定包下的class指定不同的日志级别等。-->
    <!--然后定义loggers，只有定义了logger并引入的appender，appender才会生效-->
    <loggers>

        <!--过滤掉spring和mybatis的一些无用的DEBUG信息-->
        <logger name="org.mybatis" level="info" additivity="false">
            <AppenderRef ref="Console"/>
        </logger>
        <!--监控系统信息-->
        <!--若是additivity设为false，则 子Logger 只会在自己的appender里输出，而不会在 父Logger 的appender里输出。-->
        <Logger name="org.springframework" level="info" additivity="false">
            <AppenderRef ref="Console"/>
        </Logger>

        <root level="info">
            <appender-ref ref="Console"/>
            <appender-ref ref="FileLog"/>
            <appender-ref ref="RollingFileInfo"/>
            <appender-ref ref="RollingFileWarn"/>
            <appender-ref ref="RollingFileError"/>
        </root>
    </loggers>

</configuration>
```

#### 配置参数

1. 日志级别

```
trace：追踪，就是程序推进一下，可以写个trace输出
debug：调试，一般作为最低级别，trace基本不用。
info： 输出重要的信息，使用较多
warn： 警告，有些信息不是错误信息，但也要给程序员一些提示。
error：错误信息。用的也很多。
fatal：致命错误。
```

2. 输出源

```
CONSOLE（输出到控制台）
FILE   （输出到文件）
```

3. 格式

```
SimpleLayout： 以简单的形式显示
HTMLLayout：   以HTML表格显示
PatternLayout：自定义形式显示
```

4. PatternLayout自定义日志布局：

```
%d{yyyy-MM-dd HH:mm:ss, SSS} : 日志产生时间,输出到毫秒的时间
%-5level : 输出日志级别，-5表示左对齐并且固定输出5个字符，如果不足在右边补0
%c : logger的名称(%logger)
%t : 输出当前线程名称
%p : 日志输出格式
%m : 日志内容，即 logger.info("message")
%n : 换行符
%C : Java类名(%F)
%L : 行号
%M : 方法名
%l : 输出语句所在的行数, 包括类名、方法名、文件名、行数
hostName : 本地机器名
hostAddress : 本地ip地址
```

#### log4j2.xml配置解释

```
根节点Configuration
有两个属性:
status
monitorinterval
有两个子节点:

Appenders
Loggers(表明可以定义多个Appender和Logger).
status用来指定log4j本身的打印日志的级别.
monitorinterval用于指定log4j自动重新配置的监测间隔时间，单位是s,最小是5s.

Appenders节点
常见的有三种子节点:Console、RollingFile、File
Console节点用来定义输出到控制台的Appender.

name:指定Appender的名字.
target:SYSTEM_OUT 或 SYSTEM_ERR,一般只设置默认:SYSTEM_OUT.
PatternLayout:输出格式，不设置默认为:%m%n.
File节点用来定义输出到指定位置的文件的Appender.

name:指定Appender的名字.
fileName:指定输出日志的目的文件带全路径的文件名.
PatternLayout:输出格式，不设置默认为:%m%n.
RollingFile节点用来定义超过指定条件自动删除旧的创建新的Appender.

name:指定Appender的名字.
fileName:指定输出日志的目的文件带全路径的文件名.
PatternLayout:输出格式，不设置默认为:%m%n.
filePattern : 指定当发生Rolling时，文件的转移和重命名规则.
Policies:指定滚动日志的策略，就是什么时候进行新建日志文件输出日志.
TimeBasedTriggeringPolicy:Policies子节点，基于时间的滚动策略，interval属性用来指定多久滚动一次，默认是1 hour。modulate=true用来调整时间：比如现在是早上3am，interval是4，那么第一次滚动是在4am，接着是8am，12am...而不是7am.
SizeBasedTriggeringPolicy:Policies子节点，基于指定文件大小的滚动策略，size属性用来定义每个日志文件的大小.
DefaultRolloverStrategy:用来指定同一个文件夹下最多有几个日志文件时开始删除最旧的，创建新的(通过max属性)。
Loggers节点，常见的有两种:Root和Logger.
Root节点用来指定项目的根日志，如果没有单独指定Logger，那么就会默认使用该Root日志输出

level:日志输出级别，共有8个级别，按照从低到高为：All < Trace < Debug < Info < Warn < Error < AppenderRef：Root的子节点，用来指定该日志输出到哪个Appender.
Logger节点用来单独指定日志的形式，比如要为指定包下的class指定不同的日志级别等。
level:日志输出级别，共有8个级别，按照从低到高为：All < Trace < Debug < Info < Warn < Error < Fatal < OFF.
name:用来指定该Logger所适用的类或者类所在的包全路径,继承自Root节点.
AppenderRef：Logger的子节点，用来指定该日志输出到哪个Appender,如果没有指定，就会默认继承自Root.如果指定了，那么会在指定的这个Appender和Root的Appender中都会输出，此时我们可以设置Logger的additivity="false"只在自定义的Appender中进行输出。
```

