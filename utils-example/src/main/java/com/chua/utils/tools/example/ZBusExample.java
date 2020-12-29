package com.chua.utils.tools.example;

import org.zbus.mq.server.MqServer;
import org.zbus.mq.server.MqServerConfig;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/28
 */
public class ZBusExample {

    public static void main(String[] args) throws Exception {
        MqServerConfig config = new MqServerConfig();
        config.serverPort = 15555;
        config.storePath = "./store";
        final MqServer server = new MqServer(config);
        server.start();
    }
}
