package com.chua.utils.netx.kafka.context;

import com.chua.unified.properties.NetxProperties;
import com.chua.utils.netx.kafka.factory.KafkaProducerFactory;
import com.chua.utils.tools.common.ThreadHelper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutorService;

/**
 * kafka消费者
 * @author CH
 */
public class KafkaProducerContext implements AutoCloseable{

    private final KafkaProducerFactory kafkaProducerFactory;
    private final KafkaProducer kafkaProducer;
    private NetxProperties netxProperties;
    private ExecutorService executorService;

    public KafkaProducerContext(NetxProperties netxProperties) {
        this.netxProperties = netxProperties;
        this.kafkaProducerFactory = new KafkaProducerFactory(netxProperties);
        this.kafkaProducerFactory.start();
        this.kafkaProducer = this.kafkaProducerFactory.client();
        this.executorService = ThreadHelper.newCachedThreadPool();
    }

    public KafkaProducerContext(NetxProperties netxProperties, final int core) {
        this.netxProperties = netxProperties;
        this.kafkaProducerFactory = new KafkaProducerFactory(netxProperties);
        this.kafkaProducerFactory.start();
        this.kafkaProducer = this.kafkaProducerFactory.client();
        this.executorService = ThreadHelper.newFixedThreadExecutor(core);
    }
    /**
     * 订阅
     * @param topics 订阅
     * @param data 数据
     */
    public synchronized void asyncSubscribe(Collection<String> topics, final String data) {
        this.executorService.execute(new Runnable() {
            @Override
            public void run() {
                for (String topic : topics) {
                    ProducerRecord<String, String> record = new ProducerRecord<>(topic, data);
                    kafkaProducer.send(record);
                }
            }
        });
    }
    /**
     * 订阅
     * @param topic 订阅
     * @param data 数据
     */
    public synchronized void subscribe(String topic, final String data) {
        subscribe(Collections.singletonList(topic), data);
    }
    /**
     * 订阅
     * @param topics 订阅
     * @param data 数据
     */
    public synchronized void subscribe(Collection<String> topics, final String data) {
        for (String topic : topics) {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, data);
            kafkaProducer.send(record);
        }
    }

    @Override
    public void close() throws Exception {
        if(null != this.executorService) {
            this.executorService.shutdown();
            this.executorService.shutdownNow();
        }

        if(null != kafkaProducerFactory) {
            kafkaProducerFactory.close();
        }
    }
}
