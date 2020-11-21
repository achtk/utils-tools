package com.chua.utils.netx.redis.rabbitmq.resolver;

import com.chua.utils.netx.resolver.NetResolver;
import com.chua.utils.netx.resolver.entity.NetPubSubConf;
import com.chua.utils.netx.resolver.entity.Service;
import com.chua.utils.netx.resolver.mq.NetPubSub;
import com.chua.utils.tools.bean.copy.BeanCopy;
import com.google.common.base.Strings;
import com.rabbitmq.client.*;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * rabbit解释器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
@NoArgsConstructor
public class RabbitMqResolver extends NetResolver implements NetPubSub<Channel> {

    private ConnectionFactory connectionFactory;
    private Connection connection;

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        BeanCopy<ConnectionFactory> beanCopy = BeanCopy.of(new ConnectionFactory());
        beanCopy.with(properties);

        this.connectionFactory = beanCopy.create();
        try {
            this.connection = this.connectionFactory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Service get() {
        return new Service(this.connection);
    }


    @Override
    public void publish(NetPubSubConf<Channel> netPubSubConf, byte[] data) throws IOException {
        Channel channel = netPubSubConf.getChannel();
        if (null == channel) {
            channel = connection.createChannel();
            netPubSubConf.setChannel(channel);
        }
        //绑定交换机
        channel.exchangeDeclare(netPubSubConf.getExchange(), netPubSubConf.getExchangeType());
        //发布
        channel.basicPublish(netPubSubConf.getExchange(), netPubSubConf.getRoutingKey(), null, data);
        //尝试关闭管道
        if (netPubSubConf.isAutoClose()) {
            try {
                channel.close();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void consumer(NetPubSubConf<Channel> netPubSubConf, Consumer<byte[]> consumer) throws IOException {
        Channel channel = netPubSubConf.getChannel();
        if (null == channel) {
            channel = connection.createChannel();
            netPubSubConf.setChannel(channel);
        }
        //管道
        final Channel fChannel = channel;
        //绑定交换机
        fChannel.exchangeDeclare(netPubSubConf.getExchange(), netPubSubConf.getExchangeType());
        //队列名称
        String queueName = Strings.isNullOrEmpty(netPubSubConf.getQueue()) ? fChannel.queueDeclare().getQueue() : netPubSubConf.getQueue();
        //绑定队列
        fChannel.queueBind(queueName, netPubSubConf.getExchange(), netPubSubConf.getRoutingKey());
        //尝试关闭管道
        if (netPubSubConf.isAutoClose()) {
            //订阅
            fChannel.basicConsume(queueName, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    super.handleDelivery(consumerTag, envelope, properties, body);
                    consumer.accept(body);
                }
            });
            try {
                fChannel.close();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            return;
        }

        ExecutorService executorService = netPubSubConf.getExecutorService();
        executorService.execute(() -> {
            //订阅
            try {
                fChannel.basicConsume(queueName, new DefaultConsumer(fChannel) {
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        super.handleDelivery(consumerTag, envelope, properties, body);
                        consumer.accept(body);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
