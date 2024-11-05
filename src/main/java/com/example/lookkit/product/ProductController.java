package com.example.lookkit.product;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.lookkit.cart.CartVO;
import com.example.lookkit.order.OrderDetailVO;
import com.example.lookkit.order.OrderService;
import com.example.lookkit.order.OrderVO;
import com.example.lookkit.user.CustomUser;
import com.example.lookkit.cart.CartService;


@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;

    @Autowired
    public ProductController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping("/{productId}")
    public String getProductDetails(@PathVariable int productId, Model model) {
        ProductVO product = productService.getProductById(productId);
        model.addAttribute("product", product);
        return "product"; 
    }

    @PostMapping("/add")
    public String addToCart(@ModelAttribute CartVO cartVO, Authentication authentication) {
        CustomUser user = (CustomUser) authentication.getPrincipal();
        cartVO.setUserId(user.getUserId());

        CartVO existingCartItem = cartService.findCartItemByUserIdAndProductId(user.getUserId(), cartVO.getProductId());
        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartVO.getQuantity());
            cartService.updateCart(existingCartItem.getCartId(), existingCartItem.getQuantity());
        } else {
            cartService.addToCart(cartVO);
        }
        return "redirect:/cart"; 
    }

    @PostMapping("/buy-now")
    @ResponseBody
        public ResponseEntity<Map<String, Object>> buyNow(@RequestBody Map<String, Object> payload) {
        int productId = Integer.parseInt(payload.get("productId").toString());
        int quantity = Integer.parseInt(payload.get("quantity").toString());

        Map<String, Object> response = new HashMap<>();
        response.put("productId", productId);
        response.put("quantity", quantity);
        return ResponseEntity.ok(response);
    }

}


