package com.example.lookkit.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lookkit.product.ProductService;
import com.example.lookkit.product.ProductVO;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductService productService;

    // 사용자 ID로 장바구니 아이템 조회
    public List<CartVO> getCartItemsByUserId(long userId) {
        return cartMapper.getCartItemsByUserId(userId);
    }

    // 장바구니에 상품 추가
    public void addToCart(CartVO cart) {
        CartVO existingCartItem = cartMapper.findCartItemByUserIdAndProductId(cart.getUserId(), cart.getProductId());
        if (existingCartItem != null) {
            // 상품이 이미 장바구니에 있을 경우 수량 업데이트
            int updatedQuantity = existingCartItem.getQuantity() + cart.getQuantity();
            cartMapper.updateCart(existingCartItem.getCartId(), updatedQuantity);
        } else {
            // 상품이 장바구니에 없을 경우 새로 추가
            cartMapper.addToCart(cart);
        }
    }

    // 장바구니 상품 수량 업데이트
    public void updateCart(int cartId, int quantity) {
        if (quantity > 0) {
            cartMapper.updateCart(cartId, quantity);
        } else {
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }
    }

    // 장바구니에서 선택된 상품 삭제
    public void deleteFromCart(List<Integer> cartIds) {
        cartMapper.deleteFromCart(cartIds);
    }

    // 장바구니 총 금액 및 수량 계산
    public int calculateTotalAmount(List<CartVO> cartItems) {
        return cartItems.stream().mapToInt(item -> item.getProductPrice() * item.getQuantity()).sum();
    }

    public int calculateTotalQuantity(List<CartVO> cartItems) {
        return cartItems.stream().mapToInt(CartVO::getQuantity).sum();
    }

    // 사용자 ID로 장바구니와 해당 상품 정보 가져오기
    public List<CartVO> fetchCartItemsWithDetails(long userId) {
        List<CartVO> cartItems = getCartItemsByUserId(userId);
        cartItems.forEach(cartItem -> {
            ProductVO product = productService.getProductById(cartItem.getProductId());
            cartItem.setProductName(product.getProductName());
            cartItem.setProductThumbnail(product.getProductThumbnail());
            cartItem.setProductPrice(product.getProductPrice());
            cartItem.setBrandName(product.getBrandName());
        });
        return cartItems;
    }

    public CartVO findCartItemByUserIdAndProductId(long userId, int productId) {
        return cartMapper.findCartItemByUserIdAndProductId(userId, productId);
    }
}




