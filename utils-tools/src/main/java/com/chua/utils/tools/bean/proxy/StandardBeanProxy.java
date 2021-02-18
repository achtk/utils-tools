package com.chua.utils.tools.bean.proxy;

import com.chua.utils.tools.function.intercept.MethodIntercept;
import com.chua.utils.tools.storage.ProxyStorage;
import com.chua.utils.tools.util.ClassUtils;
import com.google.common.base.Strings;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * bean代理
 *
 * @author CH
 * @version 1.0.0
 * @since 2021/2/8
 */
public class StandardBeanProxy<T> implements BeanProxy<T> {


    private final T object;
    private final Map<String, MethodIntercept<T>> intercepts = new HashMap<>();

    public StandardBeanProxy(T object) {
        this.object = object;
    }

    @Override
    public BeanProxy<T> addAdaptor(String methodName, MethodIntercept<T> intercept) {
        if (!Strings.isNullOrEmpty(methodName)) {
            intercepts.put(methodName, intercept);
        }
        return this;
    }

    @Override
    @SuppressWarnings("ALL")
    public T create() {
        if (null == object) {
            return null;
        }
        T entity = object;
        if (object instanceof Class) {
            entity = (T) ClassUtils.forObject((Class<T>) object);
        } else if (object instanceof String) {
            entity = (T) ClassUtils.forObject(object.toString().trim());
        }

        return ProxyStorage.run(entity, new MethodIntercept<T>() {
            @Override
            public Object invoke(Object obj, Method method, Object[] args, T proxy) throws Throwable {
                String name = method.getName();
                if (intercepts.containsKey(name)) {
                    MethodIntercept<T> intercept = intercepts.get(name);
                    return intercept.invoke(obj, method, args, proxy);
                }
                return ClassUtils.getMethodValue(obj, method, args);
            }
        });
    }
}
