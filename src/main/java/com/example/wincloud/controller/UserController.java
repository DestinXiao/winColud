package com.example.wincloud.controller;

import com.example.wincloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/sing_in", method = RequestMethod.GET)
    public String SingIn() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password) {
        System.out.println(username + " " + password);
        boolean login = userService.login(username, password);
        if(login)
            return "index";
        else
            return "login";
    }
}
