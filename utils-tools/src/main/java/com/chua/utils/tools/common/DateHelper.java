package com.chua.utils.tools.common;

import com.google.common.base.Strings;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 日期工具类
 *
 * @author CH
 */
public class DateHelper {

    public static final int ACCURACY_HOURS = 4;

    public static final int ACCURACY_MINUTES = 5;

    public static final int ACCURACY_SECONDS = 6;

    public static final int ACCURACY_MILLISECONDS = 7;

    public static final int ACCURACY_MILLISECONDS_FORCED = 8;

    // 时间元素
    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String WEEK = "week";
    private static final String DAY = "day";
    private static final String HOUR = "hour";
    private static final String MINUTE = "minute";
    private static final String SECOND = "second";
    /**
     * 星期一
     */
    private static final String MONDAY = "MONDAY";
    /**
     * 星期二
     */
    private static final String TUESDAY = "TUESDAY";
    /**
     * 星期三
     */
    private static final String WEDNESDAY = "WEDNESDAY";
    /**
     * 星期四
     */
    private static final String THURSDAY = "THURSDAY";
    /**
     * 星期五
     */
    private static final String FRIDAY = "FRIDAY";
    /**
     * 星期六
     */
    private static final String SATURDAY = "SATURDAY";
    /**
     * 星期日
     */
    private static final String SUNDAY = "SUNDAY";

    /**
     * 每秒毫秒数
     */
    public static final int MILLISECONDS_PER_SECONDE = 1000;
    /**
     * 每分毫秒数 60*1000
     */
    public static final int MILLISECONDS_PER_MINUTE = 60000;
    /**
     * 每小时毫秒数 36*60*1000
     */
    public static final int MILLISECONDS_PER_HOUR = 3600000;
    /**
     * 每天毫秒数 24*60*60*1000;
     */
    public static final long MILLISECONDS_PER_DAY = 86400000;

    public static final String YYYY = "yyyy";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DDHH = "yyyy-MM-dd HH";
    public static final String YYYY_MM_DDHHMM = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_MILLS_TIME = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * 普通时间的格式
     */
    public static final String YYYY_MM_DDHHMMSS = DATE_FORMAT_TIME;
    /**
     * 普通时间的格式
     */
    public static final DateTimeFormatter YYYY_MM_DDHHMMSS_DTF = DateTimeFormatter.ofPattern(DATE_FORMAT_TIME);

    /**
     * 普通时间的格式
     */
    public static final String YYYY_MM_DDHHMMSSSSS = DATE_FORMAT_MILLS_TIME;

    /**
     * 格式化时间
     * <p>
     * DateHelper.format(new Date()) =
     * </p>
     *
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
     * DateHelper.format(new Date()) =
     * </p>
     *
     * @param date 时间
     * @return 格式化时间
     */
    public static String format(Date date) {
        return format(date, DATE_FORMAT_TIME);
    }

    /**
     * 格式化时间
     * <p>
     * DateHelper.format(new Date(), "yyyy-MM-dd") =
     * </p>
     *
     * @param date    时间
     * @param pattern 表达式
     * @return 格式化时间
     */
    public static String format(Date date, String pattern) {
        DateFormat df = createDefaultDateFormat(pattern);
        return df.format(date);
    }

    /**
     * 格式化时间
     *
     * @param date       时间
     * @param dateFormat 时间格式化
     * @return 格式化时间
     */
    public static String format(Date date, DateFormat dateFormat) {
        DateFormat df = null == dateFormat ? createDefaultDateFormat(YYYY_MM_DDHHMMSS) : dateFormat;
        return df.format(date);
    }

    /**
     * 格式化时间
     * <p>
     * DateHelper.format(1111) =
     * </p>
     *
     * @param time 时间
     * @return 格式化时间
     */
    public static String format(long time) {
        return format(time, DATE_FORMAT_TIME);
    }

    /**
     * 格式化时间
     * <p>
     * DateHelper.format(1111, "yyyy-MM-dd") =
     * </p>
     *
     * @param time    时间
     * @param pattern 表达式
     * @return 格式化时间
     */
    public static String format(long time, String pattern) {
        DateFormat df = createDefaultDateFormat(pattern);
        return df.format(time);
    }

    /**
     * 格式化时间
     *
     * @param time       时间
     * @param dateFormat 时间格式化
     * @return 格式化时间
     */
    public static String format(long time, DateFormat dateFormat) {
        DateFormat df = null == dateFormat ? createDefaultDateFormat(YYYY_MM_DDHHMMSS) : dateFormat;
        return df.format(time);
    }

