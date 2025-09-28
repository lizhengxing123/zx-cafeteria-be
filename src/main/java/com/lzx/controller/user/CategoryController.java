package com.lzx.controller.user;

import com.lzx.constant.MessageConstant;
import com.lzx.entity.Category;
import com.lzx.result.Result;
import com.lzx.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * [用户端] 分类管理
 */
@Slf4j
@RestController("userCategoryController")
@RequestMapping("/user/categories")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 根据分类类型查询分类列表
     *
     * @param type 分类类型：1 菜品分类，2 套餐分类
     * @return Result<List<Category>> 分类列表
     */
    @GetMapping("/list")
    public Result<List<Category>> listQuery(@RequestParam(required = false) Integer type) {
        log.info("[用户端] 根据分类类型查询分类列表：{}", type);
        List<Category> categoryList = categoryService.listQuery(type);
        return Result.success(MessageConstant.QUERY_SUCCESS, categoryList);
    }
}
