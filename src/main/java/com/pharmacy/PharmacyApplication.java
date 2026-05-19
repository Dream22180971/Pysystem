package com.pharmacy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** 智能药店后端入口；MyBatis Mapper 扫描 {@code com.pharmacy.mapper} */
@SpringBootApplication
@MapperScan("com.pharmacy.mapper")
public class PharmacyApplication {
    public static void main(String[] args) {
        SpringApplication.run(PharmacyApplication.class, args);
    }
}

