package com.lzx.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 员工登录传递的数据模型
 */
@Data
public class EmployeeLoginDto implements Serializable {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 加密后的AES密钥
     */
    private String aesKey;
}
