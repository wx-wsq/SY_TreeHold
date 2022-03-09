package com.sq.SYTreeHole.controller;

import com.sq.SYTreeHole.common.Constants;
import com.sq.SYTreeHole.common.Result;
import com.sq.SYTreeHole.entity.User;
import com.sq.SYTreeHole.exception.LoginControllerException;
import com.sq.SYTreeHole.service.LoginAndRegisterService.LoginService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class UserController {

    private final LoginService loginService;

    public UserController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public Result<User> login(String username, String password) {
        User user = loginService.login(username, password);
        if (Objects.isNull(user))
            return new Result<>(Constants.CODE_400, "此用户尚未注册", null);
        else if(user.getId().equals("0"))
            return new Result<>(Constants.CODE_400, "用户名或密码错误", null);
        else
            return new Result<>(Constants.CODE_200,"登陆成功",null);
    }

    @PostMapping("/register")
    public User register(User user, String code) {
        if (Objects.nonNull(user) && loginService.register(user, code))
            return loginService.login(user.getUsername(), user.getPassword());
        else
            throw new LoginControllerException("账户注册失败");
    }

    @GetMapping("/code")
    public Result<String> code(String username) {
        String code = loginService.code(username);
        return new Result<>(Constants.CODE_200, "验证码", code);
    }

    @PostMapping("/resetPassword")
    public Result<String> resetPassword(String username, String newPassword, String code) {
        boolean resetPassword = loginService.resetPassword(username, newPassword, code);
        if (resetPassword)
            return new Result<>(Constants.CODE_200, "修改成功", null);
        else
            return new Result<>(Constants.CODE_400, "服务器异常，可能不存在此用户", null);
    }
}
