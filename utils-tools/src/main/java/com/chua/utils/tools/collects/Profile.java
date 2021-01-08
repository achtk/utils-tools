package com.chua.utils.tools.collects;

import com.chua.utils.tools.prop.placeholder.PropertyPlaceholder;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 配置文件
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/1/7
 */
public interface Profile {
    /**
     * 基础配置对象
     *
     * @return 配置对象
     */
    static Profile create() {
        return new BasicProfile();
    }

    /**
     * 获取指定名称的配置
     *
     * @param name 名称
     * @return 配置
     */
    Properties getProfile(String name);

    /**
     * 获取配置的值
     *
     * @param name 名称
     * @param key  索引
     * @return 索引对应的值
     */
    Object getValue(String name, String key);

    /**
     * 获取配置的值
     *
     * @param key 索引
     * @return 索引对应的值
     */
    Object getValue(String key);

    /**
     * 获取配置的值
     *
     * @param key 索引
     * @return 索引对应的值
     */
    List<Object> getValues(String key);

    /**
     * 占位符处理
     *
     * @param key 索引
     * @return 占位符
     */
    Object placeholder(String key);

    /**
     * 占位符处理
     *
     * @param value 数据
     * @return 占位符处理的数据
     */
    Object handler(Object value);

    /**
     * 占位符处理
     *
     * @param propertyPlaceholder 占位符
     */
    void propertyPlaceholder(PropertyPlaceholder propertyPlaceholder);

    /**
     * 设置值
     *
     * @param name  配置名称
     * @param key   索引
     * @param value 值
     */
    void set(String name, String key, Object value);

    /**
     * 设置值
     *
     * @param name       配置名称
     * @param properties 配置
     */
    void set(String name, Properties properties);

    /**
     * 设置值
     *
     * @param profile 配置
     */
    void set(Profile profile);

    /**
     * 配置项
     *
     * @return 配置项
     */
    Map<String, Properties> profiles();
}
