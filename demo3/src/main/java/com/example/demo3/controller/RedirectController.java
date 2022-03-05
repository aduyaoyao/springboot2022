package com.example.demo3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RedirectController {
    @GetMapping("/goto")
    public String goToPage(HttpServletRequest request){

        request.setAttribute("msg", "success");
        return "forward:/success";
    }

    @ResponseBody
    @GetMapping("/success")
    public Map<String,String> success(@RequestAttribute("msg") String msg){
        Map<String,String> map = new HashMap<>();
        map.put(msg, msg);
        return map;
    }
    @GetMapping("/params")
    public String testParams(Map<String,Object> map,
                             Model model,
                             HttpServletRequest request,
                             HttpServletResponse response){
        map.put("m1", "p1");
        model.addAttribute("model", "value");
        request.setAttribute("re", "asd");

        Cookie cookie = new Cookie("c1", "v1");
        response.addCookie(cookie);
        return "forward:/f";
    }
    @ResponseBody
    @GetMapping("/f")
    public Map<String,Object> success1(HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        Object m1 = request.getAttribute("m1");
        Object model = request.getAttribute("model");
        Object re = request.getAttribute("re");
        map.put("m1", m1);
        map.put("model", model);
        map.put("re", re);
        return map;
    }
}
