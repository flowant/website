package org.flowant.authserver;

import static org.springframework.util.ResourceUtils.getFile;

import java.io.FileNotFoundException;
import java.security.KeyPair;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Configuration
@ConfigurationProperties(prefix = "keystore")
@Getter @Setter @ToString
public class KeyStoreConfig {

    String path;
    String alias;
    String password;

    KeyStoreKeyFactory keyStoreKeyFactory;

    @Bean
    KeyPair exposeKeyPair() throws FileNotFoundException {
        keyStoreKeyFactory = new KeyStoreKeyFactory(new FileSystemResource(getFile(path)),
                password.toCharArray());
        return keyStoreKeyFactory.getKeyPair(alias);
    }
}
