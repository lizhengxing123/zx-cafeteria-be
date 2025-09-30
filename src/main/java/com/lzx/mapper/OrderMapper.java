package com.lzx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzx.entity.Order;
import com.lzx.vo.OrderOverviewVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author 李正星
 * @since 2025-09-22
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    /**
     * 根据日期范围和状态统计营业额
     *
     * @param begin  开始日期
     * @param end    结束日期
     * @param status 订单状态
     * @return 营业额统计结果
     */
    @MapKey("date")
    Map<String, Map<String, Object>> selectTurnoverByDateRange(LocalDate begin, LocalDate end, Integer status);

    /**
     * 根据日期范围统计订单数：已完成的订单数和总订单数
     *
     * @param begin  开始日期
     * @param end    结束日期
     * @param status 订单状态
     * @return 订单统计结果
     */
    @MapKey("date")
    Map<String, Map<String, Object>> selectCountByDateRange(LocalDate begin, LocalDate end, Integer status);

    /**
     * 查询不同订单状态的数量
     *
     * @return 不同订单状态的数量
     */
    @Select("select count(*) as totalCount, " +
            "sum(IF(status = 2, 1, 0)) as waitingOrderCount, " +
            "sum(IF(status = 3, 1, 0)) as deliveredOrderCount, " +
            "sum(IF(status = 5, 1, 0)) as completedOrderCount, " +
            "sum(IF(status = 6, 1, 0)) as cancelledOrderCount " +
            "from orders " +
            "where order_time between #{begin} and #{end}")
    OrderOverviewVo selectCountByStatus(LocalDateTime begin, LocalDateTime end);

    /**
     * 根据日期范围计算营业额
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 营业额统计结果
     */
    @Select("select sum(amount) as turnover " +
            "from orders " +
            "where order_time between #{begin} and #{end} " +
            "and status = #{status}")
    BigDecimal sumTurnoverByDateRange(LocalDateTime begin, LocalDateTime end, Integer status);
}

