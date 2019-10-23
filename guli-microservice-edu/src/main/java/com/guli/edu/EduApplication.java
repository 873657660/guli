package com.guli.edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author zhangyanan
 * @create 2019-09-24 11:40
 */
@EnableCircuitBreaker // 熔断机制
@EnableFeignClients
@ComponentScan(basePackages = {"com.guli.common","com.guli.edu"})
@EnableEurekaClient
@SpringBootApplication
public class EduApplication {
    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class, args);
    }
}
