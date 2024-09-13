### 概述

```mysql
mysql --version

#删除mysql服务
sc delete MySQL服务名
#启动MySQL服务命令
net start MySQL服务名
#停止MySQL服务命令
net stop MySQL服务名

mysql -uroot -p123456 -hlocalhost -P3306

select version();
show databases;
create database dbtest1;
use dbtest1;
show create table dbtest1;
#查看字符集
show variables like 'character_%';
#查看比较规则
show variables like 'collation_%';
#修改数据库的字符编码为utf8
alter database dbtest1 charset utf8;
#查看mysql支持的字符集和比较规则
show charset;
或
show character set;
#查看当前的sql_mode
select @@session.sql_mode
select @@global.sql_mode
或者
show variables like 'sql_mode';
```

**root用户密码忘记，重置的操作**

1. 通过任务管理器或者服务管理，关掉mysqld(服务进程)

2. 通过命令行+特殊参数开启mysqld

   mysqld --defaults-file='D:\ProgramFiles\mysql\MySQLServer5.7Data\my.ini'  --skip-grant-tables

3. 此时，mysqld服务进程已经打开，并且不需要权限检查

4. mysql -uroot无密码登录服务器，另起一个客户端进行

5. 修改权限表

   1. use mysql
   2. update user set authentication_string=password('新密码') where user='root' and Host='localhost';
   3. flush privileges;

6. 通过任务管理器关掉mysqld服务进程

7. 再次通过服务管理，打开mysql服务

8. 即可用修改后的新密码登录

**SQL分类**

+ DDL（Data Definition Language，数据定义语言）：create、drop、alter、rename、truncate
+ DML（Data Manipulation Language，数据操作语言）：inert、delete、update、select
+ DCL（Data Control Language，数据控制语言）：grant、revoke、commit、rollback、savepoint

**MySQL在windows环境下是大小写不敏感的**

**MySQL在Linux环境下是大小写敏感的**

+ 数据库名、表名、表的别名、变量名是严格区分大小写的
+ 关键字、函数名、列名（或字段名）、列的别名（字段的别名）是忽略大小写的

推荐采用统一的书写规范：

+ 数据库名、表名、表的别名、字段名、字段别名等都小写
+ SQL关键字、函数名、绑定变量等都大写

**注释**

单行注释：#注释文字（MySQL特有的方式）

单行注释：-- 注释文字

多行注释：/* 注释文字 */

### MySQL8.0新特性

LIMIT 3 OFFSET 2 等价于 LIMIT2,3

#### 计算列

某一列的值是通过别的列计算得来的，例如，a列值为1，b列值为2，c列不需要手动插入，定义a+b的结果为c的值，那么c就是计算列

CREATE TABLE 和 ALTER TABLE中都支持增加计算列

```mysql
CREATE TABLE test1(
	a INT,
    b INT,
    c INT GENERATED ALWAYS AS (a + b) VIRTUAL
)
INSERT INTO test1(a,b)
VALUES (10,20)
```

#### 自增变量的持久化

在MySQL8.0之前，自增主键auto_increment的值如果大于max(primary key) + 1，在MySQL重启后，会重置auto_increment = max(primary key) + 1，这种现象在某些情况下会导致业务主键冲突或者其他难以发现的问题

MySQL5.7系统中，对于自增主键的分配规则，是由InnoDB数据字典内部一个计数器来决定的，而计数器只在内存中维护，并不会持久化到磁盘中

MySQL8.0将自增组件的计数器持久化到重做日志中，每次计数器发生变化，都会将其写入重做日志中

#### 全局变量的持久化

SET PERSIST 变量名=变量值

MySQL会将该命令的配置保存到数据目录下的mysqld-auto.cnf文件中，下次启动时会读取该文件，用其中的配置来覆盖默认的配置文件

#### 窗口函数

窗口函数的作用类似于在查询中对数据进行分组，不同的是，分组操作会把分组的结果聚合成一条记录，而窗口函数是将结果置于每一条数据记录中

窗口函数可分为静态窗口函数和动态窗口函数

+ 静态窗口函数的窗口大小是固定的，不会因为记录的不同而不同
+ 动态窗口函数的窗口大小会随着记录的不同而变化

| 函数分类 | 函数              | 函数说明                                  |
| -------- | ----------------- | ----------------------------------------- |
| 序号函数 | ROW_NUMBER()      | 顺序排序                                  |
|          | RANK()            | 并列排序，会跳过重复的序号，比如1，1，3   |
|          | DENSE_RANK()      | 并列排序，不会跳过重复的序号，比如1，2，3 |
| 分布函数 | PERCENT_RANK()    | 等级值百分比（rank-1/rows-1）             |
|          | CUME_DIST()       | 累积分布值                                |
| 前后函数 | LAG(expr,n)       | 返回当前行的前n行的expr的值               |
|          | LEAD(expr,n)      | 返回当前行的后n行的expr的值               |
| 首尾函数 | FIRST_VALUE(expr) | 返回第一个expr的值                        |
|          | LAST_VALUE(expr)  | 返回最后一个expr的值                      |
| 其他函数 | NTH_VALUE(expr,n) | 返回第n个expr的值                         |
|          | NTILE(n)          | 将分区中的有序数据分为n个桶，记录桶编号   |

```mysql
#窗口函数的语法结构
函数 OVER ([PARTITION BY 字段名 ORDER BY 字段名 ASC|DESC])
或
函数 OVER 窗口名 ... WINDOW 窗口名 AS ([PARTITION BY 字段名 ORDER BY 字段名 ASC|DESC])
```

+ OVER关键字指定函数窗口的范围
  + 如果省略后面括号中的内容，则窗口函数会包含满足WHERE条件的所有记录，窗口函数会基于所有满足WHERE条件的记录进行计算
  + 如果OVER关键字后面的括号不为空，则可以使用如下语法设置窗口
+ 窗口名：为窗口设置一个别名，用来标识窗口
+ PARTITION BY子句：指定窗口函数按照哪些字段进行分组。分组后，窗口函数可以在每个分组中分别执行
+ ORDER BY子句：指定窗口函数按照哪些字段进行排序。执行排序操作使窗口函数按照排序后的数据记录的顺序进行编号
+ FRAME子句：为分区中的某个子集定义规则，可以用来作为滑动窗口使用

#### 公用表表达式

简称CTE(Common Table Expressions)。CTE是一个命名的临时结果集，作用范围是当前语句。CTE可以理解成一个可以复用的子查询，当然跟子查询还是有点区别的，CTE可以引用其他CTE，但子查询不能引用其他子查询，所以可以考虑代替子查询

根据语法结构和执行方式的不同，可以分为普通公用表表达式和递归公用表表达式

**普通公用表表达式**

```mysql
WITH CTE名称
AS (子查询)
SELECT|DELETE|UPDATE 语句;
```

**递归公用表表达式**

```mysql
WITH RECURSIVE
CTE名称 AS (子查询)
SELECT|DELETE|UPDATE 语句;
```

递归公用表表达式由2部分组成，分别是种子查询和递归查询，中间通过关键字UNION[ALL]进行连接。这里的种子查询，意思就是获得递归的初始值。这个查询只会执行一次，以创建初始数据集，之后递归查询会一直执行，直到没有任何新的查询数据产生，递归返回

### SELECT

+ DUAL：伪表，SELECT 1+1 FROM DUAL 

+ 列的别名可以使用一对""引起来，不要使用''

+ 所有运算符或列值遇到null值，运算的结果都为null

+ 着重号：``，字段和保留字、数据库系统或常用方法冲突时使用

+ 使用DESCRIBE或DESC命令，表示表结构：DESCRIBE dbtest1;

+ 在SQL中+没有连接的作用，就表示加法运算，此时，会将字符串转换为数值（隐式转换）

  + SELECT 100 + '1' #101
  + SELECT 100 + 'a' #100
  + SELECT 100 + NULL #NULL

+ 除法运算/，分母为0结果为NULL：SELECT 100 / 0 #NULL

+ 取模运算%，结果符号与被除数符号相同

+ =的使用

  + 字符串存在隐式转换，如果转换数值不成功，则看作0：SELECT 1='a',0='a';#false,true
  + 只要有NULL参与判断，结果就为NULL：SELECT 1=NULL,NULL=NULL;#NULL,NULL
  + SELECT name FROM dbtest1 WHERE name=NULL;#此时执行，不会有任何结果

+ <=>：安全等于，可以用来对NULL进行判断，在两个操作数均为NULL时返回1，当一个操作数为NULL时，返回0

+ LEAST：最小值运算符，SELECT D FROM TABLE WHERE C LEAST(A,B)

+ GREATEST：最大值运算符，SELECT D FROM TABLE WHERE C GREATEST(A,B)

+ REGEXP：正则表达式运算符，SELECT C FROM TABLE WHERE A REGEXPB

+ RLIKE：正则表达式运算符，SELECT C FROM TABLE WHERE A RLIKEB

+ 转义字符

  + \，WHERE name LIKE '_\ _a%'
  + 自定义，WHERE name LIKE '_$ _a%' ESCAPE '$'

+ XOR：逻辑异或

+ 内连接：合并具有同一列的两个以上的表的行，结果集中不包含一个表与另一个表不匹配的行

+ 外连接：合并具有同一列的两个以上的表的行，结果集中除了包含一个表与另一个表不匹配的行之外，还查询到了左表或右表中不匹配的行

+ NATURAL JOIN：自然连接（SQL99新特性），可以理解为SQL92中的等值连接，他会帮你自动查询两张连接表中所有相同的字段，然后进行等值连接

+ USING：（SQL99新特性），指定了具体的相同的字段名称

  SELECT * FROM A JOIN B USING(department_id)

### 单行函数

#### 数值函数

**基本函数**

| 函数                  | 用法                                                         |
| --------------------- | ------------------------------------------------------------ |
| ABS(x)                | 返回x的绝对值                                                |
| SIGN(x)               | 返回x的符号，正数返回1，负数返回-1，0返回0                   |
| PI(x)                 | 返回圆周率的值                                               |
| CEIL(x)，CEILING(x)   | 返回大于或等于某个值的最小整数                               |
| FLOOR(x)              | 返回小于或等于某个值的最大整数                               |
| LEAST(e1,e2,e3...)    | 返回列表中的最小值                                           |
| GREATEST(e1,e2,e3...) | 返回列表中的最大值                                           |
| MOD(x,y)              | 返回x除以y后的余数                                           |
| RAND()                | 返回0~1的随机值                                              |
| RAND(x)               | 返回0~1的随机值，其中x的值用作种子值，相同的x值会产生相同的随机数 |
| ROUND(x)              | 返回一个对x值进行四舍五入后，最接近于x的整数                 |
| ROUND(x,y)            | 返回一个对x值进行四舍五入后，最接近x的值，并保留到小数点后面y位 |
| TRUNCATE(x,y)         | 返回数字x截断为y位小数的结果                                 |
| SQRT(x)               | 返回x的平方根。当x的值为负数时，返回NULL                     |

**角度与弧度互换函数**

| 函数       | 用法                                  |
| ---------- | ------------------------------------- |
| RADIANS(x) | 将角度转化为弧度，其中，参数x为角度值 |
| DEGRESS(x) | 将弧度转化为角度，其中，参数x为弧度值 |

**三角函数**

| 函数       | 用法                                                         |
| ---------- | ------------------------------------------------------------ |
| SIN(x)     | 返回x的正弦值，其中，参数x为弧度值                           |
| ASIN(x)    | 返回x的反正弦值，即获取正弦为x的值，如果x的值不在-1到1之间，则返回NULL |
| COS(x)     | 返回x的余弦值，其中，参数x为弧度值                           |
| ACOS(x)    | 返回x的反余弦值，即获取余弦为x的值，如果x的值不在-1到1之间，则返回NULL |
| TAN(x)     | 返回x的正切值，其中，参数x为弧度值                           |
| ATAN(x)    | 返回x的反正切值，即返回正切值为x的值                         |
| ATAN2(m,n) | 返回两个参数的反正切值                                       |
| COT(x)     | 返回x的余切值，其中，x为弧度值                               |

**指数与对数**

| 函数                 | 用法                                             |
| -------------------- | ------------------------------------------------ |
| POW(x,y)，POWER(x,y) | 返回x的y次方                                     |
| EXP(x)               | 返回e的x次方，其中e是一个常数，2.718281828459045 |
| LN(x)，LOG(x)        | 返回以e为底的x的对数，当x<=0时，返回结果为NULL   |
| LOG10(x)             | 返回以10为底的x的对数，当x<=0时，返回结果为NULL  |
| LOG2(x)              | 返回以2为底的x的对数，当x<=0时，返回结果为NULL   |

**进制间的转换**

| 函数          | 用法                   |
| ------------- | ---------------------- |
| BIN(x)        | 返回x的二进制编码      |
| HEX(x)        | 返回x的十六进制编码    |
| OCT(x)        | 返回x的八进制编码      |
| CONV(x,f1,f2) | 返回f1进制变成f2进制数 |

#### 字符串函数

| 函数                           | 用法                                                         |
| ------------------------------ | ------------------------------------------------------------ |
| ASCII(s)                       | 返回字符串s中的第一个字符的ASCII码值                         |
| CHAR_LENGTH(s)                 | 返回字符串s的字符数，作用与CHARACTER_LENGTH(s)相同           |
| LENGTH(s)                      | 返回字符串s的字节数，和字符集有关                            |
| CONCAT(s1,s2,..)               | 连接s1,s2..为一个字符串                                      |
| CONCAT_WS(x,s1,s2..)           | 同CONCAT(s1,s2,..)函数，但是每个字符串之间要加上x            |
| INSERT(str,idx,len,replacestr) | 将字符串str从第idx位置开始，len个字符长的字串替换为字符串replacestr |
| REPLACE(str,a,b)               | 用字符串b替换字符串str中所有出现的字符串a                    |
| UPPER(s)或UCASE(s)             | 将字符串s的所有字母转成大写字母                              |
| LOWER(s)或LCASE(s)             | 将字符串s的所有字母转成小写字母                              |
| LEFT(str,n)                    | 返回字符串str最左边的n个字符                                 |
| RIGHT(str,n)                   | 返回字符串str最右边的n个字符                                 |
| LPAD(str,len,pad)              | 用字符串pad对str最左边进行填充，直到str的长度为len个字符     |
| RPAD(str,len,pad)              | 用字符串pad对str最右边进行填充，直到str的长度为len个字符     |
| LTRIM(s)                       | 去掉字符串s左侧的空格                                        |
| RTRIM(s)                       | 去掉字符串s右侧的空格                                        |
| TRIM(s)                        | 去掉字符串s开始与结尾的空格                                  |
| TRIM(s1 FROM s)                | 去掉字符串s开始与结尾的s1                                    |
| TRIM(LEADING s1 FROM s)        | 去掉字符串s开始处的s1                                        |
| TRIM(TRAILING s1 FROM s)       | 去掉字符串s结尾处的s1                                        |
| REPEAT(str,n)                  | 返回str重复n次的结果                                         |
| SPACE(n)                       | 返回n个空格                                                  |
| STRCMP(s1,s2)                  | 比较字符串s1,s2的ASCII码值的大小                             |
| SUBSTR(s,index,len)            | 返回从字符串s的index位置起len个字符，作用与SUBSTRIN(s,n,len)、MID(s,n,len)相同 |
| LOCATE(substr,str)             | 返回字符串substr在字符串str中首次出现的位置，作用与POSITION(substr IN str)、INSTR(str,substr)相同，未找到，返回0 |
| ELT(m,s1,s1...)                | 返回指定位置的字符串，如果m=1，则返回s1，如果m=2，则返回s2，如果m=n，则返回sn |
| FIELD(s,s1,s2...)              | 返回字符串s在字符串列表中第一次出现的位置                    |
| FIELD_IN_SET(s1,s2)            | 返回字符串s1在字符串s2中出现的位置，其中，字符串s2是一个以逗号分隔的字符串 |
| REVERSE(s)                     | 返回s反转后的字符串                                          |
| NULLIF(value1,value2)          | 比较两个字符串，如果value1与value2相等，则返回NULL，否则返回value1 |

#### 日期和时间函数

**获取日期、时间**

| 函数                                                         | 用法                           |
| ------------------------------------------------------------ | ------------------------------ |
| CURDATE()，CURRENT_DATE()                                    | 返回当前日期，只包含年、月、日 |
| CURTIME()，CURRENT_TIME()                                    | 返回当前时间，只包含时、分、秒 |
| NOW() / SYSDATE() / CURRENT_TIMESTAMP() / LOCALTIME()/LOCALTIMESTAMP() | 返回当前系统日期和时间         |
| UTC_DATE()                                                   | 返回UTC（世界标准时间）日期    |
| UTC_TIME()                                                   | 返回UTC（世界标准时间）时间    |

**日期与时间戳的转换**

| 函数                     | 用法                                     |
| ------------------------ | ---------------------------------------- |
| UNIX_TIMESTAMP()         | 以UNIX时间戳的形式返回当前时间，单位秒   |
| UNIX_TIMESTAMP(date)     | 将时间date以UNIX时间戳的形式返回，单位秒 |
| FROM_UNIXTIME(timestamp) | 将UNIX时间戳的时间转换为普通格式的时间   |

**获取月份、星期、星期数、天数等函数**

| 函数                                     | 用法                       |
| ---------------------------------------- | -------------------------- |
| YEAR(date) / MONTH(date) / DAY(date)     | 返回具体的日期值           |
| HOUR(time) / MINUTE(time) / SECOND(time) | 返回具体的时间值           |
| MONTHNAME(date)                          | 返回月份：January...       |
| DAYNAME(date)                            | 返回星期几：MONDAY...      |
| WEEKDAY(date)                            | 返回周几，周一是0，周日是6 |
| QUARTER(date)                            | 返回日期对应的季度1-4      |
| WEEK(date)，WEEKOFYEAR(date)             | 返回一年中的第几周         |
| DAYOFYEAR(date)                          | 返回日期是一年中的第几天   |
| DAYOFMONTH(date)                         | 返回日期位于月份的第几天   |
| DAYOFWEEK(date)                          | 返回周几，周日是1，周六是7 |

**日期的操作函数**

| 函数                    | 用法                                       |
| ----------------------- | ------------------------------------------ |
| EXTRACT(type FROM date) | 返回指定日期中特定的部分，type返回指定的值 |

| type取值           | 含义                       |
| ------------------ | -------------------------- |
| MICROSECOND        | 返回毫秒数                 |
| SENCOND            | 返回秒数                   |
| MINUTE             | 返回分钟数                 |
| HOUR               | 返回小时数                 |
| DAY                | 返回天数                   |
| WEEK               | 返回日期在一年中第几个星期 |
| MONTH              | 返回日期在一年中第几个月   |
| QUARTER            | 返回日期在一年中第几个季度 |
| YEAR               | 返回日期的年份             |
| SECOND_MICROSECOND | 返回秒和毫秒数             |
| MINUTE_MICROSECOND | 返回分钟和毫秒数           |
| MINUTE_SECOND      | 返回分钟和秒数             |
| HOUR_MICROSECOND   | 返回小时和毫秒数           |
| HOUR_SECOND        | 返回小时和秒数             |
| HOUR_MINUTE        | 返回小时和分钟             |
| DAY_MICROSECOND    | 返回天和毫秒数             |
| DAY_SECOND         | 返回天和秒数               |
| DAY_MINUTE         | 返回天和分钟数             |
| DAY_HOUR           | 返回天和小时               |
| YEAR_MOUTH         | 返回年和月                 |

**时间和秒钟转换的函数**

| 函数                 | 用法                                        |
| -------------------- | ------------------------------------------- |
| TIME_TO_SEC(time)    | 将time转化为秒并返回结果值                  |
| SEC_TO_TIME(seconds) | 将seconds秒数转换为包含小时、分钟和秒的时间 |

**计算日期和时间的函数**

| 函数                                                         | 用法                                           |
| ------------------------------------------------------------ | ---------------------------------------------- |
| DATE_ADD(datetime,INTERVAL expr type), ADDDATE(date,INTERVAL expr type) | 返回与给定日期时间相差INTERVAL时间段的日期时间 |
| DATE_SUB(date,INTERVAL expr type), SUBDATE(date,INTERVAL expr type) | 返回与date相差INTERVAL时间间隔的日期           |

| type取值      | 含义       |
| ------------- | ---------- |
| HOUR          | 时         |
| MINUTE        | 分         |
| SECOND        | 秒         |
| YEAR          | 年         |
| MONTH         | 月         |
| DAY           | 日         |
| YEAR_MONTH    | 年月       |
| DAY_HOUR      | 日和小时   |
| DAY_MINUTE    | 日和分钟   |
| DAY_SECOND    | 日和秒     |
| HOUR_MINUTE   | 小时和分钟 |
| HOUR_SECOND   | 小时和秒   |
| MINUTE_SECOND | 分钟和秒   |

| 函数                         | 用法                                                         |
| ---------------------------- | ------------------------------------------------------------ |
| ADDTIME(time1,time2)         | 返回time1加上time2的时间，当time2为一个数字时，代表的是秒，可以为负数 |
| ADDTIME(time1,time2)         | 返回time1减去time2的时间，当time2为一个数字时，代表的是秒，可以为负数 |
| DATEDIFF(date1,date2)        | 返回date1-date2的日期间隔天数                                |
| TIMEDIFF(time1,time2)        | 返回time1-time2的时间间隔                                    |
| FROM_DAYS(N)                 | 返回从0000年1月1日起，N天以后的日期                          |
| TO_DAYS(date)                | 返回日期距离0000年1月1日的天数                               |
| LAST_DAY(date)               | 返回date所在月份的最后一天的日期                             |
| MAKEDATE(year,n)             | 针对给定年份与所在年份中的天数返回一个日期                   |
| MAKETIME(hour,minute,second) | 将给定的小时、分钟和秒组成时间并返回                         |
| PERIOD_ADD(time,n)           | 返回time加上n后的时间                                        |

**日期的格式化与解析**

| 函数                              | 用法                                       |
| --------------------------------- | ------------------------------------------ |
| DATE_FORMAT(date,fmt)             | 按照字符串fmt格式化日期date                |
| TIME_FORMAT(time,fmt)             | 按照字符串fmt格式化时间time                |
| GET_FORMAT(date_type,format_type) | 返回日期字符串的显示格式                   |
| STR_TO_DATE(str,fmt)              | 按照字符串fmt对str进行解析，解析为一个日期 |

| fmt格式符 | 说明                                                    | fmt格式符       | 说明                                                    |
| --------- | ------------------------------------------------------- | --------------- | ------------------------------------------------------- |
| %Y        | 4位数字表示年份                                         | %y              | 表示两位数字表示年份                                    |
| %M        | 月名表示年份（January）                                 | %m              | 两位数字表示月份                                        |
| %b        | 缩写的月名（Jan,Feb..）                                 | %c              | 数字表示月份（1,2...）                                  |
| %D        | 英文后缀表示月中的天数（1st,2nd..）                     | %d              | 两位数字表示月中的天                                    |
| %e        | 数字形式表示月中的天数（1,2..）                         |                 |                                                         |
| %H        | 两位数字表示小时，24小时制                              | %h和%I（大写i） | 两位数字表示小时，12小时制                              |
| %k        | 数字形式的小时，24小时制（1,2..）                       | %l（小写L）     | 数字形式表示小时，12小时制（1,2..）                     |
| %i        | 两位数字表示分钟                                        | %S和%s          | 两位数字表示秒                                          |
| %W        | 一周中的星期名称（Sunday）                              | %a              | 一周中的星期缩写（Sun,Mon..）                           |
| %w        | 以数字表示周中的天数（0=Sunday，1=Monday..）            |                 |                                                         |
| %j        | 以三位数字表示年中的天数                                | %U              | 以数字表示年中的第几周（1,2..），其中Sunday为周中第一天 |
| %u        | 以数字表示年中的第几周（1,2..），其中Monday为周中第一天 |                 |                                                         |
| %T        | 24小时制                                                | %r              | 12小时制                                                |
| %p        | AM或PM                                                  | %%              | 表示%                                                   |

GET_FORMAT函数中date_type和format_type参数取值

| 日期类型 | 格式化类型 | 返回的格式化字符串 |
| -------- | ---------- | ------------------ |
| DATE     | USA        | %m.%d.%Y           |
| DATE     | JIS        | %Y-%m-%d           |
| DATE     | ISO        | %Y-%m-%d           |
| DATE     | EUR        | %d.%m.%Y           |
| DATE     | INTERNAL   | %Y%m%d             |
| TIME     | USA        | %h:%i:%s%p         |
| TIME     | JIS        | %H:%i:%s           |
| TIME     | ISO        | %H:%i:%s           |
| TIME     | EUR        | %H:%i:%s           |
| TIME     | INTERNAL   | %H%i%s             |
| DATETIME | USA        | %Y-%m-%d %H.%i.%s  |
| DATETIME | JIS        | %Y-%m-%d %H:%i:%s  |
| DATETIME | ISO        | %Y-%m-%d %H:%i:%s  |
| DATETIME | EUR        | %Y-%m-%d %H.%i.%s  |
| DATETIME | INTERNAL   | %Y%m%d%H%i%s       |

#### 流程控制函数

| 函数                                                         | 用法                                            |
| ------------------------------------------------------------ | ----------------------------------------------- |
| IF(value,value1,value2)                                      | 如果value的值为true，返回value1，否则返回value2 |
| IFNULL(value1,value2)                                        | 如果value1不为NULL，返回value1，否则返回value2  |
| CASE WHEN 条件1 THEN 结果1 WHEN 条件2 THEN 结果2...[ELSE resultn] END | 相当于Java的if..else if...else...               |
| CASE expr WHEN 常量值1 THEN 值1 WHEN常量值2 THEN 值2...[ELSE 值n] END | 相当于Java的switch...case...                    |

#### 加密与解密函数

| 函数                        | 用法                                                         |
| --------------------------- | ------------------------------------------------------------ |
| PASSWORD(str)               | 返回字符串的加密版本，41位长的字符串。加密结果不可逆，常用于用户的密码加密（8.0被弃用） |
| MD5(str)                    | 返回字符串str的md5加密后的值，也是一种加密方式，如果参数为NULL，则返回NULL |
| SHA(str)                    | 从原明文密码str计算并返回加密后的密码字符串，当参数为NULL时，返回NULL，SHA加密算法比MD5更安全 |
| ENCODE(value,password_seed) | 返回使用password_seed作为加密密码加密value（8.0被弃用）      |
| DECODE(value,password_seed) | 返回使用password_seed作为加密密码解密value（8.0被弃用）      |

#### MySQL信息函数

| 函数                                                  | 用法                                                     |
| ----------------------------------------------------- | -------------------------------------------------------- |
| VERSION()                                             | 返回当前MySQL的版本号                                    |
| CONNECTION_ID()                                       | 返回当前MySQL服务器的连接数                              |
| DATABASE()，SCHEMA()                                  | 返回MySQL命令行当前所在的数据库                          |
| USER()，CURRENT_USER()，SYSTEM_USER()，SESSION_USER() | 返回当前连接MySQL的用户名，返回结果格式为"主机名@用户名" |
| CHARSET(value)                                        | 返回字符串value自变量的字符集                            |
| COLLATION(value)                                      | 返回字符串value的比较规则                                |

#### 其他函数

| 函数                           | 用法                                                         |
| ------------------------------ | ------------------------------------------------------------ |
| FORMAT(value,n)                | 返回对数字value进行格式化后的结果数据，n表示四舍五入后保留到小数点后n位 |
| CONV(value,from,to)            | 将value的值进行不同进制之间的转换                            |
| INET_ATON(ipvalue)             | 将以点分隔的IP地址转化为一个数字                             |
| INET_NTOA(value)               | 将数字形式的IP地址转化为以点分隔的IP地址                     |
| BENCHMARK(n,expr)              | 将表达式expr重复执行n次，用于测试MySQL处理expr表达式所耗费的时间 |
| CONVERT(value USING char_code) | 将value所使用的字符编码修改为char_code                       |

### 聚合函数

+ AVG()
+ SUM()
+ MAX()
+ MIN()
+ COUNT()
  + COUNT(*)：计算表中有多少条记录
  + COUNT(1)：计算表中有多少条记录
  + COUNT(具体字段)：不包含NULL值
  + AVG = SUM / COUNT 都不计算NULL值

GROUP BY ...WITH ROLLUP：在所有查询出的分组记录之后增加一条记录，该记录计算查询出的所有记录的总和，即统计记录数量，当使用ROLLUP时，不能同时使用ORDER BY子句

**HAVING**

如果过滤条件中使用了聚合函数，则必须使用HAVING来替换WHERE

HAVING必须声明在GROUP BY后面

|        | 优点                         | 缺点                                 |
| ------ | ---------------------------- | ------------------------------------ |
| WHERE  | 先筛选数据再关联，执行效率高 | 不能使用分组中的计算函数进行筛选     |
| HAVING | 可以使用分组中的计算函数     | 在最后的结果集中进行筛选，执行效率低 |

### 多行子查询

| 操作符 | 含义                                                 |
| ------ | ---------------------------------------------------- |
| IN     | 等于列表中的任意一个                                 |
| ANY    | 需要和单行比较符一起使用，和子查询返回的某一个值比较 |
| ALL    | 需要和单行比较符一起使用，和子查询返回的所有值比较   |
| SOME   | 实际上是ANY的别名，作用相同，一般常用ANY             |

### 创建和管理表

#### 创建数据库

```mysql
#创建数据库
CREATE DATABASE 数据库名;

#创建数据库并指定字符集
CREATE DATABASE 数据库名 CHARACTER SET 字符集;

#判断数据库是否已经存在，不存在则创建数据库
CREATE DATABASE IF NOT EXISTS 数据库名;
CREATE DATABASE IF NOT EXISTS 数据库名 CHARACTER SET 字符集;

```

#### 管理数据库

```mysql
#查看当前连接中的数据库
SHOW DATABASES;

#切换数据库
USE 数据库名;

#查看当前数据库中保存的数据表
SHOW TABLES;

#查看当前使用的数据库
SELECT DATABASE() FROM DUAL;

#查看指定数据库下保存的数据表
SHOW TABLES FROM 数据库名;

```

#### 修改数据库

```mysql
#更改数据库字符集
ALTER DATABASE 数据库名 CHARACTER SET 字符集; #比如gbk、utf8
```

#### 删除数据库

```mysql
#删除指定的数据库
DROP DATABASE 数据库名;

#删除指定的数据库
DROP DATABASE IF EXISTS 数据库名;

```

#### 创建数据表

```mysql
#创建表
CREATE TABLE [IF NOT EXISTS] 表名(
	字段1, 数据类型 [约束条件] [默认值],
    字段3, 数据类型 [约束条件] [默认值],
    ...
    [表约束条件]
);

#基于现有的表创建
CREATE TABLE 表名
AS
SELECT employee_id,last_name,salary
FROM employees;
```

#### 修改表

```mysql
#添加一个字段
ALTER TABLE myemp1 ADD salary DOUBLE(10,2);
ALTER TABLE myemp1 ADD phone_number VARCHAR(20) FIRST;
ALTER TABLE myemp1 ADD email VARCHAR(45) AFTER emp_name;

#修改一个字段：数据类型、长度、默认值
ALTER TABLE myemp1 MODIFY emp_name VARCHAR(35) DEFAULT 'aaa';

#重命名
ALTER TABLE myemp1 CHANGE salary monthly_salary DOUBLE(10,3);

#删除一个字段
ALTER TABLE myemp1 DROP COLUMN email;
```

#### 重命名表

```mysql
RENAME TABLE emp TO myemp;
ALTER TABLE emp RENAME [TO] myemp;
```

#### 删除表

```mysql
DROP TABLE IF EXISTS myemp; 
```

#### 清空表

```mysql
TRUNCATE TABLE myemp;
```

TRUNCATE语句不能回滚，而使用DELETE语句删除数据，可以回滚

#### COMMIT和ROLLBACK

+ COMMIT：提交数据，一旦执行COMMIT，则数据就被永久的保存在了数据库中，意味着数据不可以回滚
+ ROLLBACK：回滚数据，一旦执行ROLLBACK，则可以实现数据的回滚，回滚到最近一次COMMIT之后

**DDL和DML的说明**

+ DDL的操作一旦执行，就不可回滚（因为在执行完DDL之后，一定会执行一次COMMIT，而此COMMIT操作不受SET autocommit = FALSE 影响）
+ DML的操作默认情况，一旦执行，也是不可回滚的。但是，如果在执行DML之前，执行了SET autocommit = FALSE，则执行的DML操作就可以实现回滚

### 数据处理之增删改

#### 插入数据

```mysql
#一条一条添加数据
INSERT INTO emp1 
VALUES(1,'Tom','2000-12-21');

#指明添加的字段
INSERT INTO emp1(id,hire_date,salary)
VALUES(1,'2000-12-21','4000');

#同时添加多条数据
INSERT INTO emp1(id,hire_date,salary)
VALUES
(1,'2000-12-21','4000'),
(2,'2000-12-21','4000');

#将查询结果插入到表中
INSERT INTO emp1(id,hire_date,salary)
SELECT id,hire_date,salary
FROM employees
WHERE id = 1;
```

#### 更新数据

```mysql
UPDATE emp 
SET hire_date = CURDATE(), 	salary = 6000
WHERE id = 5;
```

#### 删除数据

```mysql
DELETE FROM departments
WHERE deaprtment_id = 50;
```

### MySQL中的数据类型

