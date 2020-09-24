package com.chua.utils.tools.common;

import com.google.common.base.Strings;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期工具类
 *
 * @author CHTK
 */
public class DateHelper {

    public static final int ACCURACY_HOURS = 4;

    public static final int ACCURACY_MINUTES = 5;

    public static final int ACCURACY_SECONDS = 6;

    public static final int ACCURACY_MILLISECONDS = 7;

    public static final int ACCURACY_MILLISECONDS_FORCED = 8;

    /**
     * 每秒毫秒数
     */
    public static final int    MILLISECONDS_PER_SECONDE = 1000;
    /**
     * 每分毫秒数 60*1000
     */
    public static final int    MILLISECONDS_PER_MINUTE  = 60000;
    /**
     * 每小时毫秒数 36*60*1000
     */
    public static final int    MILLISECONDS_PER_HOUR    = 3600000;
    /**
     * 每天毫秒数 24*60*60*1000;
     */
    public static final long   MILLISECONDS_PER_DAY     = 86400000;

    public static final String YYYY         = "yyyy";
    public static final String YYYY_MM         = "yyyy-MM";
    public static final String YYYY_MM_DD         = "yyyy-MM-dd";
    public static final String YYYY_MM_DDHH         = "yyyy-MM-dd HH";
    public static final String YYYY_MM_DDHHMM         = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_TIME         = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_MILLS_TIME   = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * 普通时间的格式
     */
    public static final String YYYY_MM_DDHHMMSS         = DATE_FORMAT_TIME;

    /**
     * 普通时间的格式
     */
    public static final String YYYY_MM_DDHHMMSSSSS         = DATE_FORMAT_MILLS_TIME;
    /**
     * 格式化时间
     * <p>
     *     DateHelper.format(new Date()) =
     * </p>
     * @param str 时间
     * @return
     */
    public static Date format(String str, String pattern) throws ParseException {
        DateFormat df = new SimpleDateFormat(pattern);
        return df.parse(str);
    }
    /**
     * 格式化时间
     * <p>
     *     DateHelper.format(new Date()) =
     * </p>
     * @param date 时间
     * @return
     */
    public static String format(Date date) {
        return format(date, DATE_FORMAT_TIME);
    }
    /**
     * 格式化时间
     * <p>
     *     DateHelper.format(new Date(), "yyyy-MM-dd") =
     * </p>
     * @param date 时间
     * @param pattern 表达式
     * @return
     */
    public static String format(Date date, String pattern) {
        DateFormat df = createDefaultDateFormat(pattern);
        return df.format(date);
    }
    /**
     * 格式化时间
     * @param date 时间
     * @param dateFormat 时间格式化
     * @return
     */
    public static String format(Date date, DateFormat dateFormat) {
        DateFormat df = null == dateFormat ? createDefaultDateFormat(YYYY_MM_DDHHMMSS) : dateFormat;
        return df.format(date);
    }
    /**
     * 格式化时间
     * <p>
     *     DateHelper.format(1111) =
     * </p>
     * @param time 时间
     * @return
     */
    public static String format(long time) {
        return format(time, DATE_FORMAT_TIME);
    }
    /**
     * 格式化时间
     * <p>
     *     DateHelper.format(1111, "yyyy-MM-dd") =
     * </p>
     * @param time 时间
     * @param pattern 表达式
     * @return
     */
    public static String format(long time, String pattern) {
        DateFormat df = createDefaultDateFormat(pattern);
        return df.format(time);
    }
    /**
     * 格式化时间
     * @param time 时间
     * @param dateFormat 时间格式化
     * @return
     */
    public static String format(long time, DateFormat dateFormat) {
        DateFormat df = null == dateFormat ? createDefaultDateFormat(YYYY_MM_DDHHMMSS) : dateFormat;
        return df.format(time);
    }
    /**
     * 获取df
     * @param pattern
     * @return
     */
    private static DateFormat createDefaultDateFormat(String pattern) {
        return createDateFormat(pattern, null);
    }
    /**
     * 获取df
     * @param pattern
     * @return
     */
    private static DateFormat createDateFormat(String pattern, String timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        if(!Strings.isNullOrEmpty(timeZone)) {
            TimeZone gmt = TimeZone.getTimeZone(timeZone);
            sdf.setTimeZone(gmt);
        }
        sdf.setLenient(true);
        return sdf;
    }

    /**
     * 到下一分钟0秒的毫秒数
     *
     * @param rightnow 当前时间
     * @return the int 到下一分钟的毫秒数
     */
    public static int getDelayToNextMinute(long rightnow) {
        return (int) (MILLISECONDS_PER_MINUTE - (rightnow % MILLISECONDS_PER_MINUTE));
    }

    /**
     * 上一分钟的最后一毫秒
     *
     * @param rightnow 当前时间
     * @return 上一分钟的最后一毫秒
     */
    public static long getPreMinuteMills(long rightnow) {
        return rightnow - (rightnow % MILLISECONDS_PER_MINUTE) - 1;
    }

    /**
     * 得到时间字符串
     *
     * @param date 时间
     * @return 时间字符串
     */
    public static String dateToStr(Date date) {
        return dateToStr(date, DATE_FORMAT_TIME);
    }

    /**
     * 时间转字符串
     *
     * @param date   时间
     * @param format 格式化格式
     * @return 时间字符串
     */
    public static String dateToStr(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 字符串转时间
     *
     * @param dateStr 时间字符串
     * @return 时间字符串
     * @throws ParseException 解析异常
     */
    public static Date strToDate(String dateStr) throws ParseException {
        return strToDate(dateStr, DATE_FORMAT_TIME);
    }

    /**
     * 字符串转时间戳
     * @param dateStr 时间字符串
     * @return 时间戳
     * @throws ParseException 解析异常
     */
    public static Long strToLong(String dateStr) throws ParseException {
        return strToDate(dateStr).getTime();
    }

    /**
     * 字符串转时间
     *
     * @param dateStr 时间字符串
     * @param format  格式化格式
     * @return 时间字符串
     * @throws ParseException 解析异常
     */
    public static Date strToDate(String dateStr, String format) throws ParseException {
        return new SimpleDateFormat(format).parse(dateStr);
    }

    /**
     * 得到毫秒级时间字符串
     *
     * @param date 时间
     * @return 时间字符串
     */
    public static String dateToMillisStr(Date date) {
        return dateToStr(date, DATE_FORMAT_MILLS_TIME);
    }

    /**
     * 得到Date
     *
     * @param millisDateStr 毫秒级时间字符串
     * @return Date
     * @throws ParseException 解析异常
     */
    public static Date millisStrToDate(String millisDateStr) throws ParseException {
        return strToDate(millisDateStr, DATE_FORMAT_MILLS_TIME);
    }

    /**
     * 当前时间
     * @return
     */
    public static String currentString() {
        return format(current(), YYYY_MM_DDHHMMSS);
    }

    /**
     * 当前时间戳
     * @return
     */
    public static long current() {
        return System.currentTimeMillis();
    }
}
