package com.mm.common.aspect;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.mm.common.exception.GException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * API请求日志
 *
 * @author lwl
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ApiLogAspect {

    final HttpServletRequest request;

    @Pointcut("execution(public * com.mm.modules.api.*.*.*.*(..))")
    public void apiLog() {
    }

    @Around("apiLog()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = point.proceed();
        StringBuffer sb = getParam(point);
        sb.append("RESULT:").append(result).append(" ");
        sb.append("TIME:").append(System.currentTimeMillis() - beginTime).append("ms");
        log.debug("around:{}", sb);
        return result;
    }

    @AfterThrowing(value = "apiLog()", throwing = "e")
    public void afterThrowing(JoinPoint point, Exception e) {
        StringBuffer sb = getParam(point);
        String err = e.toString();
        if (e instanceof GException) {
            GException ge = (GException) e;
            Map<String, Object> es = new HashMap<>();
            es.put("code", ge.getCode());
            es.put("msg", ge.getMsg());
            err = JSONUtil.toJsonStr(es);
        }
        sb.append("EXCEPTION:").append(err).append(" ");
        log.debug("afterThrowing:{}", sb);
    }

    private StringBuffer getParam(JoinPoint point) {
        StringBuffer sb = new StringBuffer(" ");
        sb.append("HTTP_METHOD:").append(request.getMethod()).append(" ");
        sb.append("URL:").append(request.getRequestURL().toString()).append(" ");
        sb.append("IP:").append(ServletUtil.getClientIP(request)).append(" ");
        sb.append("METHOD:").append(point.getTarget().getClass().getName()).append(".")
                .append(point.getSignature().getName()).append(" ");
        Map<String, String> param = new TreeMap<>();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            param.put(entry.getKey(), entry.getValue()[0]);
        }
        sb.append("PARAM:").append(param).append(" ");
        return sb;
    }
}
