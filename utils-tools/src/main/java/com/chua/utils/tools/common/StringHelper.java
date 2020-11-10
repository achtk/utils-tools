package com.chua.utils.tools.common;

import com.chua.utils.tools.collects.collections.ListHelper;
import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.common.charset.CharsetHelper;
import com.chua.utils.tools.empty.Empty;
import com.chua.utils.tools.function.IPreMatcher;
import com.chua.utils.tools.guid.GUID;
import com.chua.utils.tools.named.NamedHelper;
import com.chua.utils.tools.text.IdHelper;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.chua.utils.tools.constant.NumberConstant.INDEX_NOT_FOUND;
import static com.chua.utils.tools.constant.PatternConstant.*;
import static com.chua.utils.tools.constant.StringConstant.DECIMAL_FORMAT;
import static com.chua.utils.tools.constant.SymbolConstant.*;

/**
 * 字符串工具类
 *
 * @author CH
 */
public class StringHelper {

    /**
     * 是否包含其它字符
     * <pre>
     *     contains("test*", "?") = false;
     *     contains("test*", "?", "*") = true;
     * </pre>
     *
     * @param value 数据
     * @param signs 符号
     * @return boolean
     */
    public static boolean contains(final String value, final String... signs) {
        if (Strings.isNullOrEmpty(value)) {
            return false;
        }

        for (String sign : signs) {
            if (value.contains(sign)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否包含其它字符
     *
     * @param value 数据
     * @param signs 符号
     * @return boolean
     */
    public static boolean contains(final String value, final char signs) {
        return !isEmpty(value) && value.indexOf(signs) > -1;
    }

    /**
     * 是否包含其它字符
     *
     * @param value 数据
     * @param signs 符号
     * @return 第一个满足条件的位置
     */
    public static int indexOf(final String value, String signs) {
        if (Strings.isNullOrEmpty(value)) {
            return INDEX_NOT_FOUND;
        }
        return value.indexOf(signs);
    }

    /**
     * 翻转
     * <pre>
     *      StringHelper.reverse(null)  = null
     *      StringHelper.reverse("")    = ""
     *      StringHelper.reverse("bat") = "tab"
     * </pre>
     *
     * @param source 源数据
     * @return String
     */
    public static String reverse(final String source) {
        if (source == null) {
            return null;
        }
        return new StringBuilder(source).reverse().toString();
    }

    /**
     * 根据分隔列表获取字符串数组
     *
     * @param source    数据
     * @param delimiter 分隔符
     * @return String[]
     */
    public static String[] delimitedListToStringArray(String source, String delimiter) {
        return delimitedListToStringArray(source, delimiter, null);
    }

    /**
     * 根据分隔列表获取字符串数组
     *
     * @param source        数据
     * @param delimiter     分隔符
     * @param charsToDelete 删除符号
     * @return String[]
     */
    public static String[] delimitedListToStringArray(String source, String delimiter, String charsToDelete) {
        if (source == null) {
            return new String[0];
        }
        if (delimiter == null) {
            return new String[]{source};
        }

        List<String> result = new ArrayList<>();
        if (delimiter.isEmpty()) {
            for (int i = 0; i < source.length(); i++) {
                result.add(deleteAny(source.substring(i, i + 1), charsToDelete));
            }
        } else {
            int pos = 0;
            int delPos;
            while ((delPos = source.indexOf(delimiter, pos)) != -1) {
                result.add(deleteAny(source.substring(pos, delPos), charsToDelete));
                pos = delPos + delimiter.length();
            }
            if (source.length() > 0 && pos <= source.length()) {
                result.add(deleteAny(source.substring(pos), charsToDelete));
            }
        }
        return result.toArray(new String[0]);
    }

    /**
     * 删除字符串
     * <pre>
     *     capitalize("11", 1) = ""
     *     capitalize("t1", "") = "t1"
     *     capitalize(null, "1") = null
     *     capitalize("//", "1") = "//"
     *     capitalize("T1", "1") = "T"
     * </pre>
     *
     * @param source        源数据
     * @param charsToDelete 待删除字符
     * @return 删除后的字符串
     */
    public static String deleteAny(String source, String charsToDelete) {
        if (!hasLength(source) || !hasLength(charsToDelete)) {
            return source;
        }

        StringBuilder sb = new StringBuilder(source.length());
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            if (charsToDelete.indexOf(c) == -1) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 第一个字符大小写
     * <pre>
     *     capitalize("11", true) = "11"
     *     capitalize("t1", true) = "T1"
     *     capitalize(null, true) = null
     *     capitalize("//", true) = "//"
     *     capitalize("T1", false) = "t1"
     * </pre>
     *
     * @param source     数据
     * @param capitalize true:大写, false: 小写
     * @return String
     */
    public static String changeFirstCharacterCase(String source, boolean capitalize) {
        if (hasNoneLength(source)) {
            return source;
        }

        char baseChar = source.charAt(0);
        char updatedChar;
        if (capitalize) {
            updatedChar = Character.toUpperCase(baseChar);
        } else {
            updatedChar = Character.toLowerCase(baseChar);
        }
        if (baseChar == updatedChar) {
            return source;
        }

        char[] chars = source.toCharArray();
        chars[0] = updatedChar;
        return new String(chars, 0, chars.length);
    }


    /**
     * 第一个字符大写
     * <pre>
     *     capitalize("11") = "11"
     *     capitalize("t1") = "T1"
     *     capitalize(null) = null
     *     capitalize("//") = "/"
     * </pre>
     *
     * @param source 数据
     * @return String
     */
    public static String capitalize(String source) {
        return changeFirstCharacterCase(source, true);
    }

    /**
     * 删除重复的//
     * <pre>
     *     noRepeat("11") = "11"
     *     noRepeat(null) = null
     *     noRepeat("//") = "/"
     * </pre>
     *
     * @param source 原始数据
     * @return String
     */
    public static String noRepeatSlash(String source) {
        return Strings.isNullOrEmpty(source) ? source : source.replaceAll("(/){1,}", SYMBOL_LEFT_SLASH);
    }

    /**
     * 删除重复
     * <pre>
     *     noRepeat("11") = "1"
     *     noRepeat(null) = null
     *     noRepeat("//") = "/"
     * </pre>
     *
     * @param source 原始数据
     * @return String
     */
    public static String noRepeat(String source) {
        if (Strings.isNullOrEmpty(source)) {
            return source;
        }
        StringBuffer stringBuffer = new StringBuffer();
        char before = '\b';
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            if (before != c) {
                stringBuffer.append(c);
                before = c;
            }
        }
        return stringBuffer.toString();
    }

    /**
     * 替换字符
     *
     * @param source     原始数据
     * @param oldPattern 旧正则
     * @param newPattern 新正则
     * @return String
     */
    public static String replace(String source, String oldPattern, String newPattern) {
        if (hasLength(source) && hasLength(oldPattern) && newPattern != null) {
            int index = source.indexOf(oldPattern);
            if (index == -1) {
                return source;
            } else {
                int capacity = source.length();
                if (newPattern.length() > oldPattern.length()) {
                    capacity += 16;
                }

                StringBuilder sb = new StringBuilder(capacity);
                int pos = 0;

                for (int patLen = oldPattern.length(); index >= 0; index = source.indexOf(oldPattern, pos)) {
                    sb.append(source.substring(pos, index));
                    sb.append(newPattern);
                    pos = index + patLen;
                }

                sb.append(source.substring(pos));
                return sb.toString();
            }
        } else {
            return source;
        }
    }

    /**
     * jdk guid
     *
     * @return guid
     */
    public static String guid() {
        return GUID.randomGuid().toString();
    }

    /**
     * 计算
     * <pre>
     *     calcString(1, 4) = 0.250
     *     calcString(1, 1) = 1.000
     * </pre>
     *
     * @param number  数量1
     * @param number2 数量2
     * @return String
     */
    public static String calcString(long number, long number2) {
        float perNumber = (float) number / (float) number2;
        return DECIMAL_FORMAT.format(perNumber);
    }

    /**
     * 计算
     * <pre>
     *     calcString(1, 4) = 0.250
     *     calcString(1, 1) = 1.000
     * </pre>
     *
     * @param number  数量1
     * @param number2 数量2
     * @return Float
     */
    public static Float calcFloat(int number, long number2) {
        float perNumber = (float) number / (float) number2;
        return Float.valueOf(DECIMAL_FORMAT.format(perNumber));
    }

    /**
     * 获取关键词前字符串
     *
     * @param key   元数据
     * @param split 关键词
     * @param index 位置
     * @return String
     */
    public static String split(String key, String split, int index) {
        if (Strings.isNullOrEmpty(key)) {
            return SYMBOL_EMPTY;
        }
        String[] split1 = key.split(SYMBOL_WELL);
        index = index + 1;
        int max = split1.length;
        index = index > max ? max : index;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < index; i++) {
            sb.append(SYMBOL_WELL).append(split1[i]);
        }
        return sb.length() > 0 ? sb.substring(1) : SYMBOL_EMPTY;
    }


    /**
     * join.
     * <pre>
     *     join([1,2,3], "_") = "1_2_3"
     *     join([], "_") = ""
     *     join(null, "_") = ""
     * </pre>
     *
     * @param array 字符串数组
     * @param split 连接符
     * @return String.
     */
    public static String join(String[] array, char split) {
        if (null == array || array.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, length = array.length; i < length; i++) {
            if (i > 0) {
                sb.append(split);
            }
            sb.append(array[i]);
        }
        return sb.toString();
    }

    /**
     * join.
     * <pre>
     *     join({1:1}, "=", "&") = "1=1"
     *     join({1: 1, 2: 2}, "=", "&") = "1=1&2=2"
     * </pre>
     *
     * @param bodyer            数据
     * @param keyValueDelimiter key-value分隔符
     * @param itemDelimiter     数据分隔符
     * @return String.
     */
    public static String join(Map<String, Object> bodyer, String keyValueDelimiter, String itemDelimiter) {
        if (!BooleanHelper.hasLength(bodyer)) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Object> entry : bodyer.entrySet()) {
            stringBuilder.append(itemDelimiter).append(entry.getKey()).append(keyValueDelimiter).append(entry.getValue());
        }

        return stringBuilder.substring(itemDelimiter.length());
    }

    /**
     * join.
     * <pre>
     *     join([1,2,3], "_") = "1_2_3"
     *     join([], "_") = ""
     *     join(null, "_") = ""
     * </pre>
     *
     * @param coll  字符串数组
     * @param split 连接符
     * @return String.
     */
    public static String join(Collection<String> coll, String split) {
        if (null == coll || coll.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (String s : coll) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(split);
            }
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * 获取 separator 后面字符
     * <p>
     * subAndEndstring(null, null, "/") = ""
     * subAndEndstring("test", null, null) = ""
     * subAndEndstring(null, "/", "/") = ""
     * subAndEndstring("test/", "/", "/") = ""
     * subAndEndstring("/test/@", "/", "@") = "test/"
     * </p>
     *
     * @param source       源数据
     * @param subSeparator 分隔符
     * @param endSeparator 分隔符
     * @return String
     */
    public static String subAndEndString(String source, String subSeparator, String endSeparator) {
        if (isEmpty(source) || isEmpty(endSeparator) || isEmpty(subSeparator)) {
            return "";
        }

        final int subIndex = source.indexOf(subSeparator);
        final int endIndex = source.indexOf(endSeparator);
        if (subIndex >= endIndex) {
            return "";
        }
        return subIndex > -1 ? source.substring(subIndex + 1, endIndex) : source;
    }

    /**
     * 获取 separator 后面字符
     * <p>
     * substring(null, null) = ""
     * substring("test", null) = ""
     * substring(null, "/") = ""
     * substring("test/", "/") = ""
     * substring("/test", "/") = "test"
     * substring("/test", "@") = "/test"
     * </p>
     *
     * @param source    源数据
     * @param separator 分隔符
     * @return String
     */
    public static String substring(String source, String separator) {
        return substring(source, separator, "");
    }

    /**
     * 获取 separator 后面字符
     * <p>
     * substring(null, null, "*") = ""
     * substring("test", null, "*") = ""
     * substring(null, "/", "*") = ""
     * substring("test/", "/", "*") = "*"
     * substring("/test", "/", "*") = "*test"
     * substring("/test", "@", "*") = "/test"
     * </p>
     *
     * @param source    源数据
     * @param separator 分隔符
     * @return String
     */
    public static String substring(String source, String separator, String replace) {
        if (isEmpty(source) || isEmpty(separator)) {
            return "";
        }

        final int index = source.indexOf(separator);
        return index > -1 ? replace + source.substring(index + 1) : source;
    }

    /**
     * 获取 separator 后面字符
     * <p>
     * lastSubstring(null, null) = ""
     * lastSubstring("test", null) = ""
     * lastSubstring(null, "/") = ""
     * lastSubstring("test/", "/") = ""
     * lastSubstring("/test/", "/") = ""
     * lastSubstring("/test", "@") = "/test"
     * lastSubstring("/tes/t", "/") = "t"
     * </p>
     *
     * @param source    源数据
     * @param separator 分隔符
     * @return String
     */
    public static String lastSubstring(String source, String separator) {
        return lastSubstring(source, separator, "");
    }

    /**
     * 获取 separator 后面字符
     * <p>
     * lastSubstring(null, null, "*") = ""
     * lastSubstring("test", null, "*") = ""
     * lastSubstring(null, "/", "*") = ""
     * lastSubstring("test/", "/", "*") = ""
     * lastSubstring("/test/", "/", "*") = "*"
     * lastSubstring("/test", "@", "*") = "/test"
     * lastSubstring("/tes/t", "/", "*") = "*t"
     * </p>
     *
     * @param source    源数据
     * @param separator 分隔符
     * @return String
     */
    public static String lastSubstring(String source, String separator, String replace) {
        if (isEmpty(source) || isEmpty(separator)) {
            return "";
        }

        final int index = source.lastIndexOf(separator);
        return index > -1 ? replace + source.substring(index + 1) : source;
    }

    /**
     * 获取 separator 前面字符
     * <p>
     * endstring(null, null) = ""
     * endstring("test", null) = ""
     * endstring(null, "/") = ""
     * endstring("test/", "/") = "test"
     * endstring("/test", "/") = ""
     * endstring("/test", "@") = "/test"
     * </p>
     *
     * @param source    源数据
     * @param separator 分隔符
     * @return String
     */
    public static String endstring(String source, String separator) {
        return endstring(source, separator, "");
    }

    /**
     * 获取 separator 前面字符
     * <p>
     * endstring(null, null, "*") = ""
     * endstring("test", null, "*") = ""
     * endstring(null, "/", "*") = ""
     * endstring("test/", "/", "*") = "test*"
     * endstring("/test", "/", "*") = "*"
     * endstring("/test", "@", "*") = "/test"
     * </p>
     *
     * @param source    源数据
     * @param separator 分隔符
     * @return String
     */
    public static String endstring(String source, String separator, String replace) {
        if (isEmpty(source) || isEmpty(separator)) {
            return "";
        }

        final int index = source.indexOf(separator);
        return index > -1 ? source.substring(0, index) + replace : source;
    }

    /**
     * 获取 separator 前面字符
     * <p>
     * lastEndstring(null, null) = ""
     * lastEndstring("test", null) = ""
     * lastEndstring(null, "/") = ""
     * lastEndstring("test/", "/") = "test"
     * lastEndstring("/test/", "/") = "/test"
     * lastEndstring("/test", "@") = ""
     * lastEndstring("/tes/t", "/") = "/tes"
     * </p>
     *
     * @param source    源数据
     * @param separator 分隔符
     * @return String
     */
    public static String lastEndstring(String source, String separator) {
        return lastSubstring(source, separator, "");
    }

    /**
     * 获取 separator 前面字符
     * <p>
     * lastEndstring(null, null, "*") = ""
     * lastEndstring("test", null, "*") = ""
     * lastEndstring(null, "/", "*") = ""
     * lastEndstring("test/", "/", "*") = "test*"
     * lastEndstring("/test/", "/", "*") = "/test*"
     * lastEndstring("/test", "@", "*") = ""
     * lastEndstring("/tes/t", "/", "*") = "/tes*"
     * </p>
     *
     * @param source    源数据
     * @param separator 分隔符
     * @return String
     */
    public static String lastEndstring(String source, String separator, String replace) {
        if (isEmpty(source) || isEmpty(separator)) {
            return "";
        }

        final int index = source.lastIndexOf(separator);
        return index > -1 ? source.substring(0, index) + replace : source;
    }

    /**
     * 是否有文本
     *
     * @param str
     * @return
     */
    public static boolean hasText(String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    /**
     * 是否包含文本
     *
     * @param source
     * @return
     */
    private static boolean containsText(CharSequence source) {
        int strLen = source.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(source.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * stringbuffer截取
     *
     * @param stringBuffer
     * @param index
     * @return
     */
    public static String stringbuffer(StringBuffer stringBuffer, int index) {
        return null == stringBuffer || stringBuffer.length() <= index ? "" : stringBuffer.substring(index);
    }

    /**
     * 获取字符串
     *
     * @param names
     * @return
     */
    public static String defineString(Collection<?> names) {
        if (BooleanHelper.hasLength(names)) {
            StringBuffer sb = new StringBuffer();
            for (Object name : names) {
                sb.append(",").append(name);
            }
            return stringbuffer(sb, 1);
        }
        return "";
    }

    /**
     * object 转String
     *
     * @param key 索引
     * @return
     */
    public static String toStringObject(Object key) {
        return null == key ? "" : key.toString();
    }

    /**
     * 下划线转驼峰
     * <pre>
     *     lineToHump("T_TABLE") = tTable
     * </pre>
     *
     * @param source 元数据
     * @return
     */
    public static String lineToHump(final String source) {
        return NamedHelper.toHump(source);
    }

    /**
     * 驼峰转下划线
     * <pre>
     *     humpToLine2("tTable") = T_TABLE
     * </pre>
     *
     * @param source 元数据
     * @return
     */
    public static String humpToLine(final String source) {
        if (Strings.isNullOrEmpty(source)) {
            return source;
        }
        return humpToLine2(source).toUpperCase();
    }

    /**
     * 驼峰转下划线
     * <pre>
     *     humpToLine2("tTable") = t_table
     * </pre>
     *
     * @param source 元数据
     * @return
     */
    public static String humpToLine2(final String source) {
        return humpToLine2(source, "_");
    }

    /**
     * 驼峰转自定义
     * <pre>
     *     humpToLine2("tTable") = t_table
     * </pre>
     *
     * @param source 元数据
     * @return
     */
    public static String humpToLine2(final String source, final String seq) {
        if (Strings.isNullOrEmpty(source)) {
            return source;
        }
        Matcher matcher = HUMP_PATTERN.matcher(source);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, seq + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 驼峰转下划线
     * <pre>
     *     humpToMin("tTable") = T-TABLE
     * </pre>
     *
     * @param source 元数据
     * @return
     */
    public static String humpToMin(final String source) {
        if (Strings.isNullOrEmpty(source)) {
            return source;
        }
        return humpToMin2(source).toUpperCase();
    }

    /**
     * 驼峰转减号
     * <pre>
     *     humpToMin2("tTable") = t-table
     * </pre>
     *
     * @param source 元数据
     * @return
     */
    public static String humpToMin2(final String source) {
        if (Strings.isNullOrEmpty(source)) {
            return source;
        }
        Matcher matcher = HUMP_PATTERN.matcher(source);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "-" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 删除空格
     * <pre>
     * deleteWhitespace(null)         = null
     * deleteWhitespace("")           = ""
     * deleteWhitespace("abc")        = "abc"
     * deleteWhitespace("   ab  c  ") = "abc"
     * </pre>
     *
     * @param source 元数据
     * @return
     */
    public static String trimWhite(String source) {
        return deleteWhitespace(source);
    }

    /**
     * 删除空格
     * <pre>
     * deleteWhitespace(null)         = null
     * deleteWhitespace("")           = ""
     * deleteWhitespace("abc")        = "abc"
     * deleteWhitespace("   ab  c  ") = "abc"
     * </pre>
     *
     * @param source 元数据
     * @return
     */
    public static String deleteWhitespace(String source) {
        if (isEmpty(source)) {
            return source;
        }
        int sz = source.length();
        char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(source.charAt(i))) {
                chs[count++] = source.charAt(i);
            }
        }
        if (count == sz) {
            return source;
        }
        return new String(chs, 0, count);
    }

    /**
     * 通配符转正则
     *
     * @param wildcard
     */
    public static String wildcard2RegEx(final String wildcard) {
        if (Strings.isNullOrEmpty(wildcard)) {
            return wildcard;
        }

        String newWildcard = wildcard;
        newWildcard = newWildcard.replace('.', '#');
        newWildcard = newWildcard.replaceAll(SYMBOL_WELL, "\\\\.");
        newWildcard = newWildcard.replace('*', '#');
        newWildcard = newWildcard.replaceAll(SYMBOL_WELL, ".*");
        newWildcard = newWildcard.replace('?', '#');
        newWildcard = newWildcard.replaceAll(SYMBOL_WELL, ".?");

        return "^" + newWildcard + "$";
    }

    /**
     * 空默认值
     * <pre>
     *      StringHelper.getStringOrDefault(null)  = ""
     *      StringHelper.getStringOrDefault("")    = ""
     *      StringHelper.getStringOrDefault(" ")   = " "
     *      StringHelper.getStringOrDefault("bat") = "bat"
     *      StringHelper.getStringOrDefault("")      = ""
     * </pre>
     *
     * @param source 源数据
     * @return String
     */
    public static String getStringOrDefault(String source) {
        return getStringOrDefault(source, SYMBOL_EMPTY);
    }

    /**
     * UUID默认值
     *
     * @param source 源数据
     * @return String
     */
    public static String getStringOrUuid(String source) {
        return getStringOrDefault(source, IdHelper.createSimpleUuid());
    }

    /**
     * 空默认值
     * <pre>
     *      StringHelper.getStringOrDefault(null, "NULL")  = "NULL"
     *      StringHelper.getStringOrDefault("", "NULL")    = "NULL"
     *      StringHelper.getStringOrDefault(" ", "NULL")   = " "
     *      StringHelper.getStringOrDefault("bat", "NULL") = "bat"
     *      StringHelper.getStringOrDefault("", null)      = null
     * </pre>
     *
     * @param source     源数据
     * @param defaultStr 默认值
     * @return String
     */
    public static String getStringOrDefault(String source, String defaultStr) {
        return null == source ? defaultStr : source;
    }

    /**
     * 获取启动pid
     *
     * @return pid
     */
    public static String getPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return name.split(SYMBOL_AT)[0];
    }

    /**
     * 分隔符开头移除该信息
     *
     * @param source    数据
     * @param separator 分隔符
     * @return
     */
    public static String startsWithAndEmpty(String source, String separator) {
        if (isEmpty(source) || isEmpty(separator)) {
            return source;
        }

        return source.startsWith(separator) ? source.substring(separator.length()) : source;
    }

    /**
     * 压缩字符串
     *
     * @param source 元数据
     * @return
     */
    public static String compress(final String source, final String charset) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(source));
        return GzipHelper.compressToString(source, getStringOrDefault(charset, CharsetHelper.UTF_8));
    }

    /**
     * 压缩字符串
     *
     * @param source 元数据
     * @return
     */
    public static String compress(final String source) {
        return compress(source, CharsetHelper.UTF_8);
    }

    /**
     * 解压字符串
     *
     * @param source 元数据
     * @return
     */
    public static String uncompress(final String source, final String charset) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(source));
        return GzipHelper.uncompress(source, getStringOrDefault(charset, CharsetHelper.UTF_8));
    }

