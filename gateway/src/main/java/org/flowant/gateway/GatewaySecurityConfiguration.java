package org.flowant.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;

import reactor.core.publisher.Mono;

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
//        csrfTokenRepository.setCookiePath("/");

        return http.authorizeExchange()
                .pathMatchers("/", "/index.html", "/*.js", "/favicon.ico").permitAll()
                .anyExchange().authenticated()
                    .and()
                .csrf().csrfTokenRepository(csrfTokenRepository)
                    .and()
                .httpBasic().securityContextRepository(new WebSessionServerSecurityContextRepository())
                    .and()
                 /* Issue: Webflux basic auth returns www-authenticate even when x-requested-with is set #5234
                  * https://github.com/spring-projects/spring-security/issues/5234
                  *
                  * This always remove WWW-Authenticate header in response regardless of x-requested-with header in Request.
                  * So, we don't have to add x-requested-with header in Angular.
                  *
                  * see also HttpBasicServerAuthenticationEntryPoint class */
                 .exceptionHandling()
                     .authenticationEntryPoint((exchange, e) -> Mono.fromRunnable(() -> 
                             exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                     .and()
                .build();
    }

}

