package com.example.lookkit.common.dto;

import lombok.*;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserOrderDTO {
        private int orderId;            // 주문 ID
        private String userUuid;        // 회원 UUID
        private int totalAmount;        // 주문 총액
        private String orderStatus;     // 주문 상태
        private String orderComment;    // 주문 요청사항
        private String orderDate;       // 주문 날짜
        private String orderAddress;    // 주문 주소
        private String orderAddressee;  // 수령인
        private String orderPhone;      // 연락처
}
