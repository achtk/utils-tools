package com.chua.utils.schedule.template;

import com.chua.utils.tools.function.ScheduleJob;
import com.chua.utils.tools.time.schedule.SimpleScheduleOperations;
import lombok.NonNull;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.quartz.TriggerBuilder.newTrigger;

/**
 * quartz 模板
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/25
 */
public class QuartzSimpleScheduleTemplate implements SimpleScheduleOperations<Job> {
    /**
     * 调度工厂
     */
    @NonNull
    private SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    /**
     *
     */
    private static final String EXECUTE = "execute";
    /**
     * 任务组
     */
    private static String JOB_GROUP_NAME = "FH_JOBGROUP_NAME";
    /**
     * 触发器组
     */
    private static String TRIGGER_GROUP_NAME = "FH_TRIGGERGROUP_NAME";

    public QuartzSimpleScheduleTemplate() {
    }

    public QuartzSimpleScheduleTemplate(@NonNull SchedulerFactory schedulerFactory) {
        this.schedulerFactory = schedulerFactory;
    }

    /**
     * 移除一个任务
     *
     * @param jobName 任务名称
     */
    public void removeJob(final String jobName) throws Exception {
        // 通过SchedulerFactory构建Scheduler对象
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.deleteJob(JobKey.jobKey(jobName, JOB_GROUP_NAME));
    }

    /**
     * 启动所有定时任务
     */
    public void startJobs() throws Exception {
        // 通过SchedulerFactory构建Scheduler对象
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.start();
    }

    /**
     * 关闭所有定时任务
     */
    public void shutdownNow() {
        try {
            // 通过SchedulerFactory构建Scheduler对象
            Scheduler scheduler = schedulerFactory.getScheduler();
            if (!scheduler.isShutdown()) {
                //未传参或false：不等待执行完成便结束；true：等待任务执行完才结束
                scheduler.shutdown();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改一个任务的触发时间
     *
     * @param jobName 任务名称
     * @param cron    cron
     */
    public void updateCronJobTime(final String jobName, final String cron) throws Exception {
        // 通过SchedulerFactory构建Scheduler对象
        Scheduler scheduler = schedulerFactory.getScheduler();

        Trigger trigger = newTrigger().withIdentity(jobName, TRIGGER_GROUP_NAME)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron)).startNow().build();

        try {
            scheduler.rescheduleJob(TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME), trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改一个任务的触发时间
     *
     * @param jobName  任务名称
     * @param time     时间
     * @param timeUnit 类型
     */
    public void updateSimpleJobTime(final String jobName, final int time, final TimeUnit timeUnit) throws Exception {
        // 通过SchedulerFactory构建Scheduler对象
        Scheduler scheduler = schedulerFactory.getScheduler();

        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger().withIdentity(jobName, TRIGGER_GROUP_NAME);
        //构造简单时间触发器
        initialSimpleTrigger(triggerBuilder, time, timeUnit);

        Trigger trigger = triggerBuilder.startNow().build();
        try {
            scheduler.rescheduleJob(TriggerKey.triggerKey(jobName, TRIGGER_GROUP_NAME), trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建Simple调度
     *
     * @param time     时间
     * @param timeUnit 类型
     * @param jobName  任务名称
     * @param job      调度
     */
    @Override
    public void addSimpleJob(final String jobName, final int time, TimeUnit timeUnit, final Class<Job> job) throws Exception {
        timeUnit = Optional.ofNullable(timeUnit).orElse(TimeUnit.SECONDS);
        // 通过SchedulerFactory构建Scheduler对象
        Scheduler scheduler = schedulerFactory.getScheduler();
        // 用于描叙Job实现类及其他的一些静态信息，构建一个作业实例
        JobDetail jobDetail = JobBuilder.newJob(job).withIdentity(jobName, JOB_GROUP_NAME).build();

        // 创建一个新的TriggerBuilder来规范一个触发器
        TriggerBuilder<Trigger> triggerBuilder = newTrigger()
                // 给触发器起一个名字和组名
                .withIdentity(jobName, TRIGGER_GROUP_NAME);

        //构造简单时间触发器
        initialSimpleTrigger(triggerBuilder, time, timeUnit);

        Trigger trigger = triggerBuilder.build();

        scheduler.scheduleJob(jobDetail, trigger);
        if (!scheduler.isShutdown()) {
            scheduler.start(); // 启动
        }
    }

    @Override
    public void addSimpleJob(String jobName, int time, TimeUnit timeUnit, ScheduleJob job) throws Exception {
        Class<Job> quartzJobClass = (Class<Job>) QuartzCronScheduleTemplate.createJobClass(job);;
        addSimpleJob(jobName, time, timeUnit, quartzJobClass);
    }

    /**
     * 构造简单时间触发器
     *
     * @param triggerBuilder 触发器
     * @param time           时间
     * @param timeUnit       类型
     */
    private void initialSimpleTrigger(TriggerBuilder<Trigger> triggerBuilder, int time, TimeUnit timeUnit) {
        if (timeUnit == TimeUnit.SECONDS) {
            triggerBuilder.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(time));
        } else if (timeUnit == TimeUnit.MINUTES) {
            triggerBuilder.withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(time));
        } else if (timeUnit == TimeUnit.HOURS) {
            triggerBuilder.withSchedule(SimpleScheduleBuilder.repeatHourlyForever(time));
        }
    }

    @Override
    public void close() throws Exception {
        this.shutdownNow();
    }
}
