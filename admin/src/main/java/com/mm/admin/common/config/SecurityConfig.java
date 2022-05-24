package com.mm.admin.common.config;

import cn.hutool.json.JSONUtil;
import com.mm.admin.common.filter.VerifyCodeFilter;
import com.mm.admin.common.handler.AuthFailureHandler;
import com.mm.common.util.R;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;
import java.io.PrintWriter;

/**
 * security config
 *
 * @author lwl
 */
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    VerifyCodeFilter verifyCodeFilter;

    @Resource
    AuthFailureHandler authFailureHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.addFilterBefore(verifyCodeFilter, UsernamePasswordAuthenticationFilter.class).authorizeRequests()
                .antMatchers("/statics/**", "/login/get_code").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/sys/login")
                .successHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=utf-8");
                    try (PrintWriter writer = response.getWriter()) {
                        writer.write(JSONUtil.toJsonStr(R.ok()));
                    }
                })
                .failureHandler(authFailureHandler)
                .permitAll()
                .and()
                .csrf().disable();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
