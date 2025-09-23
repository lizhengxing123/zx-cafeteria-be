package com.lzx.filter;

import com.lzx.constant.SecurityConstant;
import com.lzx.dto.EmployeeLoginDto;
import com.lzx.exception.DecryptDataException;
import com.lzx.utils.AesGcmUtil;
import com.lzx.utils.RsaUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 登录请求解密过滤器（在Spring Security认证前执行）
 */
@Slf4j
@Component
public class DecryptFilter extends OncePerRequestFilter {

    // 注入RSA私钥（从配置文件读取）
    @Value("${zx.rsa.private-key}")
    private String rsaPrivateKey;

    // 匹配登录请求
    private final RequestMatcher loginMatcher = PathPatternRequestMatcher.withDefaults().matcher(SecurityConstant.LOGIN_URL);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        log.info("DecryptFilter 执行，请求URL: {}", httpRequest.getRequestURL());

        // 仅对登录请求执行解密
        if (loginMatcher.matches(httpRequest)) {
            // 1. 读取请求体
            String requestBody = readRequestBody(httpRequest);
            EmployeeLoginDto loginRequest = objectMapper.readValue(requestBody, EmployeeLoginDto.class);
            try {
                // 2. RSA解密AES密钥
                String aesKey = RsaUtil.decryptByPrivateKey(loginRequest.getAesKey(), RsaUtil.getPrivateKey(rsaPrivateKey));

                // 3. AES-GCM解密密码和用户名
                String password = AesGcmUtil.decrypt(loginRequest.getPassword(), aesKey);
                String username = AesGcmUtil.decrypt(loginRequest.getUsername(), aesKey);

                // 4. 包装请求，替换解密后的密码
                Map<String, Object> decryptedData = new HashMap<>();
                decryptedData.put("username", username);
                decryptedData.put("password", password);

                HttpServletRequest wrappedRequest = new CustomRequestWrapper(
                        httpRequest,
                        objectMapper.writeValueAsString(decryptedData)
                );

                // 继续执行过滤器链（将解密后的请求传递给Spring Security）
                chain.doFilter(wrappedRequest, response);
            } catch (Exception e) {
                throw new DecryptDataException("登录解密失败: " + e.getMessage());
            }
        } else {
            // 非登录请求直接放行
            chain.doFilter(request, response);
        }
    }

    // 指定不需要过滤的请求
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !loginMatcher.matches(request);
    }

    // 读取请求体内容
    private String readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    // 自定义请求包装器，用于修改请求体
    private static class CustomRequestWrapper extends HttpServletRequestWrapper {
        private final byte[] requestBody;

        // 构造函数 - 关键改进：确保字节数组非空
        public CustomRequestWrapper(HttpServletRequest request, String body) {
            super(request);
            // 防止body为null导致的字节数组为null
            this.requestBody = (body != null) ? body.getBytes(StandardCharsets.UTF_8) : new byte[0];
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            // 确保返回有效的输入流，即使字节数组为空
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody);
            return new ServletInputStream() {
                @Override
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }

                @Override
                public boolean isFinished() {
                    return byteArrayInputStream.available() == 0;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                    // 不需要实现
                }
            };
        }

        // 重写getParameter相关方法（如果需要）
        @Override
        public String getParameter(String name) {
            // 根据需要实现，确保不会返回null或正确处理null
            return super.getParameter(name);
        }
    }
}
