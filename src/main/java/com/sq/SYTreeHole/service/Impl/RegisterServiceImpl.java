package com.sq.SYTreeHole.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sq.SYTreeHole.dao.RegisterMapper;
import com.sq.SYTreeHole.entity.User;
import com.sq.SYTreeHole.service.RegisterService;
import org.springframework.stereotype.Service;

@Service
public class RegisterServiceImpl extends ServiceImpl<RegisterMapper,User> implements RegisterService {
    @Override
    public boolean register(User user) {
        return save(user);
    }
}
