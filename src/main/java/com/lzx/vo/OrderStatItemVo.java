package com.lzx.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 每天的订单统计视图对象
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatItemVo  {
    /**
     * 日期
     */
    private String date;

    /**
     * 有效订单数：已完成的订单
     */
    private Long count;

    /**
     * 订单总数
     */
    private Long totalCount;
}
