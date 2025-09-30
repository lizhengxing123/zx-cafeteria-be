package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.constant.StatusConstant;
import com.lzx.entity.Order;
import com.lzx.mapper.OrderMapper;
import com.lzx.service.StatService;
import com.lzx.vo.TurnoverStatVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计分析服务实现类
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class StatServiceImpl implements StatService {

    private final OrderMapper orderMapper;

    /**
     * 营业额统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 营业额统计结果
     */
    @Override
    public List<TurnoverStatVo> turnover(LocalDate begin, LocalDate end) {
        // 根据开始日期和结束日期查询每天已完成的订单，并计算每天总的营业额
        List<TurnoverStatVo> turnoverStatVoList = new ArrayList<TurnoverStatVo>();

        // 1、计算日期范围内的所有日期
        List<LocalDate> dateRange = getDateRange(begin, end);

        // 2、 使用数据库聚合查询，直接计算每天的营业额
        // 这里我们需要编写自定义SQL或使用MyBatis-Plus的复杂查询功能
        // 假设我们添加了一个自定义的mapper方法来执行聚合查询
        List<Map<String, Object>> turnoverData = orderMapper.selectDataByDateRange(begin, end, StatusConstant.COMPLETED);

        // 3、构建日期到营业额的映射
        Map<String, BigDecimal> turnoverMap = new HashMap<>();
        for (Map<String, Object> data : turnoverData) {
            String date = (String) data.get("orderDate");
            BigDecimal amount = new BigDecimal(data.get("totalAmount").toString());
            turnoverMap.put(date, amount);
        }

        // 4、准备返回结果
        for (LocalDate date : dateRange) {
            String dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
            BigDecimal amount = turnoverMap.getOrDefault(dateStr, BigDecimal.ZERO);
            turnoverStatVoList.add(TurnoverStatVo.builder()
                    .date(dateStr)
                    .amount(amount)
                    .build());
        }

        // 返回统计结果
        return turnoverStatVoList;
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
