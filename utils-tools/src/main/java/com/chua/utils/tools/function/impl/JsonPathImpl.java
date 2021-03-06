package com.chua.utils.tools.function.impl;

import com.chua.utils.tools.collects.FlatHashMap;
import com.chua.utils.tools.collects.FlatMap;
import com.chua.utils.tools.function.JsonPath;
import com.chua.utils.tools.util.JsonUtils;
import com.chua.utils.tools.util.MapUtils;
import com.chua.utils.tools.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.chua.utils.tools.constant.NumberConstant.DEFAULT_INITIAL_CAPACITY;

/**
 * json path implement
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/26
 */
public class JsonPathImpl implements JsonPath {

    private final Map<String, Object> profile;
    private final String json;

    public JsonPathImpl(String json) {
        this.json = json;
        this.profile = MapUtils.levelsMapClose(JsonUtils.fromJson2Map(json));
    }

    @Override
    public List<Object> find(String path) {
        List<Object> result = new ArrayList<>();
        for (Map.Entry<String, Object> entry : profile.entrySet()) {
            if (StringUtils.wildcardMatch(entry.getKey(), path)) {
                result.add(entry.getValue());
            }
        }
        return result;
    }


    @Override
    public Map<String, Object> findMap(String path) {
        Map<String, Object> result = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
        for (Map.Entry<String, Object> entry : profile.entrySet()) {
            if (StringUtils.wildcardMatch(entry.getKey(), path)) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        if (result.isEmpty()) {
            return result;
        }
        return MapUtils.levelsMapOpen(result);
    }


    @Override
    public FlatMap flatMap(String path) {
        Map<String, Object> result = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
        for (Map.Entry<String, Object> entry : profile.entrySet()) {
            if (StringUtils.wildcardMatch(entry.getKey(), path)) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        if (result.isEmpty()) {
            return new FlatHashMap();
        }
        return new FlatHashMap(MapUtils.levelsMapOpen(result));
    }

    @Override
    public void set(String path, Object value) {
        Map<String, Object> result = new HashMap<>(DEFAULT_INITIAL_CAPACITY);
        for (String key : profile.keySet()) {
            if (StringUtils.wildcardMatch(key, path)) {
                result.put(key, value);
            }
        }
        if (result.isEmpty()) {
            profile.put(path, value);
        } else {
            profile.putAll(result);
        }
    }
}
