package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzx.constant.MessageConstant;
import com.lzx.constant.StatusConstant;
import com.lzx.dto.DishDto;
import com.lzx.dto.DishPageQueryDTO;
import com.lzx.entity.*;
import com.lzx.exception.DeletionNotAllowedException;
import com.lzx.mapper.*;
import com.lzx.result.PageResult;
import com.lzx.service.DishService;
import com.lzx.vo.DishVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增菜品，同时保存菜品的口味数据
     * @param dishDto 新增菜品传递的数据模型
     */
    @Override
    @Transactional // 开启事务
    public void saveWithFlavors(DishDto dishDto) {
        // 保存菜品
        Dish dish = new Dish();
        // 拷贝属性
        BeanUtils.copyProperties(dishDto, dish);
        // 设置默认状态为起售
        dish.setStatus(StatusConstant.ENABLE);
        // 保存菜品，并获取菜品的id
        dishMapper.insert(dish);

        // 保存菜品的口味数据
        List<DishFlavor> flavors = dishDto.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            for (DishFlavor flavor : flavors) {
                // 将菜品的口味数据的dishId设置为当前菜品的id
                flavor.setDishId(dish.getId());
            }
            // 批量保存菜品的口味数据
            dishFlavorMapper.insert(flavors);
        }
    }

    /**
     * 分页查询菜品列表
     *
     * @param dishPageQueryDTO 分页查询菜品列表传递的数据模型
     * @return PageResult<Dish> 分页查询菜品列表成功返回的数据模型
     */
    @Override
    public PageResult<DishVo> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        // 分页查询
        Page<DishVo> page = dishMapper.selectDishWithCategoryName(new Page<>(dishPageQueryDTO.getPageNum(), dishPageQueryDTO.getPageSize()), dishPageQueryDTO);
        return new PageResult<>(page.getTotal(), page.getRecords());
    }

    /**
     * 批量删除菜品
     *
     * @param ids 菜品id列表
     */
    @Override
    @Transactional // 开启事务
    public void deleteByIds(List<Long> ids) {
        // 1、起售状态的菜品不能删除
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, ids);
        queryWrapper.eq(Dish::getStatus, StatusConstant.ENABLE);
        List<Dish> dishes = dishMapper.selectList(queryWrapper);
        if (!dishes.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }

        // 2、被套餐关联的菜品不能删除
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getDishId, ids);
        List<SetmealDish> setmealDishes = setmealDishMapper.selectList(setmealDishLambdaQueryWrapper);
        if (!setmealDishes.isEmpty()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 3、批量删除菜品
        dishMapper.deleteByIds(ids);

        // 4、批量删除菜品的口味数据
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.in(DishFlavor::getDishId, ids);
        dishFlavorMapper.delete(dishFlavorLambdaQueryWrapper);
    }
}
