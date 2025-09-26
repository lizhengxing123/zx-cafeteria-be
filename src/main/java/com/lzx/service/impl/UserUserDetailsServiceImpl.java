package com.lzx.service.impl;

import com.lzx.constant.MessageConstant;
import com.lzx.entity.CustomUserDetails;
import com.lzx.entity.Employee;
import com.lzx.entity.User;
import com.lzx.mapper.EmployeeMapper;
import com.lzx.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 用户端UserDetailsService实现类，用于加载用户信息
 */
@Service("userUserDetailsService")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserUserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        // 根据用户 id 查询用户信息
        User user = userMapper.selectById(Long.valueOf(userId));
        if (user == null) {
            throw new UsernameNotFoundException(MessageConstant.USER_NOT_FOUND);
        }

        // 这里可以根据实际需求添加角色权限
        // 目前简单处理，只添加一个默认角色
        return new CustomUserDetails(
                user.getId(),
                user.getOpenid(),
                user.getOpenid(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }
}