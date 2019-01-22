package org.flowant.website;

import org.flowant.website.repository.AuthserverUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(
                        // sign in and up
                        "/", "/favicon.ico", "/error**",
                        // OAuth2 server public key
                        "/.well-known/jwks.json",
                        // debug
                        "/actuator/**", "/oauth/**")
                    .permitAll()
                .antMatchers("/user").hasAuthority("USER")
                .antMatchers("/admin").hasAuthority("ADMIN")
                .anyRequest().authenticated()
                .and()
            .oauth2Login()
                .loginPage("/login")
                .permitAll()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .rememberMe()
                .and()
            .httpBasic()
                .and()
            .logout()
                .permitAll()
                .and()
            .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new AuthserverUserDetailsService();
    }

}
