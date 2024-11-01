package com.example.lookkit.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartMapper cartMapper;

    @Autowired
    public CartService(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    public List<CartVO> getCartItemsByUserId(long userId) {
        return cartMapper.getCartItemsByUserId(userId); 
    }

    public void addToCart(CartVO cartVO) {
        cartMapper.addToCart(cartVO); 
    }

    public void updateCart(int cartId, int quantity) {
        cartMapper.updateCart(cartId, quantity); 
    }

    public void deleteFromCart(List<Integer> cartIds) {
        cartMapper.deleteFromCart(cartIds);
    }    

    public CartVO findCartItemByUserIdAndProductId(long userId, int productId) {
        return cartMapper.findCartItemByUserIdAndProductId(userId, productId);
    }
}
