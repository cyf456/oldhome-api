package com.xydl.web.user.service;

import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

public interface UserService {

    public List<Map<String, Object>> getUserList();

    public int saveAppUser(JSONObject jsonObj);

    public int insertAppUser(JSONObject jsonObj);

    /**
     * 查询手机号码
     * @param
     * @return
     */
    public List<Map<String,Object>> selectAppUser();

}
