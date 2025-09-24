package com.lzx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzx.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 菜品及套餐分类 Mapper 接口
 * </p>
 *
 * @author 李正星
 * @since 2025-09-22
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
    /**
     * 根据名称查询分类
     *
     * @param name 分类名称
     * @return Category 分类实体类
     */
    @Select("select * from category where name = #{name}")
    Category selectByName(String name);
}
