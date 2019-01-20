package org.flowant.frontend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
public class FrontendSecurityConfiguration {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange()
                .pathMatchers("/index.html", "/").permitAll()
                .pathMatchers("/actuator/**", "/**").permitAll()//TODO: remove
                .anyExchange().hasRole("USER").and()
                .httpBasic()
                    .and()
                .build();
    }
}

