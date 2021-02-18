package com.chua.utils.tools.storage;

import com.chua.utils.tools.time.schedule.CronScheduleOperations;
import com.chua.utils.tools.time.schedule.JdkCronScheduleTemplate;
import com.chua.utils.tools.time.schedule.JdkSimpleScheduleTemplate;
import com.chua.utils.tools.time.schedule.SimpleScheduleOperations;
import com.chua.utils.tools.util.ClassUtils;
import com.chua.utils.tools.util.IdUtils;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 调度
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/29
 */
public class TimerStorage {

    private static final String QUARTZ_CRON = "com.chua.utils.schedule.template.QuartzCronScheduleTemplate";
    private static final String QUARTZ_SIMPLE = "com.chua.utils.schedule.template.QuartzSimpleScheduleTemplate";

    private TimerStorage() {
    }

    /**
     * 处理任务
     *
     * @param supplier 回调
     * @param cron     cron
     */
    public static void run(final Consumer<String> supplier, final String cron) {
        run(supplier, IdUtils.createUuid(), cron);
    }

    /**
     * 处理任务
     *
     * @param consumer 回调
     * @param group    分组
     * @param cron     cron
     */
    public static void run(final Consumer<String> consumer, final String group, final String cron) {
        CronScheduleOperations<?> cronScheduleTemplate;
        if (ClassUtils.isPresent(QUARTZ_CRON)) {
            cronScheduleTemplate = ClassUtils.forObject(QUARTZ_CRON);
        } else {
            cronScheduleTemplate = new JdkCronScheduleTemplate();
        }

        try {
            cronScheduleTemplate.addCronJob(group, cron, () -> consumer.accept(group));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                cronScheduleTemplate.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理任务
     *
     * @param consumer 回调
     * @param time     time
     */
    public static void doWithSimple(final Consumer<String> consumer, final int time) {
        doWithSimple(consumer, IdUtils.createUuid(), time);
    }

    /**
     * 处理任务
     *
     * @param consumer 回调
     * @param group    分组
     * @param time     time
     */
    public static void doWithSimple(final Consumer<String> consumer, final String group, final int time) {
        SimpleScheduleOperations<?> scheduleOperations;
        if (ClassUtils.isPresent(QUARTZ_SIMPLE)) {
            scheduleOperations = ClassUtils.forObject(QUARTZ_SIMPLE);
        } else {
            scheduleOperations = new JdkSimpleScheduleTemplate();
        }

        try {
            scheduleOperations.addSimpleJob(group, time, TimeUnit.SECONDS, () -> consumer.accept(group));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                scheduleOperations.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
