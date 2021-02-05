package com.chua.utils.tools.classes.convertor;

import com.chua.utils.tools.empty.EmptyOrBase;
import com.google.common.base.Splitter;

/**
 * 字符串转换器
 *
 * @author CH
 * @version 1.0
 * @since 2020/10/24 21:07
 */
public class ArrayConvertor implements Convertor<Object[]> {
    @Override
    public Object[] convert(Object value) {
        if(null == value) {
            return EmptyOrBase.EMPTY_OBJECT;
        }

        if(value.getClass().isArray()) {
            return (Object[]) value;
        }
        if(value instanceof String) {
            return Splitter.on(delimiter()).omitEmptyStrings().trimResults().splitToList(value.toString()).toArray(new Object[0]);
        }


        return EmptyOrBase.EMPTY_OBJECT;
    }
}
