###  java基础

比特（bit），计算机中最小的存储单位

字节（byte），计算机中最基本的存储单元，每个字节由8个比特构成。

原码： 原码就是符号位加上真值的绝对值, 即用第一位表示符号, 其余位表示值 

反码：正数的反码是其本身，负数的反码是在其原码的基础上, 符号位不变，其余各个位取反

补码：正数的补码就是其本身，负数的补码是在其原码的基础上, 符号位不变, 其余各位取反, 最后+1. (即在反码的基础上+1)

&按位与：两个都为1，结果为1，否则为0

|按位或： 参加运算的两个对象只要有一个为1，其值为1 

^按位异或： 参加运算的两个对象，如果两个相应位为“异”（值不同），则该位结果为1，否则为0 

~按位取反： 对一个二进制数按位取反，即将0变1，1变0 



**基本数据类型之间的运算规则：**

1. 自动类型提升

   当容量小的数据类型的变量与容量大的数据类型的变量做运算时，结果自动提升为容量大的类型

   byte、char、short --> int --> long --> float --> double

   特别的：byte,char,short之间做运算时，结果是int

2. 强制类型转换（自动类型提升的逆运算）

   需要使用强转符，可能导致精度损失

%取余运算：结果的符号与被模数的符号相同 

例：12%5=2，-12%5=-2，-12%-5=-2，12%-5=2

&和&&的运算结果相同，当符号左边时true时，二者都会执行右边的运算，当符号左边为false时，&继续执行符号右边的运算，&&不再执行符号右边的运算

|和||的运算结果相同，当符号左边时false时，二者都会执行右边的运算，当符号左边为true时，|继续执行符号右边的运算，||不再执行符号右边的运算

switch表达式只能是如下的六种数据类型之一：byte,short,int,char,枚举类型,string类型

**浅拷贝：**浅拷贝仅仅复制所考虑的对象，而不会复制他所引用的对象

**深拷贝：**深拷贝把要复制的对象所引用的对象都复制了一遍

1. 选择排序

   + 直接排序
   + 堆排序

2. 交换排序

   + 冒泡排序

     ```java
     int[] arr=new int[]{9,8,7,6,5,4,3,2,1,0};
     int temp;
     for (int i=0;i<arr.length;i++){
         for (int j=0;j<arr.length-i-1;j++){
             if (arr[j] > arr[j+1]) {
                 temp=arr[j];
                 arr[j]=arr[j+1];
                 arr[j+1]=temp;
             }
         }
     }
     ```

   + 快速排序

     ```java
     //low=0,high=array.length-1
     public void quickSort(int date[], int low, int high) {
         if (low > high) {//必须要加这一句话 不然会弹出StackOverflowError的警告
             return;
         }
         int i = low, j = high, temp = date[low], t;//i是最左边 j是最右边 t临时变量 temp基准点
         //开始总循环,i不能大于j，不然弹出StackOverflowError的警告
         if (low < high) {
             //从右往左看依次递减
             while (date[j] >= temp && i < j) {
                 j--;
             }
             //从左网友看依次递增
             while (date[i] <= temp && i < j) {
                 i++;
             }
             //如果满足条件则交换
             if (i < j) {
                 t = date[j];
                 date[j] = date[i];
                 date[i] = t;
             }
         }
         //i，j相等跳出循环
         date[low] = date[i];
         date[i] = temp;
         
         quickSort(date, low, j - 1);//递归调用左半数组
         quickSort(date, j + 1, high);//递归调用右半数组
     }
     ```

3. 插入排序

   + 直接插入排序

     ```java
     int[] arr=new int[]{9,8,7,6,5,4,3,2,1,0};
     for (int i=1;i<arr.length;i++){
         int temp=arr[i];
         int j=i-1;
         for (;j>=0&&arr[j]>temp;j--){
             arr[j+1]=arr[j];
         }
         arr[j+1]=temp;
     }
     ```

   + 折半插入排序

   + 希尔排序

4. 归并排序

5. 桶式排序

6. 基数排序

**值传递机制**

如果参数是基本数据类型，此时实参赋给形参的是实参真实存储的数据值

如果变量是引用数据类型，此时实参赋给形参的是实参存储数据的地址值

**继承**

父类中声明为private的属性和方法，子类继承父类后，仍然认为获取了父类中私有的结构。只是因为封装性的原因，使得子类不能直接调用父类的结构而已

子类继承父类后，就获得了直接父类和所有间接父类中声明的属性和方法

子类重写的方法的权限修饰符不小于父类被重写的方法的权限修饰符

子类不能重写父类中声明为private权限方法

父类被重写方法的返回值类型是A类型，则子类重写的方法的返回值类型可以是A类或A类的子类

父类被重写的方法的返回值类型是基本数据类型，则子类重写的方法的返回类型必须是相同的基本数据类型

子类重写的方法抛出的异常不大于父类被重写的方法抛出的异常类型

子类和父类中的同名同参数的方法要么都声明为非static的（考虑重写），要么都声明为static的（不是重写）



**类中各成员初始化顺序**

**Object类中toString()的使用**

1. 当我们输出一个对象的引用时，实际上就是调用当前对象的toString()
2. String、Date、File、包装类等都重写了Object类中的toString()方法



**基本数据类型 --> 包装类：**调用包装类的构造器   //Integer int1=new Integer(10);

**包装类 --> 基本数据类型：**调用包装类的xxxValue()方法   //int i1=int1.intValue();

**自动装箱：**Integer int1=10;

**自动拆箱：**int i1=int1;

**基本数据类型、包装类 ---> String类型：**调用String重载的valueOf(Xxx xxx)   //String str=String.valueOf(i1)

**String类型 ---> 基本数据类型、包装类：**调用包装类的parseXxx()   //int i1=Integer.parseInt(str)

```java
//三元运算符类型会提升为同一个类型
例：Object o1=true?new Interger(1):new Double(2.0);
	System.out.println(o1);//1.0
```



**单例模式饿汉式和懒汉式**

**饿汉式：**加载时间过长，饿汉式是线程安全的

**懒汉式：**延迟对象创建，线程不安全



**代码块**

作用：初始化类、对象，如果有修饰的话，只能使用static

**静态代码块：**内部可以有输出语句，随着类的加载而执行，而且只执行一次，如果一个类中定义了多个静态代码块，则按照声明的先后顺序执行，静态代码块的执行要优先于非静态代码块的执行，静态代码块内只能调用静态的属性、方法，不能调用非静态的结构

**非静态代码块：**内部可以有输出语句，随着对象的创建而执行，没创建一个对象，就执行一次非静态代码块，可以在创建对象时，对对象的属性等进行初始化，如果一个类中定义了多个非静态代码块，则按照声明的先后顺序执行，非静态代码块可以调用静态的属性、方法，非静态的属性、方法

**对属性赋值的位置：**

1. 默认初始化
2. 显示初始化/5. 在代码块中赋值
3. 构造器中初始化
4. 有了对象以后，可以通过"对象.属性"或"对象.方法"的方式，进行赋值

执行的先后顺序：1 --> 2/5 --> 3 --> 4



**对象初始化顺序：**父类静态变量，父类静态代码块，子类静态变量，子类静态代码块，父类成员变量，父类非静态代码块，父类构造方法，子类成员变量，子类非静态代码块，子类构造方法



**final**

当final修饰一个基本数据类型时，表示该基本数据类型的值一旦在初始化后便不能发生变化；如果final修饰一个引用类型时，则在对其初始化之后便不能再让其指向其他对象了，但该引用所指向的对象的内容是可以发生变化的。 



**抽象类**

1. 抽象类中一定有构造器
2. 抽象类中可以存在普通属性，方法，静态属性和方法。
3. 抽象类中可以存在抽象方法，抽象方法不能有方法体。
4. abstract不能修饰属性，构造器等结构
5. 如果一个类中有一个抽象方法，那么当前类一定是抽象类；抽象类中不一定有抽象方法。
6. 抽象类中的抽象方法，需要有子类实现，如果子类不实现，则子类也需要定义为抽象的。
7. 抽象类不能被实例化，抽象类和抽象方法必须被 abstract 修饰。
8. 关键字使用注意：
   抽象类中的抽象方法（其前有 abstract 修饰）不能用 private、final、static、synchronized、native 访问修饰符修饰。
9. 抽象类可以实现接口，但是抽象类不必实现接口的方法。
   但当抽象类不实现接口方法时，继承该抽象类的非抽象类必须实现抽象类的抽象方法和接口方法，但是当抽象类实现接口方法的时候，继承的类就不用在实现该方法了，本质上，接口和抽象类的方法都是abstract 方法，abstract 方法需要实现，沿着继承链一层层找一下，总要有实现该方法的类。

**接口**

1. 在接口中只有方法的声明，没有方法体。
2. 在接口中只有常量，因为定义的变量，在编译的时候都会默认加上 public static final
3. 在接口中的方法，永远都被 public 来修饰，没有修饰的情况下默认是public abstract。
4. 1.8中可以有静态方法和默认方法
5. 接口中没有构造方法，也不能实例化接口的对象。（所以接口不能继承类）
6. 接口可以实现多继承 也就是 可以extends interfaceA,interfaceB,
7. 接口中定义的方法都需要有实现类来实现，如果实现类不能实现接口中的所有方法则实现类定义为抽象类。
8. 接口可以继承接口，用 extends，但是接口不允许implements
   注意：

抽象类 可以有 private 属性和 private 方法
原因：因为抽象类可以有自己方法实现，自己方法的实现可能会用到自己的私有变量。可以通过自身公有方法调用自身的私有方法。

接口中只能有全局常量和公共的抽象方法，不能有 private 属性和方法

匿名内部类可以不声明显式的对象而直接实例化抽象方法或者接口。

**如果子类（或实现类）继承的父类和实现的接口声明了同名同参数的方法，那么子类在没有重写此方法的情况下，默认调用的是父类中的同名同参数的方法 --> 类优先原则**

**如果子类（或实现类）继承的父类和实现的接口声明了同名同类型的属性，会报错**

接口名.super.方法   //调用接口中的默认方法



**内部类**

Java中允许将一个类A声明在另一个类B中，则类A就是内部类，类B成为外部类

内部类的分类：成员内部类（静态、非静态），局部内部类（方法内、代码块内、构造器内）

成员内部类：

+ 一方面，作为外部类的成员
  + 调用外部类的结构，外部类.this.属性/方法
  + 可以被static修饰
  + 可以被4种不同的权限修饰

+ 另一方面，作为一个类
  + 类内可以定义属性、方法、构造器等
  + 可以被final修饰，表示此类不能被继承
  + 可以被abstract修饰

在局部内部类的方法中，如果调用外部类所声明的方法中的局部变量，要求此局部变量声明为final的，jdk8以后，可以省略final的声明

如何初始化成员内部类的对象

```java
//静态成员内部类
Person.Dog dog=new Person.Dog();
//非静态成员内部类
Person p=new Person();
Person.Bird bird=p.new Bird();
```

如何在成员内部类中区分调用外部类的结构

```java
public void display(String name){
    //方法的形参
    System.out.println(name);
    //内部类的属性
    System.out.println(this.name);
    //外部类的属性
    System.out.println(Person.this.name);
}
```



### 异常处理

