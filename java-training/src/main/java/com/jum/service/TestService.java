package com.jum.service;

import org.springframework.stereotype.Service;

@Service
public class TestService {

//    public String postData() {
//        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//        requestFactory.setConnectTimeout(60 * 1000);
//        requestFactory.setReadTimeout(60 * 1000);
//        RestTemplate restTemplate = new RestTemplate(requestFactory);
//        User user = new User();
//        user.setName("jum");
//        user.setAddress("123456");
//        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8080/pushData", user, String.class);
//        System.out.println("responseEntity>>>>>" + responseEntity.getBody());
//        return responseEntity.getBody();
//    }
//
//
//    public String postData2() throws Exception{
//
//        User user = new User();
//        user.setName("jum");
//        user.setAddress("123456");
//        String result = HttpAccessUtil.httpPostSerialObject("http://localhost:8080/getStreamTest",20*6000,20*6000,user);
//        return result;
//    }
//
//    public String postData3() throws Exception{
//        User user = new User();
//        user.setName("jum");
//        user.setAddress("123456");
//
//        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//        requestFactory.setConnectTimeout(60 * 1000);
//        requestFactory.setReadTimeout(60 * 1000);
//        RestTemplate restTemplate = new RestTemplate(requestFactory);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<User> entity = new HttpEntity<User>(user, headers);
//
//        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8080/pushData3", entity, String.class);
//        System.out.println("responseEntity>>>>>" + responseEntity.getBody());
//        return responseEntity.getBody();
//    }
//
//
//    public String postData4() throws Exception{
//        User user = new User();
//        user.setName("jum");
//        user.setAddress("123456");
//        JSONObject jsonObj = (JSONObject) JSON.toJSON(user);
//        System.out.println(JSON.toJSONString(jsonObj));
//
//        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//        requestFactory.setConnectTimeout(60 * 1000);
//        requestFactory.setReadTimeout(60 * 1000);
//        RestTemplate restTemplate = new RestTemplate(requestFactory);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<Object> entity = new HttpEntity<Object>(jsonObj, headers);
//        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8080/pushData4", entity, String.class);
//        System.out.println("responseEntity>>>>>" + responseEntity.getBody());
//        return responseEntity.getBody();
//    }

}