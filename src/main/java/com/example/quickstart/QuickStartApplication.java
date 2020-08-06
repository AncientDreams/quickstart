package com.example.quickstart;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring boot 启动类
 *
 * @author system
 */
@SpringBootApplication
@MapperScan(basePackages = "com.example.quickstart.mapper")
@EnableTransactionManagement
public class QuickStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickStartApplication.class, args);
    }

}
