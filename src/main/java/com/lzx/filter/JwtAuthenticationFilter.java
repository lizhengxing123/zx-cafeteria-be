package com.lzx.filter;

import com.lzx.constant.JwtClaimsConstant;
import com.lzx.constant.SecurityConstant;
import com.lzx.exception.JwtAuthenticationException;
import com.lzx.properties.JwtProperties;
import com.lzx.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * JWT认证过滤器
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserDetailsService userDetailsService; // 需自定义实现，从数据库加载用户信息

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtAuthenticationFilter 执行，请求URL: {}", request.getRequestURL());

        try {
            // 1. 从请求头中获取 Token
            String jwt = parseJwt(request);
            if (jwt != null) {
                // 2. 从 Token 中获取信息
                Claims claims = JwtUtils.parseJWT(jwtProperties.getAdminSecretKey(), jwt);
                // 3. 从 Claims 中获取员工username
                String username = claims.get(JwtClaimsConstant.USERNAME).toString();

                // 4. 加载用户信息
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 5. 创建认证令牌并设置到安全上下文
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. 设置认证信息到安全上下文
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            throw new JwtAuthenticationException("Jwt 解析异常" + e.getMessage());
        }
        // 继续执行后续过滤器
        filterChain.doFilter(request, response);
    }

    // 解析请求头中的 Token
    private String parseJwt(HttpServletRequest request) {
        return request.getHeader(jwtProperties.getAdminTokenName());
    }

    // 重写该方法以排除不需要认证的路径
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // 使用常量数组来检查路径是否在白名单中
        String path = request.getRequestURI();
        for (String whiteUrl : SecurityConstant.WHITE_LIST_URLS) {
            if (path.contains(whiteUrl)) {
                return true;
            }
        }
        return false;
    }
}