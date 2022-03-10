package com.sq.SYTreeHole.service.Impl.PublishServiceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sq.SYTreeHole.dao.publishDao.PublishMapper;
import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.exception.PublishControllerException;
import com.sq.SYTreeHole.service.publishService.PublishService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(cacheNames = "publishes")
@Service
public class PublishServiceImpl extends ServiceImpl<PublishMapper, Publish> implements PublishService {

    private static final int number = 10;

    @Cacheable(key = "'all:'+#page")
    @Override
    public List<Publish> publishAsAll(String page) {
        if (Strings.isBlank(page))
            throw new PublishControllerException("参数为空");
        long pageStart = (Long.parseLong(page)-1)*10L;
        return getBaseMapper().publishesAsTime(pageStart, pageStart+ number);
    }

    @Cacheable(key = "'hot:'+#page")
    @Override
    public List<Publish> publishAsHot(String page) {
        if (Strings.isBlank(page))
            throw new PublishControllerException("参数为空");
        long pageStart = (Long.parseLong(page)-1)*10L;
        return getBaseMapper().publishesAsHot(pageStart, pageStart+ number);
    }
}