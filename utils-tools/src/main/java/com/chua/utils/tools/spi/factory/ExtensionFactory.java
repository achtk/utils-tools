package com.chua.utils.tools.spi.factory;

import com.chua.utils.tools.aware.NamedAware;
import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.resource.entity.Resource;
import com.chua.utils.tools.resource.template.ResourceTemplate;
import com.chua.utils.tools.spi.common.SpiConfigs;
import com.chua.utils.tools.spi.entity.ExtensionClass;
import com.chua.utils.tools.spi.extension.ExtensionLoader;
import com.chua.utils.tools.spi.options.SpiOptions;
import com.chua.utils.tools.spi.processor.*;
import com.chua.utils.tools.util.BooleanUtils;
import com.chua.utils.tools.util.ClassUtils;
import com.chua.utils.tools.util.CollectionUtils;
import com.chua.utils.tools.util.IoUtils;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 扩展工厂类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/6/3 18:24
 */
@SuppressWarnings("all")
@Slf4j
@RequiredArgsConstructor
public class ExtensionFactory implements Runnable {
    /**
     * 定时时间
     */
    @NonNull
    private int timePeriod;

    /**
     * 时间类型
     */
    public static TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    /**
     *
     */
    private static final Lock LOCK = new ReentrantLock();
    /**
     * loader缓存
     */
    private static final ConcurrentMap<Class<?>, ExtensionLoader> LOADER_MAP = new ConcurrentHashMap<>();
    /**
     * 单例监控线程
     */
    private static ExecutorService EXECUTOR_SERVICE = null;

    /**
     * 监听数据变化
     */
    public static synchronized void monitor(final int timePeriod) {
        if (timePeriod > 0) {
            LOCK.lock();
            try {
                if (EXECUTOR_SERVICE == null) {
                    EXECUTOR_SERVICE = ThreadHelper.newSingleThreadExecutor("monitor-spi");
                    EXECUTOR_SERVICE.execute(new ExtensionFactory(timePeriod));
                    return;
                }
            } finally {
                LOCK.unlock();
            }
            log.warn("监听已存在, 需要关闭后重新注册");
        }
    }

