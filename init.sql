-- 创建uaa数据库
CREATE DATABASE IF NOT EXISTS uaa_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE uaa_db;

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
                                     id BIGint AUTO_INCREMENT PRIMARY KEY,
                                     username varchar(50) NOT NULL UNIQUE,
    password varchar(100) NOT NULL,
    enabled boolean NOT NULL DEFAULT TRUE,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- 创建角色表
CREATE TABLE IF NOT EXISTS roles (
                                     id bigint AUTO_INCREMENT PRIMARY KEY,
                                     name varchar(50) NOT NULL UNIQUE,
    description varchar(255)
    );

-- 创建用户角色关联表
CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id bigint NOT NULL,
                                          role_id bigint NOT NULL,
                                          PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
    );

-- 插入角色数据
INSERT INTO roles (name, description) VALUES
                                          ('USER', '普通用户'),
                                          ('EDITOR', '编辑'),
                                          ('PRODUCT_ADMIN', '产品管理员');

-- 插入用户数据 (密码：123456)
INSERT INTO users (username, password) VALUES
                                           ('user_1', '$2a$10$8.5fZJ7lF3L6Q4H4QJZf8OeX5QJZf8OeX5QJZf8OeX5QJZf8OeX5'),  -- 密码: user_1
                                           ('editor_1', '$2a$10$8.5fZJ7lF3L6Q4H4QJZf8OeX5QJZf8OeX5QJZf8OeX5QJZf8OeX5'),  -- 密码: editor_1
                                           ('adm_1', '$2a$10$8.5fZJ7lF3L6Q4H4QJZf8OeX5QJZf8OeX5QJZf8OeX5QJZf8OeX5');

-- 插入用户角色关联数据
INSERT INTO user_roles (user_id, role_id) VALUES
                                              (1, 1), -- user_1 -> USER
                                              (2, 2), -- editor_1 -> EDITOR
                                              (3, 3); -- adm_1 -> PRODUCT_ADMIN

-- 创建product数据库
CREATE DATABASE IF NOT EXISTS product_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE product_db;

-- 创建产品表
CREATE TABLE IF NOT EXISTS products (
                                        id bigint AUTO_INCREMENT PRIMARY KEY,
                                        name varchar(100) NOT NULL,
    created_at timestamp NOT NULL DEFAULT DEFAULT CURRENT CURRENT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- 插入测试产品数据
INSERT INTO products (name) VALUES
                                ('产品1'),
                                ('产品2'),
                                ('产品3');

-- 创建nacos配置数据库
CREATE DATABASE IF NOT EXISTS nacos_config CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
