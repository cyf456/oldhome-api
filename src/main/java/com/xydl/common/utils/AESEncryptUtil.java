package com.xydl.common.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * <p>AES加密处理工具类</p>
 * @author chenchen
 * @version 2020/7/29
 */
public class AESEncryptUtil {

    /**
     * AES加密
     * @param content  字符串内容
     * @param password 密钥
     */
    public static String encrypt(String content, String password) throws Exception{
        return aes(content,password,Cipher.ENCRYPT_MODE);
    }


    /**
     * AES解密
     * @param content  字符串内容
     * @param password 密钥
     */
    public static String decrypt(String content, String password) throws Exception{
        return aes(content,password,Cipher.DECRYPT_MODE);
    }

    /**
     * AES加密/解密 公共方法
     * @param content  字符串
     * @param password 密钥
     * @param type     加密：{@link Cipher#ENCRYPT_MODE}，解密：{@link Cipher#DECRYPT_MODE}
     */
    private static String aes(String content, String password, int type) throws Exception{
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes());
        generator.init(128, random);
        SecretKey secretKey = generator.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(type, key);
        if (type == Cipher.ENCRYPT_MODE) {
//            byte[] byteContent = content.getBytes("utf-8");
//            return Hex2Util.parseByte2HexStr(cipher.doFinal(byteContent));
            byte[] result = cipher.doFinal(content.getBytes("utf-8"));
            return new String(Base64.encodeBase64(result));
        } else {
//            byte[] byteContent = Hex2Util.parseHexStr2Byte(content);
//            return new String(cipher.doFinal(byteContent));
            byte[] result = cipher.doFinal(Base64.decodeBase64(content.getBytes()));
            return new String(result, "utf-8");
        }
    }


    /**
     * 随机生成秘钥
     */
    public static void getKey(){
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128);//要生成多少位，只需要修改这里即可128, 192或256
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            String s = byteToHexString(b);
            System.out.println(s.toUpperCase());
            System.out.println("十六进制密钥长度为"+s.length());
            System.out.println("二进制密钥的长度为"+s.length()*4);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.out.println("没有此算法。");
        }
    }

    /**
     * byte数组转化为16进制字符串
     * @param bytes
     * @return
     */
    public static String byteToHexString(byte[] bytes){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String strHex=Integer.toHexString(bytes[i]);
            if(strHex.length() > 3){
                sb.append(strHex.substring(6));
            } else {
                if(strHex.length() < 2){
                    sb.append("0" + strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }
        return  sb.toString();
    }





    public static void main(String[] args){
        try {
//            getKey();
            String content = "{msg=操作成功, code=200, data=[{password=123456, create_time=2020-07-28 15:19:47.0, user_name=陈晨, login_time=2020-07-28 15:19:45.0, phonenumber=15895889836, id=1}, {password=123456, create_time=2020-08-04 17:57:11.0, user_name=测试, login_time=2020-08-04 17:57:11.0, phonenumber=15895889835, id=2}]}";
            String encryptStr = encrypt(content,"F9BE6D002BC721FB9F02D1376EA73B5D");
            System.out.println("加密后参数：" + encryptStr);
            String decryptStr = decrypt("VK+7KET/7IgKSfHgfAAH4yZoaWeHopgqzeNbxN1XUZtmtIViIjDAkc2feYggb7Apc/mIjcKUGDlCQdLKv48j6NZEPO40LGcZSmcOfFeyizpBVdM+vGbTBgQfMzi2I8It3rjtB3Rb1bjYINisJKuE7h1AvjG16CcjJfKe70R4Hg0DQPzTsMXOHhHl1kvfLHl/ecxucNQJVjpVDF8qKC3BvpGHUOwJYCNfsl56HOOWJbtmWBuvV684euHV6q08aVnVVEQ9MPnZ3T+OtQsUJsiN1/JfkzfOiDuCcn1k35HnJqLl0UIxU+9SU17cwBnOxssywZJV2CF2KinNgAPp5OBuV7mYddqwyDhC1TJOsGtfWBojEIVbej1Tzx6ZwNHlf6tfKctJCl1qUL1YvAP/7YBc9AxAjhd+fRTKEIeegyPj8/Ju5ECkZXbPhputAuoAYfCfEwySYAwJig3JHJZWTxAokstdNwU7cgXR0NKdjEFtLV6ywYkxRhi7bHhec+3BUPXghwR+CWu8qJw8p38ojWQIokKtSZpdk0KUJAjgLj5uvPbEYpU5GLljeT99TiFiNin35zQFN+kWOjrVRdQZCsAK2Y/LfGJvQthGJwdHKA2rNLhOQyAeXnXHCn6mwDaLAhBgUD3g1Nph62A/Z6tegp20jEmZNaneS54WLKdBRXohshayNFaYnEnWPPNfWxDWtGZito3CCiBG+Q/Hqi7IcmlX2h/e3hYNSBumO9cjmzGVRiWOcepeE6kbpVG1wdiSHZmw4B5elJiVueAun3wiOj6PblHWu46gpJNdrbOkYLRmx6Em4c+orsGDzxMF6ra716A3XNZ+DoiSxm1O+Tg3Bhwzz30sAmRQp1aLOifc+oQiY6n0mLLih5ZXfeU7YDUEZkSQzsFBaWYMaKHfbqQFVZQTlbLETl+RvUhqgUlKKTNQrT0AdfY4aUQ8n0MIbFax3Z1pZTX6ljIe/gb5w4UnDhNachN3Bp64PGb1ZmAtayZx3yiQ1xjyCnIF16GzCNhfPMBcd3JH23q3kssYC6lNJwI1sg==","F9BE6D002BC721FB9F02D1376EA73B5D");
            System.out.println("解密后参数：" + decryptStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        System.out.println(decrypt("4E65CD9979B7D7900FADA1297D2B0F45EDE320ACDAA7AA57306423BB8A4A6586A3B0FE02B2FF37040C81B9383B3C041E25129D62CCA30B49063CC3CCE83AE8EF8DD7BB453761B9DDEA2D84C473C51E1F3FCA0B89A2D5D2B3FC8D19558BA36D265F8717EE513E72261975457797834DDF92810A6B3996B233C0A75BFDCD98F1383CADDC95B5FAD076FDAE5B31121C852D7F2F30A61C964CD5317DCC08AE31D51D","F9BE6D002BC721FB9F02D1376EA73B5D"));
    }
}

