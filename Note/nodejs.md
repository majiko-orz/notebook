Node.js是一个基于chrome V8引擎的JavaScript运行环境

**终端中的快捷键**

1. 使用↑键，可以快速定位到上一次执行的命令
2. 使用tab键，能够快速补全路径
3. 使用esc键，能够快速清空当前已输入的命令
4. 输入cls命令，可以清空终端

### fs文件系统模块

fs模块是Node.js官方提供的、用来操作文件的模块。它提供了一系列的方法和属性，用来满足用户对文件的操作需求，例如：

+ fs.readFile()方法，用来读取指定文件中的内容
+ fs.writeFile()方法，用来向指定的文件中写入内容

如果要在JavaScript代码中，使用fs模块来操作文件，则需要使用如下的方式先导入它：

```js
const fs = require('fs');
```

#### 读取指定文件中的内容

**fs.readFile()的语法格式**

```js
fs.readFile(path[, options],callback)
```

+ 参数1：必选参数，字符串，表示文件的路径
+ 参数2：可选参数，表示以什么编码格式来读取文件
+ 参数3：必选参数，文件读取完成后，通过回调函数拿到读取的结果。err失败的结果，dataStr成功的结果

```js
const fs = require('fs');
fs.readFile('./files/1.txt','utf8',function(err,dataStr){
    //打印失败的结果
    //如果读取成功，则err的值为null
    //如果读取失败，则err的值为错误对象，dataStr的值为undefined
    console.log(err);
    
    //打印成功的结果
    console.log(dataStr)
})
```

**判断文件是否读取成功**

可以判断err对象是否为null，从而知晓文件读取的结果

```js
const fs = require('fs');
fs.readFile('./files/1.txt','utf8',function(err,dataStr){
    if(err){
        return console.log(err);
    }
    
    console.log(dataStr)
})
```

#### 向指定的文件中写入内容

**fs.writeFile()的语法格式**

```js
fs.readFile(file,data[, options],callback)
```

+ 参数1：必选参数，需要指定一个文件路径的字符串，表示文件的存放路径
+ 参数2：必选参数，表示要写入的内容
+ 参数3：可选参数，表示以什么格式写入文件内容，默认是utf8
+ 参数4：必选参数，文件写入完成后的回调函数

```js
const fs = require('fs');

fs.writeFile('./file/2.txt','abcd',function(err){
    //如果文件写入成功，则err的值等于null
    //如果文件写入失败，则err的值等于一个错误对象
    console.log(err);
})
```

**判断文件是否写入成功**

可以判断err对象是否为null，从而知晓文件写入的结果

```js
const fs = require('fs');
fs.writeFile('./files/2.txt','abcd',function(err){
    if(err){
        return console.log('文件写入失败' + err.message)
    }
    
    console.log('文件写入成功')
})
```

#### fs模块-路径动态拼接的问题

在使用fs模块操作文件时，如果提供的操作路径是以./或../开头的相对路径时，很容易出现路径动态拼接错误的问题

原因：代码在运行的时候，会以执行node命令时所处的目录，动态拼接出被操作文件的完整路径

解决方案：在使用fs模块操作文件时，直接提供完整的路径，不要提供./或../开头的相对路径，从而防止动态路径拼接的问题，但移植性差，不利于维护

node提供**__dirname**变量表示当前文件所处的目录

### path路径模块

path模块是node.js官方提供的、用来处理路径的模块。它提供了一系列的方法和属性，用来满足用户对路径的处理需求，例如：

+ path.join()方法，用来将多个路径片段拼接成一个完整的路径字符串
+ path.basename()方法，用来从路径字符串中，将文件名解析出来

如果要在JavaScript代码中，使用path模块来处理路径，则需要使用如下的方式导入它：

```js
const path = require('path')
```

#### 路径拼接

**path.join()的语法格式**

使用path.join()方法，可以把多个路径片段拼接为完整的路径字符串

```js
path.join([...paths])
```

+ ...paths< string>路径片段的序列
+ 返回值：< string>

