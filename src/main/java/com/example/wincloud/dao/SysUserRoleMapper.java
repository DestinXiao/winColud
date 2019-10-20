package com.example.wincloud.dao;

import com.example.wincloud.pojo.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserRoleMapper {

    @Select(value = "SELECT `sys_role`.`id`, `sys_role`.`name`, `sys_role`.`description`, `sys_role`.`status`, `sys_role`.`create_time`, `sys_role`.`update_time` " +
            "FROM `sys_role`, `sys_user_role` " +
            "WHERE `sys_user_role`.`role_id` = `sys_role`.`id` " +
            "AND `sys_user_role`.`user_id` = #{user_id};")
    List<SysRole> listRoleByUserId(Long user_id);
}
