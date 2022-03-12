package com.sq.SYTreeHole.controller;

import com.sq.SYTreeHole.common.Constants;
import com.sq.SYTreeHole.common.Result;
import com.sq.SYTreeHole.entity.Comment;
import com.sq.SYTreeHole.exception.CommentsException;
import com.sq.SYTreeHole.service.publishService.comments.CommentsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommentsController {

    private final CommentsService commentsService;

    public CommentsController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @GetMapping("/comments")
    public Result<?> comments(String publishId,String page){
        List<Comment> commentList = commentsService.Comments(publishId, page);
        return new Result<>(Constants.CODE_200,"成功",commentList);
    }

    @GetMapping("/commentIncrStar")
    public void commentIncrStar(String commentId){
        commentsService.star(commentId,1);
    }

    @GetMapping("/commentDecrStar")
    public void commentDecrStar(String commentId){
        commentsService.star(commentId,-1);
    }

    @PostMapping("/commentDel")
    public Result<?> commentDel(String commentId,String userId){
        commentsService.deleteComment(commentId,userId);
        return new Result<>(Constants.CODE_200,"删除成功",null);
    }

    @PostMapping("/commentIns")
    public Result<?> commentInsert(Comment comment){
        boolean insertComment = commentsService.InsertComment(comment);
        if(insertComment)
            return new Result<>(Constants.CODE_200,"添加成功",comment);
        else
            throw new CommentsException("添加评论失败...");
    }
}
