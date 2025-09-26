package com.lzx.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 小程序登录相关属性配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "zx.wechat")
public class WechatProperties {

    /**
     * 小程序appid
     */
    private String appid;

    /**
     * 小程序 secret
     */
    private String secret;

     /**
     * 小程序登录地址
     */
    private String loginUrl;

     /**
     * grant_type：固定值，默认值为：authorization_code
     */
    private String grantType = "authorization_code";

}
