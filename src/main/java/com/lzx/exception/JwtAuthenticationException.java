package com.lzx.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * JWT解析异常，继承 spring security 提供的 AuthenticationException 异常
 */
public class JwtAuthenticationException extends AuthenticationException {
    public JwtAuthenticationException(String message) {
        super(message);
    }
}
