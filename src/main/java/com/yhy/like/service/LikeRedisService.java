package com.yhy.like.service;

import com.yhy.like.bean.UserLike;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface LikeRedisService {

    public void saveOrUpdate(UserLike userLike);

    /**
     *   获取所有被点赞类型set的id
     *   set的所有value：post_like 等-->value
     */
    public Set<Object> getAllTypeIdByTypeInSet(Integer type);

    /**
     * 获取所有被点赞类型id的点赞用户id
     * comment::1里的hashKey
     */
    public Set<String> getAllUserIdInHash(String key);

    /**
     * 根据多个hashKey获取对应值
     */
    public List<Object> getMultiValueInHash(String key, Collection<String> hashKeys);
}
