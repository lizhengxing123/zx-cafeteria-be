package com.lzx.service;

import com.lzx.vo.BusinessDataVo;
import com.lzx.vo.DishOverviewVo;
import com.lzx.vo.OrderOverviewVo;
import com.lzx.vo.SetmealOverviewVo;

/**
 * 工作台服务接口
 */
public interface WorkspaceService {

    /**
     * 查询今日数据
     *
     * @return 今日数据
     */
    BusinessDataVo businessData();

    /**
     * 查询订单总览
     *
     * @return 订单总览
     */
    OrderOverviewVo orderOverview();

    /**
     * 查询菜品总览
     *
     * @return 菜品总览
     */
    DishOverviewVo dishOverview();

    /**
     * 查询套餐总览
     *
     * @return 套餐总览
     */
    SetmealOverviewVo setmealOverview();
}