**Error：**Java虚拟机无法解决的严重问题。如：jvm系统内部错误、资源耗尽等严重情况。比如：StackOverflowError和OOM。一般不编写针对性的代码进行处理。

**Exception：**其他因编程错误或偶然的外在因素导致的一般性问题，可以使用针对性的代码进行处理。例如：空指针访问、试图读取不存在的文件、网络连接中断、数组角标越界。

**编译时异常：**IOException、FileNotFoundException、ClassNotFoundException

**运行时异常：**NullPointerException、ArrayIndexOutOfBoundsException、ClassCastException、NumberFormatException、InputMismatchException、ArithmaticException

**异常处理方式**

+ try-catch-finally
+ throws + 异常类型

catch中的异常类型如果没有子父类关系，则谁声明在上，谁声明在下无所谓。

**catch中的异常类型如果满足子父类关系，则要求子类一定声明在父类的上面。否则，报错。**

**子类重写的方法抛出的异常类型不大于父类被重写的方法抛出的异常类型**

finally中声明的是一定会被执行的代码，即使catch中出现异常，try中有return语句，catch中有return语句等情况，**finally在return之前执行**

像数据库连接、输入输出流、网络编程Socket等资源，jvm是不能自动回收的，我们需要**手动的进行资源的释放**。此时的资源释放，就需要声明在finally中

"throws + 异常类型"写在方法的声明处。指明此方法执行时，可能会抛出的异常类型。一旦方法体执行时出现异常，仍会在异常代码处生成一个异常类的对象，此对象满足throws后异常类型时，就会被抛出。异常代码后续的代码，就不再执行。

**手动抛出异常：**throw new Exception()

**自定义异常**

1. 继承于现有的异常结构，RuntimeException、Exception
2. 提供全局常量：serialVersionUID
3. 提供重载的构造器



### 多线程

**程序：**是为了完成特定任务、用某种语言编写的一组指令的集合。即指一段静态的代码，静态对象。

**进程：**是程序的一次执行过程，或是正在运行的一个程序。是一个动态的过程：有它自身的产生、存在、和消亡的过程。进程作为资源分配的单位，系统运行时会为每个进程分配不同的内存区域。

**线程：**进程可进一步细化为线程，是一个程序内部的一条执行路径。线程作为调度和执行的单位，每个线程拥有独立的运行栈和程序计数器，同一个进程中的线程共享堆和方法区。



**多线程的创建**

1. 继承Thread类
   1. 创建一个继承于Thread类的子类
   2. 重写Thread类的run()
   3. 创建Thread类的子类的对象
   4. 通过此对象调用start()
   
2. 实现Runnable接口
   1. 创建一个类实现了Runnable接口的类
   2. 实现类去实现Runnable中的抽象方法：run()
   3. 创建实现类的对象
   4. 将此对象作为参数传递到Thread类的构造器中，创建Thread类的对象
   5. 通过Thread类的对象调用start()
   
3. 实现Callable接口

   与使用Runnable相比，Callable功能更强大

   相比run()方法，call()方法可以有返回值，方法可以抛出异常，支持泛型的返回值，需要借助FutureTask类，比如获取返回结果

   1. 创建一个实现Callable接口的实现类
   2. 重写call方法，将此线程需要执行的操作声明在call方法中
   3. 创建Callable接口实现类的对象
   4. 将此Callable接口实现类的对象作为参数传递到FutureTask构造器中，创建FutureTask的对象
   5. 将FutureTask的对象作为参数传递到Thread类的构造器中，创建Thread对象，并调用start()
   6. 获取Callable中call方法的返回值

4. 使用线程池

   好处：提高响应速度（减少了线程创建时间），降低资源消耗（重复利用线程池中线程，不需要每次都创建），便于线程管理（corePoolSize：核心池大小，maximumPoolSize：最大线程数，keepAliveTime：线程没有任务时最多保持多长时间后会终止）

   **ExcutorService：**真正的线程池接口。常见子类ThreadPoolExecutor

   + void execute(Runnable command)：执行任务/命令，没有返回值，一般用来执行Runnable
   + <T> Future <T> submit (Callable<T> task)：执行任务，有返回值，一般用来执行Callable
   + void shutdown()：关闭连接池

   **Executors：**工具类、线程池的工厂类，用于创建不同类型的线程池

   + Executors.newCachedThreadPool()：创建一个可根据需要创建新线程的线程池

   + Executors.newFixedThreadPool()：创建一个可重用固定线程数的线程池
   + Executors.newSingleThreadPool()：创建一个只有一个线程的线程池

   + Executors.newScheduledThreadPool()：创建一个线程池，它可安排 在给定延迟后运行命令或者定期的执行

   1. 创建指定线程数量的线程池 
   2. 执行指定的线程的操作。需要提供实现Runnable接口或Callable接口实现类的对象
   3. 关闭连接池



比较创建线程的两种方式。

优先选择：实现Runnable接口的方式

原因：实现的方式没有单继承的局限性，实现的方式更适合来处理多个线程有共享数据的情况



**Thread中的常用方法**

1. start()：启动当前线程；调用当前线程的run()
2. run()：通常需要重写Thread类中的此方法，将创建的线程要执行的操作声明在此方法中
3. currentThread()：静态方法，返回执行当前代码的线程
4. getName()：获取当前线程的名字
5. setName()：设置当前线程的名字
6. yield()：释放当前cpu的执行权
7. join()：在线程a中调用线程b的join()，此时线程a就进入阻塞状态，直到线程b完全执行完成以后，线程a才结束阻塞状态
8. stop()：已过时，强制结束当前线程。
9. sleep(long millitime)：让当前线程睡眠指定的millitime毫秒，在millitime毫秒时间内，当前线程是阻塞状态
10. isAlive()：判断当前线程是否存活

我们不能通过直接调用run()的方式启动线程，不可以让已经start()的线程调用start().



**线程的优先级**

1. MAX_PRIORITY:10   MIN_PRIORITY:1   NORM_PRIORITY:5
2. getPriority()：获取线程的优先级
3. setPriority(int p)：设置线程的优先级
4. 高优先级的线程要抢占低优先级线程cpu的执行权。但是只是从概率上讲，高优先级的线程高概率的情况下被执行，并不意味着只有当高优先级的线程执行完以后，低优先级的线程才执行



**线程的生命周期**

**新建：**当一个Thread类或其子类的对象被声明并创建时，新生的线程对象处于创建状态

**就绪：**处于新建状态的线程被start()后，将将进入线程队列等待CPU时间片，此时它已具备了运行的条件，只是没分配到CPU资源

**运行：**当就绪的线程被调度并获得CPU资源时，便进入运行状态，run()方法定义了线程的操作和功能

**阻塞：**在某种特殊情况下，被人为挂起或执行输入输出操作时，让出CPU并临时中止自己的执行，进入阻塞状态

**死亡：**线程完成了它的全部工作或线程被提前强制性地中止或出现异常导致结束



![](img/java%E5%9F%BA%E7%A1%80_1.png)

**线程的同步**

**同步代码块：**

```java
sysnchronized(同步监视器){
	//需要被同步的代码
}
//同步监视器：俗称，锁，任何一个类的对象，都可以充当锁，要求多个线程必须要共用同一把锁
```

**同步方法：**同步方法仍然涉及到同步监视器，只是不需要我们显式的声明，非静态的同步方法，同步监视器是：this

静态的同步方法，同步监视器是：当前类本身



同步的方式，解决了线程的安全问题，但操作同步代码时，只能有一个线程参与，其他线程等待。相当于是一个单线程的过程，效率低。

**synchronized与Lock的异同**

二者都可以解决线程安全问题，但synchronized机制在执行完相应的同步代码以后，自动的释放同步监视器，Lock需要手动的启动同步（lock()），同时结束同步也需要手动的实现（unlock()）

**线程通信三个方法：**wait()、notify()、notifyAll()，这三个方法必须使用在同步代码块或同步方法中，三个方法的调用者必须是同步代码块或同步方法中的同步监视器

**sleep()和wait()的异同**

相同点：一旦执行方法，都可以使得当前线程进入阻塞状态

不同点：两个方法声明的位置不同：Thread类中声明sleep()，Object类中声明wait()，调用的要求不同，sleep()可以在任何需要的场景下调用，wait()必须使用在同步代码块或同步方法中，如果两个方法都使用在同步代码块或同步方法中，sleep()不会释放锁，wait()会释放锁



### 常用类

通过字面量的方式给一个字符串赋值，此时的字符串值声明在字符串常量池中

字符串常量池中不会存储相同内容的字符串

**String的不可变性**

1. 当对字符串重新赋值时，需要重新指定内存区域赋值，不能使用原有的value进行赋值
2. 当对现有的字符串进行连接操作时，也需要重新指定内存区域赋值，不能使用原有的value进行赋值
3. 当调用string的replace方法修改指定字符或字符串时，也需要重新指定内存区域赋值



**String的实例化方式**

1. 通过字面量的方式：字符串声明在方法去中的字符串常量池中
2. 通过new+构造器的方式：数据在堆空间中开辟空间以后对应的地址值



常量与常量的拼接结果在常量池。且常量池中不会存在相同内容的常量。// "a"+"b"

只要其中有一个是变量，结果就在堆中   //"a"+s1

如果拼接的结果调用intern()方法，返回值就在常量池中



**String、StringBuffer、StringBuilder**

String：不可变字符串，底层使用char[]存储

StringBuffer：可变的字符序列，线程安全的，效率低、底层使用char[]存储，创建了一个长度是16的数组，默认扩容为原来的**2倍+2**

StringBuilder：可变的字符序列、线程不安全、效率高、底层使用char[]存储

效率：StringBuilder > StringBuffer > String



**Coparable接口的使用（自然排序）**

1. 像String、包装类等实现了Comparable接口，重写了compareTo(obj)方法，给出了比较两个对象大小的方式

2. 重写compareTo(obj)的规则：

   如果当前对象this大于形参对象obj，则返回正整数，如果当前对象this小于形参对象obj，则返回负整数，如果当前对象this等于形参对象obj，则返回零

3. 对于自定义类来说，如果需要排序，我们可以让自定义类实现Comparable接口，重写CompareTo(obj)方法，在compareTo(obj)中指明如何排序

**Comparator（定制排序）**

1. 当元素的类型没有实现java,lang.Comparable接口而又不方便修改代码，或者实现了java.lang.Comparable接口的排序规则不适合当前的操作。那么可以考虑使用Comparator的对象来排序，强行对多个对象进行整体排序比较

2. 重写compare(Object o1,Object o2)方法

### 枚举类与注解

枚举类的使用

1. 枚举类的理解：类的对象只有有限个、确定的。我们称此为枚举类
2. 当需要定义一组常量时，强烈建议使用枚举类
3. 如果枚举类中只有一个对象，可以作为单例模式的实现方式



如何定义枚举类

