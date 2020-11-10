package com.chua.utils.tools.manager;

import com.chua.utils.tools.classes.reflections.ReflectionsFactory;
import com.chua.utils.tools.function.Filter;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.spi.processor.ExtensionProcessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/**
 * 对象管理器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public interface ObjectContextManager {
    /**
     * 通过类型查找子类
     *
     * @param tClass 类型
     * @param <T>    T
     * @return Set<T>
     */
    <T> Set<Class<? extends T>> getSubTypesOf(Class<T> tClass);

    /**
     * 获取资源
     *
     * @param filter 过滤器
     * @return
     */
    Set<String> getResources(Filter<String> filter);

    /**
     * 获取包含注解的类
     *
     * @param annotation 注解
     * @return
     */
    Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation);

    /**
     * 获取包含注解的方法
     *
     * @param annotation 注解
     * @return
     */
    Set<Method> getMethodsAnnotatedWith(Class<? extends Annotation> annotation);

    /**
     * 获取包含注解的字段
     *
     * @param annotation 注解
     * @return
     */
    Set<Field> getFieldsAnnotatedWith(Class<? extends Annotation> annotation);

    /**
     * 获取ReflectionsFactory
     *
     * @return ReflectionsFactory
     */
    ReflectionsFactory getReflectionsFactory();

    /**
     * 检索类
     *
     * @param matcher matcher
     */
    void doWithMatcher(Matcher<String> matcher);

    /**
     * 等待初始化完成
     *
     * @throws ExecutionException   ExecutionException
     * @throws InterruptedException InterruptedException
     */
    void loadingFinished() throws ExecutionException, InterruptedException;

    /**
     * 通过spi方式类型查找子类
     *
     * @param tClass             类型
     * @param extensionProcessor 扩展器
     * @param spiName            spiName
     * @param <T>                T
     * @return T
     */
    <T> T getSpiSubOfType(String spiName, Class<T> tClass, ExtensionProcessor extensionProcessor);

    /**
     * 通过spi方式类型查找子类
     *
     * @param tClass  类型
     * @param spiName spiName
     * @param <T>     T
     * @return T
     */
    default <T> T getSpiSubOfType(String spiName, Class<T> tClass) {
        return getSpiSubOfType(spiName, tClass, null);
    }
}
