package com.yhy.like.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserLike implements Serializable {
    public Long likeId;
    public Integer type;
    public Long typeId;
    public Integer status;
    public String userId;
    public Date likeTime;
}
