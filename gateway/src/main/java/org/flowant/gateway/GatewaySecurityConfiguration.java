package org.flowant.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;

@Configuration
public class GatewaySecurityConfiguration {

    @SuppressWarnings("deprecation")
    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        return new MapReactiveUserDetailsService(
                User.withDefaultPasswordEncoder().username("user").password("user")
                .roles("USER").authorities("ROLE_USER").build(),
                User.withDefaultPasswordEncoder().username("admin").password("admin")
                .roles("USER", "ADMIN", "READER", "WRITER").authorities("ROLE_ACTUATOR", "ROLE_USER").build(),
                User.withDefaultPasswordEncoder().username("audit").password("audit")
                .roles("USER", "ADMIN", "READER").authorities("ROLE_ACTUATOR", "ROLE_USER").build());
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        /*
         * Angular add the X-XSRF-TOKEN header only if the XSRF-TOKEN cookie
         * was generated server-side with the following options:
         * Path = / and httpOnly = false
         * TODO check again
         */
        CookieServerCsrfTokenRepository csrfTokenRepository = 
                CookieServerCsrfTokenRepository.withHttpOnlyFalse();
        csrfTokenRepository.setCookiePath("/"); 

        return http.authorizeExchange()
                .pathMatchers("/login", "/index.html", "/", "*.bundle.*" ,"favicon.ico").permitAll()
                .anyExchange().authenticated()
                    .and()
                .csrf().csrfTokenRepository(csrfTokenRepository)
                    .and()
                .httpBasic().securityContextRepository(new WebSessionServerSecurityContextRepository())
                    .disable()
                .formLogin()
                    .and()
                .build();
    }

}

