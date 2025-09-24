package com.lzx.controller.admin;

import com.lzx.constant.MessageConstant;
import com.lzx.dto.DishDto;
import com.lzx.result.Result;
import com.lzx.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @param dishDto 新增菜品传递的数据模型
     * @return 新增结果
     */
    @PostMapping
    public Result<String> save(@RequestBody DishDto dishDto) {
        log.info("新增菜品：{}", dishDto);
        dishService.saveWithFlavors(dishDto);
        return Result.success(MessageConstant.SAVE_SUCCESS);
    }
}
