package com.lzx.controller.admin;

import com.lzx.constant.JwtClaimsConstant;
import com.lzx.constant.MessageConstant;
import com.lzx.dto.EmployeeDto;
import com.lzx.dto.EmployeeLoginDto;
import com.lzx.dto.EmployeePageQueryDTO;
import com.lzx.entity.Employee;
import com.lzx.properties.JwtProperties;
import com.lzx.result.PageResult;
import com.lzx.result.Result;
import com.lzx.service.EmployeeService;
import com.lzx.utils.JwtUtils;
import com.lzx.vo.EmployeeLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 员工登录
     *
     * @param employeeLoginDto 员工登录传递的数据模型
     * @return Result<EmployeeLoginVo> 员工登录成功返回的数据模型
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVo> login(@RequestBody EmployeeLoginDto employeeLoginDto) {
        log.info("员工登录：{}", employeeLoginDto);
        Employee employee = employeeService.login(employeeLoginDto);

        // 生成 JWT 令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        claims.put(JwtClaimsConstant.USERNAME, employee.getUsername());

        String token = JwtUtils.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims
        );

        // 生成返回数据
        EmployeeLoginVo employeeLoginVo = EmployeeLoginVo.builder()
                .id(employee.getId())
                .username(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(MessageConstant.LOGIN_SUCCESS, employeeLoginVo);
    }

    /**
     * 新增员工
     *
     * @param employeeDto 新增员工传递的数据模型
     * @return Result<String> 新增员工成功返回的消息
     */
    @PostMapping
    public Result<String> save(@RequestBody EmployeeDto employeeDto) {
        log.info("新增员工：{}", employeeDto);
        employeeService.save(employeeDto);
        return Result.success(MessageConstant.SAVE_SUCCESS);
    }

    /**
     * 分页查询员工列表
     *
     * @param employeePageQueryDTO 分页查询员工列表传递的数据模型
     * @return Result<PageResult<Employee>> 分页查询员工列表成功返回的数据模型
     */
    @GetMapping("/page")
    public Result<PageResult<Employee>> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("分页查询员工列表：{}", employeePageQueryDTO);
        PageResult<Employee> employeePageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(MessageConstant.QUERY_SUCCESS, employeePageResult);
    }

    /**
     * 根据 ID 禁用或启用员工
     *
     * @param status  状态值，1 表示启用，0 表示禁用
     * @param id      员工 ID
     * @return Result<String> 根据 ID 禁用或启用员工成功返回的消息
     */
    @PostMapping("/status/{status}")
    public Result<String> updateStatus(@PathVariable Integer status, @RequestParam Long id) {
        log.info("根据 ID 禁用或启用员工：员工状态{}，员工ID{}", status, id);
        employeeService.updateStatus(status, id);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }

    /**
     * 根据 ID 查询员工
     *
     * @param id 员工 ID
     * @return Result<Employee> 根据 ID 查询员工成功返回的数据模型
     */
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id) {
        log.info("根据 ID 查询员工：{}", id);
        Employee employee = employeeService.getById(id);
        return Result.success(MessageConstant.QUERY_SUCCESS, employee);
    }

    /**
     * 根据 ID 删除员工
     *
     * @param id 员工 ID
     * @return Result<String> 根据 ID 删除员工成功返回的消息
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteById(@PathVariable Long id) {
        log.info("根据 ID 删除员工：{}", id);
        employeeService.removeById(id);
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

    /**
     * 根据 ID 更新员工信息
     *
     * @param id          员工 ID
     * @param employeeDto 更新员工信息传递的数据模型
     * @return Result<String> 更新员工信息成功返回的消息
     */
    @PutMapping("/{id}")
    public Result<String> update(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
        log.info("根据 ID 更新员工信息：员工ID{}，员工信息{}", id, employeeDto);
        employeeService.updateById(id, employeeDto);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }
}
