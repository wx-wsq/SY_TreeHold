package com.sq.SYTreeHole.service;

import com.sq.SYTreeHole.entity.User;

public interface LoginService{

    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @return 返回User对象
     */
    User login(String username, String password, String code);

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
}
