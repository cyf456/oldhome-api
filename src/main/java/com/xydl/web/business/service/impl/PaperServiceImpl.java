package com.xydl.web.business.service.impl;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.xydl.common.utils.JsonUtils;
import com.xydl.common.utils.uuid.IdUtils;
import com.xydl.web.business.dao.OrganizationMapper;
import com.xydl.web.business.dao.PaperMapper;
import com.xydl.web.business.service.PaperService;
import com.xydl.web.user.dao.UserMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.*;

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
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private UserMapper userMapper;


    /**
     * 根据机构id查询问卷
     *
     * @return 机构图片集合
     */
    public List<Map<String, Object>> selectPaperByOrganizationId(JSONObject jsonObj) {
        //将json类型转换成Map集合
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(jsonObj.toString());
        return paperMapper.selectPaperByOrganizationId(paramsMap);
    }

    /**
     * 根据问卷Id查询题目和选项
     *
     * @param jsonObj
     * @return
     */
    public List<Map<String, Object>> selectQuestionByPaperId(JSONObject jsonObj) {
        //将json类型转换成Map集合
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(jsonObj.toString());

        //题目list(有选项)
        List<Map<String, Object>> paperList = new ArrayList<>();
        //题目list(无选项)
        List<Map<String, Object>> questionList = paperMapper.selectQuestionByPaperId(paramsMap);
        //选项list
        List<Map<String, Object>> optionList = paperMapper.selectOptionByPaperId(paramsMap);

        return questionAddOption(questionList, optionList);
    }

    /**
     * 将选项添加到题目中
     *
     * @return
     */
    public static List<Map<String, Object>> questionAddOption(List<Map<String, Object>> questionList, List<Map<String, Object>> optionList) {
        //题目map
        Map<String, Object> questionMap;
        //题目list(有选项)
        List<Map<String, Object>> paperList = new ArrayList<>();
        //每个题目的选项list
        List<Map<String, Object>> questionOptionList;

        if (questionList != null && questionList.size() > 0 && optionList != null && questionList.size() > 0) {
            for (int i = 0; i < questionList.size(); i++) {
                questionOptionList = new ArrayList<>();
                questionMap = questionList.get(i);
                for (int j = 0; j < optionList.size(); j++) {
                    if (questionList.get(i).get("questionId").equals(optionList.get(j).get("questionId"))) {
                        //将与题目id相同的选项添加到题目的选项list中
                        questionOptionList.add(optionList.get(j));
                    }
                }
                //将题目选项list添加到
                questionMap.put("optionList", questionOptionList);
                //将题目添加到题目集合中
                paperList.add(questionMap);
            }
        }
        return paperList;
    }


    /**
     * 添加问卷回答选项和分数表
     *
     * @param jsonObject
     * @return
     */
    public Map<String, Object> answerPaper(JSONObject jsonObject) {
//        "body":{
//            "appUserId":"767a1f2e-41d7-4149-94ab-51bc09d5efe1",
//                    "surveyUserId":"a6d7317a9b1c427d94e0694559ec5d92",
//                    "paperId":"1EBD0B86B0114B17BB8EAAB4B4658A6C",
//                    "paperName":"老年人长期照护需求与风险评估问卷",
//                    "paperResultId":"0",
//                    "answerArr":[
//            {
//                "optionId":"70B295CD97434B559CA6370D312DFD93"
//            },
//            {
//                "optionId":"6AB27B051118482D8C740E8AACD5E94B"
//            },
//            {
//                "optionId":"F60DF73439DC4528AAC9D15D0DACE069"
//            }
//        ]
//        }

        //1、获取前端传过来的问卷调查结果
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(jsonObject.toString());
        //1.1获取answer集合
        //JSONArray answerArr = JSONArray.fromObject(paramsMap.get("answerArr"));
        //List<Map<String, Object>> answerArr = (List)JSONArray.toCollection(JSONArray.fromObject(paramsMap.get("answerArr")));
        List<Map<String, Object>> answerArr = (List<Map<String, Object>>) JSONArray.fromObject(paramsMap.get("answerArr"));

        //返回结果，成功添加answer数量和成功添加result数量
        Map<String, Object> map = new HashMap<>();
        //保存的分数表对象
        Map<String, Object> resultMap = new HashMap<>();
        //保存的answer对象
        Map<String, Object> answerMap;

        //需要保存的选项List
//        for (Map<String,Object> a:answerArr
//             ) {
//            System.out.println("optionId============================="+a.get("optionId").toString());
//        }
        if (answerArr == null || answerArr.size() == 0) {
            map.put("answer", "没有一个选项。");
            return map;
        }
        List<Map<String, Object>> optionList = paperMapper.selectOptionByOptionListId(answerArr);
        //保存的answer对象集合
        List<Map<String, Object>> answerList = new ArrayList<>();

        //分数
        int paperScore = 0;
        //题目数量
        int questionNum = paperMapper.questionNumByPaperId(paramsMap);
        //成功保存的答案数量
        int answerNum = 0;
        //成功保存或修改问卷结果数量
        int resultNum = 0;

        //计算回答进度
        int status = 10;
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(0);
//        System.out.println("回答题目数：=================================="+answerArr.size());
//        System.out.println("总题目数：====================================="+questionNum);
        String resultStatus = numberFormat.format((float) answerArr.size() / (float) questionNum * 10);
        //System.out.println("回答进度==================================="+resultStatus);
        resultMap.put("resultStatus", resultStatus);

        //System.out.println("======================================"+paramsMap.get("paperResultId"));
        System.out.println("问卷结果id###################################################" + paramsMap.get("paperResultId"));
        if (paramsMap.get("paperResultId").equals("0")) {
            //生成resultId
            String paperResultId = IdUtils.fastSimpleUUID();
            for (int i = 0; i < optionList.size(); i++) {
                answerMap = optionList.get(i);
                //根据optionId查询选项
                answerMap.put("answerId", IdUtils.fastSimpleUUID());
                answerMap.put("paperResultId", paperResultId);
                answerMap.put("surveyUserId", paramsMap.get("surveyUserId"));
                paperScore += (int) optionList.get(i).get("answerScore");
                answerList.add(answerMap);
            }
            //拼接resultMap
            resultMap.put("paperResultId", paperResultId);
            resultMap.put("appUserId", paramsMap.get("appUserId"));
            resultMap.put("surveyUserId", paramsMap.get("surveyUserId"));
            resultMap.put("paperId", paramsMap.get("paperId"));
            resultMap.put("paperName", paramsMap.get("paperName"));
            resultMap.put("paperScore", paperScore);

            if (answerList != null && answerList.size() > 0) {
                //添加result
                resultNum = paperMapper.insertPaperResult(resultMap);
                //保存answer
                answerNum = paperMapper.batchSaveAnswerList(answerList);
            }
        } else if (paramsMap.get("paperResultId") != "" && paramsMap.get("paperResultId") != null) {
            //删除原来的answer
            int num = paperMapper.deleteAnswerByResultId(paramsMap);
            for (int i = 0; i < optionList.size(); i++) {
                answerMap = optionList.get(i);
                //根据optionId查询选项
                answerMap.put("answerId", IdUtils.fastSimpleUUID());
                answerMap.put("paperResultId", paramsMap.get("paperResultId"));
                answerMap.put("surveyUserId", paramsMap.get("surveyUserId"));
                paperScore += (int) optionList.get(i).get("answerScore");
                answerList.add(answerMap);
            }
            resultMap.put("paperScore", paperScore);
            resultMap.put("paperResultId", paramsMap.get("paperResultId"));
            if (answerList != null && answerList.size() > 0) {
                //修改result
                resultNum = paperMapper.updatePaperResult(resultMap);
                //保存answer
                answerNum = paperMapper.batchSaveAnswerList(answerList);
            }
        }

//        //添加返回数据
        map.put("answerNum", answerNum);
        map.put("resultNum", resultNum);
        return map;
    }


    /**
     * 根据用户id查询分数
     *
     * @return
     */
    public Map<String, Object> selectResultByAppUserId(JSONObject jsonObject) throws Exception {
        //完成的结果map
        Map<String, Object> finishMap;
        //未完成的结果map
        Map<String, Object> unfinishMap;
        //最终返回结果map
        Map<String, Object> objectMap = new HashMap<>();
        //获取前端穿过来的数据
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(jsonObject.toString());

//        // 完成的结果集合
//        List<Map<String, Object>> finishList = new ArrayList<>();
        // 每个老人完成的结果集合
        List<Map<String, Object>> finishSurveyList;
        //每个老人未完成的结果集合
        //List<Map<String, Object>> unfinishSurveyList;
        //根据appUserId查询result
        List<Map<String, Object>> resultList = paperMapper.selectResultByAppUserId(paramsMap);
        //根据appUserId查询老人信息
        List<Map<String, Object>> surveyUserList = organizationMapper.selectSurveyUserByAppUserId(paramsMap);
        //未添加result的完成结果集合
        List<Map<String, Object>> finishSurveyUserList2 = new ArrayList<>();
        //        //未完成的结果集合
        List<Map<String, Object>> unfinishList = new ArrayList<>();
        //未添加result的未完成结果集合
        //List<Map<String, Object>> unfinishSurveyUserList2 = new ArrayList<>();

        //便利数据集合，判断受否完成
        int finishj = 0;
        int unfinishj = 0;
        for (int i = 0; i < surveyUserList.size(); i++) {
            finishSurveyList = new ArrayList<>();
            //unfinishSurveyList = new ArrayList<>();
            for (Map<String, Object> r : resultList
            ) {
                //修改时间格式
                //Timestamp timestamp = r.get("createTime");
//                String createTime1 =  r.get("createTime").toString();
//                String createTime2 = createTime1.substring(0,10);
                //System.out.println("创建时间：======================================="+createTime2);
                if (r != null && r.get("resultStatus").equals("10") && r.get("surveyUserId").equals(surveyUserList.get(i).get("surveyUserId"))) {
                    finishMap = new HashMap<>();
                    finishMap.put("paperResultId", r.get("paperResultId"));
                    //finishMap.put("surveyUserId", r.get("surveyUserId"));
                    finishMap.put("paperId", r.get("paperId"));
                    finishMap.put("paperName", r.get("paperName"));
                    finishMap.put("paperScore", r.get("paperScore"));
                    finishMap.put("resultStatus", r.get("resultStatus"));
                    finishMap.put("createTime", r.get("createTime"));
                    finishSurveyList.add(finishMap);
                } else if (r.get("surveyUserId").equals(surveyUserList.get(i).get("surveyUserId"))) {
                    //将未完成的数据存到map中
                    unfinishMap = new HashMap<>();
                    //unfinishMap = surveyUserList.get(i);
                    unfinishMap.putAll(surveyUserList.get(i));
                    unfinishMap.put("paperResultId", r.get("paperResultId"));
                    //unfinishMap.put("surveyUserId", r.get("surveyUserId"));
                    unfinishMap.put("paperId", r.get("paperId"));
                    unfinishMap.put("paperName", r.get("paperName"));
                    unfinishMap.put("paperScore", r.get("paperScore"));
                    unfinishMap.put("resultStatus", r.get("resultStatus"));
                    unfinishMap.put("createTime", r.get("createTime"));
                    //计算年龄
                    int age = OrganizationServiceImpl.getAge(surveyUserList.get(i).get("surveyUserBirthdate").toString());
                    unfinishMap.put("age", age);
                    unfinishList.add(unfinishMap);
//                    for (int j = 0; j < unfinishList.size(); j++) {
//                        System.out.println("问卷名称：++++++++++++++++++++++++++++++++++++++++++++++"+unfinishList.get(j).get("paperName"));
//                    }
//                    System.out.println("====================================================================");
                    continue;
                }
            }
            if (finishSurveyList != null && finishSurveyList.size() > 0) {
                surveyUserList.get(i).put("resultList", finishSurveyList);
                surveyUserList.get(i).put("age", OrganizationServiceImpl.getAge(surveyUserList.get(i).get("surveyUserBirthdate").toString()));
                finishSurveyUserList2.add(surveyUserList.get(i));

                //finishSurveyUserList2.get(finishj).put("resultList",finishSurveyList);
                //计算年龄
                //int age = OrganizationServiceImpl.getAge(finishSurveyUserList2.get(finishj).get("surveyUserBirthdate").toString());
                //finishSurveyUserList2.get(finishj).put("age",age);
                //finishSurveyUserList2.get(finishj).remove("surveyUserBirthdate");

                //finishj++;
            }
//            if(unfinishSurveyList != null && unfinishSurveyList.size()>0){
//                unfinishSurveyUserList2.add(surveyUserList.get(i));
//                unfinishSurveyUserList2.get(unfinishj).put("resultList",unfinishSurveyList);
//                int age = OrganizationServiceImpl.getAge(unfinishSurveyUserList2.get(unfinishj).get("surveyUserBirthdate").toString());
//                unfinishSurveyUserList2.get(unfinishj).put("age",age);
//                //unfinishSurveyUserList2.get(unfinishj).remove("surveyUserBirthdate");
//
//                unfinishj++;
//            }

        }

        objectMap.put("finishList", finishSurveyUserList2);
        objectMap.put("unfinishList", unfinishList);
        return objectMap;
    }

    /**
     * 根据问卷结果Id删除结果
     */
    public int deleteResultByResultId(JSONObject jsonObject) {
        //获取前端穿过来的数据
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(jsonObject.toString());
        //根据问卷被调查老人id和问卷id删除回答结果
        //Map<String, Object> resultMap = paperMapper.selectResultByResultId(paramsMap);
        //删除问卷结果表
        int numResult = paperMapper.deleteResultByResultId(paramsMap);
        //根据问卷被调查老人id和问卷id删除回答结果
        if (numResult != 0) {
            int numAnswer = paperMapper.deleteAnswerByResultId(paramsMap);
        }
        return numResult;
    }

    /**
     * 根据ResultId查询answer,question,option
     *
     * @param jsonObj
     * @return
     */
    public List<Map<String, Object>> selectResultQuestionByResultId(JSONObject jsonObj) {
        //获取前端传过来的resultId
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(jsonObj.toString());
        //查询result
        Map<String, Object> resultMap = paperMapper.selectResultByResultId(paramsMap);

        //题目list(无选项)
        List<Map<String, Object>> questionList = paperMapper.selectQuestionByPaperId(resultMap);
        //题目list(有选项)
        List<Map<String, Object>> questionList2 = new ArrayList<>();
        //选项list(无answer状态)
        List<Map<String, Object>> optionList = paperMapper.selectOptionByPaperId(resultMap);
        //选项list(有answer状态)
        List<Map<String, Object>> optionList2 = new ArrayList<>();
        //查询answer
        List<Map<String, Object>> answerList = paperMapper.selectAnswerByResultId(paramsMap);

        //选项list添加回答状态
        if (optionList != null && optionList.size() > 0 && answerList != null && answerList.size() > 0) {
            for (int i = 0; i < optionList.size(); i++) {
                optionList2.add(optionList.get(i));

                for (int j = 0; j < answerList.size(); j++) {
                    if (answerList.get(j).get("optionId").equals(optionList.get(i).get("optionId"))) {
                        optionList2.get(i).put("answer", "1");
                        break;
                    } else {
                        optionList2.get(i).put("answer", "0");
                    }
                }
            }
        }

        //选项添加到题目中
        questionList2 = questionAddOption(questionList, optionList2);

        return questionList2;
    }

    /**
     * 根据机构id查询评论员及result
     *
     * @return
     */
