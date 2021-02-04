package com.yhy.like.mq;

import com.yhy.like.bean.UserLike;
import com.yhy.like.enums.LikeSetNameEnum;
import com.yhy.like.enums.LikeStatusEnum;
import com.yhy.like.service.LikeRedisService;
import com.yhy.like.strategy.LikeFactory;
import com.yhy.like.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.Date;

import static com.yhy.like.enums.LikeSetNameEnum.*;

/**
 * 点赞记录消费者
 */
@Component
@Slf4j
public class UserLikeReceiver {
    @Autowired
    LikeFactory likeFactory;

    @JmsListener(destination = "like-redis", containerFactory = "activeMQFactory")
    public void handle(Message message) {
        ActiveMQObjectMessage activeMqObjectMessage = (ActiveMQObjectMessage) message;
        try {
            UserLike userLike = (UserLike) activeMqObjectMessage.getObject();
            Integer type = userLike.getType();
            if (type != null) {
                try {
                    likeFactory.getLikeStrategy(type).like(userLike);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
            log.info("点赞记录消费：" + userLike.getUserId() + "点赞了type=" + type + ",typeId=" + userLike.getTypeId());
        } catch (JMSException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
