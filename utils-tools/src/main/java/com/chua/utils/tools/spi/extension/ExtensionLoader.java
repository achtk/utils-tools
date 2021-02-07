package com.chua.utils.tools.spi.extension;

import com.chua.utils.tools.aware.NamedFactoryAware;
import com.chua.utils.tools.collects.MultiSortValueMap;
import com.chua.utils.tools.collects.MultiValueSortMap;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.FinderHelper;
import com.chua.utils.tools.constant.StringConstant;
import com.chua.utils.tools.spi.Spi;
import com.chua.utils.tools.spi.entity.ExtensionClass;
import com.chua.utils.tools.spi.entity.SpiConfig;
import com.chua.utils.tools.spi.processor.AbstractSimpleExtensionProcessor;
import com.chua.utils.tools.spi.processor.CustomExtensionProcessor;
import com.chua.utils.tools.spi.processor.ExtensionProcessor;
import com.chua.utils.tools.spi.processor.ServiceLoaderProcessor;
import com.chua.utils.tools.util.ArrayUtils;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Ordering;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * spi扩展器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/6/3 15:20
 */
@Getter
@Setter
@Slf4j
@SuppressWarnings("all")
public class ExtensionLoader<T> {

    public static final String ANY = StringConstant.ANY;
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
     * 默认
     */
    private static final ExtensionProcessor[] DEFAULT_PROCESSOR = new ExtensionProcessor[]{
            new CustomExtensionProcessor(),
            new ServiceLoaderProcessor()
    };
    /**
     * 处理器
     */
    private ExtensionProcessor[] extensionProcessor = DEFAULT_PROCESSOR;

    private final Comparator<ExtensionClass<T>> comparator = new Comparator<ExtensionClass<T>>() {
        @Override
        public int compare(ExtensionClass<T> o1, ExtensionClass<T> o2) {
            return Ordering.natural().compare(o2.getOrder(), o1.getOrder());
        }
    };
    /**
     * 缓存数据
     */
    private final MultiSortValueMap<String, ExtensionClass<T>> extensionClassMultimap = MultiValueSortMap.create(comparator);

    private ExtensionLoader() {
    }

    public ExtensionLoader(Class<T> service) {
        this.service = service;
    }

    public ExtensionLoader(ExtensionProcessor... extensionProcessor) {
        this.extensionProcessor = extensionProcessor;
    }

    public ExtensionLoader(Class<T> service, ExtensionProcessor... extensionProcessor) {
        this.service = service;
        this.extensionProcessor = extensionProcessor;
    }

    public ExtensionLoader(Class<T> service, ClassLoader classLoader) {
        this.service = service;
        this.classLoader = classLoader;
    }

    public ExtensionLoader(Class<T> service, ClassLoader classLoader, ExtensionProcessor... extensionProcessor) {
        this.service = service;
        this.classLoader = classLoader;
        this.extensionProcessor = extensionProcessor;
    }

