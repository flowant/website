package org.flowant.gateway.rest;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class UserRest {
    final static String USER = "/user";

    @RequestMapping("/user")
    public Mono<Map<String, Object>> user(Principal user) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("name", user.getName());
        map.put("roles", AuthorityUtils.authorityListToSet(((Authentication) user).getAuthorities()));
        return Mono.just(map);
    }
}
