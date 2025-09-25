package com.lzx.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 菜品分页查询传递的数据模型
 */
@Data
public class DishPageQueryDTO extends BasePageQueryDto implements Serializable {

    /**
     * 菜品名称
     */
    private String name;

    /**
     * 菜品分类 ID，关联 category 表的主键 ID
     */
    private Long categoryId;

    /**
     * 菜品售卖状态：1 起售，0 停售
     */
    private Integer status;

}
