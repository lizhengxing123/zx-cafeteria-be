package com.lzx.service;

import com.lzx.vo.TurnoverStatVo;
import com.lzx.vo.UserStatVo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 统计分析服务接口
 */
public interface StatService {
    /**
     * 营业额统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 营业额统计结果
     */
    List<TurnoverStatVo> turnoverStat(LocalDate begin, LocalDate end);

    /**
     * 用户统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 用户统计结果
     */
    List<UserStatVo> userStat(LocalDate begin, LocalDate end);
}
