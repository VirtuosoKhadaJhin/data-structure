package com.nuanyou.cms.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kevin on 2016/8/13.
 */
public class DistanceUtils {

    private static double EARTH_RADIUS = 6378137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两个位置的经纬度，来计算两地的距离（单位为M）
     * 参数为String类型
     *
     * @param srcLat 用户经度
     * @param srcLng 用户纬度
     * @param tarLat 商家经度
     * @param tarLng 商家纬度
     * @return
     */
    public static int getDistance(Double srcLat, Double srcLng, Double tarLat, Double tarLng) {
        double radLat1 = rad(srcLat);
        double radLat2 = rad(tarLat);
        double difference = radLat1 - radLat2;
        double mDifference = rad(srcLng) - rad(tarLng);
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(difference / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(mDifference / 2), 2)));
        distance = distance * EARTH_RADIUS;
        distance = Math.round(distance * 10000) / 10000;

        return (int) distance;
    }

    /**
     * 获取当前用户一定距离以内的经纬度值
     * 单位米 return minLat
     * 最小经度 minLng
     * 最小纬度 maxLat
     * 最大经度 maxLng
     * 最大纬度 minLat
     */
    public static Map getAround(double latitude, double longitude, double raidusMile) {
        Map map = new HashMap();
        Double degree = (24901 * 1609) / 360.0; // 获取每度

        Double mpdLng = Double.parseDouble((degree * Math.cos(latitude * (Math.PI / 180)) + "").replace("-", ""));
        Double dpmLng = 1 / mpdLng;
        Double radiusLng = dpmLng * raidusMile;
        //获取最小经度
        Double minLat = longitude - radiusLng;
        // 获取最大经度
        Double maxLat = longitude + radiusLng;

        Double dpmLat = 1 / degree;
        Double radiusLat = dpmLat * raidusMile;
        // 获取最小纬度
        Double minLng = latitude - radiusLat;
        // 获取最大纬度
        Double maxLng = latitude + radiusLat;

        map.put("minLat", minLat + "");
        map.put("maxLat", maxLat + "");
        map.put("minLng", minLng + "");
        map.put("maxLng", maxLng + "");

        return map;
    }

    //初始化距离值
    public static String initDistance(int distance) {
        String result = String.valueOf(distance);
        if (distance < 1000) {
            result += "m";
        } else {
            result = Math.ceil(distance * 10 / 1000) / 10 + "km";
        }
        return result;
    }

}