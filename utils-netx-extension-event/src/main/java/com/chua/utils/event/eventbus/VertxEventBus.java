package com.chua.utils.event.eventbus;

import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.manager.eventbus.ServerEventBus;
import com.google.common.base.Splitter;
import com.google.common.eventbus.AsyncEventBus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import io.vertx.core.net.SocketAddress;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * vertx 消息总线
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/24
 */
@Slf4j
public class VertxEventBus extends AbstractVerticle implements ServerEventBus {

    private String address = "127.0.0.1";
    private int port;

    private AsyncResult<NetSocket> session;

    private AsyncEventBus asyncEventBus = new AsyncEventBus(ThreadHelper.newSingleThreadExecutor());

    private Vertx vertx = Vertx.vertx();
    /**
     * 创建TCP客户端
     */
    private NetClient netClient = vertx.createNetClient();
    private String name;

    public VertxEventBus(String address, int port) {
        this.address = address;
        this.port = port;
    }
    public VertxEventBus(int port) {
        this.port = port;
    }

    @Override
    public void register(Object object) {
        netClient.connect(port, address, conn -> {
            if (conn.succeeded()) {
                session = conn;
                session.result().write(name);

                session.result().handler(buffer -> {
                    if(log.isDebugEnabled()) {
                        SocketAddress socketAddress = session.result().localAddress();
                        log.debug("获取到远程数据[{}:{}]", socketAddress.host(), socketAddress.port());
                    }
                    String string = buffer.toString();
                    List<String> strings = Splitter.on("\r\n").trimResults().omitEmptyStrings().splitToList(string);
                    for (String s : strings) {
                        if(JsonHelper.isJson(s)) {
                            Object json = null;
                            try {
                                json = JsonHelper.fromJson(s, Object.class);
                                asyncEventBus.post(json);
                            } catch (Throwable ignore) {
                            }
                        }
                    }
                });
                return;
            }
            log.error("{}:{}注册消息组件失败", address, port);
        });
    }

    @Override
    public void unregister(Object object) {

    }

    @Override
    public void post(Object event) {
        asyncEventBus.post(event);
    }

    @Override
    public List<BusEntity> getBus() {
        return null;
    }

    @Override
    public void post(String channel, Object event) {
        post(event);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setObject(Object object) {
        asyncEventBus.register(object);
    }

    @Override
    public void close() throws Exception {
        netClient.close();
        vertx.close();
    }
}
