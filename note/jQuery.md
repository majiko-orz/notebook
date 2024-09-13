### jQuery

jQuery是一个快速、简洁的JavaScript库，把js中的DOM操作做了封装，我们可以快速的查询使用里面的功能

版本：

+ 1x：兼容IE678等低版本浏览器，官网不再更新
+ 2x：不兼容IE678等低版本浏览器，官网不再更新
+ 3x：不兼容IE678等低版本浏览器，是官网主要更新维护的版本

#### jQuery的基本使用

**jQuery的入口函数**

```js
$(function () {
    ... //此处是页面DOM加载完成的入口
});

$(document).ready(function(){
   ... //此处是页面DOM加载完成的入口
});
    
//示例
<body>
    <script>
    $(function() {
    $('div').hide();
})
    </script>
</body>
```

1. 等着DOM结构渲染完毕即可执行内部代码，不必等到所有外部资源加载完成，jQuery帮我们完成了封装
2. 相当于原生js中的DOMContentLoaded
3. 不同于原生js中的load事件是等页面文档、外部的js文件、css文件、图片加载完毕才执行内部代码

**jQuery的顶级对象$**

1. $是jQuery的别称，在代码中可以使用jQuery代替$，但一般为了方便，通常都直接使用$
2. $是jQuery的顶级对象，相当于原生JavaScript中的window。把元素利用$包装成jQuery对象，就可以调用jQuery的方法

**jQuery对象和DOM对象**

1. 用原生JS获取来的对象就是DOM对象
2. jQuery方法获取的元素就是jQuery对象
3. jQuery对象本质是：利用$对DOM对象包装后产生的对象（伪数组形式存储）

**DOM对象和jQuery对象之间是可以相互转换的**

 因为原生js比jQuery更大，原生的一些属性和方法jQuery没有给我们封装，要想使用这些属性和方法需要把jQuery对象转为DOM对象才能使用

1. DOM对象转换为jQuery对象：$(DOM对象)

   ```js
   //1.直接获取视频，得到就是jQuery对象
   $('video');
   
   //2.已经使用原生js获取过来DOM对象
   var myvideo = document.querySelector('video');
   $(myvideo);
   ```

2. jQuery对象转换为DOM对象（两种方式）

   ```js
   $('div')[index] //index是索引号
   
   $('div').get(index) //index是索引号
   ```

#### jQuery基本和层级选择器

**jQuery基础选择器**

原生js获取元素方式很多，很杂，而且兼容性情况不一样，因此jQuery给我们做了封装，使获取元素统一标准

```js
$("选择器") //里面选择器直接写CSS选择器即可，但是要加引号
```

| 名称       | 用法            | 描述                     |
| ---------- | --------------- | ------------------------ |
| ID选择器   | $("#id")        | 获取指定ID的元素         |
| 全选选择器 | $('*')          | 匹配所有元素             |
| 类选择器   | $(".class")     | 获取同一类class的元素    |
| 标签选择器 | $("div")        | 获取同一类标签的所有元素 |
| 并集选择器 | $("div,p,li")   | 选取多个元素             |
| 交集选择器 | $("li.current") | 交集元素                 |

**jQuery层级选择器**

| 名称       | 用法       | 描述                                                         |
| ---------- | ---------- | ------------------------------------------------------------ |
| 子代选择器 | $("ul>li") | 使用>号，获取亲儿子层级的元素；注意，并不会获取孙子层级的元素 |
| 后代选择器 | $("ul li") | 使用空格，代表后代选择器，获取ul下的所有的li元素，包括孙子等 |

**隐式迭代（重要）**

遍历内部DOM元素（伪数组形式存储）的过程就叫做隐式迭代

简单理解：给匹配到的所有元素进行循环遍历，执行相应的方法，而不用我们再进行循环，简化我们的操作，方便我们调用

```js
//jQuery设置样式
//修改所有匹配的元素的属性，不需要for循环
$('div').css('属性','值') 
```

