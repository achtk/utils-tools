package com.chua.utils.tools.spring.request;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.NumberHelper;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * HttpServletRequest 处理类
 *
 * @author CH
 */
public class RequestHelper {

    /**
     * 获取 ServletRequestAttributes
     * @return
     */
    public static ServletRequestAttributes getServletRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * 获取 HttpServletRequest
     * @return
     */
    public static HttpServletRequest getHttpServletRequest() {
        return getServletRequestAttributes().getRequest();
    }
    /**
     * 获取客户端请求的路径名，如：/object/delObject
     * @return String
     */
    public static String getServletPath(HttpServletRequest request) {
        try {
            return request.getServletPath();
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 获取服务器地址，如：localhost
     * @return String
     */
    public static String getServerName(HttpServletRequest request) {
        try {
            return request.getServerName();
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 获取服务器端口，如8080
     * @return String
     */
    public static String getServerPort(HttpServletRequest request) {
        try {
            return request.getServerPort()+"";
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取request参数
     * @param request
     * @param key 索引
     * @return
     */
    public static String getParameterString(HttpServletRequest request, String key) {
        return getParameterString(request, key, "");
    }
    /**
     * 获取request参数
     * @param request
     * @param key 索引
     * @return
     */
    public static String getParameterString(HttpServletRequest request, String key, String defaultValue) {
        if(isNull(request)) {
            return defaultValue;
        }
        String parameter = request.getParameter(key);
        return null == parameter ? defaultValue : parameter;
    }
    /**
     * 获取request参数
     * @param request
     * @param key 索引
     * @return
     */
    public static boolean getParameterBoolean(HttpServletRequest request, String key) {
        return getParameterBoolean(request, key, false);
    }
    /**
     * 获取request参数
     * @param request
     * @param key 索引
     * @return
     */
    public static boolean getParameterBoolean(HttpServletRequest request, String key, boolean defaultValue) {
        if(isNull(request)) {
            return defaultValue;
        }
        String string = getParameterString(request, key);
        return null == string ? defaultValue : BooleanHelper.toBoolean(string);
    }
    /**
     * 获取request参数
     * @param request
     * @param key 索引
     * @return
     */
    public static int getParameterInteger(HttpServletRequest request, String key) {
        return getParameterInteger(request, key, -1);
    }
    /**
     * 获取request参数
     * @param request
     * @param key 索引
     * @return
     */
    public static int getParameterInteger(HttpServletRequest request, String key, int defaultValue) {
        if(isNull(request)) {
            return defaultValue;
        }
        return NumberHelper.toInt(getParameterString(request, key), defaultValue);
    }
    /**
     * 获取request参数
     * @param request
     * @param key 索引
     * @return
     */
    public static long getParameterLong(HttpServletRequest request, String key) {
        return getParameterLong(request, key, -1L);
    }
    /**
     * 获取request参数
     * @param request
     * @param key 索引
     * @return
     */
    public static long getParameterLong(HttpServletRequest request, String key, long defaultValue) {
        if(isNull(request)) {
            return defaultValue;
        }
        return NumberHelper.toLong(getParameterString(request, key), defaultValue);
    }
    /**
     * 获取request参数
     * @param request
     * @param key 索引
     * @return
     */
    public static float getParameterFloat(HttpServletRequest request, String key) {
        return getParameterFloat(request, key, -1f);
    }
    /**
     * 获取request参数
     * @param request
     * @param key 索引
     * @return
     */
    public static float getParameterFloat(HttpServletRequest request, String key, float defaultValue) {
        if(isNull(request)) {
            return defaultValue;
        }
        return NumberHelper.toFloat(getParameterString(request, key), defaultValue);
    }
    /**
     * 获取request参数
     * @param request
     * @param key 索引
     * @return
     */
    public static double getParameterDouble(HttpServletRequest request, String key) {
        return getParameterDouble(request, key, -1d);
    }
    /**
     * 获取request参数
     * @param request
     * @param key 索引
     * @return
     */
    public static double getParameterDouble(HttpServletRequest request, String key, double defaultValue) {
        if(isNull(request)) {
            return defaultValue;
        }
        return NumberHelper.toDouble(getParameterString(request, key), defaultValue);
    }
    /**
     * 请求是否为空
     *
     * @return
     */
    private static boolean isNull(HttpServletRequest request) {
        return null == request;
    }
}
