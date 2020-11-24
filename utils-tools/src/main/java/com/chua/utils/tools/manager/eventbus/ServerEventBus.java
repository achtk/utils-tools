package com.chua.utils.tools.manager.eventbus;

/**
 * 消息总线
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/24
 */
public interface ServerEventBus extends EventBus {
    /**
     * 名称
     *
     * @param name 名称
     */
    void setName(String name);

    /**
     * 对象
     *
     * @param object 对象
     */
    void setObject(Object object);
}
