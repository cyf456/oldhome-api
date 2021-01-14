package com.xydl.web.business.controller;

import com.xydl.common.annotation.CheckToken;
import com.xydl.common.utils.CommonResult;
import com.xydl.web.business.service.OrganizationService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.XXXX.com
 *
 * @author 陈一帆
 * @version V1.0.0
 * @projectName oldhome-api
 * @title OrganizationController
 * @package com.xydl.web.business.controller
 * @description OrganizationController
 * @date 2021/1/6 14:34
 * @copyright 2021 www.XXXXX.com
 * 注意 本内容仅限于 南京星源动力信息技术有限公司，禁止外泄以及用于其他的商业
 */
@RestController
@RequestMapping("/Organization")
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;

    /**
     * 根据id查询机构
     *
     * @return 机构
     */
    @CheckToken
    @RequestMapping(value = "/getOrganizationById",method = RequestMethod.POST)
    public CommonResult getOrganizationById(HttpServletRequest request){
        String requestJson = (String)request.getAttribute("requestJson");
        Map<String,Object> organizationMap = new HashMap<>();
        try {
            JSONObject jsonObject = JSONObject.fromObject(requestJson);
            organizationMap = organizationService.selectOrganizationById(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.success(organizationMap);
    }

    /**
     * 添加老人信息
     * @param
     * @return
     */
    @CheckToken
    @RequestMapping(value = "/addSurveyUser",method = RequestMethod.POST)
    public CommonResult addSurveyUser(HttpServletRequest request){
        String requestJson = (String)request.getAttribute("requestJson");
        int num=0;
        try {
            JSONObject jsonObject = JSONObject.fromObject(requestJson);
            num = organizationService.insertSurveyUser(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.success(num);
    }

    /**
     * 根据机构id查询用户
     *
     * @return 机构
     */
    @CheckToken
    @RequestMapping(value = "/getAppUserByOrganizationId",method = RequestMethod.POST)
    public CommonResult getAppUserByOrganizationId(HttpServletRequest request){
        String requestJson = (String)request.getAttribute("requestJson");
        List<Map<String,Object>> mapList = new ArrayList<>();
        try {
            JSONObject jsonObject = JSONObject.fromObject(requestJson);
            mapList = organizationService.selectAppUserByOrganizationId(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.success(mapList);
    }

    /**
     * 根据评论员id查询老人
     * @param
     * @return
     */
    @CheckToken
    @RequestMapping(value = "/getSurveyUserByAppUserId",method = RequestMethod.POST)
    public CommonResult getSurveyUserByAppUserId(HttpServletRequest request){
        String requestJson = (String)request.getAttribute("requestJson");
        List<Map<String,Object>> mapList = new ArrayList<>();
        try {
            JSONObject jsonObject = JSONObject.fromObject(requestJson);
            mapList = organizationService.selectSurveyUserByAppUserId(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.success(mapList);
    }
}
