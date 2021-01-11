package com.xydl.web.business.controller;

import com.xydl.common.annotation.CheckToken;
import com.xydl.common.annotation.Encrypt;
import com.xydl.common.utils.CommonResult;
import com.xydl.common.utils.JsonUtils;
import com.xydl.web.business.service.CaseDeclarerService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.XXXX.com
 *
 * @author 陈晨
 * @version V1.0.0
 * @projectName xydl-api
 * @title CaseDeclarerController
 * @package com.xydl.web.business.controller
 * @description CaseDeclarerController
 * @date 2020/8/15 17:10
 * @copyright 2020 www.XXXXX.com
 * 注意 本内容仅限于 南京星源动力信息技术有限公司，禁止外泄以及用于其他的商业
 */
@RestController
@RequestMapping("/caseDeclarer")
public class CaseDeclarerController {
    private static final Logger log = LoggerFactory.getLogger(CaseDeclarerController.class);

    @Autowired
    private CaseDeclarerService caseDeclarerService;

    /**
     *
     * @param request
     * @return
     */
//    @CheckToken
//    @Encrypt
    @RequestMapping(value = "/saveCaseDeclarerData",method = RequestMethod.POST)
    public CommonResult saveCaseDeclarerData(HttpServletRequest request){
        String requestJson = (String) request.getAttribute("requestJson");
        JSONObject jsonObj = JSONObject.fromObject(requestJson);
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(requestJson);
        int result = caseDeclarerService.saveCaseDeclarerData(paramsMap);
        log.info("主键：" + paramsMap.get("id"));
        return CommonResult.success(result);
    }
}
