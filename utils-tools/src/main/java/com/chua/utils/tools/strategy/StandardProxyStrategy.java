package com.chua.utils.tools.strategy;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.function.intercept.MethodIntercept;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 标准的代理策略
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/5
 */
@Getter
@Setter
public class StandardProxyStrategy<T> implements ProxyStrategy<T, MethodProxy> {

    private Class<?>[] interfaces;
    private ClassLoader classloader;
    private MethodIntercept<MethodProxy> methodIntercept;
    private static final List<String> SKIP_METHOD_NAME = Lists.newArrayList("toString", "equals");
    private T source;

    @Override
    public void interfaces(Class<?>... interfaces) {
        this.interfaces = interfaces;
    }

    @Override
    public void classloader(ClassLoader classloader) {
        this.classloader = classloader == null ? ClassHelper.getDefaultClassLoader() : classloader;
    }

    @Override
    public void intercept(MethodIntercept<MethodProxy> methodIntercept) {
        this.methodIntercept = methodIntercept;
    }

    @Override
    public T proxy(T source) {
        if(!(source instanceof Class)) {
            this.source = source;
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(source.getClass());
        enhancer.setUseCache(true);
        if (null != interfaces) {
            enhancer.setInterfaces(interfaces);
        }
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                String name = method.getName();
                if (isSkip(name) || (null == methodIntercept && !(source instanceof Class))) {
                    return proxy.invokeSuper(obj, args);
                }

                if (null == methodIntercept) {
                    throw new NullPointerException();
                }
                return methodIntercept.invoke(obj, method, args, proxy);
            }
        });
        return (T) enhancer.create();
    }

    /**
     * 跳过方法
     *
     * @param name 方法名
     * @return boolean
     */
    private boolean isSkip(String name) {
        return SKIP_METHOD_NAME.contains(name);
    }
}
