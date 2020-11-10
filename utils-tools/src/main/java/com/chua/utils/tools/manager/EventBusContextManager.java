package com.chua.utils.tools.manager;

import java.util.List;

/**
 * 消息总线
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public interface EventBusContextManager {

    /**
     * 获取所有消息总线
     *
     * @param name     总线名称
     * @param eventBus 消息
     */
    void registerEventBus(String name, List<Object> eventBus);

    /**
     * 发送消息到总线
     *
     * @param name    总线名称
     * @param message 消息
     */
    void sendEventBus(String name, Object message);
}
