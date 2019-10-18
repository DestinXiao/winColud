package com.example.wincloud.dao;

import com.example.wincloud.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.annotation.Resource;

@Mapper
@Resource
public interface UserMapper {

    // 通过用户名查询用户信息
    @Select(value = "SELECT `id`, `username`, `password`, `state` FROM `user` WHERE `username` = #{username}")
    User getUserByUsername(@Param(value = "username") String username);
}