//    public List<Map<String, Object>> selectAppuserResultByOrganizationId(JSONObject jsonObj) {
//
//    }

    /**
     * 根据机构id查询评论员及result
     *
     * @return
     */
    public List<Map<String, Object>> selectAppuserResultByOrganizationId(JSONObject jsonObj) {
        //获取前端传过来的resultId
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(jsonObj.toString());
        //根据机构id查询用户信息
        List<Map<String, Object>> userList = userMapper.selectAppUserByOrganization(paramsMap);
        //用户集合（含老人信息）
        List<Map<String, Object>> userList2 = new ArrayList<>();
        //老人list
        List<Map<String, Object>> surveyList1;
        //老人list(含result)
        List<Map<String, Object>> surveyList2;
        //老人问卷结果集合
        List<Map<String, Object>> surveyResultList;

        for (int i = 0; i < userList.size(); i++) {
            surveyList2 = new ArrayList<>();
            //用户id查询result
            surveyList1 = organizationMapper.selectSurveyUserByAppUserId(userList.get(i));
            if (surveyList1 != null && surveyList1.size() > 0) {
                for (int j = 0; j < surveyList1.size(); j++) {
                    surveyList2.add(surveyList1.get(j));
                    //根据老人id查询result
                    surveyResultList = paperMapper.selectResultBySurveyId(surveyList1.get(j));
                    if (surveyResultList != null && surveyResultList.size() > 0) {
                        surveyList2.get(j).put("resultList", surveyResultList);
                    }
                }
            }
            userList2.add(userList.get(i));
            if (surveyList2 != null && surveyList2.size() > 0) {
                userList2.get(i).put("surveyList", surveyList2);
            }
        }

        return userList2;
    }

    /**
     * 根据评估员id查询老人及问卷结果表
     *
     * @param jsonObj
     * @return
     */
    public List<Map<String, Object>> selectSurveyResultByAppUserId(JSONObject jsonObj) throws Exception {
        //获取前端传过来的resultId
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(jsonObj.toString());
        //老人list
        List<Map<String, Object>> surveyList = organizationMapper.selectSurveyUserByAppUserId(paramsMap);
        //老人list(含result)
        List<Map<String, Object>> surveyList2 = new ArrayList<>();
        //问卷结果list
        List<Map<String, Object>> resultList = paperMapper.selectResultByAppUserId(paramsMap);
        //每个老人的问卷结果list
        List<Map<String, Object>> surveyResultList = null;
        //老人年龄
        int age = 0;

        for (int i = 0; i < surveyList.size(); i++) {
            surveyResultList = new ArrayList<>();
            age = OrganizationServiceImpl.getAge(surveyList.get(i).get("surveyUserBirthdate").toString());
            //age = surveyList.get(i).get("surveyUserBirthdate")
            for (int j = 0; j < resultList.size(); j++) {
                if (surveyList.get(i).get("surveyUserId").equals(resultList.get(j).get("surveyUserId")) && resultList.get(j).get("resultStatus").toString().equals("10")) {
                    surveyResultList.add(resultList.get(j));
                }
            }
            if (surveyResultList != null && surveyResultList.size() > 0) {
                surveyList.get(i).put("age", age);
                surveyList.get(i).put("resultList", surveyResultList);
                surveyList2.add(surveyList.get(i));
            }
        }
        return surveyList2;
    }

    /**
     * 导出pdf
     *
     * @param jsonObj
     * @return
     * @throws Exception
     */
    public String expotResultByResultId(JSONObject jsonObj, HttpServletResponse response) throws Exception {
        //获取前端传过来的resultId
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(jsonObj.toString());
        //查询result
        Map<String, Object> resultMap = paperMapper.selectResultByResultId(paramsMap);
        if (resultMap == null) {
            return "没有指定结果表！";
        }
        //被调查老人map
        Map<String, Object> surveyUserMap = organizationMapper.selectSurveyUserBySurveyUserId(resultMap);
        //评论员map

        //题目list(无选项)
        List<Map<String, Object>> questionList = paperMapper.selectQuestionByPaperId(resultMap);
        //题目list(有选项)
        List<Map<String, Object>> questionList2;
        //选项list(包含未回答)
        List<Map<String, Object>> optionList = paperMapper.selectOptionByPaperId(resultMap);
        //选项list(不包含未回答)
        List<Map<String, Object>> optionList2 = new ArrayList<>();
        //查询answer
        List<Map<String, Object>> answerList = paperMapper.selectAnswerByResultId(paramsMap);
        //导出字符串list
        List<String> stringList = new ArrayList<>();
        //地址map
        Map<String, Object> surveyMap = new HashMap<>();
        //评估员信息
        Map<String, Object> appUserMap = userMapper.selectAppUserByAppUserId(surveyUserMap);

        if (optionList != null && optionList.size() > 0 && answerList != null && answerList.size() > 0) {
            for (int i = 0; i < optionList.size(); i++) {
                optionList2.add(optionList.get(i));

                for (int j = 0; j < answerList.size(); j++) {
                    if (answerList.get(j).get("optionId").equals(optionList.get(i).get("optionId"))) {
                        optionList2.get(i).put("answer", "*");
                        break;
                    } else {
                        optionList2.get(i).put("answer", "o");
                    }
                }
            }
        }

        questionList2 = questionAddOption(questionList, optionList2);

        //拼接地址
        String surveyAddress = "";
        surveyMap.put("code", surveyUserMap.get("provinceCode").toString());
        surveyMap = organizationMapper.selectAreaByCode(surveyMap);
        surveyAddress = surveyAddress + surveyMap.get("areaName").toString();
        surveyMap.put("code", surveyUserMap.get("cityCode").toString());
        surveyMap = organizationMapper.selectAreaByCode(surveyMap);
        surveyAddress = surveyAddress + surveyMap.get("areaName").toString();
        surveyMap.put("code", surveyUserMap.get("countyCode").toString());
        surveyMap = organizationMapper.selectAreaByCode(surveyMap);
        surveyAddress = surveyAddress + surveyMap.get("areaName").toString();
        System.out.println("地址：====================================" + surveyAddress);

        //设置导出字符串
        stringList.add(resultMap.get("paperName").toString() + "(总分：" + resultMap.get("paperScore").toString() + "分)");
        stringList.add("\n\n");
        stringList.add("评估时间：" + resultMap.get("createTime").toString());
        stringList.add("姓名：" + surveyUserMap.get("surveyUserName").toString());
        if (surveyUserMap.get("surveyUserSex").toString().equals("0")) {
            stringList.add("性别：男");
        } else if (surveyUserMap.get("surveyUserSex").toString().equals("1")) {
            stringList.add("性别：女");
        }
        stringList.add("评估员：" + appUserMap.get("appUserName").toString());
        stringList.add("地址：" + surveyAddress);
        stringList.add("详细地址：" + surveyUserMap.get("detailAddress").toString());
        stringList.add("年龄：" + OrganizationServiceImpl.getAge(surveyUserMap.get("surveyUserBirthdate").toString()));

        stringList.add("\n\n");
        for (int i = 0; i < questionList2.size(); i++) {
            stringList.add(i + 1 + "." + questionList2.get(i).get("questionName").toString());
            JSONArray optionjSON = JSONArray.fromObject(questionList2.get(i).get("optionList"));
            for (int j = 0; j < optionjSON.size(); j++) {
                stringList.add(optionjSON.getJSONObject(j).get("answer").toString()
                        + "." + optionjSON.getJSONObject(j).get("optionContent").toString()
                        + "。(" + optionjSON.getJSONObject(j).get("optionScore").toString() + "分)");
                //stringList.add("\n");
            }
            stringList.add("\n\n");
        }
        return PDF(stringList, response, String.valueOf(jsonObj.get("uuId")));
    }

    public static String PDF(List<String> stringList, HttpServletResponse response, String UUID) throws Exception {
        //获取部署地址
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        String path1 = path + UUID + ".pdf";
        //导出pdf
        // 第一步，实例化一个document对象
        Document document = new Document();
        // 第二步，设置要到出的路径
        FileOutputStream out = new FileOutputStream(path1);
        // 如果是浏览器通过request请求需要在浏览器中输出则使用下面方式
//        OutputStream out = response.getOutputStream();
//        response.setContentType("text/pdf;charset=UTF-8");
        //response.setHeader("content-type", "text/html;charset=UTF-8");
        // 第三步,设置字符
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
        //BaseFont bfChinese = BaseFont.createFont("iso-8859-1", "utf-8", false);
        Font font1 = new Font(bfChinese, 12.0F, 0);
        font1.setStyle(Font.BOLD);

        Font fontZH = new Font(bfChinese, 12.0F, 0);
        // 第四步，将pdf文件输出到磁盘
        PdfWriter writer = PdfWriter.getInstance(document, out);
        // 第五步，打开生成的pdf文件
        document.open();
        // 第六步,设置内容
        document.add(new Paragraph(new Chunk(stringList.get(0), font1).setLocalDestination(stringList.get(0))));
        for (int i = 1; i < stringList.size(); i++) {
            document.add(new Paragraph(new Chunk(stringList.get(i), fontZH).setLocalDestination(stringList.get(i))));
        }
//        for (String l : stringList
//        ) {
//            l
//            document.add(new Paragraph(new Chunk(l, fontZH).setLocalDestination(l)));
//        }
        // 第七步，关闭document
        document.close();

        return path1;
    }


    public static void main(String[] args) throws Exception {
        //word();
        pdf();
    }

    public static void pdf() throws Exception {
        // 第一步，实例化一个document对象
        Document document = new Document();
        // 第二步，设置要到出的路径
        FileOutputStream out = new FileOutputStream("D:/office/workbook111.pdf");
        // 如果是浏览器通过request请求需要在浏览器中输出则使用下面方式
        // OutputStream out = response.getOutputStream();
        // 第三步,设置字符
        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
        Font fontZH = new Font(bfChinese, 12.0F, 0);
        // 第四步，将pdf文件输出到磁盘
        PdfWriter writer = PdfWriter.getInstance(document, out);
        // 第五步，打开生成的pdf文件
        document.open();
        // 第六步,设置内容
        String title = "标题";
        document.add(new Paragraph(new Chunk(title, fontZH).setLocalDestination(title)));
        document.add(new Paragraph("\n"));
//         创建table,注意这里的2是两列的意思,下面通过table.addCell添加的时候必须添加整行内容的所有列
//        PdfPTable table = new PdfPTable(2);
//        PdfPCell pdfPCell = new PdfPCell();
//        //pdfPCell.setBorder(0);
//        table.setWidthPercentage(100.0F);
//        table.setHeaderRows(1);
//        table.getDefaultCell().setHorizontalAlignment(1);
//        table.addCell(new Paragraph("序号", fontZH));
//        table.addCell(new Paragraph("结果", fontZH));
//        table.addCell(new Paragraph("1", fontZH));
//        table.addCell(new Paragraph("出来了", fontZH));
//
//        document.add(table);
//        document.add(new Paragraph("\n"));
//         第七步，关闭document
        document.close();

        System.out.println("导出pdf成功~");

    }

    public static void word() throws Exception {
        XWPFDocument doc = new XWPFDocument(); //创建word文件
        XWPFParagraph p1 = doc.createParagraph(); //创建段落
        p1.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun r1 = p1.createRun(); //创建段落文本
        r1.setText("Helloworld"); //设置文本
        r1.addBreak(); // 换行
        r1.setText("世界你好!");

        // TODO 其他操作请自己百度
        FileOutputStream outputStream = new FileOutputStream("D:\\office\\word.docx"); // 保存文件的路径
        doc.write(outputStream); // 保存Excel文件
        outputStream.close(); // 关闭文件流

        System.out.println("word--1生成成功!");
    }
}
