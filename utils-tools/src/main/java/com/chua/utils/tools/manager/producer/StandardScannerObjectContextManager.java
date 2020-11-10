package com.chua.utils.tools.manager.producer;

import com.chua.utils.tools.cache.CacheProvider;
import com.chua.utils.tools.cache.ConcurrentCacheProvider;
import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.reflections.ReflectionsFactory;
import com.chua.utils.tools.common.FileHelper;
import com.chua.utils.tools.common.StringHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.function.Filter;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.manager.ObjectContextManager;
import com.chua.utils.tools.spi.factory.ExtensionFactory;
import com.chua.utils.tools.spi.processor.ExtensionProcessor;
import com.chua.utils.tools.strategy.helper.StrategyHelper;
import org.reflections.scanners.*;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.vfs.Vfs;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 标准的对象管理器
 *
 * @author CH
 * @version 1.0.0
 * @since 2020/11/9
 */
public class StandardScannerObjectContextManager implements Runnable, ObjectContextManager {

    private ExecutorService executorService = ThreadHelper.newSingleThreadExecutor("object-context-manager");
    private ReflectionsFactory reflectionsFactory;
    private final static CacheProvider SUB_CACHE = new ConcurrentCacheProvider<>();
    private final static CacheProvider<Class<? extends Annotation>, Set<Class<?>>> ANNOTATION_TYPE_CACHE = new ConcurrentCacheProvider<>();
    private final static CacheProvider<Class<? extends Annotation>, Set<Method>> ANNOTATION_METHOD_CACHE = new ConcurrentCacheProvider<>();
    private final static CacheProvider<Class<? extends Annotation>, Set<Field>> ANNOTATION_FIELD_CACHE = new ConcurrentCacheProvider<>();

    private static final String[] EXCLUDE_URLS = new String[]{
            "netty-*", "vertx-*", "jackson-*", "lucene-*", "*-guava-*", "kotlin-*",
            "mockio-*", "woodstox-*", "*-guava"
    };

    private Future<?> future;

    public StandardScannerObjectContextManager() {
        this.future = executorService.submit(this);
        try {
            this.loadingFinished();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                new MethodAnnotationsScanner(),
                new TypeAnnotationsScanner());
        configurationBuilder.setExecutorService(ThreadHelper.newProcessorThreadExecutor());
        configurationBuilder.addUrls(lessUrl(ClassHelper.getUrlsByClassLoaderExcludeJdk(ClassHelper.getDefaultClassLoader())));
        configurationBuilder.setClassLoaders(new ClassLoader[]{ClassHelper.getDefaultClassLoader()});
        this.reflectionsFactory = new ReflectionsFactory(configurationBuilder);
    }

    /**
     * 减少无用包查询
     *
     * @param urlsByClassLoaderExcludeJdk 非JDK URL
     * @return 减少的URL集合
     */
    private List<URL> lessUrl(URL[] urlsByClassLoaderExcludeJdk) {
        List<URL> result = new ArrayList<>(urlsByClassLoaderExcludeJdk.length);
        for (URL url : urlsByClassLoaderExcludeJdk) {
            if (!isSkip(url)) {
                result.add(url);
            }
        }
        return result;
    }

    /**
     * 是否跳过无效包
     *
     * @param url url
     * @return 是无效包返回true
     */
    private boolean isSkip(URL url) {
        for (String exclude : EXCLUDE_URLS) {
            if (!StringHelper.wildcardMatch(FileHelper.getName(url.getPath()), exclude)) {
                continue;
            }
            return true;
        }
        return false;
    }
}
