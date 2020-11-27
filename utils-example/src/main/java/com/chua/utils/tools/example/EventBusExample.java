package com.chua.utils.tools.example;

import com.chua.utils.event.eventbus.VertxEventBus;
import com.chua.utils.event.eventbus.server.VertxServerEventBus;
import com.chua.utils.tools.manager.EventBusContextManager;
import com.chua.utils.tools.manager.producer.StandardEventBusContextManager;
import com.chua.utils.tools.text.IdHelper;
import com.google.common.eventbus.Subscribe;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/24
 */
public class EventBusExample {

    public static void main(String[] args) throws Exception {
        EventBusContextManager eventBusContextManager = new StandardEventBusContextManager();
        // eventBusContextManager.registerEventBus("demo", new GuavaEventBus(), new EventBusDemo());
        // eventBusContextManager.registerEventBus("demo", new GuavaAsyncEventBus(), new EventBusDemo());
        eventBusContextManager.registerEventBus("demo", new VertxServerEventBus(12345), new EventBusDemo("server"));
        eventBusContextManager.registerEventBus("demo", new VertxEventBus(12345), new EventBusDemo("client"));

        Thread.sleep(500);
        IntStream.range(0, 10000).forEach(action -> {
            eventBusContextManager.sendEventBus("demo", IdHelper.createSimpleUuid());
            //    eventBusContextManager.sendEventBus("demo1", IdHelper.createSimpleUuid());
        });

        eventBusContextManager.close();
    }

    public static class EventBusDemo {
        private String name;
        private AtomicInteger atomicInteger = new AtomicInteger();

        public EventBusDemo(String name) {
            this.name = name;
        }

        @Subscribe
        public void subscribe(Object o) {
            System.out.println(name + "@" + atomicInteger.incrementAndGet() + ":" + o);
        }
    }
}
