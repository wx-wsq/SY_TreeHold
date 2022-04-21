package com.sq.SYTreeHole.Interceptor;

import lombok.extern.log4j.Log4j2;
import org.apache.catalina.connector.RequestFacade;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j2
public class ErrorInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ip;
        if (request.getRequestURI().equals("/error")) {
            if (request.getHeader("x-forwarded-for") == null)
                ip = request.getRemoteAddr() + ":" + request.getRemotePort();
            else
                ip = request.getHeader("x-forwarded-for");
            log.error("发生/error错误：来自于：{},访问的接口为：{}", ip, ((RequestFacade) ((ServletRequestWrapper) request).getRequest()).getRequestURI());
            throw new Exception("此操作会被记录IP，请注意");
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
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
