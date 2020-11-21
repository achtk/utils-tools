package com.chua.utils.tools.function.psub;

/**
 * 订阅发布
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public interface PubSub {
    /**
     * 订阅
     *
     * @param channel 管道
     * @param message 消息
     */
    void onMessage(String channel, String message);
}
