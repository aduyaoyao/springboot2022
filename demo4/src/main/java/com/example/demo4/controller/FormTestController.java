package com.example.demo4.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传测试
 */
@Slf4j
@Controller
public class FormTestController {

    @GetMapping("/form_layouts")
    public String form_layouts(){
        return "form/form_layouts";
    }
    @PostMapping("/upload")
    public String upload(@RequestParam("email") String email,
                         @RequestParam("username") String username,
                         @RequestPart("headerImg") MultipartFile headerImg,
                         @RequestPart("photos") MultipartFile[] photos
                          ) throws IOException {

        if(!headerImg.isEmpty()){
            //保存到服务器中
            headerImg.transferTo(new File("D:\\"+headerImg.getOriginalFilename()));
        }
        if(photos.length>0){
            for (MultipartFile photo : photos) {
                photo.transferTo(new File("D:\\"+photo.getOriginalFilename()));
            }
        }
        return "main";
    }
}
