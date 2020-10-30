package com.chua.utils.netx.factory;

import com.chua.utils.tools.properties.NetProperties;

/**
 * 消费工厂
 * @author CH
 * @version 1.0.0
 * @since 2020/8/5 11:37
 */
public interface INetFactory<Client> extends AutoCloseable {
    /**
     * 基础配置
     * @param netProperties 基础配置
     */
    void configure(NetProperties netProperties);

    /**
     * 获取客户端
     * @return Client
     */
    Client client();
    /**
     * 启动
     */
    void start();

    /**
     * 是否启动
     * @return boolean
     */
    boolean isStart();


}
