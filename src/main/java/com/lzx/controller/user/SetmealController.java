package com.lzx.controller.user;

import com.lzx.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [用户端] 套餐管理
 */
@Slf4j
@RestController
@RequestMapping("/user/setmeals")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SetmealController {
//        private final SetmealService setmealService;

        /**
         * TODO 待实现
         * 根据分类 ID 查询套餐列表
         */
//        @GetMapping("/list")
//        public Result<List<SetmealVo>> listQuery(Long categoryId) {
//            List<SetmealVo> setmealVoList = setmealService.listQuery(categoryId);
//            return Result.success(setmealVoList);
//        }
}
