package com.chua.utils.tools.classes.reflections;

import com.chua.utils.tools.cache.ConcurrentCacheProvider;
import com.chua.utils.tools.cache.ICacheProvider;
import com.chua.utils.tools.classes.ClassExtensionHelper;
import com.chua.utils.tools.common.ArraysHelper;
import com.chua.utils.tools.common.CollectionHelper;
import com.google.common.base.Strings;
import org.reflections.Reflections;
import org.reflections.scanners.*;
import org.reflections.util.ConfigurationBuilder;
import sun.misc.SharedSecrets;
import sun.misc.URLClassPath;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

/**
 * Reflections工具
 * @author CH
 */
public class ReflectionsHelper extends ClassExtensionHelper {


    private static final ICacheProvider<Class, Set<Class>> SUB_CACHE = new ConcurrentCacheProvider();
    private static final ICacheProvider<Class<? extends Annotation>, Set<Class<?>>> ANNOTATION_CACHE = new ConcurrentCacheProvider();
    private static final ICacheProvider<String, Set<String>> PACKAGE_CACHE = new ConcurrentCacheProvider();
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
        if(SUB_CACHE.container(tClass)) {
            return SUB_CACHE.get(tClass);
        }
        ConfigurationBuilder configurationBuilder = newConfigurationBuilder(new SubTypesScanner());
        Reflections reflections = new Reflections(configurationBuilder);
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
        if(ANNOTATION_CACHE.container(annotation)) {
            return ANNOTATION_CACHE.get(annotation);
        }
        ConfigurationBuilder configurationBuilder = newConfigurationBuilder(new TypeAnnotationsScanner());
        Reflections reflections = new Reflections(configurationBuilder);
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
        Reflections reflections = new Reflections(configurationBuilder);
        return reflections.getFieldsAnnotatedWith(annotation);
    }
    /**
     * 获取注解注释的方法
     * @param annotation 注解类
     * @return
     */
    public static Set<Method> getMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
        ConfigurationBuilder configurationBuilder = newConfigurationBuilder(new MethodAnnotationsScanner());
        Reflections reflections = new Reflections(configurationBuilder);
        return reflections.getMethodsAnnotatedWith(annotation);
    }
    /**
     * 获取注解注释的方法
     * @param annotation 注解类
     * @return
     */
    public static Set<Method> getMethodsWithAnyParamAnnotated(Class<? extends Annotation> annotation) {
        ConfigurationBuilder configurationBuilder = newConfigurationBuilder(new MethodParameterScanner());
        Reflections reflections = new Reflections(configurationBuilder);
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
        if(PACKAGE_CACHE.container(packages)) {
            return PACKAGE_CACHE.get(packages);
        }
        ConfigurationBuilder configurationBuilder = newConfigurationBuilder(packages);
        Reflections reflections = new Reflections(configurationBuilder);
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
        return urlClassPath.getURLs();
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
