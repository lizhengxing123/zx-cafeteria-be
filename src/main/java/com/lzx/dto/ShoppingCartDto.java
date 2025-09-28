package com.lzx.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 新增、修改购物车传递的数据模型
 */
@Data
public class ShoppingCartDto implements Serializable {

    /**
     * 菜品 ID
     */
    private Long dishId;
    /**
     * 套餐 ID
     */
    private Long setmealId;
    /**
     * 菜品口味
     */
    private String dishFlavor;
}
