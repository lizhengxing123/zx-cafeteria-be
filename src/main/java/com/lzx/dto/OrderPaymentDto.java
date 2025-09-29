package com.lzx.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单支付 DTO
 */
@Data
public class OrderPaymentDto implements Serializable {
    /**
     * 订单号
     */
    private String orderNumber;

     /**
      * 支付方式
      */
    private Integer payMethod;
}
