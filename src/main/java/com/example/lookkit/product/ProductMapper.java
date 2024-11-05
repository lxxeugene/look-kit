package com.example.lookkit.product;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
}





