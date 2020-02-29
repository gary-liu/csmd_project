package com.lxjy.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * create by gary 2020/2/21
 * 技术交流请加QQ:498982703
 */
@SpringBootApplication
@EnableCircuitBreaker
@EnableDiscoveryClient
public class RedisServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisServerApplication.class);

    }
}
