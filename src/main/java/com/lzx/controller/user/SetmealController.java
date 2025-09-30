package com.lzx.controller.user;

import com.lzx.constant.CacheNameConstant;
import com.lzx.constant.MessageConstant;
import com.lzx.constant.StatusConstant;
import com.lzx.entity.Setmeal;
import com.lzx.result.Result;
import com.lzx.service.SetmealService;
import com.lzx.vo.DishItemVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * [用户端] 套餐管理
 */
@Slf4j
@RestController("userSetmealController")
@RequestMapping("/user/setmeals")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class SetmealController {
    private final SetmealService setmealService;

    /**
     * 根据分类 ID 查询套餐列表
     *
     * @param categoryId 分类 ID
     * @return Result<List < SetmealVo>> 套餐列表，每个套餐包含菜品信息
     */
    @GetMapping("/list")
    @Cacheable(cacheNames = CacheNameConstant.SETMEAL_CACHE_NAME, key = "#categoryId")
    public Result<List<Setmeal>> listQuery(@RequestParam Long categoryId) {
        log.info("[用户端] 根据分类 ID 查询套餐列表：{}", categoryId);
        List<Setmeal> setmealList = setmealService.listQuery(categoryId, StatusConstant.ENABLE);
        return Result.success(MessageConstant.QUERY_SUCCESS, setmealList);
    }

    /**
     * 根据套餐 ID 查询套餐包含的菜品列表
     *
     * @param id 套餐 ID
     * @return Result<List < DishItemVO>> 套餐包含的菜品列表
     */
    @GetMapping("/dishes/{id}")
    public Result<List<DishItemVO>> detailQueryWithDishes(@PathVariable Long id) {
        log.info("[用户端] 根据 ID 查询套餐包含的菜品列表：{}", id);
        List<DishItemVO> dishItemVOList = setmealService.getDishItemsBySetmealId(id);
        return Result.success(MessageConstant.QUERY_SUCCESS, dishItemVOList);
    }
}
