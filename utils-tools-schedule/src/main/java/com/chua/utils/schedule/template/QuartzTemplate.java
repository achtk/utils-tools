package com.chua.utils.schedule.template;

import com.chua.utils.tools.function.ScheduleJob;
import com.chua.utils.tools.text.IdHelper;
import com.chua.utils.tools.util.ClassUtils;
import javassist.CannotCompileException;
import javassist.CtField;
import javassist.NotFoundException;
import lombok.NonNull;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * quartz 模板
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/25
 */
public class QuartzTemplate {
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

    public QuartzTemplate() {
    }

    public QuartzTemplate(@NonNull SchedulerFactory schedulerFactory) {
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
     * @param jobName 任务名称
     * @param time    时间
     * @param type    类型
     */
    public void updateSimpleJobTime(final String jobName, final int time, final SimpleTimeType type) throws Exception {
        // 通过SchedulerFactory构建Scheduler对象
        Scheduler scheduler = schedulerFactory.getScheduler();

        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger().withIdentity(jobName, TRIGGER_GROUP_NAME);
        //构造简单时间触发器
        initialSimpleTrigger(triggerBuilder, time, type);

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
     * @param time 时间
     * @param job  调度
     */
    public void addSimpleSecondJob(final int time, final Class<? extends Job> job) throws Exception {
        addSimpleJob(IdHelper.createSimpleUuid(), time, SimpleTimeType.SECOND, job);
    }

    /**
     * 创建Simple调度
     *
     * @param time 时间
     * @param job  调度
     */
    public void addSimpleMinuteJob(final int time, final Class<? extends Job> job) throws Exception {
        addSimpleJob(IdHelper.createSimpleUuid(), time, SimpleTimeType.MINUTE, job);
    }

    /**
     * 创建Simple调度
     *
     * @param time    时间
     * @param type    类型
     * @param jobName 任务名称
     * @param job     调度
     */
    public void addSimpleJob(final String jobName, final int time, SimpleTimeType type, final Class<? extends Job> job) throws Exception {
        type = Optional.ofNullable(type).orElse(SimpleTimeType.SECOND);
        // 通过SchedulerFactory构建Scheduler对象
        Scheduler scheduler = schedulerFactory.getScheduler();
        // 用于描叙Job实现类及其他的一些静态信息，构建一个作业实例
        JobDetail jobDetail = JobBuilder.newJob(job).withIdentity(jobName, JOB_GROUP_NAME).build();

        // 创建一个新的TriggerBuilder来规范一个触发器
        TriggerBuilder<Trigger> triggerBuilder = newTrigger()
                // 给触发器起一个名字和组名
                .withIdentity(jobName, TRIGGER_GROUP_NAME);

        //构造简单时间触发器
        initialSimpleTrigger(triggerBuilder, time, type);

        Trigger trigger = triggerBuilder.build();

        scheduler.scheduleJob(jobDetail, trigger);
        if (!scheduler.isShutdown()) {
            scheduler.start(); // 启动
        }
    }

    /**
     * 构造简单时间触发器
     *
     * @param triggerBuilder 触发器
     * @param time           时间
     * @param type           类型
     */
    private void initialSimpleTrigger(TriggerBuilder<Trigger> triggerBuilder, int time, SimpleTimeType type) {
        if (type == SimpleTimeType.SECOND) {
            triggerBuilder.withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(time));
        } else if (type == SimpleTimeType.MINUTE) {
            triggerBuilder.withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(time));
        } else if (type == SimpleTimeType.HOUR) {
            triggerBuilder.withSchedule(SimpleScheduleBuilder.repeatHourlyForever(time));
        }
    }

    /**
     * 创建Cron调度
     *
     * @param cron      cron
     * @param groupName 分组名
     * @param job       调度
     */
    public void addCronJob(final String groupName, final String cron, final ScheduleJob job) throws Exception {
        Class<? extends Job> quartzJobClass = null;
        if (job instanceof Job) {
            quartzJobClass = (Class<? extends Job>) job.getClass();
        } else {
            quartzJobClass = (Class<? extends Job>) createJobClass(job);
        }
        addCronJob(groupName, cron, quartzJobClass);
    }
    /**
     * 创建Cron调度
     *
     * @param cron      cron
     * @param groupName 分组名
     * @param job       调度
     */
    public void addCronJob(final String groupName, final String cron, final Job job) throws Exception {
        addCronJob(groupName, cron, job.getClass());
    }

    /**
     * 创建Cron调度
     *
     * @param cron cron
     * @param job  调度
     */
    public void addCronJob(final String cron, final Class<? extends Job> job) throws Exception {
        addCronJob(IdHelper.createSimpleUuid(), cron, job);
    }

    /**
     * 创建Cron调度
     *
     * @param cron    cron
     * @param jobName 任务名称
     * @param job     调度
     */
    public void addCronJob(final String jobName, final String cron, final Class<? extends Job> job) throws Exception {
        // 通过SchedulerFactory构建Scheduler对象
        Scheduler scheduler = schedulerFactory.getScheduler();
        // 用于描叙Job实现类及其他的一些静态信息，构建一个作业实例
        JobDetail jobDetail = JobBuilder.newJob(job).withIdentity(jobName, JOB_GROUP_NAME).build();

        // 创建一个新的TriggerBuilder来规范一个触发器
        CronTrigger trigger = newTrigger()
                // 给触发器起一个名字和组名
                .withIdentity(jobName, TRIGGER_GROUP_NAME)
                .withSchedule(cronSchedule(cron)).build();

        scheduler.scheduleJob(jobDetail, trigger);
        if (!scheduler.isShutdown()) {
            scheduler.start(); // 启动
        }
    }

    /**
     * 创建Job类
     *
     * @param bean 对象
     * @return job 类
     */
    private Class<?> createJobClass(Object bean) {
        final String tempField = "oldJob";
        Class<?> oldJob1 = ClassUtils.doAddInterfaceForClass(bean, method -> {
            try {
                if ("execute".equals(method.getName()) && method.getParameterTypes().length == 1) {
                    return "oldJob.execute();";
                }
            } catch (NotFoundException e) {
            }
            return null;
        }, (ctClass, classPool) -> {
            try {
                CtField oldJob = new CtField(ClassUtils.toCtClass(bean.getClass()), tempField, ctClass);
                oldJob.setModifiers(Modifier.STATIC);
                ctClass.addField(oldJob);
            } catch (Exception e) {
                if(e.getMessage().contains("Lambda") && bean instanceof ScheduleJob) {
                    try {
                        CtField make = CtField.make("private "+ ScheduleJob.class.getName() +" " + tempField + ";", ctClass);
                        make.setModifiers(Modifier.STATIC);
                        ctClass.addField(make);
                    } catch (CannotCompileException cannotCompileException) {
                        cannotCompileException.printStackTrace();
                    }
                }
            }
        }, Job.class);

        Object object = ClassUtils.forObject(oldJob1);
        try {
            Field oldJob = oldJob1.getDeclaredField(tempField);
            ClassUtils.setFieldValue(oldJob, bean, object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object.getClass();
    }

    /**
     * 简单类型
     */
    public enum SimpleTimeType {
        /**
         * 秒
         */
        SECOND,
        /**
         * 分
         */
        MINUTE,
        /**
         * 时
         */
        HOUR,
    }
}
