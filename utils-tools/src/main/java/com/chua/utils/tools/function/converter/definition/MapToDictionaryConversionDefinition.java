package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.empty.EmptyOrBase;
import com.chua.utils.tools.util.MapUtils;

import javax.annotation.Nullable;
import java.util.Dictionary;
import java.util.Map;

/**
 * Map -> Dictionary
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class MapToDictionaryConversionDefinition implements TypeConversionDefinition<Map, Dictionary> {

    @Nullable
    @Override
    public Dictionary convert(Map source) {
        if (null == source) {
            return EmptyOrBase.EMPTY_PROPERTIES;
        }
        return MapUtils.toDictionary(source);
    }
}
