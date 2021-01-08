package com.xydl.web.business.controller;

import com.xydl.common.annotation.CheckToken;
import com.xydl.common.utils.CommonResult;
import com.xydl.web.business.service.PaperService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
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
 * @title PaperController
 * @package com.xydl.web.business.controller
 * @description PaperController
 * @date 2021/1/6 17:38
 * @copyright 2021 www.XXXXX.com
 * 注意 本内容仅限于 南京星源动力信息技术有限公司，禁止外泄以及用于其他的商业
 */
@RestController
@RequestMapping("/Paper")
public class PaperController {
    @Autowired
    private PaperService paperService;

    /**
     * 根据机构Id查询问卷
     * @param request
     * @return
     */
    @CheckToken
    @RequestMapping(value = "/getPaperByOrganizationId",method = RequestMethod.POST)
    public CommonResult getPaperByOrganizationId(HttpServletRequest request){
        //获取传入的数据
        String requestJson = (String)request.getAttribute("requestJson");
        List<Map<String,Object>> PaperList = new ArrayList();
        try {
            //将string类型转换成json
            JSONObject jsonObject = JSONObject.fromObject(requestJson);
            PaperList = paperService.selectPaperByOrganizationId(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.success(PaperList);
    }

    /**
     * 根据问卷id查询题目和答案
     * @param request
     * @return
     */
    @CheckToken
    @RequestMapping(value = "/getQuestionByPaperId",method = RequestMethod.POST)
    public CommonResult getQuestionByPaperId(HttpServletRequest request){
        //获取前端传过来的问卷调查结果
        String requestJson = (String)request.getAttribute("requestJson");
        //
        List<Map<String, Object>> paperList = new ArrayList<>();
        try {
            //将string类型转换成json
            JSONObject jsonObject = JSONObject.fromObject(requestJson);
            paperList = paperService.selectQuestionByPaperId(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.success(paperList);
    }

    /**
     * 添加问卷回答选项和成绩表
     * @param request
     * @return
     */
    @CheckToken
    @RequestMapping(value = "/answerPaper",method = RequestMethod.POST)
    public CommonResult answerPaper(HttpServletRequest request){
        String requestJson = (String)request.getAttribute("requestJson");
        Map<String,Object> map = new HashedMap();
        try {
            //将string类型转换成json
            JSONObject jsonObject = JSONObject.fromObject(requestJson);
            map = paperService.answerPaper(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.success(map);
    }

    /**
     * 根据用户返回评估信息
     * @param request
     * @return
     */
    @CheckToken
    @RequestMapping(value = "/getResultByAppUserId",method = RequestMethod.POST)
    public CommonResult getResultByAppUserId(HttpServletRequest request){
        String requestJson = (String)request.getAttribute("requestJson");
        Map<String,Object> map = new HashMap<>();
        try {
            //将string类型转换成json
            JSONObject jsonObject = JSONObject.fromObject(requestJson);
            map = paperService.selectResultByAppUserId(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.success(map);
    }

    /**
     * 删除指定id的结果
     * @param request
     * @return
     */
    @CheckToken
    @RequestMapping(value = "/delResultByResultId",method = RequestMethod.POST)
    public CommonResult delResultByResultId(HttpServletRequest request){
        String requestJson = (String)request.getAttribute("requestJson");
        int num = 0;
        try {
            //将string类型转换成json
            JSONObject jsonObject = JSONObject.fromObject(requestJson);
            num = paperService.deleteResultByResultId(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CommonResult.success(num);
    }
}
