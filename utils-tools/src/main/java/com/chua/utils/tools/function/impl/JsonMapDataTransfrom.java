package com.chua.utils.tools.function.impl;

import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.function.IDataTransform;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * JSON TO Map
 *
 * @author CH
 * @date 2020-09-30
 */
public class JsonMapDataTransfrom implements IDataTransform<Map<String, Object>, String> {
    private String keyPrefix = ".";

    @Override
    public Map<String, Object> transfrom(String s) {
        return transfrom(s, keyPrefix);
    }

    public Map<String, Object> transfrom(String jsonStr, String keyPrefix) {
        if (keyPrefix == null) {
            keyPrefix = "";
        }

        Map<String, Object> keyValueMap = new TreeMap<>();

        /*
         * 当值为字符串数组（如："pages_en":["one","two","three"]），递归解析字符串时，会抛异常（如：JSON.parse("one") ）。
         * 这里进行异常捕获，按键值对进行储存。
         * 详情查看 src/main/java/org/kwok/util/json/Test_OrgJson_Parse.java 。
         */
        Object obj;
        try {
            obj = JsonHelper.fromJson2Map(jsonStr);
        } catch (Exception e1) {
            try {
                obj = JsonHelper.fromJson2List(jsonStr);
            } catch (Exception e2) {
                obj = jsonStr;
            }
        }

        if (obj instanceof Map) {
            Map<String, Object> jsonObject = (Map<String, Object>) obj;
            Iterator<String> keys = jsonObject.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                if (jsonObject.get(key) instanceof Map) {
                    String tempKeyPrefix = keyPrefix + key + ".";
                    keyValueMap.putAll(transfrom(jsonObject.get(key).toString(), tempKeyPrefix));
                } else if (jsonObject.get(key) instanceof List) {
                    String tempKeyPrefix = keyPrefix + key + ".";
                    keyValueMap.putAll(transfrom(jsonObject.get(key).toString(), tempKeyPrefix));
                } else {
                    /*
                     * 处理属性值为 null 的情况，这里转为空字符串。
                     */
                    if (jsonObject.get(key) == null) {
                        keyValueMap.put(keyPrefix + key, "");
                    } else {
                        keyValueMap.put(keyPrefix + key, jsonObject.get(key));
                    }
                }
            }
        } else if (obj instanceof List) {
            List<Object> jsonArray = (List<Object>) obj;
            for (int i = 0; i < jsonArray.size(); i++) {
                String tempKeyPrefix = keyPrefix == "" ? keyPrefix + "[" + i + "]" + "." : keyPrefix.substring(0, keyPrefix.length() - 1) + "[" + i + "]" + ".";
                /*
                 * 处理数组中元素为 null 的情况，这里转为空字符串。如：{"pages":[1,2,null]}。
                 */
                if (jsonArray.get(i) == null) {
                    keyValueMap.putAll(transfrom("", tempKeyPrefix));
                } else {
                    keyValueMap.putAll(transfrom(jsonArray.get(i).toString(), tempKeyPrefix));
                }

            }
        } else {
            /*
             * 当值为数组，递归时进入该分支。如：{"pages":[1,2,3]}。
             */
            keyValueMap.put(keyPrefix == "" ? keyPrefix : keyPrefix.substring(0, keyPrefix.length() - 1), jsonStr);
        }
        return keyValueMap;
    }
}
