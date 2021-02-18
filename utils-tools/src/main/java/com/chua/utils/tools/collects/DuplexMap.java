package com.chua.utils.tools.collects;

import java.util.Map;

/**
 * 双向Map
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/17
 */
public interface DuplexMap<K, V> extends Map<K, V> {
    /**
     * 获取Key
     *
     * @param value 值
     * @return K
     */
    K getInverse(V value);

}