| 类型             | 类型举例                                                     |
| ---------------- | ------------------------------------------------------------ |
| 整数类型         | TINYINT、SAMLLINT、MEDIUMINT、INT(或INTEGER)、BIGINT         |
| 浮点类型         | FLOAT、DOUBLE                                                |
| 定点数类型       | DECIMAL                                                      |
| 位类型           | BIT                                                          |
| 日期时间类型     | YEAR、TIME、DATE、DATETIME、TIMESTAMP                        |
| 文本字符串类型   | CHAR、VARCHAR、TINYTEXT、TEXT、MEDIUMTEXT、LONGTEXT          |
| 枚举类型         | ENUM                                                         |
| 集合类型         | SET                                                          |
| 二进制字符串类型 | BINARY、VARBINARY、TINYBLOB、BLOB、MEDIUMBLOB、LONGBLOB      |
| JSON类型         | JSON对象、JSON数组                                           |
| 空间数据类型     | 单值类型：GEOMETRY、POINT、LINESTRING、POLYGON<br />集合类型：MULTIPOINT、MULTILINESTRING、MULTIPOLYGON、GEOMETRYCOLLECTION |

常见数据类型的属性

| MySQL关键字        | 含义                     |
| ------------------ | ------------------------ |
| NULL               | 数据列可包含NULL值       |
| NOT NULL           | 数据列不允许包含NULL值   |
| DEFAULT            | 默认值                   |
| PRIMARY KEY        | 主键                     |
| AUTO_INCREMENT     | 自动递增，适用于整数类型 |
| UNSIGNED           | 无符号                   |
| CHARACTER SET name | 指定一个字符集           |

```mysql
#创建表的时候，指明表的字符集
CREATE TABLE temp(
id INT)
CHARACTER SET 'utf8'

#创建表，指明表中的字段时，可以指定字段的字符集
CREATE TABLE temp(
id INT,
NAME VARCHAR(15) CHARACTER SET 'gbk')
```

#### 整数类型

| 整数类型     | 字节 | 有符号数取值范围                         | 无符号数取值范围       |
| ------------ | ---- | ---------------------------------------- | ---------------------- |
| TINYINT      | 1    | -128~127                                 | 0~255                  |
| SAMLLINT     | 2    | -32768~32767                             | 0~65535                |
| MEDIUMINT    | 3    | -8388608~8388607                         | 0~16777215             |
| INT、INTEGER | 4    | -2147483648~2147483647                   | 0~4294967295           |
| BIGINT       | 8    | -9223372036854775808~9223372036854775807 | 0~18446744073709551615 |

**整数类型的可选属性**

+ M：表示显示宽度，M的取值范围是（0，255）、例如int(5)，当数据宽度小于5位的时候在数字前面需要用字符填满宽度。该项功能需要配合"ZEROFILL"使用，表示用0填满宽度，否则指定显示宽度无效

  不会对插入的数据有任何影响，还是按照类型的实际宽度保存，即显示宽度与类型可以存储的取值范围无关，从MySQL8.0.17开始，整数类型不推荐使用显示宽度属性

+ UNSIGNED：无符号类型

+ ZEROFILL：当使用ZEROFILL时，自动会添加UNSIGNED

#### 浮点类型

+ FLOAT表示单精度浮点数

+ DOUBLE表示双精度浮点数

+ REAL默认就是DOUBLE。如果把SQL模式设定为启用REAL_AS_FLOAT，那么MySQL就认为REAL是FLOAT

  启用REAL_AS_FLOAT：SET sql_mode = "REAL_AS_FLOAT";

| 类型   | 有符号数取值范围                                             | 无符号数取值范围                                        | 占用字节数 |
| ------ | ------------------------------------------------------------ | ------------------------------------------------------- | ---------- |
| FLOAT  | (-3.402823466E+38，-1.175494351E-38)，0，（1.175494351E-38，3.402823466E+38） | 0，（1.175494351E-38，3.402823466E+38）                 | 4          |
| DOUBLE | （-1.7976931348623157E+308，-2.2250738585072014E-308），0，（2.2250738585072014E-308，1.7976931348623157E+308） | 0，（2.2250738585072014E-308，1.7976931348623157E+308） | 8          |

MySQL存储浮点数的格式为：符号（S）、尾数（M）和阶码（E）。因此，无论有没有符号，MySQL浮点数都会存储表示符号的部分，因此，所谓的无符号数取值范围，其实就是有符号数取值范围大于0的部分

FLOAT(M,D)，DOUBLE(M,D)，整数位超出报错，小数位超出四舍五入，在8.0.17不推荐使用

**浮点数是不准确的，避免使用=来判断两个数是否相等**

#### 定点数类型

| 数据类型                 | 字节数  | 含义               |
| ------------------------ | ------- | ------------------ |
| DECIMAL(M,D),DEC,NUMERIC | M+2字节 | 有效范围由M和D决定 |

M为精度，D为标度，0<=M<=65，0<=D<=30

定点数在MySQL内部是以字符串的形式进行存储，这就决定了它一定是精准的

默认DECIMAL(10,0)

#### 位类型

BIT类型中存储的是二进制值，类似010110

| 二进制字符串类型 | 长度 | 长度范围 | 占用空间        |
| ---------------- | ---- | -------- | --------------- |
| BIT(M)           | M    | 1<=M<=64 | 约(M+7)/8个字节 |

M表示二进制的位数，默认是1

#### 日期时间类型

| 类型      | 名称     | 字节 | 日期格式            | 最小值                  | 最大值                 |
| --------- | -------- | ---- | ------------------- | ----------------------- | ---------------------- |
| YEAR      | 年       | 1    | YYYY或YY            | 1901                    | 2155                   |
| TIME      | 时间     | 3    | HH:MM:SS            | -838:59:59              | 838:59:59              |
| DATE      | 日期     | 3    | YYYY-MM-DD          | 1000-01-01              | 9999-12-03             |
| DATETIME  | 日期时间 | 8    | YYYY-MM-DD HH:MM:SS | 1000-01-01 00:00:00     | 9999-12-31 23:59:59    |
| TIMESTAMP | 日期时间 | 4    | YYYY-MM-DD HH:MM:SS | 1970-01-01 00:00:00 UTC | 2038-01-19 03:14:07UTC |

从MySQL5.5.27开始，2位格式的YEAR已经不推荐使用

**TIMESTAMP和DATETIME区别**

+ TIMESTAMP存储空间比较小，表示的日期时间范围也比较小
+ 底层存储方式不同，TIMESTAMP底层存储的是毫秒值，距离1970-1-1 0:0:0 0毫秒的毫秒值
+ 两个日期比较大小或日期计算时，TIMESTAMP更方便、更快
+ TIMESTAMP和时区有关。TIMESTAMP会根据用户的时区不同，显示不同的结果。而DATETIME则只能反映出插入时当地的时区，其他时区的人查看数据必然会有误差的

修改当前时区：SET time_zone = '+9:00';

#### 文本字符串类型

| 文本字符串类型 | 值的长度 | 长度范围         | 占用的存储空间   |
| -------------- | -------- | ---------------- | ---------------- |
| CHAR(M)        | M        | 0<=M<=255        | M个字节          |
| VARCHAR(M)     | M        | 0<=M<=65535      | M+1个字节        |
| TINYTEXT       | L        | 0<=L<=255        | L+2个字节        |
| TEXT           | L        | 0<=L<=65535      | L+2个字节        |
| MEDIUMTEXT     | L        | 0<=L<=16777215   | L+3个字节        |
| LONGTEXT       | L        | 0<=L<=4294967295 | L+4个字节        |
| ENUM           | L        | 0<=L<=65535      | 1或2个字节       |
| SET            | L        | 0<=L<=64         | 1,2,3,4或8个字节 |

**CHAR和VARCHAR**

| 类型       | 特点     | 空间上       | 时间上 | 适用场景             |
| ---------- | -------- | ------------ | ------ | -------------------- |
| CHAR(M)    | 固定长度 | 浪费存储空间 | 效率高 | 存储不大，速度要求高 |
| VARCHAR(M) | 可变长度 | 节省存储空间 | 效率低 | 非CHAR的情况         |

+ MyISAM：最好使用CHAR，这样使得整个表静态化，从而使数据检索更快，用空间换时间
+ MEMORY：MEMORY数据表目前都使用固定长度的数据行进行存储，因此无论使用CHAR或VARCHAR都没有关系，两者都是作为CHAR类型处理的
+ InnoDB：建议使用VARCHAR类型。因为对于InnoDB数据表，内存的行存储格式并没有区分固定长度和可变列长度（所有数据行都使用指向数据列值的头指针），而且主要影响性能的因素是数据行使用的存储总量，由于char平均占用的空间多于varchar，所以除了简短并且固定长度的，其他考虑varchar，这样节省空间，对磁盘I/O和数据存储总量比较好

由于实际存储的长度不确定MySQL不允许TEXT类型的字段做主键

TEXT文本类型，可以存比较大的文本段，搜索速度稍慢，因此如果不是特别大的内容，建议使用CHAR、VARCHAR来代替。还有TEXT类型不用加默认值，加了也没用。而且TEXT和BLOB类型的数据删除后容易导致空洞，使得文件碎片比较多，所以频繁使用的表不建议包含TEXT类型字段，建议单独分出去，单独用一个表

#### ENUM类型

ENUM类型的取值范围需要在定义字段时进行指定，设置字段时，ENUM类型只允许从成员中选取单个值，不能一次选取多个值

| 文本字符串类型 | 长度 | 长度范围    | 占用的存储空间 |
| -------------- | ---- | ----------- | -------------- |
| ENUM           | L    | 1<=L<=65535 | 1或2个字节     |

```mysql
CREATE TABLE test_enum(
season ENUM('春','夏','秋','冬')
);
```

#### SET类型

SET表示一个字符串对象，可以包含0个或多个成员，但成员个数的上限为64。设置字段值时，可以取取值范围内的0个或多个值

| 成员个数范围（L表示实际成员个数） | 占用的存储空间 |
| --------------------------------- | -------------- |
| 1<=L<=8                           | 1              |
| 9<=L<=16                          | 2              |
| 17<=L<=24                         | 3              |
| 25<=L<=32                         | 4              |
| 33<=L<=64                         | 8              |

SET类型在选取成员时，可以一次选择多个成员，这一点与ENUM类型不同

```mysql
CREATE TABLE test_enum(
s SET('A','B','C','D')
);
```

#### 二进制字符串类型

主要存储一些二进制数据，比如图片、音频、视频等二进制数据

| 二进制字符串类型 | 特点     | 值的长度                    | 占用空间  |
| ---------------- | -------- | --------------------------- | --------- |
| BINARY(M)        | 固定长度 | 0<=M<=255                   | M个字节   |
| VARBINARY(M)     | 可变长度 | 0<=M<=65535                 | M+1个字节 |
| TINYBLOB         | L        | 0<=L<=255                   | L+1个字节 |
| BLOB             | L        | 0<=L<=65535(相当于64KB)     | L+2个字节 |
| MEDIUMBLOB       | L        | 0<=L<=16777215(相当于16MB)  | L+3个字节 |
| LONGBLOB         | L        | 0<=L<=4294967295(相当于4GB) | L+4个字节 |

实际工作中，往往不会在MySQL数据库中使用BLOB类型存储大对象数据，通常会将图片、音频和视频文件存储到服务器的磁盘上，并将图片、音频和视频的访问路径存储到MySQL中

#### JSON类型

```mysql
CREATE TABLE test_json(
	js json
);

INSERT INTO test_json
VALUES('{"name":"songhk","age":18,"address":"{"province":"beijing","city":"beijing"}"}');

SELECT js -> '$.name' AS NAME, js -> '$.age' AS age, js -> '$.address.province' AS PROVINCE
FROM test_json;
```

### 数据完整性与约束

```mysql
#查看某个表已有的约束
SELECT * FROM information_schema.table_constraints
WHERE table_name = '表名称';
```

+ not null

  + 非空约束只能出现在表对象的列上，只能某个列单独限定非空，不能组合非空
  + 一个表可以有很多列都分别限定了非 空

+ unique

  + 唯一约束可以是某一个列的值唯一，也可以多个列组合的值唯一
  + 唯一约束，允许出现多个空值NULL
  + 在创建唯一约束的时候，如果不给唯一约束命名，就默认和列名相同
  + MySQL会给唯一约束的列上默认创建一个唯一索引
  + 删除唯一约束只能通过删除唯一索引的方式删除
  + 删除时需要指定唯一索引名，唯一索引名和唯一约束名相同

  ```mysql
  #表级约束
  CREATE TABLE test(
  	id INT UNIQUE,
      last_name VARCHAR(15),
      email VARCHAR(25),
      salary DECIMAL(10,2),
      
      CONSTRAINT uk_test_email UNIQUE(email)
  );
  
  #在ALTER TABLE时添加约束
  ALTER TABLE test 
  ADD CONSTRAINT uk_test_email UNIQUE(email);
  
  ALTER TABLE test
  MODIFY last_name VARCHAR(15) UNIQUE;
  
  #删除唯一索引
  ALTER TABLE test
  DROP INDEX uk_name_pwd;
  
  #查看表索引
  SHOW INDEX FROM 表名：
  ```

+ primary key

  + 一个表最多只能有一个主键约束，建立主键约束可以在列级别创建，也可以在表级别创建
  + 主键约束对应着表中的一列或者多列（复合主键）
  + 如果是多列组合的复合主键约束，那么这些列都不允许为空值，并且组合的值不允许重复
  + MySQL的主键名总是PRIMARY，就算自己命名了主键约束名也没用
  + 当创建主键约束时，系统默认会在所在的列或列组合上建立对应的主键索引，如果删除主键约束，主键约束对应的索引就自动删除

  ```mysql
  #在CREATE TABLE时添加约束
  CREATE TABLE test(
  	id INT PRIMARY KEY,
      last_name VARCHAR(15),
  );
  
  #表级约束
  CREATE TABLE test(
  	id INT,
      last_name VARCHAR(15),
      
      CONSTRAINT pk_test_id PRIMARY KEY(id) #没有必要起名字
  );
  
  #在ALTER TABLE时添加约束
  ALTER TABLE test 
  ADD PRIMARY KEY(id);
  
  #删除主键索引
  ALTER TABLE test
  DROP PRIMARY KEY;
  ```

+ auto_increment

  + 一个表最多只能有一个自增长列
  + 当需要产生唯一标识符或顺序值时，可设置自增长
  + 自增长列约束的列必须是键列（主键列，唯一键列）
  + 自增长列约束的列的数据类型必须是整数类型
  + 如果自增列指定了0和null，会在当前最大值的基础上自增；如果自增列手动指定了具体值，直接赋值为具体值

  ```mysql
  CREATE TABLE test(
  	id INT PRIMARY KEY AUTO_INCREMENT,
      last_name VARCHAR(15),
  );
  
  #在ALTER TABLE时添加约束
  ALTER TABLE test 
  MODIFY id INT AUTO_INCREMENT;
  
  #在ALTER TABLE时删除约束
  ALTER TABLE test 
  MODIFY id INT;
  ```

+ foregin key

  + 从表的外键列，必须引用/参考主表的键主键或唯一约束的列，因为被依赖/被参考的值必须是唯一的
  + 在创建外键约束时，如果不给外键约束命名，默认名不是列名，而是自动产生一个外键名，也可以指定外键约束名
  + 创建表时就指定外键约束的话，先创建主表，再创建从表
  + 删表时，先删从表（或先删除外键约束），再删除主表
  + 当主表的记录被从表参照时，主表的记录将不允许删除，如果要删除数据，需要先删除从表中依赖该记录的数据，然后才可以删除主表的数据
  + 在从表中指定外键约束，而且一个表可以建立多个外键约束
  + 从表的外键列与主表被参照的列名字可以不相同，但是数据类型必须一样，逻辑意义一致。如果类型不一样，创建子表时，就会出现错误"ERROR 1005(HY000):Cant create table database.tablename(error:150)"
  + 当创建外键约束时，系统默认会在所在的列上建立对应的普通索引，但是索引名是列名，不是外键约束名
  + 删除外键索引后，必须手动删除对应的索引

  ```mysql
  #在CREATE TABLE时添加
  CREATE TABLE emp(
  	emp_id INT PRIMARY KEY AUTO_INCREMENT,
      department_id INT,
      
      CONSTRAINT fk_emp1_dept_id FOREIGN KEY(department_id) REFERENCES dept(dept_id)
  );
  
  CREATE TABLE emp(
  	emp_id INT PRIMARY KEY AUTO_INCREMENT,
      deptid INT,
      FOREIGN KEY (deptid) REFERENCES dept(did) ON UPDATE CASCADE ON DELETE RESTRICT
     
  );
  
  #在ALTER TABLE时添加约束
  ALTER TABLE emp 
  ADD CONSTRAINT fk_emp1_dept_id FOREIGN KEY(department_id) REFERENCES dept(dept_id);
  
  #删除外键约束
  ALTER TABLE 从表名
  DROP FOREIGN KEY 外键约束名;
  ```

  **约束等级**

  + Cascade方式：在父表上update/delete记录时，同步update/delete掉子表上的匹配记录
  + Set null方式：在父表上update/delete记录时，将子表上匹配记录的列设为null，但是要注意子表的外键列不能为not null
  + No action方式：如果子表中有匹配的记录，则不允许对父表对应候选键进行update/delete操作
  + Restrict方式：同no action，都是立即检查外键约束
  + Set default方式：父表有变更时，子表将外键列设置成一个默认的值，但InnoDB不能识别

  如果没有指定等级，就相当于Restrict方式

  对于外键约束，最好采用：ON UPDATE CASCADE ON DELETE RESTRICT的方式

+ check

  + MySQL5.7不支持，MySQL8.0支持

  ```mysql
  CREATE TABLE test(
  	id INT,
      last_name VARCHAR(15),
      salary DECIMAL(10,2) CHECK(salary > 2000)
  );
  ```

+ default

  ```mysql
  #在CREATE TABLE时添加
  CREATE TABLE test(
  	id INT,
      last_name VARCHAR(15),
      salary DECIMAL(10,2) DEFAULT 2000
  );
  
  #在ALTER TABLE时添加约束
  ALTER TABLE test 
  MODIFY salary DECIMAL(8,2) DEFAULT 2500;
  ```

### 视图

**常见的数据库对象**

| 对象                  | 概述                                                         |
| --------------------- | ------------------------------------------------------------ |
| 表（TABLE）           | 表是存储数据的逻辑单元，以行和列的形式存在，列就是字段，行就是记录 |
| 数据字典              | 就是系统表，存放数据库相关信息的表，系统表的数据通常由数据库系统维护，程序员通常不应该修改，只可查看 |
| 约束（COSTRAINT）     | 执行数据校验的规则，用于保证数据完整性的规则                 |
| 视图（VIEW）          | 一个或多个数据表里的数据的逻辑显示，视图并不存储数据         |
| 索引（INDEX）         | 用于提高查询性能，相当于书的目录                             |
| 存储过程（PROCEDURE） | 用于完成一次完整的业务逻辑，没有返回值，但可通过传出参数将多个值传给调用环境 |
| 存储函数（FUNCTION）  | 用于完成一次特定的计算，具有一个返回值                       |
| 触发器（TRIGGER）     | 相当于一个事件监听器，当数据库发生特定事件后，触发器被触发，完成相应的处理 |

**视图的理解**

+ 视图是一种虚拟表，本身是不具有数据的，占用很少的内存空间
+ 视图建立在已有表的基础上，视图赖以建立的这些表称为基表
+ 视图的创建和删除只影响视图本身，不影响对应的基表，但是当对视图中的数据进行添加、删除和修改操作时，数据表中的数据会相应地发生变化，反之亦然
+ 向视图提供数据内容的语句为SELECT语句，可以将视图理解为存储起来的SELECT语句
+ 优点：简化查询，控制数据访问权限

```mysql
CREATE [OR REPLACE]
[ALGORITHM = {UNDEFINED | MERGE | TEMPTABLE}]
VIEW 视图名称[(字段列表)]
AS 查询语句
[WITH [CASCADED | LOCAL] CHECK OPTION]

#精简版
CREATE VIEW 视图名称[(字段列表)]
AS 查询语句

#基于视图emp_dept和emp_yaer_salary创建视图
CREATE VIEW emp_dept_ysalary
AS
SELECT emp_dept.ename,dname,year_salary
FROM emp_dept INNER JOIN emp_year_salary
ON emp_dept.ename = emp_year_salary.ename

#查看视图
#语法1：查看数据库的表对象、视图对象
SHOW TABLES;
#语法2：查看视图的结构
DESCRIBE vu_emp;
#语法3：查看视图的属性信息
SHOW TABLE STATUS LIKE 'vu_emp'\G;
#语法4：查看视图的详细定义信息
SHOW CREATE VIEW vu_emp;

#修改视图
#方式1：使用CREATE OR REPLACE VIEW修改视图
CREATE OR REPLACE VIEW empvu
(id_number,name,sal,department_id)
AS
SELECT employee_id,first_name || ' ' || last_name,salary,department_id
FROM employees
WHERE department_id=80;

#方式2：ALTER VIEW
ALTER VIEW 视图名称
AS
查询语句

#删除视图
DROP VIEW [IF EXISTS] vu_emp;
```

**不可更新的视图**

要使视图可以更新，视图中的行和底层基本表中的行必须存在一对一关系。另外当视图定义出现如下情况时，视图不支持更新操作：

+ 在定义视图的时候指定了ALGORITHM=TEMPTABLE，视图将不支持INSERT和DELETE操作
+ 视图中不包含基表中所有被定义为非空又未指定默认值的列，视图将不支持INSERT操作
+ 在定义视图的SELECT语句中使用了JOIN联合查询，视图将不支持INSERT和DELETE操作
+ 在定义视图的SELECT语句后的字段列表中使用了数学表达式或子查询，视图将不支持INSERT，也不支持UPDATE使用了数学表达式、子查询的字段值
+ 在定义视图的SELECT语句后的字段列表中使用DISTINCT、聚合函数、GROUP BY、HAVING、UNION等视图将不支持INSERT、UPDATE、DELETE
+ 在定义视图的SELECT语句中包含了子查询，而子查询中引用了FROM后面的表，视图将不支持INSERT、UPDATE、DELETE
+ 视图定义基于一个不可更新视图
+ 常量视图

### 存储过程与函数

#### 存储过程

相较于函数，存储过程没有返回值

存储过程的参数类型可以是IN、OUT和INOUT，默认为IN

```mysql
#创建存储过程
CREATE PROCEDURE 存储过程名(IN|OUT|INOUT 参数名 参数类型,...)
[characteristics...]
BEGIN
存储过程体
END

#设置结束符
DELIMITER 新的结束标记

#存储过程的调用
CALL 存储过程名(参数);

#带OUT
DELIMITER $
CREATE PROCEDURE show_min_salary(OUT ms DOUBLE)
BEGIN
	SELECT MIN(salary) INTO ms
	FROM employees;
END
DELIMITER ;

#调用
CALL show_min_salary(@ms);
SELECT @ms;

#带IN
DELIMITER $
CREATE PROCEDURE show_someone_salary(IN empname VARCHAR(20))
BEGIN
	SELECT salary FROM employees
	WHERE last_name = empname;
END
DELIMITER ;

#调用方式1
CALL show_someone_salary('Abel');
#调用方式2
SET @empname := 'Abel';
CALL show_someone_salary(@empname);

#带INOUT
DELIMITER $
CREATE PROCEDURE show_mgr_name(INOUT empname VARCHAR(25))
BEGIN
	SELECT last_name INTO empname
	FROM employees
	WHERE employee_id = (
                           SELECT manager_id
                           FROM employees
                           WHERE last_name = empname
                         );
END
DELIMITER ;

SET @empname = 'Abel';
CALL show_mgr_name(@empname);
SELECT @empname;
```

characteristics表示创建存储过程时指定的对存储过程的约束条件，其取值信息如下：

+ LANGLACE SQL：说明存储过程执行体是由SQL语句组成的，当前系统支持的语言为SQL
+ [NOT] DETERMINISTIC：指明存储过程执行的结果是否确定。DETERMINISTIC表示结果是确定的。每次执行存储过程时，相同的输入会得到相同的输出。NOT DETERMINISTIC表示结果是不确定的，相同的输入可能得到不同的输出。如果没有指定任意一个值，默认为NOT DETERMINISTIC
+ {CONTAINS SQL | NO SQL | READS SQL DATA | MODIFIES SQL DATA}：指明子程序使用SQL的限制
  + CONTAINS SQL表示当前存储过程的子程序包含SQL语句，但是并不包含读写数据的SQL语句
  + NO SQ表示当前存储过程的子程序不包含任何SQL
  + READS SQL DATA表示当前存储过程的子程序包含读取数据的SQL语句
  + MODIFIES SQL DATA表示当前存储过程的子程序包含写数据的SQL语句
  + 默认情况下，系统会指定为CONTAINS SQL
+ SQL SECURITY {DEFINER | INVOKER}：执行当前存储过程的权限，即指明哪些用户能够执行当前存储过程
  + DEFINER表示只有当前存储过程的创建者或者定义者才能执行当前存储过程
  + INVOKER表示拥有当前存储过程的访问权限的用户能够执行当前存储过程
  + 如果没有设置相关的值，则MySQL默认指定值为DEFINER
+ COMMENT 'string'：注释信息，可以用来描述存储过程

#### 存储函数

```mysql
#创建函数
CREATE FUNCTION 函数名(参数名 参数类型...)
RETURNS 返回值类型
[characteristics...]
BEGIN
	函数体  #函数体中肯定有RETURN语句
END

#例1
DELIMITER $
CREATE FUNCTION email_by_name()
RETURNS VARCHAR(25)
        DETERMINISTIC
        CONTAINS SQL
        READS SQL DATA
BEGIN
	RETURN(SELECT email FROM employees WHERE last_name = 'Abel');
END
DELIMITER ;

#调用
SELECT email_by_name();
```

若在创建存储函数中报错"you might want to use the less safe log_bin_trust_function_creators variable"，有两种处理方法：

1. 加上必要的函数特性 [NOT] DETERMINISTIC和{CONTAINS SQL | NO SQL | READS SQL DATA | MIDIFIES SQL DATA}
2. SET GLOBAL log_bin_trust_function_creators  = 1;

|          | 关键字    | 调用语法        | 返回值          | 应用场景                         |
| -------- | --------- | --------------- | --------------- | -------------------------------- |
| 存储过程 | PROCEDURE | CALL 存储过程() | 理解为0个或多个 | 一般用于更新                     |
| 存储函数 | FUNCTION  | SELECT 函数()   | 只能一个        | 一般用于查询结果为一个值并返回时 |

#### 存储过程和函数的查看、修改、删除

**查看**

```mysql
#使用SHOW CREATE查看
SHOW CREATE {PROCEDURE | FUNCTION} 存储过程名或函数名

#使用SHOW STATUS查看
SHOW {PROCEDURE | FUNCTION} STATUS [LIKE '存储过程名或函数名']

#从information_schema.Routines表中查看
SELECT * FROM information_schema.Routines
WHERE ROUTINE_NAME = '存储过程名或函数名' [AND ROUTINE_TYPE = {'PROCEDURE | FUNCTION'}];
```

**修改**

修改存储过程和函数，不影响存储过程或函数功能，只是修改相关特性

```mysql
#
ALTER {PROCEDURE | FUNCTION} 存储过程或函数名 [characteristic ...]
```

**删除**

```mysql
DROP {PROCEDURE | FUNCTION} [IF EXISTS] 存储过程或函数名
```

### 变量、流程控制与游标

#### 变量

**系统变量**

系统变量分为全局系统变量（需要添加global关键字）以及会话系统变量（需要添加session关键字），如果不写，默认会话级别

```mysql
#查看所有全局变量
SHOW GLOBAL VARIABLES;

#查看所有会话变量
SHOW SESSION VARIABLES;
或
SHOW VARIABLES

#查看满足条件的部分系统变量
SHOW GLOBAL VARIABLES LIKE '%标识符%';

#查看满足条件的部分会话变量
SHOW SESSION VARIABLES LIKE '%标识符%';

#查看指定系统变量
#@@首先标记会话系统变量，如果会话系统变量不存在，则标记全局系统变量
SELECT @@global.变量名;

#查看指定会话变量
SELECT @@session.变量名;
或
SELECT @@变量名;

#修改系统变量的值
SET @@global.变量名=变量值;
或
SET GLOBAL 变量名=变量值;

#修改会话变量的值
SET @@session.变量名=变量值;
或
SET SESSION 变量名=变量值;
```

**用户变量**

用户变量是自己定义的，用@开头，根据作用范围不同又分为会话用户变量和局部变量

+ 会话用户变量：作用域和会话变量一样，只对当前连接会话有效

  ```mysql
  #变量的定义
  #方式1：=或:=
  SET @用户变量 = 值;
  SET @用户变量 := 值;
  
  #方式2：:=或INTO
  SELECT @用户变量 := 表达式 [FROM 等子句];
  SELECT 表达式 INTO @用户变量 [FROM 等子句];
  
  #查看用户变量的值
  SELECT @用户变量
  ```

+ 局部变量：只在BEGIN和END语句块中有效，局部变量只能在存储过程和函数中使用

  ```mysql
  BEGIN
      #声明局部变量
  	DECLARE 变量名 变量数据类型 [DEFAULT 变量默认值]
  	
  	#为局部变量赋值
  	SET 变量名 = 值;
  	SELECT 值 INTO 变量名 [FROM子句]
  	
  	#查看局部变量的值
  	SELECT 变量名;
  END
  ```

#### 定义条件

定义条件就是给MySQL中的错误码命名，它将一个错误名字和指定的错误条件关联起来，这个名字可以随后被用在定义处理程序的DECLARE HANDLER语句中

错误码的说明：

+ MySQL_error_code和sqlstate_value都可以表示MySQL的错误
  + MySQL_error_code是数值类型错误代码
  + sqlstate_value是长度为5的字符串类型错误代码
+ 例如，在ERROR1418(HY000)中1418是MySQL_error_code，HY000是sqlstate_value

```mysql
#定义条件
DECLARE 错误名称 CONDITION FOR 错误码（或错误条件）

#例1
#方式1,使用MySQL_error_code
DECLARE Field_Not_Be_NULL CONDITION FOR 1048;

#方式2,使用sqlstate_value
DECLARE Field_Not_Be_NULL CONDITION FOR SQLSTATE '23000';
```

**定义处理程序**

```mysql
#定义处理程序
DECLARE 处理方式 HANDLER FOR 错误类型 处理语句
```

+ 处理方式：处理方式有3个取值：CONTINUE、EXIT、UNDO
  + CONTINUE：表示遇到错误不处理，继续执行
  + EXIT：表示遇到错误马上退出
  + UNDO：表示遇到错误后撤回之前的操作。MySQL中暂时不支持这样的操作
+ 错误类型
  + SQLSTATE '字符串错误码'：长度为5的字符串类型错误代码
  + MySQL_error_code：数值类型错误代码
  + 错误名称：表示DECLARE...CONDITION定义的错误条件名称
  + SQLWARNING：匹配所有以01开头的SQLSTATE错误代码
  + NOT FOUND：匹配所有以02开头的SQLSTATE错误代码
  + SQLEXCEPTION：匹配所有没有被SQLWARNING或NOT FOUND捕捉的SQLSTATE错误代码
+ 处理语句：如果出现上述条件之一，则采用对应的处理方式，并执行指定的处理语句。语句可以像是"SET 变量 = 值"这样简单语句，也可以是使用"BEGIN...END"编写的复合语句

#### 流程控制

**分支结构**

```mysql
#IF
IF 表达式1 THEN 操作1
[ELSEIF 表达式2 THEN 操作2]
[ELSE 操作N]
END IF

#CASE
#情况一：类似于switch
CASE 表达式
WHEN 值1 THEN 结果1或语句1(如果是语句，需要加分号)
WHEN 值2 THEN 结果2或语句2(如果是语句，需要加分号)
...
ELSE 结果N或语句N(如果是语句，需要加分号)
END [CASE] (如果是放在begin end中需要加case，如果放在select后面不需要)

#情况二：类似于多重IF
CASE
WHEN 条件1 THEN 结果1或语句1(如果是语句，需要加分号)
WHEN 条件2 THEN 结果2或语句2(如果是语句，需要加分号)
...
ELSE 结果N或语句N(如果是语句，需要加分号)
END [CASE] (如果是放在begin end中需要加case，如果放在select后面不需要)
```

**循环结构**

这三种循环都可以省略名称，但如果循环中添加了循环控制语句（LEAVE或ITERATE）则必须添加名称

```mysql
#LOOP
#可使用LEAVE跳出循环
[loop_label:] LOOP
	循环执行的语句
END LOOP [loop_babel]

#例1
BEGIN
	DECLARE num INT DEFAULT 1;
	loop_label:LOOP
		SET num = num + 1;
		IF num >= 10 THEN LEAVE loop_label;
		END IF;
	END LOOP loop_label;
END

#WHILE
[while_label:] WHERE 循环条件 DO
	循环体
END WHILE [while_label];

#例2
BEGIN
	DECLARE num INT DEFAULT 1;
	WHILE num <= 10 DO
		循环体
		SET num = num + 1;
	END WHERE;
END

#REPEAT
[repeat_label:] REPEAT
	循环体
UNTIL 结束循环的表达式
END REPEAT [repeat_label]

#例3
BEGIN
	DECLARE num INT DEFAULT 1;
	REPEAT
		SET num = num + 1;
	UNTIL num >= 10
	END REPEAT;
END
```

**LEAVE和ITERATE的使用**

