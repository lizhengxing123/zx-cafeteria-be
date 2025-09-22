package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.constant.MessageConstant;
import com.lzx.constant.StatusConstant;
import com.lzx.dto.EmployeeLoginDto;
import com.lzx.entity.Employee;
import com.lzx.exception.AccountLockedException;
import com.lzx.exception.AccountNotFoundException;
import com.lzx.exception.PasswordErrorException;
import com.lzx.mapper.EmployeeMapper;
import com.lzx.service.EmployeeService;
import com.lzx.vo.EmployeeLoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 员工管理服务实现类
 *
 * @author 李正星
 * @since 2025-09-22
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * 员工登录
     *
     * @param employeeLoginDto 员工登录传递的数据模型
     * @return 员工实体类
     */
    @Override
    public Employee login(EmployeeLoginDto employeeLoginDto) {
        // 获取用户名和密码
        String username = employeeLoginDto.getUsername();
        String password = employeeLoginDto.getPassword();
        // 从数据库查询员工信息
        Employee employee = employeeMapper.selectOne(
                new LambdaQueryWrapper<Employee>()
                        .eq(Employee::getUsername, username)
        );
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 验证密码
        System.out.println("password: " + password);
        System.out.println("employee password: " + employee.getPassword());
        if (!passwordEncoder.matches(password, employee.getPassword())) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (Objects.equals(employee.getStatus(), StatusConstant.DISABLE)) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        // 登录成功，返回员工登录成功的数据模型
        return employee;
    }
}
