package com.chua.utils.tools.spi.processor;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.reflections.ReflectionsFactory;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.spi.entity.ExtensionClass;
import com.chua.utils.tools.spi.entity.SpiConfig;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 反射机制获取子类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
public class ReflectionExtensionProcessor<T> extends AbstractSimpleExtensionProcessor<T> {

    private static final Multimap<String, ExtensionClass<?>> CACHE = HashMultimap.create();
    private ReflectionsFactory reflections;

    @Override
    public void init(SpiConfig spiConfig) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.addUrls(ClassHelper.getUrlsByClassLoader(getClassLoader()));
        configurationBuilder.addScanners(new SubTypesScanner());
        configurationBuilder.setExecutorService(ThreadHelper.newForkJoinPool());
        configurationBuilder.addClassLoader(getClassLoader());
        configurationBuilder.filterInputsBy(s -> false);
        configurationBuilder.setInputsFilter(s -> s.endsWith(".class"));

        this.reflections = new ReflectionsFactory(configurationBuilder);
    }

    @Override
    public Collection<ExtensionClass<?>> analyze(Class<T> service, ClassLoader classLoader) {
        if (CACHE.containsKey(service.getName())) {
            return CACHE.get(service.getName());
        }
        this.setClassLoader(classLoader);
        this.setInterfaceClass(service);

        List<ExtensionClass<?>> result = new ArrayList<>();
        Set<Class<? extends T>> subTypesOf = reflections.getSubTypesOf(service);
        for (Class<? extends T> aClass : subTypesOf) {
            List<ExtensionClass<T>> extensionClasses = buildExtensionClassByClass(aClass);
            for (ExtensionClass<T> extensionClass : extensionClasses) {
                extensionClass.setUrl(reflections.getClassFromUrl(extensionClass.getImplClass().getName()));
                result.add(extensionClass);
            }
        }

        CACHE.putAll(service.getName(), result);
        return result;
    }


    @Override
    public void removeAll() {
        CACHE.clear();
    }

    @Override
    public void remove(Class<T> service) {
        CACHE.removeAll(service.getName());
    }
}
