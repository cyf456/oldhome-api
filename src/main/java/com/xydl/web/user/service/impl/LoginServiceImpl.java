package com.xydl.web.user.service.impl;

import com.xydl.common.utils.JsonUtils;
import com.xydl.web.user.dao.UserMapper;
import com.xydl.web.user.service.LoginService;
import com.xydl.web.user.service.TokenService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserMapper userMapper;

    public String login(JSONObject userObj){
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(userObj.toString());
        Map<String, Object> userMap = userMapper.getUserInfo(paramsMap);
        return tokenService.createToken(JSONObject.fromObject(userMap));
    }
}
