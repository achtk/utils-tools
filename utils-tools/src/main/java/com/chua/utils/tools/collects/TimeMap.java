package com.chua.utils.tools.collects;

import java.util.Map;
import java.util.function.BiConsumer;

/**
 * the time map
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/17
 */
public interface TimeMap<K, V> extends Map<K, V> {
    /**
     * 检测器
     * @param consumer 回调
     */
    void detector(BiConsumer<K, V> consumer);
}
