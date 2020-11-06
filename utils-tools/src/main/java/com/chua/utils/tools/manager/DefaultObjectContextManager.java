package com.chua.utils.tools.manager;

import com.chua.utils.tools.cache.CacheProvider;
import com.chua.utils.tools.cache.ConcurrentCacheProvider;
import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.reflections.ReflectionsFactory;
import com.chua.utils.tools.collects.collections.CollectionHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.function.Filter;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import com.chua.utils.tools.spi.processor.ExtensionProcessor;
import com.chua.utils.tools.strategy.helper.StrategyHelper;
import org.reflections.scanners.*;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 对象管理器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/10/27
 */
public class DefaultObjectContextManager implements Runnable, ObjectContextManager {

    private ExecutorService executorService = ThreadHelper.newSingleThreadExecutor("object-context-manager");
    private ReflectionsFactory reflectionsFactory;
    private final static CacheProvider SUB_CACHE = new ConcurrentCacheProvider<>();
    private final static CacheProvider<Class<? extends Annotation>, Set<Class<?>>> ANNOTATION_TYPE_CACHE = new ConcurrentCacheProvider<>();
    private final static CacheProvider<Class<? extends Annotation>, Set<Method>> ANNOTATION_METHOD_CACHE = new ConcurrentCacheProvider<>();
    private final static CacheProvider<Class<? extends Annotation>, Set<Field>> ANNOTATION_FIELD_CACHE = new ConcurrentCacheProvider<>();

    private final Future<?> future;

    {
        this.future = executorService.submit(this);
    }

    @Override
    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> tClass) {
        return StrategyHelper.doWithCache(((Class) tClass), (Supplier<Set>) () -> {
            return reflectionsFactory.getSubTypesOf(tClass);
        }, SUB_CACHE);
    }

    @Override
    public Set<String> getResources(Filter<String> filter) {
        return reflectionsFactory.getResources(s -> filter.matcher(s));
    }

    @Override
    public Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> annotation) {
        return StrategyHelper.doWithCache(annotation, () -> {
            return reflectionsFactory.getTypesAnnotatedWith(annotation);
        }, ANNOTATION_TYPE_CACHE);
    }

    @Override
    public ReflectionsFactory getReflectionsFactory() {
        return reflectionsFactory;
    }

    @Override
    public Set<Method> getMethodsAnnotatedWith(final Class<? extends Annotation> annotation) {
        return StrategyHelper.doWithCache(annotation, () -> {
            return reflectionsFactory.getMethodsAnnotatedWith(annotation);
        }, ANNOTATION_METHOD_CACHE);
    }
    @Override
    public Set<Field> getFieldsAnnotatedWith(final Class<? extends Annotation> annotation) {
        return StrategyHelper.doWithCache(annotation, () -> {
            return reflectionsFactory.getFieldsAnnotatedWith(annotation);
        }, ANNOTATION_FIELD_CACHE);
    }

    @Override
    public void doWithMatcher(Matcher<String> matcher) {
        reflectionsFactory.getAllTypes().parallelStream().forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                try {
                    matcher.doWith(s);
                } catch (Throwable throwable) {
                    return;
                }
            }
        });
    }

    @Override
    public void loadingFinished() throws ExecutionException, InterruptedException {
        future.get();
        executorService.shutdownNow();
    }

    @Override
    public <T> T getSpiSubOfType(String spiName, Class<T> tClass, ExtensionProcessor extensionProcessor) {
        return ExtensionFactory.getExtensionLoader(tClass, extensionProcessor).getExtension(spiName);
    }

    @Override
    public void run() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.addScanners(new SubTypesScanner(),
                new FieldAnnotationsScanner(),
                new MethodAnnotationsScanner(),
                new ResourcesScanner(),
                new TypeAnnotationsScanner(),
                new MethodParameterNamesScanner(), new MethodParameterScanner());
        configurationBuilder.setExecutorService(ForkJoinPool.commonPool());
        configurationBuilder.addUrls(CollectionHelper.toList(ClassHelper.getUrlsByClassLoaderExcludeJdk(ClassHelper.getDefaultClassLoader())));
        configurationBuilder.setClassLoaders(new ClassLoader[]{ClassHelper.getDefaultClassLoader()});
        this.reflectionsFactory = new ReflectionsFactory(configurationBuilder);
    }
}
