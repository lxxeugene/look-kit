package com.example.lookkit.cart;

import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CartMapper {

    @Select("SELECT * FROM cart WHERE user_id = #{userId}")
    List<CartVO> getCartItemsByUserId(@Param("userId") long userId);  

    @Insert("INSERT INTO cart (user_id, product_id, quantity) VALUES (#{userId}, #{productId}, #{quantity})")
    void addToCart(CartVO cartVO); 

    @Update("UPDATE cart SET quantity = #{quantity} WHERE cart_id = #{cartId}")
    void updateCart(@Param("cartId") int cartId, @Param("quantity") int quantity); 

    @Delete({
        "<script>",
        "DELETE FROM cart WHERE cart_id IN ",
        "<foreach item='item' index='index' collection='cartIds' open='(' separator=',' close=')'>",
        "#{item}",
        "</foreach>",
        "</script>"
    })
    void deleteFromCart(@Param("cartIds") List<Integer> cartIds); 

    @Select("SELECT * FROM cart WHERE user_id = #{userId} AND product_id = #{productId} LIMIT 1")
    CartVO findCartItemByUserIdAndProductId(@Param("userId") long userId, @Param("productId") int productId);
}