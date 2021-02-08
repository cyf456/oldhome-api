package com.xydl.web.business.service;

import com.xydl.web.business.entity.DingUserDTO;
import com.xydl.web.business.entity.DingUserIdDTO;

public interface DingUserService {

    DingUserIdDTO getUserId(String access_token, String code);

    DingUserDTO getUserInfo(String access_token, String userid);

    /**
     * 添加钉钉用户信息
     * @param dingUserDTO
     * @return
     */
    public int addDingDingUser(DingUserDTO dingUserDTO);
}
