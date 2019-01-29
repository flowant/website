package org.flowant.website;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;

@Configuration
public class FrontendSecurityConfiguration {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers("/", "/index.html", "/*.js", "/favicon.ico").permitAll()
                .anyExchange().authenticated()
                    .and()
                .oauth2Login()
                    .and()
                .csrf()
                    .csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
                    .and()
                .httpBasic().disable()
                .formLogin().disable()
                .build();
    }
}

