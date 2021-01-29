package com.chua.utils.tools.util;

import com.chua.utils.tools.common.ObjectHelper;

/**
 * Object工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/29
 */
public class ObjectUtils extends ObjectHelper {

    /**
     * 是否全部为空
     *
     * @param objects 对象集合
     * @return 全部为空返回true
     */
    public static boolean isAllEmpty(Object... objects) {
        if (null == objects || objects.length == 0) {
            return true;
        }
        boolean isAllEmpty = true;
        for (Object object : objects) {
            if (null != object) {
                isAllEmpty = false;
                break;
            }

        }
        return isAllEmpty;
    }


    /**
     * 是否全部为空
     *
     * @param objects 对象集合
     * @return 全部为空返回true
     */
    public static boolean isAnyEmpty(Object... objects) {
        if (null == objects || objects.length == 0) {
            return true;
        }
        boolean isAllEmpty = false;
        for (Object object : objects) {
            if (null == object) {
                isAllEmpty = true;
                break;
            }

        }
        return isAllEmpty;
    }
}
