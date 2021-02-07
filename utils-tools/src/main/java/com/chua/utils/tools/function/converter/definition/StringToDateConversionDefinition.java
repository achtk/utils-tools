package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.common.DateHelper;
import com.chua.utils.tools.common.NumberHelper;
import com.chua.utils.tools.constant.PatternConstant;
import com.chua.utils.tools.util.DateUtils;

import javax.annotation.Nullable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * String -> date
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/4
 */
public class StringToDateConversionDefinition implements TypeConversionDefinition<String, Date> {
    private static final String DAY = "-01";
    private static final Pattern LOCALE_DATE_SIMPLE = Pattern.compile("\\d*-\\d*");
    private static final Pattern LOCALE_DATE = Pattern.compile("\\d*-\\d*-\\d*");
    private static final Pattern FORMAT_DATE1 = Pattern.compile("\\d*-\\d*-\\d*\\s+\\d*");
    private static final Pattern FORMAT_DATE2 = Pattern.compile("\\d*-\\d*-\\d*\\s+\\d*:\\d*");
    private static final Pattern FORMAT_DATE3 = Pattern.compile("\\d*-\\d*-\\d*\\s+\\d*:\\d*:\\d*");

    private static final ThreadLocal<SimpleDateFormat> FORMAT_DATE_SDF1 = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH"));
    private static final ThreadLocal<SimpleDateFormat> FORMAT_DATE_SDF2 = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm"));
    private static final ThreadLocal<SimpleDateFormat> FORMAT_DATE_SDF3 = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));


    @Nullable
    @Override
    public Date convert(String source) {
        try {
            if (PatternConstant.NUMBER_PATTERN.matcher(source).find()) {
                long aLong = NumberHelper.toLong(source);
                return new Date(aLong);
            }


            try {
                return DateUtils.parseDate(source);
            } catch (ParseException ignore) {
            }
            if (FORMAT_DATE3.matcher(source).find()) {
                return FORMAT_DATE_SDF3.get().parse(source);
            }

            if (FORMAT_DATE2.matcher(source).find()) {
                return FORMAT_DATE_SDF2.get().parse(source);
            }

            if (FORMAT_DATE1.matcher(source).find()) {
                return FORMAT_DATE_SDF1.get().parse(source);
            }

            if (LOCALE_DATE.matcher(source).find()) {
                return DateHelper.toDate(LocalDate.parse(source));
            }

            if (LOCALE_DATE_SIMPLE.matcher(source).find()) {
                return DateHelper.toDate(LocalDate.parse(source + DAY));
            }
            return DateHelper.toDate(LocalDate.parse(source));
        } catch (Exception ignore) {
        }
        return null;
    }
}
