package com.yhy.like.strategy;

import com.yhy.like.bean.UserLike;
import com.yhy.like.enums.LikeSetNameEnum;
import com.yhy.like.enums.LikeStatusEnum;
import com.yhy.like.utils.RedisUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 策略父类
 */
@Component
public class LikeStrategy {

    @Autowired
    protected RedisUtil redisUtil;

    /**
     * 点赞，根据不同点赞类型供实现类使用
     */
    protected void likeByType(UserLike userLike, LikeSetNameEnum likeSetNameEnum) {
        Integer status = userLike.getStatus();
        Long typeId = userLike.getTypeId();
        String userId = userLike.getUserId();
        Date likeTime = userLike.getLikeTime();
        //获取类型名
        String typeNameInSet = likeSetNameEnum.getTypeName();
        //根据不同点赞类型拼接key名
        String typeLikeKey = typeNameInSet + "::" + typeId;
        //如果为点赞状态
        if (status == LikeStatusEnum.LIKE.getCode()) {
            //如果点赞类型set集合不存在此记录，则将被点赞的类型添加到点赞集合里
            if (!redisUtil.sIsMember(typeNameInSet, typeId)) {
                redisUtil.sAdd(typeNameInSet, typeId);
            }
            //添加一条hash key=（类型+类型ID），value为hashKey=点赞用户ID和hashValue=点赞时间
            redisUtil.hSet(typeLikeKey, userId, likeTime);
        } else {
            //为0，从具体点赞记录hash中取消赞
            redisUtil.hDel(typeLikeKey, userId);
            //点赞为0，从点赞set集合中移除
            if (redisUtil.sCard(typeLikeKey) == 0) {
                redisUtil.sRem(typeNameInSet, typeId);
            }
        }
    }

    public void like(UserLike userLike) {
    }

    /**
     * 根据子类获取类型名
     */
    public String getTypeName() {
        return null;
    }

}



