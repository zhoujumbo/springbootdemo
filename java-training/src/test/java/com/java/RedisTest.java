package com.java;


import com.google.common.collect.Lists;
import com.jibug.cetty.sample.SampleApplication;
import com.jibug.cetty.sample.common.redistool.RedisKeyUtil;
import com.jibug.cetty.sample.constants.MlGoodsConstants;
import lombok.Data;
import org.apache.catalina.Pipeline;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SampleApplication.class)
public class RedisTest {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Test
    public void test01() throws InterruptedException {

        User u1 = new User().setName("aaa").setAge(17).setSex("Y");
        User u2 = new User().setName("aaa").setAge(17).setSex("Y");
        User u3 = new User().setName("aaa").setAge(17).setSex("Y");
        List<User> us = Lists.newArrayList(u1,u2);
        String key = RedisKeyUtil.getKey("ml","mx","goods");
        System.out.println(key);
        redisTemplate.expire(key,5, TimeUnit.SECONDS);
        redisTemplate.opsForList().leftPushAll(key,us);
        redisTemplate.opsForList().leftPush(key,u3);
        Thread.sleep(5000);
        System.out.println(redisTemplate.hasKey(key));
        System.out.println(redisTemplate.opsForList().size(key) );
        redisTemplate.opsForList().leftPop(key);
        System.out.println(redisTemplate.opsForList().size(key) );
        List<User> users = new ArrayList<>();
        //清空
        while (redisTemplate.opsForList().size(key) > 0){
            users.add((User) redisTemplate.opsForList().leftPop(key));
        }
        System.out.println(users.toString());
        System.out.println(redisTemplate.opsForList().size(key));
        redisTemplate.opsForList().getOperations().delete(key);
        System.out.println(redisTemplate.hasKey(key));
        redisTemplate.delete(key);
        System.out.println(redisTemplate.hasKey(key));
        System.out.println(redisTemplate.opsForList().size(key));
    }


    @Test
    public void test02() throws InterruptedException {
        String key = "test:list:rang";
        redisTemplate.delete(key);
        System.out.println(redisTemplate.opsForList().size(MlGoodsConstants.KEY_ML_BR));

        System.out.println("################################");
        System.out.println(redisTemplate.opsForList().size(MlGoodsConstants.KEY_ML_MX));
    }

    @Test
    public void test03(){
        String key = "test:list:rang";
        redisTemplate.delete(key);
//        List<Object> List = redisTemplate.executePipelined(new RedisCallback<Object>() {
//            @Nullable
//            @Override
//            public Object doInRedis(RedisConnection connection) throws DataAccessException {
//                connection.openPipeline();
//                for (int i = 0; i < 10000; i++) {
//                    connection.zCount(key.getBytes(), 0,Integer.MAX_VALUE);
//                }
//                return null;
//            }
//        }, redisTemplate.getValueSerializer());

//        List<Object> List = redisTemplate.executePipelined(new RedisCallback<Object>() {
//            @Nullable
//            @Override
//            public Object doInRedis(RedisConnection connection) throws DataAccessException {
////                connection.openPipeline();
////                connection.del(key.getBytes());
////                for (int i = 0; i < 10000; i++) {
////                    connection.set(key.getBytes(),"abcdefg".getBytes());
////                }
//
////                connection.closePipeline();
//                return null;
//            }
//        });
        List<String> param = new ArrayList<>();
        for(int i=0; i<500000;i++){
            param.add("abcdefg");
        }
        long bTime1 = System.currentTimeMillis();
        redisTemplate.opsForList().leftPushAll(key,param);
        long eTime1 = System.currentTimeMillis();
        System.out.println("共耗时："+(eTime1-bTime1));


        long bTime2 = System.currentTimeMillis();
        List<Object> list =  redisTemplate.opsForList().range(key,0,10000);

//        List<Object> list = redisTemplate.executePipelined(new RedisCallback<Object>() {
//            @Nullable
//            @Override
//            public Object doInRedis(RedisConnection connection) throws DataAccessException {
//                connection.openPipeline();
//                connection.get(key.getBytes());
//                return null;
//            }
//        }, redisTemplate.getValueSerializer());


        System.out.println("list size:"+list.size());
        long eTime2 = System.currentTimeMillis();
        System.out.println("共耗时："+(eTime2-bTime2));

        System.out.println(redisTemplate.hasKey(key));
        System.out.println(redisTemplate.opsForList().size(key) );
        redisTemplate.delete(key);
        System.out.println(redisTemplate.hasKey(key));
    }



}


@Data
class User{

    private String name;
    private int age;
    private String sex;

    public User setName(String name) {
        this.name = name == null ? null : name.trim();
        return this;
    }

    public User setAge(int age) {
        this.age = age;
        return this;
    }

    public User setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
        return this;
    }
}