package com.chua.utils.netx.function;

import com.chua.utils.tools.function.Matcher;

import java.util.List;

/**
 * ZSet
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/29
 */
public interface RList<K, V> {
    /**
     * 删除
     *
     * @param k 索引
     * @throws Exception Exception
     */
    void delete(K k) throws Exception;

    /**
     * 添加
     *
     * @param k 索引
     * @param v 值
     * @throws Exception Exception
     */
    void add(K k, V v) throws Exception;

    /**
     * 添加
     *
     * @param k  索引
     * @param vs 值
     * @throws Exception Exception
     */
    void addAll(K k, List<V> vs) throws Exception;

    /**
     * 添加
     *
     * @param k   索引
     * @param v   值
     * @param ttl 超时时间(秒)
     * @throws Exception Exception
     */
    void add(K k, V v, int ttl) throws Exception;

    /**
     * 添加
     *
     * @param k   索引
     * @param vs  值
     * @param ttl 超时时间(秒)
     * @throws Exception Exception
     */
    void addAll(K k, List<V> vs, int ttl) throws Exception;

    /**
     * 是否存在
     *
     * @param k 索引
     * @return boolean
     * @throws Exception Exception
     */
    boolean exist(K k) throws Exception;

    /**
     * 遍历
     *
     * @param v 索引
     * @param matcher 匹配器
     * @throws Exception
     */
    void iterator(K v, Matcher<V> matcher) throws Throwable;
}
