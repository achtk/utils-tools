package com.chua.utils.tools.handler;

/**
 * 周期性任务
 *
 * @author CH
 * @date 2020-10-08
 */
public interface PeriodicHandler<Action> extends MessageHandler<Action> {
    /**
     * 延迟多少时间推送
     *
     * @return long
     */
    long delay();
}
