package com.lzx.dto;

import com.lzx.entity.SetmealDish;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 新增、更新套餐传递的数据模型
 */
@Data
public class SetmealDto implements Serializable {

    /**
     * 套餐分类 ID，关联 category 表的主键 ID
     */
    private Long categoryId;

    /**
     * 套餐名称
     */
    private String name;

    /**
     * 套餐单价
     */
    private BigDecimal price;

    /**
     * 套餐图片 URL
     */
    private String image;

    /**
     * 套餐描述
     */
    private String description;

    /**
     * 套餐状态：0 停售，1 起售（默认值为 1）
     */
    private Integer status;

     /**
     * 套餐包含的菜品信息
     */
    private List<SetmealDish> setmealDishes = new ArrayList<>();
}
