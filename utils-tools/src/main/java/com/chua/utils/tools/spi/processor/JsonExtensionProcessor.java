package com.chua.utils.tools.spi.processor;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.JsonHelper;
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

/**
 * spring.factories解析
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/30
 */
@Slf4j
public class JsonExtensionProcessor<T> extends AbstractSimpleExtensionProcessor<T> {

    private List<String> extensionLoadPath;
    private static final String FACTORIES_RESOURCE_LOCATION = "tools-extension.json";
    private static final Multimap<String, ExtensionClass<?>> CACHE = HashMultimap.create();

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

    @Override
    public void removeAll() {
        CACHE.clear();
    }

    @Override
    public void remove(Class<T> service) {
        CACHE.removeAll(service.getName());
    }

    /**
     * 加载Json
     *
     * @param path 路径
     * @return List
     */
    private List<? extends ExtensionClass<T>> loadFromFile(String path) {
        List<ExtensionClass<T>> all = new ArrayList<>();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(path + FACTORIES_RESOURCE_LOCATION);
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                List<ExtensionClass> list = JsonHelper.fromJson2List(url, ExtensionClass.class);
                for (ExtensionClass extensionClass : list) {
                    extensionClass.setUrl(url);
                    all.add(extensionClass);
                }
            }
        } catch (IOException ex) {
            log.info("Unable to load factories from location [" + FACTORIES_RESOURCE_LOCATION + "]", ex);
            return Collections.emptyList();
        }
        return all;
    }
}
