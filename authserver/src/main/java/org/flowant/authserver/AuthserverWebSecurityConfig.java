package org.flowant.authserver;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@EnableWebSecurity
@Order(-100)
public class AuthserverWebSecurityConfig extends WebSecurityConfigurerAdapter {

    PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder setPasswordEncoder() {
        return passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        setPasswordEncoder();
        return new InMemoryUserDetailsManager(
            User.builder().passwordEncoder(passwordEncoder::encode).username("user")
                .password("user")
                .roles("USER")
                .build());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**").authorizeRequests().antMatchers("/**").permitAll()
                .anyRequest().authenticated().and();
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and();
    }
}
