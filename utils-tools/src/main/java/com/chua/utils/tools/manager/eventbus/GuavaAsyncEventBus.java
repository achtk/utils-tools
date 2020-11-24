package com.chua.utils.tools.manager.eventbus;

import com.chua.utils.tools.common.ThreadHelper;

/**
 * guava-eventbus
 *
 * @author CH
 * @version 1.0.0
 * @see com.google.common.eventbus.EventBus
 * @since 2020/11/24
 */
public class GuavaAsyncEventBus implements EventBus {

    private com.google.common.eventbus.AsyncEventBus eventBus = new com.google.common.eventbus.AsyncEventBus(ThreadHelper.newSingleThreadExecutor());

    @Override
    public void register(Object object) {
        eventBus.register(object);
    }

    @Override
    public void unregister(Object object) {
        eventBus.unregister(object);
    }

    @Override
    public void post(Object event) {
        eventBus.post(event);
    }
}
