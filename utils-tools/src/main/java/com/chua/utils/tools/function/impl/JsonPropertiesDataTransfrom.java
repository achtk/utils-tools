package com.chua.utils.tools.function.impl;

import com.chua.utils.tools.function.IDataTransform;

import java.util.Map;
import java.util.Properties;

/**
 * JSON TO Properties
 * @author CH
 * @date 2020-09-30
 */
public class JsonPropertiesDataTransfrom implements IDataTransform<Properties, String> {

    private IDataTransform<Map<String, Object>, String> dataTransform = new JsonMapDataTransfrom();

    @Override
    public Properties transfrom(String s) {
        Properties properties = new Properties();
        properties.putAll(dataTransform.transfrom(s));
        return properties;
    }
}
