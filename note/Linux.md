### Linux基础

Linux主要发行版本：**Ubuntu(乌班图)、RedHat(红帽)、CentOS**、Debain(蝶变)、Fedora、SuSE、OpenSUSE

#### Linux目录结构

+ /bin：binary的缩写，存放经常使用的命令
+ /sbin：s是Super User的意思，这里存放的是系统管理员使用的系统管理程序
+ /home：存放普通用户的主目录，在Linux中每个用户都有一个自己的目录，一般该目录名是以用户的账号命名
+ /root：该目录为系统管理员，也称作超级权限者的用户主目录
+ /lib：系统开机所需要的最基本的动态连接共享库，其作用类似于windows里的dll文件。几乎所有的应用程序都需要用到这些共享库
+ /lost+found：这个目录一般是空的，当系统非法关机后，这里就存放一些文件
+ /etc：所有系统管理所需要的配置文件和子目录
+ /user：这是一个非常重要的目录，用户的很多应用程序和文件都放在这个目录下，类似于Windows下的program files目录
+ /boot：存放的是启动Linux时使用的一些核心文件，包括一些连接文件以及镜像文件
+ /proc：这个目录是一个虚拟的目录，他是系统内存的映射，访问这个目录来获取系统信息
+ /srv：service的缩写，该目录存放一些服务启动之后需要提取的数据
+ /sys：这是Linux2.6内核的一个很大的变化，该目录下安装了2.6内核中新出现的一个文件系统sysfs
+ /tmp：这个目录是用来存放一些临时文件的
+ /dev：类似于Windows的设备管理器，把所有的硬件用文件的形式存储
+ /media：Linux系统会自动识别一些设备，例如U盘，光驱等等，当识别后，Linux会把识别的设备挂载到这个目录下
+ /mnt：系统提供该目录就是为了让用户临时挂载别的文件系统的，我们可以将外部的存储挂载在/mnt/上，然后进入该目录就可以查看里面的内容了
+ /opt：这是给主机额外安装软件所摆放的目录。如安装ORACLE数据库就可以放到该目录下。默认为空
+ /usr/local：这是另一个给主机额外安装软件所安装的目录。一般是通过编译源码的方式安装的程序
+ /var：这个目录存放着在不断扩充着的东西，习惯将经常被修改的目录放在这个目录下，包括各种日志文件
+ /selinux：SELinux是一种安全子系统，他能控制程序只能访问特定文件，有三种工作模式，可以自行设置

#### Vi和Vim编辑器

vi和vim常用的三种模式

+ 正常模式：以vim打开一个档案就直接进入一般模式了（这是默认的模式），在这个模式中，你可以使用上下左右按键来移动光标，你可以使用删除字符或删除整行来处理档案内容，也可以使用复制粘贴来处理文件数据
+ 插入模式：按下i、I、o、O、a、A、r、R等任何一个字母之后才会进入编辑模式，一般来说按i就可以了
+ 命令行模式：输入esc再输入:在这个模式当中，可以提供你相关的命令，完成读取、存盘、替换、离开vim、显示行号等的动作则是在此模式中达成的

![](/img/Linux_1.png)

**快捷键使用**

+ 拷贝当前行 yy，拷贝当前行向下的5行 5yy，并粘贴（输入p），一般模式下
+ 删除当前行 dd，删除当前行向下的5行 5dd
+ 在文件中查找某个单词【命令行下/关键字，回车查找，输入n就是查找下一个】
+ 设置文件的行号，取消文件的行号.【命令行下:set bu和:set nonu】
+ 编辑/etc/profile文件，使用快捷键到该文档的最末行【G】和最首行【gg】
+ 在一个文件中输入hello，然后又撤销这个动作【u】，一般模式下
+ 编辑/etc/profile文件，并将光标移动到20行【输入20，再输入shift+g】，一般模式下

![](/img/Linux_2.png)

#### 关机重启命令

+ shutdown -h now：立刻进行关机
+ shutdown -h 1："hello，1分钟后会关机了"
+ shutdown -r now：现在重新启动计算机
+ halt：关机，作用和上面一样
+ reboot：现在重新启动计算机
+ sync：把内存的数据同步到磁盘

注意：

+ 不管是重启系统还是关闭系统，首先要允许sync命令，把内存中的数据写到磁盘中
+ 目前的shutdown/reboot/halt等命令均已经在关机前进行了sync，但小心驶得万年船

#### 用户登录和注销

+ 登陆时尽量少用root账号登录，因为他是系统管理员，最大的权限，避免操作失误。可以利用普通用户登录，登陆后再用su - 用户名命令来切换成系统管理员身份
+ 在提示符下输入logout即可注销用户

#### 用户管理

**添加用户**

+ useradd 用户名：默认用户的家目录在/home/用户名

  当创建用户成功时，会自动创建和用户同名的家目录，也可以通过useradd -d 指定目录 新的用户名，给新创建的用户指定家目录

**指定/修改密码**

+ password 用户名
+ pwd：显示当前用户所在的目录

**删除用户**

+ userdel 用户名：删除用户，但是要保留家目录
+ userdel -r 用户名：删除用户以及用户家目录

一般情况下建议保留家目录

**查询用户信息指令**

+ id 用户

**切换用户**

+ su - 切换用户名

从权限高的用户切换到权限低的用户，不需要输入密码，反之需要

当需要返回到原来的用户时，使用exit/logout指令

**查看当前用户/登录用户**

+ whoami/who am i

**用户组**

类似于角色，系统可以对有共性/权限的多个用户进行统一的管理

+ groupadd 组名：增加组
+ groupdel 组名：删除组
+ useradd -g 用户组 用户名：增加用户时直接加上组
+ usermod -g 用户组 用户名：修改用户的组

用户和组相关文件

+ /etc/passwd文件：用户（user）的配置文件，记录用户的各种信息

  每行的含义：用户名:口令:用户标识号:组标识号:注释性描述:主目录:登录Shell

+ /etc/shadow文件：口令的配置文件

  每行的含义：登录名:加密口令:最后一次修改时间:最小时间间隔:最大时间间隔:警告时间:不活动时间:失效时间:标志

+ /etc/group文件：组（group）的配置文件，记录Linux包含的组信息

  每行含义：组名:口令:组标识号:组内用户列表


#### 运行级别

运行级别说明：

+ 0：关机
+ 1：单用户【找回丢失的密码】
+ 2：多用户状态没有网络服务
+ 3：多用户状态有网络服务
+ 4：系统未使用保留给用户
+ 5：图形界面
+ 6：系统重启

常用运行级别时3和5，也可以指定默认运行级别

+ init [0123456]：切换不同的运行级别

CentOS7之前，在/etc/inittab文件中设置，CentOS7之后进行了简化：

multi-user.target：等同于3级别

graphocal.target：等同于5级别

+ systemctl get-default：查看当前默认级别
+ systemctl set-default TARGET.target：设置默认级别

#### Linux找回root密码（CentOS7.6）

1. 首先启动系统，进入开机界面，在界面中按e进入编辑界面
2. 进入编辑界面，使用键盘上的上下键把光标往下移动，找到以Linux16开头内容所在的行数，在行的最后面输入：init=/bin/sh
3. 接着，输入完成后，直接按快捷键：Ctrl + x 进入单用户模式
4. 接着，在光标闪烁的位置中输入：mount -o remount,rw /，完成后按键盘的回车键
5. 在新的一行最后面输入：passwd，完成后按键盘的回车键。输入密码，然后再次确认密码即可。密码修改成功后，会显示passwd...的样式，说明密码修改成功
6. 接着，在鼠标闪烁的位置中，输入：touch /.autorelabel，完成后按回车
7. 继续在光标闪烁的位置中，输入：exec /sbin/init，完成后按回车，等待系统自动修改密码，完成后，系统会自动重启，新的密码生效了

