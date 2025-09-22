package com.lzx.controller.admin;

import com.lzx.constant.JwtClaimsConstant;
import com.lzx.constant.MessageConstant;
import com.lzx.dto.EmployeeLoginDto;
import com.lzx.entity.Employee;
import com.lzx.properties.JwtProperties;
import com.lzx.result.Result;
import com.lzx.service.EmployeeService;
import com.lzx.utils.JwtUtils;
import com.lzx.vo.EmployeeLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/employee")
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
                employee.getId(),
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
}
