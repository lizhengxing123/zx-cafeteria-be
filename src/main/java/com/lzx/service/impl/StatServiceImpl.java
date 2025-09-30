package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.constant.StatusConstant;
import com.lzx.entity.Order;
import com.lzx.entity.User;
import com.lzx.mapper.OrderMapper;
import com.lzx.mapper.UserMapper;
import com.lzx.service.StatService;
import com.lzx.vo.OrderStatItemVo;
import com.lzx.vo.OrderStatVo;
import com.lzx.vo.TurnoverStatVo;
import com.lzx.vo.UserStatVo;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.MapKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统计分析服务实现类
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class StatServiceImpl implements StatService {

    private final OrderMapper orderMapper;
    private final UserMapper userMapper;


    /**
     * 营业额统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 营业额统计结果
     */
    @Override
    public List<TurnoverStatVo> turnoverStat(LocalDate begin, LocalDate end) {
        // 根据开始日期和结束日期查询每天已完成的订单，并计算每天总的营业额
        List<TurnoverStatVo> turnoverStatVoList = new ArrayList<TurnoverStatVo>();

        // 1、计算日期范围内的所有日期
        List<LocalDate> dateRange = getDateRange(begin, end);

        // 2、 使用数据库聚合查询，直接计算每天的营业额
        Map<String, Map<String, Object>> turnoverMap = orderMapper.selectTurnoverByDateRange(begin, end, StatusConstant.COMPLETED);

        // 3、准备返回结果
        for (LocalDate date : dateRange) {
            String dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
            BigDecimal amount = BigDecimal.ZERO;
            if (turnoverMap.containsKey(dateStr)) {
                amount = (BigDecimal) turnoverMap.get(dateStr).getOrDefault("totalAmount", BigDecimal.ZERO);
            }
            turnoverStatVoList.add(TurnoverStatVo.builder()
                    .date(dateStr)
                    .amount(amount)
                    .build());
        }

        // 返回统计结果
        return turnoverStatVoList;
    }

    /**
     * 用户统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 用户统计结果
     */
    public List<UserStatVo> userStat(LocalDate begin, LocalDate end) {
        // 根据开始日期和结束日期查询每天新增用户数和总用户数
        List<UserStatVo> userStatVoList = new ArrayList<UserStatVo>();

        // 1、计算日期范围内的所有日期
        List<LocalDate> dateRange = getDateRange(begin, end);
        // 2、计算每天新增的用户数
        Map<String, Map<String, Object>> newUserCountMap = userMapper.selectCountByDateRange(
                begin.atStartOfDay(), end.plusDays(1).atStartOfDay().minusSeconds(1));
        // 3、计算开始时间之前的总人数
        Long totalUserCount = userMapper.selectCount(
                new LambdaQueryWrapper<User>().lt(User::getCreateTime, begin.atStartOfDay())
        );

        // 4、遍历日期范围，计算每天的总用户数
        for (LocalDate date : dateRange) {
            String dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
            Long newCount = 0L;
            if (newUserCountMap.containsKey(dateStr)) {
                newCount = (Long) newUserCountMap.get(dateStr).getOrDefault("newCount", 0);
            }
            totalUserCount += newCount;

            // 构建UserStatVo对象
            userStatVoList.add(UserStatVo.builder()
                    .date(dateStr)
                    .newCount(newCount)
                    .totalCount(totalUserCount)
                    .build());
        }

        return userStatVoList;
    }

    /**
     * 订单统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 订单统计结果
     */
    @Override
    public List<OrderStatVo> orderStat(LocalDate begin, LocalDate end) {
        // 根据开始日期和结束日期查询每天订单数和日期区间的订单数
        List<OrderStatVo> orderStatVoList = new ArrayList<OrderStatVo>();

        // 1、计算日期范围内的所有日期
        List<LocalDate> dateRange = getDateRange(begin, end);

        // 2、使用一条SQL查询每天的订单总数和有效订单数
        Map<String, Map<String, Object>> orderCountMap = orderMapper.selectCountByDateRange(begin, end, StatusConstant.COMPLETED);

        // 3、计算整个日期区间的订单总数和有效订单数
        long totalCount = 0L;
        long completedCount = 0L;
        List<OrderStatItemVo> records = new ArrayList<>();

        for (LocalDate date : dateRange) {
            String dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
            long dayTotalCount = 0L;
            long dayCompletedCount = 0L;

            if (orderCountMap.containsKey(dateStr)) {
                Map<String, Object> countMap = orderCountMap.get(dateStr);
                dayTotalCount = (Long) countMap.getOrDefault("totalCount", 0L);
                // statusCount 是 BigDecimal 类型，需要转换为 Long
                dayCompletedCount = ((BigDecimal) countMap.getOrDefault("statusCount", BigDecimal.ZERO)).longValue();
            }

            totalCount += dayTotalCount;
            completedCount += dayCompletedCount;

            // 添加当天订单统计
            records.add(OrderStatItemVo.builder()
                    .date(dateStr)
                    .count(dayCompletedCount)
                    .totalCount(dayTotalCount)
                    .build());
        }

        // 4、计算订单完成率，保留两位小数
        double rate = 0.0;
        if (totalCount > 0) {
            rate = Math.round(((double) completedCount / totalCount) * 100) / 100.00;
        }

        // 5、构建并返回订单统计结果
        OrderStatVo orderStatVo = OrderStatVo.builder()
                .count(completedCount)
                .totalCount(totalCount)
                .rate(rate)
                .records(records)
                .build();

        orderStatVoList.add(orderStatVo);
        return orderStatVoList;
    }


    // ------------------------私有方法------------------------

    /**
     * 获取日期范围内的所有日期
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 日期范围内的所有日期
     */
    private List<LocalDate> getDateRange(LocalDate begin, LocalDate end) {
        List<LocalDate> dateRange = new ArrayList<>();
        LocalDate current = begin;
        // end 不是在当前日期之后，说明已经遍历完所有日期（相同日期也会进一次循环）
        while (!current.isAfter(end)) {
            dateRange.add(current);
            current = current.plusDays(1);
        }
        return dateRange;
    }
}
