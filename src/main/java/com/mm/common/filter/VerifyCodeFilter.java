package com.mm.common.filter;

import cn.hutool.core.util.StrUtil;
import com.mm.common.exception.ValidateCodeException;
import com.mm.common.handler.AuthFailureHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码过滤器
 *
 * @author shmily
 */
@Slf4j
@Component
public class VerifyCodeFilter extends OncePerRequestFilter {

    @Autowired
    private AuthFailureHandler authFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/sys/login")) {
            try {
                String requestCaptcha = request.getParameter("code");
                if (requestCaptcha == null) {
                    throw new ValidateCodeException("验证码不存在");
                }
                String code = (String) request.getSession().getAttribute("code");
                if (StrUtil.isBlank(code)) {
                    throw new ValidateCodeException("验证码过期！");
                }
                log.info("开始校验验证码，生成的验证码为：{}，输入的验证码为：{}", code, requestCaptcha);
                if (!requestCaptcha.equals(code)) {
                    throw new ValidateCodeException("验证码不匹配");
                }
            } catch (ValidateCodeException e) {
                authFailureHandler.onAuthenticationFailure(request, response, e);
            } finally {
                filterChain.doFilter(request, response);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
