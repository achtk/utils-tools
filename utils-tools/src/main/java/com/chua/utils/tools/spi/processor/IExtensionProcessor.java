package com.chua.utils.tools.spi.processor;

import com.chua.utils.tools.common.NumberHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.spi.Spi;
import com.chua.utils.tools.spi.entity.ExtensionClass;
import com.chua.utils.tools.spi.entity.SpiConfig;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Multimap;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 扩展接口实现主要针对已有的Spi机制以及spi扩展
 * @author CH
 * @version 1.0.0
 * @since 2020/6/3 15:22
 *
 * @see com.chua.utils.tools.spi.entity.SpiConfig
 * @see com.chua.utils.tools.spi.entity.ExtensionClass
 * @see com.chua.utils.tools.spi.factory.ExtensionFactory
 */
public interface IExtensionProcessor<T> {

    /**
     * spi初始化配置
     *
     * @param spiConfig
     */
    void init(SpiConfig spiConfig);

    /**
     * 解析@Spi类
     *
     * @param service
     * @param classLoader
     * @return
     */
    Multimap<String, ExtensionClass<T>> analyze(Class<T> service, ClassLoader classLoader);

    /**
     * 刷新
     */
    void refresh();

    /**
     * 解析实体对象的 @Spi 注解
     * @param extensionClass
     * @param t
     * @return
     */
    default void analysisSpi(final T t, final ExtensionClass<T> extensionClass) {
        if (null == t) {
            return;
        }
        Class<?> tClass = t.getClass();
        Spi spi = tClass.getDeclaredAnnotation(Spi.class);

        extensionClass.setName(StringHelper.defaultIfBlank(spi.value(), tClass.getName()));
        extensionClass.setOrder(spi.order());
        extensionClass.setOverrider(spi.override());
        extensionClass.setSingle(spi.single());
    }


    /**
     * 解析为spi
     *
     * @param interfaceClass 待查询接口/类
     * @param extension      自定义扩展注解
     * @param name           spi名称
     * @param loadedClazz    实现类
     * @param order          优先级
     * @return
     */
    default List<ExtensionClass<T>> loadExtension(Class<T> interfaceClass, Class<? extends Annotation> extension, String name, final Class loadedClazz, String order) {
        if (null == loadedClazz) {
            return null;
        }
        //判断类是否为当前接口的实现
        if (!interfaceClass.isAssignableFrom(loadedClazz)) {
            //throw new IllegalStateException
            System.out.println("class line defination [" + interfaceClass + "] not an subType of(" + interfaceClass.getName() + ")");
            return null;
        }
        Class<? extends T> implClass = (Class<? extends T>) loadedClazz;
        // 检查是否有可扩展标识
        Spi spi = implClass.getAnnotation(Spi.class);
        //是否为单例模式
        boolean isSingle = true;
        //覆盖标识
        boolean overrider = false;

        if (null != spi) {
            if (null == name) {
                //spi文件里没配置，用代码里的
                name = spi.value();
            }

            if (Strings.isNullOrEmpty(order)) {
                order = spi.order() + "";
            }

            isSingle = spi.single();
            overrider = spi.override();
        }

        if (null != extension) {
            Annotation annotation = implClass.getAnnotation(extension);
            if (null == name) {
                //非@Spi注解获取注解中的名称
                name = extExtension(annotation);
            }
        }

        List<ExtensionClass<T>> extensionClassList = new ArrayList<>();
        int orderInt = NumberHelper.toInt(order, 0);

        if(null == name) {
            name = implClass.getName();
            ExtensionClass<T> extensionClass = buildExtensionClass(interfaceClass.getName(), orderInt, implClass, name, overrider);
            extensionClass.setSingle(isSingle);

            extensionClassList.add(extensionClass);
        } else {
            List<String> strings = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(name);
            for (String string : strings) {
                ExtensionClass<T> extensionClass = buildExtensionClass(interfaceClass.getName(), orderInt, implClass, string, overrider);
                extensionClass.setSingle(isSingle);

                extensionClassList.add(extensionClass);
            }

        }

        return extensionClassList;
    }

    /**
     * 自定义注解中的 value
     *
     * @param annotation 自定义注解
     * @return
     */
    default public String extExtension(Annotation annotation) {
        if (null != annotation) {
            Class<? extends Annotation> aClass = annotation.getClass();
            Field value = null;
            try {
                value = aClass.getField("value");
            } catch (NoSuchFieldException e) {
                throw new IllegalStateException("The @interface must contain the [value] attribute!");
            }
            if (null == value) {
                throw new IllegalStateException("The @interface must contain the [value] attribute!");
            }

            Object o = null;
            try {
                o = value.get(annotation);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Get the [value] propertiey value exception!");
            }

            return o == null ? null : o.toString();
        }
        return null;
    }

    /**
     * 构建 ExtensionClass
     *
     * @param interfaceClassName 接口名称
     * @param order              优先级
     * @param implClass          实现类
     * @param name               spi名称
     * @param overrider          是否覆盖
     * @return
     */
    default ExtensionClass<T> buildExtensionClass(String interfaceClassName, int order, Class<? extends T> implClass, String name, boolean overrider) {
        ExtensionClass<T> extensionClass = new ExtensionClass<T>();
        extensionClass.setOrder(order);
        extensionClass.setName(name);
        extensionClass.setClazz(implClass);
        extensionClass.setOverrider(overrider);
        extensionClass.setInterfaceName(interfaceClassName);
        return extensionClass;
    }
}
