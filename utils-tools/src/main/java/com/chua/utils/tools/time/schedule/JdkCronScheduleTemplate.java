package com.chua.utils.tools.time.schedule;

import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.entity.CronExpression;
import com.chua.utils.tools.function.ScheduleJob;
import com.chua.utils.tools.util.ClassUtils;
import lombok.Data;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * jdkcron调度任务
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/31
 */
public class JdkCronScheduleTemplate implements CronScheduleOperations<Runtime>, Runnable {

    private final Map<String, CronTime> groupCache = new ConcurrentHashMap<>();
    private final int processor = Runtime.getRuntime().availableProcessors();
    private final ExecutorService scheduledExecutorService = ThreadHelper.newSingleThreadExecutor("jdk-cron-thread-pools");

    {
        for (int i = 0; i < processor; i++) {
            scheduledExecutorService.execute(this);
        }
    }

    @Override
    public void addCronJob(String groupName, String cron, Class<Runtime> job) throws Exception {
        //缓存数据
        this.createJobTime(groupName, cron, job);
    }

    @Override
    public void updateJobTime(String jobName, String cron) throws Exception {
        if (groupCache.containsKey(jobName)) {
            CronTime cronTime = groupCache.get(jobName);
            cronTime.setCron(cron);
            cronTime.setCronExpression(new CronExpression(cron));
        }
    }

    @Override
    public void addCronJob(String jobName, String cron, ScheduleJob job) throws Exception {
        //缓存数据
        this.createJobTime(jobName, cron, job);
    }

    @Override
    public void removeJob(String jobName) throws Exception {
        groupCache.remove(jobName);
    }

    /**
     * 创建缓存时间
     *
     * @param jobName 任务名称
     * @param cron    时间
     * @param job     调度
     */
    protected void createJobTime(String jobName, String cron, Object job) throws Exception {
        if (null == job) {
            throw new NullPointerException("job cannot be empty");
        }
        CronTime jobTime = null;
        if (!groupCache.containsKey(jobName)) {
            jobTime = new CronTime();
            jobTime.setGroupName(jobName);
            if (job instanceof Class) {
                jobTime.setJob(ClassUtils.forObject((Class<?>) job, Runnable.class));
            } else {
                jobTime.setJob(job);
            }
            jobTime.setCron(cron);
            jobTime.setInitialDate(new Date());
            jobTime.setStartDate(new Date());
            jobTime.setCronExpression(new CronExpression(cron));
        } else {
            jobTime = groupCache.get(jobName);
        }
        //缓存
        groupCache.put(jobName, jobTime);
    }

    @Override
    public void run() {
        while (true) {
            Date date = new Date();
            for (CronTime cronTime : groupCache.values()) {
                Date startDate = cronTime.getStartDate();
                CronExpression cronExpression = cronTime.getCronExpression();
                Date timeAfter = cronExpression.getNextValidTimeAfter(startDate);
                if (startDate.compareTo(date) == 0) {
                    this.execute(cronTime.getJob());
                    cronTime.setStartDate(timeAfter);
                    cronTime.setNextDate(cronExpression.getNextValidTimeAfter(timeAfter));
                } else if (startDate.before(date)) {
                    if (timeAfter.after(date)) {
                        continue;
                    } else if (timeAfter.before(date)) {
                        cronTime.setStartDate(cronExpression.getNextValidTimeAfter(date));
                    } else {
                        this.execute(cronTime.getJob());
                        cronTime.setStartDate(cronExpression.getNextValidTimeAfter(date));
                    }
                }
            }
        }
    }

    /**
     * 执行方法
     *
     * @param job 任务
     */
    private void execute(Object job) {
        if (job instanceof ScheduleJob) {
            try {
                ((ScheduleJob) job).execute();
            } catch (Exception e) {
            }
        } else if (job instanceof Runnable) {
            ((Runnable) job).run();
        }
    }

    @Override
    public void close() throws Exception {
        scheduledExecutorService.shutdownNow();
    }


    /**
     * 时间信息
     */
    @Data
    protected final class CronTime {
        /**
         * 初始化时间
         */
        private Date initialDate;
        /**
         * 开始时间
         */
        private Date startDate;
        /**
         * 下一次时间
         */
        private Date nextDate;
        /**
         * 表达式
         */
        private CronExpression cronExpression;
        /**
         * 分组名
         */
        private String groupName;
        /**
         * 时间
         */
        private String cron;

        /**
         * 任务
         */
        private Object job;
    }

}
