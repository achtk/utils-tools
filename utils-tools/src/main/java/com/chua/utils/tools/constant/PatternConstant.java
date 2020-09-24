package com.chua.utils.tools.constant;

import java.util.regex.Pattern;

/**
 * 正则
 * @author CH
 */
public class PatternConstant {
    /**
     * 空值
     */
    public static final Pattern PATTERN_EMPTY = Pattern.compile("\\s+");
    public static final Pattern PATTERN_BLANK = Pattern.compile("\\s*|\t|\r|\n");
    public static Pattern NUMERIC_PATTERN = Pattern.compile("^[0-9\\-]+$");
    public static Pattern NUMERIC_STRING_PATTERN = Pattern.compile("^[0-9\\-\\-]+$");
    public static Pattern FLOAT_NUMERIC_PATTERN = Pattern.compile("^[0-9\\-\\.]+$");
    public static Pattern ABC_PATTERN = Pattern.compile("^[a-z|A-Z]+$");
}
