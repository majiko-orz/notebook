**VUE特点**

1. 采用组件化模式，提高代码复用率、且让代码更好维护
2. 声明式编码，让编码人员无需直接操作DOM，提高开发效率
3. 使用虚拟DOM+优秀的Diff算法，尽可能复用DOM节点

### Vue核心

#### 初识vue

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>初识vue</title>
    <script src="../js/vue.js"></script>
</head>

<body>
    <!-- 
        初识vue:
        1.想让vue工作，就必须创建一个vue实例，且要传入一个配置对象
        2.root容器里的代码依然符合html规范，只不过混入了一些特殊的vue语法
        3.root容器里的代码被称为vue模板
        4.Vue实例和容器是一一对应的
        5.真实开发中只有一个Vue实例，并且会配合着组件一起使用
        6.{{xxx}}中的xxx要写成js表达式，且xxx可以自动读取到data中的所有属性
        7.一旦data中的数据发生改变，那么模板中用到该数据的地方也会自动更新
     -->
    <div id="root">
        <h1>Hello,{{name}}</h1>
    </div>

    <div id="root">
        <h1>Hello,{{name}}</h1>
    </div>
    <script>
        Vue.config.productionTip = false //阻止 vue 在启动时生成生产提示

        //创建Vue实例
        new Vue({
            //el: document.getElementById('root')
            el: '#root', //el用于指定当前vue实例为哪个容器服务，值通常为css选择器字符串
            data: { //data用于存储数据，数据供el所指定的容器去使用，值我们暂时先写成一个对象
                name: '尚硅谷aa'
            }
        })
    </script>
</body>

</html>
```

#### 模板语法

1. 插值语法

   功能：用于解析标签体内容

   写法：{{xxx}}，xxx是js表达式，且可以直接读取到data中的所有属性

2. 指令语法

   功能：用于解析标签（包括：标签属性、标签体内容、绑定事件）

   举例：v-bind:href="xxx"或简写为:href="xxx"，xxx同样要写js表达式，且可以直接读取到data中的数据

```html
<body>
    <div id="root">
        <h1>插值语法</h1>
        <h3>你好，{{name}}</h3> 
        <h1>指令语法</h1>
        <a v-bind:href="url">点我去尚硅谷学习1</a>
        <a :href="url">点我去尚硅谷学习2</a> <!--简写-->
    </div>
    
    <script>
        Vue.config.productionTip = false

        new Vue({
            el: '#root', 
            data: { 
                name: 'jack',
                url:'http://www.atguigu.com'
            }
        })
    </script>
</body>
```

#### 数据绑定

1. 单项绑定（v-bind）：数据只能从data流向页面

2. 双向绑定（v-model）：数据不仅能从data流向页面，还可以从页面流向data

   备注：

   + 双向绑定一般都应用在表单类元素上，如input、select等
   + v-model:value可以简写为v-model，因为v-model默认收集的就是value值

```html
<body>
    <div id="root">
        <span>单向数据绑定：</span><input type="text" v-bind:value="name"> <br/>
        <span>双向数据绑定：</span><input type="text" v-model:value="name">
<!-- 简写 -->
        <span>单向数据绑定：</span><input type="text" :value="name"><br/>
        <span>双向数据绑定：</span><input type="text" v-model="name">
        <!-- 如下代码是错误的，因为v-model只能应用在表单类元素（输入类元素上）上 -->
        <h2 v-model:x="name">你好啊</h2>
    </div>

    <script>
        Vue.config.productionTip = false

        new Vue({
            el: '#root',
            data: {
                name: '尚硅谷a'
            }
        })
    </script>
</body>
```

#### el与data的两种写法

el有两种写法

1. new Vue时配置el属性
2. 先创建Vue实例，随后再通过v.$mount('#root')指定el的值

data有两种写法

1. 对象式
2. 函数式

如何选择：目前哪种写法都可以，以后学到组件时，data必须使用函数式，否则会报错

一个重要原则：由Vue管理的函数，一定不要写箭头函数，一旦写了箭头函数，this就不再是Vue实例了

```html
    <script>
        Vue.config.productionTip = false

        const v = new Vue({
            //el: '#root', //第一种写法
            //data的第一种写法：对象式
            /*data: {
                name: '尚硅谷a'
            }*/
            //data的第二种写法：函数式
            data:function(){
                return{
                    name:'尚硅谷a'
                }
            }
        })
        
        v.$mount('#root') //el第二种写法
    </script>
```

#### MVVM模型

1. M：模型Model：对应data中的数据
2. V：视图View：模板
3. VM：视图模型ViewModel：Vue实例对象

![](C:\Users\admin\Desktop\个人资料\笔记\img\Vue_1.png)

注意：

1. data中所有的属性，最后都出现在了vm身上
2. vm身上所有的属性，及Vue原型上所有的属性，在Vue模板中都可以直接使用

#### 数据代理

```html
<script>
let number = 18
let person = {
    name:'张三',
    sex:'男'
}
Object.defineProperty(person,'age',{
    //value:18
    //enumerable:true,//控制属性是否可以枚举，默认值为false
    //writable:true,//控制属性是否可以被修改，默认值为false
    //configurable:true,//控制属性是否可以被删除，默认值是false
    
    //当有人读取person的age属性时，get函数（getter）就会被调用，且返回值就是age的值
    get(){
        console.log('有人读取age属性了')
        return number
    }
    
    //当有人修改person的age属性时，set函数（setter）就会被调用，且会收到修改的具体值
    set(value){
    console.log('有人修改了age属性，且值是',value)
    number = value
}
})
</script>
```

 ![](C:\Users\admin\Desktop\个人资料\笔记\img\Vue_2.png)

vue中的数据代理：通过vm对象来代理data对象中属性的操作（读/写）

vue中数据代理的好处：更加方便的操作data中的数据

基本原理：通过Object.defineProperty()把data对象中所有的属性和方法添加到vm上，为每一个添加到vm上的属性，都指定一个getter/setter，在getter/setter内部去操作data中对应的属性

#### 事件处理

```html
<body>
    <div id="root">
        <h2>欢迎来到{{name}}学习</h2>
        <button v-on:click="showInfo">点我提示信息</button>
        <button @click="showInfo1">点我提示信息1</button>
        <button @click="showInfo2(66,$event)">点我提示信息2</button>
    </div>

    <script>
        Vue.config.productionTip = false

        new Vue({
            el: '#root',
            data: {
                name: '尚硅谷a'
            },
            methods: {
                showInfo1(event) {
                    //console.log(this) //此处的this是vm
                    alert('同学你好')
                },
                showInfo2(number,event) {
                    //console.log(this) //此处的this是vm
                    alert('同学你好')
                }
            }
        })
    </script>
</body>
```

事件的基本使用：

1. 使用v-on:xxx或@xxx绑定事件，其中xxx是事件名
2. 事件的回调需要配置在methods对象中，最终会在vm上
3. methods中配置的函数，不要用箭头函数，否则this就不是vm了
4. methods中配置的函数，都是被vue所管理的函数，this的指向是vm或组件实例对象
5. @click="demo"和@click="demo($event)"效果一致，但后者可以传参

```html
<!--阻止默认事件-->
<a href="http://www.atguigu.com" @click.prevent="showInfo">点击</a>
<!--阻止事件冒泡-->
<div class='demo1' @click="showInfo">
    <button @click.stop="showInfo">点我提示信息</button>
</div>
<!--事件只触发一次-->
<button @click.once="showInfo">点我提示信息</button>
```

Vue中的事件修饰符：

1. prevent：阻止默认事件（常用）
2. stop：阻止事件冒泡（常用）
3. once：事件只触发一次（常用）
4. capture：使用事件的捕获模式
5. self：只有event.target是当前操作的元素时才触发事件
6. passive：事件的默认行为立即执行，无需等待事件回调执行完毕

```html
<input type="text" plaveholder="按下回车提示输入" @keyup.enter="showInfo">
```

Vue中常用的按键别名

1. 回车 => enter
2. 删除 => delete（捕获删除和退格键）
3. 退出 => esc
4. 空格 => space
5. 换行=> tab
6. 上 => up
7. 下 =>down
8. 左 => left
9. 右 => right

Vue未提供别名的按键，可以使用按键原始的key值去绑定，但注意要转为kebab-case（段横线命名）。例如CapsLock => @keyup.caps-lock

系统修饰键（用法特殊）：ctrl、alt、shift、meta

1. 配合keyup使用：按下修饰键的同时，再按下其他键，随后释放其他键，事件才触发
2. 配合keydown使用：正常触发事件

也可以使用keyCode去指定具体的按键（不推荐）

Vue.config.keyCodes.自定义键名 = 键码，可以去定制按键别名

小技巧

1. @keyup.crtl.y：按下ctrl+y触发
2. @click.stop.prevent：先阻止冒泡，再阻止默认事件

#### 计算属性

定义：要用的属性不存在，要通过已有属性计算得来

原理：底层借助了Object.defineproperty方法提供的getter和setter

get函数什么时候执行？

1. 初次读取时会执行一次
2. 当依赖的数据发生改变时会被再次调用

优势：与methods实现相比，内部有缓存机制，效率高，调试方便

备注：

1. 计算属性最终会出现在vm上，直接读取使用即可
2. 如果计算属性要被修改，那必须写set函数去响应修改，且set中要引起计算时依赖的数据发生改变

```html
<body>
    <div id="root">
        姓：<input type="text" v-model="firstName"><br> 名：
        <input type="text" v-model="lastName"><br> 全名：
        <span>{{fullName}}</span>
    </div>

    <script>
        Vue.config.productionTip = false

        new Vue({
            el: '#root',
            data: {
                firstName: '张',
                lastName: '三'
            },
            computed: {
                fullName: {
                    //get有什么作用？当有人读取fullName时，get就会被调用，且返回值作为fullName的值
                    //get什么时候调用？1.初次读取fullName时。2.所依赖的数据发生变化时
                    get() {
                        console.log('get被调用了')
                            //此处的this是vm
                        return this.firstName + '-' + this.lastName
                    },
                    set(value) {
                        console.log('set', value)
                        const arr = value.split('-')
                        this.firstName = arr[0]
                        this.lastName = arr[1]
                    }
                }
                //只读不改才能简写
                fullName:function(){
                     console.log('get被调用了')
                     //此处的this是vm
                     return this.firstName + '-' + this.lastName
                  }
            }
        })
    </script>
</body>
```

#### 监视属性

监视属性watch:

1. 当被监视的属性发生变化时，回调函数自动调用，进行相关操作
2. 监视的属性必须存在，才能进行监视
3. 监视的两种写法
   + new Vue时传入watch配置
   + 通过vm.$watch监视

```html
<body>
    <div id="root">
        <h2>今天天气很{{info}}</h2>
        <button @click="changeWeather">切换天气</button>
    </div>

    <script>
        Vue.config.productionTip = false

        const vm = new Vue({
            el: '#root',
            data: {
                isHot: true,
            },
            computed: {
                info() {
                    return this.isHot ? '炎热' : '凉爽'
                }
            },
            methods: {
                changeWeather() {
                    this.isHot = !this.isHot
                }
            },
            watch: {
                isHot: {
                    immediate: true, //初始化时让handler调用一下
                    //handler什么时候调用?当isHot发生改变时
                    handler(newValue, oldValue) {
                        console.log('isHot被修改了', newValue, oldValue)
                    }
                },
                //只有handler时可以简写
                isHot(newValue,oldValue) {
                    console.log('isHot被修改了', newValue, oldValue)
                }
            }
        })
        //第二种写法
        vm.$watch('isHot', {
            immediate: true, //初始化时让handler调用一下
            //handler什么时候调用?当isHot发生改变时
            handler(newValue, oldValue) {
                console.log('isHot被修改了', newValue, oldValue)
            }
        })
        //简写
        vm.$watch('isHot',function(newValue,oldValue){
            console.log('isHot被修改了', newValue, oldValue)
        })
    </script>
