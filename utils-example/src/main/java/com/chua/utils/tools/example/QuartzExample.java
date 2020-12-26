package com.chua.utils.tools.example;

import com.chua.utils.schedule.template.QuartzTemplate;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.util.DateUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/25
 */
public class QuartzExample {

    public static void main(String[] args) throws Exception {
        QuartzTemplate quartzTemplate = new QuartzTemplate();
        quartzTemplate.addCronJob("test", "0/1 * * * * ?", () -> {
            System.out.println(DateUtils.currentString());
        });

        ThreadHelper.sleepQuietly(4, TimeUnit.SECONDS);

        quartzTemplate.updateCronJobTime("test", "0/2 * * * * ?");
    }
}
