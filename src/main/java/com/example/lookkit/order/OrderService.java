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

    public void completeOrder(OrderVO orderVO) {
        orderMapper.insertOrder(orderVO);
        for (OrderDetailVO detail : orderVO.getOrderDetails()) {
            orderMapper.insertOrderDetail(detail);
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
}



    