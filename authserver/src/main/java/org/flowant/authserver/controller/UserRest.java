package org.flowant.authserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserRest {
    @RequestMapping("/user")
    public String user() {
        return "hello user";
    }

    @RequestMapping("/admin")
    public String admin() {
        return "hello admin";
    }
}
