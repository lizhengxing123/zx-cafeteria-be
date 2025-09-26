package com.lzx.controller.user;

import com.lzx.constant.MessageConstant;
import com.lzx.result.Result;
import com.lzx.service.DishService;
import com.lzx.vo.DishVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * [用户端] 菜品管理
 */
@Slf4j
@RestController
@RequestMapping("/user/dishes")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DishController {
    private final DishService dishService;

    /**
     * 根据分类 ID 查询菜品列表
     *
     * @param categoryId 分类 ID
     * @return Result<List < DishVo>> 菜品列表
     */
    @GetMapping("/list")
    public Result<List<DishVo>> listQuery(@RequestParam Long categoryId) {
        log.info("[用户端] 根据分类 ID 查询菜品列表：{}", categoryId);
        List<DishVo> dishVoList = dishService.listQuery(categoryId);
        return Result.success(MessageConstant.QUERY_SUCCESS, dishVoList);
    }
}
