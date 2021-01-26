package com.chua.utils.tools.function.converter;

import com.chua.utils.tools.common.DateHelper;
import com.chua.utils.tools.common.NumberHelper;
import com.chua.utils.tools.constant.PatternConstant;
import com.chua.utils.tools.util.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 时间格式转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/26
 */
public class DateTypeConverter implements TypeConverter<Date> {

    private static final String DAY = "-01";
    private static Pattern LOCALE_DATE_SIMPLE = Pattern.compile("\\d*\\-\\d*");
    private static Pattern LOCALE_DATE = Pattern.compile("\\d*\\-\\d*\\-\\d*");
    private static Pattern FORMAT_DATE1 = Pattern.compile("\\d*\\-\\d*\\-\\d*\\s+\\d*");
    private static Pattern FORMAT_DATE2 = Pattern.compile("\\d*\\-\\d*\\-\\d*\\s+\\d*\\:\\d*");
    private static Pattern FORMAT_DATE3 = Pattern.compile("\\d*\\-\\d*\\-\\d*\\s+\\d*\\:\\d*\\:\\d*");

    private static ThreadLocal<SimpleDateFormat> FORMAT_DATE_SDF1 = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH"));
    private static ThreadLocal<SimpleDateFormat> FORMAT_DATE_SDF2 = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm"));
    private static ThreadLocal<SimpleDateFormat> FORMAT_DATE_SDF3 = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    @Override
    public Date convert(Object value) {
        if (null == value) {
            return null;
        }

        if (value instanceof Date) {
            return (Date) value;
        }

        if (value instanceof LocalDate) {
            return DateUtils.toDate((LocalDate) value);
        }

        if (value instanceof Instant) {
            return DateUtils.toDate((Instant) value);
        }

        if (value instanceof ZonedDateTime) {
            return DateUtils.parserDate((ZonedDateTime) value);
        }

        if (value instanceof Long) {
            return DateUtils.parseDate((Long) value);
        }

        if (value instanceof String) {
            String newValue = value.toString();
            try {
                if (PatternConstant.NUMBER_PATTERN.matcher(newValue).find()) {
                    long aLong = NumberHelper.toLong(newValue);
                    return new Date(aLong);
                }


                try {
                    return DateUtils.parseDate(newValue);
                } catch (ParseException e) {
                }
                if (FORMAT_DATE3.matcher(newValue).find()) {
                    return FORMAT_DATE_SDF3.get().parse(newValue);
                }

                if (FORMAT_DATE2.matcher(newValue).find()) {
                    return FORMAT_DATE_SDF2.get().parse(newValue);
                }

                if (FORMAT_DATE1.matcher(newValue).find()) {
                    return FORMAT_DATE_SDF1.get().parse(newValue);
                }

                if (LOCALE_DATE.matcher(newValue).find()) {
                    return DateHelper.toDate(LocalDate.parse(value.toString()));
                }

                if (LOCALE_DATE_SIMPLE.matcher(newValue).find()) {
                    return DateHelper.toDate(LocalDate.parse(value.toString() + DAY));
                }
                return DateHelper.toDate(LocalDate.parse(value.toString()));
            } catch (Exception e) {
            }
        }
        return null;
    }

    @Override
    public Class<Date> getType() {
        return Date.class;
    }
}
