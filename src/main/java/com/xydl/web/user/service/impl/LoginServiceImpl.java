package com.xydl.web.user.service.impl;

import com.xydl.common.utils.CommonResult;
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

    public CommonResult login(JSONObject userObj){
        JSONObject resObj = new JSONObject();
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(userObj.toString());
        Map<String, Object> userMap = userMapper.getUserInfo(paramsMap);
        if(userMap != null && userMap.size() > 0) {
            String token = tokenService.createToken(JSONObject.fromObject(userMap));
            resObj.put("token", token);
            resObj.put("userObj", userMap);
            return CommonResult.success(resObj);
        }else{
            return CommonResult.error(1001,"登录失败,手机号或姓名错误");
        }
    }
}
