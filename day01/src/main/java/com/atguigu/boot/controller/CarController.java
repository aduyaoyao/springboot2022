package com.atguigu.boot.controller;

import com.atguigu.boot.beans.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarController {
    @Autowired
    Car car;

    @RequestMapping("/car")
    public Car car(){
        return car;
    }
}