```mysql
#LEAVE
#可以用在循环语句内，或者以BEGIN...END包裹起来的程序体内，表示跳出循环或者跳出程序体的操作
LEAVE 标记名

#例1
DELIMITER //
CREATE PROCEDURE leave_begin(IN num INT)
begin_label: BEGIN
	IF num <= 0 THEN LEAVE begin_label;
	ELSEIF num = 1 THEN SELECT AVG(salary) FROM employees;
	ELSE SELECT MAX(salary) FROM employees;
	END ID;
END //
DELIMITER ;

#例2
DELIMITER //
CREATE PROCEDURE leave_while(OUT num INT)
BEGIN
	DECLARE avg_sal DOUBLE;
	while_label: WHILE TRUE DO
		IF avg_sal <= 10000 THEN LEAVE while_label;
		END IF;
	END WHILE while_label;
END //
DELIMITER ;

#ITERATE
#只能在循环语句中使用，表示重新开始循环，将执行顺序转到语句段开头处，类似于continue
ITERATE label

#例3
DELIMITER //
CREATE PROCEDURE test_iterate()
BEGIN
	DECLARE num INT DEFAULT 0;
	loop_label:LOOP
		SET num = num + 1;
		IF num < 10 THEN ITERATE loop_label;
		ELSEIF num > 15 THEN LEAVE loop_label;
		END IF;
	END LOOP;
END //
DELIMITER ;
```

#### 游标

游标提供了一种灵活的操作方式，让我们能够对结果集中的每一条记录进行定位，并对指向的记录中的数据进行操作的数据结构，游标让SQL这种面向集合的语言有了面向过程开发的能力

在SQL中，游标是一种临时的数据库对象，可以指向存储在数据库表中的数据行指针，这里游标充当了指针的作用，我们可以通过操作游标来对数据行进行操作

MySQL中游标可以在存储过程和函数中使用

```mysql
#使用游标步骤
#游标必须在声明处理程序之前被声明，并且变量和条件还必须在声明游标或处理程序之前被声明

#1.声明游标
#MySQL、SQL server、DB2、MariDB
DECLARE cursor_name CURSOR FOR select_statement;

#ORACLE、PostgreSQL
DECLARE cursor_name CURSOR IS select_statement;

#2.打开游标
#当我们定义好游标之后，如果想要使用游标，必须先打开游标，打开游标的时候SELECT语句的查询结果集就会送到游标工作区，为后面游标的逐条读取结果集中的记录做准备
OPEN cursor_name

#3.使用游标
#这句的作用是使用cursor_name这个游标来读取当前行，并且将数据保存到var_name这个变量中，游标指针指到下一行，如果游标读取的数据行有多个列名，则在INTO关键字后面赋值给多个变量名即可
#注意，游标的查询结果集中的字段数，必须跟INTO后面的变量数一致
FETCH cursor_name INTO var_name [, var_name]...

#4.关闭游标
#游标会占用系统资源，如果不及时关闭，游标会一直保持到存储过程结束，影响系统运行的效率
CLOSE cursor_name

#例1
DELIMITER //
CREATE PROCEDURE get_count_by_limit_total_salary(IN limit_total_salary DOUBLE, OUT total_count INT)
BEGIN
	DECLARE sum_sal DOUBLE DEFAULT 0.0;
	DECLARE emp_sal DOUBLE;
	DECLARE emp_count INT DEFAULT 0;
	
	DECLARE emp_cursor CURSOR FOR SELECT salary FROM employees ORDER BY salary DESC;
	OPEN emp_cursor;
	REPEAT
		FETCH emp_cursor INTO emp_sal;
		SET sum_sal = sum_sal + emp_sal;
		SET emp_count = emp_count + 1;
		UNTIL sum_salary >= limit_total_salary
	END REPEAT;
	
	SET total_count = emp_count;
	CLOSE emp_cursor;
END ;
DELIMITER ;
```

### 触发器

触发器是由事件来触发某个操作，这些事件包括INSERT、UPDATE、DELETE事件，所谓事件就是指用户的动作或者触发某项行为，如果定义了触发程序，当数据库执行这些语句的时候，就相当于事件发生了，就会自动激发触发器执行相应的操作。

当对数据表中的数据执行插入、更新和删除操作，需要自动执行一些数据库逻辑时，可以使用触发器来实现

```mysql
#创建触发器
CREATE TRIGGER 触发器名称
{BEFORE|AFTER} {INSERT|UPDATE|DELETE} ON 表名
FOR EACH ROW
触发器执行的语句块；

#查看触发器
#方式1：查看数据库中所有的触发器
SHOW TRIGGERS\G;
#方式2：查看数据库中某个触发器
SHOW CREATE TRIGGER 触发器名
#方式3：从系统库information_schema的TRIGGERS表中查询触发器
SELECT * FROM information_schema.TRIGGRES;

#删除触发器
DROP TRIGGER IF EXISTS 触发器名称;
```

注意，如果在子表中定义了外键约束，并且外键指定了 ON UPDATE/DELETE CASCADE/SET NULL，此时修改父表被引用的键值或删除父表被引用的记录行时，也会引起子表的修改和删除，此时基于子表的UPDATE和DELETE语句定义的触发器并不会被激活

### MySQL高级特性

my.ini/my.cnf

```mysql
[server]
#设置字符集
character_set_server=utf8
#设置比较规则
collation_server=gbk_chinese_ci
#修改系统表空间
innodb_data_file_path=data1:512M;data2:512M:autoextend
#指定使用系统表空间还是独立表空间，0表示使用系统表空间，1代表使用独立表空间
innodb_file_per_table=0

[mysqld]
sql_mode=ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
```

### 字符集和数据目录

#### 字符集

+ character_set_server：服务器级别的字符集
+ character_set_database：当前数据库的字符集
+ character_set_client：服务器解码请求时请求使用的字符集
+ character_set_connection：服务器处理请求时会把请求字符串从character_set_client转为character_set_connection
+ character_set_results：服务器向客户端返回数据时使用的字符集

```mysql
#修改已创建数据库的字符集
alter database test1 character set 'utf8';

#修改已创建数据表的字符集
alter table t_emp convert to character set 'utf8';

#服务器级别
#设置字符集
character_set_server=utf8
#设置比较规则
collation_server=gbk_chinese_ci

#数据库级别
#创建数据库时指定字符集和比较规则
CREATE DATABASE 数据库名
[[DEFAULT] CHARACTER SET 字符集名称]
[[DEFAULT] COLLATE 比较规则名称]
#修改数据库时指定字符集和比较规则
ALTER DATABASE 数据库名
[[DEFAULT] CHARACTER SET 字符集名称]
[[DEFAULT] COLLATE 比较规则名称]
#创建和修改数据库时没有指明字符集和比较规则，将使用服务器的字符集和比较规则

#表级别
#创建表时指定
CREATE TABLE 表名
[[DEFAULT] CHARACTER SET 字符集名称]
[COLLATE 比较规则名称]
#修改表时指定
ALTER TABLE 表名
[[DEFAULT] CHARACTER SET 字符集名称]
[COLLATE 比较规则名称]
#创建和修改表时没有指明字符集和比较规则，将使用数据库的字符集和比较规则

#列级别
#创建表时指定
CREATE TABLE 表名(
	列名 字符串类型 [CHARACTER SET 字符集名称] [COLLATE 比较规则名称],
);
#修改表时指定
ALTER TABLE 表名 MODIFY 列名 字符串类型 [CHARACTER SET 字符集名称] [COLLATE 比较规则名称];

#查看mysql支持的字符集和比较规则
show charset;
或
show character set;

SET NAMES 字符集名;
相当于
SET character_set_client = 字符集名;
SET character_set_connection = 字符集名;
SET character_set_results = 字符集名;
```

**utf8与utf8mb4**

utf8字符集表示一个字符需要使用1~4个字节，但是我们常用的一些字符使用1~3个字节就可以表示了。而字符集表示一个字符所用的最大字节长度，在某些方面会影响系统的存储和性能，所以设计MySQL的设计者偷偷的定义了两个概念：

+ utf8mb3：阉割过的utf8字符集，只是用1~3个字节表示字符
+ utf8mb4：正宗的utf8字符集，使用1~4个字节表示字符

在MySQL中utf8是utf8mb3的别名

**比较规则**

show charset中Default collation列表示这种字符集中默认的比较规则，里面包含该比较规则主要作用域哪种语言，比如utf8_polish_ci表示以波兰语的规则比较，utf8_spanish_ci是以西班牙语的规则比较，utf8_general_ci是一种通用的比较规则

比较规则后缀表示是否区分语言中的重音、大小写。

| 后缀 | 英文释义          | 描述             |
| ---- | ----------------- | ---------------- |
| _ai  | accent insenstive | 不区分重音       |
| _as  | accent senstive   | 区分重音         |
| _ci  | case insenstive   | 不区分大小写     |
| _cs  | case senstive     | 区分大小写       |
| _bin | binary            | 以二进制方式比较 |

show charset最后一列Maxlen表示该种字符集表示一个字符最多需要几个字节

utf8_unicode_ci和utf8_general_ci对中英文来说没有实质的差别

utf8_general_ci校对速度快，但准确度稍差

utf8_unicode_ci准确度高，但校对速度稍慢

一般情况，用utf8_general_ci就够了，但如果你的应用有德语、法语或者俄语，请一定使用utf8_unicode_ci

**sql_mode**

sql_mode会影响MySQL支持的SQL语法以及它执行的数据验证检查。

+ MySQL5.6的mode默认值为空（即：NO_ENGINE_SUBSTITUTION），其实表示的是一个空值，相当于没有什么模式设置，可以理解为宽松模式，在这种设置下可以允许一些非法操作的，比如允许一些非法数据的插入
+ MySQL5.7的mode是STRICT_TRANS_TABLES，也就是严格模式，用于进行数据的严格校验，错误数据不能插入，报error，并且事物回滚

```mysql
#查看当前的sql_mode
select @@session.sql_mode
select @@global.sql_mode
或者
show variables like 'sql_mode';
```

**默认数据库**

+ mysql：系统自带的核心数据库，它存储了MySQL的用户账户和权限信息，一些存储过程、事件的定义信息，一些运行过程中产生的日志信息，一些帮助信息以及时区信息等
+ information_schema：保存着MySQL服务器维护的所有其他数据库的信息，比如有哪些表、哪些视图、哪些触发器、哪些列，哪些索引。这些信息并不是真实的用户数据，而是一些描述性的信息，也称之为元数据。在数据库information_schema中提供了一些以innodb_sys开头的表，用于表示内部系统表
+ performance_schema：保存MySQL服务器运行过程中的一些状态信息，可以用来监控MySQL服务的各类性能指标，包括统计最近执行了哪些语句，在执行过程的每个阶段都花费了多长时间，内存的使用情况等信息
+ sys：通过视图的形式把information_schema和performance_schema结合起来，帮助系统管理员和开发人员监控MySQL的技术性能

#### 数据库在文件系统中的表示

**InnoDB存储引擎模式**

使用CREATE DATABASE 数据库名创建数据库时：

1. 在数据目录datadir下下创建一个和数据库名同名的子目录
2. 在与该数据库同名的子目录下创建一个db.opt文件（仅限MySQL5.7及之前版本），这个文件中包含了该数据库的各种属性，比如该数据库的字符集和比较规则

**系统表空间**

默认情况下，InnoDB会在数据目录下创建一个名为ibdata1、大小为12M的文件，这个文件就是对应的系统表空间在文件系统上的表示，这个文件是自扩展文件，当不够时他会增大文件大小

如果想让系统表空间对应文件系统上多个实际文件，或者仅仅觉得原来的ibdata1这个文件名难听，可以修改

```mysql
[server]
#修改系统表空间
innodb_data_file_path=data1:512M;data2:512M:autoextend
```

autoexrend表明这两个文件如果不够用会自动扩展data2文件的大小

在一个MySQL服务器中，系统表空间只有一份。从MySQL5.5.7到MySQL5.6.6之间的各个版本中，我们表中的数据都会被默认存储到这个系统表空间

**独立表空间**

MySQL5.6.6以及之后的版本，InnoDB不会默认的把各个表的数据存储到系统表空间中，而是为每一个表建立一个独立表空间，文件名为：表名.ibd，ibd存储数据和索引

表名.frm文件存储表结构

```mysql
[server]
#指定使用系统表空间还是独立表空间，0表示使用系统表空间，1代表使用独立表空间
innodb_file_per_table=0
```

.frm在MySQL8中不存在了。那去哪里了呢？

这就需要解析ibd文件。Oracle官方将frm文件的信息及更多信息移动到叫做序列化字典信息（Serialized Dictionary Information，SDI），SDI被写在ibd文件内部。为了从IBD文件中提取SDI信息，Oracle提供了一个程序ibd2sdi，MySQL8自带，只要配好环境变量就能到处用。

1. 查看表结构

   到存储ibd文件的目录下，执行下面命令

   ```mysql
   ibd2sdi --dump-file=student.txt student.ibd
   ```

**MyISAM存储引擎模式**

MyISAM数据和索引分开存储，创建表时会为表创建.frm（存储表结构）、.MYD（存储数据）、.MYI（存储索引）三个文件

MySQL8中frm为sdi文件

**视图在文件系统中从表示**

MySQL中的视图是虚拟表，所以存储视图的时候不需要存储真实数据，只需要把它的结构存储起来就行了。和表一样，描述视图结构的文件也会被存储到所属数据库对应的子目录下边，只会存储一个视图名.frm的文件

**其他文件**

+ 服务器进程文件
+ 服务器日志文件
+ 默认/自动生成的SSL和RSA证书和密钥文件

### 用户与权限管理

#### 用户管理

```mysql
#登录MySQL服务器
mysql -h hostname|hostIP -P port -u username -p DatabaseName -e "SQL语句"
```

+ -h参数后面接主机名或者主机IP
+ -P参数后面接MySQL服务的端口
+ -u参数后面接用户名
+ -p参数会提示输入密码
+ DatabaseName参数指明登录到哪一个数据库
+ -e参数后面可以直接加SQL语句，登录后即可执行这个SQL语句，然后退出MySQL服务器

**创建用户**

使用CREATE USER语句创建新用户，创建新用户时，必须拥有CREATE USER权限，每添加也给用户就会在mysql.user表中添加一条记录，但是新创建的用户没有任何权限

```mysql
CREATE USER 用户名 [IDENTIFIED BY '密码'][,用户名 [IDENTIFIED BY '密码']];

#例
create user 'zhang3'@'localhost' identified by '123456';
```

+ 用户名参数表示新建用户的账户，由用户（user）和主机名（host）构成
+ []表示可选，可以指定用户登录时需要密码验证，也可以不指定密码验证，这样用户可以直接登录

**修改用户**

```mysql
UPDATE mysql.user SET USER='li4' WHERE USER='wang5';

FLUSH PRIVILEGES;
```

**删除用户**

```mysql
#方式1,user参数由用户名和主机名组成
DROP USER user[,user]...;

#方式2,不推荐，有残留信息
DELETE FROM mysql.user WHERE host='localhost' AND user='Emily';
```

**设置当前用户密码**

适用于root用户修改自己的密码，以及普通用户登录后修改自己的密码

root用户有很高的权限，因此必须保证root用户的密码安全。root用户可以通过多种方式修改密码，使用ALTER USER修改用户密码是MySQL官方推荐的方式，此外也可以通过SET语句修改密码，由于MySQL8中已经移除了PASSWORD()函数，因此不再使用UPDATE语句直接操作用户表修改密码

```mysql
#旧的写法
SET PASSWORD = PASSWORD('123456');

#推荐写法
#方式1,修改当前登录用户密码
ALTER USER USER() IDENTIFIED BY 'new_password';

#方式2
SET PASSWORD = 'new_password';
```

**修改其他用户的密码**

```mysql
#方式1
ALTER USER user [IDENTIFIED BY '新密码'][,user [IDENTIFIED BY '新密码']]...;

#
ALTER USER 'kangshifu'@'localhost' IDENTIFIED BY '123456';

#方式2
SET PASSWORD FOR 'username'@'hostname'='new_password';

#方式3
UPDATE mysql.user SET authentication_string=PASSWORD('123456') 
WHERE USER = 'username' AND HOST = 'hostname'; 
```

**MySQL8密码管理**

MySQL中记录使用过的历史密码，目前包含如下密码管理功能：

1. 密码过期：要求定期修改密码

   MySQL中，可以手动设置账号密码过期，也可建立一个自动密码过期策略

   过期策略可以是全局的，也可以为每个账号设置单独的过期策略

   ```mysql
   #手动设置账号密码过期
   ALTER USER user PASSWORD EXPIRE;
   
   #手动设置指定时间过期方式1：全局
   #方式1，default_password_lifetime默认是0，表示禁用自动密码过期
   SET PERSIST default_password_lifetime = 180; #设置密码每隔180天过期
   
   #方式2：配置文件
   [mysqld]
   default_password_lifetime=180
   
   #手动设置指定时间过期方式2：单独设置
   CREATE USER 'kangshifu'@'localhost' PASSWORD EXPIRE INTERVAL 90 DAY;
   ALTER USER 'kangshifu'@'localhost' PASSWORD EXPIRE INTERVAL 90 DAY;
   
   #设置密码永不过期
   CREATE USER 'kangshifu'@'localhost' PASSWORD EXPIRE NEVER;
   ALTER USER 'kangshifu'@'localhost' PASSWORD EXPIRE NEVER;
   
   #延用全局密码过期策略
   CREATE USER 'kangshifu'@'localhost' PASSWORD EXPIRE DEFAULT;
   ALTER USER 'kangshifu'@'localhost' PASSWORD EXPIRE DEFAULT;
   ```

2. 密码重用限制：不允许使用旧密码

   MySQL限制使用已用过的密码。重用限制策略基于密码更改的数量和使用的时间。重用策略可以是全局的，也可以为每个账号设置单独的策略

   ```mysql
   #手动设置密码重用方式1：全局
   #方式1
   SET PERSIST password_history = 6 #设置不能选择最近使用过的6个密码
   SET PERSIST password_reuse_interval = 365; #设置不能选择最近一年内的密码
   
   #方式2，配置文件
   [mysqld]
   password_history=6
   password_reuse_interval=365
   
   #手动设置密码重用方式2：单独设置
   CREATE USER 'kangshifu'@'localhost' PASSWORD HISTORY 5;
   ALTER USER 'kangshifu'@'localhost' PASSWORD HISTORY 5;
   
   CREATE USER 'kangshifu'@'localhost' PASSWORD REUSE INTERVAL 365 DAY;
   ALTER USER 'kangshifu'@'localhost' PASSWORD REUSE INTERVAL 365 DAY;
   
   #既不能使用最近5个密码，也不能使用365天内的密码
   CREATE USER 'kangshifu'@'localhost' 
   PASSWORD HISTORY 5
   PASSWORD REUSE INTERVAL 365 DAY;
   
   ALTER USER 'kangshifu'@'localhost' 
   PASSWORD HISTORY 5
   PASSWORD REUSE INTERVAL 365 DAY;
   
   #沿用全局策略
   CREATE USER 'kangshifu'@'localhost' 
   PASSWORD HISTORY DEFAULT
   PASSWORD REUSE INTERVAL DEFAULT;
   
   ALTER USER 'kangshifu'@'localhost' 
   PASSWORD HISTORY DEFAULT
   PASSWORD REUSE INTERVAL DEFAULT;
   ```

3. 密码强度评估：要求使用高强度的密码

#### 权限管理

```mysql
#查看总的权限表
show privileges;

#给用户授权的方式有两种：分别通过把角色赋予用户给用户授权和直接给用户授权
#授权命令，如果没有发现该用户，则会直接新建一个用户
GRANT 权限1,权限2... ON 数据库名称.表名称 TO 用户名@用户地址 [IDENTIFIED BY '密码口令'];

#授予所有权限，与root用户的区别是没有GRANT权限,如果要赋予GRANT权限，添加参数WITH GRANT OPTION
GRANT ALL PRIVILEGES ON *.* TO 'li4'@'%';

#查看当前用户权限
SHOW GRANTS;
或
SHOW GRANTS FOR CURRENT_USER;
或
SHOW GRANTS FOR CURRENT_USER();

#查看某用户的全局权限
SHOW GRANTS FOR 'user'@'主机地址';

#收回权限
REVOKE 权限1,权限2... ON 数据库名称.表名称 FROM 用户名@用户地址;

#收回所有权限
REVOKE ALL PRIVILEGES ON *.* FROM 'li4'@'%';
```

#### 权限表

MySQL通过权限表来控制用户对数据库的访问，权限表放在mysql数据库中。MySQL数据库系统会根据这些表的内容为每个用户赋予相应的权限。这些权限表中最重要的是user表，db表。除此之外，还有table_priv表、column_priv表和proc_priv表，在MySQL启动时，服务器将这些数据库中权限信息的内容读入内存

#### 访问控制

1. 连接核实阶段

   当用户试图连接MySQL服务器时，服务器基于用户的身份以及用户是否能提供正确的密码验证身份来确定接受或者拒绝连接。即MySQL服务器收到用户请求后，会使用user表中的host、user、authentication_string 这3个字段匹配客户端提供信息，如果连接核实没有通过，服务就完全拒绝访问

2. 请求核实阶段

   一旦建立了连接，服务器就进入了访问控制的阶段2，也就是请求核实阶段

   ![](/img/mysql_1.png)

#### 角色管理

MySQL8.0引入的新功能

```mysql
#创建角色
CREATE ROLE 'role_name'[@'host_name'] [,'rolename'[@'host_name']]...

#给角色赋予权限,privileges代表权限的名称，可用show privileges查看
GRANT privileges ON db.table_name TO 'role_name'[@'host_name'];

#查看角色权限
SHOW GRANTS FOR 'role_name'[@'host_name'];

#回收角色的权限
REVOKE privileges ON db.table_name FROM 'role_name'[@'host_name'];

#删除角色
DROP ROLE role [,role2]...

#给用户赋予角色
GRANT role [,role2,...] TO user [,user2,...];

#查看当前用户角色
SHOW CURRENT_ROLE();

#激活角色，MySQL中创建了角色之后，默认都是没有被激活，必须手动激活
#方式1，使用set default role命令激活角色
SET DEFAULT ROLE ALL TO 'user_name'[@'host_name'] [,'user_name'[@'host_name']];
#例
SET DEFAULT ROLE 'manager'@'localhost' TO 'kangshifu'@'localhost';

#方式2，将activate_all_roles_on_login设置为ON
SET GLOBAL activate_all_roles_on_login=ON

#撤销用户的角色
REVOKE role FROM user;

#设置强制角色（mandatory role）
#强制角色是给每个创建账户的默认角色，不需要手动设置。强制角色无法被REVOKE或者DROP
#方式1：服务启动前设置
[mysqld]
mandatory_roles='role1,role2@localhost'

#方式2：运行时设置
SET PERSIST mandatory_roles='role1,role2@localhost'; #系统重启后有效
SET GLOBAL mandatory_roles='role1,role2@localhost'; #系统重启后失效
```

#### 配置文件

与在命令行中指定启动选项不同的是，配置文件中的启动选项被分为若干个组，每个组有一个组名，用括号[]括起来

```properties
[server]
...
[mysqld]
...
[mysqld_safe]
...
[client]
...
[mysql]
...
[mysqladmin]
...
```

配置文件中不同的选项组是给不同的启动命令使用的，不过有两个选项组比较特别：

+ [server]组下边的启动选项将作用于所有的服务器程序
+ [client]组下边的启动选项将作用于所有的客户端程序

下面是启动命令能读取的选项组有哪些：

| 启动命令     | 类别       | 能读取的组                         |
| ------------ | ---------- | ---------------------------------- |
| mysqld       | 启动服务器 | [mysqld]、[server]                 |
| mysqld_safe  | 启动服务器 | [mysqld]、[server]、[mysqld_safe]  |
| mysql.server | 启动服务器 | [mysqld]、[server]、[mysql.server] |
| mysql        | 启动客户端 | [mysql]、[client]                  |
| mysqladmin   | 启动客户端 | [mysqladmin]、[client]             |
| mysqldump    | 启动客户端 | [mysqldump]、[client]              |

**特定MySQL版本的专用选项组**

可以在选项组的名称后加上特定的MySQL版本号，比如对[mysqld]选项组来说，我们可以定义一个[mysqld-5.7]的选项组，它的含义和[mysqld]一样，只不过只有版本号为5.7的mysqld程序才能使用这个选项组中的选项

**同一个配置文件中多个组的优先级**

以最后一个出现的组中的启动选项为准

**命令行和配置文件中启动选项的区别**

如果同一个启动项既出现在命令行中，又出现在配置文件中，那么以命令行中的启动项为准

### 逻辑架构

#### 逻辑架构剖析

MySQL是典型的C/S架构，服务器端程序使用的是mysqld

![](/img/mysql_2.png)

![](/img/mysql_3.png)

1. **第一层：连接层**

   系统访问MySQL服务器前，第一件事就是建立TCP连接，经过三次握手建立连接成功后，MySQL服务器对TCP传输过来的账号密码做身份验证、权限获取

   多个系统可以和MySQL服务器建立连接，每个系统建立的连接肯定不止一个。所以为了 解决TCP无限创建与TCP频繁创建销毁带来的资源耗尽、性能下降问题，MySQL服务器有专门的TCP连接池限制连接数，采用长连接模式复用TCP连接，来解决上述问题

   TCP接收到请求后，必须要分配给一个线程专门与这个客户端的交互，所以还会有个线程池，去走后面的流程，每一个连接从线程池中获取线程，省去了创建和销毁线程的开销

   连接管理的职责是负责认证、管理连接、获取权限信息

2. **第二层：服务层**

   ​	第二层架构主要完成大多数的核心服务功能，如SQL接口，并完成缓存的查询，SQL的分析优化及部分内置函数的执行，所有跨存储引擎的功能也在这一层实现，如过程、函数等

   在该层，服务器会解析查询并创建相应的内部解析树，并对其完成相应的优化：如确定查询表的顺序，是否利用索引等，最后生成相应的执行操作

   如果是SELECT操作，服务器还会查询内部的缓存。

   + SQL Interface：SQL接口
     + 接收用户的SQL命令，并且返回用户需要查询的结果
     + MySQL支持DML、DDL、存储过程、视图、触发器、自定义函数等多种SQL语言接口
   + Parser：解析器
     + 在解析器中对SQL语句进行语法分析、语义分析。将SQL语句分解成数据结构，并将这个结构传递到后续步骤，以后SQL语句的传递和处理就是基于这个结构的，如果在分解构成中遇到错误，那么就说明这个SQL语句是不合理的
     + 在SQL命令传递到解析器的时候会被解析器验证和解析，并为其创建语法树，并根据数据字典丰富查询语法树，会验证该客户端是否具有执行该查询的权限。创建好语法树后，MySQL还会对SQL查询进行语法上的优化，进行查询重写
   + Optimizer：查询优化器
     + SQL语句在语法解析之后、查询之前会使用查询优化器确定SQL语句的执行路径，生成一个执行计划
     + 这个执行计划表明应该使用哪些索引进行查询，表之间的连接顺序如何，最后会按照执行计划中的步骤调用存储引擎提供的方法来真正的执行查询，并将查询结果返回用户
     + 它使用选取-投影-连接策略进行查询。例如：SELECT id,name FROM student WHERE gender = '女'；这个SELECT查询先根据WHERE语句进行选取，而不是将表全部查询出来以后再进行gender过滤。这个SELECT查询先根据id,name进行属性投影，而不是将属性全部取出以后再进行过滤，将这两个查询条件连接起来生成最终查询结果
   + Caches & Buffers：查询缓存组件
     + MySQL内部维持着一些Cache和Buffer，比如Query Cache用来缓存一条SELECT语句的执行结果，如果能够在其中找到对应的查询结果，那么就不必再进行查询解析、优化和执行的整个过程了，直接将结果返回客户端
     + 这个缓存机制是由一系列小缓存组成的。比如表缓存、记录缓存，key缓存、权限缓存等
     + 这个查询缓存可以在不同客户端之间共享
     + 从MySQL5.7.20开始，不推荐使用查询缓存，并在MySQL8.0中删除

3. **第三层：引擎层**

   与其他数据库相比，MySQL有点与众不同，它的架构可以在多种不同的场景中应用并发挥良好的作用，主要体现在存储引擎的架构上，插件式的存储引擎架构将查询处理和其他的系统任务以及数据的存储提取相分离。这种架构可以根据业务的需求和实际需要选择合适的存储引擎。提示开源的MySQL还允许开发人员设置自己的存储引擎。

   这种高效的模块化架构为那些希望专门针对特定应用程序需求（例如数据仓库、事务处理或高可用性情况）的人提供了巨大的好处，同时享受使用一组独立于任何接口和服务的优势存储引擎。

   插件式存储引擎层（Storage Engines），真正的负责了MySQL中数据的存储和提取，对物理服务器级别维护的底层数据执行操作，服务器通过API与存储引擎进行通信。不同的存储引擎具有的功能不同，这样我们可以根据自己的实际需要进行选取

**存储层**

所有的数据，数据库、表的定义、表的每一行内容，索引，都是存在文件系统上，以文件的方式存在的，并完成与存储引擎的交互。当然有些存储引擎比如InnoDB，也支持不使用文件系统直接管理裸设备，但现代文件系统的实现使得这样做没有必要了。在文件系统之下，可以使用本地磁盘，可以使用DAS、NAS、SAN等各种存储系统

#### SQL执行流程

![](/img/mysql_4.png)

**MySQL8.0中SQL执行原理**

```mysql
#确认profiling是否开启
#了解查询语句底层执行的过程
select @@profiling
show variables like 'profiling';

#profiling=0代表关闭，需要打开
set profiling=1;

#查看profile
show profiles;
show profile for query 7;

#除了查看cpu、io阻塞等参数情况，还可以查询下列参数的利用情况
SHOW PROFILE [type [, type]...]
	[FOR QUERY n]
	[LIMIT row_count [OFFSET offset]]

type : {
	ALL    #显示所有参数的开销信息
   |BLOCK IO    #显示IO的相关开销
   |CONTEXT SWITCHES    #上下文切换开销
   |CPU    #显示CPU相关开销信息
   |IPC    #显示发送和接收相关开销信息
   |MEMORY    #显示内存相关开销信息
   |PAGE FAULTS    #显示页面错误相关开销信息
   |SOURCE    #显示和Source_function,Source_file,Source_line相关的开销信息
   |SWAPS    #显示交换次数相关的开销信息
}
```

#### 数据库缓冲池

减少与磁盘直接进行IO的时间

**缓冲池**

InnoDB缓冲池包含数据页、索引页、插入缓存、锁信息、自适应索引哈希、数据字典信息等

缓存原则：

位置 * 频次原则，首先位置决定效率，提供缓冲池就是为了在内存中可以直接访问数据。其次，频次决定优先级顺序。因为缓冲池的大小是有限的，会优先对使用频次高的热数据进行加载

缓冲池的预读特性：

读取数据的时候存在一个局部性原理，也就是说我们使用了一些数据，大概率还会使用它周围的一些数据，因此采用预读的机制提前加载，可以减少未来可能的磁盘IO操作

**缓冲池如何读取数据**

![](/img/mysql_5.png)

如果我们执行SQL语句的时候更新了缓冲池中的数据，那么这些数据会马上同步到磁盘上吗

实际上，当我们对数据库中的记录进行修改的时候，首先会修改缓冲池中页里面的记录信息，然后数据库会以一定的频率刷新到磁盘上。注意并不是每次发生更新操作都会立刻进行磁盘回写。缓冲池会采用一种叫做checkpoint的机制将数据回写到磁盘上，这样做的好处就是提升了数据库的整体性能

比如，当缓冲池不够用时，需要释放掉一些不常用的页，此时就可以强行采用checkpoint的方式，将不用的脏页回写到磁盘上，然后再从缓冲池中将这些页释放掉。这里脏页指的是缓冲池中被修改过的页，与磁盘上的数据页不一致

**查看/设置缓冲池大小**

MyISAM存储引擎只缓存索引，不缓存数据，对应的键缓存参数为：key_buffer_size

InnoDB存储引擎可以通过查看innodb_buffer_pool_size变量来查看缓冲池的大小，默认128M

```mysql
#设置缓冲池大小为256M
set global innodb_buffer_pool_size=268435456;
或
[server]
innodb_buffer_pool_size=268435456
```

**多个buffer pool实例**

buffer pool本质是InnoDB向操作系统申请的一块连续的内存空间，在多线程环境下，访问buffer pool中的数据都需要加锁操作，在buffer pool特别大而且多线程并发访问特别高的情况下，单一的buffer pool可能会影响请求的处理速度。所以在buffer pool特别大的时候，可以拆分成若干个小的buffer pool，每个buffer pool称为一个实例，他们都是独立的，独立的去申请内存空间，独立的管理各种链表。所以在多线程并发访问时并不会相互影响，从而提高并发处理能力

InnoDB规定：当innodb_buffer_pool_size的值小于1G时候，设置多个实例是无效的

```mysql
#设置buffer pool实例
[server]
innodb_buffer_pool_instances=2
```

### 存储引擎

存储引擎就是表的类型

```mysql
#查看存储引擎
show engines;

#查看默认的存储引擎
show variables like '%storage_engine%'
或
SELECT @@default_storage_engine;

#修改默认的存储引擎
SET DEFAULT_STORAGE_ENGINE-MyISAM;
或修改配置文件
default_storage_engine=MyISAM

#创建表时指定存储引擎
CREATE TABLE 表名(
	建表语句;
) ENGINE = 存储引擎名称;

#修改表的存储引擎
ALTER TABLE 表名 ENGINE=存储引擎名称;
```

**InnoDB引擎：具备外键支持功能的事物存储引擎**

