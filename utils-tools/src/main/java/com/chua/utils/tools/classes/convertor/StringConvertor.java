package com.chua.utils.tools.classes.convertor;

/**
 * 字符串转换器
 *
 * @author CH
 * @version 1.0
 * @since 2020/10/24 21:07
 */
public class StringConvertor implements Convertor<String> {
    @Override
    public String convert(Object value) {
        if(null == value) {
            return null;
        }
        if(value instanceof String) {
            return value.toString();
        }
        return null;
    }
}
