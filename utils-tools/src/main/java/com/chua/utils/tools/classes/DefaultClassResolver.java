package com.chua.utils.tools.classes;

import com.chua.utils.tools.cache.ConcurrentCacheProvider;
import com.chua.utils.tools.cache.ICacheProvider;
import com.chua.utils.tools.function.intercept.MethodIntercept;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 默认类解释器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/24
 */
@Slf4j
public class DefaultClassResolver<T> implements ClassResolver {

    private T object;
    protected ICacheProvider<String, Annotation> annotationCache = new ConcurrentCacheProvider<>();
    protected static final CopyOnWriteArraySet<Class> subTypes = new CopyOnWriteArraySet<>();
    private Class<?> sourceClass;

    public DefaultClassResolver(T object) {
        this.object = object;
        this.initial();
    }

    private void initial() {
        if (null == object) {
            return;
        }
        this.sourceClass = ClassHelper.getClass(object);
        Annotation[] annotations = sourceClass.getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            annotationCache.put(annotation.annotationType().getName(), annotation);
        }
    }

    @Override
    public Map<String, Annotation> annotations() {
        return annotationCache.asMap();
    }

    @Override
    public boolean isAssignableFrom(Class<?> type) {
        if (!validObject()) {
            log.warn("Uninitialized object");
            return false;
        }
        return type.isAssignableFrom(object.getClass());
    }

    @Override
    public Set<? extends Class> findSubType() {
        if (validObject()) {
            log.warn("Uninitialized object");
            return Collections.emptySet();
        }
        if (!subTypes.isEmpty()) {
            return subTypes;
        }
        Set<? extends Class> subTypesOf = ClassHelper.getSubTypesOf(object.getClass());
        subTypes.addAll(subTypesOf);
        subTypes.add(object.getClass());
        return subTypesOf;
    }

    @Override
    public Object proxy(MethodIntercept methodIntercept) {
        if (validObject()) {
            log.warn("Uninitialized object");
            return null;
        }
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(object.getClass());
        enhancer.setInterfaces(object.getClass().getInterfaces());
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                return methodIntercept.invoke(obj, method, args, proxy);
            }
        });
        return enhancer.create();
    }

    @Override
    public <T> T newInstance() throws Exception {
        if(null != object && !(object instanceof Class)) {
            return (T) object;
        }
        if(Modifier.isPublic(this.sourceClass.getModifiers())) {
            return newInstanceByConstruction();
        }
        Class<?> aClass = ClassHelper.makeModifiers(sourceClass);
        return (T) aClass.newInstance();
    }

    /**
     * 通过构造实例化
     * @param <T>
     * @return
     */
    private <T> T newInstanceByConstruction() throws Exception {
        if(ClassHelper.hasNoParamterConstructor(sourceClass)) {
            return (T) sourceClass.newInstance();
        }
        ModifiableClassResolver modifiableClassResolver = new DefaultModifiableClassResolver(sourceClass);
        modifiableClassResolver.addConstruct(new Class[0]);
        return (T) modifiableClassResolver.toObject();
    }


    private boolean validObject() {
        return null != object;
    }
}
