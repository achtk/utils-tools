package com.chua.utils.netx.function;

/**
 * mq
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/29
 */
public interface RMq {
    /**
     * 推数据
     *
     * @param key   索引
     * @param value 值
     */
    void lpush(String key, String... value) throws Exception;

    /**
     * 获取数据
     *
     * @param key 索引
     * @return
     * @throws Exception
     */
    String lpop(String key) throws Exception;
}
