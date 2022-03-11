package com.sq.SYTreeHole.service.Impl.PublishServiceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sq.SYTreeHole.Utils.RedisUtils;
import com.sq.SYTreeHole.dao.publishDao.PublishDetailMapper;
import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.exception.PublishDetailException;
import com.sq.SYTreeHole.service.publishService.PublishDetailService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class PublishDetailServiceImpl extends ServiceImpl<PublishDetailMapper, Publish> implements PublishDetailService {
    @Override
    public Publish detail(Serializable publishId) {
        if (Strings.isBlank((String) publishId))
            throw new PublishDetailException("空参异常");
        Publish publishCache = RedisUtils.getPublishCache(publishId);
        if (Strings.isBlank(publishCache.getUserId())) {
            Publish publish = getById(publishId);
            publish.setVisits(publish.getVisits() + 1);
            RedisUtils.setPublishCache(publish);
            return publish;
        } else {
            RedisUtils.incrVisits(publishCache);
            return publishCache;
        }
    }

    @Override
    public void doStar(Serializable publishId) {
        if (Strings.isBlank((String) publishId))
            throw new PublishDetailException("空参异常");
        Publish publishCache = RedisUtils.getPublishCache(publishId);
        if (Strings.isBlank(publishCache.getUserId())) {
            Publish publish = getById(publishId);
            publish.setStar(publish.getStar()+1);
            RedisUtils.setPublishCache(publish);
        } else
            RedisUtils.incrStar(publishCache);
    }
}
