package com.chua.utils.tools.classes.callback;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/10/12
 */

import java.lang.reflect.Field;

/**
 * 字段回调
 */
@FunctionalInterface
public interface FieldCallback {

    /**
     * 字段回调
     * @param field
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    void doWith(Field field) throws IllegalArgumentException, IllegalAccessException;
}