    /**
     * 解压字符串
     *
     * @param source 元数据
     * @return
     */
    public static String uncompress(final String source) {
        return uncompress(source, CharsetHelper.UTF_8);
    }

    /**
     * 字符串转数组
     *
     * @param str               字符串
     * @param delimiters        分隔符
     * @param trimTokens        去空格
     * @param ignoreEmptyTokens 忽略空值
     * @return
     */
    public static String[] tokenizeToStringArray(
            String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {

        if (str == null) {
            return null;
        }

        StringTokenizer st = new StringTokenizer(str, delimiters);
        List<String> tokens = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (trimTokens) {
                token = token.trim();
            }
            if (!ignoreEmptyTokens || token.length() > 0) {
                tokens.add(token);
            }
        }
        return tokens.toArray(Empty.EMPTY_STRING);
    }

    /**
     * 页面中去除字符串中的空格、回车、换行符、制表符
     * <pre>
     *     replaceBlank("23") = "23"
     *     replaceBlank("23\t") = "23"
     *     replaceBlank("23\n") = "23"
     *     replaceBlank("23\r") = "23"
     *     replaceBlank("23 ") = "23"
     *     replaceBlank("2 3 ") = "23"
     *     replaceBlank(null) = ""
     * </pre>
     *
     * @param str 需要处理的字符串
     */
    public static String replaceBlank(String str) {
        if (Strings.isNullOrEmpty(str)) {
            return "";
        }
        Matcher m = PATTERN_BLANK.matcher(str);
        return m.replaceAll("");
    }

