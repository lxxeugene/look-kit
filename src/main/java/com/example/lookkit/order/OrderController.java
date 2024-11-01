package com.example.lookkit.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.lookkit.cart.CartService;
import com.example.lookkit.cart.CartVO;
import com.example.lookkit.product.ProductService;
import com.example.lookkit.product.ProductVO;
import com.example.lookkit.user.CustomUser;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;
    private final ProductService productService;

    @Autowired
    public OrderController(OrderService orderService, CartService cartService, ProductService productService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.productService = productService;
    }

    private List<OrderDetailDTO> fetchOrderDetails(long userId) {
        List<CartVO> cartItems = cartService.getCartItemsByUserId(userId);

        return cartItems.stream().map(cartItem -> {
            ProductVO product = productService.getProductById(cartItem.getProductId());
            return OrderDetailDTO.builder()
                    .productId(cartItem.getProductId())
                    .productName(product.getProductName())
                    .brandName(product.getBrandName())
                    .quantity(cartItem.getQuantity())
                    .productPrice(product.getProductPrice())
                    .build();
        }).toList();
    }

    @GetMapping
    public String getOrderPage(Authentication authentication, Model model) {
        CustomUser user = (CustomUser) authentication.getPrincipal();
        long userId = user.getUserId();

        List<OrderDetailDTO> orderItems = fetchOrderDetails(userId);
        int totalAmount = orderItems.stream().mapToInt(item -> item.getProductPrice() * item.getQuantity()).sum();
        int totalQuantity = orderItems.stream().mapToInt(OrderDetailDTO::getQuantity).sum();

        model.addAttribute("orderItems", orderItems);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("totalQuantity", totalQuantity);

        return "order";
    }

    @GetMapping("/details")
    @ResponseBody
    public List<OrderDetailDTO> getOrderDetails(Authentication authentication) {
        CustomUser user = (CustomUser) authentication.getPrincipal();
        long userId = user.getUserId();

        return fetchOrderDetails(userId);
    }

    @PostMapping
    public String handleOrder(@RequestParam int productId, @RequestParam int quantity, Authentication authentication, Model model) {
        CustomUser user = (CustomUser) authentication.getPrincipal();
        long userId = user.getUserId();

        ProductVO product = productService.getProductById(productId);
        OrderDetailDTO orderItem = OrderDetailDTO.builder()
                .productId(productId)
                .productName(product.getProductName())
                .brandName(product.getBrandName())
                .quantity(quantity)
                .productPrice(product.getProductPrice())
                .build();

        List<OrderDetailDTO> orderItems = List.of(orderItem);
        int totalAmount = product.getProductPrice() * quantity;

        model.addAttribute("orderItems", orderItems);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("totalQuantity", quantity);

        return "order";
    }

    @GetMapping("/addAddress")
    public String addAddressPage() {
        return "addAddress";
    }

    @GetMapping("/orderComplete")
    public String orderCompletePage(Model model) {
       return "orderComplete";
    }
}



