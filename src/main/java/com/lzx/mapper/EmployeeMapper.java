package com.lzx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzx.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 员工信息 Mapper 接口
 * </p>
 *
 * @author 李正星
 * @since 2025-09-22
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
