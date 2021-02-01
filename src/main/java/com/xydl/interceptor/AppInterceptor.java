package com.xydl.interceptor;

import com.xydl.common.annotation.CheckToken;
import com.xydl.common.annotation.Encrypt;
import com.xydl.common.utils.*;
import com.xydl.common.utils.constant.HttpStatus;
import com.xydl.common.utils.http.HttpHelper;
import com.xydl.web.user.service.TokenService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @类功能说明：App端所有接口请求拦截器
 * @公司名称：南京星源动力信息技术有限公司
 * @作者：chenchen
 * @创建时间：2020/7/29 10:00
 * @版本：V1.0
 */
public class AppInterceptor implements HandlerInterceptor{
    // aeskey
    @Value("${aes.key}")
    private String key;

    @Autowired
    private TokenService tokenService;

    //日志工具类
    private static final Logger log = LoggerFactory.getLogger(AppInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//        log.info("request.getRequestURI====================================="+request.getRequestURI());
//        log.info("request.getRequestURL====================================="+request.getRequestURL());
//        if(request.getRequestURI().equals("/oldhome-api/Paper/expotResultByResultId")){
//            return true;
//        }
        if(request.getMethod().equals("GET")){
            return true;
        }

        /*if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            //支持跨域
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setStatus(200);
            return true;
        } else {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
        }*/

        response.setHeader("Access-Control-Allow-Origin", "*");//允许所有域名访问
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Methods", "*");

        //输入参数
        String requestBody = HttpHelper.getBodyString(request);
        log.info("输入参数：" + requestBody);
        System.out.println("======================================"+requestBody);
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        //验证encrypt
        Encrypt encryptAnnotation = method.getAnnotation(Encrypt.class);

        //校验token
        CheckToken tokenAnnotation = method.getAnnotation(CheckToken.class);

        if(encryptAnnotation != null){
            try {
                //需要对参数进行解密
                log.info("开始解密输入参数...");
                requestBody = AESUtil.decrypt(requestBody, key);
                log.info("解密后输入参数：" + requestBody);
            } catch (Exception e) {
                e.printStackTrace();
                CommonResult commonResult = CommonResult.error(HttpStatus.ERROR,"请求参数解析异常");
                ServletUtils.renderString(response, AESUtil.encrypt(JSONObject.fromObject(commonResult).toString(), key));
                return false;
            }
        }

        request.setAttribute("requestHeader", JSONObject.fromObject(requestBody).getString("header"));

        if (tokenAnnotation != null)
        {
            log.info("开始验证token...");
            //验证token
            JSONObject userObj = tokenService.getLoginUser(request);
            if(StringUtils.isNotEmpty(userObj)){
                tokenService.verifyToken(userObj);
            }else{
                log.info("token验证失败...");
                CommonResult commonResult = CommonResult.error(HttpStatus.FORBIDDEN,"访问受限，授权过期");
                String body = JSONObject.fromObject(commonResult).toString();
                log.info("未加密返回参数：" + body);
                if(encryptAnnotation != null) {
                    log.info("开始加密返回参数...");
                    body = AESUtil.encrypt(body, key);
                    log.info("加密后返回参数：" + body);
                    ServletUtils.renderString(response,  body);
                }else{
                    ServletUtils.renderString(response, body);
                }
                return false;
            }
            log.info("token验证成功...");
        }

        request.setAttribute("requestJson", JSONObject.fromObject(requestBody).getString("body"));
        return true;//继续流程
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
