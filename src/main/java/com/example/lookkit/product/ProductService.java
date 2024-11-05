package com.example.lookkit.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public ProductVO getProductById(int productId) {
        return productMapper.findById(productId);
    }

    public List<ProductVO> getProductsByCategory(String type) {
        return productMapper.getProductsByCategoryType(type);
    }

    public List<ProductVO> searchProductsByKeyword(String keyword) {
        return productMapper.searchProductsByKeyword(keyword);
    }
}