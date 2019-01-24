package org.flowant.website;

import org.flowant.website.repository.AuthserverUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    OAuth2ClientConfig oAuth2ClientConfig;

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
                .antMatchers("/user").hasRole("USER")
                .antMatchers("/admin").hasRole("ADMIN")
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
            .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
            .addFilterBefore(oAuth2ClientConfig.ssoFilter(), BasicAuthenticationFilter.class);
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new AuthserverUserDetailsService();
    }
}
