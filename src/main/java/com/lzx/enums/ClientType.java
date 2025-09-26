// com/lzx/enums/ClientType.java
package com.lzx.enums;

import com.lzx.constant.JwtClaimsConstant;
import com.lzx.constant.SecurityConstant;
import com.lzx.properties.JwtProperties;

import java.util.Optional;
import java.util.function.Function;

public enum ClientType {
    ADMIN(
            SecurityConstant.ADMIN_PATH_PREFIX,
            JwtClaimsConstant.USERNAME,
            JwtProperties::getAdminTokenName,
            JwtProperties::getAdminSecretKey
    ),
    USER(
            SecurityConstant.USER_PATH_PREFIX,
            JwtClaimsConstant.USER_ID,
            JwtProperties::getUserTokenName,
            JwtProperties::getUserSecretKey
    );

    private final String pathPrefix;
    private final String identifierKey;
    private final Function<JwtProperties, String> tokenNameGetter;
    private final Function<JwtProperties, String> secretKeyGetter;

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
     * 根据请求路径匹配客户端类型
     */
    public static Optional<ClientType> fromPath(String path) {
        for (ClientType type : values()) {
            if (path.startsWith(type.pathPrefix)) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    // Getter方法
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