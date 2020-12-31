package com.chua.utils.tools.util;

import com.chua.utils.tools.common.DateHelper;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * 日期工具类<br />
 * 部分工具类来自Apache
 * 包含Date、LocalDate、LocalDateTime、LocalTime、Instant、ZonedDateTime、YearMonth、Timestamp和long等互相转换<br>
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
    public static Date parseDate(String str, final String parsePattern) throws ParseException {
        return parseDateWithLeniency(str, new String[]{parsePattern}, true);
    }

    /**
     * 时间戳epochMilli毫秒转Date
     *
     * @param epochMilli 时间戳
     * @return Date
     */
    public static Date parseDate(final Long epochMilli) {
        if (null == epochMilli) {
            return null;
        }
        int length = epochMilli.toString().length();

        long newLongValue = epochMilli;
        if (length < MILLISECOND) {
            newLongValue *= ((Double) Math.pow(10D, MILLISECOND - length)).longValue();
        }

        return new Date(newLongValue);
    }

    /**
     * YearMonth转Date
     * 注意dayOfMonth范围：1到31之间，最大值根据月份确定特殊情况，如2月闰年29，非闰年28
     * 如果要转换为当月最后一天，可以使用下面方法：toDateEndOfMonth(YearMonth)
     *
     * @param yearMonth YearMonth
     * @return Date
     */
    public static Date parserDate(YearMonth yearMonth) {
        Objects.requireNonNull(yearMonth, "yearMonth");
        return toDate(yearMonth.atDay(1));
    }

    /**
     * YearMonth转Date
     * 注意dayOfMonth范围：1到31之间，最大值根据月份确定特殊情况，如2月闰年29，非闰年28
     * 如果要转换为当月最后一天，可以使用下面方法：toDateEndOfMonth(YearMonth)
     *
     * @param yearMonth  YearMonth
     * @param dayOfMonth 天
     * @return Date
     */
    public static Date parserDate(YearMonth yearMonth, int dayOfMonth) {
        Objects.requireNonNull(yearMonth, "yearMonth");
        return toDate(yearMonth.atDay(dayOfMonth));
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

    /**
     * LocalDate -> Date
     *
     * @param localDate localDate
     * @return Date
     */
    public static Date parseDate(final LocalDate localDate) {
        return parseDate(localDate, null);
    }

    /**
     * LocalDate -> Date
     *
     * @param localDate localDate
     * @param zone      zone
     * @return Date
     */
    public static Date parseDate(final LocalDate localDate, final ZoneId zone) {
        if (null == localDate) {
            return null;
        }

        Instant instant = localDate.atStartOfDay(Optional.ofNullable(zone).orElse(ZoneId.systemDefault())).toInstant();
        return Date.from(instant);
    }

    /**
     * Instant -> Date
     *
     * @param instant instant
     * @return Date
     */
    public static Date parseDate(final Instant instant) {
        return Date.from(Optional.ofNullable(instant).orElse(Instant.now()));
    }

    /**
     * LocalDate and LocalTime -> Date
     *
     * @param localDate localDate
     * @param localTime localTime
     * @param zone      zone
     * @return Date
     */
    public static Date parseDate(final LocalDate localDate, final LocalTime localTime, final ZoneId zone) {
        if (null == localDate || null == localTime) {
            return null;
        }
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return parseDate(localDateTime, zone);
    }

    /**
     * ZonedDateTime转Date
     * 注意时间对应的时区和默认时区差异
     *
     * @param zonedDateTime ZonedDateTime
     * @return Date
     */
    public static Date parserDate(ZonedDateTime zonedDateTime) {
        Objects.requireNonNull(zonedDateTime, "zonedDateTime");
        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * LocalDateTime -> Date
     *
     * @param localDateTime localTime
     * @return Date
     */
    public static Date parseDate(final LocalDateTime localDateTime) {
        return parseDate(localDateTime, null);
    }

    /**
     * LocalDateTime -> Date
     *
     * @param localDateTime localTime
     * @param zone          zone
     * @return Date
     */
    public static Date parseDate(final LocalDateTime localDateTime, final ZoneId zone) {
        if (null == localDateTime) {
            return null;
        }

        Instant instant = localDateTime.atZone(Optional.ofNullable(zone).orElse(ZoneId.systemDefault())).toInstant();
        return Date.from(instant);
    }

    /**
     * Date -> LocalDateTime
     *
     * @param date date
     * @param zone zone
     * @return LocalDateTime
     */
    public static LocalTime toLocalTime(final Date date, final ZoneId zone) {
        return toLocalDateTime(date, zone).toLocalTime();
    }

    /**
     * Date转LocalTime
     *
     * @param date Date
     * @return LocalTime
     */
    public static LocalTime toLocalTime(Date date) {
        return toLocalDateTime(date).toLocalTime();
    }

    /**
     * LocalDateTime转LocalTime
     *
     * @param localDateTime LocalDateTime
     * @return LocalTime
     */
    public static LocalTime toLocalTime(LocalDateTime localDateTime) {
        Objects.requireNonNull(localDateTime, "localDateTime");
        return localDateTime.toLocalTime();
    }

    /**
     * Instant转LocalTime
     *
     * @param instant Instant
     * @return LocalTime
     */
    public static LocalTime toLocalTime(Instant instant) {
        return toLocalDateTime(instant).toLocalTime();
    }

    /**
     * temporal转LocalTime
     *
     * @param temporal TemporalAccessor
     * @return LocalTime
     */
    public static LocalTime toLocalTime(TemporalAccessor temporal) {
        return LocalTime.from(temporal);
    }

    /**
     * ZonedDateTime转LocalTime
     * 注意时间对应的时区和默认时区差异
     *
     * @param zonedDateTime ZonedDateTime
     * @return LocalTime
     */
    public static LocalTime toLocalTime(ZonedDateTime zonedDateTime) {
        Objects.requireNonNull(zonedDateTime, "zonedDateTime");
        return zonedDateTime.toLocalTime();
    }

    /**
     * Date转Instant
     *
     * @param date Date
     * @return Instant
     */
    public static Instant toInstant(Date date) {
        Objects.requireNonNull(date, "date");
        return date.toInstant();
    }

    /**
     * Timestamp转Instant
     *
     * @param timestamp Timestamp
     * @return Instant
     */
    public static Instant toInstant(Timestamp timestamp) {
        Objects.requireNonNull(timestamp, "timestamp");
        return timestamp.toInstant();
    }

    /**
     * LocalDateTime转Instant
     *
     * @param localDateTime LocalDateTime
     * @return Instant
     */
    public static Instant toInstant(LocalDateTime localDateTime) {
        Objects.requireNonNull(localDateTime, "localDateTime");
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    /**
     * LocalDate转Instant
     *
     * @param localDate LocalDate
     * @return Instant
     */
    public static Instant toInstant(LocalDate localDate) {
        return toLocalDateTime(localDate).atZone(ZoneId.systemDefault()).toInstant();
    }

    /**
     * LocalTime转Instant
     * 以当天的日期+LocalTime组成新的LocalDateTime转换为Instant
     *
     * @param localTime LocalTime
     * @return Instant
     */
    public static Instant toInstant(LocalTime localTime) {
        return toLocalDateTime(localTime).atZone(ZoneId.systemDefault()).toInstant();
    }

    /**
     * 时间戳epochMilli毫秒转Instant
     *
     * @param epochMilli 时间戳
     * @return Instant
     */
    public static Instant toInstant(long epochMilli) {
        Objects.requireNonNull(epochMilli, "epochMilli");
        return Instant.ofEpochMilli(epochMilli);
    }

    /**
     * temporal转Instant
     *
     * @param temporal TemporalAccessor
     * @return Instant
     */
    public static Instant toInstant(TemporalAccessor temporal) {
        return Instant.from(temporal);
    }

    /**
     * ZonedDateTime转Instant
     * 注意，zonedDateTime时区必须和当前系统时区一致，不然会出现问题
     *
     * @param zonedDateTime ZonedDateTime
     * @return Instant
     */
    public static Instant toInstant(ZonedDateTime zonedDateTime) {
        Objects.requireNonNull(zonedDateTime, "zonedDateTime");
        return zonedDateTime.toInstant();
    }

    /**
     * Date -> LocalDateTime
     *
     * @param date date
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(final Date date) {
        return toLocalDateTime(date, null);
    }

    /**
     * Timestamp转LocalDateTime
     *
     * @param timestamp Timestamp
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        Objects.requireNonNull(timestamp, "timestamp");
        return timestamp.toLocalDateTime();
    }

    /**
     * LocalDate转LocalDateTime
     *
     * @param localDate LocalDate
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(LocalDate localDate) {
        Objects.requireNonNull(localDate, "localDate");
        return localDate.atStartOfDay();
    }

    /**
     * LocalTime转LocalDateTime
     * 以当天的日期+LocalTime组成新的LocalDateTime
     *
     * @param localTime LocalTime
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(LocalTime localTime) {
        Objects.requireNonNull(localTime, "localTime");
        return LocalDate.now().atTime(localTime);
    }

    /**
     * Instant转LocalDateTime
     *
     * @param instant Instant
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * 时间戳epochMilli毫秒转LocalDateTime
     *
     * @param epochMilli 时间戳
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(long epochMilli) {
        Objects.requireNonNull(epochMilli, "epochMilli");
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
    }

    /**
     * temporal转LocalDateTime
     *
     * @param temporal TemporalAccessor
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(TemporalAccessor temporal) {
        return LocalDateTime.from(temporal);
    }


    /**
     * ZonedDateTime转LocalDateTime
     * 注意时间对应的时区和默认时区差异
     *
     * @param zonedDateTime ZonedDateTime
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(ZonedDateTime zonedDateTime) {
        Objects.requireNonNull(zonedDateTime, "zonedDateTime");
        return zonedDateTime.toLocalDateTime();
    }

    /**
     * Date -> LocalDateTime
     *
     * @param date date
     * @param zone zone
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(final Date date, final ZoneId zone) {
        if (null == date) {
            return null;
        }
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, Optional.ofNullable(zone).orElse(ZoneId.systemDefault()));
    }

    /**
     * Date转LocalDate
     *
     * @param date Date
     * @return LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        return toLocalDateTime(date).toLocalDate();
    }

    /**
     * LocalDateTime转LocalDate
     *
     * @param localDateTime LocalDateTime
     * @return LocalDate
     */
    public static LocalDate toLocalDate(LocalDateTime localDateTime) {
        Objects.requireNonNull(localDateTime, "localDateTime");
        return localDateTime.toLocalDate();
    }

    /**
     * Instant转LocalDate
     *
     * @param instant Instant
     * @return LocalDate
     */
    public static LocalDate toLocalDate(Instant instant) {
        return toLocalDateTime(instant).toLocalDate();
    }

    /**
     * 时间戳epochMilli毫秒转LocalDate
     *
     * @param epochMilli 时间戳
     * @return LocalDate
     */
    public static LocalDate toLocalDate(long epochMilli) {
        Objects.requireNonNull(epochMilli, "epochMilli");
        return toLocalDateTime(epochMilli).toLocalDate();
    }

    /**
     * temporal转LocalDate
     *
     * @param temporal TemporalAccessor
     * @return LocalDate
     */
    public static LocalDate toLocalDate(TemporalAccessor temporal) {
        return LocalDate.from(temporal);
    }

    /**
     * ZonedDateTime转LocalDate
     * 注意时间对应的时区和默认时区差异
     *
     * @param zonedDateTime ZonedDateTime
     * @return LocalDate
     */
    public static LocalDate toLocalDate(ZonedDateTime zonedDateTime) {
        Objects.requireNonNull(zonedDateTime, "zonedDateTime");
        return zonedDateTime.toLocalDate();
    }

    /**
     * YearMonth转LocalDate
     * 注意dayOfMonth范围：1到31之间，最大值根据月份确定特殊情况，如2月闰年29，非闰年28
     * 如果要转换为当月最后一天，可以使用下面方法：toLocalDateEndOfMonth(YearMonth)
     *
     * @param yearMonth  YearMonth
     * @param dayOfMonth 天
     * @return LocalDate
     */
    public static LocalDate toLocalDate(YearMonth yearMonth, int dayOfMonth) {
        Objects.requireNonNull(yearMonth, "yearMonth");
        return yearMonth.atDay(dayOfMonth);
    }

    /**
     * YearMonth转LocalDate，转换为当月第一天
     *
     * @param yearMonth YearMonth
     * @return LocalDate
     */
    public static LocalDate toLocalDateStartOfMonth(YearMonth yearMonth) {
        return toLocalDate(yearMonth, 1);
    }

    /**
     * YearMonth转LocalDate，转换为当月最后一天
     *
     * @param yearMonth YearMonth
     * @return LocalDate
     */
    public static LocalDate toLocalDateEndOfMonth(YearMonth yearMonth) {
        Objects.requireNonNull(yearMonth, "yearMonth");
        return yearMonth.atEndOfMonth();
    }

    /**
     * Date转时间戳
     * 从1970-01-01T00:00:00Z开始的毫秒值
     *
     * @param date Date
     * @return 时间戳
     */
    public static long toEpochMilli(Date date) {
        Objects.requireNonNull(date, "date");
        return date.getTime();
    }

    /**
     * Timestamp转时间戳
     * 从1970-01-01T00:00:00Z开始的毫秒值
     *
     * @param timestamp Timestamp
     * @return 时间戳
     */
    public static long toEpochMilli(Timestamp timestamp) {
        Objects.requireNonNull(timestamp, "timestamp");
        return timestamp.getTime();
    }

    /**
     * LocalDateTime转时间戳
     * 从1970-01-01T00:00:00Z开始的毫秒值
     *
     * @param localDateTime LocalDateTime
     * @return 时间戳
     */
    public static long toEpochMilli(LocalDateTime localDateTime) {
        return toInstant(localDateTime).toEpochMilli();
    }

    /**
     * LocalDate转时间戳
     * 从1970-01-01T00:00:00Z开始的毫秒值
     *
     * @param localDate LocalDate
     * @return 时间戳
     */
    public static long toEpochMilli(LocalDate localDate) {
        return toInstant(localDate).toEpochMilli();
    }

    /**
     * Instant转时间戳
     * 从1970-01-01T00:00:00Z开始的毫秒值
     *
     * @param instant Instant
     * @return 时间戳
     */
    public static long toEpochMilli(Instant instant) {
        Objects.requireNonNull(instant, "instant");
        return instant.toEpochMilli();
    }

    /**
     * ZonedDateTime转时间戳，注意，zonedDateTime时区必须和当前系统时区一致，不然会出现问题
     * 从1970-01-01T00:00:00Z开始的毫秒值
     *
     * @param zonedDateTime ZonedDateTime
     * @return 时间戳
     */
    public static long toEpochMilli(ZonedDateTime zonedDateTime) {
        Objects.requireNonNull(zonedDateTime, "zonedDateTime");
        return zonedDateTime.toInstant().toEpochMilli();
    }

    /**
     * Date转ZonedDateTime，时区为系统默认时区
     *
     * @param date Date
     * @return ZonedDateTime
     */
    public static ZonedDateTime toZonedDateTime(Date date) {
        Objects.requireNonNull(date, "date");
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault());
    }

    /**
     * Date转ZonedDateTime
     *
     * @param date   Date
     * @param zoneId 目标时区
     * @return ZonedDateTime
     */
    public static ZonedDateTime toZonedDateTime(Date date, String zoneId) {
        Objects.requireNonNull(zoneId, "zoneId");
        return toZonedDateTime(date, ZoneId.of(zoneId));
    }

    /**
     * Date转ZonedDateTime
     *
     * @param date Date
     * @param zone 目标时区
     * @return ZonedDateTime
     */
    public static ZonedDateTime toZonedDateTime(Date date, ZoneId zone) {
        Objects.requireNonNull(date, "date");
        Objects.requireNonNull(zone, "zone");
        return Instant.ofEpochMilli(date.getTime()).atZone(zone);
    }

    /**
     * LocalDateTime转ZonedDateTime，时区为系统默认时区
     *
     * @param localDateTime LocalDateTime
     * @return ZonedDateTime
     */
    public static ZonedDateTime toZonedDateTime(LocalDateTime localDateTime) {
        Objects.requireNonNull(localDateTime, "localDateTime");
        return localDateTime.atZone(ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转ZonedDateTime，时区为zoneId对应时区
     * 注意，需要保证localDateTime和zoneId是对应的，不然会出现错误
     *
     * @param localDateTime LocalDateTime
     * @param zoneId        LocalDateTime
     * @return ZonedDateTime
     */
    public static ZonedDateTime toZonedDateTime(LocalDateTime localDateTime, String zoneId) {
        Objects.requireNonNull(localDateTime, "localDateTime");
        Objects.requireNonNull(zoneId, "zoneId");
        return localDateTime.atZone(ZoneId.of(zoneId));
    }

    /**
     * LocalDate转ZonedDateTime，时区为系统默认时区
     *
     * @param localDate LocalDate
     * @return ZonedDateTime such as 2020-02-19T00:00+08:00[Asia/Shanghai]
     */
    public static ZonedDateTime toZonedDateTime(LocalDate localDate) {
        Objects.requireNonNull(localDate, "localDate");
        return localDate.atStartOfDay().atZone(ZoneId.systemDefault());
    }

    /**
     * LocalTime转ZonedDateTime
     * 以当天的日期+LocalTime组成新的ZonedDateTime，时区为系统默认时区
     *
     * @param localTime LocalTime
     * @return ZonedDateTime
     */
    public static ZonedDateTime toZonedDateTime(LocalTime localTime) {
        Objects.requireNonNull(localTime, "localTime");
        return LocalDate.now().atTime(localTime).atZone(ZoneId.systemDefault());
    }

    /**
     * Instant转ZonedDateTime，时区为系统默认时区
     *
     * @param instant Instant
     * @return ZonedDateTime
     */
    public static ZonedDateTime toZonedDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).atZone(ZoneId.systemDefault());
    }

    /**
     * 时间戳epochMilli毫秒转ZonedDateTime，时区为系统默认时区
     *
     * @param epochMilli 时间戳
     * @return ZonedDateTime
     */
    public static ZonedDateTime toZonedDateTime(long epochMilli) {
        Objects.requireNonNull(epochMilli, "epochMilli");
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault())
                .atZone(ZoneId.systemDefault());
    }

    /**
     * temporal转ZonedDateTime，时区为系统默认时区
     *
     * @param temporal TemporalAccessor
     * @return ZonedDateTime
     */
    public static ZonedDateTime toZonedDateTime(TemporalAccessor temporal) {
        return LocalDateTime.from(temporal).atZone(ZoneId.systemDefault());
    }

    /**
     * Date转YearMonth
     *
     * @param date Date
     * @return YearMonth
     */
    public static YearMonth toYearMonth(Date date) {
        LocalDate localDate = toLocalDate(date);
        return YearMonth.of(localDate.getYear(), localDate.getMonthValue());
    }

    /**
     * LocalDateTime转YearMonth
     *
     * @param localDateTime LocalDateTime
     * @return YearMonth
     */
    public static YearMonth toYearMonth(LocalDateTime localDateTime) {
        LocalDate localDate = toLocalDate(localDateTime);
        return YearMonth.of(localDate.getYear(), localDate.getMonthValue());
    }

    /**
     * LocalDate转YearMonth
     *
     * @param localDate LocalDate
     * @return YearMonth
     */
    public static YearMonth toYearMonth(LocalDate localDate) {
        Objects.requireNonNull(localDate, "localDate");
        return YearMonth.of(localDate.getYear(), localDate.getMonthValue());
    }

    /**
     * Instant转YearMonth
     *
     * @param instant Instant
     * @return YearMonth
     */
    public static YearMonth toYearMonth(Instant instant) {
        LocalDate localDate = toLocalDate(instant);
        return YearMonth.of(localDate.getYear(), localDate.getMonthValue());
    }

    /**
     * ZonedDateTime转YearMonth
     *
     * @param zonedDateTime ZonedDateTime
     * @return YearMonth
     */
    public static YearMonth toYearMonth(ZonedDateTime zonedDateTime) {
        LocalDate localDate = toLocalDate(zonedDateTime);
        return YearMonth.of(localDate.getYear(), localDate.getMonthValue());
    }

    /**
     * Date转Timestamp
     *
     * @param date Date
     * @return Timestamp
     */
    public static Timestamp toTimestamp(Date date) {
        Objects.requireNonNull(date, "date");
        return new Timestamp(date.getTime());
    }

    /**
     * LocalDateTime转Timestamp
     *
     * @param localDateTime LocalDateTime
     * @return Timestamp
     */
    public static Timestamp toTimestamp(LocalDateTime localDateTime) {
        Objects.requireNonNull(localDateTime, "localDateTime");
        return Timestamp.valueOf(localDateTime);
    }

    /**
     * Instant转Timestamp
     *
     * @param instant Instant
     * @return Timestamp
     */
    public static Timestamp toTimestamp(Instant instant) {
        Objects.requireNonNull(instant, "instant");
        return Timestamp.from(instant);
    }

    /**
     * 时间戳epochMilli转Timestamp
     *
     * @param epochMilli 时间戳
     * @return Timestamp
     */
    public static Timestamp toTimestamp(long epochMilli) {
        return new Timestamp(epochMilli);
    }
}
