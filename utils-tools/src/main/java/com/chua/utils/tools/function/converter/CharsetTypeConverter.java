package com.chua.utils.tools.function.converter;

import java.nio.charset.Charset;

/**
 * Charset 转化
 * <br />默认返回: UTF-8
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/31
 */
public class CharsetTypeConverter implements TypeConverter<Charset> {

    @Override
    public Class<Charset> getType() {
        return Charset.class;
    }
}
