package com.chua.utils.tools.spi.extension;

import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.spi.Spi;
import com.chua.utils.tools.spi.entity.ExtensionClass;
import com.chua.utils.tools.spi.entity.SpiConfig;
import com.chua.utils.tools.spi.processor.ExtensionProcessor;
import com.chua.utils.tools.spi.processor.IExtensionProcessor;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * spi扩展器
 * @author CH
 * @version 1.0.0
 * @since 2020/6/3 15:20
 */
@Getter
@Setter
@Slf4j
public class ExtensionLoader<T> {
    /**
     * 待查询类
     */
    private Class<T> service;
    /**
     * 类加载器
     */
    private ClassLoader classLoader;
    /**
     * spi配置
     */
    private SpiConfig spiConfig;
    /**
     *
     */
    private IExtensionProcessor extensionProcessor = new ExtensionProcessor();
    /**
     *
     */
    private final Multimap<String, ExtensionClass<T>> EXTENSION_CACHE = HashMultimap.create();

    private ExtensionLoader() {}

    public ExtensionLoader(Class<T> service) {
        this.service = service;
    }

    public ExtensionLoader(IExtensionProcessor extensionProcessor) {
        this.extensionProcessor = extensionProcessor;
    }

    public ExtensionLoader(Class<T> service, IExtensionProcessor extensionProcessor) {
        this.service = service;
        this.extensionProcessor = extensionProcessor;
    }

    public ExtensionLoader(Class<T> service, ClassLoader classLoader) {
        this.service = service;
        this.classLoader = classLoader;
    }

    public ExtensionLoader(Class<T> service, ClassLoader classLoader, IExtensionProcessor extensionProcessor) {
        this.service = service;
        this.classLoader = classLoader;
        this.extensionProcessor = extensionProcessor;
    }

    /**
     * 检索数据
     * @return
     */
    public ExtensionLoader<T> search() {
        Preconditions.checkArgument(null != service);

        long startTime = 0L;
        if(log.isDebugEnabled()) {
            startTime = System.currentTimeMillis();
        }

        if(!EXTENSION_CACHE.containsKey(service.getName())) {
            if(null == extensionProcessor) {
                extensionProcessor = new ExtensionProcessor();
            }
            extensionProcessor.init(spiConfig);
            Multimap multimap = extensionProcessor.analyze(service, classLoader);

            if(null == multimap) {
                multimap = HashMultimap.create();
            }
            EXTENSION_CACHE.putAll(multimap);
        }
        if(log.isDebugEnabled()) {
            log.debug("[{}]共检索类/接口[{}: {}], 耗时: {}ms", extensionProcessor.getClass().getSimpleName(), service.getName(), EXTENSION_CACHE.size(), (System.currentTimeMillis() - startTime));
        }
        return this;
    }
    /**
     * 获取当前spi下的所有加载器
     * @param name
     * @return
     */
    public Collection<ExtensionClass<T>> getExtensionClasses(String name) {
        if(Strings.isNullOrEmpty(name)) {
            return null;
        }
        return EXTENSION_CACHE.get(name);
    }

    /**
     * 获取当前spi下优先级最高的加载器
     * @param name
     * @return
     */
    public ExtensionClass<T> getExtensionClass(String name) {
        if(null == name) {
            return null;
        }
        Collection<ExtensionClass<T>> classes = getExtensionClasses(name.toLowerCase());
        if(null == classes) {
            return null;
        }

        int maxOrder = -1;
        ExtensionClass<T> result = null;
        for (ExtensionClass<T> aClass : classes) {
            int order = aClass.getOrder();
            if(order > maxOrder) {
                result = aClass;
            }
        }
        return result;
    }

    /**
     * 获取当前spi下的所有实现
     * @param name
     * @return
     */
    public List<T> getExtensions(String name) {
        Collection<ExtensionClass<T>> extensionClasses = getExtensionClasses(name);
        if(null == extensionClasses) {
            return null;
        }
        List<T> result = new ArrayList<>(extensionClasses.size());
        for (ExtensionClass<T> extensionClass : extensionClasses) {
            result.add(extensionClass.getObj());
        }

        return result;
    }
    /**
     * 获取当前spi下的实现
     * @param name
     * @return
     */
    public T getExtension(String name) {
        ExtensionClass<T> extensionClasses = getExtensionClass(name);
        if(null == extensionClasses) {
            return null;
        }

        return extensionClasses.getObj();
    }
    /**
     * 获取当前spi下有效的实现
     * @return
     */
    public T getExtension() {
        Set<String> strings = EXTENSION_CACHE.keySet();
        if(!BooleanHelper.hasLength(strings)) {
            return null;
        }
        return getExtension(FinderHelper.firstElement(strings));
    }
    /**
     * 获取当前spi下的实现
     * @param name
     * @return
     */
    public T getSpiService(String name) {
        return getExtension(name);
    }

    /**
     * 获取当前spi下的实现
     * @return
     */
    public T getSpiService() {
        Preconditions.checkArgument(null != service);
        Spi spi = service.getDeclaredAnnotation(Spi.class);
        if(null == spi) {
            throw new IllegalStateException("The "+ service.getName() +" must contain the [@Spi] annotation!");
        }

        return getExtension(spi.value());
    }
    /**
     * 获取所有缓存
     * @return
     */
    public synchronized Multimap<String, ExtensionClass<T>> getAllExtensionClassess() {
        return EXTENSION_CACHE;
    }

    /**
     * 获取所有的Spi服务
     * @return
     */
    public Set<T> getAllSpiService() {
        Multimap<String, ExtensionClass<T>> multimap = EXTENSION_CACHE;
        if(null == multimap) {
            return null;
        }

        Set<T> result = new HashSet<>(multimap.size());
        for (ExtensionClass<T> value : multimap.values()) {
            T obj = value.getObj();
            if(null == obj) {
                continue;
            }
            result.add(obj);
        }
        return result;
    }

    /**
     * 获取不为空的服务
     * @return
     */
    public T getNotNullSpiService() {
        return FinderHelper.firstElement(getAllSpiService());
    }
    /**
     * 获取随机spi
     * @return
     */
    public T getRandomSpiService() {
        Set<T> spiService = getAllSpiService();
        int size = spiService.size();
        int index = ((Double)(Math.random() * size)).intValue() + 1;
        return FinderHelper.findElement(spiService, Math.min(index, size));
    }

    /**
     * 获取所有Spi名称
     * @return
     */
    public Set<String> keys() {
        Multimap<String, ExtensionClass<T>> multimap = EXTENSION_CACHE;
        return null == multimap ? null : multimap.keySet();
    }

    /**
     * 获取优先级高的所有实现
     * @return
     */
    public Map<String, ExtensionClass<T>> asMap() {
        Multimap<String, ExtensionClass<T>> multimap = EXTENSION_CACHE;
        if(null == multimap) {
            return null;
        }
        Map<String, ExtensionClass<T>> result = new HashMap<>();
        Set<String> names = multimap.keySet();
        for (String name : names) {
            result.put(name, getExtensionClass(name));
        }

        return result;
    }

    /**
     * 重置
     */
    public void refresh() {
        if(null != extensionProcessor) {
            extensionProcessor.refresh();
        }
    }
}
