package com.example.lookkit.order;

import com.example.lookkit.common.dto.UserOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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



    public List<UserOrderDTO> orderAllListWithUserUuid(){
        return orderMapper.getAllOrdersWithUserUuid();
    }


    public int updateOrderStatus(int orderId,String orderStatus){
        return orderMapper.updateOrderStatus(orderId,orderStatus);
    }



}