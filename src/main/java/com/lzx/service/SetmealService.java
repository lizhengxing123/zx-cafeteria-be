package com.lzx.service;

import com.lzx.dto.SetmealDto;
import com.lzx.dto.SetmealPageQueryDTO;
import com.lzx.entity.Setmeal;
import com.lzx.result.PageResult;
import com.lzx.vo.DishItemVO;
import com.lzx.vo.SetmealVo;

import java.util.List;

/**
 * 套餐服务接口
 */
public interface SetmealService {
    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     *
     * @param setmealDto 新增套餐的数据模型
     */
    void saveWithDishes(SetmealDto setmealDto);


    /**
     * 分页查询套餐列表
     *
     * @param setmealPageQueryDTO 分页查询套餐列表传递的数据模型
     * @return PageResult<SetmealVo> 分页查询套餐列表成功返回的数据模型
     */
    PageResult<SetmealVo> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 批量删除套餐
     *
     * @param ids 套餐 ID 列表
     */
    void deleteByIds(List<Long> ids);

    /**
     * 根据 ID 停售或起售套餐
     *
     * @param status 状态值：1 表示起售，0 表示停售
     * @param id     套餐 ID
     */
    void updateStatus(Integer status, Long id);

    /**
     * 根据 ID 查询套餐（包含菜品信息）
     *
     * @param id 套餐 ID
     * @return SetmealVo 根据 ID 查询套餐成功返回的数据模型
     */
    SetmealVo getByIdWithDishes(Long id);

    /**
     * 根据 ID 更新套餐信息（包含菜品信息）
     *
     * @param id         套餐 ID
     * @param setmealDto 更新套餐信息传递的数据模型
     */
    void updateByIdWithDishes(Long id, SetmealDto setmealDto);

    /**
     * 根据分类 ID 查询套餐列表
     *
     * @param categoryId 分类 ID
     * @return List<Setmeal> 套餐列表
     */
    List<Setmeal> listQuery(Long categoryId);

    /**
     * 根据分类 ID 查询套餐列表（包含菜品信息）
     *
     * @param categoryId 分类 ID
     * @return List<SetmealVo> 套餐列表（包含菜品信息）
     */
    List<SetmealVo> listQueryWithDishes(Long categoryId);

    /**
     * 根据套餐 ID 查询菜品数据
     *
     * @param setmealId 套餐 ID
     * @return List<DishItemVO> 套餐关联的菜品数据列表
     */
    List<DishItemVO> getDishItemsBySetmealId(Long setmealId);
}
