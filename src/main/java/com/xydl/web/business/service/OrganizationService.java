package com.xydl.web.business.service;

import net.sf.json.JSONObject;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.XXXX.com
 *
 * @author 陈一帆
 * @version V1.0.0
 * @projectName oldhome-api
 * @title OrganizationService
 * @package com.xydl.web.business.service
 * @description OrganizationService
 * @date 2021/1/6 14:30
 * @copyright 2021 www.XXXXX.com
 * 注意 本内容仅限于 南京星源动力信息技术有限公司，禁止外泄以及用于其他的商业
 */
public interface OrganizationService {
    /**
     * 根据id查询机构
     *
     * @return 机构
     */
    public Map<String,Object> selectOrganizationById(JSONObject jsonObj);

    /**
     * 添加老人信息
     * @param jsonObject
     * @return
     */
    public int insertSurveyUser(JSONObject jsonObject) throws ParseException;

    /**
     * 根据机构id查询用户
     *
     * @return 机构
     */
    public List<Map<String,Object>> selectAppUserByOrganizationId(JSONObject jsonObj);

    /**
     * 根据评论员id查询老人
     * @param
     * @return
     */
    public List<Map<String,Object>> selectSurveyUserByAppUserId(JSONObject jsonObj) throws Exception;

}
