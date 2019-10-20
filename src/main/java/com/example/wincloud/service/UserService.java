package com.example.wincloud.service;


import com.example.wincloud.pojo.SysUser;

import java.util.List;

public interface UserService {

    String login(String username, String password);

    void logout();

    List<SysUser> listUser();

}
