package com.xydl.common.utils;

import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonUtils {
    //json字符串转换为MAP
    public static Map jsonStrToMap(String s) {
        Map map = new HashMap();
        //注意这里JSONObject引入的是net.sf.json
        JSONObject json = JSONObject.fromObject(s);
        Iterator keys = json.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = json.get(key).toString();
            if (value.startsWith("{") && value.endsWith("}")) {
                map.put(key, jsonStrToMap(value));
            } else {
                map.put(key, value);
            }

        }
        return map;
    }
}
