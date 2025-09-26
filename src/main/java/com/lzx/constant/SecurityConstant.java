package com.lzx.constant;

/**
 * 安全相关常量类
 */
public class SecurityConstant {
    // 解密过滤器应用标志
    public static final String FILTER_APPLIED = "alreadyDecrypted";

    // 管理端登录请求路径
    public static final String LOGIN_URL = "/admin/employees/login";
    // 用户端登录请求路径
    public static final String USER_LOGIN_URL = "/user/users/login";

    // 不需要认证的路径
    public static final String[] WHITE_LIST_URLS = {
            LOGIN_URL,
            USER_LOGIN_URL,
            // 静态资源路径
            "/files/**",
            // 获取公钥路径
            "/admin/auth/getPublicKey",
            // 获取店铺状态
            "/admin/shops/status",
            "/user/shops/status",
            "/doc.html",
            "/swagger-resources",
            "/v3/api-docs",
            "/webjars/"
    };
    public static final String ADMIN_PATH_PREFIX = "/admin";
    public static final String USER_PATH_PREFIX = "/user";
}