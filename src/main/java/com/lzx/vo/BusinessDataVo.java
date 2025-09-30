package com.lzx.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 工作台今日数据视图对象
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessDataVo implements Serializable {

    /**
     * 新增用户数
     */
    private Long newUserCount;

    /**
     * 订单完成率
     */
    private Double orderCompletionRate;

    /**
     * 营业额
     */
    private BigDecimal turnoverAmount;

    /**
     * 平均客单价
     */
    private BigDecimal averageOrderPrice;

    /**
     * 有效订单数：已完成的订单
     */
     private Long validOrderCount;
}