#### 帮助指令

+ man：获得帮助信息

  基本语法：man [命令或配置文件] ，获得帮助信息

  例如：查看ls命令的帮助信息，man ls

  在Linux下，隐藏文件是以.开头的

+ help

  基本语法：help 命令，获得shell内置命令的帮助信息

#### 文件目录类

+ pwd：显示当前工作目录的绝对路径

+ ls：ls [选项] [目录或是文件]

  常用选项

  + -a：显示当前目录所有的文件和目录，包括隐藏的

  + -l：以列表的方式显示信息

+ cd：cd [参数]，切换到指定目录

  + cd ~或者cd ：回到自己的家目录，比如你是root，就回到/root，tom回到/home/tom
  + cd ..：回到当前目录的上一级目录

+ mkdir：mkdir [选项] 要创建的目录，创建目录

  常用选项

  + -p：创建多级目录

+ rmdir：rmdir [选项] 要删除的空目录，删除空目录

  rmdir删除的是空目录，如果目录下有内容是无法删除的

  如果要删除非空目录，需要使用rm -rf 要删除的目录

+ touch：touch 文件名称，创建空文件

  touch hello.txt

+ cp：cp [选项] source dest，拷贝文件到指定目录

  常用选项

  + -r：递归复制整个文件夹

  强制覆盖不提示的方法：\cp，例如 \cp -r /home/bbb /opt 

+ rm：rm [选项] 要删除的文件或目录，移除文件或目录

  常用选项：

  + -r：递归删除整个文件夹
  + -f：强制删除不提示

+ mv：移动文件与目录或重命名

  基本语法

  + mv oldNameFile newNameFile：重命名
  + mv /temp/movefile /targetFolder：移动文件

+ cat：cat [选项] 要查看的文件，查看文件内容

  常用选项

  + -n：显示行号

  cat只能浏览文件，而不能修改文件，为了浏览方便，一般会带上管道命令| more

+ more：more指令是一个基于VI编辑器的文本过滤器，他以全屏幕的方式按页显示文本文件的内容。more指令中内置了若干快捷键

  基本语法

  more 要查看的文件

  操作说明

  | 操作            | 功能说明                             |
  | --------------- | ------------------------------------ |
  | 空白键（space） | 代表向下翻一页                       |
  | Enter           | 代表向下翻一行                       |
  | q               | 代表立刻离开more，不再显示该文件内容 |
  | Ctrl + F        | 向下滚动一屏                         |
  | Ctrl + B        | 返回上一屏                           |
  | =               | 输出当前行行号                       |
  | :f              | 输出文件名和当前行行号               |

+ less：less指令用来分屏查看文件内容，他的功能与more指令类似，但是比more指令更加强大，支持各种显示终端。less指令在显示文件内容时，并不是一次将整个文件加载之后才显示，而是根据显示需要加载内容，对于显示大型文件具有较高的效率

  基本语法

  less 要查看的文件

  操作说明

  | 操作       | 功能说明                                       |
  | ---------- | ---------------------------------------------- |
  | 空白键     | 向下翻动一页                                   |
  | [pagedown] | 向下翻动一页                                   |
  | [pageup]   | 向上翻动一页                                   |
  | /字串      | 向下搜索[字串]的功能；n：向下查找，N：向上查找 |
  | ?字串      | 向上搜索[字串]的功能；n：向上查找，N：向下查找 |
  | q          | 离开less这个程序                               |

+ echo：echo [选项] [输出内容]，输出内容到控制台

+ head：head用于显示文件的开头部分内容，默认情况下head指令显示文件的前10行内容

  基本语法：

  + head 文件：查看文件头10行内容
  + head -n 5 文件：查看文件头5行内容，5可以是任意行数

+ tail：tail用于输出文件中尾部的内容，默认情况下tail指令显示文件的前10行内容

  基本语法

  + tail 文件：查看文件尾10行内容
  + tail -n 5 文件：查看文件尾5行内容，5可以是任意行数
  + tail -f 文件：实时追踪该文档的所有更新

+ `>`指令和>>指令：> 输出重定向和>>追加

  基本语法

  + ls -l > 文件：列表的内容写入文件a.txt中
  + ls -al >> 文件：列表的内容追加到文件aa.txt的末尾
  + cat 文件1 > 文件2：将文件1的内容覆盖到文件2
  + echo 内容 >> 文件

+ ln：软链接也称为符号链接，类似于Windows里的快捷方式，主要存放了链接其他文件的路径

  基本语法

  + ln -s [原文件或目录] [软链接名]：给原文件创建一个软链接
  + rm /home/myroot：删除软链接myroot

+ history：查看已经执行过的历史命令，也可以执行历史命令

  基本语法

  + history：查看所有的历史命令
  + history 10：显示最近使用的10条指令
  + !5：执行历史编号为5的指令


#### 时间日期类

+ date：显示当前日期，设置日期

  基本语法

  + date：显示当前时间
  + date +%Y：显示当前年份
  + date +%m：显示当前月份
  + date +%d：显示当前是哪一天
  + date +"%Y-%m-%d %H:%M:%S"：显示年月日时分秒
  + date -s 字符串时间：设置系统时间

+ cal：cal [选项]，查看日历指令，不加选项，显示本月日历

  显示2023年日历：cal 2023

#### 搜索查找类

+ find：find指令将从指定目录下递归地遍历其各个子目录，将满足条件的文件或者目录显示在终端

  基本语法

  + find [搜索范围] [选项]

    | 选项            | 功能                             |
    | --------------- | -------------------------------- |
    | -name<查询方式> | 按照指定的文件名查找模式查找文件 |
    | -user<用户名>   | 查找属于指定用户名所有文件       |
    | -size<文件大小> | 按照指定的文件大小查找文件       |

    根据名称查找/home目录下的hello.txt文件：find /home -name hello.txt

    查找/opt目录下，用户名称为nobody的文件：find /opt -user nobody

    查找整个linux系统下大于200M的文件（+n 大于 -n 小于 n 等于，单位有k,M,G）：find / -size +200M

+ locate：locate指令可以快速定位文件路径。locate指令利用事先建立的系统中所有文件名称及路径的locate数据库实现快速定位给定的文件。locate指令无需遍历整个文件系统，查询速度较快。为了保证查询结果的准确度，管理员必须定期更新locate实例

  基本语法

  + locate 搜索文件

  特别说明：由于locate指令基于数据库进行查询，所以第一次运行前，必须使用updatedb指令创建locate数据库

  which指令，可以查看某个指令在哪个目录下，比如ls指令在哪个目录下：which ls

+ grep指令和管道符号|：grep过滤查找，管道符|，表示将前一个命令的处理结果输出传递给后面的命令处理

  基本语法

  + grep [选项] 查找内容 源文件：

  常用选项

  + -n：显示匹配行及行号
  + -i：忽略字母大小写

  在hello.txt文件中，查找yes所在行，并且显示行号

  + 写法1：cat /home/hello.txt | grep -n "yes"
  + 写法2：grep -n "yes" /home/hello.txt

#### 压缩和解压类

