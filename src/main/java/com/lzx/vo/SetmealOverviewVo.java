package com.lzx.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 套餐总览视图对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetmealOverviewVo implements Serializable {
    /**
     * 已启售数量
     */
    private Long soldCount;
    /**
     * 已停售数量
     */
    private Long discontinuedCount;
}