    /**
     * 获取 DateFormat
     *
     * @param pattern 表达式
     * @return DateFormat
     * @see java.text.DateFormat
     */
    private static DateFormat createDefaultDateFormat(String pattern) {
        return createDateFormat(pattern, null);
    }

    /**
     * 获取 DateFormat
     *
     * @param pattern  表达式
     * @param timeZone 时区
     * @return DateFormat
     * @see java.text.DateFormat
     */
    private static DateFormat createDateFormat(String pattern, String timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        if (!Strings.isNullOrEmpty(timeZone)) {
            TimeZone gmt = TimeZone.getTimeZone(timeZone);
            sdf.setTimeZone(gmt);
        }
        sdf.setLenient(true);
        return sdf;
    }

    /**
     * 到下一分钟0秒的毫秒数
     *
     * @param rightNow 当前时间
     * @return the int 到下一分钟的毫秒数
     */
    public static int getDelayToNextMinute(long rightNow) {
        return (int) (MILLISECONDS_PER_MINUTE - (rightNow % MILLISECONDS_PER_MINUTE));
    }

    /**
     * 上一分钟的最后一毫秒
     *
     * @param rightNow 当前时间
     * @return 上一分钟的最后一毫秒
     */
    public static long getPreMinuteMills(long rightNow) {
        return rightNow - (rightNow % MILLISECONDS_PER_MINUTE) - 1;
    }

    /**
     * 当前时间 {@link #YYYY_MM_DDHHMMSS}
     *
     * @return 当前时间
     */
    public static String currentString() {
        return format(current(), YYYY_MM_DDHHMMSS);
    }

    /**
     * 当前时间戳
     *
     * @return 当前时间时间戳
     */
    public static long current() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间 {@link #currentString()}
     *
     * @return 当前时间
     */
    public static String getCurrentDate() {
        return currentString();
    }

    /**
     * 获取当前日期之后（之后）的节点事件<br>
     * <ul>
     * 比如当前时间为：2019-03-30 10:20:30
     * </ul>
     * <li>node="hour",num=5L:2019-03-30 15:20:30</li>
     * <li>node="day",num=1L:2019-03-31 10:20:30</li>
     * <li>node="year",num=1L:2020-03-30 10:20:30</li>
     *
     * @param node 节点元素（“year”,"month","week","day","huor","minute","second"）
     * @param num  第几天（+：之后，-：之前）
     * @return 之后或之后的日期
     */
    public static String getAfterOrPreNowTime(final String node, final Long num) {
        return getAfterOrPreNowTime(node, num, YYYY_MM_DDHHMMSS_DTF);
    }

    /**
     * 获取当前日期之后（之后）的节点事件<br>
     * <ul>
     * 比如当前时间为：2019-03-30 10:20:30
     * </ul>
     * <li>node="hour",num=5L:2019-03-30 15:20:30</li>
     * <li>node="day",num=1L:2019-03-31 10:20:30</li>
     * <li>node="year",num=1L:2020-03-30 10:20:30</li>
     *
     * @param node              节点元素（“year”,"month","week","day","huor","minute","second"）
     * @param num               第几天（+：之后，-：之前）
     * @param dateTimeFormatter 格式化当前时间格式{@link #YYYY_MM_DDHHMMSS_DTF}
     * @return 之后或之后的日期
     */
    public static String getAfterOrPreNowTime(String node, Long num, DateTimeFormatter dateTimeFormatter) {
        LocalDateTime now = LocalDateTime.now();
        if (HOUR.equals(node)) {
            return now.plusHours(num).format(dateTimeFormatter);
        } else if (DAY.equals(node)) {
            return now.plusDays(num).format(dateTimeFormatter);
        } else if (WEEK.equals(node)) {
            return now.plusWeeks(num).format(dateTimeFormatter);
        } else if (MONTH.equals(node)) {
            return now.plusMonths(num).format(dateTimeFormatter);
        } else if (YEAR.equals(node)) {
            return now.plusYears(num).format(dateTimeFormatter);
        } else if (MINUTE.equals(node)) {
            return now.plusMinutes(num).format(dateTimeFormatter);
        } else if (SECOND.equals(node)) {
            return now.plusSeconds(num).format(dateTimeFormatter);
        } else {
            return "Node is Error!";
        }
    }

    /**
     * 获取当前时间前{@link #getBeforeDate(int)} num}天
     *
     * @param num 天数
     * @return 获取当前时间前{num}天
     */
    public static List<Date> getBeforeDate(final int num) {
        assert num > 0 : "num不能小于1";

        LocalDate localDate = LocalDate.now();
        List<Date> dateList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            dateList.add(Date.from(localDate.minusDays(i).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
        }
        return dateList;
    }
}
