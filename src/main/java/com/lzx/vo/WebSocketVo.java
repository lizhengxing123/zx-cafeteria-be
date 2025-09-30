package com.lzx.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * WebSocket 返回消息 VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebSocketVo implements Serializable {
    /**
     * 消息类型：1 来单提醒，2 客户催单
     */
    private Integer type;

    /**
     * 订单 ID
     */
    private Long orderId;

    /**
     * 消息内容
     */
    private String content;
}
