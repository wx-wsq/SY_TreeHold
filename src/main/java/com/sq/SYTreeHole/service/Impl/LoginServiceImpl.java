package com.sq.SYTreeHole.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sq.SYTreeHole.Utils.RedisUtils;
import com.sq.SYTreeHole.Utils.SHA256Utils;
import com.sq.SYTreeHole.dao.LoginMapper;
import com.sq.SYTreeHole.entity.User;
import com.sq.SYTreeHole.service.LoginService;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;

@Service
public class LoginServiceImpl extends ServiceImpl<LoginMapper,User> implements LoginService {

    /**
     * 登录
     * @param userName 用户名
     * @param password 密码
     * @return 返回User对象
     */

    public User login(String userName, String password){
        password = SHA256Utils.encode(password);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("username",userName)
                .eq("password",password);
        return getOne(queryWrapper);
    }

    @Override
    public boolean resetPassword(String username, String newPassword, String code) {
        if(code.equals(code(username))) {
            newPassword = SHA256Utils.encode(newPassword);
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("username", username).set("password", newPassword);
            return update(updateWrapper);
        }else
            return false;
    }


    @Override
    public String code(String username) {
        StringBuilder sb =new StringBuilder();
        Random random= new Random();
        for (int i = 0; i < 7; i++) {
            sb.append(random.nextInt(10));
        }
        Boolean set = RedisUtils.getRedis()
                .set("code".getBytes(StandardCharsets.UTF_8),
                        sb.toString().getBytes(StandardCharsets.UTF_8),
                        Expiration.seconds(300),
                        RedisStringCommands.SetOption.SET_IF_ABSENT);
        if(Boolean.TRUE.equals(set)) {
            //TODO 发送短信操作

            return sb.toString();
        }
        else
            return new String(Objects.requireNonNull(RedisUtils.getRedis().get("code".getBytes(StandardCharsets.UTF_8))),StandardCharsets.UTF_8);
    }
}
