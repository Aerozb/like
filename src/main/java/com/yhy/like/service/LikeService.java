package com.yhy.like.service;

public interface LikeService {

    /**
     * 从redis中取出数据，跟数据库数据比较，新增添加，没有删除
     */
    public void saveOrDeleteAll();

}
