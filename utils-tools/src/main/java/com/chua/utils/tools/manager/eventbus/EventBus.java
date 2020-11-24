package com.chua.utils.tools.manager.eventbus;

/**
 * 消息总线
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/24
 */
public interface EventBus {
    /**
     * 注册组件
     *
     * @param object 组件
     */
    void register(Object object);

    /**
     * 注销组件
     *
     * @param object 组件
     */
    void unregister(Object object);

    /**
     * 发布消息
     *
     * @param event 消息
     */
    void post(Object event);
}