    /**
     * 检索扩展对象
     *
     * @return this
     */
    public synchronized ExtensionLoader<T> search() {
        Preconditions.checkArgument(null != service);

        long startTime = 0L;
        if (log.isDebugEnabled()) {
            startTime = System.currentTimeMillis();
        }

        if (!extensionClassMultimap.containsKey(service.getName())) {
            if (ArrayUtils.isNothing(extensionProcessor)) {
                extensionProcessor = DEFAULT_PROCESSOR;
            }
            for (ExtensionProcessor processor : extensionProcessor) {
                processor.init(spiConfig);
                Collection<ExtensionClass<T>> collection = processor.analyze(service, classLoader);
                cache(collection);
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("[{}]共检索类/接口[{}: {}], 耗时: {}ms", extensionProcessor.getClass().getSimpleName(), service.getName(), extensionClassMultimap.size(), (System.currentTimeMillis() - startTime));
        }
        return this;
    }

    /**
     * 获取当前spi下的所有加载器
     *
     * @param name 扩展名称
     * @return 返回所有的扩展对象
     */
    public synchronized SortedSet<ExtensionClass<T>> getExtensionClasses(String name) {
        if (null == name) {
            return null;
        }
        MultiSortValueMap<String, ExtensionClass<T>> sortMap = new MultiValueSortMap<>(comparator);

        for (Map.Entry<String, ExtensionClass<T>> entry : extensionClassMultimap.entrySet()) {
            String key = entry.getKey();
            if (key.equals(service.getName())) {
                continue;
            }
            ExtensionClass<T> extensionClass = entry.getValue();
            NamedFactoryAware namedFactoryAware = extensionClass.getNamedFactoryAware();
            sortMap.put(key, extensionClass);
            if (null != namedFactoryAware) {
                String named = namedFactoryAware.named();
                if (null != named) {
                    sortMap.put(namedFactoryAware.named(), extensionClass);
                }
            }
        }
        return sortMap.get(name);
    }

    /**
     * 获取当前spi下的所有加载器
     *
     * @param name 扩展名称
     * @return 返回所有的扩展对象
     */
    public synchronized ExtensionClass<T> getExtensionClassOne(String name) {
        if (null == name) {
            return null;
        }
        MultiSortValueMap<String, ExtensionClass<T>> sortMap = new MultiValueSortMap<>(comparator);

        for (Map.Entry<String, ExtensionClass<T>> entry : extensionClassMultimap.entrySet()) {
            String key = entry.getKey();
            if (key.equals(service.getName())) {
                continue;
            }
            ExtensionClass<T> extensionClass = entry.getValue();
            NamedFactoryAware namedFactoryAware = extensionClass.getNamedFactoryAware();
            if (null == namedFactoryAware) {
                sortMap.put(key, extensionClass);
                continue;
            }
            String named = namedFactoryAware.named();
            if (null != named) {
                sortMap.put(namedFactoryAware.named(), extensionClass);
            }
        }


        if (Strings.isNullOrEmpty(name) && sortMap.keySet().size() == 1) {
            return sortMap.values().stream().findFirst().get();
        }

        SortedSet<ExtensionClass<T>> extensionClasses = null;
        if (sortMap.containsKey(name)) {
            extensionClasses = sortMap.get(name);
        } else {
            extensionClasses = getExtensionClasses(name);
        }

        if (null == extensionClasses) {
            return null;
        }

        return extensionClasses.first();
    }

    /**
     * 获取当前spi下优先级最高的加载器
     *
     * @param name 扩展名称
     * @return 当前spi下优先级最高的加载器
     */
    public synchronized ExtensionClass<T> getExtensionClass(String name) {
        if (null == name) {
            return null;
        }
        return getExtensionClassOne(name.toLowerCase());
    }

    /**
     * 获取当前spi下的所有实现
     *
     * @param name 扩展名称
     * @param <T>  类型
     * @return 当前spi下的所有实现
     */
    public synchronized List<T> getExtensions(String name) {
        Collection<ExtensionClass<T>> extensionClasses = getExtensionClasses(name);
        if (null == extensionClasses) {
            return null;
        }
        List<T> result = new ArrayList<>(extensionClasses.size());
        for (ExtensionClass<T> extensionClass : extensionClasses) {
            result.add(extensionClass.getObj());
        }

        return result;
    }

    /**
     * 当实现是唯一的情况下获取唯一的扩展实现, 否则返回 null
     *
     * @return 优先级最高的扩展实现
     */
    public synchronized T getExtension() {
        ExtensionClass<T> extensionClasses = getExtensionClass("");
        if (null == extensionClasses) {
            return null;
        }
        return extensionClasses.getObj();
    }

    /**
     * 获取优先级最高的扩展实现
     *
     * @param name 扩展名称
     * @return 优先级最高的扩展实现
     */
    public synchronized T getExtension(String name) {
        ExtensionClass<T> extensionClasses = getExtensionClass(name);
        if (null == extensionClasses) {
            return null;
        }

        return extensionClasses.getObj();
    }

    /**
     * 获取优先级最高的扩展实现
     *
     * @param name 扩展名称
     * @return 优先级最高的扩展实现
     */
    public synchronized <E> E getExtension(Class<E> tClass) {
        if (null == tClass) {
            return null;
        }
        Collection<ExtensionClass<T>> values = extensionClassMultimap.values();
        List<ExtensionClass<T>> collect = values.stream().filter(item -> {
            return tClass.isAssignableFrom(item.getObj().getClass());
        }).collect(Collectors.toList());
        ExtensionClass<T> extensionClass = values.stream().filter(item -> {
            return tClass.isAssignableFrom(item.getObj().getClass());
        }).sorted(new Comparator<ExtensionClass<T>>() {
            @Override
            public int compare(ExtensionClass<T> o1, ExtensionClass<T> o2) {
                return o1.getOrder() > o2.getOrder() ? 1 : 0;
            }
        }).findFirst().get();
        return (E) extensionClass.getObj();
    }

    /**
     * 获取当前spi下有效的实现
     *
     * @return
     */
    public synchronized T getFirst() {
        Set<String> strings = extensionClassMultimap.keySet();
        if (!BooleanHelper.hasLength(strings)) {
            return null;
        }
        return getExtension(FinderHelper.firstElement(strings));
    }

    /**
     * 获取当前spi下的实现
     *
     * @param name 扩展名称
     * @return 优先级最高的扩展实现
     * @see #getExtension(String)
     */
    public synchronized T getSpiService(String name) {
        return getExtension(name);
    }

    /**
     * 获取当前spi注解标识的实现
     *
     * @return 当前spi注解标识的实现
     */
    public synchronized T getSpiService() {
        Preconditions.checkArgument(null != service);
        Spi spi = service.getDeclaredAnnotation(Spi.class);
        if (null == spi) {
            throw new IllegalStateException("The " + service.getName() + " must contain the [@Spi] annotation!");
        }

        String[] value = spi.value();
        if (!BooleanHelper.hasLength(value)) {
            return null;
        }
        return getExtension(value[0]);
    }

    /**
     * 获取所有缓存
     *
     * @return 所有缓存
     */
    public synchronized MultiSortValueMap<String, ExtensionClass<T>> getAllExtensionClassess() {
        return extensionClassMultimap;
    }

    /**
     * 获取所有的Spi服务
     *
     * @return 所有的Spi服务
     */
    public synchronized Set<T> getAllSpiService() {
        MultiSortValueMap<String, ExtensionClass<T>> multimap = extensionClassMultimap;
        if (null == multimap) {
            return Collections.emptySet();
        }

        Set<T> result = new HashSet<>(multimap.size());
        for (ExtensionClass<T> value : multimap.values()) {
            T obj = value.getObj();
            if (null == obj) {
                continue;
            }
            result.add(obj);
        }
        return result;
    }

    /**
     * 获取不为空的服务
     *
     * @return 获取不为空的实现
     */
    public synchronized T getNotNullSpiService() {
        return FinderHelper.firstElement(getAllSpiService());
    }

    /**
     * 获取随机spi
     *
     * @return 随机实现
     */
    public synchronized T getRandomSpiService() {
        Set<T> spiService = getAllSpiService();
        int size = spiService.size();
        int index = ((Double) (Math.random() * size)).intValue() + 1;
        return FinderHelper.findElement(spiService, Math.min(index, size));
    }

    /**
     * 获取所有Spi名称
     *
     * @return 所有Spi名称
     */
    public synchronized Set<String> keys() {
        MultiSortValueMap<String, ExtensionClass<T>> multimap = extensionClassMultimap;
        return null == multimap ? null : multimap.keySet();
    }

    /**
     * 获取优先级高的所有实现
     *
     * @return 优先级高的所有实现
     */
    public synchronized Map<String, ExtensionClass<T>> asMap() {
        MultiSortValueMap<String, ExtensionClass<T>> multimap = extensionClassMultimap;
        if (null == multimap) {
            return Collections.emptyMap();
        }
        Set<String> names = multimap.keySet();
        Map<String, ExtensionClass<T>> result = new HashMap<>(names.size());

        for (String name : names) {
            result.put(name, getExtensionClass(name));
        }

        return result;
    }

    /**
     * 重置缓存
     */
    public synchronized void refresh() {
        if (null != extensionProcessor) {
            for (ExtensionProcessor processor : extensionProcessor) {
                processor.removeAll();
                if (processor instanceof AbstractSimpleExtensionProcessor) {
                    AbstractSimpleExtensionProcessor abstractSimpleExtensionProcessor = (AbstractSimpleExtensionProcessor) processor;
                    Collection<ExtensionClass<T>> analyze = processor.analyze(abstractSimpleExtensionProcessor.getInterfaceClass(), abstractSimpleExtensionProcessor.getClassLoader());
                    extensionClassMultimap.clear();
                    cache(analyze);
                }
            }
        }
    }

    /**
     * 缓存数据
     *
     * @param collection 数据
     */
    private void cache(Collection<ExtensionClass<T>> collection) {
        if (null == collection) {
            collection = new TreeSet<>();
        }
        collection.forEach(new Consumer<ExtensionClass<T>>() {
            @Override
            public void accept(ExtensionClass<T> tExtensionClass) {
                extensionClassMultimap.put(tExtensionClass.getName(), tExtensionClass);
                extensionClassMultimap.put(service.getName(), tExtensionClass);
            }
        });
    }

    /**
     * 获取优先级最高的扩展数据
     *
     * @return 优先级最高的扩展数据
     */
    public Map<String, T> getPriorityExtension() {
        Set<String> keySet = extensionClassMultimap.keySet();
        Map<String, T> result = new HashMap<>(keySet.size());

        for (String key : keySet) {
            result.put(key, getExtension(key));
        }
        return result;
    }

    /**
     * 添加/覆盖已有的缓存
     *
     * @param extensionClass 扩展类
     */
    public void appendExtension(final ExtensionClass<T> extensionClass) {
        extensionClassMultimap.put(extensionClass.getName(), extensionClass);
    }

    /**
     * 获取Map所有接口
     *
     * @return 所有接口
     */
    public Map<String, T> toMap() {
        Map<String, T> result = new ConcurrentHashMap<>();
        Map<String, ExtensionClass<T>> extensionClassMap = asMap();
        extensionClassMap.keySet().forEach(key -> {
            T extension = getExtension(key);
            if (extension == null) {
                return;
            }
            result.put(key, extension);
        });

        return result;
    }

    /**
     * 优先级数据
     *
     * @return 优先级数据
     */
    public <K> Map<K, T> toPriorityMap(final Function<ExtensionClass<T>, K> supplier) {
        Map<K, T> result = new HashMap<>();
        Map<String, ExtensionClass<T>> first = extensionClassMultimap.getAllFirst();
        for (Map.Entry<String, ExtensionClass<T>> entry : first.entrySet()) {
            if (!entry.getKey().equals(service.getName())) {
                result.put(supplier.apply(entry.getValue()), entry.getValue().getObj());
            }
        }
        return result;
    }

    /**
     * 流式操作
     *
     * @return 流
     */
    public Stream<Map.Entry<String, ExtensionClass<T>>> stream() {
        Map<String, ExtensionClass<T>> first = extensionClassMultimap.getAllFirst();
        return first.entrySet().stream();
    }
}
