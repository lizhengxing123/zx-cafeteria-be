package com.lzx.dto;

import com.lzx.entity.DishFlavor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 新增、更新菜品传递的数据模型
 */
@Data
public class DishDto implements Serializable {
    /**
     * 菜品名称
     */
    private String name;
    /**
     * 菜品分类 ID，关联 category 表的主键 ID
     */
    private Long categoryId;
    /**
     * 菜品单价
     */
    private BigDecimal price;
    /**
     * 菜品图片 URL
     */
    private String image;
    /**
     * 菜品描述
     */
    private String description;
    /**
     * 口味
     */
    private List<DishFlavor> flavors = new ArrayList<>();
}
