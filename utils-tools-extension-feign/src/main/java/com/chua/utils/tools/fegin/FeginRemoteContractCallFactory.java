package com.chua.utils.tools.fegin;

import com.chua.utils.tools.fegin.target.ContractTarget;
import com.chua.utils.tools.properties.ServerConfig;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.Target;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

/**
 * Contract方式调用Fegin
 * @author CH
 * @date 2020-09-29
 */
public class FeginRemoteContractCallFactory extends FeginRemoteCallFactory {

    public FeginRemoteContractCallFactory() {
    }

    public FeginRemoteContractCallFactory(String serverUrl) {
        super(serverUrl);
    }

    public FeginRemoteContractCallFactory(ServerConfig serverConfig, String serverUrl) {
        super(serverConfig, serverUrl);
    }

    @Override
    public <T> T target(Class<T> tClass, String serverUrl, ServerConfig serverConfig) {
        Feign.Builder builder = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(serverConfig.getConnectTimeoutMillis(1000), serverConfig.getReadTimeoutMillis(3500)))
                .retryer(new Retryer.Default(100, 2000, serverConfig.getRetry(3)));
        Target<T> target = new ContractTarget(tClass, serverUrl);
        return builder.target(target);
    }

}
