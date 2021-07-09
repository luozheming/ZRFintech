package com.utils;

import okhttp3.HttpUrl;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

public class SignUtil {

    private static final String ENCODING = "UTF-8";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    /**
     * SHA256WithRSA签名
     *
     * @param data
     * @param privateKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     * @throws SignatureException
     * @throws UnsupportedEncodingException
     */
    public static byte[] sign256(String data, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException,
            SignatureException, UnsupportedEncodingException {

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);

        signature.initSign(privateKey);

        signature.update(data.getBytes(ENCODING));

        return signature.sign();
    }

    public static boolean verify256(String data, byte[] sign, PublicKey publicKey) {
        if (data == null || sign == null || publicKey == null) {
            return false;
        }

        try {
            Signature signetcheck = Signature.getInstance(SIGNATURE_ALGORITHM);
            signetcheck.initVerify(publicKey);
            signetcheck.update(data.getBytes("UTF-8"));
            return signetcheck.verify(sign);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * sha1加密
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String getSha1Str(String data) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        messageDigest.update(data.getBytes(StandardCharsets.UTF_8));
        return null;
    }

    /**
     * MD5加密
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String getMD5Str(String data) throws NoSuchAlgorithmException {
        // 生成一个MD5加密计算摘要
        MessageDigest md = MessageDigest.getInstance("MD5");
        // 计算md5函数
        md.update(data.getBytes());
        // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
        return new BigInteger(1, md.digest()).toString(16);
    }

    /**
     * 二进制数据编码为BASE64字符串
     *
     * @param
     * @return
     */
    public static String encodeBase64(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    /**
     * BASE64解码
     *
     * @param bytes
     * @return
     */
    public static byte[] decodeBase64(byte[] bytes) {
        byte[] result = null;
        try {
            result = Base64.decodeBase64(bytes);
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    /**
     * 构建签名信息
     * @param method
     * @param url
     * @param nonceStr
     * @param body
     * @return
     */
    public static String buildMessage(String method, HttpUrl url, String nonceStr, String body) {
        String canonicalUrl = url.encodedPath();
        if (url.encodedQuery() != null) {
            canonicalUrl += "?" + url.encodedQuery();
        }
        String timeStamp = String.valueOf(System.currentTimeMillis());// 时间戳
        //官方的方法自动做了换行的所有动作，注意唤起支付的参数不一样需要更换（这里是统一下单所以直接照搬即可）
        return method + "\n"
                + canonicalUrl + "\n"
                + timeStamp + "\n"
                + nonceStr + "\n"
                + body + "\n";
    }
}