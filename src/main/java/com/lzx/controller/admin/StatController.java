package com.lzx.controller.admin;

import com.lzx.constant.MessageConstant;
import com.lzx.result.Result;
import com.lzx.service.StatService;
import com.lzx.vo.TurnoverStatVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * [管理端] 统计分析
 */
@Slf4j
@RestController
@RequestMapping("/admin/stats")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class StatController {

    private final StatService statService;

    /**
     * 营业额统计
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return 营业额统计结果
     */
    @GetMapping("/turnover")
    public Result<List<TurnoverStatVo>> turnover(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ) {
        log.info("营业额统计");
        List<TurnoverStatVo> map = statService.turnover(begin, end);
        return Result.success(MessageConstant.QUERY_SUCCESS, map);
    }
}
