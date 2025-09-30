package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.constant.StatusConstant;
import com.lzx.entity.Order;
import com.lzx.entity.User;
import com.lzx.mapper.DishMapper;
import com.lzx.mapper.OrderMapper;
import com.lzx.mapper.SetmealMapper;
import com.lzx.mapper.UserMapper;
import com.lzx.service.WorkspaceService;
import com.lzx.vo.BusinessDataVo;
import com.lzx.vo.DishOverviewVo;
import com.lzx.vo.OrderOverviewVo;
import com.lzx.vo.SetmealOverviewVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 工作台服务实现类
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class WorkspaceServiceImpl implements WorkspaceService {

    private final DishMapper dishMapper;
    private final SetmealMapper setmealMapper;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;

    // 当天开始时间和结束时间
    private final LocalDateTime begin = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
    private final LocalDateTime end = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);

    /**
     * 查询今日数据
     *
     * @return 今日数据
     */
    @Override
    public BusinessDataVo businessData() {
        // 1、查询今日新增用户数
        LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
        userQueryWrapper.between(User::getCreateTime, begin, end);
        Long newUserCount = userMapper.selectCount(userQueryWrapper);

        // 2、查询今日营业额
        BigDecimal turnoverAmount = BigDecimal.ZERO;
        BigDecimal turnover = orderMapper.sumTurnoverByDateRange(begin, end, StatusConstant.COMPLETED);
        if (turnover != null) {
            turnoverAmount = turnover;
        }

        // 总的订单数
        Long allOrderCount = 0L;
        // 3、有效的订单数
        Long validOrderCount = 0L;
        // 查询订单分类统计
        OrderOverviewVo orderOverviewVo = orderMapper.selectCountByStatus(begin, end);
        if (orderOverviewVo != null) {
            allOrderCount = orderOverviewVo.getAllOrderCount();
            validOrderCount = orderOverviewVo.getCompletedOrderCount();
        }

        // 4、计算订单完成率
        Double orderCompletionRate = allOrderCount > 0 ?
                (double) validOrderCount / allOrderCount : 0.0;

        // 5、计算客单价：营业额 / 有效订单数
        BigDecimal averageOrderPrice = validOrderCount > 0 ?
                turnoverAmount.divide(BigDecimal.valueOf(validOrderCount), RoundingMode.CEILING) : BigDecimal.ZERO;

        return BusinessDataVo.builder()
                .newUserCount(newUserCount)
                .turnoverAmount(turnoverAmount)
                .orderCompletionRate(orderCompletionRate)
                .averageOrderPrice(averageOrderPrice)
                .validOrderCount(validOrderCount)
                .build();
    }

    /**
     * 查询订单总览
     *
     * @return 订单总览
     */
    @Override
    public OrderOverviewVo orderOverview() {
        // 查询订单分类统计
        OrderOverviewVo orderOverviewVo = orderMapper.selectCountByStatus(begin, end);
        if (orderOverviewVo == null) {
            orderOverviewVo = new OrderOverviewVo(0L, 0L, 0L, 0L, 0L);
        }
        return orderOverviewVo;
    }

    /**
     * 查询菜品总览
     *
     * @return 菜品总览
     */
    @Override
    public DishOverviewVo dishOverview() {
        DishOverviewVo dishOverviewVo = dishMapper.countDishStatus();
        if (dishOverviewVo == null) {
            dishOverviewVo = new DishOverviewVo(0L, 0L);
        }
        return dishOverviewVo;
    }

    /**
     * 查询套餐总览
     *
     * @return 套餐总览
     */
    @Override
    public SetmealOverviewVo setmealOverview() {
        SetmealOverviewVo setmealOverviewVo = setmealMapper.countSetmealStatus();
        if (setmealOverviewVo == null) {
            setmealOverviewVo = new SetmealOverviewVo(0L, 0L);
        }
        return setmealOverviewVo;
    }
}
