package com.example.uaa.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("user_roles")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long userId;

    @TableId
    private Long roleId;
}
