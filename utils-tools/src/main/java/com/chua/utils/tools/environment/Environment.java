package com.chua.utils.tools.environment;

import com.chua.utils.tools.prop.source.PropertySource;

import java.util.List;

/**
 * 环境变量
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/4
 */
public interface Environment {
    /**
     * 添加数据源
     *
     * @param propertySource 数据源
     */
    void addPropertySource(PropertySource propertySource);

    /**
     * 添加数据源到最后
     *
     * @param propertySource 数据源
     */
    void addLastPropertySource(PropertySource propertySource);

    /**
     * 添加数据源到最后
     *
     * @param propertySource 数据源
     */
    void addFirstPropertySource(PropertySource propertySource);

    /**
     * 获取 PropertySource
     *
     * @param name PropertySource名称
     * @return PropertySource
     */
    PropertySource get(String name);

    /**
     * 获取值
     *
     * @param name 索引
     * @return 值
     */
    Object getObject(String name);

    /**
     * 获取值
     *
     * @param name 索引
     * @return 值
     */
    List<Object> getList(String name);

    /**
     * 是否包含索引
     *
     * @param name 索引
     * @return 是否包含索引
     */
    default boolean container(String name) {
        return null == getObject(name);
    }

    /**
     * 获取值
     *
     * @param name 索引
     * @return 值
     */
    default String getString(String name) {
        Object object = getObject(name);
        return null == object ? null : object.toString();
    }

    /**
     * 获取值
     *
     * @param name         索引
     * @param defaultValue 默认值
     * @return 值
     */
    default String getStringOrDefault(String name, final String defaultValue) {
        String object = getString(name);
        return null == object ? defaultValue : object;
    }

    /**
     * 获取值
     *
     * @param name 索引
     * @return 值
     */
    default Integer getInteger(String name) {
        Object object = getObject(name);
        try {
            return null == object ? null : Integer.valueOf(object.toString());
        } catch (NumberFormatException e) {
        }
        return null;
    }

    /**
     * 获取值
     *
     * @param name         索引
     * @param defaultValue 默认值
     * @return 值
     */
    default Integer getIntegerOrDefault(String name, final Integer defaultValue) {
        Integer integer = getInteger(name);
        return null == integer ? defaultValue : integer;
    }

    /**
     * 获取值
     *
     * @param name 索引
     * @return 值
     */
    default int getIntValue(String name) {
        Object object = getObject(name);
        try {
            return null == object ? 0 : Integer.valueOf(object.toString());
        } catch (NumberFormatException e) {
        }
        return 0;
    }

    /**
     * 获取值
     *
     * @param name         索引
     * @param defaultValue 默认值
     * @return 值
     */
    default int getIntValueOrDefault(String name, final Integer defaultValue) {
        Object object = getObject(name);
        try {
            return null == object ? defaultValue : Integer.valueOf(object.toString());
        } catch (NumberFormatException e) {
        }
        return defaultValue;
    }

    /**
     * 获取值
     *
     * @param name 索引
     * @return 值
     */
    default Long getLong(String name) {
        Object object = getObject(name);
        try {
            return null == object ? null : Long.valueOf(object.toString());
        } catch (NumberFormatException e) {
        }
        return null;
    }

    /**
     * 获取值
     *
     * @param name         索引
     * @param defaultValue 默认值
     * @return 值
     */
    default Long getIntegerOrDefault(String name, final Long defaultValue) {
        Long aLong = getLong(name);
        return null == aLong ? defaultValue : aLong;
    }

    /**
     * 获取值
     *
     * @param name 索引
     * @return 值
     */
    default long getLongValue(String name) {
        Object object = getObject(name);
        try {
            return null == object ? 0L : Integer.valueOf(object.toString());
        } catch (NumberFormatException e) {
        }
        return 0L;
    }

    /**
     * 获取值
     *
     * @param name         索引
     * @param defaultValue 默认值
     * @return 值
     */
    default long getLongValueOrDefault(String name, final long defaultValue) {
        Object object = getObject(name);
        try {
            return null == object ? defaultValue : Integer.valueOf(object.toString());
        } catch (NumberFormatException e) {
        }
        return defaultValue;
    }
}
