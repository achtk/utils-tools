package com.chua.utils.netx.rabbitmq.util;

import com.rabbitmq.client.Channel;

import java.io.IOException;

/**
 * rabbit-mq 工具类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
public class RabbitMqUtil {


    public static void publish(String channelName, byte[] data, Channel channel) throws IOException {
        //声明队列
        //queueDeclare第一个参数表示队列名称、
        // 第二个参数为是否持久化（true表示是，队列将在服务器重启时生存）、
        // 第三个参数为是否是独占队列（创建者可以使用的私有队列，断开后自动删除）、
        // 第四个参数为当所有消费者客户端连接断开时是否自动删除队列、第五个参数为队列的其他参数
        channel.queueDeclare(channelName, false, false, false, null);

        //发送消息到队列
        //basicPublish第一个参数为交换机名称、
        // 第二个参数为队列映射的路由key、
        // 第三个参数为消息的其他属性、
        // 第四个参数为发送信息的主体
        channel.basicPublish("", channelName, null, data);
    }
}
