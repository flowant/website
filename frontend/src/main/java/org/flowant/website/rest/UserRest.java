package org.flowant.website.rest;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class UserRest {
    public final static String USER = "/user";

    @RequestMapping(USER)
    public Mono<Principal> user(Principal user) {
      return Mono.just(user);
    }
}
