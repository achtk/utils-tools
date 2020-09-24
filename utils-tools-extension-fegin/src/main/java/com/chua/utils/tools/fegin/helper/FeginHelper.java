package com.chua.utils.tools.fegin.helper;

import com.chua.unified.configure.ServerConfig;
import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

/**
 * Fegin工具类
 * @author CH
 */
public class FeginHelper {
    /**
     * 调用远程服务
     * @param tClass 调用转化类
     * @param serviceUrl 远程服务
     * @param <T>
     * @return
     */
    public static <T>T getObjectForEntity(final Class<T> tClass, String serviceUrl) {
        return getObjectForEntity(new ServerConfig<T>(tClass), serviceUrl);
    }
    /**
     * 调用远程服务
     * @param serverConfig 调用服务
     * @param serviceUrl 远程服务
     * @param <T>
     * @return
     */
    public static <T>T getObjectForEntity(final ServerConfig<T> serverConfig, String serviceUrl) {
        if(null == serverConfig || null == serviceUrl) {
            return null;
        }

        Feign.Builder builder = getFeginBuilder(serverConfig);
        return builder.target(serverConfig.getTClass(), serviceUrl);
    }

    /**
     * 创建配置
     * @param serverConfig 服务配置
     * @return
     */
    private static Feign.Builder getFeginBuilder(ServerConfig serverConfig) {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .options(new Request.Options(serverConfig.getConnectTimeoutMillis(1000), serverConfig.getReadTimeoutMillis(3500)))
                .retryer(new Retryer.Default(100, 2000, serverConfig.getRetry(3)));

    }
}
