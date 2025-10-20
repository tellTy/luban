package com.example.uaa;

import org.mybatis.spring.annotation.MapperScan;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.example.uaa.mapper")
public class UaaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UaaServiceApplication.class, args);
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("uaa-public")
                .pathsToMatch("/auth/**", "/login/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("uaa-admin")
                .pathsToMatch("/admin/**")
                .build();
    }
}