</body>
```

**深度监视：**

1. vue中的watch默认不监测对象内部值的改变（一层）
2. 配置deep:true可以监测对象内部值改变（多层）

备注：

1. vue自身可以检测对象内部值的改变，但vue提供的watch默认不可以
2. 使用watch时根据数据的具体结构，决定是否采用深度监视

```html
<body>
    <div id="root">
        <h2>今天天气很{{info}}</h2>
        <button @click="changeWeather">切换天气</button>
        <hr>
        <h3>a的值是：{{numbers.a}}</h3>
        <button @click="numbers.a++">点我让a+1</button>
    </div>

    <script>
        Vue.config.productionTip = false

        const vm = new Vue({
            el: '#root',
            data: {
                isHot: true,
                numbers: {
                    a: 1,
                    b: 2
                }
            },
            computed: {
                info() {
                    return this.isHot ? '炎热' : '凉爽'
                }
            },
            methods: {
                changeWeather() {
                    this.isHot = !this.isHot
                }
            },
            watch: {
                isHot: {
                    //immediate:true,
                    //handler什么时候调用?当isHot发生改变时
                    handler(newValue, oldValue) {
                        console.log('isHot被修改了', newValue, oldValue)
                    }
                },
                //监视多级结构中某个属性的变化
                'numbers.a': {
                    handler() {
                        console.log('a被改变了')
                    }
                },
                //监视多级结构中所有属性的变化
                numbers: {
                    deep: true,
                    handler() {
                        console.log('number改变了')
                    }
                }
            },

        })
        v
    </script>
</body>
```

**computed和watch之间的区别**

1. computed能完成的功能，watch都可以完成
2. watch能完成的功能，computed不一定能完成，例如：watch可以进行异步操作

两个重要的小原则：

1. 所有被Vue管理的函数，最好写成普通函数，这样this的指向才是vm或组件实例对象
2. 所有不被vue所管理的函数（定时器的回调函数、ajax的回调函数等），最好写成箭头函数，这样this的指向才是vm或组件实例对象

#### class与style绑定

class样式

写法 :class="xxx"  xxx可以是字符串、对象、数组

字符串写法适用于：类名不确定，要动态获取

对象写法适用于：要绑定多个样式，个数不确定，名字也不确定

数组写法适用于：要绑定多个样式，个数确定，名字也确定，但不确定用不用

style样式

:style="{fontSize: xxx}"其中xxx是动态值

:style="[a,b]"其中a、b是样式对象

```html
<body>
    <div id="root">
        <!-- 绑定class样式--字符串写法，适用于：样式的类名不确定，需要动态指定 -->
        <div class="basic" :class="mood" @click="changeMood">{{name}}</div><br>
        <!-- 绑定class样式--数组写法，适用于：要绑定的样式个数不确定，名字也不确定 -->
        <div class="basic" :class="arr">{{name}}</div>
        <!-- 绑定class样式--对象写法，适用于：要绑定的样式个数确定，名字也确定，但要动态决定用不用 -->
        <div class="basic" :class="classObj">{{name}}</div>

        <!-- 绑定style样式--对象写法 -->
        <div class="basic" :style="styleObj">{{name}}</div>
        <!-- 绑定style样式--数组写法 -->
        <div class="basic" :style="styleArr">{{name}}</div>
    </div>
    <script>
        Vue.config.productionTip = false

        new Vue({
            el: '#root',
            data: {
                name: '尚硅谷',
                mood: 'normal',
                arr: ['atguigu1', 'atguigu2', 'atguigu3'],
                classObj: {
                    atguigu1: false,
                    atguigu2: false,
                },
                styleObj: {
                    fontSize: '40px',
                    color: 'red',
                },
                styleObj2: {
                    backgroundColor: 'orange'
                },
                styleArr: [{
                    fontSize: '40px',
                    color: 'red',
                }, {
                    backgroundColor: 'orange'
                }]
            },
            methods: {
                changeMood() {
                    this.mood = 'happy'
                }
            }
        })
    </script>
</body>
```

#### 条件渲染

1. v-if

   写法：v-if="表达式"、v-else-if="表达式"、v-else="表达式"

   适用于：切换频率较低的场景

   特点：不展示的DOM元素直接被移除

   注意：v-if可以和v-else-if、v-else一起使用，但要求结构不能被打断

2. v-show

   写法：v-show="表达式"

   适用于：切换频率较高的场景

   特点：不展示的DOM元素未被移除，仅仅是使用样式隐藏掉

3. 备注：使用v-if时，元素可能无法取到，而使用v-show一定可以取到

```html
<body>
    <div id="root">
        <!-- 使用v-show做条件渲染 -->
        <h2 v-show="a">欢迎来到{{name}}</h2>

        <!-- 使用v-if做条件渲染 -->
        <h2 v-if="false">欢迎来到{{name}}</h2>

        <!-- v-else和v-else-if -->
        <div v-if="n === 1">Angular</div>
        <div v-else-if="n === 2">React</div>
        <div v-else-if="n === 3">Vue</div>
        <div v-else>哈哈</div>

        <!-- v-if与template的配合使用 -->
        <template v-if="n === 1">
        <h2>你好</h2>
        <h2>尚硅谷</h2>
        <h2>北京</h2>
        </template>
    </div>
    <script>
        Vue.config.produnctionTip = false

        const vm = new Vue({
            el: '#root',
            data: {
                name: '尚硅谷',
                a: false
            }
        })
    </script>
```

#### 列表渲染

v-for指令

1. 用于展示列表数据
2. 语法：v-for="(item,index) in xxx" :key="yyy"
3. 可遍历：数组、对象、字符串（用的少）、指定次数（用的很少）

```html
<body>
    <div id="root">
        <!-- 遍历数组 -->
        <ul>
            <li v-for="(p,index) in persons" :key="index">
                {{p.name}}-{{p.age}}
            </li>
        </ul>
        <!-- 遍历对象 -->
        <ul>
            <li v-for="(value,key) of car" :key="key">
                {{key}}-{{value}}
            </li>
        </ul>
        <!-- 遍历字符串 -->
        <ul>
            <li v-for="(char,index) of str" :key="index">
                {{char}}-{{index}}
            </li>
        </ul>
        <!-- 遍历指定次数 -->
        <ul>
            <li v-for="(number,index) of 5" :key="index">
                {{index}}-{{number}}
            </li>
        </ul>
    </div>

    <script>
        Vue.config.produnctionTip = false

        const vm = new Vue({
            el: '#root',
            data: {
                persons: [{
                    id: '001',
                    name: '张三',
                    age: 18
                }, {
                    id: '002',
                    name: '李四',
                    age: 19
                }, {
                    id: '003',
                    name: '王五',
                    age: 20
                }],
                car: {
                    name: '奥迪A8',
                    price: '70万',
                    color: '黑色'
                },
                str:'hello'
            }
        })
    </script>
</body>
```

#### key作用与原理

1. 虚拟DOM中key的作用：

   key是虚拟DOM对象的标识，当状态中的数据发生变化时，Vue会根据新数据生成新的虚拟DOM，随后Vue进行新虚拟DOM与旧虚拟DOM的差异比较，比较规则如下：

2. 对比规则：

   1. 旧虚拟DOM中找到了与新虚拟DOM相同的key:

      + 若虚拟DOM中内容没变，直接用之前的真实DOM
      + 若虚拟DOM中内容变了，则生成新的真实DOM，随后替换掉页面中之前的真实DOM

   2. 旧虚拟DOM中未找到与新虚拟DOM相同的key

      创建新的真实DOM，随后渲染到页面

3. 用index作为key可能会引发的问题：

   1. 若对数据进行：逆序添加、逆序删除等破坏顺序操作：会产生没有必要的真实DOM更新 ==> 界面效果没问题，但效率低
   2. 如果结构中还包含输入类的DOM：会产生错误DOM更新 ==>界面有问题

4. 开发中如何选择key?:

   1. 最好使用每条数据的唯一标识作为key,比如id、手机号、身份证号等唯一值
   2. 如果不存在对数据的逆序添加、逆序删除等破坏顺序操作，仅用于渲染列表用于展示，使用index作为key是没有问题的

![](C:\Users\admin\Desktop\个人资料\笔记\img\Vue_3.png)

![](C:\Users\admin\Desktop\个人资料\笔记\img\Vue_4.png)

#### Vue监视数据的原理

1. vue会监视data中所有层次的数据

2. 如何监测对象中的数据?

   通过setter实现监视，且要在new Vue时就传入要监测的数据

   + 对象中后追加的属性，Vue默认不做响应式处理
   + 如需给后添加的属性做响应式，请使用如下API
     + Vue.set(target, propertyName/index, value)或
     + vm.$set(target, propertyName/index, value)

3. 如何监测数组中的数据?

   通过包裹数组更新元素的方法实现，本质就是做了两件事：

   + 调用原生对应的方法对数组进行更新
   + 重新解析模板，进而更新页面

4. 在Vue修改数组中的某个元素一定要用如下方法：

   1. 使用这些API：push()、pop()、shift()、unshift()、splice()、sort()、reverse()
   2. Vue.set()或vm.$set()

5. 特别注意：Vue.set()和vm.$set()不能给vm或vm的根数据对象添加属性

```html
<body>
    <div id="root">
        <h1>学生信息</h1>

        <button @click="student.age++">年龄+1岁</button><br>
        <button @click="addSex">添加性别属性，默认值：男</button><br>
        <button @click="student.sex = '未知' ">修改性别</button><br>
        <button @click="addFriend">在列表首位添加一个朋友</button><br>
        <button @click="updateFirstFriendName">修改第一个朋友的名字为：张三</button><br>
        <button @click="addHobby">添加一个爱好</button><br>
        <button @click="updateHobby">修改第一个爱好为：开车</button><br>

        <h3>姓名：{{student.name}}</h3>
        <h3>年龄：{{student.age}}</h3>
        <h3 v-if="student.sex">性别：{{student.sex}}</h3>
        <h3>爱好：</h3>
        <ul>
            <li v-for="(h,index) in student.hobby" :key="index">
                {{h}}
            </li>
        </ul>
        <h3>朋友们：</h3>
        <ul>
            <li v-for="(f,index) in student.friends" :key="index">
                {{f.name}}--{{f.age}}
            </li>
        </ul>
    </div>

    <script>
        Vue.config.produnctionTip = false

        const vm = new Vue({
            el: '#root',
            data: {
                student: {
                    name: 'tom',
                    age: 18,
                    hobby: ['抽烟', '喝酒', '烫头'],
                    friends: [{
                        name: 'jerry',
                        age: 35
                    }, {
                        name: 'tony',
                        age: 36
                    }]
                }
            },
            methods: {
                addSex() {
                    //Vue.set(this.student, 'sex', '男')
                    this.$set(this.student, 'sex', '男')
                },
                addFriend() {
                    this.student.friends.unshift({
                        name: 'jack',
                        age: 70
                    })
                },
                updateFirstFriendName() {
                    this.student.friends[0].name = '张三'
                },
                addHobby() {
                    this.student.hobby.push('学习')
                },
                updateHobby() {
                    //this.friends.hobby[0].splice(0, 1, '开车')
                    Vue.set(this.student.hobby, 0, '开车')
                }
            }
        })
    </script>
