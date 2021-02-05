package com.chua.utils.tools.util;

import com.chua.utils.tools.common.ArraysHelper;
import com.chua.utils.tools.empty.EmptyOrBase;

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

    /**
     * 获取数组类型
     *
     * @param params 数组
     * @return 数组类型
     */
    public static Class<?>[] toClass(Object[] params) {
        if(null == params) {
            return EmptyOrBase.EMPTY_CLASS;
        }
        Class<?>[] result = new Class<?>[params.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = params[i] == null ? void.class : params[i].getClass();
        }
        return result;
    }

    /**
     * 判断元素是否全部无效{null, [], [null]}
     *
     * @param values 集合
     * @return 全部无效返回true
     */
    public static boolean isNothing(Object[] values) {
        if (null == values || values.length == 0) {
            return true;
        }
        for (Object value : values) {
            if (null != value) {
                return false;
            }
        }

        return true;
    }
}
