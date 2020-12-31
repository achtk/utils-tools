package com.chua.tools.example;

import com.chua.utils.tools.entity.CronExpression;
import com.chua.utils.tools.util.DateUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * Cron工具测试
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/31
 */
public class CronUtilsExample extends BaseExample {
    public static void main(String[] args) throws ParseException {
        CronExpression cronExpression = new CronExpression("0 1 * * * ?");
        Date date = new Date();
        for (int i = 0; i < 10; i++) {
            date = cronExpression.getNextValidTimeAfter(date);
            log.info("表达式第{}次下一次执行时间: {}", i, DateUtils.format(date));
        }
    }
}
