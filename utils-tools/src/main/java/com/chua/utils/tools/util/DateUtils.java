package com.chua.utils.tools.util;

import com.chua.utils.tools.common.DateHelper;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类<br />
 * 部分工具类来自Apache
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/21
 */
public class DateUtils extends DateHelper {
    /**
     * unix 时间长度
     */
    private static final int UNIX_LENGTH = 10;
    /**
     * 毫秒长度
     */
    private static final int MILLISECOND = 13;


    private final static String[] DATE_FORMATS = {
            "EEE, d MMM yyyy HH:mm:ss z",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd HH:mm:ss.SSSZ",
            "yyyy-MM-dd HH:mm:ssZ",
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd HH:mm:ss"};

    /**
     * <p>通过尝试各种不同的解析器来解析表示日期的字符串。</ p>
     * <p>解析将依次尝试每种解析模式。
     * 仅当解析了整个输入字符串时，解析才被认为是成功的。
     * 如果没有任何匹配的解析模式，则抛出ParseException。</ p>
     * 解析器将对解析日期宽容。
     *
     * @param str           解析日期，不为null
     * @param parsePatterns 要使用的日期格式模式，请参见SimpleDateFormat，不为null
     * @return 日期
     * @throws IllegalArgumentException 如果日期字符串或模式数组为null
     * @throws ParseException           如果没有合适的日期模式（或没有合适的日期）
     */
    public static Date parseDate(String str, String[] parsePatterns) throws ParseException {
        return parseDateWithLeniency(str, parsePatterns, true);
    }

    /**
     * <p>通过尝试各种不同的解析器来解析表示日期的字符串。</ p>
     * <p>解析将依次尝试每种解析模式。
     * 仅当解析了整个输入字符串时，解析才被认为是成功的。
     * 如果没有任何匹配的解析模式，则抛出ParseException。</ p>
     * 解析器将对解析日期宽容。
     *
     * @param str 解析日期，不为null
     * @return 日期
     * @throws IllegalArgumentException 如果日期字符串或模式数组为null
     * @throws ParseException           如果没有合适的日期模式（或没有合适的日期）
     */
    public static Date parseDate(String str) throws ParseException {
        return parseDateWithLeniency(str, DATE_FORMATS, true);
    }

    /**
     * long 转 Date
     *
     * @param longValue 待处理的数据
     * @return Date
     * @see Date
     */
    public static Date parseDate(final Long longValue) {
        if (null == longValue) {
            return null;
        }
        int length = longValue.toString().length();

        long newLongValue = longValue;
        if (length < MILLISECOND) {
            newLongValue *= ((Double) Math.pow(10D, MILLISECOND - length)).longValue();
        }

        return new Date(newLongValue);
    }

    /**
     * <p>通过尝试各种不同的解析器来解析表示日期的字符串。</ p>
     * <p>解析将依次尝试每种解析模式。
     * 仅当解析了整个输入字符串时，解析才被认为是成功的。
     * 如果没有解析模式匹配，则抛出ParseException。</ p>
     *
     * @param str           解析日期，不为null
     * @param parsePatterns 要使用的日期格式模式，请参见SimpleDateFormat，不为null
     * @param lenient       指定日期/时间解析。
     * @return Date
     * @throws IllegalArgumentException 如果日期字符串或模式数组为null
     * @throws ParseException           如果没有合适的日期模式
     * @see java.util.Calendar
     */
    public static Date parseDateWithLeniency(String str, String[] parsePatterns, boolean lenient) throws ParseException {
        if (str == null || parsePatterns == null) {
            throw new IllegalArgumentException("Date and Patterns must not be null");
        }

        SimpleDateFormat parser = new SimpleDateFormat();
        parser.setLenient(lenient);
        ParsePosition pos = new ParsePosition(0);
        for (String parsePattern : parsePatterns) {

            String pattern = parsePattern;

            // LANG-530 - need to make sure 'ZZ' output doesn't get passed to SimpleDateFormat
            if (parsePattern.endsWith("ZZ")) {
                pattern = pattern.substring(0, pattern.length() - 1);
            }

            parser.applyPattern(pattern);
            pos.setIndex(0);

            String str2 = str;
            // LANG-530 - need to make sure 'ZZ' output doesn't hit SimpleDateFormat as it will ParseException
            if (parsePattern.endsWith("ZZ")) {
                int signIdx = indexOfSignChars(str2, 0);
                while (signIdx >= 0) {
                    str2 = reformatTimezone(str2, signIdx);
                    signIdx = indexOfSignChars(str2, ++signIdx);
                }
            }

            Date date = parser.parse(str2, pos);
            if (date != null && pos.getIndex() == str2.length()) {
                return date;
            }
        }
        throw new ParseException("Unable to parse the date: " + str, -1);
    }

    /**
     * Index of sign charaters (i.e. '+' or '-').
     *
     * @param str      要搜索的字符串
     * @param startPos 开始位置
     * @return 第一个符号字符的索引；如果找不到，则返回-1
     */
    private static int indexOfSignChars(String str, int startPos) {
        int idx = StringUtils.indexOf(str, '+', startPos);
        if (idx < 0) {
            idx = StringUtils.indexOf(str, '-', startPos);
        }
        return idx;
    }

    /**
     * 在日期字符串中重新格式化时区。
     *
     * @param str     输入字符串
     * @param signIdx 符号字符的索引位置
     * @return 重新格式化的字符串
     */
    private static String reformatTimezone(String str, int signIdx) {
        String str2 = str;
        if (signIdx >= 0 &&
                signIdx + 5 < str.length() &&
                Character.isDigit(str.charAt(signIdx + 1)) &&
                Character.isDigit(str.charAt(signIdx + 2)) &&
                str.charAt(signIdx + 3) == ':' &&
                Character.isDigit(str.charAt(signIdx + 4)) &&
                Character.isDigit(str.charAt(signIdx + 5))) {
            str2 = str.substring(0, signIdx + 3) + str.substring(signIdx + 4);
        }
        return str2;
    }
}
