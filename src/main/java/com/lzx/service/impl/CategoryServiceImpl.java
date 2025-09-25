package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzx.constant.MessageConstant;
import com.lzx.constant.StatusConstant;
import com.lzx.dto.CategoryDto;
import com.lzx.dto.CategoryPageQueryDto;
import com.lzx.entity.Category;
import com.lzx.entity.Dish;
import com.lzx.entity.Setmeal;
import com.lzx.exception.DataNotFoundException;
import com.lzx.exception.DeletionNotAllowedException;
import com.lzx.exception.DuplicateDataException;
import com.lzx.mapper.CategoryMapper;
import com.lzx.mapper.DishMapper;
import com.lzx.mapper.SetmealMapper;
import com.lzx.result.PageResult;
import com.lzx.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * 分类管理服务实现类
 *
 * @author 李正星
 * @since 2025-09-22
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CategoryServiceImpl implements CategoryService {
    private static final String SERVICE_NAME = "分类";

    private final CategoryMapper categoryMapper;
    private final DishMapper dishMapper;
    private final SetmealMapper setmealMapper;

    /**
     * 新增分类
     */
    @Override
    public void save(CategoryDto categoryDto) {
        checkNameDuplicate(categoryDto.getName(), null);

        Category category = new Category();
        BeanUtils.copyProperties(categoryDto, category);
        category.setStatus(StatusConstant.DISABLE);
        categoryMapper.insert(category);
    }

    /**
     * 分页查询分类列表
     */
    @Override
    public PageResult<Category> pageQuery(CategoryPageQueryDto queryDTO) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<Category>()
                .like(StringUtils.hasText(queryDTO.getName()), Category::getName, queryDTO.getName())
                .eq(queryDTO.getType() != null, Category::getType, queryDTO.getType())
                .orderByAsc(Category::getSort);

        Page<Category> page = categoryMapper.selectPage(
                new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize()),
                queryWrapper
        );

        return new PageResult<>(page.getTotal(), page.getRecords());
    }

    /**
     * 根据ID启用或禁用分类
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        Category category = checkCategoryExists(id);
        category.setStatus(status);
        categoryMapper.updateById(category);
    }

    /**
     * 根据ID查询分类
     */
    @Override
    public Category getById(Long id) {
        return checkCategoryExists(id);
    }

    /**
     * 根据ID删除分类
     */
    @Override
    public void removeById(Long id) {
        checkCategoryExists(id);

        long dishCount = dishMapper.selectCount(new LambdaQueryWrapper<Dish>()
                .eq(Dish::getCategoryId, id));
        if (dishCount > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        long setmealCount = setmealMapper.selectCount(new LambdaQueryWrapper<Setmeal>()
                .eq(Setmeal::getCategoryId, id));
        if (setmealCount > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        categoryMapper.deleteById(id);
    }

    /**
     * 根据ID更新分类信息
     */
    @Override
    public void updateById(Long id, CategoryDto categoryDto) {
        checkCategoryExists(id);
        checkNameDuplicate(categoryDto.getName(), id);

        Category updatedCategory = new Category();
        BeanUtils.copyProperties(categoryDto, updatedCategory);
        updatedCategory.setId(id);
        categoryMapper.updateById(updatedCategory);
    }

    /**
     * 根据分类类型查询分类列表
     */
    @Override
    public List<Category> listQuery(Integer type) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<Category>()
                .eq(type != null, Category::getType, type)
                .orderByAsc(Category::getSort);
        return categoryMapper.selectList(queryWrapper);
    }

    // ------------------------------ 私有工具方法 ------------------------------

    /**
     * 检查分类是否存在，不存在则抛出异常
     */
    private Category checkCategoryExists(Long id) {
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new DataNotFoundException(SERVICE_NAME + MessageConstant.NOT_FOUND);
        }
        return category;
    }

    /**
     * 检查分类名称是否重复
     *
     * @param name      分类名称
     * @param excludeId 排除的ID（更新时使用，为null表示新增）
     */
    private void checkNameDuplicate(String name, Long excludeId) {
        Category existingCategory = categoryMapper.selectByName(name);
        if (existingCategory != null) {
            if (!Objects.equals(existingCategory.getId(), excludeId)) {
                throw new DuplicateDataException(SERVICE_NAME + "【" + name + "】" + MessageConstant.ALREADY_EXISTS);
            }
        }
    }
}
