package com.example.lookkit.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.lookkit.product.ProductVO;
import com.example.lookkit.product.ProductMapper;
import com.example.lookkit.cart.CartService;
import com.example.lookkit.cart.CartVO;
import java.util.List;

@Service
public class OrderService {

    private final OrderMapper orderMapper;

    @Autowired
    public OrderService(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    public List<OrderDetailDTO> getOrderDetails(List<Integer> selectedItems) {
        return orderMapper.getOrderDetails(selectedItems); // 주문 상세 정보 가져오기
    }

    public void completeOrder(OrderVO orderVO) {
        orderMapper.completeOrder(orderVO); // 주문 정보 저장하기
    }
}