1. jdk5.0之前，自定义枚举类

   ```java
   class Season{
       //1.声明Season对象的属性：private final 修饰
       private final String seasonName;
       private final String seasonDesc;
       //2.私有化类的构造器，并给对象属性赋值
       private Season(String seasonName,String seasonDesc){
           this.seasonName=seasonName;
           this.seasonDesc=seasonDesc;
       }
       //3.提供当前枚举类的多个对象
       public static final Season SPRING=new Season("春天","春暖花开");
       public static final Season SPRING1=new Season("春天","春暖花开");
       public static final Season SPRING2=new Season("春天","春暖花开");
       //4.其他诉求1：获取枚举类对象的属性
       public String getSeasonName(){
           return seasonName;
       }
       public String getSeasonDesc(){
           return seasonDesc;
       }
       //4.其他诉求2：提供toString()
       
   }
   ```

2. jdk5.0,可以使用Enum关键字定义枚举类

   ```java
   //说明：定义的枚举类默认继承于java.lang.Enum类
   enum Season{
      //1.提供当前枚举类的对象，多个对象之间用","隔开，末尾对象”;“结束
      SPRING("春天","春暖花开"),
      SPRING1("春天","春暖花开"),
      SPRING2("春天","春暖花开");
       
   }
   ```



使用enum关键字定义的枚举类实现接口的情况

1. 实现接口，在enum类中实现抽象方法

2. 让枚举类的对象分别去实现接口中的抽象方法

   ```java
   enum Season implements 接口{
      SPRING("春天","春暖花开"){
          //实现接口中的方法
      },
      SPRING1("春天","春暖花开"),
      SPRING2("春天","春暖花开");
       
   }
   ```



**注解（Annotation）**

jdk内置的3个注解

@Override：限定重写父类方法，该注解只用于方法

@Deprecated：用于表示所修饰的元素已过时

@SuppressWarnings：抑制编译器警告

**自定义注解**

+ 定义新的Annotation类型使用@interface关键字
+ 自定义注解自动继承了java.lang.annotation.Annotation接口
+ Annotation的成员变量在Annotation定义中以无参方法的形式来声明。其方法名和返回值定义了该成员的名字和类型。我们称为配置参数。类型只能是八种基本数据类型、String类型、Class类型、enum类型、Annotation类型、以上所有类型的数组
+ 可以在定义Annotation的成员变量时为其指定初始值，指定成员变量的初始值可以使用default关键字
+ 如果只有一个参数成员，建议使用参数名为value
+ 如果定义的注解含有配置参数，那么使用时必须指定参数值，除非他有默认值。格式是"参数名 = 参数值"，如果只有一个参数成员，且名字为value可以省略"value="
+ 没有成员定义的Annotation称为标记；包含成员变量的Annotation称为元数据Annotation

jdk提供的4个元注解

**@Retension：**指定所修饰的Annotation的生命周期：@Rentention包含一个RetentionPolicy类型的成员变量：SOURCE / CLASS(默认行为) / RUNTIME，只有声明为RUNTIME生命周期的注解，才能通过反射获取。

+ RetentionPolicy.SOURCE：在源文件中有效（即源文件保留），编译器直接丢掉这种策略的注释
+ RetentionPolicy.CLASS：在class文件中有效（即class保留），当运行java程序时，JVM不会保留注释。这是默认值
+ RetentionPolicy.RUNTIME：在运行时有效（即运行时保留），当运行Java程序时，JVM会保留注释。程序可以通过反射获取该注释

**@Target：**用于指定被修饰的Annotation能用于修饰哪些程序元素

**@Documented：**表示所修饰的注解在被javadoc解析时，保留下来

**@Inherited：**被他修饰的Annotation将具有继承性。如果某个类使用了被@Inherited修饰的Annotaion，则其子类将自动具有该注解



**jdk8新特性**

可重复注解：在注解上声明@Repeatable

类型注解：@Target中

ElementType.TYPE_PARAMETER  表示该注解能写在类型变量的声明语句中（如：泛型声明）

ElementType.TYPE_USE  表示该注解能写在使用类型的任何语句中



### 集合

数组在存储多个数据方面的特点：

优点：一旦初始化以后，其长度就确定了，数组一旦定义好，其元素类型也就确定了，我们只能操作指定类型的数据

缺点：一旦初始化，长度不可修改，数组中提供的方法非常有限，对于添加、删除、插入数据等操作非常不便，同时效率不高，获取数组中实际元素个数的需求，数组没有现成的属性或方法可用，对于无序、不可重复的需求，不能满足



集合框架

Collection接口：单列集合，用来存储一个一个的对象

向Collection接口的实现类的对象中添加数据obj时，要求obj所在类要重写equals()

+ List接口：存储有序的、可重复的数据

  + ArrayList：线程不安全，效率高，底层使用Object[]存储
  + LinkedList：底层使用双向链表存储，对于频繁的插入删除操作，使用此类效率比ArrayList高
  + Vector：线程安全，效率低，底层使用Object[]存储

  ArrayList、LinkedList、Vector三者的异同

  相同点：三个类都实现了List接口，存储数据的特点相同；存储有序的、可重复的数据

  不同点：如上

  ```java
  //ArrayList源码分析
  //jdk7情况下
  ArrayList list=new ArrayList();//底层创建了长度是10的Object[]数组
  list.add(11);//如果此次的添加导致底层elementData数组容量不够，则扩容，默认情况下，扩容为原来容量的1.5倍，同时需要将原有数组中的数据复制到新的数组中
  //建议开发中去使用带参的构造器：ArrayList list=new ArrayList(int capacity)
  
  //jdk8情况下
  ArrayList list=new ArrayList();//底层Object[] elementData初始化为{}，并没有创建长度为10的数组
  list.add(123);//第一次调用add()时，底层才创建了长度10的数组，并将数据123添加到elementData中
  ```

+ Set接口：存储无序的、不可重复的数据

  以HashSet为例说明：
  
  1. 无序性：不等于随机性。存储的数据在底层数组中并非按照数组索引的顺序添加，而是根据数据的哈希值决定的
  
  2. 不可重复性：保证添加的元素按照equals()判断时，不能返回true，即相同的元素只能添加一个
  3. 添加元素过程：我们向HashSet中添加元素a，首先调用元素a所在类的hashCode()方法，计算元素a的哈希值，此哈希值接着通过某种算法计算出在HashSet底层数组中存放的位置（即为：索引位置），判断数组此位置上是否已经有元素，如果此位置上没有其他元素，则元素a添加成功，如果此位置上有其他元素b（或以链表形式存在的多个元素），则比较元素a和元素b的hash值，如果值不相同，则元素a添加成功，如果hash值相同，进而需要调用元素a所在类的equals()方法：equals()返回true，元素a添加失败，equals()返回false，则元素添加成功
  
  要求：向set中添加的数据，其所在的类一定要重写hashCode()和equals()，重写的hashCode()和equals()尽可能保持一致性：相等的对象必须具有相等的散列码
  
  + HashSet：线程不安全的，可以存储null值，底层数组+链表+红黑树
  + LinkedHashSet：作为HashSet的子类，在添加数据的同时，每个数据还维护了两个变量，记录此数据的前一个数据和后一个数据，对于频繁的遍历操作，此类执行效率高于HashSet；遍历其内部数据时，可以按照添加的顺序遍历
  +  TreeSet：底层红黑树，可以按照添加对象的指定属性，进行排序，向TreeSet中添加的数据，要求是相同类的对象，两种排序方式：自然排序（实现Comparable接口）和定制排序（Comparator），自然排序中，比较两个对象是否相同的标准为：compareTo()返回0，不再是equals()，定制排序中，比较两个对象是否相同的标准为：compare()返回0，不再是equals()

Map接口：双列集合，用来存储一对（key-value）一对的数据

+ HashMap：底层数组+链表+红黑树，线程不安全，效率高，存储null的key和value
+ LinkedHashMap：保证在遍历map元素时时，可以按照添加的顺序实现遍历，在原有的HashMap底层结构基础上，添加了一对指针，指向前一个和后一个元素，对于频繁的遍历操作，此类执行效率高于HashMap
+ TreeMap：底层红黑树，保证按照添加的key-value对进行排序，实现排序遍历，此时考虑key的自然排序和定制排序
+ HashTable：底层数组+链表+红黑树，线程安全，效率低，不能存储null的key和value
+ Properties：常用来处理配置文件。key和value都是String类型



Map结构的理解

+ Map中的key：无序的、不可重复的，使用Set存储所有的key ---> key所在的类要重写equals()和hashCode()方法（以HashMap为例）
+ Map中的value：无序的、可重复的，使用Collection存储所有的value ---> value所在的类要重写equals()
+ 一个键值对：key-value构成了一个Entry对象。
+ Map中的entry：无序的、不可重复的，使用Set存储所有的entry



HashMap的底层实现原理

```java
HashMap map=new HashMap();//在实例化之后，底层创建了长度是16的一维数组Entry[] table
map.put(k1,v1);
//首先，调用k1所在类的hashCode()计算k1哈希值，此哈希值经过某种算法计算以后，得到在Entry数组中的存放位置。如果此位置上的数据为空，此时的k1-v1添加成功。如果此位置上的数据不为空，（意味着此位置上存在一个或多个数据（以链表形式存在）），比较k1和已经存在的一个或多个数据的哈希值：如果k1的哈希值于已经存在的数据的哈希值都不相同，此时k1-v1添加成功。如果k1的哈希值和已经存在的某一个数据(k2-v2)的哈希值相同，继续比较：调用k1所在类的equals(k2),如果equals()返回false:此时k1-v1添加成功，如果equals()返回true:使用v1替换v2值

//默认扩容方式，扩容为原来容量的2倍，并将原有的数据复制过来
jdk8相较于jdk7在底层实现方面的不同：
1.new HashMap():底层没有创建一个长度为16的数组
2.jdk8底层的数组是：Node[],而非Entry[]
3.首次调用put()方法时，底层创建长度为16的数组
4.jdk7底层结构只有数组+链表。jdk8中底层结构：数组+链表+红黑树。
    当数组的某一个索引位置上的元素以链表形式存在的数据个数>8且当前数组的长度>64时，
    此时此索引位置上的所有数据改为使用红黑树存储
    
    DEFAULT_INITIAL_CAPACITY:HashMap的默认容量，16
    DEFAULT_LOAD_FACTOR:HashMap的默认加载因子：0.75
    threshold:扩容的临界值，=容量*填充因子：16*0.75=12
    TREEIFY_THRESHOLD:Bucket中链表长度大于该默认值，转化为红黑树：8
    MIN_TREEIFY_CAPACITY:桶中的Node被树化时最小的hash表容量:64
```



集合元素的遍历操作

1. 使用迭代器Iterator接口

   ```java
   while(iterator.hasNext()){
   	System.out.println(iterator.next());
   }
   ```

2. foreach

   ```java
   for(Object obj:集合对象){
   	System.out.println(obj);
   }
   ```

   

### 泛型

**泛型的概念**

所谓泛型，就是允许在定义类、接口时通过一个标识表示类中某个属性的类型或者是某个方法的返回值及参数类型。这个类型参数将在使用时（例如，继承或实现这个接口，用这个类型声明变量、创建对象时）确定（即传入实际的类型参数，也称为类型实参）



静态方法中不能使用类的泛型，异常类不能是泛型的

泛型方法可以声明为静态的，原因：泛型参数是在调用方法时确定的。并非在实例化类时确定

