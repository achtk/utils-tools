package com.chua.utils.netx.resolver.node;

import com.chua.utils.netx.resolver.entity.NetNodeConf;

import java.io.IOException;
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
     * @throws IOException
     */
    byte[] addNode(NetNodeConf netNodeConf, byte[] data) throws IOException;

    /**
     * 删除节点
     *
     * @param netNodeConf 节点
     * @return 数据
     * @throws IOException
     */
    byte[] deleteNode(NetNodeConf netNodeConf) throws IOException;

    /**
     * 节点状态
     *
     * @param netNodeConf 节点
     * @return 数据
     * @throws IOException
     */
    boolean existNode(NetNodeConf netNodeConf) throws IOException;

    /**
     * 监听
     *
     * @param netNodeConf 节点
     * @param consumer    回调
     * @throws IOException
     */
    void monitor(NetNodeConf netNodeConf, Consumer<T> consumer) throws IOException;
}
