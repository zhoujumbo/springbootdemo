package com.jum.db1.dao;

import com.jum.db1.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserMapper {
    int deleteByPrimaryKey(Integer uuid);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByPrimaryKey(Integer uuid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}