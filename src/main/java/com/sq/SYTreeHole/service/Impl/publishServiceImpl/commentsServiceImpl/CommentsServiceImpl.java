package com.sq.SYTreeHole.service.Impl.publishServiceImpl.commentsServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sq.SYTreeHole.Utils.RedisUtils;
import com.sq.SYTreeHole.dao.publishDao.PublishDetailMapper;
import com.sq.SYTreeHole.dao.publishDao.commentsDao.CommentsMapper;
import com.sq.SYTreeHole.entity.Comment;
import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.exception.CommentsException;
import com.sq.SYTreeHole.exception.PowerException;
import com.sq.SYTreeHole.service.publishService.comments.CommentsService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@CacheConfig(cacheNames = "comments")
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comment> implements CommentsService {
    private static final int number = 10;

    @Resource
    private PublishDetailMapper publishDetailMapper;

    @Cacheable(key = "#publishId+':'+#page")
    @Override
    public List<Comment> Comments(String publishId, String page) {
        if (Strings.isBlank(publishId) || Strings.isBlank(page))
            throw new CommentsException("空参异常");
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("publish_id", publishId)
                .orderByDesc("id");
        IPage<Comment> iPage = new Page<>(Long.parseLong(page), number);
        return getBaseMapper().selectPage(iPage, queryWrapper).getRecords();
    }

    @CacheEvict(cacheNames = "comments", beforeInvocation = true, allEntries = true)
    @Override
    public boolean InsertComment(Comment comment) {
        if (Objects.isNull(comment)
                || Strings.isBlank(comment.getText())
                || Strings.isBlank(comment.getUserId())
                || Strings.isBlank(comment.getPublishId()))
            throw new CommentsException("空参异常");
        else {
            Publish publish = RedisUtils.getPublishCache(comment.getPublishId());
            if (Objects.isNull(publish.getId()))
                publish = publishDetailMapper.getById(comment.getPublishId());
            publish.setCommentsNumber(publish.getCommentsNumber() + 1);
            RedisUtils.setPublishCache(publish);
            return save(comment);
        }
    }

    @CacheEvict(cacheNames = "comments", beforeInvocation = true, allEntries = true)
    @Override
    public void deleteComment(String commentId, String userId) {
        if (getById(commentId).getUserId().equals(userId)) {
            Publish publish = RedisUtils.getPublishCache(userId);
            publish.setCommentsNumber(publish.getCommentsNumber() - 1);
            RedisUtils.setPublishCache(publish);
            removeById(commentId);
        } else
            throw new PowerException("无权进行此操作");
    }

    @CacheEvict(cacheNames = "comments", beforeInvocation = true, allEntries = true)
    @Override
    public void star(Serializable commentId, Integer IOrD) {
        Comment comment = getById(commentId);
        comment.setStar(comment.getStar() + IOrD);
        updateById(comment);
    }
}