+ gzip/gunzip：gzip用于压缩文件，gunzip用于解压的

  基本语法

  + gzip 文件：压缩文件，只能将文件压缩为*.gz文件
  + gunzip 文件.gz：解压缩文件命令

+ zip/unzip：zip用于压缩文件，unzip用于解压的，这个在项目打包发布中很有用的

  基本语法

  + zip [选项] xxx.zip 将要压缩的内容：压缩文件和目录的命令
  + unzip [选项] xxx.zip：解压缩文件

  zip常用选项

  + -r：递归压缩，即压缩目录

  unzip常用选项

  + -d<目录>：指定解压后文件的存放目录

+ tar：tar指令是打包指令，最后打包后的文件是.tar.gz的文件

  基本语法

  + tar [选项] xxx.tar.gz 打包的内容：打包目录，压缩后的文件格式.tar.gz

  选项说明

  | 选项 | 功能               |
  | ---- | ------------------ |
  | -c   | 产生.tar打包文件   |
  | -v   | 显示详细信息       |
  | -f   | 指定压缩后的文件名 |
  | -z   | 打包同时压缩       |
  | -x   | 解包.tar文件       |

  压缩多个文件，将/home/pig.txt和/home/cat.txt压缩成pc.tar.gz：tar -zcvf pc.tar.gz /home/pig.txt /home/cat.txt

  将/home的文件夹压缩成myhome.tar.gz：tar -zcvf myhome.tar.gz /home/

  将pc.tar.gz解压当当前目录：tar -zxvf pc.tar.gz

  将myhome.tar.gz解压到/opt/tmp2目录下：(1)mkdir /opt/tmps (2)tar -zxcf /home/myhome.tar.gz -C /opt/tmp2

#### 组管理和权限管理

在Linux中的每个用户都必须属于一个组，不能独立于组外。在Linux中每个文件有所有者、所有组、其他组的概念

文件/目录所有者：一般为文件的创建者，谁创建了该文件，就自然的成为该文件的所有者

+ ls -ahl：查看文件所有者
+ chown 用户名 文件名：修改文件所有者
+ groupadd 组名：组的创建

当某个用户创建了一个文件后，这个文件的所在组就是该用户所在的组

+ ls -ahl：查看文件/目录所在组
+ chgrp 组名 文件名：修改文件所在的组

除文件的所有者和所在组的用户外，系统的其他用户都是文件的其他组

在添加用户时，可以指定将该用户添加到哪个组中，同样的用root的管理权限可以改变某个用户所在的组

+ usermod -g 组名 用户名：改变用户所在组
+ usermod -d 目录名 用户名：改变该用户登录的初始目录，用户需要有进入到新目录的权限

**权限管理**

ls -l中显示的内容如下：

-rwxrw-r-- 1 root root 1213 Feb 2 09:39 abc

0-9位说明：

1. 第0位确定文件类型（d、-、l、c、b）

   l是链接，相当于Windows的快捷方式

   d是目录，相当于Windows的文件夹

   c是字符设备文件，鼠标、键盘

   b是块设备，比如硬盘

   -是普通文件

2. 第1-3位确定所有者（该文件的所有者）拥有该文件的权限

3. 第4-6位确定所属组（同用户组）拥有该文件的权限

4. 第7-9位确定其他用户拥有该文件的权限

**rwx权限详解**

+ rwx作用到文件
  1. r代表可读：可以读取查看
  2. w代表可写：可以修改，但是不代表可以删除该文件，删除一个文件的前提条件是对该文件所在的目录有写权限，才能删除该文件
  3. x代表可执行：可以被执行
+ rwx作用到文件夹
  1. r代表可读：可以读取，ls查看目录内容
  2. w代表可写：可以修改，对目录内创建+删除+重命名目录
  3. x代表可执行：可以进入该目录

可用数字表示为：r=4，w=2，x=1

**修改权限-chmod**

通过chmod指令，可以修改文件或者目录的权限

+ +、-、=变更权限，u：所有者 g：所有组 o：其他人 a：所有人（u、g、o的总和）

  + chmod u=rwx,g=rx,o=x 文件/目录名

  + chmod o+w 文件/目录名
  + chmod a-x 文件/目录名

+ 通过数字变更权限

  chmod u=rwx,g=rx,o=x 文件/目录名，相当于chmod 751 文件/目录名

**修改文件所有者-chown**

+ chown newowner 文件/目录：改变所有者
+ chown newowner:newgroup 文件/目录：改变所有者和所在组
+ -R：如果是目录，则使其下所有子文件或目录递归生效

**修改文件/目录所在组-chgrp**

+ chgrp newgroup 文件/目录：改变所有组
+ -R：递归修改

#### 定时任务调度

crontab进行定时任务的设置

任务调度：是指系统在某个时间执行的特定的命令或程序

任务调度分类：

+ 系统工作：有些重要的工作必须周而复始的进行，如病毒扫描等
+ 个别用户工作：个别用户可能希望执行某些程序，比如对mysql数据库的备份

基本语法

+ crontab [选项]
+ service crond restart：重启任务调度

| 选项 | 功能                          |
| ---- | ----------------------------- |
| -e   | 编辑crontab定时任务           |
| -l   | 查询crontab任务               |
| -r   | 删除当前用户所有的crontab任务 |

设置任务调度文件：/etc/crontab

设置个人任务调度，执行crontab -e命令，接着输入任务到调度文件，如*/1 * * * * ls -l /etc/ > /tmp/to.txt命令，意思是说每小时的每分钟执行ls -l /etc/ > /tmp/to.txt命令

5个占位符的说明

| 项目    | 含义                 | 范围                    |
| ------- | -------------------- | ----------------------- |
| 第一个* | 一小时当中的第几分钟 | 0-59                    |
| 第二个* | 一天当中的第几小时   | 0-23                    |
| 第三个* | 一个月当中的第几天   | 1-31                    |
| 第四个* | 一年当中的第几月     | 1-12                    |
| 第五个* | 一周当中的星期几     | 0-7（0和7都代表星期日） |

特殊符号的说明

| 特殊符号 | 含义                                                         |
| -------- | ------------------------------------------------------------ |
| *        | 代表任何时间                                                 |
| ,        | 代表不连续的时间，比如0 8,12,16 * * *，代表每天的8点，12点，16点执行 |
| -        | 代表连续的时间范围，比如0 5 * * 1-6，代表周一到周六的5点执行 |
| */n      | 代表多久执行一次，比如*/10 * * * *，代表每隔10分钟执行       |

**at定时任务**

1. at命令是一次性定时计划任务，at的守护进程atd会以后台模式运行，检查作业队列来运行
2. 默认情况下，atd守护进程每60秒检查作业队列，有作业时，会检查作业运行时间，如果时间与当前时间匹配，则运行此作业
3. at命令是一次性定时计划任务，执行完一个任务后不再执行此任务了
4. 在使用at命令的时候，一定要保证atd进程的启动，可以使用相关指令来查看，ps -ef | grep atd

+ at [选项] [时间]

  Ctrl + D：结束at命令的输入

+ atq：查看系统中没有执行的工作任务

