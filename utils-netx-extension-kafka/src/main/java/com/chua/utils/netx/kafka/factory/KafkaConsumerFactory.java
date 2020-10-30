package com.chua.utils.netx.kafka.factory;

import com.chua.utils.tools.properties.NetProperties;
import com.chua.utils.netx.factory.INetFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

/**
 * kafka消费者
 * @author CH
 */
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerFactory implements AutoCloseable, INetFactory<KafkaConsumer> {

    private KafkaConsumer kafkaConsumer;
    @NonNull
    private NetProperties netProperties;

    @Override
    public void close() {
        if(null != this.kafkaConsumer) {
            try {
                kafkaConsumer.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void configure(NetProperties netProperties) {
        this.netProperties = netProperties;
    }

    @Override
    public KafkaConsumer client() {
        return kafkaConsumer;
    }

    @Override
    public void start() {
        log.info(">>>>>>>>>>> KafkaConsumerFactory Starting to connect");
        Properties props = new Properties();
        // 是否启用自动提交，默认true
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        // key反序列化指定类
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // value反序列化指定类，注意生产者与消费者要保持一致，否则解析出问题
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 服务器ip:端口号，集群用逗号分隔
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, netProperties.getHost());

        try {
            this.kafkaConsumer = new KafkaConsumer(props);
            log.info(">>>>>>>>>>> KafkaConsumerFactory connection complete.");
        } catch (Exception e) {
            e.printStackTrace();
            log.info(">>>>>>>>>>> KafkaConsumerFactory connection activation failed.");
        }
    }

    @Override
    public boolean isStart() {
        return null != kafkaConsumer;
    }
}
