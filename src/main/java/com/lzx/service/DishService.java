package com.lzx.service;

import com.lzx.dto.DishDto;
import com.lzx.dto.DishPageQueryDTO;
import com.lzx.result.PageResult;
import com.lzx.vo.DishVo;

import java.util.List;

public interface DishService {
    /**
     * 新增菜品，同时保存菜品的口味数据
     *
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

    /**
     * 批量删除菜品
     *
     * @param ids 菜品id列表
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据 ID 停售或起售菜品
     *
     * @param status 状态值：1 表示起售，0 表示停售
     * @param id     菜品 ID
     */
    void updateStatus(Integer status, Long id);

    /**
     * 根据 ID 查询菜品
     *
     * @param id 菜品 ID
     * @return DishVo 根据 ID 查询菜品成功返回的数据模型
     */
    DishVo getByIdWithFlavors(Long id);

    /**
     * 根据 ID 更新菜品信息
     *
     * @param id      菜品 ID
     * @param dishDto 更新菜品信息传递的数据模型
     */
    void updateByIdWithFlavors(Long id, DishDto dishDto);

     /**
     * 根据分类 ID 查询菜品列表
     *
     * @param categoryId 分类 ID
     * @return List<DishVo> 菜品列表
     */
    List<DishVo> listQuery(Long categoryId);
}
