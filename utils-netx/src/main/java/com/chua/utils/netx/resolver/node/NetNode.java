package com.chua.utils.netx.resolver.node;

import com.chua.utils.netx.resolver.entity.NetNodeConf;

import java.util.function.Consumer;

/**
 * 节点
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public interface NetNode<T> extends AutoCloseable {
    /**
     * 添加节点
     *
     * @param netNodeConf 节点
     * @param data        数据
     * @return 数据
     * @throws Exception
     */
    boolean addNode(NetNodeConf netNodeConf, String data) throws Exception;

    /**
     * 获取数据
     *
     * @param netNodeConf 节点
     * @return 数据
     * @throws Exception
     */
    String getValue(NetNodeConf netNodeConf) throws Exception;

    /**
     * 删除节点
     *
     * @param netNodeConf 节点
     * @return 数据
     * @throws Exception
     */
    boolean deleteNode(NetNodeConf netNodeConf) throws Exception;

    /**
     * 节点状态
     *
     * @param netNodeConf 节点
     * @return 数据
     * @throws Exception
     */
    boolean existNode(NetNodeConf netNodeConf) throws Exception;

    /**
     * 监听
     *
     * @param netNodeConf 节点
     * @param consumer    回调
     * @throws Exception
     */
    void monitor(NetNodeConf netNodeConf, Consumer<T> consumer) throws Exception;
}
