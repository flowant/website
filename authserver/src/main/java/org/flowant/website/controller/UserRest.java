package org.flowant.website.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRest {

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal; //TODO response minimal information to browser
    }

    @RequestMapping("/admin")
    public Principal admin(Principal principal) {
        return principal; //TODO response minimal information to browser
    }
}
