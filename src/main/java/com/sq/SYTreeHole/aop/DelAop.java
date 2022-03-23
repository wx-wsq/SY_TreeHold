package com.sq.SYTreeHole.aop;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.sq.SYTreeHole.Utils.JwtUtils;
import com.sq.SYTreeHole.exception.ManagementPublishException;
import com.sq.SYTreeHole.exception.PowerException;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Aspect
@Component
public class DelAop {
    @Resource
    HttpServletRequest httpServletRequest;

    @Pointcut("execution(public * com.sq.SYTreeHole.service.Impl..delete*(..))")
    public void delAspect() {
    }

    @Before("delAspect()")
    public void doBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if(Objects.isNull(args[1]))
            throw new ManagementPublishException("空参异常");
        String token = httpServletRequest.getHeader("token");
        if(Strings.isBlank(token))
            throw new PowerException("无权此操作");
        DecodedJWT tokenData = JwtUtils.getTokenData(token, "SY-server");
        String userId = tokenData.getClaim("userId").asString();
        if (Strings.isBlank(userId))
            throw new PowerException("无权此操作");
        if(!args[1].equals(userId))
            throw new PowerException("无权此操作");
    }
}
