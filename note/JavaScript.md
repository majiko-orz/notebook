### JS基础

基础知识

**JavaScript是什么**

+ JavaScript是一种运行在客户端的脚本语言
+ 脚本语言：不需要编译，运行过程中由js解释器（js引擎）逐行来进行解释并执行

**浏览器执行JS**

浏览器分为两部分：渲染引擎和JS引擎

+ 渲染引擎：用来解析HTML和CSS，俗称内核，比如chrome浏览器的blink，老版本的webkit
+ JS引擎：也称为JS解释器。用来读取网页中的JavaScript代码，对其处理后运行，比如chrome浏览器的V8

浏览器本身并不会执行JS代码，而是通过内置JavaScript引擎（解释器）来执行JS代码。JS引擎执行代码时逐行解释每一句源码（转换为机器语言），然后由计算机去执行，所以JavaScript语言归为脚本语言，会逐行解释执行

**JS三种书写位置**

JS有三种书写位置，分别是行内、内嵌和外部。

1.行内式的js 直接写到元素的内部

```html
<input type="button" value="唐伯虎" onclick="alert('秋香')">
```

+ 可以将单行或少量JS代码写在html标签的事件属性中（以on开头的属性），如onclick
+ 注意单双引号的使用：在HTML中我们推荐使用双引号，JS中我们推荐使用单引号
+ 可读性差，在html中编写大量JS代码时，不方便阅读；
+ 引号易错，引号多层嵌套匹配时，非常容易弄混；
+ 特殊情况下使用

2.内嵌式

```html
<script>
	alert('亚索');
</script>
```

3.外部js

```html
<script src="my.js"></script>
```

+ 引用外部JS文件的script标签中间不可以写代码
+ 适合JS代码量较大的情况

#### JavaScript输入输出语句

| 方法             | 说明                           | 归属   |
| ---------------- | ------------------------------ | ------ |
| alert(msg)       | 浏览器弹出警示框               | 浏览器 |
| console.log(msg) | 浏览器控制台打印输出信息       | 浏览器 |
| prompt(info)     | 浏览器弹出输入框，用户可以输入 | 浏览器 |

#### 变量

变量在使用时分为两步：

1. 声明变量

   ```js
   var age;
   ```

   + var是一个JS关键字，用来声明变量。使用该关键字声明变量后，计算机会自动为变量分配内存空间
   + age是程序员定义的变量名，我们通过变量名来访问内存中分配的空间

2. 赋值

   ```js
   age = 10;
   ```

   + =用来把右边的值赋给左边的变量空间中 此处代表赋值的意思
   + 变量值是程序员保存到变量空间里的值

3. 变量的初始化

   ```js
   var age=10;
   ```

   声明一个变量并赋值，我们称之为变量的初始化

同时声明多个变量时，只需要写一个var，多个变量名之间使用英文逗号隔开。

```js
var age = 10, name = 'zs', sex = 2;
```

声明变量特殊情况

| 情况                       | 说明           | 结果      |
| -------------------------- | -------------- | --------- |
| var age; console.log(age); | 只声明，不赋值 | undefined |
| console.log(age)           | 不声明 不赋值  | 报错      |
| age = 10;console.log(age); | 不声明 只赋值  | 10        |

**变量命名规范**

+ 由字母（A-Za-z）、数字（0-9）、下划线（_）、美元符号（$）组成
+ 严格区分大小写
+ 不能以数字开头
+ 不能是关键字
+ 变量名必须有意义
+ 遵循驼峰命名法

#### 数据类型

JS是一种弱类型或者说动态语言。JS的变量数据类型是只有程序在运行过程中，根据等号右边的值来确定的

**数据类型的分类**

JS把数据类型分为两类：

+ 简单数据类型（Number,String,Boolean,Undifined,Null）
+ 复杂数据类型（object）

| 简单数据类型 | 说明                                     | 默认值    |
| ------------ | ---------------------------------------- | --------- |
| Number       | 数字型，包含整型值和浮点型值，如21、0.21 | 0         |
| Boolean      | 布尔值类型，如true、false                | false     |
| String       | 字符串类型                               | ""        |
| Undefined    | var a;声明了变量a但是没有给值            | undefined |
| Null         | var a = null; 声明了变量a 为空值         | null      |

数字前面加0表示八进制，加0x表示十六进制

数字型的最大值：Number.MAX_VALUE，1.7976931348623157e+308

数字型的最小值：Number.MIN_VALUE，5e-324

Infinity，代表无穷大，大于任何数值

-Infinity，代表无穷小，小于任何数值

NaN，not a number，代表一个非数值

isNaN() 这个方法用来判断非数字，如果是是数字返回false，如果不是返回true

JS字符串可以用单引号双引号，推荐使用单引号

JS可以用单引号嵌套双引号，或者用双引号嵌套单引号（外双内单，外单内双）

字符串转义符：

| 转义符 | 解释说明                 |
| ------ | ------------------------ |
| \n     | 换行符，n是newline的意思 |
| \ \    | 斜杠\                    |
| \ '    | '单引号                  |
| \ "    | "双引号                  |
| \t     | tab缩进                  |
| \b     | 空格，b是blank的意思     |

字符串长度：str.length；字符串拼接：+

undefined和数字相加，最后结果是NaN

判断一个变量类型：typeof  变量名

数据类型转换：

转为字符串

| 方式             | 说明                         | 案例                           |
| ---------------- | ---------------------------- | ------------------------------ |
| toString         | 转成字符串                   | var num=1; alert(num.toString) |
| String()强制转换 | 转成字符串                   | var num=1; alert(String(num))  |
| 加号拼接字符串   | 和字符串拼接的结果都是字符串 | var num=1; alert(num+"字符串") |

转为数字型（重点）

| 方式                   | 说明                         | 案例                |
| ---------------------- | ---------------------------- | ------------------- |
| parseInt(string)函数   | 将string类型转成整数数值型   | parseInt('78')      |
| parseFloat(string)函数 | 将string类型转成浮点数数值型 | parseFloat('78.21') |
| Number()强制转换函数   | 将string类型转成数值型       | Number('12')        |
| js隐式转换（- * /）    | 利用算术运算隐式转换为数值型 | '12' - 0            |

转换为布尔型

| 方式      | 说明               | 案例            |
| --------- | ------------------ | --------------- |
| Boolean() | 其他类型转成布尔型 | Boolean('true') |

代表空、否定的值会被转换为false，如''、0、NaN、null、undefined，其余值会转换为true

#### JS运算符

**算术运算符**

| 运算符 | 描述           | 实例                     |
| ------ | -------------- | ------------------------ |
| +      | 加             | 10 + 20 = 30             |
| -      | 减             | 10 - 20 = -10            |
| *      | 乘             | 10 * 20 = 200            |
| /      | 除             | 10 / 20 = 0.5            |
| %      | 取余数（取模） | 返回除法的余数 9 % 2 = 1 |

浮点数的精度问题

浮点数值的最高精度是17位小数，但在进行算术运算时其精确度远远不如整数，所以不要直接判断两个浮点数是否相等

```js
var result = 0.1 + 0.2;//结果不是0.3，而是：0.30000000000000004
console.log(0.07 * 100);//结果不是7，而是：7.0000000000000001
```

**递增和递减运算符**

递增（++），递减（--），递增和递减运算符只能配合变量使用

**比较运算符**

比较运算符（关系运算符）是两个数据进行比较时所使用的运算符，比较运算后，会返回一个布尔值（true/false）作为比较运算符的结果

| 运算符名称 | 说明                        | 案例        | 结果  |
| ---------- | --------------------------- | ----------- | ----- |
| <          | 小于号                      | 1 < 2       | true  |
| >          | 大于号                      | 1 > 2       | false |
| >=         | 大于等于号                  | 2 >= 2      | true  |
| <=         | 小于等于号                  | 3 <= 2      | false |
| ==         | 判等号（会转型）            | 37 == '37'  | true  |
| !=         | 不等号                      | 37 != 37    | false |
| === !==    | 全等 要求值和数据类型都一致 | 37 === '37' | false |

**逻辑运算符**

| 逻辑运算符 | 说明   | 案例          |
| ---------- | ------ | ------------- |
| &&         | 逻辑与 | true&&false   |
| \|\|       | 逻辑或 | true\|\|false |
| !          | 逻辑非 | !true         |

 **赋值运算符**

| 赋值运算符 | 说明                 | 案例                       |
| ---------- | -------------------- | -------------------------- |
| =          | 直接赋值             | var userName='我是值'      |
| +=、-=     | 加、减一个数后在赋值 | var age = 10; age+=5; //15 |
| *=、/=、%= | 乘、除、取模后在赋值 | var age = 2; age*=5; //10  |

**运算符优先级**

| 优先级 | 运算符     | 顺序           |
| ------ | ---------- | -------------- |
| 1      | 小括号     | ()             |
| 2      | 一元运算符 | ++  --  !      |
| 3      | 算术运算符 | 先* / % 后 + - |
| 4      | 关系运算符 | > >= < <=      |
| 5      | 相等运算符 | == != === !==  |
| 6      | 逻辑运算符 | 先 && 后 \|\|  |
| 7      | 赋值运算符 | =              |
| 8      | 逗号运算符 | ，             |

#### 流程控制

**分支结构**

+ if

  ```js
  if (条件表达式) {
    //条件成立执行的代码语句
  }
  
  if (条件表达式) {
    //条件成立执行的代码语句
  }else {
      //条件不成立执行的代码
  }
  
  多分支语句
  if (条件表达式) {
    //条件成立执行的代码语句
  }else if(条件表达式){
      //条件成立执行的代码
  }else {
      
  }
  ```

+ switch

  ```js
  switch(表达式) {
      case value1:
          执行语句1;
          break;
       case value2:
          执行语句2;
          break;
       ...
       default:
           执行最后的语句;
  }
  1.表达式经常写成变量
  2.变量和case里面的值相匹配的时候是全等，必须是值和数据类型一致才可以
  3.如果当前的case里面没有break则不会退出switch，继续执行下一个case
  ```

switch语句和if else if语句的区别

1. switch通常处理case为比较确定值的情况，而if..else..if常用于范围判断
2. switch语句进行条件判断后直接执行到程序的条件语句，效率更高。而if...else语句有几种条件，就得判断多少次
3. 当分支比较少时，if...else语句的执行效率比switch语句高
4. 当分支比较多时，switch语句的执行效率比较高，而且结构更清晰

**三元表达式**

```js
条件表达式 ? 表达式1 : 表达式2
//如果条件表达式为真，则返回表达式1的值，否则返回表达式2的值
```

#### 循环

+ for循环

  ```js
  for(初始化变量;条件表达式;操作表达式){
      //循环体
  }
  ```

+ while循环

  ```js
  while (条件表达式) {
      //循环体
  }
  ```

+ do...while循环

  ```js
  do {
      //循环体
  } while (条件表达式)
  ```

**continue break**

continue关键字用于立即跳出本次循环，继续下一次循环

break关键字用于立即跳出整个循环

#### 命名规范以及语法格式

**标识符命名规范**

+ 变量、函数的命名必须要有意义
+ 变量的名称一般用名词
+ 函数的名称一般用动词

**操作符规范**

```js
//操作符的左右两侧各保留一个空格，小括号两侧保留一个空格
for (var i = 1; i <= 5; i++){
    if(i == 3){
        break; //单行注释前面注意有个空格
    }
}
```

#### 数组

数组就是一组数据的集合，存储在单个变量下，**数组里面可以放任意数据类型**

**创建数组的方式**

+ 利用new创建数组

  ```js
  var 数组名 = new Array();
  ```

+ 利用数组字面量创建数组

  ```js
  //1.利用数组字面量方式创建空的数组
  var 数组名 = [];
  //2.使用数组字面量方式创建带初始值的数组
  var 数组名 = [1,'小黑','大黄',true];
  ```

获取数组长度：array.length

**数组中新增元素**

