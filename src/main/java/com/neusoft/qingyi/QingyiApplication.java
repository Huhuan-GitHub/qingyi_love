package com.neusoft.qingyi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableScheduling
@SpringBootApplication
@MapperScan(basePackages = "com.neusoft.qingyi.mapper")
@EnableSwagger2
public class QingyiApplication {

    public static void main(String[] args) {
        SpringApplication.run(QingyiApplication.class, args);
    }
    @PostConstruct
    void started() {
        // 手动设置jvm时间：将时间改为第8时区的时间
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
    }
}
