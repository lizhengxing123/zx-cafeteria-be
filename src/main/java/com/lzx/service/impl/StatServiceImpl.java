package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.constant.MessageConstant;
import com.lzx.constant.StatusConstant;
import com.lzx.entity.User;
import com.lzx.exception.BaseException;
import com.lzx.mapper.OrderDetailMapper;
import com.lzx.mapper.OrderMapper;
import com.lzx.mapper.UserMapper;
import com.lzx.service.StatService;
import com.lzx.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 统计分析服务实现类
 * <p>
 * 主要提供：
 * - 营业额统计
 * - 用户统计
 * - 订单统计
 * - 销量排名统计
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class StatServiceImpl implements StatService {

    // 常量定义 - 消除魔法值
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final String TOTAL_AMOUNT = "totalAmount";
    private static final String NEW_COUNT = "newCount";
    private static final String TOTAL_COUNT = "totalCount";
    private static final String STATUS_COUNT = "statusCount";

    // Mapper注入
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;
    private final OrderDetailMapper orderDetailMapper;

    /**
     * 营业额统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 按天统计的营业额列表
     */
    @Override
    public List<TurnoverStatVo> turnoverStat(LocalDate begin, LocalDate end) {
        validateDateRange(begin, end);

        // 获取日期范围内的所有日期
        List<LocalDate> dateRange = getDateRange(begin, end);

        // 查询指定日期范围内已完成订单的营业额
        Map<String, Map<String, Object>> turnoverMap =
                orderMapper.selectTurnoverByDateRange(begin, end, StatusConstant.COMPLETED);

        // 将查询结果转换为Vo对象
        return dateRange.stream().map(date -> {
            String dateStr = formatDate(date);
            BigDecimal amount = getBigDecimalValue(turnoverMap.get(dateStr), TOTAL_AMOUNT);

            return TurnoverStatVo.builder()
                    .date(dateStr)
                    .amount(amount)
                    .build();
        }).toList();
    }

    /**
     * 用户统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 按天统计的用户数据
     */
    @Override
    public List<UserStatVo> userStat(LocalDate begin, LocalDate end) {
        validateDateRange(begin, end);

        // 获取日期范围内的所有日期
        List<LocalDate> dateRange = getDateRange(begin, end);

        // 转换日期为时间范围
        LocalDateTime start = getStartOfDay(begin);
        LocalDateTime endTime = getEndOfDay(end);

        // 查询指定日期范围内的新用户数量
        Map<String, Map<String, Object>> newUserCountMap =
                userMapper.selectCountByDateRange(start, endTime);

        // 初始总用户数（开始日期之前注册的用户）
        Long totalUserCount = userMapper.selectCount(
                new LambdaQueryWrapper<User>().lt(User::getCreateTime, start)
        );

        // 计算每日用户数据
        List<UserStatVo> result = new ArrayList<>(dateRange.size());
        for (LocalDate date : dateRange) {
            String dateStr = formatDate(date);
            Long newCount = getLongValue(newUserCountMap.get(dateStr), NEW_COUNT);
            totalUserCount += newCount;

            result.add(UserStatVo.builder()
                    .date(dateStr)
                    .newCount(newCount)
                    .totalCount(totalUserCount)
                    .build());
        }
        return result;
    }

    /**
     * 订单统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 订单统计数据
     */
    @Override
    public List<OrderStatVo> orderStat(LocalDate begin, LocalDate end) {
        validateDateRange(begin, end);

        // 获取日期范围内的所有日期
        List<LocalDate> dateRange = getDateRange(begin, end);

        // 查询指定日期范围内的订单数量
        Map<String, Map<String, Object>> orderCountMap =
                orderMapper.selectCountByDateRange(begin, end, StatusConstant.COMPLETED);

        long totalCount = 0L;
        long completedCount = 0L;
        List<OrderStatItemVo> records = new ArrayList<>(dateRange.size());

        // 计算每日订单数据
        for (LocalDate date : dateRange) {
            String dateStr = formatDate(date);
            Map<String, Object> countMap = orderCountMap.get(dateStr);

            long dayTotal = getLongValue(countMap, TOTAL_COUNT);
            long dayCompleted = getLongValue(countMap, STATUS_COUNT);

            totalCount += dayTotal;
            completedCount += dayCompleted;

            records.add(OrderStatItemVo.builder()
                    .date(dateStr)
                    .count(dayCompleted)
                    .totalCount(dayTotal)
                    .build());
        }

        // 计算订单完成率（使用BigDecimal提高精度）
        double rate = calculateCompletionRate(totalCount, completedCount);

        return List.of(OrderStatVo.builder()
                .count(completedCount)
                .totalCount(totalCount)
                .rate(rate)
                .records(records)
                .build());
    }

    /**
     * 销量排名 TOP10
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 销量排名前十的商品
     */
    @Override
    public List<TopTenStatVo> topTenStat(LocalDate begin, LocalDate end) {
        validateDateRange(begin, end);

        // 转换日期为时间范围
        LocalDateTime start = getStartOfDay(begin);
        LocalDateTime endTime = getEndOfDay(end);

        // 查询销量排名前十的商品
        return orderDetailMapper.selectTopTenByDateRange(start, endTime, StatusConstant.COMPLETED);
    }

    // ----------------------- 私有方法 -----------------------

    /**
     * 校验日期范围的有效性
     *
     * @param begin 开始日期
     * @param end   结束日期
     */
    private void validateDateRange(LocalDate begin, LocalDate end) {
        if (begin == null || end == null || begin.isAfter(end)) {
            throw new BaseException(MessageConstant.INVALID_DATE_RANGE);
        }
    }

    /**
     * 获取日期范围内的所有日期
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 日期列表
     */
    private List<LocalDate> getDateRange(LocalDate begin, LocalDate end) {
        List<LocalDate> dateRange = new ArrayList<>((int) (end.toEpochDay() - begin.toEpochDay() + 1));
        for (LocalDate current = begin; !current.isAfter(end); current = current.plusDays(1)) {
            dateRange.add(current);
        }
        return dateRange;
    }

    /**
     * 格式化日期
     *
     * @param date 日期
     * @return 格式化后的日期字符串
     */
    private String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    /**
     * 获取一天的开始时间
     *
     * @param date 日期
     * @return 一天的开始时间 (00:00:00)
     */
    private LocalDateTime getStartOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    /**
     * 获取一天的结束时间
     *
     * @param date 日期
     * @return 一天的结束时间 (23:59:59)
     */
    private LocalDateTime getEndOfDay(LocalDate date) {
        return date.plusDays(1).atStartOfDay().minusSeconds(1);
    }

    /**
     * 安全获取Map中的Long值
     *
     * @param map Map对象
     * @param key 键
     * @return Long值（如果不存在或转换失败则返回0L）
     */
    private Long getLongValue(Map<String, Object> map, String key) {
        if (map == null || !map.containsKey(key)) {
            return 0L;
        }
        Object value = map.get(key);
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).longValue();
        } else if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        return 0L;
    }

    /**
     * 安全获取Map中的BigDecimal值
     *
     * @param map Map对象
     * @param key 键
     * @return BigDecimal值（如果不存在或转换失败则返回BigDecimal.ZERO）
     */
    private BigDecimal getBigDecimalValue(Map<String, Object> map, String key) {
        if (map == null || !map.containsKey(key)) {
            return BigDecimal.ZERO;
        }
        Object value = map.get(key);
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        return BigDecimal.ZERO;
    }

    /**
     * 计算订单完成率
     *
     * @param totalCount     总订单数
     * @param completedCount 已完成订单数
     * @return 订单完成率（保留两位小数）
     */
    private double calculateCompletionRate(long totalCount, long completedCount) {
        if (totalCount == 0) {
            return 0.0;
        }

        return new BigDecimal(completedCount)
                .divide(new BigDecimal(totalCount), 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}