### 基本概念

9300端口为elasticsearch集群间组件的通信端口，9200端口为浏览器访问的http协议RESTFUL端口

elasticsearch是面向文档型数据库，一条数据在这里就是一个文档

![](/img/elasticsearch_1.png)

**索引（Index）**

一个索引就是一个拥有几分相似特征的文档的集合。一个索引由一个名字来标识（必须全是小写字母）。

elasticsearch索引的精髓：一切的设计都是为了提高搜索的性能

**倒排索引**

以字或词为关键字进行索引，表中关键字对应的记录表项记录了出现这个字或词的所有文档

**类型（Type）**

一个类型就是你的索引的一个逻辑上的分类/分区。

type概念已经被逐渐弱化，elasticsearch6.X中，一个index下已经只能包含一个type，elasticsearch7.X中，type概念已经被删除了

**文档（Document）**

一个文档是一个可被索引的基础信息单元，也就是一条数据。文档以JSON格式来表示

**字段（Field）**

相当于数据表的字段，对文档数据根据不同属性进行的分类标识

**映射（Mapping）**

相当于表结构信息。mapping是处理数据的方式和规则方面做一些限制，如：某个字段的数据类型、默认值、分析器、是否被索引等等。其他就是处理ES里面数据的一些使用规则设置也叫做映射。

**分片（Shards）**

Elasticsearch提供了将索引划分为多份的能力，每一份就称之为分片。当你创建索引的时候，可以指定你想要的分片的数量。

分片很重要，主要有两方面原因：

1. 允许你水平分割/扩展你的内容容量
2. 允许你在分片上进行分布式的、并行的操作，进而提高性能/吞吐量

**副本（Replicas）**

Elasticsearch允许你创建分片的一份或多份拷贝，这些拷贝叫做复制分片（副本）。

复制分片之所以重要，有两个主要原因：

1. 在分片/节点失败的情况下，提高了可用性。因为这个原因，注意到复制分片从不与原/主分片置于同一节点上是非常重要的
2. 扩展你的搜索量/吞吐量，因为搜素可以在所有的副本上并行运行

**分配（Allocation）**

将分片分配给某个节点的过程，包括分配主分片或者副本。如果是副本，还包含从主分片复制数据的过程。这个过程由master节点完成

**系统架构**

![](/img/elasticsearch_2.png)



### HTTP

#### 索引

**创建**

PUT：http://127.0.0.1:9200/shopping，创建一个shopping的索引，具有幂等性

**查询**

GET：http://127.0.0.1:9200/shopping，获取shopping索引信息

GET：http://127.0.0.1:9200/_cat/indices?v，查看es中所有的索引

**删除**

DELETE：http://127.0.0.1:9200/shopping，删除shopping索引

#### 文档

文档类比为关系型数据库中的表数据，添加的数据格式为JSON格式

**创建**

POST：http://127.0.0.1:9200/shopping/_doc

POST：http://127.0.0.1:9200/shopping/_doc/1001，自定义id为1001

PUT：http://127.0.0.1:9200/shopping/_doc/1001，自定义id为1001

PUT：http://127.0.0.1:9200/shopping/_create/1001，自定义id为1001

**查询**

GET：http://127.0.0.1:9200/shopping/_doc/1001

GET：http://127.0.0.1:9200/shopping/_search，查询全部

GET：http://127.0.0.1:9200/shopping/_search?q=category:小米，条件查询，查询category等于小米的

GET：http://127.0.0.1:9200/shopping/_search，条件查询，条件写在请求体中

