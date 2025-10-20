package com.example.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${security.oauth2.resource-server.jwt.secret}")
    private String jwtSecret;

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKey secretKey = new SecretKeySpec(jwtSecret.getBytes(), "HS512");
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/auth/**", "/login/**", "/oauth2/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .pathMatchers("/products/**").authenticated()
                .anyExchange().authenticated()
                .and()
                .oauth2ResourceServer()
                .jwt();

        return http.build();
    }
}
