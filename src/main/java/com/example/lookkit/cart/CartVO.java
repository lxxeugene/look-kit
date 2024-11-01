package com.example.lookkit.cart;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartVO {
    private int cartId;
    private long userId;
    private int productId;
    private int quantity;
    private String brandName;
    private String productName;
    private String productThumbnail;
    private int productPrice;
    private int codiId;
}
