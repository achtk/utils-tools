package com.chua.utils.tools.function.able;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.callback.FieldCallback;
import com.chua.utils.tools.collects.HashTripleMap;
import com.chua.utils.tools.collects.TripleMap;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.manager.ObjectContextManager;
import com.chua.utils.tools.manager.producer.StandardContextManager;
import com.chua.utils.tools.manager.producer.StandardScannerObjectContextManager;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 初始化可被缓存接口
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/6
 */
public class InitializingCacheable implements Cacheable {

    private static final TripleMap<String, String, ClassDescription> TRIPLE_MAP = new HashTripleMap<>();

    {
        doAnalyseClassCache(this);
    }

    /**
     * 类分析
     *
     * @param object 子类对象
     */
    private static void doAnalyseClassCache(Object object) {
        if (null == object) {
            return;
        }

        ClassHelper.doWithFields(object.getClass(), new FieldCallback() {
            @Override
            public void doWith(Field item) throws Throwable {
                if (Modifier.isStatic(item.getModifiers()) && !item.getType().equals(TripleMap.class)) {
                    ClassDescription classDescription = new ClassDescription();
                    classDescription.setEntity(object);
                    classDescription.setField(item);
                    TRIPLE_MAP.put(item.getDeclaringClass().getName(), item.getName(), classDescription);
                }
            }
        });
    }

    /**
     * 加载所有{@link InitializingCacheable}子类
     */
    public static void reload() {
        ObjectContextManager objectContextManager = new StandardScannerObjectContextManager();
        Set<Class<? extends InitializingCacheable>> classes = objectContextManager.getSubTypesOf(InitializingCacheable.class);
        for (Class<? extends InitializingCacheable> aClass : classes) {
            doAnalyseClassCache(ClassHelper.forObject(aClass));
        }
    }

    /**
     * 获取缓存
     *
     * @param aClass 类
     * @param name   缓存名称
     * @param tClass 缓存对象类型
     * @param <T>    缓存对象类型
     * @return 缓存对象
     */
    public static <T> T getValue(Class<?> aClass, String name, Class<T> tClass) {
        return new InitializingCacheable().get(aClass, name, tClass);
    }

    @Override
    public <T> T get(Class<?> aClass, String name, Class<T> tClass) {
        List<ClassDescription> right = TRIPLE_MAP.getRight(aClass.getName(), name);
        ClassDescription classDescription = FinderHelper.firstElement(right);
        if (null == classDescription) {
            return null;
        }
        Field field = classDescription.getField();
        ClassHelper.makeAccessible(field);
        try {
            return (T) field.get(classDescription.getEntity());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 类描述
     */
    @Data
    static
    class ClassDescription {
        private Object entity;
        private Field field;
    }
}
