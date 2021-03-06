package com.sq.SYTreeHole.controller;

import com.sq.SYTreeHole.DTO.LoginDTO;
import com.sq.SYTreeHole.Utils.JwtUtils;
import com.sq.SYTreeHole.common.Constants;
import com.sq.SYTreeHole.common.Result;
import com.sq.SYTreeHole.entity.User;
import com.sq.SYTreeHole.exception.LoginException;
import com.sq.SYTreeHole.service.userService.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestController
public class UserController {

    private final UserService loginService;

    public UserController(UserService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/user/{id}")
    public Result<?> getUserData(@PathVariable String id) {
        return new Result<>(Constants.CODE_200, "访问成功", loginService.getUserData(id));
    }

    @PostMapping("/setUserData")
    public Result<?> setUserData(User user, HttpServletRequest httpServletRequest) {
        boolean userData = loginService.setUserData(user, (MultipartHttpServletRequest) httpServletRequest);
        if (userData)
            return new Result<>(Constants.CODE_200, "修改成功！", null);
        else
            throw new LoginException("保存用户信息失败");
    }

    @PostMapping("/loginForPass")
    public LoginDTO loginForPass(String username, String password) {
        User user = loginService.loginForPass(username, password);
        if (Objects.isNull(user))
            return new LoginDTO(Constants.CODE_400, "此用户尚未注册", null, null, null);
        else if (user.getId().equals("0"))
            return new LoginDTO(Constants.CODE_400, "账户或密码错误", null, null, null);
        else {
            String token = JwtUtils.getToken(user.getId(), "SY-server");
            return new LoginDTO(Constants.CODE_200, "登录成功", -1L, token, user.getId());
        }
    }

    @PostMapping("/loginForCode")
    public LoginDTO loginForCode(String username, String code) {
        User user = loginService.loginForCode(username, code);
        if (Objects.isNull(user))
            return new LoginDTO(Constants.CODE_400, "此用户尚未注册", null, null, null);
        else if (user.getId().equals("0"))
            return new LoginDTO(Constants.CODE_400, "用户名错误", null, null, null);
        else {
            String token = JwtUtils.getToken(user.getId(), "SY-server");
            return new LoginDTO(Constants.CODE_200, "登陆成功", -1L, token, user.getId());
        }
    }

    @PostMapping("/register")
    public LoginDTO register(User user, String code) {
        System.out.println(user + "::::" + code);
        if (loginService.register(user, code)) {
            if (Objects.isNull(loginService.loginForPass(user.getUsername(), user.getPassword())))
                throw new LoginException("服务器异常");
            String token = JwtUtils.getToken(user.getId(), "SY-server");
            return new LoginDTO(Constants.CODE_200, "注册成功", -1L, token, user.getId());
        } else
            throw new LoginException("账户注册失败");
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