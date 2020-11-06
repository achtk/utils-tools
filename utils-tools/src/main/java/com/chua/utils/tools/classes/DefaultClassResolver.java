package com.chua.utils.tools.classes;

import com.chua.utils.tools.cache.CacheProvider;
import com.chua.utils.tools.cache.ConcurrentCacheProvider;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.function.Converter;
import com.chua.utils.tools.function.intercept.MethodIntercept;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
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

    private final T object;
    protected CacheProvider<String, Annotation> annotationCache = new ConcurrentCacheProvider<>();
    protected static final CopyOnWriteArraySet<Class> SUB_TYPES = new CopyOnWriteArraySet<>();
    private Class<?> sourceClass;

    public DefaultClassResolver(T object) {
        this.object = object;
        initial();
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
    public <T> T automaticAssembly(@NonNull final T entity, String prefix, final Converter<String, Object> converter) {
        Class<?> aClass = entity.getClass();
        if (null != prefix && !prefix.endsWith(".")) {
            prefix += ".";
        }
        final String newPrefix = prefix;
        ClassHelper.doWithLocalFields(aClass, item -> renderingField(item, entity, newPrefix, converter));
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
        if (!type.isEnum() && Modifier.isStatic(type.getModifiers())) {
            return;
        }
        String s = type.toGenericString();
        if (s.startsWith("[") || name1.contains("$")) {
            return;
        }
        String name = StringHelper.humpToLine2(name1, "-");
        String newName = prefix + name;
        if (ClassHelper.isBasicClass(type)) {
            basicRenderingClass(field, newName, object, converter);
            return;
        }
        if (type.isEnum()) {
            Object convert = converter.convert(newName, field.getType());
            if(null == convert) {
                return;
            }
            Object fieldValue = ClassHelper.getFieldValue(field, object);

            Object obj = ClassHelper.getEnum(convert.toString(), field.getType());
            if(fieldValue == obj) {
                return;
            }
            ClassHelper.setFieldValue(field, obj, object);
            return;
        }
        Object fieldValue = ClassHelper.getFieldValue(field, object);
        Object childrenValue = subclassRendering(fieldValue, newName + ".", converter);
        if (null == childrenValue) {
            return;
        }
        ClassHelper.setFieldValue(field, childrenValue, object);
    }

    /**
     * 子类渲染
     *
     * @param object    对象
     * @param prefix    前缀
     * @param converter 转化器
     * @return 对象
     */
    private Object subclassRendering(Object object, String prefix, Converter<String, Object> converter) {
        if (null == object) {
            return null;
        }
        ClassHelper.doWithFields(object.getClass(), item -> renderingField(item, object, prefix, converter));
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
        Object property = converter.convert(key, field.getType());
        if (null == property) {
            return;
        }
        field.setAccessible(true);
        try {
            field.set(object, property);
        } catch (Throwable ignored) {
        }
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
        if (!SUB_TYPES.isEmpty()) {
            return SUB_TYPES;
        }
        Set<? extends Class> subTypesOf = ClassHelper.getSubTypesOf(object.getClass());
        SUB_TYPES.addAll(subTypesOf);
        SUB_TYPES.add(object.getClass());
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
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> methodIntercept.invoke(obj, method, args, proxy));
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
     * @param <T> T
     * @return 实例化
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
