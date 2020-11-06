package com.chua.utils.tools.spi.factory;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.spi.entity.ExtensionClass;
import com.chua.utils.tools.spi.extension.ExtensionLoader;
import com.chua.utils.tools.spi.processor.*;
import com.google.common.base.Strings;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 扩展工厂类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/6/3 18:24
 */
public class ExtensionFactory implements Runnable {
    /**
     * 时间周期
     */
    public static int TIME_PERIOD = 5;
    /**
     * 时间类型
     */
    public static TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    /**
     * loader缓存
     */
    private static final ConcurrentMap<Class<?>, ExtensionLoader> LOADER_MAP = new ConcurrentHashMap<>();
    /**
     * 单例监控线程
     */
    private static final ExecutorService EXECUTOR_SERVICE = ThreadHelper.newSingleThreadExecutor("monitor-spi");

    static {
        if (TIME_PERIOD > 0) {
            EXECUTOR_SERVICE.execute(new ExtensionFactory());
        }
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
     * 获取扩展类
     *
     * @param <T>       类型
     * @param className 类名
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
    public static synchronized <T> ExtensionLoader<T> getExtensionLoader(Class<T> clazz, final ExtensionProcessor extensionProcessor) {
        ExtensionLoader loader = LOADER_MAP.get(clazz);
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
        }, 0, TIME_PERIOD, TIME_UNIT);
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