```java
//泛型类
public class Order <T>{
    private String orderName;
    private int orderId;

    //类的内部结构就可以使用类的泛型
    private T orderT;

    public Order(){
        //编译不通过
       // T[] arr = new T[5];

        //编译通过
        T[] arr = (T[])new Object[5];
    }
}

//泛型接口
public interface Collection<E> extends Iterable<E>{
}

//泛型方法
public <E> List<E> copyFromArrayToList(E[] arr){
    ArrayList<E> list=new ArrayList<>();
    for(E e:arr){
        list.add(e);
    }
    return list;
}
```

+ 泛型类可能有多个参数，此时应将多个参数一起放在尖括号内。比如：<E1,E2,E3>
+ 泛型类的构造器如下：public GenericClass(){}，而public GenericClass<E>(){}是错误的
+ 实例化后，操作原来泛型位置的结构必须与指定的泛型类型一致
+ 泛型不同的引用不能相互赋值
+ 静态方法中不能使用类的泛型，异常类不能是泛型的
+ 不能使用new E[]

对于List<?>不能向其内部添加数据，除了null之外，允许读取数据，读取的数据类型为Object

有限制条件的通配符的使用

？ extends A：G<？ extends A> 可以作为G<A>和G<B>的父类，其中B是A的子类

？ super A：G<？ super A> 可以作为G<A>和G<B>的父类，其中B是A的父类



### BIO、NIO、AIO

https://blog.csdn.net/qq_40378034/article/details/119710529

Java共支持3种网络编程的I/O模型：BIO、NIO、AIO

BIO：同步并阻塞（传统阻塞型），服务器实现模式为一个连接一个线程，即客户端有连接请求时服务器端就需要启动一个线程进行处理，如果这个连接不做任何事情会造成不必要的线程开销

NIO：同步非阻塞，服务器实现模式为一个线程处理多个请求（连接），即客户端发送的连接请求都会注册到多路复用器上，多路复用器轮询到连接有I/O请求就进行处理

AIO：异步非阻塞，服务器实现模式为一个有效请求一个线程，客户端的I/O请求都是由操作系统先完成了再通知服务器应用去启动线程进行处理，一般适用于连接数较多且连接时间较长的应用

#### BIO

流的分类

按操作数据单位不同分为：字节流（8bit）二进制文件，字符流（按字符）文本文件

按数据流的流向不同分为：输入流，输出流

按流的角色的不同分为：节点流，处理流（包装流）

对于文本文件（.txt，.java，.c，.cpp），使用字符流处理

对于非文本文件（.jpg，.mp3，.mp4，.avi，.doc，.ppt，...），使用字节流处理

节点流可以从一个特定的数据源读写数据，如FileReader、FileWriter

处理流（也叫包装流）是连接在已存在的流（节点流或处理流）之上，为程序提供更为强大的读写功能，如BufferedReader、BufferedWritter	

| 分类                       | 字节输入流              | 字节输出流               | 字符输入流            | 字符输出流             |
| -------------------------- | ----------------------- | ------------------------ | --------------------- | ---------------------- |
| **抽象基类**               | **InputStream**         | **OutputStream**         | **Reader**            | **Writer**             |
| **访问文件**               | **FileInputStream**     | **FileOutputStream**     | **FileReader**        | **FileWriter**         |
| **访问数组**               | ByteArrayInputStream    | ByteArrayOutputStream    | CharArrayReader       | CharArrayWriter        |
| **访问管道**               | PipedInputStream        | PipedOutputStream        | PipedReader           | PipedWriter            |
| **访问字符串**             |                         |                          | StringReader          | StringWriter           |
| **缓冲流（以下为处理流）** | **BufferedInputStream** | **BufferedOutputStream** | **BufferedReader**    | **BufferedWriter**     |
| **转换流**                 |                         |                          | **InputStreamReader** | **OutputStreamWriter** |
| **对象流**                 | **ObjectInputStream**   | **ObjectOutputStream**   |                       |                        |
| **抽象基类**               | FilterInputStream       | FilterOutputStream       | FilterReader          | FilterWriter           |
| **打印流**                 |                         | PrintStream              |                       | PrintWriter            |
| **推回输入流**             | PushbackInputStream     |                          | PushbackReader        |                        |
| **特殊流（数据流）**       | DataInputStream         | DataOutputStream         |                       |                        |

**InputStream：字节输入流**

常用子类：

1. FileInputStream：文件输入流
2. BufferedInputStream：缓冲字节输入流
3. ObjectInputStream：对象字节输入流

**处理流之一：缓冲流的使用**

作用：提高流的读取、写入的速度

提高读写速度的原因：内部提供了一个缓冲区

```java
//1.造文件
File srcFile=new File("a.jpg");
File destFile=new File("b.jpg");
//2.造流
//2.1 造节点流
FileInputStream fis=new FileInputStream(srcFile);
FileOutputStream fos=new FileOutputStream(destFile);
//2.2 造缓冲流
BufferedInputStream bis=new BufferedInputStream(fis);
BufferedOutputStream bos=new BufferedOutputStream(fos);
//3.复制的细节：读取、写入
byte[] buffer=new byte[10];
int len;
while((len=bis.read(buffer))!=-1){
    bos.write(buffer,0,len);
}
//4.资源关闭
//要求：先关外层的流，再关内层的流
bos.close();
bis.close();
//说明：关闭外层流的同时，内层流也会自动的进行关闭。关于内层流的关闭，我们可以忽略。
//fos.close();
//fis.close();
```



**处理流之二：转换流的使用**

转换流属于字符流

InputStreamReader：将一个字节的输入流转换为字符的输入流

OutputStreamWriter：将一个字符的输出流转换为字节的输出流

作用：提供字节流和字符流之间的转换

解码：字节、字节数组 ---> 字符数组、字符串

编码：字符数组、字符串 ---> 字节、字节数组

```java
//使用InputStreamReader
FileInputStream fis=new FileInputStream("a.txt");
//InputStreamReader isr=new InputStreamReader(fis);//使用系统默认的字符集
//参数2指明了字符集，具体使用哪个字符集，取决于文件a.txt保存时使用的字符集
InputStreamReader isr=new InputStreamReader(fis,"UTF-8");

char[] cbuf=new char[20]
int len;
while((len=isr.read(cbuf))!=-1){
    String str=new String(cbuf,0,len);
    System.out.print(str);
}
isr.close();

//综合使用InputStreamReader和OutputStreamWriter
File file1=new File("a.jpg");
File file2=new File("b_gbk.jpg");

FileInputStream fis=new FileInputStream(file1);
FileOutputStream fos=new FileOutputStream(file2);

InputStreamReader isr=new InputStreamReader(fis,"utf-8");
OutputStreamWriter osr=new OutputStreamWriter(fos,"gbk")
    
char[] cbuf=new char[20]
int len;
while((len=isr.read(cbuf))!=-1){
    osw.write(cbuf,0,len);
}
isr.close();
osw.close();
```



其他流的使用

1. 标准的输入输出流

   System.in：标准的输入流，默认从键盘输入

   System.out：标准的输出流，默认从控制台输出

   System类的setIn(InputStream is)  /  setOut(PrintStream ps)方式重新指定输入和输出的流。

   

2. 打印流

   实现将基本数据类型的数据格式转化为字符串输出

   ```java
   PrintStream ps=null;
   FileOutoutStream fos=new FileOutputStream(new File("D:\\io\\text.txt"));
   //创建打印输出流，设置为自动刷新模式（写入换行符或字节'\n'时都会刷新输出缓冲区）
   ps=new PrintStream(fos,true);
   if(ps!=null){//把标准输出流（控制台输出）改为文件
       System.setOut(ps);
   }
   for(int i=0;i<=255;i++){//输出ASCII字符
       System.out.print((char)i);
       if(i%50==0){//每50个数据一行
           System.out.println();//换行
       }
   }
   ```

3. 数据流

   作用：用于读取或写出基本数据类型的变量或字符串

   ``` java
   //将内存中的字符串、基本数据类型的变量写出到文件中
   DataOutputStream dos=new DataOutputStream(new FileOutputStream("data.txt"));
   
   dos.writeUTF("张三");
   dos.flush();//刷新操作，将内存中的数据写入文件
   dos.writeInt(23);
   dos.flush();
   dos.writeBoolean(true);
   dos.flush();
   dos.close();
   //将文件中存储的基本数据类型变量和字符串读取到内存中，保存在变量中
   //注意点：读取不同类型的数据的顺序要与当初写入文件时，保存的数据的顺序一致
   DataInputStream dis=new DataInputStream(new FileInputStream("data.txt"));
   
   String name=dis.readUTF();
   int age=dis.readInt();
   boolean isMale=dis.readBoolean();
   
   dis.close();
   ```

4. 对象流

   用于存储和读取基本数据类型数据或对象的处理流。他的强大之处就是可以把Java中的对象写入到数据源中，也能把对象从数据源中还原出来

   ObjectOutputStream和ObjectInputStream不能序列化static和transient修饰的成员

5. 随机存取文件流（RandomAccessFile）可用于实现断点续传

   构造器

   public RandomAccessFile(File file,String mode)

   public RandomAccessFile(File name,String mode)

   创建RandomAccessFile类实例需要指定一个mode参数，该参数指定RandomAccessFile的访问模式；

   r: 以只读方式打开

   rw: 打开以便读取和写入

   rwd: 打开以便读取和写入；同步文件内容的更新

   rws: 打开以便读取和写入；同步文件内容和元数据的更新

   如果模式为只读r，则不会创建文件，而是会去读取一个已经存在的文件，如果读取的文件不存在会出现异常。如果模式为rw读写，如果文件不存在则会去创建文件，如果存在则不会创建

   + RandomAccessFile声明在java.io包下，但直接继承于java.lang.Object类，并且它实现了DataInput、DataOutput这两个接口，也就意味着这个类既可以读也可以写
   + RandomAccessFile支持随机访问的方式，程序可以直接跳到文件的任意地方来读写文件
   + RandomAccessFile对象包含一个记录指针，用以标示当前读写处的位置。RandomAccessFile类对象可以自由移动记录指针，**getFilePointer()**:获取文件记录指针的当前位置，**seek(long pos)**:将文件记录指针定位到pos位置

   

**对象的序列化**

对象序列化机制允许把内存中的Java对象转换为平台无关的二进制流，从而允许把这种二进制流持久地保存在磁盘上，或通过网络将这种二进制流传输到另一个网络节点。当其他程序获取了这种二进制流，就可以恢复成原来的Java对象



**类需要满足以下的要求，方可序列化**

1. 需要实现接口：Serializable
2. 当前类提供一个全局常量：serialVersionUID
3. 除了当前类需要实现Serializable接口之外，还必须保证其内部所有属性也必须是可序列化的。（默认情况下， 基本数据类型可序列化）



#### **NIO**

NIO支持面向缓冲区的、基于通道的IO操作，NIO将以更加高效的方式进行文件的读写操作

**阻塞IO**

通常在进行同步IO操作时，如果读取数，代码会阻塞直至有可供读取的数据。同样写入调用将会阻塞直至数据能够写入。传统的Server/Client模式会基于TPR（Thread Per Request），服务器会为每个客户端请求建立一个线程，有该线程单独负责处理一个客户请求

**非阻塞IO**

