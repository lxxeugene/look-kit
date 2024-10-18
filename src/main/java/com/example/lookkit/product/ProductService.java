package com.example.lookkit.product;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public ProductVO getProductById(long productId) {
        ProductVO product = productMapper.getProductById(productId);
        // List<ProductImageVO> images = productMapper.getProductImagesByProductId(productId);
        // product.setProductImages(images); 
        return product;
    }
}
