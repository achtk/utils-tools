package com.chua.utils.tools.util;

import com.chua.utils.tools.entity.CronExpression;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Cron表达式工具类
 * <p>目前支持三种常用的cron表达式 </p>
 * <p>1.每天的某个时间点执行 例:12 12 12 * * ? 表示每天12时12分12秒执行 </p>
 * <p>2.每周的哪几天执行 例:12 12 12 ? * 1,2,3 表示每周的周1周2周3,12时12分12秒执行</p>
 * <p>3.每月的哪几天执行 例:12 12 12 1,21,13 * ?表示每月的1号21号13号 12时12分12秒执行</p>
 * cron工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/25
 */
public class CronUtils {

    /**
     * 验证Cron表达式
     *
     * @param cronExpression Cron表达式
     * @return boolean
     */
    public static boolean isValidExpression(String cronExpression) {
        return CronExpression.isValidExpression(cronExpression);
    }

    /**
     * 格式化Cron表达式
     *
     * @param cronExpression Cron表达式
     * @return String
     */
    public static String formatExpression(String cronExpression) {
        try {
            return new CronExpression(cronExpression).toString();
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 生成下一个执行时间
     *
     * @param cronExpression Cron表达式
     * @param date           日期
     * @return Date
     */
    public static Date getNextTime(String cronExpression, Date date) {
        try {
            if (date != null) {
                return new CronExpression(cronExpression).getNextValidTimeAfter(date);
            } else {
                return new CronExpression(cronExpression).getNextValidTimeAfter(new Date());
            }
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 生成下一个执行时间
     *
     * @param cronExpression Cron表达式
     * @return Date
     */
    public static Date getNextTime(String cronExpression) {
        return getNextTime(cronExpression, null);
    }

    /**
     * 生成下一个执行时间的日期格式化
     *
     * @param cronExpression Cron表达式
     * @param date           日期
     * @return String 返回格式化时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getNextTimeStr(String cronExpression, Date date) {
        return DateUtils.format(getNextTime(cronExpression, date));
    }

    /**
     * 生成下一个执行时间的日期格式化
     *
     * @param cronExpression Cron表达式
     * @return String 返回格式化时间 yyyy-MM-dd HH:mm:ss
     */
    public static String getNextTimeStr(String cronExpression) {
        return getNextTimeStr(cronExpression, null);
    }

    /**
     * 生成多个执行时间
     *
     * @param cronExpression Cron表达式
     * @param date           日期
     * @param num            数量
     * @return 时间列表
     */
    public static List<Date> getNextTimeList(String cronExpression, Date date, int num) {
        List<Date> dateList = new ArrayList<>();
        if (num < 1) {
            throw new RuntimeException("num must be greater than 0");
        }
        Date startDate = date != null ? date : new Date();
        for (int i = 0; i < num; i++) {
            startDate = getNextTime(cronExpression, startDate);
            if (startDate != null) {
                dateList.add(startDate);
            }
        }
        return dateList;
    }

    /**
     * 生成多个执行时间
     *
     * @param cronExpression Cron表达式
     * @param num            数量
     * @return 时间列表
     */
    public static List<Date> getNextTimeList(String cronExpression, int num) {
        return getNextTimeList(cronExpression, null, num);
    }

    /**
     * 生成多个执行时间的日期格式化
     *
     * @param cronExpression Cron表达式
     * @param date           日期
     * @param num            数量
     * @return 返回格式化时间列表 yyyy-MM-dd HH:mm:ss
     */
    public static List<String> getNextTimeStrList(String cronExpression, Date date, int num) {
        List<String> dateStrList = new ArrayList<>();
        List<Date> dateList = getNextTimeList(cronExpression, date, num);
        if (!BooleanUtils.isEmpty(dateList)) {
            dateList.stream().forEach(d -> {
                String dateStr = DateUtils.format(d);
                dateStrList.add(dateStr);
            });
        }
        return dateStrList;
    }

    /**
     * 生成多个执行时间的日期格式化
     *
     * @param cronExpression Cron表达式
     * @param num            数量
     * @return 返回格式化时间列表 yyyy-MM-dd HH:mm:ss
     */
    public static List<String> getNextTimeStrList(String cronExpression, int num) {
        return getNextTimeStrList(cronExpression, null, num);
    }

    /**
     * 对比Cron表达式下一个执行时间是否与指定date相等
     *
     * @param cronExpression Cron表达式
     * @param date           日期
     * @return boolean
     */
    public static boolean isSatisfiedBy(String cronExpression, Date date) {
        try {
            return new CronExpression(cronExpression).isSatisfiedBy(date);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
