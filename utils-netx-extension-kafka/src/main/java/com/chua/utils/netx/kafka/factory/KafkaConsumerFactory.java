package com.chua.utils.netx.kafka.factory;

import com.chua.unified.properties.NetxProperties;
import com.chua.utils.netx.factory.INetxFactory;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

/**
 * @author CH
 */
public class KafkaConsumerFactory implements AutoCloseable, INetxFactory<KafkaConsumer> {

    private KafkaConsumer kafkaConsumer;
    private NetxProperties netxProperties;

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
    public void configure(NetxProperties netxProperties) {
        this.netxProperties = netxProperties;
    }

    @Override
    public KafkaConsumer client() {
        return kafkaConsumer;
    }

    @Override
    public void start() {
        Properties props = new Properties();
        // 是否启用自动提交，默认true
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        // key反序列化指定类
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // value反序列化指定类，注意生产者与消费者要保持一致，否则解析出问题
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        // 服务器ip:端口号，集群用逗号分隔
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, netxProperties.getHost());

        this.kafkaConsumer = new KafkaConsumer(props);
    }

    @Override
    public boolean isStart() {
        return null != kafkaConsumer;
    }
}
