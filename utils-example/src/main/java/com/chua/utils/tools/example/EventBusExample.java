package com.chua.utils.tools.example;

import com.chua.utils.event.eventbus.VertxEventBus;
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

    public static void main(String[] args) {
        EventBusContextManager eventBusContextManager = new StandardEventBusContextManager();
        // eventBusContextManager.registerEventBus("demo", new GuavaEventBus(), new EventBusDemo());
        // eventBusContextManager.registerEventBus("demo", new GuavaAsyncEventBus(), new EventBusDemo());
        //eventBusContextManager.registerEventBus("demo", new VertxServerEventBus(12345), null);
        eventBusContextManager.registerEventBus("demo", new VertxEventBus( 12345), new EventBusDemo());

        IntStream.range(0, 10).forEach(action -> {
            eventBusContextManager.sendEventBus("demo", IdHelper.createSimpleUuid());
        //    eventBusContextManager.sendEventBus("demo1", IdHelper.createSimpleUuid());
        });
    }

    static class EventBusDemo {

        private AtomicInteger atomicInteger = new AtomicInteger();

        @Subscribe
        public void subscribe(Object o) {
            System.out.println(atomicInteger.incrementAndGet() + ":" + o);
        }
    }
}
