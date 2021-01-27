package com.chua.utils.tools.function.impl;

import com.chua.utils.tools.function.JsonPath;
import com.chua.utils.tools.util.JsonUtils;
import com.chua.utils.tools.util.MapUtils;
import com.chua.utils.tools.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * json path implement
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/26
 */
public class JsonPathImpl implements JsonPath {

    private final Map<String, Object> profile;
    private String json;

    public JsonPathImpl(String json) {
        this.json = json;
        this.profile = MapUtils.levelsMapClose(JsonUtils.fromJson2Map(json));
    }

    @Override
    public List<Object> find(String path) {
        List<Object> result = new ArrayList<>();
        for (String key : profile.keySet()) {
            if (StringUtils.wildcardMatch(key, path)) {
                result.add(profile.get(key));
            }
        }
        return result;
    }


    @Override
    public Map<String, Object> findMap(String path) {
        Map<String, Object> result = new HashMap<>();
        for (String key : profile.keySet()) {
            if (StringUtils.wildcardMatch(key, path)) {
                result.put(key, profile.get(key));
            }
        }
        return MapUtils.levelsMapOpen(result);
    }
}
