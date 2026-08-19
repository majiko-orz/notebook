### 基础

#### 注释

**单行注释**

python官方建议在#和注释的内容之间加一个空格，在语句和#之间加两个空格

```python
# print("这是一行被注释的代码")

print("这是一行被注释的代码")  # 注释
```

**多行注释**

三个引号开始，三个引号结束（单引号或双引号都可以）

```python
'''
print("这是一行被注释的代码")
'''

"""
print("这是一行被注释的代码")
"""
```

#### 变量

创建方式：变量名 = 值

```python
teacher = "allen"
age = 18
```

#### 数据类型

```python
# 打印数据类型
num1 = 1000
print(type(num1))

# isinstance()判断数据类型，注意在比较的过程中，如果当前类型属于某类型的子类，也会返回True
bool1 = True
print(isinstance(bool1, bool))  # True
print(isinstance(bool1, int))  # True
```

+ 数值

  + 整数（int）

  + 浮点数（float）

    ```python
    # 浮点数有精度问题，可用decimal处理
    num1 = 0.1
    num2 = 0.2
    print(num1 + num2)  # 不等于0.3
    
    from decimal import Decimal
    num3 = Decimal('0.1')
    num4 = Decimal('0.2')
    print(num3 + num4)  # 等于0.3
    
    # 科学计数法表示
    num5 = 3.14e7
    print(num5)  # 31400000.0
    ```

  + 布尔（bool）：True或False

  + 复数（complex）：主要用于数学计算

    ```python
    # a + bj ==> a是实部，b是虚部，j是虚数单位
    num2 = 3 + 4j
    num3 = 1 + 2j
    print(num2 + num3)  # 4+6j
    ```

+ 字符串（str）：引号括起来的都是字符串，可以是单引号，也可以是双引号

  ```python
  name1 = "allen"
  name2 = 'allen'
  name3 = '''allen'''
  ```

  **转义字符**

  如果字符串需要使用特殊字符，使用\转义字符

  | 转义字符 | 说明             |
  | -------- | ---------------- |
  | \\       | 反斜杠符号       |
  | \ '      | 单引号           |
  | \ "      | 双引号           |
  | \        | 在行尾作为续行符 |
  | \b       | 退格             |
  | \n       | 换行             |
  | \t       | 横向制表符       |
  | \r       | 回到行首         |

  **intern机制**

  每个字符串，不夹杂空格或者特殊符号，默认开启intern机制，共享内存、靠引用计数法决定是否销毁。相同的字符串默认只保留一份，当创建一个字符串，他会先检查内存里有没有这个字符串，如果有就不再创建新的了

  **格式化输出**

  百分号格式化占位符

  | 符号   | 说明                                        |
  | ------ | ------------------------------------------- |
  | %s     | 字符串（或任何对象，使用str()转换为字符串） |
  | %d     | 十进制整数                                  |
  | %f     | 浮点数                                      |
  | %e或%E | 科学计数法表示的浮点数（小写e或大写E）      |
  | %g或%G | 根据值的大小采用%f或%e（%G使用大写E）       |
  | %x或%X | 十六进制整数（小写或大写）                  |
  | %o     | 八进制整数                                  |
  | %c     | 字符（整数对应的ASCII字符）                 |
  | %r     | 字符串（使用repr()转换）                    |

  ```python
  name = "Alice"
  age = 25
  print("Name: %s, Age: %d" % (name, age))
  
  # str.format()方法
  # 右对齐，用指定字符填充
  print(":>10".format("hi"))
  # 左对齐，用指定字符填充
  print(":<10".format("hi"))
  # 居中对齐
  print(":^10".format("hi"))
  
  #f-string（Python 3.6+，最推荐）
  str = "hi"
  # 右对齐
  print(f"{str:>10}")
  # 左对齐
  print(f"{str:<10}")
  # 居中对齐
  print(f"{str:^10}")
  
  print(f"我叫{name},今年{age}岁")
  ```

+ 列表（list）

  一种有序、可变的数据集合，可以存储任意类型的对象

  **创建列表**

  ```python
  # 创建一个空列表
  empty_list = []
  empty_list = list()
  
  mixed = [1, "hello", True]  # 列表可以包含不同类型的元素
  
  # 使用list()构造函数从其他可迭代对象创建
  from_string = list("abc")  # ['a', 'b', 'c']
  from_range = list(range(5))  # [0, 1, 2, 3, 4]
  from_tuple = list((1, 2, 3))  # [1, 2, 3]
  ```

  **访问列表元素**

  ```python
  my_list = ["a", "b", "c", "d", "e"]
  
  # 正向索引（从0开始）
  print(my_list[0])  # 输出: "a"
  print(my_list[2])  # 输出: "c"
  
  # 负向索引（从-1开始，表示从后往前）
  print(my_list[-1])  # 输出: "e"
  print(my_list[-2])  # 输出: "d"
  
  # 切片 [start:stop:step] - 获取子列表
  print(my_list[1:3])  # 输出: ["b", "c"] 索引1到3，不包括3
  print(my_list[:3])  # 输出: ["a", "b", "c"] 从开始到索引3
  print(my_list[2:])  # 输出: ["c", "d", "e"] 从索引2到结束
  print(my_list[::2])  # 输出: ["a", "c", "e"] 步长为2
  print(my_list[::-1])  # 输出: ["e", "d", "c", "b", "a"] 反转列表
  ```

  **修改列表**

  ```python
  my_lsit = [1, 2, 3, 4]
  
  # 通过索引修改
  my_list[1] = 20
  print(my_list)  # 输出: [1, 20, 3, 4]
  
  # 通过切片修改
  my_list[1:3] = [200, 300]
  print(my_list)  # 输出: [1, 200, 300, 4]
  
  my_list[1:3] = [2000, 3000, 4000]
  print(my_list)  # 输出: [1, 2000, 3000, 4000, 4]
  ```

  **删除元素**

  ```python
  my_list = ["a", "b", "c", "d", "e", "b"]
  
  # remove() 删除第一个匹配的元素
  my_list.remove("b")
  print(my_list)  # 输出: ["a", "c", "d", "e", "b"]
  
  # pop() 删除并返回指定索引的元素（默认为最后一个）
  popped_element = my_list.pop(1)
  print(popped_element)  # 输出: "c"
  print(my_list)  # 输出: ["a", "d", "e", "b"]
  
  # del语句 按索引或切片删除
  del my_list[0]
  print(my_list)  # 输出: ["d", "e", "b"]
  
  del my_list[1:]
  print(my_list)  # 输出: ["d"]
  
  # clear() 清空列表
  my_list.clear()
  print(my_list)  # 输出: []
  ```

  **常用列表的方法**

  | 方法                         | 说明                                              |
  | ---------------------------- | ------------------------------------------------- |
  | list.insert(index,x)         | 在指定位置插入x                                   |
  | list.append(x)               | 在列表末尾追加x                                   |
  | list1.extend(list2)          | 在列表1的末尾追加列表2的数据                      |
  | del list[index]              | 删除指定位置的数据或切片                          |
  | list.remove(x)               | 删除第一次出现的x                                 |
  | list.pop(index)              | 删除指定位置的数据，默认为末尾数据                |
  | list.clear()                 | 清空列表中的元素                                  |
  | list[index] = x              | 修改指定位置的数据                                |
  | list1[start:end] = list2     | 修改列表切片的数据                                |
  | sorted(list[,reverse=True])  | 返回排序后的新列表，可选降序                      |
  | list.sort([reverse=True])    | 对列表就地排序，可选降序                          |
  | list.reverse()               | 反转列表中的元素                                  |
  | list.index(x[,start,[,end]]) | 返回x在列表中首次出现的位置，可指定起始和结束范围 |
  | list.count(x)                | 返回x的数量                                       |
  | len(list)                    | 返回列表元素的个数                                |
  | max(list)                    | 返回列表中最大值                                  |
  | min(list)                    | 返回列表中最小值                                  |
  | sum(list)                    | 返回列表中所有元素和                              |
  | list.copy()                  | 拷贝列表                                          |
  | list(x)                      | 将序列转换为列表                                  |

  ```python
  # in 运算符 检查元素是否存在
  print(3 in my_list)
  
  # + 运算符 连接列表
  combined = list1 + list2
  
  # * 运算符 重复列表
  repeated = list1 * 3
  
  # 列表推导式 基本语法 [expression for item in iterable]
  squares = [x**2 for x in range(5)]
  print(squares)  # 输出: [0, 1, 4, 9, 16]
  
  even_squares = [x**2 for x in range(10) if x % 2 == 0]
  print(even_squares)  # 输出: [0, 4, 16, 36, 64]
  
  # 浅拷贝
  copy_list = list[:]
  copy_list = list.copy()
  copy_list = list(list)
  
  # 深拷贝
  import copy
  list = [[1, 2], [3, 4]]
  copy_list = copy.deepcopy(list)
  ```

+ 元组（tuple）

  与列表类似但具有不可变性

  **元组的创建**

  ```python
  empty_tuple = ()  # 空元组
  single_tuple = (5,)  # 单元素元组（注意逗号）
  multi_tuple = (1, "hello", 3.14, True)
  mixed_tuple = (1, [2, 3], {"name": "John"})  # 可以包含可变对象
  tuple1 = tuple(i*2 for i in range(10))  # 使用推导式创建
  
  # 也可以省略括号，但不推荐
  implicit_tuple = 1, 2, 3
  ```

  **访问元组元素**

  ```python
  my_tuple = ('a', 'b', 'c', 'd', 'e')
  
  print(my_tuple[0])  # 'a'
  print(my_tuple[-1])  # 'e'
  print(my_tuple[1:4])  # ('b', 'c', 'd')
  
  # 检查成员是否为元组中元素
  print('d' in my_tuple)  # True
  ```

  **元素拼接和重复**

  ```python
  combined = tuple1 + tuple2
  repeated = tuple1 * 3
  ```

  **元组解包**

  ```python
  # 基本解包
  a, b, c = (1, 2, 3)
  print(a, b, c)  # 1 2 3
  
  # 扩展解包
  first, *middle, last = (1, 2, 3, 4, 5)
  print(first)  # 1
  print(middle)  # [2, 3, 4]
  print(last)  # 5
  
  # 交换变量
  x, y = 10, 20
  x, y = y, x
  print(x, y)  # 20 10
  ```

  **常见操作**

  ```python
  # 求元组中元素的最大值、最小值、加和
  tuple1 = (100. 200, 300, 400, 500)
  print(max(tuple1))
  print(min(tuple1))
  print(sum(tuple1))
  
  # 遍历元组
  for i in tuple1:
      print(i)
      
  for i in range(len(tuple1)):
      print(i, tuple1[i])
      
  for i,val in enumerate(tuple1):
      print(i, val)
  ```

  **元组的不可变**

  ```python
  my_tuple = (1, 2, 3)
  # my_tuple[0] = 10 会报错: TypeError
  
  # 如果元组中的元素是可变数据类型，其嵌套项可以被修改
  tuple1 = (100, 200, 300, [1, 2, 3])
  tuple1[3].append(4)
  print(tuple1)
  ```

  **元组与列表的比较**

  | 特性     | 元组             | 列表         |
  | -------- | ---------------- | ------------ |
  | 可变性   | 不可变           | 可变         |
  | 内存占用 | 较小             | 较大         |
  | 性能     | 较快             | 较慢         |
  | 安全性   | 更安全           | 相对不安全   |
  | 使用场景 | 数据记录、字典键 | 动态数据集合 |

+ 集合（set）

  一种无序、不重复元素的数据结构

  **集合的创建**

  ```python
  # 使用花括号创建集合
  fruits = {"apple", "banana", "cherry"}
  
  # 使用set()构造函数创建集合
  numbers = set([1, 2, 3, 4, 5])
  
  # 创建空集合
  empty_set = set()
  
  # 注意{}创建的是空字典，不是空集合
  empty_dict = {}
  
  # 通过推导式创建集合
  set1 = {x for x in range(10) if x % 2 == 0}
  ```

  **添加集合元素**

  ```python
  my_set = {1, 2, 3}
  
  # 添加单个元素
  my_set.add(4)
  
  # 添加多个元素
  mymy_set.update([5, 6, 7])
  ```

  **删除集合元素**

  ```python
  my_set = {1, 2, 3, 4, 5}
  
  # remove() 如果元素不存在会报错
  my_set.remove(3)
  
  # discard() 如果元素不存在不会报错
  my_set.discard(10)
  
  # pop() 随即删除一个元素并返回
  removed = my_set.pop()
  
  # clear() 清空集合
  my_set.clear()
  ```

  **集合运算**

  ```python
  set1 = {1, 2, 3, 4, 5}
  set2 = {4, 5, 6}
  
  # 判断子集
  print(set1.issubset({1, 2, 3, 4, 5, 6}))  # 输出: True
  
  # 判断超集
  print(set1.issuperset({1, 2}))  # 输出: True
  
  # 判断是否有交集
  print(set1.isdisjoint({7, 8, 9}))  # 输出: True（没有交集）
  
  # 复制集合
  set_copy = set1.copy()
  ```

  **实际运用场景**

  ```python
  # 1. 去除列表中的重复元素
  numbers = [1, 2, 2, 3, 4, 4, 5, 5]
  unique_numbers = list(set(numbers))
  
  # 2. 查找两个列表的共同元素
  list1 = [1, 2, 3, 4, 5]
  list2 = [4, 5, 6, 7, 8]
  common = set(list1) & set(list2)
  print(common)  # 输出: {4, 5}
  
  # 3. 检查字符串中的唯一字符
  text = "programming"
  unique_chars = set(text)
  print(f"唯一字符: {unique_chars}")
  print(f"唯一字符数量: {len(unique_chars)}")
  ```

+ 字典（dist） 

  用于存储键值对，字典是无序的（Python 3.7+开始保持插入顺序）、可变的，且键必须是不可变类型

  **字典的创建**

  ```python
  # 方法1: 使用花括号
  person = {"name": "Alice", "age": 30}
  
  # 方法2: 使用dict()构造函数
  person = dict(name="Bob", age=25)
  
  # 方法3: 从键值对序列创建
  person = dict([("name", "Charlie"), ("age", 35)])
  
  # 方法4: 使用字典推导式
  squares = {x: x**2 for x in range(1, 6)}
  
  # 创建空字典
  empty_dict = {}
  ```

  **访问字典元素**

  ```python
  person = {"name": "Alice", "age": 30}
  
  # 通过键访问
  print(person["name"])
  
  # 使用get()方法 键不存在时返回None或默认值
  print(person.get("age"))
  
  # 获取所有键、值、键值对
  person.keys()
  person.values()
  person.items()
  
  # 字典遍历
  for key in person:
      print(key)
      
  # 遍历所有值
  for value in person.values():
      print(value)
      
  # 遍历所有键值对
  for key, value in person.items():
      print(f"{key}: {value}")
  ```

  **修改字典**

  ```python
  person = {"name": "Alice", "age": 30}
  
  # 更新现有键的值
  person["age"] = 31
  
  # 添加键值对
  person["country"] = "USA"
  
  # 使用update()合并字典
  person.update({"age": 32, "job": "Engineer"})
  
  # 使用setdefault() 如果键不存在则设置默认值
  person.setdefault("salary", 50000)
  ```

  **删除字典**

  ```python
  person = {"name": "Alice", "age": 30}
  
  # 使用del删除指定键
  del person["age"]
  
  # 使用pop()删除并返回指定键的值
  city = person.pop("city")
  
  # 使用popitem()删除并返回最后插入的键值对（Python 3.7+）
  item = person.popitem()
  
  # 清空字典
  person.clear()
  ```

  **嵌套字典**

  ```python
  # 嵌套字典示例
  users = {
      "user1": {
          "name": "Alice",
          "age": 30,
          "contacts": {
              "email": "alice@example.com",
              "phone": "123-456-7890"
          }
      },
      "user2": {
          "name": "Bob",
          "age": 25,
          "contacts": {
              "email": "bob@example.com",
              "phone": "098-765-4321"
          }
      }
  }
  
  # 访问嵌套字典
  users["user1"]["name"]
  users["user1"]["contacts"]["email"]
  
  # 修改嵌套字典
  users["user2"]["age"] = 26
  users["user2"]["contacts"]["phone"] = "111-222-3333"
  ```

  **字典和集合的比较**

  | 特性   | 字典                    | 集合             |
  | ------ | ----------------------- | ---------------- |
  | 元素   | 键值对                  | 单个值           |
  | 语法   | {key:value}             | {value}          |
  | 重复   | 键不能重复              | 值不能重复       |
  | 访问   | 通过键                  | 只能检查成员关系 |
  | 有序性 | Python 3.7+保持插入顺序 | 无序             |

#### 运算符

**算数运算符**

| 运算符 | 说明               |
| ------ | ------------------ |
| +      | 加                 |
| -      | 减或取负           |
| *      | 乘                 |
| /      | 除                 |
| //     | 整除，除后向下取整 |
| %      | 模，返回除法的余数 |
| **     | 幂                 |

**赋值运算符**

| 运算符 | 说明     |
| ------ | -------- |
| =      | 赋值     |
| +=     | 加法赋值 |
| -=     | 减法赋值 |
| *=     | 乘法赋值 |
| /=     | 除法赋值 |
| //=    | 整除赋值 |
| %=     | 模赋值   |
| **=    | 幂赋值   |

**比较运算符**

| 运算符 | 说明     |
| ------ | -------- |
| ==     | 相等     |
| !=     | 不相等   |
| >      | 大于     |
| <      | 小于     |
| >=     | 大于等于 |
| <=     | 小于等于 |

**逻辑运算符**

| 运算符 | 说明 |
| ------ | ---- |
| and    | 与   |
| or     | 或   |
| not    | 非   |

**隐式转换**

隐式转换（Implicit Conversion）通常指的是在某些操作中，Python自动将一种类型转换为另一种类型，而不需要显示地调用转换函数。这种转换也称为类型强转（Type Coercion）

+ 整数和浮点数计算
  + 整数 + 浮点数 = 浮点数
  + 整数 * 浮点数 = 浮点数
  + 整数 / 整数 = 浮点数（Python 3+）

+ 布尔值参与数值计算
  + 布尔值 True = 1，False = 0

+ 逻辑运算符中的隐式转换
  + 在条件判断中，非布尔值会被隐式转换为布尔值

**强制转换**

显示转换（也称为类型转换）是指程序员主动使用内置函数将一种数据类型转换为另一种数据类型

| 函数                 | 说明                                                |
| -------------------- | --------------------------------------------------- |
| int(x[,base])        | 将x转换为一个整数，x若为字符串可用base指定进制      |
| float(x)             | 将x转换为一个浮点数                                 |
| complex(real[,imag]) | 创建一个实部为real，虚部为imag的复数                |
| str(x)               | 将对象x转换为一个字符串                             |
| repr(x)              | 将对象x转换为一个字符串，可以转义字符串中的特殊字符 |
| eval(x)              | 执行x字符串表达式，并返回表达式的值                 |
| bin(x)               | 将一个整数转换为一个二进制字符串                    |
| oct(x)               | 将一个整数转换为一个八进制字符串                    |
| hex(x)               | 将一个整数转换为一个十六进制字符串                  |
| ord(x)               | 将一个字符串转换为它的ASCII码                       |
| chr(x)               | 将一个整数转换为一个Unicode字符                     |
| tuple(s)             | 将序列s转换为一个元组                               |
| list(s)              | 将序列s转换为一个列表                               |
| set(s)               | 转换s为可变集合                                     |

#### 输入输出

输入：字符变量 = input("提示信息")

普通输出：print()，可以用end=控制以什么结尾

```python
name = input("请输入：")
print("今天天气不错", end="。")
```

#### 流程控制

**条件语句（if，elif，else）**

```python
score = 18
if score >= 90:
    print("优秀")
elif score >= 80:
    print("良好")
else:
    print("一般")
```

**match case语句**

Python3.10新增了match case的条件判断方式，match后的对象会依次与case后的内容匹配，匹配成功则执行相应语句，否则跳过。其中_可以匹配一切

```python
status = 200
match status:
    case 200:
        return "OK"
    case 404:
        return "Not Found"
    case _:
        return "Unknown status"
        
```

**三目运算符**

三目运算符（Ternary Operator）在Python中通常被称为条件表达式。它允许在一行内根据条件选择两个值中的一个

```python
x = 10
result = "大于5" if x > 5 else "小于等于5"
```

**循环（while，for）**

while循环

```python
count = 1
while count <= 5:
    print(count)
    count += 1
    
# while else语句
count = 1
while count <= 5:
    print(count)
    count += 1
else:
    print("循环结束")
```

for循环

for循环是Python中最常用的循环结构，用于遍历序列（如列表、元组、字符串）或其他可迭代对象

```python
# 遍历列表
fruits = ["苹果", "香蕉", "橙子"]
for fruit in fruits:
    print(f"我喜欢吃{fruit}")
    
# 遍历字典
student_scores = {"张三": 85, "李四": 92, "王五": 78}
for name, score in student_score.items():
    print(f"{name}的分数是：{score}")
    
# for循环有一个可选的else子句
for i in range(5):
    print(i)
else:
    print("循环结束")
```

continue和break

continue出现在循环语句中会跳过当前循环剩余的语句，继续执行下一轮循环。一般写在if判断中

```python
for item in iterable:
    if condition:
        continue
    # 其他代码

while condition:
    if condition:
        continue
    # 其他代码
```

break跳出当前for或while的循环体，一般写在if判断中

注意：如果for或while循环通过break终止，循环对应的else将不执行

```python
for item in iterable:
    if condition:
        break
    # 其他代码

while condition:
    if condition:
        break
    # 其他代码
```

#### 函数

**函数的基本写法**

```python
def 函数名(参数1, 参数2, ...):
    """文档字符串（可选）"""
    # 函数体
    return 返回值  # 可选
```

**参数类型**

+ 位置参数

  ```python
  def add(a, b):  # 这里a, b是形参
      return a + b
  print(add(3, 5))  # 3, 5是实参
  ```

+ 默认参数

  ```python
  def power(base, exponent = 2):
      return base ** exponent
  print(power(3))  # 输出: 9
  print(power(3, 3))  # 输出: 27
  ```

+ 关键字参数

  ```python
  def create_person(name, age, city):
      return f"{name}, {age}岁, 来自{city}"
  
  # 使用关键字参数，顺序可以改变
  person = create_person(age = 25, city="北京", name="张三")
  print(person)  # 输出: 张三, 25岁, 来自北京
  ```

+ 可变参数

  ```python
  # *args接收任意数量的位置参数
  def sum_all(*args):
      return sum(args)
  
  print(sum_all(1, 2, 3, 4, 5))  # 输出: 15
  
  # **kwargs 接收任意数量的关键字参数
  def print_info(**kwargs):
      for key, value in kwargs.items():
          print(f"{key}: {value}")
          
  print_info(name="张三", age=30, city="上海")
  ```

**函数返回值**

```python
# 返回多个值（实际上是返回一个元组）
def calculate(a, b):
    add = a + b
    subtract = a - b
    multiply = a * b
    divide = a / b if b != 0 else "不能除以0"
    return add, subtract, multiply, divide

result = calculate(10, 5)
print(result)  # 输出: (15, 5, 50, 2.0)

# 解包返回值
add, sub, mul, div = calculate(10, 5)
```

**函数的嵌套调用**

```python
def function_a():
    print("函数a执行")
def function_b():
    print("函数b执行")
    function_a()
function_b()
```

**匿名函数（Lambda函数）**

```python
# 简单的lambda函数
square = lambda x: x ** 2
print(square(5))

# 在列表排序中使用
names = ["Alice", "Bob", "Charlie"]
sorted_names = sorted(names, key=lambda x: len(x))
```

**递归函数**

递归的基本概念（三要素）

1. 基准情况（Base Case）：递归终止的条件
2. 递归步骤（Recursive Step）：将问题分解为更小的子问题
3. 推进（Progress）：每次递归调用都向基准情况靠近

```python
def factorial(n):
    """计算n的阶乘"""
    # 基准情况
    if n == 0 or n == 1:
        return 1
    # 递归步骤
    else:
        return n * factorial(n - 1)
```

**回调函数**

回调函数是一个被作为参数传递给另一个函数的函数，他不会立即执行，而是在特定事件发生或条件满足时被回调

```python
def callback_function(name):
    print(f"回调函数被调用: 你好, {name}")
   
def main_function(callback, name):
    print("主函数执行")
    callback(name)
    
main_function(callback_function, "小明")

# 带参数的回调函数
def success_callback(result):
    print(f"操作成功，结果: {result}")
    
def error_callback(error_message):
    print(f"操作失败，错误: {error_message}")
    
def process_data(data, on_success, on_error):
    """处理数据，根据结果调用不同的回调函数"""
    try:
        if data < 0:
            raise ValueError("数据不能为负数")
        result = data * 2
        on_success(result)
    except Exception as e:
        on_error(str(e))
        
process_data(10, success_callback, error_callback)
```

**函数作用域**

```python
global_var = "我是全局变量"

def test_scope():
    local_var = "我是局部变量"
    print(global_var)
    print(local_var)
    
test_scope()

# 修改全局变量
counter = 0

def increment():
    global counter  # 声明使用全局变量
    counter += 1
    
increment()
print(counter)  # 输出: 1
```

#### 闭包

允许函数记住并访问其词法作用域中的变量，即使函数在其作用域之外执行

闭包是一个函数对象，他记住了创建它的环境中的值，即使这些值在内存中不再存在

闭包的特点：

1. 嵌套函数：闭包涉及至少两个嵌套的函数
2. 内部函数引用外部变量：内部函数使用外部函数的变量
3. 外部函数返回内部函数：外部函数将内部函数作为返回值

```python
def counter(num):
    def add(addend):
        print(num + addend)
    return add
fn_add = counter(3)
fn_add(5)
```

#### 装饰器

装饰器本质上是一个Python函数，它可以让其他函数在不需要做任何代码变动的前提下增加额外功能，装饰器的返回值也是一个函数对象

```python
def my_decorator(func):
    def wrapper():
        print("函数执行前")
        func()
        print("函数执行后")
    return wrapper

@my_decorator
def say_hello():
    print("hello")

say_hello()

# 通过闭包来写
def my_decorator(func):
    def wrapper():
        print("函数执行前")
        func()
        print("函数执行后")
    return wrapper

def say_hello():
    print("hello")

fn = my_decorator(say_hello)
print(fn())
```

#### 类

面向对象编程是一种基于对象的编程范式，他将数据和操作数据的方法组合在一起

四大基本概念

+ 封装（Encapsulation）

  隐藏内部实现细节，通过公共接口访问数据，保护数据的完整性

+ 继承（Inheritance）

  代码复用，建立类之间的层次关系，扩展父类功能

+ 多态（Polymorphism）

  同一操作作用于不同的对象，可以有不同的解释和不同的执行结果

  接口统一，实现不同，运行时确定调用哪个方法，一个接口，多个实现

+ 抽象（Abstraction）

  关注做什么，而不是怎么做，定义接口规范，简化复杂系统

```python
class Person:
    """类说明文档"""
    # 类属性
    species = "人类"
    def __init__(self, name. age):
        # self实例对象
        self.name = name
        self.age = age
        
    # 实例方法
    def say(self):
        return f"我叫{self.name}, 今年{self.age}岁"
    
# 使用
p = Person("张三", 18)
print(p.say())
```

**__ init __方法详解**

作用：初始化新创建的对象，设置对象的初始状态

**实例属性和类属性**

类属性会被所有实例访问到

```python
class Car:
    # 类属性
    wheels = 4
    count = 0
    
    def __init__(self, brand, color):
        # 给实例设置属性
        self.brand = brand
        self.color = color
        self.speed = 0
        Car.count += 1
```

**方法类型**

+ 实例方法

  ```python
  class Student:
      def __init__(self, name, score):
          self.name = name
          self.score = score
          
      # 实例方法 第一个参数必须是self
      def get_grade(self):
          if self.score >= 90:
              return "A"
          elif self.score >= 80:
              return "B"
          else:
              return "C"
          
      # 实例方法可以访问和修改实例属性
      def add_bonus(self, bonus):
          self.score += bonus
          
  student = Student("小明", 85)
  print(student.get_grade())  # B
  student.add_bonus(10)
  print(student.get_grade())  # A
  ```

+ 类方法

  ```python
  class MyClass:
      class_attribute = 0
      
      def __init__(self, value):
          self.instance_attribute = value
          
      @classmethod
      def class_method(cls):  # 第一个参数是cls，表示类本身
          cls.class_attribute += 1
          return f"类属性值: {cls.class_attribute}“
      
      @classmethod
      def create_with_default(cls):
          # 类方法可以作为替代构造函数
          return cls("默认值")
  
  print(MyClass.class_method())  # 类属性值: 1
  obj = MyClass.create_with_default()
  print(obj.instance_attribute)  # 默认值
  ```

+ 静态方法

  ```python
  class MathUtils:
      @staticmethod
      def add(a, b):
          return a + b
      
      @staticmethod
      def multiply(a, b):
          return a * b
      
      @staticmethod
      def is_even(number):
          return number % 2 == 0
      
  print(MathUtils.add(5, 3))
  ```

+ 特殊方法

  方法名中有两个前缀下划线和两个后缀下划线的方法为特殊方法，也叫魔法方法

  + __ new __()

    对象实例化时第一个调用的方法，静态方法，接收类cls

  + __ init __()

    类的初始化方法

  + __ del __()

    对象的销毁器，定义了当对象被垃圾回收时的行为。使用del xxx时不会主动调用del()，除非此时引用计数==0

  + __ str __()

    定义了对类的实例调用str()时的行为

  + __ repr __()

    定义了对类的实例调用repr()时的行为。str()和repr()最主要的差别在于目标用户。repr()的作用是产生机器可读的输出（大部分情况下，其输出可以作为有效的Python代码），而str()则产生人类可读的输出

  + __ getattribute __()

    属性访问拦截器，定义了属性被访问前的操作

**继承**

```python
class ParentClass:
    """父类/基类"""
    pass

class ChildClass(ParentClass):
    """子类/派生类"""
    pass

# 示例
class Animal:
    def __init__(self, name, age)
    self.name = name
    self.age = age
    
    def eat(self):
        return f"{self.name}正在吃东西"
    
    def sleep(self):
        return f"{self.name}正在睡觉"
    
class Dog(Animal):  # Dog继承Animal
    def bark(self):
        return f"{self.name}在汪汪叫"
    
class Cat(Animal):  # Cat继承Animal
    def meow(self):
        return f"{self.name}在喵喵叫"
    
dog = Dog("旺财", 3)
cat = Cat("咪咪", 2)
print(dog.eat())
print(dog.bark())
print(cat.sleep)
```

**多重继承**

```python
class Flyable:
    def __init__(self, max_altitude=1000):
        self.max_altitude = max_altitude
        
    def fly(self):
        return f"飞行高度可达{self.max_altitude}米"
    
class Swimmable:
    def __init__(self, max_depth=10):
        self.max_depth = max_depth
        
    def swim(self):
        return f"潜水深度可达{self.max_depth}米"
    
class Runnable:
    def __init__(self, max_speed=50):
        self.max_speed = max_speed
        
    def run(self):
        return f"奔跑速度可达{self.max_speed}km/h"
    
# 多重继承
class Duck(Flyable, Swimmable, Runnable):
    def __init__(self, name):
        self.name = name
        # 分别初始化各个父类
        Flyable.__init__(self, 500)
        Swimmable.__init_(self, 5)
        Runnable.__init__(self, 10)
        
    def quack(self):
        return f"{self.name}在嘎嘎叫"
    
duck = Duck("唐老鸭")
print(duck.quack())
print(duck.fly())
print(duck.swim())
print(duck.run())
```

#### 文件的操作

**打开文件**

```python
file = open(filename, mode, encoding)  # 三个参数分别对应 文件名, 模式, 编码
```

| 模式 | 描述                             |
| ---- | -------------------------------- |
| 'r'  | 只读（默认）                     |
| 'w'  | 写入，会覆盖已有内容             |
| 'a'  | 追加，在文件末尾添加             |
| 'x'  | 创建新文件，如果文件已存在则失效 |
| 'b'  | 二进制模式                       |
| 't'  | 文本模式（默认）                 |
| '+'  | 更新（可读可写）                 |

**读取文件**

```python
# 传统方式
file = open('example.txt', 'r')
try:
    content = file.read()
    # 处理文件内容
finally:
    file.close()

# 使用with语句（推荐）
with open('example.txt', 'r') as file:
    content = file.read()
    # 处理文件内容
# 文件会自动关闭，无需手动调用file.close()
```

**写入文件**

```python
# 写入文件（覆盖）
with open('example.txt', 'w') as file:
    file.write("Hello World\n")
    file.write("这是第二行\n")
    
# 追加内容
with open('example.txt', 'a') as file:
    file.write("这是追加的内容\n")
    
# 写入多行
lines = ["第一行\n", "第二行\n", "第三行\n"]
with open('example.txt', 'w') as file:
    file.writelines(lines)
```

**实用文件操作函数**

import os 是Python中导入标准库os模块的语句，该模块提供了与操作系统交互的各种功能

+ 检查文件是否存在

  ```python
  import os
  
  filename = 'example.txt'
  
  if os.path.exists(filename):
      print(f"文件 {filename} 存在")
  else:
      print(f"文件 {filename} 不存在")
  ```

+ 获取文件信息

  ```python
  import os
  
  file_info = os.stat('example.txt')
  print(f"文件大小: {file_info.st_size}字节")
  print(f"最后修改时间: {file_info.st_mtime}")
  ```

+ 文件重命名和删除

  ```python
  impport os
  
  # 重命名文件
  os.rename('old_name.txt', 'new_name.txt')
  
  # 删除文件
  if os.path.exists('file_to_delete.txt'):
      os.remove('file_to_delete.txt')
  ```

**CSV文件操作**

```python
import csv

# 写入csv文件
with open('data.csv', 'w', newline='', encoding='utf-8') as file:
    writer = csv.writer(file)
    writer.writerow(['姓名', '年龄', '城市'])
    writer.writerow(['张三', 25, '北京'])
    writer.writerow(['李四', 30, '上海'])
    
# 读取csv文件
with open('data.csv', 'r', encoding='utf-8') as file:
    reader = csv.reader(file)
    for row in reader:
        print(row)
        
# 使用字典方式读写csv
with open('data.csv', 'w', newline='', encoding='utf-8') as file:
    fieldnames = ['姓名', '年龄', '城市']
    writer = csv.DictWriter(file, fieldnames=fieldnames)
    writer.writeheader()
    writer.writerow({'姓名': '王五', '年龄': 28, '城市': '广州'})
```

**JSON文件操作**

```python
import json

# 数据写入JSON文件
data = {
    'name': '张三',
    'age': 25,
    'cities': ['北京', '上海', '广州'],
    'is_student': False
}

with open('data.json', 'w', encoding='utf-8') as file:
    json.dump(data, file, ensure_ascii=False, indent=4)

# 从JSON文件读取数据
with open('data.json', 'r', encoding='utf-8') as file:
    loaded_data = json.load(file)
    print(loaded_data)
```

#### 错误和异常

**常见的Python内置异常**

| 异常名称          | 描述                 |
| ----------------- | -------------------- |
| Exception         | 所有内置异常的基类   |
| ValueError        | 值错误，如int('abc') |
| TypeError         | 类型错误，如'2' + 2  |
| IndexError        | 索引超出范围         |
| KeyError          | 字典键不存在         |
| FileNotFoundError | 文件未找到           |
| ZeroDivisionError | 除以0                |
| AttributeError    | 属性引用失效         |
| ImportError       | 导入模块失败         |

**try-except块**

```python
try:
    # 可能引发异常的代码
    result = 10 / 0
except ZeroDivisionError:
    # 处理特定异常
    print("不能除以零")
```

**处理多种异常**

```python
try:
    num = int(input("请输入一个数字："))
    result = 10 / num
except ValueError:
    print("输入的不是一个有效数字")
except ZeroDivisionError:
    print("不能除以0")
except Exception as e:
    print(f"发生了未知错误: {e}")
    
# 用条件判断
num = input("请输出一个数字:")
if not num.replace(".", "").isdigit():
    raise TypeError("参数类型错误")
elif int(num) == 0:
    raise ValueError("除数不能为0")
else:
    result = 10 / int(num)
    print(result)
```

**else语句**

```python
try:
    num = int(input("请输出一个数字:"))
except ValueError:
    print("输入无效")
else:
    # 如果没有异常发生，执行这里的代码
    print(f"你输入的数字是: {num}")
```