+ 通过修改length长度新增数组元素

  ```js
  var arr = ['red','green','blue'];
  arr.length = 5; //把我们数组的长度修改为了5 
  ```

+ 通过修改数组索引新增数组元素

  ```js
  var arr = ['red','green','blue'];
  arr[3] = 'pink';
  arr = 'yellow'; //不要直接给数组名赋值，否则里面的数组元素都没有了
  ```

#### 函数

函数就是封装了一段可以被重复执行调用的代码块，目的是让大量代码重复使用

**函数的使用**

+ 声明函数

  ```js
  function 函数名() {
      //函数体
  }
  ```

+ 调用函数：函数名();

**函数的参数**

```js
function 函数名(形参1,形参2...) { //在声明函数的小括号里面是形参
    
}

函数名(实参1,实参2...); //在函数调用的小括号里面是实参
```

| 参数个数             | 说明                               |
| -------------------- | ---------------------------------- |
| 实参个数等于形参个数 | 输出正确的结果                     |
| 实参个数多于形参个数 | 只取到形参的个数                   |
| 实参个数小于形参个数 | 多的形参定义为undefined，结果为NaN |

**函数的返回值**

```js
function 函数名() {
    return 需要返回的结果
}
//我们函数只是实现某种功能，最终的结果需要返回给函数的调用者
//如果函数没有return，则返回undefined
```

**arguments的使用**

当我们不确定有多少个参数传递时，可以用arguments来获取。在JS中，arguments实际上它是当前函数的一个内置对象。所有函数都内置了一个arguments对象，arguments对象中存储了传递的所有实参

arguments展示形式是一个伪数组，伪数组具有以下特点：

+ 具有length属性
+ 按索引方式存储数据
+ 不具有数组的push，pop等方法

**函数的两种声明方式**

+ 利用函数关键字自定义函数

  function fn() {}

+ 函数表达式(匿名函数)

  ```js
  var 变量名 = function(){};
  //示例
  var fun = function(aru){
      return aru;
  }
  fun('小猪佩奇');
  ```

#### 作用域

JS作用域：就是代码名字在某个范围内起作用和效果，目的是提高程序的可靠性，减少命名冲突

**JS的作用域（es6）之前：全局作用域，局部作用域**

+ 全局作用域：整个script标签或者是一个单独的js文件
+ 局部作用域：在函数内部就是局部作用域

**变量作用域的分类**

+ 全局变量：在全局作用域下的变量，注意：如果在函数内部，没有声明直接赋值的变量也属于全局变量
+ 局部变量：在局部作用域下的变量

从执行效率来看全局变量和局部变量

+ 全局变量只有浏览器关闭的时候才会销毁，比较占内存资源
+ 局部变量当我们程序执行完毕就会销毁，比较节约内存资源

**作用域链**

```js
var num = 10;
function fn() { //外部函数
    var num = 20;
    function fun() { //内部函数
        console.log(num);
    }
}
//输出20，就近原则
```

根据在内部函数可以访问外部函数变量的这种机制，用链式查找决定哪些数据能被内部函数访问，就称作作用域链，就近原则

#### JS预解析	

```js
console.log(num); //undefined 坑1
var num = 10;

fun(); //报错，坑2
var fun = function() {
    console.log(22);
}
```

0JavaScript代码是由浏览器中JavaScript解析器来执行的。JavaScript解析器在运行JavaScript代码的时候分为两步：预解析和代码执行

1. 预解析，js引擎会把js里面所有的 var 还有 function 提升到当前作用域的最前面
   + 预解析分为变量预解析（变量提升）和函数预解析（函数提升）
   + 变量提升就是把所有的变量声明提升到当前的作用域最前面，不提升赋值操作
   + 函数提升就是把所有的函数声明提升到当前作用域的最前面，不调用函数
   + 函数表达式调用必须写在函数表达式的下面
2. 代码执行，按照代码书写的顺序从上往下执行

#### 对象

在JS中，对象是一组无序的相关属性和方法的集合，所有的事物都是对象，例如字符串、数值、数组、函数等

**创建对象的三种方式**

+ 利用字面量创建对象

  对象字面量：就是花括号{}里面包含了表达这个具体事物（对象）的属性和方法

  ```js
  var obj = {
      uname: '张三丰',
      age: '18',
      sayHi: function() {
          console.log('hi~');
      }
  }
  ```

  调用对象的属性

  + 对象名.属性名
  + 对象名['属性名']

+ 利用new Object创建对象

  ```js
  var obj = new Object();
  obj.name = '张三丰';
  obj.age = '18';
  obj.sayHi = function() {
      console.log('hi~');
  }
  ```

+ 利用构造函数创建对象

  构造函数就是把我们对象里面一些相同的属性和方法抽象出来封装到函数里面

  ```js
  //构造函数语法格式
  function 构造函数名() {
      this.属性 = 值;
      this.方法 = function() {
         
      }
  }
  new 构造函数名();
  
  function Star(uname,age,sex) {
      this.name = uname;
      this.方法 = function() {
         
      }
  }
  ```

  构造函数名字首字母要大写，构造函数不需要return就可以返回结果，调用构造函数必须使用new，我们只要new Star()调用函数就创建一个对象

  利用构造函数创建对象的过程称为对象的实例化

**new关键字执行过程**

1. 在内存中创建一个新的空对象
2. 让this指向这个新的对象
3. 执行构造函数里面的代码，给这个新对象添加属性和方法
4. 返回这个新对象

**变量、属性、函数、方法的区别**

变量和属性的相同点：都是用来存储数据的

不同点：变量单独声明并赋值，使用的时候直接写变量名，单独存在

​                属性在对象里面不需要声明，使用的时候必须是对象.属性

函数和方法的相同点：都是实现某种功能，做某件事

不同点：函数是单独声明的，并且调用的函数名()单独存在的

​                方法在对象里面，调用的时候对象.方法()

**for in 遍历对象**

```js
for(变量 in 对象){
    
}

for(var k in obj) {
    console.log(k); // k变量输出，得到的是属性名
    console.log(obj[k]); //obj[k]得到的是属性值
}
```

#### 内置对象

**Math对象**

```js
Math.PI                 //圆周率
Math.floor()            //向下取整
Math.ceil()             //向上取整
Math.round()            //四舍五入 就近取整，注意-3.5 结果是-3
Math.abs()              //绝对值
Math.max()/Math.min()   //求最大和最小值
Math.random()           //返回一个[0,1)之间的小数
```

**日期对象**

Date()日期对象，是一个构造函数，必须使用new来调用创建我们的日期对象

```js
var now = new Date(); //获取当前时间
```

Date()构造函数如果括号里有时间，就返回参数里面的时间。例如日期格式字符串为'2019-5-1'，可以写成new Date('2019-5-1')，或者new Date('2019/5/1')

| 方法名        | 说明                       |
| ------------- | -------------------------- |
| getFullYear() | 获取当年                   |
| getMonth()    | 获取当月（0-11）           |
| getDate()     | 获取当天日期               |
| getDay()      | 获取星期几（周日0到周六6） |
| getHours()    | 获取当前小时               |
| getMinutes()  | 获取当前分钟               |
| getSeconds()  | 获取当前秒钟               |

获取时间戳：

+ valueOf()，getTime()
+ var date1 = +new Date();（最常用的写法）
+ Date.now()，H5新增

**数组对象**

```js
var arr1 = new Array();     //创建了一个空数组
var arr1 = new Array(2);    //创建长度为2的数组
var arr1 = new Array(2,3);  //等价于[2,3]，数组里面有两个元素，2和3
```

**检测是否为数组**

+ instanceof 运算符：arr instanceof Array
+ Array.isArray(arr)：H5新增的方法，ie9以上版本支持

**添加删除数组元素的方法**

| 方法名            | 说明                                                    | 返回值               |
| ----------------- | ------------------------------------------------------- | -------------------- |
| push(参数1...)    | 末尾添加一个或多个元素，注意修改原数组                  | 返回新的长度         |
| pop()             | 删除数组最后一个元素，把数组长度减1，无参数、修改原数组 | 返回它删除的元素的值 |
| unshift(参数1...) | 向数组的开头添加一个或更多元素，注意修改原数组          | 返回新的长度         |
| shift()           | 删除数组的第一个元素，数组长度减一，无参数、修改原数组  | 返回第一个元素的值   |

**数组排序**

| 方法名    | 说明                         | 是否修改原数组                     |
| --------- | ---------------------------- | ---------------------------------- |
| reverse() | 颠倒数组中元素的顺序，无参数 | 该方法会改变原来的数组，返回新数组 |
| sort()    | 对数组的元素进行排序         | 该方法会改变原来的数组，返回新数组 |

```js
arr1.sort(function(a, b) {
    return a - b; 升序的顺序排列
    return b - a; 降序的顺序排列
})
```

**数组索引方法**

| 方法名        | 说明                           | 返回值                                 |
| ------------- | ------------------------------ | -------------------------------------- |
| indexOf()     | 数组中查找给定元素的第一个索引 | 如果存在返回索引号，如果不存在，返回-1 |
| lastIndexOf() | 在数组中的最后一个的索引       | 如果存在返回索引号，如果不存在，返回-1 |

**数组转换为字符串**

| 方法名         | 说明                                       | 返回值         |
| -------------- | ------------------------------------------ | -------------- |
| toString()     | 把数组转换成字符串，逗号分隔每一项         | 返回一个字符串 |
| join('分隔符') | 方法用于把数组中的所有元素转换为一个字符串 | 返回一个字符串 |

| 方法名   | 说明                                   | 返回值                                         |
| -------- | -------------------------------------- | ---------------------------------------------- |
| concat() | 连接两个或多个数组，不影响原数组       | 返回一个新的数组                               |
| slice()  | 数组截取slice(begin, end)              | 返回被截取项目的新数组                         |
| splice() | 数组删除splice(第几个开始，要删除个数) | 返回被删除项目的新数组，注意，这个会影响原数组 |

**字符串对象**

基本包装类型：就是把简单数据类型包装成了复杂数据类型

字符串的不可变指的是里面的值不可变，虽然看上去可以改变内容，但其实是地址变了，内存中新开辟了一个内存空间

字符串所有的方法，都不会修改字符串本身（字符串是不可变的），操作完成会返回一个新的字符串

**根据字符返回位置**

| 方法名                             | 说明                                                         |
| ---------------------------------- | ------------------------------------------------------------ |
| indexOf('要查找的字符',开始的位置) | 返回指定内容在原字符串中的位置，如果找不到返回-1，开始的位置是index索引号 |
| lastIndexOf()                      | 从后往前找，只找第一个匹配的                                 |

**根据位置返回字符（重点）**

| 方法名            | 说明                                       | 使用                           |
| ----------------- | ------------------------------------------ | ------------------------------ |
| charAt(index)     | 返回指定位置的字符（index字符串的索引号）  | str.charAt(0)                  |
| charCodeAt(index) | 返回指定位置处字符的ASCII码（index索引号） | str.charCodeAt(indexz)         |
| str[index]        | 获取指定位置处字符                         | HTML5,IE8+支持，和charAt()等效 |

**字符串操作方法（重点）**

| 方法名                                 | 说明                                                         |
| -------------------------------------- | ------------------------------------------------------------ |
| concat(str1,str2,str3...)              | concat()方法用于连接两个或多个字符串。拼接字符串，等效于+    |
| substring(start,length)                | 从start位置开始（索引号），length取的个数                    |
| slice(start,end)                       | 从start位置开始，截取到end位置，end取不到                    |
| substring(start,end)                   | 从start位置开始，截取到end位置，end取不到，基本和slice相同，但是不接受负值 |
| replace('被替换的字符','替换为的字符') | 只会替换第一个字符                                           |
| split('分隔符')                        |                                                              |

#### 简单类型和复杂类型

简单类型又叫做基本数据类型或者值类型，复杂数据类型又叫做引用类型

