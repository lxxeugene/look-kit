package com.example.lookkit.product;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductVO {
    private int productId;
    private int categoryId;
    private int codiId;
    private String productName;
    private String productDescription;
    private int productPrice;
    private int productStock;
    private String brandName;
    private String productThumbnail;  
}