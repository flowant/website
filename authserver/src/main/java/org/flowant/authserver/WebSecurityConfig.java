package org.flowant.authserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${dev.user}")
    String user;
    @Value("${dev.userPassword}")
    String userPassword;

    @Value("${dev.admin}")
    String admin;
    @Value("${dev.adminPassword}")
    String adminPassword;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
            User.builder().passwordEncoder(passwordEncoder::encode).username(user)
                .password(userPassword)
                .roles("USER")
                .build(),
            User.builder().passwordEncoder(passwordEncoder::encode).username(admin)
                .password(adminPassword)
                .roles("ADMIN")
                .build());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers(
                        // sign in and up
                        "/", "/login**", "/error**",
                        // OAuth2 server public key
                        "/.well-known/jwks.json",
                        // debug
                        "/actuator/**", "/oauth/**")
                    .permitAll()
                .anyRequest().authenticated().and()
                .oauth2Login().loginPage("/login").and()
                // TODO: change to JWT client side logout
                .logout().logoutSuccessUrl("/").permitAll().and()
                // is required to post to /logout
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
}
