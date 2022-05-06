package com.sq.SYTreeHole.service.Impl.publishServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sq.SYTreeHole.Utils.RedisUtils;
import com.sq.SYTreeHole.dao.publishDao.PublishDetailMapper;
import com.sq.SYTreeHole.dao.publishDao.PublishImagesMapper;
import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.entity.PublishImages;
import com.sq.SYTreeHole.exception.PublishDetailException;
import com.sq.SYTreeHole.service.publishService.PublishDetailService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class PublishDetailServiceImpl extends ServiceImpl<PublishDetailMapper, Publish> implements PublishDetailService {

    @Resource
    private PublishImagesMapper publishImagesMapper;


    @Override
    public Publish detail(String publishId) {
        if (Strings.isBlank(publishId))
            throw new PublishDetailException("空参异常");
        Publish publishCache = RedisUtils.getPublishCache(publishId);
        if (Strings.isBlank(publishCache.getId())) {
            Publish publish = getBaseMapper().getById(publishId);
            if (Objects.isNull(publish)) {
                RedisUtils.setPublishCache(new Publish().setId(publishId + ""));
                return null;
            }
            publish.setVisits(publish.getVisits() + 1);
            if(RedisUtils.isEnable())
                RedisUtils.setPublishCache(publish);
            else
                updateById(publish);
            return publish;
        } else {
            if (Objects.nonNull(publishCache.getVisits()))
                RedisUtils.incrPublishVisits(publishCache);
            return publishCache;
        }
    }

    @Override
    public List<PublishImages> publishImages(String publishId) {
        List<PublishImages> publishImageCache = RedisUtils.getPublishImageCache(publishId);
        if (Objects.isNull(publishImageCache)) {
            QueryWrapper<PublishImages> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("publish_id", publishId);
            List<PublishImages> publishImages = publishImagesMapper.selectList(queryWrapper);
            if (publishImages.size() == 0)
                return null;
            else {
                RedisUtils.setPublishImagesCache(publishImages);
                return publishImages;
            }
        } else
            return publishImageCache;
    }

    @Override
    public void star(String publishId, Integer IOrD) {
        if (Strings.isBlank(publishId))
            throw new PublishDetailException("空参异常");
        Publish publishCache = RedisUtils.getPublishCache(publishId);
        if (Strings.isBlank(publishCache.getUserId())) {
            Publish publish = getBaseMapper().getById(publishId);
            if (Objects.isNull(publish)) {
                publish = new Publish().setId(publishId);
                RedisUtils.setPublishCache(publish);
                publish.setStar(0);
            }
            publish.setStar(publish.getStar() + IOrD);
            if(!RedisUtils.isEnable())
                RedisUtils.setPublishCache(publish);
            else
                updateById(publish);
        } else {
            RedisUtils.publishStar(publishCache, IOrD);
        }
    }
}
