package com.yhy.like.service.impl;

import com.yhy.like.bean.UserLike;
import com.yhy.like.dto.LikeSetName;
import com.yhy.like.enums.LikeSetNameEnum;
import com.yhy.like.mapper.LikeMapper;
import com.yhy.like.service.LikeRedisService;
import com.yhy.like.service.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.yhy.like.enums.LikeSetNameEnum.*;

@Service
@Slf4j
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private LikeRedisService likeRedisService;

    @Override
    public void saveOrDeleteAll() {
        Set<Object> postLikeSet = likeRedisService.getAllTypeIdByTypeInSet(POST_LIKE_SET.getType());
        Set<Object> commentLikeSet = likeRedisService.getAllTypeIdByTypeInSet(COMMENT_LIKE_SET.getType());
        Set<Object> replyLikeSet = likeRedisService.getAllTypeIdByTypeInSet(REPLY_LIKE_SET.getType());

        UserLike userLike = new UserLike();

        //保存需要插入的记录
        List<UserLike> insertList = new ArrayList<>();
        //保存需要删除的记录
        List<UserLike> deleteList = new ArrayList<>();
        
        //遍历帖子点赞集合
        addList(postLikeSet, userLike, insertList, deleteList, POST_LIKE_SET);
        //遍历评论点赞集合
        addList(commentLikeSet, userLike, insertList, deleteList, COMMENT_LIKE_SET);
        //遍历回复点赞集合
        addList(replyLikeSet, userLike, insertList, deleteList, REPLY_LIKE_SET);
        
        //先删除后保存
        if (deleteList.size() > 0) {
            likeMapper.conditionalDelete(deleteList);
        }
        if (insertList.size() > 0) {
            likeMapper.save(insertList);
        }
    }

    private void addList(Set<Object> typeLikeSet, UserLike userLike, List<UserLike> insertList, List<UserLike> deleteList, LikeSetNameEnum likeSetNameEnum) {
        Set<String> userIdInRedisSet;
        try {
            for (Object o : typeLikeSet) {
                //1.1 拼接被赞类型在redis的key名
                String typeKey = likeSetNameEnum.getTypeName()+"::"+o;
                //redis中查找点赞此类型的所有用户
                userIdInRedisSet = likeRedisService.getAllUserIdInHash(typeKey);
                //设置查询对象到数据库查
                int type = likeSetNameEnum.getType();
                userLike.setType(type);
                userLike.setTypeId(Long.valueOf(String.valueOf(o)));
                //1.2 DB中查询点赞此类型的所有用户
                List<String> userIdInDBList = likeMapper.findAllExistUserIdByTypeAndTypeId(userLike);

                //2.1 查询redis中有，DB没有的数据，进行DB插入，因为redis数据是最新的
                userIdInRedisSet.removeAll(userIdInDBList);
                //获取要插入的数据,按hashKey获取顺序排列，所以是对应的
                List<Object> likeTimeInsertList = likeRedisService.getMultiValueInHash(typeKey, userIdInRedisSet);
                //遍历设置userLike并添加到插入集合
                Iterator<String> insertIt = userIdInRedisSet.iterator();
                int i = 0;
                while (insertIt.hasNext()) {
                    String userId = insertIt.next();
                    UserLike ul = new UserLike();
                    ul.setType(type);
                    ul.setTypeId(Long.valueOf(String.valueOf(o)));
                    ul.setStatus(1);
                    ul.setUserId(userId);
                    ul.setLikeTime((Date) likeTimeInsertList.get(i));
                    insertList.add(ul);
                    i++;
                }

                //2.2 查询DB中有，redis没有的数据，进行DB删除，因为DB中数据是旧的
                userIdInDBList.removeAll(userIdInRedisSet);
                //遍历设置userLike并添加到插入集合
                Iterator<String> delIt = userIdInDBList.iterator();
                i = 0;
                while (delIt.hasNext()) {
                    String userId = delIt.next();
                    UserLike ul = new UserLike();
                    ul.setType(type);
                    ul.setTypeId(Long.valueOf(String.valueOf(o)));
                    ul.setUserId(userId);
                    deleteList.add(ul);
                    i++;
                }
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