+ 值类型：简单数据类型/基本数据类型，在存储时，变量中存储的是值本身，因此叫做值类型

  string、number、boolean、undefined、null

+ 引用类型：复杂数据类型，在存储时变量中存储的仅仅是地址（引用），因此叫做引用数据类型

  通过new关键字创建的对象（系统对象、自定义对象），如Object，Array，Date等

+ 

**堆和栈**

1. 栈（操作系统）：由操作系统自动分配释放存放函数的参数值、局部变量的值等。其操作方式类似于数据结构中的栈

   简单数据类型放到栈里面

2. 堆（操作系统）：存储复杂类型（对象），一般由程序员分配释放，若程序员不释放，由垃圾回收机制回收

   复杂数据类型存放到堆里面

3. 简单类型传参

   函数的形参也可以看做是一个变量，当我们把一个值类型变量作为参数传递给函数的形参时，其实是把变量在栈空间里的值复制了一份给形参，那么在方法内部对形参做任何修改，都不会影响到外部变量

4. 复杂类型传参

   函数的形参也可以看作是一个变量，当我们把引用类型变量给形参时，其实是把变量在栈空间里保存的堆地址复制给了形参，形参和实参其实保存的是同一个堆地址，所以操作的是同一个对象

### Web APIs

Web API是浏览器提供的一套操作浏览器功能和页面元素的API（BOM和DOM）

### DOM

文档对象模型（Document Object Model，简称DOM），是W3C组织推荐的处理可扩展标记语言（HTML或者XML）的标准编程接口

#### DOM树

![](C:\Users\admin\Desktop\个人资料\笔记\img\JavaScript_1.png)

+ 文档：一个页面就是一个文档，DOM中使用document表示
+ 元素：页面中的所有标签都是元素，DOM中用element表示
+ 节点：网页中所有的内容都是节点（标签、属性、文本、注释），DOM中使用node表示

DOM把以上内容都看作是对象

#### 如何获取页面元素

+ 根据ID获取

  getElementById()

  ```html
  <body>
  	<div id="time">2019-9-9</div>
      <script>
      	//1.因为我们文档页面从上往下加载，所以先得有标签，所以我们script写到标签的下面
          //2.返回的是一个元素对象
          var timer = document.getElementById('time');
          //console.dir 打印我们返回的元素对象，更好的查看里面的属性和方法
          console.dir(timer);
      </script>
  </body>
  ```

+ 根据标签名获取

  getElementsByTagName()方法可以返回带有指定标签名的对象的集合

  ```js
  //返回的是获取过来元素对象的集合，以伪数组的形式存储的
  var lis = document.getElementsByTagName('li');
  //依次打印里面的元素对象
  for (var i = 0; i < lis.length; i++) {
      console.log(lis[i]);
  }
  //如果页面中只有一个li或者没有这个元素，返回的还是伪数组的形式
  //
  ```

  还可以获取某个元素（父元素）内部所有指定标签名的子元素

  ```js
  element.getElementsByTagName('标签名')
  ```

  注意：父元素必须是单个对象（必须指明是哪一个元素对象），获取的时候不包括父元素自己

+ 通过HTML5新增的方法获取

  ```js
  document.getElementsByClassName('类名'); //根据类名返回元素对象集合
  
  document.querySelector('选择器'); //根据指定选择器返回第一个元素对象
  
  document.querySelectorAll('选择器'); //返回指定选择器的所有元素对象集合
  ```

+ 特殊元素获取

  + 获取body：document.body;
  + 获取html：document.documentElement;

#### 事件

事件是有三部分组成：事件源，事件类型，事件处理程序，也称为事件三要素

```js
var btn = document.getElementById('btn');
btn.onclick = function() {
    alert('触发按钮');
}
```

| 鼠标事件    | 触发条件         |
| ----------- | ---------------- |
| onclick     | 鼠标点击左键触发 |
| onmouseover | 鼠标经过触发     |
| onmouseout  | 鼠标离开触发     |
| onfocus     | 获得鼠标焦点触发 |
| onblur      | 失去鼠标焦点触发 |
| onmousemove | 鼠标移动触发     |
| onmouseup   | 鼠标弹起触发     |
| onmousedown | 鼠标按下触发     |

#### 操作元素

JS的DOM操作可以改变网页内容、结构和样式，我们可以利用DOM操作元素来改变元素里面的内容、属性等。注意以下都是属性

**改变元素内容**

```js
element.innerText
//从起始位置到终止位置的内容，但他去除html标签，同时空格和换行也会去掉

element.innerHTML
//起始位置到终止位置的全部内容，包括html标签，同时保留空格和换行

var btn = document.querySelector('button');
var div = document.querySelector('div');
btn.onclick = function() {
    div.innerText = '2019-6-6'
}
```

**常用元素的属性操作**

1. innerText、innerHTML 改变元素内容
2. src、href
3. id、alt、title

```js
var img = document.querySelector('img');
img.onclick = function() {
    img.src = 'images/zxy.jpg';
}
```

**表单元素的属性操作**

```js
type、value、checked、selected、disabled
```

**样式属性操作**

我们可以通过JS修改元素的大小、颜色、位置等样式

```js
element.style 行内样式操作

element.className 类名样式操作

var div = document.querySelector('div');
div.onclick = function() {
    this.style.backgroundColor = 'purple';
}
```

注意：

1. JS里面的样式采取驼峰命名法 比如 fontSize、backgroundColor
2. JS修改style样式操作，产生的是行内样式，CSS权重比较高
3. 如果样式修改较多，可以采取操作类名方式更改元素样式
4. class因为是个保留字，因此使用className来操作元素类名属性
5. className会直接更改元素的类名，会覆盖原先的类名

**获取自定义属性**

1. 获取属性值

   + element.属性  获取属性值
   + element.getAttribute('属性')；

   区别：

   + element.属性  获取内置属性值（元素本身自带的属性）
   + element.getAttribute('属性');  主要获得自定义的属性（标准）

2. 设置属性值

   + element.属性 = '值'  设置内置属性值
   + element.setAttribute('属性','值');

   区别：

   + element.属性  设置内置属性值
   + element.setAttribute('属性');  主要设置自定义的属性（标准）

3. 移除属性

   + removeAttribute(属性)

**H5自定义属性**

自定义属性的目的：是为了保存并使用数据。有些数据可以保存到页面中而不用保存到数据库中。

自定义属性可以通过getAttribute('属性')获取

但是有的自定义属性很容易引起歧义，不容易判断是元素的内置属性还是自定义属性

1. 设置H5自定义属性

   H5规定自定义属性date-开头作为属性名并且赋值，比如< div data-index="1">< /div>

   或者使用JS设置：element.setAttribute('data-index',2);

2. 获取H5自定义属性

   element.getAttribute('data-index');

   H5新增element.dataset.index 或者 element.dataset['index']，ie11才开始支持

   如果自定义属性里面有多个-链接的单词，我们获取的时候采取驼峰命名法

   data-list-name，element.dataset.listName

#### 节点操作

获取元素通常使用两种方式

1. 利用DOM提供的方法获取元素：逻辑性不强、繁琐
2. 利用节点层级关系获取元素：逻辑性强，兼容性差

**节点描述**

网页中所有的内容都是节点（标签、属性、文本、注释等），在DOM中，节点使用node来表示

HTML DOM树中所有的节点均可通过JavaScript进行访问，所有HTML元素（节点）均可被修改，也可以创建或删除

一般的，节点至少拥有nodeType(节点类型)、nodeName(节点名称)、nodeValue(节点值)这三个基本属性

+ 元素节点 nodeType为1
+ 属性节点nodeType为2
+ 文本节点nodeType为3（文本节点包含文字、空格、换行等）

**节点层级**

父子兄层级关系

1. 父级节点

   node.parentNode，得到的是离元素最近的父节点，如果指定的节点没有父节点则返回null

2. 子节点

   + parentNode.childNodes（标准）：返回包含指定节点的子节点的集合，该集合为即时更新的集合，包含元素节点，文本节点等

     如果只想要获得里面的元素节点，则需要专门处理，所以我们一般不提倡使用childNodes

   + parentNode.children（非标准）：只读属性，返回所有的子元素节点。他只返回子元素节点，其余节点不返回（重点掌握）
   + parentNode.firstChild：返回第一个子节点，找不到则返回null，同样，也是包含所有的节点
   + parentNode.lastChild：返回最后一个子节点，找不到则返回null，同样，也是包含所有的节点
   + parentNode.firstElementChild：返回第一个子元素节点，找不到则返回null，有兼容性问题，ie9以上才支持
   + parentNode.lastElementChild：返回最后一个子元素节点，找不到则返回null，有兼容性问题，ie9以上才支持
   + parentNode.children[0]：实际开发的写法，返回第一个子元素，没有兼容性问题
   + parentNode.children[parentNode.children.length - 1]：获取最后一个子元素

3. 兄弟节点

   + node.nextSibling：返回当前元素的下一个兄弟节点，找不到返回null。同样，也是包含所有的节点。
   + node.previousSibling：返回当前元素的上一个兄弟节点，找不到返回null。同样，也是包含所有的节点。
   + node.nextElementSibling：返回当前元素下一个兄弟元素节点，找不到则返回null，有兼容性问题，ie9以上才支持
   + node.previousElementSibling：返回当前元素上一个兄弟元素节点，找不到则返回null，有兼容性问题，ie9以上才支持

   ```js
   //如何解决兼容性问题？
   //自己封装一个兼容性函数
   function getNextElementSibling(element) {
       var el = element;
       while (el = el.nextSibling) {
           if (el.nodeType === 1) {
               return el;
           }
       }
       return null;
   }
   ```

**创建节点**

document.createElement('tagName')：创建指定的HTML元素。因为这些元素原先不存在，是根据我们的需求动态生成的，所以也称为动态创建元素节点

**添加节点**

+ node.appendChild(child)：将一个节点添加到指定父节点的子节点列表末尾。类似于CSS里面的after伪元素
+ node.insertBefore(child,指定元素)：将一个节点添加到父节点的指定子节点前面。类似于CSS里面的before伪元素

**删除节点**

node.removeChild(child)：从DOM中删除一个子节点，返回删除的节点

**复制节点**

node.cloneNode()：返回调用该方法的节点的一个副本

注意：

1. 如果括号参数为空或者为false，则是浅拷贝，即只克隆复制节点本身，不克隆里面的子节点
2. 如果括号内参数为true，则是深拷贝，会复制节点本身以及里面所有的子节点

**三种动态创建元素区别**

+ document.write()
+ element.innerHTML
+ document.createElement()

区别：

1. document.write是直接将内容写入页面的内容流，但是文档流执行完毕，则他会导致页面全部重绘
2. innerHTML是将内容写入某个DOM节点，不会导致页面全部重绘
3. innerHTML创建多个元素效率更高（不要拼接字符串，采取数组形式拼接），结构稍微复杂
4. createElement()创建多个元素效率稍低一点点，但是结构更清晰

#### DOM重点核心

主要针对元素的操作。主要有创建、增、删、改、查、属性操作、事件操作。

1. 创建

   1. document.write
   2. innerHTML
   3. createElement

2. 增

   1. appendChild
   2. insertBefore

3. 删

   1. removeChild

4. 改

   主要修改dom元素的属性，dom元素的内容、属性，表单的值等

   1. 修改元素属性：src、href、title等
   2. 修改普通元素内容：innerHTML、innerText
   3. 修改表单元素：value、type、disabled等
   4. 修改元素样式：style、className

5. 查

   1. DOM提供的API方法：getElementById、getElementsByTagName古老用法不推荐
   2. H5提供的新方法：querySelector、querySelectorAll 提倡
   3. 利用节点操作获取元素：父（parentNode）、子（children）、兄（previousElementSibling、nextElementSibling）提倡

6. 属性操作

   主要针对自定义属性

   1. setAttribute：设置dom的属性值
   2. getAttribute：得到dom的属性值
   3. removeAttribute移除属性

