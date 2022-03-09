package com.sq.SYTreeHole.service.Impl.PublishServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sq.SYTreeHole.dao.publisDao.PublishMapper;
import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.service.publishService.PublishService;

import java.util.List;

public class PublishServiceImpl extends ServiceImpl<PublishMapper,Publish> implements PublishService {
    @Override
    public List<Publish> publishAsAll(Long page,Long count) {
        QueryWrapper<Publish> queryWrapper = new QueryWrapper<>();
        IPage<Publish> iPage = new Page<>(page,count);
        return getBaseMapper().selectPage(iPage,queryWrapper).getRecords();
    }
}
