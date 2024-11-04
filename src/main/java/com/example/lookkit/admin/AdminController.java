package com.example.lookkit.admin;

import com.example.lookkit.common.dto.InquiryUserDTO;
import com.example.lookkit.common.dto.UserOrderDTO;
import com.example.lookkit.inquiry.InquiryService;
import com.example.lookkit.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    public final InquiryService is;
    public final OrderService os;

    @GetMapping("/dashboard")
    public void dashboard(Model model){
        List<InquiryUserDTO> inquiryList = is.getInquiryAllList();
        inquiryList.forEach(inquiry -> System.out.println("Inquiry: " + inquiry));
        model.addAttribute("inquiryList", inquiryList);
    }


    @GetMapping("/inqury")
    public void inqury(Model model){
    }

    @GetMapping("/orderStatus")
    public void orderStatus(Model model){
     List<UserOrderDTO> orderList =  os.orderAllListWithUserUuid();
        orderList.forEach(order -> System.out.println("order: " + order));
        model.addAttribute("orderList",orderList);
    }


    @PostMapping("/updateOrderStatus")
    @ResponseBody
    public Map<String, Object> updateOrderStatus(@RequestBody Map<String, String> requestData){
        Map<String, Object> response = new HashMap<>();
        try {
            int orderId = Integer.parseInt(requestData.get("orderId"));
            String orderStatus = requestData.get("orderStatus");

            System.out.println("주문번호:" + orderId);
            System.out.println("주문상태:" + orderStatus);

            int result = os.updateOrderStatus(orderId, orderStatus);
            if (result > 0) {
                response.put("success", true);
                response.put("message", "주문 상태가 성공적으로 업데이트되었습니다.");
            } else {
                response.put("success", false);
                response.put("message", "주문 상태 업데이트에 실패했습니다.");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "오류 발생: " + e.getMessage());
        }

        return response;
    }


}
