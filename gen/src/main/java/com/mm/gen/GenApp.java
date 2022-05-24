package com.mm.gen;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 代码生成器
 *
 * @author shmily
 */
@SpringBootApplication
@EnableSpringUtil
public class GenApp {
    public static void main(String[] args) {
        SpringApplication.run(GenApp.class, args);
    }
}
