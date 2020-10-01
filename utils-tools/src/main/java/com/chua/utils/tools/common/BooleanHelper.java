package com.chua.utils.tools.common;

import com.chua.utils.tools.classes.ClassHelper;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.chua.utils.tools.constant.PatternConstant.*;
import static com.chua.utils.tools.constant.StringConstant.REG_EMAIL;
import static com.chua.utils.tools.constant.StringConstant.TRUE;

/**
 * boolean工具类
 * @author CH
 */
public class BooleanHelper {
    /**
     * 判断是否有长度
     *
     * @param urls 请求连接
     * @return
     */
    public static boolean hasLength(final Enumeration<URL> urls) {
        return null != urls && urls.hasMoreElements();
    }
    /**
     * 判断是否有长度
     *
     * @param iterable iterable
     * @return
     */
    public static boolean hasLength(final Iterable iterable) {
        return null != iterable && iterable.iterator().hasNext();
    }

    /**
     * 判断所有请求连接是否有长度
     *
     * @param urls 请求连接
     * @return
     */
    public static boolean hasLength(final Enumeration<URL> urls, final Enumeration<URL>... eUrls) {
        return hasAllLength(eUrls);
    }

    /**
     * 判断所有请求连接是否有长度
     *
     * @param eUrls 请求连接
     * @return
     */
    public static boolean hasAllLength(final Enumeration<URL>... eUrls) {
        return hasElementAllLength(eUrls);
    }

    /**
     * 判断所有请求连接是否有长度
     *
     * @param urls 请求连接
     * @return
     */
    public static boolean hasAnyLength(final Enumeration<URL> urls, final Enumeration<URL>... eUrls) {
        return hasLength(urls) || hasElementAllLength(eUrls);
    }

