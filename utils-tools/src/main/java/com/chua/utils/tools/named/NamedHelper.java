package com.chua.utils.tools.named;

import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.pattern.PatternBuilder;
import com.google.common.base.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.chua.utils.tools.constant.PatternConstant.HUMP_PATTERN;
import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_MINS;
import static com.chua.utils.tools.constant.SymbolConstant.SYMBOL_UNDERLINE;

/**
 * 命名工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
public class NamedHelper {
    /**
     * 将{@see symbol}符号转驼峰命名
     * <pre>
     *     NamedHelper.toHump("T_TABLE", "_") = tTable
     *     NamedHelper.toHump("t_table", "_") = tTable
     *     NamedHelper.toHump("t_taBle", "_") = tTable
     * </pre>
     *
     * @param str    源数据
     * @param symbol 符号
     * @return 驼峰数据
     */
    public static String toHump(final String str, final String symbol) {
        if (Strings.isNullOrEmpty(str)) {
            return str;
        }
        String sourceStr = str.toLowerCase();
        PatternBuilder patternBuilder = new PatternBuilder();
        Pattern pattern = patternBuilder.append(symbol).appendAnyChar().toPattern();

        Matcher matcher = pattern.matcher(sourceStr);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 将"-"符号转驼峰命名
     * <p>{@link com.chua.utils.tools.constant.SymbolConstant#SYMBOL_MINS}</p>
     * <pre>
     *     NamedHelper.toHump("T_TABLE") = tTable
     *     NamedHelper.toHump("t_table") = tTable
     *     NamedHelper.toHump("t_taBle") = tTable
     * </pre>
     *
     * @param str 源数据
     * @return 驼峰数据
     * @see com.chua.utils.tools.constant.SymbolConstant
     * @see com.chua.utils.tools.constant.SymbolConstant#SYMBOL_MINS
     */
    public static String toHump(final String str) {
        return toHump(str, SYMBOL_MINS);
    }

    /**
     * 驼峰转符号数据
     * <pre>
     *     NamedHelper.toLine("tTable", "_") = T_TABLE
     *     NamedHelper.toLine("tTable", "@") = T@TABLE
     * </pre>
     *
     * @param str    源数据
     * @param symbol 符号
     * @return 符号数据
     */
    public static String toSymbol(final String str, final String symbol) {
        if (Strings.isNullOrEmpty(str)) {
            return str;
        }
        Matcher matcher = HUMP_PATTERN.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, symbol + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰转下划线{@link com.chua.utils.tools.constant.SymbolConstant#SYMBOL_UNDERLINE}
     * <pre>
     *     NamedHelper.toLine("tTable") = T_TABLE
     * </pre>
     *
     * @param str 源数据
     * @return 符号数据
     * @see com.chua.utils.tools.constant.SymbolConstant#SYMBOL_UNDERLINE
     * @see com.chua.utils.tools.constant.SymbolConstant
     */
    public static String toLine(final String str) {
        return toSymbol(str, SYMBOL_UNDERLINE);
    }

    /**
     * 首字母大写
     * <pre>
     *     firstUpperCase("test") = "Test"
     *     firstUpperCase(null) = null
     * </pre>
     *
     * @param str 源数据
     * @return 首字母大写数据
     */
    public static String firstUpperCase(String str) {
        if (Strings.isNullOrEmpty(str)) {
            return str;
        }
        return StringHelper.first(str).toUpperCase() + str.substring(1);
    }

    /**
     * 首字母小写
     * <pre>
     *     firstLowerCase("test") = "Test"
     *     firstLowerCase(null) = null
     * </pre>
     *
     * @param str 源数据
     * @return 首字母小写
     */
    public static String firstLowerCase(String str) {
        if (Strings.isNullOrEmpty(str)) {
            return str;
        }
        return StringHelper.first(str).toLowerCase() + str.substring(1);
    }
}