```js
const path = require('path')

//../会抵消前面一层路径c
const pathStr = path.join('/a','/b/c','../','./d','e')
console.log(pathStr) // \a\b\d\e

const pathStr = path.join('/a','/b/c','../../','./d','e')
console.log(pathStr) // \a\d\e

const pathStr = path.join(__dirname,'./files/1.txt')
```

注意：今后凡是涉及到路径拼接的操作，都要使用path.join()方法进行处理。不要直接使用+进行字符串拼接

#### 获取路径中的文件名

**path.basenmae()的语法格式**

使用path.basename()方法，可以获取路径中的最后一部分，经常通过这个方法获取路径中的文件名

```js
path.basename(path[, ext])
```

+ path< string>必选参数，表示一个路径的字符串
+ ext< string>可选参数，表示文件扩展名
+ 返回：< string>表示路径中的最后一部分

```js
const path = require('path')

const fpath = 'a/b/c/index.html'

var fullname = path.basename(fpath)
console.log(fullName) //输出index.html

var nameWithoutExt = path.basename(fpath,'.html')
console.log(nameWithoutExt) //输出index
```

#### 获取路径中的文件扩展名

**path.extname()的语法格式**

使用path.extname()方法，可以获取路径中的扩展名部分

```js
path.extname(path)
```

+ path< string>必选参数，表示一个路径的字符串
+ 返回：< string>返回得到的扩展名字符串

```js
const path = require('path')

const fpath = 'a/b/c/index.html'

const fext = path.extname(fpath)
console.log(fext) //输出.html
```

### http模块

http模块是node.js官方提供的，用来创建web服务器的模块。通过http模块提供的http.createServer()方法，就能方便的把一台普通的电脑，变成一台web服务器，从而对外提供web资源服务

如果希望使用http模块创建web服务器，则需要先导入它：

```js
const http = require('http')
```

#### 创建最基本的web服务器

**创建web服务器的基本步骤**

1. 导入http模块
2. 创建web服务器实例
3. 为服务器绑定request事件，监听客户端的请求
4. 启动服务器

```js
const http = require('http')

const server = http.createServer()

//使用on()方法，为服务器绑定一个request事件
server.on('request',(req,res) => {
    console.log('someone visit our web server')
})

//调用server.listen(端口号，cb回调)方法，即可启动web服务器
server.listen(80,() => {
    console.log('http server running at http://127.0.0.1')
})
```

**req请求对象**

只要服务器接收到了客户端的请求，就会调用server.on()为服务器绑定的request事件处理函数

如果想在事件处理函数中，访问与客户端相关的数据或属性，可以使用如下的方式：

```js
server.on('request',(req) => {
    //req是请求对象，它包含了与客户端相关的数据和属性
    //req.url是客户端请求的url地址
    //req.method是客户端的method请求类型
    const str = 'your request url is ${req.url},and request method is ${req.method}'
    console.log(str)
})
```

**res响应对象**

在服务器的request事件处理函数中，如果想访问与服务器相关的数据或属性，可以使用如下的方式：

```js
server.on('request',(req,res) => {
    //req是响应对象，它包含了与服务器相关的数据和属性
    const str = 'your request url is ${req.url},and request method is ${req.method}'
    //res.end()方法的作用：
    //向客户端发送指定的内容，并结束这次请求的处理过程
    res.end(str)
})
```

**解决中文乱码问题**

当调用res.end()方法时，向客户端发送中文内容时，会出现乱码问题，此时，需要手动设置内容的编码格式

```js
server.on('request',(req,res) => {
    //req是响应对象，它包含了与服务器相关的数据和属性
    const str = 'your request url is ${req.url},and request method is ${req.method}'
    //为了防止中文显示乱码的问题，需要设置响应头Content-Type的值为text/html;charset=utf-8
    res.setHeader('Content-Type','text/html;charset=utf-8')
    res.end(str)
})
```

#### 根据不同的url响应不同的html内容

