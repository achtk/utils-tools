package com.chua.utils.tools.spi.processor;//package com.cgy.utils.base.spi.processor;
//
//import com.cgy.utils.base.arrays.ArraysHelper;
//import com.cgy.utils.base.classes.ClassHelper;
//import com.cgy.utils.base.map.MapHelper;
//import com.cgy.utils.base.common.SkipPatterns;
//import com.cgy.utils.base.spi.entity.ExtensionClass;
//import com.cgy.utils.base.spi.entity.SpiConfig;
//import com.cgy.utils.base.thread.ThreadHelper;
//import com.google.common.collect.Multimap;
//import io.github.classgraph.ClassGraph;
//import io.github.classgraph.ClassInfo;
//import io.github.classgraph.ClassInfoList;
//import io.github.classgraph.ScanResult;
//import lombok.extern.slf4j.Slf4j;
//
//import java.lang.annotation.Annotation;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Future;
//import java.util.function.Consumer;
//
///**
// * 通过资源扫描工具实现扩展
// * @author CH
// * @version 1.0.0
// * @since 2020/6/3 18:04
// *
// * @see com.cgy.utils.base.spi.processor.IExtensionProcessor
// * @see com.cgy.utils.base.resource.ResourceHelper
// */
//@Slf4j
//public class ScanProcessor<T> implements IExtensionProcessor<T> {
//
//    private final Multimap<String, ExtensionClass<T>> providerCache = MapHelper.newHashMultimap();
//    /**
//     * 全部的加载的实现类 {"alias":ExtensionClass}
//     */
//    private Class<? extends Annotation> extension;
//    private Class<T> service;
//
//    @Override
//    public void init(SpiConfig spiConfig) {
//        if(null == spiConfig) {
//            spiConfig = new SpiConfig();
//        }
//
//        this.extension = spiConfig.getExtension();
//    }
//
//    @Override
//    public Multimap<String, ExtensionClass<T>> analyze(Class<T> service, ClassLoader classLoader) {
//        if(null == service) {
//            return null;
//        }
//
//        this.service = service;
//        classLoader = null == classLoader ? ClassHelper.getDefaultClassLoader() : classLoader;
//
//        ClassGraph classGraph = new ClassGraph();
//        classGraph.enableClassInfo();
//        classGraph.addClassLoader(ClassHelper.getDefaultClassLoader());
//        classGraph.blacklistJars(ArraysHelper.toArray(SkipPatterns.JDK_LIB));
//
//        ExecutorService executorService = ThreadHelper.newProcessorThreadExecutor();
//        Future<ScanResult> scanResultFuture = classGraph.scanAsync(executorService, ThreadHelper.processor());
//
//        try (io.github.classgraph.ScanResult scanResult = scanResultFuture.get()) {
//            ClassInfoList implementing = null;
//            ClassLoader finalClassLoader = classLoader;
//            try {
//                implementing = scanResult.getClassesImplementing(service.getName());
//                analyzeClass(implementing, finalClassLoader);
//            } catch (Exception e) {
//            }
//            ClassInfoList subclasses = null;
//            try {
//                subclasses = scanResult.getSubclasses(service.getName());
//                analyzeClass(subclasses, finalClassLoader);
//            } catch (Exception e) {
//            }
//        } catch (Throwable e) {
//        } finally {
//            executorService.shutdown();
//            executorService.shutdownNow();
//        }
//        return providerCache;
//    }
//
//    @Override
//    public void refresh() {
//        providerCache.clear();
//    }
//
//    /**
//     * 遍历数据
//     * @param allResources
//     * @param finalClassLoader
//     */
//    private void analyzeClass(ClassInfoList allResources, ClassLoader finalClassLoader) {
//        allResources.parallelStream().forEach(new Consumer<ClassInfo>() {
//            @Override
//            public void accept(ClassInfo classInfo) {
//                String name = classInfo.getName();
//                Class<?> forName = ClassHelper.forName(name, finalClassLoader);
//                ExtensionClass<T> extensionClass = loadExtension(service, extension, null, forName, null);
//                if(null != extensionClass && extensionClass.isSingle()) {
//                    extensionClass.setObj(ClassHelper.forObject(extensionClass.getClass().getName(), finalClassLoader));
//                }
//                if(null != extensionClass) {
//                    providerCache.put(extensionClass.getName(), extensionClass);
//                }
//            }
//        });
//    }
//}
