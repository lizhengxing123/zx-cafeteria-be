package com.lzx.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * 新增、更新分类数传递的数据模型
 */
@Data
public class CategoryDto implements Serializable {
    /**
     * 分类名称
     */
    private String name;
    /**
     * 分类类型：1 菜品分类，2 套餐分类
     */
    private Integer type;
    /**
     * 分类排序
     */
    private Integer sort;
}
