package com.example.lookkit.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.lookkit.product.ProductVO;
import com.example.lookkit.user.UserVO;
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

    public void completeOrder(OrderVO orderVO) {
        // ORDERS 테이블에 주문 정보 저장
        orderMapper.insertOrder(orderVO);
    
        // ORDER_ITEMS 테이블에 각 주문 항목 저장
        if (orderVO.getOrderDetails() != null) {
            for (OrderDetailVO detail : orderVO.getOrderDetails()) {
                detail.setOrderId(orderVO.getOrderId()); // ORDER_ID 설정
                detail.setUserId(orderVO.getUserId());
                orderMapper.insertOrderDetail(detail);
            }
        }
    }
    

    public OrderVO getOrderById(int orderId) {
        return orderMapper.findOrderById(orderId);
    }

    public List<OrderDetailDTO> getOrderDetailsByOrderId(int orderId) {
        return orderMapper.findOrderDetailsByOrderId(orderId);
    }

    public AddressVO getUserAddress(long userId) {
        return orderMapper.findUserAddress(userId);
    }

    public void saveAddress(AddressVO addressVO) {
        orderMapper.insertAddress(addressVO);
    }

    public UserVO getUserById(long userId) {
        return orderMapper.findUserById(userId);
    }
}
