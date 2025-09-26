package com.lzx.aspect;

import com.lzx.constant.AutoFillConstant;
import com.lzx.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

/**
 * 自动填充切面类
 * 用于在新增和修改操作中自动填充创建时间、创建人、更新时间和更新人字段
 */
@Slf4j
@Aspect
@Component
public class AutoFIllAspect {
    /**
     * 新增和修改操作的切入点表达式
     * 匹配EmployeeMapper、DishMapper、CategoryMapper接口的insert和update方法
     */
    private static final String POINT_CATEGORY =
            "execution(* com.lzx.mapper.EmployeeMapper.insert*(..)) || " +
            "execution(* com.lzx.mapper.EmployeeMapper.update*(..)) || " +
            "execution(* com.lzx.mapper.DishMapper.insert*(..)) || " +
            "execution(* com.lzx.mapper.DishMapper.update*(..)) || " +
            "execution(* com.lzx.mapper.CategoryMapper.insert*(..)) || " +
            "execution(* com.lzx.mapper.CategoryMapper.update*(..))";

    /**
     * 自动填充方法
     *
     * @param joinPoint 连接点，用于获取当前方法的信息
     * @throws IllegalAccessException    反射调用方法时，访问权限异常
     * @throws NoSuchMethodException     反射调用方法时，未找到方法异常
     * @throws InvocationTargetException 反射调用方法时，目标方法异常
     */
    @Before(POINT_CATEGORY)
    public void autoFill(JoinPoint joinPoint) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        log.info("=====自动填充切面类开始执行=====");

        // 1、获取方法签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 2、查看该方式是新增还是修改
        String methodName = methodSignature.getName();
        log.info("当前方法为：{}", methodName);
        // 根据方法名判断是新增还是修改
        boolean isInsert = methodName.startsWith("insert");
        // 3、获取方法参数
        Object[] args = joinPoint.getArgs();
        log.info("当前方法参数为：{}", args);
        if (args == null || args.length == 0) {
            log.info("当前方法参数为空，无需自动填充");
            return;
        }
        Object entity = args[0];
        // 4、准备赋值数据
        LocalDateTime now = LocalDateTime.now();
        Long currentUserId = SecurityUtil.getCurrentUserId();
        // 4、根据判断是新增还是修改，进行赋值
        if (isInsert) {
            // 新增操作，赋值创建时间、创建人、更新时间、更新人
            entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class).invoke(entity, now);
            entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class).invoke(entity, now);
            entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(entity, currentUserId);
            entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class).invoke(entity, currentUserId);
        } else {
            // 修改操作，赋值更新时间、更新人
            entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class).invoke(entity, now);
            entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class).invoke(entity, currentUserId);
        }

    }
}
