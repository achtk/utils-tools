package com.chua.tools.example;


import com.chua.tools.example.entity.TDemoInfo;
import com.chua.tools.example.entity.TDemoInfoImpl;
import com.chua.utils.netx.rpc.config.RpcConsumerConfig;
import com.chua.utils.netx.rpc.config.RpcProviderConfig;
import com.chua.utils.netx.rpc.dubbo.DubboConsumer;
import com.chua.utils.netx.rpc.dubbo.DubboProvider;
import com.chua.utils.netx.rpc.resolver.RpcConsumer;
import com.chua.utils.netx.rpc.resolver.RpcProvider;
import com.chua.utils.tools.util.ThreadUtils;

/**
 * Rpc
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/2
 */
public class RpcExample {

    public static void main(String[] args) {
        //测试发布
        ThreadUtils.newAndRunThread(() -> {
            testProducer();
        });
        ThreadUtils.sleepSecondsQuietly(2);
        //测试消费
        testConsumer();

    }

    /**
     * 测试消费
     */
    private static void testConsumer() {
        RpcConsumer<TDemoInfoImpl> rpcConsumer = new DubboConsumer<>();
        TDemoInfo consumer = rpcConsumer.consumer(RpcConsumerConfig.of("demo", TDemoInfo.class));
        System.out.println(consumer.getUuid());
    }

    /**
     * 测试发布
     */
    private static void testProducer() {
        RpcProvider<TDemoInfoImpl> rpcProvider = new DubboProvider();
        rpcProvider.provider(RpcProviderConfig.of("demo", TDemoInfo.class, new TDemoInfoImpl()));
    }
}
