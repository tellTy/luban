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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;



    // LDAP 服务器配置（OpenLDAP）
    private final String ldapUrl = "ldap://openldap:389";
    private final String ldapBaseDn = "dc=example,dc=com";
    private final String ldapUserDnPatterns = "uid={0},ou=users"; // 匹配 LDAP 用户的 DN 格式
    private final String ldapManagerDn = "cn=admin,dc=example,dc=com"; // LDAP 管理员账号
    private final String ldapManagerPassword = "admin"; // LDAP 管理员密码

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 配置 LDAP 认证提供者（通用 OpenLDAP 适用）
    @Bean
    public LdapAuthenticationProvider ldapAuthenticationProvider() {
        // 1. 配置 LDAP 上下文源（连接信息）
        DefaultSpringSecurityContextSource contextSource = new DefaultSpringSecurityContextSource(ldapUrl + "/" + ldapBaseDn);
        contextSource.setUserDn(ldapManagerDn);
        contextSource.setPassword(ldapManagerPassword);
        contextSource.afterPropertiesSet(); // 初始化连接

        // 2. 配置用户搜索（从 LDAP 中查找用户）
        FilterBasedLdapUserSearch userSearch = new FilterBasedLdapUserSearch(
                "ou=users", // 用户所在的 OU（组织单元）
                "(uid={0})", // 搜索过滤器（根据用户名匹配）
                contextSource
        );

        // 3. 配置绑定认证器（验证用户密码）
        BindAuthenticator authenticator = new BindAuthenticator(contextSource);
        authenticator.setUserSearch(userSearch);

        // 4. 创建 LDAP 认证提供者
        LdapAuthenticationProvider ldapProvider = new LdapAuthenticationProvider(authenticator);

        return ldapProvider;
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() {
        // 数据库认证提供者
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(userDetailsService);
        daoProvider.setPasswordEncoder(passwordEncoder());
        daoProvider.setHideUserNotFoundExceptions(true); // 防止用户枚举攻击

        // 组合数据库和 LDAP 认证提供者
        return new ProviderManager(Arrays.asList(daoProvider, ldapAuthenticationProvider()));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // 生产环境需谨慎关闭 CSRF
                // .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 若使用 OAuth2 登录，建议注释
                .authorizeRequests(auth -> auth
                        .antMatchers("/auth/**", "/login/**", "/oauth2/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")  // 统一登录入口页
                        .authorizationEndpoint(auth -> auth.baseUri("/oauth2/authorize"))
                        .redirectionEndpoint(redir -> redir.baseUri("/login/oauth2/code/*"))
                        .userInfoEndpoint()
                        .and()
                        .successHandler((req, res, auth) -> {
                            // 建议生成 token 而非仅返回 200
                            res.setStatus(200);
                        })
                        .failureHandler((req, res, ex) -> res.setStatus(401))
                );

        // 配置 LDAP 登录过滤器，放在表单登录过滤器之前
        LdapLoginFilter ldapLoginFilter = new LdapLoginFilter("/auth/ldap/login", authenticationManager());
        ldapLoginFilter.setUsernameParameter("username");
        ldapLoginFilter.setPasswordParameter("password");
        http.addFilterBefore(ldapLoginFilter, UsernamePasswordAuthenticationFilter.class);
    }
}