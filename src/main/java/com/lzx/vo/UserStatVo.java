package com.lzx.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户统计视图对象
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserStatVo {
    /**
     * 日期
     */
    private String date;

    /**
     * 新增用户数
     */
    private Long newCount;
    /**
     * 总用户数
     */
    private Long totalCount;
}
