package com.example.uaa.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LdapLoginFilter extends AbstractAuthenticationProcessingFilter {

    // -------------------------- 可配置属性 --------------------------
    // 用户名参数名（默认 "username"，可根据前端调整）
    private String usernameParameter = "username";
    // 密码参数名（默认 "password"，可根据前端调整）
    private String passwordParameter = "password";
    // 是否仅支持 POST 请求（默认 true，增强安全性）
    private boolean postOnly = true;
    // JSON 解析器（用于处理请求体和返回结果）
    private ObjectMapper objectMapper = new ObjectMapper();


    // -------------------------- 构造方法（必选） --------------------------
    // 传入登录路径和认证管理器（固定逻辑）
    public LdapLoginFilter(String loginUrl, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(loginUrl, "POST")); // 仅处理 POST 请求
        setAuthenticationManager(authenticationManager);
    }


    // -------------------------- 核心认证逻辑（解析请求） --------------------------
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {

        // 1. 检查请求方法（如果配置了 postOnly=true，则拒绝非 POST 请求）
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new IllegalArgumentException("LDAP 登录仅支持 POST 方法");
        }

        // 2. 解析请求中的用户名和密码（支持 JSON 格式和表单格式）
        String username = null;
        String password = null;

        Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), Map.class);
        username = obtainUsername(requestBody);
        password = obtainPassword(requestBody);

        // 3. 校验用户名密码不为空
        if (username == null) username = "";
        if (password == null) password = "";
        username = username.trim();

        // 4. 创建认证令牌，交给 AuthenticationManager 处理
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        return this.getAuthenticationManager().authenticate(authRequest);
    }


    // -------------------------- 认证成功/失败的处理（可自定义） --------------------------
    // 认证成功后调用（默认返回 200，可扩展为返回 JWT 令牌）
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            javax.servlet.FilterChain chain, Authentication authResult) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);

        // 构建成功响应（例如返回用户名和角色）
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("code", 200);
        successResponse.put("message", "LDAP 登录成功");
        successResponse.put("username", authResult.getName());
        successResponse.put("roles", authResult.getAuthorities());

        objectMapper.writeValue(response.getWriter(), successResponse);
    }

    // 认证失败后调用（默认返回 401，可自定义错误信息）
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 构建失败响应
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", 401);
        errorResponse.put("message", "LDAP 登录失败：" + failed.getMessage());

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }


    // -------------------------- Getter/Setter（用于配置属性） --------------------------
    // 允许外部修改用户名参数名（如前端用 "userName" 而非 "username"）
    public void setUsernameParameter(String usernameParameter) {
        this.usernameParameter = usernameParameter;
    }

    // 允许外部修改密码参数名
    public void setPasswordParameter(String passwordParameter) {
        this.passwordParameter = passwordParameter;
    }

    // 允许外部关闭 postOnly 限制（不推荐，除非有特殊需求）
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    // 允许自定义 JSON 解析器（如配置日期格式等）
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    // -------------------------- 辅助方法（提取用户名密码） --------------------------
    private String obtainUsername(Map<String, String> requestBody) {
        return requestBody.get(usernameParameter);
    }

    private String obtainPassword(Map<String, String> requestBody) {
        return requestBody.get(passwordParameter);
    }

}