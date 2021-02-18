package com.chua.utils.tools.spi.processor;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.PropertiesHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.spi.common.SpiConfigs;
import com.chua.utils.tools.spi.entity.ExtensionClass;
import com.chua.utils.tools.spi.entity.SpiConfig;
import com.chua.utils.tools.spi.options.SpiOptions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

/**
 * spring.factories解析
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
@Slf4j
public class FactoriesExtensionProcessor<T> extends AbstractSimpleExtensionProcessor<T> {

    private static final String FACTORIES_RESOURCE_LOCATION = "spring.factories";
    private static final Multimap<String, ExtensionClass<?>> CACHE = HashMultimap.create();
    private List<String> extensionLoadPath;

    @Override
    public void init(SpiConfig spiConfig) {
        if (null == spiConfig) {
            spiConfig = new SpiConfig();
        }

        List<String> loaderPath = spiConfig.getExtensionLoaderPath();
        if (!BooleanHelper.hasLength(loaderPath)) {
            loaderPath = SpiConfigs.newConfig().getListValue(SpiOptions.EXTENSION_LOAD_PATH);
        }

        this.extensionLoadPath = loaderPath;
    }

    @Override
    public void removeAll() {
        CACHE.clear();
    }

    @Override
    public void remove(Class<T> service) {
        CACHE.removeAll(service.getName());
    }

    @Override
    public Collection<ExtensionClass<?>> analyze(Class<T> service, ClassLoader classLoader) {
        if (CACHE.containsKey(service.getName())) {
            return CACHE.get(service.getName());
        }
        this.setClassLoader(classLoader);
        this.setInterfaceClass(service);

        List<ExtensionClass<?>> result = new ArrayList<>();
        for (String path : this.extensionLoadPath) {
            if (!path.endsWith("/")) {
                path += "/";
            }
            result.addAll(loadFromFile(path));
        }
        CACHE.putAll(service.getName(), result);
        return result;
    }

    private List<ExtensionClass<T>> loadFromFile(String path) {
        List<ExtensionClass<T>> all = new ArrayList<>();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(path + FACTORIES_RESOURCE_LOCATION);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                Properties properties = PropertiesHelper.fillProperties(url);
                for (Map.Entry<?, ?> entry : properties.entrySet()) {
                    String factoryTypeName = ((String) entry.getKey()).trim();
                    for (String factoryImplementationName : StringHelper.commaDelimitedListToStringArray((String) entry.getValue())) {
                        String trim = factoryImplementationName.trim();
                        List<ExtensionClass<T>> extensionClassList = buildExtensionClassByClass((Class<? extends T>) ClassHelper.forName(trim));
                        extensionClassList.forEach(new Consumer<ExtensionClass<T>>() {
                            @Override
                            public void accept(ExtensionClass<T> tExtensionClass) {
                                tExtensionClass.setName(factoryTypeName);
                                tExtensionClass.setUrl(url);
                            }
                        });
                        all.addAll(extensionClassList);
                    }
                }
            }
        } catch (IOException ex) {
            log.info("Unable to load factories from location [" + FACTORIES_RESOURCE_LOCATION + "]", ex);
            return Collections.emptyList();
        }
        return all;
    }
}
