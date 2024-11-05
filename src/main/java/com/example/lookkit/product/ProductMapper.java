package com.example.lookkit.product;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductMapper {

    // 상품 ID를 통해 상품 정보 가져오기
    @Select("SELECT * FROM PRODUCTS WHERE PRODUCT_ID = #{productId}")
    ProductVO findById(int productId);

    // 상품의 모든 정보 가져오기
    @Select("SELECT * FROM PRODUCTS")
    List<ProductVO> findAll();

    // 상품 이름으로 검색
    @Select("SELECT * FROM PRODUCTS WHERE PRODUCT_NAME LIKE CONCAT('%', #{productName}, '%')")
    List<ProductVO> searchByName(String productName);
    @Select("SELECT * FROM products WHERE product_id = #{productId}")
    ProductVO getProductById(int productId);



    @Select("SELECT p.* " +
            "FROM products p " +
            "JOIN categories c ON p.category_id = c.category_id " +
            "WHERE c.category_type = #{categoryType}")
    List<ProductVO> getProductsByCategoryType(String categoryType);

    // 키워드로 상품명 or 설명에서 검색
    @Select("SELECT * FROM products " +
            "WHERE product_name " +
            "LIKE CONCAT('%', #{keyword}, '%') " +
            "OR product_description " +
            "LIKE CONCAT('%', #{keyword}, '%')")
    List<ProductVO> searchProductsByKeyword(String keyword);

}





