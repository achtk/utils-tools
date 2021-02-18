package com.chua.utils.tools.strategy;

import com.chua.utils.tools.common.ThreadHelper;
import lombok.NoArgsConstructor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 调度策略
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
@NoArgsConstructor
public class CycleStrategy<T> extends AsyncStrategy<T> {

    private int cycle = 10;
    private final ScheduledExecutorService scheduledExecutorService = ThreadHelper.newScheduledThreadPoolExecutor(1);


    public CycleStrategy(int cycle) {
        this.cycle = cycle;
    }

    @Override
    public Object invoke(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                consumer.accept(method.invoke(getSource(), args));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                try {
                    close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, cycle, TimeUnit.SECONDS);
        return null;
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (null != scheduledExecutorService) {
            scheduledExecutorService.shutdownNow();
            scheduledExecutorService.shutdown();
        }
    }
}
