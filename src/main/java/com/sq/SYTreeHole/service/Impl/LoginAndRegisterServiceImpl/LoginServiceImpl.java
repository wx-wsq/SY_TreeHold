package com.sq.SYTreeHole.service.Impl.LoginAndRegisterServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sq.SYTreeHole.Utils.RedisUtils;
import com.sq.SYTreeHole.Utils.SHA256Utils;
import com.sq.SYTreeHole.dao.loginAndRegisterDao.LoginMapper;
import com.sq.SYTreeHole.entity.User;
import com.sq.SYTreeHole.exception.LoginControllerException;
import com.sq.SYTreeHole.service.LoginAndRegisterService.LoginService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;
import java.util.Random;

@Service
public class LoginServiceImpl extends ServiceImpl<LoginMapper, User> implements LoginService {

    @Override
    public User getUserData(Serializable id) {
        if (Objects.isNull(id))
            throw new LoginControllerException("空参异常");
        return getById(id);
    }

    @Override
    public boolean setUserData(User user) {
        if (Objects.isNull(user) || Strings.isBlank(user.getUsername()) || Strings.isBlank(user.getPassword()))
            throw new LoginControllerException("空参异常");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        return update(user, queryWrapper);
    }

    public User loginForPass(String username, String password) {
        if (Strings.isBlank(username) || Strings.isBlank(password))
            throw new LoginControllerException("空参异常");
        password = SHA256Utils.encode(password);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("username", username)
                .eq("password", password);
        User user = getOne(queryWrapper);
        if (Objects.isNull(user)) {
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username);
            user = getOne(queryWrapper);
            if (Objects.nonNull(user))
                user.setId("0");
        }
        return user;
    }

    @Override
    public User loginForCode(String username, String code) {
        if (Strings.isBlank(username) || Strings.isBlank(code))
            throw new LoginControllerException("空参异常");
        if (!code.equals(code(code)))
            throw new LoginControllerException("验证码错误");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq("username", username);
        User user = getOne(queryWrapper);
        if (Objects.isNull(user)) {
            user = new User();
            user.setId("0");
        }
        return user;
    }

    @Override
    public boolean resetPassword(String username, String newPassword, String code) {
        if (Strings.isBlank(username) || Strings.isBlank(newPassword) || Strings.isBlank(code))
            throw new LoginControllerException("空参异常");
        if (!code.equals(code(username)))
            throw new LoginControllerException("验证码错误");
        newPassword = SHA256Utils.encode(newPassword);
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("username", username)
                .set("password", newPassword);
        return update(updateWrapper);
    }

    @Override
    public boolean register(User user, String code) {
        if (Strings.isBlank(user.getUsername()) || Strings.isBlank(user.getPassword()) || Strings.isBlank(code))
            throw new LoginControllerException("空参异常");
        else if (!code.equals(code(user.getUsername())))
            throw new LoginControllerException("验证码错误");
        else {
            user.setPassword(SHA256Utils.encode(user.getPassword()));
            return save(user);
        }
    }

    @Override
    public String code(String username) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 7; i++)
            sb.append(random.nextInt(10));
        RedisUtils.getRedisForString().set("code", sb.toString(), Duration.ofMinutes(5));
        if (RedisUtils.getRedisForString().get("code") != null) {
            //TODO 发送短信操作
            return sb.toString();
        } else
            throw new LoginControllerException("验证码获取失败....redis错误");
    }
}