7. 事件操作

   给元素注册事件，采取 事件源.事件类型=事件处理程序

   | 鼠标事件    | 触发条件         |
   | ----------- | ---------------- |
   | onclick     | 鼠标点击左键触发 |
   | onmouseover | 鼠标经过触发     |
   | onmouseout  | 鼠标离开触发     |
   | onfocus     | 获得鼠标焦点触发 |
   | onblur      | 失去鼠标焦点触发 |
   | onmousemove | 鼠标移动触发     |
   | onmouseup   | 鼠标弹起触发     |
   | onmousedown | 鼠标按下触发     |

#### 事件高级导读

**注册事件（绑定事件）**

给元素添加事件，称为注册事件或者绑定事件

注册事件有两种方式：传统方式和方法监听注册方式

+ 传统注册方式
  + 利用on开头的事件onclick
  + 注册事件的唯一性
  + 同一个元素同一个事件只能处理一个处理函数，最后注册的处理函数会覆盖前面注册的处理函数
+ 方法监听注册方式
  + w3c标准推荐方式
  + addEventListener()他是一个方法
  + IE9之前的IE不支持此方法，可以使用attachEvent()代替
  + 特点：同一个元素同一个事件可以注册多个监听器

**addEventListener事件监听方式**

```js
eventTarget.addEventListener(type,listener[,useCapture])
```

eventTarget.addEventListener()方法将指定的监听器注册到eventTarget（目标对象）上，当该对象触发指定的事件时，就会执行事件处理函数

该方法接收三个参数：

+ type：事件类型字符串，比如click、mouseover，注意这里不要带on
+ listener：事件处理函数，事件发生时，会调用该监听函数
+ useCapture：可选参数，是一个布尔值，默认时false

**attachEvent事件监听方式（非标准，尽量不要在生产环境中使用）**

```js
eventTarget.attachEvent(eventNameWithOn,callback)
```

eventTarget.attachEvent()方法将指定的监听器注册到eventTarget（目标对象）上，当该对象触发指定的事件时，指定的回调函数就会被执行

该方法接收两个参数：

+ eventNameWithOn：事件类型字符串，比如onclick、onmouseover，这里要带on
+ callback：事件处理函数，当目标触发事件时回调函数被调用

**删除事件（解绑事件）**

1. 传统注册方式

   eventTarget.onclick=null;

2. 方法监听注册方式

   eventTarget.removeEventListener(type,listener[,useCapture]);

   eventTarget.detachEvent(eventNameWidthOn,callback);

**DOM事件流**

事件流描述的是从页面中接收事件的顺序

事件发生时会在元素节点之间按照特定的顺序传播，这个传播过程即DOM事件流

![](C:\Users\admin\Desktop\个人资料\笔记\img\JavaScript_2.png)

DOM事件流分为三个阶段

1. 捕获阶段
2. 当前目标阶段
3. 冒泡阶段

事件冒泡：IE最早提出，事件开始时由最具体的元素接收，然后逐级向上传播到DOM最顶层节点的过程

事件捕获：网景最早提出，由DOM最顶层节点开始，然后逐级向下传播到最具体的元素接收的过程

注意：

1. JS代码中只能执行捕获或者冒泡其中的一个阶段
2. onclick和attachEvent只能得到冒泡阶段
3. addEventListener(type,listener[,useCapture])第三个参数如果是true，表示在事件捕获阶段调用事件处理程序；如果是false（不写默认就是false），表示在事件冒泡阶段调用事件处理程序
4. 实际开发中我们很少使用事件捕获，更关注事件冒泡
5. 有些事件是没有冒泡的，比如onblur、onfocus、onmouseenter、onmouseleave

**事件对象**

```js
eventTarget.onclick = function(event) {}
eventTarget.addEventListener('click',function(event) {})
//这个event就是事件对象，我们还喜欢写成e或者evt
```

官方解释：event对象代表事件的状态，比如键盘按键的状态、鼠标的位置、鼠标按钮的状态

简单理解：事件发生后，跟事件相关的一系列信息数据的集合都放到这个对象里面，这个对象就是事件对象event，他有很多属性和方法

ie6~8浏览器不会给方法传递参数，如果需要的话，需要到window.event中获取查找

**事件对象常见属性和方法**

| 事件对象属性方法    | 说明                                                         |
| ------------------- | ------------------------------------------------------------ |
| e.target            | 返回触发事件的对象，标准                                     |
| e.srcElement        | 返回触发事件的对象，非标准 ie6-8使用                         |
| e.type              | 返回事件的类型 比如click、mouseover不带on                    |
| e.returnValue       | 该属性阻止默认事件（默认行为）非标准 ie6-8使用，比如不让链接跳转 |
| e.preventDefault()  | 该方法阻止默认事件（默认行为）标准 ，比如不让链接跳转        |
| e.stopPropagation() | 阻止冒泡 标准                                                |
| e.cancelBubble      | 该属性阻止冒泡 ie6-8使用 非标准                              |

```js
var div = document.querySelector('div');
div.addEventListener('click',function(e){
    console.log(e.target);  //e.target返回的是触发事件的对象（元素）
    console.log(this);      //this返回的是绑定事件的对象（元素）
})
```

**事件委托（代理、委派）**

原理：不要给每个子节点单独设置事件监听器，而是事件监听器设置在其父节点上，然后利用冒泡原理影响设置每个子节点

事件委托的作用：只操作一次DOM，提高了程序的性能

**常用的鼠标事件**

| 鼠标事件    | 触发条件         |
| ----------- | ---------------- |
| onclick     | 鼠标点击左键触发 |
| onmouseover | 鼠标经过触发     |
| onmouseout  | 鼠标离开触发     |
| onfocus     | 获得鼠标焦点触发 |
| onblur      | 失去鼠标焦点触发 |
| onmousemove | 鼠标移动触发     |
| onmouseup   | 鼠标弹起触发     |
| onmousedown | 鼠标按下触发     |

1. 禁止使用鼠标右键菜单

   contextmenu主要控制应该何时显示上下文菜单，主要拥有程序员取消默认的上下文菜单

   ```js
   document.addEventListener('contextmenu',function(e) {
       e.preventDefault();
   })
   ```

2. 禁止鼠标选中（selectstart 开始选中）

   ```js
   document.addEventListener('selectstart',function(e) {
       e.preventDefault();
   })
   ```

3. 鼠标事件对象：MouseEvent

   | 鼠标事件对象 | 说明                                   |
   | ------------ | -------------------------------------- |
   | e.clientX    | 返回鼠标相对于浏览器窗口可视区的X坐标  |
   | e.clientY    | 返回鼠标相对于浏览器窗口可视区的Y坐标  |
   | e.pageX      | 返回鼠标相对于文档页面的X坐标 IE9+支持 |
   | e.pageY      | 返回鼠标相对于文档页面的Y坐标 IE9+支持 |
   | e.screenX    | 返回鼠标相对于电脑屏幕的X坐标          |
   | e.screenY    | 返回鼠标相对于电脑屏幕的Y坐标          |

**常用的键盘事件**

| 键盘事件   | 触发条件                                                     |
| ---------- | ------------------------------------------------------------ |
| onkeyup    | 某个键盘按键被松开时触发                                     |
| onkeydown  | 某个键盘按键被按下时触发                                     |
| onkeypress | 某个键盘按键被按下时触发，但是它不识别功能键，比如ctrl、shift、箭头等 |

三个事件执行顺序：keydown -- keypress -- keyup

**键盘事件对象：KeyboardEvent**

| 键盘事件对象属性 | 说明              |
| ---------------- | ----------------- |
| keyCode          | 返回该键的ASCII值 |

注意：

+ onkeydown和onkeyup不区分字母大小写，onkeypress区分字母大小写
+ 在实际开发中，更多使用keydown和keyup，它能识别所有的键（包括功能键）
+ keypress不识别功能键，但是keyCode属性能区分大小写，返回不同的ASCII值
+ keydown和keypress在文本框里面的特点：他们两个事件触发的时候，文字还没有落入文本框中，keyup事件触发的时候，文字已经落入文本框里面了

### BOM

BOM（Browser Object Model）浏览器对象模型，它提供了独立于内容而与浏览器窗口进行交互的对象，其核心对象是window

BOM缺乏标准，JavaScript语法的标准化组织是ECMA，DOM的标准化组织是W3C，BOM最初是Netspace浏览器标准的一部分

window对象是浏览器的顶级对象，它具有双重角色

1. 它是JS访问浏览器窗口的一个接口
2. 他是一个全局对象。定义在全局作用域中的变量、函数都会变成window对象的属性和方法

在调用的时候可以省略window，前面学习的对话框都属于window对象方法，如alert()、prompt()

注意：window下的一个特殊属性window.name

#### window对象的常见事件

**窗口加载事件**

```js
window.onload = function(){}
或者
window.addEventListener("load",function(){})
```

window.onload是窗口（页面）加载事件，当文档内容完全加载完成会触发该事件（包括图像、脚本文件、CSS文件等），就调用的处理函数

注意：

1. 有了window.onload就可以把JS代码写到页面元素的上方，因为onload是等页面内容全部加载完毕，再去执行处理函数
2. window.onload传统注册事件方式只能写一次，如果有多个，会以最后一个window.onload为准
3. 如果使用addEventListener则没有限制

```js
document.addEventListener('DOMContentLoaded',function({}))
```

DOMContentLoaded事件触发时，仅当DOM加载完成，不包括样式表，图片，flash等等

**调整窗口大小事件**

```js
window.onresize = function(){}
window.addEventListener("resize",function(){});
```

window.onresize是调整窗口大小加载事件，当触发时就调用的处理函数

注意：

1. 只要窗口大小发生像素变化，就会触发这个事件
2. 我们经常利用这个事件完成响应式布局。window.innerWidth当前屏幕的宽度

#### 定时器

window对象给我们提供了2个非常好用的方法-定时器

+ setTimeout()
+ setInterval()

**setTimeout()定时器**

```js
window.setTimeout(调用函数,[延迟的毫秒数]);
```

setTimeout()方法用于设置一个定时器，该定时器在定时器到期后执行调用函数

注意：

1. window可以省略
2. 这个调用函数可以直接写函数，或者写函数名或者采取字符串 '函数名()'三种形式。第三种不推荐
3. 延迟的毫秒数省略默认是0，如果写，必须是毫秒
4. 因为定时器可能有很多，所以我们经常给定时器赋值一个标识符

setTimeout()这个调用函数我们也称为回调函数callback，普通函数是按照代码顺序直接调用，而这个函数，需要等待时间，时间到了才去调用这个函数，因此称为回调函数

**停止setTimeout()定时器**

```js
window.clearTimeout(timeoutID)
```

**setInteval()定时器**

```js
window.setInterval(回调函数,[间隔的毫秒数]);
```

setInterval()方法重复调用一个函数，每隔这个时间，就去调用一次回调函数

注意：

1. window可以省略
2. 这个调用函数可以直接写函数，或者写函数名或者采取字符串 '函数名()'三种形式。
3. 间隔的毫秒数省略默认是0，如果写，必须是毫秒，表示每隔多少毫秒就自动调用这个函数
4. 因为定时器可能有很多，所以我们经常给定时器赋值一个标识符

**停止setInteval()定时器**

```js
window.clearInterval(intervalID)
```

**this指向问题**

一般情况下this的最终指向的是那个调用它的对象

1. 全局作用域或者普通函数中的this指向全局对象window（注意定时器里面的this指向window）
2. 方法调用中谁调用this指向谁
3. 构造函数中this指向构造函数的实例

#### JS执行机制

**JS是单线程**

JavaScript语言的一大特点就是单线程，也就是说，同一个时间只能做一件事。这是因为JavaScript这门脚本语言诞生的使命所致——JavaScript是为处理页面中用户的交互，以及操作DOM而诞生的。比如我们对某个DOM元素进行添加和删除操作，不能同时进行。应该先添加，之后再删除

