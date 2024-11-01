package com.example.lookkit.product;

import lombok.RequiredArgsConstructor;

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
        return productMapper.getProductById(productId); 
    }

    public List<ProductVO> getProductsByCategory(String type) {
        throw new UnsupportedOperationException("Unimplemented method 'getProductsByCategory'");
    }

    public List<ProductVO> searchProductsByKeyword(String keyword) {
        throw new UnsupportedOperationException("Unimplemented method 'searchProductsByKeyword'");
    }
}