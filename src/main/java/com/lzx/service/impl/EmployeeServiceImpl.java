package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzx.constant.MessageConstant;
import com.lzx.constant.PasswordConstant;
import com.lzx.constant.StatusConstant;
import com.lzx.dto.EmployeeDto;
import com.lzx.dto.EmployeeLoginDto;
import com.lzx.dto.EmployeePageQueryDTO;
import com.lzx.entity.Employee;
import com.lzx.exception.AccountLockedException;
import com.lzx.exception.AccountNotFoundException;
import com.lzx.exception.DuplicateDataException;
import com.lzx.exception.PasswordErrorException;
import com.lzx.mapper.EmployeeMapper;
import com.lzx.result.PageResult;
import com.lzx.service.EmployeeService;
import com.lzx.vo.EmployeeLoginVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        Employee employee = employeeMapper.selectByUsername(username);
        if (employee == null) {
            // 账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        if (!passwordEncoder.matches(password, employee.getPassword())) {
            // 密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (Objects.equals(employee.getStatus(), StatusConstant.DISABLE)) {
            // 账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }
        // 登录成功，返回员工登录成功的数据模型
        return employee;
    }

    /**
     * 新增员工
     *
     * @param employeeDto 新增员工传递的数据模型
     */
    @Override
    public void save(EmployeeDto employeeDto) {
        // 检查用户名是否已存在
        Employee existingEmployee = employeeMapper.selectByUsername(employeeDto.getUsername());
        if (existingEmployee != null) {
            throw new DuplicateDataException("用户名【" + employeeDto.getUsername() + "】" + MessageConstant.ALREADY_EXISTS);
        }
        // 创建员工实体类
        Employee employee = new Employee();
        // 将传递过来的 EmployeeDto 属性全部拷贝到 Employee 实体类
        BeanUtils.copyProperties(employeeDto, employee);
        // 密码加密
        String password = passwordEncoder.encode(PasswordConstant.DEFAULT_PASSWORD);
        System.out.println("加密: " + password);
        // 设置属性
        employee.setPassword(password);
        employee.setStatus(StatusConstant.ENABLE);

        // 设置创建时间、更新时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        // 设置创建人、修改人
        // 从 SecurityContextHolder 获取当前登录用户的 ID
        Long currentUserId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        employee.setCreateUser(currentUserId);
        employee.setUpdateUser(currentUserId);

        // 新增员工
        employeeMapper.insert(employee);
    }

    /**
     * 分页查询员工列表
     *
     * @param employeePageQueryDTO 分页查询员工列表传递的数据模型
     * @return PageResult<Employee> 分页查询员工列表成功返回的数据模型
     */
    @Override
    public PageResult<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        // 使用名称模糊匹配
        if (StringUtils.isNotBlank(employeePageQueryDTO.getName())) {
            queryWrapper.like(Employee::getName, employeePageQueryDTO.getName());
        }
        // 根据 ID 降序排序
        queryWrapper.orderByDesc(Employee::getId);
        Page<Employee> page = employeeMapper.selectPage(new Page<>(employeePageQueryDTO.getPageNum(), employeePageQueryDTO.getPageSize()), queryWrapper);
        return new PageResult<>(page.getTotal(), page.getRecords());
    }

    /**
     * 根据 ID 禁用或启用员工
     *
     * @param status  状态值，1 表示启用，0 表示禁用
     * @param id      员工 ID
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        // 根据 ID 查询员工
        Employee employee = employeeMapper.selectById(id);
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 设置状态
        employee.setStatus(status);
        // 设置更新时间和更新人
        employee.setUpdateTime(LocalDateTime.now());
        // 从 SecurityContextHolder 获取当前登录用户的 ID
        Long currentUserId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        employee.setUpdateUser(currentUserId);
        // 更新员工
        employeeMapper.updateById(employee);
    }

    /**
     * 根据 ID 查询员工
     *
     * @param id 员工 ID
     * @return Employee 根据 ID 查询员工成功返回的员工实体类
     */
    @Override
    public Employee getById(Long id) {
        Employee employee = employeeMapper.selectById(id);
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        return employee;
    }

    /**
     * 根据 ID 删除员工
     *
     * @param id 员工 ID
     */
    @Override
    public void removeById(Long id) {
        // 根据 ID 查询员工
        Employee employee = employeeMapper.selectById(id);
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 删除员工
        employeeMapper.deleteById(id);
    }

    /**
     * 根据 ID 更新员工信息
     *
     * @param id        员工 ID
     * @param employeeDto 更新员工信息传递的数据模型
     */
    @Override
    public void updateById(Long id, EmployeeDto employeeDto) {
        // 根据 ID 查询员工
        Employee employee = employeeMapper.selectById(id);
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 将传递过来的 EmployeeDto 属性全部拷贝到 Employee 实体类
        BeanUtils.copyProperties(employeeDto, employee);
        // 设置更新时间和更新人
        employee.setUpdateTime(LocalDateTime.now());
        // 从 SecurityContextHolder 获取当前登录用户的 ID
        Long currentUserId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        employee.setUpdateUser(currentUserId);
        // 更新员工
        employeeMapper.updateById(employee);
    }
}
