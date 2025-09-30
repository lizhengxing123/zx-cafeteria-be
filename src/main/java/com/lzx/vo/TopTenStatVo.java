package com.lzx.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 销量排名 TOP10 视图对象
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopTenStatVo implements Serializable {
    /**
     * 销量排名 TOP10 菜品、套餐名称
     */
    private String name;

     /**
      * 销量排名 TOP10 销售量
      */
    private Long number;
}
