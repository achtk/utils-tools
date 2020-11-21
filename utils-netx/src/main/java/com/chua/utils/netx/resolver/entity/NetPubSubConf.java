package com.chua.utils.netx.resolver.entity;

import lombok.Data;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 订阅发布对象
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
@Data
public class NetPubSubConf<T> {
    /**
     * 交换机名称
     */
    private String exchange;
    /**
     * 交换机类型
     */
    private String exchangeType = "topic";
    /**
     * 队列名称
     */
    private String queue;
    /**
     * 路由
     */
    private String routingKey;
    /**
     * 管道名称
     */
    private String topic;
    /**
     * 管道(空表示新建)
     */
    private T channel;
    /**
     * 获取超时
     */
    private long timeout = 30 * 1000;
    /**
     * 自动关闭管道
     */
    private boolean autoClose;
    /**
     * 多线程
     */
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

}
