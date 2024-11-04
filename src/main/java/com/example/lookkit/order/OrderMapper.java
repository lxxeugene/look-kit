package com.example.lookkit.order;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Delete;
import java.util.List;

@Mapper
public interface OrderMapper {
    @Select("<script>" +
        "SELECT * FROM order_details WHERE product_id IN " +
        "<foreach item='item' index='index' collection='selectedItems' open='(' separator=',' close=')'>" +
        "#{item}" +
        "</foreach>" +
        "</script>")
    List<OrderDetailDTO> getOrderDetails(@Param("selectedItems") List<Integer> selectedItems);

    @Insert("INSERT INTO orders (user_id, total_amount, order_status, order_comment, order_date, order_address, order_addressee, order_phone) VALUES (#{userId}, #{totalAmount}, 'pending', #{orderComment}, #{orderDate}, #{orderAddress}, #{orderAddressee}, #{orderPhone})")
    void completeOrder(OrderVO orderVO); // 주문 정보 저장 쿼리 매핑
}


