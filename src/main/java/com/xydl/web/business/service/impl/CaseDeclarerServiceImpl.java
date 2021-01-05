package com.xydl.web.business.service.impl;

import com.xydl.web.business.dao.CaseDeclarerMapper;
import com.xydl.web.business.service.CaseDeclarerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * All rights Reserved, Designed By www.XXXX.com
 *
 * @author 陈晨
 * @version V1.0.0
 * @projectName xydl-api
 * @title CaseDeclarerServiceImpl
 * @package com.xydl.web.business.service.impl
 * @description CaseDeclarerServiceImpl
 * @date 2020/8/15 17:13
 * @copyright 2020 www.XXXXX.com
 * 注意 本内容仅限于 南京星源动力信息技术有限公司，禁止外泄以及用于其他的商业
 */
@Service
public class CaseDeclarerServiceImpl implements CaseDeclarerService {

    @Autowired
    private CaseDeclarerMapper caseDeclarerMapper;

    public int saveCaseDeclarerData(Map<String, Object> paramsMap){
        return caseDeclarerMapper.saveCaseDeclarerData(paramsMap);
    }
}
