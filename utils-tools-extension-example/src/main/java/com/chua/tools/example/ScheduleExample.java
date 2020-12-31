package com.chua.tools.example;

import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.time.schedule.CronScheduleOperations;
import com.chua.utils.tools.time.schedule.JdkCronScheduleTemplate;
import com.chua.utils.tools.util.DateUtils;

import java.util.concurrent.TimeUnit;

/**
 * Quartz 工具
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/29
 */
public class ScheduleExample extends BaseExample {

    public static void main(String[] args) throws Exception {
        //CronScheduleOperations cronScheduleTemplate = new QuartzCronScheduleTemplate();
        CronScheduleOperations cronScheduleTemplate = new JdkCronScheduleTemplate();
        //添加任务
        cronScheduleTemplate.addCronJob("test", "0/1 * * * * ?", () -> {
            log.info(DateUtils.currentString());
        });

        ThreadHelper.sleepQuietly(10, TimeUnit.SECONDS);
        //修改任务时间
        cronScheduleTemplate.updateJobTime("test", "0/2 * * * * ?");
    }
}
