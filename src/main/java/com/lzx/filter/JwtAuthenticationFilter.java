// com/lzx/filter/JwtAuthenticationFilter.java
package com.lzx.filter;

import com.lzx.config.ClientDetailsServiceManager;
import com.lzx.constant.SecurityConstant;
import com.lzx.enums.ClientType;
import com.lzx.exception.JwtAuthenticationException;
import com.lzx.properties.JwtProperties;
import com.lzx.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * JWT认证过滤器
 * 用于拦截请求，验证JWT令牌，并将认证信息存入Spring Security上下文
 * 支持多客户端类型（如管理端/用户端），通过路径前缀自动识别客户端类型
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * JWT配置信息（包含管理端/用户端的密钥、Token请求头名称等）
     */
    private final JwtProperties jwtProperties;

    /**
     * 客户端DetailsService管理器
     * 用于根据客户端类型获取对应的UserDetailsService
     */
    private final ClientDetailsServiceManager clientDetailsServiceManager;

    /**
     * 路径匹配器，用于白名单路径匹配
     */
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 过滤器核心方法，执行JWT验证逻辑
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String path = request.getRequestURI();
            log.info("JwtAuthenticationFilter 执行，请求路径: {}", path);

            // 1. 根据请求路径判断客户端类型（管理端/用户端）
            Optional<ClientType> clientTypeOpt = ClientType.fromPath(path);

            if (clientTypeOpt.isPresent()) {
                ClientType clientType = clientTypeOpt.get();
                log.info("客户端类型: {}", clientType);

                // 2. 获取该客户端对应的Token请求头名称
                String tokenHeaderName = clientType.getTokenName(jwtProperties);
                // 3. 从请求头中获取JWT字符串
                String jwt = request.getHeader(tokenHeaderName);
                // 4. 获取该客户端对应的JWT密钥
                String secretKey = clientType.getSecretKey(jwtProperties);

                // 5. 获取该客户端对应的UserDetailsService
                UserDetailsService userDetailsService = clientDetailsServiceManager.getUserDetailsService(clientType);

                // 6. 如果Token、密钥和UserDetailsService都存在，则进行认证
                if (StringUtils.hasText(jwt) && StringUtils.hasText(secretKey) && userDetailsService != null) {
                    // 7. 解析JWT，获取Claims
                    Claims claims = JwtUtils.parseJWT(secretKey, jwt);
                    // 8. 从Claims中获取用户唯一标识（管理端为username，用户端为userId）
                    String userIdentifier = claims.get(clientType.getIdentifierKey()).toString();

                    // 9. 加载用户信息
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userIdentifier);

                    // 10. 创建认证令牌并设置到SecurityContext
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.info("JWT认证成功，用户: {}", userIdentifier);
                }
            }
        } catch (Exception e) {
            log.error("JWT解析异常，请求URL: {}", request.getRequestURL(), e);
            throw new JwtAuthenticationException("JWT解析异常: " + e.getMessage());
        }

        // 继续执行后续过滤器链
        filterChain.doFilter(request, response);
    }

    /**
     * 判断当前请求是否不需要进行过滤（白名单路径）
     *
     * @param request 请求对象
     * @return 如果请求路径在白名单中，则返回true（跳过过滤），否则返回false（需要过滤）
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return Arrays.stream(SecurityConstant.WHITE_LIST_URLS)
                .anyMatch(whiteUrl -> pathMatcher.match(whiteUrl, path));
    }
}