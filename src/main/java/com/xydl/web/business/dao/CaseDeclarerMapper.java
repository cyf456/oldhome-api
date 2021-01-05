package com.xydl.web.business.dao;

import java.util.Map;

/**
 * All rights Reserved, Designed By www.XXXX.com
 *
 * @author 陈晨
 * @version V1.0.0
 * @projectName xydl-api
 * @title CaseDeclarerMapper
 * @package com.xydl.web.business.dao
 * @description CaseDeclarerMapper
 * @date 2020/8/15 17:12
 * @copyright 2020 www.XXXXX.com
 * 注意 本内容仅限于 南京星源动力信息技术有限公司，禁止外泄以及用于其他的商业
 */
public interface CaseDeclarerMapper {

    /**
     *
     * @param null
     * @Description 保存案件申报信息  <br/>
     * @return 
     * @throw  
     * @author 陈晨
     * @createTime 2020/8/15 17:42
     * @Version V1.0.0
     * @Copyright : www.XXXX.com Inc. All rights reserved.
     * @UpateLog :		Modifier		ModifyTime		Reason/Contents
     *            ---------------------------------------------------------
     *
     */
    public int saveCaseDeclarerData(Map<String, Object> paramsMap);
}
