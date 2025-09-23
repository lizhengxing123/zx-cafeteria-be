package com.lzx.controller.admin;

import com.lzx.constant.MessageConstant;
import com.lzx.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

/**
 * 认证相关接口
 */
@RestController
@RequestMapping("/admin/auth")
public class AuthController {

    @Autowired
    private String publicKey; // 注入RSA公钥（从RsaKeyConfig获取）

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 提供RSA公钥给前端
     *
     * @return 公钥字符串
     */
    @GetMapping("/getPublicKey")
    public Result<String> getPublicKey() {
        return Result.success(MessageConstant.GET_PUBLIC_KEY_SUCCESS, publicKey);
    }
}
