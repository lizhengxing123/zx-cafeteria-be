package com.lzx.service;

import com.lzx.dto.UserLoginDto;
import com.lzx.entity.User;

/**
 * 用户管理服务接口
 */
public interface UserService {
    /**
     * 小程序用户登录
     *
     * @param userLoginDto 小程序用户登录请求参数
     * @return User 小程序用户登录成功返回的数据模型
     */
    User wxLogin(UserLoginDto userLoginDto) throws Exception;
}
