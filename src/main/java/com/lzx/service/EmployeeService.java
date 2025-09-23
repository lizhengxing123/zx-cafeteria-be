package com.lzx.service;

import com.lzx.dto.EmployeeDto;
import com.lzx.dto.EmployeeLoginDto;
import com.lzx.dto.EmployeePageQueryDTO;
import com.lzx.entity.Employee;
import com.lzx.result.PageResult;
import com.lzx.vo.EmployeeLoginVo;

/**
 * 员工管理服务接口
 *
 * @author 李正星
 * @since 2025-09-22
 */
public interface EmployeeService {
    /**
     * 员工登录
     *
     * @param employeeLoginDto 员工登录传递的数据模型
     * @return 员工实体类
     */
    Employee login(EmployeeLoginDto employeeLoginDto);

    /**
     * 新增员工
     *
     * @param employeeDto 新增员工传递的数据模型
     */
    void save(EmployeeDto employeeDto);

    /**
     * 分页查询员工列表
     *
     * @param employeePageQueryDTO 分页查询员工列表传递的数据模型
     * @return PageResult<Employee> 分页查询员工列表成功返回的数据模型
     */
    PageResult<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);
}
