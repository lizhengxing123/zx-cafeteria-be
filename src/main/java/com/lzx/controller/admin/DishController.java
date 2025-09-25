package com.lzx.controller.admin;

import com.lzx.constant.MessageConstant;
import com.lzx.dto.DishDto;
import com.lzx.dto.DishPageQueryDTO;
import com.lzx.result.PageResult;
import com.lzx.result.Result;
import com.lzx.service.DishService;
import com.lzx.vo.DishVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品管理
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@RequestMapping("/admin/dishes")
public class DishController {

    private final DishService dishService;

    /**
     * 新增菜品
     *
     * @param dishDto 新增菜品传递的数据模型
     * @return 新增结果
     */
    @PostMapping
    public Result<String> save(@RequestBody DishDto dishDto) {
        log.info("新增菜品：{}", dishDto);
        dishService.saveWithFlavors(dishDto);
        return Result.success(MessageConstant.SAVE_SUCCESS);
    }

    /**
     * 分页查询菜品列表
     *
     * @param dishPageQueryDTO 分页查询菜品列表传递的数据模型
     * @return Result<PageResult<Dish>> 分页查询菜品列表成功返回的数据模型
     */
    @GetMapping("/page")
    public Result<PageResult<DishVo>> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页查询菜品列表：{}", dishPageQueryDTO);
        PageResult<DishVo> pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(MessageConstant.QUERY_SUCCESS, pageResult);
    }

    /**
     * 批量删除菜品
     *
     * @param ids 菜品id列表
     * @return 删除结果
     */
    @DeleteMapping
    public Result<String> delete(@RequestParam List<Long> ids) {
        log.info("批量删除菜品：{}", ids);
        dishService.deleteByIds(ids);
        return Result.success(MessageConstant.DELETE_SUCCESS);
    }

    /**
     * 根据 ID 停售或起售菜品
     *
     * @param status 状态值：1 表示起售，0 表示停售
     * @param id    菜品 ID
     * @return Result<String> 根据 ID 停售或起售菜品成功返回的消息
     */
    @PostMapping("/status/{status}")
    public Result<String> updateStatus(@PathVariable Integer status, @RequestParam Long id) {
        log.info("根据 ID 停售或起售菜品：{}，{}", status, id);
        dishService.updateStatus(status, id);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }

    /**
     * 根据 ID 查询菜品
     *
     * @param id 菜品 ID
     * @return Result<DishVo> 根据 ID 查询菜品成功返回的数据模型
     */
    @GetMapping("/{id}")
    public Result<DishVo> getById(@PathVariable Long id) {
        log.info("根据id查询菜品：{}", id);
        DishVo dishVo = dishService.getByIdWithFlavors(id);
        return Result.success(MessageConstant.QUERY_SUCCESS, dishVo);
    }

    /**
     * 根据 ID 更新菜品信息
     *
     * @param id          菜品 ID
     * @param dishDto 更新菜品信息传递的数据模型
     * @return Result<String> 更新菜品信息成功返回的消息
     */
    @PutMapping("/{id}")
    public Result<String> update(@PathVariable Long id, @RequestBody DishDto dishDto) {
        log.info("根据 ID 更新菜品信息：菜品ID{}，菜品信息{}", id, dishDto);
        dishService.updateByIdWithFlavors(id, dishDto);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }
}
