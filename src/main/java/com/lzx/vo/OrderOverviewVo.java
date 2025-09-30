package com.lzx.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 工作台订单概览视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderOverviewVo implements Serializable {
    /**
     * 待接单数量
     */
    private Long waitingOrderCount;

    /**
     * 待派送数量
     */
    private Long deliveredOrderCount;

    /**
     * 已完成数量
     */
    private Long completedOrderCount;

    /**
     * 已取消数量
     */
    private Long cancelledOrderCount;

    /**
     * 全部订单数量
     */
    private Long allOrderCount;
}
