package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.entity.Employee;
import com.lzx.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * UserDetailsService实现类，用于加载用户信息
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据员工 username 查询员工信息
        Employee employee = employeeMapper.selectOne(
                new LambdaQueryWrapper<Employee>().eq(Employee::getUsername, username)
        );

        if (employee == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        // 这里可以根据实际需求添加角色权限
        // 目前简单处理，只添加一个默认角色
        return new User(
                employee.getId().toString(),
                employee.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }
}