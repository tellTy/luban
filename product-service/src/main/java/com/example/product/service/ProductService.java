package com.example.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.product.entity.Product;

import java.util.List;

public interface ProductService extends IService<Product> {

    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product createProduct(Product product);

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);
}
