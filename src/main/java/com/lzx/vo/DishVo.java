package com.lzx.vo;

import com.lzx.entity.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取菜品列表返回的数据模型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DishVo implements Serializable {
    /**
     * 菜品ID
     */
    private Long id;
    /**
     * 菜品名称
     */
    private String name;
    /**
     * 菜品分类 ID，关联 category 表的主键 ID
     */
    private Long categoryId;
    /**
     * 菜品价格
     */
    private Double price;
    /**
     * 菜品图片
     */
    private String image;
    /**
     * 菜品描述
     */
    private String description;
    /**
     * 菜品售卖状态：1 起售，0 停售
     */
    private Integer status;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 菜品分类名称
     */
    private String categoryName;
    /**
     * 菜品口味
     */
    private List<DishFlavor> flavors = new ArrayList<>();
}