+ MySQL从3.23.34a开始就包含InnoDB存储引擎。大于等于5.5之后，默认采用InnoDB引擎。
+ InnoDB是MySQL的默认事务型引擎，他被设计用来处理大量的短期事务。可以确保事务的完整提交和回滚
+ 除了增加和查询外，还需要更新、删除操作，那么，应优先选择InnoDB存储引擎。
+ 除非有非常特别的原因需要使用其他的存储引擎，否则应该优先考虑InnoDB引擎
+ InnoDB是为处理巨大数据量的最大性能设计，在以前的版本中，字典数据以元数据文件、非事务表来存储。现在这些元数据文件被删除了。比如：.frm、.par、.trn、.isl、.db、.opt等都在MySQL8.0中不存在了
+ 对比MyISAM存储引擎，InnoDB写的处理效率差一些，并且会占用更多的磁盘空间以保存数据和索引
+ MyISAM只缓存索引，不缓存真实数据；InnoDB不仅缓存索引还要缓存真实数据，对内存要求较高，而且内存大小对性能有决定性的影响

**MyISAM引擎：主要的非事务处理存储引擎**

+ MyISAM提供了大量的特性，包括全文索引、压缩、空间函数等，但不支持事务、行级锁、外键，有一个毫无疑问的缺点就是崩溃后无法安全恢复
+ 5.5之前默认的存储引擎
+ 优势是访问的速度快，对事务完整性没有要求或者以SELECT、INSERT为主的应用
+ 针对数据统计有额外的常数存储。故而count(*)的查询效率很高
+ 应用场景：只读应用或者以读为主的业务

| 对比项         | MyISAM                                                   | InnoDB                                                       |
| -------------- | -------------------------------------------------------- | ------------------------------------------------------------ |
| 外键           | 不支持                                                   | 支持                                                         |
| 事务           | 不支持                                                   | 支持                                                         |
| 行表锁         | 表锁，即使操作一条记录也会锁住整个表，不适合高并发的操作 | 行锁，操作时只锁某一行，不对其他行有影响，适合高并发的操作   |
| 缓存           | 只缓存索引，不缓存真实数据                               | 不仅缓存索引还要缓存真实数据，对内存要求较高，而且内存大小对性能有决定性的影响 |
| 自带系统表使用 | Y                                                        | N                                                            |
| 关注点         | 性能：节省资源、消耗少、简单业务                         | 事务：并发写、事务、更大资源                                 |
| 默认安装       | Y                                                        | Y                                                            |
| 默认使用       | N                                                        | Y                                                            |

### 索引的数据结构

创建索引的目的就是为了减少磁盘I/O的次数

索引是在存储引擎中实现的，每种存储引擎的索引不一定完全相同

优点

+ 提高检索效率，降低I/O成本
+ 可以创建唯一索引，保证数据库表每一行数据的唯一性
+ 加速表和表之间的连接
+ 减少查询中分组和排序的时间，降低CPU的消耗

缺点

+ 创建和维护索引要耗费时间，并且随着数据量的增加，所耗费的时间也会增加
+ 索引需要占磁盘空间
+ 降低更新表的速度

![](/img/mysql_6.png)

Compact行格式

+ record_type：记录头信息的一项属性，表示记录的类型，0表示普通记录，2表示最小记录，3表示最大记录，1暂时还没用过
+ next_record：记录头信息的一项属性，表示下条地址相对于本条记录的地址偏移量
+ 各个列的值
+ 其他信息：除了上述3种信息以外的所有信息，包括其他隐藏列的值以及记录的额外信息

![](/img/mysql_7.png)

![](/img/mysql_8.png)

因为这些16KB的页在物理存储上是不连续的，所以如果想从这么多页中根据主键值快速定位某些记录所在的页，我们需要给他们做个目录，每个页对应一个目录项。每个目录项包括下边两个部分

+ 页的用户记录中最小的主键值，我们用key表示
+ 页号，用page_no表示

![](/img/mysql_9.png)

比如查找主键为20的记录，具体查找过程分为两步：

+ 先从目录项中根据二分法快速确定出主键值为20的记录在目录项3中因为（12<20<209），他对应的页是页9
+ 再根据前边说的在页中查找记录的方式去页9中定位具体的记录

#### InnoDB中索引方案

目录页和数据页差不多，只不过目录项中的两个列是主键和页号而已，InnoDB怎么区分一条记录是普通的用户记录还是目录项记录呢？使用记录头信息里的record_type属性，他的各个值代表如下：

+ 0：普通的用户记录
+ 1：目录项记录
+ 2：最小记录
+ 3：最大记录

![](/img/mysql_10.png)

多个目录项记录的页：

![](/img/mysql_11.png)

目录记录页的目录页：

![](/img/mysql_12.png)

这个数据结构，他的名称是B+树

#### 常见索引概念

**聚簇索引**

并不是一种单独的索引类型，而是一种数据存储方式（所有的用户记录都存储在了叶子节点），也就是所谓的索引即数据，数据即索引

特点：

1. 使用记录主键值的大小进行记录和页的排序，这包括三个方面的含义：
   + 页内的记录是按照主键的大小排序成一个单向链表
   + 各个存放目录项记录的页分为不同的层次，在同一层次中的页也是根据页中目录项记录的主键大小顺序排成一个双向链表
2. B+树的叶子节点存储的是完整的用户记录

我们把具有这两种特性的B+树称为聚簇索引，所有完整的用户记录都存放在这个聚簇索引的叶子节点处。这种聚簇索引并不需要我们在MySQL中显式的使用INDEX语句去创建，InnoDB存储引擎会自动的为我们创建聚簇索引

优点

+ 数据访问快，因为聚簇索引将索引和数据保存在同一个B+树中，因此从聚簇索引中获取数据比非聚簇索引更快
+ 聚簇索引对于主键的排序查找和范围查找速度非常快
+ 按照聚簇索引排序顺序，查询显示一定范围数据的时候，由于数据都是紧密相连，数据库不用从多个数据块中提取数据，所以节省了大量的io操作

缺点

+ 插入速度严重依赖于插入顺序，按照主键的顺序插入是最快的方式，否则将出现页分裂，严重影响性能。因此，对于InnoDB表，我们一般都会定义一个自增的ID列为主键
+ 更新主键的代价很高，因为将会导致被更新的行移动。因此，对于InnoDB的表，我们一般定义主键为不可更新
+ 二级索引访问需要两次索引查找，第一次找到主键值，第二次根据主键值找到行数据

限制

+ 对于MySQL数据库目前只有InnoDB数据引擎支持聚簇索引，而MyISAM并不支持聚簇索引
+ 由于数据物理存储排序方式只能有一种，所以每个MySQL的表只能有一个聚簇索引。一般情况下就是该表的主键
+ 如果没有定义主键，InnoDB会选择非空的唯一索引代替。如果没有这样的索引，InnoDBh会隐式的定义一个主键来作为聚簇索引
+ 为了充分利用聚簇索引的聚簇特性，所以InnoDB表的主键列尽量选用有序的顺序id而不建议用无序的id，比如UUID,MD5,HASH,字符串列作为主键无法保证数据的顺序增长

**非聚簇索引（二级索引、辅助索引）**

聚簇索引只能在搜索条件是主键值时才能发挥作用。如果用其他的列作为搜索条件，可以多建几颗B+树，如下：

![](/img/mysql_13.png)

这个B+树与上边介绍的聚簇索引有几处不同：

+ B+树的叶子节点存储的并不是完整的用户记录，而只是c2列+主键这两个列的值
+ 目录项记录中不再是主键+页号的搭配，而变成了c2列+页号的搭配

回表

我们根据这个以c2列大小排序的B+树只能确定我们要查找记录的主键值，所以如果我们想根据c2列的值查找到完整的用户记录的话，仍需要到聚簇索引中再查一遍，这个过程称为回表

**联合索引**

多个列建立索引

![](/img/mysql_14.png)

#### InnoDB的B+树索引的注意事项

1. 根页面位置万年不动

   + 每当为某个表创建一个B+树索引（聚簇索引不是人为创建的，默认就有）的时候，就会为这个索引创建一个跟节点页面。最开始表中没有数据的时候，每个B+树索引对应的根节点中既没有用户记录，也没有目录项记录
   + 随后向表中插入用户记录时，先把用户记录存储到这个根节点中
   + 当根节点中的可用空间用完时继续插入记录，此时会将根节点中的所有记录复制到一个新分配的页，比如页a中，然后对这个新页进行页分裂的操作，得到另一个新页b。这时新插入的记录根据键值（也就是聚簇索引中的主键值，二级索引中对应的索引列的值）的大小就会被分配到页a或者页b中，而根节点便升级为存储目录项记录的页

   这个过程需要注意：一个B+树索引的根节点自诞生之日起，便不会再移动。这样只要我们对某个表建立一个索引，那么它的根节点的页号便会被记录到某个地方，然后凡是InnoDB存储引擎需要用到这个索引的时候，都会从那个固定的地方去出根节点的页号，从而来访问这个索引

2. 内节点中目录项记录的唯一性

   索引列值相同时，为了让新插入记录能找到自己在哪个页里，需要保证在B+树的同一层内节点的目录项记录除页号这个字段外是唯一的，所以二级索引的内节点的目录项记录的内容实际上是由三个部分构成的：

   + 索引列的值
   + 主键值
   + 页号

3. 一个页面最少存储2条记录

#### MyISAM中的索引方案

MyISAM引擎使用B+树作为索引结构，叶子节点的data域存放的是数据记录的地址，将索引和数据分开存储

**MyISAM索引原理**

+ 将表中的记录按照记录的插入顺序单独储存在一个文件中，称之为数据文件。这个文件不划分为若干个数据页，有多少记录就往这个文件中塞多少记录就成了。由于在插入数据的时候并没有按照主键大小排序，所以我们并不能在这些数据上使用二分法进行查找
+ 使用MyISAM存储引擎的表会把索引信息另外存储到一个称为索引文件的另一个文件中。MyISAM会单独为表的主键创建一个索引，只不过在索引的叶子节点存储的不是完整的用户记录，而是主键值+数据记录地址的组合

![](/img/mysql_15.png)

**MyISAM与InnoDB对比**

MyISAM索引方式都是非聚簇的，与InnoDB包含一个聚簇索引是不同的

+ 在InnoDB存储引擎中，我们只需要根据主键值对聚簇索引进行一次查找就能找到对应的记录，而在MyISAM中却需要进行一次回表操作，意味着MyISAM中建立的索引相当于全部是二级索引
+ InnoDB数据文件本身就是索引文件，而MyISAM索引文件和数据文件是分离的，索引文件仅保持数据记录的地址
+ InnoDB的非聚簇索引data域存储相应记录主键的值，而MyISAM索引记录的是地址，换句话说，InnoDB的所有非聚簇索引都引用主键作为data域
+ MyISAM的回表操作是非常快速的，因为拿着地址偏移量直接到文件中去取数据的，反观InnoDB是通过获取主键之后再去聚簇索引里找记录，虽然说也不慢，但还是比不上直接用地址去访问
+ InnoDB要求表必须要有主键（MyISAM可以没有）。如果没有显式指定，则MySQL系统会自动选择一个可以非空且唯一标识数据记录的列作为主键。如果不存在这种列，则MySQL自动为InnoDB表生成一个隐含字段作为主键，这个字段长度为6个字节，类型为长整型

#### 索引的代价

+ 空间上的代价

  每建立一个索引都要为他建立一个B+树，每一颗B+树的每一个节点都是一个数据页，一个页默认会占用16KB的存储空间，一颗很大的B+树由许多数据页组成，那就是很大的一片存储空间

+ 时间上的代价

  每次对表中的数据进行增删改操作时，都需要去修改各个B+树索引。B+树每层节点都是按照索引列的值从小到大的顺序排序而组成了双向链表。不论是叶子节点中的记录，还是内节点中的记录都是按照索引列的值从小到大的顺序而形成了一个单向链表。而增删改操作可能会对节点和记录的排序造成破坏，所以存储引擎需要额外的时间进行一些记录移位，页面分裂，页面回收等操作来维护好节点和记录的顺序。如果我们建了许多索引，每个索引对应的B+树都要进行相关的维护操作，会给性能拖后腿

#### MySQL数据结构选择的合理性

**Hash结构**

+ Hash索引仅能满足=、<>、和IN查询，如果进行范围查询，哈希表的索引，时间复杂度会退化为O(N)，而树型的有序特性，依然能保持O（logN）的高效率
+ Hash索引还有一个缺陷，数据的存储时没有顺序的，在ORDER BY的情况下，使用Hash索引还需要对数据重新排序
+ 对于联合索引的情况，Hash值是将联合索引键合并后一起来计算的，无法对单独的一个键或者几个索引键进行查询
+ 对于等值查询来说，通常Hash索引的效率更高，不过也存在一种情况，就是索引列的重复值如果很多，效率就会降低。这是因为遇到Hash冲突时，需要遍历桶中的行指针来进行比较，找到查询的关键字，非常耗时，所以，Hash索引通常不会用到重复值多的列上
+ MyISAM和InnoDB不支持Hash索引
+ InnoDB提供自适应Hash索引。如果某个数据经常被访问，当满足一定条件的时候，就会将这个数据页的地址存放到Hash表中，这样下次查询的时候，就可以直接找到这个页面的所在位置，这样让B+树也具备了Hash索引的优点

**二叉搜索树**

+ 一个节点只能有两个子节点
+ 左子节点<本节点，右子节点>=本节点

可能退化为链表

**AVL树**

它是一颗空树或它的左右两个子树的高度差的绝对值不能超过1，并且左右两个子树都是一颗平衡二叉树

需要把二叉树改成M叉树

**B-Tree**

Balance Tree，多路平衡查找树

![](/img/mysql_16.png)

每个磁盘块中包括了关键字和子节点的指针。如果一个磁盘块中包括了X个关键字，那么指针树就是X+1

关键字集合分布在整棵树中，即叶子节点和非叶子节点都存放数据

![](/img/mysql_17.png)

**B+Tree**

基于B树做出了改进，B+树更适合文件索引系统

B+树和B树的差异：

+ 有k个孩子的节点就有k个关键字，也就是孩子数量=关键字数，而B树中，孩子数量=关键字数+1
+ 非叶子节点的关键字也会同时存在在子节点中，并且是在字节点中所有关键字的最大（或最小）
+ 非叶子节点仅用于索引，不保存数据记录，跟记录有关的信息都放在叶子节点中，而B数，非叶子节点既保存索引，也保存数据记录
+ 所有关键字都在叶子节点出现，叶子节点构成一个有序链表，而且叶子节点本身按照关键字的大小从小到大顺序链接
+ B+数查询效率更稳定，查询效率更高，在查询范围上，B+数的效率也比B树高

### InnoDB数据存储结构

#### 数据库的存储结构：页

InnoDB将数据划分为若干个页，InnoDB中页的大小默认为16KB

以页作为磁盘和内存之间交互的基本单位，也就是一次最少从磁盘中读取16KB的内容到内存中，一次最少把内存中的16KB内容刷新到磁盘中。也就是说，在数据库，不论读一行还是读多行，都是将这些行所在的页进行加载，数据库管理存储空间的基本单位是页（Page），数据库I/O操作的最小单位是页

```mysql
#查看默认页大小
show variables like '%innodb_page_size%'
```

页可以不在物理结构上相连，只要通过双向链表相关联即可。每个数据页中的记录会按照主键值从小到大的顺序组成也给单向链表，每个数据页都会为存储在他里边的记录生成一个页目录，在通过主键查找某条记录的时候可以在页目录中使用二分法快速定位到对应的槽，然后再遍历该槽对应分组中的记录即可快速找到指定的记录

在数据库中，还存在着区（Extent）、段（Segment）和表空间（Tablespace）的概念。行、页、区、段、表空间的关系如下：

![](/img/mysql_18.png)

区（Extent）是比页大一级的存储结构，在InnoDB存储引擎中，一个区会分配64个连续的页，因为InnoDB中的页大小默认是16KB，所以一个区的大小是64*16KB=1MB

段（Segment）有一个或多个区组成，区在文件系统是一个连续分配饿空间（在InnoDB中是连续的64个页），不过在段中不要求区与区之间是相邻的。段是数据库中的分配单位，不同类型的数据库对象以不同的段形式存在。当我们创建数据表、索引的时候，就会创建对应的段，比如创建一张表时会创建一个表段，创建一个索引时会创建一个索引段

表空间（Tablespace）是一个逻辑容器，表空间存储的对象是段，在一个表空间中可以有一个或多个段，但是一个段只能属于一个表空间。数据库由一个或多个表空间组成，表空间从管理上可以划分为系统表空间、用户表空间、撤销表空间、临时表空间等

#### 页的内部结构

页如果按类型划分的话，常见的有数据页、系统页、Undo页和事务数据页等。数据页是我们最常用的页

数据页的16KB大小的存储空间被划为为七个部分，分别是文件头（File Header）、页头（Page Header）、最大最小记录（Infimum+supremum）、用户记录（User Records）、空闲空间（Free Space）、页目录（Page Directory）和文件尾（File Trailer）

![](/img/mysql_19.png)

| 名称             | 占用大小 | 说明                                 |
| ---------------- | -------- | ------------------------------------ |
| File Header      | 38字节   | 文件头，描述页的信息                 |
| Page Header      | 56字节   | 页头，页的状态信息                   |
| Infimum+supremum | 26字节   | 最大和最小记录，这是两个虚拟的行记录 |
| User Records     | 不确定   | 用户记录，存储行记录内容             |
| Free Space       | 不确定   | 空闲记录，页中还没有被使用的空间     |
| Page Directory   | 不确定   | 页目录，存储用户记录的相对位置       |
| File Trailer     | 8字节    | 文件尾，校验页是否完整               |

**File Header**

| 名称                             | 占用空间大小 | 描述                                                         |
| -------------------------------- | ------------ | ------------------------------------------------------------ |
| FIL_PAGE_SPACE_OR_CHKSUM         | 4字节        | 页的校验和（checksum值）                                     |
| FIL_PAGE_OFFSET                  | 4字节        | 页号                                                         |
| FIL_PAGE_PREV                    | 4字节        | 上一个页的页号                                               |
| FIL_PAGE_NEXT                    | 4字节        | 下一个页的页号                                               |
| FIL_PAGE_LSN                     | 8字节        | 页面最后被修改时对应的日志序列位置（Log Sequence Number）    |
| FIL_PAGE_TYPE                    | 2字节        | 该页的类型                                                   |
| FIL_PAGE_FILE_FLUSH_LSN          | 8字节        | 仅在系统表空间的一个页中定义，代表文件至少被刷新到了对应的LSN值 |
| FIL_PAGE_ARCH_LOG_NO_OR_SPACE_ID | 4字节        | 页属于哪个表空间                                             |

FIL_PAGE_TYPE：

| 类型名称                | 十六进制 | 描述                           |
| ----------------------- | -------- | ------------------------------ |
| FIL_PAGE_TYPE_ALLOCATED | 0x0000   | 最新分配，还没使用             |
| FIL_PAGE_UNDO_LOG       | 0x0002   | Undo日志页                     |
| FIL_PAGE_INODE          | 0x0003   | 段信息节点                     |
| FIL_PAGE_IBUF_FREE_LIST | 0x0004   | Insert Buffer空闲列表          |
| FIL_PAGE_IBUF_BITMAP    | 0x0005   | Insert Buffer位图              |
| FIL_PAGE_TYPE_SYS       | 0x0006   | 系统页                         |
| FIL_PAGE_TYPE_TRX_SYS   | 0x0007   | 事务系统数据                   |
| FIL_PAGE_TYPE_FSP_HDR   | 0x0008   | 表空间头部信息                 |
| FIL_PAGE_TYPE_XDES      | 0x0009   | 扩展描述页                     |
| FIL_PAGE_TYPE_BLOB      | 0x000A   | 溢出页                         |
| FIL_PAGE_INDEX          | 0x45BF   | 索引页，也就是我们所说的数据页 |

FIL_PAGE_SPACE_OR_CHECKSUM

对于一个很长的字符串来说，我们会通过某种算法来计算一个比较短的值来代表这个很长的字节串，这个比较短的值就称为**校验和**

比较两个很长的字节串之前，先比较这两个长字节串的校验和，如果校验和都不一样，则两个长字节串肯定是不同的，所以省去了直接比较两个比较长的字节串的时间损耗

**校验和作用**

InnoDB存储引擎以页为单位把数据加载到内存中处理，如果该页中的数据在内存中被修改了，那么在修改后的某个时间需要把数据同步到磁盘中。但是在同步了一半的时候断电了，造成了该页传输的不完整

为了检测一个页是否完整，这时可以通过文件尾的校验和与文件头的校验和做对比，如果两个值不相等，则证明页的传输有问题，需要重新进行传输，否则认为页的传输已经完成

具体的：

每当一个页面在内存中修改了，在同步之前就要把他的校验和算出来，因为File Header在页面的前边，所以校验和会被首先同步到磁盘，当完全写完时，校验和也会被写到页的尾部，如果完全同步成功，则页的首部和尾部的校验和应该是一致的。如果写了一半断电了，那么在File Header中的校验和就代表着已经修改过的页，而在File Trailer中的校验和代表着原先的页，二者不同则意味着同步中间出了错。这里校验方式就是采用Hash算法进行校验

**File Trailer**

+ 前4个字节代表页的校验和
+ 后4个字节代表页面最后修改时对应的日志序列位置（LSN）

![](/img/mysql_20.png)

**Free Space**

我们自己存储的记录会按照指定的行格式存储到User Records部分。但是一开始生成页的时候，其实并没有User Records这个部分，每当我们插入一条记录，都会从Free Space部分，也就是尚未使用的存储空间中申请一个记录大小的空间划分到User Records部分，当Free Space部分的空间全部被User Records部分替代掉之后，也就意味着这个页用完了，如果还有新的记录插入的话，就需要去申请新的页了

**User Records**

User Records中的这些记录按照指定的行格式一条一条摆在User Records部分，相互之间形成单链表

**Infimum+supremum**

InnoDB规定的最小记录和最大记录的构造十分简单，都是由5字节大小的记录头信息和8字节大小的一个固定的部分组成的，如图:

![](/img/mysql_22.png)

这两条记录不是我们自己定义的记录，所以他们不放在页的User Records部分

![](/img/mysql_23.png)

**Page Directory**

在页中，记录是以单向链表的形式进行存储的。单向链表的特点就是插入、删除非常方便，但是检索效率不高，最差的情况下需要遍历链表上的所有节点才能完成检索。因此在页结构中专门设计了页目录这个模块，专门给记录做一个目录，通过二分查找法的方式进行检索，提升效率

1. 页目录将所有的记录分成几个组，这些记录包括最小记录和最大记录，但不包括标记为已删除的记录

2. 第1组，也就是最小记录所在的分组只有1条记录，最后一组，就是最大记录所在的分组，会有1-8条记录，其余的组记录数量在4-8之间，这样做的好处是，除了第一组以外，其余组的记录会尽量平分

3. 在每个组中最后一条记录的头信息中会存储该组一共有多少条记录，作为n_owned字段
4. 页目录用来存储每组最后一条记录的地址偏移量，这些地址偏移量会按照先后顺序存储起来，每组的地址偏移量也被称为槽（slot），每个槽相当于指针指向了不同组的最后一个记录

**Page Header**

| 名称              | 占用空间大小 | 描述                                                         |
| ----------------- | ------------ | ------------------------------------------------------------ |
| PAGE_N_DIR_SLOTS  | 2字节        | 在页目录中的槽数量                                           |
| PAGE_HEAP_TOP     | 2字节        | 还未使用的空间最小地址，也就是说从该地址之后就是Free Space   |
| PAGE_N_HEAP       | 2字节        | 本页中的记录的数量（包括最大最小记录以及标记为删除的记录）   |
| PAGE_FREE         | 2字节        | 第一个已经标记为删除的记录地址（各个已删除的记录通过next_record也会组成一个单链表，这个单链表中的记录可以被重新利用） |
| PAGE_GARBAGE      | 2字节        | 已删除记录占用的字节数                                       |
| PAGE_LAST_INSERT  | 2字节        | 最后插入记录的位置                                           |
| PAGE_DIRECTION    | 2字节        | 记录插入的方向                                               |
| PAGE_N_DIRECTION  | 2字节        | 一个方向连续插入记录的数量                                   |
| PAGE_N_RECS       | 2字节        | 该页中记录的数量（不包括最小和最大记录以及被标记为删除的记录） |
| PAGE_MAX_TRX_ID   | 8字节        | 修改当前页的最大事务ID，该值仅在二级索引中定义               |
| PAGE_LEVEL        | 2字节        | 当前页在B+树中所处的层级                                     |
| PAGE_INDEX_ID     | 8字节        | 索引ID，表示当前页属于哪个索引                               |
| PAGE_BTR_SEG_LEAF | 10字节       | B+树叶子段的头部信息，仅在B+树的Root页定义                   |

#### 行格式

**指定行格式的语法**

```mysql
CREATE TABLE 表名(列的信息) ROW_FORMAT=行格式名称
ALTER TAVLE 表名 ROW_FORMAT=行格式名称
```

**compact行格式**

**变长字段长度列表**

MySQL支持一些变长的数据类型，比如VARCHAR(M)、VARBINARY(M)、TEXT类型、BLOB类型，这些数据类型修饰列称为变长字段，变长字段中存储多少字节的数据不是固定的，所以我们在存储真实数据的时候需要顺便把这些数据占用的字节数也存起来。在Compact行格式中，把所有变长字段的真实数据占用占用的字节长度都存放在记录的开头部位，从而形成一个变长字段长度列表

注意：这里面存储的变长长度和字段顺序是反过来的，比如两个varchar字段在表结构的顺序是a(10)、b(15)，那么在变长字段长度列表中存储的长度顺序就是15，10

**NULL值列表**

Compact行格式会把可以为NULL的列统一管理起来，存在一个标记为NULL值列表中。如果表中没有允许存储NULL的列，则NULL值列表也不存在了

为什么要定义NULL值列表

之所以要存储NULL是因为数据都是需要对齐的，如果没有标注出来NULL值的位置，就有可能在查询数据的时候出现混乱。如果使用一个特定的符号放到相应的数据位表示空值的话，虽然能达到效果，但是这样很浪费空间，所以直接就在行数据的头部开辟出一开空间专门用来记录该行数据哪些是非空数据，哪些是空数据，格式如下：

1. 二进制位的值为1时，代表该列的值为NULL
2. 二进制位的值为0时，代表该列的值不为NULL

**记录头信息**

![](/img/mysql_21.png)

| 名称         | 大小（单位：bit） | 描述                                                         |
| ------------ | ----------------- | ------------------------------------------------------------ |
| 预留位1      | 1                 | 没有使用                                                     |
| 预留位2      | 1                 | 没有使用                                                     |
| delete_mask  | 1                 | 标记该记录是否被删除                                         |
| min_rec_mask | 1                 | B+树的每层非叶子节点中最小记录都会添加该标记1                |
| n_owned      | 4                 | 表示当前记录拥有的记录数                                     |
| heap_no      | 13                | 表示当前记录在记录堆的位置信息                               |
| record_type  | 3                 | 表示当前记录的类型，0表示普通记录，1表示B+树非叶子节点记录，2表示最小记录，3表示最大记录 |
| next_record  | 16                | 表示下一条记录的相对位置（地址偏移量）                       |

delete_mask

+ 值为0：代表记录并没有被删除
+ 值为1：代表记录被删除掉了

被删除的记录为什么还在页中存储呢？

移除他们之后其他的记录在磁盘上需要重新排列，导致性能消耗。所以只是打一个删除标记，所有被删除掉的记录都会组成一个所谓的垃圾链表，在这个链表中的记录占用的空间称之为可重用空间，之后如果有新的记录插入到表中的话，可能把这些被删除的记录占用的存储空间覆盖掉

heap_no

MySQL会自动给每个页里加了两个记录，由于这两个记录并不是我们自己插入的，所以有时候也称为伪记录或者虚拟记录。这两个伪记录一个代表最小记录，一个代表最大记录。最小和最大记录的heap_no分别为0和1

n_owned

页目录中每个组中最后一条记录的头信息中会存储改组一共有多少条记录，作为n_owned字段

**记录的真实数据**

记录的真实数据除了我们自己定义的列的数据外，还会有三个隐藏列：

| 列名           | 是否必须 | 占用空间 | 描述                   |
| -------------- | -------- | -------- | ---------------------- |
| row_id         | 否       | 6字节    | 行ID，唯一标识一条记录 |
| transaction_id | 是       | 6字节    | 事务ID                 |
| roll_pointer   | 是       | 7字节    | 回滚指针               |

实际上这几个列的真正名称是：DB_ROW_ID、DB_TRX_ID、DB_ROLL_PTR

+ 一个表没有手动定义主键，则会选取一个Unique键作为主键，如果连Unique键都没有定义的话，则会为表默认添加一个名为row_id的隐藏列作为主键。所以row_id是在没有自定义主键以及Unique键的情况下才会存在的
+ 事务ID和回滚指针在MySQL事务日志中讲解

**Dynamic和Compressed行格式**

**行溢出**

InnoDB存储引擎可以将一条记录中的某些数据存储在真正的数据页面之外

一个页的大小一般是16KB也就是16384个字节，而一个varchar(M)类型的列最多可以存储65533（65535-2个字节的变长字段的长度[-1NULL值标识]）个字节，这样就可能出现一个页存放不了一条记录，这种现象称为行溢出

在Compact和Redundant行格式中，对于占用存储空间非常大的列，在记录的真实数据处只会存储该列的一部分数据，把剩余数据分散存储在几个其他的页中进行分页存储，然后记录的真实数据处用20个字节存储指向这些页的地址（当然20个字节中还包括这些分散在其他页面中的数据的占用的字节数），从而可以找到剩余数据所在的页，这称为页的扩展

在MySQL8.0中，默认行格式就是Dynamic，Dynamic、Compressed行格式和Compact行格式挺像，只不过在处理行溢出数据时有分歧：

+ Compressed和Dynamic两种记录格式对于存放在BLOB中的数据采用了完全的行溢出的方式。在数据页中只存放20个字节的指针（溢出页的地址），实际的数据都存放在Off Page（溢出页）中
+ Compact和Redundant两种格式会在记录的真实数据处存储一部分数据（存放768个前缀字节）

Compressed行记录格式的另一个功能就是，存储在其中的行数据会以zlib的算法进行压缩，因此对于BLIB、TEXT、VARCHAR这类大长度类型的数据能够进行非常高效的存储

**Redundant行格式**

MySQL5.0之前InnoDB的行记录存储方式

![](/img/mysql_24.png)

Compact行格式的开头是变长字段长度列表，而Redundant行格式的开头是字段长度偏移列表，与变长字段长度列表有两处不同：

+ 少了变长两个字：Redundant行格式会把该条记录中所有列（包括隐藏列）的长度信息都按照逆序存储到字段长度偏移列表
+ 多了偏移两个字：这意味着计算列值长度的方式不像Compact行格式那么直观，他是采用两个相邻数值的差值来计算各个列值的长度

不同于Compact行格式，Redundant行格式中的记录头信息固定占用6个字节

| 名称            | 大小（bit） | 描述                                                         |
| --------------- | ----------- | ------------------------------------------------------------ |
| （）            | 1           | 未使用                                                       |
| （）            | 1           | 未使用                                                       |
| deleted_mask    | 1           | 该行是否已被删除                                             |
| min_rec_mask    | 1           | B+树的每层非叶子节点中最小记录都会添加该标记                 |
| n_owned         | 4           | 该记录拥有的记录数                                           |
| heap_no         | 13          | 索引堆中该条记录的位置信息                                   |
| n_fields        | 10          | 记录中列的数量                                               |
| 1byte_offs_flag | 1           | 记录字段长度偏移列表中每个列对应的偏移量，使用一个字节还是两个字节表示 |
| next_record     | 16          | 页中下一条记录的绝对地址                                     |

与Compact行格式的记录头信息对比来看，有两处不同：

+ Redundant行格式多了n_field和1byte_offs_flag属性
+ Redundant行格式中没有record_type属性

#### 区、段与碎片区

**为什么要有区**

B+树的每一层中的页都会形成一个双向链表，如果是以页为单位来分配存储空间的话，双向链表相邻的两个页之间的物理位置可能离得非常远。我们介绍B+树索引的适用场景的时候特别提到范围查询只需要定位到最左边的记录和最右边的记录，然后沿着双向链表一直扫描就可以了，而如果链表中相邻的两个页物理位置离得非常远，就是所谓的随机I/O，随机I/O是非常慢的，所以我们应该尽量让链表中相邻的页的物理位置也相邻，这样进行范围查找的时候才可以使用所谓的顺序I/O

一个区就是在物理位置上连续的64个页，因为InnoDB中页的大小默认是16KB，所以一个区的大小是64*16KB=1MB。在表中数据量大的时候，为某个索引分配空间的时候就不再按照页为单位分配了，而是按照区为单位分配，甚至在表中的数据特别多的时候，可以一次性分配多个连续的区，虽然可能造成一点点空间的浪费（数据不足以填满整个区），但是从性能角度看，可以消除很多的随机I/O

**为什么要有段**

对于范围查询，其实是对B+树叶子节点中的记录进行顺序扫描，而如果不区分叶子节点和非叶子节点，统统把节点代表的页面放到申请到的区中的话，进行范围扫描的效果就大打折扣了。所以InnoDB对B+树的叶子节点和非叶子节点进行了区别对待，也就是说叶子节点有自己独特的区，非叶子节点也有自己独有的区。存放叶子节点的区的集合就算是一个段（segment），存放非叶子节点的区的集合也算是一个段，也就是说一个索引会生成2个段，一个叶子节点段，一个非叶子节点段

