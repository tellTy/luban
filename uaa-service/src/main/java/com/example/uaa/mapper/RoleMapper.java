package com.example.uaa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.uaa.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper extends BaseMapper<Role> {
    @Select("SELECT * FROM roles WHERE id IN (SELECT role_id FROM user_roles WHERE user_id = #{userId})")
    List<Role> selectByUserId(@Param("userId") Long userId);
}
