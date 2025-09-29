package com.lzx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement // 开启事务管理
@EnableCaching // 开启缓存注解
@EnableScheduling // 开启定时任务注解
public class ZxCafeteriaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZxCafeteriaApplication.class, args);
    }

}