**jQuery筛选选择器**

| 语法       | 用法          | 描述                                                      |
| ---------- | ------------- | --------------------------------------------------------- |
| :first     | $("li:first") | 获取第一个li元素                                          |
| :last      | $("li:last")  | 获取最后一个li元素                                        |
| :eq(index) | $("li:eq(2)") | 获取到的li元素中，选择索引号为2的元素，索引号index从0开始 |
| :odd       | $("li:odd")   | 获取到的li元素中，选择索引号为奇数的元素                  |
| :even      | $("li:enen")  | 获取到的li元素中，选择索引号为偶数的元素                  |

**jQuery筛选方法（重点）**

| 语法               | 用法                           | 说明                                                   |
| ------------------ | ------------------------------ | ------------------------------------------------------ |
| parent()           | $("li").parent();              | 查找父级                                               |
| children(selector) | $("ul").children("li")         | 相当于$("ul>li")，最近一级（亲儿子）                   |
| find(selector)     | $("ul").find("li")             | 相当于$("ul li")，后代选择器                           |
| siblings(selector) | $(".first").siblings("li")     | 查找兄弟节点，不包括自己本身                           |
| nextAll([expr])    | $(".first").nextAll()          | 查找当前元素之后所有的同辈元素                         |
| prevtAll([expr])   | $(".last").prevtAll()          | 查找当前元素之前所有的同辈元素                         |
| hasClass(class)    | $('div').hasClass("protected") | 基础当前的元素是否含有某个特定的类，如果有，则返回true |
| eq(index)          | $("li").eq(2)                  | 相当于$("li:eq(2)")，index从0开始                      |

**jQuery排他思想**

```html
<body>
    <button>快速</button>
    <button>快速</button>
    <button>快速</button>
    <button>快速</button>
    <script>
    	$(function() {
            //1.隐式迭代，给所有的按钮都绑定了点击事件
            $("button").click(function() {
               //2.当前的元素变化背景颜色
                $(this).css("background","pink");
               //3.其余的兄弟去掉背景颜色
                $(this).siblings("button").css("background","");
               //链式编程
                $(this).css("background","pink").siblings("button").css("background","");
            });
        })
    </script>
<body>
```

**链式编程**

```js
$(this).css("color","red").sibling().css("color","");
```

#### jQuery样式操作

**操作css方法**

jQuery可以使用css方法来修改简单元素样式，也可以操作类，修改多个样式

1. 参数只写属性名，则是返回属性值

   ```js
   $(this).css("color");
   ```

2. 参数是属性名，属性值，逗号分隔，是设置一组样式，属性必须加引号，值如果是数字可以不用跟单位和引号

   ```js
   $(this).css("color","red");
   ```

3. 参数可以是对象形式，方便设置多组样式。属性名和属性值用冒号隔开，属性可以不用加引号

   ```js
   $(this).css({"color":"white","font-size":"20px"});
   ```

**设置类样式方法**

作用等同于以前的classLsit，可以操作类样式，注意操作类里面的参数不要加点

1. 添加类

   ```js
   $("div").addClass("current");
   ```

2. 移除类

   ```js
   $("div").removeClass("current");
   ```

3. 切换类

   ```js
   $("div").toggleClass("current");
   ```

**类操作与className区别**

原生JS中的className会覆盖元素原先里面的类名

jQuery里面类操作只是对指定类进行操作，不影响原先的类名，相当于追加类名

```js
<div class="one"></div>
//原生js
var one = document.querySelector(".one");
one.className = "two";  //<div class="two"></div>
//jQuery
$(".one").addClass("two");  //<div class="one two"></div>
```

#### jQuery效果

显示隐藏：show()，hide()，toggle()

滑动：slideDown()，slideUp()，slideToggle()

淡入淡出：fadeIn()，fadeOut()，fadeToggle()，fadeTo()

自定义动画：animate()

**显示隐藏效果**

