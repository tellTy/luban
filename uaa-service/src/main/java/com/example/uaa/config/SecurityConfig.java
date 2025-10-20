package com.example.uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(userDetailsService);
        daoProvider.setPasswordEncoder(passwordEncoder());

        ActiveDirectoryLdapAuthenticationProvider ldapProvider = new ActiveDirectoryLdapAuthenticationProvider(
                "example.com", "ldap://openldap:389/", "dc=example,dc=com");
        ldapProvider.setConvertToUpperCaseBeforeComparison(false);

        return new ProviderManager(Arrays.asList(daoProvider, ldapProvider));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/auth/**", "/login/**", "/oauth2/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/auth/login")
                .successHandler((request, response, authentication) -> {
                    response.setStatus(200);
                })
                .failureHandler((request, response, exception) -> {
                    response.setStatus(401);
                })
                .and()
                .oauth2Login()
                .loginPage("/login")
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .and()
                .redirectionEndpoint()
                .baseUri("/login/oauth2/code/*")
                .and()
                .userInfoEndpoint()
                .and()
                .successHandler((request, response, authentication) -> {
                    response.setStatus(200);
                })
                .failureHandler((request, response, exception) -> {
                    response.setStatus(401);
                });

        // 添加LDAP登录过滤器
        http.addFilterBefore(new LdapLoginFilter("/auth/ldap/login", authenticationManagerBean()),
                OAuth2LoginAuthenticationFilter.class);
    }
}
