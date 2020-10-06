package com.chua.utils.netx.factory;

import com.chua.utils.tools.properties.NetxProperties;

/**
 * 消费工厂
 * @author CH
 * @version 1.0.0
 * @className INetxFactory
 * @since 2020/8/5 11:37
 */
public interface INetxFactory<Client> extends AutoCloseable {
    /**
     * 基础配置
     * @param netxProperties 基础配置
     */
    public void configure(NetxProperties netxProperties);

    /**
     * 获取客户端
     * @return
     */
    public Client client();
    /**
     * 启动
     */
    public void start();

    /**
     * 是否启动
     * @return
     */
    public boolean isStart();


}