**同步和异步**

为了解决这个问题，利用多核CPU的计算能力，HTML5提出Web Worker标准，允许JavaScript脚本创建多个线程。于是，JS中出现了同步和异步。

+ 同步

  前一个任务结束后再执行后一个任务，程序的执行顺序与任务的排列顺序是一致的、同步的。

+ 异步

  在做某件事的同时，还可以处理其他事情

+ 同步任务

  同步任务都在主线程上执行，形成一个执行栈

+ 异步任务

  JS的异步是通过回调函数实现的。一般而言，异步任务有以下三种类型

  1. 普通事件，如click、resize等
  2. 资源加载，如load、error等
  3. 定时器，包括setInterval、setTimeout等

**JS执行机制**

1. 先执行执行栈中的同步任务
2. 异步任务（回调函数）放入任务队列中
3. 一旦执行栈中的所有同步任务执行完毕，系统就会按次序读取任务队列中的异步任务，于是被读取的异步任务结束等待状态，进入执行栈，开始执行

![](C:\Users\admin\Desktop\个人资料\笔记\img\JavaScript_3.png)

由于主线程不断的重复获得任务、执行任务、再获取任务、再执行，所以这种机制被称为事件循环（event loop）

#### location对象

window对象给我们提供了一个location属性用于获取或设置窗体的URL，并且可以用于解析URL。因为这个属性返回一个对象，所以将这个属性称为location对象

**URL**

一般语法格式：

```
protocol://host[:port]/path/[?query]#fragment
```

| 组成     | 说明                                                         |
| -------- | ------------------------------------------------------------ |
| protocol | 通信协议 常用的http,ftp,matio等                              |
| host     | 主机（域名）www.baidu.com                                    |
| port     | 端口号 可选，省略时使用方案的默认端口 如http的默认端口为80   |
| path     | 路径由零或多个/符号隔开的字符串，一般用来表示主机上的一个目录或文件地址 |
| query    | 参数，以键值对的形式，通过&符号分隔开来                      |
| fragment | 片段，常见于链接，锚点                                       |

**location对象的属性**

| location对象属性  | 返回值                           |
| ----------------- | -------------------------------- |
| location.href     | 获取或者设置整个URL              |
| location.host     | 返回主机（域名）                 |
| location.port     | 返回端口号，如果未写返回空字符串 |
| location.pathname | 返回路径                         |
| location.search   | 返回参数                         |
| location.hash     | 返回片段                         |

**location对象的方法**

| location对象方法   | 返回值                                                       |
| ------------------ | ------------------------------------------------------------ |
| location.assign()  | 跟href一样，可以跳转页面（也称为重定向页面）,可以后退        |
| location.replace() | 替换当前页面，因为不记录历史，所以不能后退页面               |
| location.reload()  | 重新加载页面，相当于刷新按钮或者f5，如果参数为true强制刷新ctrl+f5 |

#### navigator对象

navigator对象包含有关浏览器的信息，他有很多属性，我们最常用的是userAgent，该属性可以返回由客户机发送服务器的user-agent头部的值

#### history对象

window对象给我们提供了一个history对象，与浏览器历史记录进行交互。该对象包含用户（在浏览器窗口中访问过的URL）

| history对象方法 | 作用                                                         |
| --------------- | ------------------------------------------------------------ |
| back()          | 可以后退功能                                                 |
| forward()       | 前进功能                                                     |
| go(参数)        | 前进后退功能，参数如果是1前进一个页面，如果是-1，后退一个页面 |

#### offset

偏移量，使用offset相关属性可以动态的得到该元素的位置（偏移）、大小等

+ 获得元素距离带有定位父元素的位置
+ 获取元素自身的大小（宽度高度）
+ 返回的数值不带单位

| offset系列属性       | 作用                                                         |
| -------------------- | ------------------------------------------------------------ |
| element.offsetParent | 返回作为该元素带有定位的父级元素，如果父级都没有定位，则返回body |
| element.offsetTop    | 返回元素相对带有定位父元素上方的偏移                         |
| element.offsetLeft   | 返回元素相对带有定位父元素左边框的偏移                       |
| element.offsetWidth  | 返回自身包括padding、边框、内容区的宽度，返回数值不带单位    |
| element.offsetHeight | 返回自身包括padding、边框、内容区的高度，返回数值不带单位    |

**offset和style的区别**

offset

+ 可以得到任意样式表中的样式值
+ offset系列获得的数值是没有单位的
+ offsetWidth包含padding+border+width
+ offsetWidth等属性是只读属性，只能获取不能赋值
+ 所以，想要获取元素大小位置，用offset更合适

style

+ style只能得到行内样式表中的样式值
+ style.width获得的是带有单位的字符串
+ style.width获得不包含padding和border的值
+ style.width是可读写属性，可以获取也可以赋值
+ 所以，我们想要给元素更改值，则需要用style改变

#### client

client相关属性获取元素可视区的相关信息，通过client系列的相关属性可以动态的得到该元素的边框大小，元素大小等

| client系列属性       | 作用                                                         |
| -------------------- | ------------------------------------------------------------ |
| element.clientTop    | 返回元素上边框的大小                                         |
| element.clientLeft   | 返回元素左边框的大小                                         |
| element.clientWidth  | 返回自身包括padding、内容区的宽度，不含边框，返回数值不带单位 |
| element.clientHeight | 返回自身包括padding、内容区的高度，不含边框，返回数值不带单位 |

#### 立即执行函数

```js
//立即执行函数：不需要调用，立马能够自己执行的函数
//写法：
(function() {})() 或者 (function(){}());
//也可以传参进来
(function(a,b){
    console.log(a+b);
})(1,2)
//立即执行函数最大的作用就是独立创建了一个作用域，里面所有的变量都是局部变量，不会有命名冲突的情况
```

#### scroll

scroll相关属性可以动态的得到该元素的大小、滚动距离等

| scroll系类属性       | 作用                                           |
| -------------------- | ---------------------------------------------- |
| element.scrollTop    | 返回被卷去的上侧距离，返回数值不带单位         |
| element.scrollLeft   | 返回被卷去的左侧距离，返回数值不带单位         |
| element.scrollWidth  | 返回自身实际的宽度，不含边框，返回数值不带单位 |
| element.scrollHeight | 返回自身实际的高度，不含边框，返回数值不带单位 |

#### mouseenter和mouseover的区别

**mouseenter鼠标事件**

+ 当鼠标移动到元素上时就会触发mouseenter事件
+ 类似mouseover，它们两者之间的差别是
+ mouseover鼠标经过自身盒子会触发，经过子盒子还会触发。mouseenter只会经过自身盒子触发
+ 之所以这样，就是因为mouseenter不会冒泡

#### 动画

动画原理：通过定时器setInterval()不断移动盒子

**缓动动画**

缓动动画就是让元素运动速度有所变化，最常见的是让速度慢慢停下来

1. 让盒子每次移动的距离慢慢变小，速度就会慢慢落下来
2. 核心算法：（目标值-现在的位置）/ 10 作为每次移动的距离步长
3. 停止的条件是：让当前盒子位置等于目标位置就停止定时器

**动画函数添加回调函数**

回调函数原理：函数可以作为一个参数。将整个函数作为参数传到另一个函数里面，当那个函数执行完之后，再执行传进去的这个函数，这个过程就叫做回调

回调函数写的位置：定时器结束的位置

**节流阀**

防止轮播图连续点击造成播放过快

节流阀目的：当上一个函数动画内容执行完毕，再去执行下一个函数动画，让事件无法连续触发

核心实现思路：利用回调函数，添加一个变量来控制，锁住函数和解锁函数

开始设置一个变量var flag=true;

if(flag){flag=false;do something} 关闭水龙头

利用回调函数动画执行完毕，flag=true 打开水龙头

#### 本地存储

**本地存储特性**

1. 数据存储在用户浏览器中
2. 设置、读取方便、甚至页面刷新不丢失数据
3. 容量较大，sessionStorage约5M，localStorage约20M
4. 只能存储字符串，可以将对象JSON.stringify()编码后存储

**window.sessionStorage**

1. 生命周期为关闭浏览器窗口
2. 在同一个窗口（页面）下数据可以共享
3. 以键值对的形式存储使用

+ 存储数据：

```js
sessionStorage.setItem(key,value)
```

+ 获取数据：

```js
sessionStorage.getItem(key)
```

+ 删除数据：

```js
sessionStorage.removeItem(key)
```

+ 删除所有数据：

```js
sessionStorage.clear()
```

**window.localStorage**

1. 生命周期永久生效，除非手动删除，否则页面关闭也会存在
2. 可以多窗口（页面）共享（同一浏览器可以共享）
3. 以键值对的形式存储使用

+ 存储数据：

```js
localStorage.setItem(key,value)
```

+ 获取数据：

```js
localStorage.getItem(key)
```

+ 删除数据：

```js
localStorage.removeItem(key)
```

+ 删除所有数据：

```js
localStorage.clear()
```

### JS高级

#### ES6中类和对象

**创建类**

```js
class Star {
    //class body
    constructor(name,age){
        this.name = name;
        this.age = age;
    }
}

var xx = new Star('亚索',18); //类必须使用new实例化对象
```

+ 类里面有个constructor函数，可以接收传递过来的参数，同时返回实例对象
+ constructor函数只要new生成实例时，就会自动调用这个函数，如果不写这个函数，类也会自动生成这个函数
+ 生成实例new不能省略

**类添加方法**

```js
class Person {
    constructor(name,age){
        this.name = name;
        this.age = age;
    }
    say() {
        console.log(this.name + '你好');
    }
}
```

**继承**

```js
class Father{
}
class Son extends Father {
}
```

**super关键字**

super关键字用于访问和调用对象父类上的函数，可以调用父类的构造函数，也可以调用父类的普通函数

super放在this前面

**使用类的注意事项**

+ ES6中类没有变量提升，所以必须先定义类，才能通过类实例化对象
+ 类里面共有的属性和方法一定要加this使用

#### 构造函数和原型

在ES6之前，对象不是基于类创建的，而是用一种称为构造函数的特殊函数来定义对象和他们的特征

创建对象可以通过以下三种方式：

1. 对象字面量
2. new Object()
3. 自定义构造函数

**构造函数**

JavaScript的构造函数中可以添加一些成员，可以在构造函数本身上添加，也可以在构造函数内部的this上添加。通过这两种方式添加的成员，就分别称为静态成员和实例成员

+ 静态成员：在构造函数本上添加的成员称为静态成员，只能由构造函数本身来访问
+ 实例成员：在构造函数内部创建的对象成员称为实例成员，只能由实例化的对象来访问

**构造函数原型prototype**

构造函数方法很好用，但是存在浪费内存的问题，会开辟一个新的内存空间

构造函数通过原型分配的函数时所有对象所共享的

JavaScript规定，每一个构造函数都有一个prototype属性，指向另一个对象。注意这个prototype就是一个对象，这个对象的所有属性和方法都会被构造函数所拥有

我们可以把那些不变的方法，直接定义在prototype对象上，这样所有对象的实例就可以共享这些方法

```js
function Star(uname,age) {
    this.name = name;
    this.age = age;    
}
Star.prototype.sing = function(){
    console.log('我会唱歌');
}
```

**对象原型__ proto__**

对象都会有一个属性__ proto__指向构造函数的prototype原型对象，之所以我们对象可以使用构造函数prototype原型对象的属性和方法，就是因为对象有_ __ proto__原型的存在

+ __ proto__对象原型和原型对象prototype是等价的
+ __ proto__对象原型的意义就在于为对象的查找机制提供一个方向，或者说一条路线，但是他是一个非标准属性，因此实际开发中，不可以使用这个属性，他只是内部指向原型对象prototype

首先看对象上是否有该方法，如果有就执行这个对象上的方法，如果没有该方法，因为有__ proto__的存在，就去构造函数原型对象prototype身上去查找这个方法

**constructor构造函数**

