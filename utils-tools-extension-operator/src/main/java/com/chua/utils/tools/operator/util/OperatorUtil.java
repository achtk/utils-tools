package com.chua.utils.tools.operator.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/2
 */
public class OperatorUtil {
    private static final Pattern HUMP_PATTERN = Pattern.compile("[A-Z]");
    private static final String UNDERLINE = "_";

    /**
     * 驼峰转下划线
     *
     * @param source 数据
     * @return 数据
     */
    public static String humpToUnderline(final String source) {
        if (null == source) {
            return null;
        }
        Matcher matcher = HUMP_PATTERN.matcher(source);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, UNDERLINE + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 获取字段类型
     *
     * @param type 类型
     * @return 字段类型
     */
    public static String getColumnType(Class<?> type) {
        if (Integer.class.isAssignableFrom(type)) {
            return "INTEGER";
        } else if (Double.class.isAssignableFrom(type)) {
            return "DOUBLE";
        } else if (Date.class.isAssignableFrom(type)) {
            return "DATE";
        } else if (BigDecimal.class.isAssignableFrom(type)) {
            return "DECIMAL";
        } else if (Float.class.isAssignableFrom(type)) {
            return "FLOAT";
        } else if (BigInteger.class.isAssignableFrom(type)) {
            return "BIGINT";
        } else if (Boolean.class.isAssignableFrom(type)) {
            return "BIT";
        } else if (byte[].class.isAssignableFrom(type)) {
            return "BLOB";
        } else if (Time.class.isAssignableFrom(type)) {
            return "TIME";
        } else if (Timestamp.class.isAssignableFrom(type)) {
            return "DATETIME";
        }
        return "VARCHAR";
    }
}
