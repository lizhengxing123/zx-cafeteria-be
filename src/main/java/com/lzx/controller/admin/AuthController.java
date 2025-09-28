package com.lzx.controller.admin;

import com.lzx.constant.MessageConstant;
import com.lzx.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * [管理端] 认证接口
 */
@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping("/admin/auth")
public class AuthController {

    // // 注入RSA公钥（从RsaKeyConfig获取）
    private final String publicKey;

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
