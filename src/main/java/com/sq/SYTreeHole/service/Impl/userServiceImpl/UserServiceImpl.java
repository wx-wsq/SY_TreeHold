package com.sq.SYTreeHole.service.Impl.userServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sq.SYTreeHole.Utils.RedisUtils;
import com.sq.SYTreeHole.Utils.SHA256Utils;
import com.sq.SYTreeHole.dao.UserDao.UserMapper;
import com.sq.SYTreeHole.entity.User;
import com.sq.SYTreeHole.exception.LoginException;
import com.sq.SYTreeHole.service.userService.UserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getUserData(Serializable id) {
        if (Objects.isNull(id))
            throw new LoginException("空参异常");
        return getById(id);
    }

    @Transactional
    @Override
    public boolean setUserData(User user, MultipartHttpServletRequest multipartHttpServletRequest){
        if (Objects.isNull(user) || Strings.isBlank(user.getUsername()) || Strings.isBlank(user.getPassword()))
            throw new LoginException("空参异常");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        MultipartFile head = multipartHttpServletRequest.getFile("head");
        if(Objects.nonNull(head)){
            String headImageName;
            if(Strings.isNotBlank(user.getHeadUrl()))
                headImageName = UUID.randomUUID() +".jpg";
            else
                headImageName = user.getHeadUrl().substring(user.getHeadUrl().lastIndexOf("/"));
            try {
                //TODO 更改路径
                head.transferTo(new File("D:/images/heads/"+headImageName));
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("头像保存失败...");
            }
            //TODO 更改映射路径
            user.setHeadUrl("http:/localhost/image/"+headImageName);
        }
        return update(user, queryWrapper);
    }

    public User loginForPass(String username, String password) {
        if (Strings.isBlank(username) || Strings.isBlank(password))
            throw new LoginException("空参异常");
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
            throw new LoginException("空参异常");
        if (!equalsCode(username, code))
            throw new LoginException("验证码错误");
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
            throw new LoginException("空参异常");
        if (!code.equals(code(username)))
            throw new LoginException("验证码错误");
        newPassword = SHA256Utils.encode(newPassword);
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("username", username)
                .set("password", newPassword);
        return update(updateWrapper);
    }

    @Override
    public boolean register(User user, String code) {
        if (Objects.isNull(user) || Strings.isBlank(user.getUsername()) || Strings.isBlank(user.getPassword()))
            throw new LoginException("空参异常");
        if (equalsCode(user.getUsername(), code))
            throw new LoginException("验证码错误");
        else {
            user.setPassword(SHA256Utils.encode(user.getPassword()));
            return save(user);
        }
    }

    @Override
    public String code(String username) {
        if (Strings.isBlank(username))
            throw new LoginException("空参异常");
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 5; i++)
            sb.append(random.nextInt(10));
        RedisUtils.getRedisForObject().set(username + ":code", sb.toString(), Duration.ofMinutes(5));
        if (RedisUtils.getRedisForObject().get(username + ":code") != null) {
            //TODO 发送短信操作
            return sb.toString();
        } else
            throw new LoginException("验证码获取失败....redis错误");
    }

    public boolean equalsCode(String username, String code) {
        String cacheCode = (String)RedisUtils.getRedisForObject().get(username + ":code");
        return code.equals(cacheCode);
    }
}
