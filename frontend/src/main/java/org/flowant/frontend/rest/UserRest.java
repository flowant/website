package org.flowant.frontend.rest;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class UserRest {
    final static String USER = "/user";

//    @RequestMapping("/user")
//    public Mono<Map<String, Object>> user(Principal user) {
//        Map<String, Object> map = new LinkedHashMap<String, Object>();
//        map.put("name", user.getName());
//        map.put("roles", AuthorityUtils.authorityListToSet(((Authentication) user).getAuthorities()));
//        return Mono.just(map);
//    }

    @GetMapping(value = "/{path:[^\\.]*}")
    public Mono<String> redirect() {
        return Mono.just("forward:/");
    }

    @RequestMapping("/user")
    public Mono<Map<String, String>> user(Principal user) {
      return Mono.just(Collections.singletonMap("name", user.getName()));
    }
}
