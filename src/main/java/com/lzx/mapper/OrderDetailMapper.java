package com.lzx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzx.entity.OrderDetail;
import com.lzx.vo.TopTenStatVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单明细表 Mapper 接口
 * </p>
 *
 * @author 李正星
 * @since 2025-09-22
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
    /**
     * 查询指定日期范围内销量排名前10的菜品/套餐
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @param status    订单状态
     * @return 销量排名前10的菜品/套餐列表
     */
    @Select("SELECT name, SUM(od.number) as number FROM order_detail od " +
            "LEFT JOIN orders o ON od.order_id = o.id " +
            "WHERE o.order_time BETWEEN #{beginTime} AND #{endTime} " +
            "AND o.status = #{status} " +
            "GROUP BY name " +
            "ORDER BY number DESC " +
            "LIMIT 10")
    List<TopTenStatVo> selectTopTenByDateRange(LocalDateTime beginTime, LocalDateTime endTime, Integer status);

}
