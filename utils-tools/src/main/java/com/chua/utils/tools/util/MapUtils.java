package com.chua.utils.tools.util;

import com.chua.utils.tools.collects.map.MapOperableHelper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * map工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/6
 */
public class MapUtils extends MapOperableHelper {
    /**
     * 合并姐
     *
     * @param source 集合
     * @return 合并的集合
     */
    public static <K, V> Map<K, V> merge(final Map<K, V>... source) {
        if (null == source) {
            return Collections.emptyMap();
        }
        Map<K, V> result = new HashMap<>();
        for (Map<K, V> map : source) {
            if (isEmpty(map)) {
                continue;
            }
            result.putAll(map);
        }
        return result;
    }
}
