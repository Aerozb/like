package com.yhy.like.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LikeSetNameEnum {
    /**
     * 帖子类型
     */
    POST_LIKE_SET(0,"post_like"),
    /**
     * 评论类型
     */
    COMMENT_LIKE_SET(1, "comment_like"),
    /**
     * 回复类型
     */
    REPLY_LIKE_SET(2, "reply_like");

    /**
     * 点赞类型
     */
    private int type;
    /**
     * 点赞类型名
     */
    private String typeName;

    /**
     * 查询与type对应的名
     * @param type 点赞类型
     * @return enum
     */
    public static String getTypeName(Integer type) {
        for (LikeSetNameEnum lsne : LikeSetNameEnum.values()) {
            if (type.equals(lsne.getType())) {
                return lsne.getTypeName();
            }
        }
        return null;
    }

}
