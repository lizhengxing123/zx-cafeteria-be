package com.lzx.service;

import com.lzx.vo.TurnoverStatVo;

import java.time.LocalDate;
import java.util.List;

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
    List<TurnoverStatVo> turnover(LocalDate begin, LocalDate end);
}