    /**
     * 正则匹配
     * <pre>
     *     wildcardMatch("23", "2") = true
     *     wildcardMatch("", 2) = false
     *     wildcardMatch(null, 2) = false
     *     wildcardMatch(null, -1) = false
     *     wildcardMatch("2", -1) = false
     *     wildcardMatch(null, null) = true
     * </pre>
     *
     * @return
     */
    public static boolean wildcardMatch(final String source, final String matcher) {
        if (null == source) {
            return null == matcher;
        }

        if (null == matcher) {
            return false;
        }

        return FileHelper.wildcardMatch(source, matcher);
    }

    /**
     * repeat - 通过源字符串重复生成N次组成新的字符串。
     * <pre>
     *     repeat("23", 2) = "2323"
     *     repeat("", 2) = ""
     *     repeat(null, 2) = ""
     *     repeat(null, -1) = ""
     *     repeat("2", -1) = ""
     * </pre>
     *
     * @param src - 源字符串 例如: 空格(" "), 星号("*"), "浙江" 等等...
     * @param num - 重复生成次数
     * @return 返回已生成的重复字符串
     */
    public static String repeat(String src, int num) {
        if (Strings.isNullOrEmpty(src)) {
            return "";
        }

        if (num < 1) {
            return src;
        }

        StringBuilder s = new StringBuilder();
        for (int i = 0; i < num; i++) {
            s.append(src);
        }
        return s.toString();
    }

