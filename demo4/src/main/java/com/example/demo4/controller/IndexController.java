package com.example.demo4.controller;

import com.example.demo4.beans.Account;
import com.example.demo4.beans.User;
import com.example.demo4.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {

    /**
     *去往登录页
     * @return
     */
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @PostMapping("/login")
    public String main(User user, HttpSession session, Model model){
        if(!StringUtils.isEmpty(user.getUserName())){
            session.setAttribute("loginUser", user);
            //登录重定向到main.html，防止表单重复提交
            return "redirect:/main.html";
        }else {
            model.addAttribute("msg", "账户密码错误");
            return "login";
        }
    }

    @GetMapping("/main.html")
    public String mainPage(HttpSession session,Model model){
//        //是否登录成功。main页面自己的拦截内容
//        Object loginUser = session.getAttribute("loginUser");
//        if(loginUser != null){
//            return "main";
//        }else{
//            model.addAttribute("msg", "请登录");
//            return "login";
//        }

        return "main";
    }

    @Autowired
    private AccountMapper accountMapper;

    @ResponseBody
    @GetMapping("/account")
    public Account getAccount(@RequestParam("id") int id){
        return accountMapper.getAccount1(id);
//        return accountMapper.getAccount(id);
    }
}
