package com.lzx.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 日期区间的订单统计视图对象
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatVo implements Serializable {
    /**
     * 有效订单数：已完成的订单
     */
    private Long count;

    /**
     * 订单总数
     */
    private Long totalCount;

    /**
     * 订单完成率
     */
    private Double rate;

    /**
     * 每天的订单详情
     */
    private List<OrderStatItemVo> records;
}