**实现步骤**

1. 获取请求的url地址
2. 设置默认的相应内容为404 Not found
3. 判断用户请求的是否为/或/index.html首页
4. 判断用户请求的是否为/about.html关于页面
5. 设置Content-Type响应头，防止中文乱码
6. 使用res.end()把内容响应给客户端

```js
server.on('request',function(req,res) {
    const url = req.url
    let content = '<h1>404 Not found</h1>'
    if(url === '/' || url === '/index.html'){
        content = '<h1>首页</h1>'
    }else if(url === '/about.html'){
        content = '<h1>关于页面</h1>'
    }
    res.setHeader('Content-Type','text/html;charset=utf-8')
    res.end(content)
}
```

### 模块化

模块化是指解决一个复杂问题时，自顶向下逐层把系统划分为若干模块的过程。对于整个系统来说，模块是可组合、分解和更换的单元

编程领域中的模块化，就是遵守固定的规则，把一个大文件拆成独立并相互依赖的多个小模块

#### node.js中模块的分类

node.js中根据模块来源的不同，将模块分为了3大类，分别是：

+ 内置模块（内置模块是由node.js官方提供的，例如fs、path、http等）
+ 自定义模块（用户创建的每个js文件，都是自定义模块）
+ 第三方模块（由第三方开发出来的模块，使用前需要先下载）

#### 加载模块

使用强大的require()方法，可以加载需要的内置模块、用户自定义模块、第三方模块进行使用。例如：

```js
//加载内置的fs模块
const fs = require('fs')

//加载用户自定义模块
const custom = require('./custom.js')

//加载第三方模块
const moment = require('moment')
```

注意：使用require()方法加载其他模块时，会执行被加载模块中的代码

#### node.js中的模块作用域

和函数作用域类似，在自定义模块中定义的变量、方法等成员，只能在当前模块内被访问，这种模块级别的访问限制，叫做模块作用域

好处：防止了全局变量污染的问题

#### 向外共享模块作用域中的成员

**module对象**

在每个.js自定义模块中都有一个module对象，它里面存储了和当前模块有关的信息

**module.exports对象**

在自定义模块中，可以使用module.exports对象，将模块内的成员共享出去，供外界使用.

外界require()方法名导入自定义模块时，得到的就是module.exports所指的对象

**exports对象**

node提供了exports对象，默认情况下，exports和module.exports指向同一个对象，最终共享的结果，还是以module.exports指向的对象为准

**exports和module.exports的使用误区**

时刻谨记，require()模块时，得到的永远都是module.exports指向的对象

#### node.js中的模块化规范

node.js遵循了CommonJS模块化规范，CommonJS规定了模块的特性和各模块间如何相互依赖

CommonJS规定：

1. 每个模块内部，module变量代表当前模块
2. module变量是一个对象，它的exports属性（即module..exports）是对外的接口
3. 加载某个模块，其实是加载该模块的module.exports属性。require()方法用于加载模块

### npm与包

node.js中的第三方模块又叫做包

包是基于内置模块封装出来的，提供了更高级、更方便的API，极大的提高了开发效率

下载包：

+ 从https://www.npmjs.com/网站上搜索自己需要的包
+ 从https://registry.npmjs.org/服务器上下载自己需要的包

Node Package Manager 简称npm包管理工具，随node.js安装包一起被安装到用户电脑上，npm -v 查看npm包管理工具版本号

**在项目中安装指定名称的包**

```js
npm install 包的完整名称
//简写
npm i 包的完整名称
```

**导入包**

```js
require('包的名称')
```

**初次装包后多了哪些文件**

+ node_modules文件夹：存放所有已安装到项目中的包。require()导入第三方包时，就是从这个目录中查找并加载包
+ package-lock.json配置文件：用来记录node_modules目录下每一个包的下载信息，例如包的名字、版本号、下载地址等

**安装指定版本的包**

默认情况下，使用npm install 命令安装包的时候，会自动安装最新版本的包。如果需要安装指定版本的包，可以在包名之后，通过@符号指定具体的版本，例如：

