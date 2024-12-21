#### 什么是nginx

nginx是一个高性能的HTTP和反向代理服务器，特点是占有内存少，并发能力强，事实上nginx的并发能力确实在同类型的网页服务器中表现较好

nginx专为性能优化而开发，性能是其最重要的考量，实现上非常注重效率，能经受高负载的考验，有报告表面能支持高达50000个并发连接数

#### 正向代理

nginx不仅可以做反向代理，实现负载均衡，还能用作正向代理来进行上网等功能。

正向代理：如果把局域网外的Internet想象成一个巨大的资源库，则局域网中的客户端要访问Internet，则需要通过代理服务器来访问，这种代理服务就称为正向代理

#### 反向代理

反向代理，其实客户端对代理是无感知的，因为客户端不需要任何配置就可以访问，我们只需要将请求发送到反向代理服务器，由反向代理服务器去选择目标服务器获取数据后，在返回给客户端，此时反向代理服务器和目标服务器对外就是一个服务器，暴露的是代理服务器地址，隐藏了真实服务器IP地址

#### 负载均衡

单个服务器解决不了，我们增加服务器的数量，然后将请求分发到各个服务器上，将原先请求集中到单个服务器上的情况改为将请求分发到多个服务器上，将负载分发到不同的服务器，也就是我们所说的负载均衡

分配策略

1. 轮询（默认）

   每个请求按时间顺序逐一分配到不同的后端服务器，如果后端服务器down掉，能自动剔除

2. weight

   weight代表权重，默认为1，权重越高被分配的客户端越多

   指定轮询几率，weight和访问比率成正比，用于后端服务器性能不均的情况

3. ip_hash

   每个请求按访问ip的hash结果分配，这样每个访客固定访问一个后端服务器，可以解决session的问题。

4. fair(第三方)

   按后端服务器的响应时间来分配请求，响应时间短的优先分配

   ![](\img\nginx_5.png)

#### 动静分离

为了加快网站的解析速度，可以把动态页面和静态页面由不同的服务器来解析，加快解析速度，降低原来单个服务器的压力

nginx动静分离简单来说就是把动态跟静态请求分开，不能理解成只是单纯的把动态页面和静态页面物理分离。严格意义上说应该是动态请求跟静态请求分开，可以理解成使用nginx处理动态页面，tomcat处理动态页面，动静分离从目前实现角度来讲大致分为两种

一种是纯粹把静态文件独立成单独的域名，放在服务器上，也是目前主流推崇的方案；

另外一种方法就是动态跟静态文件混合在一起发布，通过nginx分开

#### nginx安装

https://blog.csdn.net/qq_42716761/article/details/126970218

#### nginx操作的常用命令

```shell
使用nginx操作命令前提条件：必须进入nginx的目录
/usr/local/nginx/sbin

查看nginx的版本号
./nginx -v

启动nginx
./nginx

关闭nginx
./nginx -s stop

重新加载nginx
./nginx -s reload
```

#### nginx配置文件

nginx配置文件位置

/usr/local/nginx/conf/nginx.conf

```shell
#user  nobody;
worker_processes  1;

#error_log  logs/error.log;
#error_log  logs/error.log  notice;
#error_log  logs/error.log  info;

#pid        logs/nginx.pid;


events {
    worker_connections  1024;
}


http {
    include       mime.types;
    default_type  application/octet-stream;

    #log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
    #                  '$status $body_bytes_sent "$http_referer" '
    #                  '"$http_user_agent" "$http_x_forwarded_for"';

    #access_log  logs/access.log  main;

    sendfile        on;
    #tcp_nopush     on;

    #keepalive_timeout  0;
    keepalive_timeout  65;

    #gzip  on;

    server {
        listen       80;
        server_name  localhost;

        #charset koi8-r;

        #access_log  logs/host.access.log  main;

        location / {
            root   html;
            index  index.html index.htm;
        }

        #error_page  404              /404.html;

        # redirect server error pages to the static page /50x.html
        #
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }

        # proxy the PHP scripts to Apache listening on 127.0.0.1:80
        #
        #location ~ \.php$ {
        #    proxy_pass   http://127.0.0.1;
        #}

        # pass the PHP scripts to FastCGI server listening on 127.0.0.1:9000
        #
        #location ~ \.php$ {
        #    root           html;
        #    fastcgi_pass   127.0.0.1:9000;
        #    fastcgi_index  index.php;
        #    fastcgi_param  SCRIPT_FILENAME  /scripts$fastcgi_script_name;
        #    include        fastcgi_params;
        #}

        # deny access to .htaccess files, if Apache's document root
        # concurs with nginx's one
        #
        #location ~ /\.ht {
        #    deny  all;
        #}
    }


    # another virtual host using mix of IP-, name-, and port-based configuration
    #
    #server {
    #    listen       8000;
    #    listen       somename:8080;
    #    server_name  somename  alias  another.alias;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}


    # HTTPS server
    #
    #server {
    #    listen       443 ssl;
    #    server_name  localhost;

    #    ssl_certificate      cert.pem;
    #    ssl_certificate_key  cert.key;

    #    ssl_session_cache    shared:SSL:1m;
    #    ssl_session_timeout  5m;

    #    ssl_ciphers  HIGH:!aNULL:!MD5;
    #    ssl_prefer_server_ciphers  on;

    #    location / {
    #        root   html;
    #        index  index.html index.htm;
    #    }
    #}

}
```

