package com.example.lookkit.order;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import com.example.lookkit.user.UserVO;

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
}











