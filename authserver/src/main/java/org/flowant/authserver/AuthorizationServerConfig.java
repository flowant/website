package org.flowant.authserver;

import java.security.KeyPair;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Import(JwkSetEndpointConfiguration.class)
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    PasswordEncoder passwordEncoder;

    AuthenticationManager authenticationManager;
    static KeyPair keyPair = loadKeyPair();

    public AuthorizationServerConfig(AuthenticationConfiguration authenticationConfiguration) throws Exception {

        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    static KeyPair loadKeyPair() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                new ClassPathResource("keystore/keystore.jks"), "spassword".toCharArray());
        return keyPair = keyStoreKeyFactory.getKeyPair("website_keystore");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient("client").secret(passwordEncoder.encode("client"))
                .authorizedGrantTypes("password", "authorization_code", "refresh_token", "client_credentials")
                .scopes("read", "write", ".*")
                .autoApprove(true)
                .accessTokenValiditySeconds(3600)
                .redirectUris("http://localhost:9093/");
//
//                .and().withClient("fooClientIdPassword").secret(passwordEncoder().encode("secret")).authorizedGrantTypes("password", "authorization_code", "refresh_token", "client_credentials").scopes("foo", "read", "write").accessTokenValiditySeconds(3600)
//                // 1 hour
//                .refreshTokenValiditySeconds(2592000)
//                // 30 days
//                .redirectUris("xxx","http://localhost:8089/","http://localhost:8080/login/oauth2/code/custom","http://localhost:8080/ui-thymeleaf/login/oauth2/code/custom", "http://localhost:8080/authorize/oauth2/code/bael", "http://localhost:8080/login/oauth2/code/bael")
//
//                .and().withClient("barClientIdPassword").secret(passwordEncoder().encode("secret")).authorizedGrantTypes("password", "authorization_code", "refresh_token").scopes("bar", "read", "write").accessTokenValiditySeconds(3600)
//                // 1 hour
//                .refreshTokenValiditySeconds(2592000) // 30 days
//
//                .and().withClient("testImplicitClientId").authorizedGrantTypes("implicit").scopes("read", "write", "foo", "bar").autoApprove(true).redirectUris("xxx");

    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
//        endpoints.authenticationManager(authenticationManager)
//                .accessTokenConverter(accessTokenConverter())
//                .tokenStore(tokenStore());;

        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
        endpoints.tokenStore(tokenStore()).tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager);
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair);
        return converter;
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

}