除索引的叶子节点段和非叶子节点段之外，InnoDB中还有为存储一些特殊的数据而定义的段，比如回滚段。所以，常见的段有数据段、索引段、回滚段。数据段即为B+树的叶子节点，索引段即为B+树的非叶子节点

在InnoDB存储引擎中，对段的管理都是由引擎自身所完成的，DBA不能也没有必要对其进行控制。这从一定程度上简化了DBA对于段的管理

段其实不对应表空间中某一个连续的物理区域，而是一个逻辑上的概念，由若干个零散的页面以及一些完整的区组成

**为什么要有碎片区**

默认情况下，一个使用InnoDB存储引擎的表只有一个聚簇索引，一个索引会生成两个段，而段是以区为单位申请存储空间的，一个区默认占用1M存储空间，所以默认情况下一个只存了几条记录的小表也需要2M的存储空间吗？以后每次添加一个索引都需要多申请2M的存储空间吗？这对于存储记录比较少的表简直是天大的浪费。这个问题的症结在于到现在为止我们介绍的区都是非常纯粹的，也就是一个区被整个分配给某一个段，或者说区中的所有页面都是为了存储同一个段的数据而存在的，即使段的数据填不满区中所有的页面，那余下的页面也不能挪作他用

为了考虑以完整的区为单位分配给某个段对于数据量较小的表太浪费存储空间的这种情况，InnoDB提出了一个碎片（fragment）区的概念。在一个碎片区中，并不是所有的页都是为了存储同一个段的数据而存在的，而是碎片区中的页可以用于不同的目的，比如有些页用于段A，有些页用于段B，有些页甚至哪个段都不属于。碎片区直属于表空间，并不属于任何一个段

所以此后某个段分配存储空间的策略是这样的：

+ 在刚开始向表中插入数据的时候，段是从某个碎片区以单个页面为单位来分配存储空间的
+ 当某个段以已经占用了32个碎片区页面之后，就会申请以完整的区为单位来分配存储空间

所以现在段不能仅定义为是某些区的集合，更精确的应该是某些零散页面以及一些完整区的集合

**区的分类**

区大体上可以分为4种类型：

+ 空闲的区（FREE）：现在还没有用到这个区中的任何页面
+ 有剩余空间的碎片区（FREE_FRAG）：表示碎片区中还有可用的页面
+ 没有剩余空间的碎片区（FULL_FRAG）：表示碎片区中所有的页面都被使用，没有空闲页面
+ 附属于某个段的区（FSEG）：每一个索引都可以分为叶子节点段和非叶子节点段

处于FREE、FREE_FRAG以及FULL_FRAG这三种状态的区都是独立的，直属于表空间，而处于FSEG状态的区是附属于某个段的

#### 表空间

表空间可以看作是InnoDB存储引擎逻辑结构的最高层，所有数据都存放在表空间中

表空间是一个逻辑容器，表空间存储的对象是段，在一个表空间中可以有一个或多个段，但是一个段只能属于一个表空间。表空间数据库由一个或多个表空间组成，表空间从管理上可以划分为系统表空间（System tablespace）、独立表空间（File-per-table tablespace）、撤销表空间（Undo Tablespace）、临时表空间（Temporary Tablespace）

**独立表空间**

独立表空间，即每张表有一个独立的表空间，也就是数据和索引信息都会保存在自己的表空间中。独立表空间可以在不同的数据库之间进行迁移

空间可以回收（DROP TABLE操作可自动回收表空间；其他情况，表空间不能自己回收）。如果对于统计分析或是日志表，删除大量数据后可以通过：alter table TableName engine=innodb;回收不用的空间。对于使用独立表空间的表，不管怎么删除，表空间的碎片不会太严重的影响性能，而且还有机会处理

独立表空间由段、区、页组成

```mysql
#查看InnoDB的表空间类型
show variables like 'innodb_file_per_table';
```

**系统表空间**

整个MySQL进程只有一个系统表空间，在系统表空间中会额外记录一些有关整个系统信息的页面，这部分是独立表空间中没有的

**InnoDB数据字典**

每当我们向一个表中插入一条记录的时候，MySQL校验过程如下：

先要校验一下插入语句对应的表存不存在，插入的列和表中的列是否符合，如果语法没有问题的话，还需要知道该表的聚簇索引和所有二级索引对应的根页面是哪个表空间的哪个页面，然后把记录插入对应索引的B+树中。所以说，MySQL除了保存着我们插入的用户数据外，还需要保存许多额外的信息：

- 某个表属于哪个表空间，表里边有多少列
- 表对应的每一个列的类型是什么
- 该表有多少索引，每个索引对应哪几个字段，该索引对应的根页面在哪个表空间的哪个页面
- 该表有哪些外键，外键对应哪个表的哪些列
- 某个表空间对应文件系统上文件路径是什么
- ...

上述这些信息并不是我们使用INSERT语句插入的用户数据，实际上是为了更好的管理我们这些用户数据而不得已引入的一些额外数据，这些数据也称为元数据。InnoDB存储引擎特意定义了一些列的内部系统表（internal system table）来记录这些元数据：

| 表名             | 描述                                                       |
| ---------------- | ---------------------------------------------------------- |
| SYS_TABLES       | 整个InnoDB存储引擎中所有表的信息                           |
| SYS_COLUMNS      | 整个InnoDB存储引擎中所有的列的信息                         |
| SYS_INDEXES      | 整个InnoDB存储引擎中所有的索引的信息                       |
| SYS_FIELDS       | 整个InnoDB存储引擎中所有的索引对应的列的信息               |
| SYS_FOREIGN      | 整个InnoDB存储引擎中所有的外键的信息                       |
| SYS_FOREIGN_COLS | 整个InnoDB存储引擎中所有的外键对应的列的信息               |
| SYS_TABLESPACES  | 整个InnoDB存储引擎中所有的表空间的信息                     |
| SYS_DATAFILE     | 整个InnoDB存储引擎中所有的表空间对应文件系统中文件路径信息 |
| SYS_VIRTUAL      | 整个InnoDB存储引擎中所有的虚拟生成列的信息                 |

这些系统表也被称为数据字典，他们都是以B+树的形式保存在系统表空间的某些页面中，其中SYS_FILES、SYS_COLUMNS、SYS_INDEXES、SYS_FIELDS这四个表尤其重要，称之为基础系统表

注意：用户是不能直接访问InnoDB的这些内部系统表，除非你直接去解析系统表空间对应文件系统上的文件。不过考虑到查看这些表的内容可能有助于大家分析问题，所以在系统数据库information_schema中提供了以innodb_sys开头的表：

```mysql
SHOW TABLES LIKE 'innodb_sys%';
```

这些表并不是真正的系统表，而是在存储引擎启动时读取这些SYS开头的系统表，然后填充到这些以INNODB_SYS开头的表中，以INNODB_SYS开头的表和以SYS开头的表中的字段并不完全一样，但供大家参考已经足矣

### 索引的创建与设计原则

#### 索引的声明与使用

**索引的分类**

MySQL的索引包括普通索引、唯一性索引、全文索引、单列索引、多列索引和空间索引

+ 从功能逻辑上说，索引主要有四种，分别是普通索引、唯一索引、主键索引、全文索引
+ 按照物理实现方式，索引可以分为两种：聚簇索引和非聚簇索引
+ 按照作用字段个数进行划分，分为单列索引和联合索引

全文索引

是目前搜索引擎使用的一种关键技术。它能够利用分词技术等多种算法智能分析出文本文字中关键词的频率和重要性，然后按照一定的算法规则智能的筛选出我们想要的搜索结果。全文索引非常适合大型数据集，对于小的数据集，它的用处比较小

使用参数FULLTEXT可以设置索引为全文索引。在定义索引的列上支持值的全文查找，允许在这些索引列中插入重复值和空值。全文索引只能创建在CHAR、VARCHAR或TEXT类型及其系列类型的字段上，查询数据量较大的字符串类型的字段时，使用全文索引可以提高查询速度。

全文索引典型的有两种类型：自然语言的全文索引和布尔全文索引

+ 自然语言搜索引擎将计算每一个文档对象和查询的相关度。这里，相关度是基于匹配的关键词的个数，以及关键词在文档中出现的次数。在整个索引中出现次数越少的词语，匹配时的相关度就越高。相反，非常常见的单词将不会被搜索，如果一个词语的在超过50%的记录中都出现了，那么自然语言的搜索将不会搜索这类词语

MySQL从3.23.23版开始支持全文索引，但MySQL5.6.4之前只有MyISAM支持，5.6.4版本以后innoDB才支持，但是官方版本不支持中文分词，需要第三方分词插件。在5.7.6版本，MySQL内置了ngram全文解析器，用来支持亚洲语种的分词。

随着大数据时代的到来，关系型数据库应对全文索引的需求已力不从心，逐渐被solr、ElasticSearch等专门的搜索引擎所替代

空间索引

使用参数SPATIAL可以设置索引为空间索引。空间索引只能建立在空间数据类型上，这样可以提高系统获取空间数据的效率，MySQL中的空间数据类型包括GEOMETRY、POINT、LINESTRING、POLYGON等，目前只有MyISAM支持空间索引，而且索引的字段不能为空值

**创建索引**

```mysql
#创建表的时候创建索引
#隐式的方式创建索引：在声明有主键约束、唯一性约束、外键约束的字段上，会自动的添加相关的索引
#显示方式创建
CREATE TABLE table_name [col_name data_type]
[UNIQUE|FULLTEXT|SPATIAL] [INDEX|KEY] [index_name](col_name [length]) [ASC|DESC]

#查看索引
SHOW CREATE TABLE table_name;
或
SHOW INDEX FROM table_name;

#通过删除主键约束的方式删除主键索引
ALTER TABLE table_name
DROP PRIMARY KEY;

#全文索引使用match+against方式查询
SELECT * FROM papers WHERE MATCH(title,concent) AGAINST ('查询字符串');

#在已经存在的表上创建索引
#方式1：使用ALTER TABLE语句创建索引
ALTER TABLE table_name ADD [UNIQUE|FULLTEXT|SPATIAL] [INDEX|KEY] [index_name](col_name[(length)]) [ASC|DESC]

#方式2：使用CREATE INDEX ... ON ...
CREATE [UNIQUE|FULLTEXT|SPATIAL] INDEX index_name ON table_name(col_name[(length)]);
```

**删除索引**

```mysql
#添加AUTO_INCREMENT约束字段的唯一索引不能被删除
#方式1：ALTER TABLE
ALTER TABLE table_name DROP INDEX index_name;

#方式2：DROP INDEX
DROP INDEX index_name ON table_name
```

#### MySQL8.0索引新特性

**支持降序索引**

降序索引以降序存储键值，虽然在语法上，从MySQL4版本开始就已经支持降序索引的语法了，但实际上该DESC定义是被忽略的，直到MySQL8版本才开始真正支持降序索引（仅限于InnoDB引擎）

MySQL在8.0版本之前创建的仍然是升序索引，使用时进行反向扫描，这大大降低了数据库的效率，如果一个查询，需要对多个列进行排序，且顺序要求不一致，那么使用降序索引将会避免数据库使用额外的文件排序操作，从而提高性能

CREATE TABLE ts1 (a int,b int,index index idx_a_b(a asc, b desc))

**隐藏索引**

在MySQL5.7版本及以前，只能通过显式的方式删除索引，此时，如果发现删除索引后出现错误，又只能通过显式创建索引的方式将删除的索引创建回来。如果数据表中的数据量非常大，或者数据表本身比较大，这种操作就会消耗系统过多的资源，操作成本非常高

从MySQL8开始支持隐藏索引（invisible indexes），只需要将待删除的索引设置为隐藏索引，使查询优化器不再使用这个索引，确认将该索引设置为隐藏索引后系统不受任何影响，就可以彻底删除索引。这种通过先将索引设置为隐藏索引，再删除索引的方式就是软删除。

同时，如果你想验证某个索引删除之后的查询性能影响，就可以暂时先隐藏该索引

索引默认是可见的，在使用CREATE TABLE，CREATE INDEX或者ALTER TABLE等语句时可以通过VISIBLE或者INVISIBLE关键字设置索引的可见性

```mysql
#创建表时直接创建
CREATE TABLE table_name(
	列信息,
    INDEX [index_name](col_name[(length)]) INVISIBLE
);

#创建表以后
ALTER TABLE table_name ADD [UNIQUE|FULLTEXT|SPATIAL] [INDEX|KEY] [index_name](col_name[(length)]) [ASC|DESC] INVISIBLE;

CREATE [UNIQUE|FULLTEXT|SPATIAL] INDEX index_name ON table_name(col_name[(length)]) INVISIBLE;

#修改索引的可见性
ALTER TABLE table_name ALTER INDEX index_name [INVISIBLE|VISIBLE];

#使隐藏索引对查询优化器可见
select @@optimizer_switch;
set session optimizer_switch="use_invisible_indexes=on"
```

当索引被隐藏时，他的内容仍然是和正常索引一样实时更新的，如果一个索引需要长期被隐藏，那么可以将其删除，因为索引的存在会影响插入、更新和删除的性能

#### 索引的设计原则

**哪些情况适合创建索引**

1. 字段的数值有唯一性的限制

2. 频繁作为WHERE查询条件的字段

3. 经常GROUP BY和ORDER BY的列

4. UPDATE、DELETE的WHERE条件列

5. DISTINCT字段需要创建索引

6. 多表JOIN连接操作时，创建索引注意事项

   1. 连接表的数量尽量不要超过3张，因为每增加一张表就相当于增加了一次嵌套的循环，数量级增长会非常快，严重影响查询的效率
   2. 对WHERE条件创建索引，因为WHERE才是对数据条件的过滤
   3. 对用于连接的字段创建索引，并且该字段在多张表中的类型必须一致

7. 使用列的类型小的创建索引

   这里说的类型小指的就是该类型表示的数据范围的大小

   以整数类型为例，有TINYINT、MEDIUMINT、INT、BIGINT，能用INT就不要使用BIGINT，这是因为：

   + 数据类型越小，在查询时进行的比较操作越快
   + 数据类型越小，索引占用的存储空间就越少，在一个数据页内就可以放下更多记录，从而减少磁盘I/O带来的性能损耗，也就意味着可以把更多的数据页缓存在内存中，从而加快读写效率

8. 使用字符串前缀创建索引

   使用索引列前缀的方式无法支持使用索引排序，只能使用文件排序

9. 区分度高（散列性高）的列适合作为索引

10. 使用最频繁的列放到联合索引的左侧

11. 在多个字段都要创建索引的情况下，联合索引优于单列索引

**限制索引数目**

建议单表索引不超过6个，原因：

+ 每个索引都需要占用磁盘空间，索引越多，需要的磁盘空间就越大
+ 索引会影响INSERT、DELETE、UPDATE等语句的性能
+ 优化器在选择如何优化查询时，会根据统一信息，对每个可以用到的索引进行评估，以生成出一个最好的执行计划，如果同时有很多个索引都可以用于查询，会增加MySQL优化器生成执行计划时间，降低查询效率

**哪些情况不适合创建索引**

1. 在WHERE中使用不到的字段，不要设置索引

2. 数据量小的表最好不要使用索引

   比如少于1000个

3. 有大量重复数据的列上不要建立索引

   重复度高于10%

4. 避免对经常更新的表创建过多的索引

5. 不建议使用无序的值作为索引

   比如身份证、UUID、MD5、HASH、无序长字符串等

6. 删除不再使用或者使用很少的索引

7. 不要定义冗余或重复的索引

### 性能分析工具的使用

#### 查看系统性能参数

```mysql
SHOW [GLOBAL|SESSION] STATUS LIKE '参数';
```

一些常用的性能参数：

+ Connections：连接MySQL服务器的次数
+ Uptime：MySQL服务器的上线时间
+ Slow_queries：慢查询的次数
+ Innodb_rows_read：Select查询返回的行数
+ Innodb_rows_inserted：执行INSERT操作插入的行数
+ Innodb_rows_updated：执行UPDATE操作更新的行数
+ Innodb_rows_deleted：执行DELETE操作删除的行数
+ Com_select：查询操作的次数
+ Com_insert：插入操作的次数，对于批量插入的INSERT操作，只累加一次
+ Com_update：更新操作的次数
+ Com_delete：删除操作的次数

#### 统计SQL的查询成本：last_query_cost

一条SQL查询语句在执行前需要确定查询执行计划，如果存在多种执行计划的话，MySQL会计算每个执行计划需要的成本，从中选择成本最小的一个作为最终执行的执行计划

如果我们想要查看某条SQL语句的查询成本，可以在执行完这条语句之后，通过查看当前会话中的last_query_cost变量值来得到当前查询的成本。它通常也是我们评价一个查询执行效率的一个常用指标。这个查询成本对应的是SQL语句所需要读取的页的数量

```mysql
SHOW STATUS LIKE 'last_query_cost';
```

#### 定位执行慢的SQL：慢查询日志

MySQL的慢查询日志，用来记录在MySQL中响应时间超过阀值的语句，具体指运行时间超过long_query_time值的SQL，则会被记录到慢查询日志中。long_query_time的默认值是10秒

默认情况下，MySQL数据库没有开启慢查询日志，需要我们手动来设置这个参数。如果不是调优需要的话，一般不建议启动该参数，因为开启慢查询日志或多或少带来一些性能影响

```mysql
#开启slow_query_log
set global slow_query_log='ON';

#查看慢查询日志是否开启，以及慢查询日志文件的位置
show variables like '%slow_query_log';

#修改long_query_time阈值
#设置global的方式对当前session的long_query_time失效，对新连接的客户端有效，所以一并执行下面语句
set global long_query_time=1;
set long_query_time=1;

#配置文件中一并设置
[mysqld]
slow_query_log=ON
slow_query_log_file=/var/lib/mysql/slow.log
long_query_time=1
log_output=FILE

#查看慢查询数目
SHOW GLOBAL STATUS LIKE '%Slow_queries%';

#除了上述变量，控制慢查询日志的还有一个系统变量：min_examined_low_limit，这个变量的意思是，查询扫描过的最小记录数。这个变量和查询执行时间，共同组成了判别一个查询是否是慢查询的条件，如果查询扫描过的记录数大于等于这个变量的值，并且查询执行时间超过long_query_time的值，那么，这个查询就被记录到慢查询日志中；反之，则不被记录到慢查询日志中
```

**慢查询日志分析工具：mysqldumpslow**

**删除慢查询日志**

通过SHOW VARIABLES LIKE 'slow_query_log%';显示慢查询日志信息，从执行结果看出，慢查询日志的目录默认为MySQL的数据目录，在该目录下手动删除慢查询日志文件即可，使用命令

```mysql
#重新生成慢查询日志文件
mysqladmin -uroot -p flush-logs slow
```

#### 查看SQL执行成本：SHOW PROFILE

逻辑架构中已有

show profile是MySQL提供的可以用来分析当前会话中SQL都做了什么，执行的资源消耗情况的工具，可用于sql调优的测量。默认情况下处于关闭状态，并保存最近15次的运行结果

```mysql
#查看
show variables like 'profiling';

#开启
set profiling = 'ON';
```

日常开发中需注意的结论：

1. converting HEAP to MyISAM：查询结果太大，内存不够，数据往磁盘上搬了
2. Creating tmp table：创建临时表。先拷贝数据到临时表，用完再删除临时表
3. Copying to tmp table on disk：把内存中临时表复制到磁盘上，警惕！
4. locked

如果在show profile诊断结果中出现了以上4条结果中的任何一条，则sql语句需要优化

不过show profile命令将被弃用，我们可以从information_schema中的profiling数据表进行查看

#### 分析查询语句：EXPLAIN

EXPLAIN或DESCRIBE

EXPLAIN语句输出的各个列的作用如下：

| 列名          | 描述                                                     |
| ------------- | -------------------------------------------------------- |
| id            | 在一个大的查询语句中，每个SELECT关键字都对应一个唯一的id |
| select_type   | SELECT关键字对应的那个查询的类型                         |
| table         | 表名                                                     |
| partitions    | 匹配的分区信息                                           |
| type          | 针对单表的访问方法                                       |
| possible_keys | 可能用到的索引                                           |
| key           | 实际上用到的索引                                         |
| key_len       | 实际用到的索引长度                                       |
| ref           | 当使用索引列等值查询时，与索引列进行等值匹配的对象信息   |
| rows          | 预估的需要读取的记录条数                                 |
| filtered      | 某个表经过搜索条件过滤后剩余记录条数的百分比             |
| extra         | 一些额外的信息                                           |

**explain各列作用**

1. table：表名

2. id

   查询语句中每出现一个SELECT关键字，MySQL就会为它分配一个唯一的id

   对于连接查询来说，一个SELECT关键字后边的FROM子句中可以跟随多个表，所以在连接查询的执行计划中，每个表都会对应一条记录，但是这些记录的id都是相同的，出现在前面的表表示驱动表，出现在后边的表表示被驱动表

   对于包含子查询的查询语句来说，就可能涉及多个SELECT关键字，所以在包含子查询的查询语句的执行计划中，每个SELECT关键字都会对应一个唯一的id，需要注意的是，查询优化器可能对涉及子查询的查询语句进行重写，从而转为连接查询

   对于包含UNION子句的查询语句来说，会生成一张临时表

   小结：

   + id如果相同，可以认为是一组，从上往下顺序执行
   + 在所有组中，id值越大，优先级越高，越先执行
   + 关注点：id号每个号码，表示一趟独立的查询，一个sql的查询趟数越少越好

3. select_type

   + SIMPLE：不包含UNION或者子查询的查询都算作SIMPLE类型
   + PRIMARY：对于包含UNION或者UNION ALL或者子查询的大查询来说，它是由几个小查询组成的，其中最左边的查询就是PRIMARY
   + UNION：对于包含UNION或者UNION ALL的大查询来说，它是由几个小查询组成的，除了最左边的，其余的小查询就是UNION
   + UNION RESULT：MySQL选择使用临时表来完成UNION查询的去重工作，针对该临时表的查询就是UNION RESULT
   + SUBQUERY：如果包含子查询的查询语句不能够转为对应的连接查询，并且该子查询是不相关子查询则为SUBQUERY
   + DEPENDENT SUBQUERY：如果包含子查询的查询语句不能够转为对应的连接查询，并且该子查询是相关子查询则为DEPENDENT SUBQUERY
   + DEPENDENT UNION：在包含UNION或UNION ALL的大查询中，如果各个小查询都依赖于外层查询的话，那除了最左边的那个小查询外，其余的小查询就是DEPENDENT UNION
   + DERIVED：对于包含派生表的查询，该派生表对应的子查询就是DERIVED
   + MATERIALIZED：当查询优化器在执行包含子查询的语句时，选择将子查询物化之后与外层查询进行连接查询时，该子查询就是MATERIALIZED

4. partitions

   代表分区表中的命中情况，非分区表，该项为NULL，一般情况下，我们查询语句的执行计划的partitions列的值都是NULL

5. type

   执行计划的一条记录就代表着MySQL对某个表的执行查询时的访问方法，又称访问类型，其中type列就表明了这个访问方法是啥，是较为重要的一个指标。性能由好到坏排序如下：

   + system

     当表中只有一条记录，并且该表使用的存储引擎的统计数据是精确的，比如MyISAM、Memory，那么对该表的访问方法就是system

   + const

     当我们根据主键或者唯一二级索引列与常数进行等值匹配时，对单表的访问方法就是const

   + eq_ref

     在连接查询时，如果被驱动表是通过主键或者唯一二级索引列等值匹配的方式进行访问的（如果该主键或者唯一二级索引是联合索引的话，所有的索引列都必须进行等值比较），则对该驱动表的访问方法就是eq_ref

   + ref

     当通过普通的二级索引列与常量进行等值匹配时来查询某个表，那么对该表的访问方法就可能是ref

   + fulltext

     全文索引

   + ref_or_null

     当对普通二级索引进行等值匹配查询，该索引列的值也可以是NULL值时，那么对该表的访问方法就可能是ref_or_null

   + index_merge

     单表访问方法时在某些场景下可以使用intersection、union、sort-union这三种索引合并的方式来执行查询

   + unique_subquery

     针对在一些包含IN子查询的查询语句中，如果查询优化器决定将IN子查询转换为EXISTS子查询，而且子查询可以使用到主键进行等值匹配的话，那么该子查询的执行计划的type就是unique_subquery

   + index_subquery

     index_subquery与unique_subquery类似，只不过访问子查询中的表时使用的是普通的索引

   + range

     如果使用索引获取某些范围区间的记录，那么就可能使用到range访问方法

   + index

     当我们可以使用索引覆盖，但需要扫描全部的索引记录时，该表的访问方法就是index

   + ALL

     全表扫描

6. possible_keys和key

   可能用到的索引和实际用到的索引

7. key_len

   实际使用到的索引长度（即字节数），帮你检查是否充分的利用上了索引，值越大越好，主要针对联合索引

8. ref

   当使用索引列等值查询时，与索引列进行等值匹配的对象信息

9. rows

   预估的需要读取的记录条数，越小越好

10. filtered

    某个表经过搜索条件过滤后剩余记录条数的百分比

    表示符合查询条件的数据百分比，最大100。用rows × filtered可获得和下一张表连接的行数。例如rows = 1000，filtered = 50%，则和下一张表连接的行数是500。

11. Extra

    + No tables used：查询语句没有FROM子句
    + Impossible WHERE：查询语句没有FROM子句
    + Using where：当使用全表扫描来执行对某个表的查询，并且该语句的WHERE子句中有针对该表的搜索条件时。当使用索引访问来执行对某个表的查询，并且该语句的WHERE子句中有除了该索引包含的列之外的其他搜索条件时
    + No matching min/max row：当查询列表处有MIN或MAX聚合函数，但是并没有符合WHERE的搜索条件的记录时
    + Select tables optimized away：当查询列表处有MIN或MAX聚合函数，但是有符合WHERE的搜索条件的记录时
    + Using index：当我们的查询列表以及搜索条件中只包含某个索引列，也就是在可以使用覆盖索引的情况下
    + Using index condition：有些搜索条件中虽然出现了索引列，但却不能使用索引，索引条件下推
    + Using join buffer：在连接查询执行过程中，当被驱动表不能有效的利用索引加快访问速度，MySQL一般会为其分配一块名叫join buffer的内存块来加快查询速度，也就是我们所讲的基于块的嵌套循环算法
    + Not exist：当我们使用左外连接时，如果WHERE子句中包含要求被驱动表的某个列等于NULL值的搜索条件，而且那个列又是不允许存储NULL值的
    + Using sort_union：准备使用sort-union索引合并的方式执行查询
    + Using union：准备使用union索引合并的方式进行查询
    + Using intersect：准备使用intersect索引
    + Zero limit：limit 0
    + Using filesort：很多情况下排序操作无法使用到索引，只能在内存中（记录较少的时候）或者磁盘中（记录较多的时候）进行排序，MySQL把这种在内存中或者磁盘上进行排序的方式统称为文件排序，如果某个查询需要使用文件排序的方式执行查询，就会显示Using filesort
    + Using temporary：在许多查询的执行过程中，MySQL可能会借助临时表来完成一些功能，比如去重、排序之类的，比如我们在执行许多包含DISTINCT、GROUP BY、UNION等子句的查询过程中，如果不能有效利用索引来完成查询，MySQL很有可能寻求通过建立内部的临时表来执行查询

**explain的四种输出格式**

+ 传统格式：EXPLAIN

+ JSON格式：EXPLAIN FORMAT=JSON

  传统格式中的输出缺少了一个衡量执行计划好坏的重要属性-成本。而JSON格式是四种格式里面输出信息最详尽的格式，里面包含了执行的成本信息

  | Column      | JSON Name   |
  | ----------- | ----------- |
  | id          | select_id   |
  | select_type | None        |
  | type        | access_type |
  | key_len     | key_length  |
  | Extra       | None        |

  cost_info包含read_cost、eval_cost、prefix_cost、data_read_per_join

  + read_cost：IO成本和检测rows x (1 - filter)条记录的CPU成本组成
  + eval_cost：检测rows x filter条记录的成本
  + prefix_cost：单独查询s1表的成本，也就是read_cost + eval_cost
  + data_read_per_join：表示此次查询中需要读取的数据量

+ TREE格式：EXPLAIN FORMAT=TREE

  8.0.16后引入的新格式，主要根据查询的各个部分之间的关系和各部分的执行顺序来描述如何查询

+ 可视化输出

**SHOW WARNINGS**

使用EXPLAIN语句查看某个查询的执行计划后，紧接着可以使用SHOW WARNINGS查看与这个查询的执行计划有关的一些扩展信息

+ level：
+ code：当code为1003时，message字段展示的信息类似于查询优化器重写后的语句
+ message

#### 分析优化器执行计划：trace

OPTIMIZER_TRACE是MySQL5.6引入的一项追踪功能，它可以跟踪优化器做出的各种决策（比如访问表的方法、各种开销计算、各种转换等），并将跟踪结果记录到INFORMATION_SCHEMA.OPTIMIZER_TRACE表中

此功能默认关闭，开启trace，并设置格式为JSON，同时设置trace最大能够使用的内存大小，避免解析过程中因为默认内存过小而不能够完整展示

```mysql
SET optimizer_trace="enables=on",end_makers_in_json=on;
SET optimizer_trace_max_mem_size=1000000;
```

开启后，可分析如下语句：SELECT、INSERT、REPLACE、UPDATE、DELETE、EXPLAIN、SET、DECLARE、CASE、IF、RETURN、CALL

最后，查询information_schema.optimizer_trace就可以知道MySQL是如何执行SQL的

#### MySQL监控分析视图-sys schema

关于MySQL的性能监控和问题诊断，我们一般从performance_schema中去获取想要的数据，在MySQL5.7.7版本中，新增sys schema，他将performance_schema和information_schema中的数据以更容易理解的方式总结归纳为视图，其目的就是为了降低查询performance_schema的复杂度

**Sys schema视图摘要**

1. 主机相关：以host_summary开头，主要汇总了IO延迟的信息
2. Innodb相关：以innodb开头，汇总了innodb buffer信息和事务等待innodb锁的信息
3. I/O相关：以io开头，汇总了等待I/O、I/O使用量情况
4. 内存使用情况：以memory开头，从主机、线程、事件等角度展示内存的使用情况
5. 连接与会话信息：processlist和session相关视图，总结了会话相关信息
6. 表相关：以schema_table开头的视图，展示了表的统计信息
7. 索引信息：统计了索引的使用情况，包含冗余索引和未使用的索引情况
8. 语句相关：以statement开头，包含执行全表扫描、使用临时表、排序等的语句信息
9. 用户相关：以user开头的视图，统计了用户使用的文件I/O、执行语句统计信息
10. 等待事件相关信息：以wait开头，展示等待事件的延迟情况

```mysql
#查询冗余索引
SELECT * FROM sys.schema_redundant_indexes;
#查询未使用过的索引
SELECT * FROM sys.schema_unused_indexes;
#查询索引的使用情况
SELECT index_name,rows_selected,rows_inserted,rows_updated,rows_deleted
FROM sys.schema_index_statistics WHERE table_schema='dbname';

#查询表的访问量
SELECT table_schema,table_name,SUM(io_read_requests+io_write_requests) AS io
FROM sys.schema_table_statistics GROUP BY table_schema,table_name ORDER BY io DESC;
#查询占用bufferpool较多的表
SELECT object_schema,object_name,allocated,DATA
FROM sys.innodb_buffer_stats_by_table ORDER BY allocated LIMIT 10;
#查看表的全表扫描情况
SELECT * FROM sys.statements_with_full_table_scans WHERE db='dbname';

#监控SQL执行的频率
SELECT db,exec_count,QUERY 
FROM sys.statement_analysis ORDER BY exec_count DESC;
#监控使用了排序的SQL
SELECT db,exec_count,first_seen,last_seen,QUERY 
FROM sys.statements_with_sorting LIMIT 1;
#监控使用了临时表或者磁盘临时表的SQL
SELECT db,exec_count,tmp_tables,tmp_disk_tables,QUERY 
FROM sys.statement_analysis WHERE tmp_tables>0 OR tmp_disk_tables>0
ORDER BY (tmp_tables+tmp_disk_tables) DESC;

#查看消耗磁盘IO的文件
select file,avg_read,avg_write,avg_read+avg_write as avg_io
from sys.io_global_by_file_by_bytes order by avg_read limit 10;

#行锁阻塞情况
select * from sys.innodb_lock_waits;
```

通过sys库去查询时，MySQL会消耗大量资源去收集相关信息，严重的可能导致业务请求被阻塞，从而引起故障。生产上不要频繁查询sys、performance_schema、information_schema

### 索引优化与查询优化

#### 索引失效

1. 能使用联合索引时会先使用联合索引导致单个索引失效

2. 最佳左前缀法则：对于多列索引，过滤条件使用索引必须按照索引建立时的顺序，依次满足，一旦跳过某个字段，索引后面的字段都无法使用

3. 主键插入顺序：主键最好是依次递增，如果忽大忽小插入时会导致页面分裂，造成性能损耗

4. 计算、函数、类型转换（自动或手动）导致索引失效

5. 范围条件右边的列的索引失效

   CREATE INDEX idx_age_classid_name ON student(age,classId,NAME)

   SELECT SQL_NO_CACHE * FROM student WHERE student.age=30 AND student.classId>20 AND student.name='abc';

   NAME索引失效

6. 不等于（!=或者<>）索引失效

