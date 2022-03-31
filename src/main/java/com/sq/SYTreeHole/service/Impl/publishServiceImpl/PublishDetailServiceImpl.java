package com.sq.SYTreeHole.service.Impl.publishServiceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sq.SYTreeHole.Utils.RedisUtils;
import com.sq.SYTreeHole.dao.publishDao.PublishDetailMapper;
import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.exception.PublishDetailException;
import com.sq.SYTreeHole.service.publishService.PublishDetailService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Objects;

@Service
public class PublishDetailServiceImpl extends ServiceImpl<PublishDetailMapper, Publish> implements PublishDetailService {
    @Override
    public Publish detail(Serializable publishId) {
        if (Strings.isBlank((String) publishId))
            throw new PublishDetailException("空参异常");
        Publish publishCache = RedisUtils.getPublishCache(publishId);
        if (Objects.isNull(publishCache.getId())) {
            Publish publish = getById(publishId);
            if(Objects.isNull(publish)) {
                RedisUtils.setPublishCache(new Publish().setId(publishId + ""));
                return null;
            }
            publish.setVisits(publish.getVisits() + 1);
            RedisUtils.setPublishCache(publish);
            return publish;
        } else {
            if(Objects.nonNull(publishCache.getVisits()))
                RedisUtils.incrPublishVisits(publishCache);
            return publishCache;
        }
    }

    @Override
    public void star(Serializable publishId, Integer IOrD) {
        if (Strings.isBlank((String) publishId))
            throw new PublishDetailException("空参异常");
        Publish publishCache = RedisUtils.getPublishCache(publishId);
        if (Strings.isBlank(publishCache.getUserId())) {
            Publish publish = getById(publishId);
            publish.setStar(publish.getStar() + IOrD);
            RedisUtils.setPublishCache(publish);
        } else {
            RedisUtils.publishStar(publishCache, IOrD);
        }
    }
}
