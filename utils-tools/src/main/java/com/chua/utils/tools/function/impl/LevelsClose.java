package com.chua.utils.tools.function.impl;

import com.chua.utils.tools.function.Levels;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 层级压缩
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/27
 */
public class LevelsClose implements Levels {
    @Override
    public Map<String, Object> apply(Map<String, Object> stringObjectMap) {
        Map<String, Object> properties1 = new HashMap<>();
        analysisHierarchicalAnalysis(stringObjectMap, properties1);
        return properties1;
    }


    /**
     * 解析map
     *
     * @param map    数据
     * @param result 返回结果集
     */
    private static void analysisHierarchicalAnalysis(Map<String, Object> map, final Map<String, Object> result) {
        if(null == map) {
            return;
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            dataFormatProfileHierarchicalAnalysis(key, value, result);
        }
    }


    /**
     * 数据格式化成 配置格式
     *
     * @param parentName  父配置名称
     * @param valueObject 数据
     * @param result      返回对象
     */
    private static void dataFormatProfileHierarchicalAnalysis(String parentName, Object valueObject, Map<String, Object> result) {
        if (valueObject instanceof Map) {
            doAnalysisMapValueHierarchicalAnalysis(parentName, (Map<String, Object>) valueObject, result);
        } else if (valueObject instanceof List) {
            doAnalysisListValueHierarchicalAnalysis(parentName, (List<Object>) valueObject, result);
        } else {
            result.put(parentName, valueObject);
        }
    }

    /**
     * 循环解析 Map
     *
     * @param parentName 父配置名称
     * @param map        数据
     * @param result     返回对象
     */
    private static void doAnalysisMapValueHierarchicalAnalysis(String parentName, Map<String, Object> map, Map<String, Object> result) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            dataFormatProfileHierarchicalAnalysis(parentName + "." + key, value, result);
        }
    }

    /**
     * 循环解析 List
     *
     * @param parentName 父配置名称
     * @param source     数据
     * @param result     返回对象
     */
    private static void doAnalysisListValueHierarchicalAnalysis(String parentName, List<Object> source, Map<String, Object> result) {
        for (int i = 0; i < source.size(); i++) {
            dataFormatProfileHierarchicalAnalysis(parentName + "[" + i + "]", source.get(i), result);
        }
    }
}
