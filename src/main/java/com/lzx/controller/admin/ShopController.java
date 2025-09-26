package com.lzx.controller.admin;

import com.lzx.constant.MessageConstant;
import com.lzx.result.Result;
import com.lzx.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * [管理端] 店铺管理
 */
@Slf4j
@RestController("adminShopController")
@RequestMapping("/admin/shops")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShopController {

    private final RedisService redisService;

    /**
     * 切换店铺状态
     *
     * @param status 状态值：1 表示营业，0 表示打烊
     * @return Result<String> 切换店铺状态成功返回的消息
     */
    @PutMapping("/status/{status}")
    public Result<String> updateStatus(@PathVariable Integer status) {
        log.info("切换店铺状态：{}", status);
        redisService.set(MessageConstant.REDIS_SHOP_STATUS_KEY, status);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }

    /**
     * 获取店铺状态
     *
     * @return Result<Integer> 店铺状态
     */
    @GetMapping("/status")
    public Result<Integer> getStatus() {
        Integer status = (Integer) redisService.get(MessageConstant.REDIS_SHOP_STATUS_KEY);
        log.info("获取店铺状态：{}", status);
        return Result.success(MessageConstant.QUERY_SUCCESS, status);
    }
}
