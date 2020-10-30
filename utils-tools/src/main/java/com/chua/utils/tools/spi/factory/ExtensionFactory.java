package com.chua.utils.tools.spi.factory;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.spi.extension.ExtensionLoader;
import com.chua.utils.tools.spi.processor.ExtensionProcessor;
import com.google.common.base.Strings;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 扩展工厂类
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/6/3 18:24
 */
public class ExtensionFactory {
    /**
     * loader缓存
     */
    private static final ConcurrentMap<Class, ExtensionLoader> LOADER_MAP = new ConcurrentHashMap<>();

    /**
     * 获取扩展加载器
     *
     * @param clazz 类
     * @param <T>
     * @return
     */
    public static synchronized <T> ExtensionLoader<T> getExtensionLoader(Class<T> clazz) {
        return getExtensionLoader(clazz, null);
    }


    /**
     * 获取扩展类
     *
     * @param <T>
     * @param className 类名
     * @return
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
     * @param clazz              类
     * @param extensionProcessor 扩展器
     * @param <T>
     * @return
     * @see com.chua.utils.tools.spi.processor.ReflectionExtensionProcessor
     * @see com.chua.utils.tools.spi.processor.CustomExtensionProcessor
     * @see com.chua.utils.tools.spi.processor.JsonExtensionProcessor
     * @see com.chua.utils.tools.spi.processor.FactoriesExtensionProcessor
     * @see com.chua.utils.tools.spi.processor.ServiceLoaderProcessor
     */
    public static synchronized <T> ExtensionLoader<T> getExtensionLoader(Class<T> clazz, final ExtensionProcessor extensionProcessor) {
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
     * @param clazz              类
     * @param extensionProcessor 扩展器
     * @param <T>
     * @return
     * @see com.chua.utils.tools.spi.processor.ReflectionExtensionProcessor
     * @see com.chua.utils.tools.spi.processor.CustomExtensionProcessor
     * @see com.chua.utils.tools.spi.processor.JsonExtensionProcessor
     * @see com.chua.utils.tools.spi.processor.FactoriesExtensionProcessor
     * @see com.chua.utils.tools.spi.processor.ServiceLoaderProcessor
     */
    public static synchronized <T> ExtensionLoader<T> getRefreshExtensionLoader(Class<T> clazz, final ExtensionProcessor extensionProcessor) {
        remove(clazz);
        return getExtensionLoader(clazz, extensionProcessor);
    }

    /**
     * 获取@Spi服务
     *
     * @param clazz 类
     * @param <T>
     * @return
     */
    public static synchronized <T> T getSpiService(Class<T> clazz) {
        ExtensionLoader<T> extensionLoader = getExtensionLoader(clazz);
        return null == extensionLoader ? null : extensionLoader.getSpiService();
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
            ExtensionLoader extensionLoader = LOADER_MAP.get(tClass);
            if (null != extensionLoader) {
                extensionLoader.refresh();
            }
            LOADER_MAP.remove(tClass);
        }
    }

    /**
     * 删除所有缓存
     */
    public static synchronized <T> void removeAll() {
        LOADER_MAP.clear();
    }

    /**
     * 刷新缓存
     *
     * @param <T>
     * @param tClass 类
     * @return
     */
    public static synchronized <T> ExtensionLoader<T> refresh(Class<T> tClass) {
        if (null == tClass) {
            return null;
        }

        remove(tClass);
        return getExtensionLoader(tClass);
    }


}
