package com.example.wincloud.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SysRole {

    private Long id;
    private String name;
    private String description;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
