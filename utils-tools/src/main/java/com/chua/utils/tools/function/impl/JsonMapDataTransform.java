package com.chua.utils.tools.function.impl;

import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.function.IDataTransform;

import java.util.Map;

/**
 * map与json转化
 * @author CH
 */
public class JsonMapDataTransform implements IDataTransform<String, Map<String, Object>> {
    @Override
    public String transTo(Map<String, Object> stringObjectMap) {
        return JsonHelper.toJson(stringObjectMap);
    }

    @Override
    public Map<String, Object> transFrom(String entity) {
        return JsonHelper.fromJson2Map(entity);
    }
}