    /**
     * 判断数组是否有长度
     *
     * @param source 数组
     * @return
     */
    private static <T> boolean hasElementAllLength(final T... source) {
        if (null == source || source.length == 0) {
            return false;
        }
        for (T ts : source) {
            if (ts instanceof Collection) {
                if (!hasLength((Collection) ts)) {
                    return false;
                }
            } else if (ts instanceof Iterator) {
                if (!hasLength((Iterator) ts)) {
                    return false;
                }
            } else if (ts instanceof String) {
                if (!hasLength((String) ts)) {
                    return false;
                }
            } else if (ts instanceof Map) {
                if (!hasLength((Map) ts)) {
                    return false;
                }
            } else if (ts instanceof Array) {
                if (!hasLength((T[]) ts)) {
                    return false;
                }
            } else if (ts instanceof Enumeration) {
                if (!hasLength((Enumeration) ts)) {
                    return false;
                }
            } else if (ts instanceof Properties) {
                if (!hasLength((T[]) ts)) {
                    return false;
                }
            } else if (ts instanceof Preferences) {
                if (!hasLength((T[]) ts)) {
                    return false;
                }
            } else if (ts instanceof StringBuffer) {
                if (!hasLength((StringBuffer) ts)) {
                    return false;
                }
            } else if (ts instanceof StringBuilder) {
                if (!hasLength((StringBuilder) ts)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判断数组是否有长度
     * <pre>
     *     BooleanHelper.hasLength([]) = false
     *     BooleanHelper.hasLength([]) = false
     *     BooleanHelper.hasLength([1,2,3]) = true
     * </pre>
     *
     * @param source 数组
     * @return
     */
    public static <T> boolean hasLength(final T[] source) {
        return null != source && source.length > 0;
    }

    /**
     * 判断数组是否有长度
     * <pre>
     *     BooleanHelper.hasLength([]) = false
     *     BooleanHelper.hasLength([]) = false
     *     BooleanHelper.hasLength([1,2,3], [2]) = true
     *     BooleanHelper.hasLength([1,2,3], [3]) = true
     *     BooleanHelper.hasLength([1,2,3], [4], []) = false
     * </pre>
     *
     * @param source 数组
     * @return
     */
    public static <T> boolean hasLength(final T[] source, final T[]... ts) {
        return hasAllLength(source, ts);
    }

    /**
     * 判断数组是否有长度
     *
     * @param source 数组
     * @return
     */
    public static <T> boolean hasAllLength(final T[] source, final T[]... ts) {
        return hasElementAllLength(ts);
    }

    /**
     * 判断数组是否有长度
     * <pre>
     *     BooleanHelper.hasLength([]) = false
     *     BooleanHelper.hasLength([]) = false
     *     BooleanHelper.hasLength([1,2,3], [2]) = true
     *     BooleanHelper.hasLength([1,2,3], [3]) = true
     *     BooleanHelper.hasLength([1,2,3], [4], []) = false
     * </pre>
     *
     * @param source 数组
     * @return
     */
    public static <T> boolean hasAnyLength(final T[] source, final T[]... ts) {
        return hasLength(source) || hasElementAllLength(ts);
    }


    /**
     * 有指定长度的集合
     * <pre>
     *     BooleanHelper.hasLength([], 0) = false
     *     BooleanHelper.hasLength([], -1) = false
     *     BooleanHelper.hasLength([1,2,3], 2) = true
     *     BooleanHelper.hasLength([1,2,3], 3) = true
     *     BooleanHelper.hasLength([1,2,3], 4) = false
     * </pre>
     *
     * @param sources 集合
     * @param length  索引
     * @return
     */
    public static <T> boolean hasLength(final T[] sources, final int length) {
        return hasLength(sources) && sources.length >= length;
    }

    /**
     * 判断集合是否有长度
     *
     * @param collection 集合
     * @return
     */
    public static <T> boolean hasLength(final Collection<T> collection) {
        return null != collection && !collection.isEmpty();
    }

    /**
     * 判断数组是否有长度
     * <pre>
     *     BooleanHelper.hasLength([], 0) = false
     *     BooleanHelper.hasLength([], -1) = false
     *     BooleanHelper.hasLength([1,2,3], 2) = true
     *     BooleanHelper.hasLength([1,2,3], 3) = true
     *     BooleanHelper.hasLength([1,2,3], 4) = false
     * </pre>
     *
     * @param source 数组
     * @return
     */
    public static <T> boolean hasLength(final Collection<T> source, final Collection<T>... ts) {
        return hasAllLength(ts);
    }

    /**
     * 判断数组是否有长度
     *
     * @param ts 数组
     * @return
     */
    public static <T> boolean hasAllLength(final Collection<T>... ts) {
        return hasElementAllLength(ts);
    }

    /**
     * 判断数组是否有长度
     *
     * @param source 数组
     * @return
     */
    public static <T> boolean hasAnyLength(final Collection<T> source, final Collection<T>... ts) {
        return hasLength(source) || hasElementAllLength(ts);
    }


    /**
     * 有指定长度的集合
     * <pre>
     *     BooleanHelper.hasLength([], 0) = false
     *     BooleanHelper.hasLength([], -1) = false
     *     BooleanHelper.hasLength([1,2,3], 2) = true
     *     BooleanHelper.hasLength([1,2,3], 3) = true
     *     BooleanHelper.hasLength([1,2,3], 4) = false
     * </pre>
     *
     * @param sources 集合
     * @param length  索引
     * @return
     */
    public static <T> boolean hasLength(final Collection<T> sources, final int length) {
        return hasLength(sources) && sources.size() >= length;
    }

    /**
     * 判断是否有长度
     *
     * @param iterator 迭代器
     * @return
     */
    public static <E> boolean hasLength(final Iterator<E> iterator) {
        return null != iterator && iterator.hasNext();
    }

    /**
     * 判断数组是否有长度
     *
     * @param source 数组
     * @return
     */
    public static <E> boolean hasLength(final Iterator<E> source, final Iterator<E>... ts) {
        return hasAllLength(ts);
    }

    /**
     * 判断数组是否有长度
     *
     * @param ts 数组
     * @return
     */
    public static <E> boolean hasAllLength(final Iterator<E>... ts) {
        return hasElementAllLength(ts);
    }

    /**
     * 判断数组是否有长度
     *
     * @param source 数组
     * @return
     */
    public static <E> boolean hasAnyLength(final Iterator<E> source, final Iterator<E>... ts) {
        return hasLength(source) || hasElementAllLength(ts);
    }

    /**
     * 判断是否有长度
     *
     * @param source 集合
     * @return
     */
    public static <T, V> boolean hasLength(final Map<T, V> source) {
        return null != source && !source.isEmpty();
    }

    /**
     * 有指定长度的集合
     * <pre>
     *     BooleanHelper.hasLength([], 0) = false
     *     BooleanHelper.hasLength([], -1) = false
     *     BooleanHelper.hasLength([1,2,3], 2) = true
     *     BooleanHelper.hasLength([1,2,3], 3) = true
     *     BooleanHelper.hasLength([1,2,3], 4) = false
     * </pre>
     *
     * @param sources 集合
     * @param length  索引
     * @return
     */
    public static <T, V> boolean hasLength(final Map<T, V> sources, final int length) {
        return hasLength(sources) && sources.keySet().size() >= length;
    }
    /**
     * 是否包含value
     *
     * @param sources 集合
     * @param index   索引
     * @param <V>
     * @param <K>
     * @return
     */
    public static <V, K> boolean hasValue(final Map<K, V> sources, final V index) {
        return BooleanHelper.hasLength(sources) && sources.containsValue(index);
    }

    /**
     * 是否包含key
     *
     * @param sources 集合
     * @param index   索引
     * @param <V>
     * @param <K>
     * @return
     */
    public static <V, K> boolean hasKey(final Map<K, V> sources, final K index) {
        return BooleanHelper.hasLength(sources) && sources.containsKey(index);
    }

    /**
     * 判断数组是否有长度
     *
     * @param source 数组
     * @return
     */
    public static <T, V> boolean hasLength(final Map<T, V> source, final Map<T, V>... ts) {
        return hasAllLength(ts);
    }

    /**
     * 判断数组是否有长度
     *
     * @param ts 数组
     * @return
     */
    public static <T, V> boolean hasAllLength(final Map<T, V>... ts) {
        return hasElementAllLength(ts);
    }

    /**
     * 判断数组是否有长度
     *
     * @param source 数组
     * @return
     */
    public static <T, V> boolean hasAnyLength(final Map<T, V> source, final Map<T, V>... ts) {
        return hasLength(source) || hasElementAllLength(ts);
    }

    /**
     * 判断是否有长度
     *
     * @param properties 集合
     * @return
     */
    public static boolean hasLength(final Properties properties) {
        return null != properties && !properties.isEmpty();
    }

    /**
     * 判断数组是否有长度
     *
     * @param source 数组
     * @return
     */
    public static boolean hasLength(Properties source, Properties... ts) {
        return hasAllLength(ts);
    }
    /**
     * 判断数组是否有长度
     *
     * @param source 数组
     * @return
     */
    public static boolean hasValue(Properties source, Object value) {
        return !hasLength(source) || null == value ? null : source.containsValue(value);
    }
    /**
     * 判断数组是否有长度
     *
     * @param source 数组
     * @return
     */
    public static boolean hasKey(Properties source, Object value) {
        return !hasLength(source) || null == value ? null : source.containsKey(value);
    }
    /**
     * 判断数组是否有长度
     *
     * @param ts 数组
     * @return
     */
    public static boolean hasAllLength(final Properties... ts) {
        return hasElementAllLength(ts);
    }

    /**
     * 判断数组是否有长度
     *
     * @param source 数组
     * @return
     */
    public static boolean hasAnyLength(final Properties source, final Properties... ts) {
        return hasLength(source) || hasElementAllLength(ts);
    }

    /**
     * 判断是否有长度
     *
     * @param preferences 集合
     * @return
     */
    public static <T, V> boolean hasLength(final Preferences preferences) {
        return null != preferences;
    }
    /**
     * 是否包含value
     *
     * @param sources 集合
     * @param index   索引
     * @return
     */
    public static boolean hasKey(final Preferences sources, final String index) throws BackingStoreException {
        return BooleanHelper.hasLength(sources) && sources.nodeExists(index);
    }

    /**
     * 判断数组是否有长度
     *
     * @param source 数组
     * @return
     */
    public static boolean hasLength(final Preferences source, final Preferences... ts) {
        return hasAllLength(ts);
    }

    /**
     * 判断数组是否有长度
     *
     * @param ts 数组
     * @return
     */
    public static boolean hasAllLength(final Preferences... ts) {
        return hasElementAllLength(ts);
    }

    /**
     * 判断数组是否有长度
     *
     * @param source 数组
     * @return
     */
    public static boolean hasAnyLength(Preferences source, Preferences... ts) {
        return hasLength(source) || hasElementAllLength(ts);
    }

    /**
     * 是否有长度
     *
     * @param source
     * @return
     */
    public static boolean hasLength(String source) {
        return StringHelper.hasLength(source);
    }

    /**
     * 判断数组是否有长度
     *
     * @param source 数组
     * @return
     */
    public static boolean hasLength(final String source, final String... ts) {
        return hasAllLength(ts);
    }

    /**
     * 判断数组是否有长度
     *
     * @param ts 数组
     * @return
     */
    public static boolean hasAllLength(final String... ts) {
        return hasElementAllLength(ts);
    }

    /**
     * 判断数组是否有长度
     *
     * @param source 数组
     * @return
     */
    public static boolean hasAnyLength(final String source, final String... ts) {
        return hasLength(source) || hasElementAllLength(ts);
    }

    /**
     * 判断数据是否有长度
     * @param stringBuffer
     * @return
     */
    public static boolean hasLength(final StringBuffer stringBuffer) {
        return null != stringBuffer && stringBuffer.length() > 0;
    }

    /**
     * 判断数据是否有长度
     * @param stringBuilder
     * @return
     */
    public static boolean hasLength(final StringBuilder stringBuilder) {
        return null != stringBuilder && stringBuilder.length() > 0;
    }
    /**
     * 判断是否有值
     *
     * @param obj 请求
     * @return
     */
    public static boolean isNull(final Object obj) {
        return null == obj;
    }

    /**
     * 判断是否有值
     *
     * @param obj 请求
     * @return
     */
    public static boolean isNotNull(final Object obj) {
        return !isNull(obj);
    }

    /**
     * 判断是否有值
     *
     * @param number 请求
     * @return
     */
    public static boolean isZero(final Number number) {
        return null == number || number.intValue() == 0;
    }

    /**
     * 判断是否有值
     *
     * @param value 请求
     * @return
     */
    public static boolean isZero(final Double value) {
        return null == value || value.intValue() == 0;
    }

    /**
     * 有方法
     *
     * @param name   方法名
     * @param entity 对象
     * @return
     */
    public static boolean hasLength(final String name, final Object entity) {
        Class<?> aClass = entity.getClass();
        try {
            aClass.getDeclaredMethod(name);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * 是否为空
     *
     * @param source
     * @return
     */
    public static <T> boolean isEmpty(final Collection<T> source) {
        return !hasLength(source);
    }

    /**
     * 是否为空
     *
     * @param source
     * @return
     */
    public static <T> boolean isEmpty(final T[] source) {
        return !hasLength(source);
    }

    /**
     * 是否为空
     *
     * @param source
     * @return
     */
    public static boolean isEmpty(final String source) {
        return StringHelper.isEmpty(source);
    }

    /**
     * 是否不为空
     *
     * @param source
     * @return
     */
    public static boolean isNotEmpty(final String source) {
        return StringHelper.isNotEmpty(source);
    }

    /**
     * 是否为空
     *
     * @param source
     * @return
     */
    public static boolean isBlank(final String source) {
        return StringHelper.isBlank(source);
    }

    /**
     * 是否不为空
     *
     * @param source
     * @return
     */
    public static boolean isNotBlank(final String source) {
        return StringHelper.isNotBlank(source);
    }


    /**
     * 是否为int
     *
     * @param str 数字字符串
     * @return
     */
    public static boolean isInteger(final String str) {
        return NumberHelper.isNumber(str);
    }

    /**
     * 是否为空
     *
     * @param source
     * @return
     */
    public static boolean isEmpty(final Object source) {
        return ObjectHelper.isEmpty(source);
    }

    /**
     * 获取boolean
     *
     * @param source 原始数据
     * @return
     */
    public static Boolean toBoolean(final String source) {
        return Boolean.valueOf(source);
    }

    /**
     * 获取boolean
     *
     * @param source 原始数据
     * @return
     */
    public static Boolean toBoolean(final String source, final Boolean defaultValue) {
        Boolean aBoolean = Boolean.valueOf(source);
        return aBoolean == null ? defaultValue : aBoolean;
    }

    /**
     * <pre>
     *   BooleanHelper.isTrue(Boolean.TRUE)  = true
     *   BooleanHelper.isTrue(Boolean.FALSE) = false
     *   BooleanHelper.isTrue(null)          = false
     * </pre>
     *
     * @param bool
     * @return
     */
    public static boolean isTrue(final Boolean bool) {
        return Boolean.TRUE.equals(bool);
    }

    /**
     * <pre>
     *   BooleanHelper.isNotTrue(Boolean.TRUE)  = false
     *   BooleanHelper.isNotTrue(Boolean.FALSE) = true
     *   BooleanHelper.isNotTrue(null)          = true
     * </pre>
     *
     * @param bool
     */
    public static boolean isNotTrue(final Boolean bool) {
        return !isTrue(bool);
    }

    /**
     * <pre>
     *   BooleanHelper.isFalse(Boolean.TRUE)  = false
     *   BooleanHelper.isFalse(Boolean.FALSE) = true
     *   BooleanHelper.isFalse(null)          = false
     * </pre>
     *
     * @param bool
     */
    public static boolean isFalse(final Boolean bool) {
        return Boolean.FALSE.equals(bool);
    }

    /**
     * <pre>
     *   BooleanHelper.isNotFalse(Boolean.TRUE)  = true
     *   BooleanHelper.isNotFalse(Boolean.FALSE) = false
     *   BooleanHelper.isNotFalse(null)          = true
     * </pre>
     *
     * @param bool
     */
    public static boolean isNotFalse(final Boolean bool) {
        return !isFalse(bool);
    }

    /**
     * <pre>
     *   BooleanHelper.toBoolean(0) = false
     *   BooleanHelper.toBoolean(1) = true
     *   BooleanHelper.toBoolean(2) = true
     * </pre>
     *
     * @param value
     */
    public static boolean toBoolean(final int value) {
        return value != 0;
    }

    /**
     * <pre>
     *   BooleanHelper.toInteger(true)  = 1
     *   BooleanHelper.toInteger(false) = 0
     * </pre>
     *
     * @param bool
     */
    public static int toInteger(final boolean bool) {
        return bool ? 1 : 0;
    }

    /**
     * <pre>
     *   BooleanHelper.toBooleanObject(null)    = null
     *   BooleanHelper.toBooleanObject("true")  = Boolean.TRUE
     *   BooleanHelper.toBooleanObject("T")     = Boolean.TRUE
     *   BooleanHelper.toBooleanObject("false") = Boolean.FALSE
     *   BooleanHelper.toBooleanObject("f")     = Boolean.FALSE
     *   BooleanHelper.toBooleanObject("No")    = Boolean.FALSE
     *   BooleanHelper.toBooleanObject("n")     = Boolean.FALSE
     *   BooleanHelper.toBooleanObject("on")    = Boolean.TRUE
     *   BooleanHelper.toBooleanObject("ON")    = Boolean.TRUE
     *   BooleanHelper.toBooleanObject("off")   = Boolean.FALSE
     *   BooleanHelper.toBooleanObject("oFf")   = Boolean.FALSE
     *   BooleanHelper.toBooleanObject("yes")   = Boolean.TRUE
     *   BooleanHelper.toBooleanObject("Y")     = Boolean.TRUE
     *   BooleanHelper.toBooleanObject("blue")  = null
     *   BooleanHelper.toBooleanObject("true ") = null
     *   BooleanHelper.toBooleanObject("ono")   = null
     * </pre>
     *
     * @param str
     */
    public static Boolean toBooleanObject(final String str) {
        if (str == null) {
            return null;
        }
        if (TRUE.equals(str)) {
            return Boolean.TRUE;
        }
        switch (str.length()) {
            case 1: {
                final char ch0 = str.charAt(0);
                if (ch0 == 'y' || ch0 == 'Y' ||
                        ch0 == 't' || ch0 == 'T') {
                    return Boolean.TRUE;
                }
                if (ch0 == 'n' || ch0 == 'N' ||
                        ch0 == 'f' || ch0 == 'F') {
                    return Boolean.FALSE;
                }
                break;
            }
            case 2: {
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                if ((ch0 == 'o' || ch0 == 'O') && (ch1 == 'n' || ch1 == 'N')) {
                    return Boolean.TRUE;
                }
                if ((ch0 == 'n' || ch0 == 'N') &&
                        (ch1 == 'o' || ch1 == 'O')) {
                    return Boolean.FALSE;
                }
                break;
            }
            case 3: {
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                final char ch2 = str.charAt(2);
                if ((ch0 == 'y' || ch0 == 'Y') &&
                        (ch1 == 'e' || ch1 == 'E') &&
                        (ch2 == 's' || ch2 == 'S')) {
                    return Boolean.TRUE;
                }
                if ((ch0 == 'o' || ch0 == 'O') &&
                        (ch1 == 'f' || ch1 == 'F') &&
                        (ch2 == 'f' || ch2 == 'F')) {
                    return Boolean.FALSE;
                }
                break;
            }
            case 4: {
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                final char ch2 = str.charAt(2);
                final char ch3 = str.charAt(3);
                if ((ch0 == 't' || ch0 == 'T') &&
                        (ch1 == 'r' || ch1 == 'R') &&
                        (ch2 == 'u' || ch2 == 'U') &&
                        (ch3 == 'e' || ch3 == 'E')) {
                    return Boolean.TRUE;
                }
                break;
            }
            case 5: {
                final char ch0 = str.charAt(0);
                final char ch1 = str.charAt(1);
                final char ch2 = str.charAt(2);
                final char ch3 = str.charAt(3);
                final char ch4 = str.charAt(4);
                if ((ch0 == 'f' || ch0 == 'F') &&
                        (ch1 == 'a' || ch1 == 'A') &&
                        (ch2 == 'l' || ch2 == 'L') &&
                        (ch3 == 's' || ch3 == 'S') &&
                        (ch4 == 'e' || ch4 == 'E')) {
                    return Boolean.FALSE;
                }
                break;
            }
            default:
                break;
        }

        return null;
    }


    /**
     * 判断是否数字表示
     *
     * @param src 源字符串
     * @return 是否数字的标志
     */
    public final static boolean isNumeric(final String src) {
        boolean returnValue = false;
        if (src != null && src.length() > 0) {
            Matcher m = NUMERIC_PATTERN.matcher(src);
            if (m.find()) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    /**
     * 判断是否纯字母组合
     *
     * @param src 源字符串
     * @return 是否纯字母组合的标志
     */
    public final static boolean isabc(final String src) {
        boolean returnValue = false;
        if (src != null && src.length() > 0) {
            Matcher m = ABC_PATTERN.matcher(src);
            if (m.find()) {
                returnValue = true;
            }
        }
        return returnValue;
    }


    /**
     * 判断是否浮点数字表示
     *
     * @param src 源字符串
     * @return 是否数字的标志
     */
    public final static boolean isFloatNumeric(final String src) {
        boolean returnValue = false;
        if (src != null && src.length() > 0) {
            Matcher m = FLOAT_NUMERIC_PATTERN.matcher(src);
            if (m.find()) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    /**
     * 判断字符串str是否符合正则表达式reg
     *
     * @param str 需要处理的字符串
     * @param reg 正则
     * @return 是否匹配
     */
    public final static boolean isMatcher(final String str, final String reg) {
        Pattern pattern = Pattern.compile(reg);
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 获取符合reg正则表达式的字符串在String中出现的次数
     *
     * @param str 需要处理的字符串
     * @param reg 正则
     * @return 出现的次数
     */
    public final static int countSubStrReg(final String str, final String reg) {
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        int i = 0;
        while (m.find()) {
            i++;
        }
        return i;
    }


    /**
     * 判断是否是符合邮箱
     *
     * @param email 判断的字符串
     * @return 是否是符合的邮箱
     */
    public final static boolean isEmail(final String email) {
        if (email == null || email.length() < 1 || email.length() > 256) {
            return false;
        }
        Pattern pattern = Pattern.compile(REG_EMAIL);
        return pattern.matcher(email).matches();
    }

    /**
     * 是否为空
     *
     * @param entity        实体对象
     * @param defaultEntity 默认实体对象
     * @param <O>
     * @return
     */
    public static <O> O ifBlank(final O entity, final O defaultEntity) {
        return null == entity ? defaultEntity : entity;
    }

    /**
     * 是否是Hex字符串
     *
     * @param value
     * @return
     */
    public static boolean isHexNumber(final String value) {
        int index = (value.startsWith("-") ? 1 : 0);
        return (value.startsWith("0x", index) || value.startsWith("0X", index) || value.startsWith("#", index));
    }

    /**
     * 文件是否存在
     *
     * @param file 文件
     * @return
     */
    public static boolean isExist(final File file) {
        return null != file && file.exists();
    }

    /**
     * 是否文件
     *
     * @param file 文件
     * @return
     */
    public static boolean isFile(final File file) {
        return isExist(file) && file.isFile();
    }

    /**
     * 是否文件夹
     *
     * @param file 文件
     * @return
     */
    public static boolean isDirector(final File file) {
        return isExist(file) && file.isDirectory();
    }

    /**
     * 是否 file: **.jar
     *
     * @param url
     * @return
     */
    public static boolean isJarFile(final URL url) {
        return UrlHelper.isJarFileURL(url);
    }

    /**
     * 是否 jar
     *
     * @param url
     * @return
     */
    public static boolean isJar(final URL url) {
        return UrlHelper.isJar(url);
    }

    /**
     * 是否 file
     *
     * @param url
     * @return
     */
    public static boolean isFile(final URL url) {
        return UrlHelper.isFile(url);
    }

    /**
     * 是否 file
     *
     * @param url
     * @return
     */
    public static boolean isWsJar(final URL url) {
        return UrlHelper.isWsJar(url);
    }

    /**
     * 是否 file
     *
     * @param url
     * @return
     */
    public static boolean isWar(final URL url) {
        return UrlHelper.isWar(url);
    }

    /**
     * 文件是否存在
     *
     * @param file 文件
     * @return
     */
    public static boolean isNotExist(final File file) {
        return !isExist(file);
    }

    /**
     * 是否存在该类
     *
     * @param className   类名
     * @param classLoader 类加载
     * @return
     */
    public static boolean isPresent(String className, ClassLoader classLoader) {
        return ClassHelper.isPresent(className, classLoader);
    }

    /**
     * 是否存在该类
     * <pre>
     *     BooleanHelper.isPresent("java.lang.String") == true
     *     BooleanHelper.isPresent("java.lang.String1") == false
     * </pre>
     *
     * @param className 类名
     * @return
     */
    public static boolean isPresent(String className) {
        return isPresent(className, ClassHelper.getDefaultClassLoader());
    }

    /**
     * 是否存在该类
     *
     * @param json 类名
     * @return
     */
    public static boolean isJson(String json) {
        return JsonHelper.isJson(json);
    }

    /**
     * 判断集合是否大于等于索引
     * <pre>
     *     BooleanHelper.gte([1,2,3], 0) == true
     *     BooleanHelper.gte([1,2,3], 3) == true
     *     BooleanHelper.gte([1,2,3], 4) == false
     *     BooleanHelper.gte([], 0) == true
     *     BooleanHelper.gte(null, 0) == true
     * </pre>
     *
     * @param source 集合
     * @param length 索引
     * @return
     */
    public static <T> boolean gteLength(final Collection<T> source, final int length) {
        return hasLength(source) ? source.size() >= length : length <= 0;

    }

    /**
     * 判断集合是否大于长度
     * <pre>
     *     BooleanHelper.gt([1,2,3], 0) == true
     *     BooleanHelper.gt([1,2,3], 3) == false
     *     BooleanHelper.gt([1,2,3], 4) == false
     *     BooleanHelper.gt([], 0) == false
     *     BooleanHelper.gt(null, 0) == false
     * </pre>
     *
     * @param source 集合
     * @param length 长度
     * @return
     */
    public static <T> boolean gtLength(final Collection<T> source, final int length) {
        return hasLength(source) ? source.size() > length : length < 0;
    }

    /**
     * 判断集合是否小于等于长度
     * <pre>
     *     BooleanHelper.lte([1,2,3], 0) == false
     *     BooleanHelper.lte([1,2,3], 3) == true
     *     BooleanHelper.lte([1,2,3], 4) == true
     *     BooleanHelper.lte([], 0) == true
     *     BooleanHelper.lte(null, 0) == true
     * </pre>
     *
     * @param source 集合
     * @param length 长度
     * @return
     */
    public static <T> boolean lteLength(final Collection<T> source, final int length) {
        return hasLength(source) ? source.size() <= length : length >= 0;

    }

    /**
     * 判断集合是否小于长度
     * <pre>
     *     BooleanHelper.lt([1,2,3], 0) == false
     *     BooleanHelper.lt([1,2,3], 3) == false
     *     BooleanHelper.lt([1,2,3], 4) == true
     *     BooleanHelper.lt([], 0) == false
     *     BooleanHelper.lt([], -1) == false
     * </pre>
     *
     * @param source 集合
     * @param length 长度
     * @return
     */
    public static <T> boolean ltLength(final Collection<T> source, final int length) {
        return hasLength(source) ? source.size() < length : length > 0;
    }

    /**
     * 判断集合是否等于指定长度
     * <pre>
     *     BooleanHelper.eq([1,2,3], 0) == false
     *     BooleanHelper.eq([1,2,3], 3) == true
     *     BooleanHelper.eq([], 0) == true
     * </pre>
     *
     * @param source 集合
     * @param length 长度
     * @return
     */
    public static <T> boolean eqLength(final Collection<T> source, final int length) {
        return hasLength(source) ? source.size() == length : length <= 0;
    }

    /**
     * 判断集合是否不等于长度
     * <pre>
     *     BooleanHelper.neq([1,2,3], 0) == true
     *     BooleanHelper.neq([1,2,3], 3) == false
     *     BooleanHelper.neq([], 0) == true
     * </pre>
     *
     * @param source 集合
     * @param length 长度
     * @return
     */
    public static <T> boolean neqLength(final Collection<T> source, final int length) {
        return !eqLength(source, length);
    }

    /**
     * 是否是数组
     * <pre>
     *     BooleanHelper.isArray([]) == true;
     *     BooleanHelper.isArray(null) == false;
     *     BooleanHelper.isArray("") == false;
     * </pre>
     *
     * @param objects 数据
     * @return
     */
    public static boolean isArray(final Object objects) {
        return null == objects ? false : objects.getClass().isArray();
    }

    /**
     * 是否是集合
     * <pre>
     *     BooleanHelper.isArray([]) == true;
     *     BooleanHelper.isArray(null) == false;
     *     BooleanHelper.isArray("") == false;
     * </pre>
     *
     * @param objects 数据
     * @return
     */
    public static boolean isCollection(final Object objects) {
        return null == objects ? false : objects instanceof Collection;
    }

    /**
     * 是否是集合
     * <pre>
     *     BooleanHelper.isMap({}) == true;
     *     BooleanHelper.isMap([]) == false;
     *     BooleanHelper.isMap(null) == false;
     *     BooleanHelper.isMap("") == false;
     * </pre>
     *
     * @param objects 数据
     * @return
     */
    public static boolean isMap(final Object objects) {
        return null == objects ? false : objects instanceof Map;
    }

    /**
     *
     * 获取默认对象
     * @param t 源对象
     * @param defaultValue 默认对象
     * @param <T>
     * @return
     */
    public static <T>T[] ifNulls(T[] t, T... defaultValue) {
        return BooleanHelper.hasLength(t) ? t : defaultValue;
    }
    /**
     *
     * 获取默认对象
     * @param t 源对象
     * @param defaultValue 默认对象
     * @param <T>
     * @return
     */
    public static <T>T ifNull(T t, T defaultValue) {
        Optional<T> optional = Optional.ofNullable(t);
        return optional.isPresent() ? optional.get() : defaultValue;
    }

    /**
     * 文件是否存在
     * @param file 文件
     * @return
     */
    public static boolean hasLength(final File file) {
        return null != file && (
                (file.isDirectory() && BooleanHelper.hasLength(file.list())) ||
                        (file.isFile() && file.exists())
        );
    }

    /**
     * isWin
     * @return
     */
    public static boolean isWin() {
        return System.getProperty("os.name").toLowerCase().contains("window");
    }

    /**
     * isWin
     * @return
     */
    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    /**
     * 有效索引
     * @param properties 数据
     * @param index 索引
     * @return
     */
    public static boolean isValid(Map<?, ?> properties, String index) {
        if(null == properties || StringHelper.isBlank(index) || !properties.containsKey(index)) {
            return false;
        }

        Object o = properties.get(index);
        if(null != o && (o instanceof String)) {
            return !com.google.common.base.Strings.isNullOrEmpty(String.valueOf(o));
        }

        return true;

    }
}
