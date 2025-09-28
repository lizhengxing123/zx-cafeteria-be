package com.lzx.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 套餐分页查询传递的数据模型
 */
@Data
public class SetmealPageQueryDTO extends BasePageQueryDto implements Serializable {

    /**
     * 套餐名称
     */
    private String name;

    /**
     * 套餐分类 ID，关联 category 表的主键 ID
     */
    private Long categoryId;

    /**
     * 套餐售卖状态：1 起售，0 停售
     */
    private Integer status;

}
