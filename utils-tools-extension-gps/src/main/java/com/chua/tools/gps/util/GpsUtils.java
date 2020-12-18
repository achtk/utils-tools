package com.chua.tools.gps.util;

import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;

/**
 * gps工具类
 * @author CH
 * @version 1.0.0
 * @since 2020/12/18
 */
public class GpsUtils {

    /**
     * 通过经纬度获取距离(单位: 米)
     *
     * @param lat1 经度1
     * @param lng1 纬度1
     * @param lat2 经度2
     * @param lng2 纬度2
     * @return double返回类型距离
     */
    public static double getDistanceMeter(final double lng1, final double lat1, final double lng2, final double lat2){
        GlobalCoordinates source = new GlobalCoordinates(lng1, lat1);
        GlobalCoordinates target = new GlobalCoordinates(lng2, lat2);
        //创建GeodeticCalculator，调用计算方法，传入坐标系、经纬度用于计算距离
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(Ellipsoid.Sphere, source, target);
        return geoCurve.getEllipsoidalDistance();
    }
}
