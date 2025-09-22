package com.lzx.dto;

import lombok.Getter;

/**
 * 员工登录传递的数据模型
 */
@Getter
public class EmployeeLoginDto {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
