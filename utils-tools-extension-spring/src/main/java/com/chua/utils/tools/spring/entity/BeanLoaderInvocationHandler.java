package com.chua.utils.tools.spring.entity;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.google.common.collect.HashMultimap;
import lombok.AllArgsConstructor;
import org.springframework.core.annotation.Order;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * BeanLoader代理
 * @param <T>
 */
@AllArgsConstructor
class BeanLoaderInvocationHandler<T> implements InvocationHandler {

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