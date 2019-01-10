package org.flowant.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

//  @Configuration
//  @Order(SecurityProperties.BASIC_AUTH_ORDER)
//  protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//      @Autowired
//      public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
//          // @formatter:off
//          auth.inMemoryAuthentication().withUser("user").password("password").roles("USER").and().withUser("admin")
//                  .password("admin").roles("USER", "ADMIN", "READER", "WRITER").and().withUser("audit")
//                  .password("audit").roles("USER", "ADMIN", "READER");
//          // @formatter:on
//      }
//
//      @Override
//      protected void configure(HttpSecurity http) throws Exception {
//          // @formatter:off
//          http.httpBasic().and().logout().and().authorizeRequests().antMatchers("/index.html", "/").permitAll()
//                  .anyRequest().authenticated().and().csrf()
//                  .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
//          // @formatter:on
//      }
//  }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers("/index.html", "/").permitAll()
                .anyExchange().authenticated()
                    .and()
                .csrf().csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
                    .and()
                .securityContextRepository(new WebSessionServerSecurityContextRepository())
                .httpBasic()
                    .and()
                .logout()
                    .and()
                .build();
    }

}

