package com.sq.SYTreeHole.service.Impl.publishServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sq.SYTreeHole.Utils.RedisUtils;
import com.sq.SYTreeHole.dao.publishDao.PublishImagesMapper;
import com.sq.SYTreeHole.dao.publishDao.PublishMapper;
import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.entity.PublishImages;
import com.sq.SYTreeHole.exception.PublishListException;
import com.sq.SYTreeHole.service.publishService.PublishService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class PublishServiceImpl extends ServiceImpl<PublishMapper, Publish> implements PublishService {

    @Resource
    private PublishImagesMapper publishImagesMapper;

    private static final int number = 10;

    @Override
    public List<Publish> publishAsAll(String page) {
        if (Strings.isBlank(page))
            throw new PublishListException("参数为空");
        long pageStart = (Long.parseLong(page) - 1) * 10L;
        List<Publish> publishListCacheOfAll = RedisUtils.getPublishListCache("publishAll", page);
        if (publishListCacheOfAll.size() == 0) {
            publishListCacheOfAll = getBaseMapper().publishesAsTime(pageStart, pageStart + number);
            RedisUtils.setPublishListCache(publishListCacheOfAll);
            StringBuilder sb = new StringBuilder();
            for (Publish publish : publishListCacheOfAll) sb.append(publish.getId()).append(",");
            RedisUtils.setPublishListCacheOfId("publishAll", page, sb.toString());
        }
        return publishListCacheOfAll;
    }

    @Override
    public List<Publish> publishAsHot(String page) {
        if (Strings.isBlank(page))
            throw new PublishListException("参数为空");
        long pageStart = (Long.parseLong(page) - 1) * 10L;
        List<Publish> publishListCacheOfHot = RedisUtils.getPublishListCache("publishHot", page);
        if (publishListCacheOfHot.size() == 0) {
            publishListCacheOfHot = getBaseMapper().publishesAsHot(pageStart, pageStart + number);
            RedisUtils.setPublishListCache(publishListCacheOfHot);
            StringBuilder sb = new StringBuilder();
            for (Publish publish : publishListCacheOfHot) sb.append(publish.getId()).append(",");
            RedisUtils.setPublishListCacheOfId("publishHot", page, sb.toString());
        }
        return publishListCacheOfHot;
    }

    @Override
    public List<List<PublishImages>> publishImages(List<Publish> publishes) {
        List<List<PublishImages>> publishImagesList = new ArrayList<>();
        for (Publish publish : publishes) {
            List<PublishImages> publishImageCache = RedisUtils.getPublishImageCache(publish.getId());
            if (publishImageCache == null) {
                QueryWrapper<PublishImages> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("publish_id", publish.getId());
                List<PublishImages> publishImages = publishImagesMapper.selectList(queryWrapper);
                publishImagesList.add(publishImages);
                RedisUtils.setPublishImagesCache(publishImages);
            } else
                publishImagesList.add(publishImageCache);
        }
        return publishImagesList;
    }
}
