package com.example.quickstart.utils;

import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;

/**
 * <p>
 * MD5 加密工具类
 * </p>
 *
 * @author ZhangXianYu   Email: 1600501744@qq.com
 * @since 2020-07-13 11:39
 */
public class Md5Util {

    private static final String CHARSET_NAME = "UTF-8";

    private static String encode(byte[] bytes) {
        return DigestUtils.md5DigestAsHex(bytes).toUpperCase();
    }

    public static String encode(String arg) {
        try {
            return encode(arg.getBytes(CHARSET_NAME));
        } catch (UnsupportedEncodingException e) {
            return encode(arg.getBytes());
        }
    }

}
