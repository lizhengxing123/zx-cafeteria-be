package com.lzx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzx.entity.User;
import com.lzx.vo.UserStatVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户信息 Mapper 接口
 * </p>
 *
 * @author 李正星
 * @since 2025-09-22
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 根据日期范围查询用户总数
     *
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 用户总数统计结果
     */
    @MapKey("date")
    Map<String, Map<String, Object>> selectCountByDateRange(LocalDateTime beginTime, LocalDateTime endTime);
}
