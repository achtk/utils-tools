package com.chua.utils.tools.bean.copy;

import java.util.Map;

/**
 * bean拷贝
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/10
 */
public interface BeanCopy<T> {
    /**
     * 赋值
     *
     * @param name  字段名称
     * @param value 字段值
     * @return this
     */
    BeanCopy with(String name, Object value);

    /**
     * 赋值
     *
     * @param param 参数
     * @return this
     */
    BeanCopy with(Map<String, Object> param);

    /**
     * 赋值
     *
     * @param entity 实体对象
     * @return this
     */
    BeanCopy with(Object entity);

    /**
     * 构建实体
     *
     * @return 实体
     */
    T create();
}
