package com.lzx.dto;

import lombok.Data;

import java.io.Serializable;

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