```js
npm i moment@2.22.2
```

**包的语义化版本规范**

第1位数字：大版本

第2位数字：功能版本

第3位数字：bug修复版本

#### 包管理配置文件

npm规定，在项目根目录中，必须提供一个叫做package.json的包管理配置文件，用来记录与项目有关的一些配置信息。例如：

+ 项目的名称、版本号、描述等
+ 项目中都用到了哪些包
+ 哪些包只在开发期间会用到
+ 哪些包在开发和部署时都需要用到

**多人协作的问题**

第三方包的体积过大，不方便团队成员之间共享项目源代码

解决方案：共享时剔除node_modules

**如何记录项目中安装了哪些包**

在项目根目录中，创建一个叫做package.json的配置文件，即可用来记录项目中安装了哪些包。从而方便剔除node_modules目录后，在团队成员之间共享项目的源代码

注意：在项目开发中，一定要把node_modules文件夹，添加到.gitignore忽略文件中

**快速创建package.json**

npm包管理工具提供了一个快捷命令，可以在执行命令时所处的目录中，快速创建package.json这个包管理配置文件

```js
npm init -y
```

上述命令只能在英文的目录下成功运行，所以，项目文件夹的名称一定要使用英文命名，不要使用中文，不要出现空格

运行npm install命令安装包的时候，npm包管理工具会自动把包的名称和版本号，记录到package.json中去

**dependencies节点**

package.json文件中，有一个dependencies节点，专门用来记录您使用npm install命令安装了哪些包

**一次性安装所有的包**

```js
npm install 或 npm i
```

执行npm install命令时，npm包管理工具会先读取package.json中的dependencies节点，读取到记录的所有依赖包名称和版本号之后，包管理工具会把这些包一次性下载到项目中

**卸载包**

```
npm unstall 包名
```

npm uninstall命令执行成功后，会把卸载的包，自动从package.json的dependencies中移除掉

**devDependencies节点**

如果某些包只在项目开发阶段会用到，在项目上线之后不会用到，则建议把这些包记录到devDependencies节点中

```js
npm i 包名 -D

//注意：上述命令是简写形式，等价于下面完整的写法
npm install 包名 --save-dev
```

**解决下包速度慢的问题**

切换npm的下包镜像源

```js
//查看当前的下包镜像源
npm config get registry

//将下包的镜像源切换为淘宝镜像源
npm config set registry=https://registry.npm.taobao.org/

//检查镜像源是否下载成功
npm config get registry
```

**nrm**

为了更方便的切换下包的镜像源，我们可以安装nrm这个小工具，利用nrm提供的终端命令，可以快速查看和切换下包的镜像源

```js
//通过npm包管理工具，将nrm安装为全局可用的工具
npm i nrm -g

//查看所有可用的镜像源
nrm ls

//将下包的镜像源切换为taobao镜像
nrm use taobao
```

#### **包的分类**

1. 项目包：被安装到项目的node_modules目录中的包，都是项目包

   + 开发依赖包：被记录到devDependencies节点中的包，只在开发期间会用到
   + 核心依赖包：被记录到dependencies节点中的包，在开发期间和项目上线之后都会用到

2. 全局包：在执行npm install命令时，如果提供了-g参数，则会把包安装为全局包

   全局包会被安装到C:\User\用户目录\AppData\Roaming\npm\node_modules目录下

   ```js
   npm i 包名 -g
   npm uninstall 包名 -g
   ```

   只有工具性质的包，才有全局安装的必要性，因为他们提供了好用的终端命令

3. i5ting_toc：i5ting_toc是一个可以把md文档转为html页面的小工具，使用步骤如下：

   ```js
   npm install -g i5ting_toc
   
   //调用i5ting_toc，轻松实现md转html的功能
   i5ting_toc -f 要转换的md文件路径 -o
   ```

#### 规范的包结构

一个规范的包，他的组成结构，必须符合以下3点要求：

