package com.lzx.controller.admin;

import com.lzx.constant.MessageConstant;
import com.lzx.dto.DishDto;
import com.lzx.dto.DishPageQueryDTO;
import com.lzx.entity.Dish;
import com.lzx.result.PageResult;
import com.lzx.result.Result;
import com.lzx.service.DishService;
import com.lzx.vo.DishVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 菜品管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/dishes")
public class DishController {
    @Autowired
    private DishService dishService;

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
}
