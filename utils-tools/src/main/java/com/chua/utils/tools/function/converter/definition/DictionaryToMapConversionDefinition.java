package com.chua.utils.tools.function.converter.definition;

import com.chua.utils.tools.util.MapUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Map;

/**
 * Dictionary -> Map
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/7
 */
public class DictionaryToMapConversionDefinition implements TypeConversionDefinition<Dictionary, Map> {


    @Nullable
    @Override
    public Map convert(Dictionary source) {
        if (null == source) {
            return Collections.emptyMap();
        }
        return MapUtils.toMap(source);
    }
}
