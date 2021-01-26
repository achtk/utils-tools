package com.chua.utils.tools.interfaces;


import com.chua.utils.tools.properties.ServerConfig;

/**
 * 远程调用工厂
 *
 * @author CH
 */
public interface RemoteCallFactory {
    /**
     * 远程调用
     *
     * @param tClass       接口类
     * @param serverUrl    服务地址
     * @param serverConfig 请求配置
     * @param <T>
     * @return
     */
    <T> T target(Class<T> tClass, String serverUrl, ServerConfig serverConfig);
    /**
     * 远程调用
     *
     * @param tClass       接口类
     * @param serverUrl    服务地址
     * @param <T>
     * @return
     */
    <T> T target(Class<T> tClass, String serverUrl);
    /**
     * 远程调用
     *
     * @param tClass       接口类
     * @param <T>
     * @return
     */
    <T> T target(Class<T> tClass);
}
