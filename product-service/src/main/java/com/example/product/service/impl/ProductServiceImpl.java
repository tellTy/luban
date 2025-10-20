package com.example.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.product.entity.Product;
import com.example.product.mapper.ProductMapper;
import com.example.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public List<Product> getAllProducts() {
        return baseMapper.selectList(null);
    }

    @Override
    public Product getProductById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public Product createProduct(Product product) {
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        baseMapper.insert(product);
        return product;
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = baseMapper.selectById(id);
        if (existingProduct == null) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        existingProduct.setName(product.getName());
        existingProduct.setUpdatedAt(LocalDateTime.now());
        baseMapper.updateById(existingProduct);
        return existingProduct;
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = baseMapper.selectById(id);
        if (product == null) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        baseMapper.deleteById(id);
    }
}
