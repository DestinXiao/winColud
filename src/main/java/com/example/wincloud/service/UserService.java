package com.example.wincloud.service;

public interface LoginService {

    boolean login(String username, String password);

    void logout();
}