对象原型__ proto__和构造函数prototype原型对象里面都有一个属性constructor属性，constructor我们称为构造函数，因为它指回构造函数本身

constructor主要用于记录该对象引用于哪个构造函数，它可以让原型对象重新指向原来的构造函数

**构造函数、实例。原型对象三者之间的关系**

![](C:\Users\admin\Desktop\个人资料\笔记\img\JavaScript_4.png)

**原型链**

![](C:\Users\admin\Desktop\个人资料\笔记\img\JavaScript_5.png)

**JavaScript的成员查找机制（规则）**

1. 当访问一个对象的属性（包括方法）时，首先查找这个对象自身有没有该属性
2. 如果没有就查找它的原型（也就是__ proto __指向的prototype原型对象）
3. 如果还没有就查找原型对象的原型（Object的原型对象）
4. 依次类推一直找到Object为止（null）
5. __ proto __对象原型的意义就在于为对象成员查找机制提供一个 方向，或者说一条路线

**原型对象this指向**

原型对象函数里的this指向的是实例对象

**扩展内置对象**

可以通过原型对象，对原来的内置对象进行扩展自定义的方法。比如给数组增加自定义求偶数和的功能

```js
Array.prototype.sum = function() {
    var sum = 0;
    for (var i = 0; i < this.length; i++) {
        sum += this[i];
    }
    return sum;
}
```

#### 继承

ES6之前没有extends继承，我们可以通过构造函数+原型对象模拟实现继承，被称为组合继承

**call()**

调用这个函数并且修改函数运行时的this指向

```js
fun.call(thisArg,arg1,arg2, ...)
```

+ thisArg：当前调用函数this的指向对象
+ arg1,arg2：传递的其他参数

**借用构造函数继承父类型属性**

核心原理：通过call()把父类型的this指向子类型的this，这样就可以实现子类型继承父类型的属性

![](C:\Users\admin\Desktop\个人资料\笔记\img\JavaScript_6.png)

#### 类的本质

1. class本质还是function
2. 类的所有方法都定义在类的prototype属性上
3. 类创建的实例，里面也有__ proto __指向类的prototype原型对象
4. 所以ES6的类他的绝大部分功能，ES5都可以做到，新的class写法只是让对象原型的写法更加清晰、更像面向对象编程的语法而已

#### ES5中新增方法

**数组方法**

迭代（遍历）方法：forEach()、map()、filter()、some()、every()

```js
array.forEach(function(currentValue,index,arr))
```

+ currentValue：数组当前项的值
+ index：数组当前项的索引
+ arr：数组对象本身

```js
array.filter(function(currentValue,index,arr))
```

+ filter()方法创建一个新的数组，新数组中的元素是通过检查指定数组中符合条件的所有元素，主要用于筛选数组
+ 注意它直接返回一个新数组
+ currentValue：数组当前项的值
+ index：数组当前项的索引
+ arr：数组对象本身

```js
array.some(function(currentValue,index,arr))
```

+ some()方法用于检测数组中的元素是否满足指定条件
+ 注意它的返回值是布尔值，如果查找到这个元素就返回true，否则返回false
+ 如果找到第一个满足条件的元素，则终止循环，不在继续查找
+ currentValue：数组当前项的值
+ index：数组当前项的索引
+ arr：数组对象本身

**字符串方法**

```js
str.trim()
```

trim()方法会从一个字符串的两端删除空白字符

trim()方法并不影响字符串本身，它返回的是一个新字符串

**对象方法**

```js
Object.keys(obj)
```

获取对象自身所有的属性

+ 效果类似for...in
+ 返回一个由属性名组成的数组

```JS
Object.defineProperty(obj,prop,descriptor)
//
Object.defineProperty(obj,'num',{
	value: 1000
})
```

Object.defineProperty()定义对象中新属性或修改原有的属性

+ obj：必需，目标对象
+ prop：必需，需定义或修改的属性的名字
+ descriptor：必需，目标属性所拥有的特性

Object.defineProperty()第三个参数descriptor说明：以对象形式{}书写

+ value：设置书写的值，默认为undefined
+ writable：值是否可以修改。true|false 默认为false
+ enumerable：目标属性是否可以被枚举，true|false 默认为false
+ configurable：目标属性是否可以被删除或是否可以再次修改特性，true|false 默认为false

#### 函数进阶

**函数的定义方式**

1. 函数声明方式function 关键字(命名函数)
2. 函数表达式（匿名函数）
3. new Function()

```js
var fn = new Function('参数1','参数2'...,'函数体')
```

+ Function里面参数都必须是字符串格式
+ 第三种执行效率低，也不方便写，因此较少使用
+ 所有函数都是Function的实例
+ 函数也属于对象

**函数的调用方式**

1. 普通函数

   ```js
   function fn() {
       console.log('111');
   }
   fu();或fn.call()
   ```

2. 对象的方法

   ```js
   var o = {
       sayHi : function() {
       console.log('111');
       }
   }
   o.sayHi();
   ```

3. 构造函数

   ```js
   function Star() {};
   new Star();
   ```

4. 绑定事件函数

   ```js
   btn.onclick = function() {};
   //点击了按钮就可以调用这个函数
   ```

5. 定时器函数

   ```js
   setInterval(funtion() {},1000);
   //这个函数是定时器自动1秒钟调用一次
   ```

6. 立即执行函数

   ```js
   (function() {
       console.log('111');
   })()
   //立即执行函数是自动调用
   ```

**函数内this的指向**

| 调用方式     | this指向                                   |
| ------------ | ------------------------------------------ |
| 普通函数调用 | window                                     |
| 构造函数调用 | 实例对象，原型对象里面的方法也指向实例对象 |
| 对象方法调用 | 该方法所属对象                             |
| 事件绑定方法 | 绑定事件对象                               |
| 定时器函数   | window                                     |
| 立即执行函数 | window                                     |

**改变函数内部this指向**

1. call方法

   call()方法调用一个对象。简单理解为调用函数的方式，但是它可以改变函数的this指向

   ```js
   fun.call(thisArg,arg1,arg2,...)
   ```

2.  apply方法

   ```js
   fun.apply(thisArg,[argsArray])
   ```

   + thisArg：在fun函数运行时指定的this值
   + argsArray：传递的值，必须包含在数组里面
   + 返回值就是函数的返回值，因为他就是调用函数

   ```js
   //apply的主要应用，比如我们可以利用apply借助于数学内置对象求最大值
   var arr = [1,66,3,99,4];
   var max = Math.max.apply(Math,arr);
   ```

3. bind方法

   bind()方法不会调用函数，但是能改变函数内部this指向

   ```js
   fun.bind(thisArg,arg1,arg2,..)
   ```

   + thisArg：在fun函数运行时指定的this值
   + arg1、arg2：传递的其他参数
   + 返回由指定的this值和初始化参数改造的原函数拷贝

#### 严格模式

JavaScript除了提供正常模式外，还提供了严格模式（strict mode）。ES5的严格模式是采用具有限制性JavaScript变体的一种方式，即在严格的条件下运行JS代码

严格模式在IE10以上版本的浏览器中才会被支持，旧版本浏览器中会被忽略

严格模式对正常的JavaScript语义做了一些更改：

1. 消除了JavaScript语法的一些不合理、不严谨之处，减少了一些怪异行为
2. 消除代码运行的一些不安全之处，保证代码运行的安全
3. 提高编译器效率，增加运行速度
4. 禁用了在ECMAScript的未来版本中可能会定义的一些语法，为未来新版本的JavaScript做好铺垫。比如一些保留字：class,enum,export,extends,import,super不能做变量名

**开启严格模式**

严格模式可以应用到整个脚本或个别函数中。因此，在使用时，我们可以将严格模式分为为脚本开启严格模式和为函数开启严格模式两种情况

为整个脚本文件开启严格模式，需要在所有语句之前放一个特定语句"use strict";或（'use strict';）

```html
<script>
    "use strict";
    console.log("这是严格模式")
</script>
<!--因为"use strict"加了引号，所以老版本的浏览器会把他当作一行普通字符串而忽略-->
<script>
	(function() {
        "use strict";
        var num = 10;
        function fun() {}
    })();
</script>
```

有的script基本是严格模式，有的script脚本是正常模式，这样不利于文件合并，所以可以将整个脚本文件放在一个立即执行的匿名函数中。这样独立创建一个作用域而不影响其他script脚本文件

```js
function fn() {
    'use strict';
    //下面的代码按照严格模式执行
}

function fun() {
    //里面的还是按照普通模式执行
}
```

**严格模式中的变化**

严格模式对JavaScript的语法和行为，都做了一些改变

1. 变量规定
   1. 在正常模式中，如果一个变量没有声明就赋值，默认是全局变量。严格模式禁止这种写法，变量都必须先用var命令声明，然后再使用
   2. 严禁删除已经声明变量。例如，delete x;语法是错误的
2. 严格模式下this指向问题
   1. 以前再全局作用域函数中的this指向window对象
   2. 严格模式下全局作用域中函数的this是undefined
   3. 以前构造函数时不加new也可以调用，当普通函数，this指向全局变量
   4. 严格模式下，如果构造函数不加new调用，this会报错
   5. new实例化的构造函数指向创建的对象实例
   6. 定时器this还是指向window
   7. 事件、对象还是指向调用者
3. 函数变化
   1. 函数不能有重名的参数
   2. 函数必须声明在顶层。新版本的JavaScript会引入"块级作用域"（ES6中已引入），为了与新版本接轨，不允许在非函数的代码块内声明函数

#### 高阶函数

高阶函数是对其他函数继续操作的函数，它接收函数作为参数或将函数作为返回值输出

#### 闭包

闭包（closure）指有权访问另一个函数作用域中变量的函数

闭包的作用，延伸了变量的作用范围 

```js
//fn外面的作用域可以访问fn内部的局部变量
function fn() {
    var num = 10;
    function fun() {
        console.log(num);
    }
    return fun;
}
var f = fn();
f();
```

#### 递归

如果一个函数在内部可以调用其本身，那么这个函数就是递归函数

**浅拷贝和深拷贝**

1. 浅拷贝只是拷贝一层，更深层次对象级别的只拷贝引用
2. 深拷贝拷贝多层，每一级别的数据都会拷贝
3. Object.assign(target,...source) es6新增方法可以浅拷贝

#### 正则表达式

正则表达式（Regular Expression）是用来匹配字符串中字符组合的模式。在JavaScript中，正则表达式也是对象

正则表达式通常被用来检索、替换那些符合某个模式（规则）的文本，例如验证表单：用户名表单只能输入英文字母、数字或者下划线，昵称输入框中可以中文（匹配）。此外，正则表达式还常用于过滤掉页面内容中的一些敏感词（替换），或从字符串中获取我们想要的特定部分（提取）等

**正则表达式的特点**

1. 灵活性、逻辑性、功能性非常强
2. 可以迅速地利用极简单的方式达到字符串的复杂控制
3. 对于刚接触的人来说，比较晦涩难懂
4. 实际开发中，一般都是直接复制写好的正则表达式，但是要求会使用正则表达式并且根据实际情况修改正则表达式

**创建正则表达式**

1. 通过调用RegExp对象的构造函数创建

   ```js
   var regExp = new RegExp(/表达式/);
   ```

2. 通过字面量创建

   ```js
   var 变量名 = /表达式/;
   ```

**测试正则表达式 test**

test()正则对象方法，用于检测字符串是否符合该规范，该对象会返回true或false，其参数是测试字符串

```js
regexObj.test(str);
```

1. regexObj是写的正则表达式
2. str 我们要测试的文本
3. 就是检测str文本是否符合我们写的正则表达式规范

**正则表达式的组成**

一个正则表达式可以由简单的字符构成，比如/abc/，也可以是简单和特殊字符的组合，比如/ab*c/，其中特殊字符也被称为元字符，在正则表达式中是具有特殊意义的专用符号，如^、$、+等

**边界符**

