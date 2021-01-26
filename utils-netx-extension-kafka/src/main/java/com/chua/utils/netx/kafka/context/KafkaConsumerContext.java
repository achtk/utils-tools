package com.chua.utils.netx.kafka.context;

import com.chua.utils.netx.kafka.factory.KafkaConsumerFactory;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.function.ToolsConsumer;
import com.chua.utils.tools.properties.NetProperties;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.ExecutorService;

/**
 * kafka消费者
 * @author CH
 */
public class KafkaConsumerContext implements AutoCloseable {

    private final KafkaConsumerFactory kafkaConsumerFactory;
    private final KafkaConsumer kafkaConsumer;
    private NetProperties netProperties;
    private ExecutorService executorService;

    public KafkaConsumerContext(NetProperties netProperties) {
        this.netProperties = netProperties;
        this.kafkaConsumerFactory = new KafkaConsumerFactory(netProperties);
        this.kafkaConsumerFactory.start();
        this.kafkaConsumer = this.kafkaConsumerFactory.client();
        this.executorService = ThreadHelper.newCachedThreadPool();
    }

    public KafkaConsumerContext(NetProperties netProperties, final int core) {
        this.netProperties = netProperties;
        this.kafkaConsumerFactory = new KafkaConsumerFactory(netProperties);
        this.kafkaConsumerFactory.start();
        this.kafkaConsumer = this.kafkaConsumerFactory.client();
        this.executorService = ThreadHelper.newFixedThreadExecutor(core);
    }
    /**
     * 订阅
     * @param topics 订阅
     * @param timeout 超时时间
     * @param consumer 回调
     */
    public synchronized void asyncSubscribe(Collection<String> topics, final int timeout, ToolsConsumer consumer) {
        this.executorService.execute(new Runnable() {
            @Override
            public void run() {
                kafkaConsumer.subscribe(topics);
                while (true) {
                    ConsumerRecords records = kafkaConsumer.poll(Duration.ofMillis(timeout));
                    consumer.next(records);
                }
            }
        });
    }
    /**
     * 订阅
     * @param topics 订阅
     * @param timeout 超时时间
     * @param consumer 回调
     */
    public synchronized void subscribe(Collection<String> topics, final int timeout, ToolsConsumer consumer) {
        this.kafkaConsumer.subscribe(topics);
        while (true) {
            ConsumerRecords records = this.kafkaConsumer.poll(Duration.ofMillis(timeout));
            consumer.next(records);
        }
    }

    @Override
    public void close() throws Exception {
        if(null != this.executorService) {
            this.executorService.shutdown();
            this.executorService.shutdownNow();
        }

        if(null != this.kafkaConsumerFactory) {
            kafkaConsumerFactory.close();
        }
    }
}
