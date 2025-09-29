package com.lzx.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户下单传递的数据模型
 */
@Data
public class OrderSubmitDto implements Serializable {

    /**
     * 地址薄 ID
     */
    private Long addressBookId;

    /**
     * 总金额
     */
    private Long amount;

    /**
     * 配送状态：1 立即配送， 0 选择具体时间
     */
    private Integer deliveryStatus;

    /**
     * 预计送达时间
     */
    private LocalDateTime estimatedDeliveryTime;

    /**
     * 打包费
     */
    private Long packAmount;

    /**
     * 付款方式：1 微信支付， 0 支付宝支付
     */
    private Integer payMethod;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 餐具数量
     */
    private Integer tablewareCount;

    /**
     * 餐具数量状态：1 按餐量提供， 0 选择具体数量
     */
    private Integer tablewareStatus;
}
