package com.sq.SYTreeHole.service.UserService;

import com.sq.SYTreeHole.entity.User;

import java.io.Serializable;

public interface UserService {

    /**
     * 获取用户信息
     * @param id 用户序列化主键
     * @return 返回用户信息
     */
    User getUserData(Serializable id);

    /**
     * 存储用户信息
     * @param user 用户信息实体
     * @return 是否更新成功
     */
    boolean setUserData(User user);

    /**
     * 密码登录
     * @param username 用户名
     * @param password 密码
     * @return 返回User对象
     */
    User loginForPass(String username, String password);


    /**
     * 验证码登录
     * @param username 用户账号
     * @param code 验证码
     * @return 登陆后用户实体
     */
    User loginForCode(String username, String code);

    /**
     * 重置密码
     * @param username 用户账户
     * @param newPassword 新密码
     * @param code 验证码(五分钟有效)
     * @return 是否成功
     */
    boolean resetPassword(String username,String newPassword,String code);

    /**
     * 生成验证码并存入redis中
     * 五分钟有效
     * @param username 用户账户
     * @return redis中的验证码
     */
    String code(String username);

    /**
     *
     * @param user Controller传来的User对象
     * @return 是否添加成功
     */
    boolean register(User user, String code);

}
