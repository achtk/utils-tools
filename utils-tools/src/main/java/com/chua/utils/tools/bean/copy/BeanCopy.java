package com.chua.utils.tools.bean.copy;

import com.chua.utils.tools.collects.HashOperateMap;
import com.chua.utils.tools.function.Converter;

import java.util.Map;
import java.util.function.Supplier;

/**
 * <p>bean拷贝</p>
 * Example:
 * <pre>
 * 参见代码: BeanCopy<T> beanCopy = StandardBeanCopy.of(T.class);
 * </pre>
 *
 * @author CH
 * @version 1.0.0
 * @see StandardBeanCopy
 * @since 2020/11/10
 */
public interface BeanCopy<T> {
    /**
     * 初始化
     *
     * @param t   对象
     * @param <T> 对象类型
     * @return this
     */
    @SuppressWarnings("all")
    static <T> BeanCopy<T> of(T t) {
        return StandardBeanCopy.<T>of(t);
    }

    /**
     * 赋值
     *
     * @param name  字段名称
     * @param value 字段值
     * @return this
     */
    BeanCopy<T> with(String name, Object value);

    /**
     * 赋值
     *
     * @param param 参数
     * @return this
     */
    BeanCopy<T> with(Map<String, Object> param);

    /**
     * 赋值
     *
     * @param entity 实体对象
     * @return this
     */
    BeanCopy<T> with(Object entity);

    /**
     * 赋值
     *
     * @param converter 回调
     * @return this
     */
    BeanCopy<T> with(Converter<String, Object> converter);

    /**
     * 构建实体
     *
     * @return 实体
     */
    T create();

    /**
     * 获取所有字段
     *
     * @return 字段
     */
    HashOperateMap asMap();
}