7. is null可以使用索引，is not null 无法使用索引

   设计表的时候设置NOT NULL约束，同理not like也无法使用索引

8. like以通配符%开头索引失效

9. OR 前后存在非索引的列，索引失效

10. 数据库和表的字符集统一使用utf8mb4，不同字符集比较前需要进行转换会造成索引失效

一般性建议：

+ 对于单列索引，尽量选择针对当前query过滤性更好的索引
+ 在选择组合索引的时候，当前query中过滤性最好的字段在索引字段顺序中，位置越靠前越好
+ 在选择组合索引的时候，尽量选择能够包含当前query中的where子句中更多字段的索引
+ 在选择组合索引的时候，如果某个字段可能出现范围查找时，尽量把这个字段放在索引次序的最后面

#### 关联查询优化

小表驱动大表

左外连接在被驱动表上建立索引

对于内连接来说，查询优化器可以决定谁作为驱动表，谁作为被驱动表出现的

对于内连接来讲，如果表的连接条件中只能有一个字段有索引，则有索引的字段所在的表会作为被驱动表

对于内连接来说，在两个表的连接条件都存在索引的情况下，会选择小表作为驱动表

**join语句原理**

+ Simple Nested-Loop Join(简单嵌套循环连接)

  ![](/img/mysql_25.png)

  | 开销统计         | SNLJ  |
  | ---------------- | ----- |
  | 外表扫描次数     | 1     |
  | 内表扫描次数     | A     |
  | 读取记录数       | A+B*A |
  | JOIN比较次数     | B*A   |
  | 回表读取记录次数 | 0     |

  

+ Index Nested-Loop Join(索引嵌套循环连接)

  优化思路只要是为了减少内层表数据的匹配次数，所以要求被驱动表上必须有索引才行

  ![](/img/mysql_26.png)

  | 开销统计         | SNLJ  | INLJ                  |
  | ---------------- | ----- | --------------------- |
  | 外表扫描次数     | 1     | 1                     |
  | 内表扫描次数     | A     | 0                     |
  | 读取记录数       | A+B*A | A+B(match)            |
  | JOIN比较次数     | B*A   | A*Index(Height)       |
  | 回表读取记录次数 | 0     | B(match)(if possible) |

+ Block Nested-Loop Join(块嵌套循环连接)

  如果存在索引，那么会使用index的方式进行join，如果join的列没有索引，被驱动表要扫描的次数太多，每次访问被驱动表，其表中的记录都会被加载到内存中，然后再从驱动表中取一条与其匹配，匹配结束后清除内存，然后再从驱动表中加载一条记录。为了减少被驱动表的IO次数，就出现了Block Nested-Loop Join

  不再是逐条获取驱动表的数据，而是一块一块的获取，引入了join buffer缓冲区，将驱动表相关的部分数据列（大小受join buffer的限制）缓存到join buffer中，然后全表扫描被驱动表，被驱动表的每一条记录一次性和join buffer中所有的驱动表记录进行匹配（内存中操作），将简单嵌套循环中的多次比较合并成一次，降低了被驱动表的访问频率

  这里缓存的不只是关联表的列，select后面的列也会缓存起来，在一个有N个JOIN关联的sql中会分配N-1个join buffer，所以查询的时候尽量减少不必要的字段，可以让join buffer中存放更多的列

  ![](/img/mysql_27.png)

  | 开销统计         | SNLJ  | INLJ                  | BNLJ                                       |
  | ---------------- | ----- | --------------------- | ------------------------------------------ |
  | 外表扫描次数     | 1     | 1                     | 1                                          |
  | 内表扫描次数     | A     | 0                     | A*used_column_size/join_buffer_size+1      |
  | 读取记录数       | A+B*A | A+B(match)            | A+B* (A*used_column_size/join_buffer_size) |
  | JOIN比较次数     | B*A   | A*Index(Height)       | B*A                                        |
  | 回表读取记录次数 | 0     | B(match)(if possible) | 0                                          |

```mysql
#查看block_nested_loop,默认是开启的
SHOW VARIABLES LIKE '%optimizer_switch%';

#join_buffer_size，默认情况下为256k
SHOW VARIABLES LIKE '%join_buffer%';
```

**join小结**

1. 整体效率比较：INLJ>BNLJ>SNLJ

2. 永远用小结果集驱动大结果集（其本质是减少外层循环的数据数量，小的度量单位指的是表行数*每行大小）

   straight_join不让查询优化器破坏前后顺序

   SELECT t1.b, t2.* from t1 straight_join t2 on t1.b=t2.b where t2.id<=100;

3. 为被驱动表匹配的条件增加索引（减少内层表的循环匹配次数）

4. 增大join buffer size的大小（一次缓存的数据越多，那么内层包的扫表次数就越少）

5. 减少驱动表不必要的字段查询（字段越少，join buffer所缓存的数据就越多）

**hash join**

从MySQL的8.0.20版本开始将废弃BNLJ，因为从MySQL8.0.18版本开始就加入了hash join默认都会使用hash join

+ Nested Loop：对于连接的数据子集较小的情况，Nested Loop是个较好的选择
+ Hash Join是做大数据集连接时的常用方式，优化器使用两个表中较小的表利用Join Key在内存中建立散列表，然后扫描较大的表并探测散列表，找出与hash表匹配的行
  + 这种方式适用于较小的表完全可以放于内存中的情况，这样总成本就是访问两个表的成本之和
  + 在表很大的情况下并不能完全放入内存，这时优化器会将它分割成若干不同的分区，不能放入内存的部分就把该分区写入磁盘的临时段，此时要求有较大的临时段从而尽量提高I/O性能
  + 它能够很好的工作于没有索引的大表和并行查询的环境中，并提供最好的性能，大多数人都说他是join的重型升降机。Hash Join只能应用于等值连接，这是由hash的特点决定的

#### 子查询优化

子查询的执行效率不高：

+ 执行子查询时，MySQL需要为内层查询语句的查询结果建立一个临时表，然后外层查询语句从临时表中查询记录，查询完毕后，再撤销这些临时表，这样会消耗过多的CPU和IO资源，产生大量的慢查询
+ 子查询的结果集存储的临时表，不论是内层临时表还是磁盘临时表都不会存在索引，所以查询性能会受到一定的影响
+ 对于返回结果集比较大的子查询，其对查询性能的影响也就越大

**在MySQL中，可以使用JOIN查询来替换子查询**，连接查询不需要建立临时表，其速度要比子查询要快，如果使用索引，性能会更好

尽量不要使用NOT IN或者NOT EXISTS，用LEFT JOIN xxx ON xxx WHERE xx IS NULL替代

#### 排序优化

MySQL中支持两种排序方式，分别是FileSort和Index排序

+ Index排序中，索引可以保证数据库的有序性，不需要再进行排序，效率更高
+ FileSort排序则一般在内存中进行排序，占用CPU较多。如果待排结果较大，会产生临时文件I/O到磁盘进行排序的情况，效率较低

优化建议：

1. SQL中，可以在WHERE子句和ORDER BY子句中使用索引，目的是在WHERE子句中避免全表扫描，在ORDER BY子句避免使用FileSort排序，当然，某些情况下全表扫描，或者FileSort不一定比索引慢，但总的来说，我们还是要避免，以提高查询效率
2. 尽量使用Index完成ORDER BY排序。如果WHERE和ORDER BY后面是相同的列就是用单列索引，如果不同就使用联合索引
3. 无法使用Index时，需要对FileSort方式进行调优

ORDER BY 时不LIMIT，索引可能失效

ORDER BY 顺序错误，索引失效

ORDER BY 时规则不一致，索引失效（顺序错，不索引，方向反，不索引）

两个索引同时存在，MySQL自动选择最优的方案，但是，随着数据量的变化，选择的索引也会随之变化的

当范围条件和GROUP BY或者ORDER BY的字段出现二选一时，优先观察条件字段的过滤数量，如果过滤的数据足够多，而需要排序的数据并不多时，优先把索引放在范围字段上，反之亦然

#### filesort算法：双路排序和单路排序

排序的字段如果不在索引列上，则filesort会有两种算法：双路排序和单路排序

双路排序（慢）

+ MySQL4.1之前使用，两次扫描磁盘，最终得到数据，读取行指针和order by 列，对他们进行排序，然后扫描已经排序好的列表，按照列表中的值重新从列表中读取对应的数据输出
+ 从磁盘取排序字段，在buffer进行排序，再从磁盘取其他字段

单路排序（快）

从磁盘读取查询需要的所有列，按照order by列在buffer对他们进行排序，然后扫描排序后的列表进行输出，他的效率更快一些，避免了二次读取数据，并且把随机IO变成了顺序IO，但是他会用到更多的空间，因为他把每一行都保存到内存中

但是单路有问题，在sort_buffer中，单路比多路要多占用很多空间，因为单路是把所有字段都取出，所以有可能取出的数据的总大小超出了sort_buffer的容量，导致每次只能取sort_buffer容量大小的数据（创建tmp文件，多路合并），排完再取sort_buffer容量大小，再排......从而多次IO

优化策略：

+ 尝试提高sort_buffer_size

  不管使用哪种算法，提高这个参数都会提高效率，要根据系统的能力去提高，因为这个参数是针对每个进程（connection）的1M-8M之间调整。MySQL5.7，InnoDB存储引擎默认值是1048576字节，1MB

+ 尝试提高max_length_for_sort_data

  提高这个参数，会增加用改进算法的概率。默认1024字节。

  如果设的太高，数据总容量超出sort_buffer_size的概率增大，明显症状是高的磁盘IO活动，低的处理器使用率，如果需要返回的列的总长度大于max_length_for_sort_data，使用双路算法，否则使用单路算法，1024-8192字节之间调整

+ Order by时select * 是一个大忌，最好只Query需要的字段

#### GROUP BY优化

+ group by使用索引的原则几乎跟order by一致，group by即使没有过滤条件用到索引，也可以直接使用索引
+ group by先排序再分组，遵照索引建的最佳左前缀法则
+ 当无法使用索引列，增大max_length_for_sort_data和sort_buffer_size参数的设置
+ where效率高于having，能写在where限定的条件就不要写在having中
+ 减少使用order by，order by，group by，distinct这些语句较为耗费CPU
+ 包含了order by, group by,distinct这些查询语句，where条件过滤出来的结果集请保持在1000行以内，否则SQL会很慢

#### 优化分页查询

一般分页查询时，通过创建覆盖索引能够比较好的提高性能。一个常见又非常头疼的问题就是，imit 2000000,10，此时需要MySQL排序前2000010记录，仅仅返回2000000-2000010的记录，其他记录丢掉，查询排序的代价非常大

```mysql
SELECT * FROM student LIMIT 2000000,10

#优化思路一：在索引上完成排序分页操作，最后根据主键关联回原表查询所需要的其他列内容
SELECT * FROM student t, (SELECT id FROM student ORDER BY id LIMIT 2000000,10) a
WHERE t.id=a.id;

#优化思路二：该方案适用于自增的表，可以把limit查询转换成某个位置的查询
SELECT * FROM student WHERE id > 2000000 LIMIT 10;
```

#### 优先考虑覆盖索引

一个索引包含了满足查询结果的数据就叫做覆盖索引

好处

1. 避免Innodb表进行索引的二次查询（回表）

2. 可以把随机IO变成顺序IO加快查询效率

   由于覆盖索引是按键值的顺序存储的，对于IO密集型的范围查找来说，对比随机从磁盘读取每一行的数据IO要少的多

3. 由于覆盖索引可以减少树的搜索次数，显著提高查询性能，所以使用覆盖索引是一个常用的性能优化手段

弊端

索引字段的维护总是有代价的

#### 索引下推

Index Condition Pushdown(ICP)是MySQL5.6中新特性，是一种在存储引擎层使用索引过滤数据的优化方式

+ 如果没有ICP，存储引擎会遍历索引以定位基表中的行，并将它们返回给MySQL服务器，由MySQL服务器评估WHERE后面的条件是否保留行
+ 启用ICP后，如果部分WHERE条件可以仅使用索引中的列进行筛选，则MySQL服务器会把这部分WHERE条件放到存储引擎筛选，然后，存储引擎通过使用索引条目来筛选数据，并且只有在满足这一条件时才从表中读取行，ICP可以减少存储引擎必须访问基表的次数和MySQL服务器必须访问存储引擎的次数，但是ICP的加速效果取决于在存储引擎内通过ICP筛选掉的数据的比例

**ICP的开启/关闭**

+ 默认情况下启用索引条件下推，可以通过设置系统变量optimizer_switch控制

  ```mysql
  #关闭索引下推
  SET optimizer_switch = 'index_condition_pushdown=off'
  
  #打开索引下推
  SET optimizer_switch = 'index_condition_pushdown=on'
  ```

+ 当使用索引条件下推时，EXPLAIN语句输出结果中EXTRA列内容显示为Using index condition

**ICP的使用条件**

1. 如果表访问的类型为range、ref、eq_ref和ref_or_null可以使用ICP
2. ICP可用于InnoDB和MyISAM表，包括分区表InnoDB和MyISAM表
3. 对于InnoDB表，ICP仅用于二级索引，ICP的目标是减少全行读取次数，从而减少IO操作
4. 当SQL使用覆盖索引时，不支持ICP，因为这种情况下使用ICP不会减少IO
5. 相关子查询的条件不能使用ICP

#### 其他查询优化策略

**EXISTS和IN的区分**

SELECT * FROM A WHERE cc IN (SELECT cc FROM B)

SELECT * FROM A WHERE EXISTS (SELECT cc FROM B WHERE B.cc=A.cc)

当A小于B时，用EXISTS，因为EXISTS的实现，相当于外表循环，实现的逻辑类似于

for i in A

​	for j in B

​		if j.cc == i.cc then...

当B小于A时，用IN，因为实现的逻辑类似于

for i in B

​	for j in A

​		if j.cc == i.cc then...

**COUNT(*)和COUNT(具体字段)效率**

COUNT(*)和COUNT(1)本质上并没有区别，如果是MyISAM存储引擎，统计数据表的行数只需要O(1)复杂度，因为每张MyISAM表都有一个meta信息存储了row_count值，如果是InnoDB存储引擎需要扫描全表，是O(n)复杂度

在InnoDB引擎中，如果采用COUNT(具体字段)来统计行数，要尽量采用二级索引，因为主键采用的索引是聚簇索引，聚簇索引包含的信息多，明显会大于二级索引，对于COUNT(*)和COUNT(1)，他们不需要查找具体的行，只是统计行数，系统会自动采用占用空间更小的二级索引进行统计

**关于SELECT(*)**

MySQL在解析的过程中，会通过查询数据字典将*按序转换成所有列名，这会大大的耗费资源和时间

无法使用覆盖索引

**LIMIT 1对优化下影响**

如果结果集只有一条，那么加上LIMIT 1的时候，当找到第一条结果的时候就不会继续扫描了，这样会加快查询速度

**多使用COMMIT**

能提高程序性能，需求也会因为COMMIT所释放的资源而减少，COMMIT释放的资源

+ 回滚段上用于恢复数据的信息
+ 被程序语句获得的锁
+ redo/undo log buffer中的空间
+ 管理上述3中资源中的内部花费

#### 主键如何设计

**自增ID的问题**

1. 可靠性不高：存在自增ID回溯的问题，MySQL8.0才修复
2. 安全性不高：对外暴露的接口可以非常容易猜测对应的信息
3. 性能差：需要在数据库服务器端生成
4. 交互多：业务还需要额外执行一次类似last_insert_id()的函数才能知道刚才插入的自增值，这需要多一次的网络交互
5. 局部唯一性：自增ID是局部唯一的，只在当前数据库实例中唯一，而不在全局唯一

尽量不要使用跟业务有关的字段做主键

**推荐的主键算法**

非核心业务：对应表的主键自增ID，如告警、日志、监控等信息

核心业务：主键设计至少应该是全局唯一且单调递增

### 数据库设计规范

#### 范式

在关系数据库中，关于数据库设计的基本原则、规则就称为范式，Normal Form，简称NF

六种常见范式：第一范式（1stNF）、第二范式（2ndNF）、第三范式（3rdNF）、巴斯-科德范式（BCNF）、第四范式（4NF）、第五范式（5NF，又称完美范式）

**键和相关属性的概念**

+ 超键：能唯一标识元组的属性集叫做超键
+ 候选键：如果超键不包括多余的属性，那么这个超键就是候选键
+ 主键：用户可以从候选键中选择一个作为主键
+ 外键：如果数据表R1中的某属性集不是R1的主键，而是另一个数据表R2的主键，那么这个属性集就是数据表R1的外键
+ 主属性：包含在任一候选键中的属性
+ 非主属性：不包含在任何一个候选键中的属性

**第一范式**

确保数据表中的每个字段的值必须具有原子性，也就是说数据表中每个字段的值为不可再拆分的最小数据单元

**第二范式**

满足第一范式的基础上，还要满足数据表里的每一条数据记录，都是可唯一标识的。而且所有非主键字段，都必须完全依赖主键，不能只依赖主键的一部分

**第三范式**

在第二范式的基础上，要求数据表中的所有非主键字段不能依赖于其他非主键字段

**BCNF(巴斯范式)**

若一个关系达到了第三范式，并且他只有一个候选键，或者他的每个候选键都是单属性，则该关系自然达到BC范式，消除了主属性对候选键的部分依赖或者传递依赖

**第四范式**

多值依赖的概念：

+ 多值依赖即属性之间的一对多关系，记为K→→A
+ 函数依赖事实上是单值依赖，所以不能表达属性值之间的一对多关系
+ 平凡的多值依赖：全集U=K+A，一个K可以对应多个A，即K→→A
+ 非平凡的多值依赖：全集U=K+A+B，一个K可以对应多个A，也可以对应多个B，A和B相互独立，即K→→A，K→→B。整个表有多组一对多关系，且有：一部分是相同的属性集合，多部分是互相独立的属性集合

第四范式即在满足巴斯-科德范式（BCNF）的基础上，消除非平凡且非函数依赖的多值依赖（即把同一表内的多对多关系删除）

**第五范式**

在第四范式的基础上，消除不是由候选键所蕴含的连接依赖，如果关系模式R中的每一个连接依赖均由R的候选键所隐含，则称此关系符合第五范式

**小结**

范式的优点：有助于消除数据库中的数据冗余

范式的缺点：可能降低查询效率

#### ER模型

实体关系模型

+ 实体：可以看作数据对象，用矩形表示。实体分为强实体和弱实体，强实体是指不依赖于其他实体的实体，弱实体是指对另一个实体有很强的依赖关系的实体
+ 属性：实体的特性，用椭圆形表示
+ 关系：指实体之间的联系，用菱形表示

#### 数据表的设计原则

三少一多

1. 数据表的个数越少越好
2. 数据库中的字段个数越少越好
3. 数据表中联合主键的字段个数越少越好
4. 使用主键和外键越多越好

### 数据库其他调优策略

#### 优化MySQL的参数

+ innodb_buffer_pool_size：这个参数是MySQL数据库最重要的参数之一，表示InnoDB类型的表和索引的最大缓存。它不仅缓存索引数据，还会缓存表的数据，这个值越大，查询的速度就越快，但是这个值太大会影响操作系统的性能
+ key_buffer_size：表示所有缓冲区的大小。索引缓冲区是所有的线程共享。增加索引缓冲区可以得到更好处理的索引（对所有读和多重写）。当然，这个值不是越大越好，他的大小取决于内存的大小，如果这个值太大，就会导致操作系统频繁换页，降低系统性能。对于内存在4GB左右的服务器该参数可以设置为256M或384M
+ table_cache：表示同时打开的表的个数，这个值越大，能够同时打开的表的个数越多。物理内存越大，设置就越大。默认为2402，调到512-1024最佳。这个值不是越大越好，因为同时打开表太对会影响操作系统的性能
+ query_cache_size：表示查询缓冲区的大小。可以通过在MySQL控制台观察，如果Qcache_lowmem_prunes的值非常大，则表明经常出现缓冲不够的情况，就要增加query_cache_size的值；如果Qcache_hits的值非常大，则表明查询缓冲使用非常频繁，如果该值较小反而会影响效率。MySQL8.0之后失效，该参数需要和query_cache_type配合使用
+ query_cache_type的值是0时，所有的查询都不使用查询缓存区。但是query_cache_type=0并不会导致MySQL释放query_cache_size所配置的缓存区内存
  + =1时，所有的查询都将使用查询缓存区，除非在查询语句中指定SQL_NO_CACHE
  + =2时，只有在查询语句中使用SQL_CACHE关键字，查询才会使用查询缓存区
+ sort_buffer_size：表示每个需要进行排序的线程分配的缓冲区的大小。增加这个参数的值可以提高ORDER BY或GROUP BY操作的速度。默认数值是2097144字节（约2MB），对于内存在4GB左右的服务器推荐设置为6-8M，如果有100个连接，那么实际分配的总共排序缓冲区大小为100x6=600M
+ join_buffer_size=8M：表示联合查询操作所能使用的缓冲区大小，和sort_buffer_size一样，该参数对应的分配内存也是每个连接独享
+ read_buffer_size：表示每个线程连续扫描时为扫描的每个表分配的缓冲区的大小（字节）。当线程从表中连续读取记录时需要用到这个缓冲区。SET SESSION read_buffer_size=n可以设置该参数的值。默认为64k，可以设置为4M
+ innodb-flush_log_at_trx_commit：表示何时将缓冲区的数据写入日志文件，并且将日志文件写入磁盘中，该参数对于innoDB引擎非常重要，该参数有3个值，分别为0、1、2.该参数的默认值为1。
  + 值为0时，表示每秒1次的频率将数据写入日志文件并将日志文件写入磁盘。每个事务的commit并不会触发前面的操作。该模式速度最快，但不太安全，MySQL进程的崩溃会导致上一秒所有事务数据的丢失
  + 值为1时，表示每次提交事务时将数据写入日志文件并将日志文件写入磁盘进行同步。该模式是最安全的，但也是最慢的一种方式。因为每次事务提交或事务外的指令都需要把日志写入磁盘
  + 值为2时，表示每次提交事务将数据写入日志文件，每隔1秒将日志文件写入磁盘。该模式速度较快，也比0安全，只有在操作系统崩溃或者系统断电的情况下，上一秒所有事务数据才可能丢失
+ innodb_log_buffer_size：这是innodb存储引擎的事务日志所使用的缓冲区。为了提高性能，也是先将信息写入innodb log buffer中，当满足innodb_flush_log_trx_commit参数所设置的相应条件（或者日志缓冲区写满）之后，才会将日志写到（或者同步到磁盘）文件中
+ max_connections：表示允许连接到MySQL数据库的最大数量，默认是151，如果状态变量connection_errors_max_connections不为零，并且一直增长，这说明不断有连接请求因数据库连接数已达到允许最大值而失败，这时可以考虑增大max_connections的值，在Linux平台下，性能好的服务器，支持500-1000个连接不是难事，需要根据服务器性能进行评估设定。这个连接数不是越大越好，因为这些连接会浪费内存的资源。过多的连接可能会导致MySQL服务器僵死
+ back_log：用于控制MySQL监听TCP端口时设置的积压请求栈大小。如果MySQL的连接数达到max_connections时，新来的请求将会被存放在堆栈中，以等待某一连接释放资源，该堆栈的数量即back_log，如果等待连接的数量超过back_log，将不被授予连接资源，将会报错。5.6.6版本之前默认值为50，之后版本默认为50+（max_connections/5），对于Linux系统推荐设置为小于512的整数，但最大不超过900
+ thread_cache_size：线程池缓存线程数量的大小，当客户端断开连接后将当前线程缓存起来，当在接到新的连接请求时快速响应无需创建新的线程。这尤其对那些使用短链接的应用程序来说可以极大的提高创建连接的效率。那么为了提高性能可以增大该参数的值。默认为60，可以设置为120
+ wait_timeout：指定一个请求的最大连接时间，对于4GB左右内存的服务器可以设置为5-10
+ interactive_timeout：表示服务器在关闭连接前等待行动的秒数

### 事务基础知识

事务：一组逻辑操作单元，使数据从一种状态变换到另一种状态

事务处理的原则：保证所有事务都作为一个工作单元来执行，即使出现了故障，都不能改变这种执行方式。当在一个事务中执行多个操作时，要么所有的事务都被提交（commit），那么这些修改就永久的保存下来，要么数据库管理系统将放弃所有的修改，整个事务回退（rollback）到最初状态

#### ACID特性

+ 原子性（automicity）

  原子性是指事务是一个不可分割的工作单位，要么全部提交，要么全部失败回滚

+ 一致性（consistency）

  一致性是指事物执行前后，数据从一个合法性状态变换到另外一个合法性状态。满足预定的约束的状态就叫做合法的状态

+ 隔离性（isolation）

  隔离性是指一个事务的执行不能被其他事务干扰，即一个事物内部的操作及使用的数据对并发的其他事务是隔离的，并发执行的各个事物之间不能相互干扰

+ 持久性（durability）

  持久性是指一个事务一旦被提交，他对数据库中数据的改变就是永久性的，接下来的其他操作和数据库故障不应该对其有任何影响

  持久性是通过事务日志来保证的。日志包括了重做日志和回滚日志。当我们通过事务对数据进行修改的时候，首先会将数据库的变化信息记录到重做日志中，然后再对数据库中对应的行进行修改。这样做的好处是，即使数据库崩溃，数据库重启后也能找到没有更新到数据库系统中的重做日志，重新执行，从而使事务具有持久性

**事务的状态**

+ 活动的（active）

  事务对应的数据库操作正在执行过程中

+ 部分提交的（partially committed）

  当事务中的最后一个操作执行完成，但由于操作都在内存中执行，所造成的影响并没有刷新到磁盘

+ 失败的（failed）

  当事务处在活动的或者部分提交的状态时，可能遇到了某些错误而无法继续执行，或者人为的停止当前事务的执行

+ 中止的（aborted）

  如果事务执行了一部分而变成失败的状态，那么就需要把已经修改的事务中的操作还原到事务执行前的状态。换句话说，就是要撤销失败事务对当前数据库造成的影响，我们把这个撤销过程称之为回滚。当回滚操作执行完毕时，也就是数据库恢复到了执行事务之前的状态，我们就说该事务处在了中止的状态

+ 提交的（committed）

  当一个处在部分提交状态的事务将修改过的数据都同步到磁盘上之后

#### 显式事务

START TRANSACTION或者BEGIN，作用是显式开启一个事务

START TRANSACTION相较于BEGIN特别之处在于，后边能跟随几个修饰符：

+ READ ONLY：标识当前事务是一个只读事务，也就是属于该事务的数据库操作只能读取数据，而不能修改数据
+ READ WRITE：默认，标识当前事务是一个读写事务
+ WITH CONSISTENT SNAPSHOT：启动一致性读

```mysql
#提交事务
COMMIT;
#回滚事务
ROLLBACK;
#将事务回滚到某个保存点
ROLLBACK TO [SAVEPOINT]

#在事务中创建保存点
SAVEPOINT 保存点名称;
#删除保存点
RELEASE SAVEPOINT 保存点名称;
```

#### 隐式事务

默认情况下，如果我们不显式的使用START TRANSACTION或者BEGIN开启一个事务，那么每一条语句都算是一个独立的事务，这种特性称之为事务的自动提交。

```mysql
#查看系统变量autocommit
SHOW VARIABLES LIKE 'autocommit';

#关闭自动提交
#方式一：显式的使用START TRANSACTION或者BEGIN
#方式二：关闭autocommit，针对DML操作是有效的，对DDL操作是无效的
SET autocommit=OFF;
或
SET autocommit=0;
```

**隐式提交数据的情况**

+ 数据定义语言（DDL）

  数据库对象，指的是数据库、表、视图、存储过程等结构。当我们使用CREATE、ALTER、DROP等语句去修改数据库对象时，就会隐式的提交前边的语句所属于的事务

+ 隐式使用或修改MySQL中数据库中的表

  当我们使用ALTER USER、CREATE USER、DROP USER、GRANT、RENAME USER、REVOKE、SET PASSWORD等语句时也会隐式的提交前边语句所属于的事务

+ 事务控制或关于锁定的语言

  + 当我们在一个事务还没提交或回滚时就又使用START TRANSACTION或者BEGIN语句开启了另一个事务时，会隐式的提交上一个事务
  + 当前的autocommit系统变量为OFF，我们手动把他改为ON时，也会隐式的提交前边的事务
  + 使用LOCK TABLES、UNLOCK TABLES等关于锁定的语句也会隐式的提交前边的事务

+ 加载数据的语句

  LOAD DATA

+ 关于MySQL复制的一些语句

  START SLAVE、DROP SLAVE、RESET SLAVE、CHANGE MASTER TO 

+ 其他的一些语句

  ANALYZE TABLE、CACHE TABLE、CHECK TABLE、FLUSH、LOAD INDEX INTO CACHE、OPTIMIZE TABLE、REPAIR TABLE、RESET

系统变量completion_type

+ completion=0，默认情况。当我们执行COMMIT的时候会提交事务，在执行下一个事务时，还需要使用START TRANSACTION或者BEGIN来开启
+ completion=1，当我们提交事务后，相当于执行了COMMIT AND CHAIN，也就是开启一个链式事务，即当我们提交事务之后会开启一个相同隔离级别的事务
+ completion=2，这种情况下COMMIT=COMMIT AND RELEASE，也就是当我们提交后，会自动与服务器断开连接

**事务的常见分类**

+ 扁平事务（Flat Transactions）

  扁平事务是事务类型中最简单的一种，在扁平事务中，所有操作都处于同一层次，其由BEGIN WORK开始，由COMMIT WORK或ROLLBACK WORK结束

+ 带有保存点的扁平事务（Flat Transactions with Savepoints）

+ 链事务（Chained Transactions）

  一个事务由多个子事务链式组成，他可以视为保存点模式的一个变种。在提交一个事务时，释放不需要的数据对象，将必要的处理上下文隐式的传给下一个要开始的事务，前一个子事务的提交操作和下一个事务的开始操作合并成一个原子操作，这意味着下一个事务将看到上一个事务的结果，就好像在一个事务中进行一样

+ 嵌套事务（Nested Transactions）

  是一个层次结构空间，由一个顶层事务控制着各个层次的事务，顶层事务之下嵌套的事务被称为子事务，其控制着每一个局部的变换，子事务本身也可以是嵌套事务。

