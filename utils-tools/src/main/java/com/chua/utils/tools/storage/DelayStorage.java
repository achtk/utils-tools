package com.chua.utils.tools.storage;

import lombok.Getter;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 延迟
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/29
 */
public class DelayStorage {

    /**
     * 处理任务
     *
     * @param supplier 回调
     * @param delay    延迟
     * @return 结果
     */
    public synchronized static Object run(final Supplier<?> supplier, final int delay) {
        DelayQueue<DelayItem> queue = new DelayQueue<>();
        queue.put(new DelayItem(delay, supplier));

        DelayItem delayItem = null;
        try {
            delayItem = queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return delayItem.getValue().get();
    }

    static final class DelayItem implements Delayed {
        /* 触发时间*/
        private long time;
        @Getter
        private Supplier<?> value;

        public DelayItem(long time, Supplier<?> value) {
            this.time = System.currentTimeMillis() + (time > 0 ? time : 0);
            this.value = value;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return time - System.currentTimeMillis();
        }

        @Override
        public int compareTo(Delayed o) {
            DelayItem item = (DelayItem) o;
            long diff = this.time - item.time;
            if (diff <= 0) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