NIO中非阻塞IO采用了基于Reactor模式的工作方式，IO调用不会被阻塞，相反是注册感兴趣的特定IO事件，如可读数据到达，新的套接字连接等等，在发生特定事件时，系统再通知我们。NIO中实现非阻塞IO的核心对象就是Selector

javaNIO由以下几个核心部分组成：

+ Channels
+ Buffers
+ Selectors

##### **Channel**

通道，Channel和IO中的Stream是差不多一个等级的。只不过Stream是单向的，Channel是双向的，既可以用来进行读操作，又可以用来进行写操作

NIO中Channel的主要实现有：FileChannel、DatagramChannel、SocketChannel、ServerSocketChannel，分别对应文件IO、UDP、TCP（Server和Client）

+ FileChannel从文件中读写数据
+ DatagramChannel能通过UDP读写网络中的数据
+ SocketChannel能通过TCP读写网络中的数据
+ ServerSocketChannel可以监听新进来的TCP连接，像web服务器那样，对每一个新进来的连接都会创建一个SocketChannel

**FileChannel**

```java
//FileChannel读取数据到buffer中
public static void main(String[] args) {
    //创建FileChannel
    RandomAccessFile aFile = new RandomAccessFile("d:\\atguigu\\01.txt","rw");
    FileChannel channel = aFile.getChannel();
    //创建Buffer
    ByteBuffer buf = ByteBuffer.allocate(1024);
    //读取数据到buffer中
    int bytesRead = channel.read(buf);
    while(bytesRead != -1) {
        System.out.println("读取了："+bytesRead);
        buf.flip();
        while(buf.hasRemaining()) {
            System.out.println((char)buf.get());
        }
        buf.clear();
        bytesRead = channel.read(buf);
    }
    aFile.close();
    System.out.println("结束了");
}
```

FileChannel无法直接打开，需要通过一个InputStream、OutputStream或RandomAccessFile来获取一个FileChannel实例

```java
//FileChannel写操作
public static void main(String[] args) {
    //打开FileChannel
    RandomAccessFile aFile = new RandomAccessFile("d:\\atguigu\\01.txt","rw");
    FileChannel channel = aFlie.getChannel();
    
    //创建buffer对象
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    String newData = "date atguigu";
    buffer.clear();
    //写入内容
    buffer.put(newData.getBytes());
    buffer.flip();
    //FileChannel完成最终实现
    while(buffer.hasRemaining()) {
        channel.write(buffer);
    }
    
    //关闭
    channel.close();
}
```

**position方法：**可以调用position()方法获取FileChannel的当前位置，也可以通过调用position(long pos)方法设置FileChannel的当前位置

**size方法：**返回该实例所关联的文件的大小

**truncate方法：**截取文件。截取文件时，文件中指定长度后面的部分将被删除。如：channel.truncate(1024);

**force方法：**将通道里尚未写入磁盘的数据强制写到磁盘上，force()方法有一个boolean类型的参数，指明是否同时将文件元数据（权限信息等）写到磁盘上

**transferTo方法和transferFrom方法：**将数据从一个channel传输到另一个channel

```java
//通道之间数据的传输,transferFrom()
public static void main(String[] args) {
    RandomAccessFile aFile = new RandomAccessFile("d:\\atguigu\\01.txt","rw");
    FileChannel fromChannel = aFile.getChannel();
    
    RandomAccessFile bFile = new RandomAccessFile("d:\\atguigu\\02.txt","rw");
    FileChannel toChannel = bFile.getChannel();
    
    toChannel.transferFrom(fromChannel,0,fromChannel.size());
    aFile.close();
    bFile.close();
}
```

**Socket通道**

新的socket通道类可以运行非阻塞模式并且是可选择的

DatagramChannel和SocketChannel实现定义读写功能的接口，而ServerSocketChannel不实现。ServerSocketChannel负责监听传入的连接和创建新的SocketChannel对象，他本身不传输数据。

**SocketServerChannel**

一个基于通道的socket监听器。能够在非阻塞模式下运行，没有bind()方法，需要取出对等的socket并使用他来绑定到一个端口以开始监听连接

```java
public static void main(String[] args) {
    int port = 8888;
    ByteBuffer buffer = ByteBuffer.wrap("hello atguigu".getBytes());
    
    ServerSocketChannel ssc = ServerSocketChannel.open();
    //绑定
    ssc.socket().bind(new InetSocketAddress(port));
    //设置非阻塞模式
    ssc.configureBlocking(false);
    
    while(true) {
        System.out.println("Waiting for connections");
        SocketChannel sc = ssc.accept();
        if(sc == null) {
            System.out.println("null");
            Thread.sleep(2000);
        }else{
            System.out.println("Incoming connection from" + sc.socket().getRemoteSocketAddress());
            buffer.rewind();//指针0
            sc.write(buffer);
            sc.close();
        }
    }
}
```

**SocketChannel**

+ SocketChannel是用来连接Socket套接字
+ SocketChannel主要用途用来处理网络IO的通道
+ SocketChannel基于TCP连接传输
+ SocketChannel实现了可选择通道，可以被多路复用的

1. 对于以及存在的socket不能创建SocketChannel

2. SocketChannel中提供的open接口创建的Channel并没有进行网络连接，需要使用connet接口连接到指定地址
3. 未进行连接的SocketChannel执行IO操作时，会抛出NotYetConnectedException
4. SocketChannel支持两种IO模式：阻塞式和非阻塞式
5. SocketChannel支持异步关闭。如果SocketChannel在一个线程上阻塞，另一个线程对该SocketChannel调用shutdownInput，则阻塞的线程将返回-1，表示没有读取任何数据；如果SocketChannel在一个线程上write阻塞，另一个线程对该SocketChannel调用shutdownWrite，则写阻塞的线程将抛出AsynchronousCloseException
6. SocketChannel支持设定参数
   + SO_SNDBUF 套接字发送缓冲区大小
   + SO_RCVBUF 套接字接收缓冲区大小
   + SO_KEEPALIVE 保活连接
   + O_REUSEADDR 复用地址
   + SO_LINGER 有数据传输时延缓关闭Channel（只有在非阻塞模式下有用）
   + TCP_NODELAY 禁用Nagle算法

```java
public static void main(String[] args) {
    //创建SocketChannel，方式一
    SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("www.baidu.com",80));
    //方式二
    SocketChannel socketChannel2 = SocketChannel.open();
    socketChannel2.connect(new InetSocketAddress("www.baidu.com",80));
    
    //设置阻塞和非阻塞
    socketChannel.configureBlocking(false);
    
    ByteBuffer byteBuffer = ByteBuffer.allocate(16);
    socketChannel.read(byteBuffer);
    socketChannel.close();
    System.out.println("read over");
}
```

**DatagramChannel**

SocketChannel对应Socket，每一个DatagramChannel对象也有一个关联的DatagramSocket对象。DatagramChannel是无连接的，每个数据报都是一个自包含的实体，拥有他自己的目的地址及不依赖其他数据报的数据负载。DatagramChannel可以发送单独的数据报给不同的目的地址，同样，也可以接收来自任意地址的数据包，每个到达的数据报都含有关于他来自何处的信息（源地址）

```java
public Class DatagramChannelDemo {
    //发送的实现
    
    public void sendDatagram() {
        //打开DatagramChannel
        DatagramChannel sendChannel = DatagramChannel.open();
        InetSocketAddress sendAddress = new InetSocketAddress("127.0.0.1",9999);
        
        //发送
        while(true) {
            ByteBuffer buffer = ByteBuffer.wrap("发送atguigu".getBytes("UTF-8"));
            sendChannel.send(buffer,sendAddress);
            System.out.println("以及发送完成");
            Thread.sleep(1000);
        }
    }
    
    //接收的实现
    public void receiveDatagram() {
        DatagramChannel receiveChannel = DatagramChannel.open();
        InetSocketAddress receiveAddress = new InetSocketAddress(9999);
        
        //绑定
        receiveChannel.bind(receiveAddress);
        
        //buffer
        ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);
        
        //接收
        while(true) {
            receiveBuffer.clear();
            SocketAddress socketAddress = receiveChannel.receive(receiveBuffer);
            
            receiveBuffer.flip();
            System.out.println(socketAddress.toString());
            System.out.println(Charset.forName("UTF-8").decode(receiveBuffer));
        }
      
    }
    
    //连接 read和write
    public void testConnect() {
        DatagramChannel connChannel = DatagramChannel.open();
        connChannel.bind(new InetSocketAddress(9999));
        
        //连接
        connChannel.connect(new InetSocketAddress("127.0.0.1",9999));
        
        connChannel.write(ByteBuffer.wrap("发送atguigu".getBytes("UTF-8")));
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        
        while(true) {
            connChannel.read(readBuffer);
        }
    }
}
```

**Scatter/Gather**

javaNIO开始支持scatter/gather，scatter/gather用于描述从Channel中读取或者写入到Channel的操作

**分散（scatter）**从Channel中读取是指在读操作时将读取的数据写入多个buffer中。因此，Channel将从Channel中读取的数据"分散（scatter）"到多个buffer中

**聚集（gather）**写入Channel是指在写操作时将多个buffer的数据写入到同一个Channel，因此，Channel将多个buffer中的数据"聚集（gather）"后发送到Channel.

scatter/gather经常用于需要将传输的数据分开处理的场合，例如传输一个由消息头和消息体组成的消息，你可能会将消息头和消息体分散到不同的buffer中，这样你可以方便的处理消息头和消息体

##### **Buffer**

NIO中的关键Buffer实现有：ByteBuffer、CharBuffer、DoubleBuffer、FloatBuffer、IntBuffer、LongBuffer、ShortBuffer

使用Buffer读写数据，一般遵循以下四个步骤

1. 写入数据到Buffer
2. 调用flip()方法
3. 从Buffer中读取数据
4. 调用clear()方法或compact()方法

Buffer三个属性：

+ capacity
+ position
  + 写数据到Buffer中时，position表示写入数据的当前位置，position的初始值为0。position最大可为capacity-1
  + 读数据到Buffer中时，position表示读入数据的当前位置。通过ByteBuffer.flip()切换到读模式时position会被重置为0
+ limit
  + 写数据时，limit表示可对Buffer最多写入多少个数据。写模式下limit等于Buffer的capacity
  + 读数据时，limit表示Buffer里有多少可读数据，因此能读到之前写入的所有数据（limit被设置成已读数据的数量，这个值在写模式下就是position）

**Buffer分配：**allocate()方法

**向Buffer中写数据：**

1. 从Channel写到Buffer：channel.read(buffer)
2. 通过Buffer的put()方法写到Buffer里：buffer.put(127)

**flip()方法：**将写模式切换到读模式。调用flip方法会将position设为0，并将limit设置成之前position的值

**从Buffer中读数据：**

1. 从Buffer读取数据到Channel：channel.write(buffer)
2. 通过get()方法从Buffer中读取数据：buffer.get()

**Buffer的几个方法**

1. **rewind()方法：**将position设为0，所以可以重读Buffer中的数据。limit保持不变，仍然表示能从Buffer中读取多少个元素
2. **clear()与compact()方法：**clear清空buffer，compact清除读过的数据
3. **mark()与reset()方法：**mark标记Buffer中一个特定的position，之后通过调用Buffer.reset()方法恢复到这个position

**缓冲区操作**

