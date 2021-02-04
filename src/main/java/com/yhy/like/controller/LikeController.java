package com.yhy.like.controller;

import com.yhy.like.bean.UserLike;
import com.yhy.like.dto.Result;
import com.yhy.like.service.LikeRedisService;
import com.yhy.like.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LikeController {

    @Autowired
    private LikeRedisService likeRedisService;
    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/like")
    public Result like(UserLike userLike) {
        Result result = new Result();
        if (userLike.getType() != null && userLike.getTypeId() != null && userLike.getStatus() != null && userLike.getUserId() != null) {
            likeRedisService.saveOrUpdate(userLike);
            result.setCode(200);
            result.setMsg("OK");
        } else {
            result.setCode(400);
            result.setMsg("fail");
        }
        return result;
    }

    @GetMapping("/test")
    public Result t(String userId) {

        System.out.println(redisUtil.sAdd("userId", userId));

        Result result = new Result();
        result.setCode(200);
        result.setMsg("OK");
        return result;
    }
}
