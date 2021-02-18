package com.chua.utils.tools.time.schedule;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.function.ScheduleJob;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * jdk 简单调度任务
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/31
 */
public class JdkSimpleScheduleTemplate implements SimpleScheduleOperations<Runnable> {

    private static final String THREAD_NAME = "schedule-group-job";
    private final Map<String, JobTime> groupCache = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduledThreadPoolExecutor = ThreadHelper.newScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), THREAD_NAME);

    @Override
    public void addSimpleJob(String jobName, int time, TimeUnit timeUnit, Class<Runnable> job) throws Exception {
        //缓存数据
        this.createJobTime(jobName, time, timeUnit, job);
        //实例化
        Runnable runnable = ClassHelper.forObject(job, Runnable.class);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(runnable, 0, time, timeUnit);
    }

    @Override
    public void addSimpleJob(String jobName, int time, TimeUnit timeUnit, ScheduleJob job) throws Exception {
        //缓存数据
        this.createJobTime(jobName, time, timeUnit, job);
        //实例化
        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            try {
                job.execute();
            } catch (Throwable ignored) {
            }
        }, 0, time, timeUnit);
    }

    /**
     * 创建缓存时间
     *
     * @param jobName  任务名称
     * @param time     时间
     * @param timeUnit 类型
     * @param job      调度
     */
    private void createJobTime(String jobName, int time, TimeUnit timeUnit, Object job) {
        JobTime jobTime = null;
        if (!groupCache.containsKey(jobName)) {
            jobTime = new JobTime();
            jobTime.setGroupName(jobName);
            jobTime.setJob(job);
            jobTime.setTime(time);
            jobTime.setTimeUnit(timeUnit);
        } else {
            jobTime = groupCache.get(jobName);
        }
        //缓存
        groupCache.put(jobName, jobTime);
    }

    @Override
    public void close() throws Exception {
        scheduledThreadPoolExecutor.shutdownNow();
    }


    /**
     * 时间信息
     */
    @Data
    private final class JobTime {
        /**
         * 分组名
         */
        private String groupName;
        /**
         * 时间
         */
        private int time;
        /**
         * 时间类型
         *
         * @see #time
         */
        private TimeUnit timeUnit;

        /**
         * 任务
         */
        private Object job;
    }
}