</body>
```

#### 收集表单数据

若：< input type="text"/>，则v-model收集的是value值，用户输入的就是value值

若：< input type="radio"/>，v-model收集的是value值，且要给标签配置value值

若：< input type="checkbox"/>

1. 没有配置input的value属性，那么收集的就是checked（勾选 or 未勾选，是布尔值）
2. 配置input的value属性
   1. v-model的初始值是非数组，那么收集的就是checked（勾选 or 未勾选，是布尔值）
   2. v-model的初始值是数组，那么收集的就是value组成的数组

备注：v-model的三个修饰符：

+ lazy：失去焦点再收集数据
+ number：输入字符串转为有效的数字
+ trim：输入首尾空格过滤

```html
<body>

    <div id="root">
        <form @submit.prevent="demo">
            账号：<input type="text" v-model.trim="account"> <br> 
            密码：<input type="password" v-model="password"><br> 
            年龄：<input type="number" v-model.number="age"><br> 
            性别： 
            男<input type="radio" name="sex" v-model="sex" value="male"> 
            女<input type="radio" name="sex" v-model="sex" value="female"><br> 
            爱好： 
            学习<input type="checkbox" v-model="hobby" value="study"> 
            打游戏<input type="checkbox" v-model="hobby" value="game"> 
            吃饭<input type="checkbox" v-model="hobby" value="eat"><br> 
            所属校区
            <select v-model="city">
                <option value="">请选择校区</option>
                <option value="beijing">北京</option>
                <option value="shanghai">上海</option>
                <option value="shenzhen">深圳</option>
                <option value="wuhan">武汉</option>
            </select>
            <br> 其他信息：
            <textarea v-model.lazy="other"></textarea><br>
            <input type="checkbox" v-model="agree">阅读并接受<a href="http://www.atguigu.com">《用户协议》</a>
            <button>提交</button>
        </form>
    </div>

    <script>
        Vue.config.productionTip = false

        new Vue({
            el: '#root',
            data: {
                account: '',
                password: '',
                age: '18',
                sex: 'female',
                hobby: [],
                city: 'beijing',
                other: '',
                agree: ''
            },
            methods: {
                demo() {
                    console.log(JSON.stringify(this._data))
                }
            }
        })
    </script>
</body>
```

#### 过滤器

定义：对要显示的数据进行特定格式化后再显示（适用于一些简单逻辑的处理）

语法：

1. 注册过滤器：Vue.filter(name,callback)或new Vue({filters:{}})
2. 使用过滤器：{{xxx | 过滤器名}} 或 v-bind:属性 = "xxx | 过滤器名"

备注：

1. 过滤器也可以接收额外参数、多个过滤器也可以串联
2. 并没有改变原本的数据，是产生新的对应的数据

```html
<body>

    <div id="root">
        <h2>显示格式化后的时间</h2>
        <!-- 过滤器实现 -->
        <h3>现在是：{{time | timeFormatter}}</h3>
        <h3>现在是：{{time | timeFormatter('YYYY_MM_DD') | mySlice}}</h3>
        <h3 :x="msg | mySlice">尚硅谷</h3>
    </div>
    <script>
        Vue.config.productionTip = false
        Vue.filter('mySlice', function(value) {
            return value.slice(0, 4)
        })

        new Vue({
            el: '#root',
            data: {
                time: 1621561377603, //时间戳
                msg: '你好，尚硅谷'
            },
            //局部过滤器
            filters: {
                timeFormatter(value, str = 'YYYY年MM月DD日 HH:mm:ss') {
                    console.log('@', value)
                    return dayjs(value).format(str)
                },
                // mySlice(value){
                //     return value.slice(0,4)
                // }
            }
        })
    </script>
</body>
```

#### 内置指令

##### v-text

```html
<body>
    <div id="root">
        <div>{{name}}</div>
        <div v-text="name"></div>
    </div>
    <script>
        Vue.config.productionTip = false

        new Vue({
            el: '#root',
            data: {
                name: '尚硅谷'
            }
        })
    </script>
</body>
```

##### v-html

作用：向指定节点中渲染包含html结构的内容

与插值语法的区别：

1. v-html会替换掉节点中所有的内容，{{xx}}则不会
2. v-html可以识别html结构

严重注意：v-html有安全性问题

1. 在网站上动态渲染任意HTML是非常危险的，容易导致XSS攻击
2. 一定要在可信的内容上使用v-html，永不要用在用户提交的内容上

```html
<body>
    <div id="root">
        <div>{{name}}</div>
        <div v-html="str"></div>
    </div>
    <script>
        Vue.config.productionTip = false

        new Vue({
            el: '#root',
            data: {
                name: '尚硅谷',
                str:'<h3>你好</h3>'
            }
        })
    </script>
</body>
```

##### v-cloak(没有值)

1. 本质是一个特殊属性，Vue实例创建完毕并接管容器后，会删掉v-cloak属性
2. 使用css配合v-cloak可以解决网速慢时页面展示出{{xxx}}的问题

```html
<head>
<style>
        [v-cloak] {
            display: none;
        }
    </style>
</head>

<body>
    <div id="root">
        <h2 v-cloak>{{name}}</h2>
    </div>
    <script src="http://localhost:8000/resource/5s/vue.js"></script>
    <script>
        Vue.config.productionTip = false

        new Vue({
            el: '#root',
            data: {
                name: '尚硅谷',
            }
        })
    </script>
</body>
```

##### v-once

1. v-once所在节点在初次动态渲染后，就视为静态内容了
2. 以后数据的改变不会引起v-once所在结构的更新，可以用于性能优化

```html
<body>
    <div id="root">
        <h2 v-once>初始化的n值是：{{n}}</h2>
        <h2>当前的n值是：{{n}}</h2>
        <button @click="n++">点我n+1</button>
    </div>
    <script>
        Vue.config.productionTip = false

        new Vue({
            el: '#root',
            data: {
                n: 1,
            }
        })
    </script>
</body>
```

##### v-pre

1. 跳过其所在节点的编译过程
2. 可利用它跳过：没有使用指令语法、没有使用插值语法的节点，会加快编译

```html
<body>
    <div id="root">
        <h2 v-pre>Vue其实很简单</h2>
        <h2 v-pre>当前的n值是:{{n}}</h2>
        <button v-pre @click="n++">点我n+1</button>
    </div>
    <script>
        Vue.config.productionTip = false

        new Vue({
            el: '#root',
            data: {
                n: 1,
            }
        })
    </script>
</body>
```

#### 自定义指令

定义语法：

1. 局部指令

   ```js
   new Vue({
   	directives:{指令名：配置对象}
   })
   或
   new Vue({
       directives(){}
   })
   ```

2. 全局指令

   ```js
   Vue.derective(指令名，配置对象) 或 Vue.derective(指令名，回调函数)
   ```

配置对象中常用的三个回调：

1. bind：指令与元素成功绑定时调用
2. inserted：指令所在元素被插入页面时调用
3. updated：指令所在模板结构被重新解析时调用

备注：

1. 指令定义时不加v-，但使用时要加v-
2. 指令名如果是多个单词，要使用kebab-case命名方式，不要用camelCase命名

```html
<body>

    <!-- 需求1：定义一个v-big指令，和v-text功能类似，但会把绑定的数值放大10倍
         需求2：定义一个v-fbind指令，和v-bind功能类似，但可以让其所绑定的input元素默认获取焦点
    -->
    <div id="root">
        <h2>当前的n值是:<span v-text="n"></span></h2>
        <h2>放大10倍后的的n值是:<span v-big-number="n"></span></h2>
        <button @click="n++">点我n+1</button>
        <hr>
        <input type="text" v-fbind:value="n">
    </div>
    <script>
        Vue.config.productionTip = false
            //全局指令
        Vue.directive('fbind', {
            //指令与元素成功绑定时（一上来）
            bind(element, binding) {
                element.value = binding.value
            },
            //指令所在元素被插入页面时
            inserted(element, binding) {
                element.focus()
            },
            //指令所在模板被重新解析时
            update(element, binding) {
                element.value = binding.value
            }
        })

        new Vue({
            el: '#root',
            data: {
                n: 1,
            },
            //局部指令
            directives: {
                //big函数何时会被调用？1.指令与元素成功绑定时（一上来）
                //2.指令所在模板被重新解析时
                'big-number' (element, binding) {
                    console.log(this) //注意此处的this是window
                    element.innerText = binding.value * 10
                },
                fbind: {
                    //指令与元素成功绑定时（一上来）
                    bind(element, binding) {
                        element.value = binding.value
                    },
                    //指令所在元素被插入页面时
                    inserted(element, binding) {
                        element.focus()
                    },
                    //指令所在模板被重新解析时
                    update(element, binding) {
                        element.value = binding.value
                    }
                }
            }
        })
    </script>
</body>
```

#### 生命周期

https://cn.vuejs.org/v2/guide/instance.html#%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F%E5%9B%BE%E7%A4%BA

生命周期：

1. 又名：生命周期回调函数、生命周期函数、生命周期钩子函数
2. 是什么：Vue在关键时刻帮我们调用的一些特殊名称的函数
3. 生命周期函数的名字不可更改，但函数的具体内容是程序员根据需求编写的
4. 生命周期函数中的this指向的是vm或组件实例对象

常用的生命周期钩子：

1. mounted：发送ajax请求、启动定时器、绑定自定义事件、订阅消息等初始化操作
2. beforeDestroy：清除定时器、解绑自定义事件、取消订阅消息等收尾工作

关于销毁Vue实例

1. 销毁后借助Vue开发者工具看不到任何信息
2. 销毁后自定义事件会失效，但原生DOM事件依然有效
3. 一般不会再beforeDestroy操作数据，因为即便操作数据，也不会再触发更新流程了

```html
<body>

    <div id="root">
        <h2 v-if="a">你好啊</h2>
        <h2 :style="{opacity}">欢迎学习Vue</h2>
    </div>
    <script>
        Vue.config.productionTip = false

        const vm = new Vue({
            el: '#root',
            data: {
                a:false,
                opacity: 1
            },
            methods: {

            },
            //Vue完成模板的解析并把初识的真实DOM元素放入页面后（挂载完毕）调用mounted
            mounted() {
                setInterval(() => {
                    this.opacity -= 0.01
                    if (this.opacity <= 0) this.opacity = 1
                }, 16)
            }
        })
    </script>
</body>
```

![](C:\Users\admin\Desktop\个人资料\笔记\img\Vue_5.png)

![](C:\Users\admin\Desktop\个人资料\笔记\img\Vue_6.png)

**vm的一生（vm的生命周期）**

​                将要创建 ===> 调用beforeCreate函数

​                创建完毕 ===> 调用created函数

​                将要挂载 ===> 调用beforeMount函数

（重要）挂载完毕 ===> 调用mounted函数            ============>重要的钩子

​                将要更新 ===> 调用beforeUpdate函数

​                更新完毕 ===> 调用updated函数

（重要）将要销毁 ===> 调用beforeDestroy函数    ============>重要的钩子

​                销毁完毕 ===> 调用destroyed函数

```html
<body>

    <div id="root">
        <h2 :style="{opacity}">欢迎学习Vue</h2>
        <button @click="opacity = 1">透明度设置为1</button>
        <button @click="stop">点我停止变换</button>
    </div>
    <script>
        Vue.config.productionTip = false

        const vm = new Vue({
            el: '#root',
            data: {
                opacity: 1
            },
            methods: {
                stop() {
                    // clearInterval(this.timer)
                    this.$destroy()
                }
            },
            //Vue完成模板的解析并把初识的真实DOM元素放入页面后（挂载完毕）调用mounted
            mounted() {
                this.timer = setInterval(() => {
                    this.opacity -= 0.01
                    if (this.opacity <= 0) this.opacity = 1
                }, 16)
            },
            beforeDestroy() {
                clearInterval(this.timer)
            }
        })
    </script>
