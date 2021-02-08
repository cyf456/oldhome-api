package com.xydl.common.utils.constant;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "ding.key")
public class DingProperties {

    private String appKey;
    private String appSecret;
    private String corpId;
    public String dingAccessToken;
    public String dingGetUserId;
    public String dingGetUser;
    public int expireTime;
}
