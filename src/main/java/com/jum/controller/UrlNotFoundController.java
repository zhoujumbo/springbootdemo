package com.jum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 拦截服务器错误异常
 */
@Controller
public class UrlNotFoundController {

    @RequestMapping("*")
    @ResponseBody
    public Map<String, Object> test404(){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("errorCode","404");
        result.put("errorMsg","服务器错误");
        return result;
    }
}
