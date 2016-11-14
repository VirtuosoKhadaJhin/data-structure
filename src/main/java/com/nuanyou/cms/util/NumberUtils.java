package com.nuanyou.cms.util;

public class NumberUtils {

    public static Integer toInt(String str) {
        return toInt(str, null);
    }

    public static Integer toInt(String str, Integer def) {
        if (str == null)
            return def;
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static Long toLong(String str) {
        return toLong(str, null);
    }

    public static Long toLong(String str, Long def) {
        if (str == null)
            return def;
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    public static Double toDouble(String str) {
        return toDouble(str, null);
    }

    public static Double toDouble(String str, Double def) {
        if (str == null)
            return def;
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return def;
        }
    }

}