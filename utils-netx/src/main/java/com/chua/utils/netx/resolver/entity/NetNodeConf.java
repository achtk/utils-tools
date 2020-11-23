package com.chua.utils.netx.resolver.entity;

import lombok.Data;

import java.util.concurrent.ExecutorService;

/**
 * 节点
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
@Data
public class NetNodeConf {
    /**
     * 节点
     * <p>对应zookeeper中的node, 以及nacos中的dataId</p>
     */
    private String node;
    /**
     * 类型
     * <p>对应zookeeper中的节点类型, 以及nacos中的group</p>
     */
    private String nodeType;
    /**
     * 超时时间
     */
    private int timeoutMs;

}
