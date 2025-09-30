package com.lzx.controller.admin;

import com.lzx.constant.CacheNameConstant;
import com.lzx.constant.MessageConstant;
import com.lzx.dto.SetmealDto;
import com.lzx.dto.SetmealPageQueryDTO;
import com.lzx.entity.Setmeal;
import com.lzx.result.PageResult;
import com.lzx.result.Result;
import com.lzx.service.SetmealService;
import com.lzx.vo.SetmealVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [管理端] 套餐管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/setmeals")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SetmealController {

    private final SetmealService setmealService;

    /**
     * 新增套餐
     *
     * @param setmealDto 新增套餐的数据模型
     * @return Result<String> 新增成功返回的消息
     */
    @PostMapping
    @CacheEvict(cacheNames = CacheNameConstant.SETMEAL_CACHE_NAME, key = "#setmealDto.categoryId")
    public Result<String> save(@RequestBody SetmealDto setmealDto) {
        log.info("新增套餐：{}", setmealDto);
        setmealService.saveWithDishes(setmealDto);
        return Result.success(MessageConstant.SAVE_SUCCESS);
    }

    /**
     * 分页查询套餐列表
     *
     * @param setmealPageQueryDTO 分页查询套餐列表传递的数据模型
     * @return Result<PageResult < SetmealVo>> 分页查询套餐列表成功返回的数据模型
     */
    @GetMapping("/page")
    public Result<PageResult<SetmealVo>> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("分页查询套餐列表：{}", setmealPageQueryDTO);
        PageResult<SetmealVo> pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(MessageConstant.QUERY_SUCCESS, pageResult);
    }

    /**
     * 批量删除套餐
     *
     * @param ids 套餐 ID 列表
     * @return Result<String> 删除成功返回的消息
     */
    @DeleteMapping
    @CacheEvict(cacheNames = CacheNameConstant.SETMEAL_CACHE_NAME, allEntries = true)
    public Result<String> delete(@RequestParam List<Long> ids) {
        log.info("批量删除套餐：{}", ids);
        setmealService.deleteByIds(ids);
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

    /**
     * 根据 ID 停售或起售套餐
     *
     * @param status 状态值：1 表示起售，0 表示停售
     * @param id     套餐 ID
     * @return Result<String> 根据 ID 停售或起售套餐成功返回的消息
     */
    @PutMapping("/status/{status}")
    @CacheEvict(cacheNames = CacheNameConstant.SETMEAL_CACHE_NAME, allEntries = true)
    public Result<String> updateStatus(@PathVariable Integer status, @RequestParam Long id) {
        log.info("根据 ID 停售或起售套餐：{}，{}", status, id);
        setmealService.updateStatus(status, id);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }

    /**
     * 根据 ID 查询套餐，含菜品信息
     *
     * @param id 套餐 ID
     * @return Result<SetmealVo> 根据 ID 查询套餐成功返回的数据模型
     */
    @GetMapping("/{id}")
    public Result<SetmealVo> getById(@PathVariable Long id) {
        log.info("根据id查询套餐：{}", id);
        SetmealVo setmealVo = setmealService.getByIdWithDishes(id);
        return Result.success(MessageConstant.QUERY_SUCCESS, setmealVo);
    }

    /**
     * 根据 ID 更新套餐信息
     *
     * @param id      套餐 ID
     * @param setmealDto 更新套餐信息传递的数据模型
     * @return Result<String> 更新套餐信息成功返回的消息
     */
    @PutMapping("/{id}")
    @CacheEvict(cacheNames = CacheNameConstant.SETMEAL_CACHE_NAME, allEntries = true)
    public Result<String> update(@PathVariable Long id, @RequestBody SetmealDto setmealDto) {
        log.info("根据 ID 更新套餐信息：套餐ID{}，套餐信息{}", id, setmealDto);
        setmealService.updateByIdWithDishes(id, setmealDto);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }

    /**
     * 根据分类 ID 查询套餐列表
     *
     * @param categoryId 分类 ID
     * @return Result<List < Setmeal>> 套餐列表
     */
    @GetMapping("/list")
    public Result<List<Setmeal>> listQuery(@RequestParam Long categoryId) {
        log.info("根据分类 ID 查询套餐列表：{}", categoryId);
        List<Setmeal> setmealList = setmealService.listQuery(categoryId, null);
        return Result.success(MessageConstant.QUERY_SUCCESS, setmealList);
    }
}