**finally语句**

```python
try:
    file = open("example.txt", "r")
    content = file.read()
except FileNotFoundError:
    print("文件不存在")
finally:
    # 无论是否发生异常都会执行
    if 'file' in locals():
        file.close()
        print("清理完成")
```

#### 模块和包

**什么是模块**

+ 模块是一个包含Python代码的.py文件
+ 可以包含函数、类、变量和可执行代码
+ 通过模块可以实现代码的复用和组织

**创建模块**

```python
# math_operations.py
"""一个简单的数学运算模块"""

def add(a, b):
    return a + b

def multiply(a, b):
    return a * b

def factorial(n):
    if n == 0:
        return 1
    return n * factorial(n - 1)

# 模块级别的变量
PI = 3.15159
VERSION = "1.0"
```

**使用模块**

+ 全部导入

  导入模块的所有成员，通过模块名.成员名的方式访问。即使多次使用import导入同一模块，模块也只会被导入一次

  ```python
  # import 模块名 [ as 别名 ]
  import math_operations
  
  print(math_operations.PI)
  print(math_operations.add(5, 3))
  ```

+ 部分导入

  ```python
  # from 模块名 import 成员1[as 别名], 成员2[as 别名]...
  from math_operations import add as add1, factorial as ft
  
  print(ft(5))
  
  # 导入所有内容（不推荐）
  # from 模块名 import *
  ```

**内置模块**

| 名称            | 说明                                                         |
| --------------- | ------------------------------------------------------------ |
| os              | 多种操作系统接口                                             |
| sys             | 系统相关的形参和函数                                         |
| time            | 时间的访问和转换                                             |
| datetime        | 提供了用于操作日期和时间的类                                 |
| math            | 数学函数                                                     |
| random          | 生成伪随机数                                                 |
| re              | 正则表达式匹配操作                                           |
| json            | JSON编码器和解码器                                           |
| collections     | 实现了一些专门化的容器，提供了对Python的通用内建容器dict、list、set和tuple的补充 |
| functools       | 高阶函数，以及可调用对象上的操作                             |
| hashlib         | 安全哈希与消息摘要                                           |
| urllib          | URL处理模块                                                  |
| smtplib         | SMTP协议客户端，邮件处理                                     |
| zlib            | 与gzip兼容的压缩                                             |
| gzip            | 对gzip文件的支持                                             |
| bz2             | 对bzip2压缩算法的支持                                        |
| multiprocessing | 基于进程的并行                                               |
| threading       | 基于线程的并行                                               |
| copy            | 浅层及深层拷贝操作                                           |
| socket          | 底层级的网络接口                                             |
| shutil          | 提供了一系列对文件和文件集合的高阶操作，特别提供了一些支持文件拷贝和删除的函数 |
| glob            | Unix风格的路径名模式扩展                                     |

**创建包**

包是包含多个模块的目录，必须包含一个__ init __.py文件

```python
# init.py文件
from .module1 import function1
from .module2 import function2

__all__ = ['function1', 'function2']
```

**使用包**

```python
# 导入整个包
import mypackage

# 导入特定模块
from mypackage import module1

# 导入指定函数
from mypackage.module1 import function1
```

**第三方模块**

第三方模块官网：https://pypi.org/

使用pip安装第三方模块：pip install requests numpy  pandas

pip默认的下载源是 https://pypi.org/simple/

如果下载比较慢，可以使用国内其他镜像

+ 阿里云：https://mirrors.aliyun.com/pypi/simple/
+ 豆瓣：https://pypi.douban.com/simple/
+ 清华大学：https://pypi.tuna.tsinghua.edu.cn/simple/

pip常见命令

```python
# 查看已经安装的软件包
pip list

# 安装软件包
pip install 包名

# 卸载软件包
pip uninstall 包名

# 临时使用其他源
pip install -i 下载源 包名

# 永久修改源
pip config set global.index-url https://mirrors.aliyun.com/pypi/simple/
    
# 恢复默认源
pip config unset global.index-url
```

**模块搜索路径**

Python按以下顺序搜索模块：

1. 当前目录
2. 环境变量PYTHONPATH指定的目录
3. Python安装的标准库目录
4. 第三方库目录

```python
import sys
print(sys.path)  # 查看模块搜索路径
```

**特殊变量**

每一个模块都有一些内置的特殊变量

```python
# 在mymodule.py中添加
print(f"模块名:{__name__}")
print(f"文件位置:{__file__}")

if __name__ == "__main__":
    print("这个模块被直接运行")
else:
    print("这个模块被导入")
```

#### 浅拷贝和深拷贝

浅拷贝创建一个新对象，但只拷贝第一层内容，嵌套对象仍然共享引用

**浅拷贝**

```python
# 列表的浅拷贝
original = [1, 2, [3, 4]]

# 方法1: copy模块
from copy import copy
copy1 = copy.copy(original)

# 方法2: 列表切片
copy2 = original[:]

# 方法3: list()构造函数
copy3 = list(original)

# 方法4: 列表的copy()方法（Python 3.3+）
copy4 = original.copy()

# 字典的拷贝
original_dict = {'a': 1, 'b': [2, 3]}

shallow_dict = original_dict.copy()
shallow_dict = copy.copy(original_dict)
```

**深拷贝**

深拷贝创建一个完全独立的新对象，包括所有嵌套对象

```python
import copy

original = [1, 2, [3, 4]]
deep_copy = copy.deepcopy(original)
```

#### 线程、进程、协程

**同步和异步**

同步：在同一进程中，任务按照编写的顺序一个接一个执行。如果任务一和任务二是同步关系，那么任务二必须等待任务一完全结束后才能开始执行。这种执行方式是阻塞的，后续任务必须等待前一个任务完成

异步：执行任务一时，任务二并不会被任务一阻塞等待。如果任务一包含 I/O 请求，它可以在等待数据到达的同时执行任务二，等数据准备好后，再根据事件循环调度完成任务一。这种方式可以提高程序的效率和响应性

 同步代码

```python
import time
def write_file():
    print("写文件")
    time.sleep(1)  # 阻塞1秒
    print("写入文件完成")
    
def read_file():
    print("读文件")
    time.sleep(0.5)  # 阻塞0.5秒
    print("读取文件完成")
    
def main():
    write_file()
    read_file()

main()  # 写文件 写入文件完成 读文件 读取文件完成
```

异步代码

```python
import asyncio
async def write_file():
    print("写文件")
    await asyncio.sleep(1)
    print("写入文件完成")
    
async def read_file():
    print("读文件")
    await asyncio.sleep(0.5)
    print("读取文件完成")
    
async def main():
    await asyncio.gather(write_file(), read_file())

asyncio.run(main())  # 写文件 读文件 读取文件完成 写入文件完成
```

**并发和并行**

并发：单个CPU处理多个任务，各个任务交替执行一段时间，使用IO密集型任务

并行：多个CPU同时执行多个任务，适用CPU密集型任务

关键区别：

| 特性       | 并发（Concurrency） | 并行（Parallelism） |
| ---------- | ------------------- | ------------------- |
| 执行方式   | 任务交替执行        | 任务同时执行        |
| 硬件要求   | 单核即可            | 需要多核CPU         |
| 适用场景   | I/O密集型任务       | CPU密集型任务       |
| Python实现 | 线程、协程          | 多进程              |
| GIL影响    | 受GIL限制           | 不受GIL限制         |

**什么是进程**

进程是操作系统资源分配的基本单位，每个进程都有独立的内存空间。在Python中，由于全局解释器锁（GIL）的存在，多进程是实现真正并行的主要方式

创建进程的方法

+ multiprocessing.Process创建进程

  ```python
  import multiprocessing
  
  # Process 构造函数参数
  multiprocessing.Process(
  	group=None,  # 始终为None, 保留给未来扩展
      target=None,  # 要调用的可调用对象（函数）
      name=None,  # 进程名称
      args=(),  # 目标函数的参数元组
      kwargs={},  # 目标函数的关键字参数字典
      daemon=None  # 守护进程标志
  )
  ```

  Process类主要方法

  + start()：启动进程
  + run()：进程执行体（可重写）
  + join([timeout])：等待进程结束
  + terminate()：终止进程
  + kill()：强制杀死进程
  + close()：关闭进程对象

  Process类的主要属性

  + name：进程名称
  + pid：进程id
  + daemon：守护进程标志
  + exitcode：退出码
  + is_alive()：检查进程是否存活

```python
import time
import multiprocessing

# 向文件中写入数据
def write_file():
    with open("data.txt", "a") as f:
        while True:
            f.write("hello world\n")
            f.flush()
            time.sleep(0.5)
            
# 从文件中读取数据
def read_file():
    with open("data.txt", "r") as f:
        while True:
            time.sleep(0.1)
            print(f.read(1))
            
# 在windows上执行要加if __name__ == "__main__"
if __name__ == "__main__":
    # 创建一个子进程用于写文件
    p1 = multiprocessing.Process(target=write_file)
    # 创建一个子进程用于读文件
    p2 = multiprocessing.Process(target=read_file)
    
    # 启动子进程
    p1.start()
    p2.start()
```

**join() - 等待进程结束**

```python
import multiprocessing
import time

def counting_worker(name, count_to):
    """计数工作进程"""
    print(f"{name}开始计数...")
    for i in range(1, count_to + 1):
        print(f"{name}: {i}")
        time.sleep(0.1)
    print(f"{name} 计数完成")
    
if __name__ == "__main__":
    processes = []
    
    # 创建但不立即启动
    for i in range(2):
        p = multiprocessing.Process(
        	target=counting_worker,
            args=(f"Counter-{i}", 3)
        )
        processes.append(p)
       
    # 分批启动进程
    print("启动第一批进程...")
    processes[0].start()
    time.sleep(0.5)
    print("启动第二批进程...")
    processes[1].start()
    
    # 等待所有进程
    for p in processes:
        p.join()
        
    print("主代码进程")
```

**terminate()和kill() - 终止进程**

```python
import multiprocessing
import time
def worker(name):
    """无限循环的工作进程"""
    counter = 0
    try:
        while True:
            print(f"{name} 运行中... {counter}")
            counter += 1
            time.sleep(0.5)
    except KeyboardInterrupt:
        print(f"{name} 收到中断信号")
        
if __name__ == "__main__":
    processes = []
    # 创建无限循环的进程
    for i in range(3):
        p = multiprocessing.Process(
            targe=worker,
            args=(f"Infinite-{i}",)
        )
        processes.append(p)
        p.start()
        
    # 让进程运行一段时间
    time.sleep(2)
    
    print("\n开始终止进程...")
    
    # 终止所有进程
    for i, p in enumerate(processes):
        if p.is_alive():
            print(f"终止进程{i} (PID: {p.pid})")
            p.terminate()
          
    # 等待进程真正结束
    for p in processes:
        p.join(timeout=1)
        if p.is_alive():
            print(f"进程{p.pid} 仍然存活, kill()")
            p.kill()
            p.join()
            
    print("所有进程被终止")
```

**自定义Process子类创建进程**

```python
import os
import multiprocessing

class worker(multiprocessing.Process):
    def run(self):
        print("进程id: ", os.getpid(), "\t父进程id: ", os.getppid())
if __name__ == "__main__":
    for i in range(5):
        p = Worker()
        p.start()
```

**进程池**

进程池（multiprocessing.Pool）是Python中用于管理多个进程的高级接口，它可以自动管理进程的创建、调度和销毁，非常适合处理大量并行任务

| 方法             | 特点                  | 适用场景           |
| ---------------- | --------------------- | ------------------ |
| map()            | 同步，顺序返回        | 简单并行处理       |
| map_async()      | 异步，返回AsyncResult | 不阻塞主进程       |
| apply()          | 同步执行单个任务      | 单个复杂任务       |
| apply_async()    | 异步执行单个任务      | 任务提交后继续工作 |
| imap()           | 惰性迭代，顺序返回    | 处理大型数据集     |
| imap_unordered() | 惰性迭代，按完成顺序  | 尽快获取结果       |

```python
import multiprocessing
import time
import os

def worker_task(n):
    """工作进程任务"""
    print(f"进程 {os.getpid()} 开始处理任务 {n}")
    time.sleep(1)
    print(f"进程 {os.getpid()} 完成任务 {n}")
    
if __name__ == "__main__":
    print("=== 创建进程池 ===")
    p = multiprocessing.Pool(processes=4)
    start_time = time.perf_counter()
    # 每次同时执行4个任务，循环5次
    for _ in range(5):
        # 4个任务交给进程池完成
        # p.map(worker_task, ['a', 'b', 'c', 'd'])
        
        # 一次只能执行一个任务，跟线程池数量无关
        # p.apply(worker_task, args('a',))
        # p.apply(worker_task, args('b',))
        # p.apply(worker_task, args('c',))
        # p.apply(worker_task, args('d',))
        
        # p.apply_async(worker_task, args('a',))
        # p.apply_async(worker_task, args('b',))
        # p.apply_async(worker_task, args('c',))
        # p.apply_async(worker_task, args('d',))
        # p.close()
        # p.join()
        
    end_time = time.perf_counter()
    print(f"执行时间耗时: {end_time - start_time: .2f}s")
    print("end")
```

**多进程不共享全局变量**

```python
import os
import multiprocessing

def worker(list_data):
    for i in range(5):
        print(f"当前进程: {os.getpid()}, 数据: {list_data}")
        list_data.append(i)

if __name__ == "__main__":
    list_data = []
    p1 = multiprocessing.Process(target=worker, args=(list_data,))
    p2 = multiprocessing.Process(target=worker, args=(list_data,))
    p1.start()
    p1.join()
    p2.start()
    p2.join()
    
    print(f"主进程: {os.getpid()}, 数据:{list_data}")  # list_data为空
```

**Queue实现进程通信**

multiprocessing.Queue是Python多进程中用于进程间通信（IPC）的重要工具，它提供了线程安全和进程安全的队列实现，实现方式：生产者-消费者模式

multiprocessing.Queue(maxsize=0, ctx=None)

参数说明

+ maxsize（int，可选）：
  + 队列的最大容量
  + 默认值：0（无限大小）
  + 如果maxsize > 0：当队列达到最大容量时，put()操作会阻塞直到有空间可用
  + 如果maxsize <= 0：队列大小无限制
+ ctx（multiprocessing.context.BaseContext，可选）：
  + 上下文对象，指定启动方法
  + 通常不需要手动指定
  + 默认使用当前进程的上下文

Queue方法参数详解

+ put(obj[, block[, timeout]])：向队列中放入一个对象

  + 参数：
    + obj：要放入队列的对象（必须可序列化）
    + block（bool，可选）：
      + True：如果队列满则阻塞等待（默认）
      + False：如果队列满立即抛出queue.Full异常
    + timout（float，可选）：
      + 阻塞等待的最大秒数
      + 如果为None（默认）则无限等待
      + 超时后抛出queue.Full异常

+ put_nowait(obj)：非阻塞方式向队列中放入对象

  + 参数

    + obj：要放入队列的对象

    等价于：put(obj, block=False)

+ get([block[, tiemout]])：从队列中获取并移除一个对象

  + 参数：
    + block（bool，可选）：
      + True：如果队列为空则阻塞等待（默认）
      + False：如果队列为空立即抛出queue.Empty异常
    + timeout（float，可选）：
      + 阻塞等待的最大秒数
      + 如果为None（默认）则无限等待
      + 超时后抛出queue.Empty异常

+ get_nowait()：非阻塞方式从队列中获取对象

  等价于：get(block=False)

+ full()：检查队列是否已满

  + 返回值：bool

    + True：队列已满
    + False：队列未满

    注意：由于多进程环境，结果只是近似值

```python
# put
import multiprocessing as mp

def put_examples():
    q = mp.Queue(maxsize=2)
    
    q.put("hello")
    
    # 阻塞放入（默认行为）
    q.put("world")  # 正常放入
    # q.put("third")  # 这里会阻塞，因为队列已满
    
    # 非阻塞放入
    try:
        q.put("third", block=False)  # 立即抛出queue.Full
    except mp.queues.Full:
        print("队列已满，无法放入")
        
    # 带超时的放入
    try:
        q.put("third", block=True, timeout=2.0)  # 等待2秒
        print("成功放入")
    except mp.queues.Full:
        print("2秒后队列仍满，放弃放入")
        
if __name__ == "__main__":
    put_example()
```

```python
# get
import multiprocessing as mp

def get_examples():
    q = mp.Queue()
    
    q.put("data1")
    q.put("data2")
    
    item1 = q.get()
    
    item2 = q.get(block=False)
    
    # 非阻塞放入
    try:
        item3 = q.get(block=False)
    except mp.queues.Empty:
        print("队列为空，无法获取")
        
    # 带超时的放入
    try:
        item3 = q.get(block=True, timeout=3.0)
    except mp.queues.Empty:
        print("3秒后队列仍为空，放弃获取")
        
if __name__ == "__main__":
    get_example()
```

**什么是线程**

线程是操作系统能够进行运算调度的最小单位，它被包含在进程之中，是进程中的实际运作单位。一个进程可以包含多个线程，这些线程共享进程的资源，但各自拥有独立的执行路径

线程特点：

+ 共享资源：同一进程内的线程共享内存空间和系统资源
+ 独立执行：每个线程都有独立的程序计数器、栈和寄存器
+ 轻量级：创建和销毁线程的开销比进程小
+ 并发执行：多个线程可以同时运行

线程的生命周期：新建（New）-> 就绪（Ready） -> 运行（Running）-> 阻塞（Blocked）-> 死亡（Dead）

创建线程的基本方法

+ threading.Thread创建线程

  ```python
  threading.Thread(
      group=None,  # 保留参数，用于未来扩展，应该为None
      target=None,  # 线程要执行的可调用对象（函数）
      name=None,  # 线程名称（默认为"Thread-N"格式）
      args=(),  # 传递给target函数的参数元组
      kwargs={},  # 传递给target函数的关键字参数字典
      daemon=None  # 是否为守护线程（True/False）
  )
  ```

  ```python
  import threading
  import time
  
  # 方法1: 使用函数创建线程
  def print_numbers(thread_name, count):
      for i in range(count):
          print(f"{thread_name}: {i}")
          time.sleep(0.1)
          
  def function_thread():
      # 创建线程
      t1 = threading.Thread(target=print_numbers, args=("线程1", 5))
      t2 = threading.Thread(target=print_numbers, args=("线程2", 5))
      
      # 启动线程
      t1.start()
      t2.start()
      
      t1.join()
      t2.join()
      
      print("所有线程执行完成")
      
  if __name__ == "__main__":
      function_thread()
      
  # 方法2: 继承threading.Thread
  class MyThread(threading.Thread):
      def __init__(self, thread_name, count):
          super().__init__()
          self.thread_name = thread_name
          self.count = count
          
      def run(self):
          for i in range(self.count)
          print(f"{self.thread_name}: {i}")
          time.sleep(0.1)
          
  def class_thread():
      t1 = MyThread("自定义线程1", 5)
      t2 = MyThread("自定义线程2", 3)
      
      # 启动线程
      t1.start()
      t2.start()
      
      t1.join()
      t2.join()
      
      print("自定义线程执行完成")
  
  if __name__ == "__main__":
      class_thread()
  ```

**线程池**

线程池是一种管理和复用，可以避免频繁创建和销毁线程的开销，提高程序性能。Python提供了concurent.futures模块来实现线程池

```python
from concurrent.futures import ThreadPoolExecutor
import time
import threading

def task(name, duration):
    """模拟任务函数"""
    print(f"任务 {name} 开始执行 (线程: {threading.current_thread().name})")
    time.sleep(duration)
    print(f"任务 {name} 完成")
    return f"任务 {name} 的结果"

def basic_thread_pool():
    # 创建线程池，最大线程数为3
    with ThreadPoolExecutor(max_workers=3) as executor:
        # 提交任务
        future1 = executor.submit(task, "A", 2)
        future2 = executor.submit(task, "B", 1)
        future3 = executor.submit(task, "C", 3)
        future4 = executor.submit(task, "D", 1)
        
        # 获取结果
        print("结果: ", future1.result())
        print("结果: ", future2.result())
        print("结果: ", future3.result())
        print("结果: ", future4.result())
        
if __name__ == "__main__":
    print("=== 基础线程池演示 ===")
    basic_thread_pool()
```

**线程安全问题**

线程安全指的是在多线程环境中，当多个线程同时访问同一个共享资源时，不会出现数据污染或不一致的情况

```python
import threading
import time

def increment(name):
    global counter
    for _ in range(10):
        tmp = counter + 1
        time.sleep(0.01)
        counter = tmp
        print(f"{name}: {counter}\n", end="")
        
if __name__ == "__main__":
    counter = 0
    threads = []
    for i in range(3):
        t = threading.Thread(target=increment, args=(f"线程{i}",))
        threads.append(t)
    for t in threads:
        t.start()
    for t in threads:
        t.join()
    print(counter)  # 这里最后输出可能不是30
```

**线程安全的解决方法（使用锁Lock）**

互斥锁是一种同步锁，它确保在同一时刻只有一个线程可以访问共享资源。线程在访问共享资源时必须先获取锁，访问完成后释放锁

```python
def increment(name):
    global counter
    for _ in range(10):
        # 获取锁
        lock.acquire_lock()
        tmp = counter + 1
        time.sleep(0.01)
        counter = tmp
        print(f"{name}: {counter}\n", end="")
        # 释放锁
        lock.release_lock()

if __name__ == "__main__":
    # 创建互斥锁
    lock = threading.Lock()
```

**什么是协程**

协程（Coroutine）是Python中实现并发编程的一种重要方式，它比线程更轻量级，可以在单个线程中实现多个任务的并发执行

| 特性     | 进程     | 线程     | 协程   |
| -------- | -------- | -------- | ------ |
| 创建开销 | 大       | 中等     | 小     |
| 切换开销 | 大       | 中等     | 小     |
| 内存占用 | 大       | 中等     | 小     |
| 调度方式 | 操作系统 | 操作系统 | 程序员 |
| 并发能力 | 低       | 中等     | 高     |

创建协程的基本方法：

```python
import asyncio

async def hello_world():
    print("Hello")
    await asyncio.sleep(1)
    print("World")
    
# 运行协程
asyncio.run(hello_world())
```

**事件循环**

事件循环（Event Loop）是异步编程的核心，它负责调度和执行协程、处理I/O事件、运行回调函数等。理解事件循环对于掌握Python异步编程至关重要

事件循环是一个无限循环，它不断地检查并执行以下任务：

+ 检查协程：检查是否存在可以执行的协程
+ 让出控制：将控制权转移给可以执行的协程
+ 等待协程：等当前协程暂停或者执行完成后让出控制权

```python
import asyncio
import time

async def say_after(delay, message):
    await asyncio.sleep(delay)
    print(message)
    
async def sync_func():
    start_time = time.perf_counter()
    await say_after(2, "Hello")
    await say_after(1, "World")
    
    end_time = time.perf_counter()
    print(f"执行时间耗时：{end_time - start_time:.2f}s")

async def async_func():
    start_time = time.perf_counter()
    task1 = asyncio.create_task(say_after(2, "Hello"))
    task2 = asyncio.create_task(say_after(1, "World"))
    
    await task1
    await task2
    
    # 使用gather包装协程并收集结果
    # await asyncio.gather(say_after(2, "Hello"), say_after(1, "World"))
    
    end_time = time.perf_counter()
    print(f"执行时间耗时：{end_time - start_time:.2f}s")
    
# 同步执行
asyncio.run(sync_func())

# 异步执行
asyncio.run(async_func())
```

### 爬虫

爬虫是一种自动获取网页信息的程序或脚本，也称为网络蜘蛛（Spider）或网络机器人（Bot）

爬虫的工作流程

+ 发送请求：爬虫首先发送HTTP请求到目标网站
+ 获取响应：获取请求返回的响应内容
+ 解析响应，提取数据：爬虫解析响应内容，提取需要的信息，比如url链接、文本数据等
+ 存储数据：爬虫将提取的信息存储到本地文件或数据库中

**robots协议**

Robots协议（也称为robots.txt）是一个位于网站根目录下的文本文件，用于指示搜索引擎爬虫哪些页面可以访问，哪些页面不应该被访问。该文件包含一系列规则，定义了爬虫对网站的访问权限

Robots协议的基本语法包括两个关键字：User-agent和Disallow

+ User-agent：指定了爬虫的名称或标识符
+ Disallow：指定了不允许被访问的URL路径

案例网站：http://www.baidu.com/robots.txt

#### requests基本使用

**发送get请求**

```python
import requests
# 目标url
url = 'https://www.baidu.com'
# 向目标url发送get请求
response = requests.get(url)
# 打印响应内容
print(response.text)

# 获取到的是字符串
response.text

# 获取到的是原始的二进制数据（bytes类型的数据）
response.content


# 携带http请求头
headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/148.0.0.0 Safari/537.36"
}
response = requests.get(url, headers=headers)

# 获取页面源码
print(response.content.decode())
```

#### XPath

XPath即为XML路径语言（XML Path Language），它是一种用来确定XML文档中某部分位置的语言

XPath使用路径表达式来选取XML文档中的节点或者节点集

| 表达式   | 描述                                                     |
| -------- | -------------------------------------------------------- |
| nodename | 选取此节点的所有子节点                                   |
| /        | 从根节点选取                                             |
| //       | 从匹配选择的当前节点选择文档中的节点，而不考虑他们的位置 |
| .        | 选取当前节点                                             |
| ..       | 选取当前节点的父节点                                     |
| @        | 选取属性                                                 |

**谓语（条件过滤）**

谓语用来查找某个特定的节点或者包含某个指定的值的节点，被嵌在方括号中

| 路径表达式           | 结果                                              |
| -------------------- | ------------------------------------------------- |
| /ul/li[1]            | 选取属于ul子元素的第一个li元素                    |
| /ul/li[last()]       | 选取属于ul子元素的最后一个li元素                  |
| /ul/li[last()-1]     | 选取属于ul子元素的倒数第二个li元素                |
| /ul/li[position()<3] | 选取最前面的两个属于ul元素的子元素的li元素        |
| //div[@attr]         | 选取所有拥有名为attr的属性的div元素               |
| //div[@attr='leng']  | 选取所有div元素，且这些元素拥有值为leng的attr属性 |

**选取未知节点**

| 通配符 | 描述             |
| ------ | ---------------- |
| *      | 匹配任何元素节点 |
| @*     | 匹配任何属性节点 |

**模糊匹配**

```python
[contains(@属性,值)]

# 选取属性为attr，并且值为100的所有元素
//li[contains(@attr,100)]
```

**获取数据**

```python
# 获取文本数据
/div/ul/li/span/text()

# 获取属性值
/div//li/a/@href
```

**Python中使用XPath**

安装lxml：pip install lxml

```python
from lxml import etree
page = open('baidu.html', 'r', encoding='utf-8').read()
html = etree.HTML(page)
result = html.xpath('//div')
```

### FastAPI

| 对比维度 | FastAPI             | Flask       | Django       |
| -------- | ------------------- | ----------- | ------------ |
| 性能     | 高（异步支持）      | 中          | 较低（同步） |
| 异步支持 | 内置aync/await      | 需扩展      | 不原生       |
| 数据验证 | Pydantic自动校验    | 手动处理    | ORM极验证    |
| 自动文档 | 自动生成            | 需插件      | 需扩展       |
| 适用场景 | API、微服务、AI推理 | 小型Web项目 | 大型网站     |

#### 参数

+ 路径参数

  ```python
  from fastapi import FastAPI, Path
  
  app = FastAPI()
  
  # 路径参数
  @app.get("/book/{id}")
  async def get_book(id: int):
      return {"id": id, "title": f"这是第{id}本书"}
  
  # 类型注解Path()
  @app.get("/book/{id}")
  async def get_book(id: int = Path(..., gt=0, lt=101, description="书籍id，取值范围1-100")):
      return {"id": id, "title": f"这是第{id}本书"}
  ```

  | Path参数              | 说明          |
  | --------------------- | ------------- |
  | ...                   | 必填          |
  | gt/ge                 | 大于/大于等于 |
  | lt/le                 | 小于/小于等于 |
  | description           | 描述          |
  | min_length/max_length | 长度限制      |

+ 查询参数

  ```python
  from fastapi import FastAPI, Query
  
  app = FastAPI()
  
  # 查询参数
  # http://127.0.0.1:8000/news/news_list?skip=0&limit=10
  @app.get("/news/news_list")
  async def get_book(skip:int, limit: int=10):
      return {"skip": skip, "limit": limit}
  
  # Query注解，同Path()
  @app.get("/news/news_list")
  async def get_book(
      skip: int = Query(0, description="跳过的记录数"),
      limit: int = Query(10, description="返回的记录数")
  ):
      return {"skip": skip, "limit": limit}
  ```

+ 请求体参数

  ```python
  from pydantic import BaseModel, Field
  
  class User(BaseModel):
      username: str
      password: str
  
  # 类型注解Field
  class User(BaseModel):
      username: str = Field(default="张三", min_length=2, max_length=10)
      password: str = Field(min_length=3, max_length=20)
  ```

  ```python
  from fastapi import FastAPI
  
  app = FastAPI()
  
  @app.post("/register")
  async def register(user: User):
      return user
  ```

#### 响应类型

默认情况下，FastAPI会自动将路径操作函数返回的Python对象（字典、列表、Pydantic模型等），经由jsonable_encoder转换为JSON兼容格式，并包装为JSONResponse返回。

如果需要返回非JSON数据（如HTML、文件流），FastAPI提供了丰富的响应类型来返回不同数据

| 响应类型          | 用途                   | 示例                              |
| ----------------- | ---------------------- | --------------------------------- |
| JSONResponse      | 默认响应，返回JSON数据 | return {"key": "value"}           |
| HTMLResponse      | 返回HTML内容           | return HTMLResponse(html_content) |
| PlainTextResponse | 返回纯文本             | return PlainTextResponse("text")  |
| FileResponse      | 返回文件下载           | return FileResponse(path)         |
| StreamingResponse | 流式响应               | 生成器函数返回数据                |
| RedirectResponse  | 重定向                 | return RedirectResponse(url)      |

**响应类型设置方式**

+ 装饰器中指定响应类

  ```python
  from fastapi.responses import HTMLResponse
  
  @app.get("/html", response_class=HTMLResponse)
  async def get_html():
      return "<h1>这是标题</h1>"
  ```

+ 返回响应对象

  ```python
  from fastapi.responses import FileResponse
  
  @app.get("/file")
  async def get_file():
      file_path = "./files/1.jpeg"
      reutrn FileResponse(file_path)
  ```

**自定义响应数据格式**

response_model是路径操作装饰器（如@app.get或@app.post）的关键参数，它通过一个Pydantic模型来严格定义和约束API端点的输出格式。这一机制在提供自动数据验证和序列化的同时，更是保障数据安全性的第一道防线

```python
from pydantic import BaseModel

class News(BaseModel):
    id: int
    title: str
    content: str
    
@app.get("/news/{id}", response_model=News)
async def get_news(id: int):
    return {
        "id": id,
        "title": f"这是第{id}本书",
        "content": "这是一本好书"
    }
```

#### 异常处理

对于客户端引发的错误（4xx，如资源未找到、认证失败），应使用fastapi.HTTPException来中断正常处理流程，并返回标准错误响应

```python
from fastapi import FastAPI, HTTPException

app = FastAPI()

@app.get("/news/{id}")
async def get_news(id: int):
    id_list = [1, 2, 3, 4, 5, 6]
    if id not in id_list:
        raise HTTPException(status_code=404, detail="当前id不存在")
    return {"id": id}
```

#### 中间件

中间件（Middleware）是一个在每次请求进入FastAPI应用时都会被执行的函数

它在请求到达实际的路径操作（路由处理函数）之前运行，并且在响应返回给客户端之前再运行一次

中间件：函数的顶部使用装饰器@app.middleware("http")

```python
@app.middleware("http")
async def middleware(request, call_next):
    print("中间件开始处理 -- start")
    response = await call_next(request)
    print("中间件处理完成 -- end")
    return response
```

#### 依赖注入

使用依赖注入系统来共享通用逻辑，避免代码重复

依赖项：可重用的组件（函数/类），负责提供某种功能或数据

FastAPI自动帮你调用依赖项，并将结果注入到路径操作函数中

优点：代码复用、解耦、易于测试

**使用流程**

1. 创建依赖项

   ```python
   async def common_parameters(
       skip: int = Query(0, ge=0),
       limit: int = Query(10, le=60)
   ):
       return { "skip": skip, "limit": limit}
   ```

2. 导入Depends：from fastapi import Depends

3. 声明依赖项

   ```python
   @app.get("news/news_list")
   async def get_news_list(commons = Depends(common_parameters)):
       return commons
   ```

#### ORM

ORM（Object-RelationalMapping，对象关系映射）是一种编程技术，用于在面向对象编程语言和关系型数据库之间建立映射。它允许开发者通过操作对象的方式与数据库进行交互，而无需直接编写复杂的SQL语句

| ORM工具        | 特点                     | 适用场景                  |
| -------------- | ------------------------ | ------------------------- |
| SQLAlchemy ORM | 功能最强、最灵活、企业级 | 各类API、微服务、数据应用 |
| Django ORM     | 封装好、上手快           | Django项目、管理后台      |
| Tortoise ORM   | 全异步                   | 异步Web服务、高并发API    |

**ORM使用流程**

1. 安装：pip install sqlalchemy[asyncio] aiomysql

2. 建库、建表

   1. 创建数据库引擎

      ```python
      from sqlalchemy.ext.asyncio import create_async_engine
      
      ASYNC_DATABASE_URL = "mysql+aiomysql://root:123456@localhost:3306/fastapi_test?charset=utf8"
      
      # 创建异步引擎
      async_engine = create_async_engine(
          ASYNC_DATABASE_URL,
          echo=True,  # 可选：输出SQL日志
          pool_size=10,  # 设置连接池中保持的持久连接数
          max_overflow=20  # 设置连接池允许创建的额外连接数
      )
      ```

   2. 定义模型类

      ```python
      from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column
      
      # 基类，继承DeclarativeBase（包含通用属性和字段的映射）
      class Base(DeclarativeBase):
          create_time: Mapped[datetime] = mapped_column(DateTime, insert_default=func.now(), default=datetime.now, comment="创建时间")
          update_time: Mapped[datetime] = mapped_column(DateTime, insert_default=func.now(), onupdate=func.now(), default=datetime.now, comment="修改时间")
              
      # 数据库表对应的模型类
      class Book(Base):
          __tablename__="book"
          
          id: Mapped[int] = mapped_column(primary_key=True, comment="书籍id")
          bookname: Mapped[str] = mapped_column(String(255), comment="书名")
          author: Mapped[str] = mapped_column(String(255), comment="作者")
          price: Mapped[float] = mapped_column(Float, comment="价格")
          publisher: Mapped[str] = mapped_column(String(255), comment="出版社")
      ```

   3. 启动应用时建表

      ```python
      # FastAPI启动的时候调用建表的函数
      async def create_tables():
          async with async_engine.begin() as conn:
              await conn.run_sync(Base.metadata.create_all)
              
      @app.on_event("startup")
      async def startup_event():
          await create_tables()
      ```

   **路由匹配中使用ORM**

   核心：创建依赖项，使用Depends注入到处理函数

   ```python
   # 创建异步会话工厂
   AsyncSessionLocal = async_sessionmaker(
       bind=async_engine,  # 绑定数据库引擎
       class_=AsyncSession,  # 指定会话类
       expire_on_commit=False  # 会话对象不过期，不重新查询数据库
   )
   
   # 依赖项，用于获取数据库会话
   async def get_database():
       async with AsyncSessionLocal() as session:
           try:
               yield session  # 返回数据库会话给路由处理函数
               await session.commit()  # 无异常，提交事务
           except Exception:
               await session.rollback()  # 有异常则回滚
               raise
           finally:
               await session.close()  # 关闭会话
               
   #
   @app.get("/book/books")
   async def get_book_list(db: AsyncSession = Depends(get_database)):
       # 查询所有书籍
       result = await db.execute(select(Book))  # Book模型类
       book = result.scalars().all()
       return book
   ```

