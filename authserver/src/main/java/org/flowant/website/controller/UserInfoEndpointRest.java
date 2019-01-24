package org.flowant.website.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInfoEndpointRest {
    public static final String ME = "/me";

    @RequestMapping(ME)
    public Principal user(Principal principal) {
        return principal;
    }
}
