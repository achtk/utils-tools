package com.chua.utils.tools.function.producer;

/**
 * 简单服务启动关闭
 *
 * @author CH
 */
public interface SimpleServerProducer extends AutoCloseable {
    /**
     * 启动
     *
     * @throws Exception
     */
    void start() throws Exception;
}
