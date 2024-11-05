package com.example.lookkit.order;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import com.example.lookkit.user.UserVO;
import com.example.lookkit.common.dto.UserOrderDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO ORDERS (USER_ID, TOTAL_AMOUNT, ORDER_DATE, ORDER_STATUS, ORDER_ADDRESSEE, ORDER_ADDRESS, ORDER_PHONE, ORDER_COMMENT) " +
            "VALUES (#{userId}, #{totalAmount}, #{orderDate}, #{orderStatus}, #{orderAddressee}, #{orderAddress}, #{orderPhone}, #{orderComment})")
    @Options(useGeneratedKeys = true, keyProperty = "orderId")
    void insertOrder(OrderVO orderVO);

    @Insert("INSERT INTO ORDER_ITEMS (ORDER_ID, PRODUCT_ID, USER_ID, QUANTITY) VALUES (#{orderId}, #{productId}, #{userId}, #{quantity})")
    void insertOrderDetail(OrderDetailVO orderDetailVO);


    @Select("SELECT * FROM ORDERS WHERE ORDER_ID = #{orderId}")
    OrderVO findOrderById(int orderId);

    @Select("SELECT ORDER_ITEM_ID, PRODUCT_ID, QUANTITY, USER_ID " +
            "FROM ORDER_ITEMS WHERE ORDER_ID = #{orderId}")
    List<OrderDetailDTO> findOrderDetailsByOrderId(int orderId);

    @Select("SELECT * FROM ADDRESS_BOOK WHERE USER_ID = #{userId}")
    AddressVO findUserAddress(long userId);

    @Insert("INSERT INTO ADDRESS_BOOK (USER_ID, ADDRESS_NAME) VALUES (#{userId}, #{addressName})")
    void insertAddress(AddressVO addressVO);

    @Select("SELECT USER_ID, USER_NAME, EMAIL, PHONE, ADDRESS, CREATED_AT, LAST_UPDATE, ROLE " +
            "FROM USERS WHERE USER_ID = #{userId}")
    UserVO findUserById(long userId);
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











