package com.lzx.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.constant.MessageConstant;
import com.lzx.constant.StatusConstant;
import com.lzx.entity.Order;
import com.lzx.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单定时任务类
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderTask {

    private final OrderMapper orderMapper;

    /**
     * 处理超时订单
     * 每分钟扫描一次，将已经超时的订单状态修改为已取消
     */
    @Scheduled(cron = "10 * * * * ?")
    public void processTimeoutOrder() {
        log.info("定时处理超时订单：{}", LocalDateTime.now());
        // 查询超时订单
        List<Order> orderList = queryTimeoutOrder();
        if (orderList.isEmpty()) {
            log.info("没有超时订单");
            return;
        }
        // 遍历超时订单，修改状态为已取消
        orderList.forEach(order -> {
            // 设置订单状态为已取消
            order.setStatus(StatusConstant.CANCELLED);
            // 设置取消原因
            order.setCancelReason(MessageConstant.TIMEOUT_ORDER_CANCELLED);
            // 设置取消时间
            order.setCancelTime(LocalDateTime.now());
        });
        // 更新订单状态
        orderMapper.updateById(orderList);
        log.info("成功处理{}条超时订单", orderList.size());
    }

    /**
     * 处理一直处于派送中的订单
     * 每天凌晨3点执行，将前一天支付成功但一直处于派送中的订单状态修改为已完成
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void processDeliveringOrder() {
        log.info("定时处理一直处于派送中的订单：{}", LocalDateTime.now());
        // 查询一直处于派送中的订单
        List<Order> orderList = queryDeliveringOrder();
        if (orderList.isEmpty()) {
            log.info("前一天没有支付成功但一直处于派送中的订单");
            return;
        }
        // 遍历一直处于派送中的订单，修改状态为已完成
        orderList.forEach(order -> {
            // 设置订单状态为已完成
            order.setStatus(StatusConstant.COMPLETED);
            // 设置完成时间为当前时间
            order.setDeliveryTime(LocalDateTime.now());
        });
        // 更新订单状态
        orderMapper.updateById(orderList);
        log.info("成功处理{}条一直处于派送中的订单", orderList.size());
    }

    // ---------------------------私有方法---------------------------

    /**
     * 查询超时订单
     * 超时时间为15分钟且支付状态为待支付的订单
     */
    private List<Order> queryTimeoutOrder() {
        log.info("查询超时订单");
        // 15分钟前的时间
        LocalDateTime timeoutTime = LocalDateTime.now().minusMinutes(15);
        // 构建查询条件
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getStatus, StatusConstant.WAIT_PAYMENT)
                .le(Order::getOrderTime, timeoutTime);
        // 查询超时订单
        return orderMapper.selectList(
                new LambdaQueryWrapper<Order>()
                        .eq(Order::getStatus, StatusConstant.WAIT_PAY)
                        .le(Order::getOrderTime, timeoutTime)
        );
    }

    /**
     * 查询前一天支付成功但一直处于派送中的订单
     * 支付状态为已支付且订单状态为派送中的订单
     */
    private List<Order> queryDeliveringOrder() {
        log.info("查询上一个工作日支付成功但一直处于派送中的订单");
        // 获取前一天23:59:59
        LocalDateTime deliveryTime = LocalDateTime.now().minusDays(1).withHour(23).withMinute(59).withSecond(59);
        // 构建查询条件
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getStatus, StatusConstant.DELIVERING)
                .eq(Order::getPayStatus, StatusConstant.PAID)
                .le(Order::getDeliveryTime, deliveryTime);
        // 查询上一个工作日支付成功但一直处于派送中的订单
        return orderMapper.selectList(queryWrapper);
    }
}
