package com.xie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * @作者：xie
 * @时间：2023/6/29 21:35
 */

@SpringBootApplication
public class JokerApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(JokerApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
    }
}
