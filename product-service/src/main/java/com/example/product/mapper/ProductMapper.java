package com.example.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.product.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMapper extends BaseMapper<Product> {
}
