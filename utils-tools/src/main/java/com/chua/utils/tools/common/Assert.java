package com.chua.utils.tools.common;

import com.chua.utils.tools.collects.collections.CollectionHelper;

import java.util.Collection;
import java.util.Map;

/**
 * 断言
 *
 * @author CH
 */
public class Assert {
    private Assert() {
    }

    /**
     * @param expression
     * @param message
     */
    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * @param expression
     */
    public static void state(boolean expression) {
        state(expression, "[Assertion failed] - this state invariant must be true");
    }

    /**
     * 是否为空
     *
     * @param array 源数据
     */
    public static void notEmpty(Object array) {
        if (BooleanHelper.isEmpty(array)) {
            throw new IllegalArgumentException(array + "is not null");
        }
    }

    /**
     * 是否为空
     *
     * @param array 源数据
     */
    public static void assertEquals(Object array, Object array1) {
        if (!array.equals(array1)) {
            throw new IllegalArgumentException(array + "is not null");
        }
    }

    /**
     * 是否为空
     *
     * @param array   源数据
     * @param message 提示信息
     */
    public static void notEmpty(Object array, String message) {
        if (BooleanHelper.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 是否为空
     *
     * @param array   源数据
     * @param message 提示信息
     */
    public static void notEmpty(Object[] array, String message) {
        if (BooleanHelper.isEmpty(array)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 是否为空
     *
     * @param collection
     * @param message
     */
    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionHelper.isEmpty(collection)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 是否为空
     *
     * @param collection
     */
    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection,
                "[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
    }

    /**
     *
     * @param map
     */
    public static void notEmpty(Map<?, ?> map) {
        notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
    }

    /**
     * @param object
     * @param message
     */
    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }
    /**
     * @param object
     * @param message
     */
    public static void isNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
    /**
     * @param object
     */
    public static void isNotNull(Object object) {
       isNotNull(object, "[Assertion failed]-Object parameter must not be empty");
    }

    /**
     * @param object
     */
    public static void isNull(Object object) {
        isNull(object, "[Assertion failed] - the object argument must be null");
    }

    /**
     * 是否为空
     *
     * @param source  源数据
     * @param message 信息
     */
    public static void notNull(Object source, String message) {
        if (source == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 是否为空
     *
     * @param object
     */
    public static void notNull(Object object) {
        notNull(object, "[Assertion failed] - this argument is required; it must not be null");
    }


    /**
     * 是否为真
     *
     * @param expression
     * @param message
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 是否为真
     *
     * @param expression
     */
    public static void isTrue(boolean expression) {
        isTrue(expression, "[Assertion failed] - this expression must be true");
    }


    /**
     * 是否有长度
     *
     * @param text    文本
     * @param message 信息
     */
    public static void hasLength(String text, String message) {
        if (!StringHelper.hasLength(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * 是否有长度
     *
     * @param text
     */
    public static void hasLength(String text) {
        hasLength(text,
                "[Assertion failed] - this String argument must have length; it must not be null or empty");
    }

    /**
     * @param text
     * @param message
     */
    public static void hasText(String text, String message) {
        if (!StringHelper.hasText(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * @param textToSearch
     * @param substring
     * @param message
     */
    public static void doesNotContain(String textToSearch, String substring, String message) {
        if (StringHelper.hasLength(textToSearch) && StringHelper.hasLength(substring) &&
                textToSearch.contains(substring)) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * @param textToSearch
     * @param substring
     */
    public static void doesNotContain(String textToSearch, String substring) {
        doesNotContain(textToSearch, substring, "[Assertion failed] - this String argument must not contain the substring [" + substring + "]");
    }
}
