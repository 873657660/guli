package com.guli.statistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author shkstart
 * @create 2019-10-14 11:55
 */
@EnableFeignClients
@SpringBootApplication
@ComponentScan(basePackages={"com.guli.statistics","com.guli.common"})
@EnableEurekaClient
@EnableCircuitBreaker
public class StatisticsApplication {
    public static void main(String[] args) {
        SpringApplication.run(StatisticsApplication.class, args);
    }
}