package com.lzx.service;

import com.lzx.dto.OrderPaymentDto;
import com.lzx.dto.OrderSubmitDto;
import com.lzx.vo.OrderSubmitVo;

/**
 * 订单服务接口
 */
public interface OrderService {
    /**
     * 提交订单
     *
     * @param orderSubmitDto 订单提交数据传输对象
     * @return 订单提交成功返回的视图对象
     */
    OrderSubmitVo submitOrder(OrderSubmitDto orderSubmitDto);

    /**
     * 支付订单
     *
     * @param orderPaymentDto 订单支付数据传输对象
     */
    void payment(OrderPaymentDto orderPaymentDto);
}
