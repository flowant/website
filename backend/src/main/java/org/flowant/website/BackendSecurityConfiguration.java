package org.flowant.website;

import static org.flowant.website.rest.ContentRest.PATH_CONTENT;
import static org.flowant.website.rest.FileRest.PATH_FILES;
import static org.flowant.website.rest.ReplyRest.PATH_REPLY;
import static org.flowant.website.rest.ReviewRest.PATH_REVIEW;
import static org.flowant.website.rest.SearchRest.PATH_SEARCH;
import static org.flowant.website.rest.UserRest.PATH_SIGNUP;
import static org.flowant.website.rest.UserRest.PATH_USER;
import static org.flowant.website.rest.WebSiteRest.PATH_WEBSITE;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class BackendSecurityConfiguration {

    @Bean
    public PasswordEncoder exposePasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain backendSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange()
                .pathMatchers(HttpMethod.GET, PATH_CONTENT + "/**").permitAll()
                .pathMatchers(HttpMethod.GET, PATH_REVIEW + "/**").permitAll()
                .pathMatchers(HttpMethod.GET, PATH_REPLY + "/**").permitAll()
                .pathMatchers(HttpMethod.GET, PATH_FILES + "/**").permitAll()
                .pathMatchers(HttpMethod.GET, PATH_SEARCH + "/**").permitAll()
                .pathMatchers(HttpMethod.GET, PATH_WEBSITE + "/**").permitAll()
                .pathMatchers(HttpMethod.POST, PATH_USER + PATH_SIGNUP).permitAll()
                .anyExchange().authenticated().and()
            .csrf()
                .disable()
            .oauth2ResourceServer()
                .jwt();

        return http.build();
    }
}

