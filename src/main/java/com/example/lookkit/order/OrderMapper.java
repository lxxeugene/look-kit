package com.example.lookkit.order;

import com.example.lookkit.common.dto.UserOrderDTO;
import org.apache.ibatis.annotations.*;

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



    // 모든 주문 목록 가져오기
    @Select("SELECT o.order_id, u.user_uuid AS userUuid, o.total_amount, o.order_status, " +
            "o.order_comment, o.order_date, o.order_addressee, o.order_address, o.order_phone " +
            "FROM orders o " +
            "JOIN users u ON o.user_id = u.user_id " +
            "ORDER BY o.order_date DESC")
    @Results({
            @Result(property = "orderId", column = "order_id"),
            @Result(property = "userUuid", column = "userUuid"),
            @Result(property = "totalAmount", column = "total_amount"),
            @Result(property = "orderStatus", column = "order_status"),
            @Result(property = "orderComment", column = "order_comment", typeHandler = org.apache.ibatis.type.StringTypeHandler.class),
            @Result(property = "orderDate", column = "order_date"),
            @Result(property = "orderAddressee", column = "order_addressee"),
            @Result(property = "orderAddress", column = "order_address"),
            @Result(property = "orderPhone", column = "order_phone")
    })
    List<UserOrderDTO> getAllOrdersWithUserUuid();

    @Update("UPDATE ORDERS SET ORDER_STATUS = #{orderStatus} WHERE ORDER_ID = #{orderId}")
    int updateOrderStatus(@Param("orderId") int orderId, @Param("orderStatus") String orderStatus);

}


