package com.example.quickstart.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * <p>
 * RAS 加密算法工具类
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-06-28 15:18
 */
public class RsaUtil {


    /**
     * 用于封装随机产生的公钥与私钥
     */
    private static KeyPair keyPair = null;

    public static void main(String[] args) throws Exception {
        //加密字符串
        String message = "1234567";
        System.out.println("随机生成的公钥为:" + RsaUtil.getRsaPublicKey());
        System.out.println("随机生成的私钥为:" + RsaUtil.getRsaPrivateKey());
        String messageEn = encrypt(message);
        System.out.println(message + "\t加密后的字符串为:" + messageEn);
        String messageDe = decrypt(messageEn);
        System.out.println("还原后的字符串为:" + messageDe);
    }

    /**
     * 随机生成密匙对
     */
    private static KeyPair getKeyPair() {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
            // 初始化密钥对生成器，密钥大小为96-1024位
            keyPairGen.initialize(1024, new SecureRandom());
            // 生成一个密钥对，保存在keyPair中
            return keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取RSA加密公钥
     *
     * @return 返回公钥
     */
    public static String getRsaPublicKey() {
        if (keyPair == null) {
            KeyPair keys = getKeyPair();
            if (keys == null) {
                return null;
            }
            keyPair = keys;
        }
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        return new String(Base64.encodeBase64(publicKey.getEncoded()));
    }

    /**
     * 获取RSA解密私钥
     *
     * @return 返回私钥
     */
    public static String getRsaPrivateKey() {
        if (keyPair == null) {
            KeyPair keys = getKeyPair();
            if (keys == null) {
                return null;
            }
            keyPair = getKeyPair();
        }
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new String(Base64.encodeBase64(privateKey.getEncoded()));
    }

    /**
     * RSA公钥加密
     *
     * @param str 加密字符串
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str) throws Exception {
        String publicKey = getRsaPublicKey();
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.encodeBase64String(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * RSA私钥解密
     *
     * @param str 加密字符串
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str) throws Exception {
        String privateKey = getRsaPrivateKey();
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes(StandardCharsets.UTF_8));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }

}
