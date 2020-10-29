package com.chua.utils.netx.function;

/**
 * ZSetProducer提供者
 * @author CH
 * @version 1.0.0
 * @since 2020/10/29
 */
public interface RListProducer<K, V> {
    /**
     * 获取Zset
     * @return
     */
    RList<K, V> getRList();
}
