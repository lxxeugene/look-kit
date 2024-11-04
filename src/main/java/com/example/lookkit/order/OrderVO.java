package com.example.lookkit.order;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderVO {
    private int orderId;
    private long userId;
    private int totalAmount;
    private String orderComment;
    private String orderDate;
    private String orderAddress;
    private String orderAddressee;
    private String orderPhone;
}
