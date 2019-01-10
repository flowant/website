package org.flowant.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;

@Configuration
public class BackendSecurityConfiguration {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers(HttpMethod.POST, "/**").hasRole("WRITER")
                .anyExchange().authenticated().and()
                .csrf().csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse()).and()
                .httpBasic().disable()
                .securityContextRepository(new WebSessionServerSecurityContextRepository())
                .build();
    }

}

