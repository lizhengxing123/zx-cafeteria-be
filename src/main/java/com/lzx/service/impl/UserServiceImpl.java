package com.lzx.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lzx.constant.MessageConstant;
import com.lzx.dto.UserLoginDto;
import com.lzx.entity.User;
import com.lzx.exception.LoginFailedException;
import com.lzx.mapper.UserMapper;
import com.lzx.properties.WechatProperties;
import com.lzx.service.UserService;
import com.lzx.utils.HttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户管理服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserServiceImpl implements UserService {

    private final WechatProperties wechatProperties;
    private final UserMapper userMapper;

    /**
     * 小程序用户登录
     *
     * @param userLoginDto 小程序用户登录请求参数
     * @return User 小程序用户登录成功返回的数据模型
     */
    @Override
    public User wxLogin(UserLoginDto userLoginDto) throws Exception {
        // 1、调用微信接口，获取用户唯一标识（openid）
        String openid = getOpenid(userLoginDto.getCode());

        // 2、根据 openid 查询用户是否存在
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getOpenid, openid));
        // 2.1 如果用户不存在，创建新用户
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            userMapper.insert(user);
        }
        // 2.2 如果用户存在，直接返回用户信息
        return user;
    }

    /**
     * 获取用户唯一标识（openid）
     *
     * @param code 小程序用户登录凭证（code）
     * @return String 用户唯一标识（openid）
     */
    private String getOpenid(String code) throws Exception {
        // 1.1 构建请求参数
        String requestParams = String.format(
                "appid=%s&secret=%s&js_code=%s&grant_type=%s",
                wechatProperties.getAppid(),
                wechatProperties.getSecret(),
                code,
                wechatProperties.getGrantType()
        );
        // 1.2 构建请求URL
        String loginUrl = wechatProperties.getLoginUrl() + "?" + requestParams;
        // 1.3 发送GET请求，获取用户唯一标识（openid）
        String response = HttpUtil.get(loginUrl);
        // 1.4 解析响应，提取用户唯一标识（openid）
        JSONObject jsonObject = JSON.parseObject(response);
        String openid = jsonObject.getString("openid");
        // 1.5 检查是否成功获取用户唯一标识（openid）
        if (openid == null) {
            log.error("获取用户唯一标识（openid）失败，响应数据：{}", jsonObject);
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        return openid;
    }
}
