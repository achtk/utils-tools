package com.chua.utils.tools.spi.processor;

import com.chua.utils.tools.spi.entity.ExtensionClass;
import com.chua.utils.tools.spi.entity.SpiConfig;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.*;

/**
 * JDK原生spi实现扩展
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
public class ServiceLoaderProcessor<T> extends AbstractSimpleExtensionProcessor<T> {

    private static final Multimap<String, ExtensionClass<?>> CACHE = HashMultimap.create();

    @Override
    public void init(SpiConfig spiConfig) {

    }

    @Override
    public Collection<ExtensionClass<?>> analyze(Class<T> service, ClassLoader classLoader) {
        if (CACHE.containsKey(service.getName())) {
            return CACHE.get(service.getName());
        }
        this.setClassLoader(classLoader);
        this.setInterfaceClass(service);

        List<ExtensionClass<?>> result = new ArrayList<>();
        ServiceLoader<T> serviceLoader = ServiceLoader.load(service, getClassLoader());
        for (T t : serviceLoader) {
            result.addAll(buildExtensionClassByObject(t));
        }
        CACHE.putAll(service.getName(), result);
        return result;
    }

    @Override
    public void removeAll() {
        CACHE.clear();
    }

    @Override
    public void remove(Class<T> tClass) {
        CACHE.removeAll(tClass.getName());
    }


}
