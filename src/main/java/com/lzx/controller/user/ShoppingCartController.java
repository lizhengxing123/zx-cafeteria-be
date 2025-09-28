package com.lzx.controller.user;

import com.lzx.constant.MessageConstant;
import com.lzx.dto.ShoppingCartDto;
import com.lzx.entity.ShoppingCart;
import com.lzx.result.Result;
import com.lzx.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [用户端] 购物车接口
 */
@Slf4j
@RestController
@RequestMapping("/user/shoppingCarts")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     *
     * @param shoppingCartDto 添加购物车传递的数据模型
     */
    @PostMapping
    public Result<String> save(@RequestBody ShoppingCartDto shoppingCartDto) {
        log.info("添加购物车：{}", shoppingCartDto);
        shoppingCartService.save(shoppingCartDto);
        return Result.success(MessageConstant.SAVE_SUCCESS);
    }

    /**
     * 修改购物车数量
     *
     * @param id     购物车 ID
     * @param number 购物车数量
     */
    @PutMapping
    public Result<String> update(Long id, Integer number) {
        log.info("修改购物车数量：{}", number);
        shoppingCartService.updateNumberById(id, number);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }

    /**
     * 查看当前用户购物车列表
     *
     * @return 购物车列表
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> list() {
        log.info("查看当前用户购物车列表");
        List<ShoppingCart> shoppingCartList = shoppingCartService.listQuery();
        return Result.success(MessageConstant.QUERY_SUCCESS, shoppingCartList);
    }

    /**
     * 清空购物车
     *
     * @return 清空购物车成功
     */
    @DeleteMapping("/clear")
    public Result<String> clear() {
        log.info("清空购物车");
        shoppingCartService.clear();
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }
}
