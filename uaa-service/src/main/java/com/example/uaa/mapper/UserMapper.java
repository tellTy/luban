package com.example.uaa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.uaa.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {

    User selectByUsername(@Param("username") String username);
}
