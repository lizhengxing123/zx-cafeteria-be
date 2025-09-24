package com.lzx.service;

import com.lzx.dto.DishDto;
import com.lzx.dto.DishPageQueryDTO;
import com.lzx.entity.Dish;
import com.lzx.result.PageResult;
import com.lzx.vo.DishVo;

public interface DishService {
    /**
     * 新增菜品，同时保存菜品的口味数据
     * @param dishDto 新增菜品传递的数据模型
     */
    void saveWithFlavors(DishDto dishDto);

    /**
     * 分页查询菜品列表
     *
     * @param dishPageQueryDTO 分页查询菜品列表传递的数据模型
     * @return PageResult<DishVo> 分页查询菜品列表成功返回的数据模型
     */
    PageResult<DishVo> pageQuery(DishPageQueryDTO dishPageQueryDTO);
}