+ 分布式事务(（Distributed Transactions）

  通常是在一个分布式环境下运行的扁平事务	，因此，需要根据数据所在位置访问网络中不同节点的数据库资源

#### 事务隔离级别

**数据并发问题**

1. 脏写（Dirty Write）

   对于两个事务Session A、Session B，如果事务Session A修改了另一个未提交事务Session B修改过的数据，那就意味着发生了脏写

   ![](/img/mysql_28.png)

2. 脏读（Dirty Read）

   对于两个事务Session A、Session B，Session A读取了已经被Session B更新但还没有提交的字段。之后若Session B回滚，Session A读取的内容就是临时且无效的

   ![](/img/mysql_29.png)

3. 不可重复读（Non-Repeatable Read）

   对于两个事务Session A、Session B，Session A读取了一个字段，然后Session B更新了该字段。之后Session A再次读取同一个字段，值就不同了

   ![](/img/mysql_30.png)

4. 幻读（Phantom）

   对于两个事务Session A、Session B，Session A从一个表中读取了一个字段，然后Session B在该表中插入了一些新的行。之后，如果Session A再次读取同一个表就会多出几行

   ![](/img/mysql_31.png)

**四种隔离级别**

按问题严重程度：脏写>脏读>不可重复读>幻读

+ READ UNCOMMITTED：读未提交，在该隔离级别，所有事务都可以看到其他未提交事务的执行结果，不能避免脏读、不可重复读、幻读
+ READ COMMITTED：读已提交，一个事务只能看见已经提交事务所做的改变，这是大多数数据库系统的默认隔离级别，但不是MySQL默认的，可以避免脏读，但不可重复读、幻读问题依然存在
+ REPEATABLE READ：可重复读，事务A在读到一条数据之后，此时事务B对该数据进行了修改并提交，那么事务A再读该数据，读到的还是原来的内容。可以避免脏读、不可重复读，但幻读问题依然存在，MySQL的默认隔离级别
+ SERIALIZABLE：可串行化，确保事务可以从一个表读取相同的行。在这个事务持续期间，禁止其他事务对该表执行插入、更新和删除操作。所有并发问题都可以避免，但性能十分低下。

```mysql
#查看隔离级别，MySQL5.7.20之前
SHOW VARIABLES LIKE 'tx_isolation';

#查看隔离级别，MySQL5.7.20及之后
SHOW VARIABLES LIKE 'transaction_isolation';

#查看隔离级别，不同版本都可以使用
SELECT @@transaction_isolation;

#修改事务的隔离级别
SET [GLOBAL|SESSION] TRANSACTION ISOLATION LEVEL 隔离级别;
#隔离级别格式
READ UNCOMMITTED
READ COMMITTED
REPEATABLE READ
SERIALIZABLE
或
SET [GLOBAL|SESSION] TRANSACTION_ISOLATION = '隔离级别';
#隔离级别格式
READ-UNCOMMITTED
READ-COMMITTED
REPEATABLE-READ
SERIALIZABLE
#GLOBAL：当前已经存在的会话无效，只对执行完该语句之后产生的会话起作用
#SESSION：对当前会话的所有后续的事务有效，如果再事务之间执行，则对后续的事务有效，该语句可以在已经开启的事务中间执行，但不会影响当前正在执行的事务

```

### MySQL事务日志

事务的隔离性由锁机制实现

而事务的原子性、一致性和持久性由事务的redo日志和undo日志来保证

REDO LOG称为重做日志，提供再写入操作，恢复提交事务修改的页操作，用来保证事务的持久性。是存储引擎生成的日志，记录的是物理级别上的页修改操作，比如页号xxx、偏移量yyy写入了zzz数据。主要是为了保证数据的可靠性

UNDO LOG称为回滚日志，回滚行记录到某个特定版本，用来保证事务的原子性、一致性。是存储引擎层生成的日志，记录的是逻辑操作日志，比如对某一行数据进行了INSERT语句操作，那么undo log就记录一条与之相反的DELETE操作。主要用于事务的回滚和一致性非锁定读（MVCC）

#### redo日志

InnoDB引擎的事务采用了WAL技术(Write-Ahead Logging)，这种技术的思想就是先写日志，再写磁盘，只有日志写入成功，才算事务提交成功，这里的日志就是redo log。当发生宕机且数据未刷到磁盘的时候，可以通过redo log来恢复，保证ACID中的D，这就是redo log的作用

好处

+ redo日志降低了刷盘频率
+ redo日志占用的空间非常小

存储表空间ID、页号、偏移量以及需要更新的值，所需的存储空间是很小的，刷盘快

特点

+ redo日志是顺序写入磁盘的

  在执行事务的过程中，每执行一条语句，就可能产生若干条redo日志，这些日志是按照产生的顺序写入磁盘的，也就是使用顺序IO，效率比随机IO快

+ 事务执行过程中，redo log不断记录

  redo log跟bin log的区别，redo log是存储引擎层产生的，而bin log是数据库层产生的。假设一个事务，对表做10万行的记录插入，在这个过程中，一直不断的往redo log顺序记录，而bin log不会记录，直到这个事务提交，才会一次写入到bin log文件中

**redo的组成**

+ 重做日志的缓存（redo log buffer），保存在内存中，是易失的

  在服务器启动时就向操作系统申请了一大片称之为redo log buffer的连续内存空间，翻译成中文就是日志缓冲区。这片内存空间被划分成若干个连续的redo log block。一个redo log block占用512字节大小

  ![](/img/mysql_32.png)

  redo log buffer大小，默认16M，最大值是4096M，最小值为1M

  ```mysql
  #查看innodb_log_buffer_size
  show variables like '%innodb_log_buffer_size%'
  ```

  

+ 重做日志文件（redo log file），保存在硬盘中，是持久的

  ib_logfile0，ib_logfile1即为redo日志

**redo的整体流程**

![](/img/mysql_33.png)

1. 先将原始数据从磁盘中读入内存中来，修改数据的内存拷贝
2. 生成一条重做日志并写入redo log buffer，记录的是数据修改后的值
3. 当事务commit时，将redo log buffer中的内容刷新到redo log file，对redo log file采用追加写的方式
4. 定期将内存中修改的数据刷新到磁盘中

**redo log的刷盘策略**

redo log buffer刷盘到redo log file的过程并不是真正的刷到磁盘中去，只是刷入到文件系统缓存（page cache）中去（这是现代操作系统为了提高文件写入效率做的一个优化），真正的写入会交给系统自己来决定（比如page cache足够大了）。那么对于InnoDB来说就存在一个问题，如果交给系统来同步，同样如果系统宕机，那么数据也丢失了

针对这种情况，InnDB给出innodb_flush_log_at_trx_commit参数，该参数控制commit提交事务时，如何将redo log buffer中的日志刷新到redo log file中。它支持三种策略：

+ 设置为0：表示每次事务提交时不进行刷盘操作（系统默认master thread每隔1s进行一次重做日志的同步，redo log buffer占用的空间即将达到innodb_log_buffer_size的一半的时候，后台线程会主动刷盘）
+ 设置为1：表示每次事务提交时都将进行同步，刷盘操作（默认值）
+ 设置为2：表示每次事务提交时都只把redo log buffer内容写入page cache，不进行同步。由os自己决定什么时候同步到磁盘文件

**写入redo log buffer过程**

MySQL把对底层页面中的一次原子访问的过程称为一个Mini-Transaction，简称mtr，比如，向某个索引对应的B+树中插入一条记录的过程就是一个Mini-Transaction。一个所谓的mtr可以包含一组redo日志，在进行崩溃恢复时这一组redo日志作为一个不可分割的整体

一个事务可以包含多个语句，每一条语句其实是由若干个mtr组成，每一个mtr又可以包含若干条redo日志

向log buffer中写入redo日志的过程是顺序的，也就是先往前边的block中写，当该block的空闲空间用完之后再往下一个block中写。当我们想往log buffer中写入redo日志时，第一个遇到的问题就是应该写在哪个block的哪个偏移量处，所以InnoDB的设计者特意提供了一个称之为buf_free的全局变量，该变量指明后续写入的redo日志应该写道log buffer中的哪个位置

一个mtr执行过程中可能产生若干条redo日志，这些redo日志是一个不可分隔的组，所以其实并不是每生成一条redo日志，就将其插入到log buffer中，而是每个mtr运行过程中产生的日志先暂存到一个地方，当该mtr结束的时候，将过程中产生的一组redo日志再全部复制到log buffer中。我们现在假设有两个名为T1,T2的事务，每个事务都包含2个mtr，我们给这几个mtr命名一下

+ 事务T1的两个mtr分别称为mtr_T1_1和mtr_T1_2
+ 事务T2的两个mtr分别称为mtr_T2_1和mtr_T2_2

不同的事务可能是并发执行的，所以T1、T2之间的mtr可能是交替执行的。每当一个mtr执行完，伴随该mtr生成的一组redo日志就需要被复制到log buffer中，也就是说不同事务的mtr可能是交替写入log buffer的

![](/img/mysql_34.png)

redo log block由日志头、日志体、日志尾组成。日志头占用12字节，日志尾占用8字节，所以一个block真正存储的数据就是512-12-8=492字节

**redo log file**

相关参数设置

+ innodb_log_group_home_dir：指定redo log文件组所在的路径，默认值为./，表示在数据库的数据目录下。MySQL的默认数据目录（var/lib/mysql）下有两个名为ib_logfile0和ib_logfile1的文件，log buffer中的日志默认情况下就是刷新到这两个磁盘文件中。此redo日志文件位置还可以修改
+ innodb_log_files_in_group：指明redo log file的个数，命名方式如：ib_logfile0，ib_logfile1，ib_logfile2..。默认2个，最大100个
+ innodb_flush_log_at_trx_commit：控制redo log刷新到磁盘的策略，默认为1
+ innodb_log_file_size：单个redo log文件设置大小，默认值为48M。最大值为512G，注意最大值指的是整个redo log系列文件之和

日志文件组

磁盘上的redo日志文件不只一个，而是以一个日志文件组的形式出现的ib_logfile0，ib_logfile1，ib_logfile2...每个redo日志文件大小是一样的

在将redo日志写入日志文件组时，是从ib_logfile0开始写，如果ib_logfile0写满了，就接着ib_logfile1写，如果写到最后一个文件，那就重新转到ib_logfile0继续写

在整个日志文件组中还有两个重要的属性，分别是write pos、checkpoint

+ write pos是当前记录的位置，一边写一边后移
+ checkpoint是当前要擦除的位置，也是往后推移

每次刷盘redo log记录到日志文件组中，write pos位置就会后移更新。每次MySQL加载日志文件组恢复数据时，会清空加载过的redo log记录，并把checkpoint后移更新。write pos和checkpoint之间还空着的部分可以用来写入新的redo log记录

如果write pos追上checkpoint，表示日志文件组满了，这时候不能再写入新的redo log记录，MySQL得停下来清空一些记录，把checkpoint推进一下

![](/img/mysql_35.png)

#### undo日志

redo log是事务持久性的保证，undo log是事务原子性的保证。在事务中更新数据的前置操作其实是要先写入一个undo log

事务需要保证原子性，也就是事务中的操作要么全部完成，要么什么也不做。但有时候事务执行到一半会出现一些情况，比如：

+ 事务执行过程中可能遇到各种错误，比如服务器本身的错误，操作系统错误，甚至是突然断电导致的错误
+ 程序员可以在事务执行过程中手动输入ROLLBACK结束当前事务的运行

以上情况，我们需要把数据改回原来的样子，这个过程称之为回滚，这样就可以造成一个假象：这个事务看起来什么都没做，所以符合原子性要求

每当我们要对一条记录做改动时（INSERT、DELETE、UPDATE）都需要留一手，把回滚时所需的东西记下来。比如：

+ 插入一条记录时，至少要把这条记录的主键值记下来，之后回滚的时候只需要把这个主键值对应的记录删除就好了（对于每个INSERT，InnoDB存储引擎会完成一个DELETE）
+ 删除一条记录时，至少要把这条记录中的内容都记下来，这样之后回滚时再把这些内容组成的记录插入到表中就好了。（对于每个DELETE，InnoDB存储引擎会执行一个INSERT）
+ 修改了一条记录，至少要把修改这条记录前的旧值都记录下来，这样之后回滚时把这条记录更新为旧值就好了（对于每个UPDATE，InnoDB存储引擎会执行一个相反的UPDATE，将修改前的行放回去）

MySQL把这些为了回滚而记录的这些内容称之为撤销日志或者回滚日志，undo log会产生redo log，也就是undo log的产生会伴随着redo log的产生，这是因为undo log也需要持久性的保护

**undo日志的作用**

+ 回滚数据
+ MVCC

**undo的存储结构**

InnoDB对undo log的管理采用段的方式，也就是回滚段（rollback segment），每个回滚段记录了1024个undo log segment，而在每个undo log segment段中进行undo页的申请

+ 在InnoDB1.1版本之前（不包括1.1），只有一个rollback segment，因此支持同时在线的事务限制为1024
+ 从1.1版本开始InnoDB支持最大128个rollback segment，故其支持同时在线的事务限制提高到了128*1024

虽然1.1版本支持了128个rollback segment，但是这些rollback segment都存储于共享表空间ibdata中。从1.2版本开始，可通过参数对rollback segment做进一步的设置。这些参数包括：

+ innodb_undo_directory：设置rollback segment文件所在的路径。这意味着rollback segment可以存放在共享表空间以外的位置，即可以设置为独立表空间。该参数的默认值为./，表示当前InnoDB存储引擎的目录
+ innodb_undo_logs：设置rollback segment的个数，默认值为128，在1.2版本中，该参数用来替代之前版本的参数innodb_rollback_segments
+ innodb_undo_tablespaces：设置构成rollback segment文件的数量，这样rollback segment可以较为平均的分布在多个文件中。设置该参数后，会在路径innodb_undo_directory看到undo为前缀的文件，该文件就代表rollback segment文件，最少为2

undo log相关参数一般很少改动

undo页可以重用，当事务提交时，并不会立刻删除undo页。因为重用，所以这个undo页可能混杂着其他事务的undo log。undo log在commit之后，会放到一个链表中，然后判断undo页的使用空间是否小于3/4，如果小于3/4，则表示当前的undo页可以被重用，那么他就不会被回收，其他事务的undo log可以记录在当前undo页的后面。由于undo log是离散的，所以清理对应的磁盘空间时，效率不高

**回滚段与事务**

1. 每个事务只会使用一个回滚段，一个回滚段在同一时刻可能服务于多个事务
2. 当一个事务开始的时候，会制定一个回滚段，在事务进行的过程中，当数据被修改时，原始的数据会被复制到回滚段中
3. 在回滚段中，事务会不断填充盘区，直到事务结束或所有的空间被用完。如果当前的盘区不够用，事务会在段中请求扩展下一个盘区，如果所有已分配的盘区都被用完，事务会覆盖最初的盘区或者在回滚段允许的情况下扩展新的盘区来使用
4. 回滚段存在于undo表空间中，在数据库中可以存在多个undo表空间，但同一时刻只能使用一个undo表空间
5. 当事务提交时，InnoDB存储引擎会做以下两件事情：
   + 将undo log放入列表中，以供之后的purge操作
   + 判断undo log所在的页是否可以重用，若可以分配给下个事务使用

**回滚段中的数据分类**

+ 未提交的回滚数据（uncommited undo information）：该数据所关联的事务并未提交，用于实现读一致性，所以该数据不能被其他事务的数据覆盖
+ 已经提交但未过期的回滚数据（commited undo information）：该数据关联的事务已经提交，但是仍然受到undo retention参数的保持时间的影响
+ 事务已经提交并过期的数据（expired undo information）：事务已经提交，而且数据保存时间已经超过undo retention参数指定的时间，属于已经过期的数据。当事务回滚段满了之后，会优先覆盖事务已经提交并过期的数据

**undo的类型**

+ insert undo log

  insert undo log是指在insert操作中产生的undo log。因为insert操作的记录，只对事务本身可见，对其他事务不可见（这是事务隔离性的要求），故该undo log可以在事务提交后直接删除。不需要进行purge操作

+ update undo log

  update undo log记录的是对delete和update操作产生的undo log。该undo log可能需要提供MVCC机制，因此不能在事务提交时就进行删除。提交时放入undo log链表，等待purge线程进行最后的删除

**undo log的生命周期**

假设由两个数值，分别为A=1和B=2，然后将A修改为3，B修改为4

1. start transaction;
2. 记录A=1到undo log;
3. update A=3;
4. 记录A=3到redo log
5. 记录B=2到undo log;
6. update B=4;
7. 记录B=4到redo log
8. 将redo log刷新到磁盘
9. commit;

+ 在1-8步骤的任意一步系统宕机，事务未提交，该事务就不会对磁盘上的数据做任何影响
+ 如果在8-9之间宕机，恢复后可以选择回滚，也可以选择继续完成事务提交，因为此时redo log以及持久化
+ 若在9之后系统宕机，内存映射中变更的数据还来不及刷回磁盘，那么系统恢复后，可以根据redo log把数据刷回磁盘

![](/img/mysql_36.png)

purge线程两个主要作用是：清理undo页和清除page里面带有Delete_Bit标识的数据行

![](/img/mysql_37.png)

### 锁

事务的隔离性由锁实现

#### 锁的分类

**从数据操作的类型划分：读锁、写锁**

+ 读锁：也称为共享锁、用S表示。针对同一份数据，多个事务的读操作可以同时进行而不会互相影响，互相不阻塞的
+ 写锁：也称为排他锁、用X表示。当前写操作没有完成前，他会阻断写锁和读锁。这样能确保在给定的时间里，只有一个事务能执行写入，并防止其他用户读取正在写入的同一资源

对于InnoDB引擎来说，读锁和写锁可以加在表上，也可以加在行上

**锁定读**

在采用加锁方式解决脏读、不可重复读、幻读这些问题时，读取一条记录时需要获取该记录的S锁，其实是不严谨的，有时候需要在读取记录时就获取记录的X锁，来禁止别的事务读写该记录，为此MySQL提出了两种比较特殊的SELECT语句格式：

+ 对读取的记录加S锁

  ```mysql
  SELECT ... LOCK IN SHARE MODE;
  或
  SELECT ... FOR SHARE; #8.0新增语法
  ```

+ 对读取的记录加X锁

  ```mysql
  SELECT ... FOR UPDATE;
  ```

MySQL8.0新特性：

在5.7及之前的版本，SELECT...FOR UPDATE，如果获取不到锁，会一直等待，直到innodb_lock_wait_timeout超时。在8.0版本中，SELECT...FOR UPDATE，SELECT...FOR SHARE添加NOWAIT、SKIP LOCKED语法，跳过锁等待，或者跳过锁定。

通过添加NOWAIT、SKIP LOCKED语法，能够立即返回。如果查询的行已经加锁，那么NOWAIT会立即报错返回，而SKIP LOCKED也会立即返回，只是返回的结果中不包含被锁定的行

**写操作**

+ DELETE

  对一条记录做DELETE操作的过程其实是现在B+树中定位到这条记录的位置，然后获取这条记录的X锁，再执行delete mark操作。我们也可以把这个定位待删除的记录在B+树中的位置的过程看做是一个获取X锁的锁定读

+ UPDATE

  + 未修改该记录的键值，并且被更新的列占用的存储空间在修改前后未发生变化

    则先在B+树中定位到这条记录的位置，然后再获取一下记录的X锁，最后在原记录的位置进行修改操作，我们也可以把这个定位待修改的记录在B+树中的位置的过程看做是一个获取X锁的锁定读

  + 未修改该记录的键值，并且至少有一个被更新的列占用的存储空间在修改前后发生变化。

    则先在B+树中定位到这条记录的位置，然后获取一下记录的X锁，将该记录彻底删除掉后（就是把记录彻底移入垃圾链表），然后再插入一条新记录。这个定位待修改的记录在B+树中的位置的过程看做是一个获取X锁的锁定读，新插入的记录由INSERT操作提供的隐式锁进行保护

  + 修改了该记录的键值，则相当于在原记录上做DELETE操作之后再来一次INSERT操作，加锁操作就需要按照DELETE和INSERT的规则进行了

+ INSERT

  一般情况下，新插入一条记录的操作并不加锁，通过一种称为隐式锁的结构保护这条新插入的记录在本事务提交前不被别的事务访问

**从数据操作的粒度划分：表级锁、页级锁、行锁**

+ 表锁

  MySQL中最基本的锁策略，并不依赖于存储引擎，并且表锁是开销最小的策略。由于表锁一次会将整个表锁定，所以可以很好的避免死锁问题，但并发率大打折扣。

  + 表级别的S锁、X锁

    在对某个表执行SELECT、INSERT、DELETE、UPDATE语句时，InnoDB存储引擎是不会为这个表添加表级别的S锁或者X锁的。在对一个表执行一些诸如ALTER TABLE、DROP TABLE这类的DDL语句时，其他事务对这个表并发执行诸如SELECT、INSERT、DELETE、UPDATE语句时会发生阻塞。同理，某个事务对某个表执行SELECT、INSERT、DELETE、UPDATE语句时，在其他会话中对这个表执行DDL语句也会发生阻塞。这个过程其实是通过在server层使用一种称之为元数据锁（Metadata Locks，简称MDL）结构来实现的

    一般不会使用InnoDB存储引擎提供的表级别的S锁和X锁。只会在一些特殊情况下，比方说崩溃恢复过程中用到。比如，在系统变量autocommit=0，innodb_table_locks=1时，手动获取InnoDB存储引擎提供的表t的S锁或者X锁可以这么写

    + LOCK TABLES t READ：InnoDB存储引擎会对表t加表级别的S锁
    + LOCK TABLES t WRITE：InnoDB存储引擎会对表t加表级别的X锁

    尽量避免在InnoDB存储引擎的表上使用LOCK TABLES这样的手动锁表语句，他们并不会提供额外的保护，只会降级并发能力，InnoDB的厉害之处还是实现了更细粒度的行锁

    MyISAM在执行查询语句前，会给涉及的所有表加读锁，在执行增删改操作前，会给涉及的表加写锁。InnoDB存储引擎是不会为这个表添加表级别的读锁或者写锁的

  + 意向锁（intention lock）

    InnoDB支持多粒度所（multiple granularity locking），它允许行级锁和表级锁共存，而意向锁就是其中的一种表锁

    1. 意向锁的存在是为了协调行锁和表锁的关系，支持多粒度的锁并存
    2. 意向锁是一种不与行级锁冲突表级锁
    3. 表明某个事务正在某些行持有了锁或该事务准备去持有锁

    意向锁分为两种：

    + 意向共享锁（intention shared lock，IS）：事务有意向对表中的某些行加共享锁

      ```mysql
      #事务要获取某些行的S锁，必须先获取表的IS锁
      SELECT column FROM table ... LOCK IN SHARE MODE;
      ```

    + 意向排它锁（intention exclusive lock，IX）：事务有意向对表中的某些行加排他锁

      ```mysql
      #事务要获取某些行的X锁，必须先获得表的IX锁
      SELECT column FROM table ... LOCK FOR UPDATE;
      ```

    意向锁是由存储引擎自己维护的，用户无法手动操作意向锁，在为数据行加共享/排它锁之前，InnoDB会先获取该数据行所在数据表的对应意向锁

    意向锁要解决的问题：

    现在有两个事务，分别是T1、T2，其中T2试图在该表级别上应用共享或排他锁，如果没有意向锁存在，那么T2就需要去检查各个页或行是否存在所；如果存在意向锁，那么此时就会受到由T1控制的表级别意向锁的阻塞。T2在锁定该表前不必检查各个页或行锁，而只需检查表上的意向锁。简单来说就是给更大一级别的空间示意里面是否已经上过锁

    如果我们给某一行数据加上了排他锁，数据库会自动给更大一级的空间，比如数据页或数据表加上意向锁，告诉其他人这个数据页或数据表已经有人上过排他锁了

    |                  | 意向共享锁（IS） | 意向排他锁（IX） |
    | ---------------- | ---------------- | ---------------- |
    | 意向共享锁（IS） | 兼容             | 兼容             |
    | 意向排他锁（IX） | 兼容             | 兼容             |
    | 共享锁（S）      | 兼容             | 互斥             |
    | 排他锁（X）      | 互斥             | 互斥             |

  + 自增锁（AUTO-INC锁）

    插入数据的方式总共分为三类：

    1. Simple inserts（简单插入）

       可以预先确定要插入的行数，包括没有嵌套子查询的单行和多行INSERT ... VALUES()和REPLACE语句

    2. Bulk inserts（批量插入）

       事先不知道要插入的行数，比如INSERT...SELECT，REPLACE。。。SELECT和LOAD DATA语句，但不包括纯INSERT。

    3. Mixed-mode inserts（混合模式插入）

       这些是Simple inserts语句但是指定部分新行的自动递增值。例如INSERT INTO teacher(id,name) VALUES (1,'a'),(NULL,'b'),(5,'c')；只是指定了部分id的值。另一种类型的混合模式插入是INSERT ... ON DUPLICATE KEY UPDATE

    对于上面数据插入的案例，MySQL中采用了自增锁的方式来实现，AUTO-INC锁是当向使用含有AUTO_INCREMENT列的表中插入数据时需要获取的一种特殊的表级锁，在执行插入语句时就在表级别加一个AUTO-INC锁，然后为每条待插入记录的AUTO_INCREMENT修饰的列分配递增的值，在该语句执行结束后，再把AUTO-INC锁释放掉。一个事务在持有AUTO-INC锁的过程中，其他事务的插入语句要被阻塞，可以保证一个语句中分配的递增值是连续的。也正因为此，其并发性显然并不高，当我们向一个有AUTO_INCREMENT关键字的主键插入值的时候，每条语句都要对这个表进行竞争，这样的并发潜力其实是很低下的，所以innodb通过innodb_autoinc_lock_mode的不同取值来提供不同的锁定机制，来显著提高SQL语句的可伸缩性和性能

    innodb_autoinc_lock_mode有三种取值：

    1. 0（传统锁定模式）

       所有类型的insert语句都会获得一个特殊的表级AUTO-INC锁，用于插入具有AUTO_INCREMENT列的表。因为是表级锁，会限制并发能力

    2. 1（连续锁定模式）

       在MySQL8.0之前，连续锁定模式是默认的。在这个模式下，bulk inserts仍然使用AUTO-INC表级锁，并保持到语句结束。对于Simple inserts，则通过在mutex（轻量锁）的控制下获得所需数量的自动递增值来避免表级AUTO-INC锁，它只在分配过程的持续时间内保持，而不是直到语句完成。不使用表级AUTO-INC锁，除非AUTO-INC锁由另一个事务保持。

    3. 2（交错锁定模式）

       从MySQL8.0开始，交错锁模式是默认设置。在这种锁定模式下，所有类INSERT语句都不会使用表级AUTO-INC锁，并且可以同时执行多个语句。这是最快和最可扩展的锁定模式，但是当使用基于语句的复制或恢复方案时，从二进制文件重播SQL语句时，这是不安全的。在此锁定模式下，自动递增值保证在所有并发执行的所有类型的insert语句中是唯一且单调递增的。但是，由于多个语句可以同时生成数字，为任何给定语句插入的行生成的值可能不是连续的

  + 元数据锁（MDL锁）

    MySQL5.5引入了meta data lock，简称MDL锁，属于表锁范畴。MDL的作用是保证读写的正确性，当对一个表做增删改查操作时，加MDL读锁，当要对表做结构变更操作的时候，加MDL写锁，解决了DML和DDL操作之间的一致性问题。不需要显式调用，在访问一个表的时候会被自动加上

+ InnoDB中的行锁

  行级锁只在存储引擎层实现

  优点：锁定粒度小，发生锁冲突概率低，可以实现的并发度高

  缺点：锁的开销比较大，加锁会比较慢，容易出现死锁情况

  + 记录锁（Record Locks）

    记录锁也就是仅仅把一条记录锁上，官方的类型名称为：LOCK_REC_NOT_GAP

    记录锁是有S锁和X锁之分的，称之为S型记录锁和X型记录锁

  + 间隙锁（Gap Locks）

    MySQL在REPEATABLE READ隔离级别下是可以解决幻读问题的，解决方案有两种，可以使用MVCC方案解决，也可以采用加锁方案解决。但是在使用加锁方案时有个大问题，就是在事务第一次执行读取操作的时候，那些幻影记录尚不存在，我们无法给这些幻影记录加上记录锁。InnoDB提出了一种称之为Gap Locks的锁，官方的类型名称为LOCK_GAP，我们可以简称为gap锁。

    gap锁的提出仅仅是为了防止插入幻影记录而提出的。虽然有共享gap锁和独占gap锁，但是他们的作用是相同的。而且如果对一条记录加了gap锁（不论是共享gap锁还是独占gap锁），并不会限制其他事务对这条记录加记录锁或者继续加gap锁

    注意，给一条记录加了gap锁只是不允许其他事务往这条记录前边的间隙插入新记录，那对于最后一条记录之后的间隙该如何呢？这时讲数据页介绍的两条伪记录派上用场了

    + Infimum记录，表示该页面中最小的记录
    + Supremum记录，表示该页面中最大的记录

    为了实现阻止其他事务插入id值在（20，+∞）这个区间的新记录，我们可以给索引中的最后一条记录，也就是id值为20的那条记录所在页面的Supremum记录加上一个gap锁

    间隙锁可能会导致死锁的问题

  + 临键锁（Next-Key Locks）

    有时候我们既想锁住某条记录，又想阻止其他事务在该记录前边的间隙插入新记录，所以InnoDB就提出了一种称之为Next-key Locks的锁，官方的类型名称为：LOCK_ORDINARY，我们也可以简称为next-key锁。Next-Key Locks是在存储引擎innodb、事务级别在可重复读的情况下使用的数据库锁，innodb默认的锁就是Next-Key Locks

    next-key锁的本质就是一个记录锁和一个gap锁的合体，它既能保护该条记录，又能阻止别的事务将新记录插入被保护记录前边的间隙

  + 插入意向锁（Insert Intention Locks）

    一个事务在插入一条记录时需要判断一下插入位置是不是被别的事务加了gap锁（next-key锁也包含gap锁），如果有的话，插入操作需要等待，直到拥有gap锁的那个事务提交。但是InnoDB规定事务在等待的时候也需要在内存中生成一个锁结构，表明有事务想在某个间隙中插入新记录，但是现在正在等待。InnoDB就把这种类型的锁命名为Insert Intention Locks，官方的类型名称为LOCK_INSERT_INTENTION，我们称为插入意向锁。插入意向锁是一种Gap锁，不是意向锁，在insert操作时产生。

    插入意向锁的特性：

    1. 插入意向锁是一种特殊的间隙锁
    2. 插入意向锁之间互不排斥，所以即使多个事务在同一区间插入多条记录，只要记录本身不冲突，那么事物之间就不会出现冲突等待

    插入意向锁并不会阻止别的事务继续获取该纪录上任何类型的锁

+ 页锁

  页锁的开销介于表锁和行锁之间，会出现死锁。锁定粒度介于表锁和行锁之间，并发度一般

  每个层级的锁数量是有限制的，因为锁会占用内存空间，锁空间的大小是有限的。当某个层级的锁数量超过了这个层级的阈值时，就会进行锁升级。锁升级就是用更大粒度的锁替代多个更小粒度的锁，比如InnodDB中的行锁升级为表锁，这样做的好处是占用的锁空间降低了，但同时数据的并发度也降低了

**从对待锁的态度划分乐观锁、悲观锁**

+ 悲观锁（Pessimistic Locking）

  悲观锁是一种思想，顾名思义，就是很悲观，对数据被其他事务的修改持保守态度，会通过数据库自身的锁机制来实现，从而保证数据操作的排它性。

  悲观锁总是假设最坏的情况，每次去拿数据的时候都认为别人会修改，所以每次在拿数据的时候都会上锁，这样别人想拿这个数据就会阻塞直到它拿到锁（共享资源每次只给一个线程使用，其他线程阻塞，用完后再把资源转让给其他线程）。比如行锁、表锁、读锁、写锁等都是在操作之前先上锁，当其他线程想要访问数据时，都需要阻塞挂起

  select ... for update是MySQL中悲观锁，执行过程中所有扫描的行都会被锁上，因此在mysql中用悲观锁必须确定使用了索引，而不是全表扫描，否则将会把整个表锁住。

+ 乐观锁（Optimistic Locking）

  乐观锁认为对同一数据的并发操作不会总发生，属于小概率事件，不用每次都对数据上锁，但是在更新的时候会去判断一下在此期间别人有没有去更新这个数据，也就是不采用数据库自身的锁机制，而是通过程序来实现。在程序上，我们可以采用版本号机制或者CAS机制实现。乐观锁适用于多读的应用类型，这样可以提高吞吐量

  1. 乐观锁的版本号机制

     在表中设计一个版本字段version，第一次读的时候，会获取version字段的取值，然后对数据进行更新或删除操作时，会执行UPDATE...SET version=version+1 WHERE version=version。此时如果已经有事务对这条数据进行了更改，修改就不会成功

  2. 乐观锁的时间戳机制

     时间戳和版本号机制一样，也是在更新提交的时候，将当前数据的时间戳和更新之前取得的时间戳进行比较，如果两者一致则更新成功，否则就是版本冲突

**按加锁的方式划分：显式锁、隐式锁**

+ 隐式锁

  一个事务在执行INSERT操作时，如果即将插入的间隙已经被其他事务加了gap锁，那么本次INSERT操作就会阻塞，并且当前事务会在该间隙加上一个插入意向锁，否则一般情况下INSERT操作时是不加锁的。那如果一个事务首先插入了一条记录（此时并没有在内存生产与该记录关联的锁结构），然后另一个事务：

  + 立即使用SELECT...LOCK IN SHARE MODE语句读取这条记录，也就是要获取到这条记录的S锁，或者使用SELECT...FOR UPDATE语句读取这条记录，也就是要获取这条记录的X锁怎么办？

    如果允许这种情况的发生，那么可能产生脏读问题

  + 立即修改这条记录，也就是要获得这条记录的X锁，怎么办

    如果允许这种情况发生，那么可能产生脏写的问题

  这时候我们前边提的事务id又要起作用了。我们把聚簇索引和二级索引中的记录分开看一下：

  + 情景一：对于聚簇索引记录来说，有一个trx_id隐藏列，该隐藏列记录着最后改动该记录的事务id。那么如果在当前事务中新插入一条聚簇索引记录后，该记录的trx_id隐藏列代表的就是当前事务的事务id，如果其他事务此时想对该记录添加S锁或者X锁时，首先会看一下该记录的trx_id隐藏列代表的事务是否是当前的活跃事务，如果是的话，那么就帮助当前事务创建一个X锁（也就是为当前事务创建一个锁结构，is_waiting属性是false），然后自己进入等待状态（也就是为自己也创建一个锁结构，is_waiting属性是true）
  + 情景二：对于二级索引记录来说，本身并没有trx_id隐藏列，但是在二级索引页面的Page Header部分有个PAGE_MAX_TRX_ID属性，该属性代表对该页面做改动的最大的事务id，如果PAGE_MAX_TRX_ID属性值小于当前最小活跃事务id，那么说明对该页面做修改的事务都已经提交了，否则就需要在页面中定位到对应的二级索引记录，然后回表找到它对应的聚簇索引记录，然后再重复情景一的做法

  即：一个事务对新插入的记录可以不显式的加锁，但是由于事务id的存在，相当于加了一个隐式锁。别的事务在对这条记录加S锁或者X锁时，由于隐式锁的存在，会先帮助当前事务生成一个锁结构，然后自己再生成一个锁结构后进入等待状态。隐式锁是一种延迟加锁的机制，从而来减少锁的数量

+ 显式锁

  通过特定的语句进行加锁，我们一般称之为显式加锁

  ```mysql
  #显式共享锁
  select ... lock in share mode
  
  #显式排他锁
  select ... for update
  ```

**其他锁之：全局锁**

全局锁就是对整个数据库实例加锁。当你需要让整个库处于只读状态的时候，可以使用这个命令，之后其他线程的一下语句会被阻塞：数据更新的语句、数据定义语句和更新类事务的提交语句。全局锁的典型使用场景是：做全库逻辑备份

```mysql
#全局锁命令
Flush tables with read lock
```

**其它锁之：死锁**

死锁是指两个或多个事务在同一资源上相互占用，并请求锁定对方占用的资源，从而导致恶性循环

出现死锁后，有两种策略：

1. 直接进入等待，直到超时。这个超时时间可以通过参数innodb_lock_wait_timeout来设置，默认50s

2. 发起死锁检测，发现死锁后，主动回滚死锁链条中的某一个事务（将持有最少行级排他锁的事务进行回滚），让其他事务得以继续执行。将参数innodb_deadlock_detect设置为on，表示开启这个逻辑

   innodb提供wait-for graph算法来主动进行死锁检测，每当加锁请求无法立即满足需要并进入等待时，wait-for graph算法都会被触发。这是一种较为主动的死锁检测机制，要求数据库保存锁的信息链表和事务等待链表两部分信息

   ![](/img/mysql_38.png)

   基于这两个信息，可以绘制wait-for graph（等待图）

   ![](/img/mysql_39.png)

死锁检测的原理就是构建一个以事务为顶点、锁为边的有向图，判断有向图是否存在环，存在即有死锁。

**如何避免死锁**

+ 合理涉及索引，使业务SQL尽可能通过索引定位更少的行，减少锁竞争
+ 调整业务逻辑SQL执行顺序，避免update/delete长时间持有锁的SQL在事务面前
+ 避免大事务，尽量将大事务拆成多个小事务来处理，小事务缩短锁定资源的时间，发生锁冲突的几率也比较小
+ 在并发比较高的系统中，不要显式加锁，特别是在事务里显式加锁。如select ... for update，如果在事务里运行了start transaction或设置了autocommit=0，那么就会锁定所查找的记录
+ 降低隔离级别。如果业务允许，将隔离级别调低也是较好的选择，比如将隔离级别从RR调整为RC，可以避免掉很多因为gap锁造成的死锁

#### 锁的内存结构

符合下边这些条件的记录会放到一个锁结构中

+ 在同一个事务中进行加锁操作
+ 被加锁的记录在同一个页面中
+ 加锁的类型是一样的
+ 等待状态是一样的

InnoDB存储引擎中的锁结构如下：

![](/img/mysql_40.png)

1. 锁所在的事务信息

   不论是表锁还是行锁，都是在事务执行过程中产生的，哪个事务生成了这个锁结构，这里就记录这个事务的信息

   此锁所在的事务信息在内存结构中只是一个指针，通过指针可以找到内存中关于该事务的更多信息，比方说事务id等

2. 索引信息

   对于行锁来说，需要记录一下加锁的记录是属于哪个索引的。这里也是一个指针

3. 表锁/行锁信息

   + 表锁

     记载着是对哪个表加的锁，还有一些其他的信息

   + 行锁

     记载了三个重要的信息：

     + Space ID：记录所在表空间
     + Page Number：记录所在页号
     + n_bits：对于行锁来说，一条记录就对应着一个比特位，一个页面中包含很多记录，用不同的比特位区分到底是哪一条记录加了锁。为此在行锁结构的末尾放置了一堆比特位，这个n_bits属性代表使用了多少比特位。n_bits的值一般都比页面中记录条数多一些，主要是为了之后在页面中插入了新记录后也不至于重新分配锁结构

4. type_mode

   这是一个32位的数，被分成了lock_mode、lock_type和rec_lock_type三个部分

   ![](/IMG/mysql_41.png)

   + 锁的模式（lock_mode），占用低4位，可选的值如下：

     + LOCK_IS（十进制的0）：表示共享意向锁，也就是IS锁
     + LOCK_IX（十进制的1）：表示独占意向锁，也就是IX锁
     + LOCK_S（十进制的2）：表示共享锁，也就是S锁
     + LOCK_X（十进制的3）：表示独占锁，也就是X锁
     + LOCK_AUTO_INC（十进制的4）：表示AUTO-INC锁

     在InnoDB存储引擎中，LOCK_IS、LOCK_IX、LOCK_AUTO_INC都算是表级锁的模式，LOCK_S和LOCK_X既可以算是表级锁的模式，也可以是行级锁的模式

   + 锁的类型（lock_type），占用低5-8，不过现阶段只有第5位和第6位被使用

     + LOCK_TABLE（十进制的16），也就是当第5个比特位置为1时，表示表级锁
     + LOCK_REC（十进制的32），也就是当第6个比特位置为1时，表示行级锁

   + 行锁的具体类型（rec_lock_type），使用其余的位来表示。只有在lock_type的值为LOCK_REC时，也就是只有在该锁为行级锁时，才会被细分为更多的类型

     + LOCK_ORDINARY（十进制的0）：表示next-key锁
     + LOCK_GAP（十进制的512）：也就是当第10个比特位置为1时，表示gap锁
     + LOCK_REC_NOT_GAP（十进制的1024）：也就是当第11个比特位置为1时，表示正经记录锁
     + LOCK_INSERT_INTENTION（十进制的2048）：也就是当第12个比特位置为1时，表示插入意向锁。

   + 基于内存空间的节省，所以把is_waiting属性放到了type_mode这个32位的数字中

     + LOCK_WAIT（十进制的256）：当第9个比特位置为1时，表示is_waiting为true，也就是当前事务尚未获取到锁，处在等待状态；当这个比特位为0时，表示is_waiting为false，也就是当前事务获取锁成功

5. 其他信息：

   为了更好的管理系统运行过程中生成的各种锁结构而设计了各种哈希表和链表

6. 一堆比特位

   如果是行锁结构的话，在该结构末尾还放置了一堆比特位，比特位的数量是由上边提到的n_bits属性表示的。InnoDB数据页中的每条记录在记录头中都包含一个heap_no属性，伪记录Infimum的heap_no值为0，Supremum的heap_no值为1，之后每插入一条记录，heap_no值就增1.锁结构最后一堆比特位就对应着一个页面中的记录，一个比特位映射一个heap_no，即一个比特位映射到页内的一条记录

#### 锁监控

```mysql
show status like 'innodb_row_lock%'
```

各个状态量的说明如下：

+ Inndb_row_lock_current_waits：当前正在等待锁定的数量
+ Innodb_row_lock_time：从系统启动到现在锁定总时间长度
+ Innodb_row_lock_time_avg：每次等待所花平均时间
+ Innodb_row_lock_time_max：从系统启动到现在等待最长的一次所花的时间
+ Innodb_row_lock_waits：系统启动到现在总共等待的次数

**其他监控方法**

MySQL把事务和锁的信息记录在了information_schema库中，涉及到的三张表分别是INNODB_TRX、INNODB_LOCKS、INNODB_LOCK_WAITS

MySQL5.7及之前，可以通过information_schema.INNODB_LOCKS查看事务的锁情况，但是只能看到阻塞事务的锁，如果事务并未阻塞，则在该表中看不到该事务的锁情况

MySQL8.0删除了information_schema.INNODB_LOCKS，添加了performance_schema.data_locks，可以通过performance_schema.data_locks查看事务的锁情况，和MySQL5.7及之气不同，performance_schema.data_locks不但可以看到阻塞该事务的锁，还可以看到该事务所持有的锁

同时information_schema.INNODB_LOCK_WAITS也被performance_schema.data_lock_waits所替代

### 多版本并发控制

MVCC（Multiversion Concurrency Control），多版本并发控制，MVCC是通过数据行的多个版本管理来实现数据库的并发控制，这项技术使得在InnoDB的事务隔离级别下执行一致性读操作有了保证，换言之，就是为了查询一些正在被另一个事务更新的行，并且可以看到他们被更新之前的值，这样在做查询的时候就不用等待另一个事务释放锁。

#### 快照读与当前读

MVCC在InnoDB中的实现主要是为了提高数据库并发性能，用更好的方式去处理读写冲突，做到即使有读写冲突时，也能做到不加锁，非阻塞并发读，而这个读指的就是快照读，而非当前读。当前读实际上是一种加锁的操作，是悲观锁的实现。而MVCC本质是采用乐观锁思想的一种方式

**快照读**

快照读又叫一致性读，读取的是快照数据。不加锁的简单的SELECT都属于快照读，即不加锁的非阻塞读，快照读的实现是基于MVCC，他在很多情况下，避免了加锁操作，降低了开销，既然是基于多版本，那么快照读可能读到的并不一定是数据的最新版本，而有可能是之前的历史版本。

快照读的前提是隔离级别不是串行级别，串行级别下的快照读会退化为当前读

**当前读**

当前读读取的是记录的最新版本（最新数据，而不是历史版本的数据），读取时还要保证其他并发事务不能修改当前记录，会对读取的记录进行加锁。加锁的SELECT，或者对数据进行增删改查都会进行当前读。

#### MVCC实现原理之ReadView

MVCC的实现依赖于：隐藏字段、Undo Log、Read View

在MVCC机制中，多个事务对同一行记录进行更新会产生多个历史快照，这些历史快照保存在Undo Log里。

ReadView就是事务在使用MVCC机制进行快照读操作时产生的读试图。当事务启动时，会生成数据库系统当前的一个快照，InnoDB为每个事务构造了一个数组，用来记录并维护系统当前活跃事务的id

使用READ UNCOMMITTED隔离级别的事务，由于可以读到未提交事务修改过的记录，所以直接读取记录的最新版本就好了。

使用SERIALIZABLE隔离级别的事务，InnoDB规定使用加锁的方式来访问记录

使用READ COMMITTED和REPEATABLE READ隔离级别的事务，都必须保证读到已经提交了的事务修改过的记录。假如另一个事务已经修改了记录但是尚未提交，是不能直接读取最新版本的记录的，核心问题就是需要判断版本链中的哪个版本是当前事务可见的，这是ReadView要解决的主要问题。

ReadView主要包含4个比较重要的内容：

1. creator_trx_id：创建这个Read View的事务ID

   只有在对表中的记录做改动时（执行INSERT、DELETE、UPDATE）才会为事务分配事务id，否则在一个只读事务中的事务id值都默认为0

2. trx_ids：表示在生成ReadView时当前系统中活跃的读写事务的事务id列表

3. up_limit_id：活跃的事务中最小的事务ID

4. low_limit_id：表示生成ReadView时系统中应该分配给下一个事务的id的值。low_limit_id是系统最大的事务id值，这里要注意是系统中的事务id，需要区别于正在活跃的事务ID

**ReadView的规则**

有了这个ReadView，这样在访问某条记录时，只需要按照下边的步骤判断记录的某个版本是否可见

+ 如果被访问版本的trx_id属性值与ReadView中的creator_trx_id值相同，意味着当前事务在访问他自己修改过的记录，所以该版本可以被当前事务访问
+ 如果被访问版本的trx_id属性值小于ReadView中的up_limit_id值，表明生成该版本的事务在当前事务生成ReadView前已经提交，所以该版本可以被当前事务访问
+ 如果被访问版本的trx_id属性值大于或等于ReadView中的low_limit_id值，表明生成该版本的事务在当前事务生成ReadView后才开启，所以该版本不可以被当前事务访问
+ 如果被访问版本的trx_id属性值在ReadView的up_limit_id和low_limit_id之间，那就需要判断一下trx_id属性值是不是在trx_ids列表中
  + 如果在，说明创建ReadView时生成该版本的事务还是活跃的，该版本不可以被访问
  + 如果不在，说明创建ReadView时生成该版本的事务已经被提交，该版本可以访问

**MVCC整体操作流程**

1. 首先获取事务自己的版本号，也就是事务id
2. 获取ReadView
3. 查询得到的数据，然后与ReadView中的事务版本号进行比较
4. 如果不符合ReadView规则，就需要从Undo Log中获取历史快照
5. 最后返回符合规则的数据

如果某个版本的数据对当前事务不可见的话，那就顺着版本链找到下一个版本的数据，继续按照上边的步骤判断可见性，以此类推，直到版本链中的最后一个版本。如果最后一个版本也不可见的话，那么就意味着该条记录对该事务完全不可见，查询结果就不包含该记录

InnoDB中，MVCC是通过Undo Log+Read View进行数据读取，Undo Log保存了历史快照，而Read View规则帮我们判断当前版本的数据是否可见

在隔离级别为读已提交时，一个事务中的每一次SELECT查询都会重新获取一次Read View，此时同样的查询语句都会重新获取一次ReadView ，这时如果ReadView不同，就可能产生不可重复读或者幻读的情况

当隔离级别为可重复读的时候，就避免了不可重复读，这时因为一个事务只在第一次SELECT的时候会获取一次ReadView，而后面所有的SELECT都会复用这个ReadView

### 其他数据库日志

**日志类型**

+ 慢查询日志：记录所有执行时间超过long_query_time的所有查询，方便我们对查询进行优化
+ 通用查询日志，记录所有连接的起始时间和终止时间，以及连接发送给数据库服务器的所有指令，对我们复原操作的实际场景、发现问题，甚至是对数据库操作的审计都有很大的帮助
+ 错误日志：记录MySQL服务器的启动、运行或停止MySQL服务时出现的问题，方便我们了解服务器的状态，从而对服务器进行维护
+ 二进制日志：记录所有更改数据的语句，可以用于主从服务器之间的数据同步，以及服务器遇到故障时数据的无损失恢复
+ 中继日志：用于主从服务器架构中，从服务器用来存放主服务器二进制日志内容的一个中间文件。从服务器通过读取中继日志的内容，来同步主服务器上的操作
+ 数据定义语句日志：记录数据定义语句执行的元数据操作

除二进制日志外，其他日志都是文本文件。默认情况下，所有日志创建于MySQL数据目录中

**日志的弊端**

+ 日志功能会降低MySQL数据库的性能
+ 日志会占用大量的磁盘空间

#### 慢查询日志（slow query log）

见性能分析工具的使用

#### 通用查询日志（general query log）

用来记录用户的所有操作，包括启动和关闭MySQL服务、所有用户的连接开始时间和截至时间、发给MySQL数据库服务器的所有SQL指令等。当我们的数据发生异常时，查看通用查询日志，还原操作时的具体场景，帮助我们准确定位问题

```mysql
#查看当前状态，默认是关闭的
show variables like '%general%';

#启动日志
#方式一：永久性方式
[mysqld]
general_log=ON
general_log_file=[path[filename]]
#方式二：临时性方式
SET GLOBAL general_log=on;

#删除日志：手动删除文件

#刷新日志,重新生成日志，前提一定要开启通用日志
mysqladmin -uroot -p flush-logs
```

#### 错误日志（error log）

记录MySQL服务器启动、停止运行的时间，以及系统启动、运行和停止过程中的诊断信息，包括错误、警告和提示

```mysql
#查看当前状态
show variables like 'log_err%';

#启动日志，默认开启，无法被禁止
#名称默认为mysqld.log(Linux系统)或hostname.err(mac系统)
[mysqld]
log-error=[path[filename]]

#删除
#方式一：直接删除
#方式二：在运行状态下删除错误日志后，MySQL并不会自动创建日志文件
1.重命名文件
2.重建日志：mysqladmin -u root -p flush-logs
可能会报错：
mysqladmin:refresh failed;error:'Could not open file' /var/log/mysqld.log'for error logging.'
补充操作
install -omysql -gmysql -m0644 /dev/null var/log/mysqld.log
```

#### 二进制日志（bin log）

记录了数据库所有执行的DDL和DML等数据库更新事件的语句，它以事件形式记录并保存在二进制文件中。

主要应用场景：

+ 数据恢复，如果MySQL数据库意外停止，可通过二进制文件来查看用户执行了哪些操作，对数据库服务器文件做了哪些修改，然后根据二进制日志文件中的记录来恢复数据库服务器
+ 数据复制，由于日志的延续性和时效性，master把它的二进制日志传递给slaves来达到master-slave数据一致的目的

```mysql
#查看默认情况,在MySQL8.0默认开启
show variables like '%log_bin%'

#日志参数设置
#方式一：永久性方式
[mysqld]
log-bin=mysql-bin #打开日志（主机需要打开），可自定义，如/home/mysql_bin_log/mysql-bin
binlog_expire_logs_seconds=600 #二进制日志文件保留时长，单位是秒
max_binlog_size=100M #控制单位二进制日志大小，最大和默认值是1GB，不能严格控制Binlog的大小
#数据库文件最好不要与日志文件放到同一个磁盘上，这样当数据库文件所在的磁盘发生故障时，可以使用日志文件恢复数据
#方式二：临时性方式,mysql8中只有会话级别的设置，没有global级别的设置
set sql_log_bin = 0

#查看日志
#当MySQL创建二进制日志文件时，先创建一个以filename为名称，以.index为后缀的文件，再创建一个以filename为名称，以.000001为后缀的文件，MySQL访问重启一次，以.000001为后缀的文件就会增加一个，并且后缀名按1递增
mysqlbinlog '/var/lib/mysql/binlog/atguitu-bin.000002'
#伪SQL形式
mysqlbinlog -v '/var/lib/mysql/binlog/atguitu-bin.000002'

#更方便的查询命令
show binlog events [IN 'log_name'] [FROM pos] [LIMIT[offset,] row_count];
1.IN 'log_name'：指定要查询的binlog文件名，不指定就是第一个
2.FROM pos：指定从哪个pos起始点开始查询，不指定就是从整个文件首个pos点开始算
3.LIMIT[offset]：偏移量，不指定就是0
4.row_count：查询总条数，不指定就是所有行

#查看binlog格式
show variables like 'binlog_format'
1.Statement
每一条都会修改数据的sql都会记录在binlog中
优点：不需要记录每一行的变化，减少了binlog日志量，节约了IO，提高性能
2.Row
5.1.5版本才开始支持row level的复制，它不记录sql语句上下文相关信息，仅保存哪条记录被修改
优点：row level的日志内容会非常清楚的记录下每一行数据修改的细节，而且不会出现某些特定情况下的存储过程或function以及trigger的调用和触发无法被正确复制的问题
3.Mixed
5.1.8版本开始，实际上就是Statement和Row的结合

#使用日志恢复数据
mysqlbinlog [option] filename|mysql -uuser -ppass;
option：可选项
1.--start-date和--stop-date：可以指定恢复数据库的起始时间点和结束时间点
2.--start-position和--stop-position：可以指定恢复数据的开始位置和结束位置
例：
/usr/bin/mysqlbinlog --start-position=464 --stop-position=1308 --database=atguigu14 /var/lib/mysql/binlog/atguigu-bin.000005 | /usr/bin/mysql -uroot -p123456 -v atguigu14

#删除二进制日志
#1.PURGE MASTER LOGS：删除指定日志文件
PURGE {MASTER|BINARY} LOGS TO '指定日志文件名'
PURGE {MASTER|BINARY} LOGS BEFORE '指定日期'
#2.RESET MASTER：删除所有二进制日志文件
RESET MASTER;

#查看二进制文件
SHOW BINARY LOGS;

#关闭当前使用的binary log，然后打开一个新的binary log文件，文件的序号加1.
flush logs
```

**写入机制**

事务执行过程中，先把日志写入binlog cache，事务提交的时候，再把binlog cache写到binlog文件中，因为一个事务的binlog不能分开，无论这个事务多大，也要确保一次性写入，所以系统会给每个线程分配一个块内存作为binlog cache

w可以通过binlog_cache_size控制单个线程binlog cache大小，如果存储内容超过了这个参数，就要暂存到磁盘，binlog日志刷盘流程如下：

![](/img/mysql_42.png)

write和fsync的时机可以由参数sync_binlog控制，默认是0。为0的时候，表示每次提交事务都只write，由系统自行判断什么时候执行fsync。虽然性能得到提升，但是机器宕机，page cache里面的binlog会丢失

为了安全起见，可以设置为1，表示每次提交事务都会执行fsync，就如同redo log刷盘一样。还有一个折中方式，可以设置为N，表示每次提交事务都write，但积累N个事务后才fsync

**binlog与redo log对比**

+ redo log他是物理日志，记录内容是在某个数据页上做了什么修改，属于InnoDB引擎层产生
+ 而bin log是逻辑日志，记录内容是语句的原始逻辑，类似于给ID=2这一行的c字段加1，属于MySQL Server层
+ 虽然他们都属于持久化的保证，但是侧重点不同，redo log让InnoDB存储引擎拥有了崩溃恢复能力，bin log保证了MySQL集群架构的数据一致性

**两阶段提交**

在执行更新语句过程，会记录redo log与bin log两块日志，以基本的事务为单位，redo log在事务执行过程中可以不断写入，而binlog只有在提交事务时才写入，所以redo log和binlog的写入时机不一样

假设执行过程中写完redo log日志后，binlog日志写期间发生了异常，由于binlog没写完就异常，这时候binlog里面没有对应的修改记录。因此之后用binlog日志恢复数据时，就会少一次更新，而原库因为redo log日志恢复，最终数据不一致。

为了解决两份日志之间的逻辑一致问题，InnoDB存储引擎使用两阶段提交方案，原理很简单，将redo log拆成了两个步骤prepare和commit，这就是两阶段提交

![](/img/mysql_43.png)

使用两阶段提交后，写入binlog时发生异常也不会有影响，因为MySQL根据redo log日志恢复数据时，发现redo log还处于prepare阶段，并且没有对应binlog日志，就会回滚该事务

redo log设置commit阶段发生异常并不会回滚事务，虽然redo log是处于prepare阶段，但是能通过事务id找到对应的binlog日志，所以MySQL认为是完整的，就会提交事务恢复数据

#### 中继日志（）

中继日志只在主从服务器架构的从服务器上存在。从服务器为了与主服务器保持一致，要从主服务器读取二进制日志的内容，并且把读取到的信息写入本地的日志文件中，这个从服务器本地的日志文件就叫中继日志。然后从服务器读取中继日志，并根据中继日志的内容对从服务器的数据进行更新，完成主从服务器的数据同步。

搭建好主从服务器后，中继日志默认会保存在从服务器的数据目录下。

文件名的格式是：从服务器名-relay-bin.序号。中继日志还有一个索引文件：从服务器名-relay-bin.index，用来定位当前正在使用的中继日志

**查看中继日志**

中继日志与二进制文件日志的格式相同，可以用mysqlbinlog工具进行查看

**恢复的典型错误**

如果从服务器宕机，有时候为了系统恢复，要重装操作系统，这样就可能导致你的服务器名称和之前不同，而中继日志里是包含从服务器名的，在这种情况下，就可能导致你恢复从服务器时，无法从宕机前的中继日志里读取数据，以为是日志文件坏了，其实是名称不对了

解决的方法也很简单，把从服务器的名称改回之前的名称

### 主从复制

#### 主从复制的作用

+ 读写分离。可以通过主从复制的方式来同步数据，然后通过读写分离提高数据库并发处理能力

  ![](/img/mysql_44.png)

  Master主库负责写入数据，称之为写库，其他都是Slave从库，负责读取数据，称之为读库

+ 数据备份

+ 具有高可用性，当服务器出现故障或宕机的情况下，可以切换到从服务器，保证服务的正常运行

#### 主从复制的原理

Slave会从Master读取binlog来进行数据同步

三个线程：

![](/img/mysql_45.png)

+ 二进制日志转储线程（Binlog dump thread）是一个主库线程。当从库线程连接的时候，主库可以将二进制文件发送给从库，当主库读取事件（Event）的时候，会在Binlog上加锁，读取完之后，再将锁释放掉
+ 从库IO线程会连接到主库，向主库发送请求更新Binlog。这时从库的IO线程就可以读取主库的二进制日志转储线程发送的Binlog更新部分，并且拷贝到本地的中继日志（Relay log
+ 从库SQL线程会读取从库中的中继日志，并且执行日志中的事件，将从库中的数据与主库保持同步

#### 一主一从架构搭建

![](/img./mysql_46.png)

**主机配置文件**

建议mysql版本一致且后台以服务运行，主从所有配置项都配置在[mysqld]节点下，且都是小写字母

```mysql
#[必须]主服务器唯一ID
server-id=1

#[必须]启用二进制日志，指明路径
log-bin=atguigu-bin

#[可选]0(默认)表示读写(主机),1表示只读(从机)
read-only=0

#设置日志文件保留的时长的，单位是秒
binlog_expire_logs_seconds=6000

#控制单个二进制日志大小,此参数的最大和默认值是1GB
max_binlog_size=200M

#[可选]设置不要复制的数据库
binlog-ignore-db=test

#[可选]设置需要复制的数据库，默认全部记录
binlog-do-db=需要复制的主数据库名字

#[可选]设置binlog格式
binlog_format=STATEMENT
```

注意：先搭建完主从复制，再创建数据库，主从机都关闭防火墙

binlog格式设置：

+ STATEMENT（基于SQL语句的复制statement-based replication SBR）

  每一条会修改数据的SQL语句会记录到binlog中，默认格式

  优点：

  + 不需要记录每一行的变化，减少了binlog日志量，文件较小
  + binlog中包含了所有数据库更改信息，可以据此来审核数据库的安全等情况
  + binlog可以用于实时的还原，而不仅仅用于复制
  + 主从版本可以不一样，从服务器版本可以比主服务器版本高

  缺点：

  + 不是所有的update语句都能被复制，尤其是包含不确定操作的时候
  + 使用以下函数的语句也无法被复制：LOAD_FILE()、UUID()、USER()、FOUND_ROWS()、SYSDATE()（除非启用了 --sysdate-is-now选项）
  + INSERT...SELECT会产生比RBR更多的行锁
  + 复制需要进行全表扫描的UPDATE时，需要比RBR请求更多的行级锁
  + 对于有AUTO_INCREMENT字段的InnoDB表而言，INSERT语句会阻塞其他INSERT语句
  + 对于一些复杂的语句，从服务器上的资源消耗情况会更严重，而RBR模式下，只会对那个发生变化的记录产生影响
  + 执行复杂语句出错的话，会消耗更多资源
  + 数据表必须几乎和主服务器保持一致才行，否则可能导致复制错误

+ ROW模式（基于行的复制row-based replication RBR）

  5.1.5版本才开始支持，不记录每条sql语句的上下文信息，仅记录哪条数据被修改了，修改成什么样子

  优点：

  + 任何情况都可以被复制，这对复制来说是最安全可靠的
  + 多数情况下，从服务器上的表如果有主键的话，复制会快很多
  + 复制以下几种语句时的行锁更少：INSERT...SELECT、包含AUTO_INCREMENT字段的INSERT、没有附带条件或者并没有修改很多记录的update或delete语句
  + 执行insert、update、delete语句时锁更少
  + 从服务器上采用多线程来执行复制成为可能

  缺点：

  + binlog大了很多
  + 复杂的回滚时binlog中会包含大量的数据
  + 主服务器上执行UPDATE语句时，所有发生变化的记录都会写到binlog中，而SBR只会写一次，这会导致频繁发生binlog的并发写问题
  + 无法从binlog中看到都复制了些什么语句

+ MIXED（混合模式复制（mixed-based replication MBR））

  5.1.8版本开始支持，实际上就是Statement和Row的结合

  在mixed模式下，一般的语句修改使用statement格式保存binlog。如一些函数，statement无法完成主从复制的操作，则采用row格式保存binlog

  MySQL会根据执行的每一条具体的sql语句来区分对待记录的日志形式，也就是在statement和row中选择一种

**从机配置文件**

```mysql
#[必须]主服务器唯一ID
server-id=2

#[可选]启用中继日志
relay-log=mysql-relay
```

**主机：建立账户并授权**

```mysql
#在主机MySQL里执行授权主从复制的命令,5.5,5.7
GRANT REPLICATION SLAVE ON *.* TO 'slave1'@'从机器数据库IP' IDENTIFIED BY 'abc123';
```

注意：如果使用的是MySQL8，需要如下的方式建立账户，并授权slave

```mysql
CREATE USER 'slave1'@'%' IDENTIFIED BY '123456';

GRANT REPLICATION SLAVE ON *.* TO 'slave1'@'%';

#此语句必须执行，否则从机执行show slave status时报错
ALTER USER 'slave1'@'%' IDENTIFIED WITH mysql_native_password BY '123456';

FLUSH PRIVILEGES;

#查询Master的状态，并记录下file和position的值
show master status;
```

**从机：配置需要复制的主机**

```mysql
#从机上复制主机的命令
CHANGE MASTER TO
MASTER_HOST='主机的IP地址',
MASTER_USER='主机用户名',
MASTER_PASSWORD='主机用户名的密码',
MASTER_LOG_FILE='mysql-bin.具体数字',
MASTER_LOG_POS=具体值

#启动slave同步
START SLAVE;

#查看同步状态
SHOW SLAVE STATUS;

#停止主从同步命令
STOP SLAVE

#如果停止从服务器复制功能，再使用需要重新配置主从，否则会报错
STOP SLAVE;
RESET MASTER;
```

#### 同步数据一致性问题

**主从同步的要求**

+ 读库和写库的数据一致（最终一致）
+ 写数据必须到写库
+ 读数据必须到读库（不一定）

**如何解决一致性问题**

读写分离情况下，解决主从同步数据不一致的问题，就是解决主从之间数据复制方式的问题，如果按照数据一致性从弱到强来进行划分，有以下3种复制方式：

+ 异步复制

  异步模式就是客户端提交COMMIT之后不需要等待从库返回任何结果，而是直接将结果返回给客户端，这样做的好处时不会影响主库写的效率，但可能会存在主库宕机，而binlog还没有同步到从库的情况，也就是此时的主库和从库数据不一致。这时候从从库中选择一个作为新主，那么新主则可能缺少原来主服务器中已提交的事务。所以，这种复制模式下的数据一致性是最弱的

  ![](/img/mysql_47.png)

+ 半同步复制

  5.5版本之后开始支持半同步复制的方式。原理是在客户端提交COMMIT之后不直接返回给客户端，而是等至少有一个从库接收到了binlog，并且写入到中继日志中，再返回给客户端

  这样做的好处就是提高了数据的一致性，当然相比于异步复制来说，至少多增加了一个网络连接的延迟，降低了主库写的效率

  5.7中还增加了一个rpl_semi_sync_master_wait_for_slave_count参数，可以对应答的从库数量进行设置，默认为1。

  ![](/img/mysql_48.png)

+ 组复制

  异步复制和半同步复制都无法最终保证数据的一致性问题，半同步复制是通过判断从库相应的个数来决定是否返回给客户端，虽然数据一致性相比于异步复制有提升，但仍然无法满足对数据一致性要求高的场景。MGR很好地弥补了这两种复制模式的不足

  组复制，简称MGR(MySQL Group Replication)，5.7.17中推出的一种新的数据复制技术，这种复制技术是基于Paxos协议的状态机复制

  MGR是如何工作的

  首先我们将多个节点共同组成一个复制组，在执行读写事务的时候，需要通过一致性协议层(Consensus层)的同意，也就是读写事务想要进行提交，必须经过组里大多数人（对应Node节点）的同意，大多数指的是同意的节点数量大于（N/2+1），这样才可以进行提交，而不是原发起方一个说了算。而针对只读事务则不需要经过组内同意，直接COMMIT即可

  在一个复制组内有多个节点组成，他们各自维护了自己的数据副本，并且在一致性协议层实现了原子消息和全局有序消息，从而保证组内数据的一致性

  ![](/img/mysql_49.png)

### 数据库备份和恢复

物理备份：备份数据文件，转储数据库物理文件到某一目录。物理备份恢复速度比较快，但占用空间比较大，MySQL中可以用xtrabackup工具来进行物理备份

逻辑备份：对数据库对象利用工具进行导出工作，汇总入备份文件内。逻辑备份恢复速度慢，但占用空间小，更灵活。MySQL中常用的逻辑备份工具为mysqldump。逻辑备份就是备份sql语句，在恢复的时候执行备份的sql语句实现数据库数据的重现

#### mysqldump实现逻辑备份

**备份一个数据库**

mysqldump命令执行时，可以将数据库备份成一个文本文件，该文件实际上包含多个CREATE和INSERT语句。

```mysql
mysqldump -u 用户名称 -h 主机名称 -p密码 待备份的数据库名称[tbname,[tbname...]] > 备份文件名称.sql
```

**备份全部数据库**

```mysql
mysqldump -u 用户名称 -h 主机名称 -p密码 --all-databases > 备份文件名称.sql
mysqldump -u 用户名称 -h 主机名称 -p密码 -A > 备份文件名称.sql
```

**备份部分数据库**

```mysql
mysqldump -u 用户名称 -h 主机名称 -p密码 --databases [数据库的名称1 [数据库的名称2...]] > 备份文件名称.sql
mysqldump -u 用户名称 -h 主机名称 -p密码 -B [数据库的名称1 [数据库的名称2...]] > 备份文件名称.sql
```

**备份部分表**

```mysql
mysqldump -u 用户名称 -h 主机名称 -p密码 数据库的名称 [表名1 [表名2...]] > 备份文件名称.sql
```

**备份单表的部分数据**

```mysql
mysqldump -u 用户名称 -h 主机名称 -p密码 数据库的名称 表名 --where="过滤条件" > 备份文件名称.sql
```

**排除某些表的备份**

```mysql
mysqldump -u 用户名称 -h 主机名称 -p密码 数据库的名称 --ignore-table=数据库名.表名 > 备份文件名称.sql
```

**只备份结构或只备份数据**

```mysql
#只备份结构
mysqldump -u 用户名称 -h 主机名称 -p密码 数据库的名称 --no-data > 备份文件名称.sql

#只备份数据
mysqldump -u 用户名称 -h 主机名称 -p密码 数据库的名称 --no-create-info > 备份文件名称.sql
```

**备份中包含存储过程、函数、事件**

使用--routines或-R来备份存储过程及函数

使用--events或-E参数来备份事件

#### mysql命令恢复数据

**单库备份中恢复单库**

```mysql
#如果备份文件中包含了创建数据库的语句
mysql -uroot -p < atguigu.sql

#否则需要指定数据库名称
mysql -uroot -p 数据库名< atguigu.sql
```

**全量备份**

```mysql
mysql -uroot -p < atguigu.sql
```

**从全量备份中恢复单库**

```mysql
sed -n '/^-- Current Database: `atguigu`/,/^-- Current Database: `/p' all_database.sql > atguigu.sql
```

**从单库备份中恢复单表**

```mysql
cat atguigu.sql | sed -e '/./{H;$!d;}' -e 'x;/CREATE TABLE `class`/!d;q' > class_structure.sql
cat atguigu.sql | grep --ignore-case 'insert into `class`' > class_data.sql
```

