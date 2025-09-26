package com.lzx.controller.user;

import com.lzx.constant.MessageConstant;
import com.lzx.result.Result;
import com.lzx.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * [用户端] 店铺管理
 */
@Slf4j
@RestController("userShopController")
@RequestMapping("/user/shop")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShopController {

    private final RedisService redisService;

    /**
     * 获取店铺状态
     *
     * @return Result<Integer> 店铺状态
     */
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisService.get(MessageConstant.REDIS_SHOP_STATUS_KEY);
        log.info("店铺状态：{}", status);
        return Result.success(MessageConstant.QUERY_SUCCESS, status);
    }
}
