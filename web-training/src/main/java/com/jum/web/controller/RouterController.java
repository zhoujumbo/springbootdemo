package com.jum.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName RouterController
 * @Description TODO
 * @Author jb.zhou
 * @Date 2020/9/4
 * @Version 1.0
 */
@Controller
@RequestMapping("r")
public class RouterController {

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String test(Model model, HttpServletRequest req){
        return "index";
    }

}