+ atrm 编号：删除已经设置的任务，atrm 5

  at命令选项：

  | 选项         | 含义                                                     |
  | ------------ | -------------------------------------------------------- |
  | -m           | 当指定的任务被完成后，将给用户发送邮件，即使没有标准输出 |
  | -I           | atq的别名                                                |
  | -d           | atrm的别名                                               |
  | -v           | 显示任务将被执行的时间                                   |
  | -c           | 打印任务的内容到标准输出                                 |
  | -V           | 显示版本信息                                             |
  | -q<队列>     | 使用指定的队列                                           |
  | -f<文件>     | 从指定的文件读入任务而不是从标准输入读入                 |
  | -t<时间参数> | 以时间参数的形式提交要运行的任务                         |

  at时间定义：

  1. 接受在当前的hh:mm（小时:分钟）式的时间指定。假如该时间已过去，那么就放在第二天执行
  2. 使用midnight（深夜），noon（中午），teatime（饮茶时间，一般是下午4点）等比较模糊的词语来指定时间
  3. 采用12小时计时制，即在时间后面加上AM（上午）或PM（下午）来说明是上午还是下午。例如12pm
  4. 指定命令执行的具体日期，指定格式为month day（月 日）或mm/dd/yy（月/日/年）或dd.mm.yy（日.月.年），指定的日期必须跟在指定时间的后面。例如04:00 2021-03-01
  5. 使用相对计时法。指定格式为：now + count time-units，now就是当前时间，time-units是时间单位，这里能够是minutes（分钟）、hours（小时）、days（天）、weeks（星期）、count是时间数量，几天，几小时。例如now + 5 minutes
  6. 直接使用today（今天）、tomorrow（明天）来指定完成命令的时间

#### Linux磁盘分区、挂载

**Linux分区**

1. Linux来说无论有几个分区，分给哪一目录使用，它归根结底就只有一个根目录，一个独立且唯一的文件结构，Linux中每个分区都是用来组成整个文件系统的一部分
2. Linux采用了一种叫载入的处理方法，它的整个文件系统中包含了一整套的文件和目录，且将一个分区和一个目录联系起来。这时要载入的一个分区将使它的存储空间在一个目录下获得

![](/img/Linux_3.png)

**硬盘说明**

1. Linux硬盘分为IDE硬盘和SCSI硬盘，目前基本上是SCSI硬盘
2. 对于IDE硬盘，驱动器标识符为hdx~，其中hd表明分区所在设备的类型，这里是指IDE硬盘。x为盘号（a为基本盘，b为基本盘从属盘，c为辅助主盘，d为辅助从属盘），~代表分区，前四个分区用数字1到4表示，他们是主分区或扩展分区，从5开始就是逻辑分区。例：hda3表示为第一个IDE硬盘上的第三个主分区或扩展分区，hdb2表示为第二个IDE硬盘上的第二个主分区或扩展分区
3. 对于SCSI则标识为sdx~，SCSI硬盘是用sd来表示分区所在设备的类型的，其余则和IDE硬盘的表示方法一样

**查看所有设备挂载情况**

+ lsblk或者lsblk -f

**磁盘情况查询**

+ df -h：查询系统整体磁盘使用情况

+ du -h /目录：查询指定目录的磁盘占用情况，默认为当前目录

  + -s：指定目录占用大小汇总
  + -h：带计量单位
  + -a：带文件
  + --max-depth=1：子目录深度
  + -c：列出明细的同时，增加汇总值

  例：du -hac --max-depth=1 /opt

**工作实用指令**

1. 统计/opt文件夹下文件的个数：ls -l /opt | grep "^-" | wc -l
2. 统计/opt文件夹下目录的个数：ls -l /opt | grep "^d" | wc -l
3. 统计/opt文件夹下文夹的个数，包括子文件夹里的：ls -lR /opt | grep "^-" | wc -l
4. 统计/opt文件夹下目录的个数，包括子文件夹里的：ls -lR /opt | grep "^d" | wc -l
5. 以树状显示目录结构：tree 目录，如果没有tree，则使用yum install tree安装

#### 网络配置

+ ifconfig：查看Linux的网络配置
+ ping 目的主机：测试当前服务器是否可以连接目的主机

**网络环境配置**

+ 自动获取：登陆后，通过界面的来设置自动获取ip，特点：Linux启动后会自动获取IP，缺点是每次自动获取的ip地址可能不一样

+ 指定ip：直接修改配置文件来指定IP，并可以连接到外网

  编辑：vi /etc/sysconfig/network-scripts/ifcfg-ens33

  要求：将ip地址配置的静态的，比如：ip地址为192.168.200.130

  ifcfg-ens33文件说明

  + DEVICE=eth0：接口名（设备，网卡）
  + HWADDR=00:0C:2x:6x:0x:xx：MAC地址
  + TYPE=Ethernet：网络类型，通常是Ethernet
  + UUID=926a57ba-92c6-4231-bacb-f27e5e6a9f44：随机id
  + ONBOOT=yes：系统启动的时候网络接口是否有效
  + BOOTPROTO=static：ip的配置方法【none|static|bootp|dhcp】引导时不使用协议|静态分配IP|BOOTP协议|DHCP协议
  + IPADDR=192.168.200.130：ip地址
  + GATEWAY=192.168.200.2：网关
  + DNS1=192.168.200.2：域名解析器

  重启网络服务或者重启系统生效：service network restart、reboot

**设置主机名和hosts映射**

+ 设置主机名

  1. 为了方便记忆，可以给Linux系统设置主机名，也可以根据需要修改主机名
  2. 指令hostname：查看主机名
  3. 修改文件在/etc/hostname指定
  4. 修改后，重启生效

+ 设置hosts映射

  在/etc/hosts文件指定：192.168.200.1 ThinkPad-PC

  hosts是一个文本文件，用来记录ip和hostname的映射关系

主机解析域名分析

1. 浏览器先检查浏览器缓存中有没有该域名解析IP地址，有就先调用这个IP完成解析；如果没有，就检查DNS解析器缓存，如果有直接返回IP完成解析。这两个缓存，可以理解为本地解析器缓存

2. 一般来说，当电脑第一次成功访问某一网站后，在一定时间内，浏览器或操作系统会缓存它的IP地址（DNS解析记录），如在cmd窗口输入：

   ipconfig /displaydns：DNS域名解析缓存

   ipconfig /flushdns：手动清理dns缓存

3. 如果本地解析器缓存没有找到对应映射，检查系统中hosts文件中有没有配置对应的域名IP映射，如果有，则完成解析并返回

4. 如果本地DNS解析器缓存和hosts文件中均没有找到对应IP，则到域名服务DNS进行解析域

#### 进程管理

1. 在Linux中，每个执行的程序都被称为一个进程，每一个进程都分配一个id号（pid，进程号）

2. 每个进程都可能以两种方式存在的。前台与后台，所谓前台进程就是用户目前的屏幕上可以进行操作的。后台进程则是实际在操作，但由于屏幕上无法看到的进程，通常使用后台方式执行
3. 一般系统的服务都是以后台进程的方式存在，而且都会常驻在系统中，直到关机才结束

**显示系统执行的进程**

ps命令是用来查看目前系统中，有哪些正在执行，以及他们执行的状况。可以不加任何参数

+ ps -a：显示当前终端的所有进程信息
+ ps -u：以用户的格式显示进程信息
+ ps -x：显示后台进程运行的参数
+ ps -ef：以全格式显示当前所有的进程

ps显示的信息选项：

