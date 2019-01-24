package org.flowant.website.controller;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

@Controller
public class JwkSetRest {
    public static final String JWK_SET_URI = "/.well-known/jwks.json";
    @Autowired
    KeyPair keyPair;

    Map<String, Object> jwkSet;

    @PostConstruct
    public void setJwkSet() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        jwkSet = new JWKSet(key).toJSONObject();
    }

    @GetMapping(JWK_SET_URI)
    @ResponseBody
    public Map<String, Object> getKey() {
        return jwkSet;
    }
}
