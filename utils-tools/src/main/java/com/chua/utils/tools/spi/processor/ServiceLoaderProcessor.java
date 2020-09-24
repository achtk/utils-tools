package com.chua.utils.tools.spi.processor;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.MapHelper;
import com.chua.utils.tools.spi.entity.ExtensionClass;
import com.chua.utils.tools.spi.entity.SpiConfig;
import com.google.common.collect.Multimap;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * 使用原生spi实现扩展
 * @author CH
 * @version 1.0.0
 * @since 2020/6/3 15:23
 *
 * @see ServiceLoader
 */
public class ServiceLoaderProcessor<T> implements IExtensionProcessor<T> {

    private ClassLoader classLoader;

    @Override
    public void init(SpiConfig spiConfig) {

    }

    @Override
    public Multimap<String, ExtensionClass<T>> analyze(Class<T> service, ClassLoader classLoader) {
        if(null == service) {
            return null;
        }

        Multimap<String, ExtensionClass<T>> result = MapHelper.newHashMultimap();
        this.classLoader = null == classLoader ? ClassHelper.getDefaultClassLoader() : classLoader;

        ServiceLoader<T> serviceLoader = ServiceLoader.load(service, this.classLoader);
        if(null != serviceLoader) {
            Iterator<T> iterator = serviceLoader.iterator();
            for (T t : serviceLoader) {
                ExtensionClass<T> extensionClass = new ExtensionClass<>();
                extensionClass.setClazz(service);
                //解析@Spi注解
                analysisSpi(t, extensionClass);
                //单例模式直接实例化
                if(extensionClass.isSingle()) {
                    extensionClass.setObj(t);
                }
                //赋值
                result.put(extensionClass.getName(), extensionClass);
            }
        }
        return result;
    }

    @Override
    public void refresh() {
    }
}
