package com.chua.utils.netx.rabbitmq.resolver;

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
public class RabbitMqResolver extends NetResolver<Connection> implements NetPubSub<Channel> {

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
    public Service<Connection> get() {
        return new Service(this.connection);
    }


    @Override
    public void publish(NetPubSubConf<Channel> netPubSubConf, byte[] data) throws IOException {
        Channel channel = netPubSubConf.getChannel();
        //队列名称
        String queueName = netPubSubConf.getQueue();

        if (null == channel) {
            channel = connection.createChannel();
            //声明交换机
            // 第一个参数，exchange：交换机名称。数据类型：String
            // 第二个参数，type：交换机的类型(direct/topic/fanout)。数据类型：String
            channel.exchangeDeclare(netPubSubConf.getExchange(), netPubSubConf.getExchangeType());
            //队列名称
            queueName = Strings.isNullOrEmpty(netPubSubConf.getQueue()) ? channel.queueDeclare().getQueue() : netPubSubConf.getQueue();
            //声明队列
            // 第一个参数，queueName:对列名称。数据类型：String
            // 第二个参数，durable：是否持久化, 队列的声明默认是存放到内存中的，如果rabbitmq重启会丢失，如果想重启之后还存在就要使队列持久化，保存到Erlang自带的Mnesia数据库中，当rabbitmq重启之后会读取该数据库。数据类型：boolean
            // 第三个参数，exclusive：是否排外的。数据类型：boolean
            // 第四个参数，autoDelete：是否自动删除。数据类型：boolean
            // 第五个参数，arguments：参数。数据类型：Map<String, Object>
            channel.queueDeclare(queueName, true, false, false, netPubSubConf.getGetQueueParams());
            //绑定队列
            //第一个参数，queueName:对列名称。数据类型：String
            //第二个参数，exchange：交换机名称。数据类型：String
            //第三个参数，routingKey：队列跟交换机绑定的键值。数据类型：String
            channel.queueBind(queueName, netPubSubConf.getExchange(), netPubSubConf.getRoutingKey());
            netPubSubConf.setChannel(channel);
        }
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

        //队列名称
        String queueName = netPubSubConf.getQueue();
        if (null == channel) {
            channel = connection.createChannel();
            //声明交换机
            // 第一个参数，exchange：交换机名称。数据类型：String
            // 第二个参数，type：交换机的类型(direct/topic/fanout)。数据类型：String
            channel.exchangeDeclare(netPubSubConf.getExchange(), netPubSubConf.getExchangeType(), false, false, false, null);
            //绑定队列
            //第一个参数，queueName:对列名称。数据类型：String
            //第二个参数，exchange：交换机名称。数据类型：String
            //第三个参数，routingKey：队列跟交换机绑定的键值。数据类型：String
            channel.queueBind(queueName, netPubSubConf.getExchange(), netPubSubConf.getRoutingKey());

            netPubSubConf.setQueue(queueName);
            netPubSubConf.setChannel(channel);
        }
        //管道
        final Channel fChannel = channel;
        //队列
        final String fQueueName = queueName;
        //尝试关闭管道
        if (netPubSubConf.isAutoClose()) {
            //订阅
            fChannel.basicConsume(fQueueName, new DefaultConsumer(channel) {
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
                fChannel.basicConsume(fQueueName, new DefaultConsumer(fChannel) {
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
