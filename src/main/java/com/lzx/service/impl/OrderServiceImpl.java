package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.constant.MessageConstant;
import com.lzx.constant.StatusConstant;
import com.lzx.dto.OrderPaymentDto;
import com.lzx.dto.OrderSubmitDto;
import com.lzx.entity.AddressBook;
import com.lzx.entity.Order;
import com.lzx.entity.OrderDetail;
import com.lzx.entity.ShoppingCart;
import com.lzx.exception.OrderBusinessException;
import com.lzx.mapper.AddressBookMapper;
import com.lzx.mapper.OrderDetailMapper;
import com.lzx.mapper.OrderMapper;
import com.lzx.mapper.ShoppingCartMapper;
import com.lzx.service.OrderService;
import com.lzx.utils.SecurityUtil;
import com.lzx.vo.OrderSubmitVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final AddressBookMapper addressBookMapper;
    private final ShoppingCartMapper shoppingCartMapper;


    /**
     * 提交订单
     *
     * @param orderSubmitDto 订单提交数据传输对象
     * @return 订单提交成功返回的视图对象
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderSubmitVo submitOrder(OrderSubmitDto orderSubmitDto) {
        // 1、处理业务异常
        // 校验地址簿是否存在
        AddressBook addressBook = checkAddressBookExists(orderSubmitDto.getAddressBookId());
        // 校验当前用户购物车是否为空
        List<ShoppingCart> shoppingCartList = checkCartIsEmpty();

        // 2、新增一条订单
        // 生成订单详情
        Order order = generateOrderDetail(orderSubmitDto, addressBook, shoppingCartList);
        orderMapper.insert(order);

        // 3、新增n条订单明细
        // 生成订单明细列表
        List<OrderDetail> orderDetailList = generateOrderDetailList(shoppingCartList, order);
        orderDetailMapper.insert(orderDetailList);

        // 4、清空当前用户购物车
        shoppingCartMapper.delete(
                new LambdaQueryWrapper<ShoppingCart>()
                        .eq(ShoppingCart::getUserId, SecurityUtil.getCurrentUserId())
        );

        // 5、返回订单提交成功视图对象
        return OrderSubmitVo.builder()
                .orderId(order.getId())
                .orderNumber(order.getNumber())
                .orderAmount(order.getAmount())
                .orderTime(order.getOrderTime())
                .build();
    }

    /**
     * 支付订单
     *
     * @param orderPaymentDto 订单支付数据传输对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payment(OrderPaymentDto orderPaymentDto) {
        // 1、处理业务异常
        // 校验订单是否存在
        Order order = checkOrderExists(orderPaymentDto.getOrderNumber());
        // 校验支付状态是否为待支付
        checkPayStatus(order);

        // 2、更新订单支付状态：已付款
        order.setPayStatus(StatusConstant.PAID);
        // 3、更新订单状态：待接单
        order.setStatus(StatusConstant.WAIT_ACCEPT);
        orderMapper.updateById(order);
    }

    // ------------------------------ 私有工具方法 ------------------------------

    /**
     * 校验地址簿是否存在
     *
     * @param addressBookId 地址簿 ID
     * @return 地址簿实体对象
     */
    private AddressBook checkAddressBookExists(Long addressBookId) {
        AddressBook addressBook = addressBookMapper.selectById(addressBookId);
        if (addressBook == null) {
            throw new OrderBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        return addressBook;
    }

    /**
     * 校验当前用户购物车是否为空
     */
    private List<ShoppingCart> checkCartIsEmpty() {
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectList(
                new LambdaQueryWrapper<ShoppingCart>()
                        .in(ShoppingCart::getUserId, SecurityUtil.getCurrentUserId())
        );
        if (shoppingCartList.isEmpty()) {
            throw new OrderBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }
        return shoppingCartList;
    }

    /**
     * 校验订单是否存在
     *
     * @param orderNumber 订单号
     * @return 订单实体对象
     */
    private Order checkOrderExists(String orderNumber) {
        Order order = orderMapper.selectOne(
                new LambdaQueryWrapper<Order>()
                        .eq(Order::getNumber, orderNumber)
                        .eq(Order::getUserId, SecurityUtil.getCurrentUserId())
        );
        if (order == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        return order;
    }

    /**
     * 校验支付状态是否为待支付
     *
     * @param order 订单实体对象
     */
    private void checkPayStatus(Order order) {
        if (!Objects.equals(order.getPayStatus(), StatusConstant.WAIT_PAYMENT)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
    }

    /**
     * 生成订单详情
     *
     * @param orderSubmitDto   用户下单传递的数据模型
     * @param addressBook      地址簿实体对象
     * @param shoppingCartList 购物车实体对象列表
     * @return 订单实体对象
     */
    private Order generateOrderDetail(OrderSubmitDto orderSubmitDto, AddressBook addressBook, List<ShoppingCart> shoppingCartList) {
        Order order = new Order();
        // 先拷贝已有属性
        BeanUtils.copyProperties(orderSubmitDto, order);
        // 设置下单用户 ID
        order.setUserId(SecurityUtil.getCurrentUserId());
        // 设置下单时间
        order.setOrderTime(LocalDateTime.now());
        // 设置支付状态：待支付
        order.setPayStatus(StatusConstant.WAIT_PAYMENT);
        // 设置订单状态：待付款
        order.setStatus(StatusConstant.WAIT_PAY);
        // 设置订单号：当前时间戳字符串
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        // 设置手机号
        order.setPhone(addressBook.getPhone());
        // 设置收货人
        order.setConsignee(addressBook.getConsignee());
        // 设置详细地址
        order.setAddress(addressBook.getDetail());
        // 计算订单总金额：购物车中所有商品金额总和 + 打包费 + 配送费
        BigDecimal orderAmount = shoppingCartList.stream()
                .map(shoppingCart -> shoppingCart.getAmount().multiply(BigDecimal.valueOf(shoppingCart.getNumber())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // 增加打包费
        orderAmount = orderAmount.add(BigDecimal.valueOf(order.getPackAmount()));
        // 增加配送费：默认3元
        orderAmount = orderAmount.add(BigDecimal.valueOf(3));
        // 设置订单总金额
        order.setAmount(orderAmount);

        return order;
    }

    /**
     * 生成n条订单明细
     *
     * @param shoppingCartList 购物车实体对象列表
     * @param order            订单实体对象
     * @return 订单明细实体对象列表
     */
    private List<OrderDetail> generateOrderDetailList(List<ShoppingCart> shoppingCartList, Order order) {
        return shoppingCartList.stream()
                .map(shoppingCart -> {
                    OrderDetail orderDetail = new OrderDetail();
                    BeanUtils.copyProperties(shoppingCart, orderDetail);
                    orderDetail.setOrderId(order.getId());
                    return orderDetail;
                })
                .collect(Collectors.toList());
    }
}
