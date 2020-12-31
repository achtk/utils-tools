package com.chua.utils.tools.time.schedule;

import com.chua.utils.tools.function.ScheduleJob;

import java.util.concurrent.TimeUnit;

/**
 * 简单的调度任务
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/31
 */
public interface SimpleScheduleOperations<Job> extends ScheduleOperations<Job> {

    /**
     * 创建Simple调度
     *
     * @param jobName  任务名称
     * @param time     时间
     * @param timeUnit 类型
     * @param job      调度
     * @throws Exception Exception
     */
    void addSimpleJob(final String jobName, final int time, TimeUnit timeUnit, final Class<Job> job) throws Exception;

    /**
     * 创建Simple调度
     *
     * @param jobName  任务名称
     * @param time     时间
     * @param timeUnit 类型
     * @param job      调度
     * @throws Exception Exception
     */
    void addSimpleJob(final String jobName, final int time, TimeUnit timeUnit, final ScheduleJob job) throws Exception;
}