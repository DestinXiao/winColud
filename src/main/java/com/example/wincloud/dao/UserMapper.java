package com.example.wincloud.dao;

import com.example.wincloud.pojo.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {

    // 通过用户名查询用户信息
    @Select(value = "SELECT `id`, `username`, `password`, `status` FROM `sys_user` WHERE `username` = #{username}")
    SysUser getUserByUsername(@Param(value = "username") String username);

    @Select(value = "SELECT `sys_user`.`id`, `sys_user`.`username` FROM `sys_user`")
    List<SysUser> listUser();
}
