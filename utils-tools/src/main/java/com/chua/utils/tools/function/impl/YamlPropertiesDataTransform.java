package com.chua.utils.tools.function.impl;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.function.IDataTransform;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import pl.jalokim.propertiestojson.util.PropertiesToJsonConverter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * yaml转json
 * <p>
 * {
 *   "spring": {
 *       "server": 9090,
 *       "test": [1, 2]
 *   }
 *}
 *
 * ===>
 *
 *  {spring.test[0]=1, spring.test[1]=2, spring.server=9090}
 *
 * </p>
 * @author CH
 */
public class YamlPropertiesDataTransform implements IDataTransform<String, Properties> {

    private static String DOT = ".";

    @Override
    public Properties transFrom(String yamlString) {
        Map<String, Object> source = JsonHelper.fromJson2Map(yamlString);
        Properties properties = new Properties();
        for (Map.Entry<String, Object> entry : source.entrySet()) {
            Object value = entry.getValue();
            String key = entry.getKey();
            if(value instanceof List) {
                listIntoProperties(key, (List<Object>) value, properties);
                continue;
            }
            if(value instanceof Map) {
                mapIntoProperties(key, (Map<String, Object>) value, properties);
                continue;
            }
            properties.put(key, value);
        }

        return properties;
    }

    @Override
    public String transTo(Properties properties) {
        return new PropertiesToJsonConverter().convertToJson(properties);
    }

    /**
     * list渲染到properties
     * @param key 上层索引
     * @param value 上层数据
     * @param properties properties
     */
    private static void mapIntoProperties(String key, Map<String, Object> value, Properties properties) {
        if (!BooleanHelper.hasLength(value)) {
            return;
        }
        for (Map.Entry<String, Object> entry : value.entrySet()) {
            String key1 = entry.getKey();
            Object value1 = entry.getValue();
            String newKey = key + DOT + key1;
            if(value1 instanceof List) {
                listIntoProperties(newKey, (List<Object>) value1, properties);
                continue;
            }
            if(value1 instanceof Map) {
                mapIntoProperties(newKey, (Map<String, Object>) value1, properties);
                continue;
            }

            properties.put(newKey, value1);
        }
    }
    /**
     * list渲染到properties
     * @param key 上层索引
     * @param value 上层数据
     * @param properties properties
     */
    private static void listIntoProperties(String key, List<Object> value, Properties properties) {
        if(!BooleanHelper.hasLength(value)) {
            return;
        }

        for (int i = 0; i < value.size(); i++) {
            Object item = value.get(i);
            String newKey = key + "[" + i + "]";
            if(item instanceof List) {
                properties.put(newKey, JsonHelper.toJson(item));
                continue;
            }
            if(item instanceof Map) {
                properties.put(newKey, JsonHelper.toJson(item));
                continue;
            }
            properties.put(newKey, item);
        }
    }
}
