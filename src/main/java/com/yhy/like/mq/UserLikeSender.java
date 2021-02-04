package com.yhy.like.mq;

import com.yhy.like.bean.UserLike;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;

/**
 * 点赞记录发送者
 */
@Component
@Slf4j
public class UserLikeSender {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Queue queue;

    public void sendMessage(UserLike userLike) {
        jmsMessagingTemplate.convertAndSend(queue, userLike);
        log.info("点赞记录发送：用户 " + userLike.getUserId() + "点赞了type=" + userLike.getType() + "，typeId=" + userLike.getTypeId());
    }
}
