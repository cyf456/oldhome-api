package com.xydl.web.user.controller;

import com.xydl.common.annotation.Encrypt;
import com.xydl.common.utils.CommonResult;
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
        JSONObject resultObj = new JSONObject();
        String requestJson = (String) request.getAttribute("requestJson");
        log.info("logincontroller:" + requestJson);
        try {
            JSONObject jsonObj = JSONObject.fromObject(requestJson);
            String token = loginService.login(jsonObj);
            resultObj.put("token", token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.success(resultObj);
    }
}
