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

    @Value("${mini-user.avatar-path}")
    private String avatarPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/qingyi_posts_file/**", "/qingyi_mini_user_avatar/**")
                .addResourceLocations("file:" + postsFilePath + "/").addResourceLocations("file:" + avatarPath + "/");
    }
}
