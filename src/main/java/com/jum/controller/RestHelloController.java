package com.jum.controller;


import com.jum.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试http请求
 */
//@EnableAutoConfiguration   // 注册该类为controller服务
@RestController
public class RestHelloController {

    @Autowired
    private TestService testService;

    @RequestMapping("/getInfo")
    public String getInfo(){

        try{
            return "success";
        }catch(Exception e){

            throw new RuntimeException();
        }
    }

    @RequestMapping("/getMap")
    public Map<String, Object> getMap(){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("resultCode","0");
        result.put("resultMsg","success");
        return result;
    }

    @RequestMapping("/errorTest")
    public String testErrorException(){
        int a = 1/0;
        return "testErrorException";
    }

//    @RequestMapping("/testPush")
//    public String pushDataTest1(){
//
////        String result = testService.postData();
//        String result2 = null;
//        try {
////            result2 = testService.postData2();
////            result2 = testService.postData3();
//            result2 = testService.postData4();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result2;
//    }

//    @RequestMapping("/pushData")
//    public String pushDataTest(@RequestBody User user){
//        // entity1
//        System.out.println(user.toString());
//        System.out.println(user.getName() + "  " +user.getAddress());
//        return "success";
//    }
//
//    @RequestMapping("/getStreamTest")
//    public  String getStreamObject(HttpServletRequest req) throws IOException,ClassNotFoundException{
//
//        ObjectInputStream ois = new ObjectInputStream(req.getInputStream());//HttpServletRequest req对象
//        Object user = ois.readObject();
//        System.out.println(user);
//        com.jum.entity.User user1 = new com.jum.entity.User();
//        user1 = (com.jum.entity.User)user;
//        System.out.println(user1.getName() + "  " +user1.getAddress());
//        return "end";
//    }
//
//    @RequestMapping("/pushData3")
//    public String pushDataTest3(@RequestBody User user){
//        // entity1
//        System.out.println(user.toString());
//        System.out.println(user.getName() + "  " +user.getAddress());
//        return "success";
//    }
//
//    @RequestMapping("/pushData4")
//    public String pushDataTest4(@RequestBody JSONObject jsonObj){
//        // entity1
//        System.out.println(jsonObj.toString());
////        System.out.println(user.getName() + "  " +user.getAddress());
//        return "success";
//    }

}
