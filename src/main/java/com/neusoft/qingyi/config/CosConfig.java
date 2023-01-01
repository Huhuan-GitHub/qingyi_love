package com.neusoft.qingyi.config;

import com.neusoft.qingyi.pojo.Cos;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯云对象存储配置类
 */
@Configuration
public class CosConfig {
    @Bean
    public Cos getCosConfig() {
        return Cos.getCos();
    }
}
