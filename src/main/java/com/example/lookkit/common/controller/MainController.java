package com.example.lookkit.common.controller;

import com.example.lookkit.coordiset.CoordiProductsDTO;
import com.example.lookkit.coordiset.CoordisetService;
import com.example.lookkit.user.CustomUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class MainController {

    private final CoordisetService coordisetService;

    @GetMapping("/main")
    public String mainPage(Authentication auth, HttpSession session, Model model){
        // auth가 빈값이 아닐 때 세션 저장
        if(auth != null){
            System.out.println(auth);
            CustomUser user = (CustomUser) auth.getPrincipal();
            session.setAttribute("userid",user.getUserId());
            System.out.println("저장된 유저 유저아이디 : "+user.getUserId());
            // Session의 유효 시간 설정 (3600초 = 60분)
             session.setMaxInactiveInterval(3600);
//            System.out.println(user.getUserId());
//            System.out.println(auth.getName());
//            System.out.println(auth.getAuthorities());
//            System.out.println(auth.isAuthenticated());
        }
        List<CoordiProductsDTO> coordiList = coordisetService.getOldestEightCoordiWithProducts();
        model.addAttribute("coordiList", coordiList);
        return "/home/home.html";
    }
}
