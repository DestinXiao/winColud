package com.example.wincloud.dao;

import com.example.wincloud.pojo.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysRolePermissionMapper {

    @Select(value = "SELECT `sys_permission`.`id`, `sys_permission`.`name`, `sys_permission`.`description`, `sys_permission`.`create_time`, `sys_permission`.`update_time` " +
            "FROM `sys_permission`, `sys_role_permission` " +
            "WHERE `sys_role_permission`.`permission_id` = `sys_permission`.`id` " +
            "AND `sys_role_permission`.`role_id` = #{role_id};")
    List<SysPermission> listSysPermissionByRoleId(Long role_id);
}
