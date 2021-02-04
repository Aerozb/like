package com.yhy.like.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  LikeStatusEnum {
    
    NO_LIKE(0,"取赞"),
    LIKE(1,"点赞");
    
    /**
     * 点赞状态码
     */
    private int code;
    /**
     * 点赞状态
     */
    private String name;
    
    public static String getName(Integer code) {
        for (LikeStatusEnum lse : LikeStatusEnum.values()) {
            if (code.equals(lse.getCode())) {
                return lse.getName();
            }
        }
        return null;
    }
}
