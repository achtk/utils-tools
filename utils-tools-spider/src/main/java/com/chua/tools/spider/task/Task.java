package com.chua.tools.spider.task;

/**
 * 任务
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public interface Task extends Runnable {
    /**
     * 停止
     */
    void toStop();
}