</body>
```

### Vue组件化编程

#### 模块与组件、模块化与组件化

**模块**

1. 理解：向外提供特定功能的js程序，一般就是一个js文件
2. 为什么：js文件很多很复杂
3. 作用：复用js，简化js的编写，提高js运行效率

**组件**

1. 理解：用来实现局部功能效果的代码集合（html/css/js/image...）
2. 为什么：一个界面的功能很复杂
3. 作用：复用代码，简化项目编码，提高运行效率

**模块化**

当应用中的js都是以模块来编写的，那这个应用就是一个模块化的应用

**组件化**

当应用中的功能都是多组件的方式来编写的，那这个应用就是一个组件化的应用

#### 非单文件组件

一个文件中包含有n个组件

**Vue中使用组件的三大步骤：**

1. 定义组件（创建组件）
2. 注册组件
3. 使用组件（写组件标签）

**如何定义一个组件?**

使用Vue.extend(options)创建，其中options和new Vue(options)时传入的那个options几乎一样，但也有点区别

区别如下：

1. el不要写，为什么？——最终所有的组件都要经过一个vm的管理，由vm中的el决定服务哪个容器
2. data必须写成函数，为什么？——避免组件被复用时，数据存在引用关系

备注：使用template可以配置组件结构

**如何注册组件？**

1. 局部注册：靠new Vue的时候传入components选项
2. 全局注册：靠Vue.component('组件名',组件)

**编写组件标签**

< school>< /school>

```html
<body>

    <div id="root">
        <!-- 第三步：编写组件标签 -->
        <school></school>
        <hr>
        <!-- 第三步：编写组件标签 -->
        <student></student>
    </div>

    <div id="root2">
        <hello></hello>
    </div>
    <script>
        Vue.config.productionTip = false

        //第一步：创建school组件
        const school = Vue.extend({
            //el: '#root', //组件定义时，一定不要写el配置项，因为最终所有的组件都要被一个vm管理，由vm决定服务于哪个容器
            template: `
            <div>
                <h2>学校名称：{{schoolName}}</h2>
                <h2>学校地址：{{address}}</h2>
                <button @click="showName">点我提示学校名</button>
            </div>
            `,
            data() {
                return {
                    schoolName: '尚硅谷',
                    address: '北京昌平',
                }
            },
            methods: {
                showName() {
                    alert(this.schoolName)
                }
            }
        })


        //第一步：创建student组件
        const student = Vue.extend({
            template: `
            <div>
                <h2>学生姓名：{{studentName}}</h2>
                <h2>学生年龄：{{age}}</h2>
            </div>
            `,
            data() {
                return {
                    studentName: '张三',
                    age: 18
                }
            },
        })

        //第一步：创建hello组件
        const hello = Vue.extend({
            template: `
            <div>
                <h2>你好啊{{name}}</h2>
            </div>
            `,
            data() {
                return {
                    name: 'Tom'
                }
            },
        })

        //第二步：全局注册组件
        Vue.componet('hello',hello)

        new Vue({
            el: '#root',
            //第二步：注册组件（局部注册）
            components: {
                school,
                student
            }
        })

        new Vue({
            el: '#root2',
        })
    </script>
</body>
```

**几个注意点：**

1. 关于组件名：

   一个单词组成：

   ​	第一种写法（首字母小写）：school

   ​	第二种写法（首字母大写）：School

   多个单词组成：

   ​	第一种写法（kebab-case命名）：my-school

   ​	第二种写法（CamelCase命名）：MySchool（需要Vue脚手架支持）

   备注：

   	1. 组件名尽可能回避HTML中已有的元素名称，例如：h2、H2都不行
   	1. 可以使用name配置项指定组件再开发者工具中呈现的名字

2. 关于组件标签：

   第一种写法：< school>< /school>

   第二种写法：< school/>

   备注：不用使用脚手架时，< school/>会导致后续组件不能渲染

3. 一个简写方式：

   const school = Vue.extend(options)可简写为：const school = options

**组件的嵌套**

```html
<body>

    <div id="root">

    </div>

    <script>
        Vue.config.productionTip = false

        //定义student组件
        const student = Vue.extend({
            name: 'student',
            template: `
            <div>
                <h2>学生姓名：{{name}}</h2>
                <h2>学生年龄：{{age}}</h2>
            </div>
            `,
            data() {
                return {
                    name: '尚硅谷',
                    age: '18',
                }
            }
        })

        //定义school组件
        const school = Vue.extend({
            name: 'school',
            template: `
            <div>
                <h2>学校名称：{{name}}</h2>
                <h2>学校地址：{{address}}</h2>
                <student></student>
            </div>
            `,
            data() {
                return {
                    name: '尚硅谷',
                    address: '北京',
                }
            },
            //注册组件（局部）
            components: {
                student
            }
        })

        //定义hello组件
        const hello = Vue.extend({
            name: 'hello',
            template: `<h2>{{msg}}</h2>`,
            data() {
                return {
                    msg: '欢迎来到尚硅谷学习',
                }
            }
        })

        //定义app组件
        const app = Vue.extend({
            template: `
                <div>
                    <hello></hello>
                    <school></school>
                </div>
            `,
            components: {
                school,
                hello
            }
        })

        //创建vm
        new Vue({
            template: `<app></app>`,
            el: '#root',
            //注册组件（局部）
            components: {
                app
            }
        })
    </script>
</body>
```

**VueComponent**

1. school组件本质是一个名为VueComponent的构造函数，且不是程序员定义的，是Vue.extend生成的

2. 我们只需要写< school>< /school>，vue解析时会帮我们创建school组件的实例对象。即Vue帮我们执行的：new VueComponent(options)

3. 特别注意：每次调用Vue.extend，返回的都是一个全新的VueComponent

4. 关于this指向：

   1. 组件配置中：

      data函数、methods中的函数、watch中的函数、computed中的函数，他们的this均是VueComponent实例对象

   2. new Vue()配置中：

      data函数、methods中的函数、watch中的函数、computed中的函数，他们的this均是Vue实例对象

5. VueComponent的实例对象，以后简称vc（也可称之为：组件实例对象）

**一个重要的内置关系**

1. 一个重要的内置关系：VueComponent.prototype.__ proto__ === Vue.prototype

2. 为什么要有这个关系？让组件实例对象（vc）可以访问到Vue原型上的属性、方法

![](C:\Users\admin\Desktop\个人资料\笔记\img\Vue_7.png)

#### 单文件组件

一个文件中只包含有一个组件

**School.vue**

```vue
<template>
    <!-- 组件的结构 -->
    <div class="demo">
        <h2>学校名称：{{schoolName}}</h2>
        <h2>学校地址：{{address}}</h2>
        <button @click="showName">点我提示学校名</button>
    </div>
</template>

<script>
    // 组件交互相关的代码（数据、方法等等）
    export default{
        name:'School',
        data() {
            return {
                schoolName: '尚硅谷',
                address: '北京',
            }
        },
        methods: {
            showName() {
                alert(this.schoolName)
            }
        }
    }
</script>

<style>
/* 组件的样式 */
.demo{
    background-color: orange;
}
</style>
```

**Studnet.vue**

```vue
<template>
    <!-- 组件的结构 -->
    <div>
        <h2>学生姓名：{{name}}</h2>
        <h2>学生年龄：{{age}}</h2>
    </div>
</template>

<script>
    
    export default {
        name:'Student',
        data() {
            return {
                name: '张三',
                age: '18',
            }
        },
        methods: {
            showName() {
                alert(this.name)
            }
        }
    }
</script>
```

**APP.vue**

```vue
<template>
  <div>
      <School></School>
      <Student></Student>
  </div>
</template>

<script>
// 引入组件
import School from './School.vue'
import Student from './Studnet.vue'

export default {
    name:'App',
    components:{
        School,
        Student
    }
}
</script>
```

**index.html**

```html
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>

<body>
    <div id="root"></div>
    <script type="text/javascript" src="../js/vue.js"></script>
    <script type="text/javascript" src="./main.js"></script>
</body>

</html>
```

**mian.js**

```js
import App from './App.vue'

new Vue({
    el: '#root',
    template: `<App></App>`,
    components: {
        App
    }
})
```

### 使用vue脚手架

第一步（仅第一次执行）：全局安装@vue/cli

​	npm install -g @vue/cli

第二步：切换到你要创建项目的目录，然后使用命令创建项目

​	vue create xxxx

第三步：启动项目

​	npm run serve

备注：

1. 如出现下载缓慢请配置淘宝镜像：

   npm config set registry https://registry.npm.taobao.org

2. Vue脚手架隐藏了所有webpack相关的配置，若想查看具体的webpack配置，请执行：vue inspect > output.js

修改默认配置：vue.config.js

#### ref属性

1. 被用来给元素或子组件注册引用信息（id的替代者）

2. 应用在html标签上获取的是真实DOM元素，应用在组件标签上是组件实例对象（vc）

3. 使用方式：

   打标识：< h1 ref="xxx">...< /h1> 或 < School ref="xxx">< /School>

   获取：this.$refs.xx

```vue
<template>
<div>
    <h1 v-text="msg" ref="title"></h1>
    <button ref="btn" @click="showDOM">点我输出上方的DOM元素</button>
    <School ref="sch"></School>
</div>
</template>

<script>
import School from './components/School.vue'
export default {
    name:'App',
    data() {
        return{
            msg:'欢迎学习vue'
        }
    },
    components:{School},
    methods:{
        showDOM(){
            console.log(this.$refs.title)  //真实DOM元素
            console.log(this.$refs.btn) //真实DOM元素
            console.log(this.$refs.sch) //School组件的实例对象（vc）
        }
    }
}
</script>
```

#### props配置

功能：让组件接收外部传过来的数据

1. 传递数据

   < Student name="xxx"/>

2. 接收数据

   + 第一种方式（只接收）

     props:['name']

   + 第二种方式（限制类型）

     props:{

     ​	name:Number

     }

   + 第三种方式（限制类型、限制必要性、指定默认值）

     props:{

     ​	name:{

     ​		type:String,  //类型

     ​		required:true,  //必要性

     ​		default:'老王'   //默认值

     ​	}

     }

备注：props是只读的，Vue底层会监测你对props的修改，如果进行了修改，就会发出警			告，若业务需求确实需要修改，那么请复制props的内容到data中一份，然后去修			改data中的数据

**Student.vue**

```vue
<template>
  <div>
      <h1>{{msg}}</h1>
      <h2>学生姓名：{{name}}</h2>
      <h2>学生性别：{{sex}}</h2>
      <!-- <h2>学生年龄：{{age+1}}</h2> -->
      <h2>学生年龄：{{myAge+1}}</h2>
      <button @click="updateAge">尝试修改收到的年龄</button>
  </div>
</template>

