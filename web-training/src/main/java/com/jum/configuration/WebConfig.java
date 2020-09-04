package com.jum.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 修改静态路径映射
 * 注意：因为启动类默认已经包含Configuration，所以不能用@Configuration，否则失效
 */
@Configuration
//@EnableWebMvc
public class WebConfig extends WebMvcConfigurationSupport {

    private static final String URL_PATTERNS = "/*";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将/static/**访问映射到classpath:/mystatic/
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .addResourceLocations("classpath:/resources/")
                .addResourceLocations("classpath:/public/")
                .addResourceLocations("classpath:/web/");

        super.addResourceHandlers(registry);
    }

}
