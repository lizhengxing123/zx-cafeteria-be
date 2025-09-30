package com.lzx.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 营业额统计视图对象
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TurnoverStatVo implements Serializable {
    /**
     * 日期
     */
    private String date;

     /**
      * 营业额
      */
    private BigDecimal amount;
}
