package com.xydl.web.business.dao;

import net.sf.json.JSONObject;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.XXXX.com
 *
 * @author 陈一帆
 * @version V1.0.0
 * @projectName oldhome-api
 * @title OrganizationMapper
 * @package com.xydl.web.business.dao
 * @description OrganizationMapper
 * @date 2021/1/6 14:19
 * @copyright 2021 www.XXXXX.com
 * 注意 本内容仅限于 南京星源动力信息技术有限公司，禁止外泄以及用于其他的商业
 */
@Mapper
public interface OrganizationMapper {
    /**
     * 根据id查询机构
     *
     * @return 机构
     */
    public Map<String,Object> selectOrganizationById(Map<String, Object> paramsMap);

    /**
     * 根据机构id查询机构图片
     *
     * @return 机构图片集合
     */
    public List<Map<String, Object>> selectOrganizationImageById(Map<String, Object> paramsMap);

    /**
     * 添加养老院老人
     * @return
     */
    public int insertSurveyUser(Map<String, Object> paramsMap);

    /**
     * 修改养老院老人
     * @return
     */
    public int updateSurveyUser(Map<String, Object> paramsMap);

    /**
     * 根据id查询老人
     *
     * @return 机构
     */
    public Map<String,Object> selectSurveyUserBySurveyUserId(Map<String, Object> paramsMap);

    /**
     * 根据评论员id查询老人
     * @param paramsMap
     * @return
     */
    public List<Map<String,Object>> selectSurveyUserByAppUserId(Map<String, Object> paramsMap);

    /**
     * 根据机构id查询用户
     *
     * @return 机构
     */
    public List<Map<String,Object>> selectAppUserByOrganizationId(Map<String, Object> paramsMap);


    /**
     * 查询地址
     *
     * @return 机构
     */
    public Map<String,Object> selectAreaByCode(Map<String, Object> paramsMap);
}
