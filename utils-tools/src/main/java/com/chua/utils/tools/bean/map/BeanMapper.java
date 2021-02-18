package com.chua.utils.tools.bean.map;

import com.chua.utils.tools.bean.copy.BeanCopy;

import java.util.Map;

/**
 * 对象集合
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/2
 */
public interface BeanMapper extends Map<String, Object> {
    /**
     * 获取BeanMap
     *
     * @param obj 对象
     * @return 对象集合
     */
    static BeanMapper of(Object obj) {
        return new StandardBeanMapper(obj, null);
    }

    /**
     * 获取BeanMap
     *
     * @param obj    对象
     * @param mapper 映射
     * @return 对象集合
     */
    static BeanMapper of(Object obj, String... mapper) {
        return new StandardBeanMapper(obj, mapper);
    }

    /**
     * 获取配置
     *
     * @return Map
     */
    Map<String, Object> toMap();

    /**
     * 获取配置
     *
     * @param <T>    类型
     * @param tClass 类型
     * @return 实体
     */
    default <T> T toObject(Class<T> tClass) {
        return BeanCopy.of(tClass).with(toMap()).create();
    }
}
