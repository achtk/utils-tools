package com.chua.utils.tools.example;

import com.chua.utils.schedule.template.QuartzTemplate;
import com.chua.utils.tools.common.DateHelper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/11/25
 */
public class ScheduleExample {

    public static void main(String[] args) throws Exception {
        QuartzTemplate template = new QuartzTemplate.Builder()
                .addJob(SimpleJob.class)
                .addCronTrigger("0/1 * * * * ?")
                .build();
        template.start();
        //01025FBDDDCE 02F1001400050F3836363731343034343931333031390400
        //01005F2CBD72 02F100131C2C0F3836363731343034343931333031390F
        //01025FBDED59 02F1001400010F3836363731343034343931333031390F00
        //01025FBDEDED 02F1001300010F3836363731343034343931333031390F
    }

    public static class SimpleJob implements Job {

        private static LongAdder longAdder = new LongAdder();

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println(DateHelper.currentString() + ":" + (longAdder.longValue()));
            longAdder.increment();
        }
    }
}
