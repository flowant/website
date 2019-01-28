package org.flowant.frontend.rest;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class UserRest {
    final static String USER = "/user";

    @RequestMapping("/")
    public String email(Principal principal) {
        return principal.getName();
    }

    @RequestMapping("/user")
    public Mono<Principal> user(Principal user) {
      return Mono.just(user);
    }
}
