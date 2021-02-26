package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.empty.EmptyOrBase;
import com.chua.utils.tools.util.MapUtils;

import javax.annotation.Nullable;
import java.util.Dictionary;
import java.util.Properties;

/**
 * Properties ->  Map
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class PropertiesToDictionaryConversionDefinition implements TypeConversionDefinition<Properties, Dictionary> {

    @Nullable
    @Override
    public Dictionary convert(Properties source) {
        if (null == source) {
            return EmptyOrBase.EMPTY_PROPERTIES;
        }
        return MapUtils.toDictionary(source);
    }
}
