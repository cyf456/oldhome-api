package com.xydl.web.user.service;

import com.xydl.common.utils.CommonResult;
import net.sf.json.JSONObject;

import java.util.Map;

public interface LoginService {
    public CommonResult login(JSONObject userObj);
}
