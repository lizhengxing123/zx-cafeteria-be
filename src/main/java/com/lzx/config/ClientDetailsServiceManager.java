// com/lzx/config/ClientDetailsServiceManager.java
package com.lzx.config;

import com.lzx.enums.ClientType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 客户端DetailsService管理器
 * 用于集中管理不同客户端类型对应的UserDetailsService
 * 实现了客户端类型与用户详情服务之间的解耦
 */
@Component
public class ClientDetailsServiceManager {

    /**
     * 存储客户端类型与UserDetailsService的映射关系
     */
    private final Map<ClientType, UserDetailsService> serviceMap = new HashMap<>();

    /**
     * 构造函数，注册所有客户端类型对应的UserDetailsService
     *
     * @param adminUserDetailsService 管理端用户详情服务
     * @param userUserDetailsService  用户端用户详情服务
     */
    public ClientDetailsServiceManager(
            @Qualifier("adminUserDetailsService") UserDetailsService adminUserDetailsService,
            @Qualifier("userUserDetailsService") UserDetailsService userUserDetailsService) {

        // 注册管理端UserDetailsService
        serviceMap.put(ClientType.ADMIN, adminUserDetailsService);
        // 注册用户端UserDetailsService
        serviceMap.put(ClientType.USER, userUserDetailsService);
    }

    /**
     * 根据客户端类型获取对应的UserDetailsService
     *
     * @param clientType 客户端类型
     * @return UserDetailsService 对应的用户详情服务，如果找不到则返回null
     */
    public UserDetailsService getUserDetailsService(ClientType clientType) {
        return serviceMap.get(clientType);
    }
}