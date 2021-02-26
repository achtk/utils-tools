package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.empty.EmptyOrBase;
import com.chua.utils.tools.util.MapUtils;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Properties;

/**
 * Map -> Properties
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class MapToPropertiesConversionDefinition implements TypeConversionDefinition<Map, Properties> {

    @Nullable
    @Override
    public Properties convert(Map source) {
        if (null == source) {
            return EmptyOrBase.EMPTY_PROPERTIES;
        }
        return MapUtils.toProp(source);
    }
}
