// com/lzx/enums/ClientType.java
package com.lzx.enums;

import com.lzx.constant.JwtClaimsConstant;
import com.lzx.constant.SecurityConstant;
import com.lzx.properties.JwtProperties;

import java.util.Optional;
import java.util.function.Function;

/**
 * 客户端类型枚举
 * 用于封装不同客户端（如管理端、用户端）的配置信息
 * 包括路径前缀、JWT中的用户标识key、Token请求头名称获取器、密钥获取器
 */
public enum ClientType {

    /**
     * 管理端客户端
     * - 路径前缀: /admin
     * - 用户标识: JwtClaimsConstant.USERNAME (username)
     * - Token请求头: Admin-Token
     * - 密钥: adminSecretKey
     */
    ADMIN(
            SecurityConstant.ADMIN_PATH_PREFIX,
            JwtClaimsConstant.USERNAME,
            JwtProperties::getAdminTokenName,
            JwtProperties::getAdminSecretKey
    ),

    /**
     * 用户端客户端
     * - 路径前缀: /user
     * - 用户标识: JwtClaimsConstant.USER_ID (userId)
     * - Token请求头: User-Token
     * - 密钥: userSecretKey
     */
    USER(
            SecurityConstant.USER_PATH_PREFIX,
            JwtClaimsConstant.USER_ID,
            JwtProperties::getUserTokenName,
            JwtProperties::getUserSecretKey
    );

    /**
     * 客户端请求路径前缀
     */
    private final String pathPrefix;

    /**
     * JWT中用户唯一标识的key
     */
    private final String identifierKey;

    /**
     * Token请求头名称获取器
     */
    private final Function<JwtProperties, String> tokenNameGetter;

    /**
     * JWT密钥获取器
     */
    private final Function<JwtProperties, String> secretKeyGetter;

    /**
     * 构造函数
     *
     * @param pathPrefix        路径前缀
     * @param identifierKey     JWT中用户唯一标识的key
     * @param tokenNameGetter   Token请求头名称获取器
     * @param secretKeyGetter   JWT密钥获取器
     */
    ClientType(String pathPrefix,
               String identifierKey,
               Function<JwtProperties, String> tokenNameGetter,
               Function<JwtProperties, String> secretKeyGetter) {
        this.pathPrefix = pathPrefix;
        this.identifierKey = identifierKey;
        this.tokenNameGetter = tokenNameGetter;
        this.secretKeyGetter = secretKeyGetter;
    }

    /**
     * 根据请求路径判断客户端类型
     *
     * @param path 请求路径
     * @return Optional<ClientType> 客户端类型，如果无法匹配则返回空
     */
    public static Optional<ClientType> fromPath(String path) {
        for (ClientType type : values()) {
            if (path.startsWith(type.pathPrefix)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    // Getter 方法

    public String getPathPrefix() {
        return pathPrefix;
    }

    public String getIdentifierKey() {
        return identifierKey;
    }

    public String getTokenName(JwtProperties jwtProperties) {
        return tokenNameGetter.apply(jwtProperties);
    }

    public String getSecretKey(JwtProperties jwtProperties) {
        return secretKeyGetter.apply(jwtProperties);
    }
}