package org.flowant.backend;

import org.flowant.backend.repository.LoggingEventListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UsersApplication {
    public static void main(String[] args) {
        SpringApplication.run(UsersApplication.class, args);
    }

    @Bean
    LoggingEventListener loggingEventListener() {
        return new LoggingEventListener();
    }
}
