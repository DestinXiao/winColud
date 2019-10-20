package com.example.wincloud.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserRole {

    private Long id;
    private Long userId;
    private Long roleId;
    private Date createTime;
    private Date updateTime;
}