1. 语法规范

   ```js
   show([speed],[easing],[fn])  //显示
   
   hide([speed],[easing],[fn])  //隐藏
   
   toggle([speed],[easing],[fn])  //切换
   ```

2. 参数

   1. 参数都可以省略，无动画直接显示
   2. speed：三种预定速度之一的字符串（"show"，"normal"，"fast"）或表示动画时长的毫秒数值（如：1000）
   3. easing：（Optional）用来指定切换效果，默认是"swing"，可用参数"linear"
   4. fn：回调函数，在动画完成时执行的函数，每个元素执行一次

**滑动效果**

1. 语法规范

   ```js
   slideDown([speed],[easing],[fn])  
   
   slideUp([speed],[easing],[fn])  
   
   slideToggle([speed],[easing],[fn])  
   ```

2. 参数

   1. 参数都可以省略，无动画直接显示
   2. speed：三种预定速度之一的字符串（"show"，"normal"，"fast"）或表示动画时长的毫秒数值（如：1000）
   3. easing：（Optional）用来指定切换效果，默认是"swing"，可用参数"linear"
   4. fn：回调函数，在动画完成时执行的函数，每个元素执行一次

**事件切换**

```js
hover([over,]out)
```

1. over：鼠标移到元素上要触发的函数（相当于mouseenter）
2. out：鼠标移出元素要触发的函数（相当于mouseleave）

**动画队列及其停止排队方法**

1. 动画或效果队列

   动画或者效果一旦触发就会执行，如果多次触发，就造成多个动画或者效果排队执行

2. 停止排队

   ```js
   stop()
   ```

   1. stop()方法用于停止动画或效果
   2. 注意：stop()写到动画或者效果的前面，相当于停止结束上一次的动画

**淡入淡出效果**

1. 语法规范

   ```js
   fadeIn([speed],[easing],[fn])  
   
   fadeOut([speed],[easing],[fn])  
   
   fadeToggle([speed],[easing],[fn])  
   ```

2. 参数

   1. 参数都可以省略，无动画直接显示
   2. speed：三种预定速度之一的字符串（"show"，"normal"，"fast"）或表示动画时长的毫秒数值（如：1000）
   3. easing：（Optional）用来指定切换效果，默认是"swing"，可用参数"linear"
   4. fn：回调函数，在动画完成时执行的函数，每个元素执行一次

3. 渐进方式调整到指定的不透明度

   ```js
   fadeTo([speed,opacity,[easing],[fn]])
   ```

4. 效果参数

   1. opacity透明度必须写，取值0-1之间
   2. speed：三种预定速度之一的字符串（"show"，"normal"，"fast"）或表示动画时长的毫秒数值（如：1000），必须写
   3. easing：（Optional）用来指定切换效果，默认是"swing"，可用参数"linear"
   4. fn：回调函数，在动画完成时执行的函数，每个元素执行一次

**自定义动画animate**

1. 语法

   ```js
   animate(params,[speed],[easing],[fn])
   ```

2. 参数

   1. params：想要更改的样式属性，以对象形式传递，必须写。属性名可以不用带引号，如果是符合属性则需要采取驼峰命名法borderLedt。其余参数都可以省略

      ```js
      $(function() {
      	$("button").click(function() {
              $("div").animate({
                  left: 500,
                  top: 300,
                  opacity: .4,
                  width: 500 
              },500);
          })
      })
      ```

   2. speed：三种预定速度之一的字符串（"show"，"normal"，"fast"）或表示动画时长的毫秒数值（如：1000），必须写

   3. easing：（Optional）用来指定切换效果，默认是"swing"，可用参数"linear"

   4. fn：回调函数，在动画完成时执行的函数，每个元素执行一次

#### jQuery属性操作

**设置或获取元素固有属性值prop()**

所谓元素固有属性就是元素本身自带的属性，比如<a>元素里面的href，比如<input>元素里面的type

1. 获取属性语法

   ```js
   prop("属性")
   ```

2. 设置属性语法

   ```js
   prop("属性","属性值")
   ```