3. 操作数据

   1. 查询

      核心语句：await db.execute(select(模型类))，返回一个ORM对象

      获取所有数据：scalars.all()

      获取单条数据：scalars().first()、get(模型类，主键值)

      ```python
      @app.get("/book/books")
      async def get_book_list(db: AsyncSession = Depends(get_database)):
          
          # result = await db.execute(select(Book))  # 查询，返回一个ORM对象
          # book = result.scalars().all()  # 获取所有
          # book = result.scalars().first()  # 获取第一条
          book = await db.get(Book, 5)  # 获取单条数据，根据主键
          return book
      ```

      **查询条件**

      select(Book).where(条件, 条件2, ...)

      条件：

      + 比较判断：==、>、<、>=、<=
      + 模糊查询：like()
      + 与非查询：&、|、~
      + 包含查询：in_()

      ```python
      @app.get("/book/get_book/{book_id}")
      async def get_book_list(book_id: int, db: AsyncSession = Depends(get_database)):
          result = await db.execute(select(Book).where(Book.id == book_id))
          book = result.scalar_one_or_none()
          return book
      ```

      **聚合查询**

      func.方法(模型类, 属性)

      + count：统计行数量
      + avg：求平均值
      + max：求最大值
      + min：求最小值
      + sum：求和

      ```python
      @app.get("/book/count")
      async def get_count(db: AsyncSession = Depends(get_database)):
          result = await db.execute(select(func.avg(Book.price)))
          count = result.scalar()
          return count
      ```

      **分页查询**

      select().offset().limit()

      + offset：跳过的记录数
      + limit：返回的记录数

      ```python
      @app.get("/book/get_books")
      async def get_book_list(
          page: int = 1,
          page_size: int = 3,
          db: AsyncSession= Depends(get_database)
      ):
          skip = (page - 1) * page_size
          stmt = select(Book).offset(skip).limit(page_size)
          result = await db.execute(stmt)
          books = result.scalars().all()
          return {"books": books}
      ```

   2. 新增

      核心步骤：定义ORM对象 -> 添加对象到事务：add(对象) -> commit提交到数据库

      ```python
   from pydantic import BaseModel
      
      class BookBase(BaseModel):
          id: int
          bookname: str
          author: str
          price: str
          publisher: str
                  
      @app.post("/book/add_book")
      async def add_book(book: BookBase, ab: AsyncSession = Depend(get_database)):
          # 获取book参数，创建图书对象（__dict__返回book对象的属性字典）
          book.obj = Book(**book.__dict__)
          db.add(book_obj)
          await db.commit()
          return book
      ```
   
   3. 更新
   
      核心步骤：查询get -> 属性重新赋值 -> commit提交到数据库
   
      ```python
      from pydantic import BaseModel
      
      class BookUpdate(BaseModel):
          bookname: str
          author: str
          price: str
          publisher: str
              
      @app.put("/book/update_book/{book_id}")
      async def update_book(book_id: int, data: BookUpdate, db: AsyncSession = Depends(get_database)):
          # 1.查询
          book = await db.get(Book, book_id)
          if book is None:
              raise HTTPException(status_code=404, detail="Book not found")
          # 2.修改属性
          book.bookname = data.bookname
          book.author = dat.author
          book.price = data.price
          # 3.提交
          await db.commit()
          return book
      ```
   
   4. 删除
   
      核心步骤：查询get -> delete删除 -> commit提交到数据库
   
      ```python
      @app.delete("/book/delete_book/{book_id}")
      async def delete_book(book_id: int, db： AsyncSession = Depends(get_database)):
          db_book = await db.get(Book, book_id)
          if db_book is None:
              raise HTTPException(status_code=404, detail="Book not found")
          await db.delete(db_book)
          await db.commit()
          return {"message": "Book deleted"}
      ```

### LangChain

```python
# .env文件
DEEPSEEK_API_KEY=apikey
DEEPSEEK_BASE_URL=https://api.deepseek.com
    

# env_utils.py
import os

from dotenv import load_dotenv

# 从.env文件加载环境变量
load_dotenv(override=True)

# 加载deepseek环境变量
DEEPSEEK_API_KEY = os.getenv("DEEPSEEK_API_KEY")
DEEPSEEK_BASE_URL = os.getenv("DEEPSEEK_BASE_URL")


# my_llm.py
from langchain_deepseek import ChatDeepSeek

from env_utils import DEEPSEEK_BASE_URL, DEEPSEEK_API_KEY

deepseek_llm = ChatDeepSeek(
    model="deepseek-v4-flash",
    api_key=DEEPSEEK_API_KEY,
    base_url=DEEPSEEK_BASE_URL
)


# quick_start.py
from langchain.agents import create_agent
from langchain_core.tools import tool

from my_llm import deepseek_llm

@tool
def get_weather(location: str) -> str:
    """
    获取指定位置的天气信息
    """
    return f"天气信息：{location}的天气是晴朗的"

agent = create_agent(
    model=deepseek_llm,
    tools=[get_weather],
    system_prompt="你是一个天气助手，你可以帮助用户获取指定位置的天气信息。"
)

# 调用agent
resp = agent.invoke({"messages": [{"role": "user", "content": "北京的天气"}]})
print(type(resp))
print(resp)
```

#### model

##### 模型初始化

+ 使用特定的Model Class

  ```python
  deepseek_llm = ChatDeepSeek(
      model="deepseek-v4-flash",
      api_key=DEEPSEEK_API_KEY,
      base_url=DEEPSEEK_BASE_URL
  )
  
  print(deepseek_llm.invoke("请介绍一下你自己"))
  ```

+ 使用统一的init_chat_model函数（建议）

  ```python
  deepseek_llm = init_chat_model(
      model="deepseek-v4-flash",
      model_provider="deepseek",
      api_key=DEEPSEEK_API_KEY,
      base_url=DEEPSEEK_BASE_URL
  )
  print(deepseek_llm.invoke("请介绍一下你自己"))
  ```

##### 模型初始化的参数

| 参数           | 类型   | 参数描述                                                     |
| -------------- | ------ | ------------------------------------------------------------ |
| model          | string | 指定要使用的模型标识符                                       |
| model_provider | string | 模型提供商名称                                               |
| api_key        | string | 用于身份验证的API密钥。建议通过环境变量设置，避免硬编码      |
| base_url       | string | 指定API端点                                                  |
| temperature    | number | 控制输出的随机性，值越低，结果越确定、保守；值越高，结果越多样、有创意，范围0.0-2.0，默认0.7 |
| max_tokens     | number | 限制模型相应生成的最大令牌数，有效控制回复长度               |
| timeout        | number | 设置等待模型响应的最大时间（秒），超时则取消请求             |
| max_retries    | number | 定义请求失败（如网络问题、速率限制）时的最大重试次数，提高鲁棒性，默认6 |

Token：并非简单的字或词，而是大模型通过分词器（Tokenizer）将输入文本拆分后的最小词义单元。不同的模型采用不同的分词算法，因此同一段文本在不同模型中的Token数量可能不同

+ 英文Token估算：一个Token约对应0.75个英文单词或4个字符
+ 中文Token估算：一个token通常对应1~2个汉字，但优化较好的模型（如通义千问、文心一言）约1:1

##### 模型调用

| 核心方法  | 主要特点                    | 适用场景                                           |
| --------- | --------------------------- | -------------------------------------------------- |
| Invoke()  | 阻塞式，一次性返回完整结果  | 简单问答、批处理任务、无需实时反馈的场景           |
| ainvoke() | 非阻塞，提高系统吞吐量      | 高并发Web应用、IO密集型任务                        |
| stream()  | 流式输出，实时返回每个token | 聊天机器人、长文本生成、需要提升用户体验的交互应用 |
| astream() | 非阻塞，提高系统吞吐量      | 高并发Web应用、IO密集型任务                        |
| batch()   | 批量处理多个输入            | 高并发场景，需要同时处理大量请求                   |
| abatch()  | 非阻塞，提高系统吞吐量      | 高并发Web应用、IO密集型任务                        |

+ Invoke调用模型

  ```python
  # 单条消息调用模型
  resp = deepseek_llm.invoke("请介绍一下自己")
  print(resp.content)
  
  # 字典格式的消息列表
  conversations = [
      {"role": "system", "content": "你是一个翻译助手，可以将汉语翻译成英语"},
      {"role": "user", "content": "翻译：我喜欢编程"},
      {"role": "assistant", "content": "I like programming"},
      {"role": "user", "content": "翻译：我喜欢大模型"}
  ]
  
  resp = deepseek_llm.invoke(conversations)
  print(resp.content)
  
  # 消息对象格式的消息列表（建议）
  conversations = [
      SystemMessage(content="你是一个翻译助手，可以将汉语翻译成英语"),
      HumanMessage(content="翻译：我喜欢编程"),
      AIMessage(content="I like programming"),
      HumanMessage(content="翻译：我喜欢大模型")
  ]
  
  resp = deepseek_llm.invoke(conversations)
  print(resp.content)
  ```

  Message types有四种

  + SystemMessage：用于在对话开始时为模型设置角色、行为准则和上下文背景。它像是给AI助手的一份工作说明书，决定了其回答问题的风格、领域和专业范围
  + HumanMessage：代表用户的输入，包含简单的文本问题，也可以是复杂的多模态内容（如图片、音频、文档等），在多轮对话中，它表示用户的一次发言
  + AIMessage：代表模型的输出或回复，包括生成的文本、工具调用、元数据等
  + ToolMessage：将工具执行的结果返回给模型，让模型基于这个结果继续生成回复

+ 流式调用模型

  ```python
  response = deepseek_llm.stream("请介绍一下你自己")
  for chunk in response:
      print(type(chunk))
      print(chunk.content, end="|", flush=True)
  ```

+ 批量调用模型

  ```python
  resp = deepseek_llm.batch([
      "请介绍一下你自己",
      "飞机为什么会飞",
      "什么是大模型"
  ])
  for item in resp:
      print(item.content)
  
  resp = deepseek_llm.batch_as_completed([
      "请介绍一下你自己",
      "飞机为什么会飞",
      "什么是大模型"
  ], config={
      "max_concurrency": 3
  })
  for item in resp:
      print(item)
  ```

+ 异步调用模型

  ainvoke、astream、abatch，避免阻塞主线程，优化资源利用

  ```python
  async def demo_async_invoke():
      print("程序开始")
      print("发起异步请求调用")
      async_task = deepseek_llm.ainvoke("用一句话解释人工智能")
  
      print("模型请求已发出，程序无需等待，继续执行")
      for i in range(3):
          time.sleep(1)
          print(f"正在执行第{i + 1}个任务")
  
      print("其他任务已完成，开始处理流式结果")
      response = await async_task
      print(f"模型返回:{response.content}")
  
  async def demo_async_stream():
      print("程序开始")
      print("发起异步请求调用")
      stream_resp = deepseek_llm.stream("请一句话解释机器学习的基本概念")
  
      print("流式请求已发出，程序无需等待，继续执行")
      for i in range(3):
          time.sleep(1)
          print(f"正在执行第{i + 1}个任务")
  
      print("其他任务已完成，开始处理流式结果")
      async for chunk in stream_resp:
          if hasattr(chunk, "content"):
              print(chunk.content, end="", flush=True)
      print("流式输出结束")
  
  async def demo_async_batch():
      print("程序开始")
      print("发起异步批量调用")
      batch_resp = deepseek_llm.batch("请一句话说明深度学习与传统机器学习的区别")
  
      print("批量请求已发出，程序无需等待，继续执行")
      for i in range(3):
          time.sleep(1)
          print(f"正在执行第{i + 1}个任务")
  
      print("其他任务已完成，开始处理批量结果")
      responses = await batch_resp
      for response in responses:
          print(f"批量响应：:{response.content}")
  ```

##### content_blocks

它的核心目标是提供一种跨模型供应商、标准化的多模态数据结构

+ 数据结构：list[TypeDict]
+ 统一格式：每个block都有一个type字段，用于区分内容类型
+ 支持类型：text（文本）、image（图片）、audio（音频）、video（视频）、tool_call（工具调用）、reasoning（推理/思维链）

输入格式化：

```python
def encode_image(img_path, img_type='jpeg'):
    """将一张本地图片转换为Base64编码的Data URI字符串，方便在文本中嵌入图片数据"""
    with open(img_path, "rb") as img_file:
        return f"data:image/{img_type};base64,{base64.b64encode(img_file.read()).decode("utf-8")"
    
img_path = "image_test.png"

base64_image = encode_image(img_path)

response = model.invoke([
    # openai写法，不同模型写法可能不同
    HumanMessage(
        content=[
            {'type': 'text', 'text': '这张图里有什么？'},
            {
                'type': 'image_url',
                "image_url": base64_image
            }
        ]
    )
    # 推荐的统一写法，输入格式化
    HumanMessage(
        content_blocks=[
            {'type': 'text', 'text': '这张图里有什么？'},
            {
                'type': 'image',
                'base64': base64_image,
                'mime_type': 'image/png'
            }
        ]
    )
])

print(response.content)
# 输出格式化
print(response.content_blocks)
```

##### 提示词模板

在LangChain开发中，构造提示词既可以直接使用Python字符串拼接（如f-string、format()或+），也可以使用LangChain提供的PromtTemplate或ChatPromptTemplate

| 特性     | PromptTemplate | ChatPromptTemplate    |
| -------- | -------------- | --------------------- |
| 输出格式 | 纯文本字符串   | 消息列表              |
| 角色支持 | 无             | system/user/assistant |
| 对话历史 | 不支持         | 支持                  |
| 适用场景 | 简单提示       | 聊天、对话、多轮交互  |

因此，用于生成消息列表的ChatPromptTemplate，也自然取代了生成字符串的PromptTemplate，成为构建现代LangChain应用的首选工具

两种实例化方式：

+ 调用from_messages()（推荐）

  ```python
  chat_prompt_template = ChatPromptTemplate.from_messages([
      ("system", "你是一个友好的AI助手，你的名字叫{name}"),
      ("human", "你好，最近怎么样？"),
      ("ai", "我很好，谢谢"),
      ("human", "{user_input}")
  ])
  
  result = chat_prompt_template.invoke({"name": "小智", "user_input": "2 + 2 = ?"})
  print(result)
  
  response = model.invoke(result)
  ```

+ 使用实例初始化方法

  ```python
  chat_prompt_template = ChatPromptTemplate([
      ("system", "你是一个友好的AI助手，你的名字叫{name}"),
      ("human", "你好，最近怎么样？"),
      ("ai", "我很好，谢谢"),
      ("human", "{user_input}")
  ])
  
  result = chat_prompt_template.invoke({"name": "小智", "user_input": "2 + 2 = ?"})
  print(result)
  
  response = model.invoke(result)
  ```

ChatPromptTemplate的调用：

+ invoke()

  ```python
  # 参数类型：字典列表，返回值类型：ChatPromptValue
  result = chat_prompt_template.invoke({"name": "小智", "user_input": "2 + 2 = ?"})
  ```

+ format()

  ```python
  # 参数类型：变量值，返回值类型：字符串
  result = chat_prompt_template.format(name="小智", user_input= "2 + 2 = ?")
  ```

+ format_messages()

  ```python
  # 参数类型：变量值，返回值类型：消息列表
  result = chat_prompt_template.format_messages(name="小智", user_input= "2 + 2 = ?")
  ```


更丰富的初始化参数类型：

+ 字符串列表

  ```python
  chat_prompt_template = ChatPromptTemplate.from_messages([
      "你好，我是{name}"
  ])
  result = chat_prompt_template.invoke({"name": "小明"})
  ```

+ 元组列表

  ```python
  chat_prompt_template = ChatPromptTemplate.from_messages([
      ("human", "你好，我是{name}")
  ])
  result = chat_prompt_template.invoke({"name": "小明"})
  ```

+ 字典列表

  ```python
  chat_prompt_template = ChatPromptTemplate.from_messages([
      {"role": "human", "content": "你好，我是{name}"}
  ])
  result = chat_prompt_template.invoke({"name": "小明"})
  ```

+ 消息对象列表

  ```python
  # 消息对象中不能声明变量
  chat_prompt_template = ChatPromptTemplate.from_messages([
      HumanMessage(content="你好，我是小明")
  ])
  result = chat_prompt_template.invoke({})
  ```

+ BaseMessagePromptTemplate参数列表

  ```python
  human_mess_template = HumanMessagePromptTemplate.from_template("你好，我是{name}")
  
  chat_prompt_template = ChatPromptTemplate.from_messages([
      human_mess_template
  ])
  result = chat_prompt_template.invoke({"name": "小明"})
  ```

+ BaseChatPromptTemplate参数列表

  ```python
  inner_chat_template = ChatPromptTemplate.from_template([
      ("human","你好，我是{name}")
  ])
  
  chat_prompt_template = ChatPromptTemplate.from_messages([
      inner_chat_template
  ])
  result = chat_prompt_template.invoke({"name": "小明"})
  ```

高级特性：

+ 部分变量预填充：partial()

  预填充某些固定不变的变量，创建模板的变体

  ```python
  template = ChatPromptTemplate.from_messages([
      ("system": "你是{role}，目标用户是{audience}"),
      ("user": "{task}")
  ])
  
  final_template = template.partial(role="导游", audience="游客")
  
  result1 = final_template.invoke({"task": "介绍一下北京的故宫"})
  result2 = final_template.invoke({"task": "介绍一下北京的颐和园"})
  ```

+ 消息占位符

  使用场景：多轮对话系统存储历史消息以及Agent的中间步骤处理此功能非常有用

  ```python
  # 使用placeholder
  template = ChatPromptTemplate.from_messages([
      ("system", "我是一个AI助手"),
      ("placeholder", "{conversation}")
  ])
  
  result = template.invoke({
      "conversation" : [
          ("human", "你好，请问明天的天气如何？"),
          ("ai", "明天天气晴朗"),
          ("human", "后天的天气怎么样？")
      ]
  })
  
  # 使用MessagePlaceholder
  template = ChatPromptTemplate.from_messages([
      ("system", "我是一个AI助手"),
      MessagePlaceholder(variable_name="conversation")
  ])
  
  result = template.invoke({
      "conversation" : [
          ("human", "你好，请问明天的天气如何？"),
          ("ai", "明天天气晴朗"),
          ("human", "后天的天气怎么样？")
      ]
  })
  ```

+ 可复用模板库

  ```python
  class PromptLibrary:
      """可复用的提示词模板库"""
      
      TRANSLATOR = ChatPromptTemplate.from_messages({
          ("system", "你是专业翻译，精通{source_lang}和{target_lang}"),
          ("user", "翻译以下文本：\n{text}")
      })
      
      CODE_REVIEWER = ChatPromptTemplate.from_messages({
          ("system", "你是{language}代码审查专家，重点关注{focus}"),
          ("user", "审查代码：\n```{language}\n{code}\n```")
      })
      
  # 调用
  messages = PromptLibrary.TRANSLATOR.format_messages(
      source_lang="英语",
      target_lang="中文",
      text="Hello World"
  )
  ```

+ 模板组合

  将多个模板片段组合成复杂的提示词

  ```python
  # 方法1：字符串组合
  role_part = "你是一个{domain}专家"
  style_part = "回答风格：{style}"
  constraint_part = "限制：{constraint}"
  
  full_system = role_part + style_part + constraint_part
  
  template = ChatPromptTemplate.from_messags({
      ("system", full_system),
      ("user", "{question}")
  })
  
  # 方法2：使用+运算符
  template1 = ChatPromptTemplate.from_messags({
      ("system", "你是助手"),
  })
  
  template2 = ChatPromptTemplate.from_messags({
      ("user", "{input}"),
  })
  
  combined = template1 + template2
  ```

##### 模型结构化输出

+ Pydantic模型（推荐）

  Python的Pydantic库定义强类型数据模型，支持复杂嵌套结构

  ```python
  # 定义一个Pydantic模型，用于结构化输出简单对象
  class Movie(BaseModel):
      title: str = Field(description="电影标题")
      year: int = Field(description="电影上映年份")
      director: str = Field(description="电影导演")
      rating: float = Field(description="电影评分")
  
  model_with_structured_output = deepseek_llm.with_structured_output(Movie)
  resp = model_with_structured_output.invoke("介绍下电影《泰坦尼克号》, 不超过十个字，禁止返回电影年份和导演任何信息")
  
  print(type(resp))
  print(resp)
  
  # 返回嵌套对象
  class Actor(BaseModel):
      name: str = Field(description="演员姓名")
      role: str = Field(description="演员在电影中的角色")
      
  class Movie(BaseModel):
      title: str = Field(description="电影标题")
      year: int = Field(description="电影上映年份")
      director: str = Field(description="电影导演")
      rating: float = Field(description="电影评分")
      cast: list[Actor] = Field(description="电影演员列表")
  
  model_with_structured_output = deepseek_llm.with_structured_output(Movie)
  resp = model_with_structured_output.invoke("介绍下电影《泰坦尼克号》, 不超过十个字，禁止返回电影年份和导演任何信息")
  
  print(type(resp))
  print(resp)
  ```

  Pydantic高级特性

  ```python
  # 1.可选字段，没有就为None
  class Person(BaseModel):
      age: Optional[int] = Field(description="年龄")
          
  # 2.默认值，不同的模型商，对于此字段的支持是不同的
  class Person(BaseModel):
      age: int = Field(10, description="年龄")
          
  # 3.枚举类型
  # 方式1
  class Priority(str, Enum):
      LOW="低",
      MEDIUM="中",
      HIGH="高"
  
  class Task(BaseModel):
      priority: Priority  # 只能是LOW/MEDIUM/HIGH
       
  # 方式2
  class Task(BaseModel):
      priority: Literal["低", "中", "高"]
          
  # 4.列表提取
  class Person(BaseModel):
      name: str = Field(description="姓名")
         
  class PersonList(BaseModel):
      people: List[Person]
        
  structured_model = model.with_structured_output(PersonList)
  
  # 5.嵌套结构
  class Address(BaseModel):
      city: str = Field(description="城市")
      district: str = Field(description="区域")
          
  class Company(BaseModel):
      name: str = Field(description="公司名称")
      address: Address = Field(description="公司所在地")
        
  structured_model = model.with_structured_output(Company)
  
  # 6.限制条件
  class User(BaseModel):
      name: str = Field(description="姓名", min_length=2, max_length=50)
      age: int = Field(description="年龄", le=150)
  ```

+ TypedDict

  TypedDict是Python 3.8+引入的一种类型提示工具，它允许为字典对象定义固定的键名和对应的值类型

  ```python
  # 使用TypedDict定义简单结构化输出模型
  # Annotated用来在类型之外，再附加一些额外信息，即元数据，类似Pydantic的Field
  class Movie(TypedDict):
      title: Annotated[str, ..., "电影标题"]  # ...表示必须要填充这个字段
      year: Annotated[int, "电影上映年份"]
      director: Annotated[str, "电影导演"]
      rating: Annotated[float, "电影评分"]
  
  model_with_structured_output = deepseek_llm.with_structured_output(Movie)
  resp = model_with_structured_output.invoke("介绍下电影《泰坦尼克号》")
  
  print(type(resp))
  print(resp)
  
  # 返回嵌套对象
  class Actor(TypedDict):
      name: Annotated[str, "演员姓名"]
      role: Annotated[str, "演员在电影中的角色"]
  
  class Movie(TypedDict):
      title: Annotated[str, "电影标题"]
      year: Annotated[int, "电影上映年份"]
      director: Annotated[str, "电影导演"]
      rating: Annotated[float, "电影评分"]
      cast: Annotated[list[Actor], "电影演员列表"]
  
  model_with_structured_output = deepseek_llm.with_structured_output(Movie)
  resp = model_with_structured_output.invoke("介绍下电影《泰坦尼克号》")
  
  print(type(resp))
  print(resp)
  ```

+ JsonSchema

  类型提示工具，它允许为字典对象定义固定的键名和对应的值类型

  ```python
  # 定义jsonSchema结构化输出模型
  json_schema = {
      "title": "MovieInfo",  # 不能使用中文
      "description": "电影信息",
      "type": "object",
      "properties": {
          "title": {"type": "string", "description": "电影标题"},
          "year": {"type": "integer", "description": "电影上映年份"},
          "director": {"type": "string", "description": "电影导演"},
          "rating": {"type": "number", "description": "电影评分"},
      },
      "required": ["title", "year", "director", "rating"]
  }
  
  model_with_structured_output = deepseek_llm.with_structured_output(json_schema)
  resp = model_with_structured_output.invoke("介绍下电影《泰坦尼克号》")
  
  print(type(resp))
  print(resp)
  
  # 返回嵌套对象
  json_schema = {
      "title": "MovieInfo",  # 不能使用中文
      "description": "电影信息",
      "type": "object",
      "properties": {
          "title": {"type": "string", "description": "电影标题"},
          "year": {"type": "integer", "description": "电影上映年份"},
          "director": {"type": "string", "description": "电影导演"},
          "rating": {"type": "number", "description": "电影评分"},
          "cast": {
              "type": "array",
              "items": {
                  "type": "object",
                  "properties": {
                      "name": {"type": "string", "description": "演员姓名"},
                      "role": {"type": "string", "description": "演员在电影中的角色"}
                  },
                  "required": ["name", "role"]
              },
              "description": "电影演员列表"
          },
      },
      "required": ["title", "year", "director", "rating", "cast"]
  }
  
  model_with_structured_output = deepseek_llm.with_structured_output(json_schema)
  resp = model_with_structured_output.invoke("介绍下电影《泰坦尼克号》")
  
  print(type(resp))
  print(resp)
  
  # 验证返回的对象是不是满足jsonSchema
  import jsonschema
  
  error = jsonSchema.validate(instance=resp, schema=json_schema)
  ```

+ @dataclass

  @dataclass是Python标准库dataclasses提供的类装饰器，用于简化以该字段为核心的数据类定义

  给类加上@dataclass后，Python会根据字段声明自动生成常用方法，例如：

  + __ init__
  + __ repr__
  + __  eq__

  ```python
  @dataclass
  class Movie():
      title: str = Field(description="电影标题")
          
  structured_model = model.with_structured_output(Movie)
  ```

##### 结构化输出结果

+ with_structured_output：大部分模型供应商都支持，不支持推理模型

+ 自定义解析器方式：支持推理模型

  ```python
  # 1. 定义结构
  class Movie(BaseModel):
      title: str = Field(description="电影标题")
      year: int = Field(description="上映年份")
  
  # 2. 设置提示词
  prompt = ChatPromptTemplate.from_template("""
  回答用户问题。
  问题：{question}
  你必须始终输出一个包含title(电影标题)和year(上映年份)的JSON对象
  """)
  
  # 3. 创建链
  chain = prompt | deepseek_llm | JsonOutputParser(pydantic_object=Movie)
  
  # 4. 调用（返回字典）
  response = chain.invoke({"question": "介绍电影《盗梦空间》"})
  print(response)
  ```

##### 模型调用工具（了解）

大模型工具调用（Tool Calling）可以扩展模型能力，使模型能够突破文本生成的限制，与外部系统和功能进行交互。在LangChain框架中，这一功能也被称为函数调用（Function Calling） 

```python
@tool(parse_docstring=True)
def get_weather(local: str) -> str:
    # description说明，AI依赖description来理解工具
    # 参数类型说明：如果在docstring中声明了参数的描述，则必须在函数声明处指明参数的类型
    """
    获取天气信息
    
    Args:
    	local: 具体的城市
    	
    Returns:
    	返回城市的天气
    """
    return f"在{local}天气非常晴朗"
```

模型调用单个工具

```python
# 1. 创建工具
@tool
def get_weather(local: str) -> str:
    """获取天气信息"""
    return f"在{local}天气非常晴朗"

# 2. 工具告诉给模型
model_with_tools = deepseek_llm.bind_tools([get_weather])

# 3. 准备messages
messages = []
humanMessage = HumanMessage(content="北京天气是什么")
messages.append(humanMessage)

# 4. 模型不会真正执行调用工具，只是知道要调用工具
response = model_with_tools.invoke(messages)
messages.append(response)

# 5. 获取工具调用信息
if response.tool_calls:
    for tool_call in response.tool_calls:
        # 打印工具调用信息
        if tool_call["name"] == "get_weather":
            # 手动调用工具
            tool_result = get_weather.invoke(tool_call)
            messages.append(tool_result)

# 6. 模型会根据工具调用结果，生成最终回复
print(messages)
final_response = model_with_tools.invoke(messages)
print(final_response)
```

模型调用多个工具

```python
# 1. 定义工具
# 定义股票查询工具
@tool
def get_stock_price(company: str, timeframe: str = "today") -> str:
    return ""

# 定义新闻搜索工具
@tool
def search_news(company: str) -> str:
    return ""

# 2. 模型绑定工具
model_with_tools = deepseek_llm.bind_tools([get_stock_price, search_news])


# 3. 创建消息，调用工具
messages = []
humanMessage = HumanMessage(content="苹果公司上周股价是多少？有什么新闻？")
messages.append(humanMessage)

while True:
    response = model_with_tools.invoke(messages)
    messages.append(response)

    # 如果有调用工具，处理工具调用响应
    # 开发者根据模型的响应，调用工具并获取结果
    if response.tool_calls:
        for tool_call in response.tool_calls:
            if tool_call["name"] == "get_stock_price":
                stock_result = get_stock_price.invoke(tool_call)
                messages.append(stock_result)
            if tool_call["name"] == "search_news":
                news_result = search_news.invoke(tool_call)
                messages.append(news_result)
    else:
        print("没有工具调用，直接返回答案")
        break


print(response)
print(response.content)
```

##### 模型其他内容

+ Reasoning-推理模型

  ```python
  deepseek_llm = init_chat_model(
      model="deepseek-reasoner",  # 推理模型
      model_provider="deepseek",
      api_key=DEEPSEEK_API_KEY,
      base_url=DEEPSEEK_BASE_URL
  )
  
  resp = deepseek_llm.invoke("我有5个苹果，吃了1个，还剩几个？")
  # resp中的reasoning_content是推理模型独有的属性
  print(resp)
  ```

+ Rate limiting-速率限制

  ```python
  rate_limiter = InMemoryRateLimiter(
      requests_per_second=0.1,  # 每10秒最多一个请求
      check_every_n_seconds=0.1,  # 检查间隔0.1秒
      max_bucket_size=5  # 应对流量高峰最多允许的请求个数
  )
  
  deepseek_llm = init_chat_model(
      model="deepseek-v4-flash",
      model_provider="deepseek",
      api_key=DEEPSEEK_API_KEY,
      base_url=DEEPSEEK_BASE_URL,
      rate_limiter=rate_limiter
  )
  
  for i in range(3):
      response = deepseek_llm.invoke("你好")
      print(response.content)
      print(response)
  ```

+ Invocation Config-调用配置

  在调用模型时（如使用invoke()，ainvoke()，stream()等方法时），我们可以传入config参数（类型为RunnableConfig）允许在运行时（即调用模型时）动态地配置和控制模型的行为，而无需在初始化时就固定所有参数，这为应用带来了极大的灵活性和可维护性

  ```python
  deepseek_llm.invoke(
      "你好",
      config={
          "run_name": "...",  # 在LangSmith中这行运行会显示为指定名称
          "tags": ["tag1", "tag2"],  # 打上标签便于分类查找
          "metadata": {"user_id": "123"},  # 记录用户id
          "callbacks": [cuntom_handler],  # 启用自定义回调函数
          "configurable": {
              "model": "deepseek-reasoner",  # 配置模型参数
              "temperature": 0.7,  # 配置温度参数
              "max_token": 100  # 配置最大令牌数
          }
      }
  )
  ```

  config中支持配置的参数如下：

  | 配置项          | 类型                      | 描述                                                         |
  | --------------- | ------------------------- | ------------------------------------------------------------ |
  | run_name        | str                       | 为当前运行设置一个可读的名称，如在LangSmith追踪系统中快速定位和识别不同的运行任务 |
  | tags            | List[str]                 | 为运行设置标签，用于分类和过滤。如在LangSmith追踪系统中快速定位和识别不同的运行任务 |
  | metadata        | Dict[str,Any]             | 附加任意的键值对元数据。记录本次调用的业务上下文             |
  | callbacks       | List[BaseCallbackHandler] | 设置回调处理器，在运行的不同的阶段（开始、流输出、结束等）触发，与一些监控平台（如LangSmith）集成进行深度追踪和调试 |
  | max_concurrency | int                       | 限制当前可允许对象的最大并发运行数。防止对API接口或本地资源造成过大压力，实现简单的速率限制 |
  | recursion_limit | int                       | 限制运行时递归调用的最大深度。主要在复杂工作流（如Agent执行多步工具调用）中，防止出现无限递归循环 |
  | configurable    | Dict[Str,Any]             | 一个万能字典，用于传递其他可配置参数。实现更高级的动态行为，如配置可替代的模型或组件 |

  以上configurable中可配置的参数与init_chat_model初始化模型参数一样，与在初始化模型时设置的参数（如temperature=0.7）的关键区别在于：

  + 初始化参数：是模型的默认设置，适用于该模型实例的大部分场景
  + 运行时config：是单次调用的特定设置，优先级更高，允许针对本次调用进行特殊调整

#### Agent

![](/img/langchain_1.png)

在LangChain框架中，Agent（智能体）是一个高级组件，它通过将大语言模型（LLM）与一系列外部工具（Tools）相结合，构建了一个能够自主推理并执行复杂任务的智能系统。其核心思想是利用LLM作为推理引擎（Reasoning Engine），让模型能够动态地决定为解决用户问题所需采取的行动序列，包括选择何种工具、以何种顺序调用，并迭代地处理工具返回的结果，直至任务完成

Agent原理与执行流程：

Agent的核心工作原理遵循ReAct（Ressoning + Acting，推理+行动）框架，即在一个循环（思考-行动-观察）中交替进行推理和行动，这个过程会涉及到模型、工具、记忆、中间件等核心组件

| 对比角度 | LLM                                  | LLM+工具调用                             | Agent工具调用                                      |
| -------- | ------------------------------------ | ---------------------------------------- | -------------------------------------------------- |
| 本质定位 | 文本生成器，基于训练数据生成连贯文本 | 增强型LLM，通过函数调用扩展能力边界      | 一个具备规划和执行能力的智能系统，以任务闭环为目标 |
| 核心功能 | 语言理解、文本生成、知识问答         | 基础工具调用、实时数据获取、简单操作执行 | 多步规划、工具编排、状态管理、错误恢复             |
| 工作模式 | 单次交互、静态响应                   | 单轮"请求-调用-响应"                     | 多轮"感知-规划-执行-反馈"循环（ReAct框架）         |
| 系统架构 | 单一模型接口                         | 模型+工具绑定+手动执行循环               | LLM+规划+记忆+工具使用+防护措施                    |
| 状态管理 | 无状态（除对话上下文）               | 需外部维护状态                           | 内置记忆系统，支持短期/长期状态管理                |
| 错误处理 | 失败即终止，无自动恢复               | 失败即终止、无自动恢复                   | 支持重试、回滚等恢复机制                           |

##### Agent创建方式

```python
# create_agent完整参数
agent = create_agent(
    model: str | BaseChatModel,          # 必需：聊天模型
    tools: List[BaseTool],               # 必需：工具列表
    *,
    system_prompt: str = "",             # 系统提示词
    middleware: Seguence[AgentMiddleware[StateT_co, ContextT]] = ()  # 中间件
    interrupt_before: List[str] = None,  # 在某些工具前暂停（人机协作）
    interrupt_after: List[str] = None,   # 在某些工具后暂停
    debug: bool = False,                 # 调试模式
    name: str | None = None              # 设置模型名称
)
```

+ 静态模型

  ```python
  @tool
  def get_weather(location: str) -> str:
      """
      获取指定位置的天气信息
      """
      return f"天气信息：{location}的天气是晴朗的"
  
  agent = create_agent(
      model=deepseek_llm,
      tools=[get_weather],
      system_prompt="你是一个天气助手，你可以帮助用户获取指定位置的天气信息。"
  )
  
  # 调用agent
  resp = agent.invoke({"messages": [{"role": "user", "content": "北京的天气"}]})
  ```

