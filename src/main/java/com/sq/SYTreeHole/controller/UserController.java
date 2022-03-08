package com.sq.SYTreeHole.controller;

import com.sq.SYTreeHole.common.Constants;
import com.sq.SYTreeHole.common.Result;
import com.sq.SYTreeHole.entity.User;
import com.sq.SYTreeHole.exception.LoginControllerException;
import com.sq.SYTreeHole.service.LoginService;
import com.sq.SYTreeHole.service.RegisterService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class UserController {

    private final LoginService loginService;
    private final RegisterService registerService;

    public UserController(LoginService loginService, RegisterService registerService) {
        this.loginService = loginService;
        this.registerService = registerService;
    }

    @PostMapping("/login")
    public Result<User> login(String username, String password) {
        User user = loginService.login(username, password);
        if (Objects.nonNull(user))
            return new Result<>(Constants.CODE_200, "登录成功", user);
        else
            return new Result<>(Constants.CODE_401, "第一次使用，请注册", null);
    }

    @PostMapping("/register")
    public User register(User user) {
        if (Strings.isNotBlank(user.getUsername()) && registerService.register(user))
            return loginService.login(user.getUsername(), user.getPassword());
        else
            throw new LoginControllerException("账户注册失败");
    }

    @GetMapping("/code")
    public Result<String> code(String username) {
        String code = loginService.code(username);
        return new Result<>(Constants.CODE_400, "验证码", code);
    }

    @PostMapping("/resetPassword")
    public Result<String> resetPassword(String username, String newPassword, String code) {
        if (Strings.isBlank(username) || Strings.isBlank(newPassword) || Strings.isBlank(code))
            return new Result<>(Constants.CODE_400, "参数不能为空", null);
        boolean resetPassword = loginService.resetPassword(username, newPassword, code);
        if (resetPassword)
            return new Result<>(Constants.CODE_200, "修改成功", null);
        else
            return new Result<>(Constants.CODE_400, "验证码错误", null);
    }
}
