package com.lzx.constant;

/**
 * 安全相关常量类
 */
public class SecurityConstant {
    // 解密过滤器应用标志
    public static final String FILTER_APPLIED = "alreadyDecrypted";

    // 登录请求路径
    public static final String LOGIN_URL = "/admin/employees/login";

    // 不需要认证的路径
    public static final String[] WHITE_LIST_URLS = {
            LOGIN_URL,
            "/admin/auth/getPublicKey",
            "/doc.html",
            "/swagger-resources",
            "/v3/api-docs",
            "/webjars/"
    };
}