package com.sq.SYTreeHole.service.Impl.PublishServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sq.SYTreeHole.dao.publishDao.PublishManagementMapper;
import com.sq.SYTreeHole.entity.Publish;
import com.sq.SYTreeHole.exception.ManagementPublishException;
import com.sq.SYTreeHole.service.publishService.PublishManagementService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
public class PublishManagementServiceImpl extends ServiceImpl<PublishManagementMapper, Publish> implements PublishManagementService {

    @Override
    public Publish insert(Publish publish) {
        if (Objects.isNull(publish) || Strings.isBlank(publish.getUserId()))
            throw new ManagementPublishException("空参异常");
        if (save(publish))
            return publish;
        else
            return null;
    }
    @Override
    public Publish modify(Publish publish) {
        if (Objects.isNull(publish) || Strings.isBlank(publish.getUserId()))
            throw new ManagementPublishException("空参异常");
        QueryWrapper<Publish> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("id", publish.getId())
                .eq("user_id", publish.getUserId());
        if(Objects.isNull(getOne(queryWrapper)))
            throw new ManagementPublishException("系统错误...");
        if (updateById(publish))
            return publish;
        else
            return null;
    }

    @Override
    public Publish delete(Publish publish) {
        if (Objects.isNull(publish) || Strings.isBlank(publish.getUserId()))
            throw new ManagementPublishException("空参异常");
        QueryWrapper<Publish> queryWrapper = new QueryWrapper<>(publish);
        queryWrapper
                .eq("id", publish.getId())
                .eq("user_id", publish.getUserId());
        if (remove(queryWrapper))
            return publish;
        else
            return null;
    }

    @Override
    public List<Publish> selectMy(String userId, String page) {
        if (Strings.isBlank(userId) || Strings.isBlank(page))
            throw new ManagementPublishException("空参异常");
        IPage<Publish> iPage = new Page<>(Long.parseLong(page), 10);
        QueryWrapper<Publish> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return getBaseMapper().selectPage(iPage, queryWrapper).getRecords();
    }
}
