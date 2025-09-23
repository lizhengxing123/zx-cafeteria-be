package com.lzx.exception;

/**
 * JWT解析异常
 */
public class JwtAuthenticationException extends BaseException {
    public JwtAuthenticationException() {}

    public JwtAuthenticationException(String message) {
        super(message);
    }
}
