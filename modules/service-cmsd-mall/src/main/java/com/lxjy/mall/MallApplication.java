package com.lxjy.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * create by gary 2020/2/24
 * 技术交流请加QQ:498982703
 */
@SpringBootApplication
@MapperScan("com.lxjy.mall.mapper")
@EnableDiscoveryClient
@EnableCircuitBreaker
public class MallApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallApplication.class);

    }
}
