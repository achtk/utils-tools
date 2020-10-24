package com.chua.utils.tools.classes;

import com.chua.utils.tools.cache.ConcurrentCacheProvider;
import com.chua.utils.tools.cache.ICacheProvider;
import com.chua.utils.tools.classes.callback.FieldCallback;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.function.Converter;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.function.intercept.MethodIntercept;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
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
    public <T> T automaticAssembly(final T entity, String prefix, final Converter<String, Object> converter) {
        Class<?> aClass = entity.getClass();
        if(null != prefix && !prefix.endsWith(".")) {
            prefix += ".";
        }
        final String newPrefix = prefix;
        ClassHelper.doWithLocalFields(aClass, new FieldCallback() {
            @Override
            public void doWith(Field item) throws Throwable {
                renderingField(item, entity, newPrefix, converter);
            }
        });
        return entity;
    }

    @Override
    public <T> T automaticAssembly(String prefix, Converter<String, Object> converter) {
        return (T) automaticAssembly(object, prefix, converter);
    }

    /**
     * 渲染字段
     *
     * @param field     字段
     * @param object    对象
     * @param prefix    前缀
     * @param converter 转化器
     */
    private void renderingField(Field field, Object object, String prefix, Converter<String, Object> converter) throws Throwable {
        Class<?> type = field.getType();
        String name1 = field.getName();
        if (Modifier.isStatic(type.getModifiers())) {
            return;
        }
        String s = type.toGenericString();
        if (s.startsWith("[") || name1.indexOf("$") != -1) {
            return;
        }
        String name = StringHelper.humpToLine2(name1, "-");
        String newName = prefix + name;
        if (ClassHelper.isBasicClass(type)) {
            basicRenderingClass(field, newName, object, converter);
            return;
        }
        if (type.isEnum()) {
            //String property = environment.getProperty(newName);
            return;
        }
        Object fieldValue = ClassHelper.getFieldValue(field, object);
        Object chilrenValue = subclassRendering(fieldValue, newName + ".", converter);
        if (null == chilrenValue) {
            return;
        }
        ClassHelper.setFieldValue(field, chilrenValue, object);
    }

    /**
     * 子类渲染
     *
     * @param object    对象
     * @param prefix    前缀
     * @param converter 转化器
     * @return
     */
    private Object subclassRendering(Object object, String prefix, Converter<String, Object> converter) {
        if (null == object) {
            return null;
        }
        ClassHelper.doWithFields(object.getClass(), new FieldCallback() {

            @Override
            public void doWith(Field item) throws Throwable {
                renderingField(item, object, prefix, converter);
            }
        });
        return object;
    }

    /**
     * 渲染基础类
     *
     * @param field     字段
     * @param key       索引
     * @param object    对象
     * @param converter 转化器
     */
    private void basicRenderingClass(Field field, String key, Object object, Converter<String, Object> converter) {
        Object property = converter.convert(key);
        if (null == property) {
            return;
        }
        field.setAccessible(true);
        try {
            field.set(object, property);
        } catch (Throwable e) {
        }
        return;
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
        if (null != object && !(object instanceof Class)) {
            return (T) object;
        }
        if (Modifier.isPublic(this.sourceClass.getModifiers())) {
            return newInstanceByConstruction();
        }
        Class<?> aClass = ClassHelper.makeModifiers(sourceClass);
        return (T) aClass.newInstance();
    }

    /**
     * 通过构造实例化
     *
     * @param <T>
     * @return
     */
    private <T> T newInstanceByConstruction() throws Exception {
        if (ClassHelper.hasNoParamterConstructor(sourceClass)) {
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
