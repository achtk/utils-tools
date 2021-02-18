package com.chua.utils.tools.time.schedule;

import com.chua.utils.tools.function.ScheduleJob;
import com.chua.utils.tools.text.IdHelper;

/**
 * cron调度任务
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/31
 */
public interface CronScheduleOperations<Job> extends ScheduleOperations<Job> {
    /**
     * 创建任务
     *
     * @param groupName 分组
     * @param cron      表达式
     * @param job       任务
     * @throws Exception Exception
     */
    void addCronJob(final String groupName, final String cron, final Class<Job> job) throws Exception;

    /**
     * 创建任务
     *
     * @param cron 表达式
     * @param job  任务
     * @throws Exception Exception
     */
    default void addCronJob(final String cron, final Class<Job> job) throws Exception {
        addCronJob(IdHelper.createUuid(), cron, job);
    }

    /**
     * 创建任务
     *
     * @param cron 表达式
     * @param job  任务
     * @throws Exception Exception
     */
    default void addCronJob(final String cron, final Job job) throws Exception {
        addCronJob(IdHelper.createUuid(), cron, null == job ? null : (Class<Job>) job.getClass());
    }

    /**
     * 创建Simple调度
     *
     * @param jobName 任务名称
     * @param cron    时间
     * @param job     调度
     * @throws Exception Exception
     */
    void addCronJob(final String jobName, final String cron, final ScheduleJob job) throws Exception;

    /**
     * 创建Simple调度
     *
     * @param cron 时间
     * @param job  调度
     * @throws Exception Exception
     */
    default void addCronJob(final String cron, final ScheduleJob job) throws Exception {
        addCronJob(IdHelper.createUuid(), cron, job);
    }

    /**
     * 删除任务
     *
     * @param jobName 任务名称
     * @throws Exception
     */
    void removeJob(String jobName) throws Exception;

    /**
     * 更新时间
     *
     * @param jobName 任务
     * @param cron    时间
     * @throws Exception
     */
    void updateJobTime(String jobName, String cron) throws Exception;
}
