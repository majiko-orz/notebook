### Redis简介

版本号第二位如果是奇数，则为非稳定版本，如2.7、2.9

版本号第二位如果是偶数，则为稳定版本，如2.6、2.8

Linux环境安装redis必须先具备gcc编译环境

+ gcc -v：查看gcc版本
+ yum -y install gcc- c++

**redis7安装步骤**

1. 下载获得redis-7.0.0.tag.gz后将它放入/opt
2. /opt目录下解压redis
3. 进入目录 cd redis-7.0.0
4. 在redis-7.0.0目录下执行make命令，make &&make install
5. 查看默认安装目录：usr/local/bin，安装完后查看
   + redis-benchmark：性能测试工具，服务启动后运行该命令
   + redis-check-aof：修复有问题的AOF文件
   + redis-check-dump：修复有问题的dump.db文件
   + redis-cli：客户端，操作入口
   + redis-sentinel：redis集群使用
   + redis-server：redis服务器启动命令
6. 将默认的redis.conf拷贝到自己定义好的一个路径下，比如/myredis
   + mkdir /myredis
   + cp redis.conf /myredis/redis7.conf
7. 修改.myredis目录下redis.conf配置文件做初始化设置，redis.conf配置文件，改完后确保生效，记得重启
   1. 默认daemonize no改为daemonize yes
   2. 默认protected-mode yes改为protected-mode no
   3. 默认bind 127.0.0.1改为直接注释掉（默认bind 127.0.0.1只能本地访问）或改成本机IP地址，否则影响远程IP连接
   4. 添加redis密码改为requirepass 密码
8. 启动服务：redis-server /myredis/redis7.conf
9. 连接服务：redis-cli -a 123456 -p 6379
10. 关闭
    + 单实例关闭：redis-cli -a 123456 shutdown
    + 多实例关闭，指定端口关闭：redis-cli -p 3679 shutdown

**卸载**

1. 停止redis-server服务：redis-cli shutdown
2. 删除/usr/local/bin目录下与redis相关的文件：rm -rf /usr/local/bin/redis-*

### Redis10大数据类型

**key操作命令**

```bash
keys * #查看当前库所有的key
exists key #判断某个key是否存在
type key #查看key是什么类型
del key #删除指定的key
unlink key #非阻塞删除，仅仅将keys从keyspace元数据中删除，真正的删除会在后续异步中操作
ttl key #查看还有多少秒过期，-1表示永不过期，-2表示已过期
expire key 秒钟 #为给定的key设置过期时间
move key dbindex [0-15] #将当前数据库的key移动到给定的数据库中
select dbindex #切换数据库0-15，默认为0
dbsize #查看当前数据库key的数量
flushdb #清空当前库
flushall #通杀全部库
```

命令不区分大小写，而key是区分大小写的

永远的帮助命令 help @类型，例如help @string

+ String：字符串
+ List：列表
+ Hash：哈希表
+ Set：集合
+ Sorted Set（ZSet）：有序集合
+ Geospatial：地理空间
+ Hyperloglog：基数统计
+ Bitmap：位图
+ Bitfield：位域
+ Stream：流

#### String

string是redis最基本的类型，一个key对应一个value

string类型是二进制安全的，意思是redis的string可以包含任何数据，比如jpg图片或序列化的对象

一个redis中字符串value最多可以是512M

**set key value**

```
SET key value [NX | XX] [GET] [EX seconds | PX milliseconds | EXAT unix-time-seconds | PXAT unix-time-milliseconds | KEEPTTL]
```

set命令有EX、PX、NX、XX以及KEEPTTL五个可选参数，其中KEEPTTL为6.0版本添加的可选参数，其他为2.6.12版本添加的可选参数

+ EX seconds：以秒为单位设置过期时间
+ PX milliseconds：以毫秒为单位设置过期时间
+ EXAT timestamp：设置以秒为单位的UNIX时间戳所对应的时间为过期时间
+ PXAT milliseconds-timestamp：设置以毫秒为单位的UNIX时间戳所对应的时间为过期时间
+ NX：键不存在的时候设置键值
+ XX：键存在的时候设置键值
+ KEEPTTL：保留设置前指定键的生存时间
+ GET：返回指定键原本的值，若键不存在时返回nil

SET命令使用EX、PX、NX参数，其效果等同于SETEX、PSETEX、SETNX命令。根据官方文档的描述，未来版本中SETEX、PSETEX、SETNX命令可能会被淘汰

EXAT、PXAT以及GET为redis6.2新增的可选参数

设置成功返回OK，返回nil为未执行SET命令，如不满足NX、XX条件等

若使用GET参数，则返回该键原来的值，或在键不存在时返回nil

如何获得设置指定的key过期的unix时间，单位为秒：Long.toString(System.currentTimeMillis()/1000L)

**同时设置/获取多个键值**

MSET key value [key value...]

MGET key [key...]

mset/mget/msetnx

msetnx k1 v1 k2 v2：key不存在再设置，如果有一个存在就失败，原子性操作

**获取指定区间范围内的值**

getrange/setrange

**数值增减**

一定要是数字才能进行加减

递增数字：INCR key

增加指定的整数：INCRBY key increment

递减数值：DECR key

减少指定的整数：DECRBY key decrement

**获取字符串长度和内容追加**

STRLEN key

APPEND key value

**分布式锁**

setnx key value

setex 键 秒值/setnx

**getset(先get再set)**

getset k v

#### List

redis列表是简单的字符串列表，按照插入顺序排序。你可以添加一个元素到列表的头部（左边）或者尾部（右边）

它的底层实际是个双端链表，最多可以包含2^32-1个元素（4294967295，每个列表超过40亿个元素）

+ lpush/rpush/lrange

+ lpop/rpop

+ lindex

+ llen

+ lrem key 数字N 给定值v1：删除N个值等于v1的元素

  lrem list3 0 值，表示删除全部给定的值。0个就是全部值

+ ltrim key 开始index 结束index：截取指定范围的值后再赋值给key

+ rpoplpush 源列表 目的列表：移除列表的最后一个元素，并将该元素添加到另一个列表并返回

+ lset key index value

+ linsert key before/after 已有值 插入的新值：

```bash
LPUSH list名 value #将一个值或者多个值插入到列表的头部（左）
LRANGE list名 num1 num2 #获得num1 num2之间的值
LRANGE list名 0 -1 #获取全部的值
RPUSH list名 value #将一个值或者多个值插入到列表的尾部（右）
LPOP list名 #移除list的第一个元素
RPOP list名 #移除list的最后一个元素
lindex list名 num #通过下标获得list的某一个值
Llen list名 #返回列表的长度
Lrem list名 num value #移除list中指定数目的value,精确匹配
ltrim list名 num1 num2 #截取list num1 num2之间的值，list被截断了
rpoplpush list1 list2#移除列表list1的最后一个元素，并添加到新的列表list2
EXISTS list名 #判断list是否存在
lset list名 num value #将list中指定下标num的值替换为value
linsert list名 before|after value1 value2 #在list的value1的前|后插入value2的值
```

#### Hash

hash是一个string类型的field和value的映射表，hash特别适合用于存储对象

每个hash可以存储2^32-1键值对（40多亿）

+ hset/hget/hmset/hmget/hgetall/hdel

  hset user:001 id 11 name 23 age 25

+ hlen

+ hexists key 在key里面的某个值的key

+ hkeys/hvals

+ hincrby/hincrbyfloat

+ hsetnx

```bash
hset hash名 field名 value #set一个具体的key-value
hget hash名 field名 #获取一个字段值
hmset hash名 field1 value1 field2 value2 #set多个key-value
hmget hash名 field1 field2 #获取多个字段值
hgetall hash名 # 获取全部的数据
hdel hash名 field1 #删除hash指定的key字段，对应的value也就消失了
hlen hash名 #获取hash的field字段数量
hexists hash名 field1 #判断hash中的指定字段是否存在
hkeys hash名 #只获得所有field
hvals hash名 #只获得所有value
hincrby hash名 field名 num #增加num
hdecrby hash名 field名 num #减少num
hsetnx hash名 field名 value #如果不存在则可以设置，如果存在则不能设置
```

#### Set

set是string类型的无序集合。集合成员是唯一的，这就意味着集合中不能出现重复的数据，集合对象的编码可以是intset或者hashtable

redis中的set是通过哈希表实现的，所以添加、删除、查找的复杂度都是O(1)

集合中最大的成员数为2^32-1

