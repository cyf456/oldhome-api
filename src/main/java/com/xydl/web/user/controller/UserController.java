package com.xydl.web.user.controller;

import com.xydl.common.annotation.CheckToken;
import com.xydl.common.annotation.Encrypt;
import com.xydl.common.utils.CommonResult;
import com.xydl.web.user.service.UserService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @CheckToken
//    @Encrypt
    @RequestMapping(value = "/getUserList",method = RequestMethod.POST)
    public CommonResult getUserList(HttpServletRequest request){
        String requestJson = (String) request.getAttribute("requestJson");
        List<Map<String, Object>> userList = null;
        try {
            userList = userService.getUserList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.success(userList);
    }


    /**
     * 添加评估员
     * @param request
     * @return
     */
    @CheckToken
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public CommonResult register(HttpServletRequest request){
        String requestJson = (String) request.getAttribute("requestJson");
        JSONObject jsonObj = JSONObject.fromObject(requestJson);
        int result = userService.saveAppUser(jsonObj);
        return CommonResult.success(result);
    }

    @CheckToken
    //@Encrypt
    @RequestMapping(value = "/addAppUser",method = RequestMethod.POST)
    public CommonResult addAppUser(HttpServletRequest request){
        String requestJson = (String) request.getAttribute("requestJson");
        JSONObject jsonObj = JSONObject.fromObject(requestJson);
        int result = userService.insertAppUser(jsonObj);
        return CommonResult.success(result);
    }
}
