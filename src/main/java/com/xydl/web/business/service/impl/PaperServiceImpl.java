package com.xydl.web.business.service.impl;

import com.xydl.common.utils.JsonUtils;
import com.xydl.common.utils.constant.PaperConstants;
import com.xydl.common.utils.uuid.IdUtils;
import com.xydl.web.business.dao.PaperMapper;
import com.xydl.web.business.service.PaperService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
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
 * @title PaperServiceImpl
 * @package com.xydl.web.business.service.impl
 * @description PaperServiceImpl
 * @date 2021/1/6 17:35
 * @copyright 2021 www.XXXXX.com
 * 注意 本内容仅限于 南京星源动力信息技术有限公司，禁止外泄以及用于其他的商业
 */
@Service
public class PaperServiceImpl implements PaperService {
    @Autowired
    private PaperMapper paperMapper;


    @Override
    public List<Map<String, Object>> selectPaperByOrganizationId(JSONObject jsonObj) {
        //将json类型转换成Map集合
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(jsonObj.toString());
        return paperMapper.selectPaperByOrganizationId(paramsMap);
    }

    @Override
    public List<Map<String, Object>> selectQuestionByPaperId(JSONObject jsonObj) {
        //将json类型转换成Map集合
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(jsonObj.toString());
        //查询题目
        List<Map<String, Object>> questionList = paperMapper.selectQuestionByPaperId(paramsMap);
        //查询选项
        List<Map<String, Object>> optionList = paperMapper.selectOptionByPaperId(paramsMap);
        //表示题目
        Map<String,Object> questionMap;
        //每个题目的答案集合
        List<Map<String,Object>> questionOptionList;
        //最终题目集合
        List<Map<String,Object>> paperList = new ArrayList<>();
        for (int i = 0; i < questionList.size(); i++) {
            questionOptionList = new ArrayList<>();
            for (int j = 0; j < optionList.size(); j++) {
                if (questionList.get(i).get(PaperConstants.QUESTION_ID).equals(optionList.get(j).get(PaperConstants.QUESTION_ID))) {
                    //将与题目id相同的选项添加到题目选项集合中
                    questionOptionList.add(optionList.get(j));
                }
            }
            //选项集合添加到题目中
            questionList.get(i).put("optionList",questionOptionList);
            //将题目添加到题目集合中
            paperList.add(questionList.get(i));
        }
        return paperList;
    }

    @Override
    public Map<String, Object> answerPaper(JSONObject jsonObject) {
        //        {
//            "appUserId":"晨晨",
//            "surveyUserId":"陈一帆",
//            "paperId":"1",
//            "paperName":"AA问卷",
//            "answerArr":[
//                {
//                    "questionId":"1",
//                        "optionId":"A",
//                        "optionContent":""
//                },
//                {
//                    "question":"2",
//                        "optionId":"B",
//                        "optionContent":""
//                },
//                {
//                    "question":"3",
//                    "optionId":"C",
//                    "optionContent":""
//                }
//            ]
//        }

        //返回结果，成功添加answer数量和成功添加result数量
        Map<String, Object> map = new HashedMap();
        //保存的answer对象
        Map<String, Object> answerMap;
        //保存的分数表对象
        Map<String, Object> resultMap = new HashMap<>();
        //保存的answer对象集合
        ArrayList<Map<String, Object>> answerList = new ArrayList<>();
        //分数
        int paperScore = 0;
        //1、获取前端传过来的问卷调查结果
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(jsonObject.toString());
        //1.1获取answer集合
        List<Map<String, Object>> answerArr = JSONArray.fromObject(paramsMap.get("answerArr"));
        //便利answer集合，拼装answerMap对象
        for (int i = 0; i < answerArr.size(); i++) {
            answerMap = answerArr.get(i);
            //根据optionId查询选项
            Map<String, Object> optionMap = paperMapper.selectOptionByOptionId(answerMap);
            answerMap.put("answerId", IdUtils.fastSimpleUUID());
            answerMap.put("surveyUserId", paramsMap.get("surveyUserId"));
            answerMap.put("paperId", paramsMap.get("paperId"));
            answerMap.put("answerScore", optionMap.get("optionScore"));
            answerList.add(answerMap);
            paperScore += (int) optionMap.get("optionScore");
        }
        //2、保存答案选项
        int answerNum = paperMapper.batchSaveAnswerList(answerList);
        //3、计算完成度
        //3.1、根据paperId查询试卷题目数
        int paperQuestionNum = paperMapper.questionNumByPaperId(paramsMap);
        //3.2、查询指定paper_id和survey_user_id并根据question分组，返回组数
        int answerQuestionNum = paperMapper.answerNumByQuestion(paramsMap);
        System.out.println("paperQuestionNum==================================" + paperQuestionNum);
        System.out.println("answerQuestionNum==================================" + answerQuestionNum);
        //问卷结果状态
        int resultStatus = 1;//待完成
        if (paperQuestionNum == answerQuestionNum) {
            resultStatus = 2;//已完成
        }
        //4.1 拼接resultMap
        resultMap.put("paperResultId", IdUtils.fastSimpleUUID());
        resultMap.put("appUserId", paramsMap.get("appUserId"));
        resultMap.put("surveyUserId", paramsMap.get("surveyUserId"));
        resultMap.put("paperId", paramsMap.get("paperId"));
        resultMap.put("paperName", paramsMap.get("paperName"));
        resultMap.put("paperScore", paperScore);
        resultMap.put("resultStatus", resultStatus);
        //4、保存问卷结果
        int resultNum = paperMapper.insertPaperResult(resultMap);
        //添加返回数据
        map.put("answerNum", answerNum);
        map.put("resultNum", resultNum);
        return map;
    }

