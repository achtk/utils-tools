package com.chua.utils.tools.classes.reflections;

import com.chua.utils.tools.cache.CacheProvider;
import com.chua.utils.tools.cache.ConcurrentCacheProvider;
import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.classes.reflections.configuration.RewriteConfiguration;
import com.chua.utils.tools.collects.collections.CollectionHelper;
import com.google.common.base.Strings;
import org.reflections.scanners.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

/**
 * Reflections工具
 *
 * @author CH
 */
public class ReflectionsHelper {

    private static final CacheProvider<Class, Set<Class>> SUB_CACHE = new ConcurrentCacheProvider();
    private static final CacheProvider<Class<? extends Annotation>, Set<Class<?>>> ANNOTATION_CACHE = new ConcurrentCacheProvider();
    private static final CacheProvider<String, Set<String>> PACKAGE_CACHE = new ConcurrentCacheProvider();

    /**
     * 获取子类
     *
     * @param <T>
     * @param tClass 类
     * @return
     */
    public static <T> Set<? extends Class> getSubTypesOf(Class tClass) {
        if (null == tClass) {
            return Collections.emptySet();
        }
        if (SUB_CACHE.containsKey(tClass)) {
            return SUB_CACHE.get(tClass);
        }
        RewriteConfiguration configurationBuilder = newConfigurationBuilder(new SubTypesScanner());
        RewriteReflections reflections = new RewriteReflections(configurationBuilder);
        Set<Class> typesOf = reflections.getSubTypesOf(tClass);
        SUB_CACHE.put(tClass, typesOf);
        return typesOf;
    }

    /**
     * 获取注解注释的类
     *
     * @param annotation 注解类
     * @return
     */
    public static Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        if (null == annotation) {
            return Collections.emptySet();
        }
        if (ANNOTATION_CACHE.containsKey(annotation)) {
            return ANNOTATION_CACHE.get(annotation);
        }
        RewriteConfiguration configurationBuilder = newConfigurationBuilder(new TypeAnnotationsScanner());
        RewriteReflections reflections = new RewriteReflections(configurationBuilder);
        Set<Class<?>> annotatedWith = reflections.getTypesAnnotatedWith(annotation);
        ANNOTATION_CACHE.put(annotation, annotatedWith);
        return annotatedWith;
    }

    /**
     * 获取注解注释的字段
     *
     * @param annotation 注解类
     * @return
     */
    public static Set<Field> getFieldsAnnotatedWith(Class<? extends Annotation> annotation) {
        RewriteConfiguration configurationBuilder = newConfigurationBuilder(new FieldAnnotationsScanner());
        RewriteReflections reflections = new RewriteReflections(configurationBuilder);
        return reflections.getFieldsAnnotatedWith(annotation);
    }

    /**
     * 获取注解注释的方法
     *
     * @param annotation 注解类
     * @return
     */
    public static Set<Method> getMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
        RewriteConfiguration configurationBuilder = newConfigurationBuilder(new MethodAnnotationsScanner());
        RewriteReflections reflections = new RewriteReflections(configurationBuilder);
        return reflections.getMethodsAnnotatedWith(annotation);
    }

    /**
     * 获取注解注释的方法
     *
     * @param annotation 注解类
     * @return
     */
    public static Set<Method> getMethodsWithAnyParamAnnotated(Class<? extends Annotation> annotation) {
        RewriteConfiguration configurationBuilder = newConfigurationBuilder(new MethodParameterScanner());
        RewriteReflections reflections = new RewriteReflections(configurationBuilder);
        return reflections.getMethodsWithAnyParamAnnotated(annotation);
    }

    /**
     * 获取包下的类
     *
     * @param packages 包
     * @return
     */
    public static Set<String> getAllTypes(final String packages) {
        if (Strings.isNullOrEmpty(packages)) {
            return Collections.emptySet();
        }
        if (PACKAGE_CACHE.containsKey(packages)) {
            return PACKAGE_CACHE.get(packages);
        }
        RewriteConfiguration configurationBuilder = newConfigurationBuilder(packages);
        RewriteReflections reflections = new RewriteReflections(configurationBuilder);
        Set<String> allTypes = reflections.getAllTypes();
        PACKAGE_CACHE.put(packages, allTypes);
        return allTypes;
    }

    /**
     * 创建ConfigurationBuilder
     *
     * @return
     */
    private static RewriteConfiguration newConfigurationBuilder(final Scanner... scanners) {
        RewriteConfiguration configurationBuilder = new RewriteConfiguration();
        configurationBuilder.addScanners(scanners);
        configurationBuilder.setExecutorService(ForkJoinPool.commonPool());
        configurationBuilder.addUrls(CollectionHelper.toList(ClassHelper.getUrlsByClassLoader(ClassHelper.getDefaultClassLoader())));
        configurationBuilder.setClassLoaders(new ClassLoader[]{ClassHelper.getDefaultClassLoader()});
        return configurationBuilder;
    }

    /**
     * 创建ConfigurationBuilder
     *
     * @return
     */
    private static RewriteConfiguration newConfigurationBuilder(final ClassLoader classLoader, final Scanner... scanners) {
        RewriteConfiguration configurationBuilder = new RewriteConfiguration();
        configurationBuilder.addScanners(scanners);
        configurationBuilder.setExecutorService(ForkJoinPool.commonPool());
        configurationBuilder.addUrls(CollectionHelper.toList(ClassHelper.getUrlsByClassLoader(classLoader)));
        configurationBuilder.setClassLoaders(new ClassLoader[]{classLoader});
        return configurationBuilder;
    }

    /**
     * 创建ConfigurationBuilder
     *
     * @return
     */
    private static RewriteConfiguration newConfigurationBuilder(final String packages, final Scanner... scanners) {
        RewriteConfiguration configurationBuilder = new RewriteConfiguration();
        configurationBuilder.addScanners(scanners);
        configurationBuilder.setExecutorService(ForkJoinPool.commonPool());
        configurationBuilder.forPackages(packages);
        configurationBuilder.setClassLoaders(new ClassLoader[]{ClassHelper.getDefaultClassLoader()});
        return configurationBuilder;
    }
}