| 字段    | 说明                                                         |
| ------- | ------------------------------------------------------------ |
| PID     | 进程识别号                                                   |
| PPID    | 父进程id                                                     |
| TTY     | 终端机号                                                     |
| TIME    | 此进程所消CPU时间                                            |
| CMD     | 正在执行的命令或进程名                                       |
| VSZ     | 进程占用的虚拟内存大小（单位：KB）                           |
| RSS     | 进程占用的物理内存大小（单位：KB）                           |
| TT      | 终端名称                                                     |
| STAT    | 进程状态，S-睡眠，s-表示该进程是会话的先导进程，N-表示进程拥有比普通优先级更低的优先级，R-正在运行，D-短期等待，Z-僵死进程，T-被追踪或者被停止等等 |
| STARTED | 进程的启动时间                                               |
| TIME    | CPU时间，即进程使用CPU的总时间                               |
| COMMAND | 启动进程所用的命令和参数，如果过长会被截断显示               |
| C       | CPU用于计算执行优先级的因子。数值越大，表明进程是CPU密集型运算，执行优先级会降低；数值越小，表明进程是I/O密集型运算，执行优先级会提高 |

**终止进程**

若是某个进程执行一半需要停止时，或是已消了很大的系统资源时，此时可以考虑停止该进程，使用kill命令来完成此项任务

+ kill [选项] 进程号：通过进程号杀死进程
+ killall 进程名称：通过进程名称杀死进程，也支持通配符，这在系统因负载过大而变得很慢时很有用

常用选项

- -9：表示强迫进程立即停止

**查看进程树**

+ pstree [选项]：可以更加直观的来看进程信息

常用选项

+ -p：显示进程的PID
+ -u：显示进程的所属用户

**服务管理**

服务本质就是进程，但是是运行在后台的，通常都会监听某个端口，等待其他程序的请求，比如mysql、sshd、防火墙等，因此我们又称为守护进程，是Linux中非常重要的知识点

service管理指令：

+ service 服务名 [start | stop | restart | reload | status]

在CentOS7.0后，很多服务不再使用service，而是systemctl

service指令管理的服务在/etc/init.d查看

**服务的运行级别**

Linux系统有7种运行级别（runlevel）：常用的级别是3和5

+ 0：系统停机状态，系统默认运行级别不能设为0，否则不能正常启动
+ 1：单用户工作状态，root权限，用于系统维护，禁止远程登录
+ 2：多用户状态（没有NFS），不支持网络
+ 3：完全的多用户状态（有NFS），登录后进入控制台命令行模式
+ 4：系统未使用，保留
+ 5：X11控制台，登陆后进入图形GUI模式
+ 6：系统正常关闭并重启，默认运行级别不能设为6，否则不能正常启动

开机的流程说明：

开机 -> BIOS -> /boot -> systemd进程1 -> 运行级别 -> 运行级别对应的服务

**chkconfig指令**

1. 通过chkconfig命令可以给服务的各个运行级别设置自 启动/关闭
2. chkconfig指令管理的服务在 /etc/init.d查看
3. 注意：CentOS7.0后，很多服务使用systemctl管理

+ chkconfig --list [| grep xxx]：查看服务
+ chkconfig 服务名 --list
+ chkconfig --level 5 服务名 on/off

**systemctl管理指令**

+ systemctl [start | stop | restart | status] 服务名
+ systemctl list-unit-files [| grep 服务名]：查看服务开机启动状态，grep可以进行过滤
+ systemctl enable 服务名：设置服务开机启动
+ sytemctl disable 服务名：关闭服务开机启动
+ systemctl is-enabled 服务名：查询某个服务是否是自启动的

systemctl指令管理的服务在/usr/lib/systemd/system查看

**firewall指令**

在真正的生产环境，往往需要将防火墙打开，但问题来了，如果我们把防火墙打开，那么外部请求数据包就不能跟服务器监听端口通讯。这时，需要打开指定的端口。比如80、22、8080等等

+ firewall-cmd --permanent --add-port=端口号/协议：打开端口
+ firewall-cmd --permanent --remove-port=端口号/协议：关闭端口
+ firewall-cmd --reload：重新载入才能生效
+ firewall-cmd --query-port=端口/协议：查询端口是否开放

**动态监控进程**

top和ps命令相似，他们都是用来显示正在执行的进程。top与ps最大的不同之处，在于top在执行一段时间可以更新正在运行的进程

+ top [选项]

选项说明：

| 选项    | 功能                                       |
| ------- | ------------------------------------------ |
| -d 秒数 | 指定top命令每隔几秒更新。默认是3秒         |
| -i      | 使top不显示任何闲置或者僵死的线程          |
| -p      | 通过指定监控进程id来仅仅监控某个进程的状态 |

![](/img/Linux_4.png)

交互操作

| 操作 | 功能                          |
| ---- | ----------------------------- |
| P    | 以CPU使用率排序，默认就是此项 |
| M    | 以内存的使用率排序            |
| N    | 以PID排序                     |
| q    | 退出top                       |

监控特定用户：top，回车，查看执行的进程，然后输入u，回车，再输入用户名

终止指定的进程：top，回车，查看执行的进程，然后输入k，回车，再输入要结束的进程id号

**监控网络状态**

+ netstat [选项]

选项说明

+ -an：按一定顺序排列输出
+ -p：显示哪个进程在调用

#### RPM与YUM

rpm用于互联网下载包的打包及安装工具，它包含在某些Linux分发版中。它生成具有.RPM扩展名的文件。RPM是RedHat Package Manager（RedHat软件包管理工具）的缩写，类似Windows的setup.exe，这一文件格式名称虽然打上了RedHat的标志，但理念是通用的

+ rpm -qa|grep xx：查询已安装的rpm列表
+ rpm -qa|more：
+ rpm -qa：查询所安装的所有rpm软件包
+ rpm -q 软件包名：查询软件包是否安装
+ rpm -qi 软件包名：查询软件包信息
+ rpm -ql 软件包名：查询软件包中的文件
+ rpm -qf 文件全路径名：查询文件所属的软件包
+ rpm -e RPM包的名称：卸载RPM包
+ rpm -e --nodeps RPM包的名称：强制删除，一般不推荐这样做
+ rpm -ivh RPM包全路径名称：安装rpm包
  + -i：install安装
  + v：verbose提示
  + h：hash进度条
+ 

rpm包名基本格式，一个rpm包名firefox-60.2.2-1.el7.centos.x86_64，名称firefox，版本号60.2.2-1，适用操作系统el7.centos.x86_64，表示centos7.x的64位系统，如果是i686、i386表示32位系统，noarch表示通用

**yum**

yum是一个shell前端软件包过滤器。基于RPM包管理，能够从指定的服务器自动下载RPM包并且安装，可以自动处理依赖性关系，并且一次安装所有依赖的软件包

+ yum list|grep xx软件列表：查询yum服务器是否有需要安装的软件
+ yum install xxx：下载安装

### Shell编程

shell是一个命令行解释器，他为用户提供了一个向Linux内核发送请求以便运行程序的界面系统级程序，用户可以用shell来启动、挂起、停止甚至是编写一些程序

shell脚本格式要求

1. 脚本以#!/bin/bash开头
2. 脚本需要可执行权限

脚本的常用执行方式

1. 输入脚本的绝对路径或相对路径

   首先要赋予脚本+x权限，再执行脚本

2. sh+脚本

   不用赋予脚本+x权限，直接执行即可

#### Shell的变量

1. Linux Shell中的变量分为系统变量和用户自定义变量
2. 系统变量：$HOME、$PWD、$SHELL、$USER等等，比如 echo $HOME等等
3. 显示当前shell中所有变量：set

**shell变量的定义**

1. 定义变量：变量名=值
2. 撤销变量：unset 变量
3. 声明静态变量：readonly变量，注意：不能unset

定义变量的规则：

1. 变量名称可以由字母、数字和下划线组成，但是不能以数字开头
2. 等号两侧不能有空格
3. 变量名称一般习惯为大写

