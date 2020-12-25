package com.chua.utils.tools.util;

import com.chua.utils.tools.collects.collections.CollectionHelper;

import java.util.Collection;
import java.util.Map;

/**
 * 集合工具类
 *
 * @author CH
 * @version 1.0.0
 * @see com.chua.utils.tools.collects.collections.CollectionHelper
 * @since 2020/12/22
 */
public class CollectionUtils extends CollectionHelper {
    /**
     * 获取索引对应的数据
     *
     * @param source 数据
     * @param index  索引
     * @param <T>    类型
     * @return 数据
     */
    public static <T> T findFirst(final Collection<T> source, final int index) {
        return find(source, 0);
    }

    /**
     * 获取索引对应的数据
     *
     * @param source 数据
     * @param index  索引
     * @param <T>    类型
     * @return 数据
     */
    public static <T> T findLast(final Collection<T> source, final int index) {
        return findReverse(source, -1);
    }

    /**
     * 获取索引对应的数据
     *
     * @param source 数据
     * @param index  索引
     * @param <T>    类型
     * @return 数据
     */
    public static <T> T findAny(final Collection<T> source, final int index) {
        if (index > 0) {
            return find(source, index);
        }
        return findReverse(source, -1);
    }

    /**
     * 获取索引对应的数据
     *
     * @param source 数据
     * @param index  索引
     * @param <T>    类型
     * @return 数据
     */
    public static <T> T findReverse(final Collection<T> source, final int index) {
        if (null == source || index > 0) {
            return null;
        }
        int length = source.size();
        int realIndex = length + index;
        if (realIndex > length) {
            return null;
        }
        int count = 0;
        for (T t : source) {
            if (count == realIndex) {
                return t;
            }
        }
        return null;
    }

    /**
     * 获取索引对应的数据
     *
     * @param source 数据
     * @param index  索引
     * @param <T>    类型
     * @return 数据
     */
    public static <T> T find(final Collection<T> source, final int index) {
        if (null == source || index < 0) {
            return null;
        }
        int length = source.size();
        if (index > length) {
            return null;
        }
        int count = 0;
        for (T t : source) {
            if (count == index) {
                return t;
            }
        }
        return null;
    }

    /**
     * 有且只有唯一元素的集合返回唯一元素, 否则返回 null
     *
     * @param source 集合
     * @param <T>    元素
     * @return 有且只有唯一元素的集合返回唯一元素, 否则返回 null
     */
    public static <T> T findOnly(Map<?, T> source) {
        if (null == source || source.size() != 1) {
            return null;
        }
        return source.values().stream().findFirst().get();
    }

    /**
     * 有且只有唯一元素的集合返回唯一元素, 否则返回 null
     *
     * @param source 集合
     * @param <T>    元素
     * @return 有且只有唯一元素的集合返回唯一元素, 否则返回 null
     */
    public static <T> T findOnly(Collection<T> source) {
        if (null == source || source.size() != 1) {
            return null;
        }
        return source.stream().findFirst().get();
    }
}
