package com.xydl.common.utils;

/**
 * All rights Reserved, Designed By www.XXXX.com
 *
 * @author 陈晨
 * @version V1.0.0
 * @projectName xydl-api
 * @title AESUtil AES加密处理工具类
 * @package com.xydl.common.utils
 * @description AESUtil
 * @date 2020/8/14 17:41
 * @copyright 2020 www.XXXXX.com
 * 注意 本内容仅限于 南京星源动力信息技术有限公司，禁止外泄以及用于其他的商业
 */

import org.apache.tomcat.util.codec.binary.Base64;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;

public class AESUtil {
    //密钥 (需要前端和后端保持一致)
    private static final String KEY = "F9BE6D002BC721FB";
    //算法
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

    /**
     * aes解密
     * @param encrypt   内容
     * @return
     * @throws Exception
     */
    public static String decrypt(String encrypt) {
        try {
            return decrypt(encrypt, KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * aes加密
     * @param content
     * @return
     * @throws Exception
     */
    public static String encrypt(String content) {
        try {
            return encrypt(content, KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 将byte[]转为各种进制的字符串
     * @param bytes byte[]
     * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
     * @return 转换后的字符串
     */
    public static String binary(byte[] bytes, int radix){
        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
    }

    /**
     * base 64 encode
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes){
        return Base64.encodeBase64String(bytes);
    }

    /**
     * base 64 decode
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception{
        return StringUtils.isEmpty(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);
    }


    /**
     * AES加密
     * @param content 待加密的内容
     * @param key 加密密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    public static byte[] aesEncryptToBytes(String content, String key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);
        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));

        return cipher.doFinal(content.getBytes("utf-8"));
    }


    /**
     * AES加密为base 64 code
     * @param content 待加密的内容
     * @param key 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String encrypt(String content, String key) throws Exception {
        return base64Encode(aesEncryptToBytes(content, key));
    }

    /**
     * AES解密
     * @param encryptBytes 待解密的byte[]
     * @param key 解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(128);

        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }


    /**
     * 将base 64 code AES解密
     * @param encryptStr 待解密的base 64 code
     * @param key 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String decrypt(String encryptStr, String key) throws Exception {
        return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), key);
    }

    /**
     * 测试
     */
    public static void main(String[] args) throws Exception {
//        String content = "123";
//        System.out.println("加密前：" + content);
//        System.out.println("加密密钥和解密密钥：" + KEY);
//        String encrypt = encrypt(content);
//        System.out.println("加密后：" + encrypt);
//        String decrypt = decrypt(encrypt);
//        System.out.println("解密后：" + decrypt);

        String content = "{msg=操作成功, code=200, data=[{password=123456, create_time=2020-07-28 15:19:47.0, user_name=陈晨, login_time=2020-07-28 15:19:45.0, phonenumber=15895889836, id=1}, {password=123456, create_time=2020-08-04 17:57:11.0, user_name=测试, login_time=2020-08-04 17:57:11.0, phonenumber=15895889835, id=2}]}";
        String encryptStr = encrypt(content,"F9BE6D002BC721FB");
        System.out.println("加密后参数：" + encryptStr);
        String decryptStr = decrypt("BI/HrtwSdosXwIHXdaUhbCCJnX05rdR83hNvZhK8UwAbMSRKz7dO2HK4xc8ruBJzZKKDmqR5tJ3wVf00UAGJIRX149QlnUtz5Ogbg/NvrZ8zrDgxtexPHtWyCUGBzu4Q1ZdNmHf+WxjMbrNAMa7BUejR20hzPxv+a+yxv4JMLCkl07xJY9ILbcI7uq4q00rfjmpACtpwVwZ7frA/s6DGuVh732Q3D8jC2iDXlZXvOPZhS7rF1XD/8w4/UN8ySY8lCuRCd8l5h2os/P4dimIVhTit1Mzlnh6ypiS/eCsIYS9WJeQRrkfeEerkPiCScCCO4/KHmNSIusqNajowRsIMPRVrBubYOEvE0RdI78Su9jw/TdmVaedyoSO5bscPnfS/LweGWc58VIYd+LqaaPhggVt1H+s5MSqBiGK8tsJIM8o=","F9BE6D002BC721FB");
        System.out.println("解密后参数：" + decryptStr);
    }
}