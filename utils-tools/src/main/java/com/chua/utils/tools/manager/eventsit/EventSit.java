package com.chua.utils.tools.manager.eventsit;


import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.manager.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.Data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 向侦听器分发事件，并为侦听器提供注册自己的方法
 * <p>每个侦听器只接受一次信息, 有且只有一个侦听器接受</p>
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/27
 */
public class EventSit implements EventBus {

    private Dispatcher dispatcher = new Dispatcher(this);

    @Override
    public void register(Object object) {
        dispatcher.register(object);
    }

    @Override
    public void unregister(Object object) {
        dispatcher.unregister(object);
    }

    @Override
    public void post(Object event) {
        dispatcher.post(event);
    }


    public void handleSubscriberException(Throwable throwable) {

    }

    class Dispatcher implements EventBus {

        private final ThreadLocal<Deque<Event>> eventsCache = ThreadLocal.withInitial(() -> new ArrayDeque<>());
        private final ThreadLocal<Queue<Object>> queue = ThreadLocal.withInitial(() -> new ArrayDeque<>());
        private EventSit eventSit;
        private Executor executorService;

        public Dispatcher(EventSit eventSit) {
            this.eventSit = eventSit;
            this.executorService = MoreExecutors.directExecutor();
        }

        @Override
        public void register(Object object) {
            List<Event> events = getEvents(object);
            events.forEach(event -> {
                eventsCache.get().add(event);
            });
        }

        @Override
        public void unregister(Object object) {
            List<Event> events = getEvents(object);
            events.forEach(event -> {
                Collection<Event> collection = eventsCache.get();
                collection.forEach(event1 -> {
                    if (event1.getClassName().equals(event.getClassName())) {
                        eventsCache.get().remove(event1);
                    }
                });
            });
        }

        @Override
        public void post(Object event) {
            if (null == event) {
                return;
            }

            queue.get().offer(event);
            Object data;
            while ((data = queue.get().poll()) != null) {
                List<Event> list = new ArrayList<>();
                for (Event event1 : eventsCache.get()) {
                    if (event1.getParamType().isAssignableFrom(event.getClass())) {
                        list.add(event1);
                    }
                }
                if(list.size() == 0) {
                    return;
                }
                synchronized (list) {
                    Random random = new Random();
                    int nextInt = random.nextInt(list.size());
                    Event randomOne = list.get(nextInt);
                    Object finalData = data;
                    executorService.execute(() -> {
                        try {
                            invokeSubscriberMethod(randomOne, finalData);
                        } catch (InvocationTargetException e) {
                            randomOne.getEventSit().handleSubscriberException(e);
                        }
                    });
                }

            }
        }

        void invokeSubscriberMethod(Event event1, Object event) throws InvocationTargetException {
            try {
                event1.getMethod().invoke(event1.getEntity(), checkNotNull(event));
            } catch (IllegalArgumentException e) {
                throw new Error("Method rejected target/argument: " + event, e);
            } catch (IllegalAccessException e) {
                throw new Error("Method became inaccessible: " + event, e);
            } catch (InvocationTargetException e) {
                if (e.getCause() instanceof Error) {
                    throw (Error) e.getCause();
                }
                throw e;
            }
        }


        /**
         * 获取所有事件
         *
         * @return
         */
        public List<Event> getEvents(Object listener) {
            List<Event> events = new ArrayList<>();

            ClassHelper.doWithLocalMethods(listener.getClass(), method -> {
                Subscribe subscribe = method.getDeclaredAnnotation(Subscribe.class);
                if (null == subscribe) {
                    return;
                }
                Class<?>[] parameterTypes = method.getParameterTypes();
                checkArgument(
                        parameterTypes.length == 1,
                        "Method %s has @Subscribe annotation but has %s parameters."
                                + "Subscriber methods must have exactly 1 parameter.",
                        method,
                        parameterTypes.length);
                method.setAccessible(true);

                Event event = new Event();
                event.setEventSit(eventSit);
                event.setMethod(method);
                event.setParamType(parameterTypes[0]);
                event.setEntity(listener);
                event.setClassName(listener.getClass().getName());

                events.add(event);
            });

            return events;
        }

    }

    @Data
    class Event {
        /**
         * 事件侦听器
         */
        private EventSit eventSit;
        /**
         * 方法
         */
        private Method method;
        /**
         * 对象
         */
        private Object entity;
        /**
         * 类名
         */
        private String className;
        /**
         * 参数类型
         */
        private Class<?> paramType;
    }
}
