package com.chua.utils.tools.fegin;

import com.chua.unified.configure.ServerConfig;
import com.chua.unified.interfaces.IRemoteCallFactory;
import com.chua.utils.tools.fegin.contract.FeginContract;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

/**
 * Contract方式调用Fegin
 * @author CH
 * @date 2020-09-29
 */
public class FeginRemoteContractCallFactory implements IRemoteCallFactory {


    @Override
    public <T> T target(Class<T> tClass, String serverUrl, ServerConfig serverConfig) {
        Feign.Builder builder = Feign.builder()
                .encoder(new JacksonEncoder())
                .contract(new FeginContract())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(serverConfig.getConnectTimeoutMillis(1000), serverConfig.getReadTimeoutMillis(3500)))
                .retryer(new Retryer.Default(100, 2000, serverConfig.getRetry(3)));
        return builder.target(tClass, serverUrl);
    }

    @Override
    public <T> T target(Class<T> tClass, String serverUrl) {
        return null;
    }

    @Override
    public <T> T target(Class<T> tClass) {
        return null;
    }
}
