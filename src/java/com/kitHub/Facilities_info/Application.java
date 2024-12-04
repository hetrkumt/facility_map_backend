package com.kitHub.Facilities_info;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> onApplicationReadyEvent() {
        return event -> {
            String port = event.getApplicationContext().getEnvironment().getProperty("local.server.port");
            System.out.println("현재 작동 중인 포트: " + port);
        };
    }
}
