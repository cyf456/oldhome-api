package com.xydl.web.business.dao;

import com.xydl.web.business.entity.DingUserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * All rights Reserved, Designed By www.XXXX.com
 *
 * @author 陈一帆
 * @version V1.0.0
 * @projectName oldhome-api
 * @title DingUserMapper
 * @package com.xydl.web.business.dao
 * @description
 * @date 2021/2/2 14:45
 * @copyright 2021 www.XXXXX.com
 * 注意 本内容仅限于 南京星源动力信息技术有限公司，禁止外泄以及用于其他的商业
 */
@Mapper
public interface DingUserMapper {
    /**
     * 添加
     * @param dingUserDTO
     * @return
     */
    public int insertDingUser(DingUserDTO dingUserDTO);

    public int selectDingUserByUserId(@Param("userid") String userid);
}
