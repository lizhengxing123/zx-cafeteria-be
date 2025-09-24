package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzx.constant.MessageConstant;
import com.lzx.dto.CategoryDto;
import com.lzx.dto.CategoryPageQueryDto;
import com.lzx.entity.Category;
import com.lzx.entity.Dish;
import com.lzx.entity.Employee;
import com.lzx.entity.Setmeal;
import com.lzx.exception.DataNotFoundException;
import com.lzx.exception.DeletionNotAllowedException;
import com.lzx.exception.DuplicateDataException;
import com.lzx.mapper.CategoryMapper;
import com.lzx.mapper.DishMapper;
import com.lzx.mapper.EmployeeMapper;
import com.lzx.mapper.SetmealMapper;
import com.lzx.result.PageResult;
import com.lzx.service.CategoryService;
import com.lzx.utils.SecurityUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 分类管理服务实现类
 *
 * @author 李正星
 * @since 2025-09-22
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    private static final String SERVICE_NAME = "分类";

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增分类
     *
     * @param categoryDto 新增分类传递的数据模型
     */
    @Override
    public void save(CategoryDto categoryDto) {
        // 检查名称是否已存在
        Category existingEmployee = categoryMapper.selectByName(categoryDto.getName());
        if (existingEmployee != null) {
            throw new DuplicateDataException(SERVICE_NAME + "【" + categoryDto.getName() + "】" + MessageConstant.ALREADY_EXISTS);
        }
        Category category = new Category();
        // 拷贝数据
        BeanUtils.copyProperties(categoryDto, category);
        // 设置默认状态：禁用
        category.setStatus(0);
        // 设置创建时间、更新时间
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());

        // 设置创建人、更新人
        // 从Spring Security上下文获取当前登录用户的ID
        Long currentUserId = SecurityUtil.getCurrentUserId();
        category.setUpdateUser(currentUserId);
        category.setCreateUser(currentUserId);
        // 保存分类
        categoryMapper.insert(category);
    }

    /**
     * 分页查询分类列表
     *
     * @param categoryPageQueryDTO 分页查询分类列表传递的数据模型
     * @return PageResult<Category> 分页查询分类列表成功返回的数据模型
     */
    @Override
    public PageResult<Category> pageQuery(CategoryPageQueryDto categoryPageQueryDTO) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 构建查询条件
        if(StringUtils.hasText(categoryPageQueryDTO.getName())){
            queryWrapper.like(Category::getName, categoryPageQueryDTO.getName());
        }
        if(categoryPageQueryDTO.getType() != null){
            queryWrapper.eq(Category::getType, categoryPageQueryDTO.getType());
        }
        // 根据 sort 排序
        queryWrapper.orderByAsc(Category::getSort);
        // 执行查询
        Page<Category> page = categoryMapper.selectPage(new Page<>(categoryPageQueryDTO.getPageNum(), categoryPageQueryDTO.getPageSize()), queryWrapper);
        // 构建分页结果
        return new PageResult<>(page.getTotal(), page.getRecords());
    }

    /**
     * 根据 ID 启用或禁用分类
     *
     * @param status 状态值，1 表示启用，0 表示禁用
     * @param id     分类 ID
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        // 根据 ID 查询分类
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new DataNotFoundException(SERVICE_NAME + "不存在");
        }
        // 更新状态
        category.setStatus(status);
        // 更新更新时间和更新人
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(SecurityUtil.getCurrentUserId());
        // 更新分类
        categoryMapper.updateById(category);
    }

    /**
     * 根据 ID 查询分类
     *
     * @param id 分类 ID
     * @return Category 分类实体类
     */
    @Override
    public Category getById(Long id) {
        // 根据 ID 查询分类
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new DataNotFoundException(SERVICE_NAME + "不存在");
        }
        return category;
    }

    /**
     * 根据 ID 删除分类
     *
     * @param id 分类 ID
     */
    @Override
    public void removeById(Long id) {
        // 根据 ID 查询分类
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new DataNotFoundException(SERVICE_NAME + "不存在");
        }
        // 查询当前分类是否关联了菜品或套餐
        LambdaQueryWrapper<Dish> dishQueryWrapper = new LambdaQueryWrapper<>();
        dishQueryWrapper.eq(Dish::getCategoryId, id);
        if (dishMapper.selectCount(dishQueryWrapper) > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.eq(Setmeal::getCategoryId, id);
        if (setmealMapper.selectCount(setmealQueryWrapper) > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        // 删除分类
        categoryMapper.deleteById(id);
    }

    /**
     * 根据 ID 更新分类信息
     *
     * @param id          分类 ID
     * @param categoryDto 更新分类信息传递的数据模型
     */
    @Override
    public void updateById(Long id, CategoryDto categoryDto) {
        // 根据 ID 查询分类
        Category category = categoryMapper.selectById(id);
        if (category == null) {
            throw new DataNotFoundException(SERVICE_NAME + "不存在");
        }
        // 检查名称是否已存在
        Category existingCategory = categoryMapper.selectByName(categoryDto.getName());
        if (existingCategory != null && !existingCategory.getId().equals(id)) {
            throw new DuplicateDataException(SERVICE_NAME + "【" + categoryDto.getName() + "】" + MessageConstant.ALREADY_EXISTS);
        }
        // 创建空的 Category 实体类
        Category updatedCategory = new Category();
        // 拷贝数据
        BeanUtils.copyProperties(categoryDto, updatedCategory);
        // 设置 ID
        updatedCategory.setId(id);
        // 更新更新时间和更新人
        updatedCategory.setUpdateTime(LocalDateTime.now());
        updatedCategory.setUpdateUser(SecurityUtil.getCurrentUserId());
        // 更新分类
        categoryMapper.updateById(updatedCategory);
    }
}
