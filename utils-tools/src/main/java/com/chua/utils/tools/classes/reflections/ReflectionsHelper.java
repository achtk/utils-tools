package com.chua.utils.tools.classes.reflections;

import com.chua.utils.tools.classes.ClassExtensionHelper;
import com.chua.utils.tools.common.ArraysHelper;
import com.chua.utils.tools.common.CollectionHelper;
import org.reflections.Reflections;
import org.reflections.scanners.*;
import org.reflections.util.ConfigurationBuilder;
import sun.misc.ClassLoaderUtil;
import sun.misc.SharedSecrets;
import sun.misc.URLClassPath;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

/**
 * Reflections工具
 * @author CH
 */
public class ReflectionsHelper extends ClassExtensionHelper {


    /**
     * 获取子类
     * @param <T>
     * @param tClass 类
     * @return
     */
    public static <T> Set<Class<? extends T>> getSubTypesOf(Class<T> tClass) {
        ConfigurationBuilder configurationBuilder = newConfigurationBuilder(new SubTypesScanner());
        Reflections reflections = new Reflections(configurationBuilder);
        return reflections.getSubTypesOf(tClass);
    }
    /**
     * 获取注解注释的类
     * @param annotation 注解类
     * @return
     */
    public static Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        ConfigurationBuilder configurationBuilder = newConfigurationBuilder(new TypeAnnotationsScanner());
        Reflections reflections = new Reflections(configurationBuilder);
        return reflections.getTypesAnnotatedWith(annotation);
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
        ConfigurationBuilder configurationBuilder = newConfigurationBuilder(packages);
        Reflections reflections = new Reflections(configurationBuilder);
        return reflections.getAllTypes();
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
