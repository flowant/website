package org.flowant.website;

import static org.flowant.website.rest.ContentRest.PATH_CONTENT;
import static org.flowant.website.rest.FileRest.PATH_FILES;
import static org.flowant.website.rest.ReplyRest.PATH_REPLY;
import static org.flowant.website.rest.ReviewRest.PATH_REVIEW;
import static org.flowant.website.rest.SearchRest.PATH_SEARCH;
import static org.flowant.website.rest.UserRest.PATH_EXIST;
import static org.flowant.website.rest.UserRest.PATH_USER;
import static org.flowant.website.rest.WebSiteRest.PATH_WEBSITE;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class BackendSecurityConfiguration {

    @Bean
    public SecurityWebFilterChain backendSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange()
                .pathMatchers(HttpMethod.GET, PATH_CONTENT + "/**").permitAll()
                .pathMatchers(HttpMethod.GET, PATH_REVIEW + "/**").permitAll()
                .pathMatchers(HttpMethod.GET, PATH_REPLY + "/**").permitAll()
                .pathMatchers(HttpMethod.GET, PATH_FILES + "/**").permitAll()
                .pathMatchers(HttpMethod.GET, PATH_SEARCH + "/**").permitAll()
                .pathMatchers(HttpMethod.GET, PATH_WEBSITE + "/**").permitAll()
                .pathMatchers(HttpMethod.GET, PATH_USER + PATH_EXIST).permitAll()
                .anyExchange().authenticated().and()
            .oauth2ResourceServer()
                .jwt();

        return http.build();
    }
}

