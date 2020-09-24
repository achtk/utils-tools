package com.chua.utils.tools.spring.httpinvoke;

import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;

/**
 * HttpInvoke 工具
 * @author CH
 */
public class HttpInvokeFactory {
    /**
     * 创建消费者
     * @param directUrl http路径
     * @param serviceInterface  服务接口
     * @return HttpInvokerProxyFactoryBean
     */
    public synchronized static HttpInvokerProxyFactoryBean createConsumer(final String directUrl, final Class<?> serviceInterface) {
        HttpInvokerProxyFactoryBean httpInvokerProxyFactoryBean = new HttpInvokerProxyFactoryBean();
        httpInvokerProxyFactoryBean.setServiceUrl(directUrl);
        httpInvokerProxyFactoryBean.setServiceInterface(serviceInterface);
        return httpInvokerProxyFactoryBean;
    }

    /**
     * 创建生产者
     * @param ref 实体
     * @param serviceInterface  服务接口
     * @return HttpInvokerProxyFactoryBean
     */
    public synchronized static HttpInvokerServiceExporter createProducer(final Object ref, final Class<?> serviceInterface) {
        HttpInvokerServiceExporter httpInvokerServiceExporter = new HttpInvokerServiceExporter();
        httpInvokerServiceExporter.setService(ref);
        httpInvokerServiceExporter.setServiceInterface(serviceInterface);
        return httpInvokerServiceExporter;
    }
}
