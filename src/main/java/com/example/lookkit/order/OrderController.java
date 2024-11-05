package com.example.lookkit.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping
    public String getOrderPage(Authentication authentication, 
                               @RequestParam(required = false) Integer productId, 
                               @RequestParam(required = false) Integer quantity, 
                               Model model) {
        CustomUser user = (CustomUser) authentication.getPrincipal();
        List<OrderDetailDTO> orderItems = new ArrayList<>();

        if (productId != null && quantity != null) {
            // Product 페이지에서 바로 구매하기 버튼을 누른 경우
            ProductVO product = productService.getProductById(productId);
            if (product != null) {
                OrderDetailDTO orderDetail = new OrderDetailDTO(productId, product.getProductName(), product.getBrandName(), quantity, product.getProductPrice());
                orderItems.add(orderDetail);
            }
        } else {
            // Cart 페이지에서 구매하기 버튼을 누른 경우
            List<CartVO> cartItems = cartService.getCartItemsByUserId(user.getUserId());
            if (!cartItems.isEmpty()) {
                orderItems = cartItems.stream()
                        .map(cart -> new OrderDetailDTO(cart.getProductId(), cart.getProductName(), cart.getBrandName(), cart.getQuantity(), cart.getProductPrice()))
                        .collect(Collectors.toList());
            }
        }

        if (orderItems.isEmpty()) {
            model.addAttribute("errorMessage", "주문할 상품 정보가 없습니다. 장바구니를 확인해주세요.");
            return "cart"; // 주문 정보가 없으면 장바구니로 되돌림
        }

        int totalAmount = orderItems.stream().mapToInt(item -> item.getProductPrice() * item.getQuantity()).sum();
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("userAddress", orderService.getUserAddress(user.getUserId()));  // 사용자 주소 정보 가져오기

        return "order";
    }



    @PostMapping("/complete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> completeOrder(@RequestBody OrderVO orderVO, Authentication authentication) {
        CustomUser user = (CustomUser) authentication.getPrincipal();
        orderVO.setUserId(user.getUserId());
        orderService.completeOrder(orderVO);

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", orderVO.getOrderId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orderComplete")
    public String orderCompletePage() {
        return "orderComplete";
    }

    @GetMapping("/complete/details/{orderId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getOrderCompleteDetails(@PathVariable int orderId) {
        OrderVO order = orderService.getOrderById(orderId);
        List<OrderDetailDTO> orderDetails = orderService.getOrderDetailsByOrderId(orderId);

        Map<String, Object> response = new HashMap<>();
        response.put("order", order);
        response.put("orderDetails", orderDetails);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/addAddress")
    public String addAddressPage() {
        return "addAddress";
    }

    @PostMapping("/addAddress")
    public String addAddress(@RequestBody AddressVO addressVO, Authentication authentication) {
        CustomUser user = (CustomUser) authentication.getPrincipal();
        addressVO.setUserId(user.getUserId());
        orderService.saveAddress(addressVO);
        return "redirect:/order";
    }
}