    /**
     * 为空
     * <pre>
     *     isNullOrEmpty("") == true;
     *     isNullOrEmpty(null) == true;
     * </pre>
     *
     * @param value 值
     * @return boolean
     */
    public static boolean isNullOrEmpty(final String... value) {
        if (null == value || value.length == 0) {
            return true;
        }
        for (String charSequence : value) {
            if (!Strings.isNullOrEmpty(charSequence)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 为空
     * <pre>
     *     StringHelper.isEmpty("") == true;
     *     StringHelper.isEmpty(null) == false;
     * </pre>
     *
     * @param value 值
     * @return boolean
     */
    public static boolean isEmpty(final CharSequence value) {
        return null == value || value.length() == 0;
    }

    /**
     * 为空
     * <pre>
     *     StringHelper.isEmpty("") == true;
     *     StringHelper.isEmpty(null) == false;
     * </pre>
     *
     * @param value 值
     * @return boolean
     */
    public static boolean isEmpty(final String value) {
        return Strings.isNullOrEmpty(value);
    }

    /**
     * 为空
     * <pre>
     *     isNull("") == false;
     *     isNull(null) == true;
     * </pre>
     *
     * @param value 值
     * @return boolean
     */
    public static boolean isNull(final CharSequence value) {
        return value == null;
    }


    /**
     * 为空
     * <pre>
     *  isNotEmpty(null) == false
     *  isNotEmpty("1") == true
     *  isNotEmpty("") == true
     * </pre>
     *
     * @param value 源数据
     * @return
     */
    public static boolean isNotEmpty(final String value) {
        return !isEmpty(value);
    }

    /**
     * 获取字符串之间值
     * <pre>
     *     cut("@test@", "@", "@") == test
     *     cut("@test#", "@", SYMBOL_WELL) == test
     * </pre>
     *
     * @param start    开始符号
     * @param end      结束符号
     * @param srcValue 源数据
     * @return
     */
    public static List<String> cut(final String srcValue, final String start, final String end) {
        return pattern(srcValue, start + REGEXP_ANY + end, -1);
    }

    /**
     * 获取以开始符号开始的字符串数据
     * cut("@test@", "@", -1) == ["test", ""]
     * cut("@test#", "@", -1) == ["test#"]
     *
     * @param start    开始符号
     * @param srcValue 源数据
     * @param limit    返回数量
     * @return
     */
    public static List<String> startsWith(final String srcValue, final String start, int limit) {
        return pattern(srcValue, start + REGEXP_ANY, limit);
    }

    /**
     * @param start    开始符号
     * @param srcValue 源数据
     * @return
     */
    public static List<String> startsWith(final String srcValue, final String start) {
        return pattern(srcValue, start + REGEXP_ANY, -1);
    }

    /**
     * @param end      结束符号
     * @param srcValue 源数据
     * @return
     */
    public static List<String> endsWith(final String srcValue, final String end, final int limit) {
        return pattern(srcValue, REGEXP_ANY + end, limit);
    }

    /**
     * @param end      结束符号
     * @param srcValue 源数据
     * @return
     */
    public static List<String> endsWith(final String srcValue, final String end) {
        return pattern(srcValue, REGEXP_ANY + end, -1);
    }

    /**
     * 正则匹配
     *
     * @param srcValue     源数据
     * @param patternValue 正则表达式
     * @return
     */
    public static List<String> pattern(final String srcValue, final String patternValue, final int limit) {
        return pattern(srcValue, patternValue, limit, new IPreMatcher<String>() {

            @Override
            public String matcher(Matcher matcher) {
                return matcher.group(1);
            }
        });
    }

    /**
     * 正则匹配
     *
     * @param srcValue     源数据
     * @param patternValue 正则表达式
     * @return
     */
    public static List<String> pattern(final String srcValue, final String patternValue, final int limit, IPreMatcher<String> patternHandler) {
        Pattern pattern = Pattern.compile(patternValue);
        Matcher matcher = pattern.matcher(srcValue);

        List<String> lStrings = new ArrayList<>();
        while (matcher.find()) {
            final String group = patternHandler.matcher(matcher);
            if (-1 == limit || lStrings.size() <= limit) {
                lStrings.add(null != group ? group.trim() : SYMBOL_EMPTY);
            } else {
                break;
            }
        }
        return lStrings;
    }

    /**
     * 拆分字段
     *
     * @param srcValue  源数据
     * @param condition 拆分符号
     * @return
     */
    public static List<String> splitters(String srcValue, String... condition) {
        if (isEmpty(srcValue)) {
            return ListHelper.newArrayList();
        }
        String regex = SYMBOL_EMPTY;
        for (String s : condition) {
            regex = regex.concat(SYMBOL_OR).concat(s);
        }
        if (regex.length() > 0) {
            regex = regex.substring(1);
        }

        return Splitter.onPattern(regex).omitEmptyStrings().splitToList(srcValue);
    }

    /**
     * 删除多余字符
     *
     * @param srcValue     源数据
     * @param extensionPer 删除的符号
     * @return
     */
    public static String remove(final String srcValue, final String extensionPer) {
        return srcValue.replace(extensionPer, SYMBOL_EMPTY);
    }

    /**
     * 转为字符串
     *
     * @param data 字符数组
     * @return
     */
    public static String asString(byte[] data) {
        return new String(data, Charset.forName(CharsetHelper.UTF_8));
    }

    /**
     * 转为List
     *
     * @param data      字符数组
     * @param separator 分割符号
     * @return
     */
    public static List<String> asList(byte[] data, final String separator) {
        String s = new String(data, Charset.forName(CharsetHelper.UTF_8));
        return Splitter.on(separator).trimResults().splitToList(s);
    }

    /**
     * 去除空格
     * <pre>
     * trim(null)          = null
     * trim("")            = ""
     * trim("     ")       = ""
     * trim("abc")         = "abc"
     * trim("    abc    ") = "abc"
     * </pre>
     *
     * @param srcValue 源数据
     * @return
     */
    public static String trim(final String srcValue) {
        return null == srcValue ? SYMBOL_EMPTY : srcValue.trim();
    }

    /**
     * 去除空格
     * <pre>
     * trim(null)          = null
     * trim("")            = ""
     * trim("     ")       = ""
     * trim("abc")         = "abc"
     * trim("    abc    ") = "abc"
     * </pre>
     *
     * @param srcValue 源数据
     * @param reg      正则
     * @return
     */
    public static String trim(String srcValue, String reg) {
        return null == srcValue || null == reg ? SYMBOL_EMPTY : srcValue.replaceAll(reg, SYMBOL_EMPTY);
    }

    /**
     * 去除空格
     * <pre>
     * trimToNull(null)          = null
     * trimToNull("")            = null
     * trimToNull("     ")       = null
     * trimToNull("abc")         = "abc"
     * trimToNull("    abc    ") = "abc"
     * </pre>
     *
     * @param srcValue
     * @return
     */
    public static String trimToNull(final String srcValue) {
        return isEmpty(srcValue) ? null : srcValue.trim();
    }

    /**
     * 去除空格
     * <pre>
     * trimToEmpty(null)          = ""
     * trimToEmpty("")            = ""
     * trimToEmpty("     ")       = ""
     * trimToEmpty("abc")         = "abc"
     * trimToEmpty("    abc    ") = "abc"
     * </pre>
     *
     * @param str
     * @return
     */
    public static String trimToEmpty(final String str) {
        return str == null ? SYMBOL_EMPTY : str.trim();
    }

    /**
     * 获取指定长度字符串
     * <pre>
     * truncate(null, 0)       = null
     * truncate(null, 2)       = null
     * truncate("", 4)         = ""
     * truncate("abcdefg", 4)  = "abcd"
     * truncate("abcdefg", 6)  = "abcdef"
     * truncate("abcdefg", 7)  = "abcdefg"
     * truncate("abcdefg", 8)  = "abcdefg"
     * truncate("abcdefg", -1) = ""
     * </pre>
     *
     * @param str    字符串
     * @param length 长度
     * @return
     */
    public static String truncate(final String str, final int length) {
        return truncate(str, 0, length);
    }

    /**
     * 获取指定长度的字符串
     * <pre>
     * truncate(null, 0, 0) = null
     * truncate(null, 2, 4) = null
     * truncate("", 0, 10) = ""
     * truncate("", 2, 10) = ""
     * truncate("abcdefghij", 0, 3) = "abc"
     * truncate("abcdefghij", -1, 3) = "j"
     * truncate("abcdefghij", 5, -3) = "cde"
     * truncate("abcdefghij", -5, -3) = "cde"
     * truncate("abcdefghij", 5, 6) = "fghij"
     * truncate("raspberry peach", 10, 15) = "peach"
     * truncate("abcdefghijklmno", 0, 10) = "abcdefghij"
     * truncate("abcdefghijklmno", -1, 10) = o"
     * truncate("abcdefghijklmno", Integer.MIN_VALUE, 10) = "abcdefghij"
     * truncate("abcdefghijklmno", Integer.MIN_VALUE, Integer.MAX_VALUE) = "abcdefghijklmno"
     * truncate("abcdefghijklmno", 0, Integer.MAX_VALUE) = "abcdefghijklmno"
     * truncate("abcdefghijklmno", 1, 10) = "bcdefghijk"
     * truncate("abcdefghijklmno", 2, 10) = "cdefghijkl"
     * truncate("abcdefghijklmno", 3, 10) = "defghijklm"
     * truncate("abcdefghijklmno", 4, 10) = "efghijklmn"
     * truncate("abcdefghijklmno", 5, 10) = "fghijklmno"
     * truncate("abcdefghijklmno", 5, 5) = "fghij"
     * truncate("abcdefghijklmno", 5, 3) = "fgh"
     * truncate("abcdefghijklmno", 10, 3) = "klm"
     * truncate("abcdefghijklmno", 10, Integer.MAX_VALUE) = "klmno"
     * truncate("abcdefghijklmno", 13, 1) = "n"
     * truncate("abcdefghijklmno", 13, Integer.MAX_VALUE) = "no"
     * truncate("abcdefghijklmno", 14, 1) = "o"
     * truncate("abcdefghijklmno", 14, Integer.MAX_VALUE) = "o"
     * truncate("abcdefghijklmno", 15, 1) = ""
     * truncate("abcdefghijklmno", 15, Integer.MAX_VALUE) = ""
     * truncate("abcdefghijklmno", Integer.MAX_VALUE, Integer.MAX_VALUE) = ""
     * truncate("abcdefghij", 3, -1) = "c"
     * truncate("abcdefghij", -2, 4) = "ij"
     * @param str 源数据
     * @param offset 位置
     * @param length 长度
     * @return
     */
    public static String truncate(final String str, final int offset, final int length) {
        if (str == null) {
            return null;
        }
        if (offset > str.length()) {
            return SYMBOL_EMPTY;
        }
        if (length == 0) {
            return SYMBOL_EMPTY;
        }

        if (offset >= 0 && length > 0) {
            if (str.length() > length) {
                final int ix = offset + length > str.length() ? str.length() : offset + length;
                return str.substring(offset, ix);
            }
            return str.substring(offset);

        } else if (offset > 0 && length < 0) {
            int start = offset + length;
            if (start < 0) {
                start = 0;
            }
            return str.substring(start, offset);

        } else if (offset < 0 && length > 0) {
            int strLength = str.length();
            int start = strLength + offset;
            if (strLength > length) {
                final int ix = start + length > strLength ? strLength : start + length;
                return str.substring(start, ix);
            }
            return str.substring(start);
        } else {
            int strLength = str.length();
            int end = strLength + offset;
            int newLength = end + length;
            if (newLength < 0) {
                newLength = 0;
            }
            return str.substring(newLength, end);
        }
    }

    /**
     * 去除空格
     *
     * @param str 源数据
     *            <pre>
     *                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                stripToEmpty(null)     = ""
     *                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                stripToEmpty("")       = ""
     *                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                stripToEmpty("   ")    = ""
     *                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                stripToEmpty("abc")    = "abc"
     *                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                stripToEmpty("  abc")  = "abc"
     *                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                stripToEmpty("abc  ")  = "abc"
     *                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                stripToEmpty(" abc ")  = "abc"
     *                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                stripToEmpty(" ab c ") = "ab c"
     *                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                </pre>
     * @return
     */
    public static String stripToEmpty(final String str) {
        return str == null ? SYMBOL_EMPTY : strip(str, null);
    }

    /**
     * 去除前后指定字符
     * <pre>
     * strip(null, *)          = null
     * strip("", *)            = ""
     * strip("abc", null)      = "abc"
     * strip("  abc", null)    = "abc"
     * strip("abc  ", null)    = "abc"
     * strip(" abc ", null)    = "abc"
     * strip("  abcyx", "xyz") = "  abc"
     * </pre>
     *
     * @param str        源数据
     * @param stripChars 指定字符
     * @return
     */
    public static String strip(final String str, final String stripChars) {
        if (isEmpty(str)) {
            return str;
        }
        final String newStr = stripStart(str, stripChars);
        return stripEnd(newStr, stripChars);
    }

    /**
     * 去除字符串后面指定字符
     * <pre>
     * stripEnd(null, *)          = null
     * stripEnd("", *)            = ""
     * stripEnd("abc", "")        = "abc"
     * stripEnd("abc", null)      = "abc"
     * stripEnd("  abc", null)    = "  abc"
     * stripEnd("abc  ", null)    = "abc"
     * stripEnd(" abc ", null)    = " abc"
     * stripEnd("  abcyx", "xyz") = "  abc"
     * stripEnd("120.00", ".0")   = "12"
     * </pre>
     *
     * @param str        源数据
     * @param stripChars 待去除字符
     * @return
     */
    public static String stripEnd(final String str, final String stripChars) {
        int end;
        if (str == null || (end = str.length()) == 0) {
            return str;
        }

        if (stripChars == null) {
            while (end != 0 && Character.isWhitespace(str.charAt(end - 1))) {
                end--;
            }
        } else if (stripChars.isEmpty()) {
            return str;
        } else {
            while (end != 0 && stripChars.indexOf(str.charAt(end - 1)) != -1) {
                end--;
            }
        }
        return str.substring(0, end);
    }

    /**
     * 去除字符串前面指定字符
     * <pre>
     * stripStart(null, *)          = null
     * stripStart("", *)            = ""
     * stripStart("abc", "")        = "abc"
     * stripStart("abc", null)      = "abc"
     * stripStart("  abc", null)    = "abc"
     * stripStart("abc  ", null)    = "abc  "
     * stripStart(" abc ", null)    = "abc "
     * stripStart("yxabc  ", "xyz") = "abc  "
     * </pre>
     *
     * @param str        源数据
     * @param stripChars 待去除字符
     * @return
     */
    public static String stripStart(final String str, final String stripChars) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        int start = 0;
        if (stripChars == null) {
            while (start != strLen && Character.isWhitespace(str.charAt(start))) {
                start++;
            }
        } else if (stripChars.isEmpty()) {
            return str;
        } else {
            while (start != strLen && stripChars.indexOf(str.charAt(start)) != INDEX_NOT_FOUND) {
                start++;
            }
        }
        return str.substring(start);
    }


    /**
     * 字符串编码转换的实现方法
     *
     * @param srcValue   待转换编码的字符串
     * @param newCharset 目标编码
     * @return
     * @throws UnsupportedEncodingException
     */
    public final static String changeCharset(String srcValue, String newCharset) throws UnsupportedEncodingException {
        if (srcValue != null) {
            byte[] bs = srcValue.getBytes(CharsetHelper.UTF_8);
            // 用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }

    /**
     * 字符串编码转换的实现方法
     *
     * @param srcValue   待转换编码的字符串
     * @param oldCharset 原编码
     * @param newCharset 目标编码
     * @return
     * @throws UnsupportedEncodingException
     */
    public final static String changeCharset(String srcValue, String oldCharset, String newCharset)
            throws UnsupportedEncodingException {
        if (srcValue != null) {
            // 用旧的字符编码解码字符串。解码可能会出现异常。
            byte[] bs = srcValue.getBytes(oldCharset);
            // 用新的字符编码生成字符串
            return new String(bs, newCharset);
        }
        return null;
    }

    /**
     * 获取默认值
     * <pre>
     *  getDefaultValue(null, "1") == "1"
     *  getDefaultValue("22", "1") == "22"
     * </pre>
     *
     * @param srcValue
     * @param defaultValue
     * @return
     */
    public static String getDefaultValue(final String srcValue, final String defaultValue) {
        return Strings.isNullOrEmpty(srcValue) ? defaultValue : srcValue;
    }

    /**
     * 占位符处理
     * <pre>
     * </pre>
     *
     * @param value 包含占位符的原始数据
     * @param data  待替换的原始数据
     * @param start 占位符开始符号
     * @param end   占位符结束符号
     * @return
     */
    public static String matcher(String value, final Map<String, Object> data, final String start, final String end) {
        if (Strings.isNullOrEmpty(value)) {
            return value;
        }
        int startIndex = value.indexOf(start);
        if (startIndex > -1 && value.indexOf(end, startIndex) > -1) {
            String patternStr = SYMBOL_RIGHT_SLASH + start + REGEXP_ANY + SYMBOL_RIGHT_SLASH + end;
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(value);
            while (matcher.find()) {
                String group = matcher.group();
                value = value.replace(group, MapOperableHelper.getString(data, group.replace(start, SYMBOL_EMPTY).replace(end, SYMBOL_EMPTY), SYMBOL_EMPTY));
            }

        }
        return value;
    }


    /**
     * 首尾是否包含指定符号
     *
     * @param srcValue 源数据
     * @param signs    符号
     * @return boolean
     */
    public static boolean firstAndEndContains(String srcValue, String... signs) {
        boolean isContains = true;
        if (Strings.isNullOrEmpty(srcValue) || null == signs || signs.length == 0) {
            return !isContains;
        }
        String first = srcValue.substring(0, 1);
        String end = srcValue.substring(srcValue.length() - 1);

        return ArraysHelper.contains(signs, first) && ArraysHelper.contains(signs, end);
    }

    /**
     * 首尾是否其中一个包含指定符号
     *
     * @param srcValue 源数据
     * @param signs    符号
     * @return boolean
     */
    public static boolean firstOrEndContains(String srcValue, String... signs) {
        boolean isContains = true;
        if (Strings.isNullOrEmpty(srcValue) || null == signs || signs.length == 0) {
            return !isContains;
        }
        String first = srcValue.substring(0, 1);
        String end = srcValue.substring(srcValue.length() - 1);

        return ArraysHelper.contains(signs, first) || ArraysHelper.contains(signs, end);
    }

    /**
     * 首字母小写
     *
     * @param srcValue 源数据
     * @return 首字母小写数据
     */
    public static String firstLowerCase(final String srcValue) {
        if (!hasLength(srcValue)) {
            return srcValue;
        }
        return first(srcValue).toLowerCase() + substring(srcValue, 1);
    }

    /**
     * 首字母大写
     * <pre>
     *     substring("test", 1) = "Test"
     *     substring(null) = ""
     * </pre>
     *
     * @param srcValue 源数据
     * @return 首字母大写数据
     */
    public static String firstUpperCase(final String srcValue) {
        if (!hasLength(srcValue)) {
            return srcValue;
        }
        return first(srcValue).toUpperCase() + substring(srcValue, 1);
    }

    /**
     * 从开始位置截取字符串
     * <pre>
     *     substring("test", 1) = "est"
     *     substring(null) = ""
     * </pre>
     *
     * @param srcValue 源数据
     * @param start    开始位置
     * @return
     */
    public static String substring(final String srcValue, final int start) {
        return hasLength(srcValue) ? srcValue.substring(start) : SYMBOL_EMPTY;
    }

    /**
     * 返回截取符之前数据
     * <pre>
     * substringBefore(null, *)      = null
     * substringBefore("", *)        = ""
     * substringBefore("abc", "a")   = ""
     * substringBefore("abcba", "b") = "a"
     * substringBefore("abc", "c")   = "ab"
     * substringBefore("abc", "d")   = "abc"
     * substringBefore("abc", "")    = ""
     * substringBefore("abc", null)  = "abc"
     * </pre>
     *
     * @param str       源数据
     * @param separator 分隔符
     * @return
     */
    public static String substringBefore(String str, String separator) {
        if (isEmpty(str) || separator == null) {
            return str;
        }
        if (separator.length() == 0) {
            return SYMBOL_EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * 返回截取符之后数据
     * <pre>
     * substringAfter(null, *)      = null
     * substringAfter("", *)        = ""
     * substringAfter("abc", "a")   = ""
     * substringAfter("abcba", "b") = "a"
     * substringAfter("abc", "c")   = "ab"
     * substringAfter("abc", "d")   = "abc"
     * substringAfter("abc", "")    = ""
     * substringAfter("abc", null)  = "abc"
     * </pre>
     *
     * @param str       源数据
     * @param separator 分隔符
     * @return
     */
    public static String substringAfter(String str, String separator) {
        if ((str == null) || (str.length() == 0)) {
            return str;
        }
        if (separator == null) {
            return SYMBOL_EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos < 0) {
            return SYMBOL_EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    /**
     * 从开始位置到结束位置截取字符串
     * <pre>
     *     substring("test", 1, 2) = "e"
     *     substring("test ", 2, 1) = ""
     *     substring(null) = ""
     * </pre>
     *
     * @param srcValue 源数据
     * @param start    开始位置
     * @param end      结束位置
     * @return
     */
    public static String substring(String srcValue, int start, int end) {
        return hasLength(srcValue) && start < end ? srcValue.substring(start, end) : SYMBOL_EMPTY;
    }

    /**
     * 获取首字母
     * <pre>
     *     first("test") = "t"
     *     first("test ") = "t"
     *     first(" test ") = ""
     *     first(" te st ") = ""
     *     first(null) = ""
     * </pre>
     *
     * @param value
     * @return
     */
    public static String first(String value) {
        return hasLength(value) ? value.substring(0, 1) : SYMBOL_EMPTY;
    }

    /**
     * 判断字符串是否有长度
     * <pre>
     *     hasLength("") = false;
     *     hasLength("   ") = false;
     *     hasLength("1") = true;
     * </pre>
     *
     * @param source 源数据
     * @return
     */
    public static boolean hasLength(String source) {
        return !isEmpty(source) && trim(source).length() > 0;
    }

    /**
     * 判断字符串是否有长度
     * <pre>
     *     hasNoneLength("") = true;
     *     hasNoneLength("   ") = true;
     *     hasNoneLength("1") = false;
     * </pre>
     *
     * @param source 源数据
     * @return
     */
    public static boolean hasNoneLength(String source) {
        return !hasLength(source);
    }


    /**
     * 去除所有空格
     * <pre>
     *     trimAllWhitespace("test") = "test"
     *     trimAllWhitespace("test ") = "test"
     *     trimAllWhitespace(" test ") = "test"
     *     trimAllWhitespace(" te st ") = "test"
     *     trimAllWhitespace(null) = null
     * </pre>
     *
     * @param source 原始数据
     * @return
     */
    public static String trimAllWhitespace(String source) {
        if (!hasLength(source)) {
            return source;
        } else {
            int len = source.length();
            StringBuilder sb = new StringBuilder(source.length());
            for (int i = 0; i < len; ++i) {
                char c = source.charAt(i);
                if (!Character.isWhitespace(c)) {
                    sb.append(c);
                }
            }
            return sb.toString();
        }
    }

    /**
     * 默认逗号分隔的字符串
     *
     * @param strings 字符串数组
     * @return 按分隔符分隔的字符串
     */
    public static String joinWithComma(String... strings) {
        return join(strings, ",");
    }

    /**
     * 连接字符串数组
     *
     * @param strings   字符串数组
     * @param separator 分隔符
     * @return 按分隔符分隔的字符串
     */
    public static String join(String[] strings, String separator) {
        if (strings == null || strings.length == 0) {
            return SYMBOL_EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            if (!isEmpty(string)) {
                sb.append(string).append(separator);
            }
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - separator.length()) : SYMBOL_EMPTY;
    }

    /**
     * 数组转十六进制
     *
     * @param source 数据源
     * @return
     */
    public static String toHexString(byte[] source) {
        if (null == source) {
            return SYMBOL_EMPTY;
        }
        String strHex = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < source.length; n++) {
            strHex = Integer.toHexString(source[n] & 0xFF);
            // 每个字节由两个字符表示，位数不够，高位补0
            sb.append((strHex.length() == 1) ? "0" + strHex : strHex);
        }
        return sb.toString().trim();
    }

    /**
     * 数组转十六进制
     *
     * @param hex 数据源
     * @return
     */
    public static byte[] fromHexString(String hex) {
        if (null == hex) {
            return new byte[0];
        }
        int m = 0, n = 0;
        // 每两个字符描述一个字节
        int byteLen = hex.length() / 2;
        byte[] ret = new byte[byteLen];
        for (int i = 0; i < byteLen; i++) {
            m = i * 2 + 1;
            n = m + 1;
            int intVal = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n));
            ret[i] = Byte.valueOf((byte) intVal);
        }
        return ret;
    }

    /**
     * 是否包含通配符
     *
     * @param source 数据
     * @return
     */
    public static boolean isWildcardMatch(final String source) {
        return null != source && (source.indexOf("?") > -1 || source.indexOf("*") > -1);
    }

    /**
     * 获取分隔符之后数据
     *
     * @param source    数据
     * @param delimiter 分隔符
     * @return
     */
    public static String getAfterDelimiter(String source, String delimiter) {
        if (Strings.isNullOrEmpty(source) || Strings.isNullOrEmpty(delimiter)) {
            return source;
        }
        int index = source.indexOf(delimiter);
        return -1 == index ? source : source.substring(index + 1);
    }

    /**
     * 获取最后一个分隔符之后数据
     *
     * @param source    数据
     * @param delimiter 分隔符
     * @return
     */
    public static String getAfterLastDelimiter(String source, String delimiter) {
        if (Strings.isNullOrEmpty(source) || Strings.isNullOrEmpty(delimiter)) {
            return source;
        }
        int index = source.lastIndexOf(delimiter);
        return -1 == index ? source : source.substring(index + 1);
    }

    /**
     * 获取最后一个分隔符之前数据
     *
     * @param source    数据
     * @param delimiter 分隔符
     * @return
     */
    public static String getBeforeLastDelimiter(String source, String delimiter) {
        if (Strings.isNullOrEmpty(source) || Strings.isNullOrEmpty(delimiter)) {
            return source;
        }
        int index = source.lastIndexOf(delimiter);
        return -1 == index ? source : source.substring(0, index);
    }

    /**
     * 获取分隔符之前数据
     *
     * @param source    数据
     * @param delimiter 分隔符
     * @return
     */
    public static String getBeforeDelimiter(String source, String delimiter) {
        if (Strings.isNullOrEmpty(source) || Strings.isNullOrEmpty(delimiter)) {
            return source;
        }
        int index = source.indexOf(delimiter);
        return -1 == index ? source : source.substring(0, index);
    }

    /**
     * 获取分隔符之间数据
     *
     * @param source         数据
     * @param firstDelimiter 分隔符
     * @return
     */
    public static String getBetweenDelimiter(String source, String firstDelimiter, String lastDelimiter) {
        if (isNullOrEmpty(source, firstDelimiter, lastDelimiter)) {
            return source;
        }
        String afterDelimiter = getAfterDelimiter(source, firstDelimiter);
        return getBeforeDelimiter(afterDelimiter, lastDelimiter);
    }


    /**
     * 逗号分隔列表到字符串数组
     *
     * @param str 字符串
     * @return String[]
     */
    public static String[] commaDelimitedListToStringArray(String str) {
        return delimitedListToStringArray(str, ",");
    }

    /**
     * 字符串转ascii
     *
     * @param source 数据
     * @return ascii字符串
     */
    public static String stringToAscii(String source) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = source.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append((int) chars[i]).append(",");
            } else {
                sbu.append((int) chars[i]);
            }
        }
        return sbu.toString();
    }

    /**
     * ascii转字符串
     *
     * @param ascii ascii数据
     * @return 字符串
     */
    public static String asciiToString(String ascii) {
        StringBuffer sbu = new StringBuffer();
        String[] chars = ascii.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }

    /**
     * 字符串不为空
     *
     * @param source 数据
     * @return boolean
     * @see #isEmpty(String)
     */
    public static boolean isNotBlank(String source) {
        return !isEmpty(source);
    }

    /**
     * 字符串为空
     *
     * @param source 数据
     * @return boolean
     * @see #isEmpty(String)
     */
    public static boolean isBlank(String source) {
        return isEmpty(source);
    }
}