    /**
     * 监听数据变化
     */
    public static synchronized void unmonitor() {
        LOCK.lock();
        try {
            if (EXECUTOR_SERVICE != null) {
                EXECUTOR_SERVICE.shutdownNow();
                EXECUTOR_SERVICE.shutdown();
                EXECUTOR_SERVICE = null;
            }
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * 获取扩展加载器
     *
     * @param <T>       类型
     * @param className 类称
     * @return ExtensionLoader
     */
    public static synchronized <T> ExtensionLoader<T> getExtensionLoader(String className) {
        return (ExtensionLoader<T>) getExtensionLoader(ClassHelper.forName(className), null);
    }

    /**
     * 获取扩展加载器
     *
     * @param <T>   类型
     * @param clazz 类
     * @return ExtensionLoader
     */
    public static synchronized <T> ExtensionLoader<T> getExtensionLoader(Class<T> clazz) {
        return getExtensionLoader(clazz, null);
    }

    /**
     * 获取扩展加载器
     *
     * @param <T>   类型
     * @param clazz 类
     * @return ExtensionLoader
     */
    public static synchronized <T> ExtensionLoader<T> getTempExtensionLoader(Class<T> clazz) {
        return getTempExtensionLoader(clazz, null);
    }


    /**
     * 获取扩展类
     *
     * @param <T>           类型
     * @param className     类名
     * @param extensionName 扩展名称
     * @param classLoaders  类加载器
     * @return 类
     */
    public static synchronized <T> T getExtensionLoader(final String className, final String extensionName, final ClassLoader... classLoaders) {
        if (Strings.isNullOrEmpty(className)) {
            return null;
        }
        Class<?> aClass = ClassHelper.forName(className, classLoaders);
        if (null == aClass) {
            return null;
        }
        ExtensionLoader<?> extensionLoader = getExtensionLoader(aClass);
        if (null == extensionLoader) {
            return null;
        }
        Object spiService = extensionLoader.getSpiService(extensionName);
        return null == spiService ? ClassHelper.forObject(className, classLoaders) : (T) spiService;
    }

    /**
     * 获取扩展加载器
     *
     * <table border=1 cellpadding=5 summary="implements,desc">
     * <tr>
     *      <th>index</th>
     *      <th>implements</th>
     *      <th>desc</th>
     * </tr>
     *
     * <tr>
     *   <td>1</td>
     *   <td>com.chua.utils.tools.spi.processor.ScanProcessor</td>
     *   <td>扫描所有的子类.@see com.chua.utils.tools.resource.FastResourceHelper</td>
     * </tr>
     *
     * <tr>
     *   <td>2</td>
     *   <td>com.chua.utils.tools.spi.processor.ExtensionProcessor</td>
     *   <td>根据META-INF/spi-config-default.json配置项位置，解析当前位置下接口或者抽象类所有实现/子类.</td>
     * </tr>
     *
     * <tr>
     *   <td>3</td>
     *   <td>com.chua.utils.tools.spi.processor.ServiceLoaderProcessor</td>
     *   <td>根据jdk自带ServiceLoader解析实现.</td>
     * </tr>
     * <tr>
     *   <td>4</td>
     *   <td>com.chua.utils.tools.spi.processor.MetaDataProcessor</td>
     *   <td>根据META-INF/spi-config-default.json配置项位置，解析当前位置下spi-configuration-metadata.json(默认)中的子类或者实现</td>
     * </tr>
     * </table>
     *
     * @param <T>                类型
     * @param clazz              类
     * @param extensionProcessor 扩展器
     * @return ExtensionLoader
     * @see ReflectionExtensionProcessor
     * @see CustomExtensionProcessor
     * @see JsonExtensionProcessor
     * @see FactoriesExtensionProcessor
     * @see ServiceLoaderProcessor
     */
    public static synchronized <T> ExtensionLoader<T> getExtensionLoader(Class<T> clazz, final ExtensionProcessor... extensionProcessor) {
        ExtensionLoader<T> loader = LOADER_MAP.get(clazz);
        if (null == loader) {
            loader = new ExtensionLoader<>(clazz, extensionProcessor);
            loader.search();
            LOADER_MAP.put(clazz, loader);
        }
        return loader;
    }

    /**
     * 获取扩展加载器
     *
     * <table border=1 cellpadding=5 summary="implements,desc">
     * <tr>
     *      <th>index</th>
     *      <th>implements</th>
     *      <th>desc</th>
     * </tr>
     *
     * <tr>
     *   <td>1</td>
     *   <td>com.chua.utils.tools.spi.processor.ScanProcessor</td>
     *   <td>扫描所有的子类.@see com.chua.utils.tools.resource.FastResourceHelper</td>
     * </tr>
     *
     * <tr>
     *   <td>2</td>
     *   <td>com.chua.utils.tools.spi.processor.ExtensionProcessor</td>
     *   <td>根据META-INF/spi-config-default.json配置项位置，解析当前位置下接口或者抽象类所有实现/子类.</td>
     * </tr>
     *
     * <tr>
     *   <td>3</td>
     *   <td>com.chua.utils.tools.spi.processor.ServiceLoaderProcessor</td>
     *   <td>根据jdk自带ServiceLoader解析实现.</td>
     * </tr>
     * <tr>
     *   <td>4</td>
     *   <td>com.chua.utils.tools.spi.processor.MetaDataProcessor</td>
     *   <td>根据META-INF/spi-config-default.json配置项位置，解析当前位置下spi-configuration-metadata.json(默认)中的子类或者实现</td>
     * </tr>
     * </table>
     *
     * @param <T>                类型
     * @param clazz              类
     * @param extensionProcessor 扩展器
     * @return ExtensionLoader
     * @see ReflectionExtensionProcessor
     * @see CustomExtensionProcessor
     * @see JsonExtensionProcessor
     * @see FactoriesExtensionProcessor
     * @see ServiceLoaderProcessor
     */
    public static synchronized <T> ExtensionLoader<T> getTempExtensionLoader(Class<T> clazz, final ExtensionProcessor extensionProcessor) {
        ExtensionLoader<T> loader = new ExtensionLoader<>(clazz, extensionProcessor);
        return loader.search();
    }

    /**
     * 获取扩展加载器
     *
     * <table border=1 cellpadding=5 summary="implements,desc">
     * <tr>
     *      <th>index</th>
     *      <th>implements</th>
     *      <th>desc</th>
     * </tr>
     *
     * <tr>
     *   <td>1</td>
     *   <td>com.chua.utils.tools.spi.processor.ScanProcessor</td>
     *   <td>扫描所有的子类.</td>
     * </tr>
     *
     * <tr>
     *   <td>2</td>
     *   <td>com.chua.utils.tools.spi.processor.ExtensionProcessor</td>
     *   <td>根据类解析所有实现.</td>
     * </tr>
     *
     * <tr>
     *   <td>3</td>
     *   <td>com.chua.utils.tools.spi.processor.ServiceLoaderProcessor</td>
     *   <td>根据jdk自带ServiceLoader解析实现.</td>
     * </tr>
     * </table>
     *
     * @param <T>                类型
     * @param clazz              类
     * @param extensionProcessor 扩展器
     * @return ExtensionLoader
     * @see ReflectionExtensionProcessor
     * @see CustomExtensionProcessor
     * @see JsonExtensionProcessor
     * @see FactoriesExtensionProcessor
     * @see ServiceLoaderProcessor
     */
    public static synchronized <T> ExtensionLoader<T> getRefreshExtensionLoader(Class<T> clazz, final ExtensionProcessor<T> extensionProcessor) {
        remove(clazz);
        return getExtensionLoader(clazz, extensionProcessor);
    }

    /**
     * 获取@Spi服务
     *
     * @param clazz 类
     * @param <T>   类型
     * @return 对象
     */
    public static synchronized <T> T getSpiService(Class<T> clazz) {
        ExtensionLoader extensionLoader = getExtensionLoader(clazz);
        return null == extensionLoader ? null : (T) extensionLoader.getSpiService();
    }

    /**
     * 删除缓存
     *
     * @param tClass 类
     */
    public static synchronized <T> void remove(Class<T> tClass) {
        if (null == tClass) {
            return;
        }
        if (LOADER_MAP.containsKey(tClass)) {
            ExtensionLoader<T> extensionLoader = LOADER_MAP.get(tClass);
            if (null != extensionLoader) {
                extensionLoader.refresh();
            }
            LOADER_MAP.remove(tClass);
        }
    }

    /**
     * 删除所有缓存
     */
    public static synchronized void removeAll() {
        LOADER_MAP.clear();
    }

    /**
     * 刷新缓存
     *
     * @param <T>    类型
     * @param tClass 类
     * @return ExtensionLoader
     */
    public static synchronized <T> ExtensionLoader<T> refresh(Class<T> tClass) {
        if (null == tClass) {
            return null;
        }

        remove(tClass);
        return getExtensionLoader(tClass);
    }

    /**
     * 消费Spi实现方法
     *
     * @param tClass   接口/类
     * @param consumer 实现消费
     */
    public static synchronized <T> void doWithInvoke(Class<T> tClass, Consumer<T> consumer) {
        if (null == tClass || null == consumer) {
            return;
        }
        ExtensionLoader<T> extensionLoader = getExtensionLoader(tClass);
        extensionLoader.getAllSpiService().forEach(consumer);
    }
    /**
     * 转化为spi文件
     *
     * @param interfaceClass 接口
     */
    public static void createSpi(Class<?> interfaceClass) {
       createSpi(interfaceClass, null);
    }
    /**
     * 转化为spi文件
     *
     * @param interfaceClass 接口
     * @param stringSupplier 命名
     */
    public static void createSpi(Class<?> interfaceClass, Function<Class<?>, String> stringSupplier) {
        ResourceTemplate template = new ResourceTemplate();
        List<Class<?>> subOfType = template.getSubOfType(interfaceClass);
        List<String> listValue = SpiConfigs.newConfig().getListValue(SpiOptions.EXTENSION_LOAD_PATH);
        if (!BooleanUtils.hasLength(listValue)) {
            return;
        }
        String last = CollectionUtils.findLast(listValue);
        String source = ExtensionFactory.class.getProtectionDomain().getCodeSource().getLocation().toExternalForm();
        Set<Resource> resources = template.getResources("classpath:" + last);
        Resource resource1 = resources.stream().filter(resource -> {
            return resource.getUrl().toExternalForm().startsWith(source);
        }).findFirst().get();

        String spiName = interfaceClass.getName();
        String replace = resource1.getUrl().toExternalForm().replace("/target/classes", "/src/main/resources");


        try {
            Path realSpiPath = Paths.get(new URI(replace + "/" + spiName));
            Path targetSpiPath = Paths.get(new URI(resource1.getUrl().toExternalForm() + "/" + spiName));
            if (!Files.exists(realSpiPath)) {
                if (!Files.exists(realSpiPath.getParent())) {
                    Files.createDirectories(realSpiPath.getParent());
                }
                Files.createFile(realSpiPath);
            }

            if (!Files.exists(targetSpiPath)) {
                if (!Files.exists(targetSpiPath.getParent())) {
                    Files.createDirectories(targetSpiPath.getParent());
                }
                Files.createFile(targetSpiPath);
            }
            byte[] bytes = IoUtils.toByteArray(new StringReader(Joiner.on("\r\n").join(subOfType.stream().map(s -> {
                if (null != stringSupplier) {
                    String s1 = stringSupplier.apply(s);
                    return Strings.isNullOrEmpty(s1) ? s.getName() : s1 + "=" + s.getName();
                }
                if (NamedAware.class.isAssignableFrom(s)) {
                    NamedAware forObject = ClassUtils.forObject(s, NamedAware.class);
                    return forObject.named() + "=" + s.getName();
                }
                return s.getName();
            }).collect(Collectors.toList()))));
            Files.write(realSpiPath, bytes, StandardOpenOption.TRUNCATE_EXISTING);
            Files.write(targetSpiPath, bytes, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        ScheduledExecutorService scheduledExecutorService = ThreadHelper.newScheduledThreadPoolExecutor(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (LOADER_MAP.isEmpty()) {
                    return;
                }
                for (Map.Entry<Class<?>, ExtensionLoader> loaderEntry : LOADER_MAP.entrySet()) {
                    ExtensionLoader extensionLoader = loaderEntry.getValue();
                    checkTime(extensionLoader, loaderEntry.getKey());
                }
            }
        }, 0, timePeriod, TIME_UNIT);
    }

    /**
     * 检测时间
     *
     * @param extensionLoader 加载器
     * @param key             索引
     */
    private synchronized void checkTime(ExtensionLoader extensionLoader, Class key) {
        Collection<ExtensionClass> loaderExtension = extensionLoader.getExtensionClasses(key.getName());

        boolean reload = false;
        for (ExtensionClass extensionClass : loaderExtension) {
            long recordTime = extensionClass.getRecordTime();
            if (0L == recordTime) {
                continue;
            }
            URL url = extensionClass.getUrl();
            if ("file".equals(url.getProtocol())) {
                boolean newTime = new File(url.getFile()).lastModified() > recordTime;
                if (newTime) {
                    reload = true;
                    extensionClass.setUrl(url);
                }
            }
        }

        if (reload) {
            extensionLoader.refresh();
            reload = false;
        }
    }
}
