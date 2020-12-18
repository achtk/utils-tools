package com.chua.utils.tools.util;

/**
 * 经纬度工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/18
 */
public class LocationUtils {
    /**
     * 半径
     */
    private static double EARTH_RADIUS = 6378137;
    /**
     * rad
     */
    private static double RAD = Math.PI / 180.0;

    /**
     * 通过经纬度获取距离(单位: 米)
     *
     * @param lat1 经度1
     * @param lng1 纬度1
     * @param lat2 经度2
     * @param lng2 纬度2
     * @return double返回类型距离
     */
    public static double getDistance(final double lng1, final double lat1, final double lng2, final double lat2) {
        double radLat1 = lat1 * RAD;
        double radLat2 = lat2 * RAD;
        double a = radLat1 - radLat2;
        double b = (lng1 - lng2) * RAD;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        return Math.round(s * EARTH_RADIUS * 10000D) / 10000D;
    }


    /**
     * 判断是否在范围内
     *
     * @param lat1   经度1
     * @param lng1   纬度1
     * @param lat2   经度2
     * @param lng2   纬度2
     * @param radius 圆的半径
     * @return boolean
     */
    public boolean isInCircle(final double lat1, final double lng1, final double lat2, final double lng2, final int radius) {
        double dLat = (lat2 - lat1) * Math.PI / 180;
        double dLng = (lng2 - lng1) * Math.PI / 180;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = EARTH_RADIUS * c;
        double dis = Math.round(d);
        return dis <= radius;
    }
}