<script>
export default {
    name:'Student',
    data(){
        return {
            msg:'我是一个尚硅谷的学生',
            myAge:this.age
        }
    },
    methods:{
        updateAge(){
            this.myAge++
        }
    },
    //简单接收
    props:['name','age','sex']

    //接收的同时对数据进行类型限制
    // props:{
    //     name:String,
    //     age:Number,
    //     sex:String
    // }

    //接收的同时对数据：进行类型限制+默认值指定+必要性的限制
    // props:{
    //     name:{
    //         type:String,
    //         required:true //名字是必要的
    //     },
    //     age:{
    //         type:Number,
    //         default:99 //默认值
    //     },
    //     sex:{
    //         type:String,
    //         required:true
    //     }
    // }
}
</script>
```

**App.vue**

```vue
<template>
<div>
    <Student name="李四" sex="女" :age="18"></Student>
</div>
</template>

<script>
import Student from './components/Student.vue'
export default {
    name:'App',
    components:{Student},
}
</script>
```

#### mixin混入

功能：可以把多个组件共用的配置提取成一个混入对象

使用方式：

+ 第一步定义混合，例如

  {

  ​	data(){...},

  ​	methods:{...}

  ​	...

  }

+ 第二部使用混合，例如

  + 全局混入：Vue。mixin(xxx)
  + 局部混入：mixins:['xxx']

**School.vue**

```vue
<template>
  <div>
      <h2 @click="showName">学校名称：{{name}}</h2>
      <h2>学生地址：{{address}}</h2>
  </div>
</template>

<script>
//引入一个hunhe
import {hunhe,hunhe2} from '../mixin'
export default {
    name:'School',
    data(){
        return {
            name:'尚硅谷',
            address:'北京'
        }
    },
    mixins:[hunhe,hunhe2]
}
</script>
```

**Student.vue**

```vue
<template>
  <div>
      <h2 @click="showName">学生姓名：{{name}}</h2>
      <h2>学生性别：{{sex}}</h2>
  </div>
</template>

<script>

import {hunhe,hunhe2} from '../mixin'
export default {
    name:'Student',
    data(){
        return {
            name:'张三',
            sex:'男'
        }
    },
    mixins:[hunhe,hunhe2]
}
</script>
```

**mixin.js**

```vue
export const hunhe = {
    methods: {
        showName() {
            alert(this.name)
        }
    },
    mounted() {
        console.log('你好啊')
    }
}

export const hunhe2 = {
    data() {
        return {
            x: 100,
            y: 200
        }
    }
}
```

#### 插件

功能：用于增强vue

本质：包含install方法的一个对象，install的第一个参数是vue，第二个以后的参数是插件使用者传递的数据

定义插件：

```js
对象.install = function (Vue,options){
    //1.添加全局过滤器
    Vue.filter(...)
    //2.添加全局指令
    Vue.directive(...)  
    //3.配置全局混入
    Vue.mixin(...)
    //4.添加实例方法
    Vue.prototype.$myMethod = function() {...}
    Vue.prototype.$myProperty = xxx
}
```

使用插件：Vue.use()

**plugins.js**

```js
export default {
    install(Vue) {
        //全局过滤器
        Vue.filter('mySlice',function(value){
            
        })
        //定义全局指令
        Vue.directive('fbind',{
            
        })
        //定义混入
        Vue.mixin({
            
        })
        //给Vue原型上添加一个方法（vm和vc就都能使用了）
        Vue.prototype.hello = () => {alert('你好啊')}
    }
}
```

**main.js**

```js
import { createApp } from 'vue'
import App from './App.vue'

//引入插件
import plugins from './plugins'

createApp(App).use(plugins).mount('#app') //使用插件
```

#### scoped样式

作用：让样式在局部生效，防止冲突

写法：< style scoped>

#### 组件自定义事件

1. 一种组件键通信的方式，适用于：子组件 ===> 父组件

2. 使用场景：A是父组件，B是子组件，B想给A传数据，那么就要在A中给B绑定自定义事件（事件的回调在A中）

3. 绑定自定义事件

   1. 第一种方式，在父组件中：`<Demo @atguigu="test"/>` 或 `<Demo v-on:atguigu="test"/>`

   2. 第二种方式，在父组件中：

      ```vue
      <Demo ref="demo"/>
      ...
      mounted(){
      	this.$refs.xxx.$on('atguigu',this.test)
      }
      ```

   3. 若想让自定义事件只触发一次，可以使用`once`修饰符，或`$once`方法

4. 触发自定义事件：`this.$emit('atguigu',数据)`

5. 解绑自定义事件：`this.$off('atguigu')`

6. 组件上也可以绑定原生DOM事件，需要使用`native`修饰符

7. 注意：通过`this.$refs.xxx.$on('atguigu',回调)`绑定自定义事件时，回调要么配置在methods中，要么用箭头函数，否则this指向会出问题！

**App.vue**

```vue
<template>
<div class="app">
    <h1>{{msg}},学生姓名是：{{studentName}}</h1>
    <!-- 通过父组件给子组件传递函数类型的props实现：子给父传递数据 -->
    <School :getSchoolName="getSchoolName"/>
    <!-- 通过父组件给子组件绑定一个自定义事件实现：子给父传递数据(第一种写法：使用@或v-on) -->
    <!-- <Student v-on:atguigu="getStudentName"/> -->
    <!-- <Student @atguigu="getStudentName" @demo="m1"/> -->

    <!-- 通过父组件给子组件绑定一个自定义事件实现：子给父传递数据(第二种写法：使用ref) -->   
    <Student ref="student" @click.native="show"/>
</div>
</template>

<script>
import School from './components/School.vue'
import Student from './components/Student.vue'

export default {
    name:'App',
    components:{School,Student},
    data(){
        return{
            msg:'你好啊',
            studentName:''
        }
    },
    methods:{
        getSchoolName(name){
            console.log('App收到了学校名:',name)
        },
        getStudentName(name,...params){//接收多个参数
            console.log('App收到了学生名:',name,params)
            this.studentName = name
        },
        m1(){
            console.log('demo事件被触发了')
        },
        show(){
            alert(123)
        }
    },
    mounted(){
        this.$refs.student.$on('atguigu',this.getStudentName)//绑定自定义事件
        // this.$refs.student.$once('atguigu',this.getStudentName)//绑定自定义事件（一次性）
        // this.$refs.student.$on('atguigu',(name,...params)=>{
        //     console.log('App收到了学生名:',name,params)
        //     this.studentName = name
        // })
    }
}
</script>

<style scoped>
    .app{
        background-color: gray;
        padding: 5px;
    }
</style>
```

**Student.vue**

```vue
<template>
  <div class="student">
      <h2>学生姓名：{{name}}</h2>
      <h2>学生性别：{{sex}}</h2>
      <h2>当前求和为：{{number}}</h2>
      <button @click="add">点我number++</button>
      <button @click="sendStudentName">把学生名给App</button>
      <button @click="unbind">解绑atguigu事件</button>
      <button @click="death">销毁当前Student组件的实例（vc）</button>
  </div>
</template>

<script>

export default {
    name:'Student',
    data(){
        return {
            name:'张三',
            sex:'男',
            number:0
        }
    },
    methods:{
        add(){
            console.log('add回调被调用了')
            this.number++
        },
        sendStudentName(){
            //触发Student组件实例身上的atguigu事件
            this.$emit('atguigu',this.name,666,888,900)
            this.$emit('demo')
        },
        unbind(){
            // this.$off('atguigu')//解绑一个自定义事件
            // this.$off(['atguigu','demo']) //解 绑多个自定义事件
            this.$off()//解绑所有自定义事件
        },
        death(){
            this.$destroy()//销毁了当前的Student组件实例，销毁后所有student实例的自定义事件全都不生效
        },
        
    }
}
</script>

<style scoped>
    .student{
        background-color: pink;
         padding: 5px;
         margin-top: 30px;
    }
</style>
```

**School.vue**

```vue
<template>
  <div class="school">
      <h2>学校名称：{{name}}</h2>
      <h2>学校地址：{{address}}</h2>
      <button @click="sendSchoolName">把学校名给App</button>
  </div>
</template>

<script>
export default {
    name:'School',
    props:['getSchoolName'],
    data(){
        return {
            name:'尚硅谷',
            address:'北京'
        }
    },
    methods:{
        sendSchoolName(){
            this.getSchoolName(this.name)
        }
    }
}
</script>

<style scoped>
    .school{
        background-color: skyblue;
        padding: 5px;
    }
</style>
```

#### 全局事件总线（GlobalEventBus）

1. 一种组件间通信方式，适用于任意组件间通信

2. 安装全局事件总线

   ```js
   new Vue({
       ...
       beforeCreate() {
           Vue.prototype.$bus = this //安装全局事件总线
       },
       ...
   })
   ```

3. 使用事件总线：

   1. 接收数据：A组件想接收数据，则在A组件中给$bus绑定自定义事件，事件的回调留在A组件本身

      ```js
      methods(){
          demo(data){...}
      }
      ...
      mounted(){
          this.$bus.$on('xxxx',this.demo)
      }
      ```

   2. 提供数据：`this.$bus.$emit('xxxx',数据)`

4. 最好在beforeDestroy钩子中，用$off去解绑当前组件所用到的事件

![](C:\Users\admin\Desktop\个人资料\笔记\img\Vue_8.png)

**Student.vue**

```vue
<template>
  <div class="student">
      <h2>学生姓名：{{name}}</h2>
      <h2>学生性别：{{sex}}</h2>
      <button @click="sendStudentName">把学生名给School组件</button>
  </div>
</template>

<script>

export default {
    name:'Student',
    data(){
        return {
            name:'张三',
            sex:'男',
        }
    },
    mounted(){

    },
    methods:{
        sendStudentName(){
            this.$bus.$emit('hello',this.name)
        }
    }
}
</script>

<style scoped>
    .student{
        background-color: pink;
         padding: 5px;
         margin-top: 30px;
    }
</style>
```

**School.vue**

```vue
<template>
  <div class="school">
      <h2>学校名称：{{name}}</h2>
      <h2>学校地址：{{address}}</h2>
  </div>
</template>

<script>
export default {
    name:'School',
    data(){
        return {
            name:'尚硅谷',
            address:'北京'
        }
    },
    mounted(){
        this.$bus.$on('hello',(data)=>{
            console.log('我是School组件，收到了数据',data)
        })
    },
    beforeDestroy(){
        this.$bus.$off('hello')
    }
}
</script>

<style scoped>
    .school{
        background-color: skyblue;
        padding: 5px;
    }
</style>
```

**main.js**

```js
import Vue from 'vue'
import App from './App.vue'

Vue.config.productionTip = false

new Vue({
    el: '#app',
    render: h => h(App),
    beforeCreate() {
        Vue.prototype.$bus = this //安装全局事件总线
    }
})
```

#### 消息订阅与发布：pubsub

1. 一种组件间通信方式，适用于任意组件间通信

2. 使用步骤：

   1. 安装pubsub-js：`npm i pubsub-js`

   2. 引入：`import pubsub from 'pubsub-js'`

   3. 接收数据：A组件想接收数据，则在A组件中订阅消息，订阅的回调留在A组件自身

      ```js
      methods(){
          demo(data){...}
      }
      ...
      mounted(){
          this.pid = pubsub.subscribe('xxx',this.demo) //订阅消息
      }
      ```

   4. 提供数据：`pubsub.publish('xxx',数据)`

   5. 最好在beforeDestroy钩子中，用`pubsub.unsubscribe(pid)`去取消订阅

**School.vue**

```vue
<template>
  <div class="school">
      <h2>学校名称：{{name}}</h2>
      <h2>学校地址：{{address}}</h2>
  </div>
</template>