正则表达式中的边界符（位置符）用来提示字符所处的位置，主要有两个字符

| 边界符 | 说明                           |
| ------ | ------------------------------ |
| ^      | 表示匹配行首的文本（以谁开始） |
| $      | 表示匹配行尾的文本（以谁结束） |

如果^和$在一起，表示必须是精确匹配

**字符类**

1. []表示有一系列字符可供选择，只要匹配其中一个就可以了，所有可供选择的字符都放在方括号内

   ```js
   var rg =/[abc]/;  //只要包含a或者b或者c都返回true
   console.log(rg.test("abc"));
   var rg =/^[abc]$/;  //三选一，只有是a或者b或者c这三个字母才返回true
   ```

2. [-]方括号内部范围符-

   ```js
   var rg =/^[a-z]$/;  //26个英文字母任何一个字母
   var rg =/^[^a-zA-Z0-9]$/;  //如果中括号里面有^，表示取反的意思
   ```

**量词符**

量词符用来设定某个模式出现的次数

| 量词  | 说明             |
| ----- | ---------------- |
| *     | 重复零次或更多次 |
| +     | 重复一次或更多次 |
| ？    | 重复零次或一次   |
| {n}   | 重复n次          |
| {n,}  | 重复n次或更多次  |
| {n,m} | 重复n到m次       |

```js
var rg =/^a*$/;
var rg =/^[^a-zA-Z0-9]{6,16}$/;
```

**括号总结**

1. 大括号 量词符，里面表示重复次数
2. 中括号 字符集合，匹配方括号中的任意字符
3. 小括号 表示优先级

**预定义类**

预定义类指的是某些常见模式的简写方式

| 预定类 | 说明                                                         |
| ------ | ------------------------------------------------------------ |
| \d     | 匹配0-9之间的任一数字，相当于[0-9]                           |
| \D     | 匹配所有0-9以外的字符，相当于[ ^0-9]                         |
| \w     | 匹配任意的字母数字下划线，相当于[A-Za-z0-9_]                 |
| \W     | 除所有的字母数字下划线以外的字符，相当于[ ^A-Za-z0-9_]       |
| \s     | 匹配空格（包括换行符、制表符、空格符等），相当于[\t\r\n\v\f] |
| \S     | 匹配非空格的字符，相当于[ ^\t\r\n\v\f]                       |

**replace替换**

replace()方法可以实现替换字符串操作，用来替换的参数可以是一个字符串或是一个正则表达式

```js
stringObject.replace(regexp/substr,replacement);
```

**正则表达式参数**

```js
/表达式/[switch]
```

switch(也称为修饰符)按照什么样子的模式来匹配。有三种值：

+ g：全局匹配
+ i：忽略大小写
+ gi：全局匹配+忽略大小写

```js
stringObject.replace(/激情|gay/g,'**');
```

### ES6

#### let

```js
//1.变量不能重复声明
let star = '1';
let star = '2'; //会报错

//2.块级作用域  全局，函数，eval
{
    let girl = 'aa';
    var boy = 'bb';
}
console.log(girl); //girl is not defined
console.log(girl); //不会报错

//3.不存在变量提升
console.log(song); //报错
let song = 'aa';

//4.不影响作用域链
{
   let school = '尚硅谷';
   function fn(){
       console.log(school);
   }
   fn();
} 
```

#### const

```JS
//声明常量
const SCHOOL = '尚硅谷';

//1.一定要赋初始值
//2.一般常量使用大写
//3.常量的值不能修改
//4.块级作用域
{
    const PLAER = 'A';
}
console.log(PLAYER); //报错
//5.对于数组和对象的元素修改，不算做对常量的修改，不会报错
const TEAM = ['A','B','C'];
TEAM.push('D'); //数组地址没变，不会报错
TEAM = 100; //报错
```

**解构赋值**

ES6允许按照一定模式从数组和对象中提取值，对变量进行赋值，这被称为解构赋值

```js
//1.数组的解构
const F4 = ['小沈阳','刘能','赵四','宋小宝'];
let [xiao,liu,zhao,song] = F4;
console.log(xiao); //小沈阳
console.log(liu);  //刘能
console.log(zhao); //赵四
console.log(song); //宋小宝
//2.对象的解构
const zhao = {
    name: '赵本山',
    name: '不详',
    xiaopin: function(){
        console.log("我可以演小品");
    }
}
let {name,age,xiaopin} = zhao;
```

#### 模板字符串

ES6引入新的声明字符串的方式，反引号``

```js
//1.声明
let str = `我也是一个字符串哦！`;
console.log(str,typeof str);
//2.内容中可以直接出现换行符
let str = `<ul>
            <li>a</li>
            <li>b</li>
            <li>c</li>
           </ul>`;
//3.变量拼接
let lovest = 'a';
let out = `${lovest}是我心目中最搞笑的演员`;
console.log(out); //a是我心目中最搞笑的演员
```

#### 对象的简化写法

ES6允许在大括号里面，直接写入变量和函数，作为对象的属性和方法

```js
let name = '尚硅谷';
let change = function({
    console.log('11111111');
})

const school = {
    name, //等同于name:name,
    change,
    improve(){
        console.log("22222");
    }
/*    improve: function(){
        console.log("22222");
    }
*/
}
```

#### 箭头函数

ES6允许使用箭头 =>定义函数

```js
/*let fn = function(){
    
  }
*/
let fn = (a,b) => {
    return a + b;
}
let result = fu(1,2);

//1.this是静态的，this始终指向函数声明时所在作用域下的this的值
//2.不能作为构造函数实例化对象
let Person = (name,age) => {
    this.name = name;
    this.age = age;
}
let me = new Person('xiao',30);
console.log(me); //报错

//3.不能使用arguments变量
/*4.箭头函数的简写
    1）省略小括号，当形参有且只有一个的时候
    2）省略花括号，当代码体只有一条语句的时候，此时return必须省略，而且语句的执行结果就是函数的返回值
*/
let add = n => {
    return n + n;
}
let pow = n => n * n;

//箭头函数适合与this无关的回调，定时器，数组的方法回调
//箭头函数不适合与this有关的回调，事件回调，对象的方法
```

#### ES6允许给函数参数赋值初始值

```js
//1.形参初始值,具有默认值的参数，一般位置要靠后
function add(a,b,c=10) {
    return a+b+c;
}
let result = add(1,2);
console.log(result); //13
//2.与结构赋值结合
function connect({host,username,password,port}) {
    console.log(host);
    console.log(username);
    console.log(password);
    console.log(port);
}
connect({
    host: 'localhost',
    username: 'root',
    password: 'root',
    port: 3306
})
```

#### rest参数

ES6引入rest参数，用于获取函数的实参，用来代替arguments

```js
function date(...args) {
    console.log(args);
}
//rest参数必须要放到参数最后
function fn(a,b,...args){
    console.log(args);
}
```

#### 扩展运算符(...)

扩展运算符能将数组转换为逗号分隔的参数序列

```js
const tyBoys = ['易烊千玺','王源','王俊凯'];
function chunwan(){
    console.log(arguments);
}
chunwan(...tfBoys); //chunwan('易烊千玺','王源','王俊凯');

//数组合并
const kuaizi = ['王太利','肖央'];
const fenghuang = ['曾毅','玲花'];
const a = [...kuaizi,...fenghuang];
//将伪数组转为真正的数组
const divs = document.querySelectorAll('div');
const divArr = [...div];
```

#### Symbol

ES6引入了一种新的原始数据类型Symbol，表示独一无二的值，它是JavaScript语言的第七种数据类型，是一种类似于字符串的数据类型

Symbol特点：

+ Symbol的值是唯一的，用来解决命名冲突的问题
+ Symbol值不能与其他数据进行运算
+ Symbol定义的对象属性不能使用for...in循环变量，但是可以使用Reflect.ownKeys来获取对象的所有键名

```js
let s = Symbol();
console.log(s,typeof s); //Symbol() "symbol"

let s2 = Symbol('尚硅谷');
let s3 = Symbol('尚硅谷');
console.log(s2 === s3); //false

let s4 = Symbol.for('尚硅谷');
let s5 = Symbol.for('尚硅谷');
console.log(s4,typeof s4); //Symbol('尚硅谷') "symbol"
console.log(s4 === s5); //true

//向对象中添加方法
let game = {}

let methods = {
    up: Symbol(),
    down: Symbol()
}

game[methods.up] = function() {
    console.log("我可以改变形状");
}

game[methods.down] = function() {
    console.log("我可以快速下降");
}

//向对象中添加属性
let youxi = {
    name: '狼人杀',
    [Symbol('say')]: function(){
        console.log("我可以发言")
    },
    [Symbol('zibao')]: function(){
        console.log("我可以自爆")
    }
}
```

**Symbol内置值**

| 值                        | 说明                                                         |
| ------------------------- | ------------------------------------------------------------ |
| Symbol.hasInstance        | 当其他对象使用instanceof运算符，判断是否为该对象的实例时，会调用这个方法 |
| Symbol.isConcatSpreadable | 等于是一个布尔值，表示该对象用于Array.prototype.concat()时，是否可以展开 |
| Symbol.unscopables        | 该对象指定了使用with关键字时，哪些属性会被with环境排除       |
| Symbol.match              | 当执行str.match(myObject)时，如果该属性存在，会调用它，返回该方法的返回值 |
| Symbol.replace            | 当该对象被str.replace(myObject)方法调用时，会返回该方法的返回值 |
| Symbol.search             | 当该对象被str.search(myObject)方法调用时，会返回该方法的返回值 |
| Symbol.split              | 当该对象被str.split(myObject)方法调用时，会返回该方法的返回值 |
| Symbol.iterator           | 对象进行for...in循环时，会调用Symbol.iterator方法，返回该对象的默认遍历器 |
| Symbol.toPrimitive        | 该对象被转换为原始类型的值时，会调用这个方法，返回该对象对应的原始类型值 |
| Symbol.toStringTag        | 在该对象上调用toString方法时，返回该方法的返回值             |
| Symbol.species            | 创建衍生对象时，会使用该属性                                 |

#### 生成器函数

生成器函数是ES6提供的一种异步编程解决方案，语法行为与传统函数完全不同

```js
function * gen(){
    console.log("hello generator")
}

let iterator = gen();
console.log(iterator); //没有输出hello generator
iterator.next(); //hello generator

//yield，函数代码的分隔符
function * gen(){
    console.log("111");
    yield '一只没有耳朵';
    console.log("222");
    yield '一只没有尾巴';
    console.log("333");
    yield '真奇怪';
    console.log("444");
}
let iterator = gen();
iterator.next(); //111
iterator.next(); //222
iterator.next(); //333
iterator.next(); //444
```

#### promise

promise是ES6引入的异步编程的新解决方案。语法上Promise是一个构造函数，用来封装异步操作并可以获取其成功或失败的结果

为什么要用promise

1. 指定回调函数的方式更加灵活
   + 旧的：必须在启动异步任务前指定
   + promise：启动异步任务 => 返回promise对象 => 给promise对象绑定回调函数（甚至可以在异步任务结束后指定多个）
2. 支持链式调用，可以解决回调地狱问题

```js
const p = new Promise(function(resolve,reject){
   setTimeout(function(){
       let data = '数据库中的用户数据';
       //resolve,调用这个方法，将状态改为成功，调用p.then中成功的方法
       resolve(data);
       
       let err = '数据读取失败';
       reject(err);
   },1000); 
});

//then方法的返回结果是Promise对象，对象状态由回调函数的执行结果决定
//1.如果回调函数中返回的结果是非Promise类型的属性，状态为成功，返回值为对象的成功的值
const result = p.then(function(value){
    //resolve后调用
    console.log(value); //'数据库中的用户数据'
    //非promise返回值
    //return 123;  返回123
    //2.promise返回值
    return new Promise((resolve,reject)=>{
        //resolve('ok');  //返回ok
        reject('error');  //返回error
    })
    //3.抛出错误
    throw new Error('出错了');  //状态为失败，返回'出错了'
},function(reason){
    //reject后调用
    console.error(reason); //'数据读取失败' 
})

console.log(result);

//promise.catch()，指定promise失败后的一个回调
p.catch(function(reason){
    console.warn(reason);
})
```

