package com.chua.utils.tools.manager.producer;

import com.chua.utils.tools.manager.EventBusContextManager;
import com.google.common.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 标准的消息总线管理器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
class StandardEventBusContextManager implements EventBusContextManager {

    private static final ConcurrentHashMap<String, EventBus> CONCURRENT_HASH_MAP = new ConcurrentHashMap<>();

    @Override
    public void registerEventBus(String name, List<Object> eventBus) {
        EventBus eventBus1 = null;
        if (CONCURRENT_HASH_MAP.containsKey(name)) {
            eventBus1 = CONCURRENT_HASH_MAP.get(name);
        } else {
            eventBus1 = new EventBus(name);
            CONCURRENT_HASH_MAP.put(name, eventBus1);
        }

        for (Object bus : eventBus) {
            eventBus1.register(bus);
        }
    }

    @Override
    public void sendEventBus(String name, Object message) {
        if (!CONCURRENT_HASH_MAP.containsKey(name)) {
            return;
        }
        EventBus eventBus = CONCURRENT_HASH_MAP.get(name);
        eventBus.post(message);
    }
}