<script>
import pubsub from 'pubsub-js'
export default {
    name:'School',
    data(){
        return {
            name:'尚硅谷',
            address:'北京'
        }
    },
    methods:{
        demo(msgName,data){
            console.log('有人发布了hello消息，hello消息的回调执行了',msgName,data)
        }
    },
    mounted(){
        // this.$bus.$on('hello',(data)=>{
        //     console.log('我是School组件，收到了数据',data)
        // })
        this.pubId = pubsub.subscribe('hello',this.demo)
    },
    beforeDestroy(){
        // this.$bus.$off('hello')
        pubsub.unsubscribe(this.pubId)
    }
}
</script>

<style scoped>
    .school{
        background-color: skyblue;
        padding: 5px;
    }
</style>
```

**Student.vue**

```vue
<template>
  <div class="student">
      <h2>学生姓名：{{name}}</h2>
      <h2>学生性别：{{sex}}</h2>
      <button @click="sendStudentName">把学生名给School组件</button>
  </div>
</template>

<script>
import pubsub from 'pubsub-js'
export default {
    name:'Student',
    data(){
        return {
            name:'张三',
            sex:'男',
        }
    },
    mounted(){

    },
    methods:{
        sendStudentName(){
            // this.$bus.$emit('hello',this.name)
            pubsub.publish('hello',666)
        }
    }
}
</script>

<style scoped>
    .student{
        background-color: pink;
         padding: 5px;
         margin-top: 30px;
    }
</style>
```

#### nextTick

1. 语法：`this.$nextTick(回调函数)`
2. 作用：在下一次DOM更新结束后执行其指定的回调
3. 什么时候用：当改变数据后，要基于更新后的新DOM进行某些操作时，要在nextTick所指定的回调函数中执行

#### 过度与动画

1. 作用：在插入、更新或移除DOM元素时，在合适的时候给元素添加样式类名

2. 图示：

   ![](C:\Users\admin\Desktop\个人资料\笔记\img\Vue_9.png)

3. 写法：

   1. 准备好样式：

      + 元素进入的样式：

        1. v-enter：进入的起点
        2. v-enter-active：进入过程中
        3. v-enter-to：进入的终点

      + 元素离开的样式：

        1. v-leave：离开的起点

        2. v-leave-active：离开过程中

        3. v-leave-to：离开的终点

   2. 使用`<transition>`包裹要过度的元素，并配置name属性

      ```vue
      <transition name="hello">
      	<h1 v-show="isShow">你好啊</h1>
      </transition>
      ```

   3. 备注：若有多个元素需要过度，则需要使用`<transition-group>`，且每个元素都要指定key值

4. 

**Test.vue-动画**

```vue
<template>
  <div>
      <button @click="isShow = !isShow">显示/隐藏</button>
      <transition name="hello" appear>
        <h1 v-show="isShow">你好啊！</h1>
      </transition>
  </div>
</template>

<script>
export default {
    name:'Test',
    data(){
        return{
            isShow:true,
        }
    }
}
</script>

<style scoped>
    h1 {
        background-color: orange;
    }

/* v-enter-active */
    .hello-enter-active{
        animation: atguigu 1s;
    }

/* v-leave-active */
    .hello-leave-active{
        animation: atguigu 1s reverse;
    }
    @keyframes atguigu {
        from{
            transform: translateX(-100%);
        }
        to{
            transform: translateX(0%);
        }
    }
</style>
```

**Test2.vue-过度**

```vue
<template>
  <div>
      <button @click="isShow = !isShow">显示/隐藏</button>
      <transition-group name="hello" appear>
        <h1 v-show="isShow" key="1">你好啊！</h1>
        <h1 v-show="isShow" key="2">尚硅谷</h1>
      </transition-group>
  </div>
</template>

<script>
export default {
    name:'Test2',
    data(){
        return{
            isShow:true,
        }
    }
}
</script>

<style scoped>
    h1 {
        background-color: orange;   
    }

    /* 进入的起点、离开的终点 */
    .hello-enter,.hello-leave-to{
        transform:translateX(-100%);
    }
    .hello-enter-active,.hello-leave-active{
        transition: 0.5s linear;
    }
    /* 进入的终点、离开的起点 */
    .hello-enter-to,.hello-leave{
        transform:translateX(0);
    }
</style>
```

**Test3.vue-第三方库**

```vue
<template>
  <div>
      <button @click="isShow = !isShow">显示/隐藏</button>
      <transition-group 
        appear
        name="animate__animated animate__bounce" 
        enter-active-class="animate__swing"
        leave-active-class="animate__backInUp"
      >
        <h1 v-show="!isShow" key="1">你好啊！</h1>
        <h1 v-show="isShow" key="2">尚硅谷</h1>
      </transition-group>
  </div>
</template>

<script>
import 'animate.css'
export default {
    name:'Test3',
    data(){
        return{
            isShow:true,
        }
    }
}
</script>

<style scoped>
    h1 {
        background-color: orange;   
    }
</style>
```

#### 配置代理

**方法一**

在vue.config.js中添加如下配置

```js
devServer:{
  proxy:'http://localhost:5000'
}
```

说明：

1. 优点：配置简单，请求资源时直接发给前端（8080）即可
2. 缺点：不能配置多个代理，不能灵活的控制请求是否走代理
3. 工作方式：若按照上述配置代理，当请求了前端不存在的资源时，那么该请求会转发给服务器（优先匹配前端资源）

**方法二**

编写vue.config.js配置具体代理规则

```js
module.exports = {
  devServer: {
    proxy: {
      '/api1': { //匹配所有以'/atguigu'开头的请求路径
        target: 'http://localhost:5000', //代理目标的基础路径
        ws: true,
        changeOrigin: true,
        pathRewrite: { '^/api1': '' },
      },
      '/api2': {
        target: 'http://localhost:5001',
        ws: true,
        changeOrigin: true,
        pathRewrite: { '^/api2': '' },
      },
    }
  }
}
/*
  changeOrigin设置为true时，服务器收到的请求头中的host为：localhost:5000
  changeOrigin设置为false时，服务器收到的请求头中的host为：localhost:8080
  changeOrigin默认值为true
*/
```

说明：

1. 优点：可以配置多个代理，且可以灵活的控制请求是否走代理
2. 缺点：配置略微繁琐，请求资源时必须加前缀

**App.vue**

```vue
<template>
<div>
    <button @click="getStudents">获取学生信息</button>
    <button @click="getCars">获取汽车信息</button>
</div>
</template>

<script>
import axios from 'axios'
export default {
    name:'App',
    methods:{
        getStudents(){
            axios.get('http://localhost:8000/atguigu/students').then(
                response => {
                     console.log('请求成功了',response.data)
                },
                error => {
                    console.log('请求失败了',error.message)
                }
            )
        },
        getCars(){
            axios.get('http://localhost:8000/demo/cars').then(
                response => {
                     console.log('请求成功了',response.data)
                },
                error => {
                    console.log('请求失败了',error.message)
                }
            )
        }
    }
}
</script>
```

**vue.config.js**

```js
const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,
    lintOnSave: false,
    /*关闭语法检查*/
    //开启代理服务器（方式一）
    // devServer:{
    //     proxy:'http://localhost:5000'
    // }
    //开启代理服务器（方式二）
    devServer: {
        proxy: {
            '/atguigu': {//匹配所有以'/atguigu'开头的请求路径
                target: 'http://localhost:5000',
                pathRewrite: { '^/atguigu': '' },
                ws: true, //用于支持websocket
                changeOrigin: true
            },
            '/demo': {
                target: 'http://localhost:5001',
                pathRewrite: { '^/demo': '' },
                ws: true, //用于支持websocket
                changeOrigin: true
            },
        }
    }
})
```

#### 插槽

1. 作用：让父组件可以向子组件指定位置插入html结构，也是一种组件间通信方式，适用于父组件 ===> 子组件
2. 分类：默认插槽、具名插槽、作用域插槽
3. 使用方式

**默认插槽**

**Category.vue**

```vue
<template>
  <div class="category">
      <h3>{{title}}分类</h3>
      <!-- 定义一个插槽 -->
      <slot>我是一个默认值，当使用者没有传递具体结构时，我会出现</slot>
      <!-- <ul>
          <li v-for="(item,index) in listData" :key="index">{{item}}</li>
      </ul> -->
  </div>
</template>

<script>
    export default {
        name:'Category',
        props:['title']
    }
</script>

<style>
    .category{
        background-color: skyblue;
        width: 200px;
        height: 300px;
    }
    h3{
        text-align: center;
        background-color: orange;
    }
</style>
```

**App.vue**

```vue
<template>
<div class="container">
    <Category title="美食">
        <img src="" alt="">
    </Category>
    <Category title="游戏">
        <ul>
          <li v-for="(g,index) in game" :key="index">{{g}}</li>
        </ul>
    </Category>
    <Category title="电影">
        <video src=""></video>
    </Category>
</div>
</template>

<script>
import Category from './components/Category.vue'
export default {
    name:'App',
    components:{Category},
    data(){
        return{
            foods:['火锅','烧烤','牛排'],
            games:['红色警戒','穿越火线','劲舞团','超级玛丽'],
            films:['《j教父》','《拆弹专家》','《你好，李焕英》','《尚硅谷》']
        }
    }
}
</script>

<style scoped>
    .container{
        display: flex;
        justify-content: space-around;
    }
    video{
        width: 100%;
    }
    img{
        width: 100%;
    }
</style>
```

**具名插槽**

**Category.vue**

```vue
<template>
  <div class="category">
      <h3>{{title}}分类</h3>
      <!-- 定义一个插槽 -->
      <slot name="center">我是一个默认值，当使用者没有传递具体结构时，我会出现1</slot>
      <slot name="footer">我是一个默认值，当使用者没有传递具体结构时，我会出现2</slot>

  </div>
</template>

<script>
    export default {
        name:'Category',
        props:['title']
    }
</script>

<style>
    .category{
        background-color: skyblue;
        width: 200px;
        height: 300px;
    }
    h3{
        text-align: center;
        background-color: orange;
    }
</style>
```

**App.vue**

```vue
<template>
<div class="container">
    <Category title="美食">
        <img slot="center" src="" alt="">
        <a slot="footer" href="">更多美食</a>
    </Category>
    <Category title="游戏">
        <ul slot="center">
          <li v-for="(g,index) in games" :key="index">{{g}}</li>
        </ul>
        <div class="foot" slot="footer">
            <a href="">单机游戏</a>
            <a href="">网络游戏</a>
        </div>
    </Category>
    <Category title="电影">
        <video slot="center" controls src=""></video>
        <template v-slot:footer>
            <div class="foot">
                <a href="">经典</a>
                <a href="">热门</a>
                <a href="">推荐</a>
            </div>
            <h4>欢迎前来观影</h4>
        </template>
    </Category>
</div>
</template>

<script>
import Category from './components/Category.vue'
export default {
    name:'App',
    components:{Category},
    data(){
        return{
            foods:['火锅','烧烤','牛排'],
            games:['红色警戒','穿越火线','劲舞团','超级玛丽'],
            films:['《j教父》','《拆弹专家》','《你好，李焕英》','《尚硅谷》']
        }
    }
}
</script>

<style scoped>
    .container,.foot{
        display: flex;
        justify-content: space-around;
    }
    video{
        width: 100%;
    }
    img{
        width: 100%;
    }
    h4{
        text-align: center;
    }
</style>
```

**作用域插槽**

**Category.vue**

```vue
<template>
  <div class="category">
      <h3>{{title}}分类</h3>
      <slot :games="games">我是默认的一些内容</slot>
  </div>
</template>

<script>
    export default {
        name:'Category',
        props:['title'],
        data(){
            return{
                games:['红色警戒','穿越火线','劲舞团','超级玛丽']
            }
        }
    }
</script>

