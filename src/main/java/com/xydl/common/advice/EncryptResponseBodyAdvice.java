package com.xydl.common.advice;

import com.xydl.common.annotation.Encrypt;
import com.xydl.common.utils.AESUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class EncryptResponseBodyAdvice implements ResponseBodyAdvice {

    private static final Logger log = LoggerFactory.getLogger(EncryptResponseBodyAdvice.class);

    // aeskey
    @Value("${aes.key}")
    private String key;

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        try {
            log.info("未加密返回参数：" + JSONObject.fromObject(body));
            Encrypt encrypt = returnType.getMethodAnnotation(Encrypt.class);
            if(encrypt != null){
                log.info("开始加密返回参数...");
                body = AESUtil.encrypt(JSONObject.fromObject(body).toString(), key);
                log.info("加密后返回参数：" + body);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }
}