**promise的状态**

实例对象中的一个属性：PromiseState，三种状态：

+ pending 未决定的
+ resolved / fullfilled 成功
+ rejected 失败

状态的改变只有两种可能：

1. pending变为resolved
2. pending变为rejected

说明：只有这两种，且一个promise对象只能改变一次，无论变为成功还是失败，都会有一个结果数据，成功的结果数据一般称为value，失败的结果数据一般称为reason

**promise对象的值**

实例对象中的另一个属性：PromiseResult

保存着异步任务成功/失败的结果

+ resolve
+ reject

**promise基本流程**

![](C:\Users\admin\Desktop\个人资料\笔记\img\JavaScript_7.png)

**API**

1. Promsie构造函数：Promise(excutor){}

   + excutor函数：执行器(resolve,reject) => {}
   + resolve函数：内部定义成功时我们调用的函数 value => {}
   + reject函数：内部定义失败时我们调用的函数 reason => {}
   + excutor会在Promise内部立即同步调用，异步操作在执行器中执 行

2. Promise.prototype.then方法：(onResolved,onRejected) => {}

   + onResolved函数：成功的回调函数(value) => {}
   + onRejected函数：失败的回调函数(reason) => {}

3. Promise.prototype.catch方法：(onRejected) => {}

   + onRejected函数：失败的回调函数(reason) => {}

4. Promise.resolve方法：(value) => {}

   + value：成功的数据或promise对象

   + 返回一个成功/失败的promise对象

5. Promise.reject方法：(reason) => {}

   + reason：失败的原因
   + 返回一个失败的promise对象

6. Promise.all方法：(promises) => {}

   + promises：包含n个promise的数组
   + 返回一个新的promise，只有所有的promise都成功才成功，只要有一个失败了就直接失败

7. Promise.race方法：(promises) => {}

   + promises：包含n个promise的数组
   + 返回一个新的promise，第一个完成的promise的结果状态就是最终的结果状态

**promise关键问题**

1. 如何修改对象的状态

   + resolve
   + reject
   + throw抛出错误

2. 一个promise指定多个成功/失败回调函数（p.then），都会调用吗

   当promise改变对应状态时都会执行

3. 改变promise状态和指定回调函数谁先谁后？

   + 都有可能，正常情况下是先指定回调再改变状态，但也可以先改状态再指定回调
   + 如何先改状态再指定回调
     + 在执行器中直接调用resolve()/reject()
     + 延迟更长时间才调用
   + 什么时候才能得到数据
     + 如果先指定的回调，那当状态发生改变时，回调函数就会调用，得到数据
     + 如果先改变状态，那当指定回调时，回调函数就会调用，得到数据

4. promise.then()返回新promise的结果状态由什么决定

   + 简单表达：由then()指定的回调函数执行的结果决定
   + 详细表达：
     + 如果抛出异常，新promise变为rejected，reason为抛出的异常
     + 如果返回的是非promise的任意值，新promise变为resolved，value为返回的值
     + 如果返回的是另一个新promise，此promise的结果就会成为新promise的结果

5. promise如何串联多个操作任务

   + promise的then()返回一个新的promise，可以看成then()的链式调用
   + 通过then的链式调用串联多个同步/异步任务

6. promise异常穿透

   + 当使用promise的then链式调用时，可以在最后指定失败的回调
   + 前面任何操作出了异常，都会传到最后失败的回调中处理

7. 中断promise链 

   + 当使用promise的then链式调用时，在中间中断，不再调用后面的回调函数
   + 办法：在回调函数中返回一个pending状态的promise对象

**async与await**

1. async函数

   + 函数的返回值为promise对象
   + promise对象的结果由async函数执行的返回值决定

   ```js
   async function main(){
       //1.如果返回值是一个非promise类型的数据
       //return 521;
       //2.如果返回的是一个promise对象
       return new Promise((resolve,reject) => {
          resolve('OK');
          reject('Error')
          //抛出异常
           throw "oh no";
       });
   } 
   let result= main();
   ```

2. await表达式

   + await右侧的表达式一般为promise对象，但也可以是其他的值
   + 如果表达式是promise对象，await返回的是promise成功的值
   + 如果表达式是其他值，直接将此值作为await的返回值

3. 注意

   + await必须写在async函数中，但async函数中可以没有await
   + 如果await的promise失败了，就会抛出异常，需要通过try...catch...捕获

   ```js
   async function main(){
       let p = new Promise((resolve,reject) => {
          resolve('OK');
       });
       let res = await p;
       //如果promise是失败的状态
       try{
           let res1 = await p;
       }catch(e){
           console.log(e); //失败的结果
       }
   } 
   main();
   ```

4. 

#### Set

ES6提供了新的数据结构Set（集合）。它类似于数组，但成员的值都是唯一的，集合实现了iterator接口，所以可以使用扩展运算符和for...of进行遍历

集合的属性和方法：

+ size，返回集合的元素个数
+ add，增加一个新元素，返回当前集合
+ delete，删除元素，返回boolean值
+ has，检测集合中是否包含某个元素，返回boolean值

```js
let s = new Set();
let s2 = new Set(['1','2','3','1']); //1,2,3
```

#### Map

ES6提供了Map数据结构，他类似对象，也是键值对的集合，但是键的范围不限于字符串，各种类型的值（包括对象）都可以当作键。Map也实现了iterator接口，所以可以使用扩展运算符和for...of进行遍历。

Map的属性和方法：

+ size，返回Map的元素个数
+ set，增加一个新元素，返回当前Map
+ get，返回键名对象的键值
+ has，检测Map中是否包含某个元素，返回boolean值
+ clear，清空集合，返回undefined

#### class类

ES6提供了更接近传统语言的写法，引入了Class(类)这个概念，作为对象的模板。通过class关键字，可以定义类，ES6的class，可以看作是一个语法糖，他的绝大部分功能，ES5都能做到，新的class写法只是让对象原型的写法更加清晰、更像面向对象编程的语法

+ class声明类
+ constructor定义构造函数初始化
+ extends继承父类
+ super调用父级构造方法
+ static定义静态方法和属性
+ getter和setter方法
+ 父类方法可以重写

#### ES6数值扩展

```js
//1.Number.EPSILON  JavaScript表示的最小精度，EPSILON的属性值接近于2.2204460492503130808472633361816E-16

//2.二进制和八进制
let b =0b1010;  //二进制
let o = 0o777;  //八进制

//3.Number.isfinite 检测一个数值是否为有限数
//4.Number.isNaN 检测一个数值是否为NaN
//5.Number.parseInt Number.parseFloat 字符串转整数
//6.Number.isInteger 判断一个树是否为整数
//7.Math.trunc 将数字的小数部分抹掉
//8.Math.sign 判断一个数到底是正数，负数还是零
```

#### ES6对象方法扩展

```js
//1.Object.is 判断两个值是否完全相等
console.log(Object.is(NaN,NaN)); //true
console.log(NaN === NaN); //false
//2.Object.assign 对象的合并
//3.Object.setPrototypeOf Object.getPrototypeOf  设置获取原型对象
```

#### ES6模块化

模块化是指将一个大的程序文件，拆分成许多小的文件，然后将小的文件组合起来

模块化的好处：

+ 防止命名冲突
+ 代码复用
+ 高维护性

**ES6模块化语法**

模块化功能主要由两个命令构成：export和import

+ export命令用于规定模块的对外接口
+ import命令用于输入其他模块提供的功能

```js
//m.js文件
<script type="module">
    //1.通用的导入方式
    //引入m1.js模块内容
    import * as m1 from "./src/js/m1.js";
    import * as m2 from "./src/js/m2.js";
    import * as m3 from "./src/js/m3.js";
    //2.解构赋值形式
    import{school,teach} from "./src/js/m1.js";
    import{school as guigu,findJob} from "./src/js/m2.js";
    import{default as m3} from "./src/js/m3.js";
    //3.简便形式，针对默认暴露
    import m3 from "./src/js/m3.js";
</script>

//m1.js文件，分别暴露
export let school = '尚硅谷';

export function teach() {
    console.log("111");
}

//m2.js文件，统一暴露
let school = '尚硅谷';

function findJob() {
    console.log("111");
}

export {school,findJob};

//m3.js，默认暴露
export default {
    school: 'ATGUIGU',
    change: function(){
        console.log('111');
    }
}
```

**浏览器使用ES6模块化引入**

```js
//m.js文件
<script src=".src/js/app.js" type="module"></script>

//app.js，入口文件
//模块引入
import * as m1 from "./m1.js";
import * as m2 from "./m2.js";
import * as m3 from "./m3.js";
```

### AJAX

全称为Asynchronous JavaScript And Xml，就是异步的JS和XML，通过Ajax可以在浏览器中向服务器发送异步请求，最大的优势：无刷新获取数据

**Ajax优点**

1. 可以无需刷新页面而与服务器端进行通信
2. 允许你根据用户事件来更新部分页面内容

**Ajax缺点**

1. 没有浏览历史，不能回退
2. 存在跨域问题（同源）
3. SEO不友好

**GET请求**

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AJAX GET请求</title>
    <style>
        #result {
            width: 200px;
            height: 200px;
            border: solid 1px #90b;
        }
    </style>
</head>

<body>
    <button>点击发送请求</button>
    <div id="result"></div>
    <script>
        const btn = document.getElementsByTagName('button')[0];
        const result = document.getElementById('result');
        btn.onclick = function() {
            //1. 创建对象
            const xhr = new XMLHttpRequest();
            //2. 初始化 设置请求方法和url
            xhr.open('GET', 'http://127.0.0.1:8000/server?a=100&b=200&c=300');
            //3. 发送
            xhr.send();
            //4.事件绑定 处理服务端返回的结果
            //on when 当...时候
            //readystate 是xhr对象中的属性，表示状态0,1,2,3,4
            //0创建对象，1初始化，2发送，3返回部分结果，4返回所有结果
            //change 改变
            xhr.onreadystatechange = function() {
                //服务端返回了所有的结果
                if (xhr.readyState === 4) {
                    //判断响应状态码
                    //2xx 成功
                    if (xhr.status >= 200 && xhr.status < 300) {
                        //处理结果 行 头 空行 体
                        //1.响应行
                        // console.log(xhr.status);//状态码
                        // console.log(xhr.statusText);//状态字符串
                        // console.log(xhr.getAllResponseHeaders);//所有响应头
                        // console.log(xhr.response);//响应体

                        //设置result的文本
                        result.innerHTML = xhr.response;
                    } else {

                    }
                }
            }
        }
    </script>
</body>

</html>
```

**POST请求**

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AJAX POST请求</title>
</head>
<style>
    #result {
        width: 200px;
        height: 200px;
        border: solid 1px #903;
    }
</style>

<body>
    <div id="result"></div>
    <script>
        const result = document.getElementById('result');
        result.addEventListener("mouseover", function() {
            //1. 创建对象
            const xhr = new XMLHttpRequest();
            //2. 初始化 设置请求方法和url
            xhr.open('POST', 'http://127.0.0.1:8000/server');
            //设置请求头
            xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            //3. 发送
            xhr.send('=100&b=200&c=300');
            //4.事件绑定
            xhr.onreadystatechange = function() {
                //服务端返回了所有的结果
                if (xhr.readyState === 4) {
                    //判断响应状态码
                    //2xx 成功
                    if (xhr.status >= 200 && xhr.status < 300) {
                        //处理结果 行 头 空行 体

                        result.innerHTML = xhr.response;
                    } else {

                    }
                }
            }
        })
    </script>
</body>

</html>
```

**同源策略**

同源：协议、域名、端口号必须完全相同

违背同源策略就是跨域

### axios

