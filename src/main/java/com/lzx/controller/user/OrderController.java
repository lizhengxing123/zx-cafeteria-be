package com.lzx.controller.user;

import com.lzx.constant.MessageConstant;
import com.lzx.dto.OrderPaymentDto;
import com.lzx.dto.OrderSubmitDto;
import com.lzx.result.Result;
import com.lzx.service.OrderService;
import com.lzx.vo.OrderSubmitVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [用户端] 订单管理
 */
@Slf4j
@RestController("userOrderController")
@RequestMapping("/user/orders")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderController {

    private final OrderService orderService;

    /**
     * 提交订单
     *
     * @param orderSubmitDto 订单提交数据传输对象
     * @return 订单提交成功返回的视图对象
     */
    @PostMapping("/submit")
    public Result<OrderSubmitVo> submit(@RequestBody OrderSubmitDto orderSubmitDto) {
        log.info("用户下单：{}", orderSubmitDto);
        OrderSubmitVo orderSubmitVo = orderService.submitOrder(orderSubmitDto);
        return Result.success(MessageConstant.QUERY_SUCCESS, orderSubmitVo);
    }

    /**
     * 支付订单
     *
     * @param orderPaymentDto 订单支付数据传输对象
     * @return 支付成功返回的信息
     */
    @PostMapping("/payment")
    public Result<String> payment(@RequestBody OrderPaymentDto orderPaymentDto) {
        log.info("用户支付订单：{}", orderPaymentDto);
        orderService.payment(orderPaymentDto);
        return Result.success(MessageConstant.PAYMENT_SUCCESS);
    }
}
