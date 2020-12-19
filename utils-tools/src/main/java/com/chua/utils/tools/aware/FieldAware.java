package com.chua.utils.tools.aware;

import java.lang.reflect.Field;

/**
 * 字段提供接口
 * @author CH
 * @version 1.0.0
 * @since 2020/12/19
 */
public interface FieldAware {
    /**
     * 设置字段
     * @param field 字段
     */
    void setField(Field field);
}
