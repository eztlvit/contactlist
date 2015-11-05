package com.zzt.springmvc.contactlist.mapper;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;

import com.zzt.springmvc.contactlist.entity.UserMyBatis;

@MapperScan("com.zzt.springmvc.contactlist.mapper")
public interface UserMyBatisMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserMyBatis record);

    int insertSelective(UserMyBatis record);

    UserMyBatis selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserMyBatis record);

    int updateByPrimaryKey(UserMyBatis record);
    
    List<UserMyBatis> getList();
}