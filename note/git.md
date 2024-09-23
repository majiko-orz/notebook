![](/img/git_1.png)

## 文件的四种状态

- **Untracked**:未跟踪，此文件在文件夹中，但并没有加入到git库，不参与版本控制，通过**git add** 状态变为**Staged**.
- **Unmodify**:文件已经入库，未修改，即版本库中的文件快照内容与文件夹中完全一致，这种类型的文件有两种去处，如果它被修改，而变为**modified**.如果使用**git rm**移出版本库，则成为**Untracked** 文件.
- **Modified**:文件已修改，仅仅是修改，并没有进行其他的操作。这个文件也有两个去处，通过**git add** 可进入暂存 **staged** 状态，使用**git checkout** 则丢弃修改过，返回到**unmodify**状态，这个**git checkout**即从库中取出文件，覆盖当前修改.
- **Staged:**暂存状态，执行**git commit** 则将修改同步到库中，这时库中的文件和本地的文件又变为一致，文件为**unmodify**状态，执行**git reset HEAD filename** 取消暂存，文件状态变为 **Modified**.

## 忽略文件

有时候我们不想把某些文件纳入版本控制中，比如数据库文件，临时文件，设计文件等

在主目录下建立"**.gitignore**"文件，此文件有如下规则：

1. 忽略文件中的空行或以**#**开始的行
2. 可以使用Linux通配符。例如：*****代表任意多个字符，**?**代表一个字符，**[abc]**代表可选字符范围，**{string1,string2..}**代表可选字符串等。
3. 如果名称的最前面有一个**!**,表示例外规则，将不被忽略。
4. 如果名称的最前面是一个**/**,表示要忽略的文件在此目录下，而子目录的文件不忽略。
5. 如果名称的最后面是一个**/**,表示要忽略的文件是此目录下该名称的子目录，而非文件件（默认文件或目录都忽略）。

```
/temp      #仅忽略项目根目录下的TODO文件，而不包括其他目录temp
build/     #忽略build/目录下的所有文件
```

## 常用命令

```
#设置用户签名
git config --global user.name 用户名

#设置用户签名
git config --global user.email 邮箱

#初始化本地库
git init

#查看本地库状态
git status

#添加到暂存区
git add 文件名

#提交到本地库
git commit -m "日志信息" 文件名

#查看历史记录
git reflog

#查看版本详细信息
git log

#版本穿梭
git reset --hard 版本号
```



## 新建代码库

```
# 在当前目录新建一个Git代码库
$ git init
 
# 新建一个目录，将其初始化为Git代码库
$ git init [project-name]
 
# 下载一个项目和它的整个代码历史
$ git clone [url]

```

## 配置

```
# 显示当前的Git配置
$ git config --list

# 显示系统变量
$ git config --system --list 

# 显示用户定义变量
$ git config --global --list  

# 编辑Git配置文件
$ git config -e [--global]
 
# 设置提交代码时的用户信息
$ git config [--global] user.name "[name]"
$ git config [--global] user.email "[email address]"

# 查看配置文件位置及内容
$ git config --list --show-origin
```

## 增加/删除文件

```
# 添加指定文件到暂存区
$ git add [file1] [file2] ...
 
# 添加指定目录到暂存区，包括子目录
$ git add [dir]
 
# 添加当前目录的所有文件到暂存区
$ git add .
 
# 添加每个变化前，都会要求确认
# 对于同一个文件的多处变化，可以实现分次提交
$ git add -p
 
# 删除工作区文件，并且将这次删除放入暂存区
$ git rm [file1] [file2] ...
 
# 停止追踪指定文件，但该文件会保留在工作区
$ git rm --cached [file]
 
# 改名文件，并且将这个改名放入暂存区
$ git mv [file-original] [file-renamed]
```

## 代码提交

```
# 提交暂存区到仓库区
$ git commit -m [message]
 
# 提交暂存区的指定文件到仓库区
$ git commit [file1] [file2] ... -m [message]
 
# 提交工作区自上次commit之后的变化，直接到仓库区
$ git commit -a
 
# 提交时显示所有diff信息
$ git commit -v
 
# 使用一次新的commit，替代上一次提交
# 如果代码没有任何新变化，则用来改写上一次commit的提交信息
$ git commit --amend -m [message]
 
# 重做上一次commit，并包括指定文件的新变化
$ git commit --amend [file1] [file2] ...
```

## 分支

