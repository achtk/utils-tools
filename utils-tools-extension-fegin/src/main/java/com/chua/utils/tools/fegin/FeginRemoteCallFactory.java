package com.chua.utils.tools.fegin;

import com.chua.unified.configure.ServerConfig;
import com.chua.unified.interfaces.IRemoteCallFactory;
import com.chua.utils.tools.fegin.helper.FeginHelper;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.Target;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

/**
 * Fegin远程调用工厂
 * @author CH
 */
public class FeginRemoteCallFactory implements IRemoteCallFactory {

    @Override
    public <T> T target(Class<T> tClass, String serverUrl, ServerConfig serverConfig) {
        Feign.Builder builder = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(serverConfig.getConnectTimeoutMillis(1000), serverConfig.getReadTimeoutMillis(3500)))
                .retryer(new Retryer.Default(100, 2000, serverConfig.getRetry(3)));
        return builder.target(tClass, serverUrl);
    }
}
