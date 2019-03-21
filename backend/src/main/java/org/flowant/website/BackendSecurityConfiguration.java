package org.flowant.website;

import org.flowant.website.rest.ContentRest;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class BackendSecurityConfiguration {

    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers(HttpMethod.GET, ContentRest.PATH_CONTENT + "/**").permitAll()
                .anyExchange().authenticated().and()
                .csrf().disable()
                .httpBasic()
                    .disable()
                .build();
    }
}

