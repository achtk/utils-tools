package com.chua.utils.tools.example;

import com.chua.utils.tools.common.DateHelper;

import java.util.Date;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public class DateExample {

    public static void main(String[] args) {
        System.out.println("获取当前时间:" + DateHelper.getCurrentDate());
        System.out.println("获取前2天:" + DateHelper.getBeforeDate(2));
        System.out.println("格式化时间:" + DateHelper.format(new Date()));
    }
}