    @Override
    public Map<String, Object> selectResultByAppUserId(JSONObject jsonObject) {
        //完成的结果
        Map<String, Object> finishMap;
        //未完成的结果
        Map<String, Object> unfinishMap;
        // 完成的结果集合
        List<Map<String, Object>> finishList = new ArrayList<>();
        //未完成的结果集合
        List<Map<String, Object>> unfinishList = new ArrayList<>();
        //获取前端穿过来的数据
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(jsonObject.toString());
        //根据appUserId查询result
        List<Map<String, Object>> resultList = paperMapper.selectResultByAppUserId(paramsMap);
        //便利数据集合，判断受否完成
        for (Map<String, Object> r : resultList
        ) {
            if (r != null && r.get("resultStatus").equals("1")) {
                //根据paperId查询试卷题目数
                int paperQuestionNum = paperMapper.questionNumByPaperId(r);
                //查询指定paper_id和survey_user_id并根据question分组，返回组数量
                int answerQuestionNum = paperMapper.answerNumByQuestion(r);
                //计算完成度
                NumberFormat numberFormat = NumberFormat.getInstance();
                numberFormat.setMaximumFractionDigits(2);
                String progress = numberFormat.format((float)answerQuestionNum/(float) paperQuestionNum*100);
                progress = progress+"%";
                //将未完成的数据存到map中
                unfinishMap = new HashMap<>();
                unfinishMap.put("paperResultId",r.get("paperResultId"));
                unfinishMap.put("surveyUserId",r.get("surveyUserId"));
                unfinishMap.put("paperId",r.get("paperId"));
                unfinishMap.put("paperName",r.get("paperName"));
                unfinishMap.put("paperScore",r.get("paperScore"));
                unfinishMap.put("resultStatus",r.get("resultStatus"));
                unfinishMap.put("createTime",r.get("createTime"));
                unfinishMap.put("progress",progress);
                unfinishList.add(unfinishMap);
            }else if(r != null && r.get("resultStatus").equals("2")){
                finishMap = new HashMap<>();
                finishMap.put("paperResultId",r.get("paperResultId"));
                finishMap.put("surveyUserId",r.get("surveyUserId"));
                finishMap.put("paperId",r.get("paperId"));
                finishMap.put("paperName",r.get("paperName"));
                finishMap.put("paperScore",r.get("paperScore"));
                finishMap.put("resultStatus",r.get("resultStatus"));
                finishMap.put("createTime",r.get("createTime"));
                finishList.add(finishMap);
            }
        }
        //返回结果map
        Map<String,Object> objectMap  = new HashMap<>();
        objectMap.put("finishList",finishList);
        objectMap.put("unfinishList",unfinishList);
        return objectMap;
    }

    @Override
    public int deleteResultByResultId(JSONObject jsonObject) {
        //获取前端穿过来的数据
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(jsonObject.toString());
        return paperMapper.deleteResultByResultId(paramsMap);
    }
}
