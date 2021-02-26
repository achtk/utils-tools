package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.empty.EmptyOrBase;
import com.chua.utils.tools.util.MapUtils;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Properties;

/**
 * Properties ->  Map
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class PropertiesToMapConversionDefinition implements TypeConversionDefinition<Properties, Map> {

    @Nullable
    @Override
    public Map convert(Properties source) {
        if (null == source) {
            return EmptyOrBase.EMPTY_PROPERTIES;
        }
        return MapUtils.toMap(source);
    }
}
