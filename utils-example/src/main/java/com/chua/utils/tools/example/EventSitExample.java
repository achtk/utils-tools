package com.chua.utils.tools.example;

import com.chua.utils.tools.example.entity.TDemoInfo;
import com.chua.utils.tools.manager.eventsit.EventSit;
import com.chua.utils.tools.text.IdHelper;
import com.google.common.eventbus.Subscribe;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/24
 */
public class EventSitExample {

    public static void main(String[] args) {
        EventSit eventSit = new EventSit();
        eventSit.register(new EventBusDemo1("test1"));
        eventSit.register(new EventBusDemo1("test2"));
        eventSit.register(new EventBusDemo());
        eventSit.post(IdHelper.createUuid());
        eventSit.post(IdHelper.createUuid());
        IntStream.range(0, 10).forEach(index -> {
            eventSit.post(new TDemoInfo());
        });
    }

    public static class EventBusDemo {

        private AtomicInteger atomicInteger = new AtomicInteger();

        @Subscribe
        public void subscribe(Object o) {
            System.out.println(atomicInteger.incrementAndGet() + ":" + o);
        }
    }

    public static class EventBusDemo1 {

        private String name;

        public EventBusDemo1(String name) {
            this.name = name;
        }

        private AtomicInteger atomicInteger = new AtomicInteger();

        @Subscribe
        public void subscribe(TDemoInfo o) {
            System.out.println(name + "@" + atomicInteger.incrementAndGet() + ":" + o.uuid());
        }
    }
}