+ SADD key member [member...]：添加元素
+ SMEMBERS key：遍历集合中的所有元素
+ SISMEMBER key member：判断元素是否在集合中
+ SREM key member [member...]：删除元素
+ scard：获取集合里面元素的个数
+ SRANDMEMBER key [数字]：从集合中随机展现设置的数字个数元素，元素不删除
+ SPOP key [数字]：从集合中随机弹出一个元素，出一个删一个
+ smove key1 key2 在key1里已存在的某个值：将key1里已存在的某个值赋给key2
+ SDIFF set1 set2：差集
+ SUNION set1 set2：并集
+ SINTER set1 set2：交集
+ SINTERCARD numkeys key [key...] [LIMIT limit]：不返回结果集，而只返回结果的基数，返回由所有给定集合的交集产生的集合的基数

#### **Zset**（有序集合） 

zset和set一样也是string类型元素的集合，且不允许重复的成员

不同的是每个元素都会关联一个double类型的分数，redis正是通过分数来为集合中的成员进行从小到大的排序

zset的成员是唯一的，但分数（score）却可以重复

zset是通过哈希表实现的，所以添加、删除、查找的复杂度都是O(1)，集合中最大的成员数为2^32-1

+ ZADD key score member [score member...]：添加元素

+ ZRANGE key start stop [WITHSCORES]：按照元素分数从小到大的顺序返回索引从start到stop之间的所有元素

+ ZREVRANGE key start stop [WITHSCORES]：按照元素分数从大到小的顺序返回索引从start到stop之间的所有元素

