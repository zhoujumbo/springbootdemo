package com.jum.db2.dao;


import com.jum.db2.entity.User2;

public interface User2Mapper {
    int deleteByPrimaryKey(Integer uuid);

    int insert(User2 record);

    int insertSelective(User2 record);

    User2 selectByPrimaryKey(Integer uuid);

    int updateByPrimaryKeySelective(User2 record);

    int updateByPrimaryKey(User2 record);
}