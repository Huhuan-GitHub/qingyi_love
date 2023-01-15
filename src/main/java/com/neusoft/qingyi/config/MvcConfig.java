package com.neusoft.qingyi.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class MvcConfig implements WebMvcConfigurer {

    @Value("${server.port}")
    private String port;

    @Value("${posts.file-path}")
    private String postsFilePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path = postsFilePath + "/";
        registry.addResourceHandler("/qingyi_posts_file/**").addResourceLocations("file:" + path);
    }
}
