package com.chua.utils.netx.kafka.resolver;

import com.chua.utils.netx.resolver.NetResolver;
import com.chua.utils.netx.resolver.entity.DoubleService;
import com.chua.utils.netx.resolver.entity.NetPubSubConf;
import com.chua.utils.netx.resolver.entity.Service;
import com.chua.utils.netx.resolver.mq.NetPubSub;
import com.google.common.base.Splitter;
import lombok.NoArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * kafka解释器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
@NoArgsConstructor
public class KafkaResolver extends NetResolver implements NetPubSub<Object> {

    private KafkaProducer kafkaProducer;
    private KafkaConsumer kafkaConsumer;

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        try {
            this.kafkaProducer = new KafkaProducer(properties);
        } catch (Exception e) {
        }
        try {
            this.kafkaConsumer = new KafkaConsumer(properties);
        } catch (Exception e) {
        }
    }

    @Override
    public Service get() {
        return new DoubleService(kafkaProducer, kafkaConsumer);
    }

    @Override
    public void publish(NetPubSubConf<Object> netPubSubConf, byte[] data) throws IOException {
        Object channel = netPubSubConf.getChannel();
        KafkaProducer kafkaProducer = this.kafkaProducer;
        if (null != channel && (channel instanceof KafkaProducer)) {
            kafkaProducer = (KafkaProducer) channel;
        }
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(netPubSubConf.getTopic(), data);
        //发布
        kafkaProducer.send(record);

        if (netPubSubConf.isAutoClose()) {
            kafkaProducer.close();
        }
    }

    @Override
    public void consumer(NetPubSubConf<Object> netPubSubConf, Consumer<byte[]> consumer) throws IOException {
        Object channel = netPubSubConf.getChannel();
        KafkaConsumer kafkaConsumer = this.kafkaConsumer;
        if (null != channel && (channel instanceof KafkaConsumer)) {
            kafkaConsumer = (KafkaConsumer) channel;
        }

        final KafkaConsumer fKafkaConsumer = kafkaConsumer;

        kafkaConsumer.subscribe(Splitter.on(",").omitEmptyStrings().trimResults().splitToList(netPubSubConf.getTopic()));

        if (netPubSubConf.isAutoClose()) {
            ConsumerRecords records = kafkaConsumer.poll(Duration.ofMillis(netPubSubConf.getTimeout()));
            records.forEach(value -> {
                consumer.accept((byte[]) value);
            });
            kafkaConsumer.close();
            return;
        }

        ExecutorService executorService = netPubSubConf.getExecutorService();
        executorService.execute(() -> {
            while (true) {
                ConsumerRecords records = fKafkaConsumer.poll(Duration.ofMillis(netPubSubConf.getTimeout()));
                records.forEach(value -> {
                    consumer.accept((byte[]) value);
                });
            }
        });
    }
}
