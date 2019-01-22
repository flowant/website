package org.flowant.website.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRest {

//    //TODO response minimal information to browser
//    @RequestMapping("/user")
//    public Map<String, String> user(Principal principal) {
//        Map<String, String> map = new LinkedHashMap<>();
//        map.put("name", principal.getName());
//        return map;
//    }

    //TODO response minimal information to browser
    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }

    @RequestMapping("/admin")
    public Principal admin(Principal principal) {
        return principal;
    }
}