+ 动态模型

  ```python
  @tool
  def get_current_location():
      """获取当前位置"""
      return "当前位置为北京市"
  
  @tool
  def get_weather(city: str):
      """获取指定城市的天气信息"""
      return f"{city}的天气为晴朗，25℃"
  
  basic_model: Any = init_chat_model(
      model="deepseek-chat",
      model_provider="deepseek",
      api_key=DEEPSEEK_API_KEY,
      base_url=DEEPSEEK_BASE_URL
  )
  
  advanced_model: Any = init_chat_model(
      model="qwen-plus",
      model_provider="openai",
      api_key=DEEPSEEK_API_KEY,
      base_url=DEEPSEEK_BASE_URL
  )
  
  @wrap_model_call
  def dynamic_model_selection(request:ModelRequest, handler) -> ModelResponse:
      print(request)
      # 判断消息条数，如果小于3使用basic_model，否则使用advanced_model
      message_count = len(request.state['messages'])
      if message_count < 3:
          model = basic_model
      else:
          model = advanced_model
  
      return handler(request.override(model=model))
  
  agent = create_agent(
      model=basic_model,
      tools=[get_current_location, get_weather],
      middleware=[dynamic_model_selection],
  )
  
  # Agent调用
  response = agent.invoke({"messages": [{"role":"user", "content":"我现在在的位置天气如何"}]})
  print(response)
  ```

  @wrap_model_call标记的方法是模型选择中间件方法，create_agent时通过middlewware参数指定该方法

  @wrap_model_call方法中两个参数含义：

  + request:ModelRequest封装了当前模型调用的所有请求信息
  + handler：回调函数，代表后续处理链，调用它会继续执行模型调用流程，通过handler(request.override(...))传递修改后的请求

  agent.invoke()启动了一个复杂的、可能包含多次LLM调用和工具调用的推理循环。wrap_model_call在这个循环和每个推理步骤（LLM）前都会被触发

##### Agent调用

invoke是Agent最基本的同步调用方法，它会阻塞程序执行直到返回最终结果

核心是一系列消息（messages），每条消息通常包含role（如user、assistant、system、tool）和content

+ 输入：传入的参数为字典类型，字典内通过messages字段传递消息列表
+ 输出：通过invoke调用Agent，底层可能会经历多轮交互，返回的是完整的消息列表，被封装在字典中，是messages字段的值

##### 提示词（Prompt）

在LangChain中，提示词为Agent提供了任务背景、行为准测和操作指南。通过system.prompt参数设置，它本质上定义了Agent的角色和使命

+ 基础设置

  ```python
  agent = create_agent(
      model=deepseek_llm,
      tools=[get_weather],
      system_prompt=SystemMessage(content="你是一个天气查询助手，只回答天气相关的问题，其他问题请直接回答：我不清楚这问题答案。")
  )
  ```

+ 动态设置

  通过@dynamic_prompt装饰器创建中间件，根据用户角色生成不同的系统提示
  
  ```python
  class AgentContext(TypedDict):
      query_type: str
      uid: str
  
  @dynamic_prompt
  def dynamic_support_prompt(request: ModelRequest):
      """根据用户输入动态生成支持提示"""
      query_type = request.runtime.context["query_type"]
      if query_type == "vip":
          return "你是会员用户，有专属的服务和政策"
      else:
          return "你是普通用户，有一般的服务和政策"
  
  
  agent = create_agent(
      model=deepseek_llm,
      tools=[query_order_info, search_faq],
      middleware=[dynamic_support_prompt],
      context_schema=AgentContext
  )
  
  agent.invoke(
      {"messages": [{"role": "user", "content": "查询订单ORD123456的状态"}]},
      context={"query_type": "vip", "uid": "user123"}
  )
  ```

##### Tool创建方式

+ @tool装饰器

  @tool装饰器创建工具默认的名称与函数名相同，也可以通过“@tool("get_employee_info")”指定新名称

+ Pydantic模型

  Pydantic 模型模式的主要优势在于能够精确控制工具参数的格式和验证规则，让大模型更准确地理解如何调用工具。这种方式特别适合参数复杂、有特定约束条件的业务场景

  ```python
  # 1.定义复杂的工单查询参数模型
  class TicketQueryInput(BaseModel):
      """工单查询输入参数 - 支持多种筛选条件"""
      ticket_id: Optional[str] = Field(
          default=None,
          description="工单ID"
      )
      assigner: Optional[str] = Field(
          default=None,
          description="负责人姓名"
      )
      status: Optional[Literal["open", "in_progress", "resolved", "closed"]] = Field(
          default=None,
          description="工单状态: open(待处理), in_progress(处理中), resolved(已解决), closed(已关闭)"
      )
      priority: Optional[Literal["low", "medium", "high", "urgent"]] = Field(
          default=None,
          description="优先级: low(低), medium(中), high(高), urgent(紧急)"
      )
  
      @field_validator("ticket_id")
      def convert_ticket_id_to_upper(cls, v: Optional[str]) -> Optional[str]:
          """将工单ID转换为大写"""
          return v.upper() if v else None
  
  
  # 2. 使用@tool装饰器定义工具，并通过args_schema指定参数模型
  @tool(args_schema=TicketQueryInput)
  def query_tickets(
          ticket_id: Optional[str] = None,
          assigner: Optional[str] = None,
          status: Optional[str] = None,
          priority: Optional[str] = None,
  ) -> str:
  ```

  这种方式定义工具，通过 @tool(args_schema=PydanticModelCls)将这个 Pydantic 模型与工具函数关联。

  PydanticModelCls 需要继承自 BaseModel的类，使用类型提示（如 str, int）和 Field函数来声明每个字段的名称、类型、默认值和描述。每个字段的 description参数至关重要，它直接影响大模型理解参数含义的能力。

  在 convert_ticket_id_to_upper方法中的 cls，代表的是 这个 Pydantic 模型类本身，在这里也就是 TicketQueryInput这个类。@field_validator装饰器将对应方法标记为类方法，类方法的第一个参数约定俗成地命名为 cls，它指向类而不是类的实例。这样，在验证逻辑中如果需要访问类的其他属性或方法，就可以通过 cls来操作。

