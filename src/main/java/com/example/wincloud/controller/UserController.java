package com.example.wincloud.controller;

import com.example.wincloud.common.RedisHelper;
import com.example.wincloud.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    RedisHelper redisHelper;

    @RequestMapping(value = "/sing_in", method = RequestMethod.GET)
    public String SingIn() {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, Object> login(@RequestParam(value = "username") String username,
                                     @RequestParam(value = "password") String password) {
        Map<String, Object> result = new HashMap<>();

        System.out.println(username + " " + password);
        String token = userService.login(username, password);
        redisHelper.set(username, password);

        result.put("token", token);
        return result;
    }

//    @RequestMapping(value = "/", method = RequestMethod.GET)
//    public String index() {
//        return "index";
//    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @RequiresPermissions(value = "user:view")
    public Map<String, Object> listUsers() {
        Map<String, Object> result = new HashMap<>();

        result.put("users", userService.listUser());
        return result;
    }

    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public String NOTFOUND() {
        return "404";
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String Error() {
        return "403";
    }

    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    @ResponseBody
    public String authorizationException() {
        System.out.println("aa");
        return "403";
    }
}
