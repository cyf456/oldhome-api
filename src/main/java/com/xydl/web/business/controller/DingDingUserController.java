package com.xydl.web.business.controller;

import com.xydl.web.business.entity.DingAccessTokenDTO;
import com.xydl.web.business.entity.DingUserDTO;
import com.xydl.web.business.entity.DingUserIdDTO;
import com.xydl.web.business.service.DingAuthService;
import com.xydl.web.business.service.DingUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/ding")
public class DingDingUserController {

    @Autowired
    private DingAuthService dingAuthService;

    @Autowired
    private DingUserService dingUserService;

    /**
     * 根据前台初始化后获取的免登授权码获取用户信息
     *
     * @param code 免登授权码
     * @return
     */
    @GetMapping("/login")
    public Map<String, Object> authCodeLogin(@RequestParam("code") String code) {

        DingAccessTokenDTO accessTokenDTO = dingAuthService.accessToken();
        DingUserIdDTO userIdDTO = dingUserService.getUserId(accessTokenDTO.getAccess_token(), code);
        DingUserDTO userInfo = dingUserService.getUserInfo(accessTokenDTO.getAccess_token(), userIdDTO.getUserid());
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("code", code);
        result.put("token", accessTokenDTO.getAccess_token());

        result.put("user", userInfo);

        log.debug("[钉钉] 用户免登, 根据免登授权码code, corpId获取用户信息, code: {}, result:{}", code, result);

        return result;
    }
}