1. 包必须以单独的目录而存在
2. 包的顶级目录下要必须包含package.json这个包管理文件
3. package.json中必须包含name,version,main这三个属性，分别代表包的名字、版本号、包的入口

### 模块的加载机制

#### 优先从缓存中加载

模块在第一次加载后会被缓存。这也意味着多次调用require()不会导致模块的代码被执行多次。

注意：不论是内置模块、用户自定义模块、还是第三方模块，他们都会优先从缓存中加载，从而提高模块的加载效率

#### 内置模块的加载机制

内置模块是node.js官方提供的模块，内置模块的加载优先级最高

例如，require('fs')始终返回内置的fs模块，即使在node_modules目录下有名字相同的包也叫fs

#### 自定义模块的加载机制

使用require()加载自定义模块时，必须指定以./或../开头的路径标识符。在加载自定义模块时，如果没有指定./或../这样的路径标识符，则node会把它当作内置模块或第三方模块进行加载

同时，在使用require()导入自定义模块时，如果省略了文件的扩展名，则node.js会按顺序分别尝试加载以下的文件：

1. 按照确切的文件名进行加载
2. 补全.js扩展名进行加载
3. 补全.json扩展名进行加载
4. 补全.node扩展名进行加载
5. 加载失败，终端报错

#### 第三方模块的加载机制

如果传递给require()的模块标识符不是一个内置模块，也没有./或../开头，则node.js会从当前模块的父目录开始，尝试从/node_modules文件夹中加载第三方模块

如果没有找到对应的第三方模块，则移动到再上一层父目录中，进行加载，直到文件系统的根目录

#### 目录作为模块

当把目录作为模块标识符，传递给require()进行加载的时候，有三种加载方式：

1. 在被加载的目录下查找一个叫做package.json的文件，并寻找main属性，作为require()加载的入口
2. 如果目录里没有package.json文件，或者main入口不存在或无法解析，则node.js将会试图加载目录下的index.js文件
3. 如果以上两步都失败了，则node.js会在终端打印错误信息，报告模块的缺失：Error:Cannot find module 'xxx'

### express

express是基于node.js平台，快速、开放、极简的web开发框架

#### express基本使用

**安装**

```js
npm i express@4.17.1
```

**创建基本的web服务器**

```js
//1.导入express
const express = require('express')
//2.创建web服务器
const app = express()
//3.调用app.listen(端口号，启动成功后的回调函数)，启动服务器
app.listen(80,()=>{
    console.log('express server running at http://127.0.0.1')
})
```

**监听GET请求**

通过app.get()方法，可以监听客户端的GET请求

```js
//参数1：客户端请求的url地址
//参数2：请求对应的处理函数
//req:请求对象
//res:响应对象
app.get('请求url',function(req,res){})
```

**监听POST请求**

通过app.post()方法，可以监听客户端的POST请求

```js
//参数1：客户端请求的url地址
//参数2：请求对应的处理函数
//req:请求对象
//res:响应对象
app.post('请求url',function(req,res){})
```

**把内容响应给客户端**

通过res.send()方法，可以把处理好的内容，发送给客户端

```js
app.get('/user',(req,res)=>{
    res.send({name:'zs',age:20,gender:'男'})
})

app.post('/user',(req,res)=>{
    res.send('请求成功')
})
```

**获取url中携带的查询参数**

通过req.query对象，可以访问到客户端通过查询字符串的形式，发送到服务器的参数

```js
app.get('/',(req,res)=>{
    //req.query默认是一个空对象
    //客户端使用?name=zs&age=20这种查询字符串形式，发送到服务器的参数
    //可以通过req.query对象访问到，例如
    //req.query.name
    console.log(req.query)
})
```

**获取url中的动态参数**

通过req.params对象，可以访问到url中，通过:匹配到的动态参数

```js
app.get('/user/:id',(req,res)=>{
    //req.params默认是一个空对象
    //里面存放着通过:动态匹配到的参数值
    console.log(req.params)
})
```

