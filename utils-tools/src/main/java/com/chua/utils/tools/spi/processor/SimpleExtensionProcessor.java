package com.chua.utils.tools.spi.processor;

import com.chua.utils.tools.aware.NamedAware;
import com.chua.utils.tools.aware.OrderAware;
import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.spi.Spi;
import com.chua.utils.tools.spi.entity.ExtensionClass;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 简单的扩展解释器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
@Getter
@Setter
public abstract class SimpleExtensionProcessor<T> implements ExtensionProcessor<T> {

    private ClassLoader classLoader;
    private Class<T> interfaceClass;
    private String interfaceName;

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = null == classLoader ? ClassHelper.getDefaultClassLoader() : classLoader;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
        this.interfaceName = null == interfaceClass ? null : interfaceClass.getName();
    }

    /**
     * 通过实现类构建ExtensionClass
     *
     * @param t 实现类
     * @return Multimap
     */
    protected List<ExtensionClass<T>> buildExtensionClassByObject(T t) {
        Class<?> tClass = t.getClass();
        boolean isSingle = isSingle(tClass);
        int order = getOrder(tClass, t);
        //创建实现名扩展对象
        ExtensionClass<T> extensionClassByClassName = buildExtensionClassByClassName(tClass, isSingle, order, t);
        //创建Spi扩展对象
        List<ExtensionClass<T>> extensionClassBySpi = buildExtensionClassBySpi(tClass, isSingle, order, t);

        List<ExtensionClass<T>> result = new ArrayList<>();

        if (null != extensionClassBySpi) {
            result.addAll(extensionClassBySpi);
        }
        result.add(extensionClassByClassName);
        return result;
    }

    /**
     * 通过类构建ExtensionClass
     *
     * @param tClass 类
     * @return Multimap
     */
    protected List<ExtensionClass<T>> buildExtensionClassByClass(Class<? extends T> tClass) {
        T t = null;
        try {
            t = ClassHelper.forObject(tClass);
        } catch (Exception e) {
            return Collections.emptyList();
        }
        return buildExtensionClassByObject(t);
    }

    /**
     * 获取排序优先级
     *
     * @param tClass 实现类
     * @return
     */
    private int getOrder(Class<?> tClass, T t) {
        //判断是否存在@Spi注解
        Spi spi = tClass.getDeclaredAnnotation(Spi.class);
        if (null != spi) {
            return spi.order();
        }
        //实现OrderAware
        if (OrderAware.class.isAssignableFrom(tClass)) {
            OrderAware orderAware = (OrderAware) t;
            return orderAware.order();
        }
        return 0;
    }

    /**
     * 是否是单例
     *
     * @param tClass 类
     * @return boolean
     * @see com.chua.utils.tools.spi.Spi
     */
    private boolean isSingle(Class<?> tClass) {
        //判断是否存在@Spi注解
        Spi spi = tClass.getDeclaredAnnotation(Spi.class);
        if (null != spi) {
            return spi.single();
        }
        return true;
    }

    /**
     * 通过类名构建ExtensionClass
     *
     * @param tClass   类
     * @param isSingle 是否单例
     * @param order    优先级
     * @param t        实现类
     */
    private ExtensionClass<T> buildExtensionClassByClassName(Class<?> tClass, boolean isSingle, int order, T t) {
        return buildExtensionClass(tClass.getName(), tClass, isSingle, order, t);
    }

    /**
     * 通过类名构建ExtensionClass
     *
     * @param tClass   类
     * @param isSingle 是否单例
     * @param order    优先级
     * @param t        实现类
     * @return List
     */
    private List<ExtensionClass<T>> buildExtensionClassBySpi(Class<?> tClass, boolean isSingle, int order, T t) {
        String[] names = getName(tClass, t);
        if (null == names) {
            return null;
        }
        List<ExtensionClass<T>> result = new ArrayList<>(names.length);
        for (String name : names) {
            result.add(buildExtensionClass(name, tClass, isSingle, order, t));
        }
        return result;
    }

    /**
     * 构建
     *
     * @param name     spi名称
     * @param tClass   类
     * @param isSingle 是否单例
     * @param order    优先级
     * @param t        实现类
     * @return
     */
    private ExtensionClass<T> buildExtensionClass(String name, Class<?> tClass, boolean isSingle, int order, T t) {
        ExtensionClass<T> extensionClass = new ExtensionClass<>();
        extensionClass.setName(name);
        extensionClass.setOrder(order);
        extensionClass.setImplClass(tClass);
        extensionClass.setObj(t);
        extensionClass.setInterfaceName(interfaceClass.getName());
        extensionClass.setClassLoader(classLoader);
        extensionClass.setSingle(isSingle);
        return extensionClass;
    }

    /**
     * 获取Spi名称
     *
     * @param tClass 类
     * @param t      实现类
     * @return
     */
    private String[] getName(Class<?> tClass, T t) {
        //判断是否存在@Spi注解
        Spi spi = tClass.getDeclaredAnnotation(Spi.class);
        if (null != spi) {
            return spi.value();
        }
        //实现OrderAware
        if (NamedAware.class.isAssignableFrom(tClass)) {
            NamedAware namedAware = (NamedAware) t;
            return new String[]{namedAware.named()};
        }
        return null;
    }
}
