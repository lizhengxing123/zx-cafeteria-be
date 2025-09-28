package com.lzx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzx.dto.DishPageQueryDTO;
import com.lzx.dto.SetmealPageQueryDTO;
import com.lzx.entity.Dish;
import com.lzx.entity.Setmeal;
import com.lzx.vo.DishItemVO;
import com.lzx.vo.DishVo;
import com.lzx.vo.SetmealVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 套餐 Mapper 接口
 * </p>
 *
 * @author 李正星
 * @since 2025-09-22
 */
@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {

    /**
     * 根据套餐名称查询套餐
     *
     * @param name 套餐名称
     * @return Setmeal 套餐实体类
     */
    @Select("select * from setmeal where name = #{name}")
    Setmeal selectByName(String name);

    /**
     * 分页查询菜品列表
     *
     * @param page                分页数据
     * @param setmealPageQueryDTO 查询条件
     * @return Page<SetmealVo> 分页查询套餐列表成功返回的数据模型
     */
    Page<SetmealVo> selectSetmealWithCategoryName(Page<Setmeal> page, SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据套餐 ID 查询套餐包含的菜品列表
     *
     * @param setmealId 套餐 ID
     * @return List<DishItemVO> 套餐包含的菜品列表
     */
    @Select("select sd.copies, d.name, d.price, d.status, d.description, d.image " +
            "from setmeal_dish sd " +
            "left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> selectDishItemsById(Long setmealId);
}
