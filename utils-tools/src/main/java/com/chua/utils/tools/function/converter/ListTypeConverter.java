package com.chua.utils.tools.function.converter;

import java.util.List;

/**
 * 字符串转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
public class ListTypeConverter implements TypeConverter<List> {

    @Override
    public Class<List> getType() {
        return List.class;
    }
}
