package com.chua.utils.tools.spi.processor;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.MapHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.resource.Resource;
import com.chua.utils.tools.resource.FastResourceHelper;
import com.chua.utils.tools.spi.entity.ExtensionClass;
import com.chua.utils.tools.spi.entity.SpiConfig;
import com.google.common.collect.Multimap;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.Set;

import static com.chua.utils.tools.constant.StringConstant.EXTENSION_CLASS_SUFFIX;

/**
 * 通过资源扫描工具实现扩展
 * @author CH
 * @version 1.0.0
 * @since 2020/6/3 18:04
 *
 * @see IExtensionProcessor
 * @see FastResourceHelper
 */
@Slf4j
public class FastResourceProcessor<T> implements IExtensionProcessor<T> {

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
    }

    @Override
    public Multimap<String, ExtensionClass<T>> analyze(Class<T> service, ClassLoader classLoader) {
        if(null == service) {
            return null;
        }

        this.service = service;
        this.classLoader = null == classLoader ? ClassHelper.getDefaultClassLoader() : classLoader;
        Set<Resource> resources = FastResourceHelper.getResources("subclass:" + service.getName(), this.classLoader);
        for (Resource resource : resources) {
            String name = resource.getName();
            name = StringHelper.startsWithAndEmpty(name, "/");
            name = name.replace("/", ".").replace(EXTENSION_CLASS_SUFFIX, "");

            ExtensionClass<T> extensionClass = loadExtension(service, extension, null, ClassHelper.forName(name), null);
            if(null != extensionClass && extensionClass.isSingle()) {
                extensionClass.setObj(ClassHelper.forObject(extensionClass.getClazz(), classLoader));
            }
            if(null != extensionClass) {
                providerCache.put(extensionClass.getName(), extensionClass);
            }
        }
        return providerCache;
    }

    @Override
    public void refresh() {
        providerCache.clear();
    }

}
