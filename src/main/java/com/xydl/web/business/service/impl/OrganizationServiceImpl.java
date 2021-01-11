package com.xydl.web.business.service.impl;

import com.xydl.common.utils.JsonUtils;
import com.xydl.common.utils.uuid.IdUtils;
import com.xydl.web.business.dao.OrganizationMapper;
import com.xydl.web.business.service.OrganizationService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.XXXX.com
 *
 * @author 陈一帆
 * @version V1.0.0
 * @projectName oldhome-api
 * @title OrganizationServiceImpl
 * @package com.xydl.web.business.service.impl
 * @description OrganizationServiceImpl
 * @date 2021/1/6 14:31
 * @copyright 2021 www.XXXXX.com
 * 注意 本内容仅限于 南京星源动力信息技术有限公司，禁止外泄以及用于其他的商业
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public Map<String, Object> selectOrganizationById(JSONObject jsonObj) {
        //将json类型转换成Map集合
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(jsonObj.toString());
        //根据id查询机构信息
        Map<String,Object> organizationMap = organizationMapper.selectOrganizationById(paramsMap);
        //根据机构id查询机构图片id
        List<Map<String,Object>> organizationImageMap = organizationMapper.selectOrganizationImageById(paramsMap);
        //将机构图片集合放入机构信息里面
        organizationMap.put("organizationImage",organizationImageMap);
        return organizationMap;
    }

    @Override
    public int insertSurveyUser(JSONObject jsonObject) {
        //将json类型转换成Map集合
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(jsonObject.toString());
        //添加id
        paramsMap.put("surveyUserId", IdUtils.fastSimpleUUID());
        //添加机构信息
        return organizationMapper.insertSurveyUser(paramsMap);
    }

    @Override
    public List<Map<String, Object>> selectAppUserByOrganizationId(JSONObject jsonObj) {
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(jsonObj.toString());
        return organizationMapper.selectAppUserByOrganizationId(paramsMap);
    }
}