nginx配置文件由三部分组成

+ 全局块

  从配置文件开始到events块之间的内容，主要会设置一些影响nginx服务器整体运行的配置指令，主要包括配置运行nginx服务器的用户(组）、允许生成的worker process数，进程PID存放路径、日志存放路径和类型以及配置文件的引入等

  比如worker processes 1；worker processes值越大，可以支持的并发处理量也越多

+ events块

  events块涉及的指令主要影响nginx服务器与用户的网络连接

  比如worker connections 1024;表示每个worker process支持的最大连接数为1024，这部分对nginx的性能影响较大，在实践中应该灵活配置

+ http块

  这算是nginx服务器配置中最频繁的部分，代理、缓存和日志定义等绝大多数功能和第三方模块的配置都在这里。需要注意的是：http块也可以包括http全局块、server块

  + http全局块

    http全局配置的指令包括文件引入、MIME-TYPE定义、日志自定义、连接超时时间、单链接请求数上线等。

  + server块

    这块和虚拟主机有密切关系，虚拟主机从用户角度看，和一台独立的硬件主机是完全一样的，该技术的产生是为了节省互联网服务器硬件成本

    每个http块可以包括多个server块，而每个server块就相当于一个虚拟主机。而每个server块也分为全局server块，以及可以同时包含多个location块

    1. 全局server块

    最常见的配置是本虚拟主机的监听配置和本虚拟主机的名称或IP配置

    2. location块

    一个server块可以配置多个location块，这块的主要作用是基于nginx服务器接受的请求字符串（例如server_name/uri-string），对虚拟主机名称（也可以是IP别名）以外的字符串（例如前面的/uri-string）进行匹配，对特定的请求进行处理/地址定向、数据缓存和应答控制等功能，还有许多第三方模块的配置也在这里进行

#### nginx配置实例-反向代理

1. 实现效果

   打开浏览器在浏览器地址栏输入地址www.123.com，跳转到linux系统tomcat主页面中

2. 准备工作

   (1)在Linux系统安装tomcat，使用默认端口8080

   tomcat安装文件放到Linux系统中，解压

   进入tomcat的bin目录中，./startup.sh启动tomcat服务器

   (2)对外开放访问的端口

   firewall-cmd--add-port=8080/tcp --permanent

   firewall-cmd --reload

   (3)查看已经开放的端口

   firewall-cmd --list-all

3. 具体配置

   第一步 在windows系统的host文件进行域名和ip对应关系的配置

   第二步 在nginx进行请求转发的配置（反向代理配置）

   ![](\img\nginx_1.png)

#### nginx配置实例-反向代理实例2

1. 实现效果

   使用nginx反向代理，根据访问的路径跳转到不同端口的服务中，nginx监听端口为9001

   访问http://127.0.0.1:9001/edu/  直接跳转到127.0.0.1:8080

   访问http://127.0.0.1:9001/vod/  直接跳转到127.0.0.0:8081

2. 准备工作

   准备两个tomcat服务器，一个8080端口，一个8081端口

   创建文件夹和测试界面

3. 具体配置

   找到nginx配置文件，进行反向代理配置

   ![](\img\nginx_2.png)

   开放对外访问的端口号 9001 8080 8081

   ![](\img\nginx_3.png)

#### nginx配置示例-负载均衡

1. 实现效果

   浏览器地址栏输入http://192.168.17.129/edu/a.html，负载均衡效果，平均8080和8081端口中去

2. 准备工作

   准备两台tomcat服务器，一台是8080，一台是8081

   在两台tomcat里面webapps目录中，创建名称是edu文件夹，在edu文件夹中创建页面a.html，用于测试

3. 在nginx配置文件中进行负载均衡的配置

   ![](\img\nginx_4.png)

#### nginx配置实例-动静分离

![](\img\nginx_6.png)

#### nginx配置高可用的集群

![](\img\nginx_7.png)

1. 需要两台服务器

2. 两台服务器安装nginx，keepalived

   yum install keepalived -y

   安装之后，在etc里面生成目录keepalived，有文件keepalived.conf

#### nginx原理