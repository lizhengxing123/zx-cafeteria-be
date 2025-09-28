package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzx.constant.MessageConstant;
import com.lzx.constant.StatusConstant;
import com.lzx.dto.SetmealDto;
import com.lzx.dto.SetmealPageQueryDTO;
import com.lzx.entity.Dish;
import com.lzx.entity.Setmeal;
import com.lzx.entity.SetmealDish;
import com.lzx.exception.DataNotFoundException;
import com.lzx.exception.DeletionNotAllowedException;
import com.lzx.exception.DuplicateDataException;
import com.lzx.exception.SetmealEnableFailedException;
import com.lzx.mapper.DishMapper;
import com.lzx.mapper.SetmealDishMapper;
import com.lzx.mapper.SetmealMapper;
import com.lzx.result.PageResult;
import com.lzx.service.SetmealService;
import com.lzx.vo.DishItemVO;
import com.lzx.vo.DishVo;
import com.lzx.vo.SetmealVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 套餐服务实现类
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SetmealServiceImpl implements SetmealService {
    private static final String SERVICE_NAME = "套餐";

    private final SetmealMapper setmealMapper;
    private final SetmealDishMapper setmealDishMapper;
    private final DishMapper dishMapper;

    /**
     * 新增套餐，同时保存套餐和菜品的关联关系
     *
     * @param setmealDto 新增套餐的数据模型
     */
    @Override
    public void saveWithDishes(SetmealDto setmealDto) {
        // 校验名称唯一性（新增场景）
        checkNameDuplicate(setmealDto.getName(), null);

        // 拷贝套餐信息
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto, setmeal);
        // 设置默认状态为起售
        setmeal.setStatus(StatusConstant.ENABLE);
        // 保存套餐基本信息
        setmealMapper.insert(setmeal);

        // 新增套餐和菜品的关联关系
        saveOrUpdateSetmealDishes(setmeal.getId(), setmealDto.getSetmealDishes());
    }

    /**
     * 分页查询套餐
     *
     * @param setmealPageQueryDTO 分页查询套餐列表传递的数据模型
     * @return PageResult<SetmealDto> 分页查询套餐列表成功返回的数据模型
     */
    @Override
    public PageResult<SetmealVo> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        Page<SetmealVo> page = setmealMapper.selectSetmealWithCategoryName(
                new Page<>(setmealPageQueryDTO.getPageNum(), setmealPageQueryDTO.getPageSize()),
                setmealPageQueryDTO
        );
        return new PageResult<>(page.getTotal(), page.getRecords());
    }

    /**
     * 批量删除套餐
     *
     * @param ids 套餐 ID 列表
     */
    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        // 校验是否有起售状态的套餐
        checkSaleStatus(ids);

        // 删除套餐数据
        setmealMapper.deleteByIds(ids);
        // 删除套餐关联的菜品数据
        deleteDishesBySetmealIds(ids);
    }

    /**
     * 根据 ID 停售或起售套餐
     *
     * @param status 状态值：1 表示起售，0 表示停售
     * @param id     套餐 ID
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        if (Objects.equals(status, StatusConstant.ENABLE)) {
            // 启售时校验套餐关联的菜品是否都已启售
            checkDishStatus(id);
        }
        Setmeal setmeal = checkSetmealExists(id);
        setmeal.setStatus(status);
        setmealMapper.updateById(setmeal);
    }

    /**
     * 根据 ID 查询套餐（含菜品）
     *
     * @param id 套餐 ID
     * @return SetmealVo 根据 ID 查询套餐成功返回的数据模型
     */
    @Override
    public SetmealVo getByIdWithDishes(Long id) {
        Setmeal setmeal = checkSetmealExists(id);

        // 查询关联的菜品数据
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishes = setmealDishMapper.selectList(queryWrapper);

        // 封装返回结果
        SetmealVo setmealVo = new SetmealVo();
        BeanUtils.copyProperties(setmeal, setmealVo);
        setmealVo.setSetmealDishes(setmealDishes);
        return setmealVo;
    }

    /**
     * 根据 ID 更新套餐信息
     *
     * @param id         套餐 ID
     * @param setmealDto 更新套餐信息传递的数据模型
     */
    @Override
    @Transactional
    public void updateByIdWithDishes(Long id, SetmealDto setmealDto) {
        // 校验套餐是否存在
        checkSetmealExists(id);
        // 校验名称唯一性（更新场景，排除自身）
        checkNameDuplicate(setmealDto.getName(), id);

        // 更新套餐基本信息
        Setmeal updatedSetmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDto, updatedSetmeal);
        updatedSetmeal.setId(id);
        setmealMapper.updateById(updatedSetmeal);

        // 先删除原有关联菜品，再保存新关联菜品
        deleteDishesBySetmealIds(List.of(id));
        saveOrUpdateSetmealDishes(id, setmealDto.getSetmealDishes());
    }

    /**
     * 根据分类 ID 查询套餐列表
     *
     * @param categoryId 分类 ID
     * @return List<Setmeal> 套餐列表
     */
    @Override
    public List<Setmeal> listQuery(Long categoryId) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId, categoryId)
                .eq(Setmeal::getStatus, StatusConstant.ENABLE)
                .orderByAsc(Setmeal::getId);
        return setmealMapper.selectList(queryWrapper);
    }

    /**
     * 根据分类 ID 查询套餐列表，带关联菜品数据
     *
     * @param categoryId 分类 ID
     * @return List<SetmealVo> 套餐列表，每个套餐包含关联的菜品数据
     */
    @Override
    public List<SetmealVo> listQueryWithDishes(Long categoryId) {
        // 查询套餐列表
        List<Setmeal> setmealList = listQuery(categoryId);
        if (CollectionUtils.isEmpty(setmealList)) {
            return new ArrayList<>();
        }

        // 封装返回结果
        List<SetmealVo> setmealVoList = new ArrayList<>();
        for (Setmeal setmeal : setmealList) {
            // 根据套餐ID查询关联的菜品
            SetmealVo setmealVo = getByIdWithDishes(setmeal.getId());
            setmealVoList.add(setmealVo);
        }
        return setmealVoList;
    }

    /**
     * 根据套餐 ID 查询菜品数据
     *
     * @param setmealId 套餐 ID
     * @return List<DishItemVO> 套餐关联的菜品数据列表
     */
    @Override
    public List<DishItemVO> getDishItemsBySetmealId(Long setmealId) {
        // 查询套餐关联的菜品数据
        return setmealMapper.selectDishItemsById(setmealId);
    }

    // ------------------------------ 私有工具方法 ------------------------------

    /**
     * 校验套餐是否已存在
     *
     * @param id 套餐 ID
     * @return Setmeal 校验通过返回的套餐实体类
     */
    private Setmeal checkSetmealExists(Long id) {
        Setmeal setmeal = setmealMapper.selectById(id);
        if (setmeal == null) {
            throw new DataNotFoundException(SERVICE_NAME + MessageConstant.NOT_FOUND);
        }
        return setmeal;
    }

    /**
     * 校验套餐名称是否重复
     *
     * @param name      套餐名称
     * @param excludeId 排除的ID（更新时使用）
     */
    private void checkNameDuplicate(String name, Long excludeId) {
        Setmeal existingSetmeal = setmealMapper.selectByName(name);
        if (existingSetmeal != null) {
            // 新增场景：存在即重复；更新场景：存在且不是自身即重复
            if (!Objects.equals(existingSetmeal.getId(), excludeId)) {
                throw new DuplicateDataException(SERVICE_NAME + "【" + name + "】" + MessageConstant.ALREADY_EXISTS);
            }
        }
    }

    /**
     * 保存或更新套餐关联的菜品数据
     *
     * @param setmealId     套餐ID
     * @param setmealDishes 套餐关联的菜品列表
     */
    private void saveOrUpdateSetmealDishes(Long setmealId, List<SetmealDish> setmealDishes) {
        if (!CollectionUtils.isEmpty(setmealDishes)) {
            setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(setmealId));
        }
        setmealDishMapper.insert(setmealDishes);
    }

    /**
     * 根据套餐ID删除关联的菜品数据
     *
     * @param setmealIds 套餐ID
     */
    private void deleteDishesBySetmealIds(List<Long> setmealIds) {
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SetmealDish::getSetmealId, setmealIds);
        setmealDishMapper.delete(queryWrapper);
    }

    /**
     * 校验是否有起售状态的套餐
     *
     * @param ids 套餐 ID 列表
     */
    private void checkSaleStatus(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId, ids)
                .eq(Setmeal::getStatus, StatusConstant.ENABLE);
        List<Setmeal> setmeals = setmealMapper.selectList(queryWrapper);
        if (!CollectionUtils.isEmpty(setmeals)) {
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }
    }

    /**
     * 校验套餐内的菜品是否都已启售
     *
     * @param setmealId 套餐 ID
     */
    private void checkDishStatus(Long setmealId) {
        List<DishItemVO> dishItemVOList = getDishItemsBySetmealId(setmealId);
        for (DishItemVO dishItemVO : dishItemVOList) {
            if (!Objects.equals(dishItemVO.getStatus(), StatusConstant.ENABLE)) {
                throw new SetmealEnableFailedException(
                        MessageConstant.SETMEAL_ENABLE_FAILED + "【" + dishItemVO.getName() + "】未启售！"
                );
            }
        }
    }
}
