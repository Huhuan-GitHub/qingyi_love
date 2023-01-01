package com.neusoft.qingyi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo());
    }

    /**
     * 配置Swagger信息
     * @return
     */
    private ApiInfo apiInfo() {
        // 作者信息
        Contact contact = new Contact("huhuan", "https://gitee.com/square_of_root_three", "2960036258@qq.com");
        return new ApiInfo("青衣接口文档", "青衣接口文档", "1.0", "urn:tos", contact, "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", new ArrayList());
    }
}
