package com.sq.SYTreeHole.Interceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.sq.SYTreeHole.Utils.JwtUtils;
import com.sq.SYTreeHole.exception.PowerException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if(request.getRequestURI().equals("/error"))
            return true;
        String token = request.getHeader("token");
        if (Strings.isNotBlank(token)) {
            DecodedJWT tokenData = JwtUtils.getTokenData(token, "SY-server");
            if (!Strings.isBlank(tokenData.getClaim("username").asString()))
                return true;
            else
                throw new PowerException("权限不足");
        } else
            throw new PowerException("权限不足");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
