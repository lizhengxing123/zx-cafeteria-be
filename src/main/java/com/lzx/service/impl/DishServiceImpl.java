package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzx.constant.MessageConstant;
import com.lzx.constant.StatusConstant;
import com.lzx.dto.DishDto;
import com.lzx.dto.DishPageQueryDTO;
import com.lzx.entity.Dish;
import com.lzx.entity.DishFlavor;
import com.lzx.entity.SetmealDish;
import com.lzx.exception.DataNotFoundException;
import com.lzx.exception.DeletionNotAllowedException;
import com.lzx.exception.DuplicateDataException;
import com.lzx.mapper.DishFlavorMapper;
import com.lzx.mapper.DishMapper;
import com.lzx.mapper.SetmealDishMapper;
import com.lzx.result.PageResult;
import com.lzx.service.DishService;
import com.lzx.vo.DishVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    private static final String SERVICE_NAME = "菜品";

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增菜品，同时保存菜品的口味数据
     */
    @Override
    @Transactional
    public void saveWithFlavors(DishDto dishDto) {
        // 校验名称唯一性（新增场景）
        checkNameDuplicate(dishDto.getName(), null);

        // 保存菜品基本信息
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto, dish);
        dish.setStatus(StatusConstant.ENABLE);
        dishMapper.insert(dish);

        // 批量保存口味数据
        saveOrUpdateFlavors(dish.getId(), dishDto.getFlavors());
    }

    /**
     * 分页查询菜品列表
     */
    @Override
    public PageResult<DishVo> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        Page<DishVo> page = dishMapper.selectDishWithCategoryName(
                new Page<>(dishPageQueryDTO.getPageNum(), dishPageQueryDTO.getPageSize()),
                dishPageQueryDTO
        );
        return new PageResult<>(page.getTotal(), page.getRecords());
    }

    /**
     * 批量删除菜品
     */
    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        // 校验是否有起售状态的菜品
        checkSaleStatus(ids);
        // 校验是否被套餐关联
        checkSetmealRelation(ids);

        // 删除菜品及关联口味
        dishMapper.deleteByIds(ids);
        deleteFlavorsByDishIds(ids);
    }

    /**
     * 根据 ID 停售或起售菜品
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        Dish dish = checkDishExists(id);
        dish.setStatus(status);
        dishMapper.updateById(dish);
    }

    /**
     * 根据 ID 查询菜品（含口味）
     */
    @Override
    public DishVo getByIdWithFlavors(Long id) {
        Dish dish = checkDishExists(id);

        // 查询口味数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> flavors = dishFlavorMapper.selectList(queryWrapper);

        // 封装返回结果
        DishVo dishVo = new DishVo();
        BeanUtils.copyProperties(dish, dishVo);
        dishVo.setFlavors(flavors);
        return dishVo;
    }

    /**
     * 根据 ID 更新菜品信息
     */
    @Override
    @Transactional
    public void updateByIdWithFlavors(Long id, DishDto dishDto) {
        // 校验菜品是否存在
        checkDishExists(id);
        // 校验名称唯一性（更新场景，排除自身）
        checkNameDuplicate(dishDto.getName(), id);

        // 更新菜品基本信息
        Dish updatedDish = new Dish();
        BeanUtils.copyProperties(dishDto, updatedDish);
        updatedDish.setId(id);
        dishMapper.updateById(updatedDish);

        // 先删除原有口味，再保存新口味
        deleteFlavorsByDishIds(List.of(id));
        saveOrUpdateFlavors(id, dishDto.getFlavors());
    }

    // ------------------------------ 私有工具方法 ------------------------------

    /**
     * 校验菜品是否存在，不存在则抛出异常
     */
    private Dish checkDishExists(Long id) {
        Dish dish = dishMapper.selectById(id);
        if (dish == null) {
            throw new DataNotFoundException(SERVICE_NAME + MessageConstant.NOT_FOUND);
        }
        return dish;
    }

    /**
     * 校验菜品名称是否重复
     *
     * @param name      菜品名称
     * @param excludeId 排除的ID（更新时使用）
     */
    private void checkNameDuplicate(String name, Long excludeId) {
        Dish existingDish = dishMapper.selectByName(name);
        if (existingDish != null) {
            // 新增场景：存在即重复；更新场景：存在且不是自身即重复
            if (excludeId == null || !existingDish.getId().equals(excludeId)) {
                throw new DuplicateDataException(SERVICE_NAME + "【" + name + "】" + MessageConstant.ALREADY_EXISTS);
            }
        }
    }

    /**
     * 保存或更新菜品口味数据
     *
     * @param dishId  菜品ID
     * @param flavors 口味列表
     */
    private void saveOrUpdateFlavors(Long dishId, List<DishFlavor> flavors) {
        if (!CollectionUtils.isEmpty(flavors)) {
            flavors.forEach(flavor -> flavor.setDishId(dishId));
            dishFlavorMapper.insert(flavors);
        }
    }

    /**
     * 根据菜品ID删除口味数据
     *
     * @param dishIds 菜品ID列表
     */
    private void deleteFlavorsByDishIds(List<Long> dishIds) {
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DishFlavor::getDishId, dishIds);
        dishFlavorMapper.delete(queryWrapper);
    }

    /**
     * 校验是否有起售状态的菜品
     */
    private void checkSaleStatus(List<Long> ids) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, ids)
                .eq(Dish::getStatus, StatusConstant.ENABLE);
        List<Dish> dishes = dishMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(dishes)) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }
    }

    /**
     * 校验菜品是否被套餐关联
     */
    private void checkSetmealRelation(List<Long> ids) {
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SetmealDish::getDishId, ids);
        List<SetmealDish> setmealDishes = setmealDishMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(setmealDishes)) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
    }
}