将命令的返回值赋给变量

1. A=`date`，反引号，运行里面的命令，并把结果返回给变量A
2. A=$(date)，等价于反引号

**设置环境变量**

1. export 变量名=变量值：将shell变量输出为环境变量/全局变量
2. source 配置文件：让修改后的配置信息立即生效
3. echo $变量名：查询环境变量的值

例：在/etc/profile文件中定义TOMCAT_HOME环境变量

1. vim /etc/profile
2. 输入export TOMCAT_HOME=/opt/tomcat
3. source /etc/profile
4. echo $TOMCAT_HOME

shell脚本单行注释：#

shell脚本多行注释：:<<! 内容 !

**位置参数变量**

当我们执行一个shell脚本时，如果希望获取到命令行的参数信息，就可以使用到位置参数变量。比如：./myshell.sh 100 200 ，这个就是一个执行shell的命令行，可以在myshell脚本中获取到参数信息

基本语法：

+ $n：n为数字，$0代表命令本身，$1-$9代表第一到第九个参数，十以上的参数，十以上的需要用大括号包含，如${10}
+ $*：这个变量代表命令行中所有的参数，$ *把所有的参数看成一个整体
+ $@：这个变量也代表命令行中所有的参数，不过$@把每个参数区分对待
+ $#：这个变量代表命令行中所有参数的个数

**预定义变量**

就是shell设计者事先已经定义好的变量，可以直接在shell脚本中使用

基本语法：

+ $$：当前进程的进程号PID
+ $!：后台运行的最后一个进程的进程号PID
+ $?：最后一次执行的命令的返回状态。如果这个变量的值为0，证明上一个命令正确执行；如果这个变量的值为非0，则证明上一个命令执行不正确

#### 运算符

+ $((运算式)) 或 $[运算式] 或者 expr m + n
+ 注意expr运算符之间要有空格，如果希望将expr的结果赋给某个变量，使用``
+ expr m - n
+ expr \ *，/，%：乘，除，取余

#### 条件判断

- [ condition ]：注意condition前后要有空格

应用实例

+ [ hspEdu ]：返回true
+ [  ]：返回false
+ [ condition ] && echo OK || echo notok：条件满足，执行后面的语句

判断语句：

+ =：字符串比较
+ 两个整数的比较
  + -lt：小于
  + -le：小于等于
  + -eq：等于
  + -gt：大于
  + -ge：大于等于
  + -ne：不等于
+ 按照文件权限进行判断
  + -r：有读的权限
  + -w：有写的权限
  + -x：有执行的权限
+ 按照文件类型进行判断
  + -f：文件存在并且是一个常规的文件
  + -e：文件存在
  + -d：文件存在并是一个目录

案例：

1. ok是否等于ok

   if [ "ok" = "ok" ]

   then echo "equal"

   fi

2. /root/shcode/aaa.txt目录中的文件是否存在

   if [ -f /root/shcode/aaa.txt ]

#### 流程控制

- if判断

  if [ 条件判断式 ]

  then

  代码

  fi

  或

  if [ 条件判断式 ]

  then

  代码

  elif [ 条件判断式 ]

  then

  代码

  fi

  注意事项：[ 条件判断式 ]，中括号和条件判断式之间必须有空格

- case语句

  case $变量名 in

  "值1")

  如果变量的值等于值1，则执行程序1

  ;;

  "值2")

  如果变量的值等于值2，则执行程序2

  ;;

  ...省略其他分支...

  *)

  如果变量的值都不是以上的值，则执行此程序

  ;;

  esac

- for循环

  for 变量 in 值1 值2 值3...

  do

  程序

  done

  或

  for (( 初始值;循环控制条件;变量变化 ))

  do

  程序

  done

- while循环

  while [ 条件判断式 ]

  do

  程序

  done

#### read读取控制台输入

+ read(选项)(参数)

选项：

+ -p：指定读取值时的提示符
+ -t：指定读取值时等待的时间（秒），如果没有在指定的时间内输入，就不再等待了

参数：

+ 变量：指定读取值的变量名

例：

1. 读取控制台输入一个NUM1值

   read -p "请输入一个数NUM1=" NUM1

   echo "你输入的NUM1=$NUM1" 

2. 读取控制台输入一个NUM2值，在10秒内输入

   read -t 10 -p "请输入一个数NUM2=" NUM2

   echo "你输入的NUM2=$NUM2" 

#### 函数

shell编程和其他编程语言一样，有系统函数，也可以自定义函数。系统函数中，这里介绍两个

**系统函数**

+ basename [pathname] [suffix]：返回完整路径最后/的部分，常用于获取文件名，suffix为后缀，如果suffix被指定了，basename会将pathname中的suffix去掉
+ dirname 文件绝对路径：返回完整路径最后/的前面部分，常用于返回路径部分

**自定义函数**

[ function ] functionname[()]

{

​	Action;

​	[return int;]

}

调用直接写函数名：functionname [值]

例：计算输入两个参数的和（动态的获取）

function getSum() {

​	SUM=$[$n1+$n2]

​	echo "和是=$SUM"

}

调用：getSum $n1 $n2

#### shell编程综合案例

需求分析

1. 每天凌晨2:30备份数据库hspedu到/data/backup/db
2. 备份开始和备份结束能够给出相应的提示信息
3. 备份后的文件要求以备份时间为文件名，并打包成tar.gz的形式
4. 在备份的同时，检查是否有10天前备份的数据库文件，如果有就将其删除

mysql_db_backup.sh：

```shell
#!/bin/bash
#备份目录
BACKUP=/data/backup/db
#当前时间
DATETIME=$(date +%Y-%m-%d_%H%M%S)
#数据库的地址
HOST=localhost
#数据库用户名
DB_USER=root
#数据库密码
DB_PW=hspedu100
#备份的数据库名
DATABASE=hspedu

#创建备份目录，如果不存在，就创建
[ ! -d "${BACKUP}/${DATETIME}" ] && mkdir -p "${BACKUP}/${DATETIME}"

#备份数据库
mysqldump -u${DB_USER} -p${DB_PW} --host=${HOST} -q -R --databases ${DATABASE} | gzip > ${BACKUP}/${DATETIME}/$DATETIME.sql.gz

#将文件处理成tar.gz
cd ${BACKUP}
tar -zcvf $DATETIME.tar.gz ${DATETIME}

#删除对应的备份目录
rm -rf ${BACKUP}/${DATETIME}

#删除10天前的备份文件
find ${BACKUP} -atime +10 -name "*.tar.gz" -exec rm -rf {} \;
echo "备份数据库${DATABASE}成功"
```

crontab -e

30 2 * * * /usr/sbin/mysql_db_backup.sh

### Ubuntu软件操作相关命令

+ **sudo apt-get update 更新源**
+ **sudo apt-get install package 安装包**
+ **sudo apt-get remove package 删除包**
+ sudo apt-cache search package 搜索软件包
+ **sudo apt-cache show package 获取包的相关信息，如说明、大小、版本等**
+ sudo apt-get install package --reinstall 重新安装包
+ sudo apt-get -f install 修复安装
+ sudo apt-get remove package --purge 删除包，包括配置文件等
+ sudo apt-get build-dep package 安装相关的编译环境
+ sudo apt-get upgrade 更新已安装的包
+ sudo apt-get dist-upgrage 升级系统
+ sudo apt-cache depends package 了解使用该包依赖哪些包
+ sudo apt-cache rdepends package 查看该包被哪些包依赖
+ **sudo apt-get source package 下载该包的源代码**

