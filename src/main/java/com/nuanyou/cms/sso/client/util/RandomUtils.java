package com.nuanyou.cms.sso.client.util;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    public static final String NUMERIC = "0123456789";
    public static final String ALPHABETIC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String ALPHANUMERIC = "_0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字、下划线,首字母不能为数字)
     */
    public static String randomString(int length) {
        return random(ALPHANUMERIC, length);
    }

    /**
     * 返回一个定长的随机数字
     */
    public static String randomNumber(int length) {
        return random(NUMERIC, length);
    }

    public static String random(String source, int length) {
        Random random = ThreadLocalRandom.current();

        StringBuilder sb = new StringBuilder(length);
        sb.append(source.charAt(random.nextInt(source.length())));
        for (int i = 1; i < length; i++)
            sb.append(source.charAt(random.nextInt(source.length())));
        return sb.toString();
    }

}