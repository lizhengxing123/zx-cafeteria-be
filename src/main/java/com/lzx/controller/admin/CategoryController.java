package com.lzx.controller.admin;

import com.lzx.constant.CacheNameConstant;
import com.lzx.constant.MessageConstant;
import com.lzx.dto.CategoryDto;
import com.lzx.dto.CategoryPageQueryDto;
import com.lzx.entity.Category;
import com.lzx.result.PageResult;
import com.lzx.result.Result;
import com.lzx.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [管理端] 分类管理
 *
 * @author 李正星
 * @since 2025-09-22
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping("/admin/categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 新增分类
     *
     * @param categoryDto 新增分类传递的数据模型
     * @return 新增分类的id
     */
    @PostMapping
    public Result<Long> save(@RequestBody CategoryDto categoryDto) {
        log.info("新增分类：{}", categoryDto);
        categoryService.save(categoryDto);
        return Result.success(MessageConstant.SAVE_SUCCESS);
    }

    /**
     * 分页查询分类列表
     *
     * @param categoryPageQueryDTO 分页查询分类列表传递的数据模型
     * @return PageResult<Category> 分页查询分类列表成功返回的数据模型
     */
    @GetMapping("/page")
    public Result<PageResult<Category>> pageQuery(CategoryPageQueryDto categoryPageQueryDTO) {
        log.info("分页查询分类列表：{}", categoryPageQueryDTO);
        PageResult<Category> pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(MessageConstant.QUERY_SUCCESS, pageResult);
    }

    /**
     * 根据 ID 禁用或启用分类
     *
     * @param status 状态值，1 表示启用，0 表示禁用
     * @param id     分类 ID
     * @return Result<Long> 根据 ID 禁用或启用分类成功返回的消息
     */
    @PutMapping("/status/{status}")
    // 清除菜品缓存和套餐缓存，因为分类状态改变会影响到菜品和套餐的显示
    @CacheEvict(cacheNames = {CacheNameConstant.DISH_CACHE_NAME, CacheNameConstant.SETMEAL_CACHE_NAME}, key = "#id")
    public Result<Long> updateStatus(@PathVariable Integer status, @RequestParam Long id) {
        log.info("根据 ID 启用或禁用分类：分类状态{}，分类ID{}", status, id);
        categoryService.updateStatus(status, id);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }

    /**
     * 根据 ID 查询分类
     *
     * @param id 分类 ID
     * @return Result<Category> 根据 ID 查询分类成功返回的数据模型
     */
    @GetMapping("/{id}")
    public Result<Category> getById(@PathVariable Long id) {
        log.info("根据 ID 查询分类：{}", id);
        Category category = categoryService.getById(id);
        return Result.success(MessageConstant.QUERY_SUCCESS, category);
    }

    /**
     * 根据 ID 删除分类
     *
     * @param id 分类 ID
     * @return Result<String> 根据 ID 删除分类成功返回的消息
     */
    @DeleteMapping("/{id}")
    // 清除菜品缓存和套餐缓存，因为删除分类会影响到菜品和套餐的显示
    @CacheEvict(cacheNames = {CacheNameConstant.DISH_CACHE_NAME, CacheNameConstant.SETMEAL_CACHE_NAME}, key = "#id")
    public Result<Long> delete(@PathVariable Long id) {
        log.info("根据 ID删除分类：{}", id);
        categoryService.removeById(id);
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

    /**
     * 根据 ID 更新分类信息
     *
     * @param id          分类 ID
     * @param categoryDto 更新分类信息传递的数据模型
     * @return Result<String> 更新分类信息成功返回的消息
     */
    @PutMapping("/{id}")
    public Result<String> update(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        log.info("根据 ID 更新分类信息：分类ID{}，分类信息{}", id, categoryDto);
        categoryService.updateById(id, categoryDto);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }

    /**
     * 根据分类类型查询分类列表
     *
     * @param type 分类类型：1 菜品分类，2 套餐分类
     * @return Result<List<Category>> 根据分类类型查询分类列表成功返回的数据模型
     */
    @GetMapping("/list")
    public Result<List<Category>> listQuery(@RequestParam(required = false) Integer type) {
        log.info("根据分类类型查询分类列表：{}", type);
        List<Category> categoryList = categoryService.listQuery(type, null);
        return Result.success(MessageConstant.QUERY_SUCCESS, categoryList);
    }
}
