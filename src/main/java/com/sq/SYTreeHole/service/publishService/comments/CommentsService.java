package com.sq.SYTreeHole.service.publishService.comments;

import com.sq.SYTreeHole.entity.Comment;

import java.io.Serializable;
import java.util.List;

public interface CommentsService {

    /**
     * 分页查询评论
     * @param publishId 发表ID
     * @param page 页数默认一页10条
     * @return Comment实体列表
     */
    List<Comment> Comments(String publishId, String page);


    /**
     * 新增评论
     * @param comments 评论实体
     * @return 是否添加成功
     */
    boolean InsertComment(Comment comments);


    /**
     * 删除评论
     * @param commentId 评论ID
     * @param id 用户ID 用以鉴别是否有权删除
     */
    void deleteComment(String commentId,String id);


    /**
     * 点赞
     * @param commentId 评论ID
     * @param IOrD 点赞+1，取消-1
     */
    void star(Serializable commentId,Integer IOrD);

}
