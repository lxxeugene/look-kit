package com.example.lookkit.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.lookkit.cart.CartVO;
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
        cartService.addToCart(cartVO);
        return "redirect:/cart"; 
    }

    @PostMapping("/buy-now")
    public String buyNow(@RequestParam int productId, @RequestParam int quantity) {
        return "redirect:/order"; 
    }
}