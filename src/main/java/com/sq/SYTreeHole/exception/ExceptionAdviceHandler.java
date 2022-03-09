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
public class ExceptionAdviceHandler extends Exception{

    @ExceptionHandler(RuntimeException.class)
    public Result<?> LoginException(Exception ex){
        log.error("服务器错误...\n{}",String.valueOf(ex));
        return new Result<>(Constants.CODE_500,"服务器错误",ex.getMessage());
    }
}
