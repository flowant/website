package org.flowant.website;

import static org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository.withHttpOnlyFalse;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class BackendSecurityConfiguration {

    public static final String ROLE_WRITER = "WRITER";//TODO Enum

//    @Bean // TODO modify for OAuth2 Resource server, also Testcases
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers(HttpMethod.POST, "/**").hasRole(ROLE_WRITER)
                .pathMatchers("/actuator/**", "/").permitAll() //TODO: remove
                .anyExchange().authenticated().and()
                .csrf().csrfTokenRepository(withHttpOnlyFalse()).and()
                .httpBasic()
                    .disable()
                .build();
    }
}

