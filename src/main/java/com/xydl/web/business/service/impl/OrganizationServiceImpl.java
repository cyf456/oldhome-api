package com.xydl.web.business.service.impl;

import com.xydl.common.utils.JsonUtils;
import com.xydl.common.utils.uuid.IdUtils;
import com.xydl.web.business.dao.OrganizationMapper;
import com.xydl.web.business.service.OrganizationService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    /**
     * 根据id查询机构
     *
     * @return 机构
     */
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


    /**
     * 添加老人信息
     * @param jsonObject
     * @return
     */
    public Map<String,Object> insertSurveyUser(JSONObject jsonObject) throws ParseException {
        Map<String,Object> map = new HashMap<>();
        //将json类型转换成Map集合
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(jsonObject.toString());
        //把出生日期字符串转换为日期格式。
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date  birthDay = sdf.parse(paramsMap.get("surveyUserBirthdate").toString());
        paramsMap.put("surveyUserBirthdate",birthDay);
        //老人id
        String surveyUserId="";
        int num = 0;
        if(paramsMap.get("surveyUserId").toString().equals("0")){
            surveyUserId = IdUtils.fastSimpleUUID();

            //添加id
            paramsMap.put("surveyUserId", surveyUserId);
            num = organizationMapper.insertSurveyUser(paramsMap);
        }else {
            surveyUserId = paramsMap.get("surveyUserId").toString();
            num = organizationMapper.updateSurveyUser(paramsMap);
        }
        map.put("surveyUserId",surveyUserId);
        map.put("num",num);
        //添加机构信息
        return map;
    }

    /**
     * 根据机构id查询用户
     *
     * @return 机构
     */
    public List<Map<String, Object>> selectAppUserByOrganizationId(JSONObject jsonObj) {
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(jsonObj.toString());
        return organizationMapper.selectAppUserByOrganizationId(paramsMap);
    }

    /**
     * 根据评论员id查询老人
     * @param
     * @return
     */
    public List<Map<String, Object>> selectSurveyUserByAppUserId(JSONObject jsonObj) throws Exception {
        Map<String, Object> paramsMap = JsonUtils.jsonStrToMap(jsonObj.toString());
        //查询地址
        Map<String, Object> areaMap = new HashMap<>();

        //老人list
        List<Map<String, Object>> surveyUserList = organizationMapper.selectSurveyUserByAppUserId(paramsMap);
        //老人list(含地区)
        List<Map<String, Object>> surveyUserList2 = new ArrayList<>();
        for (int i = 0; i < surveyUserList.size(); i++) {
            String birthDay = surveyUserList.get(i).get("surveyUserBirthdate").toString();
            String birthDay2 = birthDay.substring(0,10);
            surveyUserList2.add(surveyUserList.get(i));
            surveyUserList2.get(i).put("surveyUserBirthdate",birthDay2);
        }
//        //老人list(年龄)
//        List<Map<String, Object>> surveyUserList2 = new ArrayList<>();
//        if(surveyUserList != null && surveyUserList.size() > 0){
//            for (int i = 0; i < surveyUserList.size() ; i++) {
//                surveyUserList2.add(surveyUserList.get(i));
//                surveyUserList2.get(i).put("age",getAge(surveyUserList2.get(i).get("age").toString()));
//            }
//        }
        return surveyUserList2;
    }

    /**
     * 计算年龄
     * @param strDate
     * @return
     * @throws Exception
     */
    public static  int getAge(String strDate) throws Exception {
        //把出生日期字符串转换为日期格式。
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date  birthDay = sdf.parse(strDate);

        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        int monthNow = cal.get(Calendar.MONTH);  //当前月份
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;   //计算整岁数
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;//当前日期在生日之前，年龄减一
            }else{
                age--;//当前月份在生日之前，年龄减一
            }
        }
        return age;
    }
}