### 日志管理

/var/log/目录就是系统日志文件的保存位置

系统常用的日志：

| 日志文件              | 说明                                                         |
| --------------------- | ------------------------------------------------------------ |
| **/var/log/boot.log** | 系统启动日志                                                 |
| **/var/log/cron**     | 记录与系统定时任务相关的日志                                 |
| /var/log/cups/        | 记录打印信息的日志                                           |
| /var/log/dmesg        | 记录了系统在开机时内核自检的信总。也可以使用dmesg命令直接查看内核自检信息 |
| /var/log/btmp         | 记录错误登录的日志。这个文件是二进制文件，不能直接用vi看，而要使用lastb命令查看 |
| **/var/log/lasllog**  | 记录系统中所有用户最后一次登录时间的日志。这个文件也是二进制文件，要用lastlog命令查看 |
| **/var/log/mailog**   | 记录邮件信息的日志                                           |
| **/var/log/message**  | 记录系统重要消息的日志。这个日志文件中会记录Linux系统的绝大多数重要信息。如果系统出现问题，首先要检查的就是这个日志文件 |
| **/var/log/secure**   | 记录验证和授权方面的信息，只要涉及账户和密码的程序都会记录，比如系统的登录、ssh的登录、su切换用户、sudo授权，甚至添加用户和修改用户密码都会记录在这个日志文件中 |
| /var/log/wtmp         | 永久记录所有用户的登录，注销信息，同时记录系统的启动、重启、关机事件。是二进制文件，需要使用last命令查看 |
| **/var/log/ulmp**     | 记录当前已经登录的用户的信息。这个文件会随着用户的登录和注销而不断变化，只记录当前登录用户的信息。这个文件不能用vi查看，而要使用w、who、users等命令查看 |

#### 日志管理服务rsyslogd

**查询Linux中的rsyslogd服务是否启动**

ps aux | grep "rsyslogd" |grep -v "grep"

**查询rsyslogd服务的自启动状态**

systemctl list-unit-files | grep rsyslog

**配置文件：/etc/rsyslog.cong**

编辑文件时的格式为：* .*，存放日志文件

其中第一个*代表日志类型，第二个 *代表日志级别

日志类型分为：

- auth：pam产生的日志
- authpriv：ssh、ftp等登录信息的验证信息
- corn：时间任务相关
- kern：内核
- lpr：打印
- mail：邮件
- mark(syslog)-rsyslog：服务内部的信息，时间标识
- news：新闻组
- user：用户程序产生的相关信息
- uucp：unix to nuix copy主机之间相关的通信
- local 1-7：自定义的日志设备

日志级别分为：

+ debug：有调试信息的，日志通信最多
+ info：一般信息日志，最常用
+ notice：最具有重要性的普通条件的信息
+ warning：警告级别
+ err：错误级别，阻止某个功能或者模块不能正常工作的信息
+ crit：严重级别，阻止整个系统或者整个软件不能正常工作的信息
+ alert：需要立刻修改的信息
+ emerg：内核崩溃等重要信息
+ none：什么都不记录

从上到下，级别从低到高，记录信息越来越少

由日志服务rsyslogd记录的日志文件，日志文件的格式包含以下4列：

1. 事件产生的时间
2. 产生事件的服务器的主机名
3. 产生事件的服务器或程序名
4. 事件的具体信息

**自定义日志服务**

在/etc/rsyslog.conf中添加一个日志文件/var/log/hsp.log，当有事件发送时（比如sshd服务相关事件），该文件会接收到信息并保存。

*. *    /var/log/hsp.log

#### 日志轮替

日志轮替就是把旧的日志文件移动并改名，同时建立新的空日志文件，当旧日志文件超出保存的范围之后，就会进行删除

**日志轮替文件命名**

1. centos7使用logrotate进行日志轮替管理，要想改变日志轮替文件名字，通过/etc/logrotate.conf配置文件中的dateext参数
2. 如果配置文件中有dateext参数，那么日志会用日期来作为日志文件的后缀，例如secure-20201010，这样日志文件名不会重叠，也就不需要日志文件的改名，只需要指定保存日志个数，删除多余的日志文件即可
3. 如果配置文件中没有dateext参数，日志文件就需要进行改名了，当第一次进行日志轮替时，当前的secure日志会自动改名为secure.1，然后新建secure日志，用来保存新的日志。当第二次进行日志轮替时，secure.1会自动改名为secure.2，当前的secure日志会自动改名为secure.1，然后也会新建secure日志，用来保存新的日志，以此类推

**logrotate配置文件**

/etc/logrotate.cong为logrotate的全局配置文件

```shell
#rotate log files weekly,每周对日志进行一次轮替
weekly
#keep 4 weeks worth of backlogs,共保存4份日志文件，当建立新的日志文件时，旧的将会被删除
rotate 4
#create new(empty) log files after rotating old one,创建新的空日志文件，在日志轮替后
create
#use date as a suffix of the ratate file,使用日期作为日志轮替文件的后缀
dateext
#uncomment this if you want your log files compressed,日志文件是否压缩，如果取消注释，则日志会在转储的同时进行压缩
#compress
#RPM packages drop log rotation information into this directory
include /etc/logrotate.d
#包含/etc/logrotate.d/目录中所有的子配置文件。也就是说会把这个目录中所有的子配置文件读取出来
#下面是单独设置，优先级更高
#no packages own wtmp and btmp -- we'll rotate them here
/var/log/wtmp {
	monthly #每月对日志文件进行一次轮替
	create 0664 root utmp #建立新的日志文件，权限是0664，所有者是root，所属组是utmp
	minsize 1M #日志文件最小轮替大小是1MB，也就是日志一定要超过1MB才会轮替，否则就算时间达到一个月，也不进行日志转储
	rotate 1 #仅保留一个日志文件备份。也就是只有wtmp和wtmp.1日志保留而已
}
/var/log/btmp {
	missingok #如果日志不存在，则忽略该日志的警告信息
	monthly
	create 0600 root utmp
	rotate 1
}
```

参数说明：

| 参数                    | 参数说明                                                     |
| ----------------------- | ------------------------------------------------------------ |
| daily                   | 日志的轮替周期是每天                                         |
| weekly                  | 日志的轮替周期是每周                                         |
| monthly                 | 日志的轮替周期是每月                                         |
| rotate 数字             | 保留的日志文件的个数，0指没有备份                            |
| compress                | 日志轮替时，旧的日志进行压缩                                 |
| create mode owner group | 建立新日志时，同时指定新日志的权限与所有者和所属组           |
| mail address            | 当日志轮替时，输出内容通过邮件发送到指定的邮件地址           |
| missingok               | 如果日志不存在，则忽略该日志的警告信息                       |
| notifempty              | 如果日志为空文件，则不进行日志轮替                           |
| minsize 大小            | 日志轮替的最小值，也就是日志一定要达到这个最小值才会轮替，否则就算时间达到也不轮替 |
| size 大小               | 日志只有大于指定大小才进行日志轮替，而不是按照时间轮替       |
| dateext                 | 使用日期作为日志轮替文件的后缀                               |
| sharedscripts           | 在此关键字之后的脚本只执行一次                               |
| prerotate/endscript     | 在日志轮替之前执行脚本命令                                   |
| postrotate/endscript    | 在日志轮替之后执行脚本命令                                   |

**把自己的日志加入日志轮替**