+ ZRANGEBYSCORE key min max [WITHSCORES] [LIMIT offset count]：获取指定分数范围的元素

  ZRANGEBYSCORE zset1 (60 90 withscores：大于等于60，小于90

+ ZSCORE key member：获取元素的分数

+ ZCARD key：获取集合中元素的个数

+ zrem key 某score下对应的value值：删除元素

+ ZINCRBY key increment member：增加某个元素的分数

+ ZCOUNT key min max：获取指定分数范围内的元素个数

+ ZMPOP numkeys key [key...] <MIN|MAX> [COUNT count]：从键名列表中的第一个非空排序集中弹出一个或多个元素，他们是成员分数对

  ZMPOP 1 myzset min count 1：从一个zset里面弹出最小的一个

+ zrank key value：获得下标值

+ zrevrank key value值：逆序获得下标值

#### geospatial

主要用于存储地理位置信息，并对存储的信息进行操作，包括添加地理位置的坐标、获取地理位置的坐标、计算两个位置之间的距离、根据用户给定的经纬度坐标来获取指定范围内的地理位置集合

geo底层实现原理是zset，可以使用zset命令来操作geo

+ GEOADD key longitude latitude member [...]：多个经度（longitude）、纬度（latitude）、位置名称（member）添加到指定的key中，如果有中文乱码：redis-cli --raw
+ GEOPOS key member [member...]：返回经纬度
+ GEOHASH key member [member...]：返回坐标的geohash表示，geohash算法生成的base32编码值
+ GEODIST key member1 member2 [m|km|ft|mi]：两个位置之间的距离
+ GEORADIUS key longitude latitude 10 km withdist withcoord withhash count 10 desc：以半径为中心，查找附件的xxx
  + withdist：在返回位置元素的同时，将位置元素与中心之间的距离也一并返回。距离的单位和用户给定的范围单位保持一致
  + withcoord：将位置元素的经纬度也一并返回
  + withhash：以52位有符号整数的形式，返回位置元素经过原始geohash编码的有序集合分值。这个选项主要用于底层应用或者调试，实践中的作用不大
  + count：限定返回的记录数
+ GEORADIUSBYMEMBER key member 10 km withdist withcoord withhash count 10 desc：找出位于指定范围内的元素，中心点是由给定的位置元素决定

```bash
geoadd key 经度 纬度 #添加地理位置，两极无法添加
geopos key #获取指定城市经纬度
geodist key1 key2 [单位] #查看key1、key2的直线距离，单位：m表示米，km表示千米，mi表示英里，ft表示英尺
georadius key 经度 纬度 num [单位] #以指定经纬度为中心，查询指定num单位内的key
georadius key 经度 纬度 num [单位] withdist #显示到中间点的位置和距离
georadius key 经度 纬度 num [单位] withcoord #显示他人的定位信息
georadius key 经度 纬度 num [单位] withdist withcoord count num #筛选出指定num的结果
georadiusbymember key num [单位] #找出位于指定元素周围的其他元素
geohash key1 key2 #将二维的经纬度转换为一维的字符串，如果两个字符串越接近，那么距离越近 
```

#### hyperloglog

hyperloglog是用来做基数统计的算法，hyperloglog的优点是，在输入元素的数量或者体积非常非常大时，计算基数所需的空间总是固定且是很小的

基数是一种数据集，去重复后的真实个数

在redis里面，每个hyperloglog键只需要花费12KB内存，就可以计算接近2^64个不同元素的基数。这和计算基数时，元素越多耗费内存就越多的集合形成鲜明对比

但是，因为hyperloglog只会根据输入元素来计算基数，而不会存储输入元素本身，所以hyperloglog不能像集合那样，返回输入的各个元素

统计某个网站的UV，Unique Vistor，独立访客，一般理解为客户端IP

+ PFADD key element [element...]：添加指定元素到hyperloglog中
+ PFCOUNT key [key...]：返回给定hyperloglog的基数估算值
+ PFMERGE destkey sourcekey [sourcekey...]：将多个hyperloglog合并为一个hyperloglog

```bash
pfadd key value1 value2... #创建一组元素
pfcount key #统计key元素的基数数量
pfmerge key1 key2 key3 #合并两组 key2 key3=>key1 并集
```

#### bitmap

由0和1状态表现的二进制位的bit数组

位存储，操作二进制位进行记录，就只有0和1两个状态，统计用户信息，活跃，不活跃，登录，未登录，两个状态的，都可以使用bitmaps

+ setbit key offset value
+ getbit key offset
+ strlen：统计字节数占用多少，不是字符串长度而是占据几个字节，超过8位后按照8位一组一byte再扩容
+ bitcount key start end：全部键里面含有1的有多少个
+ bitop operation destkey key：对不同的二进制存储数据进行位运算（AND、OR、NOT、XOR）

```bash
setbit key offset 0|1 #设置offset处的值
getbit key offset #获取offset处的值
bitcount key #统计key中1的数量
```

#### bitfield(了解即可)

通过bitfield命令可以一次性操作多个比特位域（指的是连续的多个比特位），他会执行一系列操作并返回一个响应数组，这个数组中的元素对应参数列表中的相应操作的执行结果

将一个redis字符串看作是一个由二进制位组成的数组，并能对变长位宽和任意字节对齐的指定整型位域进行寻址和修改

作用：位域修改和溢出控制

+ GET type offset：返回指定的位域
+ SET type offset value：设置指定位域的值并返回它的原值
+ INCRBY type offset increment：自增或自减（如果increment为负数）指定位域的值并返回它的新值
+ OVERFLOW [WRAP|SAT|FAIL]：通过设置溢出行为来改变调用INCRBY指令的后续操作
  + WRAP：使用回绕（wrap around）方法处理有符号整数和无符号整数的溢出情况
  + SAT：使用饱和计算（saturation arithmetic）方法处理溢出，下溢计算的结果为最小的整数值，而上溢计算的结果为最大的整数值
  + FAIL：命令将拒绝执行那些会导致上溢或者下溢情况出现的计算，并向用户返回空值表示计算未执行

当需要一个整型时，有符号整型需在位数前加i，无符号在位数前加u。例如u8是一个8位的无符号整型，i16是一个16位的有符号整型

BITFIELD key [GET type offset] [SET type offset value] [INCRBY type offset increment] [OVERFLOW [WRAP|SAT|FAIL]]

#### Stream

Stream是redis5版本新增加的数据结构，实现消息队列，它支持消息的持久化，支持自动生成全局唯一ID，支持ack确认消息的模式，支持消费组模式等，让消息队列更加的稳定和可靠

主要用于消息队列（MQ），redis本身是有一个redis发布订阅（pub/sub）来实现消息队列的功能，但它有个缺点就是消息无法持久化，如果出现网络断开、redis宕机等，消息就会被丢弃

简单来说发布订阅可以分发消息，但是无法记录历史消息，而Stream提供了消息的持久化和主备复制功能，可以让任何客户端访问任何时刻的数据，并且能记住每一个客户端的访问位置，还能保证消息不丢失

![](/img/redis_5.png)

+ Message Content：消息内容
+ Cousumer group：消费组，通过XGROUP CREATE命令创建，同一个消费组可以有多个消费者
+ Last_delivered_id：游标，每个消费组会有个游标last_delivered_id，任意一个消费组者读取了消息都会使游标向前移动
+ Consumer：消费者，消费组中的消费者
+ Pending_ids：消费者会有一个状态变量，用于记录被当前消费者已读取但未ack的消息Id，如果客户端没有ack，这个变量里面的消息id会越来越多，一旦某个消息被ack他就开始减少。这个pending_ids变量在redis官方被称之为PEL（Pending Entries List），记录了当前已经被客户端读取的消息，但是还没有ack（Acknowledge character：确认字符），它用来确保客户端至少消费了消息一次，而不会在网络传输的中途丢失了没处理

**队列相关指令**

+ XADD：添加消息到队列的末尾，消息ID必须要比上个id大，默认用星号表示自动生成规矩，*用于XADD命令中，让系统自动生成id

  XADD mystream * id 11 cname z3

+ XRANGE：用于获取消息列表，忽略删除的消息，start表示开始值，-代表最小值，end表示结束值，+代表最大值，count表示最多获取多少个值

  XRANGE mystream - + count 1

+ XREVRANGE：反转

  XREVRANGE mystream + 1

+ XDEL：删除，按主键删

  XDEL mystream 1669453973537-0

+ XLEN：stream长度

+ XTRIM：用于对stream的长度进行截取，如超长会进行截取，MAXLEN：允许的最大长度，对流进行修剪限制长度，MINID：允许的最小id，从某个id值开始比该id值小的将会被抛弃

  XTRIM mystream maxlen 2

  XTRIM mystream minid 1669453973537-0

+ XREAD [COUNT count] [BLOCK milliseconds] STREAMS key [key...] ID [ID...]：用于获取消息（阻塞/非阻塞），只会返回大于指定id的消息

  + COUNT：最多读取多少条消息
  + BLOCK：是否以阻塞的方式读取消息，默认不阻塞，如果milliseconds设置为0，表示永远阻塞

  XREAD count 2 streams mystream $

  XREAD count 2 streams mystream 0-0

  + $：代表特殊ID，表示已当前Stream已经存储的最大的ID最为最后一个ID，当前Stream中不存在大于当前最大ID的消息，因此此时返回nil
  + 0-0：代表从最小的ID开始获取Stream中的消息，当不指定count，将会返回Stream中的所有消息，注意也可以使用0（00/000也都是可以的）

**消费组相关指令**

+ GGROUP CREATE：用于创建消费者组

  XGROUP CREATE mystream groupA $

  XGROUP CREATE mystream groupB 0

  + $：表示从stream尾部开始消费
  + 0：表示从stream头部开始消费

+ XREADGROUP GROUP：>表示从第一条尚未被消费的消息开始读取

  XREADGROUP group groupA consumer1 streams mystream > ：消费组groupA内的消费者consumer1从mystream消息队列中读取所有的消息

  stream中的消息一旦被消费组里的一个消费者读取了，就不能再被该消费组内的其他消费者读取了，即同一个消费组里的消费者不能消费同一条消息。但是不同消费组的消费者可以读取同一条消息

  消费组的目的是让多个消费者共同分担读取消息，所以我们通常会让每个消费者读取部分消息，从而实现消息读取负载在多个消费者间是均衡的

  XREADGROUP group groupC consumer1 count 1 streams mystream >

  XREADGROUP group groupC consumer2 count 1 streams mystream >

  基于Stream实现的消息队列，如何保证消费者在发生故障或宕机再次重启后，仍然可以读取未处理完的消息？

  stream会自动使用内部队列（也称为PENDING LIST）留存消费组里每个消费者读取的消息保底措施，直到消费者使用XACK命令通知streams消息已经处理完成

  消息确认增加了消息的可靠性，一般在业务处理完成之后，需要执行XACK命令确认消息已经被消费完成

+ XPENDING：查询每个消费组内所有消费组者已读取未确认的消息

  XPENDING mystream groupA

+ XACK：消息确认

  XACK mystream groupC 1669453973537-0

### Redis持久化

#### RDB（Redis Database）

![](img\redis_2.png)

在指定的时间间隔内将内存中的数据集快照写入磁盘，也就是行话讲的snapshot快照，他恢复时是将快照文件读到内存里。

redis会单独创建（fork）一个子进程来进行持久化，会先将数据写入到一个临时文件中，待持久化过程都结束了，再用这个临时文件替换上次持久化好的文件。整个过程中，主进程不需要进行任何IO操作。这就确保了极高的性能。如果需要进行大规模数据的恢复，且对于数据恢复的完整性不是非常敏感，那RDB方式要比AOF方式更加高效。RDB的缺点是最后一次持久化后的数据可能丢失。我们默认的就是RDB，一般情况下不需要修改这个配置！

**rdb保存的文件是dump.rdb 都是在配置文件快照中进行配置的**

**配置文件redis.conf**

+ redis6.0.16以前
  + save 900 1
  + save 300 10
  + save 60 10000
+ redis6.2以及redis7.0.0
  + save 3600 1 300 100 60 10000：3600秒内有一次修改或300秒内有100次修改或60秒内有10000次修改，就写一份新的RDB文件

**触发机制**

+ 自动触发：redis.conf里配置的save < seconds> < changes>

  + 修改dump文件保存路径：dir ./修改为dir /myredis/dumpfiles
  + 修改dump.rdb文件名称：dbfilename dump.rdb修改为dbfilename dump6379.rdb
  + redis里用config get dir获取dump文件保存目录

  如何恢复备份

  将备份文件dump.rdb移动到redis安装目录并启动服务即可

  执行flushall/flushdb命令也会产生dump.rdb文件，但里面是空的，无意义

  物理恢复，一定服务和备份分机隔离

+ 手动触发：redis提供了两个命令来生成RDB文件，分别是save和bgsave，生产上只允许用bgsave

  + save：在主进程中执行会阻塞当前redis服务器，直到持久化工作完成，执行save命令期间，redis不能处理其他命令，线上禁止使用

  + bgsave（默认）：redis会在后台异步进行快照操作，不阻塞快照的同时还可以响应客户端请求，该触发方式会fork一个子进程由子进程复制持久化过程

    可以通过lastsave命令获取最后一次成功执行快照的时间，linux命令date -d @lastsave获取的时间戳获得看得懂的时间

1. save的规则满足的情况下，会自动触发rdb规则
2. 执行flushall命令，也会触发我们的rdb规则
3. 退出redis,也会产生rdb文件

备份就会自动生成dump.rdb

**RDB优点**

1. 适合大规模的数据恢复
2. 按照业务定时备份
3. 对数据的完整性和一致性要求不高
4. RDB文件在内存中的加载速度比AOF快得多

**RDB缺点**

1. 在一定间隔时间做一次备份，所以如果redis意外down掉的话，就会丢失从当前至最近一次快照期间的数据，快照之间的数据会丢失
2. 内存数据的全量同步，如果数据量太大会导致I/O严重影响服务器性能
3. RDB依赖于主进程的fork，在更大的数据集中，这可能会导致服务请求的瞬间延迟。fork的时候内存中的数据被克隆了一份，大致2倍的膨胀性，需要考虑

**检查修复dump.rdb文件**

redis-check-rdb /myredis/dumpfiles/dump6379.rdb

**哪些情况会触发RDB快照**

+ 配置文件中默认的快照配置
+ 手动save/bgsave
+ 执行flushall/flushdb命令也会产生dump.rdb文件，但里面是空的，没有意义
+ 执行shutdown且没有设置开启AOF持久化
+ 主从复制时，主节点自动触发

**如何禁用快照**

+ 动态所有停止RDB保存规则的方法：redis-cli config set save ""
+ 快照禁用：配置文件save ""

**RDB优化配置项**

配置文件SNAPSHOTTING模块

+ save < seconds> < changes>

+ dbfilename

+ dir

+ stop-writes-on-bgsave-error

  默认yes，如果配置成no，表示你不在乎数据不一致或者有其他的手段发现和控制这种不一致，那么快照在写入失败时，也能确保redis接受新的写请求

+ rdbcompression

  默认yes，对于存储到磁盘中的快照，可以设置是否进行压缩存储。如果是的话，redis会采用LZF算法进行压缩。如果你不想消耗CPU来进行压缩，可以设置为关闭此功能

+ rdbchecksum

  默认yes，在存储快照后，还可以让redis使用CRC64算法来进行数据校验，但是这样做会增大大约10%的性能损耗，如果希望获取到最大的性能提升，可以关闭此功能

+ rdb-del-sync-files

  在没有持久性的情况下删除复制中使用的RDB文件启用。默认情况下no，此选项是禁用的

#### AOF(Append Only File)

以日志的形式记录每个写操作，将redis执行过的所有指令记录下来（读操作不记录），只许追加文件但不可以改写文件，redis启动之初会读取该文件重新构建数据，换言之，redis重启的话就根据日志文件的内容将写指令从前到后执行一次以完成数据的恢复工作。

默认情况下，redis是没有开启AOF（append only file）的，开启AOF功能需要设置配置appendonly yes

![](img\redis_3.png)



**aof保存的是appendonly.aof文件**

**AOF持久化工作流程**

![](/img/redis_6.png)

1. Client作为命令的来源，会有多个源头以及源源不断的请求命令
2. 在这些命令到达Redis Server以后并不是直接写入AOF文件，会将这些命令先放入AOF缓存中进行保存。这里的AOF缓存区实际上是内存中的一片区域，存在的目的是当这些命令达到一定量后再写入磁盘，避免频繁的磁盘IO操作
3. AOF缓存会根据AOF缓存区同步文件的三种写回策略将命令写入磁盘上的AOF文件
4. 随着写入AOF内容的增加为避免文件膨胀，会根据规则进行命令的合并（又称AOF重写）从而起到AOF文件压缩的目的
5. 当Redis Server服务器重启的时候会从AOF文件载入数据

**三种写回策略**

+ Always：同步写回，每个写命令执行完立刻同步地将日志写回磁盘
+ everysec：默认策略，每秒写回，每个写命令执行完，只是先把日志写到AOF文件的内存缓冲区，每隔1秒把缓冲区的内容写入磁盘
+ no：操作系统控制的写回，每个写命令执行完，只是先把日志写道AOF文件的内存缓冲区，由操作系统决定何时将缓冲区内容写回磁盘

**AOF配置/启动/修复/恢复**

开启AOF：redis.conf中appendonly修改为yes

默认写回策略：appendfsync everysec

AOF文件保存路径：

+ redis6

  AOF保存文件的位置和RDB保存文件的位置一样，都是通过redis.conf配置文件的dir配置

+ redis7之后更新

  AOF保存文件的位置redis.conf中appenddirname "appendonlydir"

AOF文件保存名称：

+ redis6

  有且仅有一个appendfilename "appendonly.aof"

+ redis7之后更新

  使用multi part AOF机制，1个文件分为3个

  appendonly.aof.1.base.rdb、appendonly.aof.1.incr.aof、appendonly.aof.mainfest

  + BASE：表示基础AOF，它一般由子进程通过重写产生，该文件最多只有一个
  + INCR：表示增量AOF，它一般会在AOFRW开始执行时被创建，该文件可能有多个
  + HISTORY：表示历史AOF，它由BASE和INCR AOF变化而来，每次AOFRW成功完成时，本地AOFRW之前对应的BASE和INCR AOF都将变为HISTORY，HISTORY类型的AOF会被Redis自动删除

  为了管理这些AOF文件，引入了一个mainfest（清单）文件来跟踪、管理这些AOF。同时，为了便于AOF备份和拷贝，我们将所有的AOF文件和mainfest文件放入了一个单独的文件目录中，目录名由appenddirname配置决定

  正常恢复：重启redis然后重写加载，结果OK

  异常恢复：异常修复命令：redis-check-aof --fix进行修复，redis-check-aof --fix appendonly.aof.1.incr.aof

**AOF优点**

1. 更好的保存数据不丢失、性能高、可做紧急恢复

**AOF缺点**

1. 相同数据集的数据而言aof文件要远大于rdb文件，恢复速度慢于rdb
2. aof运行效率要慢于rdb，每秒同步策略效率较好，不同步效率和rdb相同

**AOF重写机制**

由于AOF持久化是redis不断将写命令记录到AOF文件中，随着redis不断的进行，AOF文件会越来越大，文件越大，占用服务器内存越大以及AOF恢复要求时间越长

为了解决这个问题，Redis新增了重写机制，当AOF文件的大小超过所设定的峰值时，Redis就会自动启动AOF文件的内容压缩，只保留可以恢复数据的最小指令集，或者可以手动使用命令bgrewriteaof来重新

官网默认配置

+ auto-aof-rewrite-percentage 100：根据上次重写后的aof大小，判断当前aof大小是不是增长了1倍
+ auto-aof-rewrite-min-size 64mb：重写时满足的文件大小

注意同时满足，且的关系才会触发

自动触发：满足配置文件中的选项后，Redis会记录上次重写时的AOF大小，默认配置是当AOF文件大小是上次rewrite后大小的一倍且文件大于64M时

手动触发：客户端向服务器发送bgrewriteaof命令

例如aof中命令set k1 v1、set k2 v2重写后只保留最后一次修改值，AOF重写不仅降低了文件的占用空间，同时更小的AOF也可以更快地被Redis加载

AOF文件重写并不是对原文件进行重新整理，而是直接读取服务器现有的键值对，然后用一条命令去代替之前记录这个键值对的多条命令，生成一个新的文件后去替换原来的AOF文件

**重写原理**

1. 在重写开始前，redis会创建一个重写子进程，这个子进程会读取现有的AOF文件，并将其包含的指令进行分析压缩并写入到一个临时文件中
2. 与此同时，主进程会将新接收到的写指令一边累积到内存缓冲区，一边继续写入到原有的AOF文件中，这样做是保证原有AOF文件的可用性，避免在重写过程中出现意外
3. 当重写子进程完成重写工作后，他会给父进程发一个信号，父进程收到信号后就会将内存中缓存的写指令追加到新的AOF文件中
4. 当追加结束后，redis就会用新AOF文件来代替旧AOF文件，之后再有新的写指令，就都会追加到新的AOF文件中
5. 重写AOF文件的操作，并没有读取旧的AOF文件，而是将整个内存中的数据库内容用命令的方式重写了一个新的aof文件，这点和快照有点类似

#### RDB-AOF混合持久化

**数据恢复顺序和加载流程**

在同时开启rdb和aof持久化时，重启时只会加载aof文件，不会加载rdb文件

开启混合方式设置：设置aof-use-rdb-preamble的值为yes

RDB+AOF的混合方式，RDB镜像做全量持久化，AOF做增量持久化，先使用RDB进行快照存储，然后使用AOF持久化记录所有的写操作，当重写策略满足或手动触发重写的时候，将最新的数据存储为新的RDB记录。这样的话，重启服务的时候会从RDB和AOF两部分恢复数据，即保证了数据的完整性，又提高了恢复数据的性能。简单来说，混合持久化方式产生的文件一部分是RDB格式，一部分是AOF格式。AOF包括了RDB头部和AOF混写

**纯缓存模式**

同时关闭RDB和AOF

+ save ""：禁用rdb，禁用rdb持久化模式下，我们仍然可以使用命令save、bgsave生成rdb文件
+ appendonly no：禁用aof，禁用aof持久化模式下，我们仍然可以使用命令bgrewriteaof命令生成aof文件

### 事务

可以一次执行多个命令，一组命令的集合，一个事务中的所有命令都会被序列化，按顺序的串行化执行而不会被其他命令插入，不许加塞。一个队列中，一次性、顺序性、排它性的执行一系列命令

**redis事务 vs 数据库事务**

1. 单独的隔离操作：redis的事务仅仅是保证事务里的操作会被连续独占的执行，redis命令执行是单线程架构，在执行完事务内所有指令前是不可能再去同时执行其他客户端的请求的
2. 没有隔离级别的概念：因为事务提交前任何指令都不会被实际执行，也就不存在事务内的查询要看到事务里的更新，在事务外查询不能看到这种问题了
3. 不保证原子性：redis的事务不保证原子性，也就是不保证所有指令同时成功或者同时失败，只有决定是否开始执行全部指令的能力，没有执行到一半进行回滚的能力
4. 排它性：Redis会保证一个事务内的命令依次执行，而不会被其他命令插入

**常用命令**

+ DISCARD：取消事务，放弃执行事务块内的所有命令
+ EXEC：执行所有事务块内的命令
+ MULTI：标记一个事务块的开始
+ UNWATCH：取消WATCH命令对所有key的监视
+ WATCH key [key...]：监视一个或多个key，如果在事务执行之前这个或这些key被其他命令所改动，那么事务被打断

例：

1. 正常执行：

   ```bash
   MULTI
   set k1 v1
   set k2 v2
   EXEC
   ```

2. 放弃事务

   ```bash
   MULTI
   set k1 v1
   set k2 v2
   DISCARD
   ```

3. 全体连坐

   ```bash
   MULTI
   set k1 v1
   set k2 v2
   set k3  #出错
   EXEC
   #一个语法出错，全体连坐，如果任何一个命令语法有错，redis会直接返回错误，所有的命令都不会执行
   ```

4. 冤头债主

   ```bash
   set email aaa
   MULTI
   set k1 v1
   set k2 v2
   INCR email #编译时语法没报错
   EXEC
   #执行后报错，对的执行错的停
   ```

5. watch监控

   redis使用watch来提供乐观锁定，类似于CAS（check-and-set）

   悲观锁（Perssimistic Lock）：每次去拿数据的时候都认为别人会修改，所以每次在拿数据的时候都会上锁，这样别人想拿这个数据就会block直到它拿到锁

   乐观锁（Optimistic Lock）：每次去拿数据的时候都认为别人不会修改，所以不会上锁，但是在更新的时候会判断一下在此期间别人有没有去更新这个数据

   乐观锁策略：提交版本必须大于记录当前版本才能执行更新

   watch

   ```bash
   #客户端1
   set balance 100
   WATCH balance  #1
   MULTI #2
   set balance 110 #4
   EXEC #5
   #balance被修改，执行失败返回nil
   
   #客户端2
   set balance 120 #3
   ```

   unwatch

   ```bash
   #客户端1
   set balance 100
   WATCH balance  #1
   get balance #2
   UNWATCH #4
   MULTI
   set balance 110
   EXEC #5
   #返回110
   
   #客户端2
   set balance 120 #3
   ```

   一旦执行了exec之前加的监控锁都会被取消掉了，当客户端连接丢失的时候（比如退出连接），所有东西都会被取消监视

### Redis管道

1. 客户端向服务器发送命令分四步（发送命令->命令排队->命令执行->返回结果），并监听Socket返回，通常以阻塞模式等待服务端响应

2. 服务端处理命令，并将结果返回给客户端

上述两步称为Round Trip Time（简称RTT，数据包往返于两端的时间）

如果同时需要执行大量的命令，那么就要等待上一条命令应答后再执行，这中间不仅仅多了RTT，而且还频繁调用系统IO，发送网络请求，同时需要redis调用多次read()和write()系统方法，系统方法会将数据从用户态转移到内核态，这样就会对进程上下文有较大的影响了，性能不太好

管理（Pipeline）可以一次性发送多条命令给服务端，服务端依次处理完完毕后，通过一条响应一次性将结果返回，通过减少客户端与redis的通信次数来实现降低往返延时时间。pipeline实现的原理是队列，先进先出的特性就保证数据的顺序性

Pipeline是为了解决RTT往返回时，仅仅是将命令打包一次性发送，对整个redis的执行不造成其他任何影响

批处理命令的变种化措施，类似于redis的原生批命令（mget和mset）

cat cmd.txt | redis-cli -a 123456 --pipe，cmd.txt里是需要管道执行的redis命令

**总结**

Pipeline与原生批量命令对比

+ 原生批量命令是原子性（mset、mget），pipeline是非原子性
+ 原生批量命令一次只能执行一种命令，pipeline支持批量执行不同命令
+ 原生批量命令是服务端实现，而pipeline需要服务端与客户端共同完成

Pipeline与事务对比

+ 事务具有原子性，管道不具有原子性
+ 管道一次性将多条命令发送到服务器，事务是一条一条的发，事务只有在接收到exec命令后才会执行，管道不会
+ 执行事务时会阻塞其他命令的执行，而执行管道中的命令时不会

使用Pipeline注意事项

+ pipeline缓冲的指令只是会依次执行，不保证原子性，如果执行中指令发生异常，将会继续执行后续的指令
+ 使用pipeline组装的命令个数不能太多，不然数据量过大客户端阻塞的时间可能过久，同时服务端此时也被迫回复一个队列答复，占用很多内存

### Redis发布订阅

redis发布订阅（pub/sub）是一种消息通信模式：发送者（pub）发送消息，订阅者（sub）接收消息，可以实现进程间的消息传递。

Redis可以实现消息中间件MQ的功能，通过发布订阅实现消息的引导和分流。不推荐使用该功能，专业的事情交给专业的中间件处理，redis就做好分布式缓冲功能

redis客户端可以订阅任意数量的频道

订阅/发布消息图：

![](img\redis_4.png)

+ PSUBSCRIBE pattern [pattern...]：订阅一个或多个符合给定模式的频道

+ PUBSUB subcommand [argument [argument...]]：查看订阅与发布系统状态

  + PUBSUB CHANNELS：由活跃频道组成的列表
  + PUBSUB NUMSUB [channel [channel...]]：某个频道有几个订阅者
  + PUBSUB NUMPAT：只统计使用PSUBSCRIBE命令执行的，返回客户端订阅的唯一模式的数量

+ PUBLISH channel message：将信息发送到指定频道

+ PUNSUBSCRIBE [pattern [pattern...]]：退订所有给定模式的频道

+ SUBSCRIBE channel [channel...]：订阅给定的一个或多个频道的信息

  推荐先执行订阅后再发布，订阅成功之前发布的消息是收不到的

  订阅的客户端每次可以收到一个3个参数的消息：消息的种类，始发频道的名称，实际的消息内容

+ UNSUBSCRIBE channel [channel...]：退订给定的频道

**Pub/Sub缺点**

+ 发布的消息在Redis系统中不能持久化，因此，必须先执行订阅，再等待消息发布。如果先发布了消息，那么该消息由于没有订阅者，消息将直接被丢弃
+ 消息只管发送对于发布者而言消息是即发即失的，不管接收，也没有ACK机制，无法保证消息的消费成功

为此Redis5.0版本增加了Stream数据结构，不但支持多播，还支持数据持久化，相比Pub/Sub更加的强大

### Redis主从复制（replica）

主从复制，master以写为主，Slave以读为主，当master数据变化的时候，自动将新的数据异步同步到其他slave数据库

**作用**

+ 读写分离
+ 容灾恢复
+ 数据备份
+ 水平扩容支撑高并发

**配置**

只配置从库，不用配置主库

权限细节：master如果配置了requirepass参数，需要密码登录，那么slave就要配置masterauth来设置校验密码，否则的话，master会拒绝slave的访问请求

主要命令

+ info replication：可以查看复制节点的主从关系和配置信息
+ relicaof 主库IP 主库端口：配置指定，一般写入redis.conf配置文件内
+ slaveof 主库IP 主库端口：命令指定，每次与master断开之后，都需要重新连接，除非你配置进redis.conf文件。在运行期间修改slave节点的信息，如果该数据库以及是某个主数据库的从数据库，那么会停止和原主数据库的同步关系转而和新的主数据库同步
+ slaveof no one：使当前数据库停止与其他数据库同步，转成主数据库

修改配置文件细节操作

1. 开启daemonize yes：后台运行
2. 注释掉bind 127.0.0.1
3. protected-mode no
4. 指定端口
5. 指定当前工作目录，dir
6. pid文件名字，pidfile
7. log文件名字，logfile
8. requirepass
9. dump.rdb名字
10. aof文件，appendfilename：本步骤可选，非必须
11. 从机访问主机的通行密码masterauth，必须，从机需要配置，主机不用，replicaof 主库IP 主库端口 

常用操作：

+ 一主二仆

  + 从机只可以读取，不可以写操作
  + slave是从头复制，slave启动前master写入的数据也能复制
  + 主机shutdown后，从机不动，原地待命，从机数据可以正常使用，等待主机重启动归来
  + 主机shutdown后，重启后主从关系还在，从机能顺利复制

+ 薪火相传

  上一个slave可以是下一个slave的master，slave同样可以接收其他slaves的连接和同步请求，那么该slave作为了链条中下一个的master，可以有效	减轻主master的压力

  中途变更转向会清除之前的数据，重新建立拷贝最新的

  命令：slaveof 主库IP 主库端口

+ 反客为主

  slaveof no one：使当前数据库停止与其他数据库同步，转成主数据库

**复制原理和工作流程**

+ slave启动，同步初请

  slave启动成功连接到master后会发送一个sync命令

  slave首次全新连接master，一次完全同步（全量复制）将被自动执行，slave自身原有数据会被master数据覆盖清除

+ 首次连接，全量复制

  master节点收到sync命令后会开始在后台保存快照（即RDB持久化，主从复制时会触发RDB），同时收集所有接收到的用于修改数据集命令缓存起来，master节点执行RDB持久化完成后，master将rdb快照文件和所有缓存的命令发送到所有slave，以完成一次完全同步

  而slave服务在接收到数据库文件数据后，将其存盘并加载到内存中，从而完成复制初始化

+ 心跳持续，保持通信

  repl-ping-replica-period 10：master发出PING包的周期，默认是10秒

+ 进入平稳，增量复制

  master继续将新的所有收集到的修改命令自动依次传给slave，完成同步

+ 从机下线，重连续传

  master会检查backlog里面的offset，master和slave都会保存一个复制的offset还有一个masterid，offset是保存在backlog中的。master只会把已经复制的offset后面的数据复制给slave，类似断点续传

**复制的缺点**

+ 复制延时，信号衰减

  由于所有的写操作都是先在master上操作，然后同步更新到slave上，所以从master同步到slave机器有一定的延迟，当系统很繁忙的时候，延迟问题会更加严重，slave机器数量的增加也会使这个问题更加严重

+ master挂了，默认情况下不会在slave节点中自动重选一个master

### Redis哨兵（sentinel）

哨兵巡查监控后台master主机是否故障，如果故障了根据投票数自动将某一个从库转换为新主库，继续对外服务

哨兵的作用

1. 监控redis运行状态，包括master和slave
2. 当master宕机，能自动将slave切换成新的master

**能干嘛**

+ 主从监控：监控主从redis库运行是否正常
+ 消息通知：哨兵可以将故障转移的结果发送给客户端
+ 故障转移：如果master异常，则会进行主从切换，将其中一个slave作为新master
+ 配置中心：客户端通过连接哨兵来获得当前redis服务的主节点地址

**案例实操**

3个哨兵：自动监控和维护集群，不存放数据，只是吹哨人

1主2从：用于数据读取和存放

![](/img/redis_7.png)

1. /myredis目录下新建或者拷贝sentinel.conf文件，名字绝不能错

2. 先看/opt目录下默认的sentinel.conf文件的内容

3. 重点参数说明

   + bind：服务监听地址，用于客户端连接，默认本机地址

   + daemonize：是否以后台daemon方式运行

   + protected-mode：安全保护模式

   + port：端口

   + logfile：日志文件路径

   + pidfile：pid文件路径

   + dir：工作目录

   + sentinel monitor < master-name> < ip> < redis-port> < quorum>

     设置要监控的master服务器，quorum表示最少有几个哨兵认可客观下线，同意故障迁移的法定票数

   + sentinel auth-pass < master-name> < password>：master设置了密码，连接master服务的密码

   + sentinel down-after-milliseconds < master-name> < milliseconds>：指定多少毫秒之后，主节点没有应答哨兵，此时哨兵主观上认为主节点下线

   + sentinel parallel-syncs < master-name> < nums>：表示允许并行同步的slave个数，当master挂了之后，哨兵会选出新的master，此时，剩余的slave会向新的master发起同步数据

   + sentinel failover-timeout < master-name> < milliseconds>：故障转移的超时时间，进行故障转移时，如果超过设置的毫秒，表示故障转移失败

   + sentinel notification-script < master-name> < script-path>：配置当某一事件发生时所需要执行的脚本

   + sentinel client-reconfig-script < master-name> < script-path>：客户端重新配置主节点参数脚本

4. 本次案例哨兵sentinel文件通用配置

   ```bash
   #sentinel26379.conf
   bind 0.0.0
   daemonize yes
   protected-mode no
   port 26379
   logfile "/myredis/sentinel26379.log"
   pidfile /var/run/redis-sentinel26379.pid
   dir /myredis
   sentinel monitor mymaster 192.168.111.169 6379 2
   sentinel auth-pass mymaster 123456
   
   #sentinel26380.conf
   bind 0.0.0
   daemonize yes
   protected-mode no
   port 26380
   logfile "/myredis/sentinel26380.log"
   pidfile /var/run/redis-sentinel26380.pid
   dir /myredis
   sentinel monitor mymaster 192.168.111.169 6379 2
   sentinel auth-pass mymaster 123456
   
   #sentinel26381.conf
   bind 0.0.0
   daemonize yes
   protected-mode no
   port 26381
   logfile "/myredis/sentinel26381.log"
   pidfile /var/run/redis-sentinel26381.pid
   dir /myredis
   sentinel monitor mymaster 192.168.111.169 6379 2
   sentinel auth-pass mymaster 123456
   ```

5. 先启动一主二从3个redis实例，测试正常的主从复制

   主机新建redis6379.conf配置文件，配置masterauth 123456：6379后续可能会变成从机，需要设置访问新主机的密码

   从机配置文件设置好relicaof < masteipr> < masterport>

6. 再启动3个哨兵，完成监控

   redis-sentinel sentinel26379.conf --sentinel

   redis-sentinel sentinel26380.conf --sentinel

   redis-sentinel sentinel26381.conf --sentinel

7. 启动3个哨兵监控后再测试一次主从复制

8. 原有的master挂了

   两个小问题：broken pipe、Server closed the connection，这两个错几乎都是一样的问题

   broken pipe：pipe是管道的意思，管道里面是数据流，通常是文件或网络套接字读取的数据。当该管道从另一端突然关闭时，会发生数据突然中断，即是broken，对于socket来说，可能是网络被拔出或另一端的进程崩溃。其实当该异常产生时，对于服务端来说，并没有多少影响。因为可能是某个客户端突然中止了进程导致了该错误。这个异常是客户端读取超时关闭了连接，这时候服务器端向客户端已经断开的连接写数据时就发生了broken pipe异常

   sentinel.conf和redis.conf配置文件的内容在运行期间可能会被sentinel动态进行更改。master-slave切换后，master_redis.conf、slave_redis.conf和sentinel.conf的内容都会发生改变，即master_redis.conf中会多一行slaveof的配置，sentinel.conf的监控目标会随之调换

**哨兵运行流程和选举原理**

当一个主从配置中的master失效之后，sentinel可以选举出一个新的master用于自动接替原master的工作，主从配置中的其他redis服务器自动指向新的master同步数据。一般建议sentinel采取奇数台，防止某一台sentinel无法连接到master导致误切换

1. 三个哨兵监控一主二从

2. SDown主观下线（Subjectively Down）

   SDOWN（主观不可用）是单个sentinel自己主观上检测到的关于master的状态，从sentinel的角度来看，如果发送了PING心跳后，在一定的时间内没有收到合法的回复，就达到了SDOWN的条件

   sentinel配置文件中的down-after-milliseconds设置了判断主观下线的时间长度

3. ODown客观下线（Objectively Down）

   ODOWN需要一定数量的sentinel，多个哨兵达成一致意见才能认为一个master客观上已经宕掉

4. 选举出领导者哨兵（哨兵中选出兵王）

   当主节点被判断客观下线后，各个哨兵节点会进行协商，先选举出一个领导者哨兵节点并由该领导者节点进行failover（故障转移）

   监视该主节点的所有哨兵都有可能被选为领导者，选举使用的算法是Raft算法。Raft算法的基本思路是先到先得，即在一轮选举中，哨兵A向B发送称为领导者的申请，如果B没有同意过其他哨兵，则会同意A成为领导者

5. 由兵王开始推动故障切换流程并选出一个新master

   1. 新主登基

      选出新master的规则，剩余slave节点健康的前提下

      + redis.conf文件中，优先级slave-priority或者relica-priority最高的从节点（数字越小优先级越高）
      + 复制偏移位置offset最大的节点
      + 最小Run ID的从节点

   2. 群臣俯首

      + 执行slaveof no one命令让选出来的从节点称为新的主节点，并通过slaveof命令让其他节点成为从节点
      + Sentinel leader会对选举出的新master执行slaveof no one操作，将其提升为master节点
      + Sentinel leader向其他slave发送命令，让剩余的slave成为新的master节点的slave

   3. 旧主拜服

      + 将之前已下线的老master设置为新选出的新master的从节点，当老master重新上线后，他会成为新master的从节点
      + Sentinel leader会让原来的master降级为slave并恢复正常工作

### Redis集群（cluster）

由于数据量过大，单个master复制集难以承担，因此需要对多个复制集进行集群，形成水平扩展每个复制集只负责存储整个数据集的一部分，这就是redis的集群，其作用是提供多个redis节点间共享数据的程序集

![](/img/redis_8.png)

Redis集群是一个提供在多个Redis节点间共享数据的程序集

**能干嘛**

+ Redis集群支持多个master，每个master又可以挂载多个slave。读写分离，支持数据的高可用，支持海量数据的读写存储操作
+ 由于Cluster自带的Sentinel的故障转移机制，内置了高可用的支持，无需再去使用哨兵功能
+ 客户端与redis的节点连接，不再需要连接集群中的所有的节点，只需要任意连接集群中的一个可用节点即可
+ 槽位slot负责分配到各个物理服务节点，由对应的集群来负责维护节点、插槽和数据之间的关系

**集群算法-分片-槽位slot**

![](/img/redis_9.png)

+ redis集群的槽位slot

  redis没有使用一致性hash，而是引入了哈希槽的概念。redis集群有16384个哈希槽，每个key通过CRC16校验后对16384取模来决定放置哪个槽。集群的每个节点负责一部分hash槽

+ redis集群的分片

  使用redis集群时，我们会将存储的数据分散到多台redis机器上，这称为分片。简言之，集群中每个redis实例都被认为时整个数据的一个分片

  为了找到给定key的分片，我们对key进行CRC16(key)算法处理并通过对总分片数取模。然后，使用确定性哈希函数，这意味着给定的key将多次始终映射到同一个分片，我们可以推断将来读取特定key的位置

+ 槽位和分片的优势：方便扩缩容和数据分派查找

  这种结构很容易添加或者删除节点，比如我想新加个节点D，我需要从节点A、B、C中得部分槽到D上，如果我想移除节点A，需要将A中的槽移到B和C节点上，然后将没有任何槽的A节点从集群中移除即可。由于从一个节点将哈希槽移动到另一个节点并不会停止服务，所以无论添加删除或者改变某个节点的哈希槽的数量都不会造成集群不可用的状态

+ slot槽位映射，一般业界有3种解决方案

  + 哈希取余分区

    hash(key) % N个机器台数，计算出哈希值

    缺点：原来规划好的节点，进行扩容或者缩容就比较麻烦了，不管扩缩，每次数据变动导致节点有变动，映射关系需要重新进行计算，在服务器个数固定不变时没有问题，如果需要弹性扩容或故障停机的情况下，原来的取模公式就会发生变化：Hash(key)/3会变成Hash(key)/?。此时地址经过取余运算的结果将发生很大变化，根据公式获取的服务器也会变得不可控。

  + 一致性哈希算法分区

    1997年由麻省理工学院提出，设计目的是为了解决分布式缓存数据变动和映射问题，某个机器宕机了，分母数量改变了，自然取余就不行了

    3大步骤

    1. 算法构建一致性哈希环

       一致性哈希算法必然有个hash函数并按照算法产生hash值，整个算法的所有可能哈希值会构成一个全量集，这个集合可以成为一个hash空间[0, 2^32-1]，这是一个线性空间，但是在算法中，我们通过适当的逻辑控制将他首尾相连（0 = 2^32），这样让他逻辑上形成了一个环形空间

       他也是按照使用取模的方法，前面笔记介绍的节点取模法是对节点（服务器）的数量进行取模。而一致性Hash算法是对2^32取模，简单来说，一致性hash算法按照将整个hash空间组织成一个虚拟的环，如假设哈希函数H的值空间为0~2^32-1（即hash值是一个32位无符号整型），整个哈希环按顺时针方向组织，圆环的正上方的点代表0，0点右侧的第一个点代表1，依次类推，直到2^32-1，也就是说0点左侧的第一个点代表2^32-1，0和2^32-1在零点中方向重合，我们把这个由2^32个点组成的圆环称为hash环

    2. 服务器IP节点映射

       将集群中的各个IP节点映射到环上的某一位置，将各个服务器使用hash进行一个哈希，具体可以选择服务器的IP或主机名作为关键字进行哈希，这样每台机器就能确定其在哈希环上的位置。例如4个节点NodeA、B、C、D经过IP地址的哈希函数计算，使用IP地址哈希后在环空间的位置如下:

       ![](/img/redis_10.png)

    3. key落到服务器的落键规则

       当我们需要存储一个kv键值对时，首先计算key的hash值，将这个key使用相同的hash计算出哈希值并确定此数据在环上的位置，从此位置沿环顺时针行走，第一台遇到的服务器就是其应该定位到的服务器，并将该键值对存储在该节点上

       ![](/img/redis_11.png)

    优点：

    + 一致性哈希算法的容错性

      假设NodeC宕机，可以看到此时对象A、B、D不会收到影响。一般的，在一致性hash算法中，如果一台服务器不可用，则受影响的数据仅仅是此服务器到其环空间的前一台服务器之间的数据，其他不会受到影响。

    + 一致性哈希算法的扩展性

      数量增加了，需要增加一台节点NodeX，X的位置在A和B之间，那受到影响的也就是A到X之间的数据，重新把A到X的数据录入到X上即可，不会导致hash取余全部数据重新洗牌

    缺点：

    + 一致性哈希算法的数据倾斜问题

      一致性hash算法在服务节点太少时，容易因为节点分布不均而造成数据倾斜。

  + 哈希槽分区

    哈希槽实质就是一个数组，数组[0, 2^14-1]形成hash slot空间。能够解决均匀分配的问题，在数据和节点之间又加入了一层，把这层称为哈希槽（slot），用于管理数据和节点之间的关系，现在就相当于节点上放的是槽，槽里放的是数据。槽解决的粒度问题，相当于把粒度变大了，这样便于数据移动。哈希解决的是映射问题，使用key的哈希值来计算所在的槽，便于数据分配
    
    一个集群只能有16384个槽，编号0-16383（0~2^14-1），这些槽会分配给集群中的所有主节点，分配策略没有要求。集群会记录节点和槽的对应关系，解决了节点和槽的关系后，接下来就要对key求哈希值，然后对16384取模，余数是几key就落入对应的槽里。HASH_SLOT=CRC16(key) mod 16384。以槽为单位移动数据，因为槽的数目是固定的，处理起来比较容易，这样移动数据的问题就解决了。
    
    Redis集群中内置了16384个哈希槽，redis会根据节点数量大致均等的将哈希槽映射到不同的节点。当需要在redis集群中放置一个key-value时，redis先对key使用CRC16算法算出一个结果，然后用结果对16384取余，这样每个key都会对应一个编号在0-16383之间的哈希槽，也就是映射到某个节点上。

+ 为什么redis集群的最大槽数是16384个

  CRC16算法产生的hash值有16bit，该算法可以产生2^16=65536个值，在做mod运算时，为什么不用65536？

  正常的心跳数据包带有节点的完整配置，可以用幂等方式用旧的节点替换旧节点，以便更新旧的配置。这意味着他们包含原始节点的插槽配置，该节点使用2k的空间和16k的插槽，但是会使用8k的空间（使用65k的插槽）。同时，由于其他设计初衷，redis集群不大可能扩展到1000个以上的主节点。因此16k处于正确的范围内，以确保每个主机具有足够的插槽，最多可容纳1000个矩阵，但数量足够少，可以轻松地将插槽配置作为原始位图传播。请注意，在小型集群中位图难以压缩，因为当N较小时，位图将设置的slot/N位占设置位的很大百分比

  1. 如果槽位为65536，发送心跳信息的消息头达8k，发送的心跳包过于庞大。

     在消息头中最占空间的是myslots[CLUSTER_SLOTS/8]。当槽位为65536时，这块的大小是：65536÷8÷1024=8kb

     在消息头中最占空间的是myslots[CLUSTER_SLOTS/8]。当槽位为16384时，这块的大小是：16384÷8÷1024=2kb

     因为每秒钟，redis节点需要发送一定数量的ping消息作为心跳包，如果槽位为65536，这个ping消息的消息头太大了，浪费带宽

  2. redis的集群的主节点数量基本不可能超过1000个

     集群节点越多，心跳包的消息体内携带的数据越多。如果节点超过1000个，也会导致网络拥堵。因此redis作者不建议redis cluster节点数量超过1000个。那么，对于节点数在1000以内的redis cluster集群，16384个槽位够用了。没有必要扩展到65535个。

  3. 槽位越小，节点少的情况下，压缩比高，容易传输

     redis主节点的配置信息中它所负责的哈希槽是通过一张bitmap的形式来保存的，在传输过程中会对bitmap进行压缩，但是如果bitmap的填充率slots/N很高的话（N表示节点数），bitmap的压缩率就很低。如果节点数少，而哈希槽数量很多的话，bitmap的压缩率就会很低

+ Redis集群不保证强一致性，这意味着在特定的条件下，redis集群可能会丢掉一些被系统收到的写入请求命令

**集群环境案例步骤**

1. 3主3从redis集群配置

   1. 找3台真实虚拟机，各自新建mkdir -p /myredis/cluster

   2. 新建6个独立的redis服务实例

      + 192.168.111.175+端口6381/端口6382

        vim /myredis/cluster/redisCluster6381.conf

        vim /myredis/cluster/redisCluster6382.conf

        ```bash
        #redisCluster6381.conf
        bind 0.0.0
        daemonize yes
        protected-mode no
        port 6381
        logfile "/myredis/cluster/cluster6381.log"
        pidfile /myredis/cluster6381.pid
        dir /myredis/cluster
        dbfilename dump6381.rdb
        appendonly yes
        appendfilename "appendonly6381.aof"
        requirepass 123456
        masterauth 123456
        
        cluster-enabled yes
        cluster-config-file nodes-6381.conf
        cluster-node-timeout 5000
        
        #redisCluster6382.conf
        bind 0.0.0
        daemonize yes
        protected-mode no
        port 6382
        logfile "/myredis/cluster/cluster6382.log"
        pidfile /myredis/cluster6382.pid
        dir /myredis/cluster
        dbfilename dump6382.rdb
        appendonly yes
        appendfilename "appendonly6382.aof"
        requirepass 123456
        masterauth 123456
        
        cluster-enabled yes
        cluster-config-file nodes-6382.conf
        cluster-node-timeout 5000
        ```

      + 192.168.111.172+端口6383/端口6384

      + 192.168.111.174+端口6385/端口6386
      + 启动6台redis主机实例：redis-server /myredis/cluster/redisCluster6381.conf...

   3. 通过redis-cli命令为6台机器构建集群关系

      redis-cli -a 123456 --cluster create --cluster-replicas 1 192.168.111.175:6381 192.168.111.175:6382 192.168.111.172:6384 192.168.111.172:6385 192.168.111.174:6386 192.168.111.174:6387

      --cluster-replicas 1 表示为每个master创建一个slave节点

   4. 链接进入6381作为切入点，查看并检验集群状态

      + info replication
      + cluster info：查看某个集群节点信息
      + cluster nodes：查询集群节点信息

2. 3主3从redis集群读写

   set k1 v1报错error MOVED 12706 192.168.111.174:6385

   报错原因：一定注意槽位的范围区间，需要路由到位

   如何解决：防止路由失效加参数-c并新增两个key，redis-cli -a 123456 -p 6381 -c

   查看某个key该属于对应的槽位值：CLUSTER KEYSLOT 键名称

3. 主从容错切换迁移案例

   节点从属调整命令：CLUSTER FAILOVER

4. 主从扩容案例

   1. 新建6387、6388两个服务实例配置文件+新建后启动

   2. 启动87/88两个新的节点实例，此时他们自己都是master

   3. 将新增的6387节点（空槽号）作为master节点加入原集群

      redis-cli -a 密码 --cluster add-node 自己实际IP地址:6387 自己实际IP地址:6381

      6387就是将要作为master新增节点，6381就是原来集群节点里面的领路人，相当于6387八百6381的码头从而找到组织加入集群

   4. 重新分派槽号（reshard）

      redis-cli -a 密码 --cluster reshard IP地址:端口号

   5. 为主节点6387分配从节点6388

      redis-cli -a 密码 --cluster add-node ip:新slave端口 ip:新master端口 --cluster-slave --cluster-master-id 新主机节点id

5. 主从缩容案例

   1. 检测集群情况，先获得从节点6388的节点id

      redis-cli -a 密码 --cluster check 192.168.111.174:6388

   2. 从集群中将4号从节点6388删除

      redis-cli -a 密码 --cluster del-node ip:从机端口 从机6388节点ID

   3. 将6387的槽号清空，重新分配，本例将清出来的槽号都分配给6381

      redis-cli -a 123456 --cluster reshard ip:6381

   4. 将6387删除

      redis-cli -a 123456 --cluster del-node ip:端口 6387节点id

**集群常用操作命令和CRC16算法分析**

+ 不在同一个slot槽位下的多键操作支持不好，通识占位符登场

  不在同一个slot槽位下的键值无法使用mset、mget等多键操作，可以通过{}来定义一个组的概念，使key中的{}内相同内容的键值对放到一个slot槽位去，对照下图类似k1k2k3都映射为x，自然槽位一样

  mset k1{z} z1 k2{z} z2 k3{z} z3：存到同一个槽位

  mget k1{z} k2{z} k3{z}

+ redis中有16384个哈希槽，每个key通过CRC16校验后对16384取模来决定放置哪个槽。集群的每一节点负责一部分hash槽

+ 常用命令

  + 集群是否完整才能对外提供服务：cluster-require-full-coverage，默认为yes
  + CLUSTER COUNTKEYSINSLOT 槽位数字编号：结果为1该槽位被占用，为0该槽位没被占用
  + CLUSTER KEYSLOT 键名称：该键应该存在哪个槽位上

### SpringBoot集成Redis

**本地Java连接redis常见问题**

+ bind配置请注释掉
+ 保护模式设置为no
+ Linux系统的防火墙设置
+ redis服务器的IP地址和密码是否正确
+ 忘记写访问redis的服务端口号和auth密码

**Jedis**

Jedis Client是redis官网推荐的一个面向Java客户端，库文件实现了各类API进行封装调用

**Lettuce**

jedis和lettuce都是redis的客户端，他们都可以连接redis服务器，但是SpringBoot2.0之后默认都是使用lettuce这个客户端连接redis服务器。因为当使用jedis客户端连接redis服务器的时候，每个线程都要拿自己创建的jedis实例去连接redis客户端，当有很多个线程的时候，不仅开销大需要反复的创建关闭一个jedis连接，而且也是线程不安全的，一个线程通过jedis实例更改redis服务器中的数据之后会影响另一个线程

lettuce就不会出现上面的情况，lettuce底层使用的是Netty，当有多个线程需要连接redis服务器的时候，可以保证只创建一个lettuce连接，使所有的线程共享这一个lettuce连接，这样可以减少创建关闭一个lettuce连接时候的开销，而且这种方式也是线程安全的，不会出现一个线程通过lettuce更改redis服务器中的数据之后而影响另一个线程的情况

**RedisTemplate**

chcp 65001：修改控制台编码为UTF-8

redis-cli -a 123456 -p 6379 --raw：redis对中文的支持

redis-cli -a 123456 -p 6379 -c --raw：redis集群对中文的支持

```properties
#redis单机
spring.redis.database=0
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=123456
spring.redis.lettuce.pool.max-active=8
spring.redis.lettuce.pool.max-wait=-1ms
spring.redis.lettuce.pool.max-idle=8
spring.redis.lettuce.pool.min-idle=0

#redis集群
spring.redis.password=123456
#获取失败，最大重定向次数
spring.redis.cluster.max-redirects=3
spring.redis.lettuce.pool.max-active=8
spring.redis.lettuce.pool.max-wait=-1ms
spring.redis.lettuce.pool.max-idle=8
spring.redis.lettuce.pool.min-idle=0
#支持集群拓扑动态感应刷新，自适应拓扑刷新是否使用所有可用的更新，默认false关闭
spring.redis.lettuce.cluster.refresh.adaptive=true
#定时刷新
spring.redis.lettuce.cluster.period=2000
spring.redis.cluster.nodes=192.168.111.175:6381,192.168.111.175:6382,192.168.111.172:6383,192.168.111.172:6384
```

连接集群故障SpringBoot客户端没有动态感知到RedisCluster的最新集群信息，连接超时

原因：SpringBoot2.X版本，redis默认的连接池采用Lettuce，当redis集群节点发生变化后，lettuce默认是不会刷新节点拓扑

解决方案：

1. 排除lettuce采用jedis（不推荐）

2. 重写连接工厂实例（极度不推荐）

3. 刷新节点集群拓扑动态感应

   spring.redis.lettuce.cluster.refresh.adaptive=true
   spring.redis.lettuce.cluster.period=2000

### 缓存穿透与雪崩

**缓存穿透**（查不到）

用户想要查询一个数据，发现redis内存数据库中没有，也就是缓存没有命中，于是向持久层数据库查询。发现也没有，于是本次查询失败。当用户很多的时候，缓存都没有命中，于是就去请求了持久层数据库。这会给持久层数据库造成很大的压力，这时候就相当于出现了缓存穿透。

**布隆过滤器**

布隆过滤器是一种数据结构，对所有可能查询的参数以hash形式存储，在控制层先进行校验，不符合则丢弃，从而避免了对底层存储系统的查询压力；

但是这种方法会存在两个问题：

1. 如果空值能够被缓存起来，这就意味着缓存需要更多的空间存储更多的键，因为这当中可能会有很多的空值的键；
2. 即使对空值设置了过期时间，还是会存在缓存层的数据会有一段时间窗口的不一致，这对于需要保持一致性的业务会有影响。

**缓存击穿**（量太大，导致缓存过期）

缓存击穿是指一个key非常热点，在不停的扛着大并发，大并发集中对这一个点进行访问，当这个key在失效的瞬间，持续的大并发就穿破缓存，直接请求数据库。

当某个key在过期的瞬间，有大量的请求并发访问，这类数据一般是热点数据，由于缓存过期，会同时访问数据库来查询最新的数据，并且回写缓存，会导致数据库压力过大。

> **解决方案**

**设置热点永不过期**

从缓存层来看，没有设置过期时间，所以不会出现热点key过期后产生的问题。

**加互斥锁**

分布式锁：使用分布式锁，保证对于每个key同时只有一个线程去查询后端服务，其他线程没有获得分布式锁的权限，因此只需要等待即可。这种方式将高并发的压力转移到了分布式锁，因此对分布式锁的考验很大。

### 缓存雪崩

缓存雪崩，是指在某一个时间段，缓存集中过期失效，redis宕机。

> 解决方案

**redis高可用**

这个思想的含义是，既然redis有可能挂掉，那我多增设几台redis，这样一台挂掉之后其他的还可以继续工作，其实就是搭建的集群（异地多活）

**限流降级**

这个解决方案的思想是，在缓存失效后，通过加锁或者队列来控制读数据库写缓存的线程数量。比如对某个key只允许一个线程查询数据和写缓存，其他线程等待。

**数据预热**

数据加热的含义就是在正式部署之前，我先把可能的数据先预先访问一遍，这样部分可能大量访问的数据就会加载到缓存中。在即将发生大并发前手动触发加载缓存不同的key，设置不同的过期时间，让缓存失效的时间点尽量均匀。se't