package com.sq.SYTreeHole.exception;

import com.sq.SYTreeHole.common.Constants;
import com.sq.SYTreeHole.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ResponseBody
@ControllerAdvice
public class LoginAndRegisterException extends Exception{

    @ExceptionHandler(LoginControllerException.class)
    public Result<?> exception(Exception ex){
        log.error("登录/注册发生错误...\n{}",String.valueOf(ex));
        return new Result<>(Constants.CODE_500,"服务器错误",ex.getMessage());
    }
}
