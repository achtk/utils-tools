package com.chua.utils.netx.centor.service;

import com.chua.utils.netx.centor.listener.Listener;
import com.chua.utils.tools.common.BeansHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 中心服务
 * @author CH
 * @version 1.0.0
 * @since 2020/10/12
 */
public interface EventPublishingNamingService {
    /**
     * 创建服务节点
     *
     * @param serviceName 数据索引
     * @param group       分组
     * @param ip          地址
     * @param port        端口
     * @param clusterName 分布式
     * @throws Exception
     */
    void registerInstance(final String serviceName, final String group, final String ip, final int port, final String clusterName) throws Exception;
    /**
     * 创建服务节点
     *
     * @param serviceName 数据索引
     * @param group       分组
     * @param ip          地址
     * @param port        端口
     * @param clusterName 分布式
     * @throws Exception Exception
     */
    void deregisterInstance(final String serviceName, final String group, final String ip, final int port, final String clusterName) throws Exception;
    /**
     * 获取服务节点
     *
     * @param serviceName 数据索引
     * @param healthy healthy
     * @throws Exception Exception
     * @return List
     */
    List<Map<String, Object>> selectInstances(final String serviceName, boolean healthy) throws Exception;
    /**
     * 获取服务节点
     *
     * @param serviceName 数据索引
     * @throws Exception Exception
     * @return List
     */
    List<Map<String, Object>> getAllInstances(final String serviceName) throws Exception;

    /**
     * 创建服务节点
     *
     * @param serviceName 数据索引
     * @param listener    监听
     * @throws Exception Exception
     */
    <T>void subscribe(final String serviceName, final Listener<T> listener) throws Exception;
    /**
     * 创建服务节点
     *
     * @param serviceName 数据索引
     * @param listener    监听
     * @throws Exception
     */
    <T>void unsubscribe(final String serviceName, final Listener<T> listener) throws Exception;
}
