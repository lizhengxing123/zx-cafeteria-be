package com.lzx.service;

import com.lzx.dto.CategoryDto;
import com.lzx.dto.CategoryPageQueryDto;
import com.lzx.entity.Category;
import com.lzx.result.PageResult;

import java.util.List;

/**
 * 分类管理服务接口
 *
 * @author 李正星
 * @since 2025-09-22
 */
public interface CategoryService {
    /**
     * 新增分类
     *
     * @param categoryDto 新增分类传递的数据模型
     */
    void save(CategoryDto categoryDto);

    /**
     * 分页查询分类
     *
     * @param categoryPageQueryDTO 分页查询分类列表传递的数据模型
     * @return PageResult<Category> 分页查询分类列表成功返回的数据模型
     */
    PageResult<Category> pageQuery(CategoryPageQueryDto categoryPageQueryDTO);

    /**
     * 根据 ID 启用或禁用分类
     *
     * @param status 状态值，1 表示启用，0 表示禁用
     * @param id     分类 ID
     */
    void updateStatus(Integer status, Long id);

    /**
     * 根据 ID 查询分类
     *
     * @param id 分类 ID
     * @return Category 分类实体类
     */
    Category getById(Long id);

    /**
     * 根据 ID 删除分类
     *
     * @param id 分类 ID
     */
    void removeById(Long id);

    /**
     * 根据 ID 更新分类信息
     *
     * @param id          分类 ID
     * @param categoryDto 更新分类信息传递的数据模型
     */
    void updateById(Long id, CategoryDto categoryDto);

    /**
     * 根据分类类型查询分类列表
     *
     * @param type 分类类型：1 菜品分类，2 套餐分类
     * @param status 状态值：1 表示启用，0 表示禁用
     * @return List<Category> 分类列表
     */
    List<Category> listQuery(Integer type, Integer status);
}
