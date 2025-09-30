package com.lzx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzx.dto.DishPageQueryDTO;
import com.lzx.entity.Dish;
import com.lzx.vo.DishOverviewVo;
import com.lzx.vo.DishVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 菜品 Mapper 接口
 * </p>
 *
 * @author 李正星
 * @since 2025-09-22
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    /**
     * 根据名称查询分类
     *
     * @param name 菜品名称
     * @return Dish 菜品实体类
     */
    @Select("select * from dish where name = #{name}")
    Dish selectByName(String name);

    /**
     * 分页查询菜品列表
     *
     * @param page             分页数据
     * @param dishPageQueryDTO 查询条件
     * @return Page<DishVo> 分页查询菜品列表成功返回的数据模型
     */
    Page<DishVo> selectDishWithCategoryName(Page<Dish> page, DishPageQueryDTO dishPageQueryDTO);

    /**
     * 查询菜品起售和停售数量
     *
     * @return 菜品起售和停售数量
     */
    @Select("select sum(IF(status = 1, 1, 0)) as soldCount, " +
            "sum(IF(status = 0, 1, 0)) as discontinuedCount " +
            "from dish")
    DishOverviewVo countDishStatus();
}
