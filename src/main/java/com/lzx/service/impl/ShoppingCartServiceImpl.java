package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.dto.ShoppingCartDto;
import com.lzx.entity.Dish;
import com.lzx.entity.Setmeal;
import com.lzx.entity.ShoppingCart;
import com.lzx.mapper.DishMapper;
import com.lzx.mapper.SetmealMapper;
import com.lzx.mapper.ShoppingCartMapper;
import com.lzx.service.ShoppingCartService;
import com.lzx.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车服务实现
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartMapper shoppingCartMapper;
    private final DishMapper dishMapper;
    private final SetmealMapper setmealMapper;

    /**
     * 添加购物车
     *
     * @param shoppingCartDto 添加购物车传递的数据模型
     */
    @Override
    public void save(ShoppingCartDto shoppingCartDto) {
        // 1. 转换为购物车实体对象
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDto, shoppingCart);
        // 设置用户 ID
        shoppingCart.setUserId(SecurityUtil.getCurrentUserId());

        // 2. 查询购物车数据是否已存在
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.selectListByEntity(shoppingCart);
        if (shoppingCartList != null && !shoppingCartList.isEmpty()) {
            // 3. 如果已存在，更新数量
            ShoppingCart existingCart = shoppingCartList.getFirst();
            existingCart.setNumber(existingCart.getNumber() + 1);
            shoppingCartMapper.updateById(existingCart);
        } else {
            // 4. 如果不存在，新增购物车数据
            Long dishId = shoppingCart.getDishId();
            if (dishId != null) {
                // 需要将菜品信息查出来
                Dish dish = dishMapper.selectById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
            } else {
                // 需要将套餐信息查出来
                Setmeal setmeal = setmealMapper.selectById(shoppingCart.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            // 设置默认数量为 1
            shoppingCart.setNumber(1);
            // 设置创建时间
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 修改购物车数量
     *
     * @param id     购物车 ID
     * @param number 购物车数量
     */
    @Override
    public void updateNumberById(Long id, Integer number) {
        // 1. 构建查询条件
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getId, id);
        if(number <= 0) {
            // 直接删除购物车数据
            shoppingCartMapper.delete(queryWrapper);
        } else {
            // 2. 更新购物车数据
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setNumber(number);
            shoppingCartMapper.update(shoppingCart, queryWrapper);
        }
    }

    /**
     * 查看当前用户购物车列表
     *
     * @return 购物车列表
     */
    @Override
    public List<ShoppingCart> listQuery() {
        // 1. 构建查询条件
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(SecurityUtil.getCurrentUserId());
        // 2. 查询购物车数据
        return shoppingCartMapper.selectListByEntity(shoppingCart);
    }

    /**
     * 清空购物车
     */
    @Override
    public void clear() {
        // 1. 构建查询条件
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, SecurityUtil.getCurrentUserId());
        // 2. 删除购物车数据
        shoppingCartMapper.delete(queryWrapper);
    }
}
