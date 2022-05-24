package com.mm.admin.common.aspect;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import com.mm.admin.common.annotation.SysLog;
import com.mm.admin.common.util.SecurityUtil;
import com.mm.service.sys.entity.SysLogEntity;
import com.mm.service.sys.service.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 系统日志，切面处理类
 *
 * @author lwl
 */
@Aspect
@Component
public class SysLogAspect {

    @Autowired(required = false)
    private SysLogService sysLogService;
    @Autowired
    private HttpServletRequest request;

    @Pointcut("@annotation(com.mm.admin.common.annotation.SysLog)")
    public void logPointCut() {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        //执行方法
        Object result = point.proceed();
        //执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;

        //保存日志
        saveSysLog(point, time);

        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        SysLogEntity sysLog = new SysLogEntity();
        SysLog syslog = method.getAnnotation(SysLog.class);
        if (syslog != null) {
            //注解上的描述
            sysLog.setOperation(syslog.value());
        }

        //请求的方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setMethod(className + "." + methodName + "()");

        //请求的参数
        Object[] args = joinPoint.getArgs();
        try {

            String params = JSONUtil.toJsonStr(args[0]);
            sysLog.setParams(params);
        } catch (Exception e) {

        }

        //设置IP地址
        sysLog.setIp(ServletUtil.getClientIP(request));

        //用户名
        sysLog.setUsername(SecurityUtil.getUsername());

        sysLog.setTime(time);
        //保存系统日志
        sysLogService.save(sysLog);
    }
}
