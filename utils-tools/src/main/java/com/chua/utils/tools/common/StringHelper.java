package com.chua.utils.tools.common;

import com.chua.utils.tools.function.IPreMatcher;
import com.chua.utils.tools.guid.GUID;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.chua.utils.tools.constant.NumberConstant.INDEX_NOT_FOUND;
import static com.chua.utils.tools.constant.PatternConstant.PATTERN_BLANK;
import static com.chua.utils.tools.constant.StringConstant.*;

/**
 * 字符串工具类
 *
 * @author CH
 */
public class StringHelper {
    /**
     * 添加单引号
     * <pre>
     *     StringHelper.quote("test") = 'test'
     *     StringHelper.quote(null) = null
     * </pre>
     *
     * @param source
     * @return
     */
    public static String quote(String source) {
        return source != null ? "'" + source + "'" : null;
    }

    /**
     * 如果是字符串添加单引号
     * <pre>
     *     StringHelper.quoteIfString("test") = 'test'
     *     StringHelper.quoteIfString(null) = null
     *     StringHelper.quoteIfString(1) = 1
     * </pre>
     *
     * @param source
     * @return
     */
    public static Object quoteIfString(Object source) {
        return source instanceof String ? quote((String) source) : source;
    }

    /**
     * 获取.后部分
     * <pre>
     *     StringHelper.unqualify("", '.') = ""
     *     StringHelper.unqualify("test", '.') = ""
     *     StringHelper.unqualify("test.jpg", '.') = "jpg"
     * </pre>
     *
     * @param source 原始数据
     * @return
     */
    public static String unqualify(String source) {
        return unqualify(source, '.');
    }

    /**
     * 获取【separator】后部分
     * <pre>
     *     StringHelper.unqualify("", '.') = ""
     *     StringHelper.unqualify("test", '.') = ""
     *     StringHelper.unqualify("test.jpg", '.') = "jpg"
     * </pre>
     *
     * @param source    原始数据
     * @param separator 分隔符
     * @return
     */
    public static String unqualify(String source, char separator) {
        if (isBlank(source)) {
            return null;
        }
        final int index = source.indexOf(separator);
        return index > -1 ? source.substring(source.lastIndexOf(separator) + 1) : "";
    }

    /**
     * 字符串模式匹配
     *
     * @param srcValue 字符串
     * @param model    匹配模式
     * @return
     */
    public static List<String> matcher(final String srcValue, final String model) {
        List<String> matches = new ArrayList<>();
        if (isAnyNotBlank(srcValue, model)) {
            StringBuffer sb = new StringBuffer();
            if (StringHelper.contains(model, "*", "?")) {
                char[] chars = model.toCharArray();
                for (char aChar : chars) {
                    if (aChar == '*') {
                        sb.append(".*");
                    } else if (aChar == '?') {
                        sb.append(".?");
                    } else {
                        sb.append(aChar);
                    }
                }
                final Pattern compile = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
                Matcher matcher = compile.matcher(srcValue);
                while (matcher.find()) {
                    String group = matcher.group();
                    matches.add(group);
                }
            } else {
                if (model.equals(srcValue)) {
                    matches.add(srcValue);
                }
            }
        }
        return matches;
    }

