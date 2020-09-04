package com.jum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;

/**
 * @ClassName BackApplication
 * @Description TODO
 * @Author jb.zhou
 * @Date 2019/6/12
 * @Version 1.0
 */
@SpringBootApplication
@EnableConfigurationProperties
public class JumApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

    public static void main(String[] args) {
        SpringApplication.run(JumApplication.class, args);
    }
}
