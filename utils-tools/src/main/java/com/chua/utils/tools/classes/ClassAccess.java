package com.chua.utils.tools.classes;

import com.chua.utils.tools.classes.classloader.AccessClassLoader;
import com.chua.utils.tools.classes.factory.ClassAccessFactory;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * 类解析<br />
 * 参见 Faster
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/12/30
 */
public interface ClassAccess<T> extends FieldAccess<T>, PropertyAccess<T>, MethodAccess<T> {

    static final Map<Class, ClassAccess> CLASS_ACCESS_MAP = new WeakHashMap<>();

    /**
     * 实例化
     *
     * @param tClass 类型
     * @return ClassAccess
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    static <T> ClassAccess<T> getInstance(Class<T> tClass) throws IllegalAccessException, InstantiationException {
        ClassAccess<T> classAccess = CLASS_ACCESS_MAP.get(tClass);
        if (classAccess == null) {
            synchronized (CLASS_ACCESS_MAP) {
                classAccess = CLASS_ACCESS_MAP.get(tClass);

                if (classAccess == null) {
                    Class<?> classAccessClass = null;
                    try {
                        classAccessClass = createClassAccessClass(tClass);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    classAccess = (ClassAccess<T>) classAccessClass.newInstance();
                    CLASS_ACCESS_MAP.put(tClass, classAccess);
                }
            }
        }
        return classAccess;
    }

    /**
     * 创建Access类
     *
     * @param tClass 类
     * @return ClassAccess类
     * @throws ClassNotFoundException ClassNotFoundException
     */
    static <T> Class<? extends ClassAccess> createClassAccessClass(Class<T> tClass) throws ClassNotFoundException {
        String className = ClassAccessFactory.getClassNameOfClassAccessFor(tClass);
        try {
            return (Class<? extends ClassAccess>) AccessClassLoader.get(tClass).loadClass(className);
        } catch (Throwable ignored) {
        }

        new ClassAccessFactory(tClass).buildClassAccessClass();
        return (Class<? extends ClassAccess>) AccessClassLoader.get(tClass).loadClass(className);
    }


    /**
     * 初始化
     *
     * @param tClass 类
     * @return ClassAccess
     */
    static <T> ClassAccess<T> build(Class<T> tClass) {
        try {
            return getInstance(tClass);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
