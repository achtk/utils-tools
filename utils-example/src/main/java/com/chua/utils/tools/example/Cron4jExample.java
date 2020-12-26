package com.chua.utils.tools.example;

import it.sauronsoftware.cron4j.Scheduler;

/**
 * @author CH
 * @version 1.0.0
 * @since 2020/12/25
 */
public class Cron4jExample {

    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();
        //写法一：此种方式，控制台每分钟打印
        scheduler.schedule("*/1 * * * *", () -> System.out.println("Every Minute Run."));
        scheduler.start();
        try {
            Thread.sleep(1000L * 60L * 10L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scheduler.stop();
    }
}
