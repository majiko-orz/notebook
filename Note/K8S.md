## 集群架构及组件

Kubernetes是一个开源的，用于管理云平台中多个主机上的容器化的应用

k8s的特点：自我修复、弹性伸缩、自动部署和回滚、服务发现和负载均衡、机密和配置管理、存储编排、批处理

![](/img/k8s_1.png)

**控制面板（Master）**

+ kube-apiserver：接口服务，基于REST风格开放k8s接口的服务
+ kube-controller-manager：控制器管理器，管理各个类型的控制器，针对k8s中的各种资源进行管理
+ cloud-controller-manager：云控制器管理器，第三方云平台提供的控制器API对接管理功能
+ kube-schedule：调度器，负责将Pod基于一定算法，将其调用到更合适的节点（服务器）上
+ etcd：理解为k8s的数据库，键值类型存储的分布式数据库，提供了基于Raft算法实现自主的集群高可用，老版本基于内存，新版本持久化存储

**节点（Node）**

+ kubelet：负责Pod的生命周期、存储
+ kube-proxy：网络代理，负责Service的服务发现、负载均衡（4层负载）
+ container-runtime：容器运行时环境，docker、containerd、CRI-O
+ Pod：运行容器

## 资源和对象

k8s中所有的内容都被抽象为资源，如pod、service、Node等都是资源。对象就是资源的实例，如某个具体的pod、某个具体的node，kubernetes使用这些实体去表示整个集群的状态。

对象的创建删除修改都是通过api-server组件提供的API接口进行操作的

k8s中的资源类别有很多种，kubectl可以通过配置文件来创建这些对象，配置文件格式可以是JSON或YAML，常用YAML

**资源的分类**

+ 元数据型：对于资源的元数据描述，每一个资源都可以使用元空间的数据

  + Horizontal Pod Autoscaler（HPA）：Pod自动扩容，可以根据CPU使用率或自定义指标自动对Pod进行扩容缩容

    控制管理器每隔30s（可以通过-horizontal-pod-autoscaler-sync-period修改）查询metrics的资源使用情况

    支持三种metrics类型

    + 预定义metics（比如Pod的CPU）以利用率的方式计算
    + 自定义的Pod metrics，以原始值（raw value）的方式计算
    + 自定义的object metrics

    支持两种metrics查询方式：Heapster和自定义的REST API

    支持多metrics

  + PodTemplate：关于Pod的定义，但是被包含在其他的kubernetes对象中（例如Deployment、StatefulSet、DaemonSet等控制器）。控制器通过Pod Template信息来创建Pod

  + LimitRange：可以对集群内Request和Limits的配置做一个全局的统一的限制，相当于批量设置了某一个范围内（某个命名空间）的Pod的资源使用限制

+ 集群级：作用于集群之上，集群下的所有资源都可以共享使用

  + Namespace
  + Node：不像其他的资源（如Pod和Namespace），Node本质上不是k8s来创建的，k8s只是管理Node上的资源，虽然可以通过Manifest创建一个Node对象，但k8s也只是去检查是否真的是有这么一个Node，如果检查失败，也不会往上调度Pod
  + ClusterRole
  + ClusterRoleBinding

+ 命名空间级：作用于命名空间之上，通常只能在该命名空间范围内使用

  + 工作负载型

    + Pod：k8s中最小的可部署单元，一个Pod（容器组）包含了一个应用程序容器（某些情况下是多个容器）、存储资源、一个唯一的网络IP地址、以及一些确定容器该如何运行的选项。

      副本（replicas）：一个Pod可以被复制成多份，每一份可以被称之为一个副本，这些副本除了一些描述性的信息（pod的名字，uid等）不一样以外，其他信息都是一样的，例如pod内部的容器、容器数量、容器里面运行的应用等这些信息都是一样的，这些副本提供同样的功能

      控制器：

      + 适用无状态服务

        + ReplicationController（RC）：帮助我们动态更新Pod的副本数，v1.11被RS替代

        + ReplicaSet（RS）：帮助我们动态更新Pod的副本数，可以通过selector来选择对 哪些pod生效

        + Deployment：针对RS的更高层次的封装，提供了更丰富的部署相关的功能

          自动创建ReplicaSet/Pod，滚动升级/回滚，平滑扩容和缩容，暂停与恢复Deployment

      + 适用有状态服务

        + StatefulSet

          特点：稳定的持久化存储、稳定的网络存储、有序部署、有序扩展、有序收缩、有序删除

          StatefulSet中每个Pod的DNS格式为statefulSetName-[0-N-1].serviceName.namespace.svc.cluster.local

          + Headless Service：对于有状态服务的DNS管理
          + volumnClaimTemplate：用于创建持久化的模板

          注意事项：

          + 所有的Pod的Volumn必须使用PersistentVolumn或者是管理员事先创建好
          + 为了保证数据安全，删除StatefulSet时不会创建Volumn
          + StatefulSet需要一个Headless Service来定义DNS domain，需要在StatefulSet之前创建好

      + 守护进程

        + DaemonSet：为每一个匹配的Node都部署一个守护进程，保证在每个Node上都运行一个容器副本，常用来部署一些集群的日志、监控或者其他系统管理应用

      + 任务\定时任务

        + Job：一次性任务，运行完成后Pod销毁，不再重新启动新的容器
        + CronJob：在Job基础上加了定时功能

  + 服务发现

    + Service：实现k8s集群内部网络调用、负载均衡（四层负载）
    + Ingress：实现将k8s内部服务暴露给外网访问的服务（七层负载）

  + 存储

    + Volumn：数据卷，共享Pod中容器使用的数据，用来放持久化的数据，比如数据库数据
    + CSI：Container Storage Interface是由来自k8s、Mesos、Docker等社区成员联合定制的一个行业标准接口规范，旨在将任意存储系统暴露给容器化应用程序

  + 特殊类型配置

    + ConfigMap：
    + Secret：解决了密码、token、密钥等敏感数据的配置问题，而不需要把这些数据暴露到镜像或者Pod Spec中，Secret可以以Volumn或者环境变量的方式使用
    + DownwardAPI：这个模式和其他模式不一样的地方在于他不是为了存放容器的数据也不是用来进行容器和宿主机的数据交换的，而是让Pod里的容器能够直接获取到这个Pod对象本身的一些信息

  + 其他

    + Role
    + RoleBinding

