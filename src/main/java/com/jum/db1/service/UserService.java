package com.jum.db1.service;

import com.jum.db1.dao.UserMapper;
import com.jum.db1.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;


    public List<User> getUserInfo(Integer uuid){

        return userMapper.selectByPrimaryKey(uuid);

    }


}
