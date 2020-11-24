package com.chua.utils.tools.example;

import com.chua.utils.event.eventbus.VertxEventBus;
import com.chua.utils.event.eventbus.server.ServerEventBusMonitor;
import com.chua.utils.tools.example.entity.TDemoInfo;
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
public class ServerEventBusExample {

    public static void main(String[] args) {
        ServerEventBusMonitor serverEventBusMonitor = new ServerEventBusMonitor();

        while (true) {
            serverEventBusMonitor.sendEventBus("demo", new TDemoInfo());
        }
    }
}
