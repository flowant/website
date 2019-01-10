package org.flowant.frontend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

@Configuration
public class FrontendSecurityConfiguration {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers("/index.html", "/app.html").permitAll()
                .anyExchange().hasRole("USER").and()
                .securityContextRepository(new WebSessionServerSecurityContextRepository())
                .httpBasic()
                    .and()
                .build();
    }
}