第一种方法是直接在/etc/logrotate.conf配置文件中写入该日志的轮替策略

第二种方法是在/etc/logrotate.d/目录中新建立该日志的轮替文件，在该轮替文件中写入正确的轮替策略，因为该目录中的文件都会被include到主配置文件中，所以也可以把日志加入轮替

推荐使用第二种方法，因为系统中需要轮替的日志非常多，如果全都直接写入/etc/logrotate.conf配置文件，那么这个文件的可管理性就会非常差，不利于此文件的维护

**日志轮替机制原理**

日志轮替之所以可以在指定的时间备份日志，是依赖系统定时任务，在/etc/cron.daily目录，就会发现这个目录中是有logrotate文件（可执行），logrotate通过这个文件依赖定时任务执行的

#### 查看内存日志

+ journalctl：查看全部
+ journalctl -n 3：查看最新3条
+ journalctl --since 19:00 --until 19:10:10：查看起始时间到结束时间的日志可加日期
+ journalctl -p err：报错日志
+ journalctl -o verbose：日志详细内容
+ journalctl _PID=1245 _COMM=sshd：查看包含这些参数的日志（在详细日志查看）或者journalctl | grep sshd

注意：journalctl查看的是内存日志，重启清空

### 定制自己的Linux系统

**启动流程**

1. 首先Linux要通过自检，检查硬件设备有没有故障
2. 如果有多块启动盘的话，需要在BIOS中选择启动磁盘
3. 启动MBR中的bootloader引导程序
4. 加载内核文件
5. 执行所有进程的父进程、老祖宗systemd
6. 欢迎界面

在Linux的启动流程中，加载内核文件时关键文件

1. kernel文件：vmlinuz-3.10.0-957.el7.x86_64
2. initrd文件：initramfs-3.10.0.957.el7.x86_64.img

**制作min Linux思路分析**

1. 在现有的Linux系统（centos7.6）上加一块硬盘/dev/sdb，在硬盘上分两个分区，一个是/boot，一个是/，并将其格式化。需要明确的是，现在加的这个硬盘在现有的Linux系统中是/dev/sdb，但是，当我们把东西全部设置好时，要把这个硬盘拔除，放在新系统上，此时就是/dev/sda
2. 在/dev/sdb硬盘上，将其打造成独立的Linux系统，里面的所有文件是需要拷贝进去的
3. 作为能独立运行的Linux系统，内核一定不能少，要把内核文件和initramfs文件也一起拷贝到/dev/sdb上
4. 以上步骤完成，我们的自制Linux就完成，创建一个新的Linux虚拟机，将其硬盘指向我们创建的硬盘，启动即可

**操作步骤**

1. 首先，我们在现有的Linux添加一块大小为20G的硬盘

2. 添加完成后，然后启动现有的Linux（centos7.6），通过fdisk来给我们的/dev/sdb分区

3. 接下来对/dev/sdb的分区进行格式化

   makfs.ext4 /dev/sdb1

   makfs.ext4 /dev/sdb2

4. 创建目录，并挂载新的磁盘

   mkdir -p /mnt/boot /mnt/sysroot

   mount /dev/sdb1 /mnt/boot

   mount /dev/sdb2 /mnt/sysroot/

5. 安装grub，内核文件拷贝到目标磁盘

   grub2-install --root-directory=/mnt /dev/sdb

   可以看一下二进制确认是否安装成功

   hexdump -C -n 512 /dev/sdb

   cp -rf /boot/* /mnt/boot

6. 修改grub2/grub.cfg文件

7. 创建目标主机根文件系统

   mkdir -pv /mnt/sysroot/{etc/rc.d,usr,var,proc,sys,dev,lib,lib64,bin,sbin,boot,srv,mnt,media,home,root}

8. 拷贝需要的bash（也可以拷贝你需要的指令）和库文件给新的系统使用

   cp /lib64/*. * /mnt/sysroot/lib64/

   cp /bin/bash /mnt/sysroot/bin

9. 现在我们就可以创建一个新的虚拟机，然后将默认分配的硬盘移除掉，执行我们刚刚创建的磁盘即可

10. 这时，很多指令都不能使用，比如ls，reboot，可以将需要的指令拷贝到对应的目录即可

11. 如果要拷贝指令，重新进入到原来的Linux系统拷贝相应的指令即可，比如将/bin/ls拷贝到 /mnt/sysroot/bin，将/sbin/reboot拷贝到/mnt/sysroot/sbin

    mount /dev/sdb2 /mnt/sysroot/

    cp /bin/ls /mnt/sysroot/bin/

    cp /bin/systemctl /mnt/sysroot/bin/

    cp /bin/reboot /mnt/sysroot/bin/

12. 再重新启动新的min Linux系统，就可以使用ls，reboot指令

### Linux备份与恢复

Linux的备份和恢复有两种方式：

1. 把需要的文件或分区用tar打包就行，下次需要恢复的时候，再解压覆盖即可
2. 使用dump和restore命令

如果Linux上没有dump和restore指令，需要先安装

+ yum -y install dump

+ yum -y install restore

**使用dump完成备份**

dump支持分卷和增量备份（所谓增量备份是指备份上次备份后修改/增加过的文件，也称差异备份）

+ dump [ -cu] [-123456789] [ -f <备份后文件名>] [-T <日期>] [目录或文件系统]
+ dump []-wW
+ cat /etc/dumpdates：查看备份时间文件

参数说明：

+ -c：创建新的归档文件，并将由一个或多个文件参数所指定的内容写入归档文件的开头
+ -0123456789：备份的层级。0为最完整备份，会备份所有文件。若指定0以上的层级，则备份至上一次备份以来修改或新增的文件，到9后，可以再次轮替
+ -f <备份后文件名>：指定备份后文件名
+ -j：调用bzlib库压缩备份文件，也就是将备份后的文件压缩成bz2格式，让文件更小
+ -T <日期>：指定开始备份的时间与日期
+ -u：备份完毕后，在/etc/dumpdates中记录备份的文件系统，层级，日期与时间等
+ -t：指定文件名，若该文件已存在备份文件中，则列出名称
+ -W：显示需要备份的文件及其最后一次备份的层级，时间，日期
+ -w：与-W类似，仅显示需要备份的文件

例：将/boot分区所有内容备份到/opt/boot.bak.bz2文件中，备份层级为0

dump -0uj -f /opt/boot.bak.bz2 /boot

dump备份分区时，可以支持增量备份，备份文件或者目录，不再支持增量备份，即只能使用0级别备份

**使用restore完成恢复**

restore命令用来恢复已备份的文件，可以从dump生成的备份文件中恢复原文件

+ restore [模式选项] [选项]

模式选项，不能混用，只能指定一种：

+ -C：使用对比模式，将备份的文件与已存在的文件相互对比
+ -i：使用交互模式，在进行还原操作时，restors指令将依序询问用户
+ -r：进行还原模式
+ -t：查看模式，看备份文件有哪些文件

选项：

+ -f <备份设备>：从指定文件中读取备份数据，进行还原操作

### Linux可视化管理：bt运维工具

bt宝塔Linux面板是提升运维效率的服务器管理软件，支持一键LAMP/LNMP/集群/监控/网站/FTP/数据库/Java等多项服务器管理功能

安装和使用：

1. yum install -y wget && wget -O install.sh http://download.bt.cn/install/install_6.0.sh && sh install.sh
2. 安装成功后控制台会显示登录地址，账户密码，复制浏览器打开登录

如果bt的用户名，密码忘记了，使用bt default可以查看