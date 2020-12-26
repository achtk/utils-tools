package com.chua.utils.tools.util;

import com.chua.utils.tools.common.ArraysHelper;

/**
 * 数组
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/26
 */
public class ArrayUtils extends ArraysHelper {
    /**
     * 获取不为空的数组
     * <pre>
     *     e.g. ArrayUtils.ifEmpty([], [1]) = [1]
     *     e.g. ArrayUtils.ifEmpty(null, [1]) = [1]
     *     e.g. ArrayUtils.ifEmpty([2], [1]) = [2]
     * </pre>
     *
     * @param array1 数组1
     * @param array2 数组2
     * @return 不为空的数组
     */
    public static <T> T[] ifEmpty(T[] array1, T[] array2) {
        return BooleanUtils.isEmpty(array1) ? array2 : array1;
    }
}
