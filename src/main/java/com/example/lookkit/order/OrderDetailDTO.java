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
}