```
# 列出所有本地分支
$ git branch
 
# 列出所有远程分支
$ git branch -r
 
# 查看分支
$ git branch -v

# 列出所有本地分支和远程分支
$ git branch -a
 
# 新建一个分支，但依然停留在当前分支
$ git branch [branch-name]
 
# 以远程分支为基础新建一个分支，并切换到该分支
$ git checkout -b [branch] origin/[remote-branch]
 
# 新建一个分支，指向指定commit
$ git branch [branch] [commit]
 
# 新建一个分支，与指定的远程分支建立追踪关系
$ git branch --track [branch] [remote-branch]
 
# 切换到指定分支，并更新工作区
$ git checkout [branch-name]
 
# 切换到上一个分支
$ git checkout -
 
# 建立追踪关系，在现有分支与指定的远程分支之间
$ git branch --set-upstream [branch] [remote-branch]
 
# 合并指定分支到当前分支
$ git merge [branch]
 
# 选择一个commit，合并进当前分支
$ git cherry-pick [commit]
 
# 删除分支
$ git branch -d [branch-name]
 
# 删除远程分支
$ git push origin --delete [branch-name]
$ git branch -dr [remote/branch]
```

## 标签

```
# 列出所有tag
$ git tag
 
# 新建一个tag在当前commit
$ git tag [tag]
 
# 新建一个tag在指定commit
$ git tag [tag] [commit]
 
# 删除本地tag
$ git tag -d [tag]
 
# 删除远程tag
$ git push origin :refs/tags/[tagName]
 
# 查看tag信息
$ git show [tag]
 
# 提交指定tag
$ git push [remote] [tag]
 
# 提交所有tag
$ git push [remote] --tags
 
# 新建一个分支，指向某个tag
$ git checkout -b [branch] [tag]
```

## 查看信息

```
# 显示有变更的文件
$ git status
 
# 显示当前分支的版本历史
$ git log
 
# 显示commit历史，以及每次commit发生变更的文件
$ git log --stat
 
# 搜索提交历史，根据关键词
$ git log -S [keyword]
 
# 显示某个commit之后的所有变动，每个commit占据一行
$ git log [tag] HEAD --pretty=format:%s
 
# 显示某个commit之后的所有变动，其"提交说明"必须符合搜索条件
$ git log [tag] HEAD --grep feature
 
# 显示某个文件的版本历史，包括文件改名
$ git log --follow [file]
$ git whatchanged [file]
 
# 显示指定文件相关的每一次diff
$ git log -p [file]
 
# 显示过去5次提交
$ git log -5 --pretty --oneline
 
# 显示所有提交过的用户，按提交次数排序
$ git shortlog -sn
 
# 显示指定文件是什么人在什么时间修改过
$ git blame [file]
 
# 显示暂存区和工作区的差异
$ git diff
 
# 显示暂存区和上一个commit的差异
$ git diff --cached [file]
 
# 显示工作区与当前分支最新commit之间的差异
$ git diff HEAD
 
# 显示两次提交之间的差异
$ git diff [first-branch]...[second-branch]
 
# 显示今天你写了多少行代码
$ git diff --shortstat "@{0 day ago}"
 
# 显示某次提交的元数据和内容变化
$ git show [commit]
 
# 显示某次提交发生变化的文件
$ git show --name-only [commit]
 
# 显示某次提交时，某个文件的内容
$ git show [commit]:[filename]
 
# 显示当前分支的最近几次提交
$ git reflog
```

## 远程同步

```
# 下载远程仓库的所有变动
$ git fetch [remote]
 
# 显示所有远程仓库
$ git remote -v
 
# 显示某个远程仓库的信息
$ git remote show [remote]
 
# 增加一个新的远程仓库，并命名
$ git remote add [shortname] [url]
 
# 取回远程仓库的变化，并与本地分支合并
$ git pull [remote] [branch]
 
# 上传本地指定分支到远程仓库
$ git push [remote] [branch]
 
# 强行推送当前分支到远程仓库，即使有冲突
$ git push [remote] --force
 
# 推送所有分支到远程仓库
$ git push [remote] --all
```

## 撤销

```
# 恢复暂存区的指定文件到工作区
$ git checkout [file]
 
# 恢复某个commit的指定文件到暂存区和工作区
$ git checkout [commit] [file]
 
# 恢复暂存区的所有文件到工作区
$ git checkout .
 
# 重置暂存区的指定文件，与上一次commit保持一致，但工作区不变
$ git reset [file]
 
# 重置暂存区与工作区，与上一次commit保持一致
$ git reset --hard
 
# 重置当前分支的指针为指定commit，同时重置暂存区，但工作区不变
$ git reset [commit]
 
# 重置当前分支的HEAD为指定commit，同时重置暂存区和工作区，与指定commit一致
$ git reset --hard [commit]
 
# 重置当前HEAD为指定commit，但保持暂存区和工作区不变
$ git reset --keep [commit]
 
# 新建一个commit，用来撤销指定commit
# 后者的所有变化都将被前者抵消，并且应用到当前分支
$ git revert [commit]
 
# 暂时将未提交的变化移除，稍后再移入
$ git stash
$ git stash pop
```



