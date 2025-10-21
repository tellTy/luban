package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.Environment;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@EnableDiscoveryClient
public class GatewayServiceApplication {

    public static void main(String[] args) {
        Environment env = SpringApplication.run(GatewayServiceApplication.class, args).getEnvironment();
        System.out.println("spring.main.allow-bean-definition-overriding: " +
                env.getProperty("spring.main.allow-bean-definition-overriding"));
    }
}