<style>
    .category{
        background-color: skyblue;
        width: 200px;
        height: 300px;
    }
    h3{
        text-align: center;
        background-color: orange;
    }
</style>
```

**App.vue**

```vue
<template>
<div class="container">
    <Category title="游戏">
        <template scope="atguigu">
          <ul>
            <li v-for="(g,index) in atguigu.games" :key="index">{{g}}</li>
          </ul>
        </template>
    </Category>

    <Category title="游戏">
        <template scope="{games}">
          <ol>
            <li style="color:red" v-for="(g,index) in games" :key="index">{{g}}</li>
          </ol>
        </template>
    </Category>

    <Category title="游戏">
        <template slot-scope="{games}">
          <h4 v-for="(g,index) in games" :key="index">{{g}}</h4>
        </template>
    </Category>
</div>
</template>

<script>
import Category from './components/Category.vue'
export default {
    name:'App',
    components:{Category},
    
}
</script>

<style scoped>
    .container,.foot{
        display: flex;
        justify-content: space-around;
    }
    video{
        width: 100%;
    }
    img{
        width: 100%;
    }
    h4{
        text-align: center;
    }
</style>
```

### Vuex

概念：专门在Vue中实现集中式状态（数据）管理的一个Vue插件，对vue应用中多个组件的共享状态进行集中式的管理（读/写），也是一种组件间通信的方式，且适用于任意组件通信

**什么时候使用Vuex**

1. 多个组件依赖于同一状态
2. 来自不同组件的行为需要变更同一状态

![](C:\Users\admin\Desktop\个人资料\笔记\img\Vue_10.png)

#### 搭建vuex环境

1. 创建文件：src/store/index.js

   ```js
   //该文件用于创建Vuex中最为核心的store
   
   import Vuex from 'vuex'
   //应用Vuex插件
   Vue.use(Vuex)
   
   //准备actions——用于响应组件中的动作
   const actions = {}
       //准备mutations——用于操作数据（state）
   const mutations = {}
       //准备state——用于存储数据
   const state = {}
   
   //创建并暴露store
   export default new Vuex.Store({
       actions,
       mutations,
       state,
   })
   ```

2. 在main.js中创建vm时传入store配置项

   ```js
   import store from './store'
   ...
   new Vue({
       el: '#app',
       render: h => h(App),
       store,
   })
   ```

#### 基本使用

**main.js**

```js
import Vue from 'vue'
import App from './App.vue'
import store from './store'

Vue.config.productionTip = false

new Vue({
    el: '#app',
    render: h => h(App),
    store,
    // beforeCreate() {
    //     Vue.prototype.$bus = this //安装全局事件总线
    // }
})
```

**Count.vue**

```vue
<template>
  <div>
      <h1>当前求和为：{{$store.state.sum}}</h1>
      <select v-model.number="n">
        <option value="1">1</option>
        <option value="2">2</option>
        <option value="3">3</option>
      </select>
      <button @click="increment">+</button>
      <button @click="decrement">-</button>
      <button @click="incrementOdd">当前求和为奇数再加</button>
      <button @click="incrementWait">等一等再加</button>
  </div>
</template>

<script>
export default {
    name:'Count',
    data(){
        return{
            n:1,//用户选择的数字
        }
    },
    methods:{
        increment(){
            this.$store.commit('JIA',this.n)
        },
        decrement(){
            this.$store.commit('JIAN',this.n)
        },
        incrementOdd(){
            this.$store.dispatch('jiaOdd',this.n)
        },
        incrementWait(){
            this.$store.dispatch('jiaWait',this.n)
        },
    }
}
</script>

<style lang="css">
    button{
        margin-left: 5px;
    }
</style>
```

**index.js**

```js
//该文件用于创建Vuex中最为核心的store

import Vue from 'vue'
import Vuex from 'vuex'
//应用Vuex插件
Vue.use(Vuex)

//准备actions——用于响应组件中的动作
const actions = {
        // jia(context, value) {
        //     context.commit('JIA', value)
        // },
        // jian(context, value) {
        //     context.commit('JIAN', value)
        // },
        jiaOdd(context, value) {
            if (context.state.sum % 2) {
                context.commit('JIA', value)
            }
        },
        jiaWait(context, value) {
            setTimeout(() => {
                context.commit('JIA', value)
            }, 500)
        }
    }
    //准备mutations——用于操作数据（state）
const mutations = {
        JIA(state, value) {
            state.sum += value
        },
        JIAN(state, value) {
            state.sum -= value
        }
    }
    //准备state——用于存储数据
const state = {
    sum: 0
}

//创建并暴露store
export default new Vuex.Store({
    actions,
    mutations,
    state,
})
```

#### getters的使用

1. 概念：当state中的数据需要经过加工后再使用时，可以使用getters加工

2. 在store.js中追加getters配置

   ```js
   const getters = {
       bigSum(state) {
           return state.sum * 10
       }
   }
   
   //创建并暴露store
   export default new Vuex.Store({
      ...
       getters
   })
   ```

3. 组件中读取数据：`$store.getters.bigSum`

#### 4个map方法的使用

1. mapState方法：用于帮助我们映射state中的数据为计算属性

   ```js
   computed:{
     //借助mapState生成计算属性，从state中读取数据（对象写法）
     ...mapState({he:'sum',xuexiao:'school',xueke:'subject'}),
       
     //借助mapState生成计算属性，从state中读取数据（数组写法）
     ...mapState(['sum','school','subject']),  
   }
   ```

2. mapGetters方法：用于帮助我们映射getters中的数据为计算属性

   ```js
   computed:{
      //借助mapGetters生成计算属性，从getters中读取数据（对象写法）
      ...mapGetters({bigSum:'bigSum'})
       
      //借助mapGetters生成计算属性，从getters中读取数据（数组写法）
      ...mapGetters(['bigSum'])
   }
   ```

3. mapAction方法：用于帮助我们生成于actions对话的方法，即：包含`$store.dispatch(xxx)`的函数

   ```js
   methods:{
       //借助mapMutations生成对应的方法，方法中会调用commit去联系mutations（对象写法）
       ...mapMutations({increment:'JIA',decrement:'JIAN'}),
       //借助mapMutations生成对应的方法，方法中会调用commit去联系mutations（数组写法）
       ...mapMutations(['JIA','JIAN']),
   }
   
   //调用
   <button @click="increment(n)">+</button>
   <button @click="decrement(n)">-</button>  
   ```

4. mapMutations方法：用于帮助我们生成于mutations对话的方法，即：包含`$store.commit(xxx)`的函数

   ```js
   methods:{
      //借助mapActions生成对应的方法，方法中会调用dispatch去联系actions（对象写法）
      ...mapActions({incrementOdd:'jiaOdd',incrementWait:'jiaWait'}),
      //借助mapActions生成对应的方法，方法中会调用dispatch去联系actions（数组写法）
      ...mapActions(['jiaOdd','jiaWait']),
   }
   
   //调用
   <button @click="incrementOdd(n)">当前求和为奇数再加</button>
   <button @click="incrementWait(n)">等一等再加</button>
   ```

#### 模块化+命名空间

1. 目的：让代码更好维护，让多种数据分类更加明确

2. 修改store.js

   ```js
   //求和相关的配置
   const countAbout = {
       namespaced:true,//开启命名空间
       actions:{},
       mutations:{},
       state:{},
       getters:{},
   }
   //人员管理相关的配置
   const personAbout = {
       namespaced:true,//开启命名空间
       actions:{},
       mutations:{},
       state:{},
       getters:{},
   }
   
   //创建并暴露store
   export default new Vuex.Store({
       modules:{
           countAbout,
           personAbout
       }
   })
   ```

3. 开启命名空间后，组件读取state数据

   ```js
   //方式一：自己直接读取
   this.$store.state.personAbout.list
   //方式二：借助mapState读取
   ...mapState('countAbout',['sum','school','subject'])
   ```

4. 开启命名空间后，组件读取getters数据

   ```js
   //方式一：自己直接读取
   this.$store.getters['personAbout/firstPersonName']
   //方式二：借助mapGetters读取
   ...mapGetters('countAbout',['bigSum'])
   ```

5. 开启命名空间后，组件中调用dispatch

   ```js
   //方式一：自己直接读取
   this.$store.dispatch('personAbout/addPersonWang',person)
   //方式二：借助mapActions读取
   ...mapActions('countAbout',{incrementOdd:'jiaOdd',incrementWait:'jiaWait'})
   ```

6. 开启命名空间后，组件中调用commit

   ```js
   //方式一：自己直接读取
   this.$store.commit('personAbout/ADD_PERSON',person)
   //方式二：借助mapMutations读取
   ...mapMutations('countAbout',{increment:'JIA',decrement:'JIAN'})
   ```


### vou-router

**vue-router：**vue的一个插件库，专门用来实现SPA应用

**SPA（single page web application）：**单页web应用，整个应用只有要给完整的页面，点击页面中的导航链接不会刷新页面，只会做页面的局部更新，数据需要通过ajax请求获取

**什么是路由？**

1. 一个路由就是一组映射关系（key-value）
2. key为路径，value可能是function或component

**路由的分类**

1. 后端路由：
   1. 理解：value是function，用于处理客户端提交的请求
   2. 工作过程：服务器接收到一个请求时，根据请求路径找到匹配的函数来处理请求，返回响应数据
2. 前端路由：
   1. 理解：value是component，用于展示页面的内容
   2. 工作过程：当浏览器的路径改变时，对应的组件就会显示

#### 路由的基本使用

1. 安装vue-router，命令：`npm i vue-router`

2. 应用插件：`Vue.use(VueRouter)`

3. 编写router配置项：

   ```js
   import VueRouter from 'vue-router'
   
   import About from '../pages/About'
   import Home from '../pages/Home'
   
   //创建一个路由器
   export default new VueRouter({
       routes: [{
               path: '/about',
               component: About
           },
           {
               path: '/home',
               component: Home
           },
       ]
   })
   ```

4. 实现切换（active-class可配置高亮样式）

   ```vue
   <router-link active-class="active" to="/about">About</router-link>
   ```

5. 指定展示位置

   ```vue
   <router-view></router-view>
   ```

**components/Banner.vue**

```vue
<template>
    <div class="col-xs-offset-2 col-xs-8">
        <div class="page-header"><h2>Vue Router Demo</h2></div>
    </div>
</template>

<script>
export default {
    name:'Banner'
}
</script>
```

**pages/About.vue、pages/Home.vue**

```vue
<template>
  <h2>我是About的内容</h2>
</template>

<script>
export default {
    name:'About'
}
</script>

<!--  -->
<template>
  <h2>我是Home的内容</h2>
</template>

<script>
export default {
    name:'Home'
}
</script>
```

**router/index.js**

```js
//该文件专门用于创建整个应用的路由器
import VueRouter from 'vue-router'

import About from '../pages/About'
import Home from '../pages/Home'

//创建一个路由器
export default new VueRouter({
    routes: [{
            path: '/about',
            component: About
        },
        {
            path: '/home',
            component: Home
        },
    ]
})
```

**App.vue**

```vue
<template>
  <div>
    <div class="row">
      <Banner/>
    </div>
    <div class="row">
      <div class="col-xs-2 col-xs-offset-2">
        <div class="list-group">
          <!-- 原始html中我们使用a标签实现页面的跳转 -->
          <!-- <a class="list-group-item active" href="./about.html">About</a>
          <a class="list-group-item" href="./home.html" >Home</a> -->

          <!-- Vue中借助router-link标签实现路由的切换 -->
          <router-link class="list-group-item" active-class="active" to="/about">About</router-link>
          <router-link class="list-group-item" active-class="active" to="/home" >Home</router-link>
        </div>
      </div>
      <div class="col-xs-6">
        <div class="panel">
          <div class="panel-body">
            <!-- 指定组件的呈现位置 -->
            <router-view></router-view>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Banner from './components/Banner.vue'