**k8s资源清单**

| 参数名                                      | 类型    | 说明                                                         |
| ------------------------------------------- | ------- | ------------------------------------------------------------ |
| version                                     | String  | K8S API的版本，可以用kubectl api versions命令查询            |
| kind                                        | String  | yam文件定义的资源类型和角色                                  |
| metadata                                    | Object  | 元数据对象，下面是它的属性                                   |
| metadata.name                               | String  | 元数据对象的名字，比如pod的名字                              |
| metadata.namespace                          | String  | 元数据对象的命名空间                                         |
| Spec                                        | Object  | 详细定义对象                                                 |
| spec.containers[]                           | list    | 定义Spec对象的容器列表                                       |
| spec.containers[].name                      | String  | 为列表中的某个容器定义名称                                   |
| spec.containers[].image                     | String  | 为列表中的某个容器定义需要的镜像名称                         |
| spec.containers[].imagePullPolicy           | String  | 定义镜像拉取策略，有Always、Never、IfNotPresent三个值可选<br/>Always（默认）：意思是每次都尝试重新拉取镜像<br/>Never：表示仅适用本地镜像<br/>IfNotPresent：如果本地有镜像就使用本地镜像，没有就拉取在线镜像 |
| spec.containers[].command[]                 | list    | 指定容器启动命令，因为是数组可以指定多个，不指定则使用镜像打包时使用的启动命令 |
| spec.containers[].args[]                    | list    | 指定容器启动命令参数，因为是数组可以指定多个                 |
| spec.containers[].workingDir                | String  | 指定容器的工作目录                                           |
| spec.containers[].volumeMounts[]            | list    | 指定容器内部的存储卷配置                                     |
| spec.containers[].volumeMounts[].name       | String  | 指定可以被容器挂载的存储卷的名称                             |
| spec.containers[].volumeMounts[].mountPath  | String  | 指定可以被容器挂载的存储卷的路径                             |
| spec.containers[].volumeMounts[].readOnly   | String  | 设置存储卷路径的读写模式，true或者false，默认是读写模式      |
| spec.containers[].ports[]                   | list    | 指定容器需要用到的端口列表                                   |
| spec.containers[].ports[].name              | String  | 指定端口的名称                                               |
| spec.containers[].ports[].containerPort     | String  | 指定容器需要监听的端口号                                     |
| spec.containers[].ports[].hostPort          | String  | 指定容器所在主机需要监听的端口号，默认跟上面containerPort相同，注意设置了hostPort同一台主机无法启动该容器的相同副本（因为主机的端口号不能相同，这样会冲突） |
| spec.containers[].ports[].protocol          | String  | 指定端口协议，支持TCP和UDP，默认为TCP                        |
| spec.containers[].env[]                     | list    | 指定容器运行前需设置的环境变量列表                           |
| spec.containers[].env[].name                | String  | 指定环境变量名称                                             |
| spec.containers[].env[].value               | String  | 指定环境变量值                                               |
| spec.containers[].resources                 | Object  | 指定资源限制和资源请求的值（这里开始就是设置容器的资源上限） |
| spec.containers[].resources.limits          | Object  | 指定设置容器运行时资源的运行上限                             |
| spec.containers[].resources.limits.cpu      | String  | 指定CPU的限制，单位为Core数，将用于docker run -cpu-shares参数 |
| spec.containers[].resources.limits.memory   | String  | 指定mem内存的限制，单位为MIB、GIB                            |
| spec.containers[].resources.requests        | Object  | 指定容器启动和调度时的限制设置                               |
| spec.containers[].resources.requests.cpu    | String  | CPU请求，单位为core数，容器启动时初始化可用数量              |
| spec.containers[].resources.requests.memory | String  | 内存请求，单位为MIB、GIB，容器启动时初始化可用数量           |
| spec.restartPolicy                          | String  | 定义pod的重启策略，可选值为Always、OnFailure、Never，默认值为Always<br/>Always：pod一旦终止运行，则无论容器是如何终止的，kubelet服务都将重启它<br/>OnFailure：只有pod以非零退出码终止时，kubelet才会重启该容器。如果容器正常结束（退出码为0），则kubectl将不会重启它<br/>Never：pod终止后，kubelet将退出码报告给master，不会重启该pod |
| spec.nodeSelector                           | Object  | 定义Node的label过滤标签，以key: value格式指定                |
| spec.imagePullSecrets                       | Object  | 定义pull镜像时使用secret名称，以name: secretkey格式指定      |
| spec.hostNetwork                            | Boolean | 定义是否使用主机网络模式，默认为false。设置true表示使用宿主机网络，不使用docker网桥，同时设置了true将无法在同一台宿主机上启动第二个副本 |

