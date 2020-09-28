package com.chua.utils.netx.kafka.factory;

import com.chua.unified.properties.NetxProperties;
import com.chua.utils.netx.factory.INetxFactory;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

/**
 * kafka生产者
 * @author CH
 */
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerFactory<K, V> implements AutoCloseable, INetxFactory<KafkaProducer> {

    private KafkaProducer kafkaProducer;
    @NonNull
    private NetxProperties netxProperties;


    @Override
    public void close() {
        if(null != this.kafkaProducer) {
            try {
                kafkaProducer.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void configure(NetxProperties netxProperties) {
        this.netxProperties = netxProperties;
    }

    @Override
    public KafkaProducer client() {
        return kafkaProducer;
    }

    @Override
    public void start() {
        log.info(">>>>>>>>>>> KafkaProducerFactory Starting to connect");

        Properties props = new Properties();
        // 是否启用自动提交，默认true
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        // key反序列化指定类
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // value反序列化指定类，注意生产者与消费者要保持一致，否则解析出问题
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 服务器ip:端口号，集群用逗号分隔
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, netxProperties.getHost());

        try {
            this.kafkaProducer = new KafkaProducer(props);
            log.info(">>>>>>>>>>> KafkaProducerFactory connection complete.");
        } catch (Exception e) {
            e.printStackTrace();
            log.info(">>>>>>>>>>> KafkaProducerFactory connection activation failed.");
        }
    }

    @Override
    public boolean isStart() {
        return null != kafkaProducer;
    }
}
