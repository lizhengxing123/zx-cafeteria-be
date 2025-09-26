package com.lzx.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 小程序用户登录传递的数据模型
 */
@Data
public class UserLoginDto implements Serializable {

    /**
     * 小程序登录凭证（code）
     */
    private String code;

}
