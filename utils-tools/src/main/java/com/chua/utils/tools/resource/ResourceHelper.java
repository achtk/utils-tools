package com.chua.utils.tools.resource;

import com.chua.utils.tools.classes.ClassHelper;
import com.chua.utils.tools.common.BooleanHelper;
import com.chua.utils.tools.common.ThreadHelper;
import com.chua.utils.tools.common.UrlHelper;
import com.chua.utils.tools.common.skip.SkipPatterns;
import com.chua.utils.tools.function.Matcher;
import com.chua.utils.tools.resource.entity.Resource;
import com.chua.utils.tools.resource.factory.FastResourceFactory;
import com.chua.utils.tools.resource.factory.ResourceFactory;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * 快速资源查找器
 *
 * @author CH
 * @since 1.0
 */
@Slf4j
public class ResourceHelper extends UrlHelper {

    private static final ResourceFactory FAST_RESOURCE_FACTORY = new FastResourceFactory();

    /**
     * 检索资源
     * <p>检索类加载器下的资源，支持通配符</p>
     *
     * @param names    检索的资源
     * @param excludes 除外的资源(支持通配符)
     * @return 资源
     */
    public static Set<Resource> getResources(String names, final String... excludes) {
        return getResources(names, null, resource -> {
        }, excludes);
    }

    /**
     * 检索资源
     * <p>检索类加载器下的资源，支持通配符</p>
     *
     * @param names 检索的资源
     * @return 资源
     */
    public static Set<Resource> getResources(List<String> names) {
        if (!BooleanHelper.hasLength(names)) {
            return Collections.emptySet();
        }

        Set<Resource> result = new HashSet<>();

        int size = names.size();
        ExecutorService executorService = ThreadHelper.newProcessorThreadExecutor();
        CountDownLatch countDownLatch = new CountDownLatch(size);

        for (String name : names) {
            executorService.execute(() -> {
                Set<Resource> resources = getResources(name);
                try {
                    if (null == resources) {
                        return;
                    }
                    result.addAll(resources);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();

        return result;
    }

    /**
     * 检索资源
     * <p>检索类加载器下的资源，支持通配符</p>
     *
     * @param name 检索的资源
     * @return 资源
     */
    public static Set<Resource> getResources(String name) {
        return getResources(name, null, resource -> {
        }, SkipPatterns.JDK_LIB.toArray(new String[0]));
    }

    /**
     * 检索资源
     * <p>检索类加载器下的资源，支持通配符</p>
     *
     * @param name    检索的资源
     * @param matcher 匹配器
     * @return 资源
     */
    public static Set<Resource> getResources(String name, Matcher<Resource> matcher) {
        return getResources(name, null, matcher, SkipPatterns.JDK_LIB.toArray(new String[0]));
    }

    /**
     * 检索资源
     * <p>检索类加载器下的资源，支持通配符</p>
     *
     * @param name 检索的资源
     * @return 资源
     */
    public static Set<Resource> getResources(String name, ClassLoader classLoader) {
        return getResources(name, classLoader, resource -> {
        }, SkipPatterns.JDK_LIB.toArray(new String[0]));
    }

    /**
     * 检索资源
     * <p>检索类加载器下的资源，支持通配符</p>
     *
     * @param name        检索的资源
     * @param classLoader 检索的类加载器
     * @param matcher     匹配器
     * @param excludes    除外的资源(支持通配符)
     * @return 资源
     */
    public static Set<Resource> getResources(String name, ClassLoader classLoader, Matcher<Resource> matcher, final String... excludes) {
        if (Strings.isNullOrEmpty(name)) {
            return null;
        }
        synchronized (ResourceHelper.FAST_RESOURCE_FACTORY) {
            if (null != classLoader) {
                FAST_RESOURCE_FACTORY.classLoader(classLoader);
            }
            FAST_RESOURCE_FACTORY.matcher(matcher);
            return FAST_RESOURCE_FACTORY.getResources(name.trim(), excludes);
        }
    }

    /**
     * 获取类加载器下所有注解
     *
     * @param annotation  注解
     * @param classLoader 类加载器
     * @return 资源
     */
    public static Set<Resource> getResourcesByAnnotation(Class<? extends Annotation> annotation, ClassLoader classLoader) {
        Vector<Class<?>> classes = ClassHelper.getOnlyFieldValue(classLoader, "classes", Vector.class);
        if (BooleanHelper.hasLength(classes)) {
            return Collections.emptySet();
        }
        Set<Resource> resourceSet = new HashSet<>();
        classes.parallelStream().forEach(aClass -> {
            if (!aClass.isAnnotationPresent(annotation)) {
                return;
            }
            Resource resource = new Resource();
            resourceSet.add(resource);
        });
        return resourceSet;
    }

    /**
     * 获取类加载器下所有注解
     *
     * @param classLoader 类加载器
     * @return 资源
     */
    public static Set<Resource> getResources(ClassLoader classLoader) {
        ConcurrentHashMap<String, Object> classes = ClassHelper.getOnlyFieldValue(classLoader, "parallelLockMap", ConcurrentHashMap.class);
        Set<Resource> resourceSet = new HashSet<>();

        for (Map.Entry<String, Object> entry : classes.entrySet()) {
            Class<?> aClass = ClassHelper.forName(entry.getKey());
            if (null == aClass) {
                continue;
            }
            Resource resource = new Resource();
            resourceSet.add(resource);
        }
        return resourceSet;
    }
}
