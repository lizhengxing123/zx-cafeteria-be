package com.lzx.constant;

/**
 * 状态常量，启用或者禁用
 */
public class StatusConstant {

    /**
     * 管理端相关状态值
     */
    //启用
    public static final Integer ENABLE = 1;
    //禁用
    public static final Integer DISABLE = 0;

    /**
     * 地址簿相关状态值
     */
    // 默认地址
    public static final Integer DEFAULT = 1;
    // 非默认地址
    public static final Integer NOT_DEFAULT = 0;

    /**
     * 订单相关状态值
     */
    // 待付款
    public static final Integer WAIT_PAY = 1;
    // 待接单
    public static final Integer WAIT_ACCEPT = 2;
    // 已接单, 待派送
    public static final Integer WAIT_DELIVER = 3;
    // 派送中
    public static final Integer DELIVERING = 4;
    // 已完成
    public static final Integer COMPLETED = 5;
    // 已取消
    public static final Integer CANCELLED = 6;

    /**
     * 支付相关状态值
     */
    // 待支付
    public static final Integer WAIT_PAYMENT = 1;
    // 已支付
    public static final Integer PAID = 2;
    // 已退款
    public static final Integer REFUNDED = 3;
}