**设置或获取元素自定义属性值attr()**

用户自己给元素添加的属性，我们称为自定义属性。比如给div添加index = "1"

1. 获取属性语法

   ```js
   attr("属性") //类似原生getAttribute()
   ```

2. 设置属性语法

   ```js
   attr("属性","属性值") //类似原生setAttribute()
   ```

**数据缓存data()**

data()方法可以在指定的元素上存取数据，并不会修改DOM元素结构。一旦页面刷新，之前存放的数据都将被移除

1. 附加数据语法

   ```js
   data("name","value") //向被选元素附加数据
   ```

2. 获取数据语法

   ```js
   data("name") //向被选元素获取数据
   ```

   同时，还可以读取HTML5自定义属性data-index，得到的是数字型

#### jQuery内容文本值

主要针对元素的内容还有表单的值操作

1. 普通元素内容html()（相当于原生innerHTML）

   ```js
   html()  //获取元素的内容
   html("内容")  //设置元素的内容
   ```

2. 普通元素文本内容text()（相当于原生innerText）

   ```js
   text()  //获取元素的文本内容
   text("文本内容")  //设置元素的文本内容
   ```

3. 表单的值val()（相当于原生的value）

#### jQuery元素操作

主要是遍历、创建、添加、删除元素

**遍历元素**

jQuery隐式迭代是对同一类元素做了同样的操作。如果想要给同一类元素做不同操作，就需要用到遍历

```js
$("div").each(function (index, domEle) { xxx;})
```

1. each()方法遍历匹配的每一个元素。主要用DOM处理
2. 里面的回调函数有两个参数：index是每个元素的索引号；domEle是每个DOM元素对象，不是jQuery对象

```js
$.each(object,function(index,element) {xxx;})
```

1. $.each()方法可以遍历任何对象。主要用于数据处理，比如数组，对象
2. 里面的函数有两个参数：index是每个元素的索引号；element是遍历内容

**创建元素**

```js
$("<li></li>")
```

动态的创建了一个<li>

**添加元素**

1. 内部添加

   ```js
   $("ul").append(li)    //把内容放入匹配元素内部最后面，类似原生appendChild
   $("ul").prepend(li)   //把内容放入匹配元素内部最前面
   ```

2. 外部添加

   ```js
   element.after("内容")  //把内容放入目标元素后面
   element.before("内容")  //把内容放入目标元素前面
   ```

   内部添加元素，生成之后，他们是父子关系

   外部添加元素，生成之后，他们是兄弟关系

**删除元素**

```js
element.remove() //删除匹配的元素（本身）
element.empty()  //删除匹配的元素集合中所有的子节点
element.html("") //清空匹配的元素内容，子节点
```

#### jQuery尺寸、位置操作

**jQuery尺寸**

| 语法                                 | 用法                                                  |
| ------------------------------------ | ----------------------------------------------------- |
| width() / height()                   | 取得匹配元素宽度和高度值，只算width/height            |
| innerWidth() / innerHeight()         | 取得匹配元素宽度和高度值，包含padding                 |
| outerWidth() / outerHeight()         | 取得匹配元素宽度和高度值，包含padding、border         |
| outerWidth(true) / outerHeight(true) | 取得匹配元素宽度和高度值，包含padding、border、margin |

+ 以上参数为空，则是获取相应值，返回的是数字型

+ 如果参数为数字，则是修改相应值
+ 参数可以不必写单位

**jQuery位置**

位置主要有三个：offset()、position()、scrollTop()/scrollLeft()

1. offset()设置或获取元素偏移
   + offset()方法设置或返回被选元素相对于文档的偏移坐标，跟父级没有关系
   + 该方法有两个属性left、top。offset().top用于获取距离文档顶部的距离，offset().left用于获取距离文档左侧的距离
   + 可以设置元素的偏移：offset({top:10,left:30})
