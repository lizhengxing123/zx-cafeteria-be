package com.lzx.utils;

import com.lzx.constant.MessageConstant;
import com.lzx.entity.CustomUserDetails;
import com.lzx.exception.UserNotLoginException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 安全相关工具类
 */
public class SecurityUtil {

    /**
     * 获取当前登录的自定义用户对象
     */
    public static CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new UserNotLoginException(MessageConstant.USER_NOT_LOGIN);
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof CustomUserDetails)) {
            throw new UserNotLoginException(MessageConstant.USER_NOT_LOGIN);
        }

        return (CustomUserDetails) principal;
    }

    /**
     * 直接获取当前登录用户的ID
     */
    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}

