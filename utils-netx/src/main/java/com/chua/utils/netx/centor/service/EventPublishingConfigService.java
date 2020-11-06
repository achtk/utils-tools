package com.chua.utils.netx.centor.service;

import com.chua.utils.netx.centor.listener.Listener;

/**
 * 中心服务
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/12
 */
public interface EventPublishingConfigService {

    /**
     * Get config.
     *
     * @param dataId    dataId
     * @param group     group
     * @param timeoutMs read timeout
     * @return config value
     * @throws Exception Exception
     */
    String getConfig(String dataId, String group, long timeoutMs) throws Exception;

    /**
     * Get config.
     *
     * @param dataId    dataId
     * @param group     group
     * @param timeoutMs read timeout
     * @param listener listener
     * @return config value
     * @throws Exception Exception
     */
    String getConfigAndSignListener(String dataId, String group, long timeoutMs, Listener listener) throws Exception;

    /**
     * Add a listener to the configuration
     *
     * @param dataId   dataId
     * @param group    group
     * @param listener listener
     * @throws Exception Exception
     */
    void addListener(String dataId, String group, Listener listener) throws Exception;

    /**
     * Publish config.
     *
     * @param dataId  dataId
     * @param group   group
     * @param content content
     * @return Whether publish
     * @throws Exception Exception
     */
    boolean publishConfig(String dataId, String group, String content) throws Exception;

    /**
     * Remove config.
     *
     * @param dataId dataId
     * @param group  group
     * @return whether remove
     * @throws Exception Exception
     */
    boolean removeConfig(String dataId, String group) throws Exception;

    /**
     * Remove listener.
     *
     * @param dataId   dataId
     * @param group    group
     * @param listener listener
     * @throws Exception Exception
     */
    void removeListener(String dataId, String group, Listener listener) throws Exception;

    /**
     * Get server status.
     *
     * @return whether health
     */
    String getServerStatus();

    /**
     * Shutdown the resource service.
     *
     * @throws Exception exception.
     */
    void shutDown() throws Exception;

}