2. position()获取元素偏移
   + position()方法用于返回被选元素相对于带有定位的父级偏移坐标，如果父级都没有定位，则以文档为准
3. scrollTop()/scrollLeft()设置或获取元素被卷去的头部和左侧
   + scrollTop()方法设置或返回被选元素被卷去的头部

#### jQuery事件

**jQuery事件注册**

单个事件注册

```js
element.事件(function(){})

//$("div").click(function(){事件处理程序})
```

其他事件和原生基本一致

比如mouseover、mouseout、blur、focus、change、keydown、keyup、resize、scroll等

**jQuery事件处理**

1. **事件处理on()绑定事件**

   on()方法在匹配元素上绑定一个或多个事件的事件处理函数

   ```js
   element.on(events,[selector],fn)
   
   //
   $("div").on({
      mouseenter: function() {
          $(this).css("background","skyblue");
      },
       click: function() {
          $(this).css("background","purple");
      }
   });
   //如果事件处理函数相同
   $("div").on("mouseenter mouseleave",function() {
       alert(11);
   })
   ```

   1. events：一个或多个用空格分隔的事件类型，如"click"或"keydown"
   2. selector：元素的子元素选择器
   3. fn：回调函数，即绑定在元素身上的侦听函数

2. on()方法可以事件委派操作

   事件委派的定义就是，把原来加给子元素身上的事件绑定在父元素身上，就是把事件委派给父元素

   ```js
   $("ul").on("click","li",function(){
       alert(11);
   })
   ```

   click是绑定在ul身上的，但是触发的对象是ul中的li

3. on可以给未来动态创建的元素绑定事件

   ```js
   $("ol li").click(function(){  //未绑定click事件
       alert(11);
   })
   $("ol").on("click","li",function(){  //绑定click事件
       alert(11);
   })
   var li = $("<li>我是后来创建的</li>");  
   $("ol").append(li); 
   ```

4. **事件处理off()解绑事件**

   off方法可以移除通过on()方法添加的事件处理程序

   ```js
   $("div").off();  //解除div身上的所有事件
   
   $("div").off("click");  //解除div身上的click事件
   
   $("ul").off("click","li");  //解除事件委托
   ```

5. 如果有的事件只想触发一次，可以使用one()来绑定事件

   ```js
   $("p").one("click",function(){
       alert(11);
   })
   ```

6. **自动触发事件trigger()**

   有些事件希望自动触发，比如轮播图自动播放功能跟点击右侧按钮一致。可以利用定时器自动触发右侧按钮点击事件，不必鼠标点击触发

   ```js
   element.click() //第一种简写形式
   
   element.trigger("事件") //第二种自动触发模式
   
   element.triggerHandler(事件) //第三种自动触发模式，不会触发元素的默认行为
   ```

**jQuery事件对象**

事件被触发，就会有事件对象的产生

```js
element.on(events,[selector],function(event) {})
```

阻止默认行为：event.preventDefault()或者return false

阻止冒泡：event.stopPropagation()

#### jQuery其他方法

**jQuery拷贝对象**

如果想要把某个对象拷贝（合并）给另外一个对象使用，此时可以使用$.extend()方法

```js
$.extend([deep],target,object1,[objectN])
```

1. deep：如果设置为true为深拷贝，默认为false浅拷贝
2. target：要拷贝的目标对象
3. object1：待拷贝到第一个对象的对象
4. objectN：待拷贝到第N个对象的对象
5. 浅拷贝是把被拷贝的对象复杂数据类型中的地址拷贝给目标对象，修改目标对象会影响被拷贝对象

**jQuery多库共存**

jQuery使用$作为标识符，随着jQuery的流行，其他js库也会用这$作为标识符，这样一起使用会引起冲突

jQuery解决方案：

1. 把里面的$符号统一改为jQuery。比如jQuery("div")
2. jQuery变量规定新的名称：$.noConflict()   var xx = $.noConflict();

**jQuery插件**

1. 瀑布流
2. 图片懒加载
3. 全屏滚动