#### 托管静态资源

**express.static**

express提供了一个非常好用的函数，叫做express.static()，通过它，我们可以非常方便地创建一个静态资源服务器，例如，通过以下代码就可以将public目录下的图片、CSS文件、JavaScript文件对外开放访问了

```js
app.use(express.static('public'))
```

现在就可以访问public目录中的所有文件了

http://localhost:3000/images/bg.jpg

http://localhost:3000/css/style.css

http://localhost:3000/js/login.js

注意：express在指定的静态目录中查找文件，并对外提供资源的访问路径，因此，存放静态文件的目录名不会出现在url中

**托管多个静态资源目录**

```js
app.use(express.static('public'))
app.use(express.static('files'))
```

访问静态资源文件时，express.static()函数会根据目录的添加顺序查找所需的文件

**挂载路径前缀**

如果希望在托管的静态资源访问路径之前，挂载路径前缀，则可以使用如下的方式

```js
app.use('/public',express.static('public'))
```

#### nodemon

在编写调试node.js项目的时候，如果修改了项目的代码，需要频繁的手动close掉，然后再重新启动，非常繁琐

nodemon能够监听项目文件的变动，当代被修改后，nodemon会自动帮我们重启项目，极大方便了开发和调试

**安装nodemon**

```js
npm install -g nodemon
```

**使用nodemon**

```
nodemon 启动的项目
```

#### express路由

路由：广义上讲就是映射关系

express中的路由指的是客户端的请求与服务器处理函数之间的映射关系

express中的路由分三部分组成，分别是请求的类型、请求的url地址、处理函数，格式如下

```js
app.METHOD(PATH,HANDLER)

//例如
app.get('/',function(req,res) {
    res.send('Hello world')
})
app.get('/',function(req,res) {
    res.send('Hello world')
})
```

**路由的匹配过程**

当一个请求到达服务器之后，需要先经过路由的匹配，只有匹配成功之后，才会调用对应的处理函数

在匹配时，会按照路由的顺序进行匹配，如果请求类型和url同时匹配成功，则express会将这次请求，转交给对应的function函数进行处理

**路由的使用**

1. 最简单的用法

   在express中使用路由最简单的方式就是把路由挂载到app上

   ```js
   
   const express = require('express')
   
   const app = express()
   
   app.get('/',(req,res)=>{})
   app.listen(80,()=>{
       console.log('express server running at http://127.0.0.1')
   })
   ```

2. 模块化路由

   为了方便对路由进行模块化的管理，express不建议将路由直接挂载到app上，而是推荐将路由抽离为单独的模块，将路由抽离为单独模块的步骤如下：

   1. 创建路由模块对应的.js文件
   2. 调用express.Router()函数创建路由对象
   3. 向路由对象上挂载具体的路由
   4. 使用module.exports向外共享路由对象
   5. 使用app.use()函数注册路由模块

3. 创建路由模块

   ```js
   //./router/user.js
   var express = require('express')
   var router = express.Router()
   
   router.get('/user/list',function(req.res){
       res.send('get user list')
   })
   router.post('/user/list',function(req.res){
       res.send('post user list')
   })
   module.exports = router
   ```

4. 注册路由模块

   ```js
   //1.导入路由模块
   const userRouter = require('./router/user.js')
   
   //2.使用app.use()注册路由模块
   app.use(usesRouter)
   ```

5. 为路由模块添加前缀

   ```js
   const userRouter = require('./router/user.js')
   
   app.use('/api',userRouter)
   ```

#### express中间件

当一个请求到达express的服务器之后，可以连续调用多个中间件，从而对这次请求进行预处理

**express中间件的格式**

express中间件，本质上就是一个function处理函数，express中间件的格式如下：

![](C:\Users\admin\Desktop\个人资料\笔记\img\nodejs_1.png)

中间件函数的形参列表中，必须包含next参数，而路由处理函数中只包含req和res

**next函数的作用**

next函数是实现多个中间件连续调用的关键，它表示把流转关系转交给下一个中间件或路由

