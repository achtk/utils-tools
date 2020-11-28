package com.chua.utils.event.eventbus.server;

import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.AsyncEventBus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 监测服务
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/24
 */
@Slf4j
public class VertxServerEventBus extends AbstractVerticle implements com.chua.utils.tools.manager.eventbus.ServerEventBus, Runnable {

    private static final Multimap<String, NetSocket> CLIENT_LIST = HashMultimap.create();
    private static final List<Object> LINK = new CopyOnWriteArrayList();

    private static final AsyncEventBus EVENT_BUS = new AsyncEventBus(ThreadHelper.newSingleThreadExecutor());
    private Vertx vertx = Vertx.vertx();
    /**
     * 创建TCP服务器
     */
    private NetServer server = vertx.createNetServer();
    private String name;

    public VertxServerEventBus() {
        run();
    }

    public VertxServerEventBus(int port) {
        run(port);
    }

    @Override
    public void run() {
        run(12345);
    }

    /**
     * 启动
     *
     * @param port
     */
    public void run(int port) {
        //添加链接服务
        server.connectHandler(netSocket -> {
            netSocket.handler(buffer -> {
                String name = buffer.toString();
                CLIENT_LIST.put(name, netSocket);
                log.info("获取到一个消息组件");
            });
            //移除断开链接服务
            netSocket.closeHandler(event -> {
                CLIENT_LIST.values().remove(netSocket);
                log.info("移除一个消息组件");
            });
        });

        server.listen(port, res -> {
            log.info("服务器[" + port + "]启动成功");
        });
    }

    /**
     * 发送消息
     *
     * @param name 消息名称
     * @param data 数据
     */
    public void sendEventBus(String name, Object data) {
        if (!CLIENT_LIST.containsKey(name)) {
            return;
        }
        Collection<NetSocket> netSockets = CLIENT_LIST.get(name);
        for (NetSocket netSocket : netSockets) {
            netSocket.write(JsonHelper.toJson(data) + "\r\n");
        }
    }

    @Override
    public void register(Object object) {
        if (null == object) {
            return;
        }
        EVENT_BUS.register(object);
        LINK.add(object);
    }

    @Override
    public void unregister(Object object) {
        EVENT_BUS.unregister(object);
    }

    @Override
    public void post(String channel, Object event) {
        EVENT_BUS.post(event);
        sendEventBus(channel, event);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setObject(Object object) {

    }

    @Override
    public void close() throws Exception {
        server.close();
        vertx.close();
    }
}
