package com.chua.utils.tools.function.impl;

import com.chua.utils.tools.function.IDataTransform;
import pl.jalokim.propertiestojson.util.PropertiesToJsonConverter;

import java.util.Map;
import java.util.Properties;

/**
 * JSON TO Properties
 * @author CH
 * @date 2020-09-30
 */
public class MapJsonDataTransfrom implements IDataTransform<String, Map<String, Object>> {

    @Override
    public String transfrom(Map<String, Object> map) {
        return new PropertiesToJsonConverter().convertFromValuesAsObjectMap(map);
    }
}
