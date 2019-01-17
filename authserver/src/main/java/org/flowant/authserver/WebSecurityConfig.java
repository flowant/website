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
        http.requestMatchers()
                .antMatchers("/**", "/oauth/**", "/.well-known/jwks.json")
                .and()
            .authorizeRequests()
                .antMatchers("/user").hasRole("USER")
                .and()
            .authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN")
                .and()
            .authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                .and()
            .authorizeRequests()
                .antMatchers("/.well-known/jwks.json").permitAll()
                .and();
    }
}
