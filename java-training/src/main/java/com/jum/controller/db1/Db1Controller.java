package com.jum.controller.db1;

import com.jum.db1.entity.User;
import com.jum.db1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/db1")
public class Db1Controller {
    @Autowired
    private UserService userService;

    @RequestMapping("/get")
    @ResponseBody
    public List<User> getUserInfo() {
        List<User> list = new ArrayList<>();
        list = userService.getUserInfo(2);
        System.out.println(list.size());
        return list;
    }

}
