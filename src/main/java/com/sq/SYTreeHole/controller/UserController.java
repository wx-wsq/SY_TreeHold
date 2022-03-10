package com.sq.SYTreeHole.controller;

import com.sq.SYTreeHole.Utils.JwtUtils;
import com.sq.SYTreeHole.common.Constants;
import com.sq.SYTreeHole.common.Result;
import com.sq.SYTreeHole.entity.User;
import com.sq.SYTreeHole.exception.LoginControllerException;
import com.sq.SYTreeHole.service.LoginAndRegisterService.LoginService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RestController
public class UserController {

    private final LoginService loginService;

    public UserController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/loginForPass")
    public Result<?> loginForPass(String username, String password) {
        User user = loginService.loginForPass(username, password);
        if (Objects.isNull(user))
            return new Result<>(Constants.CODE_400, "此用户尚未注册", null);
        else if(user.getId().equals("0"))
            return new Result<>(Constants.CODE_400, "用户名或密码错误", null);
        else {
            String token = JwtUtils.getToken(username, "SY-server");
            List<Object> objects = Arrays.asList(token, user);
            return new Result<>(Constants.CODE_200, "登陆成功", objects);
        }
    }

    @PostMapping("/loginForCode")
    public Result<?> loginForCode(String username, String code){
        User user = loginService.loginForCode(username, code);
        if (Objects.isNull(user))
            return new Result<>(Constants.CODE_400, "此用户尚未注册", null);
        else if(user.getId().equals("0"))
            return new Result<>(Constants.CODE_400, "用户名错误", null);
        else
            return new Result<>(Constants.CODE_200,"登陆成功",user);
    }

    @PostMapping("/register")
    public Result<?> register(User user, String code) {
        if (Objects.nonNull(user) && loginService.register(user, code)) {
            User loginUser = loginService.loginForPass(user.getUsername(), user.getPassword());
            return new Result<>(Constants.CODE_200,"注册成功",loginUser);
        }else
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
