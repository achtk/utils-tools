package com.chua.utils.tools.handler;

import lombok.EqualsAndHashCode;

/**
 * 信息
 *
 * @author CH
 * @date 2020-10-08
 */
public interface MessageHandler<Action> extends Handler<Action> {
    /**
     * 处理动作
     *
     * @param action 动作
     */
    @Override
    default void handler(Action action) {

    }

    /**
     * 消费
     *
     * @param data 数据
     * @return
     */
    void consumer(Object data);

    /**
     * 发布
     *
     * @return Message
     */
    Message publish();

    @lombok.Data
    @EqualsAndHashCode
    class Message {
        /**
         * 接收方名称
         */
        private String name;
        /**
         * 数据
         */
        private Object data;

    }

}
