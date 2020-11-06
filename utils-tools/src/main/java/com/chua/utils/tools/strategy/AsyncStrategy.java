package com.chua.utils.tools.strategy;

import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.function.intercept.MethodIntercept;
import com.google.common.base.Joiner;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * 异步策略
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
@NoArgsConstructor
public class AsyncStrategy<T> extends StandardProxyStrategy<T> implements Strategy<T>, MethodIntercept<MethodProxy>, AutoCloseable {

    @Setter
    protected Consumer consumer;

    public AsyncStrategy(Consumer consumer) {
        this.consumer = consumer;
    }

    protected ExecutorService executorService = ThreadHelper.newProcessorThreadExecutor("strategy-proxy");

    @Override
    public Object invoke(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        executorService.execute(() -> {
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
        });
        return null;
    }

    /**
     * 获取索引值
     *
     * @param method 方法名
     * @param args   参数
     * @return 索引值
     */
    private String getKey(Method method, Object[] args) {
        String name = method.getName();
        return name + "$" + Joiner.on(",").join(args);
    }

    @Override
    public T create(T source) {
        super.setMethodIntercept(this);
        return super.proxy(source);
    }

    @Override
    public void close() throws Exception {
        executorService.shutdownNow();
        executorService.shutdown();
    }
}
