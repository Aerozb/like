package com.yhy.like.strategy.impl;

import com.yhy.like.bean.UserLike;
import com.yhy.like.strategy.LikeStrategy;
import org.springframework.stereotype.Component;

import static com.yhy.like.enums.LikeSetNameEnum.POST_LIKE_SET;

/**
 * 点赞帖子行为处理类
 */
@Component
public class LikePost extends LikeStrategy {

    @Override
    public void like(UserLike userLike) {
        likeByType(userLike, POST_LIKE_SET);
    }

    @Override
    public String getTypeName() {
        return POST_LIKE_SET.getTypeName();
    }
}