1. **缓冲区分片**

   在NIO中，除了可以分配或者包装一个缓冲区对象以外，还可以根据现有的缓冲区对象来创建一个子缓冲区，即在现有缓冲区上切出一片来作为一个新的缓冲区，但现有的缓冲区与创建的子缓冲区在底层数组层面上是数据共享的，也就是说，子缓冲区相当于是现有缓冲区的一个视图窗口。调用slice()方法可以创建一个子缓冲区

2. **只读缓冲区**

   只能读取，不能写入，可以通过调用缓冲区的asReadOnlyBuffer()方法，将任何常规缓冲区转换为只读缓冲区，这个方法返回一个与原缓冲区完全相同的缓冲区，并与原缓冲区共享数据，只不过它是只读的。如果原缓冲区的内容发生了变化，只读缓冲区的内容也随之发生变化

3. **直接缓冲区**

   直接缓冲区是为加快IO速度，使用一种特殊方式为其分配内存的缓冲区，JDK文档中的描述为：给定一个直接字节缓冲区，Java虚拟机会尽最大努力直接对他执行本机IO操作，也就是说，他会在每一次调用底层操作系统的本机IO操作之前（或之后），尝试避免将缓冲区的内容拷贝到一个中间缓冲区中，或者从一个中间缓冲区中拷贝数据。要分配直接缓冲区，需要调用allocateDirect()方法

4. **内存映射文件IO**

   内存映射文件IO是一种读和写文件数据的方法，它可以比常规的基于流或基于通道的IO快的多。内存映射文件IO是通过使文件中的数据出现为内存数组的内容来完成的，一般来说，只有文件中实际读取或者写入的部分才会映射到内存中。channel.map()

##### **Selector**

一般称为选择器，也可以翻译为多路复用器。它是Java NIO核心组件中的一个，用于检查一个或多个NIO Channel的状态是否处于可读、可写。Selector运行单线程处理多个Channel，也就是可以管理多个网络链接

**可选择通道（SelectableChannel）**

不是所有的Channel都可以被Selector复用的。比方说，FileChannel就不能被选择器复用。判断一个Channel能被Selector复用，有一个前提：判断它是否继承了一个抽象类SelectableChannel，如果继承了就可以被复用

**Channel注册到Selector**

1. 使用Channel.register(Selector sel, int ops)方法，将一个通道注册到一个选择器时。第一个参数，指定通道要注册的选择器。第二个参数指定选择器需要查询的通道操作

2. 可以供选择器查询的通道操作，从类型分，包括以下四种：

   + 可读：SelectionKey.OP_READ
   + 可写：SelectionKey.OP_WRITE
   + 连接：SelectionKey.OP_CONNECT
   + 接收：SelectionKey.OP_ACCEPT

   如果Selector对通道的多操作感兴趣，可以使用位或操作符来实现：

   比如 int key = SelectionKey.OP_READ | SelectionKey.OP_WRITE

**选择键（SelectionKey）**

1. Channel注册到后，并且一旦通道处于某种就绪状态，就可以被选择器查询到。这个工作，使用选择器Selector的select()方法完成。select方法的作用，对感兴趣的通道操作，进行就绪状态的查询
2. Selector可以不断的查询Channel中发生的操作的就绪状态。并且挑选感兴趣的操作就绪状态。一旦通道有操作的就绪状态达成，并且是Selector感兴趣的操作，就会被Selector选中，放入选择键集合中
3. 一个选择键，首先是包含了注册在Selector的通道操作的类型，比分说SelectorKey.OP_READ。也包含了特定的通道与特定的选择器之间的注册关系。开发应用程序时，选择键是编程的关键。NIO的编程就是根据对应的选择键，进行不同业务逻辑的处理
4. 选择键的概念，和事件的概念比较相似。一个选择键类似监听器模式里面的一个事件。由于Selector不是事件触发的模式，而是主动去查询的模式，所以不叫事件，而是叫SelectionKey选择键

**Selector的使用方法**

1. Selector的创建

   Selector.open()方法创建一个Selector对象

2. 注册Channel到Selector中

   + 与Selector一起使用时，Channel必须处于非阻塞模式下，否则将抛出异常IllegalBlockingModeException。这意味着，FileChannel不能与Selector一起使用，因为FileChannel不能切换到非阻塞模式，而套接字相关的所有通道都可以
   + 一个通道，并没有一定要支持所有的四种操作。比如服务器通道ServerSocketChannel支持Accept操作，而SocketChannel客户端通道则不支持。可以通过通道上的validOps()方法，来获取特定通道下所有支持的操作集合

   ```java
   public static void main(String[] args) {
       //创建Selector
       Selector selector = Selector.open();
       
       //通道
   	ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
       
       //非阻塞
   	serverSocketChannel.configureBlocking(false);
       
       //绑定连接
       serverSocketChannel.bind(new InetSocketAddress(9999));
       
       //将通道注册到选择器上
       serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
       
       //查询已经就绪通道操作
       Set<SelectionKey> selectionKeys = selector.selectdKeys();
       Iterator<SelectionKey> iterator = selectionKeys.iterator();
       while(iterator.hasNext()) {
           SelectionKey key = iterator.next();
           if(key.isAcceptable()){
               
           }else if(key.isConnectable){
               
           }else if(key.isReadable){
               
           }else if(key.isWritable){
               
           }
           iterator.remove();
       }
   }
   ```

3. 轮询查询就绪操作

   + 通过Selector的select()方法，就可以查询出已经就绪的通道操作，这些就绪的状态集合，保存在一个元素时SelectionKey对象的Set集合中
   + 下面是Selector几个重载的查询select方法：
     + select()：阻塞到至少有一个通道在你注册的事件上就绪了
     + select(long timeout)：和select一样，但最长阻塞事件为timeout毫秒
     + selectNow()：非阻塞，只要有通道就绪就立即返回

4. 停止选择的方法

   选择器执行选择的过程，系统底层会依次询问每个通道是否已经就绪，这个过程可能会造成调用线程进入阻塞状态，那么我们有以下三种方式可以唤醒在select方法中阻塞的线程

   + wakeup()方法：通过调用Selector对象的wakeup()方法让处在阻塞状态的select方法立刻返回，该方法使得选择器上第一个还没有返回的选择操作立即返回，如果当前没有进行中的选择操作，那么下一次对select方法的第一次调用将立即返回
   + close()方法：通过close关闭Selector，该方法使任何一个在选择操作中阻塞的线程被唤醒，同时使得注册到该Selector的所有Channel被注销，所有的键将被取消，但是Channel本身不会关闭

**NIO编程步骤**

1. 创建ServerSocketChannel通道，绑定监听端口
2. 设置通道是非阻塞模式
3. 创建Selector选择器
4. 把Channel注册到Selector选择器上，监听连接事件
5. 调用Selector的select方法（循环调用），监听通道的就绪状态
6. 调用selectKeys方法获取就绪channel集合
7. 便利就绪channel集合，判断就绪事件类型，实现具体的业务操作
8. 根据业务，是否需要再次注册监听事件，重复执行第三步操作 

##### **Pipe**

java NIO管道是2个线程之间的单向数据连接。Pipe有一个source通道和一个sink通道。数据会被写到sink通道，从source通道读取。

1. .创建管道：通过Pipe.open()打开管道，Pipe pipe = Pipe.open()

2. 写入管道：要向管道写数据，需要访问sink通道

   Pipe.SinkChannel sinkChannel = pipe.sink()

   通过调用SinkChannel的write方法将数据写入SinkChannel

3. 从管道读取数据：从管道读取数据，需要访问source通道

   Pipe.SourceChannel sourceChannel = pipe.source()

   通过调用SourceChannel的read方法来读取数据

##### **FileLock**

文件锁在OS中很常见，如果多个程序同时访问修改一个文件，很容易因为文件数据不同步而出现问题。给文件加一个锁，同一时间，只能有一个程序修改此文件，或者是程序都只能读此文件

文件锁是进程级别的，不是线程级别的。文件锁可以解决多个进程并发访问、修改同一个文件的问题，但不能解决多线程并发访问、修改同一文件的问题

使用文件锁时，同一进程内的多个线程，可以同时访问、修改此文件

文件锁是当前程序所属的JVM实例持有的，一旦获取到文件锁，要调用release，或者关闭对应的FileChannel对象，或者当前JVM退出，才会释放这个锁

一旦某个进程对某个文件加锁，则在释放这个锁之前，此进程不能再对此文件加锁

**文件锁分类**

排它锁：又叫独占锁。对文件加排它锁后，该进程可以对此文件进行读写，该进程独占此文件，其他进程不能读写此文件，直到该进程释放文件锁

共享锁：某个进程对文件加共享锁，其他进程可以访问此文件，但是这些进程都只能读此文件，不能写，线程是安全的，只要还有一个线程持有共享锁，此文件就只能读，不能写

**获取文件锁的方法**

+ lock()：对整个文件加锁，默认为排它锁
+ lock(long position, long size, boolean shared)：自定义加锁方式，前两个参数指定要加锁的部分，第三个参数指定是否是共享锁
+ tryLock()：对整个文件加锁，默认为排它锁
+ tryLock(long position, long size, boolean shared)：自定义加锁方式，如果指定为共享锁，则其他进程可读此文件，使用进程均不能写此文件，如果某进程试图对此文件进行写操作，会抛出异常

lock是阻塞的，如果未获取到文件锁，会一直阻塞当前线程，直到获取文件锁

tryLock和lock的作用相同，只不过tryLock是非阻塞式的，tryLock是尝试获取文件锁，获取成功就返回锁对象，否则返回null，不会阻塞当前线程

**FileLock的两个方法**

isShared：此文件锁是否是共享锁

isValid：此文件锁是否有效

##### Path

**创建Path实例**

Path path = Paths.get();

**创建绝对路径**

`Path path = Paths.get("d:\\atguigu\\001.text")`

如果在Linux、MacOS等操作系统中

`Path path = Paths.get("/atguigu/001.text")`

**创建相对路径**

`Path path = Paths.get("d:\\atguigu","001.text")`

**Path.normalize**

Path接口的normalize()方法可以使路径标准化。标准化意味着他将移除所有在路径字符串中间的.和..代码，并解析路径字符串所引用的路径

##### Files

Java NIO Files类提供了几种操作文件系统中的文件的方法

**Files.createDirectory()**

用于根据Path实例创建一个新目录，如果该目录已经存在，则是抛出一个FileAlreadyExistsException异常，如果父目录不存在，则可能会抛出IOException

**Files.copy()**

Files.copy(sourcePath,destinationPath)方法从一个路径拷贝一个文件到另外一个目录

Files.copy(sourcePath,destinationPath,StandardCopyOption.REPLACE_EXISTING)方法的第三个参数，如果目标文件已经存在，这个参数指示copy()方法覆盖现有的文件

**Files.move()**

Files.move()方法用于将文件从一个路径移动到另一个路径。移动文件与重命名相同，但是移动文件既可以移动不同的目录，也可以在相同的操作中更改他的名字

Files.copy(sourcePath,destinationPath,StandardCopyOption.REPLACE_EXISTING)

**Files.delete()**

Files.delete()方法可以删除一个文件或者目录

**Files.walkFileTree()**

