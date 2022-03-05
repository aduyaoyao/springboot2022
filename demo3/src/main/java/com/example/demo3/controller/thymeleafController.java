package com.example.demo3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class thymeleafController {
    @GetMapping("/testThymeleaf")
    public String testThymeleaf(Model model){
        model.addAttribute("m1", "v1");
        model.addAttribute("link", "https://www.yuque.com/atguigu/springboot/vgzmgh#V14xB");
        return "success2";
    }
}
