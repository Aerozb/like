package com.yhy.like.strategy;

import com.yhy.like.enums.LikeSetNameEnum;
import com.yhy.like.strategy.impl.LikeComment;
import com.yhy.like.strategy.impl.LikePost;
import com.yhy.like.strategy.impl.LikeReply;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.yhy.like.enums.LikeSetNameEnum.*;

/**
 * 策略工厂
 */
@Component
public class LikeFactory implements InitializingBean {
    @Autowired
    private  ApplicationContext applicationContext;
    private  final Map<String, LikeStrategy> LIKE_STRATEGY_MAP = new HashMap<>();

    /**
     * 获取策略
     * @param type 点赞类型
     */
    public  LikeStrategy getLikeStrategy(Integer type){
        return LIKE_STRATEGY_MAP.get(LikeSetNameEnum.getTypeName(type));
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, LikeStrategy> likeStrategyMap = applicationContext.getBeansOfType(LikeStrategy.class);
        LIKE_STRATEGY_MAP.put(POST_LIKE_SET.getTypeName(), likeStrategyMap.get("likePost"));
        LIKE_STRATEGY_MAP.put(COMMENT_LIKE_SET.getTypeName(), likeStrategyMap.get("likeComment"));
        LIKE_STRATEGY_MAP.put(REPLY_LIKE_SET.getTypeName(), likeStrategyMap.get("likeReply"));
    }
}
