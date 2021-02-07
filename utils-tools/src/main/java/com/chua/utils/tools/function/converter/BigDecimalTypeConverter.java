package com.chua.utils.tools.function.converter;

import java.math.BigDecimal;

/**
 * BigDecimal转化
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/26
 */
public class BigDecimalTypeConverter implements TypeConverter<BigDecimal> {

    @Override
    public Class<BigDecimal> getType() {
        return BigDecimal.class;
    }
}
