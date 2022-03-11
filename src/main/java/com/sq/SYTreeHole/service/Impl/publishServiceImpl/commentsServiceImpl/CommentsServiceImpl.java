package com.sq.SYTreeHole.service.Impl.publishServiceImpl.commentsServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sq.SYTreeHole.dao.publishDao.commentsDao.CommentsMapper;
import com.sq.SYTreeHole.entity.Comment;
import com.sq.SYTreeHole.exception.CommentsException;
import com.sq.SYTreeHole.exception.PowerException;
import com.sq.SYTreeHole.service.publishService.comments.CommentsService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comment> implements CommentsService {
    private static final int number = 10;

    @Override
    public List<Comment> Comments(Serializable publishId, Serializable page) {
        if (Strings.isBlank((String) publishId) || Strings.isBlank((String) page))
            throw new CommentsException("空参异常");
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("publish_id", publishId).orderByDesc("id");
        IPage<Comment> iPage = new Page<>(Long.parseLong((String) page), number);
        return getBaseMapper().selectPage(iPage, queryWrapper).getRecords();
    }

    @Override
    public void InsertComment(Comment comments) {
        if (Objects.isNull(comments)
                || Strings.isBlank(comments.getText())
                || Strings.isBlank(comments.getUserId())
                || Strings.isBlank(comments.getPublishId()))
            throw new CommentsException("空参异常");
        save(comments);
    }

    @Override
    public void deleteComment(Serializable commentId, Serializable id) {
        if (Strings.isBlank((String) commentId) || Strings.isBlank((String) id))
            throw new CommentsException("空参异常");
        if (getById(id).getUserId().equals(id)) {
            removeById(commentId);
        } else
            throw new PowerException("无权进行此操作");
    }

    @Override
    public void IncrStar(Serializable commentId) {
        Comment comment = getById(commentId);
        comment.setStar(comment.getStar() + 1);
        updateById(comment);
    }
}