    /**
     * 是否包含其它字符
     * <pre>
     *     StringHelper.contains("test*", "?") = false;
     *     StringHelper.contains("test*", "?", "*") = true;
     * </pre>
     *
     * @param value 数据
     * @param signs 符号
     * @return
     */
    public static boolean contains(final String value, final String... signs) {
        if (isBlank(value)) {
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
     * @return
     */
    public static boolean contains(final String value, final char signs) {
        if (isNotBlank(value)) {
            return value.indexOf(signs) > -1;
        }
        return false;
    }

    /**
     * 是否包含其它字符
     *
     * @param value 数据
     * @param signs 符号
     * @return 第一个满足条件的位置
     */
    public static int indexOf(final String value, String signs) {
        if (isBlank(value)) {
            return -1;
        }
        return value.indexOf(signs);
    }

    /**
     * 获取非分隔符部分数据
     * <pre>
     *     StringHelper.lastIndexOfAndSplit("", "_") = ""
     *     StringHelper.lastIndexOfAndSplit("test_1", "") = "test_1"
     *     StringHelper.lastIndexOfAndSplit("test_1", null) = "test_1"
     *     StringHelper.lastIndexOfAndSplit("test_1", "_") = "test"
     *     StringHelper.lastIndexOfAndSplit("test_1_2", "_") = "test_1"
     * </pre>
     *
     * @param value 数据
     * @param split 符号
     * @return 第一个满足条件的位置
     */
    public static String lastIndexOfAndSplit(final String value, final String split) {
        if (isBlank(split)) {
            return value;
        }
        if (isBlank(value)) {
            return value;
        }

        final int index = value.lastIndexOf(split);
        return index > -1 ? value.substring(0, index) : value;
    }

    /**
     * 获取非分隔符部分数据
     * <pre>
     *     StringHelper.firstIndexOfAndSplit("", "_") = ""
     *     StringHelper.firstIndexOfAndSplit("test_1", "") = "test_1"
     *     StringHelper.firstIndexOfAndSplit("test_1", null) = "test_1"
     *     StringHelper.firstIndexOfAndSplit("test_1", "_") = "test"
     *     StringHelper.firstIndexOfAndSplit("test_1_2", "_") = "test"
     * </pre>
     *
     * @param value 数据
     * @param split 符号
     * @return 第一个满足条件的位置
     */
    public static String firstIndexOfAndSplit(final String value, final String split) {
        if (isBlank(split)) {
            return value;
        }
        if (isBlank(value)) {
            return value;
        }

        final int index = value.indexOf(split);
        return index > -1 ? value.substring(0, index) : value;
    }

    /**
     * 空默认值
     * <pre>
     * StringHelper.defaultIfBlank(null, "NULL")  = "NULL"
     * StringHelper.defaultIfBlank("", "NULL")    = "NULL"
     * StringHelper.defaultIfBlank(" ", "NULL")   = "NULL"
     * StringHelper.defaultIfBlank("bat", "NULL") = "bat"
     * StringHelper.defaultIfBlank("", null)      = null
     * </pre>
     *
     * @param source     源数据
     * @param defaultStr 默认值
     * @return
     */
    public static String defaultIfBlank(final Object source, final String defaultStr) {
        return null == source ? defaultStr : (isBlank(source.toString()) ? defaultStr : source.toString());
    }

    /**
     * 空默认值
     * <pre>
     * StringHelper.defaultIfObjectBlank(null, "NULL")  = "NULL"
     * StringHelper.defaultIfObjectBlank("", "NULL")    = "NULL"
     * StringHelper.defaultIfObjectBlank(" ", "NULL")   = "NULL"
     * StringHelper.defaultIfObjectBlank("bat", "NULL") = "bat"
     * StringHelper.defaultIfObjectBlank("", null)      = null
     * </pre>
     *
     * @param source     源数据
     * @param defaultStr 默认值
     * @return
     */
    public static String defaultIfObjectBlank(final String source, final Object defaultStr) {
        if (isEmpty(source)) {
            if (null == defaultStr) {
                return null;
            }
            return defaultStr.toString();
        }
        return source;
    }

    /**
     * 空默认值
     * <pre>
     * StringHelper.defaultIfEmpty(null, "NULL")  = "NULL"
     * StringHelper.defaultIfEmpty("", "NULL")    = "NULL"
     * StringHelper.defaultIfEmpty(" ", "NULL")   = " "
     * StringHelper.defaultIfEmpty("bat", "NULL") = "bat"
     * StringHelper.defaultIfEmpty("", null)      = null
     * </pre>
     *
     * @param source     源数据
     * @param defaultStr 默认值
     * @param <T>
     * @return
     */
    public static <T extends CharSequence> T defaultIfEmpty(final T source, final T defaultStr) {
        return isEmpty(source) ? defaultStr : source;
    }

    /**
     * 翻转
     * <pre>
     * StringHelper.reverse(null)  = null
     * StringHelper.reverse("")    = ""
     * StringHelper.reverse("bat") = "tab"
     * </pre>
     *
     * @param source 源数据
     * @return
     */
    public static String reverse(final String source) {
        if (source == null) {
            return null;
        }
        return new StringBuilder(source).reverse().toString();
    }

    /**
     * 数据交集
     * <pre>
     * StringHelper.indexOfDifference(null, null) = -1
     * StringHelper.indexOfDifference("", "") = -1
     * StringHelper.indexOfDifference("", "abc") = 0
     * StringHelper.indexOfDifference("abc", "") = 0
     * StringHelper.indexOfDifference("abc", "abc") = -1
     * StringHelper.indexOfDifference("ab", "abxyz") = 2
     * StringHelper.indexOfDifference("abcde", "abxyz") = 2
     * StringHelper.indexOfDifference("abcde", "xyz") = 0
     * </pre>
     *
     * @param source  比较集
     * @param source1 对比集
     * @return
     */
    public static int indexOfDifference(final CharSequence source, final CharSequence source1) {
        if (source == source1) {
            return INDEX_NOT_FOUND;
        }
        if (source == null || source1 == null) {
            return 0;
        }
        int i;
        for (i = 0; i < source.length() && i < source1.length(); ++i) {
            if (source.charAt(i) != source1.charAt(i)) {
                break;
            }
        }
        if (i < source1.length() || i < source.length()) {
            return i;
        }
        return INDEX_NOT_FOUND;
    }

    /**
     * kmp模式匹配（不支持通配符）
     *
     * @param srcValue 主串
     * @param model    模式串
     * @return
     */
    public static int kmp(final String srcValue, final String model) {
        if (isAnyNotBlank(srcValue, model)) {
            char[] srcChars = srcValue.toCharArray();
            char[] modelChars = model.toCharArray();

            int[] next = next(modelChars);
            int i = 0, j = 0;
            while (i < srcChars.length && j < modelChars.length) {
                if (j == -1 || srcChars[i] == modelChars[j]) {
                    i++;
                    j++;
                } else {
                    j = next[j];
                }
            }

            if (j < modelChars.length) {
                return -1;
            } else {
                return i - modelChars.length;
            }
        }
        return -1;
    }

    /**
     * 获取next函数
     *
     * @param model 模式串
     * @return
     */
    private static int[] next(final char[] model) {
        int[] ints = new int[model.length];
        ints[0] = -1;
        int i = 0;
        int j = -1;
        while (i < model.length - 1) {
            if (j == -1 || model[i] == model[j]) {
                i++;
                j++;
                ints[i] = j;
            } else {
                j = ints[j];
            }
        }

        return ints;
    }

    /**
     * @param javaClassPathProperty
     * @param property
     * @return
     */
    public static String[] delimitedListToStringArray(String javaClassPathProperty, String property) {
        return delimitedListToStringArray(javaClassPathProperty, property, null);
    }

    /**
     * @param str
     * @param delimiter
     * @param charsToDelete
     * @return
     */
    public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {

        if (str == null) {
            return new String[0];
        }
        if (delimiter == null) {
            return new String[]{str};
        }

        List<String> result = new ArrayList<>();
        if (delimiter.isEmpty()) {
            for (int i = 0; i < str.length(); i++) {
                result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
            }
        } else {
            int pos = 0;
            int delPos;
            while ((delPos = str.indexOf(delimiter, pos)) != -1) {
                result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
                pos = delPos + delimiter.length();
            }
            if (str.length() > 0 && pos <= str.length()) {
                // Add rest of String, but not in case of empty input.
                result.add(deleteAny(str.substring(pos), charsToDelete));
            }
        }
        return toStringArray(result);
    }

    /**
     * list to string[]
     *
     * @param collection
     * @return
     */
    public static String[] toStringArray(Collection<String> collection) {
        return (collection != null ? collection.toArray(new String[0]) : new String[0]);
    }

    /**
     * <pre>
     *     StringHelper.capitalize("11", 1) = ""
     *     StringHelper.capitalize("t1", "") = "t1"
     *     StringHelper.capitalize(null, "1") = null
     *     StringHelper.capitalize("//", "1") = "//"
     *     StringHelper.capitalize("T1", "1") = "T"
     * </pre>
     *
     * @param source        源数据
     * @param charsToDelete 待删除字符
     * @return
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
     *     StringHelper.capitalize("11", true) = "11"
     *     StringHelper.capitalize("t1", true) = "T1"
     *     StringHelper.capitalize(null, true) = null
     *     StringHelper.capitalize("//", true) = "//"
     *     StringHelper.capitalize("T1", false) = "t1"
     * </pre>
     *
     * @param str
     * @param capitalize true:大写, false: 小写
     * @return
     */
    public static String changeFirstCharacterCase(String str, boolean capitalize) {
        if (hasNoneLength(str)) {
            return str;
        }

        char baseChar = str.charAt(0);
        char updatedChar;
        if (capitalize) {
            updatedChar = Character.toUpperCase(baseChar);
        } else {
            updatedChar = Character.toLowerCase(baseChar);
        }
        if (baseChar == updatedChar) {
            return str;
        }

        char[] chars = str.toCharArray();
        chars[0] = updatedChar;
        return new String(chars, 0, chars.length);
    }

    /**
     * 创建对象
     *
     * @param source 原始数据
     * @return
     */
    public static Long toLong(final String source) {
        if (source == null) {
            return null;
        }
        return Long.decode(source);
    }

    /**
     * 创建对象
     *
     * @param source 原始数据
     * @return
     */
    public static Integer toInteger(final String source) {
        if (source == null) {
            return null;
        }
        return Integer.decode(source);
    }

    /**
     * 创建对象
     *
     * @param source 原始数据
     * @return
     */
    public static Short toShort(final String source) {
        if (source == null) {
            return null;
        }
        return Short.decode(source);
    }

    /**
     * 创建对象
     *
     * @param source 原始数据
     * @return
     */
    public static Double createDouble(final String source) {
        if (source == null) {
            return null;
        }
        return Double.valueOf(source);
    }

    /**
     * 创建对象
     *
     * @param source 原始数据
     * @return
     */
    public static Float createFloat(final String source) {
        if (source == null) {
            return null;
        }
        return Float.valueOf(source);
    }

    /**
     * 创建对象
     *
     * @param source 原始数据
     * @return
     */
    public static BigDecimal createBigDecimal(final String source) {
        if (source == null) {
            return null;
        }
        if (StringHelper.isBlank(source)) {
            throw new NumberFormatException("A blank string is not a valid number");
        }
        if (source.trim().startsWith("--")) {
            throw new NumberFormatException(source + " is not a valid number.");
        }
        return new BigDecimal(source);
    }

    /**
     * 第一个字符大写
     * <pre>
     *     StringHelper.capitalize("11") = "11"
     *     StringHelper.capitalize("t1") = "T1"
     *     StringHelper.capitalize(null) = null
     *     StringHelper.capitalize("//") = "/"
     * </pre>
     *
     * @param str
     * @return
     */
    public static String capitalize(String str) {
        return changeFirstCharacterCase(str, true);
    }

    /**
     * 删除//
     * <pre>
     *     StringHelper.noRepeat("11") = "11"
     *     StringHelper.noRepeat(null) = null
     *     StringHelper.noRepeat("//") = "/"
     * </pre>
     *
     * @param source 原始数据
     * @return
     */
    public static String noRepeatSlash(String source) {
        return isBlank(source) ? source : source.replaceAll("(/){1,}", EXTENSION_LEFT_SLASH);
    }

    /**
     * 删除重复
     * <pre>
     *     StringHelper.noRepeat("11") = "1"
     *     StringHelper.noRepeat(null) = null
     *     StringHelper.noRepeat("//") = "/"
     * </pre>
     *
     * @param source 原始数据
     * @return
     */
    public static String noRepeat(String source) {
        if (isBlank(source)) {
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
     * @return
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
     * 比较两个字符串的相识度
     * 核心算法：用一个二维数组记录每个字符串是否相同，如果相同记为0，不相同记为1，每行每列相同个数累加
     * 则数组最后一个数为不相同的总数，从而判断这两个字符的相识度
     *
     * @param str
     * @param target
     * @return
     */
    public static int compare(String str, String target) {
        int[][] d;              // 矩阵
        int n = str.length();
        int m = target.length();
        int i;                  // 遍历str的
        int j;                  // 遍历target的
        char ch1;               // str的
        char ch2;               // target的
        int temp;               // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];
        // 初始化第一列
        for (i = 0; i <= n; i++) {
            d[i][0] = i;
        }
        // 初始化第一行
        for (j = 0; j <= m; j++) {
            d[0][j] = j;
        }
        for (i = 1; i <= n; i++) {
            // 遍历str
            ch1 = str.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++) {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2 || ch1 == ch2 + 32 || ch1 + 32 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }


    /**
     * 获取最小的值
     */
    public static int min(int one, int two, int three) {
        return (one = one < two ? one : two) < three ? one : three;
    }

    /**
     * jdk uuid
     *
     * @param b
     * @return
     */
    public static String uuid(boolean b) {
        String s = UUID.randomUUID().toString();
        return b ? s : s.replace(EXTENSION_MINS, EXTENSION_EMPTY);
    }

    /**
     * jdk uuid
     *
     * @return
     */
    public static String uuid() {
        return uuid(false);
    }
    /**
     * jdk uuid
     *
     * @return
     */
    public static String guid() {
        return GUID.randomGUID().toString();
    }

    /**
     * 随机
     *
     * @param defaultGroupName
     * @return
     */
    public static String radon(String defaultGroupName) {
        return UUID.randomUUID().toString() + "-" + defaultGroupName;
    }

    /**
     * 计算
     * <pre>
     *     StringHelper.calcString(1, 4) = 0.250
     *     StringHelper.calcString(1, 1) = 1.000
     * </pre>
     *
     * @param number  数量1
     * @param number2 数量2
     * @return
     */
    public static String calcString(long number, long number2) {
        float perNumber = (float) number / (float) number2;
        return DECIMAL_FORMAT.format(perNumber);
    }

    /**
     * 计算
     * <pre>
     *     StringHelper.calcString(1, 4) = 0.250
     *     StringHelper.calcString(1, 1) = 1.000
     * </pre>
     *
     * @param number  数量1
     * @param number2 数量2
     * @return
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
     * @return
     */
    public static String split(String key, String split, int index) {
        if (isBlank(key)) {
            return EXTENSION_EMPTY;
        }
        String[] split1 = key.split("#");
        index = index + 1;
        int max = split1.length;
        index = index > max ? max : index;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < index; i++) {
            sb.append("#").append(split1[i]);
        }
        return sb.length() > 0 ? sb.substring(1) : EXTENSION_EMPTY;
    }

    /**
     * 格式化信息
     * <pre>
     *     StringHelper.formatMessage("test{}, {}, {}", "sdsds", 1) = testsdsds, 1, {}
     *     StringHelper.formatMessage("test{}, {}", "sdsds", 1, 2) = testsdsds, 1
     * </pre>
     *
     * @param message 数据包含占位符
     * @param params  替换数据
     * @return
     */
    public static String formatMessage(String message, Object... params) {
        Matcher matcher = PLACE_HOLDER.matcher(message);
        int cnt = 0;
        int length = params.length;
        while (matcher.find()) {
            if (cnt < length) {
                Object obj = params[cnt++];
                message = matcher.replaceFirst(null == obj ? "" : String.valueOf(obj));
                matcher = PLACE_HOLDER.matcher(message);
            } else {
                break;
            }
        }
        return message;
    }

    /**
     * 默认值渲染
     *
     * @param defaultValue    默认值
     * @param replaceNonExist 不存在占位符是否置为""
     * @param matcher         正则
     * @param key1            索引
     * @return
     */
    private static String defaultRender(String defaultValue, boolean replaceNonExist, Matcher matcher, String key1) {
        if (StringHelper.isBlank(defaultValue)) {
            if (replaceNonExist) {
                return matcher.replaceFirst("");
            } else {
                return matcher.replaceFirst("€" + key1 + "￥");
            }
        }
        return matcher.replaceFirst(defaultValue);
    }

    /**
     * join.
     * <pre>
     *     StringHelper.join([1,2,3], "_") = "1_2_3"
     *     StringHelper.join([], "_") = ""
     *     StringHelper.join(null, "_") = ""
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
     *     StringHelper.join({1:1}, "=", "&") = "1=1"
     *     StringHelper.join({1: 1, 2: 2}, "=", "&") = "1=1&2=2"
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

        return stringBuilder.toString().substring(itemDelimiter.length());
    }

    /**
     * join.
     * <pre>
     *     StringHelper.join([1,2,3], "_") = "1_2_3"
     *     StringHelper.join([], "_") = ""
     *     StringHelper.join(null, "_") = ""
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
     * 解析key-value字符串
     *
     * @param str           字符串.
     * @param itemSeparator 分隔符.
     * @return key-value map;
     */
    private static Map<String, String> parseKeyValuePair(String str, String itemSeparator) {
        String[] tmp = str.split(itemSeparator);
        Map<String, String> map = new HashMap<>(tmp.length);
        for (int i = 0; i < tmp.length; i++) {
            Matcher matcher = KVP_PATTERN.matcher(tmp[i]);
            if (matcher.matches() == false) {
                continue;
            }
            map.put(matcher.group(1), matcher.group(2));
        }
        return map;
    }

    /**
     * 是否全部不为空
     *
     * @param values
     * @return
     */
    public static boolean isAllNotEmpty(String... values) {
        if (BooleanHelper.hasLength(values)) {
            for (String value : values) {
                if (isEmpty(value)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 获取 separator 后面字符
     * <p>
     * StringHelper.subAndEndstring(null, null, "/") = ""
     * StringHelper.subAndEndstring("test", null, null) = ""
     * StringHelper.subAndEndstring(null, "/", "/") = ""
     * StringHelper.subAndEndstring("test/", "/", "/") = ""
     * StringHelper.subAndEndstring("/test/@", "/", "@") = "test/"
     * </p>
     *
     * @param source       源数据
     * @param subSeparator 分隔符
     * @param endSeparator 分隔符
     * @return
     */
    public static String subAndEndstring(String source, String subSeparator, String endSeparator) {
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
     * StringHelper.substring(null, null) = ""
     * StringHelper.substring("test", null) = ""
     * StringHelper.substring(null, "/") = ""
     * StringHelper.substring("test/", "/") = ""
     * StringHelper.substring("/test", "/") = "test"
     * StringHelper.substring("/test", "@") = "/test"
     * </p>
     *
     * @param source    源数据
     * @param separator 分隔符
     * @return
     */
    public static String substring(String source, String separator) {
        return substring(source, separator, "");
    }

    /**
     * 获取 separator 后面字符
     * <p>
     * StringHelper.substring(null, null, "*") = ""
     * StringHelper.substring("test", null, "*") = ""
     * StringHelper.substring(null, "/", "*") = ""
     * StringHelper.substring("test/", "/", "*") = "*"
     * StringHelper.substring("/test", "/", "*") = "*test"
     * StringHelper.substring("/test", "@", "*") = "/test"
     * </p>
     *
     * @param source    源数据
     * @param separator 分隔符
     * @return
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
     * StringHelper.lastSubstring(null, null) = ""
     * StringHelper.lastSubstring("test", null) = ""
     * StringHelper.lastSubstring(null, "/") = ""
     * StringHelper.lastSubstring("test/", "/") = ""
     * StringHelper.lastSubstring("/test/", "/") = ""
     * StringHelper.lastSubstring("/test", "@") = "/test"
     * StringHelper.lastSubstring("/tes/t", "/") = "t"
     * </p>
     *
     * @param source    源数据
     * @param separator 分隔符
     * @return
     */
    public static String lastSubstring(String source, String separator) {
        return lastSubstring(source, separator, "");
    }

    /**
     * 获取 separator 后面字符
     * <p>
     * StringHelper.lastSubstring(null, null, "*") = ""
     * StringHelper.lastSubstring("test", null, "*") = ""
     * StringHelper.lastSubstring(null, "/", "*") = ""
     * StringHelper.lastSubstring("test/", "/", "*") = ""
     * StringHelper.lastSubstring("/test/", "/", "*") = "*"
     * StringHelper.lastSubstring("/test", "@", "*") = "/test"
     * StringHelper.lastSubstring("/tes/t", "/", "*") = "*t"
     * </p>
     *
     * @param source    源数据
     * @param separator 分隔符
     * @return
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
     * StringHelper.endstring(null, null) = ""
     * StringHelper.endstring("test", null) = ""
     * StringHelper.endstring(null, "/") = ""
     * StringHelper.endstring("test/", "/") = "test"
     * StringHelper.endstring("/test", "/") = ""
     * StringHelper.endstring("/test", "@") = "/test"
     * </p>
     *
     * @param source    源数据
     * @param separator 分隔符
     * @return
     */
    public static String endstring(String source, String separator) {
        return endstring(source, separator, "");
    }

    /**
     * 获取 separator 前面字符
     * <p>
     * StringHelper.endstring(null, null, "*") = ""
     * StringHelper.endstring("test", null, "*") = ""
     * StringHelper.endstring(null, "/", "*") = ""
     * StringHelper.endstring("test/", "/", "*") = "test*"
     * StringHelper.endstring("/test", "/", "*") = "*"
     * StringHelper.endstring("/test", "@", "*") = "/test"
     * </p>
     *
     * @param source    源数据
     * @param separator 分隔符
     * @return
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
     * StringHelper.lastEndstring(null, null) = ""
     * StringHelper.lastEndstring("test", null) = ""
     * StringHelper.lastEndstring(null, "/") = ""
     * StringHelper.lastEndstring("test/", "/") = "test"
     * StringHelper.lastEndstring("/test/", "/") = "/test"
     * StringHelper.lastEndstring("/test", "@") = ""
     * StringHelper.lastEndstring("/tes/t", "/") = "/tes"
     * </p>
     *
     * @param source    源数据
     * @param separator 分隔符
     * @return
     */
    public static String lastEndstring(String source, String separator) {
        return lastSubstring(source, separator, "");
    }

    /**
     * 获取 separator 前面字符
     * <p>
     * StringHelper.lastEndstring(null, null, "*") = ""
     * StringHelper.lastEndstring("test", null, "*") = ""
     * StringHelper.lastEndstring(null, "/", "*") = ""
     * StringHelper.lastEndstring("test/", "/", "*") = "test*"
     * StringHelper.lastEndstring("/test/", "/", "*") = "/test*"
     * StringHelper.lastEndstring("/test", "@", "*") = ""
     * StringHelper.lastEndstring("/tes/t", "/", "*") = "/tes*"
     * </p>
     *
     * @param source    源数据
     * @param separator 分隔符
     * @return
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
        if (StringHelper.isBlank(source)) {
            return source;
        }
        String sourceStr = source.toLowerCase();
        Matcher matcher = LINE_PATTERN.matcher(sourceStr);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
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
        if (isBlank(source)) {
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
        if (isBlank(source)) {
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
        if (isBlank(source)) {
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
        if (isBlank(source)) {
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
     * StringHelper.deleteWhitespace(null)         = null
     * StringHelper.deleteWhitespace("")           = ""
     * StringHelper.deleteWhitespace("abc")        = "abc"
     * StringHelper.deleteWhitespace("   ab  c  ") = "abc"
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
     * StringHelper.deleteWhitespace(null)         = null
     * StringHelper.deleteWhitespace("")           = ""
     * StringHelper.deleteWhitespace("abc")        = "abc"
     * StringHelper.deleteWhitespace("   ab  c  ") = "abc"
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
        if (StringHelper.isBlank(wildcard)) {
            return wildcard;
        }

        String newWildcard = wildcard;
        newWildcard = newWildcard.replace('.', '#');
        newWildcard = newWildcard.replaceAll("#", "\\\\.");
        newWildcard = newWildcard.replace('*', '#');
        newWildcard = newWildcard.replaceAll("#", ".*");
        newWildcard = newWildcard.replace('?', '#');
        newWildcard = newWildcard.replaceAll("#", ".?");

        return "^" + newWildcard + "$";
    }

    /**
     * 空默认值
     * <pre>
     * StringHelper.ifNull(null, "NULL")  = "NULL"
     * StringHelper.ifNull("", "NULL")    = "NULL"
     * StringHelper.ifNull(" ", "NULL")   = " "
     * StringHelper.ifNull("bat", "NULL") = "bat"
     * StringHelper.ifNull("", null)      = null
     * </pre>
     *
     * @param source     源数据
     * @param defaultStr 默认值
     * @return
     */
    public static String ifNull(String source, String defaultStr) {
        return defaultIfEmpty(source, defaultStr);
    }

    /**
     * 获取启动pid
     *
     * @return
     */
    public static String getPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return name.split("@")[0];
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
        return GzipHelper.compressToString(source, defaultIfBlank(charset, CHARSET_UTF_8));
    }

    /**
     * 压缩字符串
     *
     * @param source 元数据
     * @return
     */
    public static String compress(final String source) {
        return compress(source, CHARSET_UTF_8);
    }

    /**
     * 解压字符串
     *
     * @param source 元数据
     * @return
     */
    public static String uncompress(final String source, final String charset) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(source));
        return GzipHelper.uncompress(source, defaultIfBlank(charset, CHARSET_UTF_8));
    }

    /**
     * 解压字符串
     *
     * @param source 元数据
     * @return
     */
    public static String uncompress(final String source) {
        return uncompress(source, CHARSET_UTF_8);
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
        return toStringArray(tokens);
    }

    /**
     * 获取两字符串的相似度
     */
    public static float getSimilarityRatio(String str, String target) {
        int max = Math.max(str.length(), target.length());
        return 1 - (float) compare(str, target) / max;
    }

    /**
     * 页面中去除字符串中的空格、回车、换行符、制表符
     * <pre>
     *     StringHelper.replaceBlank("23") = "23"
     *     StringHelper.replaceBlank("23\t") = "23"
     *     StringHelper.replaceBlank("23\n") = "23"
     *     StringHelper.replaceBlank("23\r") = "23"
     *     StringHelper.replaceBlank("23 ") = "23"
     *     StringHelper.replaceBlank("2 3 ") = "23"
     *     StringHelper.replaceBlank(null) = ""
     * </pre>
     *
     * @param str 需要处理的字符串
     */
    public static String replaceBlank(String str) {
        if (isBlank(str)) {
            return "";
        }
        Matcher m = PATTERN_BLANK.matcher(str);
        return m.replaceAll("");
    }

    /**
     * 正则匹配
     * <pre>
     *     StringHelper.wildcardMatch("23", "2") = true
     *     StringHelper.wildcardMatch("", 2) = false
     *     StringHelper.wildcardMatch(null, 2) = false
     *     StringHelper.wildcardMatch(null, -1) = false
     *     StringHelper.wildcardMatch("2", -1) = false
     *     StringHelper.wildcardMatch(null, null) = true
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
     * 占位符
     * <pre>
     *     StringHelper.placehoder("23", "2") = true
     *     StringHelper.placehoder("", 2) = false
     *     StringHelper.placehoder(null, 2) = false
     *     StringHelper.placehoder(null, -1) = false
     *     StringHelper.placehoder("2", -1) = false
     *     StringHelper.placehoder(null, null) = true
     * </pre>
     *
     * @param source
     * @param holder
     * @param objs
     * @return
     */
    public static String placehoder(String source, String holder, Object[]... objs) {
        source = source.replace(holder, "%s");
        return null;
    }

    /**
     * repeat - 通过源字符串重复生成N次组成新的字符串。
     * <pre>
     *     StringHelper.repeat("23", 2) = "2323"
     *     StringHelper.repeat("", 2) = ""
     *     StringHelper.repeat(null, 2) = ""
     *     StringHelper.repeat(null, -1) = ""
     *     StringHelper.repeat("2", -1) = ""
     * </pre>
     *
     * @param src - 源字符串 例如: 空格(" "), 星号("*"), "浙江" 等等...
     * @param num - 重复生成次数
     * @return 返回已生成的重复字符串
     */
    public static String repeat(String src, int num) {
        if (isBlank(src)) {
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
     * 隐藏邮件地址前缀。
     * <pre>
     *     StringHelper.getHideEmailPrefix("ssss@koubei.com") = "*********@koubei.com"
     *     StringHelper.getHideEmailPrefix(null) = ""
     * </pre>
     *
     * @param email - EMail邮箱地址 例如: ssss@koubei.com 等等...
     * @return 返回已隐藏前缀邮件地址, 如 *********@koubei.com.
     */
    public static String getHideEmailPrefix(String email) {
        if (isBlank(email)) {
            return "";
        }
        int index = email.lastIndexOf('@');
        return index > -1 ? repeat("*", index).concat(email.substring(index)) : "";
    }


    /**
     * <pre>
     * StringHelper.isAllBlank(null)             = true
     * StringHelper.isAllBlank(null, "foo")      = false
     * StringHelper.isAllBlank(null, null)       = true
     * StringHelper.isAllBlank("", "bar")        = false
     * StringHelper.isAllBlank("bob", "")        = false
     * StringHelper.isAllBlank("  bob  ", null)  = false
     * StringHelper.isAllBlank(" ", "bar")       = false
     * StringHelper.isAllBlank("foo", "bar")     = false
     * StringHelper.isAllBlank(new String[] {})  = true
     * </pre>
     *
     * @param css 原始数据
     * @since 3.6
     */
    public static boolean isAllBlank(final CharSequence... css) {
        if (ArraysHelper.isEmpty(css)) {
            return true;
        }
        for (final CharSequence cs : css) {
            if (isNotBlank(cs)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串比对
     * <pre>
     * StringHelper.equals(null, null)   = true
     * StringHelper.equals(null, "abc")  = false
     * StringHelper.equals("abc", null)  = false
     * StringHelper.equals("abc", "abc") = true
     * StringHelper.equals("abc", "ABC") = false
     * </pre>
     *
     * @param source1 源数据1
     * @param source2 源数据2
     * @return
     */
    public static boolean equals(final CharSequence source1, final CharSequence source2) {
        if (source1 == source2) {
            return true;
        }
        if (source1 == null || source2 == null) {
            return false;
        }
        if (source1.length() != source2.length()) {
            return false;
        }
        if (source1 instanceof String && source2 instanceof String) {
            return source1.equals(source2);
        }
        final int length = source1.length();
        for (int i = 0; i < length; i++) {
            if (source1.charAt(i) != source2.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串比对
     * <pre>
     * StringHelper.equals(null, null)   = true
     * StringHelper.equals(null, "abc")  = false
     * StringHelper.equals("abc", null)  = false
     * StringHelper.equals("abc", "abc") = true
     * StringHelper.equals("abc", "ABC") = false
     * </pre>
     *
     * @param source1 源数据1
     * @param source2 源数据2
     * @return
     */
    public static boolean equals(String source1, String... source2) {
        return equals(source1, new HashSet<>(Arrays.asList(source2)));
    }

    /**
     * 字符串比对
     * <pre>
     * StringHelper.equals(null, null)   = true
     * StringHelper.equals(null, "abc")  = false
     * StringHelper.equals("abc", null)  = false
     * StringHelper.equals("abc", "abc") = true
     * StringHelper.equals("abc", "ABC") = false
     * </pre>
     *
     * @param source1 源数据1
     * @param source2 源数据2
     * @return
     */
    public static boolean equals(String source1, Set<String> source2) {
        if (source1 == null) {
            if (source2 == null) {
                return true;
            } else {
                for (String s : source2) {
                    if (null == s) {
                        return true;
                    }
                }
                return false;
            }
        } else {
            if (null == source2 || source2.size() == 0) {
                return false;
            }
            boolean isRegals = false;
            for (String s : source2) {
                if (s.equals(source1)) {
                    isRegals = true;
                    break;
                }
            }

            return isRegals;
        }
    }

    /**
     * <pre>
     * StringHelper.isNotBlank(null)      = false
     * StringHelper.isNotBlank("")        = false
     * StringHelper.isNotBlank(" ")       = false
     * StringHelper.isNotBlank("bob")     = true
     * StringHelper.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param css 源数据
     * @return
     */
    public static boolean isNoneBlank(final CharSequence... css) {
        return !isAnyBlank(css);
    }

    /**
     * <pre>
     * StringHelper.isAnyBlank((String) null)    = true
     * StringHelper.isAnyBlank((String[]) null)  = false
     * StringHelper.isAnyBlank(null, "foo")      = true
     * StringHelper.isAnyBlank(null, null)       = true
     * StringHelper.isAnyBlank("", "bar")        = true
     * StringHelper.isAnyBlank("bob", "")        = true
     * StringHelper.isAnyBlank("  bob  ", null)  = true
     * StringHelper.isAnyBlank(" ", "bar")       = true
     * StringHelper.isAnyBlank(new String[] {})  = false
     * StringHelper.isAnyBlank(new String[]{""}) = true
     * StringHelper.isAnyBlank("foo", "bar")     = false
     * </pre>
     *
     * @param css 源数据
     * @since 3.2
     */
    public static boolean isAnyBlank(final CharSequence... css) {
        if (BooleanHelper.isEmpty(css)) {
            return false;
        }
        for (final CharSequence cs : css) {
            if (isBlank(cs)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 为空
     * <pre>
     *     StringHelper.isEmpty("") == false;
     *     StringHelper.isEmpty(null) == true;
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
     *     StringHelper.isAnyEmpty("", "") == false
     *     StringHelper.isAnyEmpty("", null) == true
     * </pre>
     *
     * @param value 源数据
     * @return boolean
     */
    public static boolean isAnyEmpty(final String... value) {
        if (null == value) {
            return true;
        }
        for (String s : value) {
            if (isEmpty(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 为空
     * <pre>
     *  StringHelper.isNotEmpty(null) == false
     *  StringHelper.isNotEmpty("1") == true
     *  StringHelper.isNotEmpty("") == true
     * </pre>
     *
     * @param value 源数据
     * @return
     */
    public static boolean isNotEmpty(final String value) {
        return !isEmpty(value);
    }

    /**
     * 为空
     * StringHelper.isAnyNotEmpty(null, "") == false
     * StringHelper.isAnyNotEmpty("1", null) == false
     * StringHelper.isAnyNotEmpty(null, null) == true
     *
     * @param value 源数据
     * @return
     */
    public static boolean isAnyNotEmpty(final String... value) {
        if (null == value) {
            return false;
        }

        for (String s : value) {
            if (isEmpty(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 为空
     * <pre>
     * StringHelper.isBlank(null)      = true
     * StringHelper.isBlank("")        = true
     * StringHelper.isBlank(" ")       = true
     * StringHelper.isBlank("bob")     = false
     * StringHelper.isBlank("  bob  ") = false
     * </pre>
     *
     * @param value 源数据
     * @return
     */
    public static boolean isBlank(final CharSequence value) {
        int strLen;
        if (value == null || (strLen = value.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 转为字符串
     * <pre>
     *     StringHelper.toString(null) == ""
     *     StringHelper.toString("") == ""
     *     StringHelper.toString("2") == "2"
     * </pre>
     *
     * @param value 值
     * @return
     */
    public static String toString(final Object value) {
        if(null == value) {
            return EMPTY;
        }
        if(value instanceof byte[]) {
            return toString((byte[]) value, Charsets.UTF_8);
        }
        return value.toString();
    }

    /**
     * <pre>
     *     StringHelper.toString(null) == ""
     *     StringHelper.toString("") == ""
     *     StringHelper.toString("2") == "2"
     * </pre>
     *
     * @since 1.0.0
     */
    public static String toString(byte[] bytes, String charsetName) throws UnsupportedEncodingException {
        return charsetName == null ? new String(bytes) : new String(bytes, charsetName);
    }
    /**
     * <pre>
     *     StringHelper.toString(null) == ""
     *     StringHelper.toString("") == ""
     *     StringHelper.toString("2") == "2"
     * </pre>
     *
     * @since 1.0.0
     */
    public static String toString(byte[] bytes, Charset charsetName) {
        return charsetName == null ? new String(bytes) : new String(bytes, charsetName);
    }

    /**
     * 判断是否包含空
     * <pre>
     *     StringHelper.isAnyBlank("", "1") == true
     *     StringHelper.isAnyBlank(null, "1") == true
     *     StringHelper.isAnyBlank("1", "1") == false
     * </pre>
     *
     * @param value 值
     * @return
     */
    public static boolean isAnyBlank(final String... value) {
        if (null == value) {
            return true;
        }
        for (String s : value) {
            if (isBlank(s)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 不为空
     * <pre>
     *     StringHelper.isNotBlank(null) == false
     *     StringHelper.isNotBlank("") == false
     *     StringHelper.isNotBlank("1") == true
     * </pre>
     *
     * @param value 值
     * @return
     */
    public static boolean isNotBlank(final CharSequence value) {
        return !isBlank(value);
    }

    /**
     * 不为空
     * <pre>
     *     StringHelper.isNotBlank(0) == false
     *     StringHelper.isNotBlank(-1) == false
     *     StringHelper.isNotBlank(1) == true
     * </pre>
     *
     * @param value 值
     * @return
     */
    public static boolean isNotBlank(final int value) {
        return value > 0;
    }

    /**
     * 全部不为空
     * <pre>
     *     StringHelper.isAnyBlank("", "1") == false
     *     StringHelper.isAnyBlank(null, "1") == false
     *     StringHelper.isAnyBlank("1", "1") == true
     * </pre>
     *
     * @param value 值
     * @return
     */
    public static boolean isAnyNotBlank(final String... value) {
        if (null == value) {
            return false;
        }

        for (String s : value) {
            if (isBlank(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取字符串之间值
     * <pre>
     *     StringHelper.cut("@test@", "@", "@") == test
     *     StringHelper.cut("@test#", "@", "#") == test
     * </pre>
     *
     * @param start    开始符号
     * @param end      结束符号
     * @param srcValue 源数据
     * @return
     */
    public static final List<String> cut(final String srcValue, final String start, final String end) {
        return pattern(srcValue, start + REGEXP_ALL + end, -1);
    }

    /**
     * 获取以开始符号开始的字符串数据
     * StringHelper.cut("@test@", "@", -1) == ["test", ""]
     * StringHelper.cut("@test#", "@", -1) == ["test#"]
     *
     * @param start    开始符号
     * @param srcValue 源数据
     * @param limit    返回数量
     * @return
     */
    public static final List<String> startsWith(final String srcValue, final String start, int limit) {
        return pattern(srcValue, start + REGEXP_ALL, limit);
    }

    /**
     * @param start    开始符号
     * @param srcValue 源数据
     * @return
     */
    public static final List<String> startsWith(final String srcValue, final String start) {
        return pattern(srcValue, start + REGEXP_ALL, -1);
    }

    /**
     * @param end      结束符号
     * @param srcValue 源数据
     * @return
     */
    public static final List<String> endsWith(final String srcValue, final String end, final int limit) {
        return pattern(srcValue, REGEXP_ALL + end, limit);
    }

    /**
     * @param end      结束符号
     * @param srcValue 源数据
     * @return
     */
    public static final List<String> endsWith(final String srcValue, final String end) {
        return pattern(srcValue, REGEXP_ALL + end, -1);
    }

    /**
     * @param end      结束符号
     * @param srcValue 源数据
     * @return
     */
    public static final String endsWithString(final String srcValue, final String end) {
        final List<String> pattern = pattern(srcValue, REGEXP_ALL + end, 1);
        return ListHelper.one(pattern);
    }

    /**
     * 正则匹配
     *
     * @param srcValue     源数据
     * @param patternValue 正则表达式
     * @return
     */
    public static final List<String> pattern(final String srcValue, final String patternValue, final int limit) {
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
    public static final List<String> pattern(final String srcValue, final String patternValue, final int limit, IPreMatcher<String> patternHandler) {
        Pattern pattern = Pattern.compile(patternValue);
        Matcher matcher = pattern.matcher(srcValue);

        List<String> lStrings = new ArrayList<>();
        while (matcher.find()) {
            final String group = patternHandler.matcher(matcher);
            if (-1 == limit || lStrings.size() <= limit) {
                lStrings.add(null != group ? group.trim() : EXTENSION_EMPTY);
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
        String regex = EXTENSION_EMPTY;
        for (String s : condition) {
            regex = regex.concat(EXTENSION_OR).concat(s);
        }
        if (regex.length() > 0) {
            regex = regex.substring(1);
        }

        return Splitter.onPattern(regex).omitEmptyStrings().splitToList(srcValue);
    }

    /**
     * 获取前缀
     *
     * @param srcValue     源数据
     * @param condition    前缀符号
     * @param defaultValue 默认值
     * @return
     */
    public static String preffix(String srcValue, String condition, String defaultValue) {
        if (isEmpty(srcValue)) {
            return defaultValue;
        }
        final String s = endsWithString(srcValue, condition);
        List<String> strings = Splitter.onPattern(REGEXP_EMPTY).trimResults().splitToList(s);
        String endValue = strings.get(strings.size() - 1);
        return isEmpty(endValue) ? defaultValue : endValue;
    }

    /**
     * 获取前缀
     *
     * @param srcValue  源数据
     * @param condition 前缀符号
     */
    public static final String preffix(String srcValue, String condition) {
        return preffix(srcValue, condition, EXTENSION_EMPTY);
    }

    /**
     * 删除多余字符
     *
     * @param srcValue     源数据
     * @param extensionPer 删除的符号
     * @return
     */
    public static String remove(final String srcValue, final String extensionPer) {
        return srcValue.replace(extensionPer, EXTENSION_EMPTY);
    }

    /**
     * 转为字符串
     *
     * @param data 字符数组
     * @return
     */
    public static String asString(byte[] data) {
        return new String(data, Charset.forName(CHARSET_UTF_8));
    }

    /**
     * 转为List
     *
     * @param data      字符数组
     * @param separator 分割符号
     * @return
     */
    public static List<String> asList(byte[] data, final String separator) {
        String s = new String(data, Charset.forName(CHARSET_UTF_8));
        return Splitter.on(separator).trimResults().splitToList(s);
    }

    /**
     * 去除空格
     * <pre>
     * StringHelper.trim(null)          = null
     * StringHelper.trim("")            = ""
     * StringHelper.trim("     ")       = ""
     * StringHelper.trim("abc")         = "abc"
     * StringHelper.trim("    abc    ") = "abc"
     * </pre>
     *
     * @param srcValue 源数据
     * @return
     */
    public static String trim(final String srcValue) {
        return null == srcValue ? EXTENSION_EMPTY : srcValue.trim();
    }

    /**
     * 去除空格
     * <pre>
     * StringHelper.trim(null)          = null
     * StringHelper.trim("")            = ""
     * StringHelper.trim("     ")       = ""
     * StringHelper.trim("abc")         = "abc"
     * StringHelper.trim("    abc    ") = "abc"
     * </pre>
     *
     * @param srcValue 源数据
     * @param reg      正则
     * @return
     */
    public static String trim(String srcValue, String reg) {
        return null == srcValue || null == reg ? EXTENSION_EMPTY : srcValue.replaceAll(reg, EXTENSION_EMPTY);
    }

    /**
     * 去除空格
     * <pre>
     * StringHelper.trimToNull(null)          = null
     * StringHelper.trimToNull("")            = null
     * StringHelper.trimToNull("     ")       = null
     * StringHelper.trimToNull("abc")         = "abc"
     * StringHelper.trimToNull("    abc    ") = "abc"
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
     * StringHelper.trimToEmpty(null)          = ""
     * StringHelper.trimToEmpty("")            = ""
     * StringHelper.trimToEmpty("     ")       = ""
     * StringHelper.trimToEmpty("abc")         = "abc"
     * StringHelper.trimToEmpty("    abc    ") = "abc"
     * </pre>
     *
     * @param str
     * @return
     */
    public static String trimToEmpty(final String str) {
        return str == null ? EXTENSION_EMPTY : str.trim();
    }

    /**
     * 获取指定长度字符串
     * <pre>
     * StringHelper.truncate(null, 0)       = null
     * StringHelper.truncate(null, 2)       = null
     * StringHelper.truncate("", 4)         = ""
     * StringHelper.truncate("abcdefg", 4)  = "abcd"
     * StringHelper.truncate("abcdefg", 6)  = "abcdef"
     * StringHelper.truncate("abcdefg", 7)  = "abcdefg"
     * StringHelper.truncate("abcdefg", 8)  = "abcdefg"
     * StringHelper.truncate("abcdefg", -1) = ""
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
     * StringHelper.truncate(null, 0, 0) = null
     * StringHelper.truncate(null, 2, 4) = null
     * StringHelper.truncate("", 0, 10) = ""
     * StringHelper.truncate("", 2, 10) = ""
     * StringHelper.truncate("abcdefghij", 0, 3) = "abc"
     * StringHelper.truncate("abcdefghij", -1, 3) = "j"
     * StringHelper.truncate("abcdefghij", 5, -3) = "cde"
     * StringHelper.truncate("abcdefghij", -5, -3) = "cde"
     * StringHelper.truncate("abcdefghij", 5, 6) = "fghij"
     * StringHelper.truncate("raspberry peach", 10, 15) = "peach"
     * StringHelper.truncate("abcdefghijklmno", 0, 10) = "abcdefghij"
     * StringHelper.truncate("abcdefghijklmno", -1, 10) = o"
     * StringHelper.truncate("abcdefghijklmno", Integer.MIN_VALUE, 10) = "abcdefghij"
     * StringHelper.truncate("abcdefghijklmno", Integer.MIN_VALUE, Integer.MAX_VALUE) = "abcdefghijklmno"
     * StringHelper.truncate("abcdefghijklmno", 0, Integer.MAX_VALUE) = "abcdefghijklmno"
     * StringHelper.truncate("abcdefghijklmno", 1, 10) = "bcdefghijk"
     * StringHelper.truncate("abcdefghijklmno", 2, 10) = "cdefghijkl"
     * StringHelper.truncate("abcdefghijklmno", 3, 10) = "defghijklm"
     * StringHelper.truncate("abcdefghijklmno", 4, 10) = "efghijklmn"
     * StringHelper.truncate("abcdefghijklmno", 5, 10) = "fghijklmno"
     * StringHelper.truncate("abcdefghijklmno", 5, 5) = "fghij"
     * StringHelper.truncate("abcdefghijklmno", 5, 3) = "fgh"
     * StringHelper.truncate("abcdefghijklmno", 10, 3) = "klm"
     * StringHelper.truncate("abcdefghijklmno", 10, Integer.MAX_VALUE) = "klmno"
     * StringHelper.truncate("abcdefghijklmno", 13, 1) = "n"
     * StringHelper.truncate("abcdefghijklmno", 13, Integer.MAX_VALUE) = "no"
     * StringHelper.truncate("abcdefghijklmno", 14, 1) = "o"
     * StringHelper.truncate("abcdefghijklmno", 14, Integer.MAX_VALUE) = "o"
     * StringHelper.truncate("abcdefghijklmno", 15, 1) = ""
     * StringHelper.truncate("abcdefghijklmno", 15, Integer.MAX_VALUE) = ""
     * StringHelper.truncate("abcdefghijklmno", Integer.MAX_VALUE, Integer.MAX_VALUE) = ""
     * StringHelper.truncate("abcdefghij", 3, -1) = "c"
     * StringHelper.truncate("abcdefghij", -2, 4) = "ij"
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
            return EXTENSION_EMPTY;
        }
        if (length == 0) {
            return EXTENSION_EMPTY;
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
     *                                                                   StringHelper.stripToEmpty(null)     = ""
     *                                                                   StringHelper.stripToEmpty("")       = ""
     *                                                                   StringHelper.stripToEmpty("   ")    = ""
     *                                                                   StringHelper.stripToEmpty("abc")    = "abc"
     *                                                                   StringHelper.stripToEmpty("  abc")  = "abc"
     *                                                                   StringHelper.stripToEmpty("abc  ")  = "abc"
     *                                                                   StringHelper.stripToEmpty(" abc ")  = "abc"
     *                                                                   StringHelper.stripToEmpty(" ab c ") = "ab c"
     *                                                                   </pre>
     * @return
     */
    public static String stripToEmpty(final String str) {
        return str == null ? EMPTY : strip(str, null);
    }

    /**
     * 去除前后指定字符
     * <pre>
     * StringHelper.strip(null, *)          = null
     * StringHelper.strip("", *)            = ""
     * StringHelper.strip("abc", null)      = "abc"
     * StringHelper.strip("  abc", null)    = "abc"
     * StringHelper.strip("abc  ", null)    = "abc"
     * StringHelper.strip(" abc ", null)    = "abc"
     * StringHelper.strip("  abcyx", "xyz") = "  abc"
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
     * StringHelper.stripEnd(null, *)          = null
     * StringHelper.stripEnd("", *)            = ""
     * StringHelper.stripEnd("abc", "")        = "abc"
     * StringHelper.stripEnd("abc", null)      = "abc"
     * StringHelper.stripEnd("  abc", null)    = "  abc"
     * StringHelper.stripEnd("abc  ", null)    = "abc"
     * StringHelper.stripEnd(" abc ", null)    = " abc"
     * StringHelper.stripEnd("  abcyx", "xyz") = "  abc"
     * StringHelper.stripEnd("120.00", ".0")   = "12"
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
     * StringHelper.stripStart(null, *)          = null
     * StringHelper.stripStart("", *)            = ""
     * StringHelper.stripStart("abc", "")        = "abc"
     * StringHelper.stripStart("abc", null)      = "abc"
     * StringHelper.stripStart("  abc", null)    = "abc"
     * StringHelper.stripStart("abc  ", null)    = "abc  "
     * StringHelper.stripStart(" abc ", null)    = "abc "
     * StringHelper.stripStart("yxabc  ", "xyz") = "abc  "
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
            byte[] bs = srcValue.getBytes(CHARSET_UTF_8);
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
     *  StringHelper.getDefaultValue(null, "1") == "1"
     *  StringHelper.getDefaultValue("22", "1") == "22"
     * </pre>
     *
     * @param srcValue
     * @param defaultValue
     * @return
     */
    public static String getDefaultValue(final String srcValue, final String defaultValue) {
        return isBlank(srcValue) ? defaultValue : srcValue;
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
        if (isBlank(value)) {
            return value;
        }
        int startIndex = value.indexOf(start);
        if (startIndex > -1 && value.indexOf(end, startIndex) > -1) {
            String patternStr = FILE_RIGHT_SLASH + start + REGEXP_ALL_1 + FILE_RIGHT_SLASH + end;
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(value);
            while (matcher.find()) {
                String group = matcher.group();
                value = value.replace(group, MapHelper.strings(group.replace(start, EXTENSION_EMPTY).replace(end, EXTENSION_EMPTY), EXTENSION_EMPTY, data));
            }

        }
        return value;
    }

    /**
     * 获取字符串最后一节
     *
     * @param srcValue 源数据
     * @return
     */
    public static String lastEnd(final String srcValue, final String... extensions) {
        if (isNotBlank(srcValue)) {
            String extension = EXTENSION_DOT;
            if (null != extensions && extensions.length > 0) {
                extension = extensions[0];
            }
            int index = srcValue.lastIndexOf(extension);
            return index > -1 ? srcValue.substring(0, index) : srcValue;
        }
        return srcValue;
    }

    /**
     * 获取字符串前面一节
     *
     * @param srcValue 源数据
     * @return
     */
    public static String firstEnd(final String srcValue, final String... extensions) {
        if (isNotBlank(srcValue)) {
            String extension = EXTENSION_DOT;
            if (null != extensions && extensions.length > 0) {
                extension = extensions[0];
            }
            int index = srcValue.indexOf(extension);
            return index > -1 ? srcValue.substring(index + 1) : srcValue;
        }
        return srcValue;
    }

    /**
     * 获取多个字符串中不为空的值
     *
     * @param srcValue1 源数据1
     * @param srcValue2 源数据2
     * @param srcValue3 源数据3
     * @return
     */
    public static String getNotBlank(String srcValue1, String srcValue2, String srcValue3) {
        if (isNotEmpty(srcValue1)) {
            return srcValue1;
        }
        if (isNotEmpty(srcValue2)) {
            return srcValue2;
        }
        if (isNotEmpty(srcValue3)) {
            return srcValue3;
        }
        return null;
    }

    /**
     * 数据是否包含指定符号
     *
     * @param srcValue 源数据
     * @param signs    符号
     * @return
     */
    public static boolean anyContains(String srcValue, String... signs) {
        boolean isContains = false;
        if (isBlank(srcValue) || null == signs || signs.length == 0) {
            return isContains;
        }
        for (String string : signs) {
            isContains = isContains || srcValue.contains(string);
        }
        return isContains;
    }

    /**
     * 数据是否包含所有指定符号
     *
     * @param srcValue 源数据
     * @param signs    符号
     * @return
     */
    public static boolean allContains(String srcValue, String... signs) {
        boolean isContains = true;
        if (isBlank(srcValue) || null == signs || signs.length == 0) {
            return !isContains;
        }
        for (String string : signs) {
            isContains = isContains && srcValue.contains(string);
        }
        return isContains;
    }

    /**
     * 首尾是否包含指定符号
     *
     * @param srcValue 源数据
     * @param signs    符号
     * @return
     */
    public static boolean firstAndEndContains(String srcValue, String... signs) {
        boolean isContains = true;
        if (isBlank(srcValue) || null == signs || signs.length == 0) {
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
     * @return
     */
    public static boolean firstOrEndContains(String srcValue, String... signs) {
        boolean isContains = true;
        if (isBlank(srcValue) || null == signs || signs.length == 0) {
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
     */
    public static String firstLowerCase(String srcValue) {
        if (!hasLength(srcValue)) {
            return srcValue;
        }
        return first(srcValue).toLowerCase() + substring(srcValue, 1);
    }

    /**
     * 首字母大写
     * <pre>
     *     StringHelper.substring("test", 1) = "Test"
     *     StringHelper.substring(null) = ""
     * </pre>
     *
     * @param srcValue 源数据
     */
    public static String firstUpperCase(String srcValue) {
        if (!hasLength(srcValue)) {
            return srcValue;
        }
        return first(srcValue).toUpperCase() + substring(srcValue, 1);
    }

    /**
     * 从开始位置截取字符串
     * <pre>
     *     StringHelper.substring("test", 1) = "est"
     *     StringHelper.substring(null) = ""
     * </pre>
     *
     * @param srcValue 源数据
     * @param start    开始位置
     * @return
     */
    public static String substring(String srcValue, int start) {
        return hasLength(srcValue) ? srcValue.substring(start) : EXTENSION_EMPTY;
    }

    /**
     * 返回截取符之前数据
     * <pre>
     * StringHelper.substringBefore(null, *)      = null
     * StringHelper.substringBefore("", *)        = ""
     * StringHelper.substringBefore("abc", "a")   = ""
     * StringHelper.substringBefore("abcba", "b") = "a"
     * StringHelper.substringBefore("abc", "c")   = "ab"
     * StringHelper.substringBefore("abc", "d")   = "abc"
     * StringHelper.substringBefore("abc", "")    = ""
     * StringHelper.substringBefore("abc", null)  = "abc"
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
            return EMPTY;
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
     * StringHelper.substringAfter(null, *)      = null
     * StringHelper.substringAfter("", *)        = ""
     * StringHelper.substringAfter("abc", "a")   = ""
     * StringHelper.substringAfter("abcba", "b") = "a"
     * StringHelper.substringAfter("abc", "c")   = "ab"
     * StringHelper.substringAfter("abc", "d")   = "abc"
     * StringHelper.substringAfter("abc", "")    = ""
     * StringHelper.substringAfter("abc", null)  = "abc"
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
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos < 0) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    /**
     * 从开始位置到结束位置截取字符串
     * <pre>
     *     StringHelper.substring("test", 1, 2) = "e"
     *     StringHelper.substring("test ", 2, 1) = ""
     *     StringHelper.substring(null) = ""
     * </pre>
     *
     * @param srcValue 源数据
     * @param start    开始位置
     * @param end      结束位置
     * @return
     */
    public static String substring(String srcValue, int start, int end) {
        return hasLength(srcValue) && start < end ? srcValue.substring(start, end) : EXTENSION_EMPTY;
    }

    /**
     * 获取首字母
     * <pre>
     *     StringHelper.first("test") = "t"
     *     StringHelper.first("test ") = "t"
     *     StringHelper.first(" test ") = ""
     *     StringHelper.first(" te st ") = ""
     *     StringHelper.first(null) = ""
     * </pre>
     *
     * @param value
     * @return
     */
    public static String first(String value) {
        return hasLength(value) ? value.substring(0, 1) : EXTENSION_EMPTY;
    }

    /**
     * 判断字符串是否有长度
     * <pre>
     *     StringHelper.hasLength("") = false;
     *     StringHelper.hasLength("   ") = false;
     *     StringHelper.hasLength("1") = true;
     * </pre>
     *
     * @param source 源数据
     * @return
     */
    public static boolean hasLength(String source) {
        return isNotBlank(source) && trim(source).length() > 0;
    }

    /**
     * 判断字符串是否有长度
     * <pre>
     *     StringHelper.hasNoneLength("") = true;
     *     StringHelper.hasNoneLength("   ") = true;
     *     StringHelper.hasNoneLength("1") = false;
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
     *     StringHelper.trimAllWhitespace("test") = "test"
     *     StringHelper.trimAllWhitespace("test ") = "test"
     *     StringHelper.trimAllWhitespace(" test ") = "test"
     *     StringHelper.trimAllWhitespace(" te st ") = "test"
     *     StringHelper.trimAllWhitespace(null) = null
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
            return EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            if (isNotBlank(string)) {
                sb.append(string).append(separator);
            }
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - separator.length()) : EMPTY;
    }

    /**
     * 数组转十六进制
     *
     * @param source 数据源
     * @return
     */
    public static String toHexString(byte[] source) {
        if (null == source) {
            return EMPTY;
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
            ret[i] = Byte.valueOf((byte)intVal);
        }
        return ret;
    }
}
