package com.lzx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzx.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
    /**
     * 根据用户名查询员工
     * @param username 用户名
     * @return 员工对象
     */
    @Select("select * from employee where username = #{username}")
    Employee selectByUsername(String username);
}
