package com.lzx.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 员工登录成功返回的数据模型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeLoginVo implements Serializable {
    /**
     * 员工ID
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 姓名
     */
    private String name;
    /**
     * token
     */
    private String token;
}
