package com.chua.utils.tools.resource;

import com.chua.utils.tools.common.BooleanHelper;
import com.google.common.collect.HashMultimap;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * 资源对象工具类
 *
 * @author CH
 */
public class ResourceUtil {
    /**
     * 根据注解获取资源信息
     *
     * @param annotation 注解
     * @param sources    资源信息
     * @return
     */
    public static Set<Resource> findResourceByAnnotation(Class<? extends Annotation> annotation, Set<Resource> sources) {
        Set<Resource> resourceSet = new HashSet<>();
        sources.parallelStream().forEach(new Consumer<Resource>() {
            @Override
            public void accept(Resource resource) {
                Annotation[] annotations = resource.getAnnotations();
                if (!BooleanHelper.hasLength(annotations)) {
                    return;
                }
                if (!isAnnotation(annotations, annotation)) {
                    return;
                }
                resourceSet.add(resource);
            }
        });
        return resourceSet;
    }

    /**
     * 是否包含注解
     *
     * @param annotations 注解集合
     * @param annotation  注解
     * @return
     */
    private static boolean isAnnotation(Annotation[] annotations, Class<? extends Annotation> annotation) {
        for (Annotation annotation1 : annotations) {
            if (!annotation.isAnnotationPresent(annotation1.annotationType())) {
                continue;
            }
            return true;
        }
        return false;
    }

    /**
     * 获取注解信息
     *
     * @param resources
     * @return
     */
    public static HashMultimap<Class, Class> findResourceForClass(Set<Resource> resources) {
        if (!BooleanHelper.hasLength(resources)) {
            return null;
        }
        HashMultimap<Class, Class> result = HashMultimap.create();
        resources.parallelStream().forEach(new Consumer<Resource>() {
            @Override
            public void accept(Resource resource) {
                Annotation[] annotations = resource.getAnnotations();
                Class<?> classes = resource.getClasses();
                if (BooleanHelper.hasLength(annotations)) {
                    for (Annotation annotation : annotations) {
                        result.put(annotation.annotationType(), classes);
                    }
                }
                Class<?> superclass = classes.getSuperclass();
                while (null != superclass && !superclass.isAssignableFrom(Object.class)) {
                    result.put(superclass, classes);
                    superclass = superclass.getSuperclass();
                }

            }
        });
        return result;
    }

    /**
     * 获取注解信息
     *
     * @param resources
     * @return
     */
    public static HashMultimap<String, Class> findResourceForAnnotation(Set<Resource> resources) {
        if (!BooleanHelper.hasLength(resources)) {
            return null;
        }
        HashMultimap<String, Class> result = HashMultimap.create();
        resources.parallelStream().forEach(new Consumer<Resource>() {
            @Override
            public void accept(Resource resource) {
                Annotation[] annotations = resource.getAnnotations();
                if (!BooleanHelper.hasLength(annotations)) {
                    return;
                }
                for (Annotation annotation : annotations) {
                    result.put(annotation.annotationType().getName(), resource.getClasses());
                }
            }
        });
        return result;
    }
}
