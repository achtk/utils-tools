package com.chua.utils.tools.manager;

import com.chua.utils.tools.manager.eventbus.EventBus;

import java.util.List;

/**
 * 消息总线
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 * @see com.chua.utils.tools.manager.producer.StandardEventBusContextManager
 * @see com.chua.utils.tools.manager.producer.StandardContextManager
 */
public interface EventBusContextManager extends AutoCloseable{

    /**
     * 获取所有消息总线
     *
     * @param name     总线名称
     * @param eventBus 消息总线
     * @param object   注册的对象
     */
    void registerEventBus(String name, EventBus eventBus, Object object);

    /**
     * 发送消息到总线
     *
     * @param name    总线名称
     * @param message 消息
     */
    void sendEventBus(String name, Object message);
}
