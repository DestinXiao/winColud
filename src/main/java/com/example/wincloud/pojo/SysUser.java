package com.example.wincloud.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SysUser implements Serializable {

    private Long id;
    private String username;
    private String password;
    private String email;
    private Integer status;
    private Date createTime;
    private Date updateTime;

}
