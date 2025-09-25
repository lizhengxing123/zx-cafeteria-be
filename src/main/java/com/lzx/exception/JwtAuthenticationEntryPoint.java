package com.lzx.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lzx.constant.MessageConstant;
import com.lzx.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 此文件主要处理 JwtAuthenticationFilter 抛出的异常，全局异常处理器捕获不到的问题
 * 当 JwtAuthenticationFilter 抛出 JwtAuthenticationException 异常时，会被此类捕获
 * Spring Security 提供了 AuthenticationEntryPoint 来统一处理认证异常
 * 当认证失败（如 JWT 失效、未携带 JWT 等）时，会触发此异常
 */
@Component // 必须加，让 Spring 管理
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper; // 用来把对象转为 JSON

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // 用你项目的统一响应类来构造返回对象
        Result<String> result = Result.fail(MessageConstant.TOKEN_EXPIRED);

        // 设置响应头
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 把 Result 对象转为 JSON 并返回
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
