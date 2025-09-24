package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzx.constant.StatusConstant;
import com.lzx.dto.DishDto;
import com.lzx.dto.DishPageQueryDTO;
import com.lzx.entity.Dish;
import com.lzx.entity.DishFlavor;
import com.lzx.entity.Employee;
import com.lzx.mapper.DishFlavorMapper;
import com.lzx.mapper.DishMapper;
import com.lzx.result.PageResult;
import com.lzx.service.DishService;
import com.lzx.vo.DishVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import com.lzx.entity.Category;
import com.lzx.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

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
        // 设置默认状态为启售
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
}
