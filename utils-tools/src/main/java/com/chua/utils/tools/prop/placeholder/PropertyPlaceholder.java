package com.chua.utils.tools.prop.placeholder;

import com.chua.utils.tools.text.IdHelper;

import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * 占位接口
 *
 * @author CH
 * @version 1.0
 * @since 2020/10/28 18:19
 */
public interface PropertyPlaceholder {
    /**
     * 占位前缀
     *
     * @param before 占位前缀
     * @return
     */
    void before(String before);

    /**
     * 占位后缀
     *
     * @param after 占位后缀
     * @return
     */
    void after(String after);

    /**
     * 分隔符
     *
     * @param valueSeparate 分隔符
     * @return
     */
    void valueSeparate(String valueSeparate);

    /**
     * 忽略处理未知字段
     *
     * @param ignoreUnknownFields
     */
    void ignoreUnknownFields(boolean ignoreUnknownFields);

    /**
     * 添加配置
     *
     * @param name       配置名称
     * @param properties 配置
     */
    void addPropertySource(String name, Properties properties);

    /**
     * 添加配置
     *
     * @param properties 配置
     */
    default void addPropertySource(Properties properties) {
        addPropertySource(IdHelper.createUuid(), properties);
    }

    /**
     * 添加配置
     *
     * @param properties 配置
     */
    default void addPropertySource(Map<String, Properties> propertieses) {
        for (Map.Entry<String, Properties> entry : propertieses.entrySet()) {
            addPropertySource(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 添加配置
     *
     * @param name 索引
     * @return Object
     */
    default Object getValue(String name) {
        return placeholder(get(name));
    }

    /**
     * 添加配置
     *
     * @param value 数据
     * @return Object
     */
    Object placeholder(Object value);


    /**
     * 允许
     *
     * @param value 值
     * @return
     */
    boolean isPlaceholder(String value);

    /**
     * 正则表达式
     *
     * @return Pattern
     */
    Pattern pattern();

    /**
     * 是否存在索引
     *
     * @param key 索引
     * @return boolean
     */
    boolean containerKey(String key);

    /**
     * 获取原始值
     *
     * @param key 索引
     * @return Object
     */
    Object get(String key);
}
