package com.atguigu.boot.controller;

import com.atguigu.boot.beans.User;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * RestController = Controller + ResponseBody
 */
@Import({User.class})
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String Handle(){
        String str = "hello";
        return str;
    }
}
