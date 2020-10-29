package com.chua.utils.netx.function;

/**
 * kv操作
 * @author CH
 * @version 1.0.0
 * @since 2020/10/29
 */
public interface RKvProducer<K, V> {
    /**
     * 获取Kv接口
     * @return
     */
    RKv<K, V> getKv();
}
