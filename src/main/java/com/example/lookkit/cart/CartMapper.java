package com.example.lookkit.cart;

import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface CartMapper {

    // 사용자 ID와 상품 ID로 장바구니 항목 찾기
    @Select("SELECT * FROM CART WHERE USER_ID = #{userId} AND PRODUCT_ID = #{productId}")
    CartVO findCartItemByUserIdAndProductId(@Param("userId") long userId, @Param("productId") int productId);

    // 사용자 ID로 장바구니 항목 가져오기 (상품 정보 포함)
    @Select("SELECT c.*, p.PRODUCT_NAME, p.PRODUCT_PRICE, p.PRODUCT_THUMBNAIL, p.BRAND_NAME " +
            "FROM CART c " +
            "JOIN PRODUCTS p ON c.PRODUCT_ID = p.PRODUCT_ID " +
            "WHERE c.USER_ID = #{userId}")
    List<CartVO> getCartItemsByUserId(@Param("userId") long userId);

    // 장바구니에 상품 추가
    @Insert("INSERT INTO CART (USER_ID, PRODUCT_ID, QUANTITY) VALUES (#{userId}, #{productId}, #{quantity})")
    @Options(useGeneratedKeys = true, keyProperty = "cartId")
    void addToCart(CartVO cart);

    // 장바구니 수량 업데이트
    @Update("UPDATE CART SET QUANTITY = #{quantity} WHERE CART_ID = #{cartId}")
    void updateCart(@Param("cartId") int cartId, @Param("quantity") int quantity);

    // 장바구니에서 선택한 상품 삭제
    @Delete("<script>" +
            "DELETE FROM CART WHERE CART_ID IN " +
            "<foreach item='cartId' collection='cartIds' open='(' separator=',' close=')'>" +
            "#{cartId}" +
            "</foreach>" +
            "</script>")
    void deleteFromCart(@Param("cartIds") List<Integer> cartIds);
}