+ JsonSchema

  ```python
  # 1. 直接使用JSON Schema字典定义复杂的查询参数
  book_query_schema = {
      "type": "object",
      "properties": {
          "title_keyword": {
              "type": "string",
              "description": "图书标题关键词，支持模糊匹配"
          },
          "author": {
              "type": "string",
              "description": "图书作者姓名"
          },
          "category": {
              "type": "string",
              "enum": ["技术", "文学", "历史", "科学", "经济学", "传记"],
              "description": "图书分类"
          }
      },
      "required": [], # 至少需要提供标题关键词、作者或分类中的一个条件，所以这里为空
  }
  
  # 2. 使用@tool装饰器定义工具，并通过args_schema指定JSON Schema
  @tool(args_schema=book_query_schema)
  def query_books(title_keyword: str = None,
                  author: str = None,
                  category: str = None) -> str:
  ```

   type：定义当前数据节点必须是什么数据类型。常见类型有 string, number, integer, boolean, object, array, null。object即是json对象

  (properties：用于定义JSON 对象（Object）中可以包含哪些属性（键），以及每个属性对应的值类型和说明

  required：当 type为 "object"时使用，是一个数组，列出了对象中必须存在的属性名。本案例中指定为空表示json对象中的属性都是非必须

##### 调用工具错误处理

当Agent调用工具异常时，我们可以使用@wrap_tool_call中间件来灵活处理调用工具发生的异常错误

```python
@wrap_tool_call
def handle_tool_errors(request, handler):
    """使用自定义消息处理工具执行错误"""
    try:
        return handler(request)
    except Exception as e:
        # 向模型返回自定义错误消息
        return ToolMessage(
            content=f"调用工具错误:请检查输入参数并重试. ({str(e)})",
            tool_call_id=request.tool_call["id"]
        )
    
agent = create_agent(
    model="gpt-4o",
    tools=[search, get_weather],
    middleware=[handle_tool_errors]
)
```

##### 结构化输出

create_agent函数中的response_format参数是控制结构化输出的核心配置项，支持四种不同的策略设置方式：

+ ProviderStrategy[StructuredResponseT]

  这种设置方式是使用模型提供商的原生结构化输出功能实现结构化输出,适用于支持原生结构化输出的模型

  ```python
  class ContactInfo(BaseModel):
      name: str
      email: str
      phone: str
  
  agent = create_agent(
      model="gpt-4o",
      response_format=ProviderStrategy(ContactInfo)
  )
  ```

+ ToolStrategy[StructuredResponseT]（推荐）

  对于不支持原生结构化输出的模型，LangChain采用“ToolStrategy”工具调用的方式实现结构化输出此策略兼容绝大多数支持工具调用的现代模型，其核心原理是动态创建一个"虚拟工具"，该工具的输入参数对应着期望的数据结构。当模型需要生成最终答案时，系统会引导模型"调用"这个虚拟工具，从而间接产生符合要求的结构化数据。

  ```python
  class ContactInfo(BaseModel):
      name: str
      email: str
      phone: str
  
  agent = create_agent(
      model="gpt-4o-mini",
      tools=[search_tool],
      response_format=ToolStrategy(ContactInfo)
  )
  ```

+ AutoStrategy/type[StructuredResponseT]

  当直接传入一个定义类型时，LangChain会根据模型能力自动选择策略：如果模型支持原生结构化输出（如OpenAI、Anthropic Claude或xAI Grok），则优先使用ProviderStrategy；否则使用ToolStrategy。

  在LangChain 1.0及以上版本中，直接传递模式（如response_format=ContactInfo）不再支持，必须显式使用ToolStrategy或ProviderStrategy

  ```python
  class ContactInfo(BaseModel):
      """Contact information for a person."""
      name: str = Field(description="The name of the person")
      email: str = Field(description="The email address of the person")
      phone: str = Field(description="The phone number of the person")
  
  agent = create_agent(
      model="gpt-5",
      response_format=ContactInfo  # Auto-selects ProviderStrategy
      # response_format=AutoStrategy(ContactInfo)
  )
  ```

+ None

  默认配置，表示不以结构化输出，以自然语言响应用户问题

##### ToolStrategy

ToolStrategy的配置包含三个主要参数:

+ schema（必需参数）：与提供商策略的schema参数功能一致，支持Pydantic模型（推荐）、TypedDict、JSON Schema、数据类（@dataclass），同时还支持联合类型Union[类型1, 类型2]（允许模型根据输入内容选择最匹配的数据结构）。
+ tool_message_content（可选参数）：用于自定义生成结构化输出时，会话历史中记录的提示信息。默认使用展示输出数据的标准响应语句。
+ handle_errors（可选参数）：用于指定数据校验失败时的重试策略，默认值为True。

1. Pydantic类型Schema（推荐）

   ```python
   # 定义Pydantic Schema
   class CustomerAnalysis(BaseModel):
       """客户分析报告"""
       customer_name: str = Field(None, description="客户姓名")
       customer_tier: Literal["潜在客户", "普通客户", "VIP客户", "流失风险"] = Field("潜在客户", description="客户等级,只能是潜在客户、普通客户、VIP客户或流失风险")
       recent_activity: str = Field(None, description="最近活动")
       spending_level: Literal["低", "中", "高"] = Field(None, description="消费水平")
       send_email: bool = Field(False, description="是否已发送感谢邮件")
   
       @field_validator('spending_level')
       def validate_spending(cls, v):
           if v not in ["低", "中", "高"]:
               raise ValueError('消费水平必须是"低"、"中"或"高"')
           return v
   
   
   # 创建智能体
   agent = create_agent(
       model=deepseek_llm,
       system_prompt=SystemMessage(content=""
                                           "请分析指定客户的情况："
                                           "1. 先搜索客户数据库了解最新情况 "
                                           "2. 如果是VIP客户，则发送感谢邮件 "
                                           "3. 基于搜索结果生成结构化分析报告 "
                                           "4. 如果用户提问与客户记录无关或找不到客户信息，则返回空对象，不发送感谢邮件"
                                           ),
       tools=[search_customer_database, send_email],
       response_format=ToolStrategy(CustomerAnalysis)
   )
   ```

2. Dataclass类型Schema

   Dataclass是Python 3.7引入的一个装饰器，用于简化数据存储类的定义。

   ```python
   # 使用Dataclass定义Schema
   @dataclass
   class CustomerAnalysis:
       """客户分析报告"""
       customer_name: Optional[str] = field(default=None, metadata={"description": "客户姓名"})
       customer_tier: Literal["潜在客户", "普通客户", "VIP客户", "流失风险"] = field(
           default="潜在客户",
           metadata={"description": "客户等级,只能是潜在客户、普通客户、VIP客户或流失风险"}
       )
       recent_activity: Optional[str] = field(default=None, metadata={"description": "最近活动"})
       spending_level: Optional[Literal["低", "中", "高"]] = field(default=None, metadata={"description": "消费水平"})
       send_email: bool = field(default=False, metadata={"description": "是否已发送感谢邮件"})
   
   
   # 创建智能体
   agent = create_agent(
       model=deepseek_llm,
       system_prompt=SystemMessage(content=""
                                           "请分析指定客户的情况："
                                           "1. 先搜索客户数据库了解最新情况 "
                                           "2. 如果是VIP客户，则发送感谢邮件 "
                                           "3. 基于搜索结果生成结构化分析报告 "
                                           "4. 如果用户提问与客户记录无关或找不到客户信息，则返回空对象，不发送感谢邮件"
                                   ),
       tools=[search_customer_database, send_email],
       response_format=ToolStrategy(CustomerAnalysis)
   )
   ```

3. TypedDict类型Schema

   ```python
   # 使用 TypedDict 定义客户分析报告 Schema
   class CustomerAnalysis(TypedDict):
       """客户分析报告"""
       customer_name: Annotated[Optional[str], None, "客户姓名"]
       customer_tier: Annotated[Literal["潜在客户", "普通客户", "VIP客户", "流失风险"], "潜在客户", "客户等级"]
       recent_activity: Annotated[Optional[str], None, "最近活动"]
       spending_level: Annotated[Optional[Literal["低", "中", "高"]], None, "消费水平"]
       send_email: Annotated[bool, False, "是否已发送感谢邮件"]
   
   
   # 创建智能体
   agent = create_agent(
       model=deepseek_llm,
       system_prompt=SystemMessage(content=""
                                           "请分析指定客户的情况："
                                           "1. 先搜索客户数据库了解最新情况 "
                                           "2. 如果是VIP客户，则发送感谢邮件 "
                                           "3. 基于搜索结果生成结构化分析报告 "
                                           "4. 如果用户提问与客户记录无关或找不到客户信息，则返回空对象，不发送感谢邮件"
                                   ),
       tools=[search_customer_database, send_email],
       response_format=ToolStrategy(CustomerAnalysis)
   )
   ```

4. JsonSchema类型Schema

   ```python
   # 定义 JSON Schema 替代 Pydantic 模型
   customer_analysis_schema = {
       "title": "CustomerAnalysis",
       "type": "object",
       "description": "客户分析报告",
       "properties": {
           "customer_name": {
               "type": "string",
               "default": "",
               "description": "客户姓名"
           },
           "customer_tier": {
               "type": "string",
               "enum": ["潜在客户", "普通客户", "VIP客户", "流失风险"],
               "default": "潜在客户",
               "description": "客户等级"
           },
           "recent_activity": {
               "type": "string",
               "default": "",
               "description": "最近活动"
           },
           "spending_level": {
               "type": "string",
               "enum": ["低", "中", "高"],
               "default": "低",
               "description": "消费水平"
           },
           "send_email": {
               "type": "boolean",
               "default": False,
               "description": "是否已发送感谢邮件"
           }
       },
       # 所有字段都是必须输出的
       "required": ["customer_name", "customer_tier", "recent_activity", "spending_level"]
   }
   
   # 创建智能体（使用 JSON Schema结构化输出）
   agent = create_agent(
       model=deepseek_llm,
       system_prompt=SystemMessage(content=""
                                           "请分析指定客户的情况："
                                           "1. 先搜索客户数据库了解最新情况 "
                                           "2. 如果是VIP客户，则发送感谢邮件 "
                                           "3. 基于搜索结果生成结构化分析报告 "
                                           "4. 如果用户提问与客户记录无关或找不到客户信息，则返回空对象，不发送感谢邮件"
                                   ),
       tools=[search_customer_database, send_email],
       response_format=ToolStrategy(customer_analysis_schema)  # 直接传入 JSON Schema
   )
   ```
   
5. 多Schema联合模式

   ToolStrategy允许指定多个类型“Union[ContactInfo, EventDetails]”这种写法，LLM能够根据输入文本的内容，智能地选择最合适的一个数据模型（Schema）来生成结构化输出，但是最终会只有一种类型输出，适用于根据不同输入内容，生成不同的结构化输出的场景,但是底层工具转换结构化输出只会转换成一种结构化类型输出。

   当ToolStrategy通过“Union[ContactInfo, EventDetails]”指定多个类型时，在内部调用生成结构化类型工具会报错，此时，handle_errors=True（默认值）开始发挥作用,系统会生成一个ToolMessage，明确告诉LLM“Error: Model incorrectly returned multiple structured responses (ContactInfo, EventDetails) when only one is expected.”，大模型收到这个精准的反馈后，会重新进行推理，最终选择并输出一个最符合要求的Schema。如果handle_errors 设置为False，执行代码过程直接报错

   ```python
   agent = create_agent(
       model=model,
       response_format=ToolStrategy(Union[ContactInfo, EventInfo])
   )
   ```

**自定义工具消息**

在LangChain中，ToolStrategy的 tool_message_content参数允许你自定义工具调用成功后，将指定的内容写入对话历史的提示信息，这样做的好处如下：

1. 在最终用户可见的对话流中，使用更自然的消息替代原始数据。
2. 用简短的确认信息替代可能很长的数据块，减少token消耗。

无论 tool_message_content如何设置，成功提取的结构化数据最终都会正确存入 result["structured_response"]返回，自定义消息仅影响对话历史中的一条记录

```python
class ContactInfo(BaseModel):
    """个人联系信息"""
    name: str = Field(description="姓名")
    email: str = Field(description="电子邮箱")
    phone: str = Field(description="电话号码")


agent = create_agent(
    model= deepseek_llm,
    system_prompt=SystemMessage(content="你是一个专业的联系信息提取器，负责从文本中提取个人的姓名、电子邮箱和电话号码。"),
    response_format=ToolStrategy(
        ContactInfo,
        # tool_message_content="联系信息提取完成！",
    )
)
```

**错误处理**

ToolStrategy通过其 handle_errors参数提供了结构化过程错误处理策略

| 策略                         | 适用场景                                                     |
| ---------------------------- | ------------------------------------------------------------ |
| handle_errors=True           | 默认方式，捕获所有异常，并使用LangChain 内置的、信息明确的错误消息模板提示模型重试。适用于大多数希望自动处理错误的通用场景。 |
| handle_errors=False          | 关闭自动重试机制，任何异常都会直接抛出，会中断程序运行。     |
| handle_errors="自定义字符串" | 捕获所有异常，但使用开发者预设的固定字符串作为错误消息。适用于需要统一、友好的用户提示，或进行特定业务引导的场景。 |
| handle_errors=ExceptionType  | 仅捕获指定类型（如ValueError）或元组中的异常类型并进行重试，其他异常直接抛出。适用于需要精准控制，只对特定错误进行重试的场景。 |
| handle_errors=callable       | 灵活性最高的方式。使用开发者自定义的函数来处理异常，可根据不同的异常类型返回差异化的提示信息。适用于需要复杂、精细化错误处理的场景。 |

```python
agent = create_agent(
    model= deepseek_llm,
    tools=[],
    response_format=ToolStrategy(
        Union[ContactInfo, EventDetails],
        tool_message_content="提取完成！",
        handle_errors=True
        # handle_errors="请检查输入数据"
        # handle_errors=custom_error_handler
    )
)

# 自定义错误处理的函数
def custom_error_handler(error: Exception) -> str:
    error_str = str(error)
    print(f"捕获到的错误类型是：{type(error).__name__}")
    print(f"错误详情{error_str}")
    
    if isinstance(error, MultipleStructuredOutputsError):
        return "检测到多个响应，请选择最相关的一个进行返回"
    elif isinstance(error, StructuredOutputValidationError):
        return "数据格式有误，请检查字段是否符合要求"
    else:
        return f"Error:{error_str}"
```

##### Agent流式输出

LangChain 实现了一套强大的流式传输系统，用于实时显示 Agent 运行过程中的更新。流式传输通过渐进式显示输出（即使在完整响应准备好之前）来显著改善用户体验，特别是在处理 LLM 延迟时尤其有效。

流式输出好处：

- 大型语言模型生成完整响应通常需要几秒钟时间，对于长输出可能达到10-20 秒,用户期望即时反馈，流式传输让等待过程更加可控。
- 相比非流式传输需要用户长时间等待完整响应，流式传输可以立即显示文字逐渐出现的效果，大幅降低用户的等待焦虑。

Agent中可以通过调用“stream”来启用流式输出结果

```python
customer_service_agent = create_agent(
    model=deepseek_llm,
    system_prompt="你是一个专业的客户服务智能体，负责回答客户关于账户信息、订单历史和促销活动的问题。",
    tools=[query_customer_data, check_order_history, get_current_promotions]
)


for chunk in customer_service_agent.stream(
        {"messages": [{
            "role": "user",
            "content": "查询客户ID为 CUST123456 的完整信息和可用优惠"
        }]}
):
    print(chunk)
```

以上代码中“agent.stream(...)”返回一个“Iterator[dict[str, Any] | Any]”对象，该对象中的dict会有“model”（模型调用）和“tools”（工具调用）两个key，分别对应模型/工具调用输出的结果，这些结果以流式交叉输出

**Agent流式输出模式**

Agent默认输出模式是“模型思考-工具调用”ReAct循环调用，最终输出大模型结果，除了这种输出模式外，Agent还支持如下输出模式：values、updates（默认）、messages、custom、checkpoints、tasks、debug，这些模式都是通过“Agent.stream(stream_mode=指定模式，默认为None)”来指定

+ values输出模式

  当stream_mode设置为values模式时，每个步骤执行后，都会输出完整的状态信息，适用于每一步都要获取完整状态、状态持久化场景

  ```python
  for chunk in customer_service_agent.stream(
          {"messages": [{"role": "user","content": "查询客户ID为 CUST123456 的完整信息和可用优惠"}]},
          stream_mode="values"
  ):
      print(chunk)
  ```

+ updates输出模式

  这种模式就是默认模式。该模式中，每个步骤执行后，只增量更新状态中发生变化的内容，用于监控Agent 执行进度，例如观察Agent决定调用工具、工具执行结果等步骤

  ```python
  for chunk in customer_service_agent.stream(
          {"messages": [{"role": "user","content": "查询客户ID为 CUST123456 的完整信息和可用优惠"}]},
          stream_mode="updates"
  ):
      print(chunk)
  ```

+ messages输出模式

  该模式中会输出流式返回的Token以及相关的元数据（如：来自哪个节点），可以用在实现类似 ChatGPT 的打字机效果场景，为聊天机器人等交互式应用提供最佳的实时体验

  ```python
  for chunk in customer_service_agent.stream(
          {"messages": [{"role": "user","content": "查询客户ID为 CUST123456 的完整信息和可用优惠"}]},
          stream_mode="messages"
  ):
      print(chunk)
  ```

+ tasks输出模式

  该模式会输出当前task任务开始和结束的时间，包含任务的结果和错误信息，该模式用于监控任务的生命周期

  ```python
  for chunk in customer_service_agent.stream(
          {"messages": [{"role": "user","content": "查询客户ID为 CUST123456 的完整信息和可用优惠"}]},
          stream_mode="tasks"
  ):
      print(chunk)
  ```

+ debug输出模式

  该模式与tasks模式类似，比task模式多输出任务步骤、时间戳、task类型（task/task_result），该模式用于调试、监控task任务的生命周期

  ```python
  for chunk in customer_service_agent.stream(
          {"messages": [{"role": "user","content": "查询客户ID为 CUST123456 的完整信息和可用优惠"}]},
          stream_mode="debug"
  ):
      print(chunk)
  ```

+ checkpoints输出模式

  该模式中，每当检查点（checkpoint）被创建时会触发输出，输出包含检查点中的状态，用于需要状态持久化、工作流恢复或分布式执行跟踪的高级场景

  ```python
  # 1. 创建内存检查点存储
  checkpointer = InMemorySaver()
  
  # 2. 创建Agent
  agent = create_agent(
      model=deepseek_llm,
      tools=[query_customer_data, check_order_history, get_current_promotions],
      checkpointer=checkpointer  # 启用检查点
  )
  
  # 3. 创建唯一的会话ID
  config = {"configurable": {"thread_id": "session01"}}
  
  # 4. 调用Agent
  checkpoint_count = 0
  
  # 使用checkpoints模式进行流式监控
  for chunk in agent.stream(
          {"messages": [{"role": "user","content": "查询客户ID为 CUST123456 的完整信息和可用优惠"}]},
          config=config,
          stream_mode="checkpoints"
  ):
      checkpoint_count += 1
      print(f"检查点 #{checkpoint_count}")
      print(chunk)
  ```

+ custom输出模式

  开发者通过get_stream_writer在工具或节点内部自定义发送的数据，用于输出业务逻辑相关的进度信息（如“已处理10/100条记录”）、自定义日志或指标

  ```python
  @tool
  def generate_sales_report() -> str:
      """生成销售报告"""
      writer = get_stream_writer()
  
      writer({"type": "生成销售报告", "message": "开始生成销售报告"})
  
      # 模拟数据处理
      for i in range(1, 4):
          time.sleep(0.5)
          writer({"type": "生成销售报告","message": f"生成销售报告进度百分比：{i * 25}%"})
  
      writer({"type": "生成销售报告", "message": "报告生成完成"})
  
      return f"销售报告：总收入150万元，同比增长12%"
  
  
  @tool
  def generate_inventory_report() -> str:
      """生成库存报告"""
      writer = get_stream_writer()
      writer("开始库存分析...")
      time.sleep(0.5)
      writer("检查当前库存量...")
      time.sleep(0.5)
      writer("生成库存报告...")
  
      return "当前库存量为10000件，库存充足，无异常"
  
  
  # 创建报告生成代理
  reporting_agent = create_agent(
      model=deepseek_llm,
      tools=[generate_sales_report, generate_inventory_report]
  )
  
  for chunk in reporting_agent.stream(
          {"messages": [{"role": "user","content": "生成销售报告和库存报告"}]},
          stream_mode="custom"
  ):
      print(chunk)
  ```

#### 中间件

Middleware（中间件），简单说就是Agent执行过程中的钩子函数，是LangChain1.x的王牌工程化能力

钩子是框架或系统在某些关键执行点暴露的扩展接口。开发者可以挂上自己的逻辑，在那些点上插入、修改或替换行为，而无需改变主流程代码

分类：

+ 自定义中间件：开发者自定义
+ 内置中间件：LangChain实现并提供的
  + 模型供应商定制的中间件
  + 和模型供应商无关的中间件

和模型供应商无关的中间件：

1. 成本与资源控制类
   + Model call limit：限制模型调用次数，防止一次任务反复请求LLM，导致费用失控
   + Tool call limit：限制工具调用次数，避免Agent无限试错、死循环调工具
   + Summarization：在上下文快满时自动总结历史，减少token消耗
   + Context editing：裁剪上下文、清理工具调用痕迹，本质上也是为了节省上下文成本
2. 稳定性与容错保障类
   + Model fallback：主模型失败时切换备用模型
   + Model retry：模型调用失败后自动重试
   + Tool retry：工具调用失败后自动重试
3. 安全与合规风控类
   + Human-in-the-loop：在关键工具调用前暂停，等人工审批
   + Pll detection：检测和处理个人敏感信息
   + Model call limit / Tool call limit：某种意义上也可归到风控，因为它能防止异常滥用
4. 决策增强与智能编排类
   + To-do list：给Agent增加任务规划、分步骤执行和状态跟踪能力
   + LLM tool selector：当工具太多时，用子模型筛选最相关的几个工具交给主模型
   + Subagent：允许生成子Agent，把复杂任务拆给不同角色处理
5. 执行能力扩展类
   + Shell tool：给Agent持久shell，会执行命令
   + File search：给Agent文件搜索能力，能做Glob/Grep
   + Filesystem：给Agent文件系统读写与长期存储能力
6. 开发调试与测试辅助类
   + LLM tool emulator：用LLM模拟工具执行，便于测试
   + Summarization：有时也可辅助调试长会话表现
   + Context editing：可用于测试上下文裁剪效果
   + Human-in-the-loop：也常用于调试高风险步骤

##### 常用内置中间件

+ SummarizationMiddleware中间件

  作用：对历史消息列表进行摘要总结，达到压缩上下文的效果

  原理：在达到触发条件时，调用大模型对历史消息进行摘要，将摘要的结果作为HumanMessage，放到消息列表最开始的位置

  参数：

  + model：用于摘要的模型，可以是模型名称也可以是模型对象，如果传递的是模型名称，底层会调用init_chat_model初始化模型

  + trigger：摘要触发条件，是一个列表，每一个元素对应一个条件，当任一条件满足时，触发摘要

    1. tokens：token的数量，历史token的累计数量达到该值触发摘要
    2. messages：历史消息数量，历史消息条数达到该值触发摘要
    3. fraction：上下文长度比例。历史token的累计数量达到模型的max_input_tokens*fraction触发摘要

    如果条件包含fraction，要求模型的profile包含max_input_tokens

  + keep：摘要时保留的原始消息

    支持三种条件，但和trigger不同，keep同一时间只接受一种条件

    1. tokens：摘要时保留的token数量
    2. messages：摘要时保留的历史消息条数
    3. fraction：摘要时保留max_input_tokens*fraction个token

  + token_counter：统计token数量的函数

    默认使用LangChain提供的count_tokens_approximately，一般不用改

  + summary_prompt：摘要时的自定义提示词

    该提示词需要包含{messages}占位符，使得历史消息列表可以被插入。不指定则使用内置提示词

  + trim_token_to_summarize：摘要时历史消息的最大token数

    如果历史消息token数大于该值，则会被裁剪，默认为4000

    如果trigger用token作为度量，调大触发阈值时，当前配置项相应调整，否则会丢失信息

  ```python
  model = init_chat_model(
      model="deepseek-v4-flash",
      model_provider="deepseek",
      # 如果条件包含fraction，要求模型的profile包含max_input_tokens
      profile={"max_input_tokens": 128_000},
      api_key=DEEPSEEK_API_KEY,
      base_url=DEEPSEEK_BASE_URL
  )
  
  agent = create_agent(
      model="deepseek-v4-flash",
      middleware=[
          SummarizationMiddleware(
              model=model,
              trigger=[
                  ("tokens", 100),
                  ("messages", 6),
                  ("fraction", 0.001)
              ],
              keep=("messages", 2),
              summary_prompt="对历史消息摘要，消息列表如下\n{messages}"
          )
      ]
  )
  ```

+ HumanInTheLoopMiddleware中间件

  HumanInTheLoopMiddleware在工具调用前中断Agent运行，等待用户对工具调用请求决策。可选的决策有：approve（同意执行）、edit（编辑调用配置后执行）、reject（拒绝执行）

  参数：

  + interrupt_on：工具名和中断策略的映射

    策略可以是True、False或InterruptOnConfig对象，精细控制决策选项

    ```python
    interrupt_on={
        "getweather": True,
        "read_email_tool": False,
        "send_email_tool": {
            "allowed_decisions": ["approve", "reject"]
        }
    }
    ```

    1. True：表示所有决策（approve、edit、reject）都可以选择
    2. False：表示不中断，即无需审批即可执行
    3. InterruptOnConfig：是一个TypedDict的子类，可以用字典直接赋值。支持的key有
       + allowed_decisions：精细控制中断后允许的决策
       + description：特定工具的中断描述信息，优先级高于description_prefix，后者会更改所有工具中断的描述

  + description_prefix：自定义中断描述

    默认为"Tool execution requires approval"

  ```python
  agent = create_agent(
      model=model,
      tools = [get_weather, get_news, read_email_tool, send_email_tool],
      checkpointer=InMemorySaver(),
      middleware=[
          HumanInTheLoopMiddleware(
              interrupt_on={
                  "getweather": True,
                  "get_news":True
                  "read_email_tool": False,
                  "send_email_tool": {
                      "allowed_decisions": ["approve", "reject"],
                      "description": "发送邮件中断了"
                  }
              },
              description_prefix="中断了"
          )
      ]
  )
  
  config = {"configurable": {"thread_id": "1"}}
  
  response = agent.invoke({
      "messages" : [HumanMessages(content="请为我查询今天北京的天气")]
  },
  config = config
  )
  
  # 指明工具调用请求决策
  weather_decision = {
      "type": "edit",
      "edited_action": {
          "name": "get_weather",
          "args": {"city": "上海市"}
      }
  }
  
  news_decision = {
      "type": "approve"
  }
  
  send_email_decision = {
      "type": "approve"
  }
  
  decisions = {
      "decisions" : []
  }
  
  interrupts = response.get("__interrupt__", [])
  action_requests = interrupts[0].value["action_requests"]
  
  for action_request in action_requests:
      if action_request["name"] == "get_weather":
          decisions["decisions"].append(weather_decision)
      if action_request["name"] == "get_news":
          decisions["decisions"].append(news_decision)
      if action_request["name"] == "send_email_tool":
          decisions["decisions"].append(send_email_decision)
        
  if interrupts:
      resumed_response = agent.invoke(
          Command(resume=decisions),
          config=config
      )
      
      for msg in resumed_response["messages"]:
          msg.pretty_print()
  ```

+ PIIMiddleware中间件

  敏感信息保护。PII中间件用于检测和处理对话中的个人身份信息（Personally Identifiable Information），支持自定义处理策略

  参数：

  + pii_type：检测的PII数据类型

    可以是内置类型或自定义类型，自定义类型有：

    + email：电子邮箱地址
    + credit_card：信用卡号
    + url：网址
    + mac_address：设备MAC地址
    + ip：IP地址

  + strategy：处理PII信息的策略

    支持四种选项：

    + redact：将检测到的PII信息用字符串[REDACTED_[PII_TYPE]]替换，其中的PII_TYPE是上面提到的具体类型，比如[REDACTED_EMAIL]这样的标签。完全擦除/隐藏真实内容。适合日志清洗、合规需求、公开输出时隐藏敏感内容
    + mask：用***将PII信息的前面一部分信息遮蔽。既隐藏大部分敏感信息，又保留了一点可辨识性，适合用户服务界面/前端显示/需要部分可识别但不泄露完整敏感内容的场景
    + hash：用检测到的PII信息的哈希值替代原值。比如<email_hash:a1b2c3d4 >。适合analytics、调试、统计分析、匿名追踪等场景
    + block：如果检测到PII信息，直接抛出异常。适合对隐私要求极高、绝不允许泄露任何敏感信息的场景

  + detector：自定义PII检测函数或者正则表达式

    如果没有提供则使用内置的检测函数。LangChain为每种PII信息定制了专门的检测函数，相关源码如下

    ```python
    BUILTIN_DETECTORS: dict[str, Detector] = {
        "email": detect_email,
        "credit_card": detect_credit_card,
        "ip": detect_ip,
        "mac_address": detect_mac_address,
        "url": detect_url
    }
    ```

  + apply_to_input：是否在调用模型前检测，默认为True

  + apply_to_output：是否在模型调用后检测，默认为False

  + apply_to_tool_results：是否在工具调用后检测，默认为False

  ```python
  # 使用内置检测器
  agent = create_agent(
      model=model,
      tools=[],
      middleware=[
          PIIMiddleware("email", strategy="redact",apply_to_input=True),
          PIIMiddleware("credit_card", strategy="mask",apply_to_input=True),
          PIIMiddleware("ip", strategy="hash",apply_to_input=True),
          PIIMiddleware("mac_address", strategy="mask",apply_to_input=True),
          PIIMiddleware("url", strategy="block",apply_to_input=True)
      ]
  )
  
  # 自定义检测器/函数
  import re
  
  def detect_phone_number(content: str):
      return [
          {
              "text": m.group(0),  # 提取出具体匹配到的11位数字文本
              "start": m.start(),  # 这段数字在原文本中的起始索引位置，从0开始
              "end": m.end()       # 这段数字在原文本中的结束索引位置
          } for m in re.finditer(r"[0-9]{11}", content)
      ]
  
  agent = create_agent(
      model=model,
      tools=[],
      middleware=[
          PIIMiddleware("api_key", strategy="hash",apply_to_input=True, detector=r"sk-[a-zA-Z0-9]+"),
          PIIMiddleware("phone_number", strategy="mask",apply_to_input=True, detector=detect_phone_number)
      ]
  )
  ```

+ TodoListMiddleware中间件

  TodoListMiddleware中间件赋予了Agent任务规划和追踪进度的能力，可以应对复杂的多步任务

  比如一个大任务被拆解为3个以上的子任务，且前面的步骤是后面步骤的前提时，如果不列Todo列表，大模型在执行到第3步时，很容易忘记自己最初的目标，或者在工具返回大量报错信息后应激，直接跳过验证去回答用户。此时，TodoListMiddleware中间件强制它把计划挂在全局状态里，时刻提醒它下一步该干什么

  To-do list的创建和维护是通过调用write_todos工具实现的

  参数：

  + system_prompt：自定义指导todo列表使用的提示词

    不提供则使用内置提示词，通常不必提供

  + tool_description：自定义write_tools工具的描述信息

    不提供则使用内置描述，通常不必提供

  ```python
  def add(a: int, b:int) -> int:
      """返回两个整数的和"""
      return a - b
  
  def test_add():
      """测试加法功能"""
      assert add(2, 3) == 5
      
  # 工具
  list_files：扫描工作目录，列出其中的所有文件
  read_file：扫描指定文件，返回文件内容
  write_file：向指定文件写入内容
  run_tests：运行测试，底层基于pytest实现
  
  agent = create_agent(
      model=model,
      tools=[list_files, read_file, write_file, run_tests],
      middleware=[TodoListMiddleware()],
      system_prompt="你是一个代码修复助手。遇到多步骤任务时，先使用write_todos制定待办事项；然后读取文件、修复代码并运行测试。工作全部在工作区下进行。"
  )
  ```

##### 其他内置中间件

+ ModelCallLimitMiddleware中间件

  限制模型调用次数，避免无限循环，控制调用成本

  ```python
  # 整个会话限制-优雅退出
  agent = create_agent(
      model=model,
      checkpointer=InMemorySaver(),
      tools=[],
      middleware=[
          ModelCallLimitMiddleware(
              thread_limit=2,  # 每个线程最多2次模型调用
              # run_limit=5,     # 每次运行最多5次
              exit_behavior="end"  # 达到限制后退出
          )
      ]
  )
  
  # 整个会话限制-抛异常
  agent = create_agent(
      model=model,
      checkpointer=InMemorySaver(),
      tools=[],
      middleware=[
          ModelCallLimitMiddleware(
              thread_limit=2,
              # run_limit=5,
              exit_behavior="error"  # 达到限制后抛异常
          )
      ]
  )
  
  # 单次调用限制-优雅退出
  agent = create_agent(
      model=model,
      checkpointer=InMemorySaver(),
      tools=[],
      middleware=[
          ModelCallLimitMiddleware(
              # thread_limit=2,
              run_limit=5,
              exit_behavior="end"
          )
      ]
  )
  
  # 单次调用限制-抛异常
  agent = create_agent(
      model=model,
      checkpointer=InMemorySaver(),
      tools=[],
      middleware=[
          ModelCallLimitMiddleware(
              # thread_limit=2,
              run_limit=5,
              exit_behavior="error"
          )
      ]
  )
  ```

+ ToolCallLimitMiddleware中间件

  限制工具调用次数，可以限制所有工具调用的总次数，也可以限制特定工具的调用次数

  退出行为有三种模式：

  + error：直接抛异常
  + end：结束整个会话
  + continue：继续运行Agent，这是默认行为，此时Agent会将工具调用超出限制的信息传递给模型，后者自主决定后续行为，如果模型能力不足，可能导致死循环。为了避免这种情况，我们实现的fake server会以20%的概率输出正确响应，从而能终止死循环

  ```python
  # 单次调用限制-优雅退出
  agent = create_agent(
      model=model,
      checkpointer=InMemorySaver(),
      tools=[],
      middleware=[
          ToolCallLimitMiddleware(
              # thread_limit=2,  # 每个线程最多2次模型调用
              run_limit=5,     # 每次运行最多5次
              exit_behavior="end"  # 达到限制后退出
          )
      ]
  )
  
  # 单次调用限制1-抛异常
  agent = create_agent(
      model=model,
      checkpointer=InMemorySaver(),
      tools=[],
      middleware=[
          ModelCallLimitMiddleware(
              # thread_limit=2,
              run_limit=5,
              exit_behavior="error"
          )
      ]
  )
  
  # 单次调用限制-继续运行
  agent = create_agent(
      model=model,
      checkpointer=InMemorySaver(),
      tools=[],
      middleware=[
          ModelCallLimitMiddleware(
              # thread_limit=2,
              run_limit=5,
              exit_behavior="continue"
          )
      ]
  )
  ```

+ ModelFallbackMiddleware中间件

  用于故障转移，当主模型无法访问时，启用备用模型

  ```python
  agent = create_agent(
      model=model,
      checkpointer=InMemorySaver(),
      tools=[],
      middleware=[
          ModelFallbackMiddleware(
              "deepseek-v4-flash",
              "deepseek-v4-pro",
          )
      ]
  )
  ```

+ LLMToolSelectorMiddleware中间件

  智能工具筛选，当工具太多时，用子模型筛选最相关的几个工具

  参数：

  + model：用于工具筛选的子模型
  + max_tools：限定可以调用的工具总数
  + always_include：指定工具不被计数

  ```python
  tool_selector = LLMToolSelectorMiddleware(
      model="openai:gpt-5.4-mini",
      max_tools=5
      always_include=["get_weather"]
  )
  agent = create_agent(
      model=model,
      tools=[...100个工具...],
      middleware=[tool_selector]
  )
  ```

+ ToolRetryMiddleware中间件

  用于指数退避算法，设置工具调用失败时的重试策略

  指数退避（Exponential Backoff）的核心思想就是：当某个操作失败（通常是网络请求、API调用或数据库连接）时，系统不会立刻重试，也不会每次都等待相同的固定时间，而是让每一次重试的延迟时间按指数级增长

  jitter是为了避免大量工具的重试请求集中在固定时间点，引入抖动

  按照策略，两次工具调用请求的时间间隔为10s，加入抖动后，可能为8.9秒，也可能为10.2秒

  ```python
  agent = create_agent(
      model=model,
      tools=[get_weather],
      middleware=[ToolRetryMiddleware(
          max_retries=6,          # 最大重试次数（不包含初始的那次调用）
          backoff_factor=2.0,     # 指数退避因子（每次重试等待时间乘以2）
          initial_delay=1.0,      # 第一次重试前的初始等待时间（1秒）
          max_delay=10.0,         # 最大等待延迟上限（防止指数增长无限大，限制在10秒）
          jitter=True,            # 开启抖动（在等待时间中加入随机性，防止并发请求时出现惊群效应）
          retry_on=(TimeoutError),# 仅针对捕获到特定的TimeoutError异常时才触发重试
          on_failure="continue"   # 当达到最大重试次数依然失败时，Agent的行为："continue"表示将错误信息包装后塞回对话历史，让大模型知道失败了并继续决策
      )]
  )
  ```

+ ModelRetryMiddleware中间件

  模型调用失败时重试，策略和工具调用的重试一样，都是基于指数退避算法

  ```python
  agent = create_agent(
      model=model,
      tools=[get_weather],
      middleware=[ToolRetryMiddleware(
          max_retries=6,
          backoff_factor=2.0,
          initial_delay=1.0,
          max_delay=10.0,
          jitter=False,
          on_failure="continue"
      )]
  )
  ```

+ LLMToolEmulator中间件

  某些情况下，工具尚未完成开发，我们希望先测试工具调用，可以用LLM tool emulator模拟工具

  ```python
  agent = create_agent(
      model=model_out,
      tools=[get_weather],
      middleware=[
          LLMToolEmulator(
          	model=model_in
      	)
      ]
  )
  ```

+ ContextEditingMiddleware中间件

  上下文编辑中间件，该中间件提供了上下文管理的一种方式

  通过更改发送给模型的消息列表来控制成本

  注意：不会更改消息列表。因此我们只能通过token用量来推测是否对消息列表进行了裁剪

  ```python
  agent = create_agent(
      model=model_out,
      tools=[get_weather],
      middleware=[
          ContextEditingMiddleware(
          	edits=[
                  # 配置清除工具调用记录的策略
                  # 作用：当上下文中的历史消息累积满足特定条件时，自动裁剪/删除旧的工具调用及返回结果
                  ClearToolUsesEdit(
                      trigger=50,  # 触发阈值（例如当工具返回的Token数或消息数达到设定值时触发，具体取决于LangChain版本实现）
                      keep=0  # 触发清理时，保留最近的几次工具调用。这里设置为0代表全部清除旧的工具消息
                  )
              ]
      	)
      ]
  )
  ```

+ FilesystemFileSearchMiddleware中间件

  基于系统的Glob和Grep检索工具，为Agent赋予本地文件搜索和分析的能力

  + Glob根据文件路径检索
  + Grep根据文件内容检索

  ```python
  agent = create_agent(
      model=model_out,
      tools=[get_weather],
      middleware=[
          FilesystemFileSearchMiddleware(
          	root_path="../todo_workspace",  # 搜索目录
              # 可选，限制搜索文件的后缀，防止模型读取非代码或无关文件
              # allowed_extensions=[".py", ".ipynb", ".js", ".md"]
              # 是否启用ripgrep搜索引擎：
              # 设为True可以获得比原生Grep更快的性能（前提是系统已安装ripgrep）
              use_ripgrep=True,
              # 单个文件的最大读取限制（单位为MB）：防止读取超大型日志或二进制文件导致OOM
              max_file_size_mb=10
      	)
      ]
  )
  ```

+ Shell tool中间件

  为Agent提供一个可执行命令的Shell环境，Windows下无法测试

+ Filesystem中间件

  源自deepagents（基于LangChain的另一个框架）的中间件

  内置了四个工具，分别用于查看目录、读文件、写文件和该文件

+ Subagent中间件

  也是来自deepagents的中间件，用于便捷地创建子Agent

##### 多个中间件组合及执行顺序

```python
agent = create_agent(
    model=model_out,
    tools=[],
    middleware=[Middleware1(), Middleware2(), Middleware3()]
)

# 执行顺序：中间件1before_model -> 中间件2before_model -> 中间件3before_model -> 中间件3after_model -> 中间件2after_model -> 中间件1after_model
```

##### 自定义中间件

通过实现LangChain暴露的中间件hook函数构建自定义中间件

Hook函数，中文常叫钩子函数，指的是在某个既定流程的特定时机，被框架、系统或主程序自动调用的扩展函数

![](/img/langchain_2.png)

无论是官方内置中间件、自定义中间件、还是下文提到的便捷装饰器中间件，通常都是通过实现其中一个或多个hook来生效的

LangChain的hook函数分类：

+ Node-style hooks（节点风格钩子）：在流程的特定节点运行
  + before_agent：在Agent开始运行之前执行
  + before_model：在模型调用之前执行
  + after_model：在模型调用之后执行
  + afer_agent：在Agent流程全部完成后执行
+ Wrap-style hooks（包装风格钩子）：在模型或工具调用前后运行
  + wrap_model_call：包裹模型调用
  + wrap_tool_call：包裹工具调用

**Node-style hooks函数用法**

+ 装饰器是函数式挂载，把一个hook快速挂载到Agent的某个节点
+ 类写法是对象化中间件，把中间件封装为一个可配置、可复用、可扩展的组件

```python
# 基于装饰器实现

@before_model
def before_model_middleware(state: AgentState, runtime: Runtime):
	state["messages"][-1].content += "----> before_model <----"
    return None

@afer_model
def after_model_middleware(state: AgentState, runtime: Runtime):
	state["messages"][-1].content += "----> after_model <----"
    return None

@before_agent
def before_agent_middleware(state: AgentState, runtime: Runtime):
	state["messages"][-1].content += "----> before_agent <----"
    return None

@after_agent
def after_agent_middleware(state: AgentState, runtime: Runtime):
	state["messages"][-1].content += "----> after_agent <----"
    return None
```

```python
# 基于类实现
# 1.必须继承AgentMiddleware
# 2.方法名固定（before_model,after_model）
# 3.类名随意

class MyMiddleware(AgentMiddleware):

    def before_model(self, state: AgentState, runtime: Runtime):
        state["messages"][-1].content += "----> before_model <----"
        return None


    def after_model(self, state: AgentState, runtime: Runtime):
        state["messages"][-1].content += "----> after_model <----"
        return None


    def before_agent(self, state: AgentState, runtime: Runtime):
        state["messages"][-1].content += "----> before_agent <----"
        return None


    def after_agent(self, state: AgentState, runtime: Runtime):
        state["messages"][-1].content += "----> after_agent <----"
        return None
```

装饰器底层会基于我们重写的方法构造一个AgentMiddleware子类的实例

参数说明：

+ state：是一个AgentState实例，维护Agent运行过程中的状态，这类状态会随着Agent的运行而发生变化，包括消息列表

+ runtime：是一个runtime实例，维护Agent运行过程中的上下文环境，包括上下文、长期记忆等

返回值说明：

+ None：不修改状态

+ 字典：更新状态

  ```python
  def after_model(self, state: AgentState, runtime: Runtime):
      count = state.get("count", 0)
      return {"count": count + 1}  # 更新状态中的count
  ```

+ {"jump_to":"__ end__"}：控制流程

  + __ end__：结束Agent
  + tools：跳到工具节点
  + 其他自定义节点

  ```python
  def before_model(self, state: AgentState, runtime: Runtime):
      if state.get("count", 0) > 10:
      	return {"jump_to": "__end__"}  # 跳过模型，直接结束
      return None
  ```

**装饰器参数：can_jump_to**

钩子函数可以改变Agent正常的运行轨迹。比如发现上下文窗口溢出，直接跳转至结尾，提前终止整个Agent

can_jump_to决定了钩子函数可以直接跳转至流程的哪些位置，可取值如下：

+ end：跳转至Agent流程末尾，或第一个after_agent钩子，直接终止整个流程
+ tools：跳转至工具节点
+ model：跳转至模型节点，或第一个before_model钩子

```python
# 基于装饰器实现

# 在模型执行前触发，允许跳转到tools节点
@before_model(can_jump_to=["tools"])
def force_tool_first(state: AgentState, runtime: Runtime):
    """
    如果用户输入包含director tool，则跳过本次大模型的思考/生成阶段，直接伪造一个大模型的tool_calls意图，强行把控制权移交给工具执行节点
    """
    return {
        "messages": [fake_tool_call],
        "jump_to": "tools"
    }

# 在模型执行生成之后触发，允许重新跳转回model节点
@after_model(can_jump_to=["model"])
def retry_with_extra_instruction(state: AgentState, runtime: Runtime):
    """
    如果大模型已经生成了回答，但发现用户最初的请求包含retry model，则动态追加一条系统提示词，强行让模型重新生成一次
    """
    return {
        "messages": [
            SystemMessages("你必须以【二次回答】开头，并且只用一句话回答")
        ],
        "jump_to": "model"
    }

# 在模型执行前后触发，允许直接跳转到end节点
@before_model(can_jump_to=["end"])
def overflow_context_processor(state: AgentState, runtime: Runtime):
    """
    模拟上下文窗口溢出（Token超限）或其他严重的系统阻断情况，一旦触发，直接熔断流程，拒绝让大模型继续处理，直接报错或返回兜底文案
    """
    return {
        "messages": [
            AIMessages("上下文窗口溢出，终止")
        ],
        "jump_to": "end"
    }
```

```python
# 基于类实现
# 和基于装饰器实现的关键区别在于：需要引入额外的装饰器@hook_config为can_jump_to传参

class MyMiddleware(AgentMiddleware):
    @hook_config(can_jump_to=["tools", "end"])
    def before_model(self, state: AgentState, runtime: Runtime):
        return {
            "messages": [],
            "jump_to": "end"
        }
```

**Wrap-style hooks函数用法**

wrap_model_call

```python
# 基于装饰器实现

@wrap_model_call
def wrap_model_call_middleware(request: ModelRequest, handler: Callable[[ModelRequest], ModelResponse]) -> ModelReponse | None:
    
    request.messages[-1].content += "---> wrap_model_call_before <---"
    
    # 模型调用
    response = handler(request)
    
    response.result[0].content += "---> wrap_model_call_after <---"
    
    return response 
```

```python
# 基于类的实现

class WrapModelCallMiddleware(AgentMiddleware):
    def wrap_model_call(self, request: ModelRequest, handler: Callable[[ModelRequest], ModelResponse]) -> ModelReponse | None:
        
        request.messages[-1].content += "---> wrap_model_call_before <---"
    
        # 模型调用
        response = handler(request)

        response.result[0].content += "---> wrap_model_call_after <---"

        return response
```

wrap_tool_call

```python
# 基于装饰器实现

@wrap_tool_call
def wrap_tool_call_middleware(request: ToolCallRequest, handler: Callable[[ToolCallRequest], ToolMessage | Command[Any]]) -> ToolMessage | Command[Any]:
    
    request.tool_call["args"]["is_forcast"] = True
    result = handler(request)
    
    return result
```

```python
# 基于类的实现

class WrapToolCallMiddleware(AgentMiddleware):
    def wrap_tool_call(self, request: ToolCallRequest, handler: Callable[[ToolCallRequest], ToolMessage | Command[Any]]) -> ToolMessage | Command[Any]:
        
        request.tool_call["args"]["is_forcast"] = True
        result = handler(request)

        return result
```

**装饰器和类的选择**

1. 中间件只用一个钩子函数，推荐用装饰器，需要多个钩子函数推荐类写法
2. 复杂配置推荐使用类实现
3. 跨项目复用推荐用类写法

**hook函数执行顺序**

1. before_model中间件的执行顺序和传递顺序一致
2. after_model中间件的执行顺序和传递顺序相反
3. wrap_model_call中间件的执行顺序是：先传递的抱在最外层，即洋葱模型

#### 记忆

##### 概述

在LangChain中，记忆就是专门负责存储历史交互信息的组件，核心作用是保存上下文和提供上下文，让LLM在每次响应时都能看到之前的对话内容

上下文工程（Context Engineering）负责合理组织这些记忆和任务信息，让LLM的响应更连贯、更贴合需求。这也是Agent能实现复杂多轮交互的核心基础

LangChain的上下文工程是基于Agent讨论的，而上下文工程的构建是在LangGraph之上的

LangGraph提供了三种管理上下文的方法：

| 上下文类型       | 描述                                                       | 可变性 | 生命周期 | 访问方法               |
| ---------------- | ---------------------------------------------------------- | ------ | -------- | ---------------------- |
| 动态运行时上下文 | 在单次运行中会演变的可变数据                               | 动态   | 单次运行 | LangGraph的state对象   |
| 动态跨会话上下文 | 在对话间共享的持久化数据。比如用户偏好、历史洞察、知识条目 | 动态   | 跨对话   | LangGraph的store对象   |
| 静态运行时上下文 | 在启动时传入的用户元数据、工具、数据库连接                 | 静态   | 单次运行 | LangGraph的context对象 |

记忆分类：

+ 短期记忆（Short-term memory、会话级记忆、thread-scoped memory）：作用范围是单个对话线程内，一旦开启新对话（更换thread_id），记忆即消失
+ 长期记忆（Long-term memory、跨会话记忆）：在会话间存储用户特定或应用级数据，并在会话线程间共享。它可以随时在任何线程中被调用。记忆的范围是任意自定义命名空间，而不仅仅是单一线程ID

记忆的管理：

在LangChainv1.x版本中，Agent是构建在LangGraph图结构之上的，通过上文提到的state和store构建记忆系统。使用更简单、功能更统一。

+ state：短期记忆对象，以会话为单位组织，包含当前会话的所有消息记录以及自定义消息
+ store：长期记忆，跨会话持久化的数据，通常需要结合向量数据库或外部存储实现

##### 短期记忆

LangChain1.x的短期记忆是三者的组合：

State（会话内部状态）+ Checkpointer（持久化机制）+ Thread ID（会话作用域）

+ State：默认存储历史消息列表messages，通过State管理历史消息
+ Checkpointer：负责将State作为检查点持久化保存，检查点是某个时刻的State快照
+ Thread ID：用于唯一标识State，LangChain运行时会按照thread_id读写State快照

InMemorySaver()将状态持久化到内存，进程结束或重建Saver()则历史状态丢失

基于外部存储介质（如PostgreSQL）的持久化器，其存储的状态不会随进程终止而丢失，只要不显式删除历史状态，即可通过thread_id加载历史状态

```python
# 短期记忆，存在内存
agent = create_agent(
    model=deepseek_llm,
    tools=[],
    checkpointer=InMemorySaver()
)

# 创建唯一的会话ID，通过thread_id来存储记忆
config = {"configurable": {"thread_id": "session01"}}

resp1 = agent.invoke({"message": [{"role": "user", "content": "我叫张三，你是谁？"}]}, config = config)

resp2 = agent.invoke({"message": [{"role": "user", "content": "我叫什么名字"}]}, config = config)


# 短期记忆，存在数据库
DB_URL = "mysql+pymysql://root:123456@localhost:3306/langchain_db?charset=utf8mb4"

with PyMySqlSaver.from_conn_string(DB_URL) as checkpointer:
    checkpointer.setup()
    
    agent = create_agent(
        model=deepseek_llm,
        tools=[],
        checkpointer=checkpointer
	)
    config = {"configurable": {"thread_id": "session01"}}

    resp1 = agent.invoke({"message": [{"role": "user", "content": "我叫张三，你是谁？"}]}, config = config)

    resp2 = agent.invoke({"message": [{"role": "user", "content": "我叫什么名字"}]}, config = config)
    
    
# 自定义状态步骤
# 1. 定义一个类，这个类是AgentState，定义要存储的状态字段
# 2. 构建Agent时指定state_schema=CustomAgentState
# 3. 可以在调用Agent时，传入自定义状态
# 在Agent运行中通过中间件（@before_model、@after_model）、toll方式读取/修改

@tool
def get_user_info(runtime: ToolRuntime):
    # 获取用户信息
    name = runtime.state["name"]
    hobby = runtime.state["hobby"]
    return f"用户：{name}，爱好：{hobby}"

# 修改短期记忆
@tool
def update_user_info(name: str, hobby: list, runtime: ToolRuntime) -> Command:
    # 更新用户信息
    if not name or not hobby:
        return Command(
            update={
                "messages":[
                    ToolMessage(
                        content="缺少用户名或爱好",
                        tool_call_id=runtime.tool_call_id
                    )
                ]
            }
        )
    update = {
        "user": name,
        "hobby": hobby,
        "messages" : [
            ToolMessage(
                content="缺少用户名或爱好",
                tool_call_id=runtime.tool_call_id
            )
        ]
    }
    return Command(update=update)

class CustomAgentState(AgentStaet):
    name: str
    hobby: list

checkpointer = InMemorySaver()

agent = create_agent(
    model=deepseek_llm,
    tools=[get_user_info, update_user_info],
    checkpointer=checkpointer,
    state_schema=CustomAgentState,
)
config = {"configurable": {"thread_id": "session01"}}

resp1 = agent.invoke({
    "message": [{"role": "user", "content": "我叫张三，你是谁？"}],
    "name": "张三",
    "hobby": ["篮球", "足球"]
}, config = config)

    resp2 = agent.invoke({"message": [{"role": "user", "content": "把我的名字改为李四，添加我的爱好还有跑步，请更新我的信息"}]}, config = config)
```

##### 记忆治理策略（上下文管理）

+ 消息裁剪

  调用模型前裁剪上下文，目标是控制token用量，通常保留系统初始消息和最近若干消息，或按token数保留末尾内容。适合成本敏感，对旧上下文依赖不强的场景

  ```python
  @before_model
  def trim_messages(state: AgentState, runtime: Runtime) -> dict[str, Any] | None:
      messages = state["messages"]
      
      if len(messages) <= 3:
          return None
      
      first_message = messages[0]
      # 如果有偶数条消息，则取最近的3条消息，如果有奇数条消息，则取最近的4条消息
      recent_messages = messages[-3:] if len(messages) % 2 == 0 else messages[-4:]
  
      new_messages = [first_message] + recent_messages
      
      return {
          "messages": [
              RemoveMessage(id=REMOVE_ALL_MESSAGES),
              *new_messages
          ]
      }
  
  agent = create_agent(
      model=model,
      middleware=[trim_messages],
      checkpointer=InMemorySaver()
  )
  
  config: RunnableConfig = {"configurable": {"thread_id": "1"}}
      
  agent.invoke({"messages": [HumanMessage("你好，我是老王")]}, config)
  ```

+ 消息删除

  消息删除强调模型调用完成后将某些消息从消息列表中移除，永久更改状态。适合明确要遗忘、清理、重置某些历史

  ```python
  @after_model
  def delete_old_messages(state: AgentState, runtime: Runtime) -> dict[str, Any] | None:
      messages = state["messages"]
      
      if len(messages) > 5:
          to_delete = len(messages) - 5
          return {"messages": [RemoveMessage(id=m.id) for m in messages[:to_delete]]}
      return None
  
  agent = create_agent(
      model=model,
      middleware=[delete_old_messages],
      checkpointer=InMemorySaver()
  )
  
  config: RunnableConfig = {"configurable": {"thread_id": "1"}}
      
  agent.invoke({"messages": [HumanMessage("你好，我是老王")]}, config)
  ```

+ 摘要

  把早期历史压缩成摘要，再替换原始消息

  ```python
  checkpointer = InMemorySaver()
  
  @tool
  def get_weather(city: str):
      return f"{city}的天气是晴天"
  
  @before_model
  def before_model(state:AgentState, runtime:ToolRuntime) -> dict|None:
      """在模型调用之前，将用户信息添加到状态中"""
      messages = state["messages"]
      return {"messages": messages}
  
  @after_model
  def after_model(state:AgentState, runtime:ToolRuntime) -> dict|None:
  	"""在模型调用之后，将用户信息添加到状态中"""
      messages = state["messages"]
      return {"messages": messages}
  
  agent = create_agent(
      model=deepseek_llm,
      tools=[get_weather],
      checkpointer=checkpointer,
      middleware=[
          before_model, 
          after_model, 
          SummarizationMiddleware(
              model=deepseek_llm,
              trigger=[("tokens", 100)],  # 超过100tokens就摘要
              keep=("messages", 2),  # 总结前面的消息，保留后两条
              summary_prompt="请摘要以下内容：{messages}"
          )
      ]
  )
  ```

+ 自定义过滤策略

  通过中间件可以随意更改消息列表，因此可以实现任意的过滤策略

##### 长期记忆

长期记忆记录的是用户特定或应用级别的数据，任何会话都可以随时访问

存储架构：store -> namespace -> key -> value

+ Store（记忆仓库）

  Store是langgraph.store.base.BaseStore的子类实例，由全类名可知，store是由LangGraph提供的

  常用实现类：

  + InMemoryStore：将长期记忆存储在内存，适合测试
  + PostgresStore：将长期记忆存储在外部的PostgreSQL数据库，适合生产环境

+ Namespace（命名空间）

  数据类型是由任意长度的tuple[str, ...]表示的层级路径。作用上很像文件路径/文件夹层级，用于给长期记忆分组和隔离。数据类型为字符串元组

+ Key（键）

  是该namespace下的唯一标识，单条记忆的唯一键，数据类型为字符串（str）

+ Value（值）

  是存储的值，数据类型为字典（dict[str, Any]）

**基础API的使用**

LangChain1.2x的长期记忆基于store持久化数据，相关的API有：

+ put()：负责写入

  参数：

  + namespace：文档所在的层级路径
  + key：该路径下的唯一键
  + value：要保存的JSON-like字典
  + index：控制语义检索索引
    + None（默认选项）：使用store初始化时配置的索引配置，如果初始化时没有指定索引策略，则index参数将会被忽略
    + False：不为该item建立语义索引
    + list[str]：只对指定字段路径建索引
  + ttl：可选，过期时间，是否支持取决于具体store实现

+ get()：负责读取

  按照namespace + key精确查询，返回的不止是value，而是完整对象。即LangGraph底层将数据封装为Item对象

  参数：

  + namespace：文档所在的层级路径
  + key：该路径下的唯一键
  + refresh_ttl：是否刷新当前item的ttl（time-to-live，存活时间）
    + 默认为None：表示采用创建store对象时指定的同名配置
    + 如果没有配置TTL，该参数被忽略

+ search()：负责检索

  参数：

  + namespace_prefix：命名空间前缀，在该前缀下搜索
  + query：语义检索时用于查询的自然语言
  + filter：过滤条件，value中的键值对组合
  + limit：可以返回item的最大条数，效果等同于SQL中的limit
  + offset：返回结果之前跳过的item数量
  + refresh_ttl：同上

我们可以在Agent执行流程之外直接访问长期记忆

```python
# 长期记忆，存在内存
store = InMemoryStore()

store.put(
    ("users",),  # 命名空间
    "user_123",  # 键
    {"name": "张三", "age": 18, "city": "北京","hobby": "旅游"}  # 值
)

store.put(
    ("users",),  # 命名空间
    "user_456",  # 键
    {"name": "李四", "age": 28, "city": "南京","hobby": "足球"}  # 值
)

@tool
def get_user_info(runtime: ToolRuntime) -> dict:
    """从长期记忆中获取用户信息"""
    store = runtime.store
    user_id = "user_123"
    user_data= store.get(("users",), user_id)
    
    if user_data:
        value = user_data.value
        return f"用户姓名：{value['name']}，用户年龄：{value['age']}"
    else:
        return "用户信息不存在"
    
agent = create_agent(
    model=deepseek_llm,
    tools=[get_user_info],
    checkpointer=InMemorySaver(),  # 短期记忆存储
    store=store  # 长期记忆存储
)

resp1 = agent.invoke({"message": [{"role": "user", "content": "获取我的信息"}]})


# 长期记忆，存在数据库
@tool
def get_user_info(runtime: ToolRuntime) -> dict:
    """从长期记忆中获取用户信息"""
    store = runtime.store
    user_id = "user_123"
    user_data= store.get(("users",), user_id)
    
    if user_data:
        value = user_data.value
        return f"用户姓名：{value['name']}，用户年龄：{value['age']}"
    else:
        return "用户信息不存在"
    
DB_URL = "mysql+pymysql://root:123456@localhost:3306/langchain_db?charset=utf8mb4"

with (
    PyMySqlSaver.from_conn_string(DB_URL) as checkpointer,
    PyMySqlStore.from_conn_string(DB_URL) as store
):
    checkpointer.setup()
    store.setup()
    
    store.put(
        ("users",),  # 命名空间
        "user_123",  # 键
        {"name": "张三", "age": 18, "city": "北京","hobby": "旅游"}  # 值
    )

    store.put(
        ("users",),  # 命名空间
        "user_456",  # 键
        {"name": "李四", "age": 28, "city": "南京","hobby": "足球"}  # 值
    )

    agent = create_agent(
        model=deepseek_llm,
        tools=[get_user_info],
        checkpointer=InMemorySaver(),  # 短期记忆存储
        store=store  # 长期记忆存储
    )
    
    resp1 = agent.invoke({"message": [{"role": "user", "content": "获取我的信息"}]})
```

##### 短期记忆和长期记忆区别总结

| 对比维度     | 短期记忆（Short-term Memory）                                | 长期记忆（Long-term Memory）                                 |
| ------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 作用域       | 线程/会话范围记忆（Thread-scoped）。与单个会话线程（thread_id）绑定 | 跨线程/会话记忆。存储在自定义命名空间（namespace）中，可被多个线程共享 |
| 核心目的     | 保证单次对话的连贯性和上下文感知                             | 实现跨对话的个性化、知识积累和持续学习                       |
| 主要存储内容 | 对话的原始历史（messages列表）以及当前会话的状态数据         | 从交互中提炼的结构化知识（如用户事实、行为经验、优化规则）   |
| 管理组件     | 检查点（Checkpointer），如InMemorySaver、PyMySQLSaver        | 存储（Store），如InMemoryStore、PyMySQLStore                 |
| 生命周期     | 随线程的创建而开始，随线程的销毁（或超时清理）而结束         | 独立于任何特定线程，除非被显式删除，否则永久或长期存在       |
| 访问方式     | 自动管理。Agent的状态在每个步骤后自动持久化到检查点，并在下次恢复 | 手动控制。必须在工具（Tool）或自定义逻辑中，通过代码显式地调用store.put()或store.get() |
| 典型运用场景 | 维持聊天上下文，让Agent记得用户在当前对话中刚说过的话        | 记住用户身份、偏好、历史行为                                 |

### LangGraph

#### 节点与可控制性

LangGraph运行时主要由三个基本要素构成：State（状态）、Node（节点）、Edge（边）

State（状态）

LangGraph运行过程中的共享数据结构，用于表示应用在某一时刻的状态快照。它承载了图运行所需的上下文信息、中间结果和后续节点需要读取的数据，是节点之间传递信息的核心载体。它与我们在学习LangChain Agent时使用的State是同一概念

Node（节点）

LangGraph中具体的执行单元，通常实现为一个函数。节点会读取当前State，执行相应的业务逻辑，并返回对State的局部更新。节点本身并不直接修改全局状态，状态的合并与提交由运行时统一完成

+ 节点是图中的基本单元，代表一个具体的功能或操作
+ 每个节点负责完成一项特定任务（如查询数据、生成文本、做决策等）
+ 节点接受输入，处理后产生输出
+ 可以是简单函数、API调用、LLM调用或其他复杂操作

Edge（边）

用于定义节点之间的流转关系，决定一个节点执行完成后下一步应该进入哪个节点。Edge可以是固定流转，也可以根据当前State进行条件判断，从而实现分支、循环等复杂控制流程

Graph（图）

+ 图是节点及其连接关系的集合，代表整个工作流程
+ 定义了信息如何从一个节点流向另一个节点
+ 可以是线性的（A->B->C）或包含分支、循环的复杂结构
+ 控制整个应用的执行流程和逻辑

##### 状态定义

状态的定义实际上是在声明状态的schema，后者是状态字段的完整描述

官方推荐了三种定义schema的方式：TypedDict（推荐）、dataclass、Pydantic

**TypedDict**

```python
# 定义节点间通讯的消息格式
class State(TypedDict):
    messages:list[AnyMessage]
    extra_field:int
        
def node(state:State):
    messages = state["messages"]
    new_message = AIMessage["你好，我是节点1"]
    
    return {
        "messages": messages + [new_message],
        "extra_field": 1
    }

graph = StateGraph(State)
graph.add_node(node)
graph.set_entry_point("node")
graph_builder = graph.compile()

# 查看节点与图结构
# Mermaid是一种基于文本的图表和可视化工具，它允许用户通过简单的文本语法来创建复杂的图表和流程图
display(Image(graph_builder.get_graph().draw_mermaind_png()))

result = graph_builder.invoke({
    "messages":[HumanMessage("你好啊，我是tomie")]
})
for message in result["messages"]:
    message.pretty_print()  # 格式化显示
```

**dataclass**

属性调用方式由['字段名']变为.字段名

```python
from dataclasses import dataclass

@dataclass
class OverAllState:
    logs: Annotated[list[str], add]
    cur_id: str
        
def node(state: OverAllState):
    pre_id = state.cur_id
    return {
        "logs": ["node运行完毕"],
        "cur_id": pre_id + ", node"
    }
```

**Pydantic**

```python
from pydantic import BaseModel

@dataclass
class OverAllState(BaseModel):
    logs: Annotated[list[str], add]
    cur_id: str
        
def node(state: OverAllState):
    pre_id = state.cur_id
    return {
        "logs": ["node运行完毕"],
        "cur_id": pre_id + ", node"
    }
```

##### State Reducer

State Reducer是LangGraph中用于合并状态更新的核心机制。在LangGraph的StateGraph中，每个节点可以读取和写入共享状态，而Reducer定义了如何将多个节点对同一状态键的合并更新

Reducer的核心特征：

+ 函数签名：(value, value) -> value，接收当前值和更新值，返回合并后的新值
+ 注解定义：通过Annotated[Type, reducer_function]为状态键指定Reducer
+ 默认行为：未指定Reducer的状态键使用覆盖策略（Last-Write-Wins）

```python
# 定义Reducer函数
# left：从最开始的位置合并到当前节点的位置的值
# right：当前节点的值
# 返回值：合并后的值
def my_reducer(left: list[str], right: list[str]):
    return left + right

# Overwrite：告诉LangGraph本次状态更新不走该字段原本定义的Reducer，而是直接用新值覆盖状态中的旧值
class OverAllState(TypedDict):
    logs: Annotated[list[str], add]
    cur_id: str
        
def node(state: OverAllState):
    pre_id = state.cur_id
    return {
        "logs": Overwrite(["node运行完毕"]),
        "cur_id": pre_id + ", node"
    }
```

##### Multi Schema

LangGraph支持在一个图中使用多个状态Schema，用于区分图的外部输入、外部输出、内部共享状态以及节点间的临时状态

常见状态可以分为以下几类：

+ 全局状态/内部状态：图内主要使用的状态，创建StateGraph时传递给state_schema参数。它通常包含图运行过程中需要读写的大部分字段
+ 输入状态：图对外接受输入时使用的状态，创建StateGraph时传递给input_schema参数。它用于约束调用图时允许传入哪些字段
+ 输出状态：图最终对外返回结果时使用的状态，创建StateGraph时传递给output_schema参数。它用于约束图运行结束后只返回哪些字段
+ 私有状态：图内部节点之间传递的临时状态，通常不作为图的输入，也不作为图的最终输出。它可以通过节点函数的入参类型注解声明，并在节点返回值中写入。

设计规范：

+ 输入状态和输出状态通常应该是全局状态的子集
+ 私有状态和全局状态应尽量避免字段重名
+ 节点函数应明确声明入参状态类型和返回状态类型
+ 节点函数中不应该访问入参状态类型中不存在的字段
+ 节点函数返回的字典应尽量和返回类型注解保持一致

##### 串行控制

```python
class State(TypedDict):
    value_1:str
    value_2:str
        
def step_1(state:State):
    return {"value_1":"a"}

def step_2(state:State):
    current_value_1 = state["value_1"]
    return {"value_1":f"{current_value_1}+b"}

def step_3(state:State):
    return {"value_2":10}

graph_builder = StateGraph(State)
graph_builder.add_node(step_1)
graph_builder.add_node(step_2)
graph_builder.add_node(step_3)

graph_builder.add_edge(START, "step_1")
graph_builder.add_edge("step_1", "step_2")
graph_builder.add_edge("step_2", "step_3")
graph_builder.add_edge("step_3", END)

graph = graph_builder.compile()

display(Image(graph.get_graph().draw_mermaind_png()))

graph.invoke({"value_1":"c"})
```

##### 分支控制

```python
# Annotated允许为类型提供额外的元数据，而不影响类型检查起对类型本身的理解
class State(TypedDict):
    aggregate:Annotated[list,operator.add]
        
def a(state:State):
    return {"aggregate":["A"]}

def b(state:State):
    return {"aggregate":["B"]}

def c(state:State):
    return {"aggregate":["C"]}

def d(state:State):
    return {"aggregate":["D"]}

builder = StateGraph(State)
builder.add_node(a)
builder.add_node(b)
builder.add_node(c)
builder.add_node(d)

builder.add_edge(START, "a")
builder.add_edge("a", "b")
builder.add_edge("a", "c")
builder.add_edge("b", "d")
builder.add_edge("c", "d")
builder.add_edge("d", END)

graph = builder.compile()

display(Image(graph.get_graph().draw_mermaind_png()))

graph.invoke({"aggregate":[]},{"configurable":{"thread_id":"foo"}})
```

##### 条件分支

add_conditional_edges

三个参数：

+ source：条件分支的起始节点
+ path：路由规则，是一个可执行对象，通常是函数
+ path_map：路由规则的返回值到真实节点名之间的映射关系

path_map：

+ 可以省略，即取默认值None，此时path返回值中出现的字符串必须是合法的节点名称

+ 可以是字典，维护path返回值和真实节点的映射

+ 也可以是列表

  ```python
  path_map=["node_a", "node_b", "node_c"]
  
  相当于
  
  path_map={
      "node_a": "node_a",
      "node_b": "node_b",
      "node_c": "node_c"
  }
  ```

**defer node execution**

某些情况下，我们希望在所有常规任务节点执行完毕后，再进行日志、审计等收尾工作

此时可以在添加节点时设置defer=True

```python
builder.add_node("audit_node", audit_node, defer=True)
```

defer=True的含义是：当前节点不会在其被触发后立即执行，而是被延迟到常规图运行流程结束后，再在额外的超步中触发执行

这类节点适合用于：日志记录、审计检查、结果汇总、收尾清理、统一校验前面节点是否已完成

```python
class State(TypedDict):
    aggregate:Annotated[list,operator.add]
        
def a(state:State):
    return {"aggregate":["A"]}

def b(state:State):
    return {"aggregate":["B"]}

builder = StateGraph(State)
builder.add_node(a)
builder.add_node(b)

def route(state:State) -> Literal["b": EMD]:
    if len(state["aggregate"]) < 7:
        return "b"
    else:
        return END
    
builder.add_edge(START, "a")
builder.add_conditional_edges("a", route)
builder.add_edge("b", "a")

graph = builder.compile()

display(Image(graph.get_graph().draw_mermaind_png()))

# 使用递归限制recursion_limit来防止异常情况下的大量无用调用
graph.invoke({"aggregate":[]}, {"recursion_limit": 4})
```

##### 图的运行时配置

```python
model = ChatDeepSeek(
    model="deepseek-v4-flash",
    temperature=0,
    api_key=DEEPSEEK_API_KEY,
    base_url=DEEPSEEK_BASE_URL
)

model1 = ChatOpenAI(
    model="gpt-3.5-turbo",
    temperature=0,
    api_key=DEEPSEEK_API_KEY,
    base_url=DEEPSEEK_BASE_URL
)

# 定义要切换的模型
models = {
    "deepseek": model,
    "openai": model1
}

class AgentState(TypedDict):
    messages: Annotated[Sequence[BaseMessage], operator.add]
        
def _call_model(state: AgentState, config: RunnableConfig):
    # 使用LCEL的配置
    model_name = config["configurable"].get("model", "deepseek")
    model = models[model_name]
    response = model.invoke(state["messages"])
    return {"messages": [response]}

builder = StateGraph(AgentState)
builder.add_node("model", _call_model)
builder.add_edge(START, "model")
builder.add_edge("model", END)

graph = builder.compile()

display(Image(graph.get_graph().draw_mermaind_png()))

# 没有增加运行时配置的情况下，默认使用deepseek
graph.invoke({"messages":[HumanMessage(content="hi 你是谁")]})

# 增加运行时配置，动态切换模型
config = {"configurable": {"model":"openai"}}
graph.invoke({"messages":[HumanMessage(content="hi 你是谁")]}, config=config)
```

##### MapReduce并行执行

```python
# 定义我们将使用的模型和提示词
subjects_prompt = """生成一个逗号分割的列表，包含2到5个与以下主题相关的例子：{topic}"""
joke_prompt = """生成一个关于{subject}的笑话"""
best_joke_prompt = """以下是一些关于{topic}的笑话，选出最好的一个，返回最佳笑话的ID
{jokes}"""

class Subjects(BaseModel):
    subjects: list[str]
        
class Joke(BaseModel):
    joke: str
        
class BestJoke(BaseModel):
    id: int = Field[description="最佳笑话的索引，从0开始"， ge=0]
        
model = ChatDeepSeek(
    model="deepseek-v4-flash",
    temperature=0,
    api_key=DEEPSEEK_API_KEY,
    base_url=DEEPSEEK_BASE_URL
)

# 这将是主图的整体状态，包含一个主题，然后将生成一个主题列表，并为每个主题生成一个笑话
class OverallState(TypedDict):
    topic: str
    subjects: list
    jokes: Annotated[list, operator.add]
    best_selected_joke: str
        
# 用于生成笑话
class JokeState(TypedDict):
    subject: str
        
# 生成笑话主题
def generate_topics(state: OverallState):
    prompt = subjects_prompt.format(topic=state["topic"])
    response = model.with_structured_output(Subjects).invoke(prompt)
    return {"subjects": response.subjects}

# 根据给定的主题生成笑话
def generate_joke(state: JokeState):
    prompt = joke_prompt.format(subject=state["subject"])
    response = model.with_structured_output(Joke).invoke(prompt)
    return {"jokes": response.joke}

# 定义映射到生成的主题上的逻辑，将在图中使用这个作为边缘
def continue_to_jokes(state: OverallState):
    # 返回一个Send对象列表，每个Send对象包含图中节点的名称以及要发送到该节点的状态
    return [Send("generate_joke", {"subject": s}) for s in state["subjects"]]

# 评判最佳笑话
def best_joke(state: JokeState):
    jokes = "\n\n".join(state["jokes"])
    prompt = best_joke_prompt.format(topic=state["topic"], jokes=jokes)
    response = model.with_structured_output(BestJoke).invoke(prompt)
    return {"best_selected_joke": state["jokes"][response.id]}

graph = StateGraph(OverallState)
graph.add_node("generate_topics", generate_topics)
graph.add_node("generate_joke", generate_joke)
graph.add_node("best_joke", best_joke)
graph.add_edge(START, "generate_topic")
graph.add_conditional_edges("generate_topics", continue_to_jokes, ["generate_joke"])
graph.add_edge("generate_joke", "bset_joke")
graph.add_edge("best_joke", END)
app = graph.compile()

for s in app.stream({"topic": "动物"}):
	print(s)
```

##### 动态分支

节点的后续执行路径在运行时才确定，可以根据当前状态、输入数据或中间结果，动态决定要触发哪些下游任务或跳转到哪个下游节点

特点：

+ 下游执行目标可以在运行时选择
+ 下游任务数量可以在运行时决定
+ 可以为同一个下游节点动态创建多个执行任务
+ 适合实现Map-Reduce式动态扇出、多任务并行处理、运行时条件跳转等场景

典型用法：

+ Send：动态扇出任务
+ Command(goto=...)：运行时跳转到下游节点

**并行节点**

Send结合add_conditional_edges()使用，可以用于动态扇出任务

所谓扇出，是指一个上游节点像扇子一样，向外分发出多个下游任务。

具体来说，路由函数可以返回一个Send实例序列。每个Send实例都描述了一次独立的任务分发：

+ 分发到哪个下游节点
+ 给这个下游节点传入什么私有状态

运行时，LangGraph会根据返回的每个Send实例创建对应的任务。这些任务通常会在同一个超步中并行执行

**条件分支**

Command是LangGraph中用于控制图执行的多功能原语，它的构造器可以接受四个参数，并记录在同名类属性中：

+ update：更新图状态，效果等同于节点直接返回状态更新字典
+ goto：指定节点执行完成后的跳转目标，可用于运行时条件分支。当需要同时更新状态并控制跳转时，比单独使用条件边更合适
+ graph：存在子图时，用于指定跳转发生在哪一层图中，例如从子图跳转到父图
+ resume：用于恢复被中断的图执行，常见于human-in-the-loop场景

##### 多分支汇聚

**静态扇入**

+ 与触发：等待所有上游分支到达

  ```python
  # config的元数据中记录了当前节点所在的SuperStep序号，可以通过下面的方式获取
  config["metadata"]["langgraph_step"]
  
  # c, d都执行完才能到e
  builder.add_edge(["node_c", "node_d"], "node_e")
  ```

+ 或触发：任意上游分支到达即可触发

  ```python
  builder.add_edge("node_c", "node_e")
  builder.add_edge("node_d", "node_e")
  ```

**动态扇入-MapReduce结构**

MapReduce是大数据计算中的经典模型，通常包含两个核心阶段：

+ Map：映射阶段

  将输入数据映射为中间结果

  在LangGraph中，可以理解为通过Send将子任务分发给多个mapper节点实例，每个节点实例独立完成局部计算：将部分输入数据映射为中间结果

+ Reduce：归约阶段

  将多个子任务产生的中间结果进行汇总、合并或聚合，得到最终结果

  在LangGraph中，通常由一个特定的reducer节点完成归约：它接收上游mapper节点实例产生的中间结果，处理后得到计算图的最终输出

```python
# 声明状态
class OverAllState(TypedDict):
    input_values: list[str]
    entries: Annotated[list[tuple[str,int]], add]
    word_counts: dict[str,int]
        
class MapperInputState(TypedDict):
    input_value:str
        
# 声明节点
# 分发节点
def router_node(state:OverAllState) -> Sequence[Send]:
    input_values = state["input_values"]
    task = []
    for input_value in input_values:
        task.append(
            Send("mapper_node", {"input_value":input_value})
        )
    return task

# 接收单独的一句话，切分为单词，组装为元组放到状态中
def mapper_node(state:MapperInputState) -> OverAllState:
    input_value = state["input_value"]
    words = input_value.split(" ")
    entries = []
    for word in words:
        entries.append((word,1))
    return {
        "entries":entries
    }

# 聚合所有mapper_node拆分的元组进行合并
def reducer_node(state:OverAllState) -> OverAllState:
    entries = state["entries"]
    shuffle_dict = {}
    for k,v in entries:
        if k not in shuffle_dict:
            shuffle_dict[k] = v
        else:
            shuffle_dict[k].append(v)
            
    reduce_dict = {}
    
    for k,v in shuffle_dict.items():
        reduce_dict[k] = sum(v)
        
    return {
        "word_counts": reduce_dict
    }

builder = StateGraph(OverallState)
builder.add_node("mapper_node", mapper_node)
builder.add_node("reducer_node", reducer_node)
builder.add_conditional_edges(START, router_node, path_map=["mapper_node"])
builder.add_edge("mapper_node", "reducer_node")
builder.add_edge("reducer_node", END)
graph = builder.compile()

res = graph.invoke({
    "input_values": ["hello world", "hello atguigu", "hello llm"]
})
```

##### 循环结构

本节通过两种方式实现经典的ReAct循环结构。LangChain Agent底层运行图架构正是ReAct

ReAct是Reason + Action的缩写，即推理+行动架构，其核心思想是：

1. Reason：大模型根据当前消息状态进行推理，判断是否需要调用工具
2. Action：如果需要调用工具，则生成工具调用请求
3. Observation：工具执行后，将执行结果以ToolMessage的形式返回给大模型
4. Loop：大模型基于新的观察结果继续推理，决定是否继续调用工具
5. Final Answer：当大模型不再发起工具调用时，生成最终回答，流程结束

本节分别使用两种方式实现该循环：

+ 静态实现：通过add_conditional_edges()在图结构中显式定义条件路由
+ 动态实现：通过Command(goto=...)在节点返回值中触发运行时跳转

**静态实现**

```python
@tool(parse_docstring=True)
def get_weather(city:str = "上海")
	"""
	查询指定城市当日天气
	
	Args:
		city: 城市名称
	"""
    return f"{city}的天气是晴朗的"

@tool(parse_docstring=True)
def get_news(domain:Literal["AI", "食品安全"])
	"""
	查询特定领域的当日热点
	
	Args:
		domain: 特定领域
	"""
    if domain == "AI":
        return "AI热点"
    elif domain == "食品安全":
        return "食品安全热点"
    else:
        return "未知领域"
    
tools = [get_weather, get_news]
model_with_tool = model.bind_tools(tools=tools)


# 声明状态
class OverAllState(MessageState):
    user_input:str
    final_output:str
        

# 输入节点，将用户输入的查询信息，记录到message中，方便后续大模型调用
def input_node(state:OverAllState) -> OverAllState:
    return {
        "messages": [HumanMessage(state("user_input"))]
    }

# 大模型节点
def llm_node(state:OverAllState) -> OverAllState:
    ai_msg = model_with_tool.invoke(state["messages"])
    return {
        "messages": ai_msg
    }

# 判断是否需要调用工具
def tool_node(state:OverAllState) -> OverAllState:
    ai_msg = state["messages"][-1]
    tool_calls = ai_msg.tool_calls
    
    # 表示函数调用60%失败
    fail_prob = 6
    
    for tool_call in tool_calls:
        if tool_call["name"] == "get_weather":
            # 生成[0-9]数字 判断大小
            if randint(0,9) < fail_prob:
                messages.append(ToolMessage(
                	content="网络波动，调用失败，请重试",
                	tool_call_id = tool_call["id"]
                ))
            else:
                messags.append(get_weather.invoke(tool_call))
        elif tool_call["name"] == "get_news":
            if randint(0,9) < fail_prob:
                messages.append(ToolMessage(
                	content="网络波动，调用失败，请重试",
                	tool_call_id = tool_call["id"]
                ))
            else:
                messags.append(get_news.invoke(tool_call))
        else:
            messages.append(ToolMessage(
                content="工具名称错误，调用失败，请重试",
            	tool_call_id = tool_call["id"]
            ))
            
    return {
        "messages":messages
    }

# 返回输出节点
def output_node(state:OverAllState) -> OverAllState:
    return {
        "final_output":state["messages"][-1].content
    }

# 判断是否需要进行工具节点的调用
def router(state:OverAllState) -> Literal["tool_node", "output_node"]:
    messages = state["messages"]
    last_msg = messages[-1]
    if last_msg.tool_calls:
        return "tool_node"
    return "output_node"

# 构建图
builder = StateGraph(state_schema=OverAllState)
builder.add_node("input_node", input_node)
builder.add_node("llm_node", llm_node)
builder.add_node("tool_node", tool_node)
builder.add_node("output_node", output_node)

builder.add_edge(START, "input_node")
builder.add_edge("input_node", "llm_node")
builder.add_conditional_edges("llm_node", router)
builder.add_edge("tool_node", "llm_node")
builder.add_edge("output_node", END)

graph = builder.compile()

ai_res = graph.invoke({
    "user_input": "查询今天上海的天气和AI新闻热点",
    "messages": [SystemMessage("如果工具调用失败，必须重新调用直到成功为止")]
})
```

**动态实现**

```python
@tool(parse_docstring=True)
def get_weather(city:str = "上海")
	"""
	查询指定城市当日天气
	
	Args:
		city: 城市名称
	"""
    return f"{city}的天气是晴朗的"

@tool(parse_docstring=True)
def get_news(domain:Literal["AI", "食品安全"])
	"""
	查询特定领域的当日热点
	
	Args:
		domain: 特定领域
	"""
    if domain == "AI":
        return "AI热点"
    elif domain == "食品安全":
        return "食品安全热点"
    else:
        return "未知领域"
    
tools = [get_weather, get_news]
model_with_tool = model.bind_tools(tools=tools)


# 声明状态
class OverAllState(MessageState):
    user_input:str
    final_output:str
        

# 输入节点，将用户输入的查询信息，记录到message中，方便后续大模型调用
def input_node(state:OverAllState) -> OverAllState:
    return {
        "messages": [HumanMessage(state("user_input"))]
    }

# 大模型节点
def llm_node(state:OverAllState) -> Command[Literal["tool_node", "output_node"]]:
    # 使用goto方式完成动态调用循环结构
    ai_msg= model_with_tool.invoke(state["messages"])
    
    if ai_msg.tool_calls:
        goto = "tool_node"
    else:
        goto = "output_node"
    return Command(
        update={
            "messages" : [ai_msg]
        },
        goto=goto
    )

# 判断是否需要调用工具
def tool_node(state:OverAllState) -> OverAllState:
    ai_msg = state["messages"][-1]
    tool_calls = ai_msg.tool_calls
    
    # 表示函数调用60%失败
    fail_prob = 6
    
    for tool_call in tool_calls:
        if tool_call["name"] == "get_weather":
            # 生成[0-9]数字 判断大小
            if randint(0,9) < fail_prob:
                messages.append(ToolMessage(
                	content="网络波动，调用失败，请重试",
                	tool_call_id = tool_call["id"]
                ))
            else:
                messags.append(get_weather.invoke(tool_call))
        elif tool_call["name"] == "get_news":
            if randint(0,9) < fail_prob:
                messages.append(ToolMessage(
                	content="网络波动，调用失败，请重试",
                	tool_call_id = tool_call["id"]
                ))
            else:
                messags.append(get_news.invoke(tool_call))
        else:
            messages.append(ToolMessage(
                content="工具名称错误，调用失败，请重试",
            	tool_call_id = tool_call["id"]
            ))
            
    return {
        "messages":messages
    }

# 返回输出节点
def output_node(state:OverAllState) -> OverAllState:
    return {
        "final_output":state["messages"][-1].content
    }

# 构建图
builder = StateGraph(state_schema=OverAllState)
builder.add_node("input_node", input_node)
builder.add_node("llm_node", llm_node)
builder.add_node("tool_node", tool_node)
builder.add_node("output_node", output_node)

builder.add_edge(START, "input_node")
builder.add_edge("input_node", "llm_node")
builder.add_edge("tool_node", "llm_node")
builder.add_edge("output_node", END)

graph = builder.compile()

ai_res = graph.invoke({
    "user_input": "查询今天上海的天气和AI新闻热点",
    "messages": [SystemMessage("如果工具调用失败，必须重新调用直到成功为止")]
})
```

**引入递归限制**

LangGraph提供了递归限制机制，用于限制单次图运行过程中允许执行的最大SuperStep数量

当运行图在达到停止条件之前耗尽允许的最大步数时，LangGraph会抛出GraphRecursionError。开发者即可以在图内部提前检测剩余步数并优雅退出，也可以在图外部捕获异常并集中处理

配置递归限制：

recursion_limit表示单次图运行过程中允许执行的最大SuperStep数量。可以在调用运行图时通过config显式配置

```python
graph.invoke(input, config={"recursion_limit": 10})
```

需要注意的是，recursion_limit的默认值可能随版本变化、运行环境切换而变化

**优雅退出：主动方法**

RemainingSteps是LangGraph提供的特殊托管值，表示剩余可用步数，由运行时维护

LangGraph运行时会根据当前步数和recursion_limit计算剩余步数，并填充到RemainingSteps类型的状态字段中。开发者可以在状态中声明一个RemainingSteps类型的字段，来获取剩余可用步数

```python
class OverAllState(TypedDict):
    remaining_steps:RemainingSteps
        
def loop_node(state:OverAllState, config:RunnableConfig) -> OverAllState:
    cur_step = config["metadata"]["langgraph_step"]
    remaining_steps = state["remaining_steps"]
    
def router(state:OverAllState) -> Literal["loop_node", END]:
    remaining_steps = state["remaining_steps"]
    if remaining_steps < 3:
        return END
    return "loop_node"

builder = StateGraph(state_schema=OverAllState)
builder.add_node("loop_node", loop_node)
builder.add_edge(START, "loop_node")
builder.add_conditional_edges("loop_node", router)

graph = builder.compile()
graph.invoke({}, config={"recursion_limit": 10})
```

**异常中断：被动方法**

```python
class EmptyState(TypedDict):
    pass

def loop_node(state:OverAllState, config:RunnableConfig) -> EmptyState:
    cur_step = config["metadata"]["langgraph_step"]
    
builder = StateGraph(state_schema=EmptyState)
builder.add_node("loop_node", loop_node)
builder.add_edge(START, "loop_node")
builder.add_conditional_edges("loop_node", "loop_node")

graph = builder.compile()

try:
	graph.invoke({}, config={"recursion_limit": 10})
except GraphRecursionError as e:
    logger.info("超步数量达到最大限制，抛出异常：{}", e)
```

##### 节点执行与容错机制

| 问题场景 | 对应机制              | 机制类型         | 说明                                                         |
| -------- | --------------------- | ---------------- | ------------------------------------------------------------ |
| 临时故障 | 重试Retry             | 节点容错机制     | 网络抖动、接口偶发失败、模型服务短暂不可用，可以通过重试机制再次执行节点 |
| 超时     | 超时设置Timeout       | 节点容错机制     | 某个节点执行时间过长，可以通过超时限制避免图运行被长时间阻塞 |
| 异常恢复 | 异常处理ErrorHandling | 节点容错机制     | 节点抛出异常后，通过异常捕获、兜底逻辑、降级返回、路由到错误处理节点等方式恢复流程 |
| 重复计算 | 缓存Cache             | 节点执行优化机制 | 相同输入反复触发耗时节点，可以通过节点缓存避免重复执行，提高性能 |

**重试机制**

```python
builder.add_node(
    "node_a",
    node_a,
    retry_policy=RetryPolicy(
        max_attempts=3,
    )
)
```

配置详解：

| 参数             | 类型                                                         | 默认值           | 描述                                     |
| ---------------- | ------------------------------------------------------------ | ---------------- | ---------------------------------------- |
| max_attempts     | int                                                          | 3                | 最大尝试次数（包括首次执行）             |
| initial_interval | float                                                        | 0.5              | 第一次重试前的等待时间，单位：秒         |
| backoff_factor   | float                                                        | 2.0              | 每次重试后等待时间的放大倍数             |
| max_interval     | float                                                        | 128.0            | 相邻两次重试之间的最大等待时间，单位：秒 |
| jitter           | bool                                                         | True             | 是否为重试间隔添加随机抖动               |
| retry_on         | type[Exception] \| Sequence[type[Exception]] \| Callable[[Exception], bool] | default_retry_on | 哪些异常需要触发重试                     |

**超时控制**

```python
builder.add_node(
    "node_a",
    node_a,
    # 节点单次执行最多运行60秒，超过则触发超时
    timeout=TimeoutPolicy(run_timeout=60)
)
```

参数说明：

| 参数         | 描述                                                         |
| ------------ | ------------------------------------------------------------ |
| run_timeout  | 超时时间，即单次节点运行的最长时间                           |
| idle_timeout | 节点没有可观察进展（如写状态、流式输出等）时的最长空闲时间   |
| refresh_on   | 空闲时间的刷新方式，可以是手动刷新或自动刷新。默认为自动刷新，此时写状态、流式输出等都会刷新 |

**错误处理**

```python
builder.add_node(
    "node_a",
    node_a,
    timeout=TimeoutPolicy(run_timeout=60),
    # handle_api_error可以返回状态更新，也可以通过Command路由到其他节点
    error_handler=handle_api_error
)
```

##### 节点缓存

节点缓存是指：将节点的历史运行结果保存下来，后续当节点收到相同输入时，不再重复执行节点函数，而是直接返回之前缓存的结果

```python
# 1. 为节点配置cache_policy
builder.add_node(
    "node_a",
    node_a,
    cache_policy=CachePolicy(ttl=10)
)

# 2. 编译图时启用缓存后端
graph = builder.compile(cache=InMemoryCache())
```

参数说明：

| 参数     | 描述                                                         |
| -------- | ------------------------------------------------------------ |
| key_func | 根据节点输入生成缓存key的函数，默认缓存键函数default_cache_key |
| ttl      | 缓存键值对的存活时间，单位为秒                               |

#### 持久化机制和可恢复执行

##### 概述

可恢复执行：把一次任务执行过程中的关键进度、状态、结果保存到可靠存储中，使任务可以在中断、失败、等待外部输入后继续执行

持久化机制：在图执行过程中，将每个关键阶段的图状态保存为检查点，并按照线程进行组织

持久化层：

+ LangGraph的内置功能，通过检点器实现
+ 保存和恢复图执行状态的机制
+ 支持应用在中断后从上次停止的地方继续

记忆：

+ 是一种认知功能，允许AI存储、检索和使用信息
+ 在LangGraph中分为两种类型：短期记忆和长期记忆
+ 记忆是持久化的一种应用场景，使AI能够在交互中保持上下文

核心组件：

| 概念               | 作用                                                         |
| ------------------ | ------------------------------------------------------------ |
| State              | 用户定义的图状态结构，负责节点间信息传递。LangGraph会将State转换为底层Channel；运行时真正负责节点间通信的是Channel |
| Channel            | LangGraph用于节点间通信的底层机制，节点从Channel读数据、向Channel写更新；既包括承载State字段更新的状态通道，也包括用于分支、任务调度等行为的内部通道 |
| Checkpoint         | 检查点，超步边界上的底层状态快照，保存Channel的值、版本等信息 |
| CheckpointMetadata | 与Checkpoint关联的元数据，如超步编号step、父检查点ID parents等 |
| Checkpointer       | 检查点存储器，负责存储检查点、元数据、配置等信息             |
| thread             | 此处的线程不同于操作系统的线程，是指LangGraph中一条逻辑上的、可持久化的执行线，也可以理解为会话。一个会话可以包含多次调用，并在执行过程中生成多个检查点。因此，要将多次调用组织在一个会话中，必须为该thread配置checkpointer |
| thread_id          | thread的唯一标识，用于区分不同的会话。复用同一个thread_id，就表示多次调用共享同一条持久化执行线，也就是共享同一个会话历史。此处的thread/thread_id和LangChain Agent中提到的thread/thread_id是同一概念 |
| checkpoint_ns      | 检查点命名空间namespace，用于区分父图和子图的checkpoint。根图通常是空字符串""，子图会有自己的namespace。这个字段在子图持久化时很重要 |
| checkpoint_id      | Checkpoint的唯一标识                                         |
| StateSnapshot      | 开发者通过get_state()/get_state_history()看到的状态快照对象。它不是原始Checkpoint，而是LangGraph基于检查点和运行时信息封装出来的开发者视图，包含当前状态值、下一步待执行节点、任务、config、metadata、中断信息等内容 |

##### 启用可恢复执行

步骤：

1. 在编译图时传入检查点存储器对象checkpointer
2. 在调用图时传递带有thread_id的配置对象

底层的持久化机制会按照thread_id将运行时检查点记录在编译时传入的checkpointer中

 **基于内存的检查点存储器**

```python
class OverAllState(MessagesState):
    output:str
        
def llm_node(state:OverAllState) -> OverAllState:
    messages = state["messages"]
    res = model.invoke(messages)
    return {
        "messages":[res]
    }

def output_node(state:OverAllState) -> OverAllState:
    return {
        "output":state["messages"][-1].content
    }

builder = StateGraph(state_schema=OverAllState)
builder.add_node("llm_node", llm_node)
buidler.add_node("output_node", output_node)
builder.adde_edge(START, "llm_node")
builder.adde_edge("llm_node", "output_node")
builder.adde_edge("output_node", END)

checkpointer = InMemorySaver()
graph = builder.compile(checkpointer=checkpointer)

config = {
    "configurable":{
        "thread_id":"thread_1"
    }
}

graph.invoke({"messages":[HumanMessage("你好，我是老王")]}, config=config)
```

**基于持久化数据库的检查点存储器**

```python
class OverAllState(MessagesState):
    output:str
        
def llm_node(state:OverAllState) -> OverAllState:
    messages = state["messages"]
    res = model.invoke(messages)
    return {
        "messages":[res]
    }

def output_node(state:OverAllState) -> OverAllState:
    return {
        "output":state["messages"][-1].content
    }

builder = StateGraph(state_schema=OverAllState)
builder.add_node("llm_node", llm_node)
buidler.add_node("output_node", output_node)
builder.adde_edge(START, "llm_node")
builder.adde_edge("llm_node", "output_node")
builder.adde_edge("output_node", END)


DB_URL = "postgresql://langgraph_user:123456@localhost:5432/langgraph_db?sslmode=disable"
with PostgreSaver.from_conn_string(DB_URL) as checkpointer:
    checkpointer.setup()
    graph = builder.compile(checkpointer=checkpointer)
    
    config = {"configurable": {"thread_id": "thread_1"}}
    graph.invoke({"messages":[HumanMessage("你好，我是老王")]}, config=config)
```

setup()在数据库中创建了4张表：

| 表名                  | 用途                                                      |
| --------------------- | --------------------------------------------------------- |
| checkpoints           | 存储完整的检查点快照（状态、元数据等）                    |
| checkpoint_writes     | 存储每个检查点对应的通道写入记录                          |
| checkpoint_blobs      | 存储大型二进制数据（如图片、文件等，如果状态中包含的话）  |
| checkpoint_migrations | 记录数据库迁移版本，LangGraph内部使用，用于管理表结构变更 |

checkpoints表：

| 字段                 | 类型  | 说明                                                         |
| -------------------- | ----- | ------------------------------------------------------------ |
| thread_id            | text  | 线程ID，对应config中的thread_id，用于区分不同会话            |
| checkpoint_ns        | text  | 检查点命名空间，默认空字符串，用于子图等场景隔离不同层级的检查点 |
| checkpoint_id        | text  | 检查点唯一ID，由LangGraph内部生成                            |
| parent_checkpoint_id | text  | 父检查点ID，构建检查点链，用于Time Travel回退                |
| type                 | text  | 检查点类型                                                   |
| checkpoint           | jsonb | 检查点核心数据（状态快照），以JSON格式存储                   |
| metadata             | jsonb | 检查点元数据（创建时间、来源、步骤序号等）                   |

+ 主键为（thread_id,checkpoint_ns,checkpoint_id）联合主键
+ 另有thread_id单列索引，加速按线程筛选查询

checkpoint_writes表：

| 字段          | 类型    | 说明                                           |
| ------------- | ------- | ---------------------------------------------- |
| thread_id     | text    | 线程ID                                         |
| checkpoint_ns | text    | 命名空间，与checkpoints表对应                  |
| checkpoint_id | text    | 所属检查点ID                                   |
| task_id       | text    | 产生写入的节点任务ID                           |
| idx           | integer | 写入顺序索引                                   |
| channel       | text    | 写入的目标通道名（如messages、__pregel_tasks） |
| type          | text    | 写入数据类型                                   |
| blob          | bytea   | 写入的二进制数据                               |
| task_path     | text    | 任务路径，用于追踪节点在子图中的层级关系       |

+ 主键为（thread_id,checkpoint_ns,checkpoint_id,task_id,idx）联合主键，确保同一任务下的写入顺序唯一

checkpoint_blobs表：

| 字段          | 类型  | 说明                                 |
| ------------- | ----- | ------------------------------------ |
| thread_id     | text  | 线程ID                               |
| checkpoint_ns | text  | 命名空间                             |
| channel       | text  | 通道名                               |
| version       | text  | 数据版本标识，同一通道可以有多个版本 |
| type          | text  | 数据类型                             |
| blob          | bytea | 二进制数据本体                       |

+ 主键为（thread_id,checkpoint_ns,channel,version）联合主键
+ 与checkpoint_writes不同，checkpoint_blobs以（channel,version）维度管理数据版本，不直接关联某个具体的checkpoint_id，允许多个检查点共享同一通道的大型二进制数据

checkpoint_migrations表：

| 字段 | 类型    | 说明                 |
| ---- | ------- | -------------------- |
| v    | integer | 当前数据库迁移版本号 |

+ 单列主键，仅一条记录，setup()执行时自动检查并更新
+ 该表由LangGraph内部管理，用户无需手动操作。未来LangGraph版本升级时，若表结构有变更，setup()会根据此版本号自动执行对应的迁移SQL

##### 持久化模式

LangGraph支持三种持久化模式，采用不同的检查点保存时机，在容灾能力和性能开销、响应时效性之间作取舍

+ exit：退出模式。只在计算图正常结束、异常退出、被中断（如Human-In-The-Loop中断）时保存检查点。不能处理中途进程崩溃的场景。这种模式响应最不及时、性能开销最小，但容灾能力最弱
+ async：异步模式。默认模式，顾名思义，检查点在后台异步写入。它会在每个超步结束后写入完整检查点（主检查点），并在图中任务执行完毕后记录中间结果。和exit模式相比，增加了性能开销，但写入操作发生在后台，不会引入明显的响应延迟，同时提升了容灾能力
+ sync：同步模式。和异步模式唯一的区别在于，LangGraph会在进入下一个超步之前等待当前主检查点的写入任务完成。它的容灾能力最强，但在async模式的基础上增加了响应延迟

```python
graph.invoke(
    {"messages":[HumanMessage("你好")]},
    config=config,
    # 持久化模式
    durability="async"
)
```

##### 查看历史检查点

+ graph.get_state_history(config)：查看指定会话的完整历史检查点
+ graph.get_state(config)：查看指定会话的最新检查点，或者查看某个指定checkpoint_id对应的检查点

**根据ID查看指定检查点**

```python
target_config = {
    "configurable": {
        "thread_id": "123",
        "checkpoint_id": "某个历史checkpoint_id"
    }
}

graph.get_state(config=target_config)
```

**失败后恢复运行**

1. 启用检查点存储器
2. 再次运行时用None作为计算图的状态输入
3. 传递的配置信息应包含thread_id而不能包含checkpoint_id

此时，LangGraph会根据thread_id从checkpointer读取该会话的最新检查点，并从改检查点继续推进

```python
config = {
    "configurable": {
        "thread_id": "123"
    }
}
graph.invoke(None,config=config)
```

##### 优化记忆

+ 消息过滤：对旧消息进行类似删除或编辑的操作，目的是为了防止撑爆上下文
+ 消息总结：对旧消息进行总结，目的一样是为了防止记忆内容过长
+ 注意对记忆的管理是一项关于召回率和精度的平衡艺术

```python
# 消息裁剪
memory = MemorySaver()

@tool
def search(query: str):
    """调用此函数可以浏览网络"""
    # 模拟一个网络搜索返回
    return "北京天气晴朗 大约22度 湿度30%"
    
tools = [search]
tool_node = ToolNode(tools)

model = ChatDeepSeek(
    model="deepseek-v4-flash",
    temperature=0,
    api_key=DEEPSEEK_API_KEY,
    base_url=DEEPSEEK_BASE_URL
)
bound_model = model.bind_tools(tools)

def should_continue(state: MessagesState):
    """返回下一个要执行的节点"""
    last_message = state["messages"][-1]
    if not last_message.tool_calls:
        return END
    return "action"

def filter_messages(messages: list):
    return messages[-1:]

def call_model(state: MessagesState):
    # 消息裁剪
    messages = filter_messages(state["messages"])
    response = bound_model.invoke(messages)
    return {"messages": response}

workflow = StateGraph(MessagesState)

workflow.add_node("agent", call_model)
workflow.add_node("action", tool_node)
workflow.add_edge(START, "agent")

workflow.add_conditional_edges(
    # 起始节点
    "agent",
    # 确定下一个调用哪个节点的函数
    should_continue,
    # 路径映射-这条边可能去往的所有可能节点
    ["action", END]
)
workflow.add_edge("action", "agent")
app = workflow.compile(checkpointer=memory)
```

```python
# 消息总结
memory = MemorySaver()

class State(MessagesState):
    summary: str

model = ChatDeepSeek(
    model="deepseek-v4-flash",
    temperature=0,
    api_key=DEEPSEEK_API_KEY,
    base_url=DEEPSEEK_BASE_URL
)

def call_model(state: State):
    # 如果存在摘要，我们将其作为系统消息添加
    summary = state.get("summary", "")
    if summary:
        system_message = f"之前对话的摘要：{summary}"
        messages = [SystemMessage[content=system_message]] + state["messages"]
    else:
        messages = state["messages"]
    response = model.invoke(messages)
    return {"messages": [response]}

def should_continue(state: State) -> Literal["summarize_conversation", END]:
    """返回下一个要执行的节点"""
    messages = state["messages"]
    if len(messages) > 6:
        return "summarize_conversation"
    return END

def summarize_conversation(state: State):
    summary = state.get["summary", ""]
    if summary:
        summary_message = (
            f"这是迄今为止对话的摘要：{summary}\n\n"
            "考虑上面的新消息，扩展摘要:"
        )
    else:
        summary_message = "创建上述对话的摘要:"
        
    messages = sate["messages"] + [HumanMessage(content=summary_message)]
    response = model.invoke(messages)
    # 删除最后两条以外的所有消息
    delete_messages = [RemoveMessage(id=m.id) for m in state["messages"][:-2]]
    return {"summary": response.content, "messages": delete_messages}

workflow = StateGraph(State)

workflow.add_node("conversation", call_model)
workflow.add_node(summarize_conversation)
workflow.add_edge(START, "conversation")

workflow.add_conditional_edges(
    "conversation",
    should_continue,
)
workflow.add_edge("summarize_conversation", END)
app = workflow.compile(checkpointer=memory)
```

#### 图记忆管理

Agent的三种记忆：

+ 短期记忆：通过运行时状态State访问，并由检查点存储器checkpointer保存，它按照thread_id组织，可以实现线程内的记忆共享
+ 长期记忆：通过长期记忆存储器Store访问和存储。数据通常按照元组类型的命名空间组织，以键值对的形式存储，提供跨会话的记忆共享
+ 运行时上下文：通过上下文对象Context访问，只对本次调用生效，不会被持久化。适合传递本次运行所需的外部依赖或调用参数，如用户名、模型配置、数据库连接、权限信息等

##### 短期记忆

见启用可恢复执行章节

##### 长期记忆

长期记忆存储器在编译图时通过store参数传递，图节点中可以通过Runtime对象访问

**存在数据库**

```python
DB_URL = "postgresql://langgraph_user:123456@localhost:5432/langgraph_db?sslmode=disable"
with PostgresStore.from_conn_string(DB_URL) as store:
    store.setup()
    
    USERS_NS:Final[Tuple[str]] = ("users",)
    PREFERENCES_KEY:Final[str] = "preferences"
        
    namespace1 = (*USERS_NS, "Alice")
    namespace2 = (*USERS_NS, "Bob")
    namespace3 = (*USERS_NS, "Black")
    
    value1 = {
        "course": "计算机组成原理",
        "sports": "跑步",
        "food": "紫光园奶皮子酸奶"
    }
    
    value2 = {
        "course": "数字电路与模拟电路",
        "sports": "跑步",
        "food": "奶皮子糖葫芦"
    }
    
    value3 = {
        "course": "数字电路与模拟电路",
        "sports": "羽毛球",
        "food": "紫光园奶皮子酸奶"
    }
    
    store.put(namespace1, PREFERENCES_KEY, value1)
    store.put(namespace2, PREFERENCES_KEY, value2)
    store.put(namespace3, PREFERENCES_KEY, value3)
    
    for item in store.search(USERS_NS):
        print(item)
```

```python
class OverAllState(MessagesState):
    username:str
    user_input:str
    output:str
    preferences:dict[str,str]
        
def router(state:OverAllState) -> Literal["check_preference_node", "llm_node"]:
    if not state.get("preferences"):
        logger.info("需要从长期记忆中读取用户偏好")
        return "check_preference_node"
    return "llm_node"

def check_preference_node(state:OverAllState, runtime:Runtime) -> OverAllState:
    username = state["username"]
    namespace = (*USERS_NS, username)
    key = PREFERENCES_KEY
    
    run_store = runtime.store
    run_item = run_store.get(namespace, key)
    
    if not run_item:
        logger.warning("长期记忆中没有{}的偏好数据", username)
        return {}
    
    logger.info("长期记忆中保存的{}的偏好数据是{}", username, run_item.value)
    return {
        "preferences": run_item.value
    }

def llm_node(state:OverAllState) -> OverAllState:
    preference = state.get("preferences", {})
    user_input = state["user_input"]
    human_prompt = (f"这是用户的偏好：{preference}\n,这是用户的需求：{user_input}")
    system_prompt = "请根据用户的偏好解决用户的需求"
    
    messages = [SystemMessage(content=system_prompt)] if not state.get("messages", []) else state["messages"]
    
    model_response = model.invoke(messages + [HumanMessage(content=human_prompt)])
    output = model_response.content
    
    return {
        "messages":messages + [HumanMessage(content=human_prompt), model_response],
        "output":output
    }

builder = StateGraph(state_schema=OverAllState)
builder.add_node("check_preference_node", check_preference_node)
builder.add_node("llm_node", llm_node)

builder.add_conditional_edges(START, router, path_map=["check_preference_node", "llm_node"])
builder.add_edge("check_preference_node", "llm_node")
builder.add_edge("llm_node", END)

with PostgresStore.from_conn_string(DB_URL) as store, \
	PostgresSaver.from_conn_string(DB_URL) as checkpointer:
        checkpointer.setup()
        graph = builder.compile(checkpointer=checkpointer, store=store)
        
        config = {
            "configurable":{"thread_id": "777"}
        }
        res = graph.invoke({"username":"Alice", "user_input":"我有点无聊，和我聊聊天吧"}, config=config)
```

**存在内存**

```python
# 使用内存存储来保存向量化后记忆数据
in_memory_store = InMemoryStore(
    index={
        "embed": OpenAIEmbeddings(
            model="deepseek-v4-flash",
            api_key=DEEPSEEK_API_KEY,
            base_url=DEEPSEEK_BASE_URL
        ),
        "dims": 1024
    }
)

model = ChatDeepSeek(
    model="deepseek-v4-flash",
    temperature=0,
    api_key=DEEPSEEK_API_KEY,
    base_url=DEEPSEEK_BASE_URL
)

def call_mode(state: MessagesState, config: RunnableConfig, *, store: BaseStore):
    # 从存储中检索用户信息
    user_id = config["configurable"]["user_id"]
    namespace = ("memories", user_id)
    memories = store.search(namespace, query=str(state["messages"][-1].content))
    info = "\n".join([d.value["data"] for d in memories])
    system_msg = {"你是一个正在与用户交谈的小助手。用户信息：{info}"}
    
    # 如果用户要求模型记住信息，则存储新的记忆
    last_message = state["messages"][-1]
    if "记住" in last_message.content.lower() or "remember" in last_message.content.lower():
        memory = "用户名字是tomiezhang"
        store.put(namespace, str(uuid.uuid4()), {"data": memory})
        
    response = model.invoke(
        [{"role": "system", "content": system_msg}] + state["messages"]
    )
    return {"messages": response}

builder = StateGraph(MessagesState)
builder.add_node("call_model", call_model)
builder.add_edge(START, "call_model")

graph = builder.compile(checkpointer=MemorySaver(), store=in_memory_store)
```

##### 运行时上下文

使用方式：

1. 初始化状态图时，使用context_schema定义上下文类型
2. 调用图时通过context参数传入上下文对象
3. 节点或路由函数中通过runtime.context访问上下文

```python
@dataclass
class UserContext:
    username:str
    membership_level:str
        
class OverAllState(MessagesState):
    user_input:str
    output:str
        
def llm_node(state:OverAllState, runtime:Runtime[UserContext]) -> OverAllState:
    runtime_context = runtime.context
    level = runtime_context.membership_level
    
    if runtime_context:
        level = runtime_context.membership_level
        username = runtime_context.username
        logger.info(f"当前用户：{username},会员等级：{level}")
        
        if level == "VIP":
            system_prompt = f"你是高级客户经理，当前VIP用户是{username},请使用尊称'您',语气热情周到，回复末尾加上'VIP服务'"
        else:
            system_prompt = f"你是普通客户经理，当前用户是{username},请友好简洁回复问题"
            
        user_input = state["user_input"]
        messages = state.get("messages",[])
        response = model.invoke([SystemMessage(content=system_prompt)] + messages + [HumanMessage(content=user_input)]).content
        
        return {
            "messages": messages,
            "output":response
        }
    
builder = StateGraph(state_schma=OverAllState, context_schema=UserContext)
builder.add_node("llm_node", llm_node)
builder.add_edge(START, "llm_node")
builder.add_edge("llm_node", END)

graph = builder.compile()
res = graph.invoke(
    {"user_input":"你好，帮我查一下最近有什么优惠活动"},
    context=UserContext(username="Alice",membership_level="VIP")
)
```

##### Node总结：LangGraph的节点

LangGraph的节点通常是一个可调用对象，最常见的是同步或异步Python函数

除此之外，节点也可以是符合要求的其他可调用对象或Runnable实例

从源码实现角度看，节点最终会被转换或包装为可运行对象，并作为PregelNode.bound保存

**节点函数的完整形态**

四个参数：

+ state：输入节点的图状态。state是位置传参，因此参数名称不重要，但通常约定命名为state

+ config：状态图的运行时配置，它是一个RunnableConfig实例，底层运行时在节点函数执行前通过关键字传参动态注入

  可以通过config访问当前线程的thread_id、超步序号、递归限制等配置信息。

  ```python
  config["configurable"]["thread_id"]
  config["recursion_limit"]
  config["metadata"]
  ```

+ runtime：状态图的运行时对象，可以通过它访问运行时上下文、长期记忆存储器等信息。底层运行时在节点函数执行前通过关键字传参动态注入

+ writer：流式写入器，通常用于自定义流式输出

#### 中断

LangGraph提供了两种中断机制：

+ 动态中断：在图的任意节点中调用interrupt()函数实现

  它可以放在代码的任意位置，并且可以根据应用逻辑设置条件触发，所以是动态的

  动态中断提供了人机交互接口，使得调用者可以人为干预计算图的运行，是业务逻辑的一部分

+ 静态中断：在编译或调用状态图时通过interrupt_before和interrupt_ager参数设置断点

  它是在运行前确定的，不能根据业务逻辑条件触发，所以是静态的

  静态中断主要用于调试，不是业务逻辑的一部分

##### 动态中断

启用中断

1. 配置检查点存储器

2. 设置thread_id

   如果要确保中断后可以恢复执行，就需要保存运行图的完整状态，所以必须启用可恢复执行

3. 在需要中断的位置调用interrupt()

恢复中断

1. 基于相同的配置再次调用计算图
2. 将输入替换为Command()实例即可恢复运行

LangGraph会从中断节点继续运行，该节点会被再次执行。

通过Command实例的resume属性将用户反馈传递给计算图，中断节点重新运行时，传递给resume属性的值将会作为interrupt()函数的返回值

常见使用模式

1. 基础HITL（Human-In-The-Loop）模式：状态图触发一次中断，获取人类输入后继续执行
2. 多个并行中断：多个并行任务分别产生中断，并根据中断ID接收各自的恢复数据
3. 审批模式：根据人类审批（批准、拒绝或其它处理方式），决定后续执行路径
4. 审核与编辑模式：把模型生成的内容交给人类检查，并允许人类直接修改后继续处理
5. 工具执行审批模式：在调用工具或执行具有副作用的操作之前，由人类确认是否允许执行
6. 单节点串行中断模式：在同一节点内多次触发中断，上一个中断恢复后才能触发下一个
7. 人类输入验证模式：对人类输入进行检验；当输入不符合要求时，再次中断并要求重新输入

**HITL**

```python
class OverAllState(TypedDict):
    username:str
        
def node_a(state:OverAllState) -> OverAllState:
    username = interrupt("请输入你的姓名")
    return {
        "username":username
    }

builder = StateGraph(state_schema=OverAllState)
builder.add_node("node_a", node_a)
builder.add_edge(START, "node_a")
builder.add_edge("node_a", END)

# 想要使用中断必须配置检查点
checkpointer = InMemorySaver()
graph = builder.compile(checkpointer=checkpointer)

config = {"configurable":{"thread_id":"123"}}
interrupt_res = graph.invoke({},config=config)


username = input(interrupt_res['__interrupt__'][0].value)
resume_res = graph.invoke(Command(resume=username),config=config)
```

**多个并行中断**

```python
class OverAllState(TypedDict):
    username:str
    age:int
        
def node_a(state:OverAllState) -> OverAllState:
    username = interrupt("请输入你的姓名")
    return {
        "username":username
    }

def node_b(state:OverAllState) -> OverAllState:
    age = interrupt("请输入你的年龄")
    return {
        "age":age
    }

builder = StateGraph(state_schema=OverAllState)
builder.add_node("node_a", node_a)
builder.add_node("node_b", node_b)
builder.add_edge(START, "node_a")
builder.add_edge(START, "node_b")
builder.add_edge("node_a", END)
builder.add_edge("node_b", END)

checkpointer = InMemorySaver()
graph = builder.compile(checkpointer=checkpointer)

config = {"configurable":{"thread_id":"123"}}
interrupt_res = graph.invoke({},config=config)

resume_map = {}
for i in interrupt_res['__interrupt__']:
    user_input = input(f"{i.value}:")
    if "年龄" in i.value:
        resume_map[i.id] = int(user_input)
    else:
        resume_map[i.id] = user_input
        
resume_res = graph.invoke(Command(resume=resume_map),config=config)
```

**审批模式**

```python
class OverAllState(TypedDict):
    topic:str
    poem:int
    is_approved:bool
        
def approve_node(state:OverAllState) -> Command[Literal["llm_node", "default_node"]]:
    is_approved = interrupt("是否同意调用模型?")
    goto = "llm_node" if is_approved else "default_node"
    return Command(
        goto=goto,
        update={"is_approved":is_approved}
    )

def llm_node(state:OverAllState) -> OverAllState:
    topic = state["topic"]
    res = model.invoke([HumanMessage(content=f"帮我写一首关于{topic}主题的七言绝句，只写诗句，不需要赏析")]).content
    
    return {
        "poem":res
    }

def default_node(state:OverAllState) -> OverAllState:
    return {
        "poem":"请求被拒绝"
    }

builder = StateGraph(state_schema=OverAllState)
builder.add_node("approve_node", approve_node)
builder.add_node("llm_node", llm_node)
builder.add_node("default_node", default_node)
builder.add_edge(START, "approve_node")
builder.add_edge("llm_node", END)
builder.add_edge("default_node", END)

checkpointer = InMemorySaver()
graph = builder.compile(checkpointer=checkpointer)

config = {"configurable":{"thread_id":"123"}}
interrupt_res = graph.invoke({"topic":"菊花"},config=config)
        
user_approved = input("是否同意调用模型?(y/n)").strip().lower() == 'y'
approved_res = graph.invoke(Command(resume=user_approved),config=config)
```

**审核与编辑模式**

```python
class OverAllState(TypedDict):
    topic:str
    poem:str
    reviewed_poem:str
        
def llm_node(state:OverAllState) -> OverAllState:
    topic = state['topic']
    
    res = model.invoke([HumanMessage(content=f"帮我写一首关于{topic}主题的七言绝句，只写诗句，不需要赏析")]).content
    
    return {
        "poem":res
    }

def review_node(state:OverAllState) -> OverAllState:
    reviewed_poem = interrupt({
        "instruction":"请审核并修改大模型生成的七言绝句",
        "poem":state["poem"]
    })
    
    return {
        "reviewed_poem":reviewed_poem
    }
    
builder = StateGraph(state_schema=OverAllState)
builder.add_node("llm_node", llm_node)
builder.add_node("review_node", review_node)
builder.add_edge(START, "llm_node")
builder.add_edge("llm_node", "review_node")
builder.add_edge("review_node", END)

checkpointer = InMemorySaver()
graph = builder.compile(checkpointer=checkpointer)

config = {"configurable":{"thread_id":"123"}}
interrupt_res = graph.invoke({"topic":"布偶猫"},config=config)
        
user_review = input("请审核并修改诗句")
reviewed_res = graph.invoke(Command(resume=user_review),config=config)
```

**工具执行审批模式**

```python
@tool(parse_docstring=True)
def get_weather(city:str):
    """
    查询指定城市的当日天气
    
    Args:
    	city: 城市名称
    """
    is_approved = interrupt({
        "action": "get_weather",
        "question": "是否同意查询天气"
    })
    
    if is_approved:
        return f"{city}今天天气不错"
    else:
        return "用户拒绝查询天气"
    
tools_by_name = {"get_weather": get_weather}
model = ChatDeepSeek(
    model="deepseek-v4-flash",
    exrea_body={
        "thinking": {
            "type": "disabled"
        }
    }
)
model_with_tools = model.bind_tools([get_weather])

def llm_node(state:MessagesState) -> MessagesState:
    messages = state["messages"]
    reponse = model_with_tools.invoke(messages)
    return {
        "messages": [response]
    }

def tool_node(state:MessagesState) -> MessagesState:
    last_msg = state["messages"][-1]
    tool_msgs = []
    for tool_call in last_msg.tool_calls:
        tool = tools_by_name[tool_call["name"]]
        tool_res = tool.invoke(tool_call["args"])
        tool_msg = ToolMessage(
            name = tool_call["name"],
            content = tool_res,
            tool_call_id = tool_call["id"]
        )
        tool_msgs.append(tool_msg)
    
    return {
        "messages": tool_msgs
    }

def router(state:MessagesState) -> Literal["tool_node", END]:
    if state["messages"][-1].tool_calls:
        return "tool_node"
    return END
    
builder = StateGraph(state_schema=MessagesState)
builder.add_node("llm_node", llm_node)
builder.add_node("tool_node", tool_node)
builder.add_edge(START, "llm_node")
builder.add_conditional_edges("llm_node", router, path_map=["tool_node", END])
builder.add_edge("tool_node", "llm_node")

checkpointer = InMemorySaver()
graph = builder.compile(checkpointer=checkpointer)

config = {"configurable":{"thread_id":"123"}}
interrupt_res = graph.invoke({"messages":[HumanMessage("今天北京天气如何")]},config=config)
        
user_approved = input("是否同意查询天气")
reviewed_res = graph.invoke(Command(resume=user_approved),config=config)
```

**单节点串行中断模式**

```python
class OverAllState(TypedDict):
    username:str
    age:int
    gender: Literal["male", "female"]
        
def get_info_node(state: OverAllState) -> OverAllState:
    username = interrupt("请输入你的用户名：")
    age = interrupt("请输入你的年龄：")
    gender = interrupt("请输入你的性别：(male/female)")
    
    return {
        "username": username,
        "age": age,
        "gender": gender
    }

builder = StateGraph(state_schema=OverAllState)
builder.add_node("get_info_node", get_info_node)
builder.add_edge(START, "get_info_node")
builder.add_edge("get_info_node", END)

checkpointer = InMemorySaver()
graph = builder.compile(checkpointer=checkpointer)

config = {"configurable":{"thread_id":"123"}}
interrupt_res = graph.invoke({},config=config)
        
user_name = input("请输入你的用户名：")")
name_interrupted_res = graph.invoke(Command(resume=user_name),config=config)

user_age = input("请输入你的年龄：")")
age_interrupted_res = graph.invoke(Command(resume=user_age),config=config)

user_gender = input("请输入你的性别：(male/female)")")
gender_interrupted_res = graph.invoke(Command(resume=user_gender),config=config)
```

中断恢复时，整个被中断的节点函数都会重新运行

单个节点中串行的多次调用interrupt()函数时，检查点存储器会记录历史的resume信息，LangGraph运行时会读取这些信息，并在interrupt()函数中维护索引，按照节点内调用interrupt()函数的顺序，逐个取出历史resume的值，并将它们作为interrupt()函数的返回值

所以，已经被恢复的interrupt()不会被重复触发。并且，由此可以推断，我们要保证历史resume可以被正确应用，就应保证中断恢复前后的多次interrupt()相对顺序保持不变

当历史resume耗尽后，本次恢复运行时传入的resume会作为本次中断的返回值，然后节点函数继续运行。

所有中断都触发并恢复后，计算图正常结束

##### 使用规范

+ 不要用try/catch包裹interrupt()调用

  中断的触发是通过抛出GraphInterrupt异常实现的，如果用try/catch包裹，则底层运行时无法感知断点，不会中断计算图

+ 不要更改单个节点内interrupt的调用顺序

+ 不要在interrupt()中传递复杂类型

  LangGraph运行时会将interrupt()函数接收到的参数经过JSON序列化之后传递给调用者

  如果传递不支持JSON序列化的复杂类型，如函数，将会抛出异常

+ 断点之前的副作用操作必须是幂等的

  副作用操作：修改了外部环境的操作，如写数据库、写文件、发送邮件或消息

##### 静态断点

**用法说明**

1. 静态断点也需要基于检查点恢复，所以必须配置检查点，启用可恢复运行机制

2. 计算图会在interrupt_before指定的节点执行前产生中断，暂停计算

3. 计算图会在interrupt_after指定的节点执行后产生中断，暂停计算

4. 计算图运行到断点位置会中断，和动态断点不同的是，它会在超步边界而非内部中断

   断点前的超步：三个阶段全部完成；断点后的超步：三个阶段都没有开始

5. 静态断点不会返回任何中断信息，只会将当前最新的状态返回

   因此，我们可以用静态断点查看每个超步边界的中间状态

6. 传入相同的配置并将None作为输入，再次调用计算图，会从断点位置继续运行

7. 支持在两个阶段设置静态断点

   + 状态图编译时

     ```python
     graph = builder.compile(
         checkpointer=checkpointer,
         interrupt_before=["node_a", "node_b"],
         interrupt_after=["node_a", "node_b"]
     )
     ```

   + 计算图调用时

     ```python
     first_res = graph.invoke(
         {},
         config=config,
         interrupt_before=["node_a", "node_b"],
         interrupt_after=["node_a", "node_b"]
     )
     ```

   断点都是在运行时生效，计算图调用时设置的断点优先级更高，如果调用时传入的断点列表不为空，则会覆盖编译时配置

**底层原理**

1. 编译时设置的静态断点保存在编译图的interrupt_before_nodes和interrupt_after_nodes中。运行时采用调用参数优先、编译配置兜底的规则。调用时传入非空节点列表会完全覆盖编译配置，而None或空列表会回退到编译配置

2. interrupt_before在超步的第一阶段：当前的超步的任务列表计算完成后、节点任务开始执行前进行检查。

   + 如果检查点中的状态和上一次静态断点或图的初始状态相比，产生了变化
   + 并且当前超步的任务列表和interrupt_before列表存在交集

   则抛出GraphInterrupt

3. interrupt_after在超步的第三阶段：当前超步的任务全部执行完成、写入应用到通道并创建检查点之后进行检查

   + 如果检查点中的状态和上一次静态断点或图的初始状态相比，产生了变化
   + 并且当前超步的任务列表和interrupt_after列表存在交集

   则抛出GraphInterrupt

4. 如果存在这样的拓扑图

   node_a -> node_b

   那么在node_a之后和node_b之前设置断点只会中断一次

   因为二者对应同一个超步边界，如果同时配置：

   + node_a的after先触发
   + 恢复运行时检查点中记录的上次断点可见状态会被更新，和最新状态保持一致
   + 而产生中断之前已经保存了检查点，恢复运行时会从node_b的超步开始
   + node_b的超步第一阶段，检查点和上次静态断点相比，没有变化，所以不会中断
   + 因此，node_a之后和node_b之前设置断点只会中断一次

**超步三个阶段**

+ 路由：根据当前State和Edge逻辑，确定本轮选中的节点

  上一轮node_1已执行完毕，出边node_1->noode_2被触发；此时只标注路由方向，node_2尚未开始

+ 执行：选中的节点开始执行

  node_2读取当前State快照，独立执行函数逻辑，产生对State的局部更新。此阶段产生的更新暂不提交到全局State

+ 提交：状态更新阶段，所有节点执行完毕，输出合并到State

  node_2的返回结果统一提交。生成新快照 -> 无更多可执行节点，图运行结束

**编译时设置断点**

```python
# 顺序节点设置断点
class OverAllState(TypedDict):
    final_res:str
        
def node_a(state:OverAllState) -> OverAllState:
    return {
        "final_res": "node_a运行的中间结果"
    }

def node_b(state:OverAllState) -> OverAllState:
    return {
        "final_res": "node_b运行的中间结果"
    }

def node_c(state:OverAllState) -> OverAllState:
    return {
        "final_res": "node_c运行的中间结果"
    }

builder = StateGraph(state_schema=OverAllState)
builder.add_node("node_a", node_a)
builder.add_node("node_b", node_b)
builder.add_node("node_c", node_c)

builder.add_edge(START, "node_a")
builder.add_edge("node_a", "node_b")
builder.add_edge("node_b", "node_c")
builder.add_edge("node_c", END)

checkpointer = InMemorySaver()
graph = builder.compile(
    checkpointer=checkpointer,
    interrupt_before=["node_a", "node_b"],
    interrupt_after=["node_a", "node_b"]
)

config = {"configurable":{"thread_id":"123"}}
# 第一次执行
first_res = graph.invoke({},config=config)
# 第二次执行
second_res = graph.invoke(None,config=config)
# 第三次执行
third_res = graph.invoke(None,config=config)
# 第四次执行
fourth_res = graph.invoke(None,config=config)
```

```python
# 存在并行节点时设置断点
class OverAllState(TypedDict):
    final_res:str
        
def node_a(state:OverAllState) -> OverAllState:
    return {
        "final_res": "node_a运行的中间结果"
    }

def node_b(state:OverAllState) -> OverAllState:
    return {
        "final_res": "node_b运行的中间结果"
    }

def node_c(state:OverAllState) -> OverAllState:
    return {
        "final_res": "node_c运行的中间结果"
    }

def node_d(state:OverAllState) -> OverAllState:
    return {
        "final_res": "node_d运行的中间结果"
    }

def node_e(state:OverAllState) -> OverAllState:
    return {
        "final_res": "node_e运行的中间结果"
    }

builder = StateGraph(state_schema=OverAllState)
builder.add_node("node_a", node_a)
builder.add_node("node_b", node_b)
builder.add_node("node_c", node_c)
builder.add_node("node_d", node_d)
builder.add_node("node_e", node_e)

builder.add_edge(START, "node_a")
builder.add_edge(START, "node_d")
builder.add_edge("node_a", "node_b")
builder.add_edge("node_b", "node_c")
builder.add_edge("node_d", "node_e")
builder.add_edge("node_e", "node_c")
builder.add_edge("node_c", END)

checkpointer = InMemorySaver()
graph = builder.compile(
    checkpointer=checkpointer,
    interrupt_before=["node_a", "node_b"],
    interrupt_after=["node_a", "node_b"]
)

config = {"configurable":{"thread_id":"123"}}
# 第一次执行,a之前中断，既不会到a，也不会到d
first_res = graph.invoke({},config=config)
# 第二次执行,a、d被执行
second_res = graph.invoke(None,config=config)
# 第三次执行，b、e被执行
third_res = graph.invoke(None,config=config)
# 第四次执行
fourth_res = graph.invoke(None,config=config)
```

中断发生在超步边界，而非节点边界

**调用时设置断点**

调用时的断点配置只对本次调用生效

正确用法：恢复调用时设置相同的断点

```python
# 第一次执行
first_res = graph.invoke(
    {},
    config=config,
    interrupt_before=["node_a", "node_b"],
    interrupt_after=["node_a", "node_b"]
)
# 第二次执行
second_res = graph.invoke(
    None,
    config=config,
    interrupt_before=["node_a", "node_b"],
    interrupt_after=["node_a", "node_b"]
)
```

##### 审查工具调用

```python
@tool
def weather_search(city: str):
    """搜索天气"""
    return "晴朗"

deepseek = ChatDeepSeek(
    model="deepseek-v4-flash",
    temperature=0,
    api_key=DEEPSEEK_API_KEY,
    base_url=DEEPSEEK_BASE_URL
)
model = deepseek.bind_tools([weather_search])

class State(MessagesState):
    """简单状态"""
    
def call_llm(state):
    return {"messages": [model.invoke(state["messages"])]}

def human_review_node(state) -> Command[Literal["call_llm", "run_tool"]]:
    last_message = state["messages"][-1]
    tool_call = last_message.tool_calls[-1]
    
    # 这是我们将通过Command(resume=<human_review>)提供的值
    human_review = interrupt(
        {
            "question": "这是正确的吗",
            "tool_call": tool_call  # 显示工具调用以供审核
        }
    )
    
    review_action = human_review["action"]
    review_data = human_review.get("data")
    
    # 如果批准，调用工具
    if review_action == "continue":
        return Command(goto="run_tool")
    # 更新AI消息并调用工具
    elif revie_action == "update":
        updated_message = {
            "role": "ai",
            "content": last_message.content,
            "tool_calls": [
                {
                    "id": tool_call["id"],
                    "name": tool_call["name"],
                    "args": review_data,
                }
            ],
            # 这很重要，需要与你替换的消息相同，否则，它将显示为一个单独的消息
            "id": last_message.id,
        }
        return Command(goto="run_tool", update={"messages": [updated_message]})
    # 向LLM提供反馈
    elif review_action == "feedback":
        tool_message = {
            "role": "tool",
            "content": review_data,
            "name": tool_call["name"],
            "tool_call_id": tool_call["id"],
        }
        return Command(goto="call_llm", update={"messages": [tool_message]})
  
def run_tool(state):
    new_messages = []
    tools = {"weather_search": weather_search}
    tool_calls = state["messags"][-1].tool_calls
    for tool_call in tool_calls:
        tool = tools[tool_call["name"]]
        result = tool.invoke(tool_call["args"])
        new_messages.append(
            {
                "role": "tool",
                "name": tool_call["name"],
                "content": result,
                "tool_call_id": tool_call["id"],
            }
        )
        return {"messages": new_messages}
    
def route_after_llm(state) -> Literal[END, "human_review_node"]:
    if len(state["messages"][-1].tool_calls) == 0:
        return END
    else:
        return "human_review_node"
 
builder = StateGraph(State)
builder.add_node(call_llm)
builder.add_node(run_tool)
builder.add_node(human_review_node)
builder.add_edge(START, "call_llm")
builder.add_conditional_edges("call_llm", route_after_llm)
builder.add_edge("run_tool", "call_llm")

memory = MemorySaver()
graph = builder.compile(checkpointer=memory)
display(Image(graph.get_graph().draw_mermaid_png()))


# 使用Command进行人机交互
for event in graph.stream(
    Command(resume={"action": "continue"}),
    thread,
    stream_mode="updates"
):
    print(event)
```

##### 对图状态进行编辑

```python
class State(TypedDict):
    input: str
     
def step_1(state):
    print("---step1---")
    
def step_2(state):
    print("---step2---")
    
def step_3(state):
    print("---step3---")
    
builder = StateGraph(State)
builder.add_node("step_1", step_1)
builder.add_node("step_2", step_2)
builder.add_node("step_3", step_3)
builder.add_edge(START, "step_1")
builder.add_edge("step_1", "step_2")
builder.add_edge("step_2", "step_3")
builder.add_edge("step_3", END)

memory = MemorySaver()
graph = builder.compile(checkpointer=memory, interrupt_before=["step_2"])

display(Image(graph.get_graph().draw_mermaid_png()))

initial_input = {"input": "你好"}
thread = {"configurable": {"thread_id": "1"}}
# 输出---step1---后打断
for event in graph.stream(initial_input, thread, stream_mode="values"):
    print(event)
    
# 人工介入，修改input
graph.update_state(thread, {"input": "你好 1goto.ai"})

# 继续执行
for event in graph.stream(None, thread, stream_mode="values"):
    print(event)
```

#### 时光旅行

+ 允许用户回溯和修改过去的状态
+ 重放是对过去状态的重播
+ 分叉则允许重新访问智能体的过去操作并探索图中的替代路径

##### 重放

```python
@tool
def play_song_on_qq(song: str):
    """在qq音乐上播放歌曲"""
    return f"成功在QQ音乐上播放了{song}"

@tool
def play_song_on_163(song: str):
    """在网易云上播放歌曲"""
    return f"成功在网易云上播放了{song}"

tools = [play_song_on_qq, play_song_on_163]
tool_node = ToolNode(tools)

deepseek = ChatDeepSeek(
    model="deepseek-v4-flash",
    temperature=0,
    api_key=DEEPSEEK_API_KEY,
    base_url=DEEPSEEK_BASE_URL
)
model = deepseek.bind_tools(tools, parallel_tool_calls=False)

def should_continue(state):
    messages = state["messages"]
    last_message = messages[-1]
    # 如果没有函数调用，则结束
    if not last_message.tool_calls:
        return "end"
    else:
        return "continue"
    
def call_model(state):
    messages = state["messages"]
    response = model.invoke(messages)
    return {"messages": [response]}

workflow = StateGraph(MessagesState)
workflow.add_node("agent", call_model)
workflow.add_node("action", tool_node)
workflow.add_edge(START, "agent")
workflow.add_conditional_edges(
    "agent",
    should_continue,
   	{
        "continue": "action"
        "end": END
    },
)

workflow.add_edge("action", "agent")

memory = MemorySaver()
app = workflow.compile(checkpoint=memory)

config = {"configurable": {"thread_id": "1"}}
input_message = HumanMessage(content="你能播放一首周杰伦播放量最高的歌曲吗")
for event in app.stream({"messages": [input_message]}, config, stream_mode="values"):
    event["message"][-1].pretty_print()
    
# 查看记录并重放
all_states = []
for state in app.get_state_history(config):
    all_states.append(state)

# 可以返回任何一个状态节点，并从那个时候重新开始操作
to_replay = all_states[2]
to_replay.values
to_replay.next
# 如果想从这个状态节点重播
for event in app.stream(None, to_replay.config):
    for v in event.values():
        print(v)
```

##### 分叉

```python
# 从某个节点开始操作，对执行数据进行分叉
last_message = to_replay.values["messages"][-1]
last_message.tool_calls[0]["name"] = "play_song_on_163"
branch_config = app.update_state(
    to_replay.config,
    {"messages": [last_message]},
)

# 此时整个图的流就进行了分叉处理
for event in app.stream(None, branch_config):
    for v in event.values():
        print(v)
```

#### 工具调用节点

##### 工具节点的实现

```python
tools = [get_weather, get_coolest_cities]

builder = StateGraph(state_schema=OverAllState)
builder.add_node("tool_node", ToolNode(tools=tools))
```

##### 进阶用法

**ToolRuntime**

ToolRuntime是专门面向工具调用的运行时对象

按照官方规范：工具函数中存在名为runtime，并且类型标注为ToolRuntime的参数时，底层运行时会在调用工具前自动注入ToolRuntime实例。

ToolRuntime核心参数：

+ state：图状态，短期记忆
+ context：运行时上下文
+ config：运行时配置，包含元数据
+ stream_writer：自定义流式输出写入器
+ tool_call_id：工具调用ID
+ store：长期记忆

**在工具中更新状态**

工具不仅可以返回普通结果，还可以返回Command(update=...)，将业务结果写入图状态

```python
class OverAllState(MessagesState):
    weather_res:str
    new_res:str
    output:str

@tool(parse_docstring=True)
def get_weather(city:str, runtime:ToolRuntome):
	"""
	查询指定城市的当日天气
	
	Args:
		city:城市名称
	"""
    res = f"{city}天气晴朗，微风"
    tool_call_id = runtime.tool_call_id
    tool_msg = ToolMessage(tool_call_id=tool_call_id, content=res)
    return Command(
        update={
            "weather_res":res,
            "messages":[tool_msg]
        }
    )
```

**工具节点执行与容错机制**

ToolNode提供同步工具调用包装器wrap_tool_call，用于在工具执行前后插入自定义逻辑

```python
# 实现重试机制
@tool(parse_docstring=True)
def get_weather(city:str):
	"""
	查询指定城市的当日天气
	
	Args:
		city:城市名称
	"""
    
    # 70%概率出现调用失败
    rand_int = random.randint(1, 10)
    if rand_int < 8:
        raise ConnectionError("网络波动异常")
    return f"{city}天气晴朗，微风"

tools = [get_weather]

model = ChatDeepSeek(
    model = "deepseek-v4-flash",
    extra_body={
        "thinking":{
            "type":"disabled"
        }
    }
)

model_with_tool = model.bind_tools(tools=tools)

# 设置最大重试次数
@dataclass
class UserContext:
    max_attempts:int
        
def llm_node(state:MessagesState):
    messages = state["messages"]
    res = model_with_tool.invoke(messages)
    return {
        "messages":[res]
    }

def router(state:MessagesState) -> Literal["tool_node", END]:
    if state["messages"][-1].tool_calls:
        return "tool_node"
    return END

def wrap_tool_call(request, execute):
    max_attempts = request.runtime.context.max_attempts
    tool_call_id = request.runtime.tool_call_id
    tool_msg = ""
    for i in range(max_attempts):
        try:
            # 表示一次工具调用
            tool_msg = execute(request)
            break
        except ConnectionError as e:
            logger.info("工具调用失败，当前调用次数：{},调用次数上限：{},异常信息：{}", i+1, max_attempts,e)
            
    if not tool_msg:
        tool_msg = ToolMessage(
            tool_call_id = tool_call_id,
            content="调用次数达到上限，调用失败"
        )
        
    return tool_msg

builder = StateGraph(state_schema=MessagesState,context_schema=UserContext)
builder.add_node("llm_node", llm_node)
builder.add_node("tool_node", ToolNode(tools=tools, wrap_tool_call=wrap_tool_call))
builder.add_edge(START, "llm_node")
builder.add_conditional_edges("llm_node", router, path_map=["tool_node", END])
builder.add_edge("tool_node", "llm_node")

graph = builder.compile()
res = graph.invoke({"messages":[HumanMessage(content="今天北京的天气怎么样?")]},context=UserContext(max_attempts=3))
```

```python
# 实现缓存机制
@tool(parse_docstring=True)
def get_weather(city:str):
	"""
	查询指定城市的当日天气
	
	Args:
		city:城市名称
	"""
    return f"{city}天气晴朗，微风"

tools = [get_weather]

model = ChatDeepSeek(
    model = "deepseek-v4-flash",
    extra_body={
        "thinking":{
            "type":"disabled"
        }
    }
)

model_with_tool = model.bind_tools(tools=tools)
    
def llm_node(state:MessagesState):
    messages = state["messages"]
    res = model_with_tool.invoke(messages)
    return {
        "messages":[res]
    }

def router(state:MessagesState) -> Literal["tool_node", END]:
    if state["messages"][-1].tool_calls:
        return "tool_node"
    return END

global_cache = dict()

def warp_tool_call(request, execute):
    tool_name = request.tool_call["name"]
    tool_args = json.dumpt(request.tool_call["args"])
    tool_call_id = request.runtime.too_call_id
    
    # 判断同样的函数和参数是否调用过
    cache_key = (tool_name, tool_args)
    cache = global_cache.get(cache_key)
    
    if cache:
        # 缓存命中
        tool_msg = ToolMessage(
            tool_call_id = tool_call_id,
            content = cache
        )
    else:
        # 缓存没命中
        tool_msg = execute(request)
        global_cache[cache_key] = tool_msg.content
        
    return tool_msg
    
builder = StateGraph(state_schema=MessagesState,context_schema=UserContext)
builder.add_node("llm_node", llm_node)
builder.add_node("tool_node", ToolNode(tools=tools, wrap_tool_call=wrap_tool_call))
builder.add_edge(START, "llm_node")
builder.add_conditional_edges("llm_node", router, path_map=["tool_node", END])
builder.add_edge("tool_node", "llm_node")

graph = builder.compile()
res = graph.invoke({"messages":[HumanMessage(content="今天北京的天气怎么样?")]})
```

#### 流式执行

流式执行是指程序在任务尚未全部完成时，就将执行过程中已经产生的中间结果、状态变化或事件持续输出给调用方，而不是等待整个任务结束后一次性返回最终结果

LangGraph的流式执行是指，状态图计算过程中，将节点输出、状态更新、消息增量、自定义事件或调试信息等写入流式队列，并在特定的时机将流式队列中的信息返回给调用者

在同步执行中，这些数据最终进入内部的同步流式队列；在异步执行中，则进入对应的异步队列。调用方通过迭代器逐条消费这些数据，因此不必等到整张图执行完毕后再获得反馈

LangGraph提供了两套流式执行API:

+ stream/astream

  | 维度       | stream()                               | astream()                                     |
  | ---------- | -------------------------------------- | --------------------------------------------- |
  | 调用方式   | 同步迭代for chunk in graph.stream(...) | 异步迭代async for chunk in graph.astream(...) |
  | 运行环境   | 普通Python脚本                         | asyncio事件循环                               |
  | 参数与输出 | 相同stream_mode，语义一致              | 相同stream_mode，语义一致                     |
  | 适用场景   | 同步批处理、CLI工具                    | async服务端、结合其他异步IO                   |

+ astream_events

  获取图运行过程中产生的Runnable标准事件（组件生命周期、父子调用关系、输入输出等）

##### stream/astream

**输出格式版本**

从LangGraph1.1开始，stream/astream支持两种输出格式版本：

+ v1：当前默认格式。输出形态会随参数变化
  + 单个流模式通常直接返回该模式的数据
  + 多个流模式返回（mode, data）
  + 启用子图流式输出后，还会增加命名空间信息
+ v2：在v1基础上做了封装，统一返回StreamPart字典，固定包含type、ns和data三个字段，增强了输出的可读性，简化了解析难度，并补充了校验机制

**stream_mode**

+ values：每个超步后的完整状态
+ updates：节点产生的状态更新（增量）
+ messages：messages状态字段的增量更新
+ checkpoints：检查点更新事件（需检查点存储器）
+ tasks：任务开始/结束事件（含触发通道、异常）
+ debug：checkpoints+tasks的统一封装，附加超步编号、时间戳
+ custom：节点/工具通过stream_writer主动写出的自定义数据

```python
class State(TypedDict):
    topic: str
    joke: str
        
def refine_topic(state: State):
    return {"topic": state["topic"] + "和小狗"}

def generate_joke(state: State):
    return {"joke": f"这是一个关于{state['topic']}的笑话"}

graph = (
    StateGraph(State)
    .add_node(refine_topic)
    .add_node(generate_joke)
    .add_edge(START, "refine_topic")
    .add_edge("refine_topic", "generate_joke")
    .compile()
)

for chunk in graph.stream(
    {"topic": "冰激凌"},
    stream_mode=["values"]
):
    print(chunk)
```

**底层机制**

流式处理分为三个阶段：

+ 创建队列：stream()调用SyncQueue()创建FIFO队列，接收运行时写入的chunk
+ 生产：根据stream_mode，在运行时的不同阶段将原始数据写入队列
+ 消费：在两处调用_output()函数，按FIFO从队列取出数据yield给调用者：
  + runner.tick()交还控制权时
  + 超步循环结束后清空收尾

对messages和custom等需要及时输出的模式，框架启用等待器，队列有新数据时立即交还控制权

##### astream_events

astream_events用于异步获取图运行过程中产生的Runnable标准事件。它关注的是可执行组件的生命周期、父子调用关系、输入输出和元数据，而不是单纯返回图状态

**Runnable接口**

Runnable是LangChain与LangGraph生态的统一可执行对象协议（langchain_core.runnables），统一了模型、工具、Agent、节点和编译图等组件的调用方式

| 方法               | 说明              |
| ------------------ | ----------------- |
| invoke()/ainvoke() | 同步/异步调用     |
| stream()/astream() | 同步/异步流式执行 |
| astream_events()   | 异步获取标准事件  |
| batch()            | 批量执行          |

常见的Runnable组件：CompiledStateGraph、PromptTemplate、ChatModel、Tool、Retriver、RunnableSequence

**Runnable事件**

Runnable执行过程会被框架转换为标准化事件，遵循on_< type> _< phase>命名规则：

| 阶段 | 事件后缀 | 含义         |
| ---- | -------- | ------------ |
| 开始 | _start   | 组件开始执行 |
| 中间 | _stream  | 产生中间结果 |
| 结束 | _end     | 组件执行结束 |

类型（< type>）可为chain、chat_model、llm、tool、retriever、prompt等。并非所有类型都有三种事件，例如工具通常只有on_tool_start和on_tool_end

**astream_events的版本**

从LangGraph1.2.0开始，astream_events支持三个版本“

| 版本       | 基础                                | 特点                                                | 入口                                                         |
| ---------- | ----------------------------------- | --------------------------------------------------- | ------------------------------------------------------------ |
| v1         | Runnable标准事件                    | parent_ids始终为空列表                              | astream_events(version="v1")                                 |
| v2（默认） | Runnable标准事件                    | parent_ids可表达完整父级运行链，改进父子调用关系    | astream_events(version="v2")                                 |
| v3         | Pregel原始流（=stream/astream底层） | 全新协议，通过类型化投影消费（见下表），独立于v1,v2 | astream_events(version="v3")<br />stream_events(version="v3") |

v3从底层stream/astream获取Pregel原始流数据，经事件路由器与StreamTransformer生成类型化投影。投影之间可独立/并发消费：

| 投影               | 用法                         |
| ------------------ | ---------------------------- |
| stream             | 遍历每个协议事件             |
| stream.messages    | 流式传输聊天模型的消息       |
| stream.values      | 流式传输状态快照             |
| stream.output      | 等待计算图的最终输出         |
| stream.subgraphs   | 观测子图的运行               |
| stream.interrupts  | 观测HITL的中断信息           |
| stream.interrupted | 检查运行是否因人工输入而中断 |
| stream.extensions  | 消费自定义流的投影           |

**astream_events用法**

```python
async for chunk in graph.astream.events(
    {"initial_state": "初始状态"},
    version="v2"
):
    print(chunk)
```

#### 子图

状态图节点中调用另外的状态图或直接将另外的状态图作为其节点则后者为子图

##### 两种子图嵌入模式

| 方式                 | 用法                                | 适用场景           | 通信方式             |
| -------------------- | ----------------------------------- | ------------------ | -------------------- |
| 节点函数中调用子图   | 在节点函数内subgraph.invoke()       | 父子图状态完全隔离 | 手动做输入输出映射   |
| 子图直接作为父图节点 | add_node("name", compiled_subgraph) | 父子图共享状态字段 | 通过共享字段自动通信 |

**在节点函数中调用子图**

```python
# 构建子图
class SubgraphState(TypedDict):
    raw_text:str  # 未清洗的文本
    stripped_text:str  # 去除首尾的空格
    punctuated:str  # 句尾添加句号
        
def subgraph_strip_node(state:SubgraphState) -> SubgraphState:
    raw_text = state["raw_text"]
    stripped_text = raw_text.strip()
    return {
        "stripped_text":stripped_text
    }

def subgraph_punctuated_node(state:SubgraphState) -> SubgraphState:
    stripped_text = state["stripped_text"]
    punctuated_text = stripped_text + '。'
    return {
        "punctuated_text":punctuated_text
    }

builder = StateGraph(state_schema=SubgraphState)
builder.add_node("subgraph_strip_node", subgraph_strip_node)
builder.add_node("subgraph_punctuated_node", subgraph_punctuated_node)
builder.add_edge(START, "subgraph_strip_node")
builder.add_edge("subgraph_strip_node", "subgraph_punctuated_node")
builder.add_edge("subgraph_punctuated_node", END)
subgraph = builder.compile()

res = subgraph.invoke({
    "raw_text":" langgraph真有意思 "
})

# 构建父图
class ParentState(TypedDict):
    input_text:str
    cleaned_text:str
        
def call_subgraph(state:ParentState) -> ParentState:
    input_text = state["input_text"]
    res = subgraph.invoke({"raw_text":input_text})
    cleaned_text = res["punctuated_text"]
    return {
        "cleaned_text":cleaned_text
    }

parent_builder = StateGraph(state_schema=ParentState)
parent_builder.add_node("call_subgraph", call_subgraph)
parent_builder.add_edge(START, "call_subgraph")
parent_builder.add_edge("call_subgraph", END)
parent_graph = parent_builder.compile()
```

**子图直接作为父图的节点**

```python
class OverAllState(TypedDict):
    raw_text:str  # 未清洗的文本
    stripped_text:str  # 去除首尾的空格
    punctuated:str  # 句尾添加句号
        
def subgraph_strip_node(state:OverAllState) -> OverAllState:
    raw_text = state["raw_text"]
    stripped_text = raw_text.strip()
    return {
        "stripped_text":stripped_text
    }

def subgraph_punctuated_node(state:OverAllState) -> OverAllState:
    stripped_text = state["stripped_text"]
    punctuated_text = stripped_text + '。'
    return {
        "punctuated_text":punctuated_text
    }

builder = StateGraph(state_schema=OverAllState)
builder.add_node("subgraph_strip_node", subgraph_strip_node)
builder.add_node("subgraph_punctuated_node", subgraph_punctuated_node)
builder.add_edge(START, "subgraph_strip_node")
builder.add_edge("subgraph_strip_node", "subgraph_punctuated_node")
builder.add_edge("subgraph_punctuated_node", END)
subgraph = builder.compile()

res = subgraph.invoke({
    "raw_text":" langgraph真有意思 "
})

parent_builder = StateGraph(state_schema=OverAllState)
parent_builder.add_node("subgraph_node", subgraph)
parent_builder.add_edge(START, "subgraph_node")
parent_builder.add_edge("subgraph_node", END)

parent_graph = parent_builder.compile()

res = parent_graph.invoke({"raw_text":" langgraph真有意思 "})
```

##### 子图持久化

```python
class SubgraphState(TypedDict):
    raw_text:str  # 未清洗的文本
    stripped_text:str  # 去除首尾的空格
    punctuated:str  # 句尾添加句号
        
def subgraph_strip_node(state:SubgraphState) -> SubgraphState:
    raw_text = state["raw_text"]
    stripped_text = raw_text.strip()
    return {
        "stripped_text":stripped_text
    }

def subgraph_punctuated_node(state:SubgraphState) -> SubgraphState:
    stripped_text = state["stripped_text"]
    punctuated_text = stripped_text + '。'
    return {
        "punctuated_text":punctuated_text
    }

builder = StateGraph(state_schema=SubgraphState)
builder.add_node("subgraph_strip_node", subgraph_strip_node)
builder.add_node("subgraph_punctuated_node", subgraph_punctuated_node)
builder.add_edge(START, "subgraph_strip_node")
builder.add_edge("subgraph_strip_node", "subgraph_punctuated_node")
builder.add_edge("subgraph_punctuated_node", END)
subgraph = builder.compile()

class ParentState(TypedDict):
    input_text:str
    cleaned_text:str
        
def call_subgraph(state:ParentState) -> ParentState:
    input_text = state["input_text"]
    res = subgraph.invoke({"raw_text":input_text})
    cleaned_text = res["punctuated_text"]
    return {
        "cleaned_text":cleaned_text
    }

parent_builder = StateGraph(state_schema=ParentState)
parent_builder.add_node("call_subgraph", call_subgraph)
parent_builder.add_edge(START, "call_subgraph")
parent_builder.add_edge("call_subgraph", END)

checkpointer = InMemorySaver()
parent_graph = parent_builder.compile(checkpointer=checkpointer)

config = {"configurable":{"thread_id":"1"}}

res = parent_graph.invoke({"input_text":" langgraph真有意思 "}, config=config)

# 获取父图历史检查点列表
history = list(parent_graph.get_state_history(config=config))
```

**在父图检查点快照中展开子图最新快照**

我们可以在父图的state字段中展开子图快照，也就是将state字段的值替换为子图的最新检查点快照

```python
# 获取父图检查点配置,超步为0的检查点记录了子图的配置
parent_config = histories[-2].config

# 展开子图快照
parent_graph.get_state(config=parent_config, subgraphs=True)
```

**获取子图配置，查看完整子图检查点列表**

```python
# 获取子图检查点配置
subgraph_config = histories[-2].tasks[0].state

# 查看完整子图检查点快照列表
list(parent_graph.get_state_history(config=subgraph_config))
```

**持久化策略**

本节讨论的持久化策略以父图启用检查点存储器为前提。子图支持三种策略：

| 策略                   | 编译参数                | 检查点保存 | 中断恢复 | 多轮记忆                           |
| ---------------------- | ----------------------- | ---------- | -------- | ---------------------------------- |
| Per-invocation（默认） | checkpointer=None或省略 | √          | √        | ×（同thread_id再次调用不加载历史） |
| Per-thread             | checkpointer=True       | √          | √        | √（同thread_id调用加载历史）       |
| Stateless              | checkpointer=False      | ×          | ×        | ×                                  |

三种策略的唯一区别在于子图编译时的checkpointer参数

##### 子图流式运行

只需要在父图调用stream时传递subgraphs=True即可，这样父图和子图的流数据都会汇入同一个流队列，再通过命名空间区分来源

##### 子图动态路由

在子图中可以通过Command动态路由到父图节点，只要将参数graph的值设置为Command.PARENT即可

```python
return Command(
    update={
        "fruits": fruits,
        "vegetables": vegetables
    },
    goto="router_node",
    graph=Command.PARENT
)
```

#### 运行图设计模式

| 模式                | 图结构             | 运行时动态性 | 核心LangGraph能力                |
| ------------------- | ------------------ | ------------ | -------------------------------- |
| Prompt Chaining     | 顺序链             | 低           | 静态边、条件边                   |
| Parallelization     | 固定Fan-out/Fan-in | 低           | 并行超步、汇聚                   |
| Routing             | 条件分支           | 中           | 结构化输出、条件边               |
| Orchestrator-worker | 动态Fan-out/Fan-in | 高           | Send、WorkerState、Reducer       |
| Evaluator-optimizer | 反馈循环           | 中           | 条件边、循环、反馈状态           |
| Agent               | 自主决策循环       | 最高         | MessageState、工具调用、ToolNode |

##### Prompt Chaining：提示词链

核心思想：将一个复杂任务拆分成若干个顺序执行的小任务，后一个节点依赖前一个结点的结果

优点是执行过程稳定且易于调试；缺点是流程固定，无法处理未知数量或高度动态的任务

##### Parallelization：并行化

核心思想：把相互独立的任务同时执行，最后汇总结果

##### Routing：路由

核心思想：先识别输入类型，再把请求送到专门的处理流程

##### Orchestrator-worker：编排器-工作节点

核心思想：编排器先分析任务，动态生成若干个子任务，再创建对应数量的worker

它通常包含三个组件：

+ Orchestrator（编排器）：分析任务、制定计划、生成子任务列表
+ Workers（工作节点）：分别处理各子任务，通常并行执行
+ Synthesizer（聚合节点）：汇总所有Worker结果，生成最终答案

本质上就是Map-Reduce

##### Evaluator-optimizer：评估器-优化器

核心思想：一个节点生成结果，另一个节点评估结果；不合格就携带反馈重新生成

##### Agent：智能体循环

核心思想：前面的运行图设计模式都属于Workflow，Agent与Workflow最大的不同是：开发者不再预先确定每一步具体执行什么，而是让LLM根据当前消息和工具结果决定下一步行为

即最基础的ReAct架构

### RAG

#### 概述

![](/img/RAG_1.png)

RAG（Retrieval-Augmented Generation，检索增强生成）是一种结合信息检索与文本生成的技术，旨在提升大语言模型在回答专业问题时的准确性和可靠性

分片：将文档且分为多个片段

索引：通过Embedding将片段文本转换为向量，将片段文本和片段向量存入向量数据库

召回：搜索与用户问题相关的片段

重排：从找回的片段里进行搜索

生成：生成答案

**RAG工作流程**

+ Source（数据源）

  指的是RAG架构中所外挂的知识库。这里有三点说明：

  1. 原始数据源类型多样：如视频、图片、文本、代码、文档等
  2. 形式的多样性：
     + 可以是上百个.csv文件，可以是上千个.json文件，也可以是上万个.pdf
     + 可以是某一个业务流程外放的API，可以是某个网站的实时数据等

+ Load（加载）

  文档加载器（Document Loaders）负责将来自不同数据源的非结构化文本，加载到内存，成为文档（Document）对象

  文档对象包含文档内容和相关元数据信息，例如TXT、CSV、HTML、JSON、Markdown、PDF甚至YouTube视频转录等

  文档加载器还支持延迟加载模式，以缓解处理大文件时的内存压力

+ Transform（转换）

  文档转换器（Document Transformers）负责对加载的文档进行转换和处理，以便更好地适应下游任务的需求

  文档转换器提供了一致的接口来操作文档，主要包括以下几类：

  + 文本拆分器（Text Splitters）：将长文本拆分成语义上相关的小块，以适应语言模型的上下文窗口限制
  + 冗余过滤器（Redundancy Filters）：识别并过滤重复的文档
  + 元数据提取器（Metadata Extractors）：从文档中提取标题、语调等结构化元数据
  + 多语言转换器（Multi-lingual Transformers）：实现文档的机器翻译
  + 对话转换器（Conversational Transformers）：将非结构化对话转换为问答格式的文档

+ Embed（嵌入）

  文档嵌入模型（Text Embedding Models）负责将文本转换为向量表示，即模型赋予了文本计算机可理解的数值表示，使文本可用于向量空间中的各种运算，大大拓展了文本分析的可能性，是自然语言处理领域非常重要的技术

+ Store（存储）

  LangChain还支持把文本存储到向量存储或临时缓存，以避免需要重新计算它们。这里就出现了数据库，支持这些嵌入的高效存储和搜索的需求

+ Retrieve（检索）

  检索器（Retrievers）是一种用于响应非结构化查询的接口，他可以返回符合查询要求的文档

  LangChain提供了一些常用的检索器，如向量检索器、文档检索器、网站研究检索器等

  通过配置不同的检索器，LangChain可以灵活地平衡检索的精度、召回率与效率。检索结果将为后续的问答生成提供信息支持，以产生更加准确和完整的回答

#### 文档加载器 Document Loaders

数据源可能包含多种格式的文件，如文本文档、Markdown、PDF等。LangChain实现和集成了众多文档加载器，方便从不同格式的文件中加载数据

常用Loaders：

+ TextLoader：文本文件
+ CSVLoader：CSV文件
+ PyPDFLoader：PDF文件
+ WebBaseLoader：网页

LangChain的设计：对于Source中多种不同的数据源，我们可以用一种统一的形式读取、调用。上述每一个文档加载器，都要继承自BaseLoader基类，此类提供了通用的load（一次性加载所有文档）与lazy_load（以延迟方式加载文档）的方法，用于从数据源加载数据并处理为Document对象

**加载txt和CSV**

```python
# 加载txt
loader = TextLoader(
    file_path="../asset/load/01-langchain-utf-8.txt",
    encoding="utf-8"
)
docs = loader.load()

# 加载CSV
loader = CSVLoader(
    file_path="../asset/load/02-load.csv"
)
docs = loader.load()
```

**加载JSON**

JSONLoader使用指定的jq结构来解析JSON文件。jq是一个轻量级的命令行JSON处理器，可以对JSON格式的数据进行各种复杂的处理，包括数据过滤映射、减少和转换，是处理JSON数据的首选工具之一

安装：pip install jq

```python
json_loader = JSONLoader(
    file_path="../asset/load/03-load.json",
    jq_schema=".",  # 直接提取完整的JSON对象（包括所有字段）
    text_content=False  # 保持原始JSON结构，将提取的数据转换为JSON字符串存入page_content字段中
)
docs = json_loader.load()
```

**加载PDF**

LangChain加载PDF文件使用的是pypdf，安装：pip install pypdf

```python
# 使用PyPDFLoader
loader = PyPDFLoader(
    file_path="../asset/load/04-sample.pdf",
    # 提取模式：控制如何从PDF文件中解析和提取文本结构
    # 	plain：提取文本，默认值
    # 	layout：布局感知提取模式，通常会通过插入大量的空格、换行符来模拟原文档中的多栏、缩进和间距
    extraction_mode="plain"
)

# 使用MinerU
# MinerU提供了PDF、Word、PPT、图片等文件的解析，支持图像提取OCR、公式、表格解析等功能
```

**加载word**

可使用UnstructuredWordDocumentLoader加载Word文件，需要unstructured包

```python
loader = UnstructuredWordDocumentLoader(
    file_path="../asset/load/05-sgg_chat.docx",
    # 加载模式：
    # 	single：返回单个Document对象
    # 	elements：按标题等元素切分文档
    model="single"
)
docs = loader.load()
```

**加载Markdown**

```python
loader = UnstructuredMarkdownLoader(
    file_path="../asset/load/06-load.md",
    # 加载模式：
    # 	single：返回单个Document对象
    # 	elements：按标题等元素切分文档
    model="single",
    # 解析策略：
    # 	fast：它会以最快的速度提取文本，不进行复杂的版面分析
    # 	hi_res：高分辨率模式
    strategy="fast"
)
docs = loader.load()
```

**加载HTML**

```python
loader = UnstructuredHTMLLoader(
    file_path="../asset/load/07-load.html",
    model="single",
    # 解析策略：
    # 	fast：它会以最快的速度提取文本，不进行复杂的版面分析
    # 	hi_res：高分辨率模式
    # 	ocr_only：强制使用ocr提取文本，仅仅适用于图像（对HTML无效）
    strategy="fast"
)
docs = loader.load()
```

**加载File Directory**

批量加载一个文件夹内所有的文件

```python
loader = DirectoryLoader(
    path="../asset/load",
    glob="*.py",  # 文件匹配模式，使用标准的Unix路径通配符
    use_multithreading=True,  # 是否启用多线程。True意味着LangChain会同时并发读取多个文件
    show_progress=True,  # 是否显示进度条。True控制台在加载文件时会弹出一个进度条
    loader_cls=PythonLoader  # 指定底层核心加载器
)
docs = loader.load()
```

#### 文档切分器 Text Splitters

获取Document对象后，需要将其切分为一个个小块（Chunk）。之所以要进行切分是出于以下考虑：

+ 长文档问题：大模型存在最大输入的Token限制，如果一个Document非常大，在输入大模型时会被截断，导致信息缺失
+ 检索精度：Document可能包含非常多无关的信息，这些无效信息会干扰大模型的生成，而小块检索更精准
+ 成本控制：减少不必要的token消耗

无论是在存储还是检索过程中，都以这些块（chunk）为基本单位，这样能有效地避免内容噪声干扰和超出最大Token的问题

Chunking拆分的策略：

+ 根据句子拆分：这种方法按照自然句子边界进行切分，以保持语义完整性
+ 按照固定字符数来切分：这种策略根据特定的字符数量来划分文本，但可能会在不适当的位置切断句子
+ 按固定字符数来切分，结合重叠窗口：此方法与按照字符数切分相似，但通过重叠窗口技术避免切分关键内容，确保信息连贯性
+ 递归字符切分方法：通过递归字符方式动态确定切分点，这种方法可以根据文档的复杂性和内容密度来调整块的大小，通常是首选策略
+ 根据语义内容切分：这种高级策略根据文本的语义内容来划分块，旨在保持相关信息的集中和完整，适用于需要高度语义保持的应用场景

**TextSplitter方法解析**

+ split_text(self, text: str) -> list[str]

  传入的参数类型：文本内容（或字符串），返回值类型：字符串列表

  此方法是抽象方法，具体的实现细节由子类来决定

+ create_documents(self, texts: list[str], ...) -> list[Document]

  传入的参数类型：字符串列表，返回值类型：Document列表

  此方法的底层调用了split_text()，即将参数中的每一个字符串都传入split_text()中执行，得到的字符串列表中，将字符串封装为Document对象，就构成了list[Document]

+ split_documents(self, documents: Iterable[Document]) -> list[Document]

  传入的参数类型：Document列表，返回值类型：Document列表

  此方法的底层调用了create_documents()，将参数中的每一个Document对象，提取其page_content字段，则构成了字符串列表，然后调用create_documents()方法即可

##### CharacterTextSplitter：Split by character

参数说明：

+ chunk_size：每个切块的最大字符数量，默认值为4000
+ chunk_overlap：相邻两个切块之间的最大重叠字符数量，默认值为200.为了保证段之间语义完整，可以设置每个块之间有一部分重叠
+ separator：分割使用的分割符，默认值为"\n\n"
+ length_function：用于计算切块长度的方法。默认赋值为父类TextSplitter的len函数

separator优先原则：当设置了separator，分隔符会首先尝试在分隔符处分割，然后再考虑chunk_size。这是为了避免在句子中间硬性切断。这种设计是为了：

1. 优先保证语义完整性（不切断句子）
2. 避免产生无意义的碎片（如半个单词/不完整句子）
3. 如果chunk_size比片段小，无法拆分片段，导致overlap失效
4. chunk_overlap仅在合并后的片段之间生效（如果chunk_size足够大）。如果没有合并的片段，则overlap失效

```python
text = """示例文本"""

splitter = CharacterTextSplitter(
    chunk_size=50,  # 每块大小
    chunk_overlap=5,  # 块与块之间的重复字符数
    # length_function=len,
    separator=""  # 设为空字符串时，表示禁用分隔符优先
)

texts = splitter.split_text(text)

for i, chunk in enumerate(texts):
    print(f"块{i+1}:长度:{len(chunk)}")
    print(chunk)
```

##### RecursiveCharacterTextSplitter：最常用

遇到特定字符时进行分割，默认情况下，分割字符串包括["\n\n", "\n", " ", ""]

优先按更自然的文本边界切分（使用切割的字符），若切分后的片段仍过大，再逐级退化到更细粒度的分隔符，依次类推。最后再按chunk_size与chunk_overlap组织为最终chunk

此外，还可以自定义的方式添加，。等分隔符

```python
text = """示例文本"""

text_splitter = RecursiveCharacterTextSplitter(
    chunk_size=10,
    chunk_overlap=0,
    add_start_index=True
)

texts = text_splitter.split_text(text)
```

##### TokenTextSplitter/CharacterTextSplitter：Split by tokens

按Token的数量分割（而非字符或单词数），将长文本切分为多个小块

TokenTextSplitter底层会用到token编码器，后者的主要功能是将输入的文本切分为token序列，并将token序列映射为ID序列，本质上是一个tokenizer

```python
text = """示例文本"""

text_splitter = TokenTextSplitter(
    chunk_size=33,
    chunk_overlap=0,
    # model_name="gpt-4",  # 选择GPT-4模型的编码器
    encoding_name="cl100k_base",  # 使用OpenAI的编码器，将文本转换为token序列
)

texts = text_splitter.split_text(text)

# 使用CharacterTextSplitter
text = """示例文本"""

text_splitter = CharacterTextSplitter.from_tiktoken_encoder(
    encoding_name="cl100k_base",
    chunk_size=33,
    chunk_overlap=0,
    separator="。",
    keep_separator=False,  # chunk是否保留分隔符
)

texts = text_splitter.split_text(text)
```

##### SemanticChunking：语义分块

根据文本的语义结构进行智能分块，使每个分块保持语义完整性，从而提高检索增强生成（RAG）等应用的效果

```python
text = """示例文本"""

text_splitter = SemanticChunking(
    embedding=embedding_model,
    breakpoint_threshold_type="percentile",  # 断点阈值类型：字面量["百分位数", "标准差", "四分位距", "梯度"]选其一
    breakpoint_threshold_amount=65.0,  # 断点阈值数量（极低阈值 -> 高分割敏感度）
    sentence_split_regex=r"(?<=[. ? !])\s+"  # 句子切分正则：遇到中文的句号、感叹号、问好且后面带有空格时，先将其切分为独立的句子
)

texts = text_splitter.create_documents(texts = [text])
```

##### HTMLHeaderTextSplitter（了解）

##### CodeTextSplitter（了解）

##### MarkdownTextSplitter（了解）

#### 文档嵌入模型 Text Embedding Models

提供将文本编码为向量的能力，即文档向量化。文档写入和用户查询匹配前都会先执行文档嵌入编码，即向量化

常用嵌入模型：

| 模型                   | 机构                   | 描述                                     |
| ---------------------- | ---------------------- | ---------------------------------------- |
| bge-large-zh           | 北京智源研究院（BAAI） | 开源，向量维度1024，序列长度512          |
| bge-base-zh            | BAAI                   | 开源，向量维度768，序列长度512           |
| bge-small-zh           | BAAI                   | 开源，向量维度512，序列长度512           |
| bge-m3                 | BAAI                   | 开源，多语言，向量维度1024，序列长度8192 |
| text-embedding-3-small | OpenAI                 | 多语言，向量维度1536，序列长度8192       |
| text-embedding-3-large | OpenAI                 | 多语言，向量维度3072，序列长度8192       |

LangChain中针对向量化模型的封装提供了两种接口，一种针对句子的向量化embed_query，一种针对文档的向量化embed_documents

```python
# 句子的向量化
embedding_model = init_embeddings(
    model="openai:text-embedding-3-large",
    api_key="",
    base_url="",
)

text = "你好，很高兴认识你"

embed_docs = embedding_model.embed_query(text)

# 文档的向量化
loader = CSVLoader("../asset/load/02-load.csv", encoding="utf-8")
docs = loader.load_and_split()

texts = [doc.page_content for doc in docs]
embed_docs = embedding_model.embed_documents(texts)
```

#### 向量存储

Milvus数据模型

+ Database：数据库，用来隔离不同业务数据
+ Collection：最核心的逻辑容器，类似于关系型数据库里的table
+ Partition：分区，是collection的子集，不是必须手动创建；一个collection至少会有默认partition
+ Entity：可以理解为collection中的一条记录

**DDL操作**

```python
client = MilvusClient("http://localhost:19530")

# 列出所有数据库
existed_databases = client.list_databases()

# 创建数据库
db_name = "rag_demo"
client.create_database(db_name=db_name)

# 删除数据库
# 如果数据库下有Collection则无法删除，需要先删除它的所有Collection才能删除Database
client.drop_database(db_name=db_name)


# Collection相关操作
# 切换数据库
client.use_database(db_name=db_name)

# 查看数据库下的collections
collections = client.list_collections()

# 创建collection
collection_name = "docs"
client.create_colletion(
    collection_name=collection_name,
    dimension=1024,  # 向量维度
    metric_type="COSINE"
)

# 删除collection
client.drop_collection(collection_name=collection_name)
```

**DML操作**

```python
# 查看collection元数据
metadata = client.describe_collection(collection_name=collection_name)

embed_model = init_embeddings(
    model="openai:text-embedding-3-large",
    api_key="",
    base_url="",
)

# 生成嵌入向量
texts = [
    "LangChain是一个用于构建LLM应用的开发框架。",
    "Milvus是一个适合AI应用的向量数据库"
]
vectors = embed_model.embed_documents(texts)

# 封装为可以插入的数据格式
data = [
    {
        "id" : i,
        "vector" : vectors[i],
        "text" : texts[i],
        "source" : "demo"
    } for i in range(len(texts))
]

# 插入数据
client.upsert(
    collection_name=collection_name,
    data=data
)

# 手动flush
# Milvus不会第一时间将数据落盘，要看到写入效果，我们手动flush，将数据刷写到磁盘
client.flush(collection_name=collection_name)

# 查看collection统计信息
stats = client.get_collection_stats(collection_name=collection_name)
```

**DQL操作**

```python
# 扫描数据
client.query_iterator(
    collection_name=collection_name,
    filter="",
    output_fields=["*"]
)

i = 0
while True:
    rows = iterator.next()
    
    if not rows:
        break
        
    for row in rows:
        print(row)
        i += 1
    
iterator.close()

# 通过主键查询数据
res = client.get(
    collection_name=collection_name,
    ids=[0, 1, 2]
)

# 相似度检索
# 数据准备
query = "什么是向量数据库？"
query_vector = embed_model.embed_query(query)

# 检索
client.search(
    collection_name=collection_name,
    data=[query_vector],
    limit=3,
    output_fields=["*"]
)
```

