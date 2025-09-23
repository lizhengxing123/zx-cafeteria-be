package com.lzx.dto;

import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 新增员工传递的数据模型
 */
@Getter
@ToString
public class EmployeeDto implements Serializable {

    /**
     * 用户名
     */
    private String username;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 身份证号
     */
    private String idNumber;
}