1. Files.walkFileTree()方法包含递归遍历目录树功能，将Path实例和FileVisitor作为参数。Path实例指向要遍历的目录，FileVisitor在遍历期间被调用

2. FileVisitor是一个接口，必须自己实现FileVisitor接口，并将实现的实例传递给walkFileTree()方法，如果不需要实现所有方法，可以扩展SimpleFileVisitor类，它包含FileVisitor接口中所有方法的默认实现

3. FileVisitor接口的方法中，每个都返回一个FileVisitorResult枚举实例，FileVisitorResult枚举包括以下四个选项：
   + CONTINUE 继续
   + TERMINATE 终止
   + SKIP_SIBLING 跳过同级
   + SKIP_SUBTREE 跳过子级

##### AsynchronousFileChannel

在Java7中，JavaNIO添加了AsynchronousFileChannel，也就是异步地将数据写入文件

**创建AsynchronousFileChannel**

AsynchronousFileChannel.open()

**通过Future读取数据**

```java
public void readAsyncFileChannelFuture() {
    //1.创建AsynchronousFileChannel
    Path path = Paths.get("d:\\atguigu\\001.text");
    AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path,StandardOpenOption.READ);
    
    //2.创建Buffer
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    
    //3.调用channel的read方法得到Future
    Future future = fileChannel.read(buffer,0);
    
    //4.判断是否完成 isDone,返回true
    while(!future.isDone());
    
    //5.读取数据到buffer里面
    buffer.flip();
    while(buffer.remaining()>0) {
        System.out.println(buffer.get());
    }
    buffer.clear();
}
```

**通过CompletionHandler读取数据**

```java
public void readAsyncFileChannelComplete() {
    //1.创建AsynchronousFileChannel
    Path path = Paths.get("d:\\atguigu\\001.text");
    AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path,StandardOpenOption.READ);
    
    //2.创建Buffer
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    
    //3.调用channel的read方法得到Future
    Future future = fileChannel.read(buffer,0,buffer,new CompleteHandler<Integer,ByteBuffer>() {
        public void completed(Integer result,ByteBuffer attachment) {
            System.out.println("result:" + result);
            
            attachment.flip();
            byte data = new byte[buffer.limit()];
            buffer.get(data);
            System.out.println(new String(data));
            buffer.clear();
            
        }
        
        public void failed(Throwable exc,ByteBuffer attachment) {
            
        }
    });
}
```

**通过Future写数据**

```java
public void writeAsyncFileChannelFuture() {
    //1.创建AsynchronousFileChannel
    Path path = Paths.get("d:\\atguigu\\001.text");
    AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path,StandardOpenOption.WRITE);
    
    //2.创建Buffer
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    
    buffer.put("atguigu".getBytes());
    buffer.flip();
    
    //3.调用channel的write方法得到Future
    Future future = fileChannel.write(buffer,0);
    
    //4.判断是否完成 isDone,返回true
    while(!future.isDone());
    
    //5.读取数据到buffer里面
    buffer.clear();
    System.out.println("write over");
}
```

**通过CompletionHandler写数据**

```java
public void writeAsyncFileChannelComplete() {
    //1.创建AsynchronousFileChannel
    Path path = Paths.get("d:\\atguigu\\001.text");
    AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path,StandardOpenOption.WRITE);
    
    //2.创建Buffer
    ByteBuffer buffer = ByteBuffer.allocate(1024);
    
    buffer.put("atguigu".getBytes());
    buffer.flip();
    
    //3.调用channel的read方法得到Future
    Future future = fileChannel.write(buffer,0,buffer,new CompleteHandler<Integer,ByteBuffer>() {
        public void completed(Integer result,ByteBuffer attachment) {
            System.out.println("bytes written" + result);
        }
        
        public void failed(Throwable exc,ByteBuffer attachment) {
            
        }
    });
}
```

##### **Charset**

**Charset常用静态方法**

+ public static Charset forName(String charsetName);//通过编码类型获得Charset对象
+ public static SortedMap<String,Charset> availableCharsets();//获得系统支持的所有编码方式
+ public static Charset defaultCharset();//获取虚拟机默认的编码方式
+ public static boolean isSupported(String charsetName);//判断是否支持该编码类型

**Charset常用普通方法**

+ public final String name();//获得Charset对象的编码类型
+ public abstract CharsetEncoder newEncoder();//获得编码器对象
+ public abstract CharsetDecoder newDecoder();//获取解码器对象 

```java
public static void main(String[] args) {
    //1.获取Charset对象
    Charset charset = Charset.forName("UTF-8");
    
    //2.获得编码器对象
    CharsetEncoder charsetEncoder = charset.newEncoder();
    
    //3.创建缓冲区
    CharBuffer charBuffer = CharBuffer.allocate(1024);
    charBuffer.put("atguigu");
    charBuffer.filp();
    
    //4.编码
    ByteBuffer byteBuffer = charsetEncoder.encode(charBuffer);
    for(int i = 0; i < byteBuffer.limit(); i++) {
        System.out.println(byteBuffer.get());
    }
    
    //5.获取解码器对象
    byteBuffer.flip();
    CharsetDecoder charsetDecoder = charset.newDecoder();
    
    //6.解码
    CharBuffer charBuffer1 = charsetDecoder.decode(byreBuffer);
    System.out.println("解码之后的结果：" + charBuffer.toString());
}
```



### 网络编程

网络编程的目的：直接或间接地通过网络协议与其他计算机实现数据交换，进行通讯



网络编程中有两个主要的问题：

1. 如何精准地得网络上一台或多台主机：定位主机上的特定的应用

2. 找到主机后如何可靠高效地进行数据传输

网络编程中的两个要素：

1. IP和端口号
2. 提供网络通信协议：TCP/IP参考模型（应用层，传输层，网络层，物理层+数据链路层 ）



![](img/java%E5%9F%BA%E7%A1%80_2.png)

![](img/java%E5%9F%BA%E7%A1%80_3.png)

位码即tcp标志位，有6种标示：SYN(synchronous建立联机) ACK(acknowledgement 确认) PSH(push传送) FIN(finish结束) RST(reset重置) URG(urgent紧急)Sequence number(顺序号码) Acknowledge number(确认号码)

##### InetAddress类

1. getLocalHost：获取本机InetAddress对象
2. getByName：根据指定主机名、域名获取ip地址对象
3. getHostName：获取InetAddress对象的主机名
4. getLocalAddress：获取InetAddress对象的地址

##### Socket

```java
public class SocketTCP01Server {
    public static void main(String[] args) {
        //1.在本机的9999端口监听，等待连接
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("服务端，在9999端口监听，等待连接...");
        
        //2.当没有客户端连接9999端口时，程序会阻塞，等待连接
        Socket socket = serverSocket.accept();
        
        //3.通过socket.getInputStream()读取客户端写入到数据通道的数据
        InputStream inputStream = socket.getInputStream();
        
        //4.IO读取
        byte[] buf = new byte[1024];
        int readLen = 0;
        while((readLen = inputStream.read(buf)) != -1) {
            System.out.println(new String(buf,0,readLen));
        }
        
        //5.获取socket相关联的输出流
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("hello,client".getBytes());
        //设置结束标记
        socket.shutdownOutput();
        
        //6.关闭流和socket
        outputStream.close();
        inputStream.close();
        socket.close();
        serverSocket.close();
    }
}
```



```java
public class SocketTCP01Client {
    public static void main(String[] args) {
        //1.连接服务端（ip,端口）,连接本机的9999端口
        Socket socket = new Socket(InetAddress.getLocalHost(),9999);
        
        //2.连接上后，生成Socket，通过socket.getOutputStream()得到和socket对象关联的输出流对象
        OutputStream outputStream = socket.getOutputStream();
        
        //3.通过输出流，写入数据到数据通道
        outputStream.write("hello,server".getBytes());
        //设置结束标记
        socket.shutdownOutput();
        
        //4.获取和socket关联的输入流，读取数据（字节），并显示
        InputStream inputStream = socket.getInputStream();
        byte[] buf = new byte[1024];
        int readLen = 0;
        while((readLen = inputStream.read(buf)) != -1) {
            System.out.println(new String(buf,0,readLen));
        }
        
        //5.关闭流对象和socket，必须关闭
        inputStream.close();
        outputStream.close();
        socket.close();
    }
}
```

**网络上传文件**

```java
public class TCPFileUploadServer {
    public static void main(String[] args) {
        //1.服务端在本机监听8888端口
        ServerSocket serverSocket = new ServerSocket(8888);
        //2.等待连接
        Socket socket = serverSocket.accept();
        
        //3.读取客户端发送的数据
       BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
        byte[] bytes = StreamUtils.streamToByteArray(bis);
        //4.将得到bytes数组，写入到指定的路径，就得到一个文件了
        String destFilePath = "src:\\qie2.png";
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFilePath));
        bos.write(bytes);
        bos.close();
        
        //向客户端回复"收到图片"
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream));
        writer.write("收到图片");
        writer.flush();
        socket.shutdownOutputStream();
        
        //关闭其他资源
        writer.close();
        bis.close();
        socket.close();
        serverSocket.close();
    }
}
```



```java
public class TCPFileUploadClient {
    public static void main(String[] args) {
        //客户端连接服务端
        Socket socket = new Socket(InetAddress.getLocalHost,8888);
        //创建读取磁盘文件的输入流
        String filePath = "e:\\qie.png"
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath));
        
        byte[] bytes = StreamUtils.streamToByteArray(bis);
        
        //通过socket获取到输出流，将bytes发送给服务器
        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream);
        bos.write(bytes);
        bis.close();
        socket.shutdownOutputStream();
        
        //接收从服务端回复的消息
        InputStream inputStream = socket.getInputStream();
        String s = StreamUtils.streamToString(inputStream);
        System.out.println(s);
        
        //关闭相关的流
        inputStream.close();
        bos.close();
        socket.close();
    }
}
```



```java
//StreamUtils
//将输入流转换成byte[]，即可以把文件的内容读到byte[]
public static byte[] streamToByteArray(InputStream is) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    byte[] b = new byte[1024];
    int len;
    while((len = is.read(b)) != -1) {
        bos.write(b,0,len);
    }
    byte[] array = bos.toByteArray();
    bos.close();
    return array;
}

//将输入流转为字符串
public static String streamToString(InputStream is) {
    BufferReader reader = new BufferReader(new InputStreamReader(is));
    StringBuilder builder = new StringBuilder();
    String line;
    while((line = reader.readLine()) != null) {
        builder.append(line + "\r\n")
    }
    return builder.toString();
}
```

##### netstat指令

1. netstat -an可以查看当前主机网络情况，包括端口监听情况和网络连接情况
2. netstat -an|more可以分页显示
3. 要求在dos控制台下执行
4. netstat -anb|more



**当客户端连接到服务端后，实际上客户端也是通过一个端口和服务端进行通讯的，这个端口是TCP/IP来分配的，是不确定的，是随机的**



##### UDP网络通信编程

1. 没有明确的服务端和客户端，演变成数据的发送端和接收端
2. 接收数据和发送数据是通过DatagramSocket对象完成
3. 将数据封装到DatagramPacket对象，发送
4. 当接收到DatagramPacket对象，需要进行拆包，取出数据
5. DatagramSocket可以指定在哪个端口接收数据

