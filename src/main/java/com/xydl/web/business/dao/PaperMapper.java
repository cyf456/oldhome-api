package com.xydl.web.business.dao;

import java.util.List;
import java.util.Map;

/**
 * All rights Reserved, Designed By www.XXXX.com
 *
 * @author 陈一帆
 * @version V1.0.0
 * @projectName oldhome-api
 * @title PaperMapper
 * @package com.xydl.web.business.dao
 * @description PaperController
 * @date 2021/1/6 13:59
 * @copyright 2021 www.XXXXX.com
 * 注意 本内容仅限于 南京星源动力信息技术有限公司，禁止外泄以及用于其他的商业
 */

public interface PaperMapper {
    /**
     * 根据机构id查询问卷
     *
     * @return 问卷集合
     */
    public List<Map<String, Object>> selectPaperByOrganizationId(Map<String, Object> paramsMap);

    /**
     * 根据问卷id查询题目
     *
     * @return 题目集合
     */
    public List<Map<String, Object>> selectQuestionByPaperId(Map<String, Object> paramsMap);

    /**
     * 根据问卷id查询选项
     *
     * @return 选项集合
     */
    public List<Map<String, Object>> selectOptionByPaperId(Map<String, Object> paramsMap);

    /**
     * 根据optionId查询选项
     *
     * @return 选项集合
     */
    public Map<String,Object> selectOptionByOptionId(Map<String, Object> paramsMap);

    /**
     * 保存回答结果answer
     */
    public int batchSaveAnswerList(List<Map<String,Object>> list);

    /**
     * 查询指定问卷的题目数量
     * @param paramsMap
     * @return
     */
    public int questionNumByPaperId(Map<String, Object> paramsMap);

    /**
     * 查询指定paper_id和survey_user_id并根据question分组，返回组数
     * @param paramsMap
     * @return
     */
    public int answerNumByQuestion(Map<String, Object> paramsMap);

    /**
     * 添加问卷结果表
     */
    public int insertPaperResult(Map<String, Object> paramsMap);

    /**
     * 根据appUserId查询问卷结果
     */
    public List<Map<String,Object>> selectResultByAppUserId(Map<String, Object> paramsMap);

    /**
     * 根据问卷回答结果id删除问卷
     */
    public int deleteResultByResultId(Map<String, Object> paramsMap);
}
