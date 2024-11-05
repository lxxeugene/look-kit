package com.example.lookkit.order;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.lookkit.product.ProductVO;

import org.apache.ibatis.annotations.Delete;
import java.util.List;

@Mapper
public interface OrderMapper {

    @Insert("INSERT INTO ORDERS (USER_ID, TOTAL_AMOUNT) VALUES (#{userId}, #{totalAmount})")
    @Options(useGeneratedKeys = true, keyProperty = "orderId")
    void insertOrder(OrderVO orderVO);

    @Insert("INSERT INTO ORDER_ITEMS (ORDER_ID, PRODUCT_ID, QUANTITY, PRODUCT_PRICE) VALUES (#{orderId}, #{productId}, #{quantity}, #{productPrice})")
    void insertOrderDetail(OrderDetailVO orderDetailVO);

    @Select("SELECT * FROM ORDERS WHERE ORDER_ID = #{orderId}")
    OrderVO findOrderById(int orderId);

    @Select("SELECT PRODUCT_ID, PRODUCT_NAME, BRAND_NAME, QUANTITY, PRODUCT_PRICE FROM ORDER_ITEMS WHERE ORDER_ID = #{orderId}")
    List<OrderDetailDTO> findOrderDetailsByOrderId(int orderId);

    @Select("SELECT * FROM ADDRESS_BOOK WHERE USER_ID = #{userId}")
    AddressVO findUserAddress(long userId);

    @Insert("INSERT INTO ADDRESS_BOOK (USER_ID, ADDRESS_NAME, ADDRESS) VALUES (#{userId}, #{addressName}, #{address})")
    void insertAddress(AddressVO addressVO);
}








