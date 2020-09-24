package com.chua.utils.tools.spi.processor;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.JsonHelper;
import com.chua.utils.tools.common.MapHelper;
import com.chua.utils.tools.spi.common.SpiConfigs;
import com.chua.utils.tools.spi.entity.ExtensionClass;
import com.chua.utils.tools.spi.entity.SpiConfig;
import com.chua.utils.tools.spi.options.SpiOptions;
import com.google.common.base.Strings;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * metadata资源解析spi
 * @author CH
 * @since 1.0
 */
@Slf4j
public class MetaDataProcessor<T> implements IExtensionProcessor<T> {

    private String metaDataName = "/spi-configuration-metadata.json";
    private final Multimap<String, ExtensionClass<T>> providerCache = MapHelper.newHashMultimap();
    private static final Multimap<String, ExtensionClass> ALL_PROVIDER_CACHE = MapHelper.newHashMultimap();
    private List<String> extensionLoadPath;
    private Class<? extends Annotation> extension;
    private ClassLoader classLoader;

    @Override
    public void init(SpiConfig spiConfig) {
        if(null == spiConfig) {
            spiConfig = new SpiConfig();
        }

        this.extension = spiConfig.getExtension();
        List<String> loaderPath = spiConfig.getExtensionLoaderPath();
        if(!BooleanHelper.hasLength(loaderPath)) {
            loaderPath = SpiConfigs.options.getListValue(SpiOptions.EXTENSION_LOAD_PATH);
        }

        String stringValue = SpiConfigs.options.getStringValue(SpiOptions.EXTENSION_META_NAME);
        if(!Strings.isNullOrEmpty(stringValue)) {
            this.metaDataName = "/" + stringValue;
        }

        this.extensionLoadPath = loaderPath;
    }

    @Override
    public void refresh() {
        ALL_PROVIDER_CACHE.clear();
        providerCache.clear();
    }

    @Override
    public Multimap<String, ExtensionClass<T>> analyze(Class service, ClassLoader classLoader) {
        if(null == this.extensionLoadPath) {
            log.warn("spi机制忽略，原因: 找不到配置文件: spi-config-default.json");
            return providerCache;
        }

        if(ALL_PROVIDER_CACHE.isEmpty()) {
            this.classLoader = classLoader == null ? ClassHelper.getDefaultClassLoader() : classLoader;

            for (String path : this.extensionLoadPath) {
                try {
                    loadFromFile(path);
                } catch (IOException e) {
                    continue;
                }
            }
        }
        Collection<ExtensionClass> extensionClasses = ALL_PROVIDER_CACHE.get(service.getName());
        for (ExtensionClass extensionClass : extensionClasses) {
            providerCache.put(extensionClass.getName(), extensionClass);
        }

        return providerCache;
    }

    /**
     * 加载数据
     * @param path
     */
    private void loadFromFile(String path) throws IOException {
        Enumeration<URL> urlEnumeration = this.classLoader.getResources(path + metaDataName);
        while (urlEnumeration.hasMoreElements()) {
            URL url = urlEnumeration.nextElement();
            Map<String, List<Map<String, Object>>> mapListMap = JsonHelper.toMapListMap(url.openStream());
            if(null == mapListMap) {
                continue;
            }

            loadSpiInCache(mapListMap);

        }
    }

    /**
     * 加载器配置到spi缓存
     * @param mapListMap
     */
    private void loadSpiInCache(Map<String, List<Map<String, Object>>> mapListMap) {
        for (Map.Entry<String, List<Map<String, Object>>> entry : mapListMap.entrySet()) {
            List<Map<String, Object>> entryValue = entry.getValue();
            for (Map<String, Object> stringObjectMap : entryValue) {
                String subclass = MapHelper.strings("subclass", stringObjectMap);

                if(Strings.isNullOrEmpty(subclass)) {
                    continue;
                }

                boolean isSingle = MapHelper.booleans("isSingle", true, stringObjectMap);

                Class<?> aClass = ClassHelper.forName(subclass, this.classLoader);

                ExtensionClass extensionClass = new ExtensionClass();
                extensionClass.setName(MapHelper.strings("name", stringObjectMap));
                extensionClass.setOrder(MapHelper.ints("order", stringObjectMap));
                extensionClass.setClassLoader(this.classLoader);
                extensionClass.setClazz(aClass);
                //单例
                if(isSingle) {
                    try {
                        extensionClass.setObj(ClassHelper.forObject(aClass));
                    } catch (Throwable e) {
                        //无法序列化
                    }
                }
                ALL_PROVIDER_CACHE.put(entry.getKey(), extensionClass);
            }
        }
    }
}
