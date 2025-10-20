package com.example.uaa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.uaa.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> selectByUserId(@Param("userId") Long userId);
}
