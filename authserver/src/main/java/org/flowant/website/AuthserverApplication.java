package org.flowant.website;

import org.flowant.website.event.MockDataGenerateEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;

@SpringBootApplication
@EnableAuthorizationServer
public class AuthserverApplication {

    @Bean
    public PasswordEncoder exposePasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = 
                SpringApplication.run(AuthserverApplication.class, args);
        ctx.publishEvent(new MockDataGenerateEvent());
    }
}
