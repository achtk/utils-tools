package com.chua.utils.tools.util;

import com.chua.utils.tools.common.BooleanHelper;

import java.util.*;

/**
 * boolean 工具类
 *
 * @author CH
 * @version 1.0.0
 * @see com.chua.utils.tools.common.BooleanHelper
 * @since 2020/12/22
 */
public class BooleanUtils extends BooleanHelper {

    /**
     * 是否为空
     *
     * @param collection 集合
     * @return 空返回true
     */
    public static boolean isEmpty(final Collection<?> collection) {
        return !hasLength(collection);
    }

    /**
     * 是否为空
     *
     * @param map 集合
     * @return 空返回true
     */
    public static boolean isEmpty(final Map<?, ?> map) {
        return !hasLength(map);
    }

    /**
     * 是否为空
     *
     * @param iterator 集合
     * @return 空返回true
     */
    public static boolean isEmpty(final Iterator<?> iterator) {
        return !hasLength(iterator);
    }
    /**
     * 是否为空
     *
     * @param dictionary 集合
     * @return 空返回true
     */
    public static boolean isEmpty(final Dictionary<?, ?> dictionary) {
        return !hasLength(dictionary);
    }

    /**
     * 是否为空
     *
     * @param enumeration 集合
     * @return 空返回true
     */
    public static boolean isEmpty(final Enumeration<?> enumeration) {
        return null == enumeration || !enumeration.hasMoreElements();
    }
    /**
     * 是否为空
     *
     * @param arrays 集合
     * @return 空返回true
     */
    public static boolean isEmpty(final Object[] arrays) {
        return null == arrays || arrays.length == 0;
    }
}
