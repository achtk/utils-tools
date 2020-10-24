package com.chua.utils.tools.classes.field;

import com.chua.utils.tools.cache.ConcurrentCacheProvider;
import com.chua.utils.tools.cache.ConcurrentSetCacheProvider;
import com.chua.utils.tools.cache.ICacheProvider;
import com.chua.utils.tools.cache.MultiCacheProvider;
import com.chua.utils.tools.classes.ClassHelper;
import com.google.common.base.Strings;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 字段解释器
 * @author CH
 * @version 1.0.0
 * @since 2020/10/24
 */
public class DefaultFieldResolver implements FieldResolver {

    private Object object;
    private Class aClass;
    private static final String DELIMITER = "#";
    private final ICacheProvider<String, Field> cacheNameTypeProvider = new ConcurrentCacheProvider<>();
    private final MultiCacheProvider<String, Field> cacheProvider = new ConcurrentSetCacheProvider();
    private final CopyOnWriteArraySet<Field> copyOnWriteArraySet = new CopyOnWriteArraySet();

    public DefaultFieldResolver(Object object) {
        this.object = object;
        this.initial();
    }

    private void initial() {
        if(null == object) {
            return;
        }
        this.aClass = ClassHelper.getClass(object);
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            cacheNameTypeProvider.put(declaredField.getName() + DELIMITER + declaredField.getType().getName(), declaredField);
            cacheProvider.add(declaredField.getName(), declaredField);
            copyOnWriteArraySet.add(declaredField);
        }
    }

    @Override
    public Set<Field> fields() {
        return copyOnWriteArraySet;
    }

    @Override
    public Field findField(String fieldName, Class<?> fieldType) {
        if(Strings.isNullOrEmpty(fieldName) || null == fieldType) {
            return null;
        }
        return cacheNameTypeProvider.get(fieldName + DELIMITER +fieldType.getName());
    }

    @Override
    public Set<Field> findField(String fieldName) {
        if(Strings.isNullOrEmpty(fieldName)) {
            return null;
        }
        Set<Field> fields = cacheProvider.get(fieldName);
        return fields;
    }

    @Override
    public <T> T getValue(String fieldName, Class<T> tClass) throws IllegalAccessException {
        Field field = findField(fieldName, tClass);
        if(null == field) {
            throw new NullPointerException();
        }
        ClassHelper.makeAccessible(field);
        return (T) field.get(object);
    }
}
