package com.chua.utils.tools.common;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 大小
 * @author CH
 */
public class SizeHelper {

    public static final Float B = 1f;
    public static final Float K = B * 1024f;
    public static final Float M = K * 1024f;
    public static final Float G = M * 1024f;
    public static final Float T = G * 1024f;
    public static final Long L_B = 1L;
    public static final Long L_K = L_B * 1024L;
    public static final Long L_M = L_K * 1024L;
    public static final Long L_G = L_M * 1024L;
    public static final Long L_T = L_G * 1024L;

    private static final int THREAD_SECONDS = 1000;
    private static final int THREAD_MINUTES = 60 * THREAD_SECONDS;
    private static final int THREAD_HOURS = 60 * THREAD_MINUTES;
    private static final int THREAD_DAYS = 24 * THREAD_HOURS;

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("####.00");
    /**
     * 字节大小计算
     * @param size
     * @return
     */
    public static String size(final long size) {
        if(size < B) {
            return size + "B";
        } else if(size < K) {
            return DECIMAL_FORMAT.format(size / B) + "K";
        } else if(size < M) {
            return DECIMAL_FORMAT.format(size / K) + "M";
        } else if(size < G) {
            return DECIMAL_FORMAT.format(size / M) + "G";
        } else if(size < T) {
            return DECIMAL_FORMAT.format(size / G) + "T";
        }
        return size + " B";
    }
    /**
     * 字节大小计算
     * @param size
     * @return
     */
    public static String kb(final long size) {
        if(size < B) {
            return size + " B";
        } else {
            return DECIMAL_FORMAT.format(size / B) + "K";
        }
    }

    /**
     * 获取集合长度
     * @param collection 集合
     * @param <E>
     * @return
     */
    public static <E>int size(final Collection<E> collection) {
        return BooleanHelper.hasLength(collection) ? collection.size() : 0;
    }
    /**
     * 获取集合长度
     * @param collection 集合
     * @param <E>
     * @return
     */
    public static <E>int sizeNotNull(final Collection<E> collection) {
        Iterator<E> eIterator = elementNotNull(collection);
        return BooleanHelper.hasLength(eIterator) ? SizeHelper.size(eIterator) : 0;
    }
    /**
     * 获取集合长度
     * @param <E>
     * @param collection 集合
     * @return
     */
    public static <E> Iterator<E> elementNotNull(final Collection<E> collection) {
        if(!BooleanHelper.hasLength(collection)) {
            return null;
        }
        Iterator<E> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if(null == iterator.next()) {
                iterator.remove();
            }
        }
        return iterator;
    }
    /**
     * 获取集合长度
     * @param iterator 集合
     * @param <E>
     * @return
     */
    public static <E>int size(final Iterator iterator) {
        if(!BooleanHelper.hasLength(iterator)) {
            return 0;
        }

        int cnt = 0;
        while (iterator.hasNext()) {
            ++ cnt;
        }

        return cnt;
    }
    /**
     * 获取集合长度
     * @param collection 集合
     * @param <E>
     * @return
     */
    public static <E>int size(final E... collection) {
        return BooleanHelper.hasLength(collection) ? collection.length : 0;
    }
    /**
     * 获取集合长度
     * @param collection 集合
     * @param <E>
     * @return
     */
    public static <E>E[] elementNotNull(final E... collection) {
        if(!BooleanHelper.hasLength(collection)) {
            return null;
        }
        return collection;
    }
    /**
     * 获取集合长度
     * @param collection 集合
     * @param <E>
     * @return
     */
    public static <E>int sizeNotNull(final E... collection) {
        E[] es = elementNotNull(collection);
        return BooleanHelper.hasLength(es) ? es.length : 0;
    }
    /**
     * 获取集合长度
     * @param kvMap 集合
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V>int size(final Map<K, V> kvMap) {
        return BooleanHelper.hasLength(kvMap) ? kvMap.size() : 0;
    }
    /**
     * 获取集合长度
     * @param kvMap 集合
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V>int sizeNotNull(final Map<K, V> kvMap) {
        if(!BooleanHelper.hasLength(kvMap)) {
            return 0;
        }
        int cnt = 0;
        for (K k : kvMap.keySet()) {
            if(null != k) {
                ++ cnt;
            }
        }
        return cnt;
    }
    /**
     * timeUnit 转化为毫秒
     * @param time 时间
     * @param timeUnit 时间类型
     * @return
     * @throws InterruptedException
     */
    public static long timeUnit(final long time, final TimeUnit timeUnit) throws InterruptedException {
        return timeUnit.toMillis(time);
    }
}
