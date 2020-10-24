package com.chua.utils.tools.spi.processor;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.reflections.ReflectionsFactory;
import com.chua.utils.tools.classes.reflections.ReflectionsHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.MapHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.spi.common.SpiConfigs;
import com.chua.utils.tools.spi.entity.ExtensionClass;
import com.chua.utils.tools.spi.entity.SpiConfig;
import com.chua.utils.tools.spi.options.SpiOptions;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.adapters.MetadataAdapter;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * 反射Spi
 * @author CH
 * @version 1.0.0
 * @since 2020/10/22
 */
@Slf4j
public class ReflectionProcessor<T> implements IExtensionProcessor<T>{

    private final Multimap<String, ExtensionClass<T>> providerCache = MapHelper.newHashMultimap();
    /**
     * 全部的加载的实现类 {"alias":ExtensionClass}
     */
    private Class<? extends Annotation> extension;
    private Class<T> service;
    private ClassLoader classLoader;

    @Override
    public void init(SpiConfig spiConfig) {
        if(null == spiConfig) {
            spiConfig = new SpiConfig();
        }

        this.extension = spiConfig.getExtension();
        List<String> loaderPath = spiConfig.getExtensionLoaderPath();
        if(!BooleanHelper.hasLength(loaderPath)) {
            loaderPath = SpiConfigs.newConfig().getListValue(SpiOptions.EXTENSION_LOAD_PATH);
        }
    }

    @Override
    public Multimap<String, ExtensionClass<T>> analyze(Class<T> service, ClassLoader classLoader) {
        this.service = service;
        this.classLoader = null == classLoader ? ClassHelper.getDefaultClassLoader() : classLoader;
        if (log.isDebugEnabled()) {
            log.debug("Loading extension of extensible {} from classloader", service.getName());
        }
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.addUrls(ClassHelper.getUrlsByClassLoader(this.classLoader));
        configurationBuilder.addScanners(new SubTypesScanner());
        configurationBuilder.setExecutorService(ThreadHelper.newForkJoinPool());
        configurationBuilder.addClassLoader(this.classLoader);
        configurationBuilder.filterInputsBy(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return false;
            }
        });
        configurationBuilder.setInputsFilter(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.endsWith(".class");
            }
        });

        ReflectionsFactory reflections = new ReflectionsFactory(configurationBuilder);
        Set<Class<? extends T>> subTypesOf = reflections.getSubTypesOf(service);
        for (Class<? extends T> aClass : subTypesOf) {
            List<ExtensionClass<T>> extensionClass = loadExtension(service, extension, null, aClass, null);
            intoProviderCache(extensionClass);
        }
        return providerCache;
    }

    /**
     * 加么茨木
     * @param extensionClass
     */
    private void intoProviderCache(List<ExtensionClass<T>> extensionClass) {
        for (ExtensionClass<T> tExtensionClass : extensionClass) {

            if(null != tExtensionClass && tExtensionClass.isSingle()) {
                tExtensionClass.setObj(ClassHelper.forObject(tExtensionClass.getClazz(), classLoader));
            }
            if(null != tExtensionClass) {
                providerCache.put(tExtensionClass.getName(), tExtensionClass);
            }
        }
    }

    @Override
    public void refresh() {
        providerCache.clear();
    }
}
