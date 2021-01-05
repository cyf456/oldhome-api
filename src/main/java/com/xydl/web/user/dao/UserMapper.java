package com.xydl.web.user.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    public List<Map<String, Object>> getUserList();

    public Map<String, Object> getUserInfo(Map<String, Object> paramsMap);

    public int saveAppUser(Map<String, Object> paramsMap);
} 
