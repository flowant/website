package org.flowant.authserver;

import java.security.KeyPair;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Configuration
@ConfigurationProperties(prefix = "oauth2-server")
@Getter @Setter @ToString
@Log4j2
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    String clientId;
    String clientSecret;
    List<String> authorizedGrantTypes;
    List<String> scopes;
    boolean autoApprove;
    int accessTokenValiditySeconds;
    String redirectUris;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    KeyPair keyPair;

    AuthenticationManager authenticationManager;

    public AuthorizationServerConfig(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        log.info(this::toString);
        clients.inMemory().withClient(clientId).secret(passwordEncoder.encode(clientSecret))
                .authorizedGrantTypes(authorizedGrantTypes.toArray(new String[authorizedGrantTypes.size()]))
                .scopes(scopes.toArray(new String[scopes.size()]))
                .autoApprove(true)
                .accessTokenValiditySeconds(accessTokenValiditySeconds)
                .redirectUris(redirectUris);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair);

        endpoints.authenticationManager(authenticationManager)
                .accessTokenConverter(converter)
                .tokenStore(new JwtTokenStore(converter));
    }

}
