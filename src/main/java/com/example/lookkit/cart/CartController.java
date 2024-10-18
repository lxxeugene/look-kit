package com.example.lookkit.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public String cartPage(@RequestParam(value = "addToCart", required = false) boolean addToCart,
                           @RequestParam(value = "productId", required = false) Integer productId,
                           @RequestParam(value = "quantity", required = false) Integer quantity,
                           Model model) {
        int userId = 1; 
        if (addToCart && productId != null && quantity != null) {
            CartVO cartVO = new CartVO();
            cartVO.setUserId(userId);
            cartVO.setProductId(productId);
            cartVO.setQuantity(quantity);
            cartService.addItemToCart(cartVO);
        }
        List<CartVO> cartItems = cartService.getCartItems(userId);
        model.addAttribute("cartItems", cartItems);
        return "cart"; 
    }

    @GetMapping("/{userId}")
    @ResponseBody
    public List<CartVO> getCartItems(@PathVariable int userId) {
        return cartService.getCartItems(userId);
    }

    @PostMapping("/add")
    @ResponseBody
    public String addItemToCart(@RequestBody CartVO cartVO) {
        cartService.addItemToCart(cartVO);
        return "Item added to cart successfully.";
    }

    @PutMapping("/update")
    @ResponseBody
    public String updateCartItem(@RequestBody CartVO cartVO) {
        cartService.updateCartItem(cartVO);
        return "Item updated successfully.";
    }

    @DeleteMapping("/delete/{cartId}")
    @ResponseBody
    public String deleteCartItem(@PathVariable int cartId) {
        cartService.deleteCartItem(cartId);
        return "Item deleted successfully.";
    }

 
    @PostMapping("/selected")
    @ResponseBody
    public List<CartVO> getSelectedCartItems(@RequestBody List<Integer> cartIds) {
        return cartService.getSelectedCartItems(cartIds);
    }
}
