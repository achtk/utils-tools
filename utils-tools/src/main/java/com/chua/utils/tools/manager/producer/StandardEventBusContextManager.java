package com.chua.utils.tools.manager.producer;

import com.chua.utils.tools.collects.MultiValueMap;
import com.chua.utils.tools.manager.EventBusContextManager;
import com.chua.utils.tools.manager.eventbus.EventBus;
import com.chua.utils.tools.manager.eventbus.ServerEventBus;

import java.util.Collection;
import java.util.List;

/**
 * 标准的消息总线管理器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public class StandardEventBusContextManager implements EventBusContextManager {

    private static final MultiValueMap<String, EventBus> CONCURRENT_HASH_MAP = new MultiValueMap<>();

    @Override
    public void registerEventBus(String name, EventBus eventBus, Object object) {
        CONCURRENT_HASH_MAP.add(name, eventBus);
        eventBus.register(object);
        if (eventBus instanceof ServerEventBus) {
            ((ServerEventBus) eventBus).setName(name);
            ((ServerEventBus) eventBus).setObject(object);
        }
    }

    @Override
    public void sendEventBus(String name, Object message) {
        if (!CONCURRENT_HASH_MAP.containsKey(name)) {
            return;
        }
        List<EventBus> eventBuses = CONCURRENT_HASH_MAP.get(name);
        for (EventBus eventBus : eventBuses) {
            if (eventBus instanceof ServerEventBus) {
                ((ServerEventBus) eventBus).post(name, message);
            } else {
                eventBus.post(message);
            }
        }
    }

    @Override
    public void close() throws Exception {
        Collection<List<EventBus>> values = CONCURRENT_HASH_MAP.values();
        values.forEach(eventBuses -> {
            if(eventBuses instanceof ServerEventBus) {
                try {
                    ((ServerEventBus) eventBuses).close();
                } catch (Exception ignore) {

                }
            }
        });
    }
}
