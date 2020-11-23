package com.chua.utils.netx.resolver.collector;

import com.chua.utils.netx.resolver.entity.NetCollectorConf;

import java.util.Set;

/**
 * 收集器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/23
 */
public interface NetCollector<Callback> {
    /**
     * 创建集合
     *
     * @param netCollectorConf 集合
     * @return 成功返回true
     */
    boolean createCollection(NetCollectorConf netCollectorConf);

    /**
     * 删除集合
     *
     * @param collectionName 集合名称
     * @return 成功返回true
     */
    boolean deleteCollection(String collectionName);

    /**
     * 获取集合
     *
     * @return 成功返回true
     */
    Set<String> listCollection();

    /**
     * 集合是否存在
     *
     * @param collectionName 集合名称
     * @return 成功返回true
     */
    boolean existCollection(String collectionName);

    /**
     * 执行
     *
     * @param action 动作
     * @param tClass 类型
     * @param <T>    类型
     * @return 数据
     */
    <T> T execute(Callback action, Class<T> tClass);
}
