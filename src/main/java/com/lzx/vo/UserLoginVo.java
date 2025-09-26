package com.lzx.vo;

import com.lzx.entity.DishFlavor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 小程序用户登录成功返回的数据模型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginVo implements Serializable {
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 小程序登陆成功返回的openid
     */
    private String openid;

    /**
     * JWT 令牌
     */
    private String token;
}
