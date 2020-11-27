package com.chua.utils.netx.resolver.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 订阅发布对象
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/21
 */
@Data
public class NetPubSubConf<T> {
    /**
     * 交换机名称
     */
    private String exchange = "default";
    /**
     * 交换机类型
     * <p>
     * 直连交换机(direct exchange): (empty string) and amq.direct/名字绑定到同名的队列
     * 默认交换机(direct exchange): empty string/名字为""的直连交换机
     * 扇型交换机(fanout exchange): amq.fanout/交换机会将消息的拷贝分别发送给这所有的N个队列
     * 主题交换机(topic exchange): amq.topic/路由键和队列到交换机的绑定模式之间的匹配，将消息路由给一个或多个队列。
     * 头交换机(headers exchange): amq.match/headers头
     */
    private String exchangeType = "topic";
    /**
     * 队列名称
     */
    private String queue = "default";
    /**
     * 队列参数
     * 队列声明后无法修改此参数
     * *Message TTL(x-message-ttl)：设置队列中的所有消息的生存周期(统一为整个队列的所有消息设置生命周期), 也可以在发布消息的时候单独为某个消息指定剩余生存时间,单位毫秒, 类似于redis中的ttl，生存时间到了，消息会被从队里中删除，注意是消息被删除，而不是队列被删除， 特性Features=TTL,
     *      单独为某条消息设置过期时间AMQP.BasicProperties.Builder properties = new AMQP.BasicProperties().builder().expiration(“6000”);
     *      channel.basicPublish(EXCHANGE_NAME, “”, properties.build(), message.getBytes(“UTF-8”));
     * Auto Expire(x-expires): 当队列在指定的时间没有被访问(consume, basicGet, queueDeclare…)就会被删除,Features=Exp
     * Max Length(x-max-length): 限定队列的消息的最大值长度n,保留最新的n条,超过指定长度将会把最早的几条删除掉， 类似于mongodb中的固定集合，例如保存最新的100条消息, Feature=Lim
     * Max Length Bytes(x-max-length-bytes)(1024): 限定队列最大占用的空间大小， 一般受限于内存、磁盘的大小, Features=Lim B
     * Dead letter exchange(x-dead-letter-exchange)： 当队列消息长度大于最大长度、或者过期的等，将从队列中删除的消息推送到指定的交换机中去而不是丢弃掉,Features=DLX
     * Dead letter routing key(x-dead-letter-routing-key)：将删除的消息推送到指定交换机的指定路由键的队列中去, Feature=DLK
     * Maximum priority(x-max-priority)：优先级队列，声明队列时先定义最大优先级值(定义最大值一般不要太大)，在发布消息的时候指定该消息的优先级， 优先级更高（数值更大的）的消息先被消费,
     * Lazy mode(x-queue-mode=lazy)： Lazy Queues: 先将消息保存到磁盘上，不放在内存中，当消费者开始消费的时候才加载到内存中
     * Master locator(x-queue-master-locator)
     */
    private Map<String, Object> getQueueParams = new HashMap<>();
    /**
     * 路由
     */
    private String routingKey = "default";
    /**
     * 管道名称
     */
    private String topic = "default";
    /**
     * 管道(空表示新建)
     */
    private T channel;
    /**
     * 获取超时
     */
    private long timeout = 30 * 1000;
    /**
     * 自动关闭管道
     */
    private boolean autoClose;
    /**
     * 多线程
     */
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

}
