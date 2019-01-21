package org.flowant.website;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

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
                .antMatchers("/user").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/admin").hasAnyAuthority("ADMIN")
                .anyRequest().authenticated().and()
                .oauth2Login().loginPage("/login").and()
                // TODO: change to JWT client side logout
                .logout().logoutSuccessUrl("/").permitAll().and()
                // is required to post to /logout
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
}
