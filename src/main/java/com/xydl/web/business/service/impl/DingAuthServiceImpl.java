package com.xydl.web.business.service.impl;

import com.xydl.common.core.redis.RedisCache;
import com.xydl.common.utils.constant.Constants;
import com.xydl.common.utils.constant.DingCodeEnum;
import com.xydl.common.utils.constant.DingProperties;
import com.xydl.web.business.entity.DingAccessTokenDTO;
import com.xydl.web.business.service.DingAuthService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class DingAuthServiceImpl implements DingAuthService{

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DingProperties dingProperties;

    private int expireTime = 120;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Autowired
    private RedisCache redisCache;

//    DingAuthService authService = ()->{
//        DingAccessTokenDTO dingAccessTokenDTO;
//        JSONObject dingTokenObject = JSONObject.fromObject(redisCache.getCacheObject(Constants.DINGDING_ACCESS_TOKEN));
//        log.info("dingTokenObjectqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"+dingTokenObject.toString());
//        if(dingTokenObject != null && dingTokenObject.size() > 0){
//            dingAccessTokenDTO = verifyToken(dingTokenObject);
//        }else {
//            dingAccessTokenDTO = createToken();
//        }
//
//        return dingAccessTokenDTO;};

    /**
     * 获取钉钉令牌信息
     *
     * @return 用户信息
     */
    public DingAccessTokenDTO getDingLoginUser()
    {
        DingAccessTokenDTO dingAccessTokenDTO;
        JSONObject dingTokenObject = JSONObject.fromObject(redisCache.getCacheObject(Constants.DINGDING_ACCESS_TOKEN));
        log.info("dingTokenObjectqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"+dingTokenObject.toString());
        if(dingTokenObject != null && dingTokenObject.size() > 0){
            dingAccessTokenDTO = verifyToken(dingTokenObject);
        }else {
            dingAccessTokenDTO = createToken();
        }

        return dingAccessTokenDTO;
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param dingTokenObject 令牌
     * @return 令牌
     */
    public DingAccessTokenDTO verifyToken(JSONObject dingTokenObject)
    {
        long expireTime = dingTokenObject.getLong("expireTime");
        long currentTime = System.currentTimeMillis();
        log.info("过期时间============================================="+expireTime);
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN)
        {
            log.info("重新获取token");
            return createToken();
        }else {
            DingAccessTokenDTO dingAccessTokenDTO = new DingAccessTokenDTO();
            dingAccessTokenDTO.setAccess_token(dingTokenObject.getString("access_token"));
            dingAccessTokenDTO.setErrmsg(dingTokenObject.getString("errmsg"));
            dingAccessTokenDTO.setErrcode(dingTokenObject.getInt("errcode"));
            dingAccessTokenDTO.setExpires_in(dingTokenObject.getLong("expires_in"));
            return dingAccessTokenDTO;
        }
    }

    /**
     * 保存钉钉令牌
     *
     * @param
     * @return 令牌
     */
    public DingAccessTokenDTO createToken()
    {
        DingAccessTokenDTO  dingAccessTokenDTO = accessToken();
        JSONObject dingTokenObject = new JSONObject();
        dingTokenObject.put("errcode",dingAccessTokenDTO.getErrcode());
        dingTokenObject.put("access_token",dingAccessTokenDTO.getAccess_token());
        dingTokenObject.put("errmsg",dingAccessTokenDTO.getErrmsg());
        dingTokenObject.put("expires_in",dingAccessTokenDTO.getExpires_in());
        refreshToken(dingTokenObject);
        return dingAccessTokenDTO;
    }

    /**
     * 刷新令牌有效期
     *
     * @param dingTokenObject 登录信息
     */
    public void refreshToken(JSONObject dingTokenObject)
    {
        dingTokenObject.put("loginTime",System.currentTimeMillis());
        dingTokenObject.put("expireTime",dingTokenObject.getLong("loginTime") + dingProperties.expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String dingKey = Constants.DINGDING_ACCESS_TOKEN;
        log.info("userObj.toString+++++++++++++++++++++++++++++++++++++++++++++++"+dingTokenObject.toString());
        redisCache.setCacheObject(dingKey, dingTokenObject.toString(), dingProperties.expireTime, TimeUnit.MINUTES);
    }

    public DingAccessTokenDTO accessToken(){
        long starTime = System.nanoTime();
        DingAccessTokenDTO dingAccessToken = null;
        String url = dingProperties.getDingAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map map = new HashMap();
        map.put("appkey", dingProperties.getAppKey());
        map.put("appsecret", dingProperties.getAppSecret());
        ResponseEntity<DingAccessTokenDTO> response = restTemplate.getForEntity(url, DingAccessTokenDTO.class,map);
        if(response.getStatusCode().is2xxSuccessful()){
            if(response.getBody()!=null && response.getBody().getErrcode().equals(DingCodeEnum.success.getCode())){
                dingAccessToken = response.getBody();
            }
        }
        if(dingAccessToken == null){
            log.warn("[1.1.1]获取钉钉token失败,地址:{},参数:{}",url,map);
        }

        log.info("[1.1.2]获取钉钉token结束,耗时:{}ms",(System.nanoTime()-starTime)/1000);
        return dingAccessToken;
    }


}
