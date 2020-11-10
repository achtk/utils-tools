package com.chua.utils.tools.example;

import com.chua.utils.tools.common.DateHelper;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public class DateExample {

    public static void main(String[] args) {
        System.out.println(DateHelper.getCurrentDate());
        System.out.println(DateHelper.getBeforeDate(30));
    }
}
