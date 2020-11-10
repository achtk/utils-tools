package com.chua.utils.tools.constant;

import java.util.regex.Pattern;

/**
 * 正则
 *
 * @author CH
 */
public class PatternConstant {
    /**
     * 空值
     */
    public static final Pattern PATTERN_EMPTY = Pattern.compile("\\s+");
    /**
     * 空值
     */
    public static final Pattern PATTERN_BLANK = Pattern.compile("\\s*|\t|\r|\n");
    /**
     * 整数
     */
    public static final Pattern NUMERIC_PATTERN = Pattern.compile("^[0-9\\-]+$");
    /**
     * 整数
     */
    public static final Pattern NUMERIC_STRING_PATTERN = Pattern.compile("^[0-9\\-\\-]+$");
    /**
     * 浮点
     */
    public static final Pattern FLOAT_NUMERIC_PATTERN = Pattern.compile("^[0-9\\-\\.]+$");
    /**
     * 字母
     */
    public static final Pattern ABC_PATTERN = Pattern.compile("^[a-z|A-Z]+$");
    /**
     * 中文
     */
    public static final Pattern CHINESE = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]");
    /**
     * (.*?)
     */
    public static final Pattern REGEXP_ANY = Pattern.compile("(.*?)");
    /**
     * 大写字母
     */
    public final static Pattern HUMP_PATTERN = Pattern.compile("[A-Z]");
    /**
     * 下划线
     */
    public final static Pattern LINE_PATTERN = Pattern.compile("_(\\w)");
    /**
     * 任意
     */
    public final static Pattern ALL_PATTERN = Pattern.compile("(.*?)");

}
