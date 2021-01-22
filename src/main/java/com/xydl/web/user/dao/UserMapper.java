package com.xydl.web.user.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    public List<Map<String, Object>> getUserList();

    public Map<String, Object> getUserInfo(Map<String, Object> paramsMap);

    public int saveAppUser(Map<String, Object> paramsMap);

    /**
     * 根据机构id查询评论员
     * @param paramsMap
     * @return
     */
    public List<Map<String,Object>> selectAppUserByOrganization(Map<String, Object> paramsMap);

    /**
     * 新增用户信息
     * @param paramsMap
     * @return
     */
    public int insertAppuser(Map<String, Object> paramsMap);

    /**
     * 根据评估员id查询评估员信息
     */
    public Map<String,Object> selectAppUserByAppUserId(Map<String, Object> paramsMap);

    /**
     * 查询手机号码
     */
    public List<Map<String,Object>> selectAppUser();
} 
