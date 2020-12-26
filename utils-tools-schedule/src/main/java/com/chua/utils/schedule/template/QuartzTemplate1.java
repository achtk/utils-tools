package com.chua.utils.schedule.template;

import lombok.NoArgsConstructor;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * quartz 模板
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/25
 */
@NoArgsConstructor
public class QuartzTemplate1 {
    /**
     * 任务组
     */
    private static String JOB_GROUP_NAME = "FH_JOBGROUP_NAME";
    /**
     * 触发器组
     */
    private static String TRIGGER_GROUP_NAME = "FH_TRIGGERGROUP_NAME";
    /**
     * 任务名称
     */
    private static String JOB_NAME = "default";

    private SchedulerFactory stdSchedulerFactory = new StdSchedulerFactory();

    private JobDetail jobDetail;
    private Trigger trigger;

    /**
     * 启动
     *
     * @throws Exception
     */
    public void start() throws Exception {
        Scheduler scheduler = stdSchedulerFactory.getScheduler();
        scheduler.scheduleJob(jobDetail, trigger);
        if (!scheduler.isShutdown()) {
            scheduler.start();
        }
    }

    /**
     * 修改一个任务的触发时间
     *
     * @param jobName
     * @param triggerName 触发器名
     * @param cron        时间设置，参考quartz说明文档
     */
    public void modify(String jobName, String triggerName, String cron) throws Exception {
        try {
            Scheduler scheduler = stdSchedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, TRIGGER_GROUP_NAME);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }

            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                // 触发器
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组
                triggerBuilder.withIdentity(triggerName, TRIGGER_GROUP_NAME);
                triggerBuilder.startNow();
                // 触发器时间设定
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                // 创建Trigger对象
                trigger = (CronTrigger) triggerBuilder.build();
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 停止
     *
     * @throws Exception
     */
    public void shutdown() throws Exception {
        shutdown(JOB_NAME, JOB_NAME);
    }

    /**
     * 停止
     *
     * @throws Exception
     */
    public void shutdown(String jobName, String triggerName) throws Exception {
        Scheduler scheduler = stdSchedulerFactory.getScheduler();
        // 通过触发器名和组名获取TriggerKey
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, TRIGGER_GROUP_NAME);
        //// 通过任务名和组名获取JobKey
        JobKey jobKey = JobKey.jobKey(jobName, JOB_GROUP_NAME);
        // 停止触发器
        scheduler.pauseTrigger(triggerKey);
        // 移除触发器
        scheduler.unscheduleJob(triggerKey);
        // 删除任务
        scheduler.deleteJob(jobKey);

    }

    /**
     * -----------------------------------------------------------------------------
     */
    public static class Builder {
        private QuartzTemplate1 template = new QuartzTemplate1();

        /**
         * 添加任务
         *
         * @param job 任务
         * @return this
         */
        public Builder addJob(Class<? extends Job> job) {
            return addJob(job, JOB_NAME);
        }

        /**
         * 添加任务
         *
         * @param job     任务
         * @param jobName 任务名称
         * @return this
         */
        public Builder addJob(Class<? extends Job> job, final String jobName) {
            template.jobDetail = JobBuilder.newJob(job).withIdentity(jobName, JOB_GROUP_NAME).build();
            return this;
        }

        /**
         * 添加触发器
         *
         * @param cron 时间
         * @return this
         */
        public Builder addCronTrigger(final String cron) {
            return addCronTrigger(cron, JOB_NAME);
        }

        /**
         * 添加触发器
         *
         * @param cron        时间
         * @param triggerName 触发器名称
         * @return this
         */
        public Builder addCronTrigger(final String cron, final String triggerName) {
            template.trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, TRIGGER_GROUP_NAME).withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
            return this;
        }

        /**
         * 添加触发器
         *
         * @param seconds 时间(秒)
         * @return this
         */
        public Builder addSimpleTrigger(final int seconds) {
            return addSimpleTrigger(seconds, JOB_NAME);
        }

        /**
         * 添加触发器
         *
         * @param seconds     时间(秒)
         * @param triggerName 触发器名称
         * @return this
         */
        public Builder addSimpleTrigger(final int seconds, final String triggerName) {
            template.trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerName, TRIGGER_GROUP_NAME)
                    .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(seconds)).build();
            return this;
        }

        public QuartzTemplate1 build() {
            return template;
        }
    }
}
