package com.chua.utils.tools.classes.reflections;

import com.chua.utils.tools.cache.ConcurrentCacheProvider;
import com.chua.utils.tools.cache.CacheProvider;
import com.chua.utils.tools.classes.ClassExtensionHelper;
import com.chua.utils.tools.collects.collections.CollectionHelper;
import com.chua.utils.tools.common.*;
import com.chua.utils.tools.common.skip.SkipPatterns;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.reflections.scanners.*;
import org.reflections.scanners.Scanner;
import org.reflections.util.ConfigurationBuilder;
import sun.misc.SharedSecrets;
import sun.misc.URLClassPath;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.function.IntFunction;

/**
 * Reflections工具
 * @author CH
 */
public class ReflectionsHelper extends ClassExtensionHelper {


    private static final CacheProvider<Class, Set<Class>> SUB_CACHE = new ConcurrentCacheProvider();
    private static final CacheProvider<Class<? extends Annotation>, Set<Class<?>>> ANNOTATION_CACHE = new ConcurrentCacheProvider();
    private static final CacheProvider<String, Set<String>> PACKAGE_CACHE = new ConcurrentCacheProvider();
    /**
     * 获取子类
     * @param <T>
     * @param tClass 类
     * @return
     */
    public static <T> Set<? extends Class> getSubTypesOf(Class tClass) {
        if(null == tClass) {
            return Collections.emptySet();
        }
        if(SUB_CACHE.containsKey(tClass)) {
            return SUB_CACHE.get(tClass);
        }
        ConfigurationBuilder configurationBuilder = newConfigurationBuilder(new SubTypesScanner());
        ReflectionsFactory reflections = new ReflectionsFactory(configurationBuilder);
        Set<Class> typesOf = reflections.getSubTypesOf(tClass);
        SUB_CACHE.put(tClass, typesOf);
        return typesOf;
    }
    /**
     * 获取注解注释的类
     * @param annotation 注解类
     * @return
     */
    public static Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        if(null == annotation) {
            return Collections.emptySet();
        }
        if(ANNOTATION_CACHE.containsKey(annotation)) {
            return ANNOTATION_CACHE.get(annotation);
        }
        ConfigurationBuilder configurationBuilder = newConfigurationBuilder(new TypeAnnotationsScanner());
        ReflectionsFactory reflections = new ReflectionsFactory(configurationBuilder);
        Set<Class<?>> annotatedWith = reflections.getTypesAnnotatedWith(annotation);
        ANNOTATION_CACHE.put(annotation, annotatedWith);
        return annotatedWith;
    }
    /**
     * 获取注解注释的字段
     * @param annotation 注解类
     * @return
     */
    public static Set<Field> getFieldsAnnotatedWith(Class<? extends Annotation> annotation) {
        ConfigurationBuilder configurationBuilder = newConfigurationBuilder(new FieldAnnotationsScanner());
        ReflectionsFactory reflections = new ReflectionsFactory(configurationBuilder);
        return reflections.getFieldsAnnotatedWith(annotation);
    }
    /**
     * 获取注解注释的方法
     * @param annotation 注解类
     * @return
     */
    public static Set<Method> getMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
        ConfigurationBuilder configurationBuilder = newConfigurationBuilder(new MethodAnnotationsScanner());
        ReflectionsFactory reflections = new ReflectionsFactory(configurationBuilder);
        return reflections.getMethodsAnnotatedWith(annotation);
    }
    /**
     * 获取注解注释的方法
     * @param annotation 注解类
     * @return
     */
    public static Set<Method> getMethodsWithAnyParamAnnotated(Class<? extends Annotation> annotation) {
        ConfigurationBuilder configurationBuilder = newConfigurationBuilder(new MethodParameterScanner());
        ReflectionsFactory reflections = new ReflectionsFactory(configurationBuilder);
        return reflections.getMethodsWithAnyParamAnnotated(annotation);
    }
    /**
     * 获取包下的类
     * @param packages 包
     * @return
     */
    public static Set<String> getAllTypes(final String packages) {
        if(Strings.isNullOrEmpty(packages)) {
            return Collections.emptySet();
        }
        if(PACKAGE_CACHE.containsKey(packages)) {
            return PACKAGE_CACHE.get(packages);
        }
        ConfigurationBuilder configurationBuilder = newConfigurationBuilder(packages);
        ReflectionsFactory reflections = new ReflectionsFactory(configurationBuilder);
        Set<String> allTypes = reflections.getAllTypes();
        PACKAGE_CACHE.put(packages, allTypes);
        return allTypes;
    }

    /**
     * 获取类加载器下的URL
     * @param classLoader 类加载器
     * @return
     */
    public static URL[] getUrlsByClassLoader(ClassLoader classLoader) {
        if(null == classLoader || !(classLoader instanceof URLClassLoader)) {
            return ArraysHelper.emptyArray(URL.class);
        }
        URLClassPath urlClassPath = SharedSecrets.getJavaNetAccess().getURLClassPath((URLClassLoader) classLoader);
        URL[] urls = urlClassPath.getURLs();
        if(BooleanHelper.hasLength(urls)) {
            return urls;
        }
        return Arrays.stream(System.getProperty("java.class.path").split(";")).map(new Function<String, URL>() {
            @Override
            public @Nullable URL apply(@Nullable String input) {
                try {
                    return new URL(input);
                } catch (MalformedURLException e) {
                    return null;
                }
            }
        }).filter(new Predicate<URL>() {
            @Override
            public boolean apply(@Nullable URL input) {
                return null != input;
            }
        }).toArray(new IntFunction<URL[]>() {
            @Override
            public URL[] apply(int value) {
                return new URL[0];
            }
        });
    }
    /**
     * 获取类加载器下的URL非JDK
     * @param classLoader 类加载器
     * @return
     */
    public static URL[] getUrlsByClassLoaderExcludeJdk(ClassLoader classLoader) {
        URL[] urlsByClassLoader = getUrlsByClassLoader(classLoader);
        List<URL> cache = new ArrayList<>(urlsByClassLoader.length);
        Arrays.stream(urlsByClassLoader).parallel().forEach(new Consumer<URL>() {
            @Override
            public void accept(URL url) {
                String form = url.toExternalForm();
                for (String item : SkipPatterns.DEFAULT) {
                    if(StringHelper.wildcardMatch(FileHelper.getName(form), item)) {
                        return;
                    }
                }
                for (String item : SkipPatterns.JDK_LIB) {
                    if(StringHelper.wildcardMatch(FileHelper.getName(form), item)) {
                        return;
                    }
                }
                cache.add(url);
            }
        });
        return cache.toArray(new URL[0]);
    }
    /**
     * 创建ConfigurationBuilder
     * @return
     */
    private static ConfigurationBuilder newConfigurationBuilder(final Scanner... scanners) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.addScanners(scanners);
        configurationBuilder.setExecutorService(ForkJoinPool.commonPool());
        configurationBuilder.addUrls(CollectionHelper.toList(getUrlsByClassLoader(getDefaultClassLoader())));
        configurationBuilder.setClassLoaders(new ClassLoader[]{getDefaultClassLoader()});
        return configurationBuilder;
    }
    /**
     * 创建ConfigurationBuilder
     * @return
     */
    private static ConfigurationBuilder newConfigurationBuilder(final ClassLoader classLoader, final Scanner... scanners) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.addScanners(scanners);
        configurationBuilder.setExecutorService(ForkJoinPool.commonPool());
        configurationBuilder.addUrls(CollectionHelper.toList(getUrlsByClassLoader(classLoader)));
        configurationBuilder.setClassLoaders(new ClassLoader[]{classLoader});
        return configurationBuilder;
    }
    /**
     * 创建ConfigurationBuilder
     * @return
     */
    private static ConfigurationBuilder newConfigurationBuilder(final String packages, final Scanner... scanners) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.addScanners(scanners);
        configurationBuilder.setExecutorService(ForkJoinPool.commonPool());
        configurationBuilder.forPackages(packages);
        configurationBuilder.setClassLoaders(new ClassLoader[]{getDefaultClassLoader()});
        return configurationBuilder;
    }
}
