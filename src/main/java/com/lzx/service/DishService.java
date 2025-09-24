package com.lzx.service;

import com.lzx.dto.DishDto;

public interface DishService {
    /**
     * 新增菜品，同时保存菜品的口味数据
     * @param dishDto 新增菜品传递的数据模型
     */
    void saveWithFlavors(DishDto dishDto);
}