**定义中间件**

```js
const mw = function(req,res,next){
    console.log('简单的中间件函数')
    next()
}
```

**全局生效的中间件**

客户端发起任何请求，到达服务器后，都会触发的中间件，叫做全局生效的中间件

通过调用app.use(中间件函数)，即可定义一个全局生效的中间件

```js
const mw = function(req,res,next){
    console.log('简单的中间件函数')
    next()
}

app.use(mw)
```

**全局中间件的简化形式**

```js
app.use(unction(req,res,next){
    console.log('简单的中间件函数')
    next()
})
```

**中间件的作用**

多个中间件之间，共享同一份req和res，我们可以在上游的中间件中，统一为req或res对象添加自定义的属性或方法，供下游的中间件或路由进行使用

**定义多个全局中间件**

可以使用app.use()连续定义多个全局中间件。客户端请求到达服务器之后，会按照中间件定义的先后顺序依次进行调用

**局部生效的中间件**

不使用app.use()定义的中间件，叫做局部生效的中间件

```js
app.get('/',mw,function(req,res){
    res.send('Home page.')
})
```

**定义多个局部中间件**

```js
app.get('/',mw1,mw2,function(req,res){res.send('Home page.')})

app.get('/',[mw1,mw2],function(req,res){res.send('Home page.')})
```

**中间件使用的5个注意事项**

1. 一定要在路由之前注册中间件
2. 客户端发过来的请求，可以连续调用多个中间件进行处理
3. 执行完中间件的业务代码后，不要忘记调用next()函数
4. 为了防止代码逻辑混乱，调用next()函数后不要再写额外的代码
5. 多个中间件之间，共享同一份req和res

**中间件的分类**

express官方把常见的中间件用法，分为了5大类，分别是：

1. 应用级别的中间件
2. 路由级别的中间件
3. 错误级别的中间件
4. express内置的中间件
5. 第三方的中间件

**应用级别的中间件**

通过app.use()或app.get()或app.post()绑定到app实例上的中间件，叫做应用级别的中间件

```js
//应用级别的中间件（全局中间件）
app.use((req,res,next) => {
    next();
})
//应用级别的中间件（局部中间件）
app.get('/',mw,(req,res) => {
    res.send('Home page.')
})
```

**路由级别的中间件**

绑定到express.Router()实例上的中间件，叫做路由级别的中间件。他的用法和应用级别中间件没有任何区别。只不过，应用级别中间件是绑定到app实例上，路由级别中间件绑定到router实例上

```js
var app = express()
var router = express.Router()

router.use(function(req,res,next) {
    next()
})

app.use('/',router)
```

**错误级别的中间件**

错误级别中间件的作用：专门用来捕获整个项目中发生的异常错误，从而防止项目异常崩溃的问题

格式：错误级别中间件的function处理函数中，必须有4个形参，形参顺序从前到后，分别是（err,req,res,next）

```js
app.get('/',function(req,res) {
throw new Error('服务器内部发生了错误')
    res.send('Home page')
})
app.use(function(err,req,res,next){
    console.log('发生了错误：'+err.message)
    res.send('error'+err.message)
})
```

**express内置的中间件**

自express4.16.0版本开始，express内置了三个常用的中间件，极大的提高了express项目的开发效率和体验

1. express.static：快速托管静态资源的内置中间件，例如：html文件、图片、css样式等（无兼容性）
2. express.json：解析json格式的请求体数据（有兼容性，仅在4.16.0+版本中可用）
3. express.urlencoded：解析url-encoded格式的请求体数据（有兼容性，仅在4.16.0+版本中可用）

```js
app.use(express.json())
app.use(express.urlencoded({extended: false}))
```

**第三方的中间件**

例如body-parser中间件

1. 运行npm install body-parser安装中间件
2. 通过require导入中间件
3. 调用app.use()注册并使用中间件

**自定义中间件**

```js
app.use(function(req,res,next){
    //中间件的业务逻辑
})
```
