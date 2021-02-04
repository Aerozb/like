# 高并发点赞问题

 1. 高并发请求下，服务器频繁创建线程。
 2. 高并发请求下，数据库连接池中的连接数有限。
 3. 高并发请求下，点赞功能是同步处理等。

# 解决办法：
## 第一步
我们通过引入Redis缓存避免高并发写数据库而造成数据库压力，同时引入Redis缓存提高读的性能，基本可以解决问题。
## 第二步
为了解决高并发请求下，点赞功能同步处理所带来的服务器压力(Redis缓存的压力或数据库压力等)，我们引入MQ消息中间件进行异步处理，用户每次点赞都会推送消息到MQ服务器并及时返回，这样用户的点赞请求就及时结束，避免了点赞请求线程占用时间长的问题。与此同时，MQ消息中间件接收到消息后，会按照“自己的方式”及时消费，还可以用MQ消息中间件来限制流量并进行异步处理等。

整个流程，如下图所示
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210204123945595.png)
## 详细设计
## 1.Redis数据结构设计

 1. 使用set来存储被点赞的类型id，key为被点赞类型名，value为类型id
 2. hash存储某个类型点赞的记录，key为类型名+类型id，hashKey为点赞人，hashValue为点赞时间
![Redis数据结构设计](https://img-blog.csdnimg.cn/20210204180756581.png)
## 2.表设计

```sql
DROP TABLE IF EXISTS `user_like`;
CREATE TABLE `user_like`  (
  `like_id` int NOT NULL AUTO_INCREMENT,
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '0：帖子，1评论，2：回复',
  `type_id` int NULL DEFAULT NULL COMMENT '点赞类型id',
  `status` tinyint(1) NULL DEFAULT NULL,
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `like_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`like_id`) USING BTREE,
  INDEX `del_index`(`type`, `type_id`, `user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10101 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
```
![在这里插入图片描述](https://img-blog.csdnimg.cn/20210204181413463.jpg#pic_center)

## 第三步代码设计
1.点赞类型由于分成三种，所以使用策略+工厂模式替代if else去执行点赞功能，根据前台传过来的type进行判断，优雅美观。
 2.使用mq来进行异步处理，每次请求进来，把信息传给mq后，直接返回前端传给点赞成功信息，后面mq收到消息后在进行处理。
 3.最后半夜把存储在redis的数据写入到数据库中，方式为：添加redis中有的数据库中无记录的，删除redis没有数据库中有的记录，因为点赞取消是直接在redis中删除。
## 第四步写代码



　　　　　　　　　　　　