```json
# 请求体，查询category等于小米的
{
    "query" : {
        "match" : {
            "category":"小米"
        }
    }
}

# 请求体，全量查询
{
    "query" : {
        "match_all" : {
            
        }
    }
}

# 请求体，分页查询，从0开始，查询2条
{
    "query" : {
        "match_all" : {
            
        }
    },
    "from" : 0,
    "size" : 2,
    "_source" : ["title"], # 只查询title的信息
    # 按价格降序排序
    "sort" : {
    	"price" : {
    		"order" : "desc"
		}
	}
}

# 多个条件查询
{
    "query" : {
        "bool" : {
            # must相当于and，should相当于or
            "must" : [
                {
            		# match全文检索匹配，match_phrase完全匹配
                    "match" : {
                        "category" : "小米"
                    }
                },
                {
                    "match" : {
                        "price" : 1999.00
                    }
                }
            ],
			# 范围查询，查询price大于5000的数据
			"filter" : {
                "range" : {
                    "price" : {
                        "gt" : 5000
                    }
                }
            }
        }
    },
	# 查询结果中的category字段高亮显示
	"highlight" : {
        "fields" : {
            "category" : {}
        }
    }
}

#聚合操作
{
    "aggs" : { # 聚合操作
        "price_group" : { # 统计结果名称，随意起名
            "terms" : { # terms分组，avg平均值
                "field" : "price" # 分组字段
            }
        }
    },
	"size" : 0 # 不用查原始数据
}
```

**修改**

PUT：http://127.0.0.1:9200/shopping/_doc/1001，完全覆盖

POST：http://127.0.0.1:9200/shopping/_update/1001，局部更新

**删除**

DELETE：http://127.0.0.1:9200/shopping/_doc/1001

#### 映射关系

相当于表结构信息

**创建**

PUT：http://127.0.0.1:9200/user/_mapping

```json
{
    "properties" : {
        "name" : {
            "type" : "text", # 文本，可以分词
            "index" : true # 可以被索引查询
        },
        "sex" : {
            "type" : "keyword", # 不能分词，完整匹配
            "index" : true
        },
        "tel" : {
            "type" : "keyword", # 不能分词，完整匹配
            "index" : false # 不能被索引查询
        },
    }
}
```

**查询**

GET：http://127.0.0.1:9200/user/_mapping

### JavaAPI

#### 索引

**创建**

```java
// 创建ES客户端
RestHighLevelClient esClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

// 创建索引
CreateIndexRequest request = new CreateIndexRequest("user");
CreateIndexResponse createIndexResponse = esClient.indices().create(request, RequestOptions.DEFAULT);

// 响应状态
boolean acknowledged = createIndexResponse.isAcknowledged();
System.out.println("索引操作：" + acknowledged);

// 关闭es客户端
esClient.close();
```

**查询**

```java
// 创建ES客户端
RestHighLevelClient esClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

// 查询索引
GetIndexRequest request = new GetIndexRequest("user");
GetIndexResponse getIndexResponse = esClient.indices().get(request, RequestOptions.DEFAULT);

// 响应状态
System.out.println(getIndexResponse.getAliases());
System.out.println(getIndexResponse.getMappings());
System.out.println(getIndexResponse.getSettings());

// 关闭es客户端
esClient.close();
```

**删除**

```java
// 创建ES客户端
RestHighLevelClient esClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

// 删除索引
DeleteIndexRequest request = new DeleteIndexRequest("user");
AcknowledgedResponse response = esClient.indices().delete(request, RequestOptions.DEFAULT);

// 响应状态
System.out.println(response.isAcknowledged());

// 关闭es客户端
esClient.close();
```

#### 文档

**插入**

```java
// 创建ES客户端
RestHighLevelClient esClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

// 插入数据
IndexRequest request = new IndexRequest();
request.index("user").id("1001");

User user = new User();
user.setName("zhangsan");
user.setAge(30);
user.setSex("男");

// 向es插入数据，必须将数据转换为JSON格式
ObjectMapper mapper = new ObjectMapper();
String userJson = mapper.writeValueAsString(user);
request.source(userJson, XContentType.JSON);

IndexResponse response = esClient.index(request, RequestOptions.DEFAULT);

System.out.println(response.getResult());

// 关闭es客户端
esClient.close();
```

**修改**

