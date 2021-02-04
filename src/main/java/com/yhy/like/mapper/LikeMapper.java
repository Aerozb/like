package com.yhy.like.mapper;

import com.yhy.like.bean.UserLike;

import java.util.List;

public interface LikeMapper {

    /**
     * 查询某个被点赞集合的所有点赞用户
     */
    public List<String> findAllExistUserIdByTypeAndTypeId(UserLike userLike);

    /**
     * 保存点赞
     */
    public void save(List<UserLike> list);

    /**
     * 更新点赞状态
     */
    public void update(UserLike userLike);

    /**
     * 条件删除：根据type，typeId
     */
    public void conditionalDelete( List<UserLike> list);


}
