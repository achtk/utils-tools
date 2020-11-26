package com.chua.utils.tools.example;

import org.quartz.CronExpression;

import java.text.ParseException;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/25
 */
public class CronUtilExample {


    //参考例子
    public static void main(String[] args) throws ParseException {
        CronExpression cronExpression = new CronExpression("0/1 * * * * ?");
    }
}
