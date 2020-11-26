package com.chua.utils.schedule.util;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 * quartz工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/25
 */
public class QuartzUtil {

    public void createSchedule(Class<?> job, String cron) throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
    }
}
