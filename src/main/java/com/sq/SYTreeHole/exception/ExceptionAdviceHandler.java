package com.sq.SYTreeHole.exception;

import com.sq.SYTreeHole.common.Constants;
import com.sq.SYTreeHole.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@ResponseBody
//@ControllerAdvice
public class ExceptionAdviceHandler extends Exception{

    @ExceptionHandler(PowerException.class)
    public Result<?> powerException(Exception ex){
        log.error("越权访问...\n{}",String.valueOf(ex));
        return new Result<>(Constants.CODE_401,"无权访问",ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<?> LoginException(Exception ex){
        log.error("参数异常...\n{}",String.valueOf(ex));
        return new Result<>(Constants.CODE_400,"参数异常",ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<?> Exception(Exception ex){
        log.error("服务器异常...\n{}",String.valueOf(ex));
        return new Result<>(Constants.CODE_500,"服务器错误",ex.getMessage());
    }
}