```java
// 创建ES客户端
RestHighLevelClient esClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

// 修改数据
UpdateRequest request = new UpdateRequest();
request.index("user").id("1001");
request.doc(XContentType.JSON, "sex", "女");

UpdateResponse response = esClient.update(request, RequestOptions.DEFAULT);

System.out.println(response.getResult());

// 关闭es客户端
esClient.close();
```

**查询**

```java
// 创建ES客户端
RestHighLevelClient esClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

// 查询数据
GetRequest request = new GetRequest();
request.index("user").id("1001");
GetResponse response = esClient.get(request, RequestOptions.DEFAULT);

System.out.println(response.getSourceAsString());

// 关闭es客户端
esClient.close();
```

**删除**

```java
// 创建ES客户端
RestHighLevelClient esClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

// 删除数据
DeleteRequest request = new DeleteRequest();
request.index("user").id("1001");

DeleteResponse response = esClient.delete(request, RequestOptions.DEFAULT);
System.out.println(response.toString());

// 关闭es客户端
esClient.close();
```

**批量插入**

```java
// 创建ES客户端
RestHighLevelClient esClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

// 批量插入数据
BulkRequest request = new BulkRequest();
request.add(new IndexRequest.index("user").id("1001").source(XContentType.JSON, "name", "zhangsan"));
request.add(new IndexRequest.index("user").id("1001").source(XContentType.JSON, "name", "lisi"));
request.add(new IndexRequest.index("user").id("1001").source(XContentType.JSON, "name", "wangwu"));

BulkResponse response = esClient.bulk(request, RequestOptions.DEFAULT);
System.out.println(response.getTook());
System.out.println(response.getItems());

// 关闭es客户端
esClient.close();
```

**批量删除**

```java
// 创建ES客户端
RestHighLevelClient esClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

// 批量删除数据
BulkRequest request = new BulkRequest();
request.add(new DeleteRequest.index("user").id("1001"));
request.add(new DeleteRequest.index("user").id("1002"));
request.add(new DeleteRequest.index("user").id("1003"));

BulkResponse response = esClient.bulk(request, RequestOptions.DEFAULT);
System.out.println(response.getTook());
System.out.println(response.getItems());

// 关闭es客户端
esClient.close();
```

**全量查询、条件查询**

```java
// 创建ES客户端
RestHighLevelClient esClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

// 查询数据
SearchRequest request = new SearchRequest();
request.indices("user");

// 全量查询
request.source(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()));
// 条件查询
// request.source(new SearchSourceBuilder().query(QueryBuilders.termQuery("age", 30)));

SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);

SearchHits hits = response.getHits();

System.out.println(hits.getTotalHits());
System.out.println(response.getTook());

for (SearchHit hit : hits) {
    System.out.println(hit.getSourceAsString());
}

// 关闭es客户端
esClient.close();
```

**分页查询**

```java
// 创建ES客户端
RestHighLevelClient esClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

SearchRequest request = new SearchRequest();
request.indices("user");

SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
// 分页查询
builder.from(0);
builder.size(2);
// 排序
builder.sort("age", SortOrder.DESC);
// 排除和包含
String[] excludes = {};
String[] includes = {"name"};
builder.fetchSource(includes, excludes);

request.source(builder);
SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);

SearchHits hits = response.getHits();

System.out.println(hits.getTotalHits());
System.out.println(response.getTook());

for (SearchHit hit : hits) {
    System.out.println(hit.getSourceAsString());
}

// 关闭es客户端
esClient.close();
```

**组合查询**

```java
// 创建ES客户端
RestHighLevelClient esClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

SearchRequest request = new SearchRequest();
request.indices("user");

SearchSourceBuilder builder = new SearchSourceBuilder();
BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

boolQueryBuilder.must(QueryBuilders.matchQuery("age", 30));
boolQueryBuilder.mustNot(QueryBuilders.matchQuery("sex", "男"));
boolQueryBuilder.should(QueryBuilders.matchQuery("age", "40"));

builder.query(boolQueryBuilder);

request.source(builder);
SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);

SearchHits hits = response.getHits();

System.out.println(hits.getTotalHits());
System.out.println(response.getTook());

for (SearchHit hit : hits) {
    System.out.println(hit.getSourceAsString());
}

// 关闭es客户端
esClient.close();
```

