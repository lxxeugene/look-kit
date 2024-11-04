package com.example.lookkit.admin;

import com.example.lookkit.common.dto.InquiryUserDTO;
import com.example.lookkit.inquiry.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    public final InquiryService is;

    @GetMapping("dashboard")
    public void dashboard(Model model){
        List<InquiryUserDTO> inquiryList = is.getInquiryAllList();

        // 람다식을 이용해 inquiryList의 각 항목을 콘솔에 출력
        inquiryList.forEach(inquiry -> System.out.println("Inquiry: " + inquiry));

        model.addAttribute("inquiryList", inquiryList);
    }


    @GetMapping("inqury")
    public void inqury(Model model){

    }

}
