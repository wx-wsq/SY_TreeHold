package com.sq.SYTreeHole.service.publishService.comments;

import com.sq.SYTreeHole.entity.Comment;

import java.io.Serializable;
import java.util.List;

public interface CommentsService {

    List<Comment> Comments(Serializable publishId, Serializable page);

    void InsertComment(Comment comments);

    void deleteComment(Serializable commentId,Serializable id);

    void IncrStar(Serializable commentId);

}
