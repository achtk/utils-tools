package com.chua.utils.tools.function;



/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * <p>
 * job.A unified access point.
 * <p>
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * <p>e.g.org.quartz.Job</p>
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/14
 */
@FunctionalInterface
public interface ScheduleJob<T> {
    /**
     * 调度执行
     *
     * @throws Exception 异常
     */
    void execute() throws Exception;
}
