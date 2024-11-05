package com.example.lookkit.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.lookkit.product.ProductService;
import com.example.lookkit.product.ProductVO;
import com.example.lookkit.user.CustomUser;
import java.util.List;


@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;

    @Autowired
    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    @GetMapping
    public String getCartPage(Model model, Authentication authentication) {
        return "cart/cart";
    }

    @GetMapping("/items")
    @ResponseBody
    public ResponseEntity<List<CartVO>> getCartItems(Authentication authentication) {
        CustomUser user = (CustomUser) authentication.getPrincipal();
        List<CartVO> cartItems = fetchCartItemsWithDetails(authentication);
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/update")
    @ResponseBody
    public void updateCart(@RequestParam int cartId, @RequestParam int quantity) {
        cartService.updateCart(cartId, quantity);
    }

    @PostMapping("/delete")
    public String deleteFromCart(@RequestParam("cartIds") List<Integer> cartIds) {
        cartService.deleteFromCart(cartIds);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Authentication authentication, Model model) {
    CustomUser user = (CustomUser) authentication.getPrincipal();
    long userId = user.getUserId();

    List<CartVO> cartItems = fetchCartItemsWithDetails(authentication);
    if (cartItems.isEmpty()) {
        model.addAttribute("errorMessage", "주문할 상품이 없습니다. 장바구니를 확인해 주세요.");
        return "cart"; // 주문할 상품이 없으면 장바구니로 되돌립니다.
    }

    int totalAmount = cartItems.stream().mapToInt(item -> item.getProductPrice() * item.getQuantity()).sum();
    int totalQuantity = cartItems.stream().mapToInt(CartVO::getQuantity).sum();

    model.addAttribute("orderItems", cartItems);
    model.addAttribute("totalAmount", totalAmount);
    model.addAttribute("totalQuantity", totalQuantity);
    return "redirect:/order";
}


    private List<CartVO> fetchCartItemsWithDetails(Authentication authentication) {
        CustomUser user = (CustomUser) authentication.getPrincipal();
        long userId = user.getUserId();

        List<CartVO> cartItems = cartService.getCartItemsByUserId(userId);
        cartItems.forEach(cartItem -> {
            ProductVO product = productService.getProductById(cartItem.getProductId());
            cartItem.setProductName(product.getProductName());
            cartItem.setProductThumbnail(product.getProductThumbnail());
            cartItem.setProductPrice(product.getProductPrice());
            cartItem.setBrandName(product.getBrandName()); 
        });

        return cartItems;
    }
}