**范围查询**

```java
// 创建ES客户端
RestHighLevelClient esClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

SearchRequest request = new SearchRequest();
request.indices("user");

SearchSourceBuilder builder = new SearchSourceBuilder();
RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("age");

rangQuery.gte(30);
rangQuery.lte(40);

builder.query(rangeQuery);

request.source(builder);
SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);

SearchHits hits = response.getHits();

System.out.println(hits.getTotalHits());
System.out.println(response.getTook());

for (SearchHit hit : hits) {
    System.out.println(hit.getSourceAsString());
}

// 关闭es客户端
esClient.close();
```

**模糊查询**

```java
// 创建ES客户端
RestHighLevelClient esClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

SearchRequest request = new SearchRequest();
request.indices("user");

SearchSourceBuilder builder = new SearchSourceBuilder();
//差一个字符能查出来，比如wangwu1
builder.query(QueryBuilders.fuzzyQuery("name", "wangwu").fuzziness(Fuzziness.ONE));

request.source(builder);
SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);

SearchHits hits = response.getHits();

System.out.println(hits.getTotalHits());
System.out.println(response.getTook());

for (SearchHit hit : hits) {
    System.out.println(hit.getSourceAsString());
}

// 关闭es客户端
esClient.close();
```

**高亮查询**

```java
// 创建ES客户端
RestHighLevelClient esClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

SearchRequest request = new SearchRequest();
request.indices("user");

SearchSourceBuilder builder = new SearchSourceBuilder();
TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("name", "wangwu");

HighlighterBuilder highlighterBuilder = new HighlighterBuilder();
highlighterBuilder.preTags("<font color='red'>");
highlighterBuilder.postTags("</font>");
highlighterBuilder.field("name");

builder.highlighter();
builder.query(termsQueryBuilder);

request.source(builder);
SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);

SearchHits hits = response.getHits();

System.out.println(hits.getTotalHits());
System.out.println(response.getTook());

for (SearchHit hit : hits) {
    System.out.println(hit.getSourceAsString());
}

// 关闭es客户端
esClient.close();
```

**聚合查询**

```java
// 创建ES客户端
RestHighLevelClient esClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

SearchRequest request = new SearchRequest();
request.indices("user");

SearchSourceBuilder builder = new SearchSourceBuilder();
AggregationBuilder aggregationBuilder = AggregationBuilders.max("maxAge").field("age");
// 分组查询
// AggregationBuilder aggregationBuilder = AggregationBuilders.terms("ageGroup").field("age");
builder.aggregation(aggregationBuilder);

request.source(builder);
SearchResponse response = esClient.search(request, RequestOptions.DEFAULT);

SearchHits hits = response.getHits();

System.out.println(hits.getTotalHits());
System.out.println(response.getTook());

for (SearchHit hit : hits) {
    System.out.println(hit.getSourceAsString());
}

// 关闭es客户端
esClient.close();
```

### 集群部署

#### Windows集群

1. 修改node-1001config下elasticsearch.yml文件

   ```yaml
   # 集群名称，节点之间要保持一致
   cluster.name: my-application
   
   # 节点名称，集群内要唯一
   node.name: node-1001
   node.master: true
   node.data: true
   
   # ip地址
   network.host: localhost
   
   # http端口
   http.port: 1001
   
   #tcp监听端口
   transport.tcp.port: 9301
   
   # 跨域配置
   http.cors.enabled: true
   http.cors.allow-origin: "*"
   
   ```

   GET：http://localhost:1001/_cluster/health，查看集群健康状态

2. 修改node-1002config下elasticsearch.yml文件

   ```yaml
   cluster.name: my-application
   
   node.name: node-1002
   node.master: true
   node.data: true
   
   network.host: localhost
   http.port: 1002
   transport.tcp.port: 9302
   discovery.seed_hosts: ["localhost:9301"]
   discovery.zen.fd.ping_timeout: 1m
   discovery.zen.fd.ping_retries: 5
   
   http.cors.enabled: true
   http.cors.allow-origin: "*"
   ```

