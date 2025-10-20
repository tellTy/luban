package com.example.product.controller;

import com.example.product.entity.Product;
import com.example.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "Product API", description = "产品管理接口")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    @Operation(summary = "获取所有产品", description = "需要USER角色")
    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('PRODUCT_ADMIN')")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取产品", description = "需要USER角色")
    @PreAuthorize("hasRole('USER') or hasRole('EDITOR') or hasRole('PRODUCT_ADMIN')")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping
    @Operation(summary = "创建产品", description = "需要EDITOR角色")
    @PreAuthorize("hasRole('EDITOR') or hasRole('PRODUCT_ADMIN')")
    public Product createProduct(@RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新产品", description = "需要EDITOR角色")
    @PreAuthorize("hasRole('EDITOR') or hasRole('PRODUCT_ADMIN')")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除产品", description = "需要EDITOR角色")
    @PreAuthorize("hasRole('EDITOR') or hasRole('PRODUCT_ADMIN')")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "Product deleted successfully";
    }
}
