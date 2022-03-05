package com.example.demo3.controller;

import com.example.demo3.beans.Person;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ParameterTestController {

    @GetMapping("/car/{id}/owner/{username}")
    public Map<String,Object> getCar(@PathVariable("id") Integer id,
                                     @PathVariable("username") String name,
                                     @PathVariable Map<String,String> pv,
                                     @RequestHeader("User-Agent") String userAgent,
                                     @RequestHeader Map<String,String> header,
                                     @RequestParam("age") Integer age,
                                     @RequestParam Map<String,String> params){
        Map<String,Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name",name);
        map.put("pv", pv);
        map.put("user-agent", userAgent);
        map.put("u",header.get("user-agent"));
        map.put("age", age);
        return map;
    }

    @PostMapping("/save")
    public Map postMethod(@RequestBody String content){
        Map<String,Object> map = new HashMap<>();
        map.put("content",content);
        return map;
    }
    @PostMapping("/saveuser")
    public Person saveuser(Person person){
        return person;
    }
}
