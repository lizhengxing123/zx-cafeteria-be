package com.lzx.service;

import com.lzx.dto.ShoppingCartDto;
import com.lzx.entity.ShoppingCart;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface ShoppingCartService {
    /**
     * 添加购物车
     *
     * @param shoppingCartDto 添加购物车传递的数据模型
     */
    void save(ShoppingCartDto shoppingCartDto);

    /**
     * 修改购物车数量
     *
     * @param id     购物车 ID
     * @param number 购物车数量
     */
    void updateNumberById(Long id, Integer number);

    /**
     * 查看当前用户购物车列表
     *
     * @return 购物车列表
     */
    List<ShoppingCart> listQuery();

    /**
     * 清空购物车
     */
    void clear();
}
