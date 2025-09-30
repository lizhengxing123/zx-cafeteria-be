package com.lzx.controller.admin;

import com.lzx.constant.MessageConstant;
import com.lzx.result.Result;
import com.lzx.service.WorkspaceService;
import com.lzx.vo.BusinessDataVo;
import com.lzx.vo.DishOverviewVo;
import com.lzx.vo.OrderOverviewVo;
import com.lzx.vo.SetmealOverviewVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * [管理端] 工作台接口
 */
@Slf4j
@RestController
@RequestMapping("/admin/workspaces")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    /**
     * 查询今日数据
     *
     * @return 今日数据
     */
    @GetMapping("/businessData")
    public Result<BusinessDataVo> businessData() {
        log.info("查询今日数据");
        BusinessDataVo businessDataVo = workspaceService.businessData();
        return Result.success(MessageConstant.QUERY_SUCCESS, businessDataVo);
    }

    /**
     * 查询订单总览
     *
     * @return 订单总览
     */
    @GetMapping("/orderOverview")
    public Result<OrderOverviewVo> orderOverview() {
        log.info("查询订单总览");
        OrderOverviewVo orderOverviewVo = workspaceService.orderOverview();
        return Result.success(MessageConstant.QUERY_SUCCESS, orderOverviewVo);
    }

    /**
     * 查询菜品总览
     *
     * @return 菜品总览
     */
    @GetMapping("/dishOverview")
    public Result<DishOverviewVo> dishOverview() {
        log.info("查询菜品总览");
        DishOverviewVo dishOverviewVo = workspaceService.dishOverview();
        return Result.success(MessageConstant.QUERY_SUCCESS, dishOverviewVo);
    }

    /**
     * 查询套餐总览
     *
     * @return 套餐总览
     */
    @GetMapping("/setmealOverview")
    public Result<SetmealOverviewVo> setmealOverview() {
        log.info("查询套餐总览");
        SetmealOverviewVo setmealOverviewVo = workspaceService.setmealOverview();
        return Result.success(MessageConstant.QUERY_SUCCESS, setmealOverviewVo);
    }
}
