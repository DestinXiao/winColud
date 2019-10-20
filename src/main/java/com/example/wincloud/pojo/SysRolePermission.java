package com.example.wincloud.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRolePermission {

    private Long id;
    private Long roleId;
    private Long permissionId;
    private Date createTime;
    private Date updateTime;
}
