package com.chua.utils.tools.spring.entity;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.CollectionHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.exceptions.NotSupportedException;
import com.chua.utils.tools.spring.enums.BeanStatus;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.core.annotation.Order;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author CH
 * @date 2020-09-26
 */
@Data
@NoArgsConstructor(staticName = "newLoader")
@EqualsAndHashCode
public class BeanLoader<T> {
    /**
     * 加载的对象
     */
    private HashMultimap<Class, T> beans = HashMultimap.create();

    /**
     * 加载的对象
     */
    private HashMultimap<String, T> beansComparison = HashMultimap.create();

    /**
     * 异常信息
     */
    private Map<String, BeanStatus> throwable = new HashMap<>();
    /**
     * 获取代理信息
     */
    private List<Object> cacheProxyResult = new ArrayList<>();

    /**
     * 添加异常信息
     *
     * @param name       bean名称
     * @param beanStatus 导致原因
     * @return
     */
    public BeanLoader<T> throwable(String name, BeanStatus beanStatus) {
        this.throwable.put(name, beanStatus);
        return this;
    }

    /**
     * 添加异常信息
     *
     * @param name bean名称
     * @return
     */
    public BeanLoader<T> throwable(String name) {
        this.throwable.put(name, BeanStatus.BEAN_NOT_EXIST);
        return this;
    }

    /**
     * 添加bean
     *
     * @param entity 数据
     * @return
     */
    public synchronized BeanLoader<T> add(Class name, T entity) {
        if (null == entity) {
            return this;
        }
        beans.put(name, entity);
        beansComparison.put(name.getName(), entity);
        return this;
    }

    /**
     * 添加bean
     *
     * @param aClass
     * @param params 数据
     * @return
     */
    public synchronized BeanLoader<T> addAll(Class<? extends Annotation> aClass, Map<String, T> params) {
        if (null == params) {
            return this;
        }
        for (Map.Entry<String, T> entry : params.entrySet()) {
            beans.put(aClass, entry.getValue());
            beansComparison.put(aClass.getName(), entry.getValue());
        }
        return this;
    }

    /**
     * 获取所有bean
     *
     * @return
     */
    public Collection<T> toCollection() {
        return beans.values();
    }

    /**
     * 根据名称获取bean
     *
     * @param name 名称
     * @return
     */
    public Collection<T> get(final String name) {
        return Strings.isNullOrEmpty(name) ? Collections.emptyList() : beansComparison.get(name);
    }

    /**
     * 返回有效的第一个实体
     *
     * @return
     */
    public T findOne() {
        if (beans.isEmpty()) {
            return null;
        }
        return FinderHelper.firstElement(beans.values());
    }

    /**
     * 代理bean
     *
     * @param aClass 接口类
     * @return
     * @throws NotSupportedException
     */
    public T proxy(Class<?> aClass) {
        if (null == aClass) {
            return null;
        }

        if (!aClass.isInterface()) {
            return (T) ClassHelper.forObject(aClass);
        }
        return (T) Proxy.newProxyInstance(ClassHelper.getDefaultClassLoader(), new Class[]{aClass}, new BeanLoaderInvocationHandler<T>(beans, cacheProxyResult));
    }

    /**
     * 获取所有代理接口
     * @return
     */
    public List<Object> proxyResult() {
        return cacheProxyResult;
    }

    /**
     * BeanLoader代理
     * @param <T>
     */
    @AllArgsConstructor
    private static class BeanLoaderInvocationHandler<T> implements InvocationHandler {

        private HashMultimap<Class, T> beans;
        private List<Object> cacheProxyResult;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Set<T> tCollection = beans.get(method.getDeclaringClass());
            tCollection = sortCollection(tCollection);

            for (T t : tCollection) {
                Class<T> tClass = (Class<T>) t.getClass();
                Method tMethod = tClass.getMethod(method.getName(), method.getParameterTypes());
                try {
                    Object invoke = tMethod.invoke(t, args);
                    cacheProxyResult.add(invoke);
                } catch (Throwable e) {
                    continue;
                }
            }
            return FinderHelper.firstElement(cacheProxyResult);
        }

        /**
         * 数据排序
         *
         * @param tCollection
         * @return
         */
        private Set<T> sortCollection(Set<T> tCollection) {
            if (!BooleanHelper.hasLength(tCollection)) {
                return tCollection;
            }
            return tCollection.stream().sorted(new Comparator<T>() {
                @Override
                public int compare(T o1, T o2) {
                    Class<?> itemClass1 = o1.getClass();
                    Class<?> itemClass2 = o2.getClass();
                    int order1 = 0, order2 = 0;
                    if (itemClass1.isAnnotationPresent(Order.class)) {
                        order1 = itemClass1.getAnnotation(Order.class).value();
                    }

                    if (itemClass2.isAnnotationPresent(Order.class)) {
                        order2 = itemClass2.getAnnotation(Order.class).value();
                    }
                    return order1 > order2 ? 1 : -1;
                }
            }).collect(Collectors.toCollection(LinkedHashSet::new));
        }
    }
}