```java
public class UDPReceiverA {
    public static void mian(String[] args) {
        //1.创建一个DatagramSocket对象，准备在9999接收数据
        DatagramSocket socket = new DatagramSocket(9999);
        //2.构建一个DatagramPacket对象，准备接收数据
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf,buf.length());
        //3.调用接收方法,将通过网络传输的DatagramPacket对象填充到packet对象
        //如果没有数据包发送到9999端口，就会阻塞等待
        socket.receive(packet);
        //4.可以把packet进行拆包，取出数据，并显示
        int length = packet.getLength();//实际接收到的数据长度
        byte[] data = packet.getData();//接收到的数据
        String s = new String(data,0,length);
        
        //5.关闭资源
        socket.close();
    }
}
```



```java
public class UDPSenderB {
    public static void mian(String[] args) {
        //1.创建一个DatagramSocket对象，准备发送和接收数据
        DatagramSocket socket = new DatagramSocket(9998);
        //2.将需要发送的数据，封装到DatagramPacket对象中
        byte[] data = "hello 明天吃火锅".getBytes();
        DatagramPacket packet = new DatagramPacket(data,data.length(),InetAddress.getByName("192.168.12.1"),9999);
        socket.send(packet);
        socket.close();
    }
}
```

**TCP文件下载**

```java
public class Homework03Server {
    public static void main(String[] args) {
        //1.监听9999端口
        ServerSocket serverSocket = new ServerSocket(9999);
        //2.等待客户端连接
        Socket socket = serverSocket.accept();
        //3.读取 客户端发送要下载的文件名
        InputStream inputStream = socket.getInputStream();
        byte[] b = new byte[1024];
        int len = 0;
        String downLoadFileName = "";
        while((len = inputStream.read(b)) != -1) {
            downLoadFileName += new String(b,0,len);
        }
        
        String resFileName = "";
        if("高山流水".equals(downLoadFileName)) {
            resFileName = "src\\高山流水.mp3"
        }else {
            resFileName = "src\\无名.mp3"
        }
        
        //4.创建一个输入流读取文件
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(resFileName));
        
        //5.使用工具类StreamUtils,读取文件到一个字节数组中
        byte[] bytes = StreamUtils.streamToByteArray(bis);
        
        //6.得到socket关联的输出流
        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
        
        //7.写入到数据通道
        bos.write(bytes);
        socket.shutdownOutput();
        
        //8.关闭相关的资源
        bis.close();
        inputStream.close();
        socket.close();
        serverSocket.close();
    }
}
```



```java
public class Homework03Client {
    public static void main(String[] args) {
        //1.接收用户输入，指定下载文件名
        Scanner scanner = new Scannet(System.in);
        String downloadFileName = scanner.next();
        
        //2.客户端连接服务器 准备发送
        Socket socket = new Socket(InetAddress.getLocalHost(),9999);
        //3.获取和socket关联的输出流
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(downloadFileName.getBytes());
        //设置写入结束标志
        socket.shutdownOutputStream();
        
        //4.读取服务端返回的文件（字节数组）
        BufferdInputStream bis = new BufferdInputStream(socket.getInputStream());
        byte[] bytes = StreamUtils.streamToByteArray(bis);
        //5.得到一个输出流，将bytes写入到磁盘
        String filePath = "e:\\" + downloadFileName + ".mp3";
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        bos.write(bytes);
        
        //6.关闭相关的资源
        bos.close();
        bis.close();
        outputStream.close();
        socket.close();
    }
}
```



### 反射

reflection（反射）被视为动态语言的关键，反射机制允许程序在执行期借助于Reflection API取得任何类的内部信息，并能直接操作任意对象的内部属性和方法



### Java8新特性



**lambda表达式**



lambda表达式的本质：作为**函数式接口**的实例

如果一个接口中，只声明了一个抽象方法，则此接口就称为函数式接口

@FunctionalInterface可以检查是否为一个函数式接口

格式：

+ ->：lambda操作符 或 箭头操作符
+ ->左边：lambda形参列表（其实就是接口中的抽象方法的形参列表）
+ ->右边：lambda体（其实就是重写的抽象方法的方法体）

```java
//语法格式一：无参，无返回值
Runnable r1 =new Runnable(){
    public void run(){
        System.out.println("a");
    }
};
//lambda
Runnable r2=() -> {System.out.println("a");};

//语法格式二：lambda需要一个参数，但是没有返回值
Consumer<String> con=new Consumer<String>(){
  public void accept(String s){
      System.out.println(s);
  }  
};
//lambda
Consumer<String> con1=(String s) -> {System.out.println(s);};

//语法格式三：数据类型可以省略，因为可由编译器推断得出，称为"类型推断"
Consumer<String> con=new Consumer<String>(){
  public void accept(String s){
      System.out.println(s);
  }  
};
//lambda
Consumer<String> con2=(s) -> {System.out.println(s);};

//语法格式四：lambda若只需要一个参数时，参数的小括号可以省略
Consumer<String> con=new Consumer<String>(){
  public void accept(String s){
      System.out.println(s);
  }  
};
//lambda
Consumer<String> con3= s -> {System.out.println(s);};

//语法格式五：lambda需要两个或以上的参数，多条执行语句，并且可以有返回值
Comparator<Integer> com1=new Comparator<Integer>(){
  public int compare(Integer o1,Integer o2){
      	System.out.println(o1);
      	System.out.println(o2);
		return Integer.compare(o1,o2);
  }  
};
//lambda
Comparator<Integer> com2=(o1,o2) -> {
    System.out.println(o1);
    System.out.println(o2);
    return Integer.compare(o1,o2);
};

//语法格式六：当lambda体只有一条语句时，return与大括号若有，都可以省略
Comparator<Integer> com1=new Comparator<Integer>(){
  public int compare(Integer o1,Integer o2){
		return Integer.compare(o1,o2);
  }  
};
//lambda
Comparator<Integer> com2=(o1,o2) -> Integer.compare(o1,o2);
//方法引用
Comparator<Integer> com2=(o1,o2) -> Integer::compare;
```

总结：

+ ->左边：lambda形参列表的参数类型可以省略（类型推断）；如果lambda形参列表只有一个参数，其一对（）也可以省略
+ ->右边：lambda体应该使用一对{}包裹；如果lambda体只有一条执行语句（可能是return语句），可以省略一对{}和return关键字



**Java内置四大核心函数式接口**

| 函数式接口              | 参数类型 | 返回类型 | 用途                                                         |
| ----------------------- | -------- | -------- | ------------------------------------------------------------ |
| Consumer<T>消费型接口   | T        | void     | 对类型为T的对象应用操作，包含方法，void accept(T t)          |
| Supplier<T>供给型接口   | 无       | T        | 返回类型为T的对象，包含方法，T get()                         |
| Function<T,R>函数型接口 | T        | R        | 对类型为T的对象应用操作，并返回结果。结果是R类型的对象。包含方法：R apply(T t) |
| Predicate<T>断定型接口  | T        | boolean  | 确定类型为T的对象是否满足某约束，并返回boolean值。包含方法：boolean test(T t) |



**方法引用与构造器引用**



方法引用

当要传递给lambda体的操作，已经有实现的方法了，可以使用方法引用，方法引用可以看做是lambda表达式深层次的表达，换句话说，方法引用就是lambda表达式，也就是函数式接口的一个实例，通过方法的名字来指向一个方法，可以认为是lambda表达式的一个语法糖

要求：实现接口的抽象方法的参数列表和返回值类型，必须与方法引用的方法的参数列表和返回值类型保持一致

格式：使用操作符  ::  将类（或对象）与方法名分隔开来

三种主要使用情况：

+ 对象::实例方法名
+ 类::静态方法名
+ 类::实例方法名



构造器引用

和方法引用类似，函数式接口的抽象方法的形参列表和构造器的形参列表一致，抽象方法的返回值类型即为构造器所属的类的类型

```java
Supplier<Employee> sup=new Supplier<Employee>(){
    public Employee get(){
        return new Employee();
    }
}
//lambda
Supplier<Employee> sup1=() -> new Employee();
//构造器引用
Supplier<Employee> sup2=Employee::new;
```



数组引用

可以把数组看成一个特殊的类，则写法与构造器引用相似

```java
Function<Integer,String[]> fun1=length -> new String[length];
Function<Integer,String[]> fun2=String[]::new;
```



**Stream API**

Stream关注的是对数据的运算，与CPU打交道，集合关注的是数据的存储，与内存打交道

Stream自己不会存储元素

Stream不会改变源对象。相反，他们会返回一个持有结果的新Stream。

Stream操作是延迟执行的。这意味着他们会等到需要结果的时候才执行

Stream执行流程

+ Stream的实例化
+ 一系列的中间操作（过滤，映射 ...）
+ 终止操作

说明：

一个中间操作链，对数据源的数据进行处理

一旦执行终止操作，就执行中间操作链，并产生结果。之后，不会再被使用



创建Stream方式一：通过集合

+ default Stream<E> stream()：返回一个顺序流
+ default Stream<E> parallelStream()：返回一个并行流

创建Stream方式二：通过数组

Java8中的Arrays的静态方法stream()可以获取数组流

+ static <T> Stream<T> stream(T[] array)：返回一个流

创建Stream方式三：通过Stream的of()

可以调用Stream类静态方法of()，通过显示值创建一个流。它可以接收任意数量的参数

+ public static<T> Stream<T> of(T... values)：返回一个流

创建Stream方式三：创建无限流

+ 迭代：public static<T> Stream<T> iterate(final T seed,final UnaryOperator<T> f)
+ 生成：public static<T> Stream<T> generate(Supplier<T> s)



Stream的中间操作

多个中间操作可以连接起来形成一个流水线，除非流水线上触发终止操作，否则中间操作不会执行任何的处理！而在终止操作时一次性全部处理，称为"惰性求值"

1. 筛选与切片

| 方法                | 描述                                                         |
| ------------------- | ------------------------------------------------------------ |
| filter(Predicate p) | 接收lambda，从流中排除某些元素                               |
| distinct()          | 筛选，通过流所生成元素的hashCode()和equals()去除重复元素     |
| limit(long maxSize) | 截断流，使其元素不超过给定数量                               |
| skip(long n)        | 跳过元素，返回一个扔掉了前n个元素的流。若流中元素不足n个，则返回一个空流。与limit(n)互补 |

2. 映射

| 方法                             | 描述                                                         |
| -------------------------------- | ------------------------------------------------------------ |
| map(Function f)                  | 接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素 |
| map ToDouble(ToDoubleFunction f) | 接收一个函数作为参数，该函数会被应用到每个元素上，产生一个新的DoubleStream |
| mapToInt(ToIntFunction f)        | 接收一个函数作为参数，该函数会被应用到每个元素上，产生一个新的IntStream |
| mapToLong(ToLongFunction f)      | 接收一个函数作为参数，该函数会被应用到每个元素上，产生一个新的LongStream |
| flatMap(Function f)              | 接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流 |

3. 排序

| 方法                   | 描述                               |
| ---------------------- | ---------------------------------- |
| sorted()               | 产生一个新流，其中按自然顺序排序   |
| sorted(Comparator com) | 产生一个新流，其中按比较器顺序排序 |

4.匹配与查找