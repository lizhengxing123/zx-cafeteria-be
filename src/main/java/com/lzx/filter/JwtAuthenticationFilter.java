package com.lzx.filter;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProperties jwtProperties;

    @Qualifier("userUserDetailsService")
    private final UserDetailsService userUserDetailsService;

    @Qualifier("adminUserDetailsService")
    private final UserDetailsService adminUserDetailsService;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("JwtAuthenticationFilter 执行，请求URL: {}", request.getRequestURL());

        try {
            String path = request.getRequestURI();
            // 根据请求路径匹配客户端类型
            Optional<ClientType> clientTypeOpt = ClientType.fromPath(path);
            log.info("匹配到的客户端类型: {}", clientTypeOpt.map(ClientType::name)
                    .orElse("未匹配到"));


            if (clientTypeOpt.isPresent()) {
                ClientType clientType = clientTypeOpt.get();
                String tokenHeaderName = clientType.getTokenName(jwtProperties);
                String jwt = request.getHeader(tokenHeaderName);
                String secretKey = clientType.getSecretKey(jwtProperties);
                UserDetailsService userDetailsService = getMatchingUserDetailsService(clientType);

                if (StringUtils.hasText(jwt) && StringUtils.hasText(secretKey) && userDetailsService != null) {
                    // 解析JWT,获取用户标识符
                    Claims claims = JwtUtils.parseJWT(secretKey, jwt);
                    String userIdentifier = claims.get(clientType.getIdentifierKey()).toString();
                    // 加载用户详细信息
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userIdentifier);
                    // 创建认证令牌
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    // 设置认证详情
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // 设置认证上下文
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            log.error("JWT解析异常，请求URL: {}", request.getRequestURL(), e);
            throw new JwtAuthenticationException("JWT解析异常: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 根据客户端类型获取对应的UserDetailsService
     */
    private UserDetailsService getMatchingUserDetailsService(ClientType clientType) {
        return switch (clientType) {
            case ADMIN -> adminUserDetailsService;
            case USER -> userUserDetailsService;
        };
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return Arrays.stream(SecurityConstant.WHITE_LIST_URLS)
                .anyMatch(whiteUrl -> pathMatcher.match(whiteUrl, path));
    }
}