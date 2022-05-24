package com.mm.admin.common.config;

import com.mm.common.xss.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import javax.servlet.DispatcherType;

/**
 * Filter配置
 *
 * @author lwl
 */
@Configuration
public class FilterConfig {

    @SuppressWarnings("unchecked")
    @Bean
    public FilterRegistrationBean xss() {
        FilterRegistrationBean<XssFilter> registration = new FilterRegistrationBean(new XssFilter());
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        registration.addUrlPatterns("/*");
        registration.setName("xssFilter");
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registration;
    }
}
