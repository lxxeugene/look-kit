package com.example.lookkit.order;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDetailDTO {
    private int productId;
    private int quantity;
    private int productPrice;
    private String productName;
    private String brandName;

    public OrderDetailDTO(int productId, String productName, String brandName, int quantity, int productPrice) {
        this.productId = productId;
        this.productName = productName;
        this.brandName = brandName;
        this.quantity = quantity;
        this.productPrice = productPrice;
    }
}
