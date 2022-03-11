package com.sq.SYTreeHole.service.Impl.publishServiceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sq.SYTreeHole.Utils.RedisUtils;
import com.sq.SYTreeHole.dao.publishDao.PublishMapper;
import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.exception.PublishListException;
import com.sq.SYTreeHole.service.publishService.PublishService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PublishServiceImpl extends ServiceImpl<PublishMapper, Publish> implements PublishService {

    private static final int number = 10;

    @Override
    public List<Publish> publishAsAll(String page) {
        if (Strings.isBlank(page))
            throw new PublishListException("参数为空");
        long pageStart = (Long.parseLong(page) - 1) * 10L;
        List<Publish> publishListCacheOfAll = RedisUtils.getPublishListCache("all", page);
        if (publishListCacheOfAll.size() == 0) {
            publishListCacheOfAll = getBaseMapper().publishesAsTime(pageStart, pageStart + number);
            RedisUtils.setPublishListCache(publishListCacheOfAll);
            StringBuilder sb = new StringBuilder();
            for (Publish publish : publishListCacheOfAll) sb.append(publish.getId()).append(",");
            RedisUtils.setPublishListCacheOfId("all", page, sb.toString());
        }
        return publishListCacheOfAll;
    }

    @Override
    public List<Publish> publishAsHot(String page) {
        if (Strings.isBlank(page))
            throw new PublishListException("参数为空");
        long pageStart = (Long.parseLong(page) - 1) * 10L;
        List<Publish> publishListCacheOfHot = RedisUtils.getPublishListCache("hot", page);
        if (publishListCacheOfHot.size() == 0) {
            publishListCacheOfHot = getBaseMapper().publishesAsHot(pageStart, pageStart + number);
            RedisUtils.setPublishListCache(publishListCacheOfHot);
            StringBuilder sb = new StringBuilder();
            for (Publish publish : publishListCacheOfHot) sb.append(publish.getId()).append(",");
            RedisUtils.setPublishListCacheOfId("hot", page, sb.toString());
        }
        return publishListCacheOfHot;
    }
}
