package com.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

@SpringBootApplication
@Slf4j
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(DemoApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        log.info("=============== 启动 Spring Boot 应用成功 ============");
    }

}
