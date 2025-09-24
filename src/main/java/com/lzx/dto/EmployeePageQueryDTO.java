package com.lzx.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 员工分页查询传递的数据模型
 */
@Data
public class EmployeePageQueryDTO extends BasePageQueryDto implements Serializable {

    /**
     * 员工姓名
     */
    private String name;

    @Override
    public String toString() {
        return "EmployeePageQueryDTO{" +
                "name='" + name + '\'' +
                ", " + super.toString() +
                "} ";
    }

}