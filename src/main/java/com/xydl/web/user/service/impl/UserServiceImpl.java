package com.xydl.web.user.service.impl;

import com.xydl.common.utils.JsonUtils;
import com.xydl.web.user.dao.UserMapper;
import com.xydl.web.user.service.UserService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    public List<Map<String, Object>> getUserList(){
        return userMapper.getUserList();
    }

    public int saveAppUser(JSONObject jsonObj){
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(jsonObj.toString());
        return userMapper.saveAppUser(paramsMap);
    }
}
