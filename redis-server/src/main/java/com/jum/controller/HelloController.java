package com.jum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    @RequestMapping("/a")  // 注意 不能和视图名称相同
    public String helloPage() {
        return "basepage/ajaxLoadPage/index.html";
    }

    @RequestMapping("/test")  // 注意 不能和视图名称相同
    public String indexInfo() {
        return "basepage/menuList";
    }

    @RequestMapping("/banner")  // 注意 不能和视图名称相同
    public String news() {
        return "news/index";
    }
//    @RequestMapping("/creatUser")
//    public String creatUser(String name, Integer age) {
//        helloUserService.creatUser(name, age);
//        return "index";
//    }

//    @RequestMapping("/pushData1")
//    public String pushDataTest(@RequestBody User user) {
//
//        System.out.println(user.toString());
//        System.out.println(user.getName() + "  " + user.getAddress());
//        return "index";
//    }

}