package com.chua.unified.environment;

import java.util.Properties;

/**
 * 环境提供接口
 * @author CH
 */
public interface IEnvironmentProducer {
    /**
     * 获取环境
     * @return
     */
    Properties getEnvironment();

    /**
     * 设置环境
     * @param properties 环境对象
     */
    void setEnvironment(Properties properties);

    /**
     * 添加数据
     * @param key 索引
     * @param value 数据
     * @return
     */
    default IEnvironmentProducer append(final Object key, final Object value) {
        Properties environment = getEnvironment();
        if(null == environment) {
            return this;
        }
        environment.put(key, value);
        return this;
    }
}
