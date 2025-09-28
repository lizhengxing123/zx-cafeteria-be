package com.lzx.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishItemVO implements Serializable {

    /**
     * 菜品名称
     */
    private String name;

    /**
     * 份数
     */
    private Integer copies;

    /**
     * 菜品图片
     */
    private String image;

    /**
     * 菜品描述
     */
    private String description;

     /**
     * 菜品价格
     */
    private BigDecimal price;

    /**
     * 菜品状态：1 起售，0 停售
     */
    private Integer status;
}
