package com.lzx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement // 开启事务管理
public class ZxCafeteriaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZxCafeteriaApplication.class, args);
    }

}
