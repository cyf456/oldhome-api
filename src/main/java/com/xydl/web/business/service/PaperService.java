package com.xydl.web.business.service;

import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.XXXX.com
 *
 * @author 陈一帆
 * @version V1.0.0
 * @projectName oldhome-api
 * @title PaperService
 * @package com.xydl.web.business.service
 * @description PaperService
 * @date 2021/1/6 17:34
 * @copyright 2021 www.XXXXX.com
 * 注意 本内容仅限于 南京星源动力信息技术有限公司，禁止外泄以及用于其他的商业
 */
public interface PaperService {
    /**
     * 根据机构id查询问卷
     *
     * @return 机构图片集合
     */
    public List<Map<String, Object>> selectPaperByOrganizationId(JSONObject jsonObj);

    /**
     * 根据问卷Id查询题目和选项
     * @param jsonObj
     * @return
     */
    public List<Map<String, Object>>  selectQuestionByPaperId(JSONObject jsonObj);

    /**
     * 添加问卷回答选项和分数表
     * @param jsonObject
     * @return
     */
    public Map<String,Object> answerPaper(JSONObject jsonObject);

    /**
     * 根据用户id查询分数
     * @return
     */
    public Map<String,Object> selectResultByAppUserId(JSONObject jsonObject);

    /**
     * 根据分数Id删除结果
     */
    public int deleteResultByResultId(JSONObject jsonObject);

    /**
     * 根据ResultId查询answer,question,option
     * @param jsonObj
     * @return
     */
    public List<Map<String, Object>>  selectResultQuestionByResultId(JSONObject jsonObj);

    /**
     *到出result
     */
    public String expotResultByResultId(JSONObject jsonObj) throws Exception;

    /**
     * 根据机构id查询评论员及result
     * @return
     */
    public List<Map<String,Object>> selectAppuserResultByOrganizationId(JSONObject jsonObj);
 }
