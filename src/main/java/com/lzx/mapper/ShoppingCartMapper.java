package com.lzx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzx.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>
 * 购物车 Mapper 接口
 * </p>
 *
 * @author 李正星
 * @since 2025-09-22
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

    /**
     * 查询传递过来的购物车数据是否已存在
     * 菜品：根据用户 ID 、菜品 ID 、口味 查询数据是否已存在
     * 套餐：根据用户 ID 、套餐 ID 查询数据是否已存在
     *
     * @param shoppingCart 购物车实体对象
     * @return 数据是否已存在
     */
    List<ShoppingCart> selectListByEntity(ShoppingCart shoppingCart);
}
