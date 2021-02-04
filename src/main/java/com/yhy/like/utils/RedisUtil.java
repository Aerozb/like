package com.yhy.like.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class RedisUtil {

    //Set操作类
    private SetOperations<String, Object> setOperations;
    //Hash操作类
    private HashOperations<String, String, Object> hashOperations;

    /**
     * 通过构造参数传入RedisTemplate，不能使用自动注入RedisTemplate，然后赋值给上面俩操作类
     */
    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        setOperations = redisTemplate.opsForSet();
        hashOperations = redisTemplate.opsForHash();
    }

    /**
     * set添加
     */
    public Long sAdd(String key, Object value) {
        Long addCount;
        try {
            addCount = setOperations.add(key, value);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return addCount;
    }

    /**
     * 移除指定set中某项值
     */
    public Long sRem(String key, Object value) {
        Long removeCount;
        try {
            removeCount = setOperations.remove(key, value);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return removeCount;
    }

    /**
     * set中是否存在此元素
     */
    public Boolean sIsMember(String key, Object value) {
        Boolean isMember;
        try {
            isMember = setOperations.isMember(key, value);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());

        }
        return isMember;
    }

    /**
     * 获取set 所有元素
     */
    public Set<Object> sMember(String key) {
        Set<Object> members;
        try {
            members = setOperations.members(key);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());

        }
        return members;
    }

    /**
     * set大小
     */
    public Long sCard(String key) {
        Long size;
        try {
            size = setOperations.size(key);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());

        }
        return size;
    }

    /**
     * hash 添加
     */
    public boolean hSet(String key, String hashKey, Object value) {
        try {
            hashOperations.put(key, hashKey, value);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    /**
     * hash 删除key的hashKey和hashValue
     */
    public Long hDel(String key, String hashKey) {
        Long deleteCount;
        try {
            deleteCount = hashOperations.delete(key, hashKey);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());

        }
        return deleteCount;
    }

    /**
     * hash key的hashKey是否存在
     */
    public Boolean hExists(String key, String hashKey) {
        Boolean exists;
        try {
            exists = hashOperations.hasKey(key, hashKey);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());

        }
        return exists;
    }

    /**
     * hash 通过key获取所有hashKey
     */
    public Set<String> hKeys(String key) {
        Set<String> userIds;
        try {
            userIds = hashOperations.keys(key);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());

        }
        return userIds;
    }

    /**
     * hash 根据多个hashKey获取相应hashValue
     */
    public List<Object> hMget(String key, Collection<String> hashKeys) {
        List<Object> likeTime;
        try {
            likeTime = hashOperations.multiGet(key, hashKeys);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());

        }
        return likeTime;
    }

}