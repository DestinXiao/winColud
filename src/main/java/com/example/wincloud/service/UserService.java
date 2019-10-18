package com.example.wincloud.service;



public interface UserService {

    boolean login(String username, String password);

    void logout();

}