3. 修改node-1003config下elasticsearch.yml文件

   ```yaml
   cluster.name: my-application
   
   node.name: node-1003
   node.master: true
   node.data: true
   
   network.host: localhost
   http.port: 1003
   transport.tcp.port: 9303
   discovery.seed_hosts: ["localhost:9301", "localhost:9302"]
   discovery.zen.fd.ping_timeout: 1m
   discovery.zen.fd.ping_retries: 5
   
   http.cors.enabled: true
   http.cors.allow-origin: "*"
   ```

#### Linux单机

1. 解压软件

   ```shell
   #解压缩
   tar -zxvf elasticsearch-7.8.0-linux-x86_64.tar.gz -C /opt/module
   
   #改名
   mv elasticsearch-7.8.0 es
   ```

2. 创建用户

   因为安全问题，elasticsearch不允许root用户直接运行，所以要创建新用户，在root用户中创建新用户

   ```shell
   #新增es用户
   useradd es
   
   #为es用户设置密码
   passwd es
   
   #如果错了，可以删除再加
   userdel -r es
   
   #文件夹所有者
   chown -R es:es /opt/module/es
   ```

3. 修改配置文件

   修改/opt/module/es/config/elasticsearch.yml

   ```yaml
   cluster.name: elasticsearch
   node.name: node-1
   network.host: 0.0.0.0
   http.port: 9200
   cluster.initial_master_nodes: ["node-1"]
   ```

   修改/etc/security/limits.conf

   ```yaml
   #在文件末尾中增加下面内容
   #每个进程可以打开的文件数限制
   es soft nofile 65536
   es hard nofile 65536
   ```

   修改/etc/security/limits.d/20-nproc.conf

   ```yaml
   #在文件末尾中增加下面内容
   #每个进程可以打开的文件数限制
   es soft nofile 65536
   es hard nofile 65536
   ```

   修改/etc/sysctl.conf

   ```yaml
   #一个进程中可以拥有的VMA(虚拟内存区域)的数量，默认值为65536
   vm.max_map_cont=655360
   ```

   重新加载：sysctl -p

4. 启动软件

   使用es用户启动

   ```shell
   cd /opt/module/es
   
   #启动
   bin/elasticsearch
   
   #后台启动
   bin/elasticsearch -d
   ```

#### Linux集群

1. 修改/opt/module/es/config/elasticsearch.yml

   ```yaml
   #集群名称
   cluster.name: elasticsearch
   
   #节点名称，每个节点的名称不能重复
   node.name: node-1
   
   #ip地址，每个节点的地址不能重复
   network.host: 0.0.0.0
   
   #是不是有资格主节点
   node.master: true
   node.data: true
   http.port: 9200
   
   #head插件需要打开这两个位置
   http.cors.allow-origin: "*"
   http.cors.enabled: true
   http.max_content_length: 200mb
   
   #es7.x之后新增加的配置，初始化一个新的集群时需要此配置来选举master
   cluster.initial_master_nodes: ["node-1"]
   
   #es7.x之后新增加的配置，节点发现
   discovery.seed_hosts: ["linux1:9300", "linux2:9300", "linux3:9300"]
   gateway.recover_after_nodes: 2
   network.tcp.keep_alive: true
   network.tcp.no_delay: true
   transport.tcp.compress: true
   
   #集群内同时启动的数据任务个数，默认是2个
   cluster.routing.allocation.cluster_concurrent_rebalance: 16
   
   #添加或删除节点及负载均衡时并发恢复的线程个数，默认4个
   cluster.routing.allocation.node_concurrent_recoveries: 16
   
   #初始化数据恢复时，并发恢复线程的个数，默认4个
   cluster.routing.allocation.node_initial_primaries_recoveries: 16
   ```

   GET：http://linux1:9200/_cat/nodes，查看集群节点
