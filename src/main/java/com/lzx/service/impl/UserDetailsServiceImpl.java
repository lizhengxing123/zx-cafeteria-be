package com.lzx.service.impl;

import com.lzx.entity.CustomUserDetails;
import com.lzx.entity.Employee;
import com.lzx.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * UserDetailsService实现类，用于加载用户信息
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserDetailsServiceImpl implements UserDetailsService {

    private final EmployeeMapper employeeMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据员工 username 查询员工信息
        Employee employee = employeeMapper.selectByUsername(username);
        if (employee == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 这里可以根据实际需求添加角色权限
        // 目前简单处理，只添加一个默认角色
        return new CustomUserDetails(
                employee.getId(),
                employee.getUsername(),
                employee.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }
}