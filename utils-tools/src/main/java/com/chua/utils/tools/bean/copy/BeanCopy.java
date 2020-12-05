package com.chua.utils.tools.bean.copy;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.collects.HashOperateMap;
import com.chua.utils.tools.collects.map.MapOperableHelper;
import com.chua.utils.tools.function.Converter;
import net.sf.cglib.beans.BeanCopier;

import java.util.Map;
import java.util.Properties;
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
     * 对象转化
     *
     * @param source 原始对象
     * @param target 目标对象
     * @param <T>    目标对象类型
     * @return 目标对象
     */
    static <T> T converter(Object source, T target) {
        if (null == source || null == target) {
            return target;
        }
        BeanCopier beanCopier = BeanCopier.create(source.getClass(), target.getClass(), false);
        beanCopier.copy(source, target, null);
        return target;
    }

    /**
     * 初始化
     *
     * @param t   对象
     * @param <T> 对象类型
     * @return this
     */
    static <T> BeanCopy<T> of(Class<T> t) {
        return StandardBeanCopy.of(t);
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
     * @param keys  索引
     * @return this
     */
    BeanCopy<T> with(Map<String, Object> param, String... keys);

    /**
     * 赋值
     *
     * @param properties 参数
     * @param keys       索引
     * @return this
     */
    default BeanCopy<T> with(Properties properties, String... keys) {
        return with(MapOperableHelper.toMap(properties), keys);
    }

    /**
     * 以类的字段作为索引赋值
     *
     * @param properties 参数
     * @param aClass     类
     * @return this
     */
    default BeanCopy<T> with(Properties properties, Class<?> aClass) {
        if (null != aClass && null != properties) {
            ClassHelper.doWithLocalFields(aClass, field -> {
                String name = field.getName();
                if (properties.containsKey(name)) {
                    with(properties, name);
                }
            });
        }
        return this;
    }

    /**
     * 赋值
     *
     * @param properties 参数
     * @return this
     */
    default BeanCopy<T> with(Properties properties) {
        return with(MapOperableHelper.toMap(properties));
    }

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
