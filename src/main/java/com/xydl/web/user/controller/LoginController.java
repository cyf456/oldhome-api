package com.xydl.web.user.controller;

import com.xydl.common.annotation.Encrypt;
import com.xydl.common.utils.CommonResult;
import com.xydl.common.utils.StringUtils;
import com.xydl.web.user.service.LoginService;
import com.xydl.web.user.service.UserService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/logincontroller")
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private LoginService loginService;

//    @Encrypt
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public CommonResult login(HttpServletRequest request){
        String requestJson = (String) request.getAttribute("requestJson");
        log.info("logincontroller:" + requestJson);
        try {
            JSONObject jsonObj = JSONObject.fromObject(requestJson);
            if(StringUtils.isEmpty(jsonObj.getString("phoneNumber"))){
                return CommonResult.error(1001,"手机号不能为空");
            }

            if(StringUtils.isEmpty(jsonObj.getString("userName"))){
                return CommonResult.error(1001,"姓名不能为空");
            }
            return loginService.login(jsonObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.success();
    }
}