export default {
    name:'App',
    components:{Banner}
}
</script>
```

**main.js**

```js
import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'

import router from './router'
Vue.config.productionTip = false

Vue.use(VueRouter)

new Vue({
    el: '#app',
    render: h => h(App),
    router: router
})
```

#### 几个注意点

1. 路由组件通常存放在pages文件夹，一般组件通常存放在components文件夹
2. 通过切换，隐藏了的路由组件，默认是被销毁掉的，需要的时候再去挂载
3. 每个组件都有自己的$route属性，里面存储着自己的路由信息
4. 整个应用只有一个router，可以通过组件的$router属性获取到

#### 嵌套路由（多级路由）

1. 配置路由规则，使用children配置项：

   ```js
   //该文件专门用于创建整个应用的路由器
   import VueRouter from 'vue-router'
   
   import About from '../pages/About'
   import Home from '../pages/Home'
   import News from '../pages/News'
   import Message from '../pages/Message'
   
   //创建一个路由器
   export default new VueRouter({
       routes: [{
               path: '/about',
               component: About
           },
           {
               path: '/home',
               component: Home,
               children: [{ //通过children配置子级路由
                       path: 'news', //此处一定不要写：/news
                       component: News,
                   },
                   {
                       path: 'message',
                       component: Message,
                   }
               ]
           },
       ]
   })
   ```

2. 跳转（要写完整路径）

   ```vue
   <router-link to="/home/news">News</router-link>
   ```

#### 路由的query参数

1. 传递参数

   ```vue
   <!-- 跳转路由并携带query参数，to的字符串写法 -->
   <router-link :to="/home/message/detail?id=666&title=你好">跳转</router-link> 
               <!-- 跳转路由并携带query参数，to的对象写法 -->
               <router-link :to="{
                   path:'/home/message/detail',
                   query:{
                       id:m.id,
                       title:m.title
                   }
               }">跳转
   </router-link>
   ```

2. 接收参数

   ```
   $route.query.id
   $route.query.title
   ```

#### 命名路由

1. 作用：可以简化路由的跳转

2. 如何使用

   1. 给路由命名：

      ```js
      //创建一个路由器
      export default new VueRouter({
          routes: [{
                  name: 'guanyu', //给路由命名
                  path: '/about',
                  component: About
              },
              {
                  path: '/home',
                  component: Home,
                  children: [{
                          path: 'news',
                          component: News,
                      },
                      {
                          path: 'message',
                          component: Message,
                          children: [{
                              name: 'xiangqing', //给路由命名
                              path: 'detail',
                              component: Detail,
                          }]
                      }
                  ]
              },
          ]
      })
      ```

   2. 简化跳转：

      ```vue
      <!--简化前，需要写完整的路径-->
      <router-link to=“/home/message/detail”>跳转</router-link>
      
      <!--简化后，直接通过名字跳转-->
      <router-link :to=“{name:'xiangqing'}”>跳转</router-link>
      
      <!--简化写法配合传递参数-->
      <router-link :to="{
                      //path:'/home/message/detail',
                      name:'xiangqing',
                      query:{
                          id:m.id,
                          title:m.title
                      }
                  }">跳转
      </router-link>
      ```

#### 路由的params参数

1. 配置路由，声明接收params参数

   ```js
   export default new VueRouter({
       routes: [{
               name: 'guanyu',
               path: '/about',
               component: About
           },
           {
               path: '/home',
               component: Home,
               children: [{
                       path: 'news',
                       component: News,
                   },
                   {
                       path: 'message',
                       component: Message,
                       children: [{
                           name: 'xiangqing',
                           path: 'detail/:id/:title',//使用占位符声明接收params参数
                           component: Detail,
                       }]
                   }
               ]
           },
       ]
   })
   ```

2. 传递参数

   ```vue
   <!--跳转并携带params参数，to的字符串写法-->
   <router-link to=“/home/message/detail/666/你好”>跳转</router-link>
   
   <router-link :to="`/home/message/detail/${m.id}/${m.title}`">{{m.title}}</router-link>
   
   <!--跳转并携带params参数，to的对象写法-->
   <router-link :to="{
                   name:'xiangqing',
                   params:{
                       id:m.id,
                       title:m.title
                   }
               }">跳转
   </router-link>
   ```

   特别注意：路由携带params参数时，若用to的对象写法，则不能使用path配置项，必须使用name配置

3. 接收参数

   ```
   $route.params.id
   $route.params.title
   ```

#### 路由的props配置

作用：让路由组件更方便的收到参数

```js
{
                        name: 'xiangqing',
                        path: 'detail/:id/:title',
                        component: Detail,
                        // props的第一种写法，值为对象，该对象中所有的key-value都会以props的形式传给detail组件
                        // props:{
                        //     a:1,
                        //     b:'hello'
                        // }

                        //props的第二种写法，值为布尔值，若布尔值为真，就会把该路由组件收到的所有params参数，以props的形式传给detail组件
                        //props: true

                        //第三种写法，值为函数，该函数返回的对象中每一组key-value都会通过props传给detail组件
                        props(route) {
                            return {
                                id: route.query.id,
                                title: route.query.title
                            }
                        }
                    }

//Detail.vue接收参数
<template>
  <ul>
    <li>消息编号：{{id}}</li>
    <li>消息标题：{{title}}</li>
  </ul>
</template>

<script>
export default {
    name:'Detail',
    props:['id','title']
}
</script>
```

#### `<router-link>`的replave属性

1. 作用：控制路由跳转时操作浏览器历史记录的模式
2. 浏览器的历史记录有两种写入方式：分别为push和replace，push是追加历史记录，replace是替换当前记录。路由跳转时候默认为push
3. 如何开启replace模式：`<router-link replace ....>News</router-link>`

#### 编程式路由导航

1. 作用：不借助`<router-link>`实现路由跳转，让路由跳转更加灵活

2. 具体编码：

   ```vue
   <template>
     <div>
       <ul>
           <li v-for="m in messageList" :key="m.id">
               <!-- 跳转路由并携带params参数，to的字符串写法 -->
               <!-- <router-link :to="`/home/message/detail/${m.id}/${m.title}`">{{m.title}}</router-link>&nbsp;&nbsp; -->
               <!-- 跳转路由并携带params参数，to的对象写法 -->
               <router-link :to="{
                   name:'xiangqing',
                   query:{
                       id:m.id,
                       title:m.title
                   }
               }">
                   {{m.title}}
               </router-link>
               <button @click="pushShow(m)">push查看</button>
               <button @click="replaceShow(m)">replace查看</button>
           </li>
       </ul>
       <hr>
       <router-view></router-view>
     </div>
   </template>
   
   <script>
   export default {
       name:'Message',
       data(){
           return{
               messageList:[
                   {id:'001',title:'消息001'},
                   {id:'002',title:'消息002'},
                   {id:'003',title:'消息003'}
               ]
           }
       },
       methods:{
           pushShow(m){
               //编程式路由
               this.$router.push({
                   name:'xiangqing',
                   query:{
                       id:m.id,
                       title:m.title
                   }
               })
           },
           replaceShow(m){
               //编程式路由
               this.$router.replace({
                   name:'xiangqing',
                   query:{
                       id:m.id,
                       title:m.title
                   }
               })
           }
       }
   }
   </script>
   
   this.$router.forward() //前进
   this.$router.back() //后退
   this.$router.go(3) //可前进也可后退
   ```

#### 缓存路由组件

1. 作用：让不展示的路由组件保持挂载，不被销毁

2. 具体编码：

   ```vue
   <!--缓存一个路由组件-->
   <keep-alive include="News">
       <router-view></router-view>
   </keep-alive>
   
   <!--缓存多个路由组件-->
   <keep-alive :include="['News','Message']">
       <router-view></router-view>
   </keep-alive>
   ```

#### 两个新的生命周期钩子

1. 作用：路由组件所独有的两个钩子，用于捕获路由组件的激活状态
2. 具体名字：
   1. `actived`路由组件被激活时触发
   2. `deactived`路由组件失活时触发

#### 路由守卫

1. 作用：对路由进行权限控制

2. 分类：全局守卫、独享守卫、组件内守卫

3. 全局守卫：

   ```js
   //创建一个路由器
   const router = new VueRouter({
       routes: [{
               name: 'guanyu',
               path: '/about',
               component: About,
               meta: { title: '关于' }
           },
           {
               name: 'zhuye',
               path: '/home',
               component: Home,
               meta: { title: '主页' },
               children: [{
                       name: 'xinwen',
                       path: 'news',
                       component: News,
                       meta: { isAuth: true, title: '新闻' }
                   },
                   {
                       name: 'xiaoxi',
                       path: 'message',
                       component: Message,
                       meta: { isAuth: true, title: '消息' },
                       children: [{
                           name: 'xiangqing',
                           path: 'detail/:id/:title',
                           component: Detail,
                           meta: { isAuth: true, title: '详情' }
                       }]
                   }
               ]
           },
       ]
   })
   
   //全局前置路由守卫——初始化的时候被调用、每次路由切换之前被调用
   router.beforeEach((to, from, next) => {
       if (to.meta.isAuth) { //判断是否需要鉴权
           if (localStorage.getItem('school') === 'atguigu') {
               next()
           }
       } else {
           next()
       }
   })
   
   //全局后置路由守卫——初始化的时候被调用、每次路由切换之后被调用
   router.afterEach((to, from, next) => {
       document.title = to.meta.title || '硅谷系统'
   })
   export default router
   ```

4. 独享守卫：

   ```js
   name: 'xinwen',
   path: 'news',
   component: News,
   meta: { isAuth: true, title: '新闻' },
   beforeEnter:(to,from,next) => {
       if (to.meta.isAuth) { //判断是否需要鉴权
              if (localStorage.getItem('school') === 'atguigu') {
                        next()
                  }
           } else {
               next()
           }
   }
   ```

5. 组件内守卫：

   ```vue
   <template>
     <h2>我是About的内容</h2>
   </template>
   
   <script>
   export default {
       name:'About',
       //通过路由规则，进入该组件时被调用
       beforeRouteEnter (to,from,next) {
         if (to.meta.isAuth) { //判断是否需要鉴权
               if (localStorage.getItem('school') === 'atguigu') {
                   next()
               }
           } else {
               next()
           }
       },
       //通过路由规则，离开该组件时被调用
       beforeRouteLeave (to,from,next){
         next()
       }
   }
   </script>
   ```

#### 路由器的两种工作模式

1. 对于有一个url来说，什么是hash值？——#及其后面的内容就是hash值
2. hash值不会包含在HTTP请求中，即：hash值不会带给服务器
3. hash模式：
   1. 地址中永远带着#号，不美观
   2. 若以后将地址通过第三方手机app分享，若app校验合格，则地址会被标记为不合法
   3. 兼容性较好
4. history模式：
   1. 地址干净，美观
   2. 兼容性和hash模式相比略差
   3. 应用部署上线时需要后端人员支持，解决刷新页面服务器404的问题

```js
const router = new VueRouter({
    mode: 'history',//开启history模式，默认为hash模式
    routes: [{
            name: 'guanyu',
            path: '/about',
            component: About,
            meta: { isAuth: true, title: '关于' }
        }]
})
```

### Vue3
