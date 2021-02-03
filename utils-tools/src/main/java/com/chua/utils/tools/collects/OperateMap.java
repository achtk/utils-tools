package com.chua.utils.tools.collects;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.function.BiAppendable;
import com.chua.utils.tools.function.MapOperable;
import com.chua.utils.tools.util.ClassUtils;

import java.util.Map;

/**
 * 扩展性Map
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/31
 */
public interface OperateMap extends Map<String, Object>, MapOperable<String>, BiAppendable<String, Object> {
    /**
     * 保存实体
     *
     * @param key       索引
     * @param className 实体名称
     * @return 当实体存在返回实体, 实体不存在返回Null
     */
    default Object putBean(String key, String className) {
        Object forObject = ClassUtils.forObject(className);
        if (null == forObject) {
            return null;
        }
        return put(key, forObject);
    }

    /**
     * 保存实体(已Map实现为主)
     *
     * @param key       索引
     * @param className 实体名称
     * @return 当实体/实体类型存在返回实体, 实体不存在返回Null
     */
    default Object putObject(String key, String className) {
        int size = this.keySet().size();
        if (size == 0) {
            return this.putObject(key, className, Object.class);
        }
        return this.putObject(key, className, Object.class);
    }

    /**
     * 保存实体
     *
     * @param key        索引
     * @param className  实体名称
     * @param valueClass 实体类型
     * @return 当实体/实体类型存在返回实体, 实体不存在返回Null
     */
    default Object putObject(String key, String className, Class<?> valueClass) {
        if (null == valueClass) {
            return null;
        }
        Class<?> aClass = ClassUtils.forName(className);
        if (null == aClass) {
            return null;
        }

        if (!valueClass.isAssignableFrom(aClass)) {
            return null;
        }

        Object forObject = ClassUtils.forObject(aClass, valueClass);
        if (null == forObject) {
            return null;
        }
        this.put(key, forObject);

        return forObject;
    }

    /**
     * 表达式处理
     *
     * @param expression bsh表达式
     * @return 执行结果
     */
    Object expression(String expression);

    /**
     * 装配对象
     *
     * @param className 类名
     * @return 对象
     */
    default Object assemblyBean(String className) {
        return assemblyBean(ClassHelper.forName(className));
    }


    /**
     * 装配对象
     *
     * @param className 类称
     * @param tClass    类型
     * @param <T>       类型
     * @return 对象
     */
    default <T> T assemblyBean(String className, Class<T> tClass) {
        return assemblyBean(ClassHelper.forObject(className, tClass));
    }

    /**
     * 装配对象
     *
     * @param tClass 类型
     * @param <T>    类型
     * @return T
     */
    default <T> T assemblyBean(Class<T> tClass) {
        return assemblyBean(ClassUtils.forObject(tClass));
    }

    /**
     * 装配对象
     *
     * @param t1  类型
     * @param <T> 类型
     * @return T
     */
    <T> T assemblyBean(T t1);

    /**
     * 获取指定实体
     *
     * @param key    索引
     * @param tClass 类型
     * @param <T>    类型
     * @return 实体
     */
    <T> T getEntity(String key, Class<T> tClass);

    /**
     * 值转类
     *
     * @param key    索引
     * @param tClass 对象类型
     * @param <T>    类型
     * @return 类
     */
    <T> Class<? extends T> getClass(final String key, final Class<T> tClass);

    /**
     * 获取类
     *
     * @param key 索引
     * @return 类
     */
    default Class<?> getClass(final String key) {
        return getClass(key, Object.class);
    }

}
