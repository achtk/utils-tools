package com.chua.tools.example.entity;

import com.chua.utils.tools.storage.function.Notifiable;
import com.chua.utils.tools.storage.function.Notify;
import com.google.common.eventbus.Subscribe;

/**
 * @author CH
 * @version 1.0.0
 * @since 2021/2/22
 */
public class DemoNotify implements Notifiable {
    @Override
    public void notice(Notify notify, Object question) {
        System.out.println(question);
    }

    @Subscribe
    public void bus(Object value) {
        System.out.println(value);
    }
}
