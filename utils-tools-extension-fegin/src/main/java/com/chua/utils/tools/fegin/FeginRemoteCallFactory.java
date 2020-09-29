package com.chua.utils.tools.fegin;

import com.chua.unified.configure.ServerConfig;
import com.chua.unified.interfaces.IRemoteCallFactory;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * Fegin远程调用工厂
 * @author CH
 */
@NoArgsConstructor
public class FeginRemoteCallFactory implements IRemoteCallFactory {

    private String serverUrl;
    private static final ServerConfig DEFAULT_SERVER_CONFIG = new ServerConfig();
    private ServerConfig serverConfig = DEFAULT_SERVER_CONFIG;

    public FeginRemoteCallFactory(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public FeginRemoteCallFactory(ServerConfig serverConfig, String serverUrl) {
        this.serverConfig = null == serverConfig ? DEFAULT_SERVER_CONFIG : serverConfig;
        this.serverUrl = serverUrl;
    }
    @Override
    public <T> T target(Class<T> tClass, String serverUrl, ServerConfig serverConfig) {
        Feign.Builder builder = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(serverConfig.getConnectTimeoutMillis(1000), serverConfig.getReadTimeoutMillis(3500)))
                .retryer(new Retryer.Default(100, 2000, serverConfig.getRetry(3)));
        return builder.target(tClass, serverUrl);
    }

    @Override
    public <T> T target(Class<T> tClass, String serverUrl) {
        return target(tClass, serverUrl, DEFAULT_SERVER_CONFIG);
    }

    @Override
    public <T> T target(Class<T> tClass) {
        return target(tClass, serverUrl, DEFAULT_SERVER_CONFIG);
    }
}
