package com.lzx.controller.user;

import com.lzx.constant.JwtClaimsConstant;
import com.lzx.constant.MessageConstant;
import com.lzx.dto.UserLoginDto;
import com.lzx.entity.User;
import com.lzx.properties.JwtProperties;
import com.lzx.result.Result;
import com.lzx.service.UserService;
import com.lzx.utils.JwtUtils;
import com.lzx.vo.UserLoginVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * [用户端] 用户管理
 */
@Slf4j
@RestController
@RequestMapping("/user/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserService userService;
    private final JwtProperties jwtProperties;

    /**
     * 小程序用户登录
     */
    @PostMapping("/login")
    public Result<UserLoginVo> login(@RequestBody UserLoginDto userLoginDto) throws Exception {
        log.info("小程序用户登录：{}", userLoginDto);
        User user = userService.wxLogin(userLoginDto);
        // 登陆成功，生成JWT令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getId());

        String token = JwtUtils.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims
        );

        // 设置用户登录成功返回的数据模型
        UserLoginVo userLoginVo = UserLoginVo.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build();

        return Result.success(MessageConstant.LOGIN_SUCCESS, userLoginVo);
    }
}
