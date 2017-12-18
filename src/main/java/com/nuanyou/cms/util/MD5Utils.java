package com.nuanyou.cms.util;

import java.security.MessageDigest;

/**
 * MD5
 * Created by Alan.ye on 2016/10/17.
 */
public class MD5Utils {

    public static String encrypt(String message) {
        MessageDigest md5;
        byte[] data;
        try {
            md5 = MessageDigest.getInstance("MD5");
            data = message.getBytes("UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        byte[] md5Bytes = md5.digest(data);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                result.append("0");
            result.append(Integer.toHexString(val));
        }
        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(MD5Utils.encrypt("dkfk"));